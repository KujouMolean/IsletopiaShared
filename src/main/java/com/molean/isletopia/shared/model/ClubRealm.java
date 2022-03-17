package com.molean.isletopia.shared.model;

import java.util.Set;
import java.util.UUID;

public class ClubRealm {
    private String icon;
    private String club;
    private String title;
    private String description;
    private String requirement;
    private String rule;
    private Set<String> options;
    private Set<UUID> whitelist;

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getOptions() {
        return options;
    }

    public void setOptions(Set<String> options) {
        this.options = options;
    }

    public Set<UUID> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(Set<UUID> whitelist) {
        this.whitelist = whitelist;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public String toString() {
        return "ClubRealm{" +
                "icon='" + icon + '\'' +
                ", club='" + club + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", requirement='" + requirement + '\'' +
                ", rule='" + rule + '\'' +
                ", options=" + options +
                ", whitelist=" + whitelist +
                '}';
    }
}
