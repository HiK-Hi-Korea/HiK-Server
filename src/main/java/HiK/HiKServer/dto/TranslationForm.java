package HiK.HiKServer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class TranslationForm {
    private String sourceSentence;
    private String desiredSituation;
}
