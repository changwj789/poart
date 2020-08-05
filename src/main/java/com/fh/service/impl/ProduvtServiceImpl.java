package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.ServerResponse;
import com.fh.dao.ProductDao;
import com.fh.entity.po.Product;
import com.fh.service.ProductService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduvtServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Override
    public ServerResponse findAllProduct(Long id) {
       List<Product> list= productDao.findAllProduct(id);
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse findIsHotProduct() {
        String isHotProduct = RedisUtil.getJedis().get("isHotProduct");
        if (!StringUtils.isEmpty(isHotProduct)){
            List<Product> list = JSONObject.parseArray(isHotProduct, Product.class);
            return ServerResponse.success(list);
        }
        List<Product> list1=productDao.findIsHotProduct();
        String s = JSONObject.toJSONString(list1);
        RedisUtil.getJedis().set("isHotProduct",s);
        RedisUtil.returnJedis(RedisUtil.getJedis());
        return ServerResponse.success(list1);
    }

    @Override
    public ServerResponse finProductById(Long id) {
        List<Product> list=productDao.finProductById(id);
        return ServerResponse.success(list);
    }

    @Override
    public Product finProductById2(Long id) {
        Product product=productDao.finProductById2(id);
        return product;
    }
}
