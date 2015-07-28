package com.huiyin.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.PhonePayResult;
import com.huiyin.utils.DateUtil;
import com.huiyin.utils.StringUtils;

/**
 * 话费-输入手机号码
 * 
 * @author 刘远祺
 * 
 * @todo TODO
 * 
 * @date 2015-7-8
 */
public class PhonePayOnLineStep1Fragment extends BaseFragment implements OnClickListener, TextWatcher {

	/**手机号码**/
	private EditText mPhone_number_edittext;
	
	/**10块**/
	private ImageView mPay_money_10_iv;
	
	/**20块**/
	private ImageView mPay_money_20_iv;
	
	/**30块**/
	private ImageView mPay_money_30_iv;
	
	/**50块**/
	private ImageView mPay_money_50_iv;
	
	/**100块**/
	private ImageView mPay_money_100_iv;
	
	/**200块**/
	private ImageView mPay_money_200_iv;
	
	/**其他金额**/
	private EditText mPay_money_other_et;
	
	/**下一步**/
	private Button mPayment_next;

	
	/**当前选中的钱**/
	private ImageView currentMoney;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment_phone_pay_step1, container, false);
		return layoutView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//绑定控件
		bindViews();
	}
	
	/**
	 * 绑定控件
	 */
	private void bindViews() {

		//找控件
		mPhone_number_edittext = (EditText) findViewById(R.id.phone_number_edittext);
		mPay_money_10_iv = (ImageView) findViewById(R.id.pay_money_10_iv);
		mPay_money_20_iv = (ImageView) findViewById(R.id.pay_money_20_iv);
		mPay_money_30_iv = (ImageView) findViewById(R.id.pay_money_30_iv);
		mPay_money_50_iv = (ImageView) findViewById(R.id.pay_money_50_iv);
		mPay_money_100_iv = (ImageView) findViewById(R.id.pay_money_100_iv);
		mPay_money_200_iv = (ImageView) findViewById(R.id.pay_money_200_iv);
		mPay_money_other_et = (EditText) findViewById(R.id.pay_money_other_et);
		mPayment_next = (Button) findViewById(R.id.water_payment_next);
		
		//设定事件
		mPay_money_10_iv.setOnClickListener(this);
		mPay_money_20_iv.setOnClickListener(this);
		mPay_money_30_iv.setOnClickListener(this);
		mPay_money_50_iv.setOnClickListener(this);
		mPay_money_100_iv.setOnClickListener(this);
		mPay_money_200_iv.setOnClickListener(this);
		mPayment_next.setOnClickListener(this);
		mPay_money_other_et.addTextChangedListener(this);
		
		//绑数据
		mPay_money_10_iv.setTag(new ViewTag(R.drawable.phone_pay_10_in, R.drawable.phone_pay_10_on, 10));
		mPay_money_20_iv.setTag(new ViewTag(R.drawable.phone_pay_20_in, R.drawable.phone_pay_20_on, 20));
		mPay_money_30_iv.setTag(new ViewTag(R.drawable.phone_pay_30_in, R.drawable.phone_pay_30_on, 30));
		mPay_money_50_iv.setTag(new ViewTag(R.drawable.phone_pay_50_in, R.drawable.phone_pay_50_on, 50));
		mPay_money_100_iv.setTag(new ViewTag(R.drawable.phone_pay_100_in, R.drawable.phone_pay_100_on, 100));
		mPay_money_200_iv.setTag(new ViewTag(R.drawable.phone_pay_200_in, R.drawable.phone_pay_200_on, 200));
		
		//默认选中10块的按钮
		currentMoney = mPay_money_10_iv;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.water_payment_next:
			
			//下一步
			if(!validateInput()){
				return;
			}
			
			//手机，话费
			String phone = getText(mPhone_number_edittext);
			String money = getInputMoney();
			
			//查询话费
			queryBill(phone, money);
			
			break;

		default:
			
			//金额
			checkButton(currentMoney, false);
			currentMoney = (ImageView)v;
			checkButton(currentMoney, true);
			
			//记住输入的钱
			String otherMoney = mPay_money_other_et.getText().toString().trim();
			mPay_money_other_et.setTag(otherMoney);
			mPay_money_other_et.setText("");
			
			break;
		}
	}
	
	
	/**
	 * 查询话费话费情况（目的是为了从服务器获取订单相关信息，可以进行支付流程）
	 * @param mobile
	 * @param money
	 */
	private void queryBill(final String mobile, final String money) {
		
		if(!appIsLogin(true)){
			return;
		}
		
		if (AppContext.userId != null) {

			String userId = AppContext.userId;
			String payItem = "3";
			String areaCode = "30000000";
			String areaName = "";
			
			String date = DateUtil.getStringDateShort();
			String[] dates = date.split("-");
			String year = dates[0];
			String month = dates[1];
			String phone = mobile;
			String amount = money;
			
			//获取支付订单信息
			PhonePayResult result = new PhonePayResult();
			
			//跳转到支付界面
			PhonePayOnlineFragment parentFragment = (PhonePayOnlineFragment)getParentFragment();
			PhonePayOnLineStep2Fragment fragmentStep2 = PhonePayOnLineStep2Fragment.getInstance(mobile, Integer.parseInt(money), result);
			parentFragment.changeFragment(fragmentStep2);
			
			
//			RequstClient.shareBill(userId, payItem, areaCode, areaName, "", "", "", year, month, phone, amount, new CustomResponseHandler(mContext) {
//				@Override
//				public void onSuccess(String content) {
//					super.onSuccess(content);
//					if(isSuccessResponse(content)){
//						
//						//获取支付订单信息
//						PhonePayResult result = new PhonePayResult(content);
//						
//						//跳转到支付界面
//						PhonePayOnlineFragment parentFragment = (PhonePayOnlineFragment)getParentFragment();
//						PhonePayOnLineStep2Fragment fragmentStep2 = PhonePayOnLineStep2Fragment.getInstance(mobile, Integer.parseInt(money), result);
//						parentFragment.changeFragment(fragmentStep2);
//					}
//				}
//			});
		}
	}

	
	
	/**
	 * 验证输入
	 * @return
	 */
	private boolean validateInput(){
		
		//手机号码不能为空
		String phone = getText(mPhone_number_edittext);
		if(TextUtils.isEmpty(phone)){
			showMyToast("请输入充值号码");
			return false;
		}
		
		//手机号码合法性验证
		if(!StringUtils.isPhoneNumber(phone)){
			showMyToast("请输入正确的手机号码");
		}
		
		//选择或输入了充值金额
		String money = getInputMoney();
		if(TextUtils.isEmpty(money)){
			showMyToast("请选择或输入充值金额");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 获的充值金额
	 * @return
	 */
	private String getInputMoney(){
		
		//其他金额-输入的钱
		String inputMoney = mPay_money_other_et.getText().toString().trim();
		if(!TextUtils.isEmpty(inputMoney)){
			return inputMoney;
		}
		
		//按钮上的钱
		ViewTag tag = (ViewTag)currentMoney.getTag();
		return tag.money+"";
	}
	
	/**
	 * 选中钱
	 * @param moneyBtn
	 */
	private void checkButton(ImageView moneyBtn, boolean checked){
		ViewTag tag = (ViewTag)moneyBtn.getTag();
		if(checked){
			moneyBtn.setImageResource(tag.iconOn);
		}else{
			moneyBtn.setImageResource(tag.iconIn);
		}
	}
	
	
	class ViewTag{
		
		//点击的两个图片
		int iconIn, iconOn;
		
		//面值
		int money;
		
		ViewTag(int iconIn, int iconOn, int money){
			this.iconIn = iconIn;
			this.iconOn = iconOn;
			this.money = money;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

	@Override
	public void afterTextChanged(Editable s) {
		String money = s.toString().trim();
		if(!TextUtils.isEmpty(money)){
			
			//不为空，则不选中钱
			checkButton(currentMoney, false);
		}else{
			
			//为空，则选中上一次点击的那个钱
			checkButton(currentMoney, true);
		}
	}
}
