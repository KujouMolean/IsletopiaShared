package com.molean.isletopia.shared.pojo.req;

public class SwitchServerRequest {
    private String player;
    private String server;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public SwitchServerRequest() {
    }

    public SwitchServerRequest(String player, String server) {
        this.player = player;
        this.server = server;
    }

    @Override
    public String toString() {
        return "SwitchServerRequest{" +
                "player='" + player + '\'' +
                ", server='" + server + '\'' +
                '}';
    }
}
