package HiK.HiKServer.User.controller;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.LearningContents.dto.LearningContentDto;
import HiK.HiKServer.User.UserDto;
import HiK.HiKServer.User.domain.User;
import HiK.HiKServer.User.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserApiController {
    @Autowired
    private UserService userService;
    @GetMapping("/user/info")
    public ResponseEntity<UserDto> getUserInfo(@RequestHeader(name = "X-UserId") String userId){
        log.info("get user info!");
        UserDto userDto = userService.getUserInfo(userId);
        return (userDto != null) ? ResponseEntity.status(HttpStatus.OK).body(userDto):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @GetMapping("/user/showList")
    public ResponseEntity<List<LearningContent>> showUserContentList(@RequestHeader(name = "X-UserId") String userId){
        log.info("show user content list 시작!");
        List<LearningContent> learningContentList = userService.getUserContentList(userId);
        log.info("존재하는 컨텐츠 : "+learningContentList.get(0).toString());
        return (learningContentList != null) ? ResponseEntity.status(HttpStatus.OK).body(learningContentList):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/user/showListDto")
    public ResponseEntity<List<LearningContentDto>> showUserContentListDto(@RequestHeader(name = "X-UserId") String userId){
        log.info("show user content list 시작!");
        List<LearningContentDto> learningContentDtos = userService.getUserContentDtoList(userId);
        log.info("존재하는 컨텐츠 : "+learningContentDtos.get(0).toString());
        return (learningContentDtos != null) ? ResponseEntity.status(HttpStatus.OK).body(learningContentDtos):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

//    @GetMapping("/{userId}/learning-contents")
//    public ResponseEntity<List<LearningContent>> getLearningContentsByUserId(@PathVariable String userId) {
//        List<LearningContent> learningContents = userService.getLearningContentsByUserId(userId);
//        return ResponseEntity.ok(learningContents);
//    }

    @PutMapping("/user/setName")
    public ResponseEntity<User> setUserName(@RequestHeader(name = "X-UserId") String userId, @RequestBody Map<String, String> nameMap){
        log.info("set user name!");
        User user = userService.setUserName(userId, nameMap.get("name"));
        return (user != null) ? ResponseEntity.status(HttpStatus.OK).body(user):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    
    @PutMapping("/user/setAge")
    public ResponseEntity<User> setUserAge(@RequestHeader(name = "X-UserId") String userId, @RequestBody Map<String, Integer> ageMap){
        log.info("set user age!");
        User user = userService.setUserAge(userId, ageMap.get("age"));
        return (user != null) ? ResponseEntity.status(HttpStatus.OK).body(user):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PutMapping("/user/setNation")
    public ResponseEntity<User> setUserNation(@RequestHeader(name = "X-UserId") String userId, @RequestBody Map<String, String> nationMap){
        log.info("set user nation!");
        User user = userService.setUserNation(userId, nationMap.get("nation"));
        return (user != null) ? ResponseEntity.status(HttpStatus.OK).body(user):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PutMapping("/user/setLanguage")
    public ResponseEntity<User> setUserLanguage(@RequestHeader(name = "X-UserId") String userId, @RequestBody Map<String,String> languageMap){
        log.info("set user language!");
        User user = userService.setUserLanguage(userId, languageMap.get("language"));
        return (user != null) ? ResponseEntity.status(HttpStatus.OK).body(user):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
    @PutMapping("/user/setGender")
    public ResponseEntity<User> setUserGender(@RequestHeader(name = "X-UserId") String userId, @RequestBody Map<String,String> genderMap){
        log.info("set user gender!");
        User user = userService.setUserGender(userId, genderMap.get("gender"));
        return (user != null) ? ResponseEntity.status(HttpStatus.OK).body(user):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
