package HiK.HiKServer.LearningContents.service;

import HiK.HiKServer.gpt.GptPrompt;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GptPrompt_Learning extends GptPrompt {
    private String prompt;
    private String system;

    private String makeTransPrompt(String input_sentence, String input_place, String input_listener, int input_intimacy,
                              String place, String listener, int intimacy){
        String prompt = "Transfer the style of the sentence according to the location, listener and intimacy below:"+
                "<Input>\n"+
                "Input_Sentence: "+input_sentence+
        "\nInput__Filter - input_location: "+input_place+", input_listener: "+input_listener+", input_intimacy: "+input_intimacy+
        "\nFilter - location: "+place+", listener: "+listener+", intimacy: "+intimacy;
        return prompt;
    }

    private String makeReasonPrompt(String input_sentence, String input_place, String input_listener, int input_intimacy,
                                    String translated_sentence, String place, String listener, int intimacy){
        log.info("make reason prompt: input sentence: "+input_sentence+" translated sentence: "+translated_sentence);
        String prompt = "Explain how the input sentence was translated into translated sentense based on the location, listener and intimacy of the input filter and the filter used in the translation. Print out your answer in less than 500 characters. The input sentence, translated sentence, and filters used are as follows.\n" +
                "<Input>\n" +
                "Input_Sentence: "+input_sentence+"\n" +
                "Input__Filter - input_location: "+input_place+", input_listener: "+input_listener+", input_intimacy: "+input_intimacy+"\n" +
                "Translated_Sentence: "+translated_sentence+"\n" +
                "Filter - location: "+place+", listener: "+listener+", intimacy: "+intimacy;
        return prompt;
    }

    public GptPrompt_Learning(String input_sentence, String input_place, String input_listener, int input_intimacy,
                              String place, String listener, int intimacy) {
        super();
        this.prompt = makeTransPrompt(input_sentence, input_place, input_listener, input_intimacy, place, listener, intimacy);
        this.system = "You are an AI assistant specializing in Korean Style Translation. Your task is translate the sentence according to the location, listener and intimacy.\n" +
                "The process follows the [Instruction] below.\n" +
                "\n" +
                "[Instruction]\n" +
                "- You should be done for natural translation that can be easily understood by any Korean.\n" +
                "- When translating a sentence, you have to naturally translate the language based on the location, listener, and intimacy of the given filter.\n" +
                "\n" +
                "[Example1]\n" +
                "<Input>\n" +
                "Input_Sentence: \"교수님 사랑합니다.\"\n" +
                "Input__Filter - input_location: university, input_listener: professor, input_intimacy: 1\n" +
                "Case 1) Filter - location: online-chatting, listener: friend, intimacy: 3\n" +
                "<Output> \n" +
                "\"사랑해\"\n" +
                "Case 2) Filter - location: store, listener: grandmother, intimacy: 3\n" +
                "<Output> \n" +
                "\"할머니 사랑합니다.\"\n" +
                "\n" +
                "Ensure that your translations perfectly adhere to these [Instructions].\n";
    }

    public GptPrompt_Learning(String input_sentence, String input_place, String input_listener, int input_intimacy,  String translated_sentence, String place, String listener, int intimacy) {
        this.prompt = makeReasonPrompt(input_sentence, input_place, input_listener, input_intimacy, translated_sentence, place, listener, intimacy);
        this.system = "You are an AI assistant specializing in Korean Style Translation. Your task is explaining the reason why you translated the given sentence in such a way based on Korean grammar. \n" +
                "The process follows the [Instruction] below.\n" +
                "\n" +
                "[Instruction]\n" +
                "- You should be done for natural translation that can be easily understood by any Korean.\n" +
                "- When explaining the reason why the sentence was translated, you must answer based on Korean grammar and given filter value(location, listener, intimacy).\n" +
                "- Most importantly, you have to answer which words were translated how and why according to Korean grammar.\n" +
                "\n" +
                "[Example1]\n" +
                "<Input>\n" +
                "Input_Sentence: \"나는 너를 사랑한다.\"\n" +
                "Input__Filter - input_location: university, input_listener: student, input_intimacy: 1\n" +
                "Translated_Sentence: \"난 널 사랑해\"\n" +
                "Filter - location: online-chatting, listener: friend, intimacy: 3\n" +
                "<Output> \n" +
                "\"\n" +
                "The difference in speech levels between \"나는 너를 사랑한다.\" and \"난 널 사랑해.\" stems from the linguistic structure and rules of formality and informality in the Korean language. The former is expressed in formal language, where the subject '나는' and the object '너를' are presented accurately, and the verb '사랑한다' is formulated in a formal tone. On the other hand, the latter is in informal language, with contractions like '난' for '나는' and '널' for '너를,' reflecting casual vocabulary usage. The verb '사랑해' is also in an informal tone. The reason for this distinction lies primarily in choosing an appropriate tone based on the relationship between the speaker and the listener and the context of the conversation, adhering to the rules of formal and informal language use in Korean.\"\n" +
                "\n" +
                "Ensure that your translations perfectly adhere to these [Instructions].";
    }

    public GptPrompt_Learning(String srcSentence, String place, String listener, int intimacy){
        log.info("gpt prompt learning 버전을 사용한 프롬프트");
        if (place.equals("online") && (listener.equals("seller") || listener.equals("buyer")))
            place = "online-transaction";
        String prompt = "Transfer the style of the sentence according to the listener and intimacy below:\n"+
                "<Input>\n"+
                "sentence: "+ srcSentence +
                "Filter - location: "+place+", listener: "+listener+", intimacy: "+intimacy;
        this.prompt = prompt;
    }

    public void setSystem(String system){
        this.system = system;
    }
    public String getPrompt() {
        return prompt;
    }

    public String getSystem() {
        return system;
    }
}
