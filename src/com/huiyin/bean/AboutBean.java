package com.huiyin.bean;

/**
 * Created by kuangyong on 2015/6/19.
 */
public class AboutBean extends BaseBean{
    private String logo;//网站logo
    private String code;//二维码路径
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
