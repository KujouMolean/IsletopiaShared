package com.molean.isletopia.shared.model;

import java.time.LocalDateTime;

public class BumpInfo {
    private final String username;
    private final LocalDateTime dateTime;

    public BumpInfo(int uid, String username, LocalDateTime dateTime) {
        this.username = username;
        this.dateTime = dateTime;
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
