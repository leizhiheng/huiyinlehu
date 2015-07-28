package com.huiyin.bean;

import org.json.JSONObject;

/**
 * 闪购数据对象
 * @author 刘远祺
 *
 * @date 2015-7-1
 */
public class HomeFlashBean {

	public String GOODS_NO1;	// 第一张图片商品id
	public String GOODS_NO4;	// 第四章图片图片商品编号
	public String GOODS_NO3;	// 第三张图片商品编号
	public String GOODS_NO2;	// 第二张图片商品编号 
	
	public String STORE_ID4;	// 第四张图片店铺id
	public String STORE_ID3;	// 第三张图片店铺id
	public String STORE_ID2;	// 第二张图片店铺id
	public String STORE_ID1;	// 第一张图片店铺id

	public String ADVER;		// 闪购广告语
	public String ACTIVE_NAME;	// 闪购活动名称

	public String GOODS_ID1;	// 第一张图片商品id
	public String GOODS_ID2;	// 第二张图片商品id
	public String GOODS_ID3;	// 第三张图片商品id
	public String GOODS_ID4;	// 第四张图片商品id

	public String IMG1;			// 第一张图片
	public String IMG2;			// 第二张图片
	public String IMG3;			// 第三张图片
	public String IMG4;			// 第四张图片
	
	
	public HomeFlashBean(JSONObject obj){
		GOODS_NO1 = obj.optString("GOODS_NO1");		// 第一张图片商品id
		STORE_ID4 = obj.optString("STORE_ID4");		// 第四张图片店铺id
		GOODS_NO4 = obj.optString("GOODS_NO4");		// 第四章图片图片商品编号
		STORE_ID3 = obj.optString("STORE_ID3");		// 第三张图片店铺id
		ADVER = obj.optString("ADVER");				// 闪购广告语
		GOODS_NO3 = obj.optString("GOODS_NO3");		// 第三张图片商品编号
		STORE_ID2 = obj.optString("STORE_ID2");		// 第二张图片店铺id
		STORE_ID1 = obj.optString("STORE_ID1");		// 第一张图片店铺id
		GOODS_NO2 = obj.optString("GOODS_NO2");		// 第二张图片商品编号 
		GOODS_ID1 = obj.optString("GOODS_ID1");		// 第一张图片商品id
		IMG4 = obj.optString("IMG4");				// 第四张图片
		GOODS_ID2 = obj.optString("GOODS_ID2");		// 第二张图片商品id
		GOODS_ID3 = obj.optString("GOODS_ID3");		// 第三张图片商品id
		IMG3 = obj.optString("IMG3");				// 第三张图片
		IMG1 = obj.optString("IMG1");				// 第一张图片
		GOODS_ID4 = obj.optString("GOODS_ID4");		// 第四张图片商品id
		IMG2 = obj.optString("IMG2");				// 第二张图片
		ACTIVE_NAME = obj.optString("ACTIVE_NAME");	// 闪购活动名称
		
//      规则：商品id以goods_id开头，第几张图片后面就带数字几
//      规格：以goods_no开头，第几张图片后面就带数字几
//      店铺：以store_id开头，第几张图片后面就带数字几
	}
}
