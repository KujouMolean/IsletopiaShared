package com.molean.isletopia.shared.pojo.req;

public class ElytraRequestObject {
    private String player;
    private String reason;

    @Override
    public String toString() {
        return "ElytraRequestObject{" +
                "player='" + player + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ElytraRequestObject() {
    }

    public ElytraRequestObject(String player, String reason) {
        this.player = player;
        this.reason = reason;
    }
}
