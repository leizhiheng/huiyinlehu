package com.huiyin.utils;

import org.apache.http.Header;

import android.app.Activity;
import android.widget.Toast;

import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;

/**
 * 登录测试
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-20
 */
public class LoginTest {
	
	private Activity context;
	private int loginIndex = 1;
	
	private CountDown cd;
	
	public LoginTest(Activity context){
		this.context = context;
		cd = new CountDown();
	}
	
	public void doLogin(final String userName, final String password) {
		cd.start("登录第"+loginIndex+"次");
		RequstClient.doLogin(userName, password, loginIndex, new CustomResponseHandler(context, false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				
				cd.stop("登录第"+loginIndex+"次");
				Toast.makeText(context, "登录第 "+loginIndex+"次成功", Toast.LENGTH_SHORT).show();
				
				
				cd.start("登录第"+loginIndex+"次   JSON解析");
				String userJson = JSONParseUtils.getJSONObject(content, "user");
				UserInfo mUserInfo = UserInfo.explainJson(userJson, context);
				cd.stop("登录第"+loginIndex+"次   JSON解析");
				
				if(loginIndex == 100){
					LogUtil.i("LoginText", "登录测试结束");
				}else{
					doLogin(userName, password);
					loginIndex++;
				}
			}

		});
	}

}
