package HiK.HiKServer.LearningContents.dto;

import lombok.Getter;

@Getter
public class ReasonRequestDto {
   String input_sentence;
   String input_place;
   String input_listener;
   int input_intimacy;
   String translated_sentence;
   String place;
   String listener;
   int intimacy;
}
