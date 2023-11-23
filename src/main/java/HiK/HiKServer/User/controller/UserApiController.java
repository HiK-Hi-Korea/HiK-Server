package HiK.HiKServer.User.controller;

import HiK.HiKServer.LearningContents.domain.LearningContent;
import HiK.HiKServer.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class UserApiController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userId}/learning-contents")
    public ResponseEntity<List<LearningContent>> getLearningContentsByUserId(@PathVariable String userId) {
        List<LearningContent> learningContents = userService.getLearningContentsByUserId(userId);
        return ResponseEntity.ok(learningContents);
    }
}
