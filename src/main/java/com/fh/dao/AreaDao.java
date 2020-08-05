package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.Area;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AreaDao extends BaseMapper<Area> {
    List<Area> findAllArea();

    Area finAreaById(int parseInt);
}
