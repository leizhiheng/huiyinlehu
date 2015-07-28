package com.huiyin.bean;

import java.util.List;

/**
 * 投诉详情
 * Created by kuangyong on 2015/6/10.
 */
public class MyComplaintDetailBean extends BaseBean{

    /**
     * QUESTION_CONTENT : 1
     * ORDER_CODE : S1433821310764
     * curTime : 2015-06-10 14:48:44
     * type : 1
     * QUESTION_TITLE : 1
     * msg : 成功
     * CREATEDATE : 2015-06-10 14:48:14
     * USER_NAME : 13265483691
     * IS_CLOSED : 0
     * ID : 451
     * AUDIT_STATUS : 1
     * NUM : 1
     * messageList : []
     * IMG :
     * CANCEL : 0
     */
    private String QUESTION_CONTENT;//投诉内容
    private String ORDER_CODE;//订单编号
    private String QUESTION_TITLE;//投诉标题
    private String CREATEDATE;//投诉时间
    private String USER_NAME;//投诉用户名
    private String IS_CLOSED;//投诉是否关闭1.是0.否
    private String ID;//投诉id
    private String AUDIT_STATUS;//1.待审核(等待处理)2.处理中，3处理解决
    private String NUM;
    private List<MesaageList> messageList;//留言信息
    private String IMG;//图片
    private String CANCEL;//会员是否取消投诉 0.否1.是
    private String FINISH_TIME;//

    public void setQUESTION_CONTENT(String QUESTION_CONTENT) {
        this.QUESTION_CONTENT = QUESTION_CONTENT;
    }

    public void setORDER_CODE(String ORDER_CODE) {
        this.ORDER_CODE = ORDER_CODE;
    }


    public void setType(int type) {
        this.type = type;
    }

    public void setQUESTION_TITLE(String QUESTION_TITLE) {
        this.QUESTION_TITLE = QUESTION_TITLE;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCREATEDATE(String CREATEDATE) {
        this.CREATEDATE = CREATEDATE;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public void setIS_CLOSED(String IS_CLOSED) {
        this.IS_CLOSED = IS_CLOSED;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setAUDIT_STATUS(String AUDIT_STATUS) {
        this.AUDIT_STATUS = AUDIT_STATUS;
    }

    public void setNUM(String NUM) {
        this.NUM = NUM;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public void setCANCEL(String CANCEL) {
        this.CANCEL = CANCEL;
    }

    public String getQUESTION_CONTENT() {
        return QUESTION_CONTENT;
    }

    public String getORDER_CODE() {
        return ORDER_CODE;
    }

    public int getType() {
        return type;
    }

    public String getQUESTION_TITLE() {
        return QUESTION_TITLE;
    }

    public String getMsg() {
        return msg;
    }

    public String getCREATEDATE() {
        return CREATEDATE;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public String getIS_CLOSED() {
        return IS_CLOSED;
    }

    public String getID() {
        return ID;
    }

    public String getAUDIT_STATUS() {
        return AUDIT_STATUS;
    }

    public String getNUM() {
        return NUM;
    }

    public List<MesaageList> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MesaageList> messageList) {
        this.messageList = messageList;
    }

    public String getIMG() {
        return IMG;
    }

    public String getCANCEL() {
        return CANCEL;
    }

    /**
     * 是否已取消
     * @return
     */
    public boolean isCancel(){
    	try{
    		return "1".equals(CANCEL);
    	}catch(Exception e){
    		return false;
    	}
    }
    
    /**
     * 是否已解决
     * @return
     */
    public boolean isDeal(){
    	try{
    		return "3".equals(AUDIT_STATUS);
    	}catch(Exception e){
    		return false;
    	}
    }
    
    
	public String getFINISH_TIME() {
		return FINISH_TIME;
	}

	public void setFINISH_TIME(String fINISH_TIME) {
		FINISH_TIME = fINISH_TIME;
	}
}
