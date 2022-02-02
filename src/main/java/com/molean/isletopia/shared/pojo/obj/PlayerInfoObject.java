package com.molean.isletopia.shared.pojo.obj;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerInfoObject {
    private Map<UUID, String> players;
    private Map<String, Map<UUID,String>> playersPerServer;

    public PlayerInfoObject() {
    }

    public PlayerInfoObject( Map<UUID, String>players, Map<String, Map<UUID,String>> playersPerServer) {
        this.players = players;
        this.playersPerServer = playersPerServer;
    }

    public  Map<UUID, String> getPlayers() {
        return players;
    }

    public void setPlayers( Map<UUID, String> players) {
        this.players = players;
    }

    public Map<String, Map<UUID,String>> getPlayersPerServer() {
        return playersPerServer;
    }

    public void setPlayersPerServer(Map<String, Map<UUID,String>> playersPerServer) {
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
