package com.fh.util;

import com.alibaba.fastjson.JSONObject;
import com.fh.entity.po.Area;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    @org.junit.Test
    public void testArea(){

        Jedis jedis = RedisUtil.getJedis();
        List<String> areaAll = jedis.hvals("areaAll");
        List<Area> list=new ArrayList<>();
        for (int i = 0; i <areaAll.size() ; i++) {
            Area area = JSONObject.parseObject(areaAll.get(i), Area.class);
            list.add(area);
        }
        for (int i = 0; i <list.size() ; i++) {
            System.out.println(list.get(i));
        }
    }

    @org.junit.Test
    public void test(){
        Map map=new HashMap();
        map.put("test1","1");
        map.put("test2","2");
        map.put("test3","3");
        /*for (Object v:map.values()) {
            System.out.println(v);
        }*/
        /*for (Object key:map.keySet()) {
            System.out.println(key+","+map.get(key));
        }*/

        /*int []a={1,2,3};
        int c=a.length; //数组.length
        int b=a[0];
        String d="test";
        int e=d.length(); //字符串.length()*/
    }

    @org.junit.Test
    public void testBigDecimal(){
        String s="1.23555232";
        BigDecimal bigDecimal=new BigDecimal(s);
        bigDecimal=bigDecimal.multiply(new BigDecimal("100"));
        System.out.println(bigDecimal);
        double doubleValue = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(doubleValue);
    }


    @org.junit.Test
    public void testBigDecimal2(){
        BigDecimal i=new BigDecimal(1.22223);
        System.out.println(i.doubleValue());
    }
}
