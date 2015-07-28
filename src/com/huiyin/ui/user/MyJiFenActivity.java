package com.huiyin.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyJiFenActivity extends BaseActivity {

	private final static String TAG = "MyJiFenActivity";
	private XListView mListView;
	private JiFenBean mJiFenBean;
	private JiFenAdapter mJiFenAdapter;
	TextView left_rb, right_rb, middle_title_tv;
	private int page = 1;
	TextView jifen_instructions, jifen_totalnum_tv;
	private int pageSize = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_jifen_layout);

		initView();
		init(true);
		setRightText(View.GONE);
	}

	private void init(final boolean initB) {

		if (AppContext.userId == null) {
			return;
		}
		RequstClient.getPoint(AppContext.userId, page + "",
				new CustomResponseHandler(this) {
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
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "getPoint:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}

							if (page == 1) {
								mJiFenBean = new Gson().fromJson(content,
										JiFenBean.class);
								mJiFenAdapter = new JiFenAdapter(mJiFenBean);
								mListView.setAdapter(mJiFenAdapter);
								jifen_totalnum_tv.setText(MathUtil
										.stringToInt(mJiFenBean.totalIntegral)
										+ "");
								if (mJiFenAdapter.getCount() < pageSize) {
									mListView.setPullLoadEnable(false);
								} else {
									mListView.setPullLoadEnable(true);
								}

							} else {
								JiFenBean jiFenBean = new Gson().fromJson(
										content, JiFenBean.class);
								if (jiFenBean.integralList != null) {
									if (jiFenBean.integralList.size() > 0) {
										mJiFenBean.integralList
												.addAll(jiFenBean.integralList);
										mJiFenAdapter.notifyDataSetChanged();
										if (jiFenBean.integralList.size() < pageSize) {
											mListView.setPullLoadEnable(false);
										} else {
											mListView.setPullLoadEnable(true);
										}
									} else {
										Toast.makeText(MyJiFenActivity.this,
												"已无更多数据！", Toast.LENGTH_LONG)
												.show();
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
		middle_title_tv.setText("我的积分");

		jifen_instructions = (TextView) findViewById(R.id.jifen_instructions);
		jifen_instructions.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent();
				i.setClass(MyJiFenActivity.this, IntroduceActivity.class);
				i.putExtra("id", 2);
				startActivity(i);
			}
		});

		jifen_totalnum_tv = (TextView) findViewById(R.id.jifen_totalnum_tv);
		jifen_totalnum_tv.setText(AppContext.getInstance().mUserInfo.integral);

		mJiFenBean = new JiFenBean();
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

				page = mJiFenAdapter.getCount() / pageSize + 1;
				if (mJiFenAdapter.getCount() % pageSize > 0)
					page++;
				init(false);
			}

		});
	}

	class JiFenAdapter extends BaseAdapter {

		LayoutInflater inflater;
		JiFenBean mJiFenBean;

		public JiFenAdapter(JiFenBean mJiFenBean) {
			this.inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.mJiFenBean = mJiFenBean;
		}

		@Override
		public int getCount() {
			return mJiFenBean.integralList == null ? 0
					: mJiFenBean.integralList.size();
		}

		@Override
		public Object getItem(int position) {
			return mJiFenBean.integralList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mJiFenBean.integralList.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder mHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.my_jifen_item, null);
				mHolder = new ViewHolder();
				mHolder.jifen_image = (ImageView) convertView
						.findViewById(R.id.jifen_image);
				mHolder.jifen_item_title = (TextView) convertView
						.findViewById(R.id.jifen_item_title);
				mHolder.jifen_item_time = (TextView) convertView
						.findViewById(R.id.jifen_item_time);
				mHolder.jifen_value_id = (TextView) convertView
						.findViewById(R.id.jifen_value_id);
				mHolder.jifen_statu_tv = (TextView) convertView
						.findViewById(R.id.jifen_statu_tv);
				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			mHolder.jifen_statu_tv.setVisibility(View.GONE);
			final JiFenBean.JiFenItem item = mJiFenBean.integralList
					.get(position);
			switch (item.INTEGRAL_TYPE) {
			case 1:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_1);
				break;
			case 2:
			case 3:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_3);
				break;
			case 4:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_4);
				break;
			case 5:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_5);
				break;
			case 6:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_6);
				break;
			case 7:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_7);
				break;
			case 8:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_8);
				break;
			case 9:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_9);
				break;
			case 10:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_10);
				break;
			case 11:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_11);
				break;
			case 12:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_12);
				break;
			case 13:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_13);
				break;
			case 14:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_14);
				break;
			case 15:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_15);
				break;
			case 16:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_16);
				break;
			case 17:
				mHolder.jifen_image.setImageResource(R.drawable.integrals_17);
				break;
			default:
				break;
			}
			// if (item.INTEGRAL_TYPE.equals("1") ||
			// item.INTEGRAL_TYPE.equals("2") ||
			// item.INTEGRAL_TYPE.equals("11")) {
			// mHolder.jifen_image.setImageResource(R.drawable.integral_1);
			// } else if (item.INTEGRAL_TYPE.equals("3")) {
			// jifen_statu_tv.setText("评论");
			// jifen_statu_tv.setVisibility(View.VISIBLE);
			// ImageLoader.getInstance().displayImage(URLs.IMAGE_URL +
			// item.COMMODITY_IMAGE_PATH, mHolder.jifen_image);
			// } else if (item.INTEGRAL_TYPE.equals("4")) {
			// mHolder.jifen_image.setImageResource(R.drawable.integral_4);
			// } else if (item.INTEGRAL_TYPE.equals("5")) {
			// mHolder.jifen_image.setImageResource(R.drawable.integral_5);
			// } else if (item.INTEGRAL_TYPE.equals("6")) {
			// mHolder.jifen_image.setImageResource(R.drawable.integral_6);
			// } else if (item.INTEGRAL_TYPE.equals("7")) {
			// mHolder.jifen_image.setImageResource(R.drawable.integral_7);
			// } else if (item.INTEGRAL_TYPE.equals("8")) {
			// mHolder.jifen_image.setImageResource(R.drawable.integral_8);
			// } else if (item.INTEGRAL_TYPE.equals("9")) {
			// mHolder.jifen_image.setImageResource(R.drawable.integral_9);
			// } else if (item.INTEGRAL_TYPE.equals("10")) {
			// mHolder.jifen_image.setImageResource(R.drawable.integral_10);
			// }
			mHolder.jifen_value_id.setText(item.PRICE);
			mHolder.jifen_item_title.setText(item.INTEGRAL_NAME);
			mHolder.jifen_item_time.setText(item.CREATE_TIME);

			return convertView;
		}

	}

	class ViewHolder {
		ImageView jifen_image;
		TextView jifen_item_title;
		TextView jifen_item_time;
		TextView jifen_value_id;
		TextView jifen_statu_tv;
	}

	public class JiFenBean {

		private String totalIntegral;// 积分总数
		public ArrayList<JiFenItem> integralList = new ArrayList<JiFenItem>();

		public class JiFenItem {

			public String PRICE;// 积分
			public String CREATE_TIME;// 时间
			public int INTEGRAL_TYPE;// 积分类型（1购买商品，2评价商品，3积分活动，4，积分抵扣）
			public String INTEGRAL_NAME;// 描述
			public String COMMODITY_IMAGE_PATH; // 图片
		}
	}

}
