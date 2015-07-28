package com.huiyin.ui.user.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.http.AsyncHttpResponseHandler;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;

@SuppressLint("ValidFragment")
public class FragmentModifyPwOne extends BaseFragment implements View.OnClickListener{

	private Context mContext;
	private Handler mHandler;
	private Spinner spinner_type;
	private TextView identityWay, get_code_tv;
	private EditText check_code;

	private OnStepToModifyPwListener mListener;

	interface OnStepToModifyPwListener {
		void stepToModifyPw();
	}

	public void setOnStepToModifyPwListener(OnStepToModifyPwListener listener) {
		mListener = listener;
	}

	public FragmentModifyPwOne(Context context, Handler handler) {
		this.mContext = context;
		this.mHandler = handler;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_modify_pw_step_1, null);

		//初始化控件
		identityWay = (TextView) view.findViewById(R.id.tv_identity_way);
		get_code_tv = (TextView) view.findViewById(R.id.btn_get_message);
		check_code = (EditText) view.findViewById(R.id.et_check_code);
		spinner_type = (Spinner) view.findViewById(R.id.spinner_type);
		view.findViewById(R.id.next_step).setOnClickListener(this);
		get_code_tv.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//初始化数据
		initData();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		
		spinner_type.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectType(arg2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		spinner_type.setAdapter(new ArrayAdapter<String>(mContext, R.layout.spin_item, getData()));
		
		//默认用手机验证方式
		selectType(0);
	}
	
	private void selectType(int type){
		switch (type) {
		case 0:
			//手机验证方式
			identityWay.setText("已验证手机:" + StringUtils.ensurePhoneNum(AppContext.mUserInfo.phone));
			get_code_tv.setText("获取短信验证码");
			check_code.setHint("请输入手机验证码");
			spinner_type.setTag(0);
			
			break;

		case 1:
			//邮箱验证方式
			identityWay.setText("已验证邮箱:" + AppContext.mUserInfo.email);
			get_code_tv.setText("获取邮箱验证码");
			check_code.setHint("请输入邮箱验证码");
			spinner_type.setTag(1);
			
			break;
		}
	}
	
	/**
	 * 获取验证方式
	 * @return
	 */
	private List<String> getData() {
		ArrayList<String> data = new ArrayList<String>();
		
		UserInfo userInfo = AppContext.mUserInfo;
		if(!TextUtils.isEmpty(userInfo.phone)){
			data.add("已验证手机");
		}

		//暂时屏蔽邮箱
		if(userInfo.isBoundEmail()){
			data.add("已验证邮箱");
		}
		
		return data;
	}

	/**
	 * 获取手机验证码
	 */
	private void getPhoneVerifyCode() {
		String phone = AppContext.mUserInfo.phone;
		if (TextUtils.isEmpty(phone)) {
			UIHelper.showToast(R.string.phone_is_null);
			return;
		} else if (!StringUtils.isPhoneNumber(phone)) {
			UIHelper.showToast(R.string.phone_disenable);
			return;
		}
		
		RequstClient.getRegeditCode(phone, "3", new CustomResponseHandler(
				FragmentModifyPwOne.this.getActivity()) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				Log.d("phone", "checkIdentity msg:" + content);
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
	
	/**
	 * 获取邮箱验证码
	 */
	private void getEmailVerifyCode() {
		UserInfo userInfo = AppContext.mUserInfo;
		RequstClient.sendCheckEmail(userInfo.userId, userInfo.userName, userInfo.email, "1", "0", new CustomResponseHandler(FragmentModifyPwOne.this.getActivity()) {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				
				int type = JSONParseUtils.getInt(content, "type");
				String msg = JSONParseUtils.getString(content, "msg");
				if(1 != type){
					showMyToast(msg);
					return;
				}else{
					
					showMyToast(msg);
					
					//倒计时60秒后可以重新获取验证码
					showResendTip();
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
							String tipMsg = String.format(getString(R.string.time_tip), sec);
							get_code_tv.setText(tipMsg);
						}
					}
				});
			}
		}, 0, 1000);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		//界面退出的时候，定时器也要杀掉
		try{
			mTimer.cancel();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/***
	 * 验证验证码
	 */
	private boolean isSuccess;

	/**
     * 验证验证码
     *
     * @param phone					手机号码/邮箱
     * @param messageVerifyCode   	验证码
     * @param flag 					0是手机验证码，1是邮箱验证码		
     * @param mHandler
     */
	private void postValidateCode() {
		
		//判断输入的手机验证码
		String code = getText(check_code);
		if(TextUtils.isEmpty(code)){
			Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_LONG).show();
			return;
		}
		
		//获取Phone，code，flag
		UserInfo userInfo = AppContext.mUserInfo;
		int type = Integer.parseInt(spinner_type.getTag().toString());
		String phone = (0==type) ? userInfo.phone : userInfo.email;//需要不带*的手机号码
		String flag = (0==type) ? "0" : "1";
		
		RequstClient.postCode(phone, code, flag, new CustomResponseHandler(FragmentModifyPwOne.this.getActivity()) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					LogUtil.i("phone", "postValidateCode:" + content);
					JSONObject result = new JSONObject(content);
					if (!result.getString("type").equals("1")) {
						String errorMsg = result.getString("msg");
						Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					
					//验证码输入正确，进入下一个界面
					isSuccess = true;
					if (mListener != null) {
						mTimer.cancel();
						mListener.stepToModifyPw();
					}
					// setMakeNewPswLayout();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.next_step:
			
			//下一步
			postValidateCode();	
			
			break;

		case R.id.btn_get_message:
			
			//获取验证码
			int type = Integer.parseInt(spinner_type.getTag().toString());
			switch (type) {
			case 0:
				//获取手机验证码
				getPhoneVerifyCode();
				
				break;

			case 1:
				//获取邮箱验证码
				getEmailVerifyCode();
				
				break;
			}
			
			break;
		}
	}
}
