package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.ServerResponse;
import com.fh.dao.ICategoryDao;
import com.fh.entity.po.Category;
import com.fh.service.ICategoryService;
import com.fh.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ICategoryServiceImpl implements ICategoryService {
    @Autowired
    private ICategoryDao iCategoryDao;
    @Override
    public ServerResponse findAllCategory() {
        String allCategory = RedisUtil.getJedis().get("allCategory");
        if (!StringUtils.isEmpty(allCategory)){
            List<Category> categories = JSONObject.parseArray(allCategory, Category.class);
            return ServerResponse.success(categories);
        }
        List<Category> list = iCategoryDao.selectList(null);

        String allCategoryJson = JSONObject.toJSONString(list);//转换为json字符串

        RedisUtil.getJedis().set("allCategory",allCategoryJson);
        RedisUtil.returnJedis(RedisUtil.getJedis());
        return ServerResponse.success(list);
    }
}
