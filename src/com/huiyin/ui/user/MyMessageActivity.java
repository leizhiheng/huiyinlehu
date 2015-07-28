package com.huiyin.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.MessageAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.SysMessageList;
import com.huiyin.bean.SysMessageList.SysMessage;
import com.huiyin.ui.MainActivity;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

/**
 * 消息中心
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-22
 */
public class MyMessageActivity extends BaseActivity {
	
	public final static String TAG = "MyMessageActivity";
	TextView left_ib,middle_title_tv ;
	
	private XListView mListView;
	private MessageAdapter messageAdapter;
	
	private List<SysMessage> messages = new ArrayList<SysMessage>();
	private int page = 1;
	private int pageNum = 1;
	private Boolean initType = true;
	
	String pushFlag;
	String letterId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mymessage_layout);
		Log.d(TAG, "MyMessageActivity.onCreate");
		initView();
		pushFlag = getIntent().getStringExtra("pushFlag") == null ? "0" : getIntent().getStringExtra("pushFlag");
		getCollectList();
	}
	
	private void initView(){
		middle_title_tv = (TextView)findViewById(R.id.ab_title);
		middle_title_tv.setText("消息中心");
		
		left_ib = (TextView)findViewById(R.id.ab_back);
		left_ib.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(pushFlag.equals("1")){
					Intent i = new Intent();
					i.setClass(getApplicationContext(), MainActivity.class);
					startActivity(i);
				}else{
					finish();
				}
			}
		});
		
		mListView = (XListView)findViewById(R.id.xlistview);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(new IXListViewListener(){
			@Override
			public void onRefresh() {
				initType = true;
				page = 1;
				getCollectList();
			}
			@Override
			public void onLoadMore() {
//				if(initType){
//					page++;
//					getCollectList();
//				}
				if(page >= pageNum){
					mListView.hideFooter();
					Toast.makeText(MyMessageActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
				}else {
					page++;
					getCollectList();
				}
			}
		});
		
	}
	/***
	 * 获取系统消息
	 */
	private void getCollectList(){
		Log.d(TAG, "AppContext.userId is null:"+(AppContext.userId == null));
		if (AppContext.userId==null) {
			return;
		}
		RequstClient.getSystemMsg(AppContext.userId, page +"", new CustomResponseHandler(this) {
			@Override
			public void onFinish() {
				super.onFinish();
				mListView.stopLoadMore();
				mListView.stopRefresh();
			}
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				if(isSuccessResponse(content)){
					SysMessageList sysMessageList = JSONParseUtils.parseSysMessageList(content);
					
					pageNum = sysMessageList.totalPageNum;
					messages = sysMessageList.letterList;
					Log.d(TAG,"解析系统消息完毕,messages.size = "+messages.size());
					if(messages != null && messages.size() > 0){
						if(page == 1){
							if(messageAdapter == null){
								messageAdapter = new MessageAdapter(MyMessageActivity.this);
								mListView.setAdapter(messageAdapter);
							}
							messageAdapter.refresh(messages);
						}else{
							messageAdapter.add(messages);
						}
						if(messages.size() < 10){
							mListView.hideFooter();
						}else{
							mListView.showFooter();
						}
					}else{
						initType = false;
						mListView.hideFooter();
						Toast.makeText(MyMessageActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
					}
				}
			}

		});
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(pushFlag.equals("1")){
				Intent i = new Intent();
				i.setClass(getApplicationContext(), MainActivity.class);
				startActivity(i);
			}else{
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
}
