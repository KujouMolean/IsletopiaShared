package com.molean.isletopia.shared.message;

import com.google.gson.Gson;
import com.molean.isletopia.shared.MessageHandler;
import com.molean.isletopia.shared.annotations.DisableTask;
import com.molean.isletopia.shared.annotations.Singleton;
import com.molean.isletopia.shared.platform.PlatformRelatedUtils;
import com.molean.isletopia.shared.pojo.WrappedMessageObject;
import com.molean.isletopia.shared.service.RedisService;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Singleton
public class RedisMessageListener extends RedisPubSubAdapter<String, String> {
    private static final Map<String, MessageHandler<?>> handlersMap = new HashMap<>();
    private static final Map<String, Type> messageTypeMap = new HashMap<>();
    private final String server = PlatformRelatedUtils.getServerName();
    private static StatefulRedisPubSubConnection<String, String> pubSubConnection;


    public RedisMessageListener(RedisService redisService) {
        pubSubConnection = redisService.getRedisClient().connectPubSub();
        pubSubConnection.addListener(this);
        RedisPubSubCommands<String, String> sync = pubSubConnection.sync();
        sync.subscribe("ServerMessage");
    }


    @DisableTask
    public void destroy() {
        pubSubConnection.close();
    }

    public <T> void setHandler(String typeName, MessageHandler<T> messageHandler, Type messageClass) {
        handlersMap.put(typeName.toLowerCase(Locale.ROOT), messageHandler);
        messageTypeMap.put(typeName.toLowerCase(Locale.ROOT), messageClass);
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
                messageHandler.handle(wrappedMessageObject, gson.fromJson(msg, messageType));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
