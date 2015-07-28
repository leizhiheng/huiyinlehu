package com.huiyin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.PhonePayResult;

/**
 * 确认并付费-确认
 * 
 * @author 刘远祺
 * 
 * @todo TODO
 * 
 * @date 2015-7-8
 */
public class PhonePayOnLineStep2Fragment extends BaseFragment implements OnClickListener {

	/**电话号码**/
	private EditText mPhone_number_edittext;
	
	/**号码归属地**/
	private TextView mPhone_address_edittext;
	
	/**支付金额**/
	private EditText mPhone_money_edittext;
	
	/**确认缴费**/
	private Button mWater_payment_next;
	
	/**返回上一步**/
	private Button mPhone_pay_back;

	
	//来自上一个fragment传递过来的数据
	/**充值号码**/
	private String phoneNumber;
	
	/**充值金额**/
	private int money;
	
	/**订单信息**/
	private PhonePayResult orderResult;
	
	public static PhonePayOnLineStep2Fragment getInstance(String phone, int money, PhonePayResult result){
		PhonePayOnLineStep2Fragment fragment = new PhonePayOnLineStep2Fragment();
		Bundle bundle = new Bundle();
		bundle.putString("phone", phone);
		bundle.putInt("money", money);
		bundle.putSerializable("result", result);
		fragment.setArguments(bundle);
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
		layoutView = inflater.inflate(R.layout.fragment_phone_pay_step2, container, false);
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
		
		//find view
		mPhone_number_edittext = (EditText) findViewById(R.id.phone_number_edittext);
		mPhone_address_edittext = (TextView) findViewById(R.id.phone_address_edittext);
		mPhone_money_edittext = (EditText) findViewById(R.id.phone_money_edittext);
		mWater_payment_next = (Button) findViewById(R.id.water_payment_next);
		mPhone_pay_back = (Button) findViewById(R.id.phone_pay_back);
		
		//点击事件
		mWater_payment_next.setOnClickListener(this);
		mPhone_pay_back.setOnClickListener(this);
		
		//电话，充值金额赋值
		mPhone_number_edittext.setText(phoneNumber);
		mPhone_money_edittext.setText(String.valueOf(money));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.water_payment_next:
			
			//确认缴费
			PhonePayOnlineFragment parentFragment = (PhonePayOnlineFragment)getParentFragment();
			PhonePayOnLineStep2PayFragment fragmentStep2Pay = PhonePayOnLineStep2PayFragment.getInstance(phoneNumber, money, orderResult);
			parentFragment.changeFragment(fragmentStep2Pay);
			
			break;

		case R.id.phone_pay_back:
			
			//返回上一步
			PhonePayOnlineFragment fragment = (PhonePayOnlineFragment)getParentFragment();
			fragment.backFragment();
			
			break;
		}
	}
}
