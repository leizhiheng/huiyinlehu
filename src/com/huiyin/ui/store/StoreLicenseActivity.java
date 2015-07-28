package com.huiyin.ui.store;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.CodeImage;

/**
 * Created by justme on 15/5/14.
 */
public class StoreLicenseActivity extends Activity {

    // Content View Elements

    private TextView ab_back;
    private TextView ab_title;

    private ViewSwitcher mViewSwitcher;

    private EditText verifiction_code_edit;
    private ImageView verifiction_code_image;
    private TextView verifiction_code_change;
    private Button verifiction_code_comfirm;

    private ScrollView scrollView;
    private TextView name;
    private TextView license_num;
    private TextView license_people;
    private TextView license_location;
    private TextView license_register_money;
    private TextView license_period_of_validity;
    private TextView license_range;
    private TextView company_loation;
    private TextView store_name;
    private TextView store_web;
    private TextView beizhu;
    private TextView store_beizhu;


    // End Of Content View Elements

    private Context mContext;
    private int storeId;
    private StoreLicenseBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_license_layout);
        mContext = this;
        storeId = getIntent().getIntExtra("storeId", -1);
        findView();
        setListener();
        InitData();
    }

    private void findView() {
        ab_back = (TextView) findViewById(R.id.ab_back);
        ab_title = (TextView) findViewById(R.id.ab_title);
        ab_title.setText("查询执照");

        mViewSwitcher =(ViewSwitcher) findViewById(R.id.store_license_vs);

        verifiction_code_edit = (EditText) findViewById(R.id.verifiction_code_edit);
        verifiction_code_image = (ImageView) findViewById(R.id.verifiction_code_image);
        verifiction_code_image.setImageBitmap(CodeImage.getInstance().createBitmap());
        verifiction_code_image.setTag(CodeImage.getInstance().getCode());
        verifiction_code_change = (TextView) findViewById(R.id.verifiction_code_change);
        verifiction_code_comfirm = (Button) findViewById(R.id.verifiction_code_comfirm);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        name = (TextView) findViewById(R.id.name);
        license_num = (TextView) findViewById(R.id.license_num);
        license_people = (TextView) findViewById(R.id.license_people);
        license_location = (TextView) findViewById(R.id.license_location);
        license_register_money = (TextView) findViewById(R.id.license_register_money);
        license_period_of_validity = (TextView) findViewById(R.id.license_period_of_validity);
        license_range = (TextView) findViewById(R.id.license_range);
        company_loation = (TextView) findViewById(R.id.company_loation);
        store_name = (TextView) findViewById(R.id.store_name);
        store_web = (TextView) findViewById(R.id.store_web);
        beizhu = (TextView) findViewById(R.id.beizhu);
        store_beizhu = (TextView) findViewById(R.id.store_beizhu);
    }

    private void setListener() {
        ab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        verifiction_code_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifiction_code_image.setImageBitmap(CodeImage.getInstance().createBitmap());
                verifiction_code_image.setTag(CodeImage.getInstance().getCode());
            }
        });
        verifiction_code_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = verifiction_code_edit.getText().toString();
                if(StringUtils.isBlank(code)) {
                    Toast.makeText(mContext,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!code.equalsIgnoreCase((String) verifiction_code_image.getTag())) {
                    Toast.makeText(mContext,"请输入正确的验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(data==null) {
                    InitData();
                }
                mViewSwitcher.setDisplayedChild(1);
            }
        });
    }

    private void InitData() {
        CustomResponseHandler handler = new CustomResponseHandler(this, true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                data = StoreLicenseBean.explainJson(content, mContext);
                if (data != null && data.type > 0) {
                    RefreshView(data.getBLInfo());
                }
            }
        };
        RequstClient.BLInfo(handler, storeId);
    }

    private void RefreshView(StoreLicenseBean.StoreLicense blInfo) {
        name.append(blInfo.getCOMMPANYNAME());
        license_num.append(blInfo.getBUSINESSLICENSENO());
        license_people.append(blInfo.getCOMMPANYLINKMANNAME());
        license_location.append(blInfo.getBLADDRESS());
        license_register_money.append(blInfo.getCOMMPANYREGISTEREDCAPITAL() + "万元");
        license_period_of_validity.append(blInfo.getBUSINESSLICENSE());
        license_range.append(blInfo.getBUSINESSLICENSESCOPE());
        company_loation.append(blInfo.getCOMMPANYADDRESS());
        store_name.append(blInfo.getSTORENAME());
        store_web.setText("店铺网址：" + blInfo.getWEBURL());
    }
}
