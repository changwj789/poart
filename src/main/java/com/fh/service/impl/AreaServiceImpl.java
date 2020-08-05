package com.fh.service.impl;

import com.fh.dao.AreaDao;
import com.fh.entity.po.Area;
import com.fh.service.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;
    @Override
    public List<Area> findAllArea() {
        return areaDao.findAllArea();
    }

    @Override
    public Area finAreaById(int parseInt) {
        return areaDao.finAreaById(parseInt);
    }
}
