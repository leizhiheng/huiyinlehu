package com.huiyin.bean;

/**
 * Created by kuangyong on 2015/6/10.
 */
public class ComplainDetaillListEntity extends BaseBean{

    /**
     * GOODS_IMG : /upload/image/admin/2015/20150608/201506081614131268_290X290.jpg
     * GOODS_PRICE : 0.01
     * ORDER_ID : 15286
     * ORDER_DETAIL_ID : 20796
     * GOODS_NAME : wap端单规格商品【固定运费5元】
     * GOODS_NO : 431431
     * STORE_ID : 0
     * GOODS_ID : 10971
     */
    private String GOODS_IMG;//商品图片
    private double GOODS_PRICE;//商品价格
    private String ORDER_ID;//订单id
    private String ORDER_DETAIL_ID;//订单详情id
    private String GOODS_NAME;//商品名臣
    private String GOODS_NO;//商品规格
    private String STORE_ID;//店铺id
    private String GOODS_ID;//商品id

    public void setGOODS_IMG(String GOODS_IMG) {
        this.GOODS_IMG = GOODS_IMG;
    }

    public void setGOODS_PRICE(double GOODS_PRICE) {
        this.GOODS_PRICE = GOODS_PRICE;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public void setORDER_DETAIL_ID(String ORDER_DETAIL_ID) {
        this.ORDER_DETAIL_ID = ORDER_DETAIL_ID;
    }

    public void setGOODS_NAME(String GOODS_NAME) {
        this.GOODS_NAME = GOODS_NAME;
    }

    public void setGOODS_NO(String GOODS_NO) {
        this.GOODS_NO = GOODS_NO;
    }

    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID = STORE_ID;
    }

    public void setGOODS_ID(String GOODS_ID) {
        this.GOODS_ID = GOODS_ID;
    }

    public String getGOODS_IMG() {
        return GOODS_IMG;
    }

    public double getGOODS_PRICE() {
        return GOODS_PRICE;
    }

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public String getORDER_DETAIL_ID() {
        return ORDER_DETAIL_ID;
    }

    public String getGOODS_NAME() {
        return GOODS_NAME;
    }

    public String getGOODS_NO() {
        return GOODS_NO;
    }

    public String getSTORE_ID() {
        return STORE_ID;
    }

    public String getGOODS_ID() {
        return GOODS_ID;
    }
}
