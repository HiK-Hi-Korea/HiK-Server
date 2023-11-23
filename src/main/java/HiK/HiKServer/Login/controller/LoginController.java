package HiK.HiKServer.Login.controller;

import HiK.HiKServer.Login.service.LoginService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/login/oauth2")
public class LoginController {

    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // 로그인 최초 요청 처리 : (/login/oauth2/request/google)
    //@NoAuth
    @GetMapping("/request/{socialLoginType}") //google이 들어올 것이다.
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String socialLoginType) throws IOException {
        loginService.request(socialLoginType);
    }


    // redirect url이다.
    @GetMapping("/code/{registrationId}")
    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        loginService.socialLogin(code, registrationId);
    }
}