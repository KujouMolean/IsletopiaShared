package com.molean.isletopia.shared.pojo.resp;

public class VisitResponse {
    private String target;
    private String response;
    private String responseMessage;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    @Override
    public String toString() {
        return "VisitResponse{" +
                "target='" + target + '\'' +
                ", response='" + response + '\'' +
                ", responseMessage='" + responseMessage + '\'' +
                '}';
    }
}
