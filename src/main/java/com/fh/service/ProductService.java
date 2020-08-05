package com.fh.service;

import com.fh.common.ServerResponse;
import com.fh.entity.po.Product;

public interface ProductService {
    ServerResponse findAllProduct(Long id);

    ServerResponse findIsHotProduct();

    ServerResponse finProductById(Long id);

    Product finProductById2(Long id);
}
