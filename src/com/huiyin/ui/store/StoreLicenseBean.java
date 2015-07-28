package com.huiyin.ui.store;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huiyin.bean.BaseBean;

/**
 * Created by justme on 15/5/15.
 */
public class StoreLicenseBean extends BaseBean{

    @Expose
    private StoreLicense BLInfo;
    @Expose
    private String curTime;

    /**
     *
     * @return
     * The BLInfo
     */
    public StoreLicense getBLInfo() {
        return BLInfo;
    }

    /**
     *
     * @param BLInfo
     * The BLInfo
     */
    public void setBLInfo(StoreLicense BLInfo) {
        this.BLInfo = BLInfo;
    }

    /**
     *
     * @return
     * The curTime
     */
    public String getCurTime() {
        return curTime;
    }

    /**
     *
     * @param curTime
     * The curTime
     */
    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public class StoreLicense {

        @SerializedName("WEB_URL")
        @Expose
        private String WEBURL; //店铺网址
        @SerializedName("COMMPANY_ADDRESS")
        @Expose
        private String COMMPANYADDRESS; //公司地址
        @SerializedName("STORE_NAME")
        @Expose
        private String STORENAME; //店铺名称
        @SerializedName("COMMPANY_LINKMAN_NAME")
        @Expose
        private String COMMPANYLINKMANNAME;//公司联系人姓名,(将”法定代表人姓名”改为“公司联系人姓名)
        @SerializedName("COMMPANY_NAME")
        @Expose
        private String COMMPANYNAME;//公司名称（将“企业名称”改为“公司名称）
        @SerializedName("COMMPANY_REGISTERED_CAPITAL")
        @Expose
        private String COMMPANYREGISTEREDCAPITAL;//企业注册资金(后面单位”万元”固定死的)
        @SerializedName("BUSINESS_LICENSE")
        @Expose
        private String BUSINESSLICENSE;//营业执照有效期
        @SerializedName("BL_ADDRESS")
        @Expose
        private String BLADDRESS;//营业执照所在地
        @SerializedName("BUSINESS_LICENSE_NO")
        @Expose
        private String BUSINESSLICENSENO;//营业执照注册号
        @SerializedName("BUSINESS_LICENSE_SCOPE")
        @Expose
        private String BUSINESSLICENSESCOPE;//营业执照经营范围

        /**
         *
         * @return
         * The WEBURL
         */
        public String getWEBURL() {
            return WEBURL;
        }

        /**
         *
         * @param WEBURL
         * The WEB_URL
         */
        public void setWEBURL(String WEBURL) {
            this.WEBURL = WEBURL;
        }

        /**
         *
         * @return
         * The COMMPANYADDRESS
         */
        public String getCOMMPANYADDRESS() {
            return COMMPANYADDRESS;
        }

        /**
         *
         * @param COMMPANYADDRESS
         * The COMMPANY_ADDRESS
         */
        public void setCOMMPANYADDRESS(String COMMPANYADDRESS) {
            this.COMMPANYADDRESS = COMMPANYADDRESS;
        }

        /**
         *
         * @return
         * The STORENAME
         */
        public String getSTORENAME() {
            return STORENAME;
        }

        /**
         *
         * @param STORENAME
         * The STORE_NAME
         */
        public void setSTORENAME(String STORENAME) {
            this.STORENAME = STORENAME;
        }

        /**
         *
         * @return
         * The COMMPANYLINKMANNAME
         */
        public String getCOMMPANYLINKMANNAME() {
            return COMMPANYLINKMANNAME;
        }

        /**
         *
         * @param COMMPANYLINKMANNAME
         * The COMMPANY_LINKMAN_NAME
         */
        public void setCOMMPANYLINKMANNAME(String COMMPANYLINKMANNAME) {
            this.COMMPANYLINKMANNAME = COMMPANYLINKMANNAME;
        }

        /**
         *
         * @return
         * The COMMPANYNAME
         */
        public String getCOMMPANYNAME() {
            return COMMPANYNAME;
        }

        /**
         *
         * @param COMMPANYNAME
         * The COMMPANY_NAME
         */
        public void setCOMMPANYNAME(String COMMPANYNAME) {
            this.COMMPANYNAME = COMMPANYNAME;
        }

        /**
         *
         * @return
         * The COMMPANYREGISTEREDCAPITAL
         */
        public String getCOMMPANYREGISTEREDCAPITAL() {
            return COMMPANYREGISTEREDCAPITAL;
        }

        /**
         *
         * @param COMMPANYREGISTEREDCAPITAL
         * The COMMPANY_REGISTERED_CAPITAL
         */
        public void setCOMMPANYREGISTEREDCAPITAL(String COMMPANYREGISTEREDCAPITAL) {
            this.COMMPANYREGISTEREDCAPITAL = COMMPANYREGISTEREDCAPITAL;
        }

        /**
         *
         * @return
         * The BUSINESSLICENSE
         */
        public String getBUSINESSLICENSE() {
            return BUSINESSLICENSE;
        }

        /**
         *
         * @param BUSINESSLICENSE
         * The BUSINESS_LICENSE
         */
        public void setBUSINESSLICENSE(String BUSINESSLICENSE) {
            this.BUSINESSLICENSE = BUSINESSLICENSE;
        }

        /**
         *
         * @return
         * The BLADDRESS
         */
        public String getBLADDRESS() {
            return BLADDRESS;
        }

        /**
         *
         * @param BLADDRESS
         * The BL_ADDRESS
         */
        public void setBLADDRESS(String BLADDRESS) {
            this.BLADDRESS = BLADDRESS;
        }

        /**
         *
         * @return
         * The BUSINESSLICENSENO
         */
        public String getBUSINESSLICENSENO() {
            return BUSINESSLICENSENO;
        }

        /**
         *
         * @param BUSINESSLICENSENO
         * The BUSINESS_LICENSE_NO
         */
        public void setBUSINESSLICENSENO(String BUSINESSLICENSENO) {
            this.BUSINESSLICENSENO = BUSINESSLICENSENO;
        }

        /**
         *
         * @return
         * The BUSINESSLICENSESCOPE
         */
        public String getBUSINESSLICENSESCOPE() {
            return BUSINESSLICENSESCOPE;
        }

        /**
         *
         * @param BUSINESSLICENSESCOPE
         * The BUSINESS_LICENSE_SCOPE
         */
        public void setBUSINESSLICENSESCOPE(String BUSINESSLICENSESCOPE) {
            this.BUSINESSLICENSESCOPE = BUSINESSLICENSESCOPE;
        }

    }

    public static StoreLicenseBean explainJson(String json, Context context) {
        Gson gson = new Gson();
        try {
            StoreLicenseBean experLightBean = gson.fromJson(json,
                    StoreLicenseBean.class);
            return experLightBean;
        } catch (Exception e) {
            Log.d("dataExplainJson", e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
