package com.huiyin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Mike on 2015/6/17.
 */
public class ExchangeResultBean implements Serializable{

    private List<VIPInfoBean.RecordDataEntity> recordData;
    private String curTime;
    private int type;
    private String msg;
    private int integral;

    public void setRecordData(List<VIPInfoBean.RecordDataEntity> recordData) {
        this.recordData = recordData;
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

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public List<VIPInfoBean.RecordDataEntity> getRecordData() {
        return recordData;
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

    public int getIntegral() {
        return integral;
    }
}
