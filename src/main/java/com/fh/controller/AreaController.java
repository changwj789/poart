package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.entity.po.Area;
import com.fh.service.AreaService;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.List;

@Controller
@RequestMapping("area")
public class AreaController {

    @Autowired
    private AreaService areaService;


    @RequestMapping("findAllArea")
    @ResponseBody
    public List<Area> findAllArea(){
        List<Area> list=areaService.findAllArea();
        Jedis jedis= RedisUtil.getJedis();
        for (int i = 0; i <list.size() ; i++) {
            String j=String.valueOf(list.get(i).getId());
           String areaObjet=JSONObject.toJSONString(list.get(i));
            /*String areaObjet=list.get(i).toString();*/
            jedis.hset("areaAll",j,areaObjet);
        }
        RedisUtil.returnJedis(jedis);
        return list;
    }

}
