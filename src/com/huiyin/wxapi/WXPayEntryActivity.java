package com.huiyin.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.CouponBean;
import com.huiyin.bean.CouponBean.Coupons;
import com.huiyin.pay.wxpay.Constants;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.shoppingcar.CommitOrderActivity;
import com.huiyin.ui.user.order.AllOrderDetailActivity;
import com.huiyin.utils.LogUtil;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler,
		OnClickListener {

	private static final String TAG = "WXPayEntryActivity";

	private IWXAPI api;

	private String price;// 金额
	private String order_id;// 订单号
    // modify by zhyao @2015/5/8  修改注视，添加WAP支付类型
	private int type;// 支付结果 1.支付成功 2.支付失败3.WAP支付返回
	private LinearLayout layout_pay_result;
	private TextView pay_find_orderinfo;// 查看订单
	private TextView pay_continueshop;// 继续购物
	private TextView tv_pay_tip1;// 支付提示
	private TextView tv_pay_orderid;// 订单编号
	private String number;// 订单编号
	private TextView title;// 标题
	private TextView tv_pay_tip2;// 您已成功使用支付宝支付完成交易
	private TextView tv_countdown;// 倒计时
	private ImageView iv_pay_image;// 支付图标
    // modify by zhyao @2015/5/8  修改支付，添加中行和建行
	private int payType;// 支付方式，1.支付宝 2.银联 3.微信4.服务卡5.中行6.建行
	// add by zhyao @2015/6/13  添加消费卷
	private CouponBean couponBean;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);

		Intent intent = getIntent();
		type = intent.getIntExtra("resultType", 1);
		payType = AppContext.payType;
		price = AppContext.price;
		order_id = AppContext.orderId;
		number = AppContext.number;
		findView();
	    // modify by zhyao @2015/5/8  修改判断是不是微信支付
