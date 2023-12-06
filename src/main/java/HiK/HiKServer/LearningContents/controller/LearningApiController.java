package HiK.HiKServer.LearningContents.controller;

import HiK.HiKServer.LearningContents.dto.*;
import HiK.HiKServer.LearningContents.service.LearningContentService;
import HiK.HiKServer.Translator.dto.TranslationForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    // 문장 클릭하고 상황변경 시켰을 때 나오는 trans sentence를 보내주는 api
    // 이전의 문장에 대한 정보 또한 input으로 받아야한다.
    @PostMapping("content/getTrans")
    public ResponseEntity<ChangeSentenceResponseDto> getTrans(@RequestHeader(name = "X-UserId") String userId, @RequestBody ChangeSentenceDto changeSentenceDto) throws IOException {
        log.info("get Trans 시작");
        ChangeSentenceResponseDto changeSentenceResponseDto = learningContentService.transSentenceInContent(userId, changeSentenceDto);
        if (changeSentenceResponseDto != null) log.info("translation - changeSentenceRequestDto" + changeSentenceResponseDto.getTranslatedSentence());
        else log.info("translatedSentence nul!!!");
        return (changeSentenceResponseDto != null) ? ResponseEntity.status(HttpStatus.OK).body(changeSentenceResponseDto):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PostMapping("content/getOriginalTranslate")
    public ResponseEntity<ChangeSentenceResponseDto> getOriginalTranslate(@RequestHeader(name = "X-UserId") String userId, @RequestBody TranslationForm translationForm) throws IOException {
        log.info("get original translate 시작");
        ChangeSentenceResponseDto changeSentenceResponseDto = learningContentService.getOriginalTranslate(translationForm);
        if (changeSentenceResponseDto != null) log.info("translation - changeSentenceRequestDto" + changeSentenceResponseDto.getTranslatedSentence());
        else log.info("translatedSentence nul!!!");
        return (changeSentenceResponseDto != null) ? ResponseEntity.status(HttpStatus.OK).body(changeSentenceResponseDto):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 문장 클릭하고 상황변경 시켰을 때 왜 이렇게 이유가 나오는지 알려주는 api
    @PostMapping("content/getReason")
    public ResponseEntity<ReasonResponseDto> getReason(@RequestHeader(name = "X-UserId") String userId, @RequestBody ReasonRequestDto reasonRequestDto) throws IOException {
        log.info("getReason 시작");
        ReasonResponseDto reasonResponseDto = learningContentService.getTransReason(userId, reasonRequestDto);
        if (reasonResponseDto != null) log.info("translation - changeSentenceRequestDto" + reasonResponseDto.getReason());
        else log.info("translatedSentence nul!!!");
        return (reasonResponseDto != null) ? ResponseEntity.status(HttpStatus.OK).body(reasonResponseDto):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
