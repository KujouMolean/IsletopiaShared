package com.molean.isletopia.shared.pojo.obj;

public class PlaySoundObject {
    private String player;
    private String soundName;

    public PlaySoundObject() {
    }

    public PlaySoundObject(String player, String soundName) {
        this.player = player;
        this.soundName = soundName;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    @Override
    public String toString() {
        return "PlaySoundObject{" +
                "player='" + player + '\'' +
                ", soundName='" + soundName + '\'' +
                '}';
    }
}
