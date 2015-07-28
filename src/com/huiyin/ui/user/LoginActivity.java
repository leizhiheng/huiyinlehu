package com.huiyin.ui.user;

import java.util.Map;

import org.apache.http.Header;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bidaround.youtui_template.YtTemplate;
import cn.bidaround.ytcore.login.AuthListener;
import cn.bidaround.ytcore.login.AuthLogin;
import cn.bidaround.ytcore.login.AuthUserInfo;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.LoginInfo;
import com.huiyin.bean.ThirdLoginBean;
import com.huiyin.pay.wxpay.Constants;
import com.huiyin.ui.MainActivity;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.wight.Tip;
import com.huiyin.wxapi.TokenBean;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class LoginActivity extends BaseActivity implements OnClickListener {

	public final static String TAG = "LoginActivity";
	public static String hkdetail_code = "hkdetail";
	public static String account_code = "account_code";
	private TextView login_forget_pwd, left_ib, middle_title_tv;
	private EditText login_user_et, login_pwd_et;
	private Toast mToast;
	private Button login_btn;
	private CheckBox login_check_box;
	private LoginInfo mLoginInfo;
	private String userName, password;
	// 判断从哪个页面跳转到这个页面
	private String code;
	private String pushFlag;
	private String customContentString;
	private String description;
	private int loginTimes = 0;
	private boolean isChecked = false;
	private TextView ab_right;

	private ImageView zfb, wx, wb, qq;
	private int loginType = 0;

	private PreferenceUtil instance = null;

	public static Map<String, String> map = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_fragment_layout);
		instance = PreferenceUtil.getInstance(mContext);
		initView();
		YtTemplate.init(this);
	}

	private void initView() {

		zfb = (ImageView) findViewById(R.id.login_zfb);
		zfb.setOnClickListener(this);
		wx = (ImageView) findViewById(R.id.login_wx);
		wx.setOnClickListener(this);
		wb = (ImageView) findViewById(R.id.login_wb);
		wb.setOnClickListener(this);
		qq = (ImageView) findViewById(R.id.login_qq);
		qq.setOnClickListener(this);

		login_forget_pwd = (TextView) findViewById(R.id.login_forget_pwd);
		login_forget_pwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(LoginActivity.this, BackPswActivity.class);
				startActivity(i);
				finish();
			}

		});

		left_ib = (TextView) findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (pushFlag.equals("1")) {
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MainActivity.class);
					startActivity(i);
				} else {
					finish();
				}
			}

		});

		code = getIntent().getStringExtra(TAG);
		pushFlag = getIntent().getStringExtra("pushFlag") == null ? "0" : getIntent().getStringExtra("pushFlag");
		customContentString = getIntent().getStringExtra("customContentString");
		description = getIntent().getStringExtra("description");

		mLoginInfo = new LoginInfo();

		middle_title_tv = (TextView) findViewById(R.id.ab_title);
		middle_title_tv.setText("登录");

		ab_right = (TextView) findViewById(R.id.ab_right);
		ab_right.setText("注册");
		ab_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(LoginActivity.this, RegisterActivity.class);
				startActivity(i);
				finish();
			}
		});

		login_user_et = (EditText) findViewById(R.id.login_user_et);
		login_pwd_et = (EditText) findViewById(R.id.login_pwd_et);

		login_btn = (Button) findViewById(R.id.login_btn);
		login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				doLogin();
