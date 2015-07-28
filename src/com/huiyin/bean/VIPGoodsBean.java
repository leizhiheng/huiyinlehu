package com.huiyin.bean;

import java.util.List;

/**
 * Created by Mike on 2015/6/15.
 */
public class VIPGoodsBean {

    /**
     * goodsList : [{"EXCHG_INTEGRAL":"2.00","GOODS_PRICE":"0.00","EXCHG_MODE":0,"ADD_AMOUNT":"-1.00","TICKET_PRICE":null,"TICKET_DEMAND":null,"GIFT_IMG":"upload/image/admin/2015/20150608/201506081537041819.png","HQ_TYPE":null,"GIFT_NAME":"非推荐","EXCHG_TYPE":1,"ID":261,"TICKET_NAME":null,"NUM":1}]
     * totalPageNum : 1
     * curTime : 2015-06-15 11:25:32
     * type : 1
     * msg : 成功
     * pageIndex : 1
     */
    private List<GoodsListEntity> goodsList;
    private int totalPageNum;
    private String curTime;
    private int type;
    private String msg;
    private int pageIndex;

    public void setGoodsList(List<GoodsListEntity> goodsList) {
        this.goodsList = goodsList;
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

    public List<GoodsListEntity> getGoodsList() {
        return goodsList;
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

    public class GoodsListEntity {
        /**
         * EXCHG_INTEGRAL : 2.00
         * GOODS_PRICE : 0.00
         * EXCHG_MODE : 0
         * ADD_AMOUNT : -1.00
         * TICKET_PRICE : null
         * TICKET_DEMAND : null
         * GIFT_IMG : upload/image/admin/2015/20150608/201506081537041819.png
         * HQ_TYPE : null
         * GIFT_NAME : 非推荐
         * EXCHG_TYPE : 1
         * ID : 261
         * TICKET_NAME : null
         * NUM : 1
         */
        private String EXCHG_INTEGRAL;
        private String GOODS_PRICE;
        private int EXCHG_MODE;
        private String ADD_AMOUNT;
        private String TICKET_PRICE;
        private String TICKET_DEMAND;
        private String GIFT_IMG;
        private String HQ_TYPE;
        private String GIFT_NAME;
        private int EXCHG_TYPE;
        private int ID;
        private String TICKET_NAME;
        private int NUM;

        public void setEXCHG_INTEGRAL(String EXCHG_INTEGRAL) {
            this.EXCHG_INTEGRAL = EXCHG_INTEGRAL;
        }

        public void setGOODS_PRICE(String GOODS_PRICE) {
            this.GOODS_PRICE = GOODS_PRICE;
        }

        public void setEXCHG_MODE(int EXCHG_MODE) {
            this.EXCHG_MODE = EXCHG_MODE;
        }

        public void setADD_AMOUNT(String ADD_AMOUNT) {
            this.ADD_AMOUNT = ADD_AMOUNT;
        }

        public void setTICKET_PRICE(String TICKET_PRICE) {
            this.TICKET_PRICE = TICKET_PRICE;
        }

        public void setTICKET_DEMAND(String TICKET_DEMAND) {
            this.TICKET_DEMAND = TICKET_DEMAND;
        }

        public void setGIFT_IMG(String GIFT_IMG) {
            this.GIFT_IMG = GIFT_IMG;
        }

        public void setHQ_TYPE(String HQ_TYPE) {
            this.HQ_TYPE = HQ_TYPE;
        }

        public void setGIFT_NAME(String GIFT_NAME) {
            this.GIFT_NAME = GIFT_NAME;
        }

        public void setEXCHG_TYPE(int EXCHG_TYPE) {
            this.EXCHG_TYPE = EXCHG_TYPE;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public void setTICKET_NAME(String TICKET_NAME) {
            this.TICKET_NAME = TICKET_NAME;
        }

        public void setNUM(int NUM) {
            this.NUM = NUM;
        }

        public String getEXCHG_INTEGRAL() {
            return EXCHG_INTEGRAL;
        }

        public String getGOODS_PRICE() {
            return GOODS_PRICE;
        }

        public int getEXCHG_MODE() {
            return EXCHG_MODE;
        }

        public String getADD_AMOUNT() {
            return ADD_AMOUNT;
        }

        public String getTICKET_PRICE() {
            return TICKET_PRICE;
        }

        public String getTICKET_DEMAND() {
            return TICKET_DEMAND;
        }

        public String getGIFT_IMG() {
            return GIFT_IMG;
        }

        public String getHQ_TYPE() {
            return HQ_TYPE;
        }

        public String getGIFT_NAME() {
            return GIFT_NAME;
        }

        public int getEXCHG_TYPE() {
            return EXCHG_TYPE;
        }

        public int getID() {
            return ID;
        }

        public String getTICKET_NAME() {
            return TICKET_NAME;
        }

        public int getNUM() {
            return NUM;
        }
    }
}
