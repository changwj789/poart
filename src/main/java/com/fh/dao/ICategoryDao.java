package com.fh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.entity.po.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ICategoryDao extends BaseMapper<Category> {

}
