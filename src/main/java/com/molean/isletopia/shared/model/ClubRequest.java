package com.molean.isletopia.shared.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClubRequest {

    public enum Result {
        ACCEPT, REFUSED, TODO, DONE,
    }

    private int id;
    private String club;
    private UUID uuid;
    private LocalDateTime localDateTime;
    private Result result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
