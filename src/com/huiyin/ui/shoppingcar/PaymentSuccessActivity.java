package com.huiyin.ui.shoppingcar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.CouponBean;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.user.order.AllOrderDetailActivity;
import com.huiyin.utils.LogUtil;
import com.orhanobut.logger.Logger;

public class PaymentSuccessActivity extends Activity implements OnClickListener {

	public static final String INTENT_KEY_ORDER_ID = "order_id";
	public static final String INTENT_KEY_ORDER_NUM = "order_num";

	private String orderId;
	
	private String number;

	private TextView ab_title;

	private TextView tv_orderid, timer;

	private Button btn_shopping, btn_order;
	
	// add by zhyao @2015/6/13  添加消费卷
	private TextView tv_coupous;
	
	private CouponBean couponBean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_success);

		initData();

		initView();
		
		// add by zhyao @2015/6/12 添加查询消费卷
		queryCoupons();
	}
	
	private void initData() {
		orderId = getIntent().getStringExtra(INTENT_KEY_ORDER_ID);
		number = getIntent().getStringExtra(INTENT_KEY_ORDER_NUM);
	}

	private void initView() {
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("支付成功");

		timer = (TextView) findViewById(R.id.timer);

		tv_orderid = (TextView) findViewById(R.id.tv_orderid);
		tv_orderid.setText("订单编号：" + number);
		
		// add by zhyao 添加消费卷提示
		tv_coupous = (TextView) findViewById(R.id.tv_coupous);
		
		btn_shopping = (Button) findViewById(R.id.btn_shopping);
		btn_shopping.setOnClickListener(this);
		
		btn_order = (Button) findViewById(R.id.btn_order);
		btn_order.setOnClickListener(this);

		mHandler.sendEmptyMessageDelayed(HANDLER_TIME_FINISH, 1000);
	}
	
	// add by zhyao @2015/6/12 添加查询消费卷
	public void queryCoupons() {
		RequstClient.queryCoupons(orderId, new CustomResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (LogUtil.isDebug) {
                    Logger.json(content);
                }
				JSONObject obj = null;
                try {
                    obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        UIHelper.showToast(errorMsg);
                        return;
                    }
                    couponBean = new Gson().fromJson(content, CouponBean.class);
    				showCouponeTip();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
			}
		});
	}
	
	// add by zhyao @2015/6/12 添加消费卷编号提示
	private void showCouponeTip() {
		//是消费卷类型
		if(!TextUtils.isEmpty(couponBean.getShowMsg())) {
			tv_coupous.setText(couponBean.getShowMsg());
		}
		// add by zhyao @2015/6/13 展示支付提示
		tv_coupous.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_shopping:// 继续购物
			continueShopping();
			break;
		case R.id.btn_order:// 查看订单
			findOrderInfo();
			break;
		}
	}

	/**
	 * 查看订单
	 */
	public void findOrderInfo() {
		Intent i = new Intent();
		i.setClass(PaymentSuccessActivity.this, AllOrderDetailActivity.class);
		i.putExtra(AllOrderDetailActivity.ORDER_ID, orderId);
		startActivity(i);
		PaymentSuccessActivity.this.finish();
	}

	/**
	 * 继续购物
	 */
	public void continueShopping() {
		
		//指定跳转到首页
		AppContext.MAIN_TASK = AppContext.FIRST_PAGE;
		
		Intent i = new Intent();
		i.setClass(PaymentSuccessActivity.this, MainActivity.class);
		startActivity(i);
		PaymentSuccessActivity.this.finish();

	}

	private int count = 10;

	private static final int HANDLER_TIME_FINISH = 1;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HANDLER_TIME_FINISH:
				if (count > 0) {
					count--;
					mHandler.sendEmptyMessageDelayed(HANDLER_TIME_FINISH, 1000);
					timer.setText(count + "s后将自动返回订单详情");
				} else {
					findOrderInfo();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		mHandler.removeMessages(HANDLER_TIME_FINISH);
		super.onDestroy();
	};
}