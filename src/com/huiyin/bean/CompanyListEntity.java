package com.huiyin.bean;

/**
 * Created by lian on 2015/6/1.
 */
public class CompanyListEntity extends BaseBean{

    /**
     * COMPANY_NAME : 百世汇通
     * ID : 7
     */
    private String COMPANY_NAME;//公司名称
    private int ID;//公司id

    public void setCOMPANY_NAME(String COMPANY_NAME) {
        this.COMPANY_NAME = COMPANY_NAME;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCOMPANY_NAME() {
        return COMPANY_NAME;
    }

    public int getID() {
        return ID;
    }
}
