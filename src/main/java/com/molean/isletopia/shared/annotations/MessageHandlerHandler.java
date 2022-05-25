package com.molean.isletopia.shared.annotations;

import com.molean.isletopia.shared.MessageHandler;
import com.molean.isletopia.shared.message.RedisMessageListener;

@BeanHandlerPriority(0)
public class MessageHandlerHandler implements BeanHandler {
    private final RedisMessageListener redisMessageListener;
    public MessageHandlerHandler(RedisMessageListener redisMessageListener) {
        this.redisMessageListener = redisMessageListener;
    }

    @Override
    public void handle(Object object) {
        if (object instanceof MessageHandler<?> messageHandler) {
            if (object.getClass().isAnnotationPresent(MessageHandlerType.class)) {
                MessageHandlerType annotation = object.getClass().getAnnotation(MessageHandlerType.class);
                Class<?> value = annotation.value();
                redisMessageListener.setHandler(value.getName(), messageHandler, value);
            }

        }
    }
}
