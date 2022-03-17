package com.molean.isletopia.shared.model;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public class Club {

    private String name;
    private String display;
    private String description;
    private String icon;
    private LocalDateTime creation;
    private Map<UUID, ClubMember> members;
    @Nullable
    private ClubRealm clubRealm;


    @Override
    public String toString() {
        return """
                %s
                %s
                                
                创建日期: %s
                """.formatted(display, description, creation.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public LocalDateTime getCreation() {
        return creation;
    }

    public void setCreation(LocalDateTime creation) {
        this.creation = creation;
    }

    public Map<UUID, ClubMember> getMembers() {
        return members;
    }

    public void setMembers(Map<UUID, ClubMember> members) {
        this.members = members;
    }

    public @Nullable ClubRealm getClubRealm() {
        return clubRealm;
    }

    public void setClubRealm(@Nullable ClubRealm clubRealm) {
        this.clubRealm = clubRealm;
    }
}
