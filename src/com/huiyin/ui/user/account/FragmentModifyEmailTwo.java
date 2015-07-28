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

/**
 * 设定邮箱
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-6
 */
public class FragmentModifyEmailTwo extends BaseFragment implements OnClickListener{

	/**邮箱输入框**/
	private EditText emailEditText;
	
	/**下一步**/
	private TextView nextStep;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_modify_email_step_2, null);

		nextStep = (TextView) view.findViewById(R.id.next_step);
		emailEditText = (EditText) view.findViewById(R.id.et_bind_email);
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
	
	/**
	 * 发送邮箱
	 */
	public void sendCheckEmail() {
		
		//获取邮箱文本
		final String bindEmail = getText(emailEditText);
		
		RequstClient.sendCheckEmail(AppContext.userId, AppContext.mUserInfo.userName, bindEmail, "0", "1", new CustomResponseHandler(getActivity()) {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if (!isSuccessResponse(content)) {
					
					String msg = JSONParseUtils.getString(content, "msg");
					showMyToast(msg);
					return;
					
				} else {
					
					String emailUrl = JSONParseUtils.getString(content, "url");
					
					//邮箱发送成功
					if(mListener != null){
						mListener.stepToModifyFinish(bindEmail, emailUrl);
					}
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(validate()){
			
			//发送邮箱
			sendCheckEmail();
		}
	}
	
	
	/**
	 * 绑定邮箱成功回调
	 */
	private OnStepToModifyFinishListener mListener;

	interface OnStepToModifyFinishListener {
		void stepToModifyFinish(String newEmail, String emailUrl);
	}

	public void setOnStepToModifyFinishListener(OnStepToModifyFinishListener listener) {
		mListener = listener;
	}
}


