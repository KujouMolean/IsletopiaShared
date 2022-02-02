package com.molean.isletopia.shared.platform;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.lang.reflect.Field;
import java.util.*;

public class VelocityRelatedUtils extends PlatformRelatedUtils {
    private static ProxyServer proxyServer;

    private static Object plugin = null;

    public static ProxyServer getProxyServer() {
        if (proxyServer != null) {
            return proxyServer;
        }
        Object plugin = getPlugin();
        ProxyServer proxyServer = null;
        try {
            Field proxyServerField = plugin.getClass().getDeclaredField("proxyServer");
            proxyServerField.setAccessible(true);
            proxyServer = (ProxyServer) proxyServerField.get(plugin);
        } catch (Exception e) {
            throw new RuntimeException("Isletopia Plugin does not contains proxyServerField!");
        }
        VelocityRelatedUtils.proxyServer = proxyServer;
        return proxyServer;
    }

    public static Object getPlugin() {
        if (plugin != null) {
            return plugin;
        }
        Collection<PluginContainer> plugins = proxyServer.getPluginManager().getPlugins();
        for (PluginContainer plugin : plugins) {
            Optional<String> name = plugin.getDescription().getName();
            if (name.isPresent() && name.get().startsWith("Isletopia")) {
                if (plugin.getInstance().isPresent()) {
                    VelocityRelatedUtils.plugin = plugin.getInstance().get();
                    return VelocityRelatedUtils.plugin;
                }
            }
        }
        throw new RuntimeException("Isletopia Plugin is not exist!");
    }

    @Override
    public void runAsync(Runnable runnable) {
        proxyServer.getScheduler().buildTask(getPlugin(), runnable);
    }

    @Override
    public Set<String> getIslandServers() {
        Collection<RegisteredServer> allServers = proxyServer.getAllServers();
        Set<String> serverList = new HashSet<>();
        for (RegisteredServer allServer : allServers) {
            String name = allServer.getServerInfo().getName();
            if (name.startsWith("server")) {
                serverList.add(name);
            }
        }
        return serverList;
    }

    @Override
    public Set<String> getAllServers() {
        Collection<RegisteredServer> allServers = proxyServer.getAllServers();
        Set<String> serverList = new HashSet<>();
        for (RegisteredServer allServer : allServers) {
            String name = allServer.getServerInfo().getName();
            serverList.add(name);
        }
        return serverList;
    }

    @Override
    public Map<UUID, String> getOnlinePlayers() {
        HashMap<UUID, String> uuidStringHashMap = new HashMap<>();
        for (Player player : proxyServer.getAllPlayers()) {
            uuidStringHashMap.put(player.getUniqueId(), player.getUsername());
        }
        return uuidStringHashMap;
    }

    @Override
    public Map<UUID, String> getPlayerServerMap() {
        return null;
    }

}
