package com.huiyin.bean;

import java.util.ArrayList;

/**
 * Created by lian on 2015/5/24.
 */
public class MyOrderDetailBean extends BaseBean {
    private static final long serialVersionUID = 1L;
    public String GOODS_IMG;//商品图片
    public double GOODS_PRICE;//商品价格
    public String GOODS_NAME;//商品名称
    public String GOODS_NO;//商品编号
    public String GOODS_ID;//商品id
    public String STORE_ID;//店铺id
    public int QUANTITY;//购买数量
    public String NORMS_VALUE;//规格值
    public String WORDS;//如果为空，则不显示这个字段的数据，不为空，则显示,表明有参与退换货的商品
    //add by zhyao @2015/6/13 添加消费卷列表
    public ArrayList<Coupon> coupons;//消费卷列表
    
    // add by zhyao @2105/6/13 添加消费卷
    public class Coupon {
    	public String GOOD_NAME;
    	public int CONSUMPTION_STATUS;//消费卷状态：0：未使用  1：已使用  2：未付款
    	public String ORDER_ID;
    	public String CONSIGNEE_PHONE;
    	public String STORE_ID;
    	public String ID;
    	public String CONSUMPTION_ID;
    	public String CONSUMPTION_TIME;
    	public String CONSUMPTION_TIMESTAMP;
    	public String GOOD_ID;
    	public String CONSIGNEE_NAME;
    }

    /**
     * 将数据转换成OrderDetail
     * @return
     */
	public OrderDetail toOrderDetail(String ORDER_ID) {
		OrderDetail detail = new OrderDetail();
		detail.GOODS_IMG = GOODS_IMG;//商品图片
		detail.ORDER_ID = ORDER_ID;//订单id
		detail.STORE_ID = STORE_ID;//店铺id
		detail.GOODS_NO = GOODS_NO;//商品编号
		detail.GOODS_NAME = GOODS_NAME;//商品名称
		detail.GOODS_ID = GOODS_ID;//商品id
		//detail.ORDER_DETAIL_ID = ORDER_DETAIL_ID;//商品详情ID
		detail.GOODS_PRICE = GOODS_PRICE+"";//单品价格
		return detail;
	}
}
