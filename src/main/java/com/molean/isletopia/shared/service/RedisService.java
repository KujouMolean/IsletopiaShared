package com.molean.isletopia.shared.service;

import com.molean.isletopia.shared.annotations.Bean;
import com.molean.isletopia.shared.annotations.DisableTask;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.netty.util.AttributeKey;
import io.netty.util.ConstantPool;

import java.lang.reflect.Field;
import java.time.Duration;
import java.util.concurrent.ConcurrentMap;

@Bean
public class RedisService {

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection1;
    private static StatefulRedisConnection<byte[], byte[]> connection2;
    private static RedisCommands<String, String> redisCommand1;
    private static RedisCommands<byte[], byte[]> redisCommand2;


    public RedisCommands<String, String> getCommand() {
        if (connection1 == null) {
            connection1 = getRedisClient().connect();
            connection1.setTimeout(Duration.ofSeconds(3));
        }
        if (redisCommand1 == null) {
            redisCommand1 = connection1.sync();
            redisCommand1.setTimeout(Duration.ofSeconds(3));
        }
        return redisCommand1;
    }


    public RedisCommands<byte[], byte[]> getByteCommand() {
        if (connection2 == null) {
            connection2 = getRedisClient().connect(ByteArrayCodec.INSTANCE);
            connection2.setTimeout(Duration.ofSeconds(3));
        }
        if (redisCommand2 == null) {
            redisCommand2 = connection2.sync();
            redisCommand2.setTimeout(Duration.ofSeconds(3));
        }
        return redisCommand2;
    }

    @SuppressWarnings("all")
    @Bean
    public RedisClient getRedisClient() {
        if (AttributeKey.exists("RedisURI")) {
            try {
                Field poolField = AttributeKey.class.getDeclaredField("pool");
                poolField.setAccessible(true);
                ConstantPool<AttributeKey<Object>> pool = (ConstantPool<AttributeKey<Object>>) poolField.get(null);
                Field constantsField = ConstantPool.class.getDeclaredField("constants");
                constantsField.setAccessible(true);
                ConcurrentMap<String, Object> constants = (ConcurrentMap<String, Object>) constantsField.get(pool);
                constants.remove("RedisURI");
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (redisClient == null) {
            redisClient = RedisClient.create("redis://localhost");
            redisClient.setDefaultTimeout(Duration.ofSeconds(3));
        }
        return redisClient;
    }


    @DisableTask
    public void destroy() {
        redisClient.shutdown();
    }
}
