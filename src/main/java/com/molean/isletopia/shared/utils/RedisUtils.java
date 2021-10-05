package com.molean.isletopia.shared.utils;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import io.netty.util.ConstantPool;

public class RedisUtils {

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection1;
    private static StatefulRedisConnection<byte[], byte[]> connection2;
    private static RedisCommands<String, String> redisCommand1;
    private static RedisCommands<byte[], byte[]> redisCommand2;

    public static RedisCommands<String, String> getCommand() {
        if (redisClient == null) {
            redisClient = RedisClient.create("redis://localhost");
        }
        if (connection1 == null) {
            connection1 = redisClient.connect();
        }
        if (redisCommand1 == null) {
            redisCommand1 = connection1.sync();
        }
        return redisCommand1;
    }

    public static RedisCommands<byte[], byte[]> getByteCommand() {
        if (redisClient == null) {
            redisClient = RedisClient.create("redis://localhost");
        }
        if (connection2 == null) {
            connection2 = redisClient.connect(ByteArrayCodec.INSTANCE);
        }
        if (redisCommand2 == null) {
            redisCommand2 = connection2.sync();
        }
        return redisCommand2;
    }

    public static RedisClient getRedisClient() {
        if (redisClient == null) {
            redisClient = RedisClient.create("redis://localhost");
        }
        return redisClient;
    }

    public static void destroy() {
        redisClient.shutdown();
    }
}
