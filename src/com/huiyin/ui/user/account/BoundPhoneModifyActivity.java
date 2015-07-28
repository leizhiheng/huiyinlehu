package com.huiyin.ui.user.account;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.account.FragmentCheckIdentity.OnStepToModifyPwListener;
import com.huiyin.ui.user.account.FragmentModifyPhone.OnStepToModifyFinishListener;

/**
 * Created by kuangyong on 2015/6/11.
 */
public class BoundPhoneModifyActivity extends BaseActivity {

	private static final String TAG = "BoundPhoneModifyActivity";
	private FrameLayout fragmentContainer;
	private FragmentCheckIdentity fragmentCheckIdentity;
	private FragmentModifyPhone fragmentModifyPhone;
	private FragmentModifyFinish fragmentModifyFinish;
	
	private TextView tvBack;
	private ImageView ivStep2,ivStep3,rightIcon;
	
	
	private FragmentManager fragmentManager;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_modify_mobilephone);
		
		init();
	}

	public void init() {
		fragmentManager = this.getSupportFragmentManager();
		fragmentCheckIdentity = new FragmentCheckIdentity(this,mHandler);
		fragmentModifyPhone = new FragmentModifyPhone(this,mHandler);
		fragmentModifyFinish = new FragmentModifyFinish();
		
		tvBack = (TextView) findViewById(R.id.btn_back);
		ivStep2 = (ImageView) findViewById(R.id.iv_mod_phone_step_2);
		ivStep3 = (ImageView) findViewById(R.id.iv_mod_phone_step_3);
		rightIcon = (ImageView) findViewById(R.id.right_icon_imageview);
		
		fragmentCheckIdentity.setOnStepToModifyPwListener(new OnStepToModifyPwListener() {
			
			@Override
			public void stepToModifyPw() {
				Log.d(TAG,"stepToModifyPw clicked" );
				replaceFragment(fragmentModifyPhone);
				ivStep2.setImageResource(R.drawable.account_modify_pw_sel);
				rightIcon.setImageResource(R.drawable.sjjt1);
			}
		});
		
		fragmentModifyPhone.setOnStepToModifyFinishListener(new OnStepToModifyFinishListener() {
			
			@Override
			public void stepToModifyFinish() {
				replaceFragment(fragmentModifyFinish);
				ivStep3.setImageResource(R.drawable.account_modify_finish_sel);
				
				//清空用户信息(在用户点击返回的时候，返回主页的乐虎主页)
	        	clearUserInfo();
			}
		});
		
		replaceFragment(fragmentCheckIdentity);
		
		tvBack.setOnClickListener(new OnClickListener() {
			
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
	}
	
	public void replaceFragment(Fragment fragment){
		fragmentManager.beginTransaction().replace(R.id.fragment_container_mod_phone, fragment).commit();
	}
	
	@Override
	public void onBackPressed() {
		
		if(!appIsLogin(false)){
			startToMainActivity(AppContext.LEHU);
		}else{
			super.onBackPressed();
		}
	}
}
