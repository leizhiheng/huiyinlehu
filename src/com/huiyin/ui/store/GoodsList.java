package com.huiyin.ui.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by justme on 15/5/14.
 */
public class GoodsList {

    @SerializedName("GOODS_IMG")
    @Expose
    private String GOODSIMG;
    @Expose
    private float PRICE;
    @SerializedName("REVIEW_NUMBER")
    @Expose
    private int REVIEWNUMBER;
    @SerializedName("STORE_ID")
    @Expose
    private int STOREID;
    @SerializedName("GOODS_NAME")
    @Expose
    private String GOODSNAME;
    @SerializedName("GOODS_NO")
    @Expose
    private long GOODSNO;
    @SerializedName("GOODS_ID")
    @Expose
    private int GOODSID;
    @Expose
    private int NUM;
    @SerializedName("REVIEW_PERCENT")
    @Expose
    private String REVIEWPERCENT;
    @Expose
    private String SHELVESTIME;

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
     * @return The PRICE
     */
    public float getPRICE() {
        return PRICE;
    }

    /**
     * @param PRICE The PRICE
     */
    public void setPRICE(float PRICE) {
        this.PRICE = PRICE;
    }

    /**
     * @return The REVIEWNUMBER
     */
    public int getREVIEWNUMBER() {
        return REVIEWNUMBER;
    }

    /**
     * @param REVIEWNUMBER The REVIEW_NUMBER
     */
    public void setREVIEWNUMBER(int REVIEWNUMBER) {
        this.REVIEWNUMBER = REVIEWNUMBER;
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
     * @return The GOODSID
     */
    public int getGOODSID() {
        return GOODSID;
    }

    /**
     * @param GOODSID The GOODS_ID
     */
    public void setGOODSID(int GOODSID) {
        this.GOODSID = GOODSID;
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

    /**
     * @return The REVIEWPERCENT
     */
    public String getREVIEWPERCENT() {
        return REVIEWPERCENT;
    }

    /**
     * @param REVIEWPERCENT The REVIEW_PERCENT
     */
    public void setREVIEWPERCENT(String REVIEWPERCENT) {
        this.REVIEWPERCENT = REVIEWPERCENT;
    }

    /**
     * @return The SHELVESTIME
     */
    public String getSHELVESTIME() {
        return SHELVESTIME;
    }

    /**
     * @param SHELVESTIME The REVIEW_PERCENT
     */
    public void setSHELVESTIME(String SHELVESTIME) {
        this.SHELVESTIME = SHELVESTIME;
    }


}