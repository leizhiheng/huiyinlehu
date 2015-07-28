package com.huiyin.ui.show;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.ui.user.LoginActivity;

public class TheShowActivity extends FragmentActivity implements
		OnClickListener, OnCheckedChangeListener {

	private TextView ab_back, ab_title;
	private ImageView ab_right_btn;

	private FragmentManager fragmentManager;
	private AttentionFragment attentionFragment;
	private RecommendFragment recommendFragment;
	private AllFragment allFragment;
	// add by zhyao @2015/6/22 添加我的秀场
	private MyShowFragment myShowFragment;

	private RadioGroup main_radio;

	private int currentIndex = R.id.theshow_all;

	// add by zhyao @2015/6/22 添加我的秀场
	private RadioButton theshow_tuijian, theshow_guanzhu, theshow_all, theshow_myshow;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_the_show);

		fragmentManager = getSupportFragmentManager();

		initView();
	}

	private void initView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_right_btn = (ImageView) findViewById(R.id.ab_right_btn);
		ab_right_btn.setImageResource(R.drawable.ab_ic_add);
		ab_back.setOnClickListener(this);
		ab_right_btn.setOnClickListener(this);
		ab_title.setText("秀场");

		main_radio = (RadioGroup) findViewById(R.id.main_radio);

		main_radio.setOnCheckedChangeListener(this);
		theshow_tuijian = (RadioButton) findViewById(R.id.theshow_tuijian);
		theshow_guanzhu = (RadioButton) findViewById(R.id.theshow_guanzhu);
		theshow_all = (RadioButton) findViewById(R.id.theshow_all);
		// add by zhyao @2015/6/22 添加我的秀场
		theshow_myshow = (RadioButton) findViewById(R.id.theshow_myshow);

		main_radio.check(currentIndex);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ab_back:
			finish();
			break;
		case R.id.ab_right_btn:
			// 登录判断
			if (AppContext.userId == null) {
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				startActivity(intent);
				return;
			} else {
//				startActivity(new Intent(getApplicationContext(),
//						TheShowReleaseActivity.class));
				// modify by zhyao @2015/6/18  跳转新的秀场发布Activity
				startActivity(new Intent(getApplicationContext(),
						TheShowReleaseActivityNew.class));
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.theshow_tuijian:
			if (attentionFragment == null) {
				attentionFragment = new AttentionFragment();
			}
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			transaction.replace(R.id.attion, attentionFragment);
			transaction.commit();
			theshow_tuijian.setSelected(true);
			theshow_guanzhu.setSelected(false);
			theshow_all.setSelected(false);
			// add by zhyao @2015/6/22 添加我的秀场
			theshow_myshow.setSelected(false);
			break;
		case R.id.theshow_guanzhu:
			if (AppContext.userId == null) {
				// 判断登录
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				startActivity(intent);
			}
			if (recommendFragment == null) {
				recommendFragment = new RecommendFragment();
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.attion, recommendFragment);
			transaction.commit();
			theshow_tuijian.setSelected(false);
			theshow_guanzhu.setSelected(true);
			theshow_all.setSelected(false);
			// add by zhyao @2015/6/22 添加我的秀场
			theshow_myshow.setSelected(false);
			break;
		case R.id.theshow_all:
			if (allFragment == null) {
				allFragment = new AllFragment();
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.attion, allFragment);
			transaction.commit();

			theshow_tuijian.setSelected(false);
			theshow_guanzhu.setSelected(false);
			theshow_all.setSelected(true);
			// add by zhyao @2015/6/22 添加我的秀场
			theshow_myshow.setSelected(false);
			break;
		// add by zhyao @2015/6/22 添加我的秀场
		case R.id.theshow_myshow:
			if (AppContext.userId == null) {
				// 判断登录
				Intent intent = new Intent(getApplicationContext(),
						LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				startActivity(intent);
			}
			if (myShowFragment == null) {
				myShowFragment = new MyShowFragment();
			}
			transaction = fragmentManager.beginTransaction();
			transaction.replace(R.id.attion, myShowFragment);
			transaction.commit();
			
			theshow_tuijian.setSelected(false);
			theshow_guanzhu.setSelected(false);
			theshow_all.setSelected(false);
			theshow_myshow.setSelected(true);
			break;
		}
		currentIndex = checkedId;
	}

}
