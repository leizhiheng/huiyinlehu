package com.huiyin.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 留言列表数据
 * Created by kuangyong on 2015/6/10.
 */
public class MesaageList extends BaseBean{

    private String MESSAGE;//留言内容
    private String CREATEDATE;//创建时间
    private String STORE_NAME;//店铺名称
    private String MESSAGE_TYPE;// 1.客户留言2.平台管理员留言，3.商户留言
    private String STORE_LOGO;//店铺logo
    private String USER_URL;//用户头像
    private String USER_NAME;//客户名称
    private String ADMIN_NAME;//管理员名称
    private String IMG;//图片列表

    public String getSTORE_LOGO() {
        return STORE_LOGO;
    }

    public void setSTORE_LOGO(String STORE_LOGO) {
        this.STORE_LOGO = STORE_LOGO;
    }

    public String getUSER_URL() {
        return USER_URL;
    }

    public void setUSER_URL(String USER_URL) {
        this.USER_URL = USER_URL;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getCREATEDATE() {
        return CREATEDATE;
    }

    public void setCREATEDATE(String CREATEDATE) {
        this.CREATEDATE = CREATEDATE;
    }

    public String getSTORE_NAME() {
        return STORE_NAME;
    }

    public void setSTORE_NAME(String STORE_NAME) {
        this.STORE_NAME = STORE_NAME;
    }

    public String getMESSAGE_TYPE() {
        return MESSAGE_TYPE;
    }

    public void setMESSAGE_TYPE(String MESSAGE_TYPE) {
        this.MESSAGE_TYPE = MESSAGE_TYPE;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getADMIN_NAME() {
        return ADMIN_NAME;
    }

    public void setADMIN_NAME(String ADMIN_NAME) {
        this.ADMIN_NAME = ADMIN_NAME;
    }

    public String getIMG() {
        return IMG;
    }



    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    private List<ImageData> imgList;
    /**获取评论图片集合**/
    public List<ImageData> getImageData(){
        if(null == imgList){
            if(!TextUtils.isEmpty(IMG)){
                imgList = new ArrayList<ImageData>();
                String[] array = IMG.split(",");
                for(int i=0; i<array.length; i++){
                    imgList.add(new ImageData("", array[i]));
                }
            }
        }

        return imgList;
    }
}
