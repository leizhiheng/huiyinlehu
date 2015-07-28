package com.huiyin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huiyin.R;
import com.huiyin.base.BaseFragment;
import com.huiyin.base.BaseLazyFragment;
import com.huiyin.bean.PayParmas;
import com.huiyin.ui.home.PayResultFragment.IOnCloseCallback;

/**
 * 话费-输入手机号码
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-8
 */
public class PhonePayOnLineStep3Fragment extends BaseFragment {

	/**支付结果**/
	public static final String PAYRESULT = "PayResult";
	
	/**支付结果**/
	private PayParmas payResult;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_phone_pay_step3, container, false);
	}

	/**
	 * 
	 * @param payResult
	 * @return
	 */
	public static PhonePayOnLineStep3Fragment getInstance(PayParmas payResult) {
		PhonePayOnLineStep3Fragment fragment = new PhonePayOnLineStep3Fragment();
		Bundle args = new Bundle();
		args.putSerializable(PAYRESULT, payResult);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			
			//获取支付结果
			payResult = (PayParmas)getArguments().getSerializable(PAYRESULT);
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//显示支付结果
		PayResultFragment payResultFragment = (PayResultFragment)mContext.getSupportFragmentManager().findFragmentById(R.id.fragment_pay_result);
		payResultFragment.setPayParmas(payResult);
		payResultFragment.setCloseCallback(new IOnCloseCallback() {
			
			@Override
			public void close() {
				
				//回到第一个页签
				((BaseLazyFragment)getParentFragment()).onFirstUserVisible();
			}
		});
	}
}
