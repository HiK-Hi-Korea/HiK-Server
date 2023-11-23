package HiK.HiKServer.Login.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleOauth implements SocialOauth{

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.redirect.uri}")
    private String redirectUri;
    @Override
    public String getOauthRedirectURL(){

        return "https://accounts.google.com/o/oauth2/auth?client_id="
                +clientId
                +"&redirect_uri=" +redirectUri
                +"&response_type=code&scope=https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile";
    }
}
