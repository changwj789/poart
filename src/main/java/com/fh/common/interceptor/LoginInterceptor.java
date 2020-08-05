package com.fh.common.interceptor;

import com.fh.common.exception.NoLoginException;
import com.fh.entity.po.Vip;
import com.fh.util.JWT;
import com.fh.util.RedisUse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

       /* Object member = request.getSession().getAttribute("member");*/
      /*  String token = request.getParameter("token");
        Map user = JWT.unsign(token, Map.class);*/

        //从header里取数据
        String token = request.getHeader("token");
        //验证头信息是否完整
        if (StringUtils.isEmpty(token)){
            throw new NoLoginException("没有登录");
        }

        //解密  字节数组
        byte[] decode = Base64.getDecoder().decode(token);
        // 得到字符串  字节数组转为字符串  字符串是什么格式  iphone+","+sign
        String singToken=new String(decode);
        //判断是否被篡改
        String[] split = singToken.split(",");
        if (split.length!=2){
            throw new NoLoginException("没有登录");
        }
        String iphone=split[0];
        String sing=split[1];
        Vip user =  JWT.unsign(sing, Vip.class);
        /*if (member==null){*/
        if (user==null){
            //返回json字符串
            throw new NoLoginException("没有登录4");
        }
        if (user!=null){
            String s = RedisUse.get("token_" + iphone);
            if (!s.equals(sing)){
                throw new NoLoginException("登陆失效,从新登陆");
            }
        }
        //如果不过期就给密钥续命
        RedisUse.set("token_"+iphone,sing,60*30);
        //将用户信息放入request中  方便后面需求处理
        request.setAttribute("user",user);
        return true;
    }
}
