package com.huiyin.bean;

import org.json.JSONObject;



/**
 * 猜你喜欢-商品
 * @author 刘远祺
 *
 */
public class GoodLikeBean extends BaseBean{
	
	public String GOODS_IMG;		// 商品图片
	
	public String PRICE;			// 商品价格
	
	public String GOODS_NAME;		// 商品名称
	
	public String GOODS_ID;			// 商品id
	
	public String GOODS_NO;			// 商品规格(商品编号)
	
	public String STORE_ID;			//店铺id
	
	public String NUM;				//商品数量
	
	public GoodLikeBean(JSONObject obj){
		GOODS_IMG = obj.optString("GOODS_IMG");
		PRICE = obj.optString("PRICE");
		GOODS_NO = obj.optString("GOODS_NO");
		STORE_ID = obj.optString("STORE_ID");
		GOODS_NAME = obj.optString("GOODS_NAME");
		GOODS_ID = obj.optString("GOODS_ID");
		NUM = obj.optString("NUM");
	}
}