package com.molean.isletopia.shared.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Island {
    protected final int id;
    protected final int x;
    protected final int z;
    @NotNull
    protected final String server;
    protected double spawnX;
    protected double spawnY;
    protected double spawnZ;
    protected float yaw;
    protected float pitch;
    @NotNull
    protected UUID uuid;
    protected String name;
    protected String biome;
    @NotNull
    protected Timestamp creation;
    protected HashSet<UUID> members;
    protected Set<String> islandFlags;
    @NotNull
    protected String icon;


    public Island(Island island) {
        this.id = island.id;
        this.x = island.x;
        this.z = island.z;
        this.server = island.server;
        this.spawnX = island.spawnX;
        this.spawnY = island.spawnY;
        this.spawnZ = island.spawnZ;
        this.yaw = island.yaw;
        this.pitch = island.pitch;
        this.name = island.name;
        this.uuid = island.uuid;
        this.biome = island.biome;
        this.creation = island.creation;
        this.members = new HashSet<>(island.members);
        this.islandFlags = new HashSet<>(island.islandFlags);
        this.icon = island.icon;
    }

    public Island(int id, int x, int z, double spawnX, double spawnY, double spawnZ, float yaw, float pitch, @NotNull String server, @NotNull UUID uuid, @Nullable String name, @Nullable String biome, @NotNull Timestamp creation, Set<UUID> members, Set<String> islandFlags, String icon) {
        this.id = id;
        this.x = x;
        this.z = z;
        this.server = server;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.name = name;
        this.uuid = uuid;
        this.biome = biome;
        this.creation = creation;
        this.members = new HashSet<>(members);
        this.islandFlags = new HashSet<>(islandFlags);
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public @NotNull String getServer() {
        return server;
    }

    public IslandId getIslandId() {
        return new IslandId(server, x, z);
    }

    public double getSpawnX() {
        return spawnX;
    }

    public double getSpawnY() {
        return spawnY;
    }

    public double getSpawnZ() {
        return spawnZ;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getBiome() {
        return biome;
    }

    public @NotNull Timestamp getCreation() {
        return creation;
    }

    public @NotNull Set<UUID> getMembers() {
        return new HashSet<>(members);
    }

    public @NotNull Set<String> getIslandFlags() {
        return new HashSet<>(islandFlags);
    }

    public boolean containsFlag(String key) {
        for (String islandFlag : islandFlags) {
            String[] split = islandFlag.split("#");
            if (split[0].equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Island island = (Island) o;

        if (x != island.x) return false;
        if (z != island.z) return false;
        return server.equals(island.server);
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + z;
        result = 31 * result + server.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Island{" +
                "id=" + id +
                ", x=" + x +
                ", z=" + z +
                ", server='" + server + '\'' +
                '}';
    }

    public @NotNull String getIcon() {
        return icon;
    }


}
