package com.huiyin.ui.classic;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.R;
import com.huiyin.adapter.ClassSortListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ProductListBean;
import com.huiyin.ui.home.SiteSearchActivity;
import com.huiyin.utils.Utils;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class CategorySearchActivity extends BaseActivity implements OnClickListener {
	protected static final String TAG = "ClassSortList1Activity";

	public static final String BUNDLE_KEY_CATEGORY_ID = "category_id";
	public static final String BUNDLE_KEY_WORD = "key_word";
	public static final String BUNDLE_MARK = "mark";
	public static final String BUNDLE_BRAND_ID = "BRAND_ID";
	public static final String BUNDLE_PROPERTY_ID = "propertyId";
	public static final String BUNDLE_STORE_ID = "storeId";


	private XListView class_sort_list1_xv;
	private ClassSortListAdapter adapter;
	private ImageView ab_filter;
	private ImageView ab_back;
	private ImageView change_view;
	private TextView ab_search;
	private TextView class_sort_list1_xiaoliang;
	private TextView class_sort_list1_pingjia;
	private TextView class_sort_list1_renqi;
	private ImageView class_sort_list1_jiage_up;
	private ImageView class_sort_list1_jiage_down;
	private ProductListBean bean;
	private int pageSize = 10;
	private int pageIndex = 1;
	private boolean isTable = false;//切换大图模式的标志位
	private String category_id;
	private int flag;
	private int sort;
	private LinearLayout class_sort_list1_jiage_ll;
	private boolean sortFlag = true;
	private TextView class_sort_list1_jiage;
	private int pageNumber;

	private TextView class_sort_tv;

	private TextView yema;

	private String key_word;// 关键字
	private String mark;// 4表示，是通过筛选
	//	private String two_category_id;// 二级分类id
	private String propertyId;// 属性id,多个属性以逗号隔开
	private String BRAND_ID;// 品牌id
	private String storeId;//店铺id
//	private String priceStart;// 价格起始值
//	private String priceEnd;// 价格结束值

	// private boolean showType = true; //展示类型 true为列表 false为网格
	private int currentSelectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_sort_list1);
		// 获取请求的id
		category_id = getIntent().getStringExtra(BUNDLE_KEY_CATEGORY_ID);
		Log.i(TAG, "=====" + category_id + "===========");
		key_word = getIntent().getStringExtra(BUNDLE_KEY_WORD);
		mark = getIntent().getStringExtra(BUNDLE_MARK);
//		two_category_id = getIntent().getStringExtra(BUNDLE_TWO_CATEGORY_ID);
		BRAND_ID = getIntent().getStringExtra(BUNDLE_BRAND_ID);
		propertyId = getIntent().getStringExtra(BUNDLE_PROPERTY_ID);
		storeId = getIntent().getStringExtra(BUNDLE_STORE_ID);
//		priceStart = getIntent().getStringExtra(BUNDLE_PRICE_START);
//		priceEnd = getIntent().getStringExtra(BUNDLE_PRICE_END);
		Log.i(TAG, "--category_id--" + category_id);
		Log.i(TAG, "--key_word--" + key_word);
		Log.i(TAG, "--mark--" + mark);
//		Log.i(TAG, "--two_category_id--" + two_category_id);
		Log.i(TAG, "--BRAND_ID--" + BRAND_ID);
		Log.i(TAG, "--propertyId--" + propertyId);
		Log.i(TAG, "--storeId--" + storeId);
//		Log.i(TAG, "--priceStart--" + priceStart);
//		Log.i(TAG, "--priceEnd--" + priceEnd);
		initView();

		initData();

		// 默认按销量降序排列(请求第一页)
		getDate(flag + "", pageIndex + "", sort + "", true);
	}

	private void initData() {
		flag = 1;
		sort = 2;
	}

	private void initView() {

		class_sort_list1_jiage = (TextView) findViewById(R.id.class_sort_list1_jiage);

		class_sort_list1_jiage_ll = (LinearLayout) findViewById(R.id.class_sort_list1_jiage_l1);
		class_sort_list1_jiage_ll.setOnClickListener(this);
		class_sort_list1_jiage_up = (ImageView) findViewById(R.id.class_sort_list1_jiage_up);
		class_sort_list1_jiage_up.setOnClickListener(this);
		class_sort_list1_jiage_down = (ImageView) findViewById(R.id.class_sort_list1_jiage_down);
		class_sort_list1_jiage_down.setOnClickListener(this);

		ab_back = (ImageView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(this);
		ab_search = (TextView) findViewById(R.id.ab_search);
		class_sort_list1_xiaoliang = (TextView) findViewById(R.id.class_sort_list1_xiaoliang);
		class_sort_list1_xiaoliang.setOnClickListener(this);
		class_sort_list1_pingjia = (TextView) findViewById(R.id.class_sort_list1_pingjia);
		class_sort_list1_pingjia.setOnClickListener(this);
		class_sort_list1_renqi = (TextView) findViewById(R.id.class_sort_list1_renqi);
		class_sort_list1_renqi.setOnClickListener(this);
		ab_filter = (ImageView) findViewById(R.id.ab_filter);
		ab_filter.setOnClickListener(this);
		ab_search.setOnClickListener(this);
		ab_search.setText(key_word);
		change_view = (ImageView) findViewById(R.id.change_view);
		change_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(adapter!=null) {
					if(isTable) {
						change_view.setImageResource(R.drawable.icon_liebiao_2);
					} else {
						change_view.setImageResource(R.drawable.icon_liebiao_1);
					}
					isTable = !isTable;
					adapter.ChangeView(isTable);
				}
			}
		});
		yema = (TextView) findViewById(R.id.class_sort_list1_yema);
		class_sort_tv = (TextView) findViewById(R.id.class_sort_tv);

		class_sort_list1_xv = (XListView) findViewById(R.id.class_sort_list1_xv);
		class_sort_list1_xv.setPullLoadEnable(true);
		class_sort_list1_xv.setPullRefreshEnable(true);
		class_sort_list1_xv.hideFooter();
		// 上次更新的时间
		class_sort_list1_xv.setRefreshTime(Utils.getCurrTiem());

		class_sort_list1_xv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageIndex = 1;
				getDate(flag + "", pageIndex + "", sort + "", true);
			}

			@Override
			public void onLoadMore() {
				int count = bean.goodsList == null ? 0 : bean.goodsList.size();
				pageIndex = count / pageSize + 1;
				if (count % pageSize > 0) {
					pageIndex++;
				}
				getDate(flag + "", pageIndex + "", sort + "", true);
			}
		});

