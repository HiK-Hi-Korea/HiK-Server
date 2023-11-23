package HiK.HiKServer.Login.controller;

import HiK.HiKServer.Login.domain.UserResource;
import HiK.HiKServer.Login.service.LoginService;
import HiK.HiKServer.User.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping(value = "/login/oauth2")
public class LoginController {

    @Autowired
    LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    // 로그인 최초 요청 처리 : (/login/oauth2/request/google)
    //@NoAuth
    @GetMapping("/request/{socialLoginType}") //google이 들어올 것이다.
    public void socialLoginRedirect(@PathVariable(name="socialLoginType") String socialLoginType) throws IOException {
        log.info("로그인 시작");
        loginService.request(socialLoginType);
    }


    // redirect url이다.
    @GetMapping("/code/{registrationId}")
    public ResponseEntity<User> googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        log.info("로그인 call back");
        UserResource userResource = loginService.socialLogin(code, registrationId); // Optional 객체 벗기기
        if (userResource != null){
            Optional<User> login_user = loginService.findUserByUserResource(userResource);
            if (login_user.isPresent()){
                // 우리 서비스에 가입되어있는 유저
                log.info("우리 서비스에 가입되어 있는 유저: {}", login_user.get());
                return ResponseEntity.status(HttpStatus.OK).body(login_user.get());
            }
            else{
                // 서비스에 가입되어 있지 않은 유저면, 바로 데이터 베이스에 회원가입 시키고 부족한 정보에 대해선 이후 set함수를 통해 구현합니다.
                Optional<User> user = loginService.newUser(userResource);
                log.info("우리 서비스에 새로 가입한 유저: {}", user);
                return user.map(value -> ResponseEntity.status(HttpStatus.OK).body(value)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

            }}
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}