package com.huiyin.ui.home;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.PayParmas;
import com.huiyin.pay.PayConstants;
import com.huiyin.pay.alipay.AlipayUtil;
import com.huiyin.pay.webview.BankPayActivity;
import com.huiyin.pay.wxpay.PayUtil;
import com.huiyin.ui.servicecard.RechargeServiceCard;
import com.huiyin.utils.BaseHelper;
import com.huiyin.utils.DialogUtil;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.orhanobut.logger.Logger;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

/**
 * 支付fragment
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-14
 */
public class PayFragment extends BaseFragment implements View.OnClickListener {

	// 各个支付平台
	private LinearLayout payway_zfb_web;	//支付宝网页
	private LinearLayout payway_serviceCard;//便民服务卡
	private TextView serviceCard_info;		//便民服务卡信息
	private LinearLayout payway_zfb_android;//支付宝客户端
	private LinearLayout payway_wx;			//微信支付
	private LinearLayout payway_cft;		//理财通
	private LinearLayout payway_yl;			//银联
	private LinearLayout payway_china_bank;	//中国隐藏
	private LinearLayout payway_construction_bank;//建设隐藏

	// 提交参数
	private PayParmas payParms;				// 上一个界面传递过来的支付相关参数
	private float price;					// 本次订单支付的总金额
	private float perpaidCardFloat;			// 便民服务卡总金额
	private String TN;						// 银联支付TN流程号
	

	private String mTitle = "汇银乐虎";		// 标题
	private String detail = "品质才是硬道理";	// 详细
	private String mBody = "汇银家电";		// 商品
	
	
	private int curPayWay;					// 当前支付方式
	private String payStatusMsg;			// 当前支付返回结果消息

	
	/**支付回调**/
	private PayCallback payCallback;
	
	/**
	 * 设置支付回调
	 * @param payCallback
	 */
	public void setPayCallback(PayCallback payCallback){
		this.payCallback = payCallback;
	}
	
	/**
	 * 设置参数 
	 * @param payParams
	 */
	public void setPayParmas(PayParmas payParams){
		
		payStatusMsg = payParams.payStatuMsg;
		this.payParms = payParams;
		
		//初始化支付参数
		initPay();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.all_pay_layout, container, false);
		
		//绑定控件
		bindViews(layoutView);
		
		//绑定事件
		setListener();
		
