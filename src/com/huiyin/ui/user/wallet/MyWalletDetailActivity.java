package com.huiyin.ui.user.wallet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.wight.XListView;

import java.util.HashMap;
import java.util.List;

public class MyWalletDetailActivity extends BaseActivity {

	private android.widget.TextView mywalletdetailtip;
	private android.widget.TextView mywalletdetailtext;
	private com.huiyin.wight.XListView mywalletdetaillistview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_wallet_detail);
		setTitle("我的钱包");
		setRightText(View.GONE);
	}

	@Override
	protected void onFindViews() {
		this.mywalletdetaillistview = (XListView) findViewById(R.id.my_wallet_detail_listview);
		this.mywalletdetailtext = (TextView) findViewById(R.id.my_wallet_detail_text);
		this.mywalletdetailtip = (TextView) findViewById(R.id.my_wallet_detail_tip);
	}

	@Override
	protected void onLoadData() {
	}

	@Override
	protected void onSetListener() {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_my_wallet_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	class DetailAdapter extends BaseAdapter {
		private Context mContext;
		private List<HashMap<String, String>> mMapList;

		DetailAdapter(Context mContext, List<HashMap<String, String>> mMapList) {
			this.mContext = mContext;
			this.mMapList = mMapList;
		}

		@Override
		public int getCount() {
			return mMapList.size();
		}

		@Override
		public Object getItem(int i) {
			return mMapList.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			Viewholder mViewholder;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.my_wallet_detail_item, viewGroup, false);
				mViewholder = new Viewholder();
				mViewholder.my_wallet_detail_item_time = (TextView) view
						.findViewById(R.id.my_wallet_detail_item_time);
				mViewholder.my_wallet_detail_item_content = (TextView) view
						.findViewById(R.id.my_wallet_detail_item_content);
				mViewholder.my_wallet_detail_item_money = (TextView) view
						.findViewById(R.id.my_wallet_detail_item_money);
			}else{
               mViewholder= (Viewholder) view.getTag();
            }
			return view;
		}
	}

	class Viewholder {
		TextView my_wallet_detail_item_time;
		TextView my_wallet_detail_item_content;
		TextView my_wallet_detail_item_money;
	}
}
