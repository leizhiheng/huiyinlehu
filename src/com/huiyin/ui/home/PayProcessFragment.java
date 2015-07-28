package com.huiyin.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huiyin.R;
import com.huiyin.base.BaseLazyFragment;

import java.lang.reflect.Field;

public class PayProcessFragment extends BaseLazyFragment {
    public static final String IDTYPE="ID";
    private int Id=0;

    public static PayProcessFragment newInstance(int ID) {
        PayProcessFragment mFragment=new PayProcessFragment();
        Bundle mBundle=new Bundle();
        mBundle.putInt(IDTYPE,ID);
        mFragment.setArguments(mBundle);
        return mFragment;
    }
	public PayProcessFragment() {
		// Required empty public constructor
	}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            Id=getArguments().getInt(IDTYPE);
        }
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater
				.inflate(R.layout.fragment_pay_process, container, false);
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
        if (Id==12){
            //水费
            changeFragment(WaterPayFragment.newInstance("1"));
        } else if (Id==13){
            //煤气费
            changeFragment(WaterPayFragment.newInstance("2"));
        } else if (Id==14){
            //电费
            changeFragment(WaterPayFragment.newInstance("0"));
        }else{
        	//水费
            changeFragment(WaterPayFragment.newInstance("1"));
        }

	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			// 修复java.lang.IllegalStateException: No activity
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
