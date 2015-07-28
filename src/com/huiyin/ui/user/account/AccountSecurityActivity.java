package com.huiyin.ui.user.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.AccountSecurity;
import com.huiyin.ui.user.LoginLogActivity;
import com.huiyin.utils.StringUtils;

/**
 * 账户安全页面
 * 
 * @author leizhiheng
 * 
 */
public class AccountSecurityActivity extends BaseCameraActivity {

	private static final String TAG = "AccountSecurityActivity";
	private ImageView user_icon;
	private TextView  bind_phone, bind_email;
	private TextView tv_user_name, tv_login_address, tv_login_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_security);
		setRightText(View.GONE);
		setTitle("账户安全");
		
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//显示账户安全信息(根据userinfo)
		showByUserData(null);
		
		//加载账户安全信息 
		loadAccountSecurity();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView(){
		user_icon = (ImageView) findViewById(R.id.menber_icon);
		tv_user_name = (TextView) findViewById(R.id.menber_name);
		tv_login_address = (TextView) findViewById(R.id.login_address);
		tv_login_time = (TextView) findViewById(R.id.login_time);
		bind_email = (TextView) findViewById(R.id.security_binded_email);
		bind_phone = (TextView) findViewById(R.id.security_binded_phone);
	}
	
	/**
	 * 加载账户安全信息
	 */
	private void loadAccountSecurity(){
		String userId = AppContext.mUserInfo.userId;
		RequstClient.accountSecurity(userId, new CustomResponseHandler(mContext, false){
			@Override
			public void onSuccess(String content) {
				
				AccountSecurity accountSecurity = AccountSecurity.explainJson(content, mContext);
				if(null != accountSecurity){
				
					//将用户信息同步到userInfo里面
					//手机号码  , 邮箱验证状态（1，是，0否）,密码强度  1弱  2中  3强,邮箱
					UserInfo userInfo = AppContext.mUserInfo;
					//userInfo.phone = accountSecurity.PHONE;
					userInfo.emailStatus = accountSecurity.EMAIL_STATUS;
					userInfo.pwdSfae = accountSecurity.PWD_SFAE;
					userInfo.email = accountSecurity.EMAIL;
					
					//显示信息
					showByUserData(accountSecurity);
				}
			}
		});
	}
	
	/**
	 * 显示数据
	 */
	public void showByUserData(AccountSecurity accountSecurity) {
		
		UserInfo userInfo = AppContext.mUserInfo;
		
		//手机
		bind_phone.setText(StringUtils.ensurePhoneNum(userInfo.phone));
		
		//头像
		loadUserHead(user_icon);
		
		//名称
		String userName = userInfo.userName;
		if(userName.equals(userInfo.phone)){
			userName = StringUtils.ensurePhoneNum(userName);//如果用户名是手机号，则加密处理后再显示
		}
		tv_user_name.setText("尊敬的" + userName);
		
		//登录时间，地址
		tv_login_time.setText(userInfo.lastDate);
		tv_login_address.setText(userInfo.loginAddress);
		
		//用户等级
		setPwLevel(userInfo.pwdSfae);
		
		if (!userInfo.isBoundEmail()) {
			bind_email.setText("还未设置绑定");
		} else {
			bind_email.setText(StringUtils.ensureEmailAddress(userInfo.email));
		}
		
		
		//从服务器重新获取返回的信息
		if(null != accountSecurity){
			
			bind_phone.setText(accountSecurity.PHONE);
			
			if(!accountSecurity.hasBoundEmail()){
				bind_email.setText("还未设置绑定");
			}else{
				
				//邮箱显示明文
				bind_email.setText(StringUtils.ensureEmailAddress(accountSecurity.EMAIL));
			}
			setPwLevel(accountSecurity.PWD_SFAE);
		}
	}

	public void setPwLevel(String pwdSfae) {
		if (pwdSfae.equals("1")) {

			findViewById(R.id.level_low).setBackgroundColor(getResources().getColor(R.color.yt_cap_yellow));
			findViewById(R.id.level_mid).setBackgroundColor(getResources().getColor(R.color.gray7));
			findViewById(R.id.level_high).setBackgroundColor(getResources().getColor(R.color.gray7));

		} else if (pwdSfae.equals("2")) {

			findViewById(R.id.level_low).setBackgroundColor(getResources().getColor(R.color.yt_cap_yellow));
			findViewById(R.id.level_mid).setBackgroundColor(getResources().getColor(R.color.yt_cap_yellow));
			findViewById(R.id.level_high).setBackgroundColor(getResources().getColor(R.color.gray7));

		} else if (pwdSfae.equals("3")) {

			findViewById(R.id.level_low).setBackgroundColor(getResources().getColor(R.color.yt_cap_yellow));
			findViewById(R.id.level_mid).setBackgroundColor(getResources().getColor(R.color.yt_cap_yellow));
			findViewById(R.id.level_high).setBackgroundColor(getResources().getColor(R.color.yt_cap_yellow));
		}
	}
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.menber_icon:

			//头像
			showCameraPopwindow(v, true, true, true);
			
			break;
		case R.id.check_login_record:
			
			//登录日志
			Intent intent=new Intent(mContext,LoginLogActivity.class);
            mContext.startActivity(intent);
			
			break;
		case R.id.change_login_password:
		case R.id.layout_login_pw:
			
			//修改密码
			startActivity(new Intent(AccountSecurityActivity.this, PasswordModifyActivity.class));
			
			break;
		case R.id.layout_bind_phone:
			
			//绑定手机
			startActivity(new Intent(AccountSecurityActivity.this, BoundPhoneModifyActivity.class));
			
			break;
		case R.id.layout_bind_email:
			
			//绑定邮箱
			UserInfo userInfo = AppContext.mUserInfo;
			if (!userInfo.isBoundEmail()) {
				
				//设置邮箱
				Intent intentBind = new Intent(mContext, BindEmailActivity.class);
				if(null != userInfo.email){
					
					// 如果之前绑定过，但未激活，把邮箱带过去
					intentBind.putExtra(BindEmailActivity.EMAIL, userInfo.email);
				}
				startActivity(intentBind);
				
			} else {
				
				//修改邮箱
				startActivity(new Intent(AccountSecurityActivity.this, BoundEmailModifyActivity.class));
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onUpLoadSuccess(String imageUrl, String imageFile) {
		super.onUpLoadSuccess(imageUrl, imageFile);
		
		//用户头像赋值
		if(null != imageUrl){
			AppContext.mUserInfo.img = imageUrl;
			loadUserHead(user_icon);
		}
	}
}