		return layoutView;
	}


	/**
	 * 初始化支付参数
	 */
	private void initPay() {
		
		AppContext.number = payParms.number;
		AppContext.orderId = payParms.orderId;
		
		//订单总金额
		if (null != payParms.money && !"".equals(payParms.money)) {
			this.price = Float.parseFloat(payParms.money);
			AppContext.price = payParms.money;
		}
		
		//便民服务卡金额
		if (!StringUtils.isBlank(payParms.perpaidCard)) {
			perpaidCardFloat = Float.parseFloat(payParms.perpaidCard);
		}
		
		//便民服务卡精确2位小数点
		if (AppContext.mUserInfo != null && AppContext.mUserInfo.getBDStatus() == 1) {
			payway_serviceCard.setVisibility(View.VISIBLE);
			payway_serviceCard.setOnClickListener(this);
			if (perpaidCardFloat > 0 && perpaidCardFloat < price) {
				serviceCard_info.setText("使用服务卡支付价格为" + MathUtil.priceForAppWithSign(payParms.perpaidCard) + "元");
			}
		}
	}

	/**
	 * 设置按钮监听事件
	 */
	private void setListener() {
		payway_zfb_web.setOnClickListener(this);
		payway_serviceCard.setOnClickListener(this);
		payway_zfb_android.setOnClickListener(this);
		payway_wx.setOnClickListener(this);
		payway_cft.setOnClickListener(this);
		payway_yl.setOnClickListener(this);
		payway_china_bank.setOnClickListener(this);
		payway_construction_bank.setOnClickListener(this);
	}

	/**
	 * 绑定控件
	 * @param root
	 */
	private void bindViews(View root) {

		// 支付情况
		payway_zfb_web = (LinearLayout) root.findViewById(R.id.payway_zfb_web);
		payway_serviceCard = (LinearLayout) root.findViewById(R.id.payway_serviceCard);
		serviceCard_info = (TextView) root.findViewById(R.id.serviceCard_info);
		payway_zfb_android = (LinearLayout) root.findViewById(R.id.payway_zfb_android);
		payway_wx = (LinearLayout) root.findViewById(R.id.payway_wx);
		payway_cft = (LinearLayout) root.findViewById(R.id.payway_cft);
		payway_yl = (LinearLayout) root.findViewById(R.id.payway_yl);
		payway_china_bank = (LinearLayout) root.findViewById(R.id.payway_china_bank);
		payway_construction_bank = (LinearLayout) root.findViewById(R.id.payway_construction_bank);
	}

	@Override
	public void onClick(View v) {
		
		// 如果金额小于等于0,不进入支付
		if (price <= 0) {
			BaseHelper.showDialog(getActivity(), "提示", "支付金额错误，请确认！", R.drawable.infoicon);
			return;
		}
		
		switch (v.getId()) {
		case R.id.payway_zfb_web:// 支付宝网页支付

			break;
		case R.id.payway_zfb_android:
			
			// 支付宝客户端支付
			// pushToNextFragment("0000 0000 0000 11","321","");
			curPayWay = PayConstants.PAY_TYPE_ALIPAY;
			payWithZFBAndroid();
			
			break;
		case R.id.payway_serviceCard: 
			
			// 便民服务卡
			curPayWay = PayConstants.PAY_TYPE_SERVERCARD;
			AppContext.payType = curPayWay;
			
			if (AppContext.mUserInfo.hasBoundServiceCard()) {
				showPayDialog();
			} else {
				UIHelper.showToast("您还没绑定便民服务卡");
				// Intent mIntent=new Intent(mContext, BindServiceCard.class);
				// startActivity(mIntent);
			}
			break;
		case R.id.payway_wx:

			// 微信支付
			curPayWay = PayConstants.PAY_TYPE_WXPAY;
			payWithWX();
			break;
		case R.id.payway_cft:

			// 财付通

			break;
		case R.id.payway_yl:

			// 银联支付
			curPayWay = PayConstants.PAY_TYPE_YINLIAN;
			payWithYL();
			
			break;
		case R.id.payway_china_bank:

			// 中国银行
			curPayWay = PayConstants.PAY_TYPE_CHINA_BANK;
			Intent intent = new Intent(mContext, BankPayActivity.class);
			intent.putExtra(BankPayActivity.ORDER_ID, payParms.orderId);
			intent.putExtra(BankPayActivity.ORDER_NUMBER, payParms.number);
			intent.putExtra(BankPayActivity.ORDER_PRICE, String.valueOf(price));
			intent.putExtra(BankPayActivity.BANK_TYPE, BankPayActivity.CHINA_BANK);
			startActivityForResult(intent, BankPayActivity.REQUEST_CODE);
			
			break;
		case R.id.payway_construction_bank:

			// 建设银行
			curPayWay = PayConstants.PAY_TYPE_CONSTRUCTION_BANK;
			Intent intent1 = new Intent(mContext, BankPayActivity.class);
			intent1.putExtra(BankPayActivity.ORDER_ID, payParms.orderId);
			intent1.putExtra(BankPayActivity.ORDER_NUMBER, payParms.number);
			intent1.putExtra(BankPayActivity.ORDER_PRICE, String.valueOf(price));
			intent1.putExtra(BankPayActivity.BANK_TYPE, BankPayActivity.CONSTRUCTION_BANK);
			startActivityForResult(intent1, BankPayActivity.REQUEST_CODE);
			
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, final int resultCode, Intent data) {
		// add by zhyao @2015/5/8 添加WAP银行支付回调
		// 中行、建行支付返回
		if (requestCode == BankPayActivity.REQUEST_CODE && resultCode == BankPayActivity.RESULT_OK) {
			goToPayResult(1);
		}
		// 银联支付返回
		else {
			/*************************************************
			 * 步骤3：处理银联手机支付控件返回的支付结果
			 ************************************************/
			if (data == null) {
				return;
			}
			/*
			 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
			 */
			String str = data.getExtras().getString("pay_result");
			// mProgress.dismiss();
			if (str.equalsIgnoreCase("success")) {// 支付成功
				payStatusMsg = "支付成功！";
				requestBackToWeb(1, TN, resultCode + "");
			} else if (str.equalsIgnoreCase("fail")) {
				payStatusMsg = "支付失败！";
				goToPayResult(2);
			} else if (str.equalsIgnoreCase("cancel")) {
				payStatusMsg = "用户取消了支付";
				goToPayResult(2);
			}
		}
	}

	/**
	 * 微信支付
	 */
	private void payWithWX() {
		PayUtil wxapi = new PayUtil(mContext, payParms.number, mBody, String.valueOf((int) (price * 100)));
		wxapi.Pay();
	}

	/**
	 * 支付宝客户端支付
	 */
	private void payWithZFBAndroid() {
		startPay(payParms.number, price);
	}

	/**
	 * 支付宝开始支付
	 * 
	 * @param orderString
	 *            订单编号
	 * @param money
	 *            总价
	 */
	public void startPay(String orderString, float money) {
		AlipayUtil alipay = new AlipayUtil(mContext);
		alipay.setPayResultCallBack(new AlipayUtil.PayCallBack() {
			@Override
			public void payResultCallBack(int type, String resultStatus) {
				if (type == 0) {
					// 支付成功
					requestBackToWeb(2, "", resultStatus);
				} else {// 支付失败
					
					payStatusMsg = "用户取消了支付";
					goToPayResult(2);
				}
			}
		});

		alipay.alipayOrder(orderString, mTitle, detail, String.valueOf(money));

	}

	/**
	 * 调用服务端回调
	 * 
	 * @param type
	 *            类型
	 * @param tn
	 *            订单信息
	 * @param resultStatus
	 *            状态码
	 */
	public void requestBackToWeb(int type, String tn, final String resultStatus) {
		RequstClient.toRequestWebResult(type, payParms.number, tn, new CustomResponseHandler(getActivity(), true) {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				// 调用pay方法进行支付
				try {
					JSONObject json = new JSONObject(content);
					String type = json.optString("type");// 返回类型
					String msg = json.optString("msg");// 返回信息
					goToPayResult(1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);
				payStatusMsg = errorMessage;
				goToPayResult(2);
			}
		});
	}

	/**
	 * 跳转支付结果页面
	 * 
	 * @param type 1.支付成功 2.支付失败
	 */
	public void goToPayResult(int type) {
		
		payParms.payWay = curPayWay;
		payParms.payStatuMsg = payStatusMsg;
		
		
		if (type == PayConstants.SUCCESS) {
			
			//支付成功，通知进入成功界面
			payParms.payResult = PayConstants.SUCCESS;
			if(null != payCallback){
				payCallback.onPaySuccess(payParms);
			}
			
		} else {
			
			//支付失败，通知进入失败界面
			
			payParms.payResult = PayConstants.FAILED;
			
			if(null != payCallback){
				payCallback.onPayFailed(payParms);
			}
		}
	}

	/**
	 * 银联支付
	 */
	private void payWithYL() {
		RequstClient.payForUP(URLs.TN_URL, payParms.number, new CustomResponseHandler(getActivity(), true) {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (LogUtil.isDebug)
					Logger.json(content);
				TN = "";
				try {
					JSONObject json = new JSONObject(content);
					json.optInt("type");
					payStatusMsg = json.optString("msg");
					TN = json.optString("tn");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				UPPayAssistEx.startPayByJAR(getActivity(), PayActivity.class, null, null, TN, PayConstants.MODE);
			}
		});
	}

	/**
	 * 显示支付对话框
	 */
	private void showPayDialog() {
		View mView = LayoutInflater.from(mContext).inflate(R.layout.service_card_dialog, null);
		final Dialog mDialog = new Dialog(mContext, R.style.dialog);
		
		TextView tip = (TextView) mView.findViewById(R.id.com_tip_tv);
		tip.setText("确认支付？");
		final EditText pwd = (EditText) mView.findViewById(R.id.com_message_tv);
		Button yes = (Button) mView.findViewById(R.id.com_ok_btn);
		Button cancle = (Button) mView.findViewById(R.id.com_cancel_btn);
		yes.setText("确定支付");
		cancle.setText("取消支付");
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				String password = pwd.getText().toString();
				if (StringUtils.isBlank(password)) {
					Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}
				PayByCard(password);
			}
		});
		mDialog.setContentView(mView);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);
		mDialog.show();
	}

	/**
	 * 根据便民卡支付
	 * @param password
	 */
	private void PayByCard(String password) {
		UserInfo mUsrInfo = AppContext.getInstance().mUserInfo;
		CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				if (bean.type > 0) {
					goToPayResult(1);
				} else {
					showFailDialog(bean.msg);
					// goToPayResult(2);
				}
			}
		};
		String money;
		if (perpaidCardFloat > 0) {
			money = MathUtil.keep2decimal(perpaidCardFloat);
		} else {
			money = MathUtil.keep2decimal(price);
		}
		RequstClient.appCardRedeem(handler, mUsrInfo.userId, mUsrInfo.cardNum, password, money, payParms.number, mUsrInfo.token, payParms.orderId);
	}

	private void showFailDialog(String msg) {
		DialogUtil.showPayFailDialog(mContext, msg, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//便民服务卡充值
				Intent fuwu_intent = new Intent();
				fuwu_intent.setClass(mContext, RechargeServiceCard.class);
				mContext.startActivity(fuwu_intent);
			}
		});
	}

	/**
	 * 支付回调
	 * 
	 * @author 刘远祺
	 * 
	 * @todo TODO
	 * 
	 * @date 2015-7-8
	 */
	public interface PayCallback {

		/**
		 * 支付成功
		 */
		public void onPaySuccess(PayParmas payParmas);

		/**
		 * 支付失败
		 */
		public void onPayFailed(PayParmas payParmas);
	}
	
	
}
