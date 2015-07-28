package com.huiyin.ui.store;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.huiyin.bean.BaseBean;

import java.util.ArrayList;

/**
 * Created by justme on 15/5/12.
 */
public class GoodsListBean extends BaseBean {

    @Expose
    private int pageIndex;
    @Expose
    private int totalPageNum;
    @Expose
    private ArrayList<GoodsList> goodsList = new ArrayList<GoodsList>();
    @Expose
    private String curTime;

    /**
     * @return The pageIndex
     */
    public int getPageIndex() {
        return pageIndex;
    }

    /**
     * @param pageIndex The pageIndex
     */
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * @return The totalPageNum
     */
    public int getTotalPageNum() {
        return totalPageNum;
    }

    /**
     * @param totalPageNum The totalPageNum
     */
    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    /**
     * @return The goodsList
     */
    public ArrayList<GoodsList> getGoodsList() {
        return goodsList;
    }

    /**
     * @param goodsList The goodsList
     */
    public void setGoodsList(ArrayList<GoodsList> goodsList) {
        this.goodsList = goodsList;
    }

    /**
     * @return The curTime
     */
    public String getCurTime() {
        return curTime;
    }

    /**
     * @param curTime The curTime
     */
    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }





    public static GoodsListBean explainJson(String json, Context context) {
        Gson gson = new Gson();
        try {
            GoodsListBean experLightBean = gson.fromJson(json,
                    GoodsListBean.class);
            return experLightBean;
        } catch (Exception e) {
            Log.d("dataExplainJson", e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
