package com.huiyin.ui.user.account;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.ui.user.account.FragmentBindEmailSetpOne.OnStepToCheckEmailListener;

/**
 * 
 * @author 刘远祺
 *
 * @todo 修改邮箱
 *
 * @date 2015-6-25
 */
public class FragmentModifyEmailOne extends Fragment {

	private static final String TAG = "FragmentModifyEmailOne";
	private OnStepToModifyEmailListener mListener;

	interface OnStepToModifyEmailListener {
		void stepToModifyEmail();
	}

	public void setOnStepToCheckEmailListener(OnStepToModifyEmailListener listener) {
		mListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_modify_email_step_1, null);
		TextView nextStep = (TextView) view.findViewById(R.id.next_step);
		TextView email = (TextView) view.findViewById(R.id.tv_input_bind_email);
		email.setText(AppContext.mUserInfo.email);
		final EditText tv_pw = (EditText) view.findViewById(R.id.et_bind_email);
		nextStep.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isSuccess){
				}else{
					verifyPasswork(tv_pw.getText().toString());
				}
			}
		});
		return view;
	}
	
	private boolean isSuccess;
	public void verifyPasswork(String pw){
		RequstClient.verifyPassword(AppContext.userId,pw,new CustomResponseHandler(getActivity()) {
			@Override
			public void onFailure(Throwable error, String content) {
				Log.d(TAG, " verifyPasswork,msg:"+content);
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")){
						Toast.makeText(FragmentModifyEmailOne.this.getActivity(), "请输入正确的登录密码", Toast.LENGTH_SHORT).show();
					} else {
						isSuccess = true;
						if(mListener != null){
							mListener.stepToModifyEmail();
						}
						Toast.makeText(FragmentModifyEmailOne.this.getActivity(), "密码验证成功", Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}

