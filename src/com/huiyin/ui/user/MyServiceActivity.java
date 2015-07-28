package com.huiyin.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.classic.ReportsListActivity;
import com.huiyin.ui.user.complaint.MyComplaintActivity;

/**
 * 我的服务
 * Created by kuangyong on 2015/6/11.
 */
public class MyServiceActivity extends BaseActivity implements View.OnClickListener{
    private android.widget.RelativeLayout lehumybespeak;
    private android.widget.LinearLayout lehumycomplaint;
    private android.widget.RelativeLayout lehumyreport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myservice);
        findView();
        setListener();
    }

    private void findView(){
        this.lehumyreport = (RelativeLayout) findViewById(R.id.lehu_my_report);
        this.lehumycomplaint = (LinearLayout) findViewById(R.id.lehu_my_complaint);
        this.lehumybespeak = (RelativeLayout) findViewById(R.id.lehu_my_bespeak);
    }

    private void setListener(){
        lehumyreport.setOnClickListener(this);
        lehumycomplaint.setOnClickListener(this);
        lehumybespeak.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.lehu_my_bespeak://我的预约
                Intent bespeak_intent = new Intent();
                bespeak_intent.setClass(mContext, MyBespeakActivity.class);
                startActivity(bespeak_intent);
                break;
            case R.id.lehu_my_complaint://我的投诉
                Intent intentComplaint = new Intent(mContext, MyComplaintActivity.class);
                startActivity(intentComplaint);
                break;
            case R.id.lehu_my_report://我的举报
            	Intent reportIntent = new Intent(mContext,ReportsListActivity.class);
            	startActivity(reportIntent);

                break;
        }
    }
}
