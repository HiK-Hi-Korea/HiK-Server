package HiK.HiKServer.Translator.controller;

import HiK.HiKServer.Translator.dto.TranslationForm;
import HiK.HiKServer.Translator.domain.Sentence;
import HiK.HiKServer.Translator.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@CrossOrigin(origins = "http://15.164.210.1:8080")
@RestController
public class TranslationController {

    @Autowired
    TranslationService translationService;

    @PostMapping("/trans")
    public ResponseEntity<Sentence> translation(@RequestHeader(name = "X-UserId") String userId, @RequestBody TranslationForm translationForm) throws IOException {
        log.info("translation 시작");
        Sentence translatedSentence = translationService.translation(userId, translationForm);
        if (translatedSentence != null) log.info("translation - translatedSentence" + translatedSentence.getTranslatedSentence());
        else log.info("translatedSentence nulll!!!");
        return (translatedSentence != null) ? ResponseEntity.status(HttpStatus.OK).body(translatedSentence):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
