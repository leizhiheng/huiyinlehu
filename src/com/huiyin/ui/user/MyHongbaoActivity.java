// add by zhyao @2015/5/8  添加红包明细
package com.huiyin.ui.user;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.introduce.IntroduceActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;
/**
 * 红包明细
 * @author zhyao
 */
public class MyHongbaoActivity extends BaseActivity {

	private final static String TAG = "MyHongbaoActivity";
	private XListView mListView;
	private HongbaoBean mHongbaoBean;
	private HongbaoAdapter mHongbaoAdapter;
	TextView left_rb, right_rb, middle_title_tv;
	private int page = 1;
	TextView hongbao_instructions, hongbao_totalnum_tv;
	private int pageSize = 10;
	
	private EditText phone_et;
	private EditText hongbao_et;
	private Button give_btn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_hongbao_layout);

		initView();
		init(true);

	}

	private void init(final boolean initB) {

		if (AppContext.userId == null) {
			return;
		}
		RequstClient.getHongbao(AppContext.userId, page + "", new CustomResponseHandler(this) {
			@Override
			public void onStart() {
				if (initB)
					super.onStart();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				mListView.stopLoadMore();
				mListView.stopRefresh();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "getPoint:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}

					if (page == 1) {
						mHongbaoBean = new Gson().fromJson(content, HongbaoBean.class);
						mHongbaoAdapter = new HongbaoAdapter(mHongbaoBean);
						mListView.setAdapter(mHongbaoAdapter);
						hongbao_totalnum_tv.setText(MathUtil.priceForAppWithSign(mHongbaoBean.totalBonus));
						if (mHongbaoAdapter.getCount() < pageSize) {
							mListView.setPullLoadEnable(false);
						} else {
							mListView.setPullLoadEnable(true);
						}

					} else {
						HongbaoBean hongbaoBean = new Gson().fromJson(content, HongbaoBean.class);
						if (hongbaoBean.bonusList != null) {
							if (hongbaoBean.bonusList.size() > 0) {
								mHongbaoBean.bonusList.addAll(hongbaoBean.bonusList);
								mHongbaoAdapter.notifyDataSetChanged();
								if (hongbaoBean.bonusList.size() < pageSize) {
									mListView.setPullLoadEnable(false);
								} else {
									mListView.setPullLoadEnable(true);
								}
							} else {
								Toast.makeText(MyHongbaoActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
								mListView.setPullLoadEnable(false);
							}
						}
						mListView.stopLoadMore();
						mListView.stopRefresh();

					}

				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	private void initView() {

		left_rb = (TextView) findViewById(R.id.left_ib);
		right_rb = (TextView) findViewById(R.id.right_ib);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("我的红包");

		hongbao_instructions = (TextView) findViewById(R.id.hongbao_instructions);
		hongbao_instructions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(MyHongbaoActivity.this, IntroduceActivity.class);
				i.putExtra("id", 2);
				startActivity(i);
			}
		});

		hongbao_totalnum_tv = (TextView) findViewById(R.id.hongbao_totalnum_tv);
		hongbao_totalnum_tv.setText(MathUtil.priceForAppWithSign(AppContext.mUserInfo.bonus));

		mHongbaoBean = new HongbaoBean();
		mListView = (XListView) findViewById(R.id.xlist_jifen);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				page = 1;
				init(true);

			}

			@Override
			public void onLoadMore() {

				page = mHongbaoAdapter.getCount() / pageSize + 1;
				if (mHongbaoAdapter.getCount() % pageSize > 0)
					page++;
				init(false);
			}

		});
		
		phone_et = (EditText) findViewById(R.id.phone_et);
		hongbao_et = (EditText) findViewById(R.id.hongbao_et);
		give_btn = (Button) findViewById(R.id.give_btn);
		give_btn.setOnClickListener(new giveHongbaoListener());
	}
	
	class giveHongbaoListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			String givePhone = phone_et.getText().toString();
			String hongbao = hongbao_et.getText().toString();
			
			//验证电话
			if(TextUtils.isEmpty(givePhone)) {
				Toast.makeText(getBaseContext(), "请输入对方手机号", Toast.LENGTH_SHORT).show();
				return;
			}
			if(!StringUtils.isPhoneNumber(givePhone)) {
				Toast.makeText(getBaseContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
				return;
			}
			//验证红包
			if(TextUtils.isEmpty(hongbao)) {
				Toast.makeText(getBaseContext(), "请输入大于0的红包金额！", Toast.LENGTH_SHORT).show();
				return;
			}
			int hongbaoValue = Integer.parseInt(hongbao);
			if(hongbaoValue <= 0) {
				Toast.makeText(getBaseContext(), "请输入大于0的红包金额！", Toast.LENGTH_SHORT).show();
				return;
			}
			// moify by zhyao @2015/6/26 红包总金额double型取整
			if(hongbaoValue > (int)Double.parseDouble(mHongbaoBean.totalBonus)) {
				Toast.makeText(getBaseContext(), "您当前最多可赠送" + (int)Double.parseDouble(mHongbaoBean.totalBonus) + "红包", Toast.LENGTH_SHORT).show();
				return;
			}
			
			RequstClient.userGiveBonus(AppContext.userId, AppContext.mUserInfo.phone, null, givePhone, hongbao, null, null, new CustomResponseHandler(MyHongbaoActivity.this){
				@Override
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
					try {
						JSONObject obj = new JSONObject(content);
						if (!obj.getString("type").equals("1")) {
							String errorMsg = obj.getString("msg");
							Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
							return;
						}
						else {
							Toast.makeText(MyHongbaoActivity.this, "赠送成功!", Toast.LENGTH_LONG).show();
							page = 1;
							init(true);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
	}

	class HongbaoAdapter extends BaseAdapter {

		LayoutInflater inflater;
		HongbaoBean mHongbaoBean;

		public HongbaoAdapter(HongbaoBean mJiFenBean) {
			this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mHongbaoBean = mJiFenBean;
		}

		@Override
		public int getCount() {
			return mHongbaoBean.bonusList == null ? 0 : mHongbaoBean.bonusList.size();
		}

		@Override
		public Object getItem(int position) {
			return mHongbaoBean.bonusList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mHongbaoBean.bonusList.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final HongbaoBean.BonusItem item = mHongbaoBean.bonusList.get(position);
			convertView = inflater.inflate(R.layout.my_hongbao_item, null);

			TextView hongbao_item_source = (TextView) convertView.findViewById(R.id.hongbao_item_source);
			TextView hongbao_item_title = (TextView) convertView.findViewById(R.id.hongbao_item_title);
			TextView hongbao_item_time = (TextView) convertView.findViewById(R.id.hongbao_item_time);
			TextView hongbao_value_id = (TextView) convertView.findViewById(R.id.hongbao_value_id);

			hongbao_value_id.setText(item.BONUS);
			hongbao_item_title.setText(item.DESCRIPTION);
			hongbao_item_time.setText(item.CREATE_TIME);
			
			switch (Integer.parseInt(item.SOURCE)) {
			case 1:
				hongbao_item_source.setText("后台操作");
				break;
			case 2:
				hongbao_item_source.setText("活动充值 ");
				break;
			case 3:
				hongbao_item_source.setText("订单消费");
				break;
			case 4:
				hongbao_item_source.setText("红包赠送");
				break;
			case 5:
				hongbao_item_source.setText("接收红包");
				break;
			default:
				break;
			}

			return convertView;
		}

	}

	public class HongbaoBean {

		private String totalBonus;// 红包总数
		public ArrayList<BonusItem> bonusList = new ArrayList<BonusItem>();

		public class BonusItem {
			
			public String DESCRIPTION;//描述
			public String ADMIN_NAME;//管理员名称
			public String NUM;//
			public String BONUS;//金额（自带+-符号）
			public String SOURCE;//来源（1后台操作 2活动充值 3订单消费）
			public String ID;//主键
			public String USER_NAME;//用户名称
			public String CREATE_TIME;//操作时间
			public String CREATETIME;//操作时间
			public String TYPE;//类型（1增加 2减少）
		}
	}

}
