package com.molean.isletopia.shared.pojo.req;

public class TeleportRequest{
    private String sourcePlayer;
    private String targetPlayer;

    public TeleportRequest() {
    }

    public TeleportRequest(String sourcePlayer, String targetPlayer) {
        this.sourcePlayer = sourcePlayer;
        this.targetPlayer = targetPlayer;
    }

    public String getSourcePlayer() {
        return sourcePlayer;
    }

    public void setSourcePlayer(String sourcePlayer) {
        this.sourcePlayer = sourcePlayer;
    }

    public String getTargetPlayer() {
        return targetPlayer;
    }

    public void setTargetPlayer(String targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    @Override
    public String toString() {
        return "TeleportRequest{" +
                "sourcePlayer='" + sourcePlayer + '\'' +
                ", targetPlayer='" + targetPlayer + '\'' +
                '}';
    }
}
