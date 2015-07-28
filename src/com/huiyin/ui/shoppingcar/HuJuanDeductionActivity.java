package com.huiyin.ui.shoppingcar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.adapter.ChooseLehjuanAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.LehuQuanBean;
import com.huiyin.ui.fillorder.FillOrderActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HuJuanDeductionActivity extends BaseActivity {

	public final static String INTENT_KEY_LIST = "list_data";
	private final static String TAG = "HuJuanDeductionActivity";
	private Context mContext = HuJuanDeductionActivity.this;
	private TextView ab_title, ab_back;
	private ListView mListView;
	private List<LehuQuanBean.TicketListEntity> listDatas;
	private ChooseLehjuanAdapter adapter;
	private TextView hj_total_num;
	private TextView ab_right, lhj_tip;
	private EditText luj_edit;
	private Button lhj_confirm;
	String total, thirdIds, brandIds, goodIds, storeIds;
	LehuQuanBean mLehuQuanBean;
	String TICEK_ID;
	String PRICE;
	String backPrice;
	private LinearLayout ll;
	private int lastVisibleIndex;
	private int NUM;
	private String lh_id;
	private String hujuan_value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hujuan_deduction);
		initView();
	}

	private void initView() {
		total = getIntent().getStringExtra("total");
		thirdIds = getIntent().getStringExtra("thirdIds");
		brandIds = getIntent().getStringExtra("brandIds");
		goodIds = getIntent().getStringExtra("goodIds");
		storeIds = getIntent().getStringExtra("storeIds");
		lh_id = getIntent().getStringExtra("lh_id");
		hujuan_value = getIntent().getStringExtra("hujuan_value");
        NUM = TextUtils.isEmpty(getIntent().getStringExtra("num"))?0:Utils.anInt(getIntent().getStringExtra("num"));

		hj_total_num = (TextView) findViewById(R.id.hj_total_num);

		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText("乐虎劵抵扣");
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				back();
			}
		});
		ab_right = (TextView) findViewById(R.id.ab_right);
		ab_right.setVisibility(View.GONE);
		lhj_tip = (TextView) findViewById(R.id.lhj_tip);
		luj_edit = (EditText) findViewById(R.id.luj_edit);
		lhj_confirm = (Button) findViewById(R.id.lhj_confirm);
		lhj_confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!TextUtils.isEmpty(luj_edit.getText().toString())) {
					vacatiedLHJ(luj_edit.getText().toString().trim());
				} else {
					UIHelper.showToast("请输入虎券编号");
				}
			}
		});
		mListView = (ListView) findViewById(R.id.mListView);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				if (IsTicketEnable(i)) {
					backPrice = adapter
							.setSelect(listDatas.get(i).getID() + "") ? listDatas
							.get(i).getCONDITION2() + ""
							: "0.00";
                    adapter.notifyDataSetChanged();
                }
			}
		});
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView absListView,
					int scrollState) {
				if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
						&& lastVisibleIndex == adapter.getCount()) {
					if (mLehuQuanBean.getPageIndex() < mLehuQuanBean
							.getTotalPageNum()) {
						getLHJ(total, thirdIds, brandIds, goodIds, storeIds,
								mLehuQuanBean.getPageIndex() + 1 + "");
					}
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
			}
		});
		/** 触底加载布局 **/
		ll = new LinearLayout(mContext);
		ProgressBar pb = new ProgressBar(mContext);
		ll.setGravity(Gravity.CENTER);
		ll.addView(pb);
		/** 要加在 setAdapter之前 **/
		mListView.addFooterView(ll);
		getLHJ(total, thirdIds, brandIds, goodIds, storeIds, "1");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		back();
	}

	private void back() {
		Intent i = new Intent();
		i.setClass(HuJuanDeductionActivity.this, FillOrderActivity.class);
		i.putExtra("lhj_value_id", adapter != null ? adapter.getSelect() : "-1");
		i.putExtra("lhj_value_price", backPrice);
		i.putExtra("lhj_num", NUM + "");
		setResult(RESULT_OK, i);
		finish();

	}

	/**
	 * 激活虎券
	 *
	 * @param ticketCode
	 */
	private void vacatiedLHJ(String ticketCode) {
		if (AppContext.userId != null) {
			RequstClient.activateCoupon(AppContext.userId, ticketCode,total,
					new CustomResponseHandler(this) {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String content) {
							super.onSuccess(statusCode, headers, content);
							LogUtil.i(TAG, "激活虎券vacatiedLHJ:" + content);
							try {
								JSONObject obj = new JSONObject(content);
								if (!obj.getString("type").equals("1")) {
									String errorMsg = obj.getString("msg");
									Toast.makeText(getBaseContext(), errorMsg,
											Toast.LENGTH_SHORT).show();
									return;
								}
								TICEK_ID = obj.getString("TICEK_ID");
								PRICE = obj.getString("PRICE");
                                NUM++;
								getLHJ(total, thirdIds, brandIds, goodIds,
										storeIds, "1");
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

	private void getLHJ(String total, String thirdIds, String brandIds,
			String goodIds, String storeIds, String pageIndex) {
		if (AppContext.userId != null) {
			RequstClient.couponData(AppContext.userId, total, thirdIds,
					brandIds, goodIds, storeIds, pageIndex,
					new CustomResponseHandler(this) {
						@Override
						public void onSuccess(int statusCode, Header[] headers,
								String content) {
							super.onSuccess(statusCode, headers, content);
							LogUtil.i(TAG, "获得本购物车的可用虎券getLHJ:" + content);
							try {
								JSONObject obj = new JSONObject(content);
								if (!obj.getString("type").equals("1")) {
									String errorMsg = obj.getString("msg");
									Toast.makeText(getBaseContext(), errorMsg,
											Toast.LENGTH_SHORT).show();
									return;
								}

								mLehuQuanBean = new Gson().fromJson(content,
										LehuQuanBean.class);
								update();
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

	/**
	 * ui填充数据
	 */
	private void update() {
		if (mLehuQuanBean.getPageIndex() == 1) {
			listDatas = mLehuQuanBean.getTicketList();
		} else {
			for (int i = 0; i < mLehuQuanBean.getTicketList().size(); i++) {
				listDatas.add(mLehuQuanBean.getTicketList().get(i));
			}
		}
        hj_total_num.setText("共" + NUM + "张乐虎劵可用");

		if (adapter == null) {
			adapter = new ChooseLehjuanAdapter(getApplicationContext(),
					listDatas);
			mListView.setAdapter(adapter);
			if (!lh_id.equals("-1")) {
				adapter.setSelect(lh_id);
				adapter.notifyDataSetChanged();
				backPrice = hujuan_value;
			}
		} else {
			adapter.setListDatas(listDatas);
			adapter.notifyDataSetChanged();
		}
		if (mLehuQuanBean.getPageIndex() >= mLehuQuanBean.getTotalPageNum()) {
			mListView.removeFooterView(ll);
		}

		if (!TextUtils.isEmpty(TICEK_ID)) {
			lhj_tip.setText("提示: 输入的优惠券可以使用，面值" + PRICE + "元");
            if (IsTicketEnable(TICEK_ID)){
                adapter.setSelect(TICEK_ID);
                adapter.notifyDataSetChanged();
                backPrice = PRICE;
            }
		}
	}

	/**
	 * 判断此优惠券是否可用
     * @param index 虎券列表的位置
	 */
	private boolean IsTicketEnable(int index) {
		if (listDatas != null) {
			if (listDatas.get(index).getHQ_TYPE() == 1) {
				// 满额
				if (listDatas.get(index).getCONDITION1() > Double.valueOf(total)) {
                    UIHelper.showToast(String.format("您的商品总价为%s元，没满%d无法使用此优惠券",
                            total,listDatas.get(index).getCONDITION1()));
					return false;
				}
			}
			return true;
		}
		return false;
	}
    /**
     * 判断此优惠券是否可用
     * @param TICEK_ID 虎券id
     */
    private boolean IsTicketEnable(String TICEK_ID) {
        if (listDatas != null) {
            for (int i = 0; i < listDatas.size(); i++) {
                if (listDatas.get(i).getID() == Utils.anInt(TICEK_ID)) {
                   return IsTicketEnable(i);
                }
            }
            return true;
        }
        return false;
    }
}
