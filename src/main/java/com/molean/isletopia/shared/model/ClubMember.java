package com.molean.isletopia.shared.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ClubMember {
    public enum Role {
        LEADER, OPERATOR, MEMBER,
    }

    private UUID uuid;
    private LocalDateTime time;
    private Role role;

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "ClubMember{" +
                "uuid=" + uuid +
                ", time=" + time +
                ", role=" + role +
                '}';
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }


    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
