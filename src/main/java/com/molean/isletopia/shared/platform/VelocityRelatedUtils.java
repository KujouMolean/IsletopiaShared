package com.molean.isletopia.shared.platform;

import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class VelocityRelatedUtils extends PlatformRelatedUtils {
    public static ProxyServer proxyServer;
    public static Logger logger;


    public static ProxyServer getProxyServer() {
        return proxyServer;
    }

    public static Object getPlugin() {
        Optional<PluginContainer> pluginContainer = proxyServer.getPluginManager().getPlugin("isletopia_velocity");
        if (pluginContainer.isEmpty()) {
            throw new RuntimeException();
        }
        Optional<?> instance = pluginContainer.get().getInstance();
        if (instance.isEmpty()) {
            throw new RuntimeException();
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
    public Logger getLogger() {
        return logger;
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

    private Method findClassMethodCache = null;

    @Override
    public Class<?> loadClass(String path) throws Exception {
        return  getClassLoader().loadClass(path);
    }

    private static ClassLoader classLoader;

    public ClassLoader getClassLoader() {
        if (classLoader == null) {
            classLoader = getPlugin().getClass().getClassLoader();
        }
        return classLoader;
    }

    @Override
    public JarFile getJarFile() throws Exception {
        String path = new File(VelocityRelatedUtils.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
        return new JarFile(path);
    }

}
