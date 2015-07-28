package com.huiyin.ui.lehuvoucher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.ui.introduce.IntroduceActivity;

public class LehuVoucherActivity extends BaseCameraActivity {

	// Content View Elements

	private TextView left_ib;
	private TextView middle_title_tv;
	private TextView apply;
	private TextView upload;
	private Button apply_info;
	private View viewpager_index;
	private ViewPager lehu_voucher_layout_viewpager;

	// End Of Content View Elements
	private Context mContext;
	private LehuVoicherViewPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lehu_voucher_layout);
		mContext = this;

		findView();
		setListener();
		InitData();
	}

	private void findView() {
		left_ib = (TextView) findViewById(R.id.left_ib);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("乐虎券申领");
		apply = (TextView) findViewById(R.id.apply);
		upload = (TextView) findViewById(R.id.upload);
		apply_info = (Button) findViewById(R.id.apply_info);
		viewpager_index = (View) findViewById(R.id.viewpager_index);
		lehu_voucher_layout_viewpager = (android.support.v4.view.ViewPager) findViewById(R.id.lehu_voucher_layout_viewpager);

	}

	private void setListener() {
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!apply.isSelected()) {
					apply.setSelected(true);
					upload.setSelected(false);
					lehu_voucher_layout_viewpager.setCurrentItem(0);
				}
			}
		});
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!upload.isSelected()) {
					apply.setSelected(false);
					upload.setSelected(true);
					lehu_voucher_layout_viewpager.setCurrentItem(1);
				}
			}
		});
		apply_info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, IntroduceActivity.class);
				intent.putExtra("id", 159);
				startActivity(intent);
			}
		});
		lehu_voucher_layout_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// Log.i("positionOffset", "positionOffset:"+positionOffset);
				// Log.i("positionOffsetPixels",
				// "positionOffsetPixels:"+positionOffsetPixels);
				// viewpager_index.setLeft(positionOffsetPixels);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}

			@Override
			public void onPageSelected(int position) {
				if (position == 1) {
					apply.setSelected(false);
					upload.setSelected(true);
					TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
							Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
					animation.setDuration(500);
					animation.setFillAfter(true);
					animation.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
					viewpager_index.startAnimation(animation);
				} else if (position == 0) {
					apply.setSelected(true);
					upload.setSelected(false);
					TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f,
							Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
					animation.setDuration(500);
					animation.setFillAfter(true);
					animation.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
					viewpager_index.startAnimation(animation);
				}
			}
		});
	}

	private void InitData() {
		mAdapter = new LehuVoicherViewPagerAdapter(getSupportFragmentManager());
		lehu_voucher_layout_viewpager.setAdapter(mAdapter);
		apply.setSelected(true);
		upload.setSelected(false);
	}

	@Override
	public void onUpLoadSuccess(String imageUrl, String imageFile) {
		int curIndex = lehu_voucher_layout_viewpager.getCurrentItem();
		Fragment curFrg = mAdapter.getCurrentFragment(curIndex);
		if(null != curFrg && (curFrg instanceof LehuVoucherUploadFragmentNew)){
			
			//将数据传递给fragment
			((LehuVoucherUploadFragmentNew)curFrg).onUpLoadSuccess(imageUrl, imageFile);
		}
	}
}
