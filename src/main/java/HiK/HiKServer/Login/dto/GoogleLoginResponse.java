package HiK.HiKServer.Login.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleLoginResponse {
    // 구글에서 받은 토큰을 저장할 VO 객체
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String scope;
    private String token_type; // 반환된 토큰 유형 (Bearer 고정)
    private String id_token;
}
