package com.huiyin.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by justme on 15/5/25.
 * 购物车店铺
 */
public class ShoppingCarStoreBean implements Serializable {
    @Expose
    private ArrayList<ShoppingCarGoodsBean> goodsList = new ArrayList<ShoppingCarGoodsBean>();
    @Expose
    private double storeDiscountPriceSum;//店铺折扣总金额
    @Expose
    private String storeDiscountPriceSumStr;//店铺折扣总金额 String类型
    @Expose
    private double storeGoodsPriceSum;//店铺商品价格 Double类型
    @Expose
    private String storeGoodsPriceSumStr;//店铺商品价格 String类型
    @Expose
    private int storeId;//店铺id（如果店铺id为0表示是自营，用默认图标）
    @Expose
    private String storeLogo;//店铺logo
    @Expose
    private String storeName;//店铺名
    @Expose
    private double storePreferentialPriceSum;//店铺折扣总金额
    @Expose
    private String storePreferentialPriceSumStr;//店铺折扣总金额 String类型

    public ArrayList<ShoppingCarGoodsBean> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(ArrayList<ShoppingCarGoodsBean> goodsList) {
        this.goodsList = goodsList;
    }

    public double getStoreDiscountPriceSum() {
        return storeDiscountPriceSum;
    }

    public void setStoreDiscountPriceSum(double storeDiscountPriceSum) {
        this.storeDiscountPriceSum = storeDiscountPriceSum;
    }

    public String getStoreDiscountPriceSumStr() {
        return storeDiscountPriceSumStr;
    }

    public void setStoreDiscountPriceSumStr(String storeDiscountPriceSumStr) {
        this.storeDiscountPriceSumStr = storeDiscountPriceSumStr;
    }

    public double getStoreGoodsPriceSum() {
        return storeGoodsPriceSum;
    }

    public void setStoreGoodsPriceSum(double storeGoodsPriceSum) {
        this.storeGoodsPriceSum = storeGoodsPriceSum;
    }

    public String getStoreGoodsPriceSumStr() {
        return storeGoodsPriceSumStr;
    }

    public void setStoreGoodsPriceSumStr(String storeGoodsPriceSumStr) {
        this.storeGoodsPriceSumStr = storeGoodsPriceSumStr;
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

    public double getStorePreferentialPriceSum() {
        return storePreferentialPriceSum;
    }

    public void setStorePreferentialPriceSum(double storePreferentialPriceSum) {
        this.storePreferentialPriceSum = storePreferentialPriceSum;
    }

    public String getStorePreferentialPriceSumStr() {
        return storePreferentialPriceSumStr;
    }

    public void setStorePreferentialPriceSumStr(String storePreferentialPriceSumStr) {
        this.storePreferentialPriceSumStr = storePreferentialPriceSumStr;
    }
}
