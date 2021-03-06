package com.huiyin.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.HomeFlashBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.flash.FlashActivity;
import com.huiyin.utils.ImageManager;

public class FlashView extends LinearLayout {
	private Context mContext;
	private HomeFlashBean data;
	private OnClickListener mClickListener;
	// Content View Elements

	private TextView flashview_name;
	private TextView flashview_adv;
	private TextView flashview_more;
	private ImageView flashview_iv1;
	private ImageView flashview_iv2;
	private ImageView flashview_iv3;
	private ImageView flashview_iv4;

	// End Of Content View Elements

	public FlashView(Context context) {
		super(context);
		findView();
		SetListener();
		InitView();

	}

	public FlashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		findView();
		SetListener();
		InitView();

	}

	public FlashView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		findView();
		SetListener();
		InitView();
	}

	public FlashView(Context context, HomeFlashBean Data) {
		super(context);
		this.mContext = context;
		this.data = Data;
		findView();
		SetListener();
		InitView();

	}

	private void findView() {
		LayoutInflater.from(mContext).inflate(R.layout.flash_view, this, true);

		flashview_name = (TextView) findViewById(R.id.flashview_name);
		flashview_adv = (TextView) findViewById(R.id.flashview_adv);
		flashview_more = (TextView) findViewById(R.id.flashview_more);
		flashview_iv1 = (ImageView) findViewById(R.id.flashview_iv1);
		flashview_iv2 = (ImageView) findViewById(R.id.flashview_iv2);
		flashview_iv3 = (ImageView) findViewById(R.id.flashview_iv3);
		flashview_iv4 = (ImageView) findViewById(R.id.flashview_iv4);

	}

	private void SetListener() {
		flashview_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, FlashActivity.class);
				intent.putExtra("maintitle", data.ACTIVE_NAME);
				mContext.startActivity(intent);
			}
		});
		mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//跳转到商品详情
				int ID = Integer.parseInt(v.getTag().toString().trim());
				Intent intent = new Intent(mContext, ProductsDetailActivity.class);
				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, String.valueOf(ID));
				mContext.startActivity(intent);
			}
		};
	}

	private void InitView() {
		flashview_name.setText(data.ACTIVE_NAME);
		flashview_adv.setText(data.ADVER);
		
		flashview_iv1.setTag(data.GOODS_ID1);
		flashview_iv1.setOnClickListener(mClickListener);
		ImageManager.LoadWithServer(data.IMG1, flashview_iv1);
		
		flashview_iv2.setTag(data.GOODS_ID2);
		flashview_iv2.setOnClickListener(mClickListener);
		ImageManager.LoadWithServer(data.IMG2, flashview_iv2);
		
		flashview_iv3.setTag(data.GOODS_ID3);
		flashview_iv3.setOnClickListener(mClickListener);
		ImageManager.LoadWithServer(data.IMG3, flashview_iv3);
		
		flashview_iv4.setTag(data.GOODS_ID4);
		flashview_iv4.setOnClickListener(mClickListener);
		ImageManager.LoadWithServer(data.IMG4, flashview_iv4);
	}
}
