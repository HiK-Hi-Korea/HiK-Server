package HiK.HiKServer.Login.controller;//package HiK.HiKServer.controller;
//
//import HiK.HiKServer.service.LoginService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/google")
//@CrossOrigin("*")
//@RequiredArgsConstructor
//public class GoogleLoginController {
//    private final LoginService googleloginService;
//
//    @GetMapping(value="/login/oauth")
//    public ResponseEntity<?> getGoogleAuthUrl(){
//        System.out.println("구글 로그인 시작!");
//        //1.reqUrl 구글로그인 창을 띄우고, 로그인 후 /login/oauth_google_check 으로 리다이렉션하게 한다.
//        return new ResponseEntity<>(googleloginService.getGoogleLoginView(), HttpStatus.MOVED_PERMANENTLY);
//    }
//
//    @GetMapping("/login")
//    public ResponseEntity<String> oauth_google_check(@RequestParam(value = "code") String code){
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(googleloginService.oauth_google_check(code));
//    }
//}
