package com.huiyin.ui.user.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.utils.CryptionUtil;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("ValidFragment")
public class FragmentModifyPwTwo extends Fragment{
	private static final String TAG = "FragmentModifyPwTwo";
			
	private EditText new_pw,check_pw;
	private View pw_level_layout;
	
	private Context mContext;
	public FragmentModifyPwTwo(Context context){
		mContext = context;
	}
	private OnStepToFinishListener mListener;

	interface OnStepToFinishListener {
		void stepToFinish();
	}

	public void setOnStepToFinishListener(OnStepToFinishListener listener) {
		mListener = listener;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_modify_pw_step_2, null);
		new_pw = (EditText) view.findViewById(R.id.et_new_pw);
		check_pw = (EditText) view.findViewById(R.id.et_check_pw);
		TextView tv_next_step = (TextView) view.findViewById(R.id.next_step);
		pw_level_layout = view.findViewById(R.id.layout_pw_level);
		new_pw.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				String sfae = CryptionUtil.getPasswordSfae(arg0.toString());
				setPwLevelView(Integer.parseInt(sfae));
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
		tv_next_step.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if((new_pw == null ? 0 : new_pw.getText().length()) <= 0){
					Toast.makeText(mContext, "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (new_pw.getText().length() < 6) {
					Toast.makeText(mContext, "密码不能少于6位数", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!checkPw()){
					Toast.makeText(mContext, "密码输入不一致", Toast.LENGTH_SHORT).show();
					return;
				}
				
				uploadNewPw();
			}
		});
		return view;
	}
	
	public void setPwLevelView(int sfae){
		Resources res = mContext.getResources();
		switch (sfae) {
		case 1:
			Log.d("zhihenge", "res = "+res+" pw_level_layout="+pw_level_layout);
			Log.d("zhihenge", "pw_level_layout.findViewById(R.id.level_low) = "+pw_level_layout.findViewById(R.id.level_low));
			pw_level_layout.findViewById(R.id.level_low).setBackgroundColor(res.getColor(R.color.yt_cap_yellow));
			pw_level_layout.findViewById(R.id.level_mid).setBackgroundColor(res.getColor(R.color.gray7));
			pw_level_layout.findViewById(R.id.level_high).setBackgroundColor(res.getColor(R.color.gray7));
			break;
		case 2:
			pw_level_layout.findViewById(R.id.level_low).setBackgroundColor(res.getColor(R.color.yt_cap_yellow));
			pw_level_layout.findViewById(R.id.level_mid).setBackgroundColor(res.getColor(R.color.yt_cap_yellow));
			pw_level_layout.findViewById(R.id.level_high).setBackgroundColor(res.getColor(R.color.gray7));
			break;
		case 3:
			pw_level_layout.findViewById(R.id.level_low).setBackgroundColor(res.getColor(R.color.yt_cap_yellow));
			pw_level_layout.findViewById(R.id.level_mid).setBackgroundColor(res.getColor(R.color.yt_cap_yellow));
			pw_level_layout.findViewById(R.id.level_high).setBackgroundColor(res.getColor(R.color.yt_cap_yellow));
			break;

		default:
			break;
		}
	}
	/**
	 * 判断两次密码输入是否一致
	 */
	public boolean checkPw(){
		return new_pw.getText().toString().equals(check_pw.getText().toString());
	}
	
	public void uploadNewPw(){
		
		//新密码
		final String newPassword = new_pw.getText().toString().trim();
		String sfae = CryptionUtil.getPasswordSfae(newPassword);
		
		RequstClient.changePwd(AppContext.mUserInfo.userId, newPassword, sfae, new CustomResponseHandler(
				FragmentModifyPwTwo.this.getActivity(), true) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "postFindPsw:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(mContext, errorMsg,Toast.LENGTH_SHORT).show();
						return;
					}else {
						
						Toast.makeText(mContext, "密码修改成功", Toast.LENGTH_SHORT).show();
						
						//密码等级重新赋值--未了避免回到上一个界面，不刷新密码等级问题
						String safe = CryptionUtil.getPasswordSfae(newPassword);
						AppContext.mUserInfo.pwdSfae = safe;
						
						if (mListener != null) {
							mListener.stepToFinish();
						}
					}
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}
	
}