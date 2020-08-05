package com.fh.service.impl;

import com.fh.common.ServerResponse;
import com.fh.dao.IBrandDao;
import com.fh.entity.po.Brand;
import com.fh.service.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IBrandServiceImp implements IBrandService {
    @Autowired
    private IBrandDao iBrandDao;
    @Override
    public ServerResponse findAllBrand() {
        List<Brand> list = iBrandDao.selectList(null);
        return ServerResponse.success(list);
    }
}
