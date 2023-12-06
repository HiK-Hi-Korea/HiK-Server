package HiK.HiKServer.gpt;

public abstract class PromptHandler {
    protected PromptHandler successor;

    public void setSuccessor(PromptHandler successor){
        this.successor = successor;
    }

    public abstract void handleRequest(GptPrompt gptPrompt, String request_place, String request_listener);
}