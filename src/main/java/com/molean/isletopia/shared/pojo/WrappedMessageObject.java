package com.molean.isletopia.shared.pojo;

public class WrappedMessageObject {
    private String subChannel;
    private String message;
    private String from;
    private String to;
    private Long time;

    public WrappedMessageObject() {
    }

    public String getSubChannel() {
        return subChannel;
    }

    public void setSubChannel(String subChannel) {
        this.subChannel = subChannel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public WrappedMessageObject(String subChannel, String message, String from, String to, Long time) {
        this.subChannel = subChannel;
        this.message = message;
        this.from = from;
        this.to = to;
        this.time = time;
    }
}
