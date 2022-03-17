package com.molean.isletopia.shared.model;

import java.time.LocalDateTime;

public class BumpInfo {
    private final String username;
    private final LocalDateTime dateTime;
    private final int uid;

    public BumpInfo(int uid, String username, LocalDateTime dateTime) {
        this.uid = uid;
        this.username = username;
        this.dateTime = dateTime;
    }

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return "BumpInfo{" +
                "username='" + username + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
