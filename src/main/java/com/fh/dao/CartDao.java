package com.fh.dao;

import com.fh.entity.vo.ProductCart;

public interface CartDao {
    ProductCart queryProductById(Long id);
}
