package com.fh.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

//redis连接池
public class RedisUtil {

    private static JedisPool jedisPool;
    //static 静态块  在jvm加载类时执行，只执行一次
    static {
        //创建redis池的配置
        JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100);
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setMinIdle(50);
        jedisPoolConfig.setMaxWaitMillis(300000);
        //初始化redis池
        jedisPool=new JedisPool(jedisPoolConfig,"192.168.188.131",7020);
    }
    //从池中拿到连接
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
    //用完以后还给池中
    public static void returnJedis(Jedis jedis){
        jedisPool.returnResource(jedis);
    }
}
