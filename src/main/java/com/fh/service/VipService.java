package com.fh.service;

import com.fh.common.ServerResponse;
import com.fh.entity.po.Vip;

public interface VipService {
    ServerResponse addVip(Vip vip);

    Vip findVipByPhone(String iphone);
}
