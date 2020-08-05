package com.fh.service;

import com.fh.common.ServerResponse;

public interface CartService {
    Integer addCart(Long id,Integer count);

    ServerResponse findAllProductCart();

    ServerResponse lessOneById(Integer id);

    ServerResponse addOneById(Integer id);

    ServerResponse deleteById(Integer id);

    ServerResponse updateCartStatus(String ids);

    ServerResponse queryCheckProduct();
}