//		class_sort_list1_xv.setOnItemClickListener(new MyOnItemClickListener());

		class_sort_list1_xv.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				switch (scrollState) {
					// 正在滑动
					case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
						if (pageNumber == 1 || pageNumber == 0 || pageIndex >= pageNumber) {
							yema.setVisibility(View.GONE);
						}
						int count = bean.goodsList == null ? 0 : bean.goodsList.size();
						if (count <= 10) {
							yema.setVisibility(View.GONE);
						} else {
							if (pageIndex >= pageNumber) {
								pageIndex = pageIndex - 1;
							}
							yema.setText(pageIndex + "/" + pageNumber);
							yema.setVisibility(View.VISIBLE);
						}
						Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.jianxiandonghua);
						animation.setFillAfter(true);
						yema.setAnimation(animation);
						break;

					default:
						break;
				}
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {

			}
		});

		class_sort_list1_xv.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {

				switch (event.getAction()) {
					// 抬起时
					case MotionEvent.ACTION_UP:
						yema.setVisibility(View.GONE);
						Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.jianyindonghua);
						animation.setFillAfter(true);// 保持最后的渐变状态
						yema.setAnimation(animation);
						break;

					default:
						break;
				}

				return false;
			}
		});
		bean = new ProductListBean();
		// 初始化数据
		initDate();
		if ((null != key_word && !key_word.equals(""))||(null != mark && mark.equals("5"))) {// 通过关键字搜索或者店铺来的,隐藏提交按钮
			ab_filter.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 将编辑框的光标移到文本末
	 *
	 * @param editText
	 */
	public void moveEditCursorToEnd(EditText editText) {
		CharSequence text = editText.getText();
		if (text instanceof Spannable) {
			Spannable spannable = (Spannable) text;
			Selection.setSelection(spannable, text.length());
		}
	}

	private void initDate() {
		class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.index_red));
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
			// 销量
			case R.id.class_sort_list1_xiaoliang:
				if(currentSelectId!=R.id.class_sort_list1_xiaoliang){
					currentSelectId=R.id.class_sort_list1_xiaoliang;
					flag = 1;
					pageIndex = 1;
					sort = 2;
					getDate(flag + "", pageIndex + "", sort + "", false);
					class_sort_list1_pingjia.setTextColor(getResources().getColor(R.color.black));
					class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.index_red));
					class_sort_list1_renqi.setTextColor(getResources().getColor(R.color.black));
					class_sort_list1_jiage.setTextColor(getResources().getColor(R.color.black));
				}
				break;
			// 价格
			case R.id.class_sort_list1_jiage_l1:
				currentSelectId = R.id.class_sort_list1_jiage_l1;
				pageIndex = 1;
				if (sortFlag) {
					class_sort_list1_jiage_up.setVisibility(View.VISIBLE);
					class_sort_list1_jiage_down.setVisibility(View.GONE);
					flag = 2;
					sort = 1;
				} else {
					class_sort_list1_jiage_up.setVisibility(View.GONE);
					class_sort_list1_jiage_down.setVisibility(View.VISIBLE);
					flag = 2;
					sort = 2;
				}
				getDate(flag + "", pageIndex + "", sort + "", false);
				sortFlag = !sortFlag;
				class_sort_list1_pingjia.setTextColor(getResources().getColor(R.color.black));
				class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.black));
				class_sort_list1_renqi.setTextColor(getResources().getColor(R.color.black));
				class_sort_list1_jiage.setTextColor(getResources().getColor(R.color.index_red));
				break;

			// 评价
			case R.id.class_sort_list1_pingjia:
				if(currentSelectId!=R.id.class_sort_list1_pingjia) {
					currentSelectId = R.id.class_sort_list1_pingjia;
					flag = 3;
					sort = 2;
					pageIndex = 1;
					getDate(flag + "", pageIndex + "", sort + "", false);
					class_sort_list1_pingjia.setTextColor(getResources().getColor(R.color.index_red));
					class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.black));
					class_sort_list1_renqi.setTextColor(getResources().getColor(R.color.black));
					class_sort_list1_jiage.setTextColor(getResources().getColor(R.color.black));
				}
				break;
			// 人气
			case R.id.class_sort_list1_renqi:
				if(currentSelectId!=R.id.class_sort_list1_renqi) {
					currentSelectId = R.id.class_sort_list1_renqi;
					flag = 4;
					sort = 2;
					pageIndex = 1;
					getDate(flag + "", pageIndex + "", sort + "", false);
					class_sort_list1_pingjia.setTextColor(getResources().getColor(R.color.black));
					class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.black));
					class_sort_list1_renqi.setTextColor(getResources().getColor(R.color.index_red));
					class_sort_list1_jiage.setTextColor(getResources().getColor(R.color.black));
				}
				break;
			case R.id.ab_back:
				CategorySearchActivity.this.finish();
				break;
			case R.id.ab_search:
				Intent i = new Intent(mContext, SiteSearchActivity.class);
				if(!TextUtils.isEmpty(storeId)) {
					i.putExtra("storeId",storeId);
				}
				startActivity(i);
				this.finish();
				break;
			// 筛选
			case R.id.ab_filter:
