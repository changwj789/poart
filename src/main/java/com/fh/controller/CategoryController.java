package com.fh.controller;
import com.fh.common.ServerResponse;
import com.fh.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("category")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping("findAllCategory")
    @ResponseBody
    public ServerResponse findAllCategory(){

        return iCategoryService.findAllCategory();
    }

  /*  @RequestMapping("RedisCategory")
    @ResponseBody
    public ServerResponse RedisCategory(){
        ServerResponse allCategory = iCategoryService.findAllCategory();
        String s = JSONObject.toJSONString(allCategory);
        Jedis jedis=new Jedis("192.168.188.131",7020);
        jedis.set("allCategory",s);
        jedis.close();
        return ServerResponse.success();
    }*/

}
