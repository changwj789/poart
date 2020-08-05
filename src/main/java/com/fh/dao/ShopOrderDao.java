package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.ShopOrder;

import java.util.List;

public interface ShopOrderDao extends BaseMapper<ShopOrder> {
    void addShopOrder(ShopOrder shopOrder);

    List<ShopOrder> findMyOrder(Integer id);

    void updatePayStatus(Integer orderId);
}
