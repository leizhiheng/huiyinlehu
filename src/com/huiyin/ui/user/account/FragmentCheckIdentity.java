package com.huiyin.ui.user.account;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.ui.servicecard.ApplyServiceCard;
import com.huiyin.ui.user.BackPswActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;
import com.unionpay.mobile.android.nocard.views.r;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 身份验证布局
 * @author leizhiheng
 *
 */
@SuppressLint("ValidFragment")
public class FragmentCheckIdentity extends Fragment {
	
	private Context mContext;
	private Handler mHandler;
	private TextView get_code_tv;
	
	private OnStepToModifyPwListener mListener;
	
	interface OnStepToModifyPwListener{
		void stepToModifyPw();
	}
	
	public void setOnStepToModifyPwListener(OnStepToModifyPwListener listener){
		mListener = listener;
	}
	
	public FragmentCheckIdentity(Context context,Handler handler){
		mContext = context;
		mHandler = handler;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_modify_phone_step_1, null);
		TextView nextStep = (TextView) view.findViewById(R.id.next_step);
		TextView boundPhone = (TextView) view.findViewById(R.id.tv_bound_phone);
		get_code_tv = (TextView) view.findViewById(R.id.btn_get_message);
		final EditText check_code = (EditText) view.findViewById(R.id.et_check_code);
		boundPhone.setText("您当前的手机号码是："+ StringUtils.ensurePhoneNum(AppContext.mUserInfo.phone));
		nextStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!isSuccess) {
					postValidateCode(AppContext.mUserInfo.phone, check_code.getText().toString());
				}else{
					Toast.makeText(mContext, "手机验证码错误", Toast.LENGTH_SHORT).show();
				}
			}
		});
		get_code_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				getVerifyCode();
			}
		});
		return view;
	}
	
	/**
	 * 获取验证码
	 */	
	private void getVerifyCode() {
		String phone = AppContext.mUserInfo.phone;
		if (TextUtils.isEmpty(phone)) {
			UIHelper.showToast(R.string.phone_is_null);
			return;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			UIHelper.showToast(R.string.phone_disenable);
			return;
		}
		RequstClient.getRegeditCode(phone, "3",
				new CustomResponseHandler(FragmentCheckIdentity.this.getActivity()) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						Log.d("phone", "checkIdentity msg:"+content);
						try {
							JSONObject result = new JSONObject(content);
							if (result.getString("type").equals("1")) {
								showResendTip();
								String msg = result.getString("msg");
								UIHelper.showToast(msg);
							} else {
								String msg = result.getString("msg");
								UIHelper.showToast(msg);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}
	
	private Timer mTimer = new Timer();
	private int sec = 60;
	private void showResendTip() {

		mTimer = new Timer();
		get_code_tv.setEnabled(false);
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {

				sec--;
				mHandler.post(new Runnable() {
					@Override
					public void run() {

						if (sec < 1) {
							get_code_tv.setEnabled(true);
							get_code_tv.setText("重新获取");
							sec = 60;
							mTimer.cancel();
						} else {
							String tipMsg = String.format(
									getString(R.string.time_tip), sec);
							get_code_tv.setText(tipMsg);
						}
					}
				});
			}
		}, 0, 1000);
	}
	
	/***
	 * 验证验证码
	 */
	private boolean isSuccess;
	private void postValidateCode(String phone, String code) {

		//验证手机
		if (TextUtils.isEmpty(phone)) {
			UIHelper.showToast(R.string.phone_is_null);
			return;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			UIHelper.showToast(R.string.phone_disenable);
			return;
		}
		
		//验证-验证码
		if (TextUtils.isEmpty(code)) {
			UIHelper.showToast("请输入验证码");
			return;
		}
		
		RequstClient.postCode(phone, code, "0", new CustomResponseHandler(
				FragmentCheckIdentity.this.getActivity()) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					LogUtil.i("phone", "postValidateCode:" + content);
					JSONObject result = new JSONObject(content);
					if (!result.getString("type").equals("1")) {
						String errorMsg = result.getString("msg");
						Toast.makeText(mContext, errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					
					//验证码校验成功
					isSuccess = true;
					if(mListener != null){
						mTimer.cancel();
						mListener.stepToModifyPw();
					}
//					setMakeNewPswLayout();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}
