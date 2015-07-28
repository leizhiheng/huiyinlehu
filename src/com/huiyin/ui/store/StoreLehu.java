package com.huiyin.ui.store;

/**
 * Created by Mike on 2015/7/4.
 */
public class StoreLehu {
    private String PRICE;           //抵扣金额
    private String NAME;            //活动名称
    private String RANGE_NAME;      //适用范围
    private String END_TIME;        //结束时间
    private String START_TIME;      //开始时间
    private String ACTIVE_ID;       //激活id
    private String IS_GET;          //0未领取，1已领取

    public String getPRICE() {
        return PRICE;
    }

    public void setPRICE(String PRICE) {
        this.PRICE = PRICE;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getRANGE_NAME() {
        return RANGE_NAME;
    }

    public void setRANGE_NAME(String RANGE_NAME) {
        this.RANGE_NAME = RANGE_NAME;
    }

    public String getEND_TIME() {
        return END_TIME;
    }

    public void setEND_TIME(String END_TIME) {
        this.END_TIME = END_TIME;
    }

    public String getSTART_TIME() {
        return START_TIME;
    }

    public void setSTART_TIME(String START_TIME) {
        this.START_TIME = START_TIME;
    }

    public String getACTIVE_ID() {
        return ACTIVE_ID;
    }

    public void setACTIVE_ID(String ACTIVE_ID) {
        this.ACTIVE_ID = ACTIVE_ID;
    }

    public String getIS_GET() {
        return IS_GET;
    }

    public void setIS_GET(String IS_GET) {
        this.IS_GET = IS_GET;
    }
}
