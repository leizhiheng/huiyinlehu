package com.huiyin.bean;

import java.util.List;

/**
 * Created by Mike on 2015/6/19.
 */
public class WaterPayRecoderBean {

    /**
     * shareBillData : [{"PAYTIME":"2015-06-17 20:28:49","BLANCE":24900,"REMARK":"系统超时","USERNO":"0109902020","PAYSTATUS":2,"PAYNBR":"CZ14345440597922","NUM":1},{"PAYTIME":null,"BLANCE":24900,"REMARK":null,"USERNO":"0109902020","PAYSTATUS":0,"PAYNBR":"CZ14345436342003","NUM":2},{"PAYTIME":null,"BLANCE":24900,"REMARK":null,"USERNO":"0109902020","PAYSTATUS":0,"PAYNBR":"CZ14345435126069","NUM":3}]
     * totalPageNum : 1
     * curTime : 2015-06-19 17:20:52
     * type : 1
     * msg : 成功
     * pageIndex : -1
     */
    private List<ShareBillDataEntity> shareBillData;
    private int totalPageNum;
    private String curTime;
    private int type;
    private String msg;
    private int pageIndex;

    public void setShareBillData(List<ShareBillDataEntity> shareBillData) {
        this.shareBillData = shareBillData;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<ShareBillDataEntity> getShareBillData() {
        return shareBillData;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public String getCurTime() {
        return curTime;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public class ShareBillDataEntity {
        /**
         * PAYTIME : 2015-06-17 20:28:49
         * BLANCE : 24900
         * REMARK : 系统超时
         * USERNO : 0109902020
         * PAYSTATUS : 2
         * PAYNBR : CZ14345440597922
         * NUM : 1
         */
        private String PAYITEM;
        private String PAYTIME;
        private int BLANCE;
        private String REMARK;
        private String USERNO;
        private int PAYSTATUS;
        private String PAYNBR;
        private int NUM;

        public String getPAYITEM() {
            return PAYITEM;
        }

        public void setPAYITEM(String PAYITEM) {
            this.PAYITEM = PAYITEM;
        }

        public void setPAYTIME(String PAYTIME) {
            this.PAYTIME = PAYTIME;
        }

        public void setBLANCE(int BLANCE) {
            this.BLANCE = BLANCE;
        }

        public void setREMARK(String REMARK) {
            this.REMARK = REMARK;
        }

        public void setUSERNO(String USERNO) {
            this.USERNO = USERNO;
        }

        public void setPAYSTATUS(int PAYSTATUS) {
            this.PAYSTATUS = PAYSTATUS;
        }

        public void setPAYNBR(String PAYNBR) {
            this.PAYNBR = PAYNBR;
        }

        public void setNUM(int NUM) {
            this.NUM = NUM;
        }

        public String getPAYTIME() {
            return PAYTIME;
        }

        public int getBLANCE() {
            return BLANCE;
        }

        public String getREMARK() {
            return REMARK;
        }

        public String getUSERNO() {
            return USERNO;
        }

        public int getPAYSTATUS() {
            return PAYSTATUS;
        }

        public String getPAYNBR() {
            return PAYNBR;
        }

        public int getNUM() {
            return NUM;
        }
    }
}
