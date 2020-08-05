package com.fh.controller;

import com.fh.common.ServerResponse;
import com.fh.entity.po.Vip;
import com.fh.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("cart")
@CrossOrigin("*")
public class cartController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CartService cartService;
    @RequestMapping("addCart")
    public ServerResponse addCart(@RequestParam("productId") Long id, Integer count){
        Vip vip = (Vip) request.getAttribute("user");
        System.out.println(vip.getPhoneNum());
        Integer count_type =cartService.addCart(id, count);
        return ServerResponse.success(count_type);
    }

    @RequestMapping("findAllProductCart")
    public ServerResponse findAllProductCart(){
        return cartService.findAllProductCart();
    }
    @RequestMapping("lessOneById")
    public ServerResponse lessOneById(Integer id){
        return cartService.lessOneById(id);
    }
    @RequestMapping("addOneById")
    public ServerResponse addOneById(Integer id){
        return cartService.addOneById(id);
    }

    @RequestMapping("deleteById")
    private ServerResponse deleteById(Integer id){

        return cartService.deleteById(id);
    }

    @RequestMapping("updateCartStatus")
    public ServerResponse updateCartStatus(String ids){
        return cartService.updateCartStatus(ids);
    }


    @RequestMapping("queryCheckProduct")
    public ServerResponse queryCheckProduct(){
        return cartService.queryCheckProduct();
    }
}
