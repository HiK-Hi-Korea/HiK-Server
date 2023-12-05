package HiK.HiKServer.Translator.service;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.LearningContents.repository.LearningContentRepository;
import HiK.HiKServer.Translator.domain.Sentence;
import HiK.HiKServer.Translator.dto.TranslationForm;
import HiK.HiKServer.Translator.repositroy.SentenceRepository;
import HiK.HiKServer.User.domain.User;
import HiK.HiKServer.User.repository.UserRepository;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableAsync
@PropertySource("classpath:apikey.properties")
@Slf4j
@Service
public class TranslationService {
    @Autowired
    private SentenceRepository sentenceRepository;
    @Autowired
    private LearningContentRepository learningContentRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3UploadService s3UploadService;

    @Value("${papago.client.secrete}")
    private String papago_clientSecret;
    @Value("${papago.client.id}")
    private String papago_clientID;

    @Value("${chatGPT.apikey}")
    private String chatGPT_apiKey;
    private final static String COMPLETION_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private final static String MODEL="gpt-4";


    @Transactional
    public Sentence translation(String userId, TranslationForm dto) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();
        int userAge = user.getAge();
        String srcSentence = dto.getSourceSentence();
        String place = dto.getPlace();
        String listener = dto.getListener();
        int intimacy = dto.getIntimacy();

        GptPrompt gptPrompt = new GptPrompt(userAge, srcSentence, place, listener, intimacy);

        // Chain of Responsibility 패턴 이용
        PromptHandler university_handler = new PromptHandler_Universty();
        PromptHandler school_handler = new PromptHandler_School();
        PromptHandler online_handler = new PromptHandler_Online();
        PromptHandler general_handler = new PromptHandler_General();
        university_handler.setSuccessor(school_handler);
        school_handler.setSuccessor(online_handler);
        online_handler.setSuccessor(general_handler);

        school_handler.handleRequest(gptPrompt, place);

        String system = gptPrompt.getSystem();
        String prompt = gptPrompt.getPrompt();

        String targetSentence = createTargetSentence(system, prompt);

        log.info("system: "+system, "\nprompt: "+prompt);
        log.info("target Sentece: "+targetSentence);
        if (targetSentence == null) {
            throw new IllegalStateException("chat gpt - generate sentence 실패!");
        }
        // TTS 해서 voiceFile 받아오기 (파일 경로 반환)
        String voiceFile = getTTS(targetSentence);
        Long temp_id = null;
        Sentence sentence = new Sentence(temp_id, user, srcSentence, place, listener, intimacy, targetSentence, voiceFile);
        // DB에 저장
        log.info("sentence test:"+sentence.getId() +" voiceFile: "+sentence.getVoiceFile()+" real: "+voiceFile);
        Sentence createdSentence = sentenceRepository.save(sentence);

