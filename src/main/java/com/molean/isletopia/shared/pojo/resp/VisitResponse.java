package com.molean.isletopia.shared.pojo.resp;

import java.util.UUID;

public class VisitResponse {
    private UUID target;
    private String response;
    private String responseMessage;

    public UUID getTarget() {
        return target;
    }

    public void setTarget(UUID target) {
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
