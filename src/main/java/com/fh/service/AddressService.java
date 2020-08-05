package com.fh.service;

import com.fh.common.ServerResponse;
import com.fh.entity.po.Address;

public interface AddressService {
    ServerResponse findAllPeo(Integer vipId);

    ServerResponse addAddress(Address address);

}
