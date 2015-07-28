package com.huiyin.ui.user.account;

import com.huiyin.R;
import com.huiyin.ui.user.account.FragmentCheckIdentity.OnStepToModifyPwListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * 修改结束时的布局
 * 
 * @author leizhiheng
 * 
 */
public class FragmentModifyFinish extends Fragment {

	private OnStepToModifyFinishListener mListener;

	interface OnStepToModifyFinishListener {
		void stepToModifyPw();
	}

	public void setOnStepToModifyPwListener(OnStepToModifyFinishListener listener) {
		mListener = listener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_modify_phone_step_3, null);
		return view;
	}
}
