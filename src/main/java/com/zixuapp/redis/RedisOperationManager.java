package com.zixuapp.redis;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RedisOperationManager {
    public static boolean del(String key) {
        Jedis jedis = RedisConnPoolManager.getConn();
        if (jedis == null) {
            return false;
        }
        try {
            jedis.del(key);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            jedis.close();
        }
    }

    public static boolean setExpire(String key, int expires) {
        Jedis jedis = RedisConnPoolManager.getConn();
        if (jedis == null) {
            return false;
        }
        try {
            jedis.expire(key, expires);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            jedis.close();
        }
    }


    public static int getExpire(String key) {
        Jedis jedis = RedisConnPoolManager.getConn();
        if (jedis == null) {
            return 0;
        }
        try {
            return Math.toIntExact(jedis.ttl(key));
        } catch (Exception e) {
            return 0;
        } finally {
            jedis.close();
        }
    }

    public static boolean setString(String key, String value, int expires) {
        Jedis jedis = RedisConnPoolManager.getConn();
        if (jedis == null) {
            return false;
        }
        try {
            jedis.set(key, value);
            if (expires > 0) {
                setExpire(key, expires);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            jedis.close();
        }
    }


    public static String getString(String key) {
        Jedis jedis = RedisConnPoolManager.getConn();
        if (jedis == null) {
            return null;
        }
        try {
            return jedis.get(key);
        } catch (Exception e) {
            return null;
        } finally {
            jedis.close();
        }
    }


    public static boolean setMap(String key, HashMap<String, String> data, int expires) {
        Jedis jedis = RedisConnPoolManager.getConn();
        if (jedis == null) {
            return false;
        }
        try {
            jedis.hmset(key, data);
            if (expires > 0) {
                setExpire(key, expires);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            jedis.close();
        }
    }

    public static HashMap<String, String> getMap(String key) {
        Jedis jedis = RedisConnPoolManager.getConn();
        if (jedis == null) {
            return null;
        }
        try {
            HashMap data = (HashMap) jedis.hgetAll(key);
            if (data.size() == 0) {
                return null;
            }
            HashMap<String, String> temp = new HashMap<>();
            for (Object o : data.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                temp.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
            return temp;
        } catch (Exception e) {
            return null;
        } finally {
            jedis.close();
        }
    }

    public static boolean batchDel(String key) {
        Jedis jedis = RedisConnPoolManager.getConn();
        if (jedis == null) {
            return false;
        }
        try {
            Set<String> set = jedis.keys(key + "*");
            for (String keyStr : set) {
                jedis.del(keyStr);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            jedis.close();
        }
    }
}
