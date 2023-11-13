package HiK.HiKServer.Translator.service;

public class GptPrompt {
    private String prompt;
    private String system;

    private String makePrompt(String srcSentence, String listener, int intimacy){
        String prompt = "Transfer the style of the sentence according to the listener and intimacy below:\n" +
                "<Input>\n"+
                "sentence: "+srcSentence+" listener: "+listener+", intimacy: "+intimacy;
        return prompt;
    }

    public GptPrompt(String srcSentence, String listener, int intimacy) {
        this.prompt = makePrompt(srcSentence, listener, intimacy);
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
