package com.molean.isletopia.shared.platform;

import com.molean.isletopia.shared.message.ServerInfoUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

public class BukkitRelatedUtils extends PlatformRelatedUtils {

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), runnable);
    }

    @Override
    public Set<String> getIslandServers() {
        Set<String> servers = ServerInfoUpdater.getServers();
        servers.removeIf(s -> !s.startsWith("server"));
        return servers;
    }

    @Override
    public Set<String> getAllServers() {
        return ServerInfoUpdater.getServers();
    }

    @Override
    public Logger getLogger() {
        return getPlugin().getLogger();
    }

    @Override
    public Map<UUID, String> getOnlinePlayers() {
        return ServerInfoUpdater.getOnlinePlayersMap();
    }

    @Override
    public Map<UUID, String> getPlayerServerMap() {
        return ServerInfoUpdater.getUUIDServerMap();
    }


    public boolean isMainThread() {
        return Bukkit.isPrimaryThread();
    }

    private static JavaPlugin javaPlugin = null;

    public static JavaPlugin getPlugin() {
        if (javaPlugin != null) {
            return javaPlugin;
        }
        @NotNull Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        for (Plugin plugin : plugins) {
            if (plugin.getName().startsWith("Isletopia")) {
                javaPlugin = (JavaPlugin) plugin;
                return (JavaPlugin) plugin;
            }
        }
        throw new RuntimeException("Isletopia Plugin is not exist!");
    }
}
