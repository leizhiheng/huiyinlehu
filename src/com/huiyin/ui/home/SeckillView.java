package com.huiyin.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.HomeSeckillBean;
import com.huiyin.bean.HomeSeckillListItemBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.seckill.SeckillActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;

import java.util.Date;

public class SeckillView extends LinearLayout {
	private Context mContext;
	private HomeSeckillBean data;

	// Content View Elements
//	private TextView name;
	private TextView time;
	private LinearLayout time_count_layout;
	private TextView hourTextView;
	private TextView minuteTextView;
	private TextView secondTextView;
	private TextView more;

//	private LinearLayout linearLayout1;
	private ImageView imageView1;
	private TextView textViewDiscount1;
	private TextView textViewname1;
	private TextView textViewprice1;
	private ImageView imageView2;
	private TextView textViewDiscount2;
	private TextView textViewname2;
	private TextView textViewprice2;
	private ImageView imageView3;
	private TextView textViewDiscount3;
	private TextView textViewname3;
	private TextView textViewprice3;
	private LinearLayout linearLayout2;
	private ImageView imageView4;
	private TextView textViewDiscount4;
	private TextView textViewname4;
	private TextView textViewprice4;
	private ImageView imageView5;
	private TextView textViewDiscount5;
	private TextView textViewname5;
	private TextView textViewprice5;
	private ImageView imageView6;
	private TextView textViewDiscount6;
	private TextView textViewname6;
	private TextView textViewprice6;

	// End Of Content View Elements
	private OnClickListener mClickListener;
	private CountDownTimer mCountDownTimer;

	public SeckillView(Context context) {
		super(context);
		findView();
		SetListener();
		InitView();

	}

	public SeckillView(Context context, AttributeSet attrs) {
		super(context, attrs);
		findView();
		SetListener();
		InitView();

	}

	public SeckillView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		findView();
		SetListener();
		InitView();
	}

	public SeckillView(Context context, HomeSeckillBean Data) {
		super(context);
		this.mContext = context;
		this.data = Data;
		findView();
		SetListener();
		InitView();

	}

	private void findView() {
		LayoutInflater.from(mContext).inflate(R.layout.seckill_view, this, true);

//		name = (TextView) findViewById(R.id.seckill_name);
		time = (TextView) findViewById(R.id.seckill_adv);
		time_count_layout = (LinearLayout) findViewById(R.id.time_count_layout);
		hourTextView = (TextView) findViewById(R.id.hourTextView);
		minuteTextView = (TextView) findViewById(R.id.minuteTextView);
		secondTextView = (TextView) findViewById(R.id.secondTextView);
		more = (TextView) findViewById(R.id.seckill_more);
//		linearLayout1 = (LinearLayout) findViewById(R.id.LinearLayout1);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		textViewDiscount1 = (TextView) findViewById(R.id.textViewDiscount1);
		textViewname1 = (TextView) findViewById(R.id.textViewname1);
		textViewprice1 = (TextView) findViewById(R.id.textViewprice1);
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		textViewDiscount2 = (TextView) findViewById(R.id.textViewDiscount2);
		textViewname2 = (TextView) findViewById(R.id.textViewname2);
		textViewprice2 = (TextView) findViewById(R.id.textViewprice2);
		imageView3 = (ImageView) findViewById(R.id.imageView3);
		textViewDiscount3 = (TextView) findViewById(R.id.textViewDiscount3);
		textViewname3 = (TextView) findViewById(R.id.textViewname3);
		textViewprice3 = (TextView) findViewById(R.id.textViewprice3);
		linearLayout2 = (LinearLayout) findViewById(R.id.LinearLayout2);
		imageView4 = (ImageView) findViewById(R.id.imageView4);
		textViewDiscount4 = (TextView) findViewById(R.id.textViewDiscount4);
		textViewname4 = (TextView) findViewById(R.id.textViewname4);
		textViewprice4 = (TextView) findViewById(R.id.textViewprice4);
		imageView5 = (ImageView) findViewById(R.id.imageView5);
		textViewDiscount5 = (TextView) findViewById(R.id.textViewDiscount5);
		textViewname5 = (TextView) findViewById(R.id.textViewname5);
		textViewprice5 = (TextView) findViewById(R.id.textViewprice5);
		imageView6 = (ImageView) findViewById(R.id.imageView6);
		textViewDiscount6 = (TextView) findViewById(R.id.textViewDiscount6);
		textViewname6 = (TextView) findViewById(R.id.textViewname6);
		textViewprice6 = (TextView) findViewById(R.id.textViewprice6);
	}

	private void SetListener() {
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, SeckillActivity.class);
				mContext.startActivity(intent);
			}
		});
		mClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
                int id=v.getId();
                int position=0;
                switch (id){
                    case R.id.imageView1:
                        position=0;
                        break;
                    case R.id.imageView2:
                        position=1;
                        break;
                    case R.id.imageView3:
                        position=2;
                        break;
                    case R.id.imageView4:
                        position=3;
                        break;
                    case R.id.imageView5:
                        position=4;
                        break;
                    case R.id.imageView6:
                        position=5;
                        break;
                }
                HomeSeckillListItemBean temp1=data.getCOMMODITY_LIST().get(position);
