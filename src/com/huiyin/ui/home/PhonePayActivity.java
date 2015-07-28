package com.huiyin.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.NestedFragmentActivity;

/**
 * 话费充值记录
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-7
 */
public class PhonePayActivity extends NestedFragmentActivity implements OnClickListener, OnPageChangeListener {

	/**viewpager默认选中的页签**/
    public static final String INDEX = "index";
	
	
	/**在线充值**/
	private TextView online_pay_textview;
	
	/**缴费记录**/
	private TextView pay_history_textview;
	
	
	/**分页page**/
    private ViewPager viewPager;
    
    /**viewpager默认选中的页签**/
    private int index = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_pay);
        setTitle("话费充值");
        setRightText(View.GONE);
        
        //获取默认选中的页签
        index=getIntent().getIntExtra(INDEX, 0);
        
        init();
    }

    private void init() {
        MyPagerAdapter mAdapter=new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(index);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    protected void onFindViews() {
        viewPager = (android.support.v4.view.ViewPager) findViewById(R.id.water_pager);
    	online_pay_textview = (TextView)findViewById(R.id.online_pay_textview);
    	pay_history_textview = (TextView)findViewById(R.id.pay_history_textview);
    	
    	online_pay_textview.setOnClickListener(this);
    	pay_history_textview.setOnClickListener(this);
    	curtv = online_pay_textview;
    }

    /**
     * 选中page
     * @param position
     */
    public void changeViewPager(int position){
        viewPager.setCurrentItem(position);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) { super(fm); }
       
        @Override
        public Fragment getItem(int position) {
           if (position == 0){
        	   
        	   //话费充值
               return  new PhonePayOnlineFragment();
           }else{
        	   
        	   //充值记录
               return new PhonePayRecoderFragment();
           }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.online_pay_textview:
			
			//在线充值
			viewPager.setCurrentItem(0);
			checkedView(online_pay_textview);
			
			break;

		case R.id.pay_history_textview:
			
			//缴费记录
			viewPager.setCurrentItem(1);
			checkedView(pay_history_textview);
			
			break;
		}
	}
	
	/**当前被选中的view**/
	private TextView curtv;
	private void checkedView(TextView tv){
		if(null != curtv){
			curtv.setTextColor(getResources().getColor(R.color.black));
		}
		tv.setTextColor(getResources().getColor(R.color.index_red));
		curtv = tv;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		if(0 == arg0){
			checkedView(online_pay_textview);
		}else{
			checkedView(pay_history_textview);
		}
	}
}
