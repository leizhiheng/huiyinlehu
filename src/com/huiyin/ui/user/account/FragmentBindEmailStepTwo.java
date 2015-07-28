package com.huiyin.ui.user.account;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.utils.JSONParseUtils;

public class FragmentBindEmailStepTwo extends BaseFragment implements OnClickListener {
	private static final String TAG = "FragmentBindEmailStepTwo";

	

	/** 发送结果 **/
	private TextView send_result;

	/** 重新发送 **/
	private TextView review_email;

	/** 倒计时，xx分xx秒后可以重新发送 **/
	private TextView tv_timer;

	
	/** 要绑定的邮箱 **/
	private String bindEmail;

	/**邮件服务器地址**/
	private String email_url;
	
	/**是否发送成功**/
	private boolean isSendSuccess;
	
	/**发送次数**/
	private int send_count;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 发送邮件
		//sendCheckEmail();
		doSendSuccess(email_url);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bind_email_step_2, null);

		send_result = (TextView) view.findViewById(R.id.tv_input_bind_email);
		review_email = (TextView) view.findViewById(R.id.next_step);
		tv_timer = (TextView) view.findViewById(R.id.tv_timer);
		tv_timer.setVisibility(View.INVISIBLE);

		// 重新发送
		review_email.setOnClickListener(this);

		return view;
	}

	
	/**
	 * 打开本地邮箱客户端
	 */
	public void verifyCheckCode() {
		Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(email_url));
		// it.setClassName("com.android.browser",
		// "com.android.browser.BrowserActivity");
		this.startActivity(it);
	}

	/**
	 * 发送邮箱
	 */
	public void sendCheckEmail() {
		RequstClient.sendCheckEmail(AppContext.userId, AppContext.mUserInfo.userName, bindEmail, "0", "1", new CustomResponseHandler(getActivity()) {

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if (!isSuccessResponse(content)) {

					// 邮件发送失败
					send_result.setText("邮件发送失败！");
					review_email.setText("重新发送");

				} else {

					// 邮件发送成功
					String emailSever = JSONParseUtils.getString(content, "url");
					doSendSuccess(emailSever);

				}
			}
		});
	}

	/**
	 * 邮件发送成功
	 */
	private void doSendSuccess(String emailSever) {
		isSendSuccess = true;

		send_count++;
		send_result.setText(Html.fromHtml(String.format(getActivity().getResources().getString(R.string.account_email_sent_tip), bindEmail, send_count)));
		review_email.setText("查看邮箱");

		tv_timer.setVisibility(View.VISIBLE);
		showMyToast("邮件发送成功");

		// 获取指定打开的邮箱客户端
		email_url = emailSever;
		if (mListener != null) {
			mListener.bindSuccess(true);
		}

		// 重新发送计时器
		showResendTip();
	}

	
	/** 邮箱发送成功回调 **/
	private OnStepToBindFinishListener mListener;
	
	interface OnStepToBindFinishListener {
		void bindSuccess(boolean isSuccess);
	}

	public void setOnStepToBindFinishListener(OnStepToBindFinishListener listener) {
		mListener = listener;
	}
	
	
	
	private Timer mTimer = new Timer();
	private int sec = 60;
	private Handler mHandler = new Handler();

	private void showResendTip() {

		mTimer = new Timer();
		tv_timer.setEnabled(false);
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {

				sec--;
				mHandler.post(new Runnable() {
					@Override
					public void run() {

						if (sec < 1) {
							isSendSuccess = false;
							tv_timer.setEnabled(true);
							// tv_timer.setText("可重新发送");
							tv_timer.setVisibility(View.INVISIBLE);
							review_email.setText("重新发送");
							sec = 60;
							mTimer.cancel();
						} else {
							try {
								String tipMsg = String.format(getString(R.string.time_tip), sec) + "可重新发送";
								tv_timer.setText(tipMsg);
							} catch (Exception e) {
								mTimer.cancel();
								return;
							}
						}
					}
				});
			}
		}, 0, 1000);
	}

	/**
	 * 设置要绑定的邮箱
	 * 
	 * @param newEmail
	 */
	public void setEmail(String newEmail, String email_url) {
		this.bindEmail = newEmail;
		this.email_url = email_url;
	}

	@Override
	public void onClick(View v) {
		
		if (isSendSuccess) {

			// 发送成功了，则打开邮箱客户端
			verifyCheckCode();

		} else {

			// 发送邮件
			sendCheckEmail();
		}
	}
}