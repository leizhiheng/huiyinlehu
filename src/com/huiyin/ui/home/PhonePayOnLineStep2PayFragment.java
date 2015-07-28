package com.huiyin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huiyin.R;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.PayParmas;
import com.huiyin.bean.PhonePayResult;
import com.huiyin.ui.home.PayFragment.PayCallback;

/**
 * 确认并付费-付费(选择付费平台付费)
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-8
 */
public class PhonePayOnLineStep2PayFragment extends BaseFragment {

	/**支付fragment**/
	private PayFragment payFragment;
	
	/**充值号码**/
	private String phoneNumber;
	
	/**充值金额**/
	private int money;
	
	/**订单信息**/
	private PhonePayResult orderResult;
	
	public static PhonePayOnLineStep2PayFragment getInstance(String phone, int money, PhonePayResult result){
		PhonePayOnLineStep2PayFragment fragment = new PhonePayOnLineStep2PayFragment();
		Bundle data = new Bundle();
		data.putString("phone", phone);
		data.putInt("money", money);
		data.putSerializable("result", result);
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		if(null != data){
			phoneNumber = data.getString("phone");
			money = data.getInt("money");
			orderResult = (PhonePayResult)data.getSerializable("result");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_phone_pay_step2_pay, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		
		//初始化支付参数
		PayParmas payParams = new PayParmas();
		payParams.status = orderResult.respCode;
		payParams.number = orderResult.orderCode;
		payParams.orderId = orderResult.orderId;
		payParams.perpaidCard = orderResult.perpaidCard;
		payParams.payStatuMsg = orderResult.msg;
		payParams.money = String.valueOf(money);
		payParams.userNo = phoneNumber;
		
		//获得支付fragment
		payFragment = (PayFragment)mContext.getSupportFragmentManager().findFragmentById(R.id.fragment_pay);
		payFragment.setPayParmas(payParams);
		
		//设置PayFragment支付回调
		payFragment.setPayCallback(new PayCallback() {
			
			@Override
			public void onPaySuccess(PayParmas payResult) {
				
				//支付成功回调
				PhonePayOnlineFragment parentFragment = (PhonePayOnlineFragment)getParentFragment();
				PhonePayOnLineStep3Fragment resultFragment = PhonePayOnLineStep3Fragment.getInstance(payResult);
				parentFragment.changeFragment(resultFragment);
			
			}
			
			@Override
			public void onPayFailed(PayParmas payResult) {
				
				//支付失败回调
				PhonePayOnlineFragment parentFragment = (PhonePayOnlineFragment)getParentFragment();
				PhonePayOnLineStep3Fragment resultFragment = PhonePayOnLineStep3Fragment.getInstance(payResult);
				parentFragment.changeFragment(resultFragment);
			
			}
		});
		
	}
}
