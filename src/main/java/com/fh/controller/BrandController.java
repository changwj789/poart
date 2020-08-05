package com.fh.controller;

import com.fh.common.ServerResponse;
import com.fh.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("brand")
public class BrandController {
    @Autowired
    private IBrandService iBrandService;

    @RequestMapping("findAllBrand")
    @ResponseBody
    public ServerResponse findAllBrand(){
        return iBrandService.findAllBrand();
    }

}
