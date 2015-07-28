package com.huiyin.bean;

import java.io.Serializable;

import com.huiyin.utils.MathUtil;

/**
 * Created by lian on 2015/5/22.
 */
public class OrderDetail implements Serializable {
    public String GOODS_IMG;//商品图片
    public String ORDER_ID;//订单id
    public String STORE_ID;//店铺id
    public String GOODS_NO;//商品编号
    public String GOODS_NAME;//商品名称
    public String GOODS_ID;//商品id
    public String ORDER_DETAIL_ID;//商品详情ID
    public String GOODS_PRICE;//单品价格
    
    /**
     * 获取商品价格，精确到2位小数点
     * @return
     */
    public String getGoodsPrice(){
    	double value = 0.00;
    	try{
    		value = Double.parseDouble(GOODS_PRICE);
    		return MathUtil.keepMost2Decimal(value);
    	}catch(Exception e){
    		return GOODS_PRICE;
    	}
    }
}
