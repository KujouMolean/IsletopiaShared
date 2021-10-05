package com.molean.isletopia.shared.utils;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import io.netty.util.AttributeKey;
import io.netty.util.ConstantPool;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

public class RedisUtils {

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection1;
    private static StatefulRedisConnection<byte[], byte[]> connection2;
    private static RedisCommands<String, String> redisCommand1;
    private static RedisCommands<byte[], byte[]> redisCommand2;

    public static RedisCommands<String, String> getCommand() {
        if (connection1 == null) {
            connection1 = getRedisClient().connect();
        }
        if (redisCommand1 == null) {
            redisCommand1 = connection1.sync();
        }
        return redisCommand1;
    }

    public static RedisCommands<byte[], byte[]> getByteCommand() {
        if (connection2 == null) {
            connection2 = getRedisClient().connect(ByteArrayCodec.INSTANCE);
        }
        if (redisCommand2 == null) {
            redisCommand2 = connection2.sync();
        }
        return redisCommand2;
    }

    public static RedisClient getRedisClient() {
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
        }
        return redisClient;
    }

    public static void destroy() {
        redisClient.shutdown();
    }
}
