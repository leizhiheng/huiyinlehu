package com.huiyin.bean;


import java.util.ArrayList;

/**
 * Created by lian on 2015/5/22.
 */
public class GoodList extends BaseBean {
    public int SELLER;//店铺id
    public String STORE_NAME;//店铺名称
    public String STORE_LOGO;//店铺logo
    public String ORDER_CODE;//订单编号
    public int ORDER_TYPE;//订单类型0  普通订单1  换货订单2 预约订单 3退货订单
    public String USER_ID;//用户id
    public float TOTAL_PRICE;//付款金额
    public String ID;//订单id
    public String STATUS;//状态id
    public String STATUS_NAME;//状态名称
    public String COMMENTS_STATUS;//评论状态 0未评论 1已评论
    public String FLAG_BESPEAK;//是否预约：0显示预约按钮，1显示预约查询按钮
    public int FLAG_AFTER;// 0 可以退换货， >0 不可以退换货
    public String CREATE_DATE;//订单创建时间
    public ArrayList<OrderDetail> orderDetalList;//订单详情
    
    /**
     * 是否预约
     * @return
     */
    public boolean hasBespeak(){
    	try{
    		int bespeak = Integer.parseInt(FLAG_BESPEAK);
    		return 1==bespeak;
    	}catch(Exception e){
    		return false;
    	}
    }
}