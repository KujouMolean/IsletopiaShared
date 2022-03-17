package com.molean.isletopia.shared.message;

import com.google.gson.Gson;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;
import com.molean.isletopia.shared.pojo.WrappedMessageObject;
import com.molean.isletopia.shared.pojo.req.SwitchServerRequest;
import com.molean.isletopia.shared.utils.RedisUtils;

import java.util.logging.Logger;

public class ServerMessageUtils {


    public static void sendMessage(String target, String channel, Object object) {
        Logger.getLogger("").info("向 " + target + " 发送消息: " + new Gson().toJson(object));
        WrappedMessageObject wrappedMessageObject = new WrappedMessageObject();
        wrappedMessageObject.setMessage(new Gson().toJson(object));
        wrappedMessageObject.setFrom(PlatformRelatedUtils.getServerName());
        wrappedMessageObject.setTo(target);
        wrappedMessageObject.setSubChannel(channel);
        wrappedMessageObject.setTime(System.currentTimeMillis());
        PlatformRelatedUtils.getInstance().runAsync(() -> {
            RedisUtils.getCommand().publish("ServerMessage", new Gson().toJson(wrappedMessageObject));
        });

    }

    public static void switchServer(String player, String server) {
        SwitchServerRequest switchServerRequest = new SwitchServerRequest(player, server);
        sendMessage("proxy", "SwitchServer", switchServerRequest);
    }


    public static void sendServerBungeeMessage(String target, String channel, Object object) {
        if (!channel.equalsIgnoreCase("PlayerInfo")) {
            Logger.getLogger("").info("发送 Bukkit 消息: " + object);
        }
        WrappedMessageObject wrappedMessageObject = new WrappedMessageObject();
        wrappedMessageObject.setMessage(new Gson().toJson(object));
        wrappedMessageObject.setFrom("proxy");
        wrappedMessageObject.setTo(target);
        wrappedMessageObject.setSubChannel(channel);
        wrappedMessageObject.setTime(System.currentTimeMillis());
        PlatformRelatedUtils.getInstance().runAsync(() -> {
            RedisUtils.getCommand().publish("ServerMessage", new Gson().toJson(wrappedMessageObject));
        });
    }

    public static void broadcastBungeeMessage(String channel, Object object) {
        for (String server : PlatformRelatedUtils.getInstance().getAllServers()) {
            sendServerBungeeMessage(server, channel, object);
        }
    }
}
