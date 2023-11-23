package HiK.HiKServer.Login.service;

import HiK.HiKServer.Login.domain.GoogleOauth;
import HiK.HiKServer.Login.domain.UserResource;
import HiK.HiKServer.User.domain.User;
import HiK.HiKServer.User.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginService {
    @Autowired
    private UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.token.uri}")
    private String tokenUri;

    @Value("${google.resource.uri}")
    private String resourceUri;

    private final GoogleOauth googleOauth;
    private final HttpServletResponse response;

    public Optional<User> findUserByUserResource(UserResource userResource){
        return userRepository.findById(userResource.getId());
    }
    public Optional<User> newUser(UserResource userResource){
        log.info("newUser: 새로 가입하는 중인 유저(userResource) ={}", userResource);
        User newUser = new User(userResource.getId(), userResource.getEmail());
        log.info("newUser2 : 새로 가입하는 중인 유저(user) ={}", newUser);
        userRepository.save(newUser);
        log.info("user repository에 save 완료");
        return Optional.of(newUser);
    }

    public void request(String socialLoginType) throws IOException {
        String redirectURL;
        switch(socialLoginType){
            case "google":{
                redirectURL = googleOauth.getOauthRedirectURL();
            }break;
            default:{
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }
        }
        log.info("redirect URL = {}",redirectURL);
        response.sendRedirect(redirectURL);
    }
    public UserResource socialLogin(String code, String registrationId) {
        log.info("======================================================");
        String accessToken = getAccessToken(code, registrationId);
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);
        log.info("userResourceNode = {}", userResourceNode);
        UserResource userResource = new UserResource();
        log.info("userResource = {}", userResource);
        switch (registrationId) {
            case "google": {
                userResource.setId(userResourceNode.get("id").asText());
                userResource.setEmail(userResourceNode.get("email").asText());
                userResource.setPicture(userResourceNode.get("picture").asText());
                break;
            } default: {
                throw new RuntimeException("UNSUPPORTED SOCIAL TYPE");
            }
        }

        log.info("id = {}", userResource.getId());
        log.info("email = {}", userResource.getEmail());
        log.info("picture = {}", userResource.getPicture());
        log.info("======================================================");
        return userResource;
    }

    private String getAccessToken(String authorizationCode, String registrationId) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}