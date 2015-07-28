package com.huiyin.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.huiyin.R;
import com.huiyin.base.NestedFragmentActivity;

public class WaterCoalElectricActivity extends NestedFragmentActivity {
    private String[] TITLES={"在线缴费","缴费记录"};

    private com.huiyin.wight.indicator.TabPageIndicator water_indicator;
    private android.support.v4.view.ViewPager water_pager;
    
    /**渠道ID**/
    public static final String ID="ID";
    
    /**viewpager默认选中的页签**/
    public static final String INDEX = "index";
    
    private int chanelId; //12水费，13煤气费，14电费
    
    private int index = 0;//viewpager默认选中的页签
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //阻止EditText自动让输入法弹出
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_water_coal_electric);
        setTitle("水电煤");
        setRightText(View.GONE);
        
        chanelId=getIntent().getIntExtra(ID, -1);
        index=getIntent().getIntExtra(INDEX, 0);
        
        init();
    }

    private void init() {
        MyPagerAdapter mAdapter=new MyPagerAdapter(getSupportFragmentManager());
        water_pager.setAdapter(mAdapter);
        water_pager.setCurrentItem(index);
        water_indicator.setViewPager(water_pager);
    }

    @Override
    protected void onFindViews() {
        water_indicator = (com.huiyin.wight.indicator.TabPageIndicator) findViewById(R.id.water_indicator);
        water_pager = (android.support.v4.view.ViewPager) findViewById(R.id.water_pager);
    }

    public void changeViewPager(int position){
        water_pager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_water_coal_electric, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           if (position % TITLES.length==0){
               return  PayProcessFragment.newInstance(chanelId);
           }else if (position % TITLES.length==1){
               return new WaterPayRecoderFragment();
           }
            return new PayProcessFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position % TITLES.length];
        }
    }
}
