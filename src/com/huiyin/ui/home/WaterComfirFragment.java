package com.huiyin.ui.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.huiyin.bean.BaseBean;
import com.huiyin.pay.alipay.AlipayUtil;
import com.huiyin.pay.webview.BankPayActivity;
import com.huiyin.pay.wxpay.PayUtil;
import com.huiyin.ui.servicecard.RechargeServiceCard;
import com.huiyin.utils.BaseHelper;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.orhanobut.logger.Logger;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class WaterComfirFragment extends Fragment implements
		View.OnClickListener {
	private static final String ARG_PRICE = "money";
	private static final String ARG_STATUS = "status";
	private static final String ARG_NUM = "number"; // 订单编号
	private static final String ARG_ORDERID = "serialNum"; // 订单ID
	private static final String ARG_TOTAL = "totalPrice"; // 总价
	private static final String ARG_PREPAIDCARD = "perpaidCard";
	private static final String ARG_MSG = "msg"; // 错误信息

	private String money;
	private String status;

	private LinearLayout water_payment_comfir_layout;
	private TextView water_payment_money;
	private TextView water_payment_status;
	private Button water_payment_comfir;
	private Button water_payment_back;
	private View water_payment_payway_layout;
	private LinearLayout payway_zfb_web;
	private LinearLayout payway_serviceCard;
	private TextView serviceCard_info;
	private LinearLayout payway_zfb_android;
	private LinearLayout payway_wx;
	private LinearLayout payway_cft;
	private LinearLayout payway_yl;
	private LinearLayout payway_china_bank;
	private LinearLayout payway_construction_bank;
	// 提交参数
	private String number;
	private String orderId;
	private String perpaidCard;
	private float price;
	private float perpaidCardFloat;
	private Context mContext;
	private String TN;
	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private static final String MODE = "00";

	private String mTitle = "汇银乐虎";// 标题
	private String detail = "品质才是硬道理";// 详细
	private String mBody = "汇银家电";// 商品
	private Dialog mDialog;
	private int way;
	private String msg;
	private View water_payment_payway_fail;
	private TextView water_payment_succes_ok, water_payment_succes_consumor;
	private TextView water_payment_succes_money;
	private ImageView water_payment_succes_way;
	private ImageView water_payment_succes_close;
	private ImageView water_payment_succes_tomain;

	/**
	 * 实例方法
	 * 
	 * @param money
	 *            应缴费用
	 * @param status
	 *            返回的缴费状态码
	 * @param number
	 *            缴费订单编号
	 * @param orderId
	 *            缴费订单ID
	 * @param perpaidCard
	 *            便民卡预付金
	 */
	public static WaterComfirFragment newInstance(String money, String status,
			String number, String orderId, String perpaidCard, String msg) {
		WaterComfirFragment fragment = new WaterComfirFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PRICE, money);
		args.putString(ARG_STATUS, status);
		args.putString(ARG_NUM, number);
		args.putString(ARG_ORDERID, orderId);
		args.putString(ARG_PREPAIDCARD, perpaidCard);
		args.putString(ARG_MSG, msg);
		fragment.setArguments(args);
		return fragment;
	}

	public WaterComfirFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			money = getArguments().getString(ARG_PRICE);
			status = getArguments().getString(ARG_STATUS);
			number = getArguments().getString(ARG_NUM);
			orderId = getArguments().getString(ARG_ORDERID);
			perpaidCard = getArguments().getString(ARG_PREPAIDCARD);
			msg = getArguments().getString(ARG_MSG);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View Root = inflater.inflate(R.layout.fragment_water_pay_comfir,
				container, false);
		bindViews(Root);
		setListener();
		init();
		return Root;
	}

	private void init() {
		mContext = getActivity();
		water_payment_money.setText("￥" + money);
		water_payment_status.setText(status);
		if (status.equals("00")) {
			water_payment_money.setText("￥" + money);
			water_payment_status.setText("已欠费");
		} else {
			water_payment_money.setText("");
			water_payment_status.setText(msg);
			water_payment_comfir
					.setBackgroundResource(R.drawable.gray7_cornor_bg);
			water_payment_comfir.setEnabled(false);
		}
	}

	private void initPay() {
		water_payment_comfir_layout.setVisibility(View.GONE);
		water_payment_payway_layout.setVisibility(View.VISIBLE);
		water_payment_payway_fail.setVisibility(View.GONE);
		AppContext.number = number;
		System.out.println("订单id----" + orderId);
		AppContext.orderId = orderId;
		if (null != money && !"".equals(money)) {
			this.price = Float.parseFloat(money);
			AppContext.price = money;
		}
		if (!StringUtils.isBlank(perpaidCard)) {
			perpaidCardFloat = Float.parseFloat(perpaidCard);
		}
		if (AppContext.mUserInfo != null
				&& AppContext.mUserInfo.getBDStatus() == 1) {
			payway_serviceCard.setVisibility(View.VISIBLE);
			payway_serviceCard.setOnClickListener(this);
			if (perpaidCardFloat > 0 && perpaidCardFloat < price) {
				serviceCard_info.setText("使用服务卡支付价格为"
						+ MathUtil.priceForAppWithSign(perpaidCard) + "元");
			}
		}
	}

	private void initPayFail() {
		water_payment_comfir_layout.setVisibility(View.GONE);
		water_payment_payway_layout.setVisibility(View.GONE);
		water_payment_payway_fail.setVisibility(View.VISIBLE);
		water_payment_succes_ok.setText("缴费失败");
		water_payment_succes_consumor.setText("失败原因:" + msg);
		if (1 == way) {// 支付宝
			water_payment_succes_way.setImageResource(R.drawable.pay_alipay);
		} else if (2 == way) {// 银联
			water_payment_succes_way.setImageResource(R.drawable.pay_up);
		} else if (3 == way) {// 微信
			water_payment_succes_way.setImageResource(R.drawable.pay_weixin);
		} else if (4 == way) {// 服务卡
			water_payment_succes_way
					.setImageResource(R.drawable.pay_service_card);
		} else if (5 == way) {// 中行
			water_payment_succes_way
					.setImageResource(R.drawable.logo_china_bank);
		} else if (6 == way) {// 建行
			water_payment_succes_way
					.setImageResource(R.drawable.logo_construction_bank);
		}
		water_payment_succes_close.setOnClickListener(this);
		water_payment_succes_tomain.setOnClickListener(this);
	}

	private void setListener() {
		water_payment_comfir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				water_payment_comfir_layout.setVisibility(View.GONE);
				water_payment_payway_layout.setVisibility(View.VISIBLE);
				initPay();
			}
		});
		water_payment_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				backFragment();
			}
		});
		payway_zfb_web.setOnClickListener(this);
		payway_serviceCard.setOnClickListener(this);
		payway_zfb_android.setOnClickListener(this);
		payway_wx.setOnClickListener(this);
		payway_cft.setOnClickListener(this);
		payway_yl.setOnClickListener(this);
		payway_china_bank.setOnClickListener(this);
		payway_construction_bank.setOnClickListener(this);
	}

	private void bindViews(View root) {
		water_payment_comfir_layout = (LinearLayout) root
				.findViewById(R.id.water_payment_comfir_layout);
		water_payment_money = (TextView) root
				.findViewById(R.id.water_payment_money);
		water_payment_status = (TextView) root
				.findViewById(R.id.water_payment_status);
		water_payment_comfir = (Button) root
				.findViewById(R.id.water_payment_comfir);
		water_payment_back = (Button) root
				.findViewById(R.id.water_payment_back);
		water_payment_payway_layout = root
				.findViewById(R.id.water_payment_payway_layout);
		// 支付情况
		payway_zfb_web = (LinearLayout) root.findViewById(R.id.payway_zfb_web);
		payway_serviceCard = (LinearLayout) root
				.findViewById(R.id.payway_serviceCard);
		serviceCard_info = (TextView) root.findViewById(R.id.serviceCard_info);
		payway_zfb_android = (LinearLayout) root
				.findViewById(R.id.payway_zfb_android);
		payway_wx = (LinearLayout) root.findViewById(R.id.payway_wx);
		payway_cft = (LinearLayout) root.findViewById(R.id.payway_cft);
		payway_yl = (LinearLayout) root.findViewById(R.id.payway_yl);
		payway_china_bank = (LinearLayout) root
				.findViewById(R.id.payway_china_bank);
		payway_construction_bank = (LinearLayout) root
				.findViewById(R.id.payway_construction_bank);

		// 支付失败
		water_payment_payway_fail = root
				.findViewById(R.id.water_payment_payway_fail);
		water_payment_succes_consumor = (TextView) root
				.findViewById(R.id.water_payment_succes_consumor);
		water_payment_succes_ok = (TextView) root
				.findViewById(R.id.water_payment_succes_ok);
		water_payment_succes_money = (TextView) root
				.findViewById(R.id.water_payment_succes_money);
		water_payment_succes_way = (ImageView) root
				.findViewById(R.id.water_payment_succes_way);
		water_payment_succes_close = (ImageView) root
				.findViewById(R.id.water_payment_succes_close);
		water_payment_succes_tomain = (ImageView) root
				.findViewById(R.id.water_payment_succes_tomain);
	}

	private void backFragment() {
		((PayProcessFragment) getParentFragment()).backFragment();
	}

	@Override
	public void onClick(View v) {
		if (0 >= price) {// 如果金额小于等于0,不进入支付
			BaseHelper.showDialog(getActivity(), "提示", "支付金额错误，请确认！",
					R.drawable.infoicon);
			return;
		}
		switch (v.getId()) {
		case R.id.payway_zfb_web:// 支付宝网页支付

			break;
		case R.id.payway_zfb_android:// 支付宝客户端支付
			// pushToNextFragment("0000 0000 0000 11","321","");
			way = 1;
			payWithZFBAndroid();
			break;
		case R.id.payway_serviceCard: // 便民服务卡
			AppContext.payType = 4;
			way = 4;
			if (AppContext.mUserInfo.hasBoundServiceCard()) {
				showPayDialog();
			} else {
				UIHelper.showToast("您还没绑定便民服务卡");
				// Intent mIntent=new Intent(mContext, BindServiceCard.class);
				// startActivity(mIntent);
			}
			break;
		case R.id.payway_wx:// 微信支付
			way = 3;
			payWithWX();
			break;
		case R.id.payway_cft: // 财付通
			break;
		case R.id.payway_yl:// 银联支付
			way = 2;
			payWithYL();
			break;
		case R.id.payway_china_bank:
			way = 5;
			Intent intent = new Intent(mContext, BankPayActivity.class);
			intent.putExtra(BankPayActivity.ORDER_ID, orderId);
			intent.putExtra(BankPayActivity.ORDER_NUMBER, number);
			intent.putExtra(BankPayActivity.ORDER_PRICE, String.valueOf(price));
			intent.putExtra(BankPayActivity.BANK_TYPE,
					BankPayActivity.CHINA_BANK);
			startActivityForResult(intent, BankPayActivity.REQUEST_CODE);
			break;
		case R.id.payway_construction_bank:
			way = 6;
			Intent intent1 = new Intent(mContext, BankPayActivity.class);
			intent1.putExtra(BankPayActivity.ORDER_ID, orderId);
			intent1.putExtra(BankPayActivity.ORDER_NUMBER, number);
			intent1.putExtra(BankPayActivity.ORDER_PRICE, String.valueOf(price));
			intent1.putExtra(BankPayActivity.BANK_TYPE,
					BankPayActivity.CONSTRUCTION_BANK);
			startActivityForResult(intent1, BankPayActivity.REQUEST_CODE);
			break;
		case R.id.water_payment_succes_close:
			((PayProcessFragment) getParentFragment()).onFirstUserVisible();
			((WaterCoalElectricActivity) getActivity()).changeViewPager(1);
			break;
		case R.id.water_payment_succes_tomain:
			getActivity().finish();
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, final int resultCode,
			Intent data) {
		// add by zhyao @2015/5/8 添加WAP银行支付回调
		// 中行、建行支付返回
		if (requestCode == BankPayActivity.REQUEST_CODE
				&& resultCode == BankPayActivity.RESULT_OK) {
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
				msg = "支付成功！";
				requestBackToWeb(1, TN, resultCode + "");
			} else if (str.equalsIgnoreCase("fail")) {
				msg = "支付失败！";
				goToPayResult(2);
			} else if (str.equalsIgnoreCase("cancel")) {
				msg = "用户取消了支付";
				goToPayResult(2);
			}
		}
	}

	private void payWithWX() {
		PayUtil wxapi = new PayUtil(mContext, number, mBody,
				String.valueOf((int) (price * 100)));
		wxapi.Pay();
	}

	private void payWithZFBAndroid() {
		startPay(number, price);
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
				if (type == 0) {// 支付成功
					requestBackToWeb(2, "", resultStatus);
				} else {// 支付失败
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
		RequstClient.toRequestWebResult(type, number, tn,
				new CustomResponseHandler(getActivity(), true) {

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
						msg = errorMessage;
						goToPayResult(2);
					}
				});
	}

	/**
	 * 跳转支付结果页面
	 *
	 * @param type
	 *            1.支付成功 2.支付失败
	 */
	public void goToPayResult(int type) {
		if (type == 1) {
			pushToNextFragment(type + "", AppContext.userNum, money, way, msg);
		} else if (type == 2) {
			initPayFail();
		}
	}

	private void pushToNextFragment(String status, String userNo, String price,
			int way, String msg) {
		((PayProcessFragment) getParentFragment())
				.changeFragment(WaterPaySuccesFragment.newInstance(status,
						userNo, price, way, msg));
	}

	private void payWithYL() {
		RequstClient.payForUP(URLs.TN_URL, number, new CustomResponseHandler(
				getActivity(), true) {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if (LogUtil.isDebug)
					Logger.json(content);
				TN = "";
				try {
					JSONObject json = new JSONObject(content);
					json.optInt("type");
					msg = json.optString("msg");
					TN = json.optString("tn");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				System.out.println(TN);
				System.out.println(MODE);

				UPPayAssistEx.startPayByJAR(getActivity(), PayActivity.class,
						null, null, TN, MODE);
			}
		});
	}

	private void showPayDialog() {
		View mView = LayoutInflater.from(mContext).inflate(
				R.layout.service_card_dialog, null);
		mDialog = new Dialog(mContext, R.style.dialog);
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
					Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT)
							.show();
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

	private void PayByCard(String password) {
		UserInfo mUsrInfo = AppContext.getInstance().mUserInfo;
		CustomResponseHandler handler = new CustomResponseHandler(
				getActivity(), true) {
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
		RequstClient.appCardRedeem(handler, mUsrInfo.userId, mUsrInfo.cardNum,
				password, money, number, mUsrInfo.token, orderId);
	}

	private void showFailDialog(String msg) {
		View mView = LayoutInflater.from(mContext).inflate(
				R.layout.service_card_pay_fail_dialog, null);
		mDialog = new Dialog(mContext, R.style.dialog);
		TextView pwd = (TextView) mView.findViewById(R.id.com_message_tv);
		pwd.setText(msg);
		Button yes = (Button) mView.findViewById(R.id.com_ok_btn);
		Button cancle = (Button) mView.findViewById(R.id.com_cancel_btn);
		cancle.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				Intent fuwu_intent = new Intent();
				fuwu_intent.setClass(mContext, RechargeServiceCard.class);
				mContext.startActivity(fuwu_intent);
			}
		});
		yes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
			}
		});
		mDialog.setContentView(mView);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);
		mDialog.show();
	}
}
