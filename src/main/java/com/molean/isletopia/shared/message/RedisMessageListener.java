package com.molean.isletopia.shared.message;

import com.google.gson.Gson;
import com.molean.isletopia.shared.MessageHandler;
import com.molean.isletopia.shared.pojo.WrappedMessageObject;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;
import com.molean.isletopia.shared.utils.RedisUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class RedisMessageListener extends JedisPubSub {
    private static final Map<String, MessageHandler<?>> handlersMap = new HashMap<>();
    private static final Map<String, Type> messageTypeMap = new HashMap<>();
    private final String server = PlatformRelatedUtils.getServerName();

    private static RedisMessageListener redisMessageListener;
    private static boolean shouldStop = false;

    @SuppressWarnings("all")
    private RedisMessageListener() {
        PlatformRelatedUtils.getInstance().runAsync(() -> {
            while (true) {
                Jedis jedis = RedisUtils.getJedis();
                jedis.subscribe(this, "ServerMessage");
                jedis.close();
                if(shouldStop){
                    break;
                }
            }

        });
    }

    public static void init(){
        try {
            shouldStop = false;
            redisMessageListener = new RedisMessageListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void destroy(){
        try {
            shouldStop = true;
            redisMessageListener.unsubscribe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void setHandler(String type, MessageHandler<T> messageHandler, Class<?> messageClass) {
        handlersMap.put(type.toLowerCase(Locale.ROOT), messageHandler);
        messageTypeMap.put(type.toLowerCase(Locale.ROOT), messageClass);
    }

    @Override
    public void onMessage(String channel, String message) {
        WrappedMessageObject wrappedMessageObject = new Gson().fromJson(message, WrappedMessageObject.class);
        if (!wrappedMessageObject.getTo().equalsIgnoreCase(server)) {
            return;
        }
        String eventType = wrappedMessageObject.getSubChannel().toLowerCase(Locale.ROOT);
        MessageHandler<?> messageHandler = handlersMap.get(eventType);
        if (messageHandler != null) {
            String msg = wrappedMessageObject.getMessage();
            Gson gson = new Gson();
            Type messageType = messageTypeMap.get(eventType);
            try {
                if (!eventType.equalsIgnoreCase("PlayerInfo")) {
                    Logger.getAnonymousLogger().info("处理来自 " + wrappedMessageObject.getFrom() + " 的消息: "
                            + gson.fromJson(msg, messageType));
                }
                messageHandler.handle(wrappedMessageObject, gson.fromJson(msg, messageType));
            } catch (Exception e) {
                Logger.getAnonymousLogger().severe("处理来自 " + wrappedMessageObject.getFrom() + " 的 "
                        + eventType + " 失败.");
                e.printStackTrace();
            }
        }
    }
}
