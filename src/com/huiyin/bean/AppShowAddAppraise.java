package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.utils.LogUtil;


public class AppShowAddAppraise implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type;
	public String msg;
	
	public static AppShowAddAppraise explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			AppShowAddAppraise experLightBean = gson.fromJson(json, AppShowAddAppraise.class);
			return experLightBean;
		} catch (Exception e) {
			LogUtil.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
	
}
