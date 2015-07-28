package com.huiyin.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huiyin.R;
import com.huiyin.base.BaseLazyFragment;

import java.lang.reflect.Field;

/**
 * 话费在线充值
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-8
 */
public class PhonePayOnlineFragment extends BaseLazyFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_pay_process, container, false);
	}

	public void changeFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
				.replace(R.id.pay_container, fragment).addToBackStack(null)
				.commitAllowingStateLoss();
	}

    public void backFragment() {
        getChildFragmentManager().popBackStack();
	}

	@Override
	public void onFirstUserVisible() {
		//话费-输入手机号码
		changeFragment(new PhonePayOnLineStep1Fragment());
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			// 修复java.lang.IllegalStateException: No activity
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
