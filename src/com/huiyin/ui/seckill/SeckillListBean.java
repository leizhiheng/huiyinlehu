package com.huiyin.ui.seckill;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huiyin.bean.BaseBean;

public class SeckillListBean extends BaseBean {
	private static final long serialVersionUID = 1L;
	@Expose
	private ArrayList<SeckillList> seckillList = new ArrayList<SeckillList>();

	@SerializedName("START_TIME")
	@Expose
	private String STARTTIME;
	@SerializedName("END_TIME")
	@Expose
	private String ENDTIME;

	/**
	 * 
	 * @return The seckillList
	 */
	public ArrayList<SeckillList> getSeckillList() {
		return seckillList;
	}

	/**
	 * 
	 * @param seckillList
	 *            The seckillList
	 */
	public void setSeckillList(ArrayList<SeckillList> seckillList) {
		this.seckillList = seckillList;
	}

	/**
	 * 
	 * @return The ENDTIME
	 */
	public String getENDTIME() {
		return ENDTIME;
	}

	/**
	 * 
	 * @param ENDTIME
	 *            The END_TIME
	 */
	public void setENDTIME(String ENDTIME) {
		this.ENDTIME = ENDTIME;
	}

	/**
	 * 
	 * @return The STARTTIME
	 */
	public String getSTARTTIME() {
		return STARTTIME;
	}

	/**
	 * 
	 * @param STARTTIME
	 *            The START_TIME
	 */
	public void setSTARTTIME(String STARTTIME) {
		this.STARTTIME = STARTTIME;
	}

	public class SeckillList {

		@SerializedName("GOODS_PRICE")//商品原价
		@Expose
		private double PRICE;
		@SerializedName("DISCOUNT_PRICE")//商品折扣价格
		@Expose
		private double DISCOUNTPRICE;
		@SerializedName("STORE_ID")//店铺ID
		@Expose
		private int storeId;
		@SerializedName("SECKILL_QUANTITY")//原来的数据 没有使用过
		@Expose
		private int SECKILLQUANTITY;
		@SerializedName("GOODS_ID")//商品ID
		@Expose
		private int COMMODITYID;
		@SerializedName("COLLECT_FLAG")//收藏的标志位
		@Expose
		private int COLLECTFLAG;
		@SerializedName("GOODS_IMG") //商品图片
		@Expose
		private String COMMODITYIMAGEPATH;
		@Expose
		private int NUM;
		@Expose
		private double DISCOUNT;//折扣
		@SerializedName("GOODS_NAME")// 商品名
		@Expose
		private String COMMODITYNAME;
		@SerializedName("SECKILL_NUMBER")
		@Expose
		private int SECKILLNUMBER;
		@SerializedName("GOODS_NO") //商品规格
		@Expose
		private long GOODSNO;
		@SerializedName("DISCOUNT_TYPE") //折扣类型  这里没有使用
		@Expose
		private int DISCOUNTTYPE;

		public int getCOLLECTFLAG() {
			return COLLECTFLAG;
		}

		public void setCOLLECTFLAG(int COLLECTFLAG) {
			this.COLLECTFLAG = COLLECTFLAG;
		}

		public int getCOMMODITYID() {
			return COMMODITYID;
		}

		public void setCOMMODITYID(int COMMODITYID) {
			this.COMMODITYID = COMMODITYID;
		}

		public String getCOMMODITYIMAGEPATH() {
			return COMMODITYIMAGEPATH;
		}

		public void setCOMMODITYIMAGEPATH(String COMMODITYIMAGEPATH) {
			this.COMMODITYIMAGEPATH = COMMODITYIMAGEPATH;
		}

		public String getCOMMODITYNAME() {
			return COMMODITYNAME;
		}

		public void setCOMMODITYNAME(String COMMODITYNAME) {
			this.COMMODITYNAME = COMMODITYNAME;
		}

		public double getDISCOUNT() {
			return DISCOUNT;
		}

		public void setDISCOUNT(double DISCOUNT) {
			this.DISCOUNT = DISCOUNT;
		}

		public double getDISCOUNTPRICE() {
			return DISCOUNTPRICE;
		}

		public void setDISCOUNTPRICE(double DISCOUNTPRICE) {
			this.DISCOUNTPRICE = DISCOUNTPRICE;
		}

		public int getDISCOUNTTYPE() {
			return DISCOUNTTYPE;
		}

		public void setDISCOUNTTYPE(int DISCOUNTTYPE) {
			this.DISCOUNTTYPE = DISCOUNTTYPE;
		}

		public long getGOODSNO() {
			return GOODSNO;
		}

		public void setGOODSNO(long GOODSNO) {
			this.GOODSNO = GOODSNO;
		}

		public int getNUM() {
			return NUM;
		}

		public void setNUM(int NUM) {
			this.NUM = NUM;
		}

		public double getPRICE() {
			return PRICE;
		}

		public void setPRICE(double PRICE) {
			this.PRICE = PRICE;
		}

		public int getSECKILLNUMBER() {
			return SECKILLNUMBER;
		}

		public void setSECKILLNUMBER(int SECKILLNUMBER) {
			this.SECKILLNUMBER = SECKILLNUMBER;
		}

		public int getSECKILLQUANTITY() {
			return SECKILLQUANTITY;
		}

		public void setSECKILLQUANTITY(int SECKILLQUANTITY) {
			this.SECKILLQUANTITY = SECKILLQUANTITY;
		}

		public int getStoreId() {
			return storeId;
		}

		public void setStoreId(int storeId) {
			this.storeId = storeId;
		}
	}

	public static SeckillListBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			SeckillListBean experLightBean = gson.fromJson(json,
					SeckillListBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
