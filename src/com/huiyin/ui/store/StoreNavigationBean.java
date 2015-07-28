package com.huiyin.ui.store;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.huiyin.bean.BaseBean;

/**
 * Created by justme on 15/5/15.
 */
public class StoreNavigationBean extends BaseBean {

    @Expose
    private String storeNavigation; //导航栏内容
    @Expose
    private String curTime; //系统时间

    /**
     * @return The storeNavigation
     */
    public String getStoreNavigation() {
        return storeNavigation;
    }

    /**
     * @param storeNavigation The storeNavigation
     */
    public void setStoreNavigation(String storeNavigation) {
        this.storeNavigation = storeNavigation;
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

    public static StoreNavigationBean explainJson(String json, Context context) {
        Gson gson = new Gson();
        try {
            StoreNavigationBean experLightBean = gson.fromJson(json,
                    StoreNavigationBean.class);
            return experLightBean;
        } catch (Exception e) {
            Log.d("dataExplainJson", e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
