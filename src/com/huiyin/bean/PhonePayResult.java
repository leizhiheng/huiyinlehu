package com.huiyin.bean;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 话费充值结果
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-14
 */
public class PhonePayResult implements Serializable{
	
	public String type;
	public String msg;
	public String respCode;
	public String price;
	public String orderId;
	public String orderCode;
	public String payItem;
	public String perpaidCard;
	public String curTime;
	
	public PhonePayResult(){
		price = "0.01";
		orderId = "1223551";
		orderCode = "4566811";
		payItem = "3";
		perpaidCard = "4";
		curTime = "";
	}
	
	public PhonePayResult(String content){
		JSONObject mJSONObject = null;
		try {
			mJSONObject = new JSONObject(content);
			this.type = mJSONObject.optString("type");
			this.msg = mJSONObject.optString("msg");
			this.respCode = mJSONObject.optString("respCode");
			this.price = mJSONObject.optString("price");
			this.orderId = mJSONObject.optString("orderId");
			this.orderCode = mJSONObject.optString("orderCode");
			this.payItem = mJSONObject.optString("payItem");
			this.perpaidCard = mJSONObject.optString("perpaidCard");
			this.curTime = mJSONObject.optString("curTime");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
