package HiK.HiKServer.LearningContents.controller;

import HiK.HiKServer.LearningContents.dto.SentenceListDto;
import HiK.HiKServer.LearningContents.service.LearningContentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class LearningApiController {

    @Autowired
    private LearningContentService learningContentService;

    @GetMapping("/content/getSentences")
    public ResponseEntity<List<SentenceListDto>> showLearningContentSentenceList(@RequestHeader(name = "X-UserId") String userId, @RequestHeader(name ="X-ContentId")Long lcId){
        log.info("show user content list 시작!");
        List<SentenceListDto> sentenceListDtos = learningContentService.getSentenceList(lcId);
        log.info("존재하는 문장 : "+sentenceListDtos.get(0).toString());
        return (sentenceListDtos != null) ? ResponseEntity.status(HttpStatus.OK).body(sentenceListDtos):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
