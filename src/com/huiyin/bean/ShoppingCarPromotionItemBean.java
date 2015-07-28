package com.huiyin.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by justme on 15/5/25.
 * 促销的基本类型
 */
public class ShoppingCarPromotionItemBean implements Serializable{
    @SerializedName("PROMOTION_NAME")
    @Expose
    private String promotionName;//促销活动名称
    @SerializedName("PROMOTION_ID")
    @Expose
    private int promotionId;//促销活动id（如果为0表示没有参与促销）
    @SerializedName("USER_GRADE")
    @Expose
    private int userGrade;//用户等级
    @SerializedName("PROMOTION_TYPE")
    @Expose
    private int promotionType;//促销类型 1、单品 2、套装 3、赠品 4、满减 5、满赠  6、今日秒杀 7、新品预约
    @SerializedName("USER_GRADE_NAME")
    @Expose
    private String userGradeName;//用户等级名称

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public int getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(int userGrade) {
        this.userGrade = userGrade;
    }

    public String getUserGradeName() {
        return userGradeName;
    }

    public void setUserGradeName(String userGradeName) {
        this.userGradeName = userGradeName;
    }
}
