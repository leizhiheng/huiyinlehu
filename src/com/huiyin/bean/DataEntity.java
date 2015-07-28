package com.huiyin.bean;

import java.io.Serializable;

public class DataEntity implements Serializable{
	
    public String time;         //第三方物流跟踪时间 e.g： 2015-06-11 13:29:38
    public String areaName;     //第三方物流跟踪城市
    public String areaCode;     //第三方物流跟踪城市编号
    public String status;       //第三方物流状态
    public String context;      //第三方物流跟踪详细内容
    public String ftime;        //第三方物流跟踪时间 e.g： 2015-06-11 13:29:38
}