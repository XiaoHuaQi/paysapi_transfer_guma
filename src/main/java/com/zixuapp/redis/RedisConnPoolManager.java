package com.zixuapp.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisConnPoolManager {
    private static JedisPool pool = null;

    private static JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            Properties prop = new Properties();
            InputStream is = RedisConnPoolManager.class.getClassLoader().getResourceAsStream("redis.properties");
            try {
                prop.load(is);
                is.close();
            } catch (IOException e) {
                return null;
            }
            config.setMaxIdle(Integer.parseInt(prop.getProperty("jedis.pool.maxIdle")));
            config.setMinIdle(Integer.parseInt(prop.getProperty("jedis.pool.minIdle")));
            config.setMaxTotal(Integer.parseInt(prop.getProperty("jedis.pool.maxActive")));
            config.setMaxWaitMillis(Long.parseLong(prop.getProperty("jedis.pool.maxWait")));
            config.setTestOnReturn(Boolean.parseBoolean(prop.getProperty("jedis.pool.testOnBorrow")));
            config.setTestOnBorrow(Boolean.parseBoolean(prop.getProperty("jedis.pool.testOnReturn")));
            config.setTimeBetweenEvictionRunsMillis(-1);
            pool = new JedisPool(
                    config,
                    prop.getProperty("redis.ip"),
                    Integer.parseInt(prop.getProperty("redis.port")),
                    Integer.parseInt(prop.getProperty("redis.timeout")),
                    prop.getProperty("redis.passWord")
            );
        }
        return pool;
    }

    public static synchronized Jedis getConn() {
        JedisPool pool = getPool();
        if (pool == null) {
            return null;
        }
        return getPool().getResource();
    }
}