//				int ID = (Integer) v.getTag();
				Intent intent = new Intent(mContext, ProductsDetailActivity.class);
				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, temp1.GOODS_ID);
				intent.putExtra(ProductsDetailActivity.GOODS_NO, temp1.GOODS_NO);
				intent.putExtra(ProductsDetailActivity.STORE_ID, temp1.STORE_ID);
				mContext.startActivity(intent);
			}
		};
	}

    /**
     * 隐藏折扣
     * @param visible
     */
    public void setDiscountVisible(int visible){
        textViewDiscount1.setVisibility(visible);
        textViewDiscount2.setVisibility(visible);
        textViewDiscount3.setVisibility(visible);
        textViewDiscount4.setVisibility(visible);
        textViewDiscount5.setVisibility(visible);
        textViewDiscount6.setVisibility(visible);
    }

	private void InitView() {
		// name.setText(data.getSECKILL_NAME());
		if (!StringUtils.isBlank(data.getSTART_TIME()) && !StringUtils.isBlank(data.getEND_TIME())
				&& !StringUtils.isBlank(data.getCurDate())) {
            TextTask(data.getSTART_TIME(), data.getEND_TIME(), data.getCurDate());
        }

        if(1==data.getPROMOTION_TYPE()){//打折
            setDiscountVisible(View.VISIBLE);
        }else{
            setDiscountVisible(View.GONE);
        }

		if (data.getCOMMODITY_LIST().size() > 0) {
			HomeSeckillListItemBean temp1 = data.getCOMMODITY_LIST().get(0);
//			textViewDiscount1.setText(MathUtil.keep1decimal(temp1.getDISCOUNT()) + "折");
            setTextViewPromotionType(textViewDiscount1,temp1);
			textViewname1.setText(temp1.getCOMMODITY_NAME());
			textViewprice1.setText(MathUtil.priceForAppWithSign(temp1.getPRICE()));
			ImageManager.LoadWithServer(temp1.getCOMMODITY_IMAGE_PATH(), imageView1);
			imageView1.setTag(temp1.GOODS_ID);
			imageView1.setOnClickListener(mClickListener);
		} else {
			return;
		}
		if (data.getCOMMODITY_LIST().size() > 1) {
			HomeSeckillListItemBean temp1 = data.getCOMMODITY_LIST().get(1);
//			textViewDiscount2.setText(MathUtil.keep1decimal(temp1.getDISCOUNT()) + "折");
            setTextViewPromotionType(textViewDiscount2,temp1);
			textViewname2.setText(temp1.getCOMMODITY_NAME());
			textViewprice2.setText(MathUtil.priceForAppWithSign(temp1.getPRICE()));
			ImageManager.LoadWithServer(temp1.getCOMMODITY_IMAGE_PATH(), imageView2);
			imageView2.setTag(temp1.GOODS_ID);
			imageView2.setOnClickListener(mClickListener);
		} else {
			return;
		}
		if (data.getCOMMODITY_LIST().size() > 2) {
			HomeSeckillListItemBean temp1 = data.getCOMMODITY_LIST().get(2);
//			textViewDiscount3.setText(MathUtil.keep1decimal(temp1.getDISCOUNT()) + "折");
            setTextViewPromotionType(textViewDiscount3,temp1);
			textViewname3.setText(temp1.getCOMMODITY_NAME());
			textViewprice3.setText(MathUtil.priceForAppWithSign(temp1.getPRICE()));
			ImageManager.LoadWithServer(temp1.getCOMMODITY_IMAGE_PATH(), imageView3);
			imageView3.setTag(temp1.GOODS_ID);
			imageView3.setOnClickListener(mClickListener);
		} else {
			return;
		}
		if (data.getCOMMODITY_LIST().size() > 3) {
			linearLayout2.setVisibility(View.VISIBLE);
			HomeSeckillListItemBean temp1 = data.getCOMMODITY_LIST().get(3);
//			textViewDiscount4.setText(MathUtil.keep1decimal(temp1.getDISCOUNT()) + "折");
            setTextViewPromotionType(textViewDiscount4,temp1);
			textViewname4.setText(temp1.getCOMMODITY_NAME());
			textViewprice4.setText(MathUtil.priceForAppWithSign(temp1.getPRICE()));
			ImageManager.LoadWithServer(temp1.getCOMMODITY_IMAGE_PATH(), imageView4);
			imageView4.setTag(temp1.GOODS_ID);
			imageView4.setOnClickListener(mClickListener);
		} else {
			return;
		}
		if (data.getCOMMODITY_LIST().size() > 4) {
			HomeSeckillListItemBean temp1 = data.getCOMMODITY_LIST().get(4);
//			textViewDiscount5.setText(MathUtil.keep1decimal(temp1.getDISCOUNT()) + "折");
            setTextViewPromotionType(textViewDiscount5,temp1);
			textViewname5.setText(temp1.getCOMMODITY_NAME());
			textViewprice5.setText(MathUtil.priceForAppWithSign(temp1.getPRICE()));
			ImageManager.LoadWithServer(temp1.getCOMMODITY_IMAGE_PATH(), imageView5);
			imageView5.setTag(temp1.GOODS_ID);
			imageView5.setOnClickListener(mClickListener);
		} else {
			return;
		}
		if (data.getCOMMODITY_LIST().size() > 5) {
			HomeSeckillListItemBean temp1 = data.getCOMMODITY_LIST().get(5);
//			textViewDiscount6.setText(MathUtil.keep1decimal(temp1.getDISCOUNT()) + "折");
            setTextViewPromotionType(textViewDiscount6,temp1);
			textViewname6.setText(temp1.getCOMMODITY_NAME());
			textViewprice6.setText(MathUtil.priceForAppWithSign(temp1.getPRICE()));
			ImageManager.LoadWithServer(temp1.getCOMMODITY_IMAGE_PATH(), imageView6);
			imageView6.setTag(temp1.GOODS_ID);
			imageView6.setOnClickListener(mClickListener);
		}
	}

    public void setTextViewPromotionType(TextView tv,HomeSeckillListItemBean temp1){
        String show="";
        switch (data.PROMOTION_TYPE){
            case 1://打折
                show=MathUtil.keep1decimal(temp1.getDISCOUNT()) + "折";
                break;
            case 2://直降
                show="省"+MathUtil.priceForAppWithSign(temp1.PRICE);
                break;
            case 3://折后价"PROMOTION_TYPE": 1.打折，2.直降，3.折后价
                show="折后价";
                break;
        }
        tv.setText(show);
    }

	private long nowSecond = 0;

	protected void TextTask(final String starttime, final String endtime, final String curtime) {
		Date start = StringUtils.toDate(starttime);
		Date end = StringUtils.toDate(endtime);
		Date nowTime = StringUtils.toDate(curtime);

		long startSecond = start.getTime();
		long endSecond = end.getTime();
		if (nowSecond == 0)
			nowSecond = nowTime.getTime();

		// Log.i("time", "nowSecond:" + nowSecond);
		// Log.i("time", "startSecond:" + startSecond);
		// Log.i("time", "endSecond:" + endSecond);

		if (mCountDownTimer != null) {
			mCountDownTimer.cancel();
		}
		if (nowSecond < startSecond) {
			time.setText("距离开始：");
			mCountDownTimer = new CountDownTimer(startSecond - nowSecond, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int hour = (int) (millisUntilFinished / 3600000);
					int minute = (int) ((millisUntilFinished % 3600000) / 60000);
					int second = (int) ((millisUntilFinished % 60000) / 1000);
					hourTextView.setText(MathUtil.intForTwoSize(hour));
					minuteTextView.setText(MathUtil.intForTwoSize(minute));
					secondTextView.setText(MathUtil.intForTwoSize(second));
					nowSecond += 1000;
					// Log.i("time", "nowSecond:" + nowSecond);
					// Log.i("time", "millisUntilFinished:" +
					// millisUntilFinished);
				}

				@Override
				public void onFinish() {
					nowSecond += 1000;
					TextTask(starttime, endtime, curtime);
				}
			};
			mCountDownTimer.start();
		} else if (nowSecond < endSecond) {
			time.setText("还剩：");
			mCountDownTimer = new CountDownTimer(endSecond - nowSecond, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					int hour = (int) (millisUntilFinished / 3600000);
					int minute = (int) ((millisUntilFinished % 3600000) / 60000);
					int second = (int) ((millisUntilFinished % 60000) / 1000);
					hourTextView.setText(MathUtil.intForTwoSize(hour));
					minuteTextView.setText(MathUtil.intForTwoSize(minute));
					secondTextView.setText(MathUtil.intForTwoSize(second));
					nowSecond += 1000;
					// Log.i("time", "nowSecond:" + nowSecond);
					// Log.i("time", "millisUntilFinished:" +
					// millisUntilFinished);
				}

				@Override
				public void onFinish() {
					nowSecond += 1000;
					TextTask(starttime, endtime, curtime);
				}
			};
			mCountDownTimer.start();
		} else if (nowSecond >= endSecond) {
			// Log.i("time", "nowSecond:" + nowSecond);
			// Log.i("time", "endSecond:" + endSecond);
			time.setText("活动结束");
			time_count_layout.setVisibility(View.GONE);
		}

	}
}
