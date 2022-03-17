package com.molean.isletopia.shared.platform;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.lang.reflect.Field;
import java.util.*;

public class VelocityRelatedUtils extends PlatformRelatedUtils {
    public static ProxyServer proxyServer;


    public static ProxyServer getProxyServer() {
        return proxyServer;
    }

    public static Object getPlugin() {
        Optional<PluginContainer> pluginContainer = proxyServer.getPluginManager().getPlugin("isletopia_velocity");
        if (pluginContainer.isEmpty()) {
            return null;
        }
        Optional<?> instance = pluginContainer.get().getInstance();
        if (instance.isEmpty()) {
            return null;
        }
        return instance.get();
    }

    @Override
    public void runAsync(Runnable runnable) {

        proxyServer.getScheduler().buildTask(getPlugin(), runnable).schedule();
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
        HashMap<UUID, String> uuidStringHashMap = new HashMap<>();
        for (RegisteredServer registeredServer : proxyServer.getAllServers()) {
            String name = registeredServer.getServerInfo().getName();
            for (Player player : registeredServer.getPlayersConnected()) {
                uuidStringHashMap.put(player.getUniqueId(), name);
            }
        }
        return uuidStringHashMap;
    }

}
