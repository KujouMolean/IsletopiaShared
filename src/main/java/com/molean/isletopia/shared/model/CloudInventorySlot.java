package com.molean.isletopia.shared.model;

import java.util.UUID;

public class CloudInventorySlot {
    private UUID owner;
    private String material;
    private int amount;

    public CloudInventorySlot() {

    }
    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
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
}
