package HiK.HiKServer.LearningContents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeSentenceDto {
    private String input_sentence;
    private String prev_place;
    private String prev_listener;
    private int prev_intimacy;

    private String cur_place;
    private String cur_listener;
    private int cur_intimacy;
}
