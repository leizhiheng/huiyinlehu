package com.huiyin.ui.store;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.introduce.IntroduceBean;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.WebViewUtil;

@SuppressLint("SetJavaScriptEnabled")
public class StoreNavigatyActivity extends BaseActivity {

    private TextView ab_back;
    private TextView ab_title;
    private WebView mWebView;

    private int storeId;
    private String title;
    private StoreNavigationBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_webpage);
        Intent intent = getIntent();
        storeId = intent.getIntExtra("storeId",-1);
        title = intent.getStringExtra("title");
        findView();
        setListener();
        InitData();
    }

    private void findView() {
        ab_back = (TextView) findViewById(R.id.ab_back);
        ab_title = (TextView) findViewById(R.id.ab_title);
        ab_title.setText(title);

        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setBlockNetworkImage(false);
        webSettings.setBlockNetworkLoads(false);
        mWebView.setBackgroundResource(android.R.color.transparent); // 设置背景色
        mWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
    }

    private void setListener() {
        ab_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void InitData() {
        MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
                true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                bean = StoreNavigationBean.explainJson(content, mContext);
                if (bean != null && bean.type > 0) {
                    RefreshView();
                }
            }

        };

        RequstClient.storeNavigation(handler,storeId);
    }

    protected void RefreshView() {
        mWebView.loadDataWithBaseURL(null,
                WebViewUtil.initWebViewFor19(bean.getStoreNavigation()), "text/html",
                "utf-8", null);
    }
}
