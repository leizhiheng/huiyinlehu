package com.huiyin.bean;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.utils.LogUtil;

/**
 * 预约信息
 * 
 * @author 刘远祺
 * 
 */
public class OrderBespeakInfoBean extends BaseBean {

	public List<GoodBean> goodsList; 				// 商品集合

	public String CONSIGNEE_NAME; 					// 收货人姓名

	public String CONSIGNEE_PHONE; 					// 收货人手机号码

	public String CONSIGNEE_ADDRESS;	 			// 收货人地址
	
	public String ORDER_CODE;						// 订单编号
	
	public String ORDER_ID;							// 订单id

	public String curTime; 							// 当前系统时间

	public List<ReservationType> reservationType; 	// 预约类型

	public static OrderBespeakInfoBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			OrderBespeakInfoBean orderbean = gson.fromJson(json, OrderBespeakInfoBean.class);
			return orderbean;
		} catch (Exception e) {
			LogUtil.d("dataExplainJson", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}
}
