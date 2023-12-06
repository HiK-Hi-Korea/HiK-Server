package HiK.HiKServer.gpt;

import java.util.Arrays;

public class PromptHandler_Universty extends PromptHandler {
    String[] university_set = {"university", "college",};
    String university_system = "";

    @Override
    public void handleRequest(GptPrompt gptPrompt, String request_place, String request_listener) {
        boolean isExist = Arrays.stream(university_set).anyMatch(request_place::equals);
        if (isExist) {
            gptPrompt.setSystem(university_system);
        } else if (successor != null) {
            successor.handleRequest(gptPrompt, request_place, request_listener);
        }
    }
}
