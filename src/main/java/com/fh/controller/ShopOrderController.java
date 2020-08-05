package com.fh.controller;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.ServerResponse;
import com.fh.common.exception.CountException;
import com.fh.service.ShopOrderService;
import com.fh.util.RedisUse;
import com.fh.util.WxUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("order")
public class ShopOrderController {
    private static Logger logger=Logger.getLogger(ShopOrderController.class);
    @Autowired
    private ShopOrderService shopOrderService;

    @RequestMapping("addOrder")
    public ServerResponse addOrder(Integer addressId,Integer payType,String flag) throws CountException {
        //处理接口幂等性    同一个请求 发送多次   结果只处理一次
        boolean exists = RedisUse.exists(flag);
        if (exists){//代表已经存在，二次请求
            return ServerResponse.error(300,"请求处理中");
        }else {
            RedisUse.set(flag,"",10);
        }
        return shopOrderService.addOrder(addressId,payType);
    }

    @RequestMapping("ErWeiMa")
    public ServerResponse ErWeiMa(String orderId, BigDecimal totleMoney) throws Exception {
        String s1 = RedisUse.get("order" + orderId + "erWeiMa");
        if (StringUtils.isEmpty(s1)!=true){
            Map rs=new HashMap();
            rs.put("code_url",s1);
            return ServerResponse.success(rs);
        }
        int intValue = totleMoney.intValue();
        String s = String.valueOf(intValue);
        Map map = WxUtil.xiaDan(orderId, s);
        logger.info(JSONObject.toJSONString(map));
        RedisUse.set("order"+orderId+"erWeiMa", (String) map.get("code_url"),30*60);
        return ServerResponse.success(map);
    }

    @RequestMapping("ErWeiMa2")
    public ServerResponse ErWeiMa2(Integer orderId) throws Exception {
        Map map= shopOrderService.ErWeiMa2(orderId);
        return ServerResponse.success(map);
    }
    @RequestMapping("CxDingDan")
    private ServerResponse CxDingDan(String orderId) throws Exception {
        Map map = WxUtil.CxDingDan(orderId);
        return ServerResponse.success(map);
    }
    @RequestMapping("updatePayStatus")
    public ServerResponse updatePayStatus(Integer orderId){
        return shopOrderService.updatePayStatus(orderId);
    }

    @RequestMapping("findMyOrder")
    public ServerResponse findMyOrder(){
        return shopOrderService.findMyOrder();
    }

    @RequestMapping("deleteOrder")
    public ServerResponse deleteOrder(Integer id){
        shopOrderService.deleteOrder(id);
        return ServerResponse.success();
    }

}
