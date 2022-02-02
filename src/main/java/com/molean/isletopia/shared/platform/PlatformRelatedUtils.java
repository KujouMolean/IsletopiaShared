package com.molean.isletopia.shared.platform;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class PlatformRelatedUtils {
    public abstract void runAsync(Runnable runnable);

    private static final String serverName = new File(System.getProperty("user.dir")).getName();

    public static String getServerName() {
        return serverName;
    }


    public abstract Set<String> getIslandServers();

    public abstract Set<String> getAllServers();

    public abstract Map<UUID, String> getOnlinePlayers();

    public abstract Map<UUID, String> getPlayerServerMap();


    private static PlatformRelatedUtils instance;

    public static PlatformRelatedUtils getInstance() {
        if(instance==null){
            if(getServerName().equalsIgnoreCase("waterfall")){
                instance= new BungeeRelatedUtils();
            } else if(getServerName().equalsIgnoreCase("velocity")) {
                instance = new VelocityRelatedUtils();
            } else {
                instance = new BukkitRelatedUtils();
            }
        }
        return instance;
    }
}
