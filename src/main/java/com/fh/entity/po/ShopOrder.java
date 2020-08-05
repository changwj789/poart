package com.fh.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.util.Date;

@TableName("shop_order")
public class ShopOrder {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("addressId")
    private Integer addressId;
    @TableField("payType")
    private Integer payType;
    @TableField("productCount")
    private Integer productCount;
    @TableField("totalmoney")
    private BigDecimal totalmoney;
    @TableField("payStatus")
    private Integer payStatus;
    @TableField("carateDate")
    private Date carateDate;
    @TableField("vipId")
    private Integer vipId;

    public Integer getVipId() {
        return vipId;
    }

    public void setVipId(Integer vipId) {
        this.vipId = vipId;
    }

    public Date getCarateDate() {
        return carateDate;
    }

    public void setCarateDate(Date carateDate) {
        this.carateDate = carateDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public BigDecimal getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(BigDecimal totalmoney) {
        this.totalmoney = totalmoney;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
}
