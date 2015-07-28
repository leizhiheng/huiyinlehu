package com.huiyin.ui.user.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.account.FragmentBindEmailSetpOne.OnStepToCheckEmailListener;
import com.huiyin.ui.user.account.FragmentBindEmailStepTwo.OnStepToBindFinishListener;

/**
 * 绑定邮箱
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-6
 */
public class BindEmailActivity extends BaseActivity {

	public static final String EMAIL = "email";

	private FragmentManager fragmentManager;
	
	private FragmentBindEmailSetpOne fragmentBindEmailSetpOne;
	
	private FragmentBindEmailStepTwo fragmentBindEmailStepTwo;
	
	private ImageView iv_icon_one;
	
	private ImageView iv_icon_two;
	
	private ImageView tvStep2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_bind_email);
		initData();
	}

	@Override
	protected void onFindViews() {
		super.onFindViews();
		
		iv_icon_one = (ImageView) findViewById(R.id.iv_icon_one);
		iv_icon_two = (ImageView) findViewById(R.id.iv_icon_two);
		tvStep2 = (ImageView) findViewById(R.id.iv_bind_email_step_2);
	}
	
	/**
	 * 初始化数据
	 */
	public void initData() {

		// 获取从账户安全里面传递过来的邮箱(未激活邮箱)
		Intent intent = getIntent();
		String unActiveEmail = intent.getStringExtra(EMAIL);

		fragmentManager = this.getSupportFragmentManager();

		// 步骤一(填写邮箱)
		fragmentBindEmailSetpOne = new FragmentBindEmailSetpOne();
		if (null != unActiveEmail) {
			fragmentBindEmailSetpOne.setUnActiveEmail(unActiveEmail);
		}

		// 步骤二(发送邮箱，获取激活链接)
		fragmentBindEmailStepTwo = new FragmentBindEmailStepTwo();
		fragmentBindEmailSetpOne.setOnStepToCheckEmailListener(new OnStepToCheckEmailListener() {

			@Override
			public void stepToCheckEmail(String email, String emailUrl) {

				// 设置填写的邮箱到步骤二，并显示到步骤二
				fragmentBindEmailStepTwo.setEmail(email, emailUrl);
				replaceFragment(fragmentBindEmailStepTwo);

				tvStep2.setImageResource(R.drawable.account_modify_pw_sel);
				iv_icon_one.setImageResource(R.drawable.sjjt1);
				iv_icon_two.setImageResource(R.drawable.sjjt1);
			}
		});
		fragmentBindEmailStepTwo.setOnStepToBindFinishListener(new OnStepToBindFinishListener() {

			@Override
			public void bindSuccess(boolean isSuccess) {

			}
		});

		//默认显示第一步
		replaceFragment(fragmentBindEmailSetpOne);
	}

	public void replaceFragment(Fragment fragment) {
		fragmentManager.beginTransaction().replace(R.id.fragment_container_bind_emial, fragment).commit();
	}
}
