package com.huiyin.ui.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.huiyin.R;
import com.huiyin.utils.DensityUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.Tip;
import com.zxing.encoding.EncodingHandler;

/**
 * Created by justme on 15/5/14.
 */
public class StoreErcodeActivity extends Activity {

    // Content View Elements

    private TextView ab_back;
    private TextView ab_title;
    private ImageView ab_right_btn;
    private TextView name;
    private ImageView image;

    // End Of Content View Elements

    private Context mContext;
//    private int storeId;
    private String storeName;
    private String webUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ercode_layout);
        mContext = this;
        Intent intent = getIntent();
//        storeId = intent.getIntExtra("storeId", -1);
        storeName = intent.getStringExtra("storeName");
        webUrl = intent.getStringExtra("webUrl");
        findView();
        setListener();
        InitData();
    }

    private void findView() {
        ab_back = (TextView) findViewById(R.id.ab_back);
        ab_title = (TextView) findViewById(R.id.ab_title);
        ab_right_btn = (ImageView) findViewById(R.id.ab_right_btn);
        name = (TextView) findViewById(R.id.name);
        name.setText(storeName);
        image = (ImageView) findViewById(R.id.image);
    }

    private void setListener() {
        ab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void InitData() {
        if(StringUtils.isBlank(webUrl)) {
            Toast.makeText(mContext,"系统错误",Toast.LENGTH_SHORT).show();
            return;
        }
        Tip.showLoadDialog(mContext,"正在生成");
        try {
            Bitmap bitmap = EncodingHandler.createQRCode(webUrl, DensityUtil.dp2px(mContext, 210));
            image.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Tip.colesLoadDialog();
            Toast.makeText(mContext,"系统错误",Toast.LENGTH_SHORT).show();
        }
        Tip.colesLoadDialog();
    }


}