//				loginLoad();
			}
		});

		login_check_box = (CheckBox) findViewById(R.id.login_check_box);
		login_check_box.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				isChecked = arg1;
				if (arg1) {
					login_check_box.setTextColor(getResources().getColor(R.color.black));
				} else {
					login_check_box.setTextColor(getResources().getColor(R.color.grey1));
				}
			}
		});

		if (login_check_box.isChecked()) {
			isChecked = true;
			login_check_box.setTextColor(getResources().getColor(R.color.black));
		} else {
			isChecked = false;
			login_check_box.setTextColor(getResources().getColor(R.color.grey1));
		}

	}

	private void showToast(int resId) {

		if (mToast == null) {
			mToast = Toast.makeText(getBaseContext(), resId, Toast.LENGTH_SHORT);
		}
		mToast.setText(resId);
		mToast.show();
	}

	private boolean checkLoginInfo(String userName, String password) {

		if (TextUtils.isEmpty(userName)) {
			showToast(R.string.user_is_null);
			return false;
		} else if (TextUtils.isEmpty(password)) {
			showToast(R.string.pwd_is_null);
			return false;
		}
		return true;

	}

	/**
	 * 登录后刷新数据
	 */
	private void refreshData() {

		mLoginInfo.psw = password;
		mLoginInfo.userName = userName;
		mLoginInfo.isChecked = isChecked;
		AppContext.saveLoginInfo(getApplicationContext(), mLoginInfo);


		if (code != null && code.equals(hkdetail_code)) {
			LoginActivity.this.finish();
		} else {
			Intent i = new Intent();
			i.setClass(LoginActivity.this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			LoginActivity.this.finish();
		}

	}


	/**
	 * 登录没带了猜你喜欢数据
	 */
	private void doLogin() {

		userName = login_user_et.getText().toString();
		password = login_pwd_et.getText().toString();
		if (!checkLoginInfo(userName, password)) {
			return;
		}
		
		//登录调用新接口
		RequstClient.loginOverLoad(userName, password, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				
				//异常消息显示
				if(JSONParseUtils.isErrorJSONResult(content)){
					String msg = JSONParseUtils.getString(content, "msg");
					showMyToast(msg);
					return;
				}

				//解析用户信息
				String userJson = JSONParseUtils.getJSONObject(content, "user");
				UserInfo mUserInfo = UserInfo.explainJson(userJson, mContext);
				AppContext.mUserInfo = mUserInfo;
				AppContext.userId = mUserInfo.userId;
				
				//处理跳转
				refreshData();
				
				//添加登录日志
				addUserLoginLog(mUserInfo.userId);
			}

		});
	}
	
	/**
	 * 添加登录日志
	 */
	private void addUserLoginLog(String userId) {
		
		// 添加登录日志
		RequstClient.addUserLoginLog(userId, new CustomResponseHandler(this, false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);

				// 异常消息显示
				if (JSONParseUtils.isErrorJSONResult(content)) {
					String msg = JSONParseUtils.getString(content, "msg");
					LogUtil.i(TAG, "添加登录日志---"+msg);
					return;
				}

				// 添加登录日志
				LogUtil.i(TAG, "添加登录日志---成功");
			}

		});
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pushFlag.equals("1")) {
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 支付宝第三方登录
		case R.id.login_zfb:

			break;
		// 微信第三方登录
		case R.id.login_wx:
			onWXClickLogin();
			break;
		// 新浪微博第三方登录
		case R.id.login_wb:
			onSinaClickLogin();
			break;
		// QQ第三方登录
		case R.id.login_qq:
			onQQClickLogin();
			break;
		default:
			break;
		}
	}

	/**
	 * 微信登录
	 */
	private void onWXClickLogin() {
		loginType = 1;
		IWXAPI api;
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		if (!api.isWXAppInstalled() || !api.isWXAppSupportAPI()) {
			Toast.makeText(mContext, "微信未安装或微信版本过低，请安装升级最新版。", Toast.LENGTH_SHORT).show();
			return;
		}
		// 注册到微信
		if (api.registerApp(Constants.APP_ID)) {
			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "com.huiyin.login";

			api.sendReq(req);
		}
	}

	/**
	 * 新浪登录
	 */
	private void onSinaClickLogin() {
		loginType = 2;
		AuthLogin authSinaLogin = new AuthLogin();
		AuthListener SinaListener = new AuthListener() {
			@Override
			public void onAuthFail() {
				Toast.makeText(mContext, "新浪微博未安装或微信版本过低，请安装升级最新版。", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAuthCancel() {
			}

			@Override
			public void onAuthSucess(AuthUserInfo info) {
				instance.setQQOpenid(info.getQqOpenid());
				Gson gson = new Gson();
				String i = gson.toJson(info, AuthUserInfo.class);
				oauthUser(null, info.getSinaUid(), i);
			}
		};
		authSinaLogin.sinaAuth(this, SinaListener);
	}

	/**
	 * QQ登录
	 */
	private void onQQClickLogin() {
		loginType = 3;
		AuthLogin authQQLogin = new AuthLogin();
		AuthListener QQListener = new AuthListener() {
			@Override
			public void onAuthCancel() {
			}

			@Override
			public void onAuthFail() {
				Toast.makeText(mContext, "QQ未安装或微信版本过低，请安装升级最新版。", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onAuthSucess(AuthUserInfo info) {
				instance.setQQOpenid(info.getQqOpenid());
				Gson gson = new Gson();
				String i = gson.toJson(info, AuthUserInfo.class);
				oauthUser(null, info.getQqOpenid(), i);
			}
		};
		authQQLogin.qqAuth(this, QQListener);
	}

	@Override
	protected void onDestroy() {
		YtTemplate.release(this);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		String code = intent.getStringExtra("code");
		if (code != null) {
			getAssessToken(code);
		}
	}

	private void getAssessToken(String code) {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, false) {

			@Override
			public void onStart() {
				super.onStart();
				Tip.showLoadDialog(mContext, "正在加载...");
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Tip.colesLoadDialog();
			}

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				TokenBean bean = TokenBean.explainJson(content, mContext);
				if (bean.access_token != null && bean.openid != null) {
					oauthUser(bean, null, null);
				} else {
					Tip.colesLoadDialog();
					Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
				}
			}
		};
		RequstClient.getWeChatToken(code, handler);
	}

	private void oauthUser(final TokenBean bean, final String openId, final String userinfo) {
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, false) {

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Tip.colesLoadDialog();
			}

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Tip.colesLoadDialog();
				ThirdLoginBean data = ThirdLoginBean.explainJson(content, mContext);
				if (data.type <= 0) {
					Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
					return;
				}
				if (data.type == 1) {
					AppContext.getInstance().mUserInfo = data.user;
					AppContext.getInstance().userId = data.user.userId;

					Intent i = new Intent();
					i.setClass(LoginActivity.this, MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					LoginActivity.this.finish();
					return;
				}
				if (data.type == 2) {
					showChoseDialog(bean, openId, userinfo);
					return;
				}
			}
		};
		switch (loginType) {
		case 1:
			RequstClient.appOpenIDOAuthPhone(bean.openid, loginType, handler);
			break;
		case 2:
			RequstClient.appOpenIDOAuthPhone(openId, loginType, handler);
			break;
		case 3:
			RequstClient.appOpenIDOAuthPhone(openId, loginType, handler);
			break;
		}

	}

	private Dialog mDialog;

	private void showChoseDialog(final TokenBean bean, final String openId, final String userinfo) {
		View mView = LayoutInflater.from(mContext).inflate(R.layout.chose_bind_dialog, null);
		mDialog = new Dialog(mContext, R.style.dialog);
		Button yes = (Button) mView.findViewById(R.id.com_ok_btn);
		Button cancle = (Button) mView.findViewById(R.id.com_cancel_btn);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				Intent i = new Intent();
				i.setClass(LoginActivity.this, RegisterActivity.class);
				i.putExtra("LoginType", loginType);
				switch (loginType) {
				case 1:
					i.putExtra("access_token", bean.access_token);
					i.putExtra("openid", bean.openid);
					break;
				case 2:
					i.putExtra("userinfo", userinfo);
					break;
				case 3:
					i.putExtra("userinfo", userinfo);
					break;
				}
				startActivity(i);
				LoginActivity.this.finish();
			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();
				Intent i = new Intent();
				i.setClass(LoginActivity.this, ThirdBindActivity.class);
				i.putExtra("LoginType", loginType);
				switch (loginType) {
				case 1:
					i.putExtra("openid", bean.openid);
					break;
				case 2:
					i.putExtra("openid", openId);
					break;
				case 3:
					i.putExtra("openid", openId);
					break;
				}
				startActivity(i);
				LoginActivity.this.finish();
			}
		});
		mDialog.setContentView(mView);
		mDialog.setCanceledOnTouchOutside(true);
		mDialog.setCancelable(true);
		mDialog.show();
	}
}
