package com.huiyin.ui.flash;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.bean.BaseBean;

public class FlashPrefectureBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	public int pageIndex;
	public int totalPageNum;
	public String curTime;
    
    public String ACTIVE_BANNER;	// banner图
    public String BEGINTIME;		// 活动开始时间
    public String ENDTIME;			// 活动结束时间
    public String ACTIVE_NAME;		// 活动名称
    
    public ArrayList<FrashGoods> goodsList;//商品列表		
    
	public static FlashPrefectureBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			FlashPrefectureBean experLightBean = gson.fromJson(json, FlashPrefectureBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
	
	/**
	 * 获取商品列表
	 * @return
	 */
	public ArrayList<FrashGoods> getProduceList() {
		return goodsList;
	}
	
	public class FrashGoods{
		
		public String GOODS_IMG;		// 商品图片
		public String GOODS_PRICE;		// 商品价格
		public String JOIN_NUM;			// 参与人数
		public String DISCOUNT_PRICE;	// 折后价格,
		public String GOODS_STOCK;		// 库存
		public String GOODS_NAME;		// 商品名称,
		public String STORE_ID;			// 店铺id
		public String GOODS_NO;			// 商品编号
		public String GOODS_ID;			// 商品id
		public String NUM;				// 1
		
		
//		public String getBANAER() {
//			//banaer  sdsds
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//
//		public String getREGIONTITLE() {
//			//regiontitle sdsds
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//
//		public String getENDTIME() {
//			//endtime  sdsd
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//
//		public String getSTARTTIME() {
//			//starttime sdsd
//			// TODO Auto-generated method stub
//			return null;
//		}


		public String getJOINNUM() {
			return JOIN_NUM;
		}

		public String getIMG() {
			return GOODS_IMG;
		}

		public String getCOMMODITYNAME() {
			return GOODS_NAME;
		}


		public String getPRICE() {
			return GOODS_PRICE;//横线
		}


		public String getLHPRICE() {
			return DISCOUNT_PRICE; //正常显示
		}


		public int getPRODUCEID() {
			return Integer.parseInt(GOODS_ID);
		}
	}

}
