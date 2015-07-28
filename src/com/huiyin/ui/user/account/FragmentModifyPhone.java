package com.huiyin.ui.user.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * 修改密码布局
 * 
 * @author leizhiheng
 * 
 */
@SuppressLint("ValidFragment")
public class FragmentModifyPhone extends Fragment implements View.OnClickListener {

	private EditText new_phone;
	private EditText check_code;
	private Context mContext;
	private Handler mHandler;
	private TextView get_code_tv;

	private static OnStepToModifyFinishListener mListener;

	interface OnStepToModifyFinishListener {
		void stepToModifyFinish();
	}

	public void setOnStepToModifyFinishListener(OnStepToModifyFinishListener listener) {
		mListener = listener;
	}

	public FragmentModifyPhone(Context context, Handler handler) {
		mContext = context;
		mHandler = handler;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_modify_phone_step_2, null);

		get_code_tv = (TextView) view.findViewById(R.id.btn_get_message);
		check_code = (EditText) view.findViewById(R.id.et_identity_code);
		new_phone = (EditText) view.findViewById(R.id.et_phone);

		view.findViewById(R.id.next_step).setOnClickListener(this);
		get_code_tv.setOnClickListener(this);
		
		return view;
	}

	/**
	 * 获取验证码
	 */
	private void getVerifyCode(String phone) {
		// String phone = AppContext.mUserInfo.phone;
		if (TextUtils.isEmpty(phone)) {
			UIHelper.showToast(R.string.phone_is_null);
			return;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			UIHelper.showToast(R.string.phone_disenable);
			return;
		}

		RequstClient.getRegeditCode(phone, "0", new CustomResponseHandler(FragmentModifyPhone.this.getActivity()) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				Log.d("phone", "checkIdentity msg:" + content);
				int type = JSONParseUtils.getInt(content, "type");
				String msg = JSONParseUtils.getString(content, "msg");
				if (1 == type) {
					showResendTip();
					UIHelper.showToast(msg);
				} else {
					UIHelper.showToast(msg);
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
							String tipMsg = String.format(mContext.getString(R.string.time_tip), sec);
							get_code_tv.setText(tipMsg);
						}
					}
				});
			}
		}, 0, 1000);
	}

	/***
	 * 修改手机号码
	 */
	private void amendPhone(final String phone, String messageVerifyCode) {

		//判断手机
		if (TextUtils.isEmpty(phone)) {
			UIHelper.showToast(R.string.phone_is_null);
			return;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			UIHelper.showToast(R.string.phone_disenable);
			return;
		}
		
		//验证码
		if (TextUtils.isEmpty(messageVerifyCode)) {
			UIHelper.showToast(R.string.ver_code_is_null);
			return;
		} 
		
		String userId = AppContext.mUserInfo.userId;
		RequstClient.amendPhone(userId, phone, messageVerifyCode, new CustomResponseHandler(FragmentModifyPhone.this.getActivity()) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					LogUtil.i("phone", "postValidateCode:" + content);
					JSONObject result = new JSONObject(content);
					if (!result.getString("type").equals("1")) {
						String errorMsg = result.getString("msg");
						Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}

					// 手机号码修改成功
					AppContext.mUserInfo.phone = phone;
					UIHelper.showToast("手机修改成功");

					//杀掉timer线程
					if (mListener != null) {
						mTimer.cancel();
						mListener.stepToModifyFinish();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get_message:

			// 获取验证码
			if ("".equals(new_phone.getText().toString())) {
				Toast.makeText(mContext, "请输入新手机号码", Toast.LENGTH_SHORT).show();
			} else {
				getVerifyCode(new_phone.getText().toString());
			}

			break;
		case R.id.next_step:

			// 下一步
			amendPhone(new_phone.getText().toString(), check_code.getText().toString());

			break;
		}
	}

}
