package com.huiyin.ui.user;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.MainActivity;
import com.huiyin.utils.CryptionUtil;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;

public class RegisterActivity extends BaseActivity {

	private final static String TAG = "RegisterActivity";
	TextView register_agreement_id, left_ib, middle_title_tv, right_ib;
	EditText register_phone, registr_code, register_pwd, register_pwd_again;
	TextView get_code_tv;
	Button register_btn;
	private static final int SHOW_TOSAT = 0;
	private Toast mToast;
	private boolean isGetRegeditCode;
	private Handler mHandler;
	Timer mTimer = null;
	int sec = 60;
	String sendCode;
	CheckBox cb_agree, show_psw_cb;

	private int LoginType;// 登陆的方式 0 普通注册 1 微信注册 2 新浪 3 QQ
	private String access_token;
	private String openid;
	private String userinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		Intent i = getIntent();
		LoginType = i.getIntExtra("LoginType", 0);
		switch (LoginType) {
		case 0:
			break;
		case 1:
			access_token = i.getStringExtra("access_token");
			openid = i.getStringExtra("openid");
			break;
		default:
			userinfo = i.getStringExtra("userinfo");
			break;
		}
		initView();
	}

	private void initView() {

		register_agreement_id = (TextView) findViewById(R.id.register_agreement_id);
		register_agreement_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(RegisterActivity.this, CommonWebPageActivity.class);
				i.putExtra("server", "服务协议");
				i.putExtra("flag", "server");
				startActivity(i);
			}
		});

		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("注册");
		right_ib = (TextView) findViewById(R.id.ab_right);
		right_ib.setBackgroundColor(getResources().getColor(R.color.index_red));
		right_ib.setText("登录");
		right_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(RegisterActivity.this, LoginActivity.class);
				startActivity(i);
				finish();
			}
		});

		register_phone = (EditText) findViewById(R.id.register_phone);
		registr_code = (EditText) findViewById(R.id.registr_code);
		register_pwd = (EditText) findViewById(R.id.register_pwd);
		register_pwd_again = (EditText) findViewById(R.id.register_pwd_again);
		cb_agree = (CheckBox) findViewById(R.id.cb_agree);
		show_psw_cb = (CheckBox) findViewById(R.id.show_psw_cb);

		show_psw_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					register_pwd
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
					register_pwd_again
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				} else {
					register_pwd
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
					register_pwd_again
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
				}
			}
		});

		register_btn = (Button) findViewById(R.id.register_btn);
		get_code_tv = (TextView) findViewById(R.id.get_code_tv);
		get_code_tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				getVerifyCode();
			}
		});

		register_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (checkInfo()) {
					doRegedit();
				}
			}
		});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case SHOW_TOSAT: {
					String message = (String) msg.obj;
					showToast(message);
				}
					break;
				}
			}
		};

	}

	private void showToast(String msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
		}
		mToast.setText(msg);
		mToast.show();
	}

	private void doRegedit() {
		String registerPhone = register_phone.getText().toString().trim();
		String password = register_pwd.getText().toString().trim();
		String pswSfae = CryptionUtil.getPasswordSfae(password);
		String userCode = registr_code.getText().toString();

		//注册调用新接口
		RequstClient.registerLoad(registerPhone, password, pswSfae, userCode, access_token,
				openid,LoginType,userinfo,new CustomResponseHandler(RegisterActivity.this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {

						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "doRegedit:" + content);

						try {
							JSONObject obj = new JSONObject(content);
							if (obj.getString("type").equals("1")) {

								UserInfo mUserInfo = new Gson().fromJson(obj.getJSONObject("user").toString(),UserInfo.class);
								
								AppContext.mUserInfo = mUserInfo;
								AppContext.userId = mUserInfo.userId;
								Toast.makeText(RegisterActivity.this,
										R.string.regedit_success,
										Toast.LENGTH_LONG).show();

								AppContext.MAIN_TASK = AppContext.LEHU;
								Intent i = new Intent();
								i.setClass(RegisterActivity.this,
										MainActivity.class);
								i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(i);
								RegisterActivity.this.finish();

							} else {
								String msg = obj.getString("msg");
								Toast.makeText(RegisterActivity.this, msg,
										Toast.LENGTH_LONG).show();
							}

						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});

	}

	/**
	 * 验证信息
	 * 
	 * @return
	 */
	private boolean checkInfo() {

		Message msg = mHandler.obtainMessage();
		msg.what = SHOW_TOSAT;
		/* 手机注册 */
		String phoneNumber = register_phone.getText().toString();
		String pwd = register_pwd.getText().toString();
		String affirmPwd = register_pwd_again.getText().toString();
		String phoneCode = registr_code.getText().toString();

		if (TextUtils.isEmpty(phoneNumber)) {
			
			//手机号码非空
			msg.obj = getString(R.string.phone_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (!StringUtils.isPhoneNumber(phoneNumber)) {
			
			//手机号码合法性验证
			msg.obj = getString(R.string.phone_disenable);
			mHandler.sendMessage(msg);
			return false;
		} else if (TextUtils.isEmpty(pwd)) {
			
			//密码非空
			msg.obj = getString(R.string.pwd_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (TextUtils.isEmpty(affirmPwd) || !affirmPwd.equals(pwd)) {
			
			//两次输入密码一致
			msg.obj = getString(R.string.affirm_pwd_not_same);
			mHandler.sendMessage(msg);
			return false;
		} else if(pwd.length() < 6){
			
			//密码长度最少6位
			msg.obj = getString(R.string.pwd_least_6);
			mHandler.sendMessage(msg);
			return false;
			
		}
		// else if (!isGetRegeditCode) {
		// /* 没有获取过验证码 */
		// msg.obj = getString(R.string.not_get_ver_code);
		// mHandler.sendMessage(msg);
		// return false;
		// }
		else if (TextUtils.isEmpty(phoneCode)) {
			msg.obj = getString(R.string.ver_code_is_null);
			mHandler.sendMessage(msg);
			return false;
		} else if (!cb_agree.isChecked()) {
			msg.obj = getString(R.string.agreement);
			mHandler.sendMessage(msg);
			return false;
		}

		return true;
	}

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

	/**
	 * 获取验证码
	 */

	private void getVerifyCode() {

		String registerPhone = register_phone.getText().toString();
		if (TextUtils.isEmpty(registerPhone)) {
			UIHelper.showToast(R.string.phone_is_null);
			return;
		} else if (!StringUtils.isPhoneNumber(registerPhone)) {
			UIHelper.showToast(R.string.phone_disenable);
			return;
		}
		RequstClient.getRegeditCode(registerPhone, "0",
				new CustomResponseHandler(RegisterActivity.this) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						isGetRegeditCode = false;
					}

					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						try {
							LogUtil.i(TAG, "getRegeditCode:" + content);
							isGetRegeditCode = true;
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

}
