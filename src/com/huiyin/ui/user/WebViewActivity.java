package com.huiyin.ui.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;

public class WebViewActivity extends BaseActivity {
	
	/**访问的Url**/
	public static final String URL = "URL";
	
	/**WebView控件**/
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_webpage);
		setTitle("");
		
		initView();

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {

		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		mWebView.setBackgroundResource(android.R.color.transparent); // 设置背景色
		mWebView.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255

		Intent intent = getIntent();
		if(null != intent && intent.hasExtra(URL)){
			String url = intent.getStringExtra(URL);
			mWebView.loadUrl(url);
		}
	}
}
