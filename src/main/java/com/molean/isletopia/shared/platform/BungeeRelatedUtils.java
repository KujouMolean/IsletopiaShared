package com.molean.isletopia.shared.platform;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.*;

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
    public Set<String> getIslandServers() {
        Set<String> stringArrayList = new HashSet<>();
        for (String s : ProxyServer.getInstance().getServers().keySet()) {
            if (s.startsWith("server")) {
                stringArrayList.add(s);
            }
        }
        return stringArrayList;
    }

    @Override
    public Set<String> getAllServers() {
        return new HashSet<>(ProxyServer.getInstance().getServers().keySet());
    }

    @Override
    public Map<UUID, String> getOnlinePlayers() {
        HashMap<UUID, String> uuidStringHashMap = new HashMap<>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            uuidStringHashMap.put(player.getUniqueId(), player.getName());
        }
        return uuidStringHashMap;
    }

    @Override
    public Map<UUID, String> getPlayerServerMap() {
        HashMap<UUID, String> uuidStringHashMap = new HashMap<>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            Server server = player.getServer();
            if (server != null) {
                uuidStringHashMap.put(player.getUniqueId(), server.getInfo().getName());
            }
        }
        return uuidStringHashMap;
    }

}
