package com.huiyin.bean;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huiyin.utils.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by justme on 15/7/1.
 * 修改之后的购物车逻辑层
 */
public class ShoppingCarDataBaseBeanNew extends BaseBean{

    @Expose
    private ShoppingCarDataReturnMap cart;

    public ShoppingCarDataReturnMap getCart() {
        return cart;
    }

    public static ShoppingCarDataBaseBeanNew explainJson(String json, Context context) {
        Gson gson = new Gson();
        try {
            ShoppingCarDataBaseBeanNew experLightBean = gson.fromJson(json,
                    ShoppingCarDataBaseBeanNew.class);
            return experLightBean;
        } catch (Exception e) {
            LogUtil.d("dataExplainJson", e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public class ShoppingCarDataReturnMap implements Serializable {
        @Expose
        private boolean allConsumptionGoods; // add by zhyao @2015/6/16  判断商品是否全部是消费卷
        @Expose
        private String brandIds;//品牌ID
        @Expose
        private boolean buy;//是否可以购买
        @Expose
        private ArrayList<ShoppingCarStoreBean> cartList = new ArrayList<ShoppingCarStoreBean>();
        @Expose
        private int experience;//用户经验值
        @Expose
        private String goodsIds;//商品ID集合
        @Expose
        private int goodsIntegralSum;//商品积分吧 大概   居然要靠猜的
        @Expose
        private double goodsPriceSum;//商品总金额
        @Expose
        private String goodsPriceSumStr;//商品总金额
        @Expose
        private int goodsSum;//商品总数量
        @Expose
        private double goodsWeightSum;//商品总重量
        @Expose
        private String goodsWeightSumStr;//商品总重量
        @Expose
        private double orderPrice;//订单总金额
        @Expose
        private String orderPriceStr;//订单总金额
        @Expose
        private int orderType;//订单类型
        @Expose
        private double preferentialPriceSum; //优惠总金额
        @Expose
        private String preferentialPriceSumStr;//优惠总金额
        @SerializedName("shopping_ids")
        @Expose
        private String shoppingIds;//购物车I集合
        @Expose
        private String storeIds;   //店铺ID
        @Expose
        private String thridIds;   //三级分类ID

        public boolean isAllConsumptionGoods() {
            return allConsumptionGoods;
        }

        public void setAllConsumptionGoods(boolean allConsumptionGoods) {
            this.allConsumptionGoods = allConsumptionGoods;
        }

        public String getBrandIds() {
            return brandIds;
        }

        public void setBrandIds(String brandIds) {
            this.brandIds = brandIds;
        }

        public boolean isBuy() {
            return buy;
        }

        public void setBuy(boolean buy) {
            this.buy = buy;
        }

        public ArrayList<ShoppingCarStoreBean> getCart() {
            return cartList;
        }

        public void setCartList(ArrayList<ShoppingCarStoreBean> cartList) {
            this.cartList = cartList;
        }

        public int getExperience() {
            return experience;
        }

        public void setExperience(int experience) {
            this.experience = experience;
        }

        public String getGoodsIds() {
            return goodsIds;
        }

        public void setGoodsIds(String goodsIds) {
            this.goodsIds = goodsIds;
        }

        public int getGoodsIntegralSum() {
            return goodsIntegralSum;
        }

        public void setGoodsIntegralSum(int goodsIntegralSum) {
            this.goodsIntegralSum = goodsIntegralSum;
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

        public int getGoodsSum() {
            return goodsSum;
        }

        public void setGoodsSum(int goodsSum) {
            this.goodsSum = goodsSum;
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

        public double getOrderPrice() {
            return orderPrice;
        }

        public void setOrderPrice(double orderPrice) {
            this.orderPrice = orderPrice;
        }

        public String getOrderPriceStr() {
            return orderPriceStr;
        }

        public void setOrderPriceStr(String orderPriceStr) {
            this.orderPriceStr = orderPriceStr;
        }

        public int getOrderType() {
            return orderType;
        }

        public void setOrderType(int orderType) {
            this.orderType = orderType;
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

        public String getShoppingIds() {
            return shoppingIds;
        }

        public void setShoppingIds(String shoppingIds) {
            this.shoppingIds = shoppingIds;
        }

        public String getStoreIds() {
            return storeIds;
        }

        public void setStoreIds(String storeIds) {
            this.storeIds = storeIds;
        }

        public String getThridIds() {
            return thridIds;
        }

        public void setThridIds(String thridIds) {
            this.thridIds = thridIds;
        }
    }
}
