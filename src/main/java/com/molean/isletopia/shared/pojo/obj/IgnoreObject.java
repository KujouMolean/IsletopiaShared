package com.molean.isletopia.shared.pojo.obj;

import java.util.List;

public class IgnoreObject {
    private String player;
    private List<String> ignores;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public List<String> getIgnores() {
        return ignores;
    }

    public void setIgnores(List<String> ignores) {
        this.ignores = ignores;
    }

    public IgnoreObject() {
    }

    public IgnoreObject(String player, List<String> ignores) {
        this.player = player;
        this.ignores = ignores;
    }

    @Override
    public String toString() {
        return "IgnoreObject{" +
                "player='" + player + '\'' +
                ", ignores=" + ignores +
                '}';
    }
}
