package com.molean.isletopia.shared.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClubAchievement {
    private String club;
    private String achievement;
    private LocalDateTime localDateTime;

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
