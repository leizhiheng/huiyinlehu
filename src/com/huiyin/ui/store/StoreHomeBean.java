package com.huiyin.ui.store;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.huiyin.bean.BaseBean;

/**
 * Created by justme on 15/5/9.
 */
public class StoreHomeBean extends BaseBean{

    @Expose
    private StoreInfo storeInfo;
    @Expose
    private String curTime;

    /**
     *
     * @return
     * The storeInfo
     */
    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    /**
     *
     * @param storeInfo
     * The storeInfo
     */
    public void setStoreInfo(StoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }

    /**
     *
     * @return
     * The curTime
     */
    public String getCurTime() {
        return curTime;
    }

    /**
     *
     * @param curTime
     * The curTime
     */
    public void setCurTime(String curTime) {
        this.curTime = curTime;
    }


    public static StoreHomeBean explainJson(String json, Context context) {

        Gson gson = new Gson();
        try {
            StoreHomeBean experLightBean = gson.fromJson(json,
                    StoreHomeBean.class);
            return experLightBean;
        } catch (Exception e) {
            Log.d("dataExplainJson", e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            return null;

        }
    }
}