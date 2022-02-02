package com.molean.isletopia.shared.message;

import com.molean.isletopia.shared.MessageHandler;
import com.molean.isletopia.shared.pojo.WrappedMessageObject;
import com.molean.isletopia.shared.pojo.obj.PlayerInfoObject;

import java.io.File;
import java.util.*;

public class ServerInfoUpdater implements MessageHandler<PlayerInfoObject> {


    private static final String serverName = new File(System.getProperty("user.dir")).getName();

    public static String getServerName() {
        return serverName;
    }

    private static Map<UUID, String> onlinePlayers = new HashMap<>();

    public static List<String> getOnlinePlayers() {
        return new ArrayList<>(onlinePlayers.values());
    }

    public static Set<UUID> getOnlinePlayersUUID() {
        return new HashSet<>(onlinePlayers.keySet());
    }

    public static Map<UUID, String> getOnlinePlayersMap() {
        return onlinePlayers;
    }

    private static final List<String> servers = new ArrayList<>();

    public static Set<String> getServers() {
        return new HashSet<>(servers);
    }


    private static final Map<String, String> playerServerMap = new HashMap<>();
    private static final Map<UUID, String> uuidServerMap = new HashMap<>();

    public static Map<String, String> getPlayerServerMap() {
        return playerServerMap;
    }
    public static Map<UUID, String> getUUIDServerMap() {
        return uuidServerMap;
    }


    public ServerInfoUpdater() {
        RedisMessageListener.setHandler("PlayerInfo", this, PlayerInfoObject.class);
    }

    @Override
    public void handle(WrappedMessageObject wrappedMessageObject, PlayerInfoObject message) {
        Map<UUID, String> players = message.getPlayers();
        Map<String, Map<UUID, String>> playersPerServer = message.getPlayersPerServer();
        onlinePlayers.clear();
        onlinePlayers = new HashMap<>(players);
        playerServerMap.clear();
        uuidServerMap.clear();
        for (String server : playersPerServer.keySet()) {
            Map<UUID, String> map = playersPerServer.get(server);
            for (String player : map.values()) {
                playerServerMap.put(player, server);
            }
            for (UUID uuid : map.keySet()) {
                uuidServerMap.put(uuid, server);
            }
        }
        servers.clear();
        servers.addAll(new ArrayList<>(playersPerServer.keySet()));

    }
}
