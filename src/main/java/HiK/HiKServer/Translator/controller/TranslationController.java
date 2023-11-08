package HiK.HiKServer.Translator.controller;

import HiK.HiKServer.Translator.dto.TranslationForm;
import HiK.HiKServer.entity.Sentence;
import HiK.HiKServer.Translator.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class TranslationController {
    @Autowired
    private TranslationService translationService;

    @PostMapping("/{userId}/trans")
    public ResponseEntity<Sentence> translation(@PathVariable String userId, @RequestBody TranslationForm translationForm) throws IOException {
        System.out.println("translation 시작");
        Sentence translatedSentence = translationService.translation(userId, translationForm);
        return (translatedSentence != null) ? ResponseEntity.status(HttpStatus.OK).body(translatedSentence):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
