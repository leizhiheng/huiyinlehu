package com.huiyin.bean;

import java.util.List;

/**
 * Created by kuangyong on 2015/6/15.
 */
public class LoginLogBean extends BaseBean{

    /**
     * historyList : [{"TIME":" 11:55:00","LASTIP":"112.84.178.50","ID":15594,"DATA":"2015-06-15","NUM":1,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:48:24","LASTIP":"112.84.178.50","ID":15591,"DATA":"2015-06-15","NUM":2,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:28:34","LASTIP":"112.84.178.50","ID":15586,"DATA":"2015-06-15","NUM":3,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:27:42","LASTIP":"112.84.178.50","ID":15585,"DATA":"2015-06-15","NUM":4,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:27:00","LASTIP":"112.84.178.50","ID":15583,"DATA":"2015-06-15","NUM":5,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:24:00","LASTIP":"112.84.178.50","ID":15581,"DATA":"2015-06-15","NUM":6,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:23:02","LASTIP":"112.84.178.50","ID":15580,"DATA":"2015-06-15","NUM":7,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:16:16","LASTIP":"112.84.178.50","ID":15578,"DATA":"2015-06-15","NUM":8,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:15:30","LASTIP":"112.84.178.50","ID":15577,"DATA":"2015-06-15","NUM":9,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:07:47","LASTIP":"112.84.178.50","ID":15574,"DATA":"2015-06-15","NUM":10,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:02:34","LASTIP":"112.84.178.50","ID":15572,"DATA":"2015-06-15","NUM":11,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 11:02:05","LASTIP":"112.84.178.50","ID":15571,"DATA":"2015-06-15","NUM":12,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 10:58:41","LASTIP":"112.84.178.50","ID":15569,"DATA":"2015-06-15","NUM":13,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 10:57:58","LASTIP":"112.84.178.50","ID":15568,"DATA":"2015-06-15","NUM":14,"TYPE":3,"IP_ADDRESS":"扬州"},{"TIME":" 10:52:07","LASTIP":"112.84.178.50","ID":15564,"DATA":"2015-06-15","NUM":15,"TYPE":3,"IP_ADDRESS":"扬州"}]
     * totalPageNum : 88
     * curTime : 2015-06-15 14:09:11
     * type : 1
     * msg : 成功
     * pageIndex : 1
     */
    private List<HistoryListEntity> historyList;
    private int totalPageNum;//总共条数
    private String curTime;
    private int pageIndex;

    public void setHistoryList(List<HistoryListEntity> historyList) {
        this.historyList = historyList;
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

    public List<HistoryListEntity> getHistoryList() {
        return historyList;
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


}
