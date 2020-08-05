package com.fh.service.impl;

import com.fh.common.ServerResponse;
import com.fh.dao.VipDao;
import com.fh.entity.po.Vip;
import com.fh.service.VipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VipServiceImpl implements VipService {
    @Autowired
    private VipDao vipDao;
    @Override
    public ServerResponse addVip(Vip vip) {
        vipDao.addVip(vip);
        return ServerResponse.success();
    }

    @Override
    public Vip findVipByPhone(String iphone) {
        return vipDao.findVipByPhone(iphone);
    }
}
