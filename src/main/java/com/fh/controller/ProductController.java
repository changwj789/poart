package com.fh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fh.common.ServerResponse;
import com.fh.entity.po.Area;
import com.fh.entity.po.Product;
import com.fh.service.AreaService;
import com.fh.service.ProductService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.List;

@Controller
@RequestMapping("product")
@CrossOrigin("*")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private AreaService areaService;

    @RequestMapping("findAllProduct")
    @ResponseBody
    public ServerResponse findAllProduct(Long id){
        return productService.findAllProduct(id);
    }

    @RequestMapping("findIsHotProduct")
    @ResponseBody
    public ServerResponse findIsHotProduct(){
        return productService.findIsHotProduct();
    }

    /*@RequestMapping("finProductById")
    @ResponseBody
    public ServerResponse finProductById(Long id){
        Jedis jedis=RedisUtil.getJedis();
        List<Product> list= (List<Product>) productService.finProductById(id).getData();
        Product product=list.get(0);
        String areaId = product.getAreaId();
        StringBuilder s=new StringBuilder();
        String[] split = areaId.split(",");
        for (int i = 0; i <split.length ; i++) {
            String hget = jedis.hget("areaAll", split[i]);
            Area area = JSONObject.parseObject(hget, Area.class);
            s.append(area.getAreaName());
        }
        product.setAreaId(s.toString());
        list.remove(list.get(0));
        list.add(product);
        return ServerResponse.success(list);
    }*/

    @RequestMapping("finProductById2")
    @ResponseBody
    public Product finProductById2(Long id){
        Jedis jedis=RedisUtil.getJedis();
        Product product= productService.finProductById2(id);
        String areaId = product.getAreaId();
        String[] split = areaId.split(",");
        StringBuilder s=new StringBuilder();
        for (int i = 0; i <split.length ; i++) {
            String hget = jedis.hget("areaAll", split[i]);
            Area area = JSON.parseObject(hget, Area.class);
            s.append(area.getAreaName()+" ");
        }
        product.setAreaId(s.toString());
        return product;
    }
}
