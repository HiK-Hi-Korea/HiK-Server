package HiK.HiKServer.service;

import HiK.HiKServer.dto.TranslationForm;
import HiK.HiKServer.entity.Sentence;
import HiK.HiKServer.repositroy.SentenceRepository;
import com.google.cloud.texttospeech.v1.*;
import com.google.protobuf.ByteString;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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

    @Value("${papago.client.secrete}")
    private String papago_clientSecret;
    @Value("${papago.client.id}")
    private String papago_clientID;

    @Value("${chatGPT.apikey}")
    private String chatGPT_apiKey;
    private final static String COMPLETION_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    private final static String ROLE_USER="user";
    private final static String MODEL="gpt-3.5-turbo";

    @Transactional
    public Sentence translation(TranslationForm dto) throws IOException {
        String srcSentence = dto.getSourceSentence();
        String place = dto.getPlace();
        String listener = dto.getListener();
        int intimacy = dto.getIntimacy();
        // papago 번역 api이용
        String secondSentence = getPapagoTransSentence(srcSentence);

        // ai모델로 translatedSentence, desSituation 넘겨서 targetSentence 받아오기
        String thirdSentence = makePrompt(secondSentence, place, listener, intimacy);
        String translatedSentence = createTargetSentence(thirdSentence);
        log.info("chat gpt로 변환된 문장 : "+translatedSentence);
        if (translatedSentence == null){
            throw new IllegalStateException("chat gpt - generate sentence 실패!");
        }
        // 최종 targetSentence 를 TTS 해서 voiceFile 받아오기 (파일 경로 반환)
        String voiceFile = getTTS(translatedSentence);
//        String voiceFile = "getTTS(translatedSentence)";
        Long temp_id = null;
        Sentence sentence = new Sentence(temp_id, srcSentence, place, listener, intimacy, translatedSentence, voiceFile);

        // DB에 저장
        Sentence saved = sentenceRepository.save(sentence);
        return saved;
    }
    public String getTTS(String text) throws IOException {
        String filePath = ""+text+".mp3";

        // Instantiates a client
        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
            // Set the text input to be synthesized
            SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

            // Build the voice request, select the language code ("en-US") and the ssml voice gender
            // ("neutral")
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

    public String makePrompt(String srcSentence, String place, String listener, int intimacy){
        String prompt = "Your task is to convert the wording of the text according to the listener.\n" +
                "\n" +
                "Below is an explanation of the speech to be converted according to the listener.\n" +
                "1) If the listener is a professor, he or she should speak in a polite way.\n" +
                "2) If the listener is a friend and the intimacy is 1, you should use honorifics.\n" +
                "3) If the listener is a friend and the intimacy is 2, half-formal should be used.\n" +
                "4) If the listener is a friend and the intimacy is 3, he or she should use an intimate tone.\n" +
                "\n" +
                "I want to tell the "+listener+" the \""+ srcSentence+"\". Please change it and give me a sentence (only one)\n";

        return prompt;
    }

    public String getPapagoTransSentence(String s){
        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String text;
        try{
            text = URLEncoder.encode(s, "UTF-8");
        }catch(UnsupportedEncodingException e){
            throw new RuntimeException("인코딩 실패, e");
        }

        Map<String, String> requestHeaders =new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", papago_clientID);
        requestHeaders.put("X-Naver-Client-Secret", papago_clientSecret);

        String responseBody = post(apiURL, requestHeaders, text);
        log.info("responseBody = " + responseBody);
        JSONObject jsonObject = new JSONObject(responseBody);

        // JSON 객체 사용 예시:
        String translatedText = jsonObject.getJSONObject("message")
                .getJSONObject("result")
                .getString("translatedText");
        return translatedText;
    }

    private String post(String apiUrl, Map<String, String> requestHeaders, String text){
        HttpURLConnection conn = connect(apiUrl);
        String postParams = "source=en&target=ko&text=" +text;
        try{
            conn.setRequestMethod("POST");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()){
                conn.setRequestProperty(header.getKey(), header.getValue());
            }
            conn.setDoOutput(true);
            try(DataOutputStream wr = new DataOutputStream(conn.getOutputStream())){
                wr.write(postParams.getBytes(StandardCharsets.UTF_8));
                wr.flush();
            }
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK){
                return readBody(conn.getInputStream());
            }else{
                return readBody(conn.getErrorStream());
            }
        }catch(IOException e){
            throw new RuntimeException("API 요청과 응답 실패",e);
        }finally {
            conn.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    public String createTargetSentence(String srcSentence) {
        String result = null;
        try {
            result= getGPTAnswer(srcSentence, 0, 1000)
                    .replaceAll("\n", "")
                    .replaceAll("\\.","")
                    .replaceAll("\\\\","")
                    .replaceAll("\"","");
        } catch (Exception e) {
            log.info("getGPTAnswer error(서버 에러)");
        }
        return result;
    }

    public String getGPTAnswer(String prompt, float temperature, int maxTokens) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatGPT_apiKey);

        List<ChatMessage> messages = new ArrayList<>();
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
