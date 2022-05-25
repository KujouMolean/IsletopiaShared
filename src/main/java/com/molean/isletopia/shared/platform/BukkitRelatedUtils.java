package com.molean.isletopia.shared.platform;

import com.molean.isletopia.shared.message.ServerInfoUpdater;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

public class BukkitRelatedUtils extends PlatformRelatedUtils {


    private boolean shutdown = false;


    public void setShutdown(boolean shutdown) {
        this.shutdown= shutdown;
    }

    public boolean isShutdown() {
        return shutdown;
    }

    public static void setJavaPlugin(JavaPlugin javaPlugin) {
        BukkitRelatedUtils.javaPlugin = javaPlugin;
    }

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



    public static JavaPlugin getJavaPlugin() {
        return javaPlugin;
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


    private static Method findClassMethodCache = null;

    private static ClassLoader classLoader = null;

    public ClassLoader getClassLoader() {
        if (classLoader == null) {
            classLoader = getPlugin().getClass().getClassLoader();
        }
        return classLoader;
    }


    @Override
    public Class<?> loadClass(String path) throws Exception {
        if (findClassMethodCache == null) {
            findClassMethodCache = PluginClassLoader.class.getDeclaredMethod("findClass", String.class);
            findClassMethodCache.setAccessible(true);
        }
        return (Class<?>) findClassMethodCache.invoke(getClassLoader(), path);
    }
//    @Override
//    public JarFile getJarFile() throws Exception {
//        Field jarField = PluginClassLoader.class.getDeclaredField("jar");
//        jarField.setAccessible(true);
//        return (JarFile) jarField.get(classLoader);
//    }


    @Override
    public JarFile getJarFile() throws Exception {
        String path = new File(VelocityRelatedUtils.class.getProtectionDomain().getCodeSource().getLocation()
                .toURI()).getPath();
        return new JarFile(path);
    }

    public boolean isMainThread() {
        return Bukkit.isPrimaryThread() && !(shutdown);
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

    public static BukkitRelatedUtils getInstance() {
        return (BukkitRelatedUtils) PlatformRelatedUtils.getInstance();
    }
}
