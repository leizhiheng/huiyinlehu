package com.huiyin.bean;

import android.text.TextUtils;

/**
 * Created by lian on 2015/5/30.
 */
public class ReturnListEntity extends BaseBean{

    /**
     * NAME : 不适合
     * ID : 131
     */
    private String NAME;
    private int ID;

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNAME() {
        return NAME;
    }

    public int getID() {
        return ID;
    }
    
    public boolean isEmpty(){
    	return TextUtils.isEmpty(NAME) || 0 == ID;
    }
}
