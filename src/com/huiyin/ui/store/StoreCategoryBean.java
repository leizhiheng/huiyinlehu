package com.huiyin.ui.store;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.bean.BaseBean;

import java.util.ArrayList;

/**
 * Created by justme on 15/5/12.
 */
public class StoreCategoryBean extends BaseBean{
    private ArrayList<StoreCategory> storeCategoryList;

    public ArrayList<StoreCategory> getStoreCategoryList() {
        return storeCategoryList;
    }

    public void setStoreCategoryList(ArrayList<StoreCategory> storeCategoryList) {
        this.storeCategoryList = storeCategoryList;
    }

    public static StoreCategoryBean explainJson(String json, Context context) {

        Gson gson = new Gson();
        try {
            StoreCategoryBean experLightBean = gson.fromJson(json,
                    StoreCategoryBean.class);
            return experLightBean;
        } catch (Exception e) {
            Log.d("dataExplainJson", e.toString());
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            return null;

        }
    }
}
