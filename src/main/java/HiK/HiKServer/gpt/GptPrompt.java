package HiK.HiKServer.gpt;

public class GptPrompt {
    private String prompt;
    private String system;

    public GptPrompt() {}

    private String makePrompt(int userAge, String srcSentence, String place, String listener, int intimacy){
        if (place.equals("online") && (listener.equals("seller") || listener.equals("buyer")))
            place = "online-transaction";
        String prompt = "Transfer the style of the sentence according to the listener and intimacy below:\n"+
                "<Input>\n"+
                "sentence: "+ srcSentence +
                "Filter - location: "+place+", listener: "+listener+", intimacy: "+intimacy;
        return prompt;
    }

    public GptPrompt(int userAge, String srcSentence, String place, String listener, int intimacy) {
        this.prompt = makePrompt(userAge, srcSentence, place, listener, intimacy);
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
