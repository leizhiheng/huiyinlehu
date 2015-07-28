package com.huiyin.ui.lehuvoucher;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class LehuVoicherViewPagerAdapter extends FragmentStatePagerAdapter {

	private LehuVoucherApplyFragment ApplyFragment;
	private LehuVoucherUploadFragmentNew UploadFragment;

	public LehuVoicherViewPagerAdapter(FragmentManager fm) {
		super(fm);
//		if (ApplyFragment == null)
//			ApplyFragment = new LehuVoucherApplyFragment();
//		if (UploadFragment == null)
//			UploadFragment = new LehuVoucherUploadFragment();
	}

	@Override
	public Fragment getItem(int arg0) {
//		if (arg0 == 0) {
//			if (ApplyFragment == null)
//				ApplyFragment = new LehuVoucherApplyFragment();
//			return ApplyFragment;
//		}
//		if (arg0 == 1) {
//			if (UploadFragment == null)
//				UploadFragment = new LehuVoucherUploadFragment();
//			return UploadFragment;
//		}
		switch (arg0) {
		case 0:
			if (ApplyFragment == null)
				ApplyFragment = new LehuVoucherApplyFragment();
			return ApplyFragment;
		case 1:
			if (UploadFragment == null)
				UploadFragment = new LehuVoucherUploadFragmentNew();
			return UploadFragment;
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		return 2;
	}

	/**
	 * 获取当前的fragment
	 * @param curIndex
	 * @return
	 */
	public Fragment getCurrentFragment(int curIndex){
		return 0==curIndex ? ApplyFragment : UploadFragment;
	}
}
