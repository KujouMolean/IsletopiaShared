package com.molean.isletopia.shared.message;

import com.google.gson.Gson;
import com.molean.isletopia.shared.annotations.AutoInject;
import com.molean.isletopia.shared.annotations.Bean;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;
import com.molean.isletopia.shared.pojo.WrappedMessageObject;
import com.molean.isletopia.shared.pojo.req.SwitchServerRequest;
import com.molean.isletopia.shared.service.RedisService;

import java.util.logging.Logger;

@Bean
public class ServerMessageService {

    @AutoInject
    private RedisService redisService;

    public  void sendMessage(String target, Object object) {
        Logger.getLogger("").info("向 " + target + " 发送消息: " + new Gson().toJson(object));
        WrappedMessageObject wrappedMessageObject = new WrappedMessageObject();
        wrappedMessageObject.setMessage(new Gson().toJson(object));
        wrappedMessageObject.setFrom(PlatformRelatedUtils.getServerName());
        wrappedMessageObject.setTo(target);
        wrappedMessageObject.setSubChannel(object.getClass().getName());
        wrappedMessageObject.setTime(System.currentTimeMillis());
        PlatformRelatedUtils.getInstance().runAsync(() -> {
            redisService.getCommand().publish("ServerMessage", new Gson().toJson(wrappedMessageObject));
        });

    }

    public  void switchServer(String player, String server) {
        SwitchServerRequest switchServerRequest = new SwitchServerRequest(player, server);
        sendMessage("proxy", switchServerRequest);
    }


    public  void sendServerBungeeMessage(String target, Object object) {
        WrappedMessageObject wrappedMessageObject = new WrappedMessageObject();
        wrappedMessageObject.setMessage(new Gson().toJson(object));
        wrappedMessageObject.setFrom("proxy");
        wrappedMessageObject.setTo(target);
        wrappedMessageObject.setSubChannel(object.getClass().getName());
        wrappedMessageObject.setTime(System.currentTimeMillis());
        PlatformRelatedUtils.getInstance().runAsync(() -> {
            redisService.getCommand().publish("ServerMessage", new Gson().toJson(wrappedMessageObject));
        });
    }

    public  void broadcastBungeeMessage(Object object) {
        for (String server : PlatformRelatedUtils.getInstance().getAllServers()) {
            sendServerBungeeMessage(server, object);
        }
    }
}
