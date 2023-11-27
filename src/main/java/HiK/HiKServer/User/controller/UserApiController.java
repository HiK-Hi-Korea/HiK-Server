package HiK.HiKServer.User.controller;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.LearningContents.dto.LearningContentDto;
import HiK.HiKServer.User.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserApiController {
    @Autowired
    private UserService userService;

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

    @GetMapping("/{userId}/learning-contents")
    public ResponseEntity<List<LearningContent>> getLearningContentsByUserId(@PathVariable String userId) {
        List<LearningContent> learningContents = userService.getLearningContentsByUserId(userId);
        return ResponseEntity.ok(learningContents);
    }
}
