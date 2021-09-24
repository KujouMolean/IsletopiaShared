package com.molean.isletopia.shared.platform;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BukkitRelatedUtils extends PlatformRelatedUtils {

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(getPlugin(), runnable);
    }

    @Override
    public List<String> getIslandServers() {
        return null;
    }

    @Override
    public List<String> getAllServers() {
        return null;
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
