package com.huiyin.bean;

import java.util.ArrayList;

/**
 * Created by lian on 2015/5/24.
 */
public class AllOrderDetailBean extends BaseBean{
    private static final long serialVersionUID = 1L;
    public AllOrderBean order;

    
    private GoodList goodList;
	public GoodList getGoodList() {
		if(null == goodList){
			goodList = new GoodList();
			
			goodList.SELLER = order.SELLER;//店铺id
			goodList.STORE_NAME = order.STORE_NAME;//店铺名称
			goodList.STORE_LOGO = order.STORE_LOGO;//店铺logo
			goodList.ORDER_CODE = order.ORDER_CODE;//订单编号
			//goodList.ORDER_TYPE = order.;//订单类型0  普通订单1  换货订单2 预约订单 3退货订单
		    //goodList.USER_ID = order.;//用户id
		    goodList.TOTAL_PRICE = order.TOTAL_PRICE;//付款金额
		    goodList.ID = order.ORDER_ID;//订单id
		    goodList.STATUS = order.STATUS;//状态id
		    goodList.STATUS_NAME = order.STATUS_NAME;//状态名称
		    goodList.COMMENTS_STATUS = order.COMMENTS_STATUS;//评论状态 0未评论 1已评论
		    goodList.FLAG_BESPEAK = order.FLAG_BESPEAK;//是否预约：0显示预约按钮，1显示预约查询按钮
		    goodList.FLAG_AFTER = order.getFLAG_AFTER();// 0 可以退换货， >0 不可以退换货
		    goodList.CREATE_DATE = order.CREATE_DATE;//订单创建时间
		    goodList.orderDetalList = order.getOrderDetailList();//订单详情
			
		}
		return goodList;
	}
}
