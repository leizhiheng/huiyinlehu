package com.huiyin.ui.user.account;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.account.FragmentModifyPwOne.OnStepToModifyPwListener;
import com.huiyin.ui.user.account.FragmentModifyPwTwo.OnStepToFinishListener;

public class PasswordModifyActivity extends BaseActivity{
	
	private FragmentManager mFragmentManager;
	private FragmentModifyPwOne fragmentModifyPwOne;
	private FragmentModifyPwTwo fragmentModifyPwTwo;
	private FragmentModifyFinish fragmentModifyFinish;
	
	private TextView tv_title_step_2;
	
	private ImageView iv_step_2,iv_step_3, rightIcon;
	
	
	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_modify_mobilephone);
		
		setTitle("修改密码");
		init();
	}
	
	public void init(){
		mHandler = new Handler();
		mFragmentManager = this.getSupportFragmentManager();
		fragmentModifyPwOne = new FragmentModifyPwOne(this,mHandler);
		fragmentModifyPwTwo = new FragmentModifyPwTwo(this);
		fragmentModifyFinish = new FragmentModifyFinish();
		
		fragmentModifyPwOne.setOnStepToModifyPwListener(new OnStepToModifyPwListener() {
			
			@Override
			public void stepToModifyPw() {
				replaceFragment(fragmentModifyPwTwo);
				iv_step_2.setImageResource(R.drawable.account_modify_pw_sel);
				rightIcon.setImageResource(R.drawable.sjjt1);
			}
		});
		fragmentModifyPwTwo.setOnStepToFinishListener(new OnStepToFinishListener() {
			
			@Override
			public void stepToFinish() {
				replaceFragment(fragmentModifyFinish);
				iv_step_3.setImageResource(R.drawable.account_modify_finish_sel);
				
				
				//清空用户信息(在用户点击返回的时候，返回主页的乐虎主页)
	        	clearUserInfo();
	        	
			}
		});
		
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//如果用户已经修改手机成功，则返回跳转到主页，乐虎
				if(!appIsLogin(false)){
					startToMainActivity(AppContext.LEHU);
				}else{
					
					//直接退出
					finish();
				}
			}
		});
		
		TextView titleTv = (TextView)findViewById(R.id.tv_title);
		tv_title_step_2 = (TextView) findViewById(R.id.tv_step_2);
		iv_step_2 = (ImageView) findViewById(R.id.iv_mod_phone_step_2);
		iv_step_3 = (ImageView) findViewById(R.id.iv_mod_phone_step_3);
		rightIcon = (ImageView) findViewById(R.id.right_icon_imageview);
		
		titleTv.setText("修改密码");
		tv_title_step_2.setText("修改登录密码");
		mFragmentManager.beginTransaction().replace(R.id.fragment_container_mod_phone, fragmentModifyPwOne).commit();
	}
	
	public void replaceFragment(Fragment fragment){
		mFragmentManager.beginTransaction().replace(R.id.fragment_container_mod_phone, fragment).commit();
	}
	
	@Override
	public void onBackPressed() {
		//如果用户已经修改手机成功，则返回跳转到主页，乐虎
		if(!appIsLogin(false)){
			startToMainActivity(AppContext.LEHU);
		}else{
			
			//直接退出
			super.onBackPressed();
		}
	}
}
