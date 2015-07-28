package com.huiyin.bean;

/**
 * Created by kuangyong on 2015/6/15.
 */
public class HistoryListEntity extends BaseBean {
    /**
     * TIME :  11:55:00
     * LASTIP : 112.84.178.50
     * ID : 15594
     * DATA : 2015-06-15
     * NUM : 1
     * TYPE : 3
     * IP_ADDRESS : 扬州
     */
    private String TIME;//登录时间
    private String LASTIP;//登录id
    private String ID;//日志id
    private String DATA;//登录时间
    private int NUM;
    private int TYPE;//登录端（0 PC端 1 Android端 2IOS端  3 点单机 4  Wap端 5 微信端）,
    private String IP_ADDRESS;//登录城市

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public void setLASTIP(String LASTIP) {
        this.LASTIP = LASTIP;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public void setNUM(int NUM) {
        this.NUM = NUM;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public void setIP_ADDRESS(String IP_ADDRESS) {
        this.IP_ADDRESS = IP_ADDRESS;
    }

    public String getTIME() {
        return TIME;
    }

    public String getLASTIP() {
        return LASTIP;
    }

    public String getID() {
        return ID;
    }

    public String getDATA() {
        return DATA;
    }

    public int getNUM() {
        return NUM;
    }

    public int getTYPE() {
        return TYPE;
    }

    public String getIP_ADDRESS() {
        return IP_ADDRESS;
    }
}
