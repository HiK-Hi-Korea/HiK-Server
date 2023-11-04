package HiK.HiKServer.Login.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleOAuthRequest {
    // 구글에서 발급받은 클라이언트 id와 리다이렉션 URI 보안키 등을 보내어 토큰을 받을 VO 객체 클래스
    private String clientId;
    private String redirectUri;
    private String clientSecret;
    private String responseType;
    private String scope;
    private String code;
    private String accessType;
    private String grantType;
    private String state;
    private String includeGrantedScopes;
    private String loginHint;
    private String prompt;
}
