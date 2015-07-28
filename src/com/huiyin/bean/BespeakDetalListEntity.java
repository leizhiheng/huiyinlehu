package com.huiyin.bean;

import org.json.JSONObject;

/**
 * Created by lian on 2015/5/30.
 */
public class BespeakDetalListEntity extends BaseBean {

    /**
     * GOODS_IMG : /attached/image/2014/20141124/201411240916566253.jpg
     * GOODS_PRICE : 3998
     * PRIMARY_ID : 583
     * STORE_ID : 0
     * GOODS_NO : 80779
     * GOODS_NAME : 三洋洗衣机DG-L7533BXS 7.5公斤全自动滚筒洗衣机
     * GOODS_ID : 969
     * NUM : 6
     * NORMS_VALUE : <span>null</span>
     */
    private String GOODS_IMG;//商品图片
    private String GOODS_PRICE;//商品
    private String PRIMARY_ID;//预约id
    private String STORE_ID;//店铺id
    private String GOODS_NO;//商品编号
    private String GOODS_NAME;//商品名称
    private String GOODS_ID;//商品id
    private String NUM;
    private String NORMS_VALUE;//规格值

    
    public BespeakDetalListEntity(JSONObject obj){
    	GOODS_IMG = obj.optString("GOODS_IMG");//商品图片
    	GOODS_PRICE = obj.optString("GOODS_PRICE");//商品
    	PRIMARY_ID = obj.optString("PRIMARY_ID");//预约id
    	STORE_ID = obj.optString("STORE_ID");//店铺id
    	GOODS_NO = obj.optString("GOODS_NO");//商品编号
    	GOODS_NAME = obj.optString("GOODS_NAME");//商品名称
    	GOODS_ID = obj.optString("GOODS_ID");//商品id
    	NUM = obj.optString("NUM");
    	NORMS_VALUE = obj.optString("NORMS_VALUE");//规格值
    }
    
    public void setGOODS_IMG(String GOODS_IMG) {
        this.GOODS_IMG = GOODS_IMG;
    }

    public void setGOODS_PRICE(String GOODS_PRICE) {
        this.GOODS_PRICE = GOODS_PRICE;
    }

    public void setPRIMARY_ID(String PRIMARY_ID) {
        this.PRIMARY_ID = PRIMARY_ID;
    }

    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID = STORE_ID;
    }

    public void setGOODS_NO(String GOODS_NO) {
        this.GOODS_NO = GOODS_NO;
    }

    public void setGOODS_NAME(String GOODS_NAME) {
        this.GOODS_NAME = GOODS_NAME;
    }

    public void setGOODS_ID(String GOODS_ID) {
        this.GOODS_ID = GOODS_ID;
    }

    public void setNUM(String NUM) {
        this.NUM = NUM;
    }

    public void setNORMS_VALUE(String NORMS_VALUE) {
        this.NORMS_VALUE = NORMS_VALUE;
    }

    public String getGOODS_IMG() {
        return GOODS_IMG;
    }

    public String getGOODS_PRICE() {
        return GOODS_PRICE;
    }

    public String getPRIMARY_ID() {
        return PRIMARY_ID;
    }

    public String getSTORE_ID() {
        return STORE_ID;
    }

    public String getGOODS_NO() {
        return GOODS_NO;
    }

    public String getGOODS_NAME() {
        return GOODS_NAME;
    }

    public String getGOODS_ID() {
        return GOODS_ID;
    }

    public String getNUM() {
        return NUM;
    }

    public String getNORMS_VALUE() {
        return NORMS_VALUE;
    }
}
