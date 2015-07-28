package com.huiyin.ui.user.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.servicecard.BindServiceCard;
import com.huiyin.ui.servicecard.ServiceCardActivity;
import com.huiyin.ui.user.MyHongbaoActivity;
import com.huiyin.ui.user.MyJiFenActivity;
import com.huiyin.ui.user.asset.LehuRedPacketActivity;

public class MyWalletActivity extends BaseActivity implements
		View.OnClickListener {
    private Context mContext=MyWalletActivity.this;
	private android.widget.TextView mywalletbalancetext;
	private android.widget.RelativeLayout mywalletbalance;
	private android.widget.TextView mywallethuquantext;
	private android.widget.RelativeLayout mywallethuquan;
	private android.widget.TextView mywalletintegraltext;
	private android.widget.RelativeLayout mywalletintegral;
	private android.widget.TextView mywalletcardtext;
	private android.widget.RelativeLayout mywalletcard;

	public static final String REDPACK = "balance";
	public static final String QUAN = "quan";
	public static final String INTEGRAL = "integral";
	public static final String CARD = "card";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wallet);
		setTitle("我的资产");
		setRightText(View.GONE);
		initText(getIntent().getStringExtra(REDPACK), getIntent()
				.getStringExtra(QUAN), getIntent().getStringExtra(INTEGRAL),
				getIntent().getStringExtra(CARD));
	}

	private void initText(String balance, String huquanNum, String integral,
			String cardPrice) {
		mywalletbalancetext.setText(balance==null?"":("¥" + balance));
		mywallethuquantext.setText(huquanNum==null?"":(huquanNum + "张"));
		mywalletintegraltext.setText(integral==null?"":integral);
		mywalletcardtext.setText(cardPrice==null?"":("¥" + cardPrice));
	}

	@Override
	protected void onFindViews() {
		this.mywalletcard = (RelativeLayout) findViewById(R.id.my_wallet_card);
		this.mywalletcardtext = (TextView) findViewById(R.id.my_wallet_card_text);
		this.mywalletintegral = (RelativeLayout) findViewById(R.id.my_wallet_integral);
		this.mywalletintegraltext = (TextView) findViewById(R.id.my_wallet_integral_text);
		this.mywallethuquan = (RelativeLayout) findViewById(R.id.my_wallet_huquan);
		this.mywallethuquantext = (TextView) findViewById(R.id.my_wallet_huquan_text);
		this.mywalletbalance = (RelativeLayout) findViewById(R.id.my_wallet_balance);
		this.mywalletbalancetext = (TextView) findViewById(R.id.my_wallet_balance_text);
	}

	@Override
	protected void onSetListener() {
		mywalletcard.setOnClickListener(this);
		mywalletintegral.setOnClickListener(this);
		mywallethuquan.setOnClickListener(this);
		mywalletbalance.setOnClickListener(this);
	}

	@Override
	protected void onLoadData() {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_my_wallet, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.my_wallet_card:
			
			//便民生活服务卡
            Intent fuwu_intent = new Intent();
            if (AppContext.getInstance().mUserInfo.getBDStatus() == 1) {
                fuwu_intent.setClass(mContext, ServiceCardActivity.class);
            } else {
                fuwu_intent.setClass(mContext, BindServiceCard.class);
            }
            startActivity(fuwu_intent);
			break;
		case R.id.my_wallet_integral:
			
			//积分
            Intent jf_intent = new Intent();
            jf_intent.setClass(mContext, MyJiFenActivity.class);
            startActivity(jf_intent);
            
			break;
		case R.id.my_wallet_huquan:
			
			//乐虎券
			Intent intentRed = new Intent();
			intentRed.setClass(mContext, LehuRedPacketActivity.class);
			startActivity(intentRed);
			
			break;
		case R.id.my_wallet_balance:
			
			//红包
			Intent hongbao_intent = new Intent();
			hongbao_intent.setClass(mContext, MyHongbaoActivity.class);
			startActivity(hongbao_intent);
			
			break;
		}
	}
}
