package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductDao extends BaseMapper<Product> {

    List<Product> findAllProduct(Long id);

    List<Product> findIsHotProduct();

    List<Product> finProductById(Long id);

    Product finProductById2(Long id);

    void updatProduct(Product product);

    int updateProductStock(@Param("id") Long id,@Param("count") Integer count);
}
