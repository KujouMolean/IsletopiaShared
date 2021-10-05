package com.molean.isletopia.shared.message;

import com.google.gson.Gson;
import com.molean.isletopia.shared.MessageHandler;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;
import com.molean.isletopia.shared.pojo.WrappedMessageObject;
import com.molean.isletopia.shared.utils.RedisUtils;
import io.lettuce.core.ConnectionBuilder;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;
import io.netty.util.AttributeKey;
import io.netty.util.ConstantPool;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

public class RedisMessageListener extends RedisPubSubAdapter<String, String> {
    private static final Map<String, MessageHandler<?>> handlersMap = new HashMap<>();
    private static final Map<String, Type> messageTypeMap = new HashMap<>();
    private final String server = PlatformRelatedUtils.getServerName();
    private static StatefulRedisPubSubConnection<String, String> pubSubConnection;
    private static RedisMessageListener redisMessageListener;


    @SuppressWarnings("all")
    private RedisMessageListener() {


        pubSubConnection = RedisUtils.getRedisClient().connectPubSub();
        pubSubConnection.addListener(this);
        RedisPubSubCommands<String, String> sync = pubSubConnection.sync();
        sync.subscribe("ServerMessage");

    }

    public static void init() {
        if (redisMessageListener == null) {
            redisMessageListener = new RedisMessageListener();
        }
    }

    public static void destroy() {
        pubSubConnection.close();

    }

    public static <T> void setHandler(String type, MessageHandler<T> messageHandler, Class<?> messageClass) {
        handlersMap.put(type.toLowerCase(Locale.ROOT), messageHandler);
        messageTypeMap.put(type.toLowerCase(Locale.ROOT), messageClass);
    }


    @Override
    public void message(String channel, String message) {
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
