package com.molean.isletopia.shared.platform;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public abstract class PlatformRelatedUtils {
    public abstract void runAsync(Runnable runnable);


    private static final String serverName = new File(System.getProperty("user.dir")).getName();

    public static String getServerName() {
        return serverName;
    }


    public abstract Set<String> getIslandServers();

    public abstract Set<String> getAllServers();

    public abstract Logger getLogger();

    public abstract Map<UUID, String> getOnlinePlayers();

    public abstract Map<UUID, String> getPlayerServerMap();

    public abstract Class<?> loadClass(String path) throws Exception;

    public abstract JarFile getJarFile() throws Exception;

    private static PlatformRelatedUtils instance;

    public static PlatformRelatedUtils getInstance() {
        if (instance == null) {
            try {
                Class.forName("org.bukkit.plugin.Plugin");
                instance = new BukkitRelatedUtils();
            } catch (ClassNotFoundException e) {
                instance = new VelocityRelatedUtils();
            }
        }
        return instance;
    }

}
