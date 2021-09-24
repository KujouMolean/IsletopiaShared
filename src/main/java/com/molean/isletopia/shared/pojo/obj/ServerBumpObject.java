package com.molean.isletopia.shared.pojo.obj;

import java.util.List;

public class ServerBumpObject {
    private String player;
    private String user;
    private List<String> items;

    public ServerBumpObject(String player, String user, List<String> items) {
        this.player = player;
        this.user = user;
        this.items = items;
    }

    public ServerBumpObject() {
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ServerBumpObject{" +
                "player='" + player + '\'' +
                ", user='" + user + '\'' +
                ", items=" + items +
                '}';
    }
}
