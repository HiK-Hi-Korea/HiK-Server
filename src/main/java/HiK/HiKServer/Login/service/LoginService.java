package HiK.HiKServer.Login.service;

import HiK.HiKServer.Login.dto.GoogleLoginResponse;
import HiK.HiKServer.Login.dto.GoogleOAuthRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {
    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.auth.url}")
    private String googleAuthUrl;

    @Value("${google.login.url}")
    private String googleApiUrl;

    @Value("${google.redirect.url}")
    private String googleRedirectUrl;

    public void socialLogin(String code, String registrationId) {
        System.out.println("code = " + code);
        System.out.println("registrationId = " + registrationId);
    }

    public String getGoogleLoginView(){
        String reqUrl = googleApiUrl + "/o/oauth2/v2/auth?client_id=" + googleClientId + "&redirect_uri=" + googleRedirectUrl
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";

        log.info("myLog-LoginUrl : {}",googleApiUrl);
        log.info("myLog-ClientId : {}",googleClientId);
        log.info("myLog-RedirectUrl : {}",googleRedirectUrl);

        //1.reqUrl 구글로그인 창을 띄우고, 로그인 후 /login/oauth_google_check 으로 리다이렉션하게 한다.
        return reqUrl;
    }

    public String oauth_google_check(String authCode){
        // 2. 구글에 등록된 HiK 설정 정보를 보내서 약속된 토큰을 받기 위한 객체 생성
        GoogleOAuthRequest googleOAuthRequest = GoogleOAuthRequest
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(authCode)
                .redirectUri(googleRedirectUrl)
                .grantType("authorization_code")
                .build();
        RestTemplate restTemplate = new RestTemplate();

        // 3. 토큰 요청
        ResponseEntity<GoogleLoginResponse> apiResponse = restTemplate.postForEntity(googleAuthUrl + "/token", googleOAuthRequest, GoogleLoginResponse.class);
        // 4. 받은 토큰을 토큰 객체에 저장
        GoogleLoginResponse googleLoginResponse = apiResponse.getBody();

        log.info("google login responseBody {}",googleLoginResponse.toString());

        String googleToken = googleLoginResponse.getId_token();

        // 5. 받은 토큰을 구글에 보내 유저 정보를 얻는다.
        String requestUrl = UriComponentsBuilder.fromHttpUrl(googleAuthUrl + "/tokeninfo").queryParam("id_token",googleToken).toUriString();

        // 6. 허가된 토큰의 유저정보를 결과로 받는다.
        String resultJson = restTemplate.getForObject(requestUrl, String.class);

        log.info("구글 로그인 유저정보:\n" + resultJson);
        return resultJson;
    }
}
