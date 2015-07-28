package com.huiyin.ui.shoppingcar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.AppContext.LocationCallBack;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.adapter.NearShopListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.NearShopBean;
import com.huiyin.bean.NearShopBean.ShopMention;
import com.huiyin.db.SQLOpearteImpl;
import com.huiyin.ui.fillorder.FillOrderActivity;
import com.huiyin.ui.shoppingcar.DatePickPopMenu.ConfirmClick;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PayWayActivity extends BaseActivity {
	private final static String TAG = "PayWayActivity";
	TextView left_rb, middle_title_tv, right_rb,payway_shop;
	GridView spin_pay_way, spin_send_way;
	EditText spin_member_num;
	TextView spin_send_time;
	PayWayBean mPayWayBean;
	Button payway_btn, payway_select_time;

	List<String> listPay ;
	List<String> listSend ;
	String payWayId, sendWayId, sendtime, member_num, pay, send;
	int shopId;
	private Context mContext;
	/** 经度 */
	private double lng = 0.0;
	/** 纬度 */
	private double lat = 0.0;
	private String cityName;
	private int cityId;
	private NearShopBean bean;
	private MyAdapter mMyAdapter, mMyAdapterSend;
    private String deliveryTime;
    public static String DEFAULT_PAY = "在线支付";             //默认支付方式
    public static String DEFAULT_DELIVER = "送货/服务上门";     //默认快递方式

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payway_layout1);
		mContext = this;
		Intent i = getIntent();
		sendWayId = i.getStringExtra("sendWayId");
		shopId = i.getIntExtra("shopId", -1);
        deliveryTime = i.getStringExtra("deliveryTime");
        send = DEFAULT_DELIVER;
		initView();
        refleshUI();
	}

	private void initView() {
		left_rb = (TextView) findViewById(R.id.left_ib);
		right_rb = (TextView) findViewById(R.id.right_ib);
		right_rb.setVisibility(View.GONE);
		left_rb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("支付及配送方式");
        payway_shop= (TextView) findViewById(R.id.payway_shop);
		spin_pay_way = (GridView) findViewById(R.id.spin_pay_way);
		spin_send_way = (GridView) findViewById(R.id.spin_send_way);
		spin_send_time = (TextView) findViewById(R.id.spin_send_time);
		payway_select_time = (Button) findViewById(R.id.payway_select_time);
		payway_select_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				DatePickPopMenu menu = new DatePickPopMenu(mContext);
				menu.setmClickListener(new ConfirmClick() {

					@Override
					public void OnConfirmClick(String temp) {
						spin_send_time.setText(temp);
					}
				});
				menu.showAtLocation(spin_send_time, Gravity.BOTTOM, 0, 0);
			}
		});
        spin_send_time.setText(deliveryTime);
		spin_member_num = (EditText) findViewById(R.id.spin_member_num);

		payway_btn = (Button) findViewById(R.id.payway_btn);
		payway_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				sendtime = spin_send_time.getText().toString();
				if (sendtime.equals("")) {
					Toast.makeText(PayWayActivity.this, "送货时间不能为空",
							Toast.LENGTH_LONG).show();
					return;
				}
				Intent i = new Intent(PayWayActivity.this,
						FillOrderActivity.class);
				i.putExtra("payWayId", payWayId);
				i.putExtra("pay", pay);
				i.putExtra("sendWayId", sendWayId);
				i.putExtra("send",send);
				i.putExtra("sendtime", sendtime);
				i.putExtra("shopId", shopId);
                if (spin_member_num!=null&&spin_member_num.getText()!=null){
                    i.putExtra("staffId", spin_member_num.getText().toString().trim());
                }
				setResult(RESULT_OK, i);
				finish();
			}
		});
	}

	private void initData() {
		RequstClient.doPayWay(new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "doPayWay:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg,
								Toast.LENGTH_SHORT).show();
						return;
					}
					mPayWayBean = new Gson()
							.fromJson(content, PayWayBean.class);
					refleshUI();

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
	/**
	 * 获得数据后刷新UI
	 */
	private void refleshUI() {
//		for (int i = 0; i < mPayWayBean.payMethodList.size(); i++) {
//			listPay.add(mPayWayBean.payMethodList.get(i).METHOD_NAME);
//		}
        listPay = new ArrayList<String>();
        listPay.add(DEFAULT_PAY);
		mMyAdapter = new MyAdapter(this, listPay, DEFAULT_PAY);
		spin_pay_way.setAdapter(mMyAdapter);
		spin_pay_way.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                mMyAdapter.setSelect(listPay.get(i));
                pay = listPay.get(i);
                mMyAdapter.notifyDataSetChanged();
            }
        });

        listSend = new ArrayList<String>();
        listSend.add(DEFAULT_DELIVER);
        listSend.add("上门自提");
		mMyAdapterSend = new MyAdapter(this, listSend,DEFAULT_DELIVER);
		spin_send_way.setAdapter(mMyAdapterSend);
		spin_send_way.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int arg2, long l) {
				mMyAdapterSend.setSelect(listSend.get(arg2));
				mMyAdapterSend.notifyDataSetChanged();
				if (arg2 != listSend.size() - 1) {
					send = listSend.get(arg2);
                    payway_shop.setText("");
				} else {
					startLoacation();
				}
			}
		});
	}

	public class PayWayBean {

		ArrayList<PayItem> payMethodList = new ArrayList<PayItem>();
		ArrayList<SendItem> distributionManagementList = new ArrayList<SendItem>();

		public class PayItem {
			String PAYMETHODID;
			String METHOD_NAME;
		}

		public class SendItem {
			String DISMANNAME;
			String DISMANID;
		}

	}

	/**
	 * 获取定位信息
	 */
	private void startLoacation() {
		lng = AppContext.getInstance().lng;
		lat = AppContext.getInstance().lat;
		cityName = AppContext.getInstance().cityName;
		if (lng == 0.0 || lat == 0.0 || StringUtils.isBlank(cityName)) {
			UIHelper.showLoadDialog(this, "定位中");
			AppContext.getInstance().StartLoacation(new LocationCallBack() {

				@Override
				public void getPoistion(BDLocation location) {
					UIHelper.cloesLoadDialog();
					Toast.makeText(mContext, "定位成功", Toast.LENGTH_SHORT).show();
					lng = location.getLongitude();
					lat = location.getLatitude();
					cityName = location.getCity();
					if (lng != 0.0 && lat != 0.0) {
						SQLOpearteImpl temp = new SQLOpearteImpl(mContext);
						cityId = temp.checkIdByName(cityName);
						getNearShop();
						temp.CloseDB();
					}
				}
			});
		} else {
			SQLOpearteImpl temp = new SQLOpearteImpl(mContext);
			cityId = temp.checkIdByName(cityName);
			getNearShop();
			temp.CloseDB();
		}
	}

	private void getNearShop() {
		if (bean != null && bean.type > 0) {
			showListDialog();
			return;
		}
		if (cityId == -1)
			return;
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				bean = NearShopBean.explainJson(content, mContext);
				if (bean.type > 0) {
					showListDialog();
				}
			}
		};
		RequstClient.appNearShop(cityId, lng, lat, handler);
	}

	private AlertDialog mDialog;

	// modify by zhyao @2015/67/6 添加自提地址筛选功能
	private void showListDialog() {
		final ArrayList<ShopMention> nearbyList = bean.nearbyList;
		final ArrayList<ShopMention> searchNearByList = new ArrayList<ShopMention>();
		
		View mView = LayoutInflater.from(mContext).inflate(
				R.layout.dialog_listview, null);
		EditText mSearchEdit = (EditText) mView.findViewById(R.id.ed_search);
		
		ListView mListView = (ListView) mView.findViewById(R.id.listView);
		final NearShopListAdapter mAdapter = new NearShopListAdapter(mContext);
		mAdapter.addItem(nearbyList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				sendWayId = "-1";
				send = "上门自提("
						+ ((NearShopListAdapter) parent.getAdapter())
								.getShopName(position) + ")";
				shopId = ((NearShopListAdapter) parent.getAdapter())
						.getShopId(position);
                payway_shop.setText(send);
				mDialog.dismiss();
			}
		});
		mSearchEdit.addTextChangedListener(new TextWatcher() {
					
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!TextUtils.isEmpty(s)) {
					for (ShopMention shopMention : nearbyList) {
						if (!TextUtils.isEmpty(shopMention.NAME) && shopMention.NAME.contains(s)) {
							searchNearByList.add(shopMention);
						}
					}
					mAdapter.deleteItem();
					mAdapter.addItem(searchNearByList);
				}
				else {
					mAdapter.deleteItem();
					mAdapter.addItem(nearbyList);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				searchNearByList.clear();
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		mDialog = new AlertDialog.Builder(mContext, 3).setTitle("选择门店")
				.setView(mView)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mDialog.dismiss();
						mMyAdapterSend.setSelect(listSend.get(0));
						mMyAdapterSend.notifyDataSetChanged();
						send = listSend.get(0);
                        payway_shop.setText("");
					}
				}).create();
		mDialog.setCancelable(false);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.show();
	}

	private class MyAdapter extends BaseAdapter {
		Context context;
		List<String> data;
		String select;

		private MyAdapter(Context context, List<String> data, String select) {
			this.context = context;
			this.data = data;
			setSelect(select);
		}

		public void setData(List<String> data) {
			if (this.data == null) {
				this.data = new ArrayList<String>();
			}
			if (data != null) {
				this.data.clear();
				for (String s : data) {
					this.data.add(s);
				}
			}
		}

		public void setSelect(String select) {
			this.select = select;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int i) {
			return data.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder mHolder;
			if (view == null) {
				view = LayoutInflater.from(context).inflate(
						R.layout.payway_grid_item, null);
				mHolder = new ViewHolder();
				mHolder.mImageView = (ImageView) view
						.findViewById(R.id.payway_item_image);
				mHolder.mTextView = (TextView) view
						.findViewById(R.id.payway_item_text);
				mHolder.mLinearLayout = (RelativeLayout) view
						.findViewById(R.id.payway_item_bg);
				view.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) view.getTag();
			}
			if (data.get(i).equals(select)) {
				mHolder.mLinearLayout
						.setBackgroundResource(R.drawable.show_red_bg);
				mHolder.mImageView.setVisibility(View.VISIBLE);
				mHolder.mTextView.setTextColor(getResources().getColor(
						R.color.red));
			} else {
				mHolder.mLinearLayout
						.setBackgroundResource(R.drawable.show_search_bg);
				mHolder.mImageView.setVisibility(View.GONE);
				mHolder.mTextView.setTextColor(getResources().getColor(
						R.color.payway_text_color));
			}
			mHolder.mTextView.setText(data.get(i));
			return view;
		}

		private class ViewHolder {
			private RelativeLayout mLinearLayout;
			public ImageView mImageView;
			public TextView mTextView;
		}
	}
}
