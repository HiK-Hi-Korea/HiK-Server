package HiK.HiKServer.controller;

import HiK.HiKServer.dto.TranslationForm;
import HiK.HiKServer.entity.Sentence;
import HiK.HiKServer.service.TranslationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class TranslationController {
    @Autowired
    private TranslationService translationService;

    @PostMapping("/trans")
    public ResponseEntity<Sentence> translation(@RequestBody TranslationForm translationForm) throws IOException {
        System.out.println("translation 시작");
        Sentence translatedSentence = translationService.translation(translationForm);
        return (translatedSentence != null) ? ResponseEntity.status(HttpStatus.OK).body(translatedSentence):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
