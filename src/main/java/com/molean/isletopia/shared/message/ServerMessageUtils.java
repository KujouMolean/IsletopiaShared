package com.molean.isletopia.shared.message;

import com.google.gson.Gson;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;
import com.molean.isletopia.shared.pojo.WrappedMessageObject;
import com.molean.isletopia.shared.pojo.obj.ChatObject;
import com.molean.isletopia.shared.pojo.obj.IgnoreObject;
import com.molean.isletopia.shared.pojo.req.SwitchServerRequest;
import com.molean.isletopia.shared.utils.RedisUtils;

import java.util.List;
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
        RedisUtils.getCommand().publish("ServerMessage", new Gson().toJson(wrappedMessageObject));

    }

    public static void switchServer(String player, String server) {
        SwitchServerRequest switchServerRequest = new SwitchServerRequest(player, server);
        sendMessage("waterfall", "SwitchServer", switchServerRequest);
    }


    public static void universalChat(String player, String message) {
        ChatObject chatObject = new ChatObject(player, message);
        sendMessage("waterfall", "Chat", chatObject);
    }

    public static void updateIgnores(String player, List<String> ignores) {
        IgnoreObject ignoreObject = new IgnoreObject(player, ignores);
        sendMessage("waterfall", "Ignore", ignoreObject);
    }

    public static void sendServerBungeeMessage(String target, String channel, Object object) {
        if (!channel.equalsIgnoreCase("PlayerInfo")) {
            Logger.getLogger("").info("发送 Bukkit 消息: " + object);
        }
        WrappedMessageObject wrappedMessageObject = new WrappedMessageObject();
        wrappedMessageObject.setMessage(new Gson().toJson(object));
        wrappedMessageObject.setFrom("waterfall");
        wrappedMessageObject.setTo(target);
        wrappedMessageObject.setSubChannel(channel);
        wrappedMessageObject.setTime(System.currentTimeMillis());
        RedisUtils.getCommand().publish("ServerMessage", new Gson().toJson(wrappedMessageObject));
    }

    public static void broadcastBungeeMessage(String channel, Object object) {
        for (String server : PlatformRelatedUtils.getInstance().getAllServers()) {
            sendServerBungeeMessage(server, channel, object);
        }
    }
}
