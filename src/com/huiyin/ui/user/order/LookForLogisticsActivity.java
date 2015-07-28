package com.huiyin.ui.user.order;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.LogisticsBean;
import com.huiyin.wight.indicator.TabPageIndicator;

/**
 * 查看物流 Created by lian on 2015/5/27.
 */
public class LookForLogisticsActivity extends BaseActivity {

	private TextView tv_order_no;// 订单编号
	private TextView tv_order_status;// 订单状态
	private RelativeLayout logistics_content;
	private TabPageIndicator logistics_indicator;
	private ViewPager logistics_pager;

	private String orderId;
	private String orderNo;
	private String statu;
	public static String ORDERID = "orderId";
	public static String ORDERNO = "orderNo";
	public static String STATU = "statu";
	private LogisticsBean mBean;
	private List<String> titles;
	String fake = "{\n" + "type: 1,\n" + "msg: \"成功\",\n"
			+ "orderLogistics: [\n" + "{\n"
			+ "DELIVERY_CODE: \"15378717677\",\n" + "COMPANY_NAME: \"飞虎队\",\n"
			+ "context: \"长江中路-大学南路,长江中路-大学南路附近\",\n"
			+ "ftime: \"2015-07-02 10:33:35\"\n" + "},\n" + "{\n"
			+ "COMPANY_NO: \"tiantian\",\n" + "COMPANY_NAME: \"天天快递\",\n"
			+ "DELIVERY_CODE: \"886143084825\",\n" + "data: [\n" + "{\n"
			+ "context: \"已签收,签收人是草签 \",\n"
			+ "time: \"2015-06-11 13:29:38\",\n"
			+ "ftime: \"2015-06-11 13:29:38\",\n" + "status: \"签收\",\n"
			+ "areaCode: \"310000000000\",\n" + "areaName: \"深圳市\"\n" + "},\n"
			+ "{\n" + "context: \"【宝安西乡】的派件员【廖航18123614262】正在派件 \",\n"
			+ "time: \"2015-06-11 09:34:58\",\n"
			+ "ftime: \"2015-06-11 09:34:58\",\n" + "status: \"在途\",\n"
			+ "areaCode: \"310000000000\",\n" + "areaName: \"深圳市\"\n" + "},\n"
			+ "{\n"
			+ "context: \"快件已到达【宝安西乡】 扫描员是【客服075527301600】上一站是【深圳分拨中心】 \",\n"
			+ "time: \"2015-06-11 08:39:14\",\n"
			+ "ftime: \"2015-06-11 08:39:14\",\n" + "status: \"在途\",\n"
			+ "areaCode: \"310000000000\",\n" + "areaName: \"深圳市\"\n" + "},\n"
			+ "{\n" + "context: \"快件已到达【深圳分拨中心】 扫描员是【操作部黎卫华】上一站是【南通分拨中心】 \",\n"
			+ "time: \"2015-06-11 02:37:5\",\n"
			+ "ftime: \"2015-06-11 02:37:5\",\n" + "status: \"在途\",\n"
			+ "areaCode: \"310000000000\",\n" + "areaName: \"深圳市\"\n" + "},\n"
			+ "{\n" + "context: \"由【南通川港】发往【深圳】 \",\n"
			+ "time: \"2015-06-09 21:36:42\",\n"
			+ "ftime: \"2015-06-09 21:36:42\",\n" + "status: \"在途\",\n"
			+ "areaCode: \"310000000000\",\n" + "areaName: \"南通市\"\n" + "},\n"
			+ "{\n" + "context: \"【南通川港】的收件员【南通川港】已收件 \",\n"
			+ "time: \"2015-06-09 19:11:11\",\n"
			+ "ftime: \"2015-06-09 19:11:11\",\n" + "status: \"在途\",\n"
			+ "areaCode: \"310000000000\",\n" + "areaName: \"南通市\"\n" + "}\n"
			+ "]\n" + "}\n" + "],\n" + "curTime: \"2015-07-02 10:34:17\"\n"
			+ "}";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_look_for_logistics);
		orderId = getIntent().getStringExtra(ORDERID);
		orderNo = getIntent().getStringExtra(ORDERNO);
		statu = getIntent().getStringExtra(STATU);
		findView();
		setListener();
		init();
		loadData();
	}

	private void findView() {
		tv_order_no = (TextView) findViewById(R.id.tv_order_no);
		tv_order_status = (TextView) findViewById(R.id.tv_order_status);
		logistics_content = (RelativeLayout) findViewById(R.id.logistics_content);
		logistics_indicator = (TabPageIndicator) findViewById(R.id.logistics_indicator);
		logistics_pager = (ViewPager) findViewById(R.id.logistics_pager);
	}

	private void setListener() {
	}

	private void init() {
		if (orderNo != null && statu != null) {
			tv_order_no.setText(orderNo);
			tv_order_status.setText(statu);
		}
	}

	private void loadData() {
		// praseResult(fake);
		if (orderId != null) {
			RequstClient.orderLogistics(orderId,
					new CustomResponseHandler(this) {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							praseResult(content);
						}
					});
		}
	}

	/**
	 * 解析结果
	 * @param content
	 */
	private void praseResult(String content) {
		mBean = new Gson().fromJson(content, LogisticsBean.class);
		if (mBean.type != 1) {
			UIHelper.showToast(mBean.msg);
		} else {
			
			//解析one two three节点数据
			mBean.setOneTwoThree();
			
			getListTitle();
		}
	}

	/**
	 * 解析出包裹的数量
	 */
	private void getListTitle() {
		if (titles == null) {
			titles = new ArrayList<String>();
		}
		String title = "包裹";
		for (int i = 0; i < mBean.orderLogistics.size(); i++) {
			titles.add(title + "" + (i + 1));
		}
		initAdapter();
	}

	/**
	 * 初始化Viewpager的适配器
	 */
	private void initAdapter() {
		logistics_content.setVisibility(View.VISIBLE);
		logistics_pager.setAdapter(new MyPagerAdapter(
				getSupportFragmentManager()));
		logistics_indicator.setViewPager(logistics_pager);
		if (titles.size() == 1) {
			logistics_indicator.setVisibility(View.GONE);
		}
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return LogisticsFragment.newInstance(mBean.orderLogistics
					.get(position % titles.size()));
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return titles.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return titles.get(position % titles.size());
		}
	}

}
