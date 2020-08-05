package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.Address;

import java.util.List;

public interface AddressDao extends BaseMapper<Address> {
    List<Address> findAllPeo(Integer vipId);

    void addAddress(Address address);
}
