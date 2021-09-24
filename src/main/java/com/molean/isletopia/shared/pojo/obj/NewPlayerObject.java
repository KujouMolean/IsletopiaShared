package com.molean.isletopia.shared.pojo.obj;

public class NewPlayerObject {
    private String player;
    private String localServerName;

    public NewPlayerObject() {
    }

    public NewPlayerObject(String player, String localServerName) {
        this.player = player;
        this.localServerName = localServerName;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getLocalServerName() {
        return localServerName;
    }

    public void setLocalServerName(String localServerName) {
        this.localServerName = localServerName;
    }

    @Override
    public String toString() {
        return "NewPlayerObject{" +
                "player='" + player + '\'' +
                ", localServerName='" + localServerName + '\'' +
                '}';
    }
}
