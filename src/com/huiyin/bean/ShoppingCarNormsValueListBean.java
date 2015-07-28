package com.huiyin.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by justme on 15/5/25.
 */
public class ShoppingCarNormsValueListBean implements Serializable{

    @SerializedName("NORMS_NAME")
    @Expose
    private String normsName;//规格名
    @SerializedName("NORMS_ID")
    @Expose
    private int normsId;//规格名ID
    @SerializedName("GOODS_NO")
    @Expose
    private long goodsNo;//商品规格ID
    @SerializedName("NORMS_VALUE_NAME")
    @Expose
    private String normsValueName;//规格值名
    @SerializedName("GOODS_ID")
    @Expose
    private int goodsId;//商品ID
    @SerializedName("NORMS_VALUE_ID")
    @Expose
    private int normsvalueid;//规格值ID
    @SerializedName("GOODS_NORMS_ID")
    @Expose
    private int goodsNormsId;

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public long getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(long goodsNo) {
        this.goodsNo = goodsNo;
    }

    public int getGoodsNormsId() {
        return goodsNormsId;
    }

    public void setGoodsNormsId(int goodsNormsId) {
        this.goodsNormsId = goodsNormsId;
    }

    public int getNormsId() {
        return normsId;
    }

    public void setNormsId(int normsId) {
        this.normsId = normsId;
    }

    public String getNormsName() {
        return normsName;
    }

    public void setNormsName(String normsName) {
        this.normsName = normsName;
    }

    public int getNormsvalueid() {
        return normsvalueid;
    }

    public void setNormsvalueid(int normsvalueid) {
        this.normsvalueid = normsvalueid;
    }

    public String getNormsValueName() {
        return normsValueName;
    }

    public void setNormsValueName(String normsValueName) {
        this.normsValueName = normsValueName;
    }
}
