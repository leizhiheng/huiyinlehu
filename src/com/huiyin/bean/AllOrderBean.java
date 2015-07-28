package com.huiyin.bean;

import java.util.ArrayList;

import android.text.TextUtils;

import com.huiyin.utils.StringUtils;

/**
 * Created by lian on 2015/5/24.
 */
public class AllOrderBean extends BaseBean {
    private static final long serialVersionUID = 1L;
    public String STAFFID;//送货员工工号
    public String PAYMENT_METHOD;//支付方式
    public String ORDER_ID;//订单id
    public double LOGISTICS_FARE;//运费
    public String DELIVERYTIME;//送货时间
    public String ORDER_CODE;//订单编号
    public String STATUS;//订单状态id
    public double PURCHASE_PRICE;//商品总金额
    public float TOTAL_PRICE;//实际付款金额
    public String PREFERENTIAL_PRICE;//优惠金额
    public String STORE_LOGO;//店铺logo
    public String STORE_NAME;//店铺名称
    public int STORE_ID;//店铺id
    public int SELLER;//店铺id
    public String PROFILE;//收货地址
    public String CONSIGNEE_PHONE;//收货手机号码
    public String INVOICING_METHOD;//发票信息
    public String INVOICES_ID;//发票id，如果为空/小于0，则表示，不存在发票
    public String CONSIGNEE_NAME;//收货人姓名
    public String SHIPPING_METHOD;//配送方式(1,门店自提，2 送货上门)
    public String CREATE_DATE;//创建时间
    public String STATUS_NAME;//订单状态名称
    public String COMMENTS_STATUS;//评论状态 0未评论 1已评论 2已追加评价
    public String words;//提示语（同一行最右边有向右的箭头，点击这行进入到物流跟踪页面）
//    public String FLAG_EVA_ADD;//是否显示“追加”:0不显示追加按钮，1显示追加按钮
//    public String FLAG_EVA_VIEW;//是否显示“评价查询”按钮：0不显示，1显示
    public String FLAG_BESPEAK;//是否预约：0显示预约按钮，1显示预约查询按钮
    public String BESPEAK_ID;	//预约ID
    
    public double POINTS_DEDUCTION;//积分抵扣金额
    public double LH_AMOUNT;//虎券抵扣金额
    public String FLAG_AFTER;//是否售后：0显示预约按钮，1显示预约查询按钮
    public String REMARK;//订单备注
    public ArrayList<MyOrderDetailBean> orderDetailList;//订单详情
    // add by zhyao @2015/5/8  添加红包抵扣金额
    public String MONEY_DEDUCTION;//红包抵扣金额
    public String IS_COMPLAINT;//是否已经投诉0，没有（页面显示投诉按钮）1.已经投诉过（页面显示查询投诉按钮）
    public String COMPLAINT_ID;//投诉id（当 IS_COMPLAINT=1时这个字段才会存在）
    // add by zhyao @2015/6/25
    public String ALLCONSUMPTION;//判断商品是否全部是消费卷 1.全消费卷  0.不全消费卷
    
    
    /**
     * 获取发票ID
     * @return
     */
    public int getINVOICES_ID(){
    	try{
    		return Integer.parseInt(INVOICES_ID);
    	}catch(Exception e){
    		e.printStackTrace();
    		return 0;
    	}
    }
    
    /**
     * 获取订单编号
     * @return
     */
    public String getRemark(){
    	if(TextUtils.isEmpty(REMARK)){
    		return "无";
    	}
    	return REMARK;
    }
    
    public int getFLAG_AFTER(){
    	try{
    		return Integer.parseInt(FLAG_AFTER);
    	}catch(Exception e){
    		return 0;
    	}
    }

    /**
     * 获取所有订单商品
     * @return
     */
	public ArrayList<OrderDetail> getOrderDetailList() {
		ArrayList<OrderDetail> list = new ArrayList<OrderDetail>();
		if(null != orderDetailList){
			MyOrderDetailBean bean = null;
			for(int i=0; i<orderDetailList.size(); i++){
				bean = orderDetailList.get(i);
				list.add(bean.toOrderDetail(ORDER_ID));
			}
		}
		return list;
	}

	/**
	 * 获取优惠金额
	 * @return
	 */
	public String getPREFERENTIAL_PRICE() {
		if(StringUtils.isEmpty(PREFERENTIAL_PRICE)){
			return "0";
		}
		return PREFERENTIAL_PRICE;
	}
}
