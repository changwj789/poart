package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.ServerResponse;
import com.fh.dao.CartDao;
import com.fh.dao.ProductDao;
import com.fh.entity.po.Product;
import com.fh.entity.po.Vip;
import com.fh.entity.vo.ProductCart;
import com.fh.service.CartService;
import com.fh.util.RedisUse;
import net.sf.jsqlparser.expression.LongValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private ProductDao productDao;
    @Override
    public Integer addCart(Long id, Integer count)  {
        // 将数据存入redis   hash  key 用户的唯一标示  filed 商品id  value  商品信息
        Vip vip = (Vip) request.getAttribute("user");
        String iphone=vip.getPhoneNum();
        //获取到对应商品，判断库存
        Product product = productDao.finProductById2(id);
        if (count>0){
            if (count>product.getStock()){
                return product.getStock()-count;
            }
        }

        ProductCart productCart=cartDao.queryProductById(id);
        productCart.setCheck(true);
        productCart.setCount(count);
        //计算money
        BigDecimal money = productCart.getPrice().multiply(new BigDecimal(count));
        productCart.setMoney(money);
        //判断redis中是否有此商品
        String hget = RedisUse.hget("cart_" + iphone, id + "");
        ProductCart cart=new ProductCart();
        if (hget!=null){
             cart = JSONObject.parseObject(hget, ProductCart.class);
             productCart.setCount(productCart.getCount()+cart.getCount());
            if (productCart.getCount()>product.getStock()){
                return product.getStock()-productCart.getCount();
            }
            BigDecimal decimal = productCart.getPrice().multiply(new BigDecimal(productCart.getCount()));
            productCart.setMoney(decimal);
        }
        //将商品信息 转成json字符串
        String s = JSONObject.toJSONString(productCart);
        //放入redis
        RedisUse.hset("cart_"+iphone,id+"",s);
        //获取商品种类的个数
        long hlen = RedisUse.hlen("cart_" + iphone);
        return (int)hlen;


    }

    @Override
    public ServerResponse findAllProductCart() {
        Vip vip = (Vip) request.getAttribute("user");
        List<String> hvals = RedisUse.hvals("cart_" + vip.getPhoneNum());
        List<ProductCart> list=new ArrayList<>();
        for (int i = 0; i <hvals.size() ; i++) {
            ProductCart cart = JSONObject.parseObject(hvals.get(i), ProductCart.class);
            list.add(cart);
        }
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse lessOneById(Integer id) {
        Vip vip = (Vip) request.getAttribute("user");
        String iphone=vip.getPhoneNum();
        Product product = productDao.finProductById2(Long.valueOf(id));
        //判断redis中是否有此商品
        String hget = RedisUse.hget("cart_" + iphone, id + "");
        ProductCart productCart = JSONObject.parseObject(hget, ProductCart.class);
        productCart.setCount(productCart.getCount()-1);
        if (productCart.getCount()==0){
            RedisUse.hdel("cart_" + iphone,id+"");
            return ServerResponse.success();
        }
        BigDecimal multiply = productCart.getPrice().multiply(new BigDecimal(productCart.getCount()));
        productCart.setMoney(multiply);
        String s = JSONObject.toJSONString(productCart);
        //放入redis
        RedisUse.hset("cart_"+iphone,id+"",s);
        if (product.getStock()<productCart.getCount()){
            return ServerResponse.success("库存不足",product.getStock());
        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse addOneById(Integer id) {
        Vip vip = (Vip) request.getAttribute("user");
        String iphone=vip.getPhoneNum();
        Product product = productDao.finProductById2(Long.valueOf(id));
        //判断redis中是否有此商品
        String hget = RedisUse.hget("cart_" + iphone, id + "");
        ProductCart productCart = JSONObject.parseObject(hget, ProductCart.class);
        productCart.setCount(productCart.getCount()+1);
        if (product.getStock()<productCart.getCount()){
            return ServerResponse.success("库存不足",product.getStock());
        }
        BigDecimal multiply = productCart.getPrice().multiply(new BigDecimal(productCart.getCount()));
        productCart.setMoney(multiply);
        String s = JSONObject.toJSONString(productCart);
        //放入redis
        RedisUse.hset("cart_"+iphone,id+"",s);

        return ServerResponse.success();
    }

    @Override
    public ServerResponse deleteById(Integer id) {
        Vip vip = (Vip) request.getAttribute("user");
        String iphone=vip.getPhoneNum();
        //根据id删除redis中指定的商品
        RedisUse.hdel("cart_"+iphone,id+"");
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateCartStatus(String ids) {
        Vip vip = (Vip) request.getAttribute("user");
        String iphone=vip.getPhoneNum();
        //获取redis中的商品
        List<String> list = RedisUse.hvals("cart_" + iphone);
        for (int i = 0; i <list.size() ; i++) {
            ProductCart cart = JSONObject.parseObject(list.get(i), ProductCart.class);
            if (!(","+ids).contains(","+cart.getId()+",")){
                cart.setCheck(false);
                RedisUse.hset("cart_" + iphone,cart.getId()+"",JSONObject.toJSONString(cart));
            }

        }
        return ServerResponse.success();
    }

    @Override
    public ServerResponse queryCheckProduct() {
        Vip vip = (Vip) request.getAttribute("user");
        String iphone=vip.getPhoneNum();
        //获取redis中的商品
        List<String> list = RedisUse.hvals("cart_" + iphone);
        List<ProductCart> plist=new ArrayList<>();
        for (int i = 0; i <list.size() ; i++) {
            ProductCart cart = JSONObject.parseObject(list.get(i), ProductCart.class);
            if (cart.isCheck()==true){
                plist.add(cart);
            }
        }
        return ServerResponse.success(plist);
    }
}
