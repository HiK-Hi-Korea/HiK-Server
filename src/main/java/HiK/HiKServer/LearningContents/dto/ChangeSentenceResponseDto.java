package HiK.HiKServer.LearningContents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeSentenceResponseDto {
    String translatedSentence;
    String voiceFile;
}
