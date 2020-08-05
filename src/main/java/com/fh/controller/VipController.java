package com.fh.controller;

import com.fh.common.ServerResponse;
import com.fh.entity.po.Area;
import com.fh.entity.po.Vip;
import com.fh.service.AreaService;
import com.fh.service.VipService;
import com.fh.util.JWT;
import com.fh.util.OssUtil;
import com.fh.util.RedisUse;
import com.fh.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("vip")
@CrossOrigin("*")
public class VipController {
    @Autowired
    private VipService vipService;
    @Autowired
    private AreaService areaService;

    @RequestMapping("sendMessage")
    public ServerResponse sendMessage(String iphone){
        //掉用发送短信的接口返回一个code
        String code="1234";
        Jedis jedis = RedisUtil.getJedis();
        jedis.setex(iphone,60*5,code);
        RedisUtil.returnJedis(jedis);
        return ServerResponse.success("短信发送成功");
    }
    @RequestMapping("login")
    public ServerResponse login(String iphone, String code, HttpServletRequest request){
        Map logMap=new HashMap();
        //获取code
        Jedis jedis=RedisUtil.getJedis();
        String redis_code = jedis.get(iphone);
        RedisUtil.returnJedis(jedis);
        if (redis_code!=null&&redis_code.equals(code)){
            // 生成一个秘钥   对应一个信息
            //根据手机号可以查找到对应的vip用户
            Vip vip= vipService.findVipByPhone(iphone);
            if (vip==null){
                logMap.put("status",300);
                logMap.put("message","用户不存在或验证码错误");
                return ServerResponse.success(logMap);
            }
            /*Map user=new HashMap();
            user.put("iphone",iphone);*/
            String sign = JWT.sign(vip, 1000 * 60 * 60 * 24);
            //加签  手机号+sign值  目的是为了防止篡改数据
            String token= Base64.getEncoder().encodeToString((iphone+","+sign).getBytes());
            // 将最新的秘钥保存到redis中  生成多个秘钥  最新的是有用的
            RedisUse.set("token_"+iphone,sign,60*30);
            logMap.put("status",200);
            logMap.put("message","登陆成功");
            logMap.put("token",token);
            logMap.put("vip",vip);
        }else {
            logMap.put("status",300);
            logMap.put("message","用户不存在或验证码错误");
        }

        return ServerResponse.success(logMap);
    }

    @RequestMapping("addVip")
    public ServerResponse addVip(Vip vip){
        String area = vip.getArea();
        String[] split = area.split(",");
        String areaName="";
        for (int i = 0; i <split.length ; i++) {
            Area a=areaService.finAreaById(Integer.parseInt(split[i]));
            areaName+=a.getAreaName()+" ";
        }
        vip.setAreaNames(areaName);
        vip.setCreateTime(new Date());
        return vipService.addVip(vip);
    }


    @RequestMapping("uploadfile")
    public Map<String,Object> uploadfile(@RequestParam("image") MultipartFile file) throws IOException {
        Map result=new HashMap<String,Object>();
        File toFile=null;
        InputStream ins = null;
        ins = file.getInputStream();
        toFile = new File(file.getOriginalFilename());
        inputStreamToFile(ins, toFile);
        ins.close();
        String s = OssUtil.uploadFile(toFile);
        delteTempFile(toFile);
        result.put("success",true);
        result.put("filePath",s);
        return result;
    }


    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 删除本地临时文件
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }

    }
}
