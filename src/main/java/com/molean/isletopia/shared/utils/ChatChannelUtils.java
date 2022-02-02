package com.molean.isletopia.shared.utils;

import com.molean.isletopia.shared.platform.PlatformRelatedUtils;

import java.util.*;

public class ChatChannelUtils {

    public static final List<String> availableChannels = List.of("黑", "深蓝", "深绿", "湖蓝", "深红", "紫", "金", "灰", "深灰", "蓝", "绿", "天蓝", "红", "粉红", "黄", "白");

    public static Set<String> getChannels(UUID uuid) {
        String s = RedisUtils.getCommand().get("Channel:" + uuid);
        if (s == null || s.isEmpty()) {
            s = "白";
        }
        return new HashSet<>(Arrays.asList(s.split(",")));
    }
    public static void setChannel(UUID uuid,Set<String> channels) {
        RedisUtils.getCommand().set("Channel:" + uuid, String.join(",", channels));
    }

    public static void addChannel(UUID uuid, String channel) {
        Set<String> channels = getChannels(uuid);
        channels.add(channel);
        setChannel(uuid, channels);
    }

    public static void removeChannel(UUID uuid, String channel) {
        Set<String> channels = getChannels(uuid);
        channels.remove(channel);
        setChannel(uuid, channels);
    }

    public static String getChannelColor(String channel) {
        if (availableChannels.contains(channel)) {
            return String.format("§%x", availableChannels.indexOf(channel));
        }
        return "§f";
    }

    public static Collection<UUID> getPlayersInChannel(String channel) {
        ArrayList<UUID> uuids = new ArrayList<>();
        Map<UUID, String> playerServerMap = PlatformRelatedUtils.getInstance().getPlayerServerMap();
        for (UUID uuid : PlatformRelatedUtils.getInstance().getOnlinePlayers().keySet()) {
            String s = playerServerMap.get(uuid);
            if (s == null || s.startsWith("club_")) {
                continue;
            }
            Set<String> channels = getChannels(uuid);
            if (channels.contains(channel)) {
                uuids.add(uuid);
            }
        }
        return uuids;
    }
}
