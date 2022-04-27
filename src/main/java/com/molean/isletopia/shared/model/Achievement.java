package com.molean.isletopia.shared.model;

public class Achievement {
    private String name;
    private String display;
    private String description;
    private String icon;
    private String access;
    private int score;

    @Override
    public String toString() {
        return """
                §f%s
                §7%s
                                
                §7获取方式: %s
                §7成就积分: %d
                """.formatted(display, description, access, score);
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

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}
