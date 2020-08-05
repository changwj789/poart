package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.OrderProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderProductDao extends BaseMapper<OrderProduct> {
    void batchAdd(@Param("list") List<OrderProduct> list,@Param("shopOrderId") Integer id);
}
