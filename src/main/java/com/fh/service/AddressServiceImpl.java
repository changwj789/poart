package com.fh.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fh.common.PayStatusEnum;
import com.fh.common.ServerResponse;
import com.fh.dao.AddressDao;
import com.fh.dao.ProductDao;
import com.fh.dao.ShopOrderDao;
import com.fh.entity.po.*;
import com.fh.entity.vo.ProductCart;
import com.fh.util.RedisUse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private HttpServletRequest request;
    @Override
    public ServerResponse findAllPeo(Integer vipId) {
        Vip vip = (Vip) request.getAttribute("user");
        List<Address> list= addressDao.findAllPeo(vipId);
        //如果表中只有地区的代号没有添加冗余字段，可以通过redis查找个id对应的地区的名字，然后进行地区拼接
        //地区在redis中以hash的形式进行存储,所以根据key和filed(以地区的id设置)就可以查找
        return ServerResponse.success(list);
    }

    @Override
    public ServerResponse addAddress(Address address) {
        addressDao.addAddress(address);
        return ServerResponse.success();
    }

}
