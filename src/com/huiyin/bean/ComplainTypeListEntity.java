package com.huiyin.bean;

/**
 * Created by kuangyong on 2015/6/9.
 */
public class ComplainTypeListEntity extends BaseBean{

    /**
     * NAME : 类型1
     * ID : 13
     */
    private String NAME;
    private String ID;

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getID() {
        return ID;
    }
}
