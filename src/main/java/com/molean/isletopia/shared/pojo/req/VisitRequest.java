package com.molean.isletopia.shared.pojo.req;

import com.molean.isletopia.shared.model.IslandId;

import java.util.UUID;

public class VisitRequest {
    private UUID sourcePlayer;
    private IslandId islandId;

    public VisitRequest() {
    }

    public VisitRequest(UUID sourcePlayer, IslandId islandId) {
        this.sourcePlayer = sourcePlayer;
        this.islandId = islandId;
    }

    public UUID getSourcePlayer() {
        return sourcePlayer;
    }

    public void setSourcePlayer(UUID sourcePlayer) {
        this.sourcePlayer = sourcePlayer;
    }

    public IslandId getIslandId() {
        return islandId;
    }

    public void setIslandId(IslandId islandId) {
        this.islandId = islandId;
    }

    @Override
    public String toString() {
        return "VisitRequest{" +
                "sourcePlayer='" + sourcePlayer.toString() + '\'' +
                ", islandId=" + islandId +
                '}';
    }
}
