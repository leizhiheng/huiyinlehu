package com.huiyin.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by justme on 15/5/25.
 * 购物车商品信息
 */
public class ShoppingCarGoodsBean implements Serializable{

    @Expose
    private int brandId;//品牌ID
    @Expose
    private boolean buy;//是否允许购买 false不可以
    @Expose
    private double discountPrice;//折扣价格
    @Expose
    private String discountPriceStr;//折扣价格 String类型
    @Expose
    private double discountPriceSum;//折扣总价格
    @Expose
    private String discountPriceSumStr;//折扣总价格 String类型
    @Expose
    private boolean gift;//是否有赠品false没有
    @Expose
    private ArrayList<ShoppingCarGoodsBean> giftGoodsList = new ArrayList<ShoppingCarGoodsBean>();//赠品集合
    @Expose
    private String goodsAd;//商品的广告语
    @Expose
    private int goodsId;//商品ID
    @Expose
    private String goodsImg;//商品图片
    @Expose
    private ArrayList<ShoppingCarGoodsBean> goodsList = new ArrayList<ShoppingCarGoodsBean>();
    @Expose
    private String goodsName;//商品的名称
    @Expose
    private long goodsNo;//商品的规格ID
    @Expose
    private double goodsPrice;//商品的价格
    @Expose
    private String goodsPriceStr;//商品价格String类型
    @Expose
    private double goodsPriceSum;//商品总价格
    @Expose
    private String goodsPriceSumStr;//商品总价格 String类型
    @Expose
    private int goodsStock;//商品库存
    @Expose
    private double goodsWeight;//商品总量 Double类型
    @Expose
    private String goodsWeightStr;//商品总量 String类型
    @Expose
    private double goodsWeightSum;//商品总重量 Double类型
    @Expose
    private String goodsWeightSumStr;//商品总重量 String类型
    /*@Expose
    private String ids;//用于处理已选商品    ? 什么意思？*/
    @Expose
    private double integral;//商品积分
    @Expose
    private int isShowApp;//是否在App显示 0不显示 1显示
    @Expose
    private int joinStock;//参与库存数量（限赠品）
    @Expose
    private ArrayList<String> msg = new ArrayList<String>();//消息集合
    @Expose
    private String normsValue;//规格
    @Expose
    private ArrayList<ShoppingCarNormsValueListBean> normsValueList = new ArrayList<ShoppingCarNormsValueListBean>();//规格值集合
    @Expose
    private int num;//商品数量
    @Expose
    private double preferentialPrice;//优惠价格 Double类型
    @Expose
    private String preferentialPriceStr;//优惠价格 String类型
    @Expose
    private double preferentialPriceSum;//优惠总价格 Double类型
    @Expose
    private String preferentialPriceSumStr;//优惠总价格 String类型
    @Expose
    private ShoppingCarPromotionBean promotion;//当前用户选择的促销
    @Expose
    private long promotionId;//促销id(long类型)
    @Expose
    private ArrayList<ShoppingCarPromotionItemBean> promotionList = new ArrayList<ShoppingCarPromotionItemBean>();//促销集合
    @Expose
    private int promotionType;//促销类型 0、普通商品 1、单品 2、套餐 3、赠品 4、满减 5、满赠  6、秒杀 7、礼品兑换 8、团购  9、天天低价 10、分销 11、闪购 12、推荐配件
    @Expose
    private boolean select;//是否选中 ture选中
    @Expose
    private int shopCartType;//购物车类型 SHOPPING_TYPE -1、普通商品 5、团购 6、秒杀 7、分销 8、套餐 9、闪购 10、礼品兑换 11、天天低价
    @Expose
    private int shoppingId;//购物车ID
    @Expose
    private int storeId;//店铺id
    @Expose
    private String storeLogo;//店铺logo
    @Expose
    private String storeName;//店铺名称
    @Expose
    private int thridId;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public boolean isBuy() {
        return buy;
    }

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscountPriceStr() {
        return discountPriceStr;
    }

    public void setDiscountPriceStr(String discountPriceStr) {
        this.discountPriceStr = discountPriceStr;
    }

    public double getDiscountPriceSum() {
        return discountPriceSum;
    }

    public void setDiscountPriceSum(double discountPriceSum) {
        this.discountPriceSum = discountPriceSum;
    }

    public String getDiscountPriceSumStr() {
        return discountPriceSumStr;
    }

    public void setDiscountPriceSumStr(String discountPriceSumStr) {
        this.discountPriceSumStr = discountPriceSumStr;
    }

    public boolean isGift() {
        return gift;
    }

    public void setGift(boolean gift) {
        this.gift = gift;
    }

    public ArrayList<ShoppingCarGoodsBean> getGiftGoodsList() {
        return giftGoodsList;
    }

    public void setGiftGoodsList(ArrayList<ShoppingCarGoodsBean> giftGoodsList) {
        this.giftGoodsList = giftGoodsList;
    }

