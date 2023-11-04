package HiK.HiKServer.Translator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class TranslationForm {
    private String sourceSentence;
    private String place;
    private String listener;
    private int intimacy;
}
