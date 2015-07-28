package com.huiyin.bean;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.huiyin.utils.LogUtil;

/**
 * 账户安全数据
 * @author 刘远祺
 */
public class AccountSecurity extends BaseBean {
	public String PHONE;//手机号码
	public String EMAIL_STATUS;//邮箱验证状态（1，是，0否）
	public String PWD_SFAE;//密码强度  1弱  2中  3强
	public String EMAIL;//邮箱
	public String curTime;//系统时间
	
	public boolean hasBoundEmail(){
		if(null != EMAIL_STATUS && "1".equals(EMAIL_STATUS)){
			return true;
		}
		return false;
	}
	
	public static AccountSecurity explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			AccountSecurity accountSecurity = gson.fromJson(json, AccountSecurity.class);
			return accountSecurity;
		} catch (Exception e) {
			LogUtil.d("dataExplainJson", e.toString());
			//Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}
}
