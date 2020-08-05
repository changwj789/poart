package com.fh.controller;

import com.fh.common.ServerResponse;
import com.fh.entity.po.Address;
import com.fh.entity.po.Area;
import com.fh.service.AddressService;
import com.fh.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("address")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private AreaService areaService;

    @RequestMapping("findAllPeo")
    public ServerResponse findAllPeo(Integer vipId){

        return addressService.findAllPeo(vipId);
    }
    @RequestMapping("addAddress")
    public ServerResponse addAddress(Address address){
        String areaIds = address.getAreaIds();
        String[] split = areaIds.split(",");
        String areaName="";
        for (int i = 0; i <split.length ; i++) {
            Area a=areaService.finAreaById(Integer.parseInt(split[i]));
            areaName+=a.getAreaName()+" ";
        }
        address.setArea(areaName);
        Integer province = address.getProvince();
        Area area = areaService.finAreaById(province);
        address.setProvinceName(area.getAreaName());
        address.setCreateDate(new Date());
        address.setIsCheck(0);
        return addressService.addAddress(address);
    }

}
