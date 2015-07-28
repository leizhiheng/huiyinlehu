// add by zhyao @2015/5/8  添加WAP银行支付页面
package com.huiyin.pay.webview;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.api.URLs;
/**
 * WAP银行支付功能
 * 
 * @author zhyao
 */
public class BankPayActivity extends Activity {
	
	public static final String TAG = "BankPayActivity";
	
	public static final String ORDER_ID = "orderId";
	public static final String ORDER_NUMBER = "orderNumer";
	public static final String ORDER_PRICE = "orderPrice";
	public static final String BANK_TYPE = "bankType";
	
	public static final int REQUEST_CODE = 1000;
	
	public static final String CHINA_BANK = "1";//中行
	public static final String CONSTRUCTION_BANK = "2";//建行

	private WebView mWebView;
	private TextView mLoadingTv;

	private TextView ab_back;
	
	private String orderId;
	private String orderNumber;
	private String orderPrice;
	private String bankType;//1:中国银行  2:建行

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lottery);

		initViews();
		
		initDatas();

		loadWebData();
	}

	public void initViews() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		mLoadingTv = (TextView) findViewById(R.id.tv_loading);
		mWebView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		webSettings.setAllowContentAccess(false);
		webSettings.setLightTouchEnabled(false);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setNeedInitialFocus(true);
	
		mWebView.setWebViewClient(new WebViewClient() {
			// 这个方法在用户试图点开页面上的某个链接时被调用
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url != null) {
					// 如果想继续加载目标页面则调用下面的语句
					Log.d(TAG, "shouldOverrideUrlLoading : url = " + url);
					view.loadUrl(url);// 如果不想那url就是目标网址，如果想获取目标网页的内容那你可以用HTTP的API把网页扒下来。
				}
				// 返回true表示停留在本WebView（不跳转到系统的浏览器）
				return true;
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				Log.d(TAG, "onPageStarted : url = " + url);
				super.onPageStarted(view, url, favicon);
				//建行支付返回,不展示WAP的回调页面，直接返回APP
				if (url != null && url.startsWith(URLs.BANK_WAP_CONSTRUCTION_CALLBACK_URL)) {
					//支付完成立刻返回到
					//finishActvitiy();
					mWebView.setVisibility(View.GONE);
					mLoadingTv.setVisibility(View.VISIBLE);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							//支付完成展示WAP支付结果结果，1S之后跳转会原生支付结果页面
							finishActvitiy();
						}
					}, 1000);
				}
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.d(TAG, "onPageFinished : url = " + url);
				//中行支付返回
				if (url != null && url.startsWith(URLs.BANK_WAP_CHAIN_CALLBACK_URL)) {
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							//支付完成展示WAP支付结果结果，1S之后跳转会原生支付结果页面
							finishActvitiy();
						}
					}, 1000);
				}
			}
		
		});

	}
	
	private void initDatas() {
		orderId = getIntent().getStringExtra(ORDER_ID);
		orderNumber = getIntent().getStringExtra(ORDER_NUMBER);
		orderPrice = getIntent().getStringExtra(ORDER_PRICE);
		bankType = getIntent().getStringExtra(BANK_TYPE);
	}
	
	@Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	  if (keyCode == KeyEvent.KEYCODE_BACK) {
		  finishActvitiy();
	      return true;
	  }
	  return super.onKeyDown(keyCode, event);
	}
	
	private void finishActvitiy() {
		Intent intent = new Intent();
	    intent.putExtra(BANK_TYPE, bankType);
	    setResult(RESULT_OK, intent);
	    finish();
	}

	private void loadWebData() {
		//Log.d(TAG, "loadUrl = " + String.format(URLs.BANK_WAP_PAY_URL, orderPrice, orderNumber, bankType));
		mWebView.loadUrl(String.format(URLs.BANK_WAP_PAY_URL, orderPrice, orderNumber, bankType));
		
	}

}
