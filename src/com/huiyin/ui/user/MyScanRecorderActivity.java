package com.huiyin.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.MyScanRecorderBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.Utils;
import com.huiyin.wight.XListView;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyScanRecorderActivity extends BaseActivity implements
		View.OnClickListener {

	private XListView myscanrecorderlistview;
	private RecoderAdapter mRecoderAdapter;
	private List<MyScanRecorderBean.HistoryListEntity> mListRecords;
	private int pageIndex = 1;
	private MyScanRecorderBean mRecorderBean;
	private final int REQUEST_CLEAR = 1;
	private final int REQUEST_DELETE = 2;
	private String goodsId;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_scan_recorder);
		
		pageIndex = 1;
        onLoadData();
	}

    @Override
	protected void onFindViews() {
		setTitle("浏览记录");
		setRightText(View.VISIBLE, "清空记录", this);
		this.myscanrecorderlistview = (XListView) findViewById(R.id.my_scan_recorder_listview);
		myscanrecorderlistview.setPullLoadEnable(false);
		myscanrecorderlistview.setFooterDividersEnabled(false);
		myscanrecorderlistview.setPullRefreshEnable(true);

		// 上次更新的时间
		myscanrecorderlistview.setRefreshTime(Utils.getCurrTiem());
		myscanrecorderlistview
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long l) {
						if (mListRecords.get(position - 1 < 0 ? 0 : position - 1)
								.getGOODS_STATUS().equals("2")) {
							UIHelper.showToast("该商品已下架");
							return;
						}
						Intent intent = new Intent(MyScanRecorderActivity.this,
								ProductsDetailActivity.class);
						intent.putExtra(
								ProductsDetailActivity.BUNDLE_KEY_GOODS_ID,
								mListRecords.get(
										position - 1 < 0 ? 0 : position - 1)
										.getGOODS_ID()
										+ "");
						startActivity(intent);
					}
				});
		myscanrecorderlistview
				.setXListViewListener(new XListView.IXListViewListener() {
					@Override
					public void onRefresh() {
						pageIndex = 1;
						onLoadData();
					}

					@Override
					public void onLoadMore() {
						if (mRecorderBean.getPageIndex() < mRecorderBean
								.getTotalPageNum()) {
							pageIndex++;
							onLoadData();
						}
					}
				});
		myscanrecorderlistview
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> adapterView,
							View view, int i, long l) {
						position = i - 1 < 0 ? 0 : i - 1;
						goodsId = mListRecords.get(position).getGOODS_ID();
						Intent mIntent = new Intent(
								MyScanRecorderActivity.this,
								CommonConfrimCancelDialog.class);
						mIntent.putExtra(
								CommonConfrimCancelDialog.TASK,
								CommonConfrimCancelDialog.TASK_DELETE_ONE_HiSTORY);
						startActivityForResult(mIntent, REQUEST_DELETE);
						return true;
					}
				});
	}

	@Override
	protected void onLoadData() {
		if (AppContext.userId != null) {
			RequstClient.historyData(AppContext.userId, pageIndex + "",
                    new CustomResponseHandler(this) {

                        @Override
                        public void onFailure(Throwable error, String content) {
                            myscanrecorderlistview.stopLoadMore();
                            myscanrecorderlistview.stopRefresh();
                        }

                        @Override
                        public void onSuccess(String content) {
                            super.onSuccess(content);
                            myscanrecorderlistview.stopLoadMore();
                            myscanrecorderlistview.stopRefresh();
                            if (LogUtil.isDebug) {
                                Logger.json(content);
                            }
                            JSONObject obj;
                            try {
                                obj = new JSONObject(content);
                                if (!obj.getString("type").equals("1")) {
                                    String errorMsg = obj.getString("msg");
                                    UIHelper.showToast(errorMsg);
                                    return;
                                }
                                mRecorderBean = new Gson().fromJson(content,
                                        MyScanRecorderBean.class);
                                if (mRecorderBean.getPageIndex() >= mRecorderBean
                                        .getTotalPageNum()) {
                                    myscanrecorderlistview.hideFooter();
                                    myscanrecorderlistview
                                            .setPullLoadEnable(false);
                                } else {
                                    myscanrecorderlistview
                                            .setPullLoadEnable(true);
                                    myscanrecorderlistview.showFooter();
                                }
                                dealResult(mRecorderBean);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
		} else {
			UIHelper.showToast("未登陆，请先登陆");
		}
	}

	/**
	 * 删除浏览记录
	 * 
	 * @param flag
	 *            1全部，2具体某个商品
	 * @param goodsId
	 *            商品id（如果flag=2是必传）
	 */
	private void deleteHistory(final String flag, String goodsId) {
		if (AppContext.userId != null) {
			RequstClient.cancelHistory(AppContext.userId, flag, goodsId,
					new CustomResponseHandler(this) {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							JSONObject mJSONObject;
							try {
								mJSONObject = new JSONObject(content);
								if (!mJSONObject.getString("type").equals("1")) {
									String errorMsg = mJSONObject
											.getString("msg");
									UIHelper.showToast(errorMsg);
									return;
								}
								if (flag.equals("2")) {
									mListRecords.remove(position);
									mRecoderAdapter.setMapList(mListRecords);
									mRecoderAdapter.notifyDataSetChanged();

								}else if (flag.equals("1")){
                                    myscanrecorderlistview.hideFooter();
                                    myscanrecorderlistview
                                            .setPullLoadEnable(false);
                                    mListRecords.clear();
                                    mRecoderAdapter.setMapList(mListRecords);
                                    mRecoderAdapter.notifyDataSetChanged();
                                }
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		} else {
			UIHelper.showToast("未登陆，请先登陆");
		}
	}

	private void dealResult(MyScanRecorderBean recorderBean) {
		if (mListRecords == null) {
			mListRecords = new ArrayList<MyScanRecorderBean.HistoryListEntity>();
		}
		if (pageIndex == 1) {
			mListRecords.clear();
			myscanrecorderlistview.setPullLoadEnable(true);
			myscanrecorderlistview.showFooter();
            if (mRecorderBean.getPageIndex() >= mRecorderBean
                    .getTotalPageNum()) {
                myscanrecorderlistview.hideFooter();
                myscanrecorderlistview
                        .setPullLoadEnable(false);
            }
        }
		for (int i = 0; i < recorderBean.getHistoryList().size(); i++) {
			mListRecords.add(recorderBean.getHistoryList().get(i));
		}

		if (mRecoderAdapter == null) {
			mRecoderAdapter = new RecoderAdapter(this, mListRecords);
			myscanrecorderlistview.setAdapter(mRecoderAdapter);
		} else {
			mRecoderAdapter.setMapList(mListRecords);
			mRecoderAdapter.notifyDataSetChanged();
		}
	}

	private void clear() {
		if (mListRecords == null || mListRecords.size() == 0)
			return;
		Intent i = new Intent(this, CommonConfrimCancelDialog.class);
		i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_DELETE_ALL_HiSTORY);
		startActivityForResult(i, REQUEST_CLEAR);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_my_scan_recorder, menu);
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CLEAR && resultCode == Activity.RESULT_OK) {//
			deleteHistory("1", "-1");

		} else if (requestCode == REQUEST_DELETE && resultCode == Activity.RESULT_OK) {
			deleteHistory("2", goodsId + "");
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.right_ib:
			clear();
			break;

		}
	}

	class RecoderAdapter extends BaseAdapter {
		private Context mContext;
		private List<MyScanRecorderBean.HistoryListEntity> mMapList;

		RecoderAdapter(Context mContext,
				List<MyScanRecorderBean.HistoryListEntity> mMapList) {
			this.mContext = mContext;
			setMapList(mMapList);
		}

		public void setMapList(
				List<MyScanRecorderBean.HistoryListEntity> historyListEntities) {
			if (mMapList == null) {
				mMapList = new ArrayList<MyScanRecorderBean.HistoryListEntity>();
			}
			mMapList.clear();
			for (int i = 0; i < historyListEntities.size(); i++) {
				mMapList.add(historyListEntities.get(i));
			}
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
			ViewHolder mViewholder;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(
						R.layout.my_scan_recorder_item, viewGroup, false);
				mViewholder = new ViewHolder();
				mViewholder.my_scan_recorder_item_image = (ImageView) view
						.findViewById(R.id.my_scan_recorder_item_image);
				mViewholder.my_scan_recorder_item_title = (TextView) view
						.findViewById(R.id.my_scan_recorder_item_title);
				mViewholder.my_scan_recorder_item_price = (TextView) view
						.findViewById(R.id.my_scan_recorder_item_price);
				mViewholder.my_scan_recorder_item_stock = (TextView) view
						.findViewById(R.id.my_scan_recorder_item_stock);
				view.setTag(mViewholder);
			} else {
				mViewholder = (ViewHolder) view.getTag();
			}
			MyScanRecorderBean.HistoryListEntity mItem = mMapList.get(i);
			if (mItem == null)
				return view;
			ImageManager.LoadWithServer(mItem.getGOODS_IMG(),
					mViewholder.my_scan_recorder_item_image);
			mViewholder.my_scan_recorder_item_title.setText(mItem
					.getGOODS_NAME());
			mViewholder.my_scan_recorder_item_price.setText("￥"
					+ mItem.getGOODS_PRICE());
			if (mItem.getGOODS_STATUS().equals("2")) {
				mViewholder.my_scan_recorder_item_stock
						.setVisibility(View.VISIBLE);
				mViewholder.my_scan_recorder_item_stock.setText("已下架");
			} else {
				mViewholder.my_scan_recorder_item_stock
						.setVisibility(View.GONE);
				mViewholder.my_scan_recorder_item_stock.setText("");
			}
			return view;
		}
	}

	class ViewHolder {
		ImageView my_scan_recorder_item_image;
		TextView my_scan_recorder_item_title;
		TextView my_scan_recorder_item_price;
		TextView my_scan_recorder_item_stock;
	}
}
