package HiK.HiKServer.LearningContents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LearningContentDto {
    private Long id;
    private String place;
    private String listener;
    private LocalDateTime timestamp;
    private String mainSentence;
}
