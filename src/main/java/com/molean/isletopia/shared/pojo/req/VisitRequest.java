package com.molean.isletopia.shared.pojo.req;

public class VisitRequest {
    private String sourcePlayer;
    private String targetPlayer;
    private int id;

    public VisitRequest() {
    }

    public VisitRequest(String sourcePlayer, String targetPlayer, int id) {

        this.sourcePlayer = sourcePlayer;
        this.targetPlayer = targetPlayer;
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "VisitRequest{" +
                "sourcePlayer='" + sourcePlayer + '\'' +
                ", targetPlayer='" + targetPlayer + '\'' +
                ", id=" + id +
                '}';
    }
}
