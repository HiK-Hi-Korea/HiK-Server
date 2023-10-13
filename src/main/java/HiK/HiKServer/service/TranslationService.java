package HiK.HiKServer.service;

import HiK.HiKServer.dto.TranslationForm;
import HiK.HiKServer.entity.Sentence;
import HiK.HiKServer.repositroy.SentenceRepository;
import jakarta.transaction.Transactional;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:apikey.properties")
@Slf4j
@Service
public class TranslationService {
    @Autowired
    private SentenceRepository sentenceRepository;

    @Value("${chatGPT}")
    private String chatGPT_apiKey;
    private static final String COMPLETION_ENDPOINT = "https://api.openai.com/v1/completions";

    @Transactional
    public Sentence translation(TranslationForm dto){
        String srcSentence = dto.getSourceSentence();
        String place = dto.getPlace();
        String listener = dto.getListener();
        int intimacy = dto.getIntimacy();
        // papago 번역 api이용
        String middleSentence = getPapagoTransSentence(srcSentence);

        // ai모델로 translatedSentence, desSituation 넘겨서 targetSentence 받아오기
        String translatedSentence = getConversation(middleSentence);
        if (translatedSentence == null){
            throw new IllegalStateException("chat gpt - generate sentence 실패!");
        }
        // 최종 targetSentence 를 TTS 해서 voiceFile 받아오기
        String voiceFile = "papago_getVoicefile()";
        Long temp_id = null;
        Sentence sentence = new Sentence(temp_id, srcSentence, place, listener, intimacy, translatedSentence, voiceFile);

        // DB에 저장
        Sentence saved = sentenceRepository.save(sentence);
        return saved;
    }

    public String getPapagoTransSentence(String s){
        String clientID ="M5H4pKogtVC1vmBrGU3g";
        String clientSecret = "owQ8rx0IZc";

        String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
        String text;
        try{
            text = URLEncoder.encode(s, "UTF-8");
        }catch(UnsupportedEncodingException e){
            throw new RuntimeException("인코딩 실패, e");
        }

        Map<String, String> requestHeaders =new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", clientID);
        requestHeaders.put("X-Naver-Client-Secret", clientSecret);

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

    public String getConversation(String srcSentence) {
        String answer = null;
        try {
            answer= generateText(srcSentence, 0.5f, 1000);
            String answerFilter1 = answer.replaceAll("\n", "");
            String result =  answerFilter1.replaceAll("\\.","");
            result =  result.replaceAll("\\\\","");
            result =  result.replaceAll("\"","");
        } catch (Exception e) {
            log.info("generateText error(서버 에러)");
        }
        return answer;
    }

    public String generateText(String prompt, float temperature, int maxTokens) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + chatGPT_apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model","text-davinci-003");
        requestBody.put("prompt", prompt);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.postForEntity(COMPLETION_ENDPOINT, requestEntity, Map.class);
        Map<String, Object> responseBody = response.getBody();
        System.out.println(responseBody.toString());

        List<Map<String, Object>> choicesList = (List<Map<String, Object>>)responseBody.get("choices");
        Map<String, Object> choiceMap = choicesList.get(0);
        String answer = (String)choiceMap.get("text");

        return answer;
    }
}