        // 비슷한 시간대와 장소에서 생성된 sentence 찾고 LearningContent 업데이트 메소드 비동기 호출
        updateLearningContentAsync(createdSentence, user);
        log.info("학습 컨텐츠까지 마무리 하고 sentence 반환직전");
        return createdSentence;
    }

    @Async
    public void updateLearningContentAsync(Sentence sentence, User user){
        // 1. 같은 유저, 같은 장소, 같은 대상에서 생성된 learning Contents 가 있는지 확인.
        // 이때 Situation 클래스를 learningContent 의 속성에 두고, 이 learningContent가 가지고 있는 Situation을 비교해서 찾기

        // 2-1. learningContent를 발견했으면, 해당 learningContent의 마지막 문장이 생성된 시각이 현재 문장이 생성된 시간과 5분 이내로 차이가 나는지 확인하기
        // 3-1. 만약 5분 이내이면 해당 learningContent에 문장 넣기
        // 3-2. 5분을 벗어났으면, (1) 다른 Learning Content를 만들기 (2) 같은 learningContent에 넣지만, --- 와 같은 표시를 주기

        // 2-2. learningContent를 발견하지 못했으면, 새로운 learningContent를 생성하고 해당 learningContent의 Situation을 설정하기
        // 3. 생성한 learningContent에 방금 만든 문장 넣기

        String sentencePlace = sentence.getPlace();
        String sentenceListener = sentence.getListener();
        int sentenceIntimacy = sentence.getIntimacy();
        LocalDateTime sentenceTimestamp = sentence.getTimestamp();
        LearningContent learningContent = learningContentRepository.findSimilarContents(user.getId(), sentencePlace, sentenceListener, sentenceIntimacy, sentenceTimestamp, sentenceTimestamp.minusMinutes(5));
        // 비슷한 Sentence가 없으면 새 LearningContent 생성
        if (learningContent == null) {
            learningContent = new LearningContent();
            learningContent.setUser(user);
            learningContent.setTimestamp(sentenceTimestamp);
            learningContent.addSentence(sentence);
            learningContent.setPlace(sentencePlace);
            learningContent.setListener(sentenceListener);
            learningContent.setIntimacy(sentenceIntimacy);
            sentence.setLearning_content(learningContent);
            learningContentRepository.save(learningContent); // 새로운 learningContent니깐 db에 넣어주기
        } else {
            // 비슷한 Sentence가 있으면 해당 LearningContent에 추가
            learningContent.setTimestamp(sentenceTimestamp);
            learningContent.addSentence(sentence);
            sentence.setLearning_content(learningContent);
            learningContentRepository.save(learningContent); // update해주기 -> 이게 맞는지 확인해야함.
        }
    }

    public String getTTS(String text) throws IOException {
        String file_text =text;
        if (text.length() >= 10){
            file_text = file_text.substring(0,9);
        }
        String filename = file_text+".mp3";
        String url;
        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // Build the voice request, select the language code ("en-US") and the ssml voice gender
            VoiceSelectionParams voice =
                    VoiceSelectionParams.newBuilder()
                            .setLanguageCode("ko-KR")
                            .setSsmlGender(SsmlVoiceGender.FEMALE)
                            .build();

            // Select the type of audio file you want returned
            AudioConfig audioConfig =
                    AudioConfig.newBuilder().setAudioEncoding(AudioEncoding.MP3).build();

            // Perform the text-to-speech request on the text input with the selected voice parameters and
            // audio file type
            SynthesizeSpeechResponse response =
                    textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            // Get the audio contents from the response
            ByteString audioContents = response.getAudioContent();

            // Write the response to the output file.
            try (OutputStream out = new FileOutputStream(filename)) {
                out.write(audioContents.toByteArray());
                log.info("Audio content written to file \""+filename+"\"");
            }

            // Upload ByteString to S3 and get URL
            url = s3UploadService.uploadByteStringToS3(audioContents, filename);
        }
        return url;
    }

    public String createTargetSentence(String system, String prompt) {
        String result = null;
        try {
            result= getGPTAnswer(system, prompt,0, 1000)
                    .replaceAll("\n", "")
                    .replaceAll("\\.","")
                    .replaceAll("\\\\","")
                    .replaceAll("\"","");
        } catch (Exception e) {
            log.info("getGPTAnswer error(서버 에러)");
        }
        return result;
    }

    public String getGPTAnswer(String system, String prompt, float temperature, int maxTokens) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatGPT_apiKey);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", system));
        messages.add(new ChatMessage("user", prompt));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model",MODEL);
        requestBody.put("messages", messages);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(COMPLETION_ENDPOINT, requestEntity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        log.info("Chat GPT response body:\n"+responseBody.toString());

        List<Map<String, Object>> choicesList = (List<Map<String, Object>>)responseBody.get("choices");
        Map<String, Object> choiceMap = choicesList.get(0);
        Map<String, Object> content = (Map<String, Object>)choiceMap.get("message");
        String answer = (String) content.get("content");
        log.info("챗지피티 답변:"+answer);

        return answer;
    }
}
