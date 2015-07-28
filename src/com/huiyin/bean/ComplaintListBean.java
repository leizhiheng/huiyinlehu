package com.huiyin.bean;

import java.util.List;

/**
 * 投诉列表
 * Created by kuangyong on 2015/6/10.
 */
public class ComplaintListBean extends BaseBean{

    /**
     * totalPageNum : 1
     * curTime : 2015-06-10 11:08:15
     * complainData : [{"ORDER_ID":15286,"STORE_NAME":"乐虎自营","STORE_LOGO":null,"ORDER_CODE":"S1433821310764","complainDetaillList":[{"GOODS_IMG":"/upload/image/admin/2015/20150608/201506081614131268_290X290.jpg","GOODS_PRICE":0.01,"ORDER_ID":15286,"ORDER_DETAIL_ID":20796,"GOODS_NAME":"wap端单规格商品【固定运费5元】","GOODS_NO":431431,"STORE_ID":0,"GOODS_ID":10971}],"ID":431,"STORE_ID":0,"CREATE_DATE":"2015-06-10 10:32:48","AUDIT_STATUS":1,"NUM":2}]
     * type : 1
     * msg : 成功
     * pageIndex : 1
     */
    private String curTime;
    private List<ComplainDataEntity> complainData;


    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }

    public void setComplainData(List<ComplainDataEntity> complainData) {
        this.complainData = complainData;
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

    public List<ComplainDataEntity> getComplainData() {
        return complainData;
    }

    public int getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }
}
