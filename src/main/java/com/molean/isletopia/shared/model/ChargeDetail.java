package com.molean.isletopia.shared.model;

import java.time.LocalDateTime;

//pojo 记录岛屿详细的数据
public class ChargeDetail {
    private long dispenser = 0;
    private long redstone = 0;
    private long piston = 0;
    private long tnt = 0;
    private long furnace = 0;
    private long hopper = 0;
    private long vehicle = 0;
    private long water = 0;
    private long food = 0;

    private long otherPowerUsage = 0;

    private int powerChargeTimes = 0;
    private int foodChargeTimes = 0;

    private int powerProduceTimes = 0;
    private int foodProduceTimes = 0;

    private int onlineMinutes = 0;

    private LocalDateTime startTime = LocalDateTime.now();
    private LocalDateTime lastCommitTime = LocalDateTime.now();


    public long getDispenser() {
        return dispenser;
    }

    public void setDispenser(long dispenser) {
        this.dispenser = dispenser;
    }

    public long getRedstone() {
        return redstone;
    }

    public void setRedstone(long redstone) {
        this.redstone = redstone;
    }

    public long getPiston() {
        return piston;
    }

    public void setPiston(long piston) {
        this.piston = piston;
    }

    public long getTnt() {
        return tnt;
    }

    public void setTnt(long tnt) {
        this.tnt = tnt;
    }

    public long getFurnace() {
        return furnace;
    }

    public void setFurnace(long furnace) {
        this.furnace = furnace;
    }

    public long getHopper() {
        return hopper;
    }

    public void setHopper(long hopper) {
        this.hopper = hopper;
    }

    public long getVehicle() {
        return vehicle;
    }

    public void setVehicle(long vehicle) {
        this.vehicle = vehicle;
    }

    public long getOtherPowerUsage() {
        return otherPowerUsage;
    }

    public void setOtherPowerUsage(long otherPowerUsage) {
        this.otherPowerUsage = otherPowerUsage;
    }

    public long getWater() {
        return water;
    }

    public void setWater(long water) {
        this.water = water;
    }

    public long getFood() {
        return food;
    }

    public void setFood(long food) {
        this.food = food;
    }

    public int getPowerChargeTimes() {
        return powerChargeTimes;
    }

    public void setPowerChargeTimes(int powerChargeTimes) {
        this.powerChargeTimes = powerChargeTimes;
    }

    public int getFoodChargeTimes() {
        return foodChargeTimes;
    }

    public void setFoodChargeTimes(int foodChargeTimes) {
        this.foodChargeTimes = foodChargeTimes;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getLastCommitTime() {
        return lastCommitTime;
    }

    public void setLastCommitTime(LocalDateTime lastCommitTime) {
        this.lastCommitTime = lastCommitTime;
    }

    public int getPowerProduceTimes() {
        return powerProduceTimes;
    }

    public void setPowerProduceTimes(int powerProduceTimes) {
        this.powerProduceTimes = powerProduceTimes;
    }

    public int getFoodProduceTimes() {
        return foodProduceTimes;
    }

    public void setFoodProduceTimes(int foodProduceTimes) {
        this.foodProduceTimes = foodProduceTimes;
    }

    public int getOnlineMinutes() {
        return onlineMinutes;
    }

    public void setOnlineMinutes(int onlineMinutes) {
        this.onlineMinutes = onlineMinutes;
    }
}
