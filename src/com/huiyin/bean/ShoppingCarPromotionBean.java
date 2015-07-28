package com.huiyin.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by justme on 15/5/25.
 */
public class ShoppingCarPromotionBean implements Serializable{

    /*
    不要问我为什么活动ID名字不一样什么的，呵呵，我也想知道呢。。。

    单品：
    PROMOTION_NAME: 活动名称,
    PRICE: 折扣
    GRADE_NAME: 满足条件的会员等级名称
    GRADE_MIN: 最低会员等级要求,
    ID: 活动id
    MOST_QUANTITY: 最多购买
    STORE_ID: 店铺id
    GOODS_NO: 商品编号
    QUOTA_ONE: 是否限购一件：0是
    LEAST_QUANTITY: 最少购买
    TYPE: 折扣类型，1打折，2直降，2折后价
    满赠：
    GRADE_NAME: 满足条件的会员等级名称,
    NAME: 活动名称,
    GRADE_MIN: 最低会员等级要求,
    DISCOUNT_CASH: 加多少钱才可以换购,
    ID: 活动id
    CASH: 满多少钱
    赠品：
    PROMOTION_NAME: 活动名称,
    USER_GRADE_NAME: 满足条件的会员等级名称,
    PROMOTION_ID: 活动id
    USER_GRADE: 会员等级id
    PROMOTION_TYPE: 活动类型
    满减：
            "NAME": 促销名称
    "dis_stair": [{ 阶梯促销（如果是满减）
        "PROMOTION_ID": 活动id
        "DISCOUNT_CASH": 减少金额
        "CASH": 满足金额
    }],
            "USER_GRADE_NAME": 会员等级
    "LINK":
            "GRADE_MIN": 用户等级的最低成长值
    "ID": 活动id（如果没有活动，则<=0）
            "TYPE": 折扣方式 1、折扣 2、直降 3、折后降
    PROMOTION_TYPE：促销类型1、单品 2、套装 3、赠品 4、满减 5、满赠  6、今日秒杀 7、新品预约*/

    @SerializedName("NAME")
    @Expose
    private String name; //促销名称
    @SerializedName("dis_stair")
    @Expose
    private ArrayList<DisStair> disStair = new ArrayList<DisStair>();//阶梯促销（如果是满减）
    @SerializedName("GRADE_NAME")
    @Expose
    private String gradeName; //会员等级名称
    @Expose
    private String LINK;
    @SerializedName("GRADE_MIN")
    @Expose
    private long gradeMin;//用户等级的最低成长值
    @Expose
    private int ID;//当前活动的ID
    @Expose
    private int TYPE;//折扣方式 1、折扣 2、直降 3、折后降

    @SerializedName("PRICE")
    @Expose
    private double price; //价格
    @SerializedName("MOST_QUANTITY")
    @Expose
    private int mostQuantity;//最多购买数量 <=0是不限制数量
    @SerializedName("STORE_ID")
    @Expose
    private int storeId;//店铺ID
    @SerializedName("GOODS_NO")
    @Expose
    private long goodsNo;//商品规格
    @SerializedName("QUOTA_ONE")
    @Expose
    private int quotaOne;//是否限制单品购买  0每个人只能购买一件,1不限购
    @SerializedName("LEAST_QUANTITY")
    @Expose
    private int leastQuantity;//最少购买数量

    @SerializedName("PROMOTION_NAME")
    @Expose
    private String promotionName;//活动名称
    @SerializedName("USER_GRADE_NAME")
    @Expose
    private String userGradeName;//会员等级名
    @SerializedName("PROMOTION_ID")
    @Expose
    private int promotionId;//活动ID
    @SerializedName("PROMOTION_TYPE")
    @Expose
    private int promotionType;//促销类型1、单品 2、套装 3、赠品 4、满减 5、满赠  6、今日秒杀 7、新品预约
    @SerializedName("DISCOUNT_CASH")
    @Expose
    private double discountCash;//满赠情况下，加多少钱才可以换购
    @SerializedName("CASH")
    @Expose
    private double cash;//满赠情况下，满多少钱才可以换购
    @SerializedName("USER_GRADE")
    @Expose
    private long userGrade;//会员等级id


    public class DisStair implements Serializable{

        @SerializedName("PROMOTION_ID")
        @Expose
        private int promotionId;  //活动id
        @SerializedName("DISCOUNT_CASH")
        @Expose
        private double discountCash; //减少金额
        @SerializedName("CASH")
        @Expose
        private double cash; //满足金额

        public double getCash() {
            return cash;
        }

        public void setCash(double cash) {
            this.cash = cash;
        }

        public double getDiscountCash() {
            return discountCash;
        }

        public void setDiscountCash(double discountCash) {
            this.discountCash = discountCash;
        }

        public int getPromotionId() {
            return promotionId;
        }

        public void setPromotionId(int promotionId) {
            this.promotionId = promotionId;
        }
    }

    public ArrayList<DisStair> getDisStair() {
        return disStair;
    }

    public void setDisStair(ArrayList<DisStair> disStair) {
        this.disStair = disStair;
    }

    public long getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(long goodsNo) {
        this.goodsNo = goodsNo;
    }

    public long getGradeMin() {
        return gradeMin;
    }

    public void setGradeMin(long gradeMin) {
        this.gradeMin = gradeMin;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getLeastQuantity() {
        return leastQuantity;
    }

    public void setLeastQuantity(int leastQuantity) {
        this.leastQuantity = leastQuantity;
    }

    public String getLINK() {
        return LINK;
    }

    public void setLINK(String LINK) {
        this.LINK = LINK;
    }

    public int getMostQuantity() {
        return mostQuantity;
    }

    public void setMostQuantity(int mostQuantity) {
        this.mostQuantity = mostQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuotaOne() {
        return quotaOne;
    }

    public void setQuotaOne(int quotaOne) {
        this.quotaOne = quotaOne;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getDiscountCash() {
        return discountCash;
    }

    public void setDiscountCash(double discountCash) {
        this.discountCash = discountCash;
    }

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

    public long getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(long userGrade) {
        this.userGrade = userGrade;
    }

    public String getUserGradeName() {
        return userGradeName;
    }

    public void setUserGradeName(String userGradeName) {
        this.userGradeName = userGradeName;
    }
}
