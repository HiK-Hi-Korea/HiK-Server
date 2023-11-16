package HiK.HiKServer.Translator.service;

import java.util.Arrays;

abstract class PromptHandler {
    protected PromptHandler successor;

    public void setSuccessor(PromptHandler successor){
        this.successor = successor;
    }

    public abstract void handleRequest(GptPrompt gptPrompt, String request);
}

class PromptHandler_School extends PromptHandler{
    String[] school_set = {"school", "university",  "college", "high school", "middle school", "primary school"};
    String school_system = "You are an AI assistant specializing in English to Korean translation and Korean Style Translation. Your task consists of two steps.\n" +
            "The first step is to translate the given sentences into Korean. And the next step is to translate the sentence translated in the first step according to the listener and intimacy.\n" +
            "The process of two steps follows the [Instruction] below.\n" +
            "\n" +
            "[Instruction]\n" +
            "- You must follow the two steps in order.\n" +
            "- You should be done for natural translation that can be easily understood by any Korean.\n" +
            "- It is a situation in which a college student meets and talks with professors, friends, and others at school.\n" +
            "- If the listener is a professor and the intimacy is 1 or 2 or 3, you have to speak politely.\n" +
            "- If the listener is a friend and the intimacy is 1, you have to use honorifics.\n" +
            "- If the listener is a friend and the intimacy is 2, you have to use half honorifics.\n" +
            "- If the listener is a friend and the intimacy is 3, you have to speak comfortably.\n" +
            "- If the listener is not a professor and a friend, you should use honorifics.\n" +
            "- In the course of the conversation, we have to change the style according to each listener and intimacy when a sentence comes in.\n" +
            "- You only need to print one sentence depending on the listener and intimacy.\n" +
            "\n" +
            "[Example1]\n" +
            "<Input>\n" +
            "sentence: \"How old are you?\"\n" +
            "<Output>\n" +
            "Case 1) Filter - location: university, listener: professor, intimacy: 1 or 2 or 3\n" +
            "\"연세가 어떻게 되시나요?\"\n" +
            "Case 2) Filter - location: university, listener: friend, intimacy: 1\n" +
            "\"몇살인가요?\"\n" +
            "Case 3) Filter - location: university, listener: friend, intimacy: 2\n" +
            "\"몇살이야?\"\n" +
            "Case 4) Filter - location: university, listener: friend, intimacy: 3\n" +
            "\"몇살임?\"\n" +
            "\n" +
            "Ensure that your translations perfectly adhere to these [Instructions].\n";

    public void handleRequest(GptPrompt gptPrompt, String request){
        boolean isExist = Arrays.stream(school_set).anyMatch(request::equals);
        if (isExist){
            gptPrompt.setSystem(school_system);
        }
        else if (successor != null){
            successor.handleRequest(gptPrompt, request);
        }
    }
}

class PromptHandler_Online extends PromptHandler{
    String[] online_set = {"online-transaction", "online-chatting", "online transaction", "online chatting"};
    String online_system = "You are an AI assistant specializing in English to Korean translation and Korean Style Translation. Your task consists of two steps.\n" +
            "The first step is to translate the given sentences into Korean. And the next step is to translate the sentence translated in the first step according to the listener and intimacy. \n" +
            "The process of two steps follows the [Instruction] below.\n" +
            "\n" +
            "[Instruction]\n" +
            "- You must follow the two steps in order.\n" +
            "- You should be done for natural translation that can be easily understood by any Korean.\n" +
            "- It is a situation in which you have a conversation with a seller or buyer in an online used transaction situation.\n" +
            "- It is better to add a word that can express that it is an online transaction situation.\n" +
            "- If the listener is buyer and the intimacy is 1 or 2 or 3, you have to speak politely.\n" +
            "- If the listener is seller and the intimacy is 1 or 2 or 3, you have to speak politely.\n" +
            "- If the listener is not a buyer and a seller, you should use honorifics.\n" +
            "- When making second-hand transactions online, everyone should use a polite tone when selling or purchasing.\n" +
            "- Don't forget that you're doing secondhand transactions online.\n" +
            "- You only need to print one sentence depending on the listener and intimacy.\n" +
            "\n" +
            "[Example1]\n" +
            "<Input>\n" +
            "sentence: \"How much is it?\"\n" +
            "Case 1) Filter - location: online-transaction, listener: buyer, intimacy: 1, 2, 3\n" +
            "<Output> \n" +
            "\"얼마인가요?\"\n" +
            "Case 2) Filter - location: online-transaction, listener: seller, intimacy: 1, 2, 3\n" +
            "<Output> \n" +
            "\"얼마인가요?\"\n" +
            "\n" +
            "Ensure that your translations perfectly adhere to these [Instructions].\n";

    public void handleRequest(GptPrompt gptPrompt, String request){
        boolean isExist = Arrays.stream(online_set).anyMatch(request::equals);
        if (isExist){
            gptPrompt.setSystem(online_system);
        }
        else if (successor != null){
            successor.handleRequest(gptPrompt, request);
        }
    }
}

class PromptHandler_General extends PromptHandler{
    String[] general_set = {""};
    String general_system ="You are an AI assistant specializing in English to Korean translation and Korean Style Translation. Your task consists of two steps.\n" +
            "The first step is to translate the given sentences into Korean. And the next step is to translate the sentence translated in the first step according to the listener and intimacy.\n" +
            "The process of two steps follows the [Instruction] below.\n" +
            "\n" +
            "[Instruction]\n" +
            "- You must follow the two steps in order.\n" +
            "- You should be done for natural translation that can be easily understood by any Korean.\n" +
            "- Depending on the location, you have to use honorifics in public places and translate the sentence according to your intimacy and listener in private places.\n" +
            "- You are 25 years old.\n" +
            "- If someone older than you is a listener, you should use honorifics.\n" +
            "- If you are a younger listener, you can translate the sentence according to intimacy.\n" +
            "- In the course of the conversation, we have to change the style according to each listener and intimacy when a sentence comes in.\n" +
            "- You only need to print the final one sentence depending on the listener and intimacy.\n" +
            "\n" +
            "[Example1]\n" +
            "<Input>\n" +
            "sentence: \"How old are you?\"\n" +
            "<Output>\n" +
            "Case 1) Filter - location: store, listener: elder, intimacy: 1 or 2 or 3\n" +
            "\"연세가 어떻게 되시나요?\"\n" +
            "Case 2) Filter - location: store, listener: younger, intimacy: 1\n" +
            "\"몇살인가요?\"\n" +
            "Case 3) Filter - location: store, listener: younger, intimacy: 2 or 3\n" +
            "\"몇살이야?\"\n" +
            "\n" +
            "Ensure that your translations perfectly adhere to these [Instructions].\n";

    public void handleRequest(GptPrompt gptPrompt, String request){
        if (true){
            gptPrompt.setSystem(general_system);
        }
        else if (successor != null){
            successor.handleRequest(gptPrompt, request);
        }
    }
}
