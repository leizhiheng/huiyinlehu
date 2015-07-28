package com.huiyin.bean;

/**
 * Created by Mike on 2015/6/16.
 */
public class VIPExchangeBean {

    /**
     * curTime : 2015-06-16 15:10:01
     * giftDetail : {"EXCHG_INTEGRAL":"2","EXCHG_MODE":"0","GOODS_PRICE":"","ADD_AMOUNT":"-1","TICKET_PRICE":"","IS_NUM_LIMIT":"0","TICKET_DEMAND":"","MIN_GRADE_TITLE":"普通会员","GIFT_IMG":"upload/image/admin/2015/20150608/201506081646488792.gif","HQ_TYPE":"","AD":"恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭","BEGIN_TIME":"","GIFT_NAME":"恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬a","GOODS_STOCK":"","GIFT_MIN_GROWTH":"0","GIFT_STOCK_SUM":"52","EXCHG_TYPE":"1","IS_TIME_LIMIT":"0","ID":"272","EXCHG_MAX_COUNT":"-1","TICKET_NAME":"","END_TIME":"","EXCHG_NOTE":"恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭"}
     * type : 1
     * msg : 成功
     */
    private String curTime;
    private GiftDetailEntity giftDetail;
    private int type;
    private String msg;

    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public void setGiftDetail(GiftDetailEntity giftDetail) {
        this.giftDetail = giftDetail;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCurTime() {
        return curTime;
    }

    public GiftDetailEntity getGiftDetail() {
        return giftDetail;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public class GiftDetailEntity {
        /**
         * EXCHG_INTEGRAL : 2
         * EXCHG_MODE : 0
         * GOODS_PRICE :
         * ADD_AMOUNT : -1
         * TICKET_PRICE :
         * IS_NUM_LIMIT : 0
         * TICKET_DEMAND :
         * MIN_GRADE_TITLE : 普通会员
         * GIFT_IMG : upload/image/admin/2015/20150608/201506081646488792.gif
         * HQ_TYPE :
         * AD : 恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭
         * BEGIN_TIME :
         * GIFT_NAME : 恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬a
         * GOODS_STOCK :
         * GIFT_MIN_GROWTH : 0
         * GIFT_STOCK_SUM : 52
         * EXCHG_TYPE : 1
         * IS_TIME_LIMIT : 0
         * ID : 272
         * EXCHG_MAX_COUNT : -1
         * TICKET_NAME :
         * END_TIME :
         * EXCHG_NOTE : 恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭敬敬恭恭
         */
        private String EXCHG_INTEGRAL; //兑换积分
        private String EXCHG_MODE;  //兑换方式:0(积分),1(积分+金额)
        private String GOODS_PRICE;  //商品价格
        private String ADD_AMOUNT;  //再加金额
        private String TICKET_PRICE;  //虎券金额
        private String IS_NUM_LIMIT;  //限制每会员兑换数量(0:不限制,1:限制)
        private String TICKET_DEMAND;  //虎券要求满足金额
        private String MIN_GRADE_TITLE;  //最小会员等级限制
        private String GIFT_IMG;  //兑换图片（只有一张，不存在左右滑动）,
        private String HQ_TYPE;  //虎券类型 1满减2不限制
        private String AD;  //广告语
        private String BEGIN_TIME;  //开始时间
        private String GIFT_NAME;  // 礼品名称
        private String GOODS_STOCK;  //商品库存
        private String GIFT_MIN_GROWTH;  //最小会员等级限制
        private String GIFT_STOCK_SUM;  //礼品库存
        private String EXCHG_TYPE;  //礼品类型/兑换类型:1(虚拟礼品),2(乐虎红包（券）),3(实物礼品)
        private String IS_TIME_LIMIT;  //限制兑换时间(0:不限制,1:限制)
        private String ID;  //兑换id
        private String EXCHG_MAX_COUNT;  //每会员限兑数量
        private String TICKET_NAME;  // 虎券名称
        private String END_TIME;  //结束时间
        private String EXCHG_NOTE;  //兑换说明
        private String REF_STORE_ID;  //兑换店铺
        private String REF_GOODS_ID;  //兑换商品id
        private String REF_GOODS_NO;  //兑换商品编号

        public void setEXCHG_INTEGRAL(String EXCHG_INTEGRAL) {
            this.EXCHG_INTEGRAL = EXCHG_INTEGRAL;
        }

        public void setEXCHG_MODE(String EXCHG_MODE) {
            this.EXCHG_MODE = EXCHG_MODE;
        }

        public void setGOODS_PRICE(String GOODS_PRICE) {
            this.GOODS_PRICE = GOODS_PRICE;
        }

        public void setADD_AMOUNT(String ADD_AMOUNT) {
            this.ADD_AMOUNT = ADD_AMOUNT;
        }

        public void setTICKET_PRICE(String TICKET_PRICE) {
            this.TICKET_PRICE = TICKET_PRICE;
        }

        public void setIS_NUM_LIMIT(String IS_NUM_LIMIT) {
            this.IS_NUM_LIMIT = IS_NUM_LIMIT;
        }

        public void setTICKET_DEMAND(String TICKET_DEMAND) {
            this.TICKET_DEMAND = TICKET_DEMAND;
        }

        public void setMIN_GRADE_TITLE(String MIN_GRADE_TITLE) {
            this.MIN_GRADE_TITLE = MIN_GRADE_TITLE;
        }

        public void setGIFT_IMG(String GIFT_IMG) {
            this.GIFT_IMG = GIFT_IMG;
        }

        public void setHQ_TYPE(String HQ_TYPE) {
            this.HQ_TYPE = HQ_TYPE;
        }

        public void setAD(String AD) {
            this.AD = AD;
        }

        public void setBEGIN_TIME(String BEGIN_TIME) {
            this.BEGIN_TIME = BEGIN_TIME;
        }

        public void setGIFT_NAME(String GIFT_NAME) {
            this.GIFT_NAME = GIFT_NAME;
        }

        public void setGOODS_STOCK(String GOODS_STOCK) {
            this.GOODS_STOCK = GOODS_STOCK;
        }

        public void setGIFT_MIN_GROWTH(String GIFT_MIN_GROWTH) {
            this.GIFT_MIN_GROWTH = GIFT_MIN_GROWTH;
        }

        public void setGIFT_STOCK_SUM(String GIFT_STOCK_SUM) {
            this.GIFT_STOCK_SUM = GIFT_STOCK_SUM;
        }

        public void setEXCHG_TYPE(String EXCHG_TYPE) {
            this.EXCHG_TYPE = EXCHG_TYPE;
        }

        public void setIS_TIME_LIMIT(String IS_TIME_LIMIT) {
            this.IS_TIME_LIMIT = IS_TIME_LIMIT;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public void setEXCHG_MAX_COUNT(String EXCHG_MAX_COUNT) {
            this.EXCHG_MAX_COUNT = EXCHG_MAX_COUNT;
        }

        public void setTICKET_NAME(String TICKET_NAME) {
            this.TICKET_NAME = TICKET_NAME;
        }

        public void setEND_TIME(String END_TIME) {
            this.END_TIME = END_TIME;
        }

        public void setEXCHG_NOTE(String EXCHG_NOTE) {
            this.EXCHG_NOTE = EXCHG_NOTE;
        }

        public String getEXCHG_INTEGRAL() {
            return EXCHG_INTEGRAL;
        }

        public String getEXCHG_MODE() {
            return EXCHG_MODE;
        }

        public String getGOODS_PRICE() {
            return GOODS_PRICE;
        }

        public String getADD_AMOUNT() {
            return ADD_AMOUNT;
        }

        public String getTICKET_PRICE() {
            return TICKET_PRICE;
        }

        public String getIS_NUM_LIMIT() {
            return IS_NUM_LIMIT;
        }

        public String getTICKET_DEMAND() {
            return TICKET_DEMAND;
        }

        public String getMIN_GRADE_TITLE() {
            return MIN_GRADE_TITLE;
        }

        public String getGIFT_IMG() {
            return GIFT_IMG;
        }

        public String getHQ_TYPE() {
            return HQ_TYPE;
        }

        public String getAD() {
            return AD;
        }

        public String getBEGIN_TIME() {
            return BEGIN_TIME;
        }

        public String getGIFT_NAME() {
            return GIFT_NAME;
        }

        public String getGOODS_STOCK() {
            return GOODS_STOCK;
        }

        public String getGIFT_MIN_GROWTH() {
            return GIFT_MIN_GROWTH;
        }

        public String getGIFT_STOCK_SUM() {
            return GIFT_STOCK_SUM;
        }

        public String getEXCHG_TYPE() {
            return EXCHG_TYPE;
        }

        public String getIS_TIME_LIMIT() {
            return IS_TIME_LIMIT;
        }

        public String getID() {
            return ID;
        }

        public String getEXCHG_MAX_COUNT() {
            return EXCHG_MAX_COUNT;
        }

        public String getTICKET_NAME() {
            return TICKET_NAME;
        }

        public String getEND_TIME() {
            return END_TIME;
        }

        public String getEXCHG_NOTE() {
            return EXCHG_NOTE;
        }

        public String getREF_STORE_ID() {
            return REF_STORE_ID;
        }

        public void setREF_STORE_ID(String REF_STORE_ID) {
            this.REF_STORE_ID = REF_STORE_ID;
        }

        public String getREF_GOODS_NO() {
            return REF_GOODS_NO;
        }

        public void setREF_GOODS_NO(String REF_GOODS_NO) {
            this.REF_GOODS_NO = REF_GOODS_NO;
        }

        public String getREF_GOODS_ID() {
            return REF_GOODS_ID;
        }

        public void setREF_GOODS_ID(String REF_GOODS_ID) {
            this.REF_GOODS_ID = REF_GOODS_ID;
        }
    }
}
