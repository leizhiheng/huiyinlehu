package com.huiyin.ui.fillorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.huiyin.R;
import com.huiyin.adapter.OrderGoodsListAdapter;
import com.huiyin.bean.ShoppingCarDataBaseBeanNew;
import com.huiyin.bean.ShoppingCarStoreBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by justme on 15/5/31. 商品清单
 */
public class FillOrderGoodsListActivity extends Activity {

	public final static String GOODS_LIST = "goods_list"; // 商品列表数据
	public final static String REMARK = "remark"; // 商品列表数据

	private TextView ab_back;
	private TextView ab_title;
	private TextView goodsInfo;
	private ListView mListview;

	private Context mContext;
	private ArrayList<ShoppingCarStoreBean> cart;
	private OrderGoodsListAdapter mAdapter;
	private String remark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goods_list_layout);
		mContext = this;

		findView();
		setListener();
		initData();
	}

	private void findView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("商品清单");

		goodsInfo = (TextView) findViewById(R.id.goods_info);
		mListview = (ListView) findViewById(R.id.m_listview);

	}

	private void setListener() {
		ab_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		Intent intent = getIntent();
		ShoppingCarDataBaseBeanNew.ShoppingCarDataReturnMap objectses =
                (ShoppingCarDataBaseBeanNew.ShoppingCarDataReturnMap) intent
				.getSerializableExtra(GOODS_LIST);
		cart =objectses.getCart();
		remark = intent.getStringExtra(REMARK);
		List<OrderGoodsListAdapter.RemarksJson> mRemarksJsons = null;
		if (!TextUtils.isEmpty(remark)) {
			mRemarksJsons = jsonToList(remark,
					OrderGoodsListAdapter.RemarksJson.class);
		}
		mAdapter = new OrderGoodsListAdapter(mContext, cart, mRemarksJsons);
		mListview.setAdapter(mAdapter);

		goodsInfo.setVisibility(cart.size() > 1 ? View.VISIBLE : View.GONE);
	}

	@Override
	public void finish() {
		Intent intent = new Intent();
		intent.putExtra("remarksJson", mAdapter.getTheResult());
		setResult(RESULT_OK, intent);
		super.finish();
	}

	static <T> ArrayList<T> jsonToList(String json, Class<T> classOfT) {
		Type type = new TypeToken<ArrayList<JsonObject>>() {
		}.getType();
		ArrayList<JsonObject> jsonObjs = new Gson().fromJson(json, type);
		ArrayList<T> listOfT = new ArrayList<T>();
		for (JsonObject jsonObj : jsonObjs) {
			listOfT.add(new Gson().fromJson(jsonObj, classOfT));
		}

		return listOfT;
	}
}
