package com.huiyin.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.PayParmas;
import com.huiyin.pay.PayConstants;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.ResourceUtils;

/**
 * 支付结果
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-8
 */
public class PayResultFragment extends BaseFragment implements View.OnClickListener {

	/**支付结果**/
	public static final String PAYRESULT = "PayResult";
	
	
	/**支付结果**/
	private TextView water_payment_succes_ok;
	
	/**缴费失败原因**/
	private TextView water_payment_succes_consumor;
	
	/**缴费金额**/
	private TextView water_payment_succes_money;
	
	/**支付方式图标**/
	private ImageView water_payment_succes_way;
	
	/**关闭页面**/
	private ImageView water_payment_succes_close;
	
	/**返回首页**/
	private ImageView water_payment_succes_tomain;
	
	
	
	/**支付结果**/
	private PayParmas payResult;
	

//	public PayResultFragment() {}

//	/**
//	 * 
//	 * @param payResult
//	 * @return
//	 */
//	public static PayResultFragment getInstance(PayParmas payResult) {
//		PayResultFragment fragment = new PayResultFragment();
//		Bundle args = new Bundle();
//		args.putSerializable(PAYRESULT, payResult);
//		fragment.setArguments(args);
//		return fragment;
//	}
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		if (getArguments() != null) {
//			
//			//获取支付结果
//			payResult = (PayParmas)getArguments().getSerializable(PAYRESULT);
//		}
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment_water_pay_succes_content, container, false);
		return layoutView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//初始化控件
		initView();
		
//		//显示支付结果
//		showResult();
	}

	/**
	 * 显示支付结果
	 */
	private void showResult(){
		
		//显示支付结果
		initPayIcon();
		
		if(payResult.payResult == PayConstants.SUCCESS){
			initPaySuccess();
		}else{
			initPayFail();
		}
	}
	
	/**
	 * 缴费成功
	 */
	private void initPaySuccess(){
		water_payment_succes_ok.setText("缴费成功");
        water_payment_succes_consumor.setText("已通过短信的方式发送到您的手机号码里，请注意查收！");
        water_payment_succes_money.setText(Html.fromHtml("缴费金额: " + ResourceUtils.changeStringColor("#FE0000", "¥" + payResult.money)));
	}
	
	/**
	 * 缴费失败
	 */
	private void initPayFail() {
		water_payment_succes_ok.setText("缴费失败");
		water_payment_succes_consumor.setText("失败原因:\n" + payResult.payStatuMsg);
	}
	
	private void initPayIcon(){
		int way = payResult.payWay;
		if (PayConstants.PAY_TYPE_ALIPAY == way) {
			
			// 支付宝
			water_payment_succes_way.setImageResource(R.drawable.pay_alipay);
		} else if (PayConstants.PAY_TYPE_YINLIAN == way) {
			
			// 银联
			water_payment_succes_way.setImageResource(R.drawable.pay_up);
		} else if (PayConstants.PAY_TYPE_WXPAY == way) {
			
			// 微信
			water_payment_succes_way.setImageResource(R.drawable.pay_weixin);
		} else if (PayConstants.PAY_TYPE_SERVERCARD == way) {
			
			// 服务卡
			water_payment_succes_way .setImageResource(R.drawable.pay_service_card);
		} else if (PayConstants.PAY_TYPE_CHINA_BANK == way) {
			
			// 中行
			water_payment_succes_way.setImageResource(R.drawable.logo_china_bank);
		} else if (PayConstants.PAY_TYPE_CONSTRUCTION_BANK == way) {
			
			// 建行
			water_payment_succes_way .setImageResource(R.drawable.logo_construction_bank);
		}
	}
	
	/**
	 * 初始化控件
	 */
	private void initView(){
		
		water_payment_succes_ok = (TextView)findViewById(R.id.water_payment_succes_ok);
		water_payment_succes_consumor = (TextView)findViewById(R.id.water_payment_succes_consumor);
		water_payment_succes_money = (TextView)findViewById(R.id.water_payment_succes_money);
		water_payment_succes_way = (ImageView)findViewById(R.id.water_payment_succes_way);
		water_payment_succes_close = (ImageView)findViewById(R.id.water_payment_succes_close);
		water_payment_succes_tomain = (ImageView)findViewById(R.id.water_payment_succes_tomain);
		
		water_payment_succes_close.setOnClickListener(this);
		water_payment_succes_tomain.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.water_payment_succes_close:
			
			//关闭页面 回到充值首页
//			Fragment fragment = getParentFragment();
//			fragment = getTargetFragment();
//			//((BaseLazyFragment)getParentFragment()).onFirstUserVisible();
//			if(mContext instanceof PhonePayActivity){
//				LogUtil.i("", "");
//			}
			if(null != closeCallback){
				closeCallback.close();
			}
			
			break;
		case R.id.water_payment_succes_tomain:
			
			//回到首页
			mContext.finish();
			
			break;
		}
	}

	/**
	 * 设置支付结果
	 * @param payResult
	 */
	public void setPayParmas(PayParmas payResult) {
		
		this.payResult = payResult;
		showResult();
	}
	
	/**关闭回调**/
	private IOnCloseCallback closeCallback;
	public void setCloseCallback(IOnCloseCallback callback){
		this.closeCallback = callback;
	}
	
	public interface IOnCloseCallback{
		public void close();
	}
}
