package com.huiyin.ui.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by justme on 15/5/9.
 */
public class StoreListItem {
    @SerializedName("GOODS_IMG") //图片
    @Expose
    private String GOODSIMG;
    @SerializedName("GOODS_PRICE") //商品价格
    @Expose
    private float GOODSPRICE;
    @SerializedName("GOODS_STOCK") //商品库存
    @Expose
    private int GOODSSTOCK;
    @Expose
    private int ID; //商品ID
    @SerializedName("GOODS_NO") //商品规格
    @Expose
    private long GOODSNO;
    @SerializedName("STORE_ID") //店铺ID
    @Expose
    private int STOREID;
    @SerializedName("GOODS_NAME") //商品名
    @Expose
    private String GOODSNAME;
    @Expose
    private int NUM;

    /**
     * @return The GOODSIMG
     */
    public String getGOODSIMG() {
        return GOODSIMG;
    }

    /**
     * @param GOODSIMG The GOODS_IMG
     */
    public void setGOODSIMG(String GOODSIMG) {
        this.GOODSIMG = GOODSIMG;
    }

    /**
     * @return The GOODSPRICE
     */
    public float getGOODSPRICE() {
        return GOODSPRICE;
    }

    /**
     * @param GOODSPRICE The GOODS_PRICE
     */
    public void setGOODSPRICE(float GOODSPRICE) {
        this.GOODSPRICE = GOODSPRICE;
    }

    /**
     * @return The GOODSSTOCK
     */
    public int getGOODSSTOCK() {
        return GOODSSTOCK;
    }

    /**
     * @param GOODSSTOCK The GOODS_STOCK
     */
    public void setGOODSSTOCK(int GOODSSTOCK) {
        this.GOODSSTOCK = GOODSSTOCK;
    }

    /**
     * @return The ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID The ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return The GOODSNO
     */
    public long getGOODSNO() {
        return GOODSNO;
    }

    /**
     * @param GOODSNO The GOODS_NO
     */
    public void setGOODSNO(long GOODSNO) {
        this.GOODSNO = GOODSNO;
    }

    /**
     * @return The STOREID
     */
    public int getSTOREID() {
        return STOREID;
    }

    /**
     * @param STOREID The STORE_ID
     */
    public void setSTOREID(int STOREID) {
        this.STOREID = STOREID;
    }

    /**
     * @return The GOODSNAME
     */
    public String getGOODSNAME() {
        return GOODSNAME;
    }

    /**
     * @param GOODSNAME The GOODS_NAME
     */
    public void setGOODSNAME(String GOODSNAME) {
        this.GOODSNAME = GOODSNAME;
    }

    /**
     * @return The NUM
     */
    public int getNUM() {
        return NUM;
    }

    /**
     * @param NUM The NUM
     */
    public void setNUM(int NUM) {
        this.NUM = NUM;
    }
}
