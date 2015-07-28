package com.huiyin.ui.user.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;

/**
 * 预约/退换
 */
public class BespeakReturnActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "BespeakReturnActivity";
    private FragmentTransaction transaction;
    private android.widget.TextView bespeaklookbespeakrecodetextview;//查看预约记录
    private android.widget.TextView bespeaklookreturnrecodetextview;//查看退换货记录
    private android.widget.TextView btnback;
    private android.widget.TextView tvtitle;
    private SearchOrderFragment searchOrderFragment;//搜索订单
    private BespeakRecordFragment recordFragment;//查看预约
    private ReturnRecordFragment returnRecordFragment;//查看退换货

    /**当前显示的fragment**/
    private Fragment currentFragment;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bespeak_return);
        transaction = getSupportFragmentManager().beginTransaction();
        findView();
        setListener();
        initView();
	}

    private void findView() {
        this.tvtitle = (TextView) findViewById(R.id.tv_title);
        this.btnback = (TextView) findViewById(R.id.btn_back);
        this.bespeaklookreturnrecodetextview = (TextView) findViewById(R.id.bespeak_look_return_recode_textview);
        this.bespeaklookbespeakrecodetextview = (TextView) findViewById(R.id.bespeak_look_bespeak_recode_textview);
    }


    private void setListener() {
        bespeaklookreturnrecodetextview.setOnClickListener(this);
        bespeaklookbespeakrecodetextview.setOnClickListener(this);
        btnback.setOnClickListener(this);


    }

    /**
     * 初始化控件
	 */
	private void initView(){
        tvtitle.setText("预约/退换货");
        searchOrderFragment=new SearchOrderFragment();
        recordFragment=new BespeakRecordFragment();
        returnRecordFragment=new ReturnRecordFragment();
//        transaction.add(R.id.fragment_order_bespeak_return,searchOrderFragment);
        //transaction.add(R.id.fragment_order_bespeak_return, recordFragment);
        //transaction.add(R.id.fragment_order_bespeak_return, returnRecordFragment);
        showFragment(searchOrderFragment);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                back();
                break;
            case R.id.bespeak_look_return_recode_textview://查看退换货记录
                showFragment(returnRecordFragment);
                break;
            case R.id.bespeak_look_bespeak_recode_textview://查看预约记录
                showFragment(recordFragment);
                break;
        }
    }

    private boolean back() {
        if(!searchOrderFragment.isHidden()){//显示
            this.finish();
            return true;
        }else{
            showFragment(searchOrderFragment);
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        back();
    }

    public void showFragment(Fragment currentShow){
        transaction=getSupportFragmentManager().beginTransaction();
        if(searchOrderFragment.isAdded() && !searchOrderFragment.isHidden()) {
            transaction.hide(searchOrderFragment);
        }

        if(recordFragment.isAdded() && !recordFragment.isHidden()) {
            transaction.hide(recordFragment);
        }

        if(returnRecordFragment.isAdded() && !returnRecordFragment.isHidden()) {
            transaction.hide(returnRecordFragment);
        }

        if(!currentShow.isAdded()){
            transaction.add(R.id.fragment_order_bespeak_return,currentShow);
        }
        transaction.show(currentShow);
        transaction.commit();
        
        //记住当前显示的fragment
        currentFragment = currentShow;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(null != currentFragment && resultCode == RESULT_OK){
    		
    		//将Activity回调到fragment
    		currentFragment.onActivityResult(requestCode, resultCode, data);
    	}
    }
}