//				Intent intent = new Intent(mContext, SortListSelection.class);
				Intent intent = new Intent(mContext, AttributeSelectionActivity.class);
				intent.putExtra("category_id", category_id);
				startActivity(intent);
				break;
			default:
				break;
		}
	}

	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			Intent intent = new Intent(mContext, ProductsDetailActivity.class);
			String goods_detail_id = bean.goodsList.get(arg2 - 1).GOODS_ID;
			String STORE_ID=bean.goodsList.get(arg2 - 1).STORE_ID;//店铺id
			String GOODS_NO=bean.goodsList.get(arg2 - 1).GOODS_NO;//商品编号
			//商品编号
			if (goods_detail_id.equals("")) {
				return;
			}
			intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, goods_detail_id);
			intent.putExtra(ProductsDetailActivity.STORE_ID, STORE_ID);
			intent.putExtra(ProductsDetailActivity.GOODS_NO, GOODS_NO);
			startActivity(intent);
		}
	}

	// 获取网络数据的方法
	private void getDate(String sales_volume, final String page, String sort, final boolean bool) {
		RequstClient.getFourClassList(sales_volume, "10", page, category_id, propertyId, BRAND_ID,  mark, key_word,  sort,storeId,
				new CustomResponseHandler(this) {

					@Override
					public void onStart() {

						if (bool)
							super.onStart();
					}

					@Override
					public void onFinish() {

						super.onFinish();
						class_sort_list1_xv.stopLoadMore();
						class_sort_list1_xv.stopRefresh();
					}

					@Override
					public void onFailure(String error, String errorMessage) {

						super.onFailure(error, errorMessage);

						if (error.equals(SERVICE_TIMEOUT)) {
							Toast.makeText(mContext, error, Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(mContext, "加载失败", Toast.LENGTH_LONG).show();
						}

					}

					@Override
					public void onSuccess(int statusCode, Header[] headers, String content) {

						super.onSuccess(statusCode, headers, content);

						try {

							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
								return;
							}

							// 总页码数
							String totalPageNum = obj.getString("totalPageNum");
							pageNumber = Integer.parseInt(totalPageNum);

							if (pageIndex == 1) {
								bean = new Gson().fromJson(content, ProductListBean.class);

								adapter = new ClassSortListAdapter(bean, mContext,isTable);
								class_sort_list1_xv.setAdapter(adapter);

								int count = bean.goodsList == null ? 0 : bean.goodsList.size();
								if (count < pageSize) {
									class_sort_list1_xv.hideFooter();
								} else {
									class_sort_list1_xv.showFooter();
								}

								if (count == 0) {
									class_sort_list1_xv.setVisibility(View.GONE);
									class_sort_tv.setVisibility(View.VISIBLE);
								} else {
									class_sort_list1_xv.setVisibility(View.VISIBLE);
									class_sort_tv.setVisibility(View.GONE);
								}
							} else {

								ProductListBean tBean = new Gson().fromJson(content, ProductListBean.class);
								if (tBean.goodsList != null) {
									if (tBean.goodsList.size() > 0) {
										bean.goodsList.addAll(tBean.goodsList);
										adapter.notifyDataSetChanged();
									} else {
										Toast.makeText(mContext, "已无更多数据！", Toast.LENGTH_SHORT).show();
										class_sort_list1_xv.hideFooter();
									}
								}
								class_sort_list1_xv.stopLoadMore();
								class_sort_list1_xv.stopRefresh();
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
}