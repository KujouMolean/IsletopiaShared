package com.molean.isletopia.shared.pojo.req;

public class TellMessageRequest {
    private String source;
    private String target;
    private String message;

    public TellMessageRequest() {
    }

    public TellMessageRequest(String source, String target, String message) {
        this.source = source;
        this.target = target;
        this.message = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "TellMessageRequest{" +
                "source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
