package HiK.HiKServer.Translator.service;

import HiK.HiKServer.LearningContents.repository.LearningContentRepository;
import HiK.HiKServer.Translator.dto.TranslationForm;
import HiK.HiKServer.Translator.repositroy.SentenceRepository;
import HiK.HiKServer.User.repository.UserRepository;
import HiK.HiKServer.entity.LearningContent;
import HiK.HiKServer.entity.Sentence;
import HiK.HiKServer.entity.User;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + userId));

        String srcSentence = dto.getSourceSentence();
        String place = dto.getPlace();
        String listener = dto.getListener();
        int intimacy = dto.getIntimacy();

        GptPrompt gptPrompt = new GptPrompt(srcSentence, place, listener, intimacy);
        String system = gptPrompt.getSystem();
        String prompt = gptPrompt.getPrompt();

        String targetSentence = createTargetSentence(system, prompt);

        log.info("target Sentece: "+targetSentence);
        if (targetSentence == null) {
            throw new IllegalStateException("chat gpt - generate sentence 실패!");
        }
        // TTS 해서 voiceFile 받아오기 (파일 경로 반환)
        String voiceFile = getTTS(targetSentence);
        Long temp_id = null;
        Sentence sentence = new Sentence(temp_id, srcSentence, place, listener, intimacy, targetSentence, voiceFile);
        // DB에 저장
        Sentence createdSentence = sentenceRepository.save(sentence);

        // 비슷한 시간대와 장소에서 생성된 sentence 찾고 LearningContent 업데이트 메소드 비동기 호출
        updateLearningContentAsync(createdSentence, user);
        return createdSentence;
    }

    @Async
    public void updateLearningContentAsync(Sentence sentence, User user){
        List<Sentence> similarSentences = sentenceRepository.findSimilarSentences(user.getUserId(), sentence.getPlace(), sentence.getTimestamp());
        // 비슷한 Sentence가 없으면 새 LearningContent 생성
        if (similarSentences.isEmpty()) {
            LearningContent learningContent = new LearningContent();
            learningContent.setUser(user);
            learningContent.addSentence(sentence);
            learningContentRepository.save(learningContent);
        } else {
            // 비슷한 Sentence가 있으면 해당 LearningContent에 추가
            LearningContent learningContent = similarSentences.get(0).getLearningContent();
            learningContent.addSentence(sentence);
            learningContentRepository.save(learningContent);
        }
    }

    public String getTTS(String text) throws IOException {
        String filePath = text+".mp3";

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
            try (OutputStream out = new FileOutputStream(filePath)) {
                out.write(audioContents.toByteArray());
                log.info("Audio content written to file \""+filePath+"\"");
            }
        }
        return filePath;
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ChatMessage{
        private String role;
        private String content;
    }

}
