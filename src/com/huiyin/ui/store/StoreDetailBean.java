package com.huiyin.ui.store;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huiyin.bean.BaseBean;

import java.util.ArrayList;

/**
 * Created by justme on 15/5/11.
 */
public class StoreDetailBean extends BaseBean {

    @Expose
    private StoreDe storeInfo;
    @Expose
    private String curTime;

    /**
     * @return The storeInfo
     */
    public StoreDe getStoreInfo() {
        return storeInfo;
    }

    /**
     * @param storeInfo The storeInfo
     */
    public void setStoreInfo(StoreDe storeInfo) {
        this.storeInfo = storeInfo;
    }

    /**
     * @return The curTime
     */
    public String getCurTime() {
        return curTime;
    }

    /**
     * @param curTime The curTime
     */
    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }


    public class StoreDe {

        @SerializedName("USER_FOCUS_STORE")
        @Expose
        private int USERFOCUSSTORE; //用户关注1关注，0没有关注
        @SerializedName("DIFFER_SPEED")
        @Expose
        private String DIFFERSPEED; //高于同行(描述相符 )
        @SerializedName("COMMPANY_NAME")
        @Expose
        private String COMMPANYNAME; //公司名称
        @SerializedName("COMMPANY_PROVINCE_NAME")
        @Expose
        private String COMMPANYPROVINCENAME; //省
        @SerializedName("STORE_CATEGORY")
        @Expose
        private ArrayList<StoreCategory> STORECATEGORY = new ArrayList<StoreCategory>(); //店铺分类
        @SerializedName("STORE_SERVER")
        @Expose
        private float STORESERVER; //服务评分
        @Expose
        private String QQ; //QQ号（如果为空，则不显示QQ图标）
        @SerializedName("DIFFER_SERVER")
        @Expose
        private String DIFFERSERVER;//高于同行（服务评分）
        @Expose
        private String ID;//店铺id
        @SerializedName("COUNT_PROMOTION_GOODS")
        @Expose
        private int COUNTPROMOTIONGOODS; //店铺促销商品数量
        @SerializedName("DIFFER_PACK")
        @Expose
        private String DIFFERPACK; //高于同行（发货速度）
        @SerializedName("COMMPANY_CITY_NAME")
        @Expose
        private String COMMPANYCITYNAME;//市
        @SerializedName("COUNT_FOCUS_STORE")
        @Expose
        private int COUNTFOCUSSTORE; //店铺关注
        @SerializedName("STORE_SPEED")
        @Expose
        private float STORESPEED; //描述相符
        @Expose
        private String WANGWANG; //旺旺号（如果为空，则不显示旺旺图标）
        @SerializedName("STORE_LOGO")
        @Expose
        private String STORELOGO; //店铺logo
        @SerializedName("COMMPANY_AREA_NAME")
        @Expose
        private String COMMPANYAREANAME; //区
        @SerializedName("STORE_SCALE_AVG")
        @Expose
        private float storeScaleAvg; //综合评分
        @SerializedName("NEW_GOODS_NUM")
        @Expose
        private int NEWGOODSNUM; //新品数量
        @SerializedName("STORE_PACK")
        @Expose
        private float STOREPACK; //发货速度
        @SerializedName("COMMPANY_DETAIL")
        @Expose
        private String COMMPANYDETAIL; //详细地址,
        @SerializedName("STORE_NAME")
        @Expose
        private String STORENAME; //店铺名称
        @SerializedName("STORE_GOODS_NUM")
        @Expose
        private int STOREGOODSNUM; //店铺商品数量
        @Expose
        private StoreNavigation storeNavigation; //店铺导航栏（即”品牌介绍”）
        @Expose
        private String UPDATETIME;
        @SerializedName("STORE_URL")
        @Expose
        private String STOREURL;
        @Expose
        private String CREATETIME;//开店时间


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
         * @return The DIFFERSPEED
         */
        public String getDIFFERSPEED() {
            return DIFFERSPEED;
        }

        /**
         * @param DIFFERSPEED The DIFFER_SPEED
         */
        public void setDIFFERSPEED(String DIFFERSPEED) {
            this.DIFFERSPEED = DIFFERSPEED;
        }

        /**
         * @return The COMMPANYNAME
         */
        public String getCOMMPANYNAME() {
            return COMMPANYNAME;
        }

        /**
         * @param COMMPANYNAME The COMMPANY_NAME
         */
        public void setCOMMPANYNAME(String COMMPANYNAME) {
            this.COMMPANYNAME = COMMPANYNAME;
        }

        /**
         * @return The COMMPANYPROVINCENAME
         */
        public String getCOMMPANYPROVINCENAME() {
            return COMMPANYPROVINCENAME;
        }

        /**
         * @param COMMPANYPROVINCENAME The COMMPANY_PROVINCE_NAME
         */
        public void setCOMMPANYPROVINCENAME(String COMMPANYPROVINCENAME) {
            this.COMMPANYPROVINCENAME = COMMPANYPROVINCENAME;
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
         * @return The STORESERVER
         */
        public float getSTORESERVER() {
            return STORESERVER;
        }

        /**
         * @param STORESERVER The STORE_SERVER
         */
        public void setSTORESERVER(float STORESERVER) {
            this.STORESERVER = STORESERVER;
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
         * @return The DIFFERSERVER
         */
        public String getDIFFERSERVER() {
            return DIFFERSERVER;
        }

        /**
         * @param DIFFERSERVER The DIFFER_SERVER
         */
        public void setDIFFERSERVER(String DIFFERSERVER) {
            this.DIFFERSERVER = DIFFERSERVER;
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
         * @return The DIFFERPACK
         */
        public String getDIFFERPACK() {
            return DIFFERPACK;
        }

        /**
         * @param DIFFERPACK The DIFFER_PACK
         */
        public void setDIFFERPACK(String DIFFERPACK) {
            this.DIFFERPACK = DIFFERPACK;
        }

        /**
         * @return The COMMPANYCITYNAME
         */
        public String getCOMMPANYCITYNAME() {
            return COMMPANYCITYNAME;
        }

        /**
         * @param COMMPANYCITYNAME The COMMPANY_CITY_NAME
         */
        public void setCOMMPANYCITYNAME(String COMMPANYCITYNAME) {
            this.COMMPANYCITYNAME = COMMPANYCITYNAME;
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
         * @return The STORESPEED
         */
        public float getSTORESPEED() {
            return STORESPEED;
        }

        /**
         * @param STORESPEED The STORE_SPEED
         */
        public void setSTORESPEED(float STORESPEED) {
            this.STORESPEED = STORESPEED;
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
         * @return The COMMPANYAREANAME
         */
        public String getCOMMPANYAREANAME() {
            return COMMPANYAREANAME;
        }

        /**
         * @param COMMPANYAREANAME The COMMPANY_AREA_NAME
         */
        public void setCOMMPANYAREANAME(String COMMPANYAREANAME) {
            this.COMMPANYAREANAME = COMMPANYAREANAME;
        }

        /**
         * @return The storeScaleAvg
         */
        public float getStoreScaleAvg() {
            return storeScaleAvg;
        }

        /**
         * @param storeScaleAvg The store_scale_avg
         */
        public void setStoreScaleAvg(float storeScaleAvg) {
            this.storeScaleAvg = storeScaleAvg;
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
         * @return The STOREPACK
         */
        public float getSTOREPACK() {
            return STOREPACK;
        }

        /**
         * @param STOREPACK The STORE_PACK
         */
        public void setSTOREPACK(float STOREPACK) {
            this.STOREPACK = STOREPACK;
        }

        /**
         * @return The COMMPANYDETAIL
         */
        public String getCOMMPANYDETAIL() {
            return COMMPANYDETAIL;
        }

        /**
         * @param COMMPANYDETAIL The COMMPANY_DETAIL
         */
        public void setCOMMPANYDETAIL(String COMMPANYDETAIL) {
            this.COMMPANYDETAIL = COMMPANYDETAIL;
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
         * @return The storeNavigation
         */
        public StoreNavigation getStoreNavigation() {
            return storeNavigation;
        }

        /**
         * @param storeNavigation The storeNavigation
         */
        public void setStoreNavigation(StoreNavigation storeNavigation) {
            this.storeNavigation = storeNavigation;
        }

        /**
         * @return The UPDATETIME
         */
        public String getUPDATETIME() {
            return UPDATETIME;
        }

        /**
         * @param UPDATETIME The UPDATETIME
         */
        public void setUPDATETIME(String UPDATETIME) {
            this.UPDATETIME = UPDATETIME;
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
        /**
         * @return The CREATETIME
         */
        public String getCREATETIME() {
            return CREATETIME;
        }

        /**
         * @param CREATETIME The CREATETIME
         */
        public void setCREATETIME(String CREATETIME) {
            this.CREATETIME = CREATETIME;
        }

        public class StoreNavigation {

            @Expose
            private String NAME;
            @Expose
            private String CONTENT;

            /**
             * @return The NAME
             */
            public String getNAME() {
                return NAME;
            }

            /**
             * @param NAME The NAME
             */
            public void setNAME(String NAME) {
                this.NAME = NAME;
            }

            /**
             * @return The CONTENT
             */
            public String getCONTENT() {
                return CONTENT;
            }

            /**
             * @param CONTENT The CONTENT
             */
            public void setCONTENT(String CONTENT) {
                this.CONTENT = CONTENT;
            }
        }
    }

    public static StoreDetailBean explainJson(String json, Context context) {
        Gson gson = new Gson();
        try {
            StoreDetailBean experLightBean = gson.fromJson(json,
                    StoreDetailBean.class);
            return experLightBean;
        } catch (Exception e) {
            Log.d("dataExplainJson", e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
