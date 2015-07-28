package com.huiyin.bean;

public class HomeSeckillListItemBean {
	public float GOODS_PRICE;// 商品价格
	public float DISCOUNT;// 折扣
	public String GOODS_ID;// 商品id ,
	public String STORE_ID;// 店铺id
	public String GOODS_NO;  //商品规格
	public String GOODS_IMG;// 商品主图
    public float PRICE;//省多少;
	public int NUM;// 1
	public String GOODS_NAME;// 商品名称

	public float getPRICE() {
		return GOODS_PRICE;
	}


    public void setPRICE(float pRICE) {
        GOODS_PRICE = pRICE;
	}

	public String getCOMMODITY_IMAGE_PATH() {
		return GOODS_IMG;
	}

	public void setCOMMODITY_IMAGE_PATH(String cOMMODITY_IMAGE_PATH) {
        GOODS_IMG = cOMMODITY_IMAGE_PATH;
	}

	public int getNUM() {
		return NUM;
	}

	public void setNUM(int nUM) {
		NUM = nUM;
	}

	public String getCOMMODITY_NAME() {
		return GOODS_NAME;
	}

	public void setCOMMODITY_NAME(String cOMMODITY_NAME) {
        GOODS_NAME = cOMMODITY_NAME;
	}

	public float getDISCOUNT() {
		return DISCOUNT;
	}

	public void setDISCOUNT(float dISCOUNT) {
		DISCOUNT = dISCOUNT;
	}

}