//		if (payType == 1 || payType == 2 || payType == 4)
		if (payType != 3)//不是微信支付
			initData();
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		api.handleIntent(intent, this);
	}

	public void initData() {

		initView();
		setListener();
		// add by zhyao @2015/6/12 添加查询消费卷
		queryCoupons();
		callBack();
	}
	
	// add by zhyao @2015/6/12 添加查询消费卷
	public void queryCoupons() {
		RequstClient.queryCoupons(order_id, new CustomResponseHandler(this){
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
			tv_pay_tip2.setText(couponBean.getShowMsg());
		}
		// add by zhyao @2015/6/13 展示支付提示
		tv_pay_tip2.setVisibility(View.VISIBLE);
	}

	public void findView() {
		layout_pay_result = (LinearLayout) findViewById(R.id.layout_pay_result);
		pay_find_orderinfo = (TextView) findViewById(R.id.pay_find_orderinfo);
		pay_continueshop = (TextView) findViewById(R.id.pay_continueshop);
		tv_pay_tip1 = (TextView) findViewById(R.id.tv_pay_tip1);
		tv_pay_tip2 = (TextView) findViewById(R.id.tv_pay_tip2);
		tv_pay_orderid = (TextView) findViewById(R.id.tv_pay_orderid);
		title = (TextView) findViewById(R.id.title);
		tv_countdown = (TextView) findViewById(R.id.tv_countdown);
		iv_pay_image = (ImageView) findViewById(R.id.iv_pay_image);
	}

	/**
	 * 初始化数据
	 */
	public void initView() {
		TextPaint tp = title.getPaint();
		tp.setFakeBoldText(true);
		TextPaint tp1 = tv_pay_tip1.getPaint();
		tp1.setFakeBoldText(true);
		tv_pay_orderid.setText("订单编号：" + number);
		if (1 == type) {// 成功
			title.setText("支付成功");
			tv_pay_tip1.setText("支付成功，谢谢惠顾！");
			String payName = "";
			int drawable = 1;
			if (1 == payType) {// 支付宝
				payName = "支付宝";
				iv_pay_image.setImageResource(R.drawable.pay_alipay);
			} else if (2 == payType) {// 银联
				payName = "银联";
				iv_pay_image.setImageResource(R.drawable.pay_up);
			} else if (3 == payType) {// 微信
				payName = "微信";
				iv_pay_image.setImageResource(R.drawable.pay_weixin);
			} else if (4 == payType) {// 服务卡
				payName = "便民服务卡";
				iv_pay_image.setImageResource(R.drawable.pay_service_card);
			}
			tv_pay_tip2.setText("您已成功使用" + payName + "支付完成交易");
		} 
	    // modify by zhyao @2015/5/8  修改支付失败判断
		else if(2 == type){
			title.setText("支付失败");
			tv_pay_tip1.setText("支付失败！");
			String payName = "";
			int drawable = 1;
			if (1 == payType) {// 支付宝
				payName = "支付宝";
				iv_pay_image.setImageResource(R.drawable.pay_alipay);
			} else if (2 == payType) {// 银联
				payName = "银联";
				iv_pay_image.setImageResource(R.drawable.pay_up);
			} else if (3 == payType) {// 微信
				payName = "微信";
				iv_pay_image.setImageResource(R.drawable.pay_weixin);
			} else if (4 == payType) {// 服务卡
				payName = "便民服务卡";
				iv_pay_image.setImageResource(R.drawable.pay_service_card);
			}
			pay_find_orderinfo.setBackgroundResource(R.drawable.btn_backpay);// 设置背景为重新支付
			tv_pay_tip2.setText("您使用" + payName + "支付交易失败");
		}
	    // add by zhyao @2015/5/8  添加WAP页面支付完成回调到查看结果页面
		else {
			title.setText("支付结果");
			tv_pay_tip1.setText("查看支付结果！");
			if(5 == payType) {//中行
				iv_pay_image.setImageResource(R.drawable.logo_china_bank);
			}
			if(6 == payType) {//建行
				iv_pay_image.setImageResource(R.drawable.logo_construction_bank);
			}
			tv_pay_tip2.setText("");
		}
		layout_pay_result.setVisibility(View.VISIBLE);
	}

	public void setListener() {
		pay_find_orderinfo.setOnClickListener(this);
		pay_continueshop.setOnClickListener(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

			if (BaseResp.ErrCode.ERR_OK == resp.errCode) {// 成功
				RequstClient.toRequestWebResult(3, number, "",
						new CustomResponseHandler(this, true) {
							@Override
							public void onFinish() {

								super.onFinish();
								WXPayEntryActivity.this.type = 1;
								initData();
							}

							@Override
							public void onSuccess(int statusCode, String content) {

								super.onSuccess(statusCode, content);
								// 调用pay方法进行支付
								try {
									JSONObject json = new JSONObject(content);
									String type = json.optString("type");// 返回类型
									String msg = json.optString("msg");// 返回信息
									// if ("1".equals(type)) {// 回调返回支付成功
									WXPayEntryActivity.this.type = 1;
									initData();
									// } else {
									// }
								} catch (JSONException e) {

									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(String error,
									String errorMessage) {

								super.onFailure(error, errorMessage);
								WXPayEntryActivity.this.type = 1;
								initData();
							}
						});
			} else {// 失败
				type = 2;
				initData();
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.pay_find_orderinfo:// 查看订单
		    // modify by zhyao @2015/5/8  添加WAP支付返回，查看订单详情
			if (1 == type || 3 == type) {// 成功、wap支付返回
				findOrderInfo();
			} else {// 失败
				backPay();// 返回支付页面
			}
			break;
		case R.id.pay_continueshop:// 继续购物
			continueShopping();
			break;
		}
	}

	/**
	 * 查看订单
	 */
	public void findOrderInfo() {
		Intent i = new Intent();
		i.setClass(WXPayEntryActivity.this, AllOrderDetailActivity.class);
		i.putExtra(AllOrderDetailActivity.ORDER_ID, order_id);
		startActivity(i);
		WXPayEntryActivity.this.finish();
	}

	/**
	 * 继续购物
	 */
	public void continueShopping() {
		
		AppContext.MAIN_TASK = AppContext.FIRST_PAGE;
		
		Intent i = new Intent();
		i.setClass(WXPayEntryActivity.this, MainActivity.class);
		startActivity(i);
		WXPayEntryActivity.this.finish();

	}

	private int count = 10;

	private void callBack() {
		count = 10;
		mHandler.sendEmptyMessageDelayed(HANDLER_TIME_FINISH, 1000);
	}

	/**
	 * 返回支付页面
	 */
	public void backPay() {
		Intent i = new Intent();
		i.setClass(WXPayEntryActivity.this, CommitOrderActivity.class);
		i.putExtra("number", number);
		i.putExtra("totalPrice", price + "");
		i.putExtra("serialNum", order_id);
		startActivity(i);
		WXPayEntryActivity.this.finish();
	}

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
					// 更新ui, count;
					tv_countdown.setText(count + "s后将自动返回订单详情");
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
	}
}