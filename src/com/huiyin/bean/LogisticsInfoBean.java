package com.huiyin.bean;

import java.util.List;

/**
 * 物流公司信息
 * Created by lian on 2015/6/1.
 */
public class LogisticsInfoBean extends BaseBean{

    /**
     * descrition : 寄回快递说明
     * companyList : [{"COMPANY_NAME":"百世汇通","ID":7},{"COMPANY_NAME":"德邦快递","ID":8},{"COMPANY_NAME":"EMS","ID":5},{"COMPANY_NAME":"飞虎队","ID":0},{"COMPANY_NAME":"急宅送","ID":9},{"COMPANY_NAME":"申通快递","ID":3},{"COMPANY_NAME":"顺丰速递","ID":6},{"COMPANY_NAME":"天天快递","ID":4},{"COMPANY_NAME":"圆通快递","ID":1},{"COMPANY_NAME":"韵达快递","ID":2},{"COMPANY_NAME":"中国邮政","ID":10}]
     * curTime : 2015-06-01 19:16:25
     * type : 1
     * msg : 成功
     */
    private String descrition;//寄回快递说明
    private List<CompanyListEntity> companyList;// 公司列表
    private String curTime;//当前时间

    public void setDescrition(String descrition) {
        this.descrition = descrition;
    }

    public void setCompanyList(List<CompanyListEntity> companyList) {
        this.companyList = companyList;
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

    public String getDescrition() {
        return descrition;
    }

    public List<CompanyListEntity> getCompanyList() {
        return companyList;
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
