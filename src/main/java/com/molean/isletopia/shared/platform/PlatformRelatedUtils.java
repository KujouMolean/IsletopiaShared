package com.molean.isletopia.shared.platform;

import java.io.File;
import java.util.List;

public abstract class PlatformRelatedUtils {
    public abstract void runAsync(Runnable runnable);

    private static final String serverName = new File(System.getProperty("user.dir")).getName();

    public static String getServerName() {
        return serverName;
    }


    public abstract List<String> getIslandServers();

    public abstract List<String> getAllServers();

    private static PlatformRelatedUtils instance;

    public static PlatformRelatedUtils getInstance() {
        if(instance==null){
            if(getServerName().equalsIgnoreCase("waterfall")){
                instance= new BungeeRelatedUtils();
            }else{
                instance= new BukkitRelatedUtils();
            }
        }
        return instance;
    }
}
