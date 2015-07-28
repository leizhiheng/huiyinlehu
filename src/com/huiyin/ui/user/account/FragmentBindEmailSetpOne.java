package com.huiyin.ui.user.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.StringUtils;

public class FragmentBindEmailSetpOne extends BaseFragment implements OnClickListener{

	private static final String TAG = "FragmentBindEmailSetpOne";
	private OnStepToCheckEmailListener mListener;

	/**邮箱**/
	private EditText emailEditText;
	
	/**下一步**/
	private TextView nextStep;
	
	
	/**未激活的Email**/
	private String unActiveEmail;
	
	interface OnStepToCheckEmailListener {
		void stepToCheckEmail(String email, String emailUrl);
	}

	/**
	 * 设置未激活的邮箱
	 * @param email
	 */
	public void setUnActiveEmail(String email){
		this.unActiveEmail = email;
	}
	
	public void setOnStepToCheckEmailListener(OnStepToCheckEmailListener listener) {
		mListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bind_email_step_1, null);
		
		emailEditText = (EditText) view.findViewById(R.id.et_bind_email);
		
		//将未激活的邮箱自动赋值
		if(null != unActiveEmail){
			emailEditText.setText(unActiveEmail);
		}
		
		nextStep = (TextView) view.findViewById(R.id.next_step);
		nextStep.setOnClickListener(this);
		return view;
	}
	
	/**
	 * 输入邮箱验证
	 */
	private boolean validate(){
		
		String email = getText(emailEditText);
		
		//非空验证
		if(TextUtils.isEmpty(email)){
			showMyToast("请输入邮箱");
			return false;
		}
		
		//邮箱合法性验证
		if(!StringUtils.isEmail(email)){
			showMyToast("请输入正确的邮箱");
			return false;
		}
		
		//判断输入的邮箱是否为自己现在绑定的邮箱
		UserInfo userInfo = AppContext.mUserInfo;
		if(userInfo.isBoundEmail() && userInfo.email.equals(email)){
			showMyToast("您已经绑定了该邮箱");
			return false;
		}
		
		return true;
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.next_step:

			//验证邮箱
			if(validate()){
				
				//发送邮箱
				String bindEmail = getText(emailEditText);
				sendCheckEmail(bindEmail);
			}
			break;
		}
	}
	
	/**
	 * 发送邮箱
	 */
	public void sendCheckEmail(final String bindEmail) {
		RequstClient.sendCheckEmail(AppContext.userId, AppContext.mUserInfo.userName, bindEmail, "0", "1", new CustomResponseHandler(getActivity()) {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if (isSuccessResponse(content)) {

					String emailUrl = JSONParseUtils.getString(content, "url");
					
					// 邮件发送成功
					if (mListener != null) {
						mListener.stepToCheckEmail(bindEmail, emailUrl);
					}
				}
			}
		});
	}
}