    public String getGoodsAd() {
        return goodsAd;
    }

    public void setGoodsAd(String goodsAd) {
        this.goodsAd = goodsAd;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public ArrayList<ShoppingCarGoodsBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(ArrayList<ShoppingCarGoodsBean> goodsList) {
        this.goodsList = goodsList;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public long getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(long goodsNo) {
        this.goodsNo = goodsNo;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsPriceStr() {
        return goodsPriceStr;
    }

    public void setGoodsPriceStr(String goodsPriceStr) {
        this.goodsPriceStr = goodsPriceStr;
    }

    public double getGoodsPriceSum() {
        return goodsPriceSum;
    }

    public void setGoodsPriceSum(double goodsPriceSum) {
        this.goodsPriceSum = goodsPriceSum;
    }

    public String getGoodsPriceSumStr() {
        return goodsPriceSumStr;
    }

    public void setGoodsPriceSumStr(String goodsPriceSumStr) {
        this.goodsPriceSumStr = goodsPriceSumStr;
    }

    public int getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsStock(int goodsStock) {
        this.goodsStock = goodsStock;
    }

    public double getGoodsWeight() {
        return goodsWeight;
    }

    public void setGoodsWeight(double goodsWeight) {
        this.goodsWeight = goodsWeight;
    }

    public String getGoodsWeightStr() {
        return goodsWeightStr;
    }

    public void setGoodsWeightStr(String goodsWeightStr) {
        this.goodsWeightStr = goodsWeightStr;
    }

    public double getGoodsWeightSum() {
        return goodsWeightSum;
    }

    public void setGoodsWeightSum(double goodsWeightSum) {
        this.goodsWeightSum = goodsWeightSum;
    }

    public String getGoodsWeightSumStr() {
        return goodsWeightSumStr;
    }

    public void setGoodsWeightSumStr(String goodsWeightSumStr) {
        this.goodsWeightSumStr = goodsWeightSumStr;
    }

    /*public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }*/

    public double getIntegral() {
        return integral;
    }

    public void setIntegral(double integral) {
        this.integral = integral;
    }

    public int getIsShowApp() {
        return isShowApp;
    }

    public void setIsShowApp(int isShowApp) {
        this.isShowApp = isShowApp;
    }

    public int getJoinStock() {
        return joinStock;
    }

    public void setJoinStock(int joinStock) {
        this.joinStock = joinStock;
    }

    public ArrayList<String> getMsg() {
        return msg;
    }

    public void setMsg(ArrayList<String> msg) {
        this.msg = msg;
    }

    public ArrayList<ShoppingCarNormsValueListBean> getNormsValueList() {
        return normsValueList;
    }

    public void setNormsValueList(ArrayList<ShoppingCarNormsValueListBean> normsValueList) {
        this.normsValueList = normsValueList;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPreferentialPrice() {
        return preferentialPrice;
    }

    public void setPreferentialPrice(double preferentialPrice) {
        this.preferentialPrice = preferentialPrice;
    }

    public String getPreferentialPriceStr() {
        return preferentialPriceStr;
    }

    public void setPreferentialPriceStr(String preferentialPriceStr) {
        this.preferentialPriceStr = preferentialPriceStr;
    }

    public double getPreferentialPriceSum() {
        return preferentialPriceSum;
    }

    public void setPreferentialPriceSum(double preferentialPriceSum) {
        this.preferentialPriceSum = preferentialPriceSum;
    }

    public String getPreferentialPriceSumStr() {
        return preferentialPriceSumStr;
    }

    public void setPreferentialPriceSumStr(String preferentialPriceSumStr) {
        this.preferentialPriceSumStr = preferentialPriceSumStr;
    }

    public ShoppingCarPromotionBean getPromotion() {
        return promotion;
    }

    public void setPromotion(ShoppingCarPromotionBean promotion) {
        this.promotion = promotion;
    }

    public long getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(long promotionId) {
        this.promotionId = promotionId;
    }

    public ArrayList<ShoppingCarPromotionItemBean> getPromotionList() {
        return promotionList;
    }

    public void setPromotionList(ArrayList<ShoppingCarPromotionItemBean> promotionList) {
        this.promotionList = promotionList;
    }

    public int getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(int promotionType) {
        this.promotionType = promotionType;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getShopCartType() {
        return shopCartType;
    }

    public void setShopCartType(int shopCartType) {
        this.shopCartType = shopCartType;
    }

    public int getShoppingId() {
        return shoppingId;
    }

    public void setShoppingId(int shoppingId) {
        this.shoppingId = shoppingId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getThridId() {
        return thridId;
    }

    public void setThridId(int thridId) {
        this.thridId = thridId;
    }

    public String getNormsValus() {
        return normsValue;
    }

    public void setNormsValus(String normsValue) {
        this.normsValue = normsValue;
    }
}
