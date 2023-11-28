package HiK.HiKServer.LearningContents.service;

public class GptPrompt_Learning {
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
        String prompt = "Explain how the input sentence was translated into translated sentense based on the location, listener and intimacy of the input filter and the filter used in the translation. Print out your answer in less than 500 characters. The input sentence, translated sentence, and filters used are as follows."
                +"\n<Input>"+
                "\nInput_Sentence: "+input_sentence+
        "\nInput__Filter - input_location: "+input_place+", input_listener: "+input_listener+", input_intimacy: "+input_intimacy+
        "\nTranslated_Sentence: "+translated_sentence+
        "\nFilter - location: "+place+", listener: "+listener+", intimacy: "+intimacy;

        return prompt;
    }

    public GptPrompt_Learning(String input_sentence, String input_place, String input_listener, int input_intimacy,
                              String place, String listener, int intimacy) {
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
                "\n" +
                "[Example1]\n" +
                "<Input>\n" +
                "Input_Sentence: \"사랑합니다.\"\n" +
                "Input__Filter - input_location: university, input_listener: professor, input_intimacy: 1\n" +
                "Translated_Sentence: \"사랑해\"\n" +
                "Filter - location: online-chatting, listener: friend, intimacy: 3\n" +
                "<Output> \n" +
                "\"한국어에서 \"합니다\"와 \"해\"는 존댓말과 반말의 차이를 나타냅니다. \"합니다\"는 공손한 표현으로 교수님과 같은 상황에서 사용되며, \"해\"는 반말로 더 친밀한 친구나 가족과의 대화에서 쓰입니다. \"사랑합니다\"는 공손한 표현으로 교수님에게, \"사랑해\"는 반말로 친구에게 사용된 것이며, 이는 한국어의 존댓말과 반말의 관습에 따른 선택입니다.\"\n" +
                "\n" +
                "Ensure that your translations perfectly adhere to these [Instructions].\n";
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
