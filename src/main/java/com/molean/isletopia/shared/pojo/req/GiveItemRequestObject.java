package com.molean.isletopia.shared.pojo.req;

import java.util.List;

public class GiveItemRequestObject {
    private String player;
    private String material;
    private int amount;
    private String name;
    private List<String> lores;

    public GiveItemRequestObject() {
    }

    public GiveItemRequestObject(String player, String material, int amount, String name, List<String> lores) {
        this.player = player;
        this.material = material;
        this.amount = amount;
        this.name = name;
        this.lores = lores;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getLores() {
        return lores;
    }

    public void setLores(List<String> lores) {
        this.lores = lores;
    }

    @Override
    public String toString() {
        return "GiveItemRequestObject{" +
                "player='" + player + '\'' +
                ", material='" + material + '\'' +
                ", amount=" + amount +
                ", name='" + name + '\'' +
                ", lores=" + lores +
                '}';
    }
}
