package com.fh.service;

import com.fh.common.ServerResponse;
import com.fh.common.exception.CountException;

import java.util.Map;

public interface ShopOrderService {


    ServerResponse addOrder(Integer addressId, Integer payType) throws CountException;

    ServerResponse updatePayStatus(Integer orderId);

    Map ErWeiMa2(Integer orderId) throws Exception;
    ServerResponse findMyOrder();

    void deleteOrder(Integer id);
}
