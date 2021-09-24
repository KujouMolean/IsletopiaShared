package com.molean.isletopia.shared.pojo.req;

public class PlayTimeRequest {
    private String player;

    public PlayTimeRequest() {

    }

    public PlayTimeRequest(String player) {
        this.player = player;
    }


    @Override
    public String toString() {
        return "PlayTimeRequest{" +
                "player='" + player + '\'' +
                '}';
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

}
