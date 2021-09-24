package com.molean.isletopia.shared.pojo.resp;

public class CommonResponseObject {
    String message;

    public String getMessage() {
        return message;
    }

    public CommonResponseObject() {
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CommonResponseObject(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GiveItemResponseObject{" +
                "message='" + message + '\'' +
                '}';
    }
}
