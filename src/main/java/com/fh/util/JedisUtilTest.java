package com.fh.util;

import com.fh.common.RedisType;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

public class JedisUtilTest {


    @Test
    public void TestJedis(){
        Jedis jedis=new Jedis("192.168.188.131",7020);
        Set<String> keys = jedis.keys("*");
        for (String key:keys) {
            if (jedis.type(key).equals(RedisType.TYPE_STRING.getType())){
                System.out.println("类型:"+RedisType.TYPE_STRING.getCn()+"---"+"值:"+jedis.get(key));
            }else if(jedis.type(key).equals(RedisType.TYPE_HASH.getType())){
                Set<String> hkeys = jedis.hkeys(key);
                for (String hashKey:hkeys) {
                    System.out.println("类型:"+RedisType.TYPE_HASH.getCn()+"---"+"值:"+jedis.hget(key,hashKey));
                }

            }else if (jedis.type(key).equals(RedisType.TYPE_LIST.getType())){
                System.out.println("类型:"+RedisType.TYPE_LIST.getCn()+"---"+"值:"+jedis.lrange(key,0,-1));


            }else if (jedis.type(key).equals(RedisType.TYPE_SET.getType())){
                System.out.println("类型:"+RedisType.TYPE_SET.getCn()+"---"+"值:"+jedis.smembers(key));

            }else {
                System.out.println("类型:"+RedisType.TYPE_ZSET.getCn()+"---"+"值:"+jedis.zrange(key,0,-1));

            }
        }

        jedis.close();
    }



}
