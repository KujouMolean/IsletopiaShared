package com.molean.isletopia.shared.pojo.req;

public class BeaconRequestObject {
    private String player;
    private String reason;

    public BeaconRequestObject() {
    }

    @Override
    public String toString() {
        return "BeaconRequestObject{" +
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

    public BeaconRequestObject(String player, String reason) {
        this.player = player;
        this.reason = reason;
    }
}
