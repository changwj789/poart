package com.fh.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fh.common.PayStatusEnum;
import com.fh.common.ServerResponse;
import com.fh.common.exception.CountException;
import com.fh.dao.OrderProductDao;
import com.fh.dao.ProductDao;
import com.fh.dao.ShopOrderDao;
import com.fh.entity.po.OrderProduct;
import com.fh.entity.po.Product;
import com.fh.entity.po.ShopOrder;
import com.fh.entity.po.Vip;
import com.fh.entity.vo.ProductCart;
import com.fh.service.ShopOrderService;
import com.fh.util.RedisUse;
import com.github.wxpay.sdk.FeiConfig;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShopOrderServiceImpl implements ShopOrderService {

    @Autowired
    private ShopOrderDao shopOrderDao;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderProductDao orderProductDao;

    @Override
    public ServerResponse addOrder(Integer addressId, Integer payType) throws CountException {
        //此次请求返回的数据
        Map map=new HashMap();
        //订单详情表
        List<OrderProduct> list=new ArrayList<>();
        //完善订单信息,保存数据库
        ShopOrder shopOrder=new ShopOrder();
        shopOrder.setPayType(payType);
        shopOrder.setAddressId(addressId);
        shopOrder.setCarateDate(new Date());
        shopOrder.setPayStatus(PayStatusEnum.PAY_STATUS_INIT.getStatus());
        //商品种类数量
        Integer typeCount=0;
        //商品总金额
        BigDecimal totleMoney=new BigDecimal(0);
        //获取当前登陆用户的信息
        Vip vip = (Vip) request.getAttribute("user");
        String iphone=vip.getPhoneNum();
        shopOrder.setVipId(vip.getId());
        List<String> hvals = RedisUse.hvals("cart_" + iphone);

        for (int i = 0; i <hvals.size() ; i++) {
            ProductCart cart = JSONObject.parseObject(hvals.get(i), ProductCart.class);
            //判断isCheck是否为true
            if (cart.isCheck()==true){
                //查询对应商品判断库存
                Product product = productDao.finProductById2(cart.getId());
                if (product.getStock()>=cart.getCount()){//库存够
                    typeCount++;
                    totleMoney=totleMoney.add(cart.getMoney());
                    //订单详情的记录
                    OrderProduct orderProduct=new OrderProduct();
                    orderProduct.setProductid(cart.getId().intValue());
                    orderProduct.setCount(cart.getCount());
                    //未生成订单号
                    list.add(orderProduct);
                    //数据库库存更新
                    //减库存  数据库的锁 保证不会超卖  update  返回一个值 int

                    /*product.setStock(product.getStock()-cart.getCount());
                    productDao.updatProduct(product);*/
                    int i1=productDao.updateProductStock(product.getId(),cart.getCount());
                    if (i1==0){//超卖了
                        throw new CountException("商品编号为："+cart.getId()+"库存不足"
                                +"只有"+product.getStock());
                    }
                }else {
                    //库存不足判断逻辑
                    throw new CountException("商品编号为："+cart.getId()+"库存不足"
                    +"只有"+product.getStock());
                }
            }
        }
        shopOrder.setTotalmoney(totleMoney);
        shopOrder.setProductCount(typeCount);
        //将订单信息保存到数据库，生成订单ID；
         /*shopOrderDao.addShopOrder(shopOrder);*/
        shopOrderDao.insert(shopOrder);
        //保存订单详情表
        orderProductDao.batchAdd(list,shopOrder.getId());
        //删除redis的数据   把购物车中已经结算的商品 移除redis
        for (int i = 0; i <hvals.size() ; i++) {
            ProductCart productCart = JSONObject.parseObject(hvals.get(i), ProductCart.class);
            if (productCart.isCheck()==true){
                RedisUse.hdel("cart_"+iphone,productCart.getId()+"");
            }
        }
        map.put("orderId",shopOrder.getId());
        map.put("totleMoney",totleMoney);
        return ServerResponse.success(map);
    }

    @Override
    public ServerResponse updatePayStatus(Integer orderId) {
         shopOrderDao.updatePayStatus(orderId);
         return ServerResponse.success();
    }

    @Override
    public Map ErWeiMa2(Integer orderId) throws Exception {
        ShopOrder shopOrder = shopOrderDao.selectById(orderId);
        BigDecimal totalmoney = shopOrder.getTotalmoney();
        totalmoney=totalmoney.multiply(new BigDecimal(1));
        int intValue = totalmoney.intValue();
        String valueOf = String.valueOf(intValue);
        // 微信支付  natvie   商户生成二维码
        //配置配置信息
        FeiConfig config = new FeiConfig();
        //得到微信支付对象
        WXPay wxpay = new WXPay(config);
        //设置请求参数
        Map<String, String> data = new HashMap<String, String>();
        //对订单信息描述
        data.put("body", "飞狐电商666-订单支付");
        //String payId = System.currentTimeMillis()+"";
        //设置订单号 （保证唯一 ）
        data.put("out_trade_no","weixin1_order_000"+orderId);
        //设置币种
        data.put("fee_type", "CNY");
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        Date d=new Date();
        String dateStr = sdf.format(new Date(d.getTime() + 120000000));
        //设置二维码的失效时间
        data.put("time_expire", dateStr);
        //设置订单金额   单位分
        data.put("total_fee",valueOf);
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        //设置支付方式
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        // 统一下单
        Map<String, String> resp = wxpay.unifiedOrder(data);
        shopOrder.setPayStatus(PayStatusEnum.PAY_STATUS_ING.getStatus());
        ShopOrder shopOrder1=new ShopOrder();
        shopOrder1.setId(shopOrder.getId());
        shopOrder1.setPayStatus(shopOrder.getPayStatus());
        shopOrder1.setCarateDate(shopOrder.getCarateDate());
        shopOrder1.setProductCount(shopOrder.getProductCount());
        shopOrder1.setTotalmoney(shopOrder.getTotalmoney());
        shopOrder1.setAddressId(shopOrder.getAddressId());
        shopOrder1.setPayType(shopOrder.getPayType());
        shopOrderDao.updateById(shopOrder1);
        return resp;
    }

    @Override
    public ServerResponse findMyOrder() {
        //获取当前登陆用户的信息
        Vip vip = (Vip) request.getAttribute("user");
        List<ShopOrder> list= shopOrderDao.findMyOrder(vip.getId());
        return ServerResponse.success(list);
    }

    @Override
    public void deleteOrder(Integer id) {

        shopOrderDao.deleteById(id);
    }
}
