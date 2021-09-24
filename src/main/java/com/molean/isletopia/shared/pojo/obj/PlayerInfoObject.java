package com.molean.isletopia.shared.pojo.obj;

import java.util.List;
import java.util.Map;

public class PlayerInfoObject {
    private List<String> players;
    private Map<String, List<String>> playersPerServer;

    public PlayerInfoObject() {
    }

    public PlayerInfoObject(List<String> players, Map<String, List<String>> playersPerServer) {
        this.players = players;
        this.playersPerServer = playersPerServer;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public Map<String, List<String>> getPlayersPerServer() {
        return playersPerServer;
    }

    public void setPlayersPerServer(Map<String, List<String>> playersPerServer) {
        this.playersPerServer = playersPerServer;
    }

    @Override
    public String toString() {
        return "PlayerInfoObject{" +
                "players=" + players +
                ", playersPerServer=" + playersPerServer +
                '}';
    }
}
