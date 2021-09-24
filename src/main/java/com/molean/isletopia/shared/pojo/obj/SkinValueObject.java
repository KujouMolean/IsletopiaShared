package com.molean.isletopia.shared.pojo.obj;

public class SkinValueObject {
    private String player;
    private String skinValue;

    public SkinValueObject(String player, String skinValue) {
        this.player = player;
        this.skinValue = skinValue;
    }

    public SkinValueObject() {
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getSkinValue() {
        return skinValue;
    }

    public void setSkinValue(String skinValue) {
        this.skinValue = skinValue;
    }

    @Override
    public String toString() {
        return "SkinValueObject{" +
                "player='" + player + '\'' +
                ", skinValue='" + skinValue + '\'' +
                '}';
    }
}
