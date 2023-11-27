package HiK.HiKServer.LearningContents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SentenceListDto {

    private Long id;
    private String srcSentence;
    private String place;
    private String listener;
    private int intimacy;
    private String translatedSentence;
    private String voiceFile;
    private LocalDateTime timestamp;
}
