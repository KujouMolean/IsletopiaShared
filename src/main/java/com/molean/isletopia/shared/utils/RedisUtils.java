package com.molean.isletopia.shared.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.JedisURIHelper;

import java.net.URI;

public class RedisUtils {

    private static JedisPool jedisPool;

    public static Jedis getJedis() {
        if (jedisPool == null) {
            jedisPool = new JedisPool(URI.create("redis://localhost:6379/"), 2000);
        }
        return jedisPool.getResource();
    }





}
