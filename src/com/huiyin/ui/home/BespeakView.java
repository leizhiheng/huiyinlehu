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
import com.huiyin.bean.ProductBespeakBean;
import com.huiyin.ui.bespeak.BespeakActivity;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.utils.ImageManager;

public class BespeakView extends LinearLayout {
	private Context mContext;
	private ProductBespeakBean data;

	private TextView bespeakview_name;
	private TextView bespeakview_adv;
	private TextView bespeakview_more;
	private ImageView bespeakview_iv1;
	private ImageView bespeakview_iv2;
	private ImageView bespeakview_iv3;

	public BespeakView(Context context) {
		super(context);
		findView();
		SetListener();
		InitView();
	}

	public BespeakView(Context context, AttributeSet attrs) {
		super(context, attrs);
		findView();
		SetListener();
		InitView();
	}

	public BespeakView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		findView();
		SetListener();
		InitView();
	}

	public BespeakView(Context context, ProductBespeakBean Data) {
		super(context);
		mContext = context;
		data = Data;
		findView();
		SetListener();
		InitView();
	}

	private void findView() {
		LayoutInflater.from(mContext)
				.inflate(R.layout.bespeak_view, this, true);

		bespeakview_name = (TextView) findViewById(R.id.bespeakview_name);
		bespeakview_adv = (TextView) findViewById(R.id.bespeakview_adv);
		bespeakview_more = (TextView) findViewById(R.id.bespeakview_more);
		bespeakview_iv1 = (ImageView) findViewById(R.id.bespeakview_iv1);
		bespeakview_iv2 = (ImageView) findViewById(R.id.bespeakview_iv2);
		bespeakview_iv3 = (ImageView) findViewById(R.id.bespeakview_iv3);

	}

	private void SetListener() {
		bespeakview_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, BespeakActivity.class);
				mContext.startActivity(intent);
			}
		});
	}

	private void InitView() {
		bespeakview_name.setText(data.getANAME());
		bespeakview_adv.setText(data.getADVER());

		bespeakview_iv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                goToProductsDetail(data.CID1,data.PID1,data.STORE_ID1,data.GOODS_NO1);
//				Intent intent = new Intent(mContext, ProductsDetailActivity.class);
//				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID,
//						String.valueOf(data.CID1));
//				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_subscribe_ID,
//						data.PID1);
//				mContext.startActivity(intent);
			}
		});
		ImageManager.LoadWithServer(data.IMG1, bespeakview_iv1);

		bespeakview_iv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                goToProductsDetail(data.CID2,data.PID2,data.STORE_ID2,data.GOODS_NO2);
//				Intent intent = new Intent(mContext, ProductsDetailActivity.class);
//				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID,
//						String.valueOf(data.CID2));
//				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_subscribe_ID,
//						data.PID2);
//				mContext.startActivity(intent);
			}
		});
		ImageManager.LoadWithServer(data.IMG2, bespeakview_iv2);

		bespeakview_iv3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                goToProductsDetail(data.CID3,data.PID3,data.STORE_ID3,data.GOODS_NO3);
//				Intent intent = new Intent(mContext, ProductsDetailActivity.class);
//				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID,
//						String.valueOf(data.CID3));
//				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_subscribe_ID,
//						data.PID3);
//				mContext.startActivity(intent);
			}
		});
		ImageManager.LoadWithServer(data.IMG3, bespeakview_iv3);

	}

    public void goToProductsDetail(String GOODS_ID,String ID,String STORE_ID,String GOODS_NO){
        Intent intent = new Intent(mContext, ProductsDetailActivity.class);
        intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID,
                GOODS_ID);
        if(!"".equals(ID)){
            int id=Integer.parseInt(ID);
            intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_subscribe_ID,
                    id);
        }
        intent.putExtra(ProductsDetailActivity.STORE_ID,
                STORE_ID);
        intent.putExtra(ProductsDetailActivity.GOODS_NO,
                GOODS_NO);
        mContext.startActivity(intent);
    }
}
