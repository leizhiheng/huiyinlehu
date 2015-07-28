package com.huiyin.bean;

import java.util.List;

/**
 * Created by kuangyong on 2015/6/10.
 */
public class ComplainDataEntity extends BaseBean{


    /**
     * ORDER_ID : 15286
     * STORE_NAME : 乐虎自营
     * STORE_LOGO : null
     * ORDER_CODE : S1433821310764
     * complainDetaillList : [{"GOODS_IMG":"/upload/image/admin/2015/20150608/201506081614131268_290X290.jpg","GOODS_PRICE":0.01,"ORDER_ID":15286,"ORDER_DETAIL_ID":20796,"GOODS_NAME":"wap端单规格商品【固定运费5元】","GOODS_NO":431431,"STORE_ID":0,"GOODS_ID":10971}]
     * ID : 431
     * STORE_ID : 0
     * CREATE_DATE : 2015-06-10 10:32:48
     * AUDIT_STATUS : 1
     * NUM : 2
     */
    private String ORDER_ID;//订单id
    private String STORE_NAME;//商品名称
    private String STORE_LOGO;//店铺logo
    private String ORDER_CODE;//订单编号
    private List<ComplainDetaillListEntity> complainDetaillList;
    private String ID;//投诉id
    private String STORE_ID;//店铺id
    private String CREATE_DATE;//投诉时间
    private int AUDIT_STATUS;//1.待审核(等待处理)2.处理中，3处理解决
    private String STATUS_NAME;//状态名称
    private int NUM;

    public String getSTATUS_NAME() {
        return STATUS_NAME;
    }

    public void setSTATUS_NAME(String STATUS_NAME) {
        this.STATUS_NAME = STATUS_NAME;
    }

    public void setORDER_ID(String ORDER_ID) {
        this.ORDER_ID = ORDER_ID;
    }

    public void setSTORE_NAME(String STORE_NAME) {
        this.STORE_NAME = STORE_NAME;
    }

    public void setSTORE_LOGO(String STORE_LOGO) {
        this.STORE_LOGO = STORE_LOGO;
    }

    public void setORDER_CODE(String ORDER_CODE) {
        this.ORDER_CODE = ORDER_CODE;
    }

    public void setComplainDetaillList(List<ComplainDetaillListEntity> complainDetaillList) {
        this.complainDetaillList = complainDetaillList;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID = STORE_ID;
    }

    public void setCREATE_DATE(String CREATE_DATE) {
        this.CREATE_DATE = CREATE_DATE;
    }

    public void setAUDIT_STATUS(int AUDIT_STATUS) {
        this.AUDIT_STATUS = AUDIT_STATUS;
    }

    public void setNUM(int NUM) {
        this.NUM = NUM;
    }

    public String getORDER_ID() {
        return ORDER_ID;
    }

    public String getSTORE_NAME() {
        return STORE_NAME;
    }

    public String getSTORE_LOGO() {
        return STORE_LOGO;
    }

    public String getORDER_CODE() {
        return ORDER_CODE;
    }

    public List<ComplainDetaillListEntity> getComplainDetaillList() {
        return complainDetaillList;
    }

    public String getID() {
        return ID;
    }

    public String getSTORE_ID() {
        return STORE_ID;
    }

    public String getCREATE_DATE() {
        return CREATE_DATE;
    }

    public int getAUDIT_STATUS() {
        return AUDIT_STATUS;
    }

    public int getNUM() {
        return NUM;
    }
//
//    /**
//     * ORDER_ID : 15286
//     * STORE_NAME : 乐虎自营
//     * STORE_LOGO : null
//     * ORDER_CODE : S1433821310764
//     * ID : 432
//     * STORE_ID : 0
//     * CREATE_DATE : 2015-06-10 10:42:40
//     * AUDIT_STATUS : 1
//     * NUM : 1
//     */
//    private String ORDER_ID;//订单id
//    private String STORE_NAME;//店铺名称
//    private String STORE_LOGO;//店铺logo
//    private String ORDER_CODE;//订单编号
//    private String ID;//投诉id
//    private String STORE_ID;//店铺id
//    private String CREATE_DATE;//投诉时间
//    private int AUDIT_STATUS;//1.待审核(等待处理)2.处理中，3处理解决
//    private int NUM;
//
//    public void setORDER_ID(String ORDER_ID) {
//        this.ORDER_ID = ORDER_ID;
//    }
//
//    public void setSTORE_NAME(String STORE_NAME) {
//        this.STORE_NAME = STORE_NAME;
//    }
//
//    public void setSTORE_LOGO(String STORE_LOGO) {
//        this.STORE_LOGO = STORE_LOGO;
//    }
//
//    public void setORDER_CODE(String ORDER_CODE) {
//        this.ORDER_CODE = ORDER_CODE;
//    }
//
//    public void setID(String ID) {
//        this.ID = ID;
//    }
//
//    public void setSTORE_ID(String STORE_ID) {
//        this.STORE_ID = STORE_ID;
//    }
//
//    public void setCREATE_DATE(String CREATE_DATE) {
//        this.CREATE_DATE = CREATE_DATE;
//    }
//
//    public void setAUDIT_STATUS(int AUDIT_STATUS) {
//        this.AUDIT_STATUS = AUDIT_STATUS;
//    }
//
//    public void setNUM(int NUM) {
//        this.NUM = NUM;
//    }
//
//    public String getORDER_ID() {
//        return ORDER_ID;
//    }
//
//    public String getSTORE_NAME() {
//        return STORE_NAME;
//    }
//
//    public String getSTORE_LOGO() {
//        return STORE_LOGO;
//    }
//
//    public String getORDER_CODE() {
//        return ORDER_CODE;
//    }
//
//    public String getID() {
//        return ID;
//    }
//
//    public String getSTORE_ID() {
//        return STORE_ID;
//    }
//
//    public String getCREATE_DATE() {
//        return CREATE_DATE;
//    }
//
//    public int getAUDIT_STATUS() {
//        return AUDIT_STATUS;
//    }
//
//    public int getNUM() {
//        return NUM;
//    }
}
