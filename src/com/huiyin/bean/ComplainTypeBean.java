package com.huiyin.bean;

import java.util.List;

/**
 * Created by kuangyong on 2015/6/9.
 */
public class ComplainTypeBean extends BaseBean{

    /**
     * complainTypeList : [{"NAME":"类型1","ID":13},{"NAME":"类型3","ID":31},{"NAME":"类型4","ID":32}]
     * curTime : 2015-06-09 14:53:08
     * type : 1
     * msg : 成功
     */
    private List<ComplainTypeListEntity> complainTypeList;
    private String curTime;

    public void setComplainTypeList(List<ComplainTypeListEntity> complainTypeList) {
        this.complainTypeList = complainTypeList;
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

    public List<ComplainTypeListEntity> getComplainTypeList() {
        return complainTypeList;
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
}
