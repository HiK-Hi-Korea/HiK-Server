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
                                    String translated_sentence){
        String prompt = "Explain how the input sentence was translated into translated sentense based on the location, listener and intimacy of the input filter. Print out your answer in less than 500 characters. The input sentence, translated sentence, and input filter used are as follows.\n"+
                "<Input>\n"+
                "Input_Sentence: "+input_sentence+
        "\nInput__Filter - input_location: "+input_place+", input_listener: "+input_listener+", input_intimacy: "+input_intimacy
        +"Translated_Sentence: "+translated_sentence;
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

    public GptPrompt_Learning(String input_sentence, String input_place, String input_listener, int input_intimacy, String translated_sentence) {
        this.prompt = makeReasonPrompt(input_sentence, input_place, input_listener, input_intimacy, translated_sentence);
        this.system = "You are an AI assistant specializing in English to Korean translation and Korean Style Translation.\n" +
                "Your task is explaining the reason why you translated the given sentence in such a way based on Korean grammar. \n" +
                "The process follows the [Instruction] below.\n" +
                "\n" +
                "[Instruction]\n" +
                "- You should be done for natural translation that can be easily understood by any Korean.\n" +
                "- When explaining the reason why the sentence was translated, you must answer based on Korean grammar and given filter value(location, listener, intimacy).\n" +
                "\n" +
                "[Example1]\n" +
                "<Input>\n" +
                "Input_Sentence: \"How much is it?\"\n" +
                "Input__Filter - input_location: university, input_listener: professor, input_intimacy: 1\n" +
                "Translated_Sentence: \"얼마인가요?\"\n" +
                "<Output> \n" +
                "\"한국어에서는 말하는 상황과 대상에 따라 존댓말과 반말을 사용하는데, \"얼마인가요?\"는 상대방에게 존경을 표시하는 존댓말입니다. 대학교 교수님은 학문적인 환경에서 규정된 교육 기관의 일원으로서 존경받는 입장이기 때문에, 그들에게 말할 때는 존댓말을 사용하는 것이 일반적입니다. \"인가요\"는 높임법으로, \"이다\"의 높임법 형태인데, 이것이 문장 끝에 사용되면 존댓말의 높임법을 나타냅니다.\n" +
                "\n" +
                "따라서 \"얼마인가요?\"는 대학교 교수님과 같이 정식적인 환경에서 상대방에게 물을 때 사용되는 공손한 표현입니다. 만약 비공식적인 상황에서 친밀도 1인 상대에게 물을 때는 \"얼마야?\"와 같은 반말을 사용할 수 있습니다.\"\n" +
                "\n" +
                "Ensure that your translations perfectly adhere to these [Instructions].";
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
