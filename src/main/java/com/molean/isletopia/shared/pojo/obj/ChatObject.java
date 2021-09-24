package com.molean.isletopia.shared.pojo.obj;

public class ChatObject {
    private String player;
    private String content;

    public ChatObject(String player, String content) {
        this.player = player;
        this.content = content;
    }

    public ChatObject() {
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ChatObject{" +
                "player='" + player + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

