package com.molean.isletopia.shared.platform;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class BungeeRelatedUtils extends PlatformRelatedUtils {
    private static Plugin javaPlugin = null;

    public static Plugin getPlugin() {
        if (javaPlugin != null) {
            return javaPlugin;
        }
        Collection<Plugin> plugins = ProxyServer.getInstance().getPluginManager().getPlugins();

        for (Plugin plugin : plugins) {
            if (plugin.getFile().getName().toLowerCase(Locale.ROOT).contains("isletopia")) {
                javaPlugin = plugin;
                return plugin;
            }
        }
        throw new RuntimeException("Isletopia Plugin is not exist!");
    }

    @Override
    public void runAsync(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(getPlugin(), runnable);
    }


    @Override
    public List<String> getIslandServers() {
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (String s : ProxyServer.getInstance().getServersCopy().keySet()) {
            if (s.startsWith("server")) {
                stringArrayList.add(s);
            }
        }
        return stringArrayList;
    }

    @Override
    public List<String> getAllServers() {
        return new ArrayList<>(ProxyServer.getInstance().getServersCopy().keySet());
    }

}
