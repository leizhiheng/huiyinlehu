package com.huiyin.ui.store;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by justme on 15/5/9.
 */
public class StoreInfo {

    @SerializedName("USER_FOCUS_STORE")
    @Expose
    private int USERFOCUSSTORE;
    @SerializedName("RECOMMEND_GOODS_LIST")
    @Expose
    private ArrayList<StoreListItem> RECOMMENDGOODSLIST = new ArrayList<StoreListItem>();
    @SerializedName("STORE_CATEGORY")
    @Expose
    private ArrayList<StoreCategory> STORECATEGORY = new ArrayList<StoreCategory>();
    private ArrayList<StoreBanner> STORE_BANNER_LIST = new ArrayList<StoreBanner>();
    private ArrayList<StoreLehu> TICKET_ACTIVE_LIST = new ArrayList<StoreLehu>();
    @Expose
    private String WANGWANG;
    @SerializedName("STORE_NAME")
    @Expose
    private String STORENAME;
    @SerializedName("STORE_LOGO")
    @Expose
    private String STORELOGO;
    @Expose
    private String QQ;
    @SerializedName("NEW_GOODS_LIST")
    @Expose
    private ArrayList<StoreListItem> NEWGOODSLIST = new ArrayList<StoreListItem>();
    @Expose
    private String ID;
    @SerializedName("COUNT_PROMOTION_GOODS")
    @Expose
    private int COUNTPROMOTIONGOODS;
    @SerializedName("STORE_GOODS_NUM")
    @Expose
    private int STOREGOODSNUM;
    @SerializedName("NEW_GOODS_NUM")
    @Expose
    private int NEWGOODSNUM;
    @SerializedName("COUNT_FOCUS_STORE")
    @Expose
    private int COUNTFOCUSSTORE;
    @SerializedName("STORE_URL")
    @Expose
    private String STOREURL;

    /**
     * @return The USERFOCUSSTORE
     */
    public int getUSERFOCUSSTORE() {
        return USERFOCUSSTORE;
    }

    /**
     * @param USERFOCUSSTORE The USER_FOCUS_STORE
     */
    public void setUSERFOCUSSTORE(int USERFOCUSSTORE) {
        this.USERFOCUSSTORE = USERFOCUSSTORE;
    }

    /**
     * @return The RECOMMENDGOODSLIST
     */
    public ArrayList<StoreListItem> getRECOMMENDGOODSLIST() {
        return RECOMMENDGOODSLIST;
    }

    /**
     * @param RECOMMENDGOODSLIST The RECOMMEND_GOODS_LIST
     */
    public void setRECOMMENDGOODSLIST(ArrayList<StoreListItem> RECOMMENDGOODSLIST) {
        this.RECOMMENDGOODSLIST = RECOMMENDGOODSLIST;
    }

    /**
     * @return The STORECATEGORY
     */
    public ArrayList<StoreCategory> getSTORECATEGORY() {
        return STORECATEGORY;
    }

    /**
     * @param STORECATEGORY The STORE_CATEGORY
     */
    public void setSTORECATEGORY(ArrayList<StoreCategory> STORECATEGORY) {
        this.STORECATEGORY = STORECATEGORY;
    }

    /**
     * @return The WANGWANG
     */
    public String getWANGWANG() {
        return WANGWANG;
    }

    /**
     * @param WANGWANG The WANGWANG
     */
    public void setWANGWANG(String WANGWANG) {
        this.WANGWANG = WANGWANG;
    }

    /**
     * @return The STORENAME
     */
    public String getSTORENAME() {
        return STORENAME;
    }

    /**
     * @param STORENAME The STORE_NAME
     */
    public void setSTORENAME(String STORENAME) {
        this.STORENAME = STORENAME;
    }

    /**
     * @return The STORELOGO
     */
    public String getSTORELOGO() {
        return STORELOGO;
    }

    /**
     * @param STORELOGO The STORE_LOGO
     */
    public void setSTORELOGO(String STORELOGO) {
        this.STORELOGO = STORELOGO;
    }

    /**
     * @return The QQ
     */
    public String getQQ() {
        return QQ;
    }

    /**
     * @param QQ The QQ
     */
    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    /**
     * @return The NEWGOODSLIST
     */
    public ArrayList<StoreListItem> getNEWGOODSLIST() {
        return NEWGOODSLIST;
    }

    /**
     * @param NEWGOODSLIST The NEW_GOODS_LIST
     */
    public void setNEWGOODSLIST(ArrayList<StoreListItem> NEWGOODSLIST) {
        this.NEWGOODSLIST = NEWGOODSLIST;
    }

    /**
     * @return The ID
     */
    public String getID() {
        return ID;
    }

    /**
     * @param ID The ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * @return The COUNTPROMOTIONGOODS
     */
    public int getCOUNTPROMOTIONGOODS() {
        return COUNTPROMOTIONGOODS;
    }

    /**
     * @param COUNTPROMOTIONGOODS The COUNT_PROMOTION_GOODS
     */
    public void setCOUNTPROMOTIONGOODS(int COUNTPROMOTIONGOODS) {
        this.COUNTPROMOTIONGOODS = COUNTPROMOTIONGOODS;
    }

    /**
     * @return The STOREGOODSNUM
     */
    public int getSTOREGOODSNUM() {
        return STOREGOODSNUM;
    }

    /**
     * @param STOREGOODSNUM The STORE_GOODS_NUM
     */
    public void setSTOREGOODSNUM(int STOREGOODSNUM) {
        this.STOREGOODSNUM = STOREGOODSNUM;
    }

    /**
     * @return The NEWGOODSNUM
     */
    public int getNEWGOODSNUM() {
        return NEWGOODSNUM;
    }

    /**
     * @param NEWGOODSNUM The NEW_GOODS_NUM
     */
    public void setNEWGOODSNUM(int NEWGOODSNUM) {
        this.NEWGOODSNUM = NEWGOODSNUM;
    }

    /**
     * @return The COUNTFOCUSSTORE
     */
    public int getCOUNTFOCUSSTORE() {
        return COUNTFOCUSSTORE;
    }

    /**
     * @param COUNTFOCUSSTORE The COUNT_FOCUS_STORE
     */
    public void setCOUNTFOCUSSTORE(int COUNTFOCUSSTORE) {
        this.COUNTFOCUSSTORE = COUNTFOCUSSTORE;
    }

    /**
     * @return The STOREURL
     */
    public String getSTOREURL() {
        return STOREURL;
    }

    /**
     * @param STOREURL The STOREURL
     */
    public void setSTOREURL(String STOREURL) {
        this.STOREURL = STOREURL;
    }

    public ArrayList<StoreBanner> getSTORE_BANNER_LIST() {
        return STORE_BANNER_LIST;
    }

    public void setSTORE_BANNER_LIST(ArrayList<StoreBanner> STORE_BANNER_LIST) {
        this.STORE_BANNER_LIST = STORE_BANNER_LIST;
    }

    public ArrayList<StoreLehu> getTICKET_ACTIVE_LIST() {
        return TICKET_ACTIVE_LIST;
    }

    public void setTICKET_ACTIVE_LIST(ArrayList<StoreLehu> TICKET_ACTIVE_LIST) {
        this.TICKET_ACTIVE_LIST = TICKET_ACTIVE_LIST;
    }
}