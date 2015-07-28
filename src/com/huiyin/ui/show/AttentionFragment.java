package com.huiyin.ui.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.AppShow;
import com.huiyin.bean.AppShowAddAttention;
import com.huiyin.bean.AppShowAddLike;
import com.huiyin.bean.AppShowCancelAttention;
import com.huiyin.ui.show.adapter.ShowAdapter;
import com.huiyin.ui.show.interf.OnAddShowLikeListener;
import com.huiyin.ui.show.interf.OnAttentionListener;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

public class AttentionFragment extends Fragment {
	private ShowAdapter mAdapter;
	private Context mContext;
	private int pageIndex = 1;

	public String user_id;

	AppShow data;
	AppShowAddLike data1;
	AppShowAddAttention data2;
	AppShowCancelAttention data3;
	private XListView listview;

	boolean load_flag;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		user_id = AppContext.userId;
		View view1 = inflater.inflate(R.layout.fragment_attention1, null);
		listview = (XListView) view1.findViewById(R.id.listView_attention);

		load_flag = false;

		mAdapter = new ShowAdapter(mContext);
		listview.setAdapter(mAdapter);
		listview.setPullLoadEnable(true);
		listview.setPullRefreshEnable(true);
		listview.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				pageIndex = 1;
				initData();
			}

			@Override
			public void onLoadMore() {
				initData();
			}
		});
		mAdapter.setmOnAddShowLikeListener(new OnAddShowLikeListener() {

			@Override
			public void addShowLike(int spotlightId) {

				addLikeData(spotlightId);
			}
		});

		mAdapter.setmOnAttentionListener(new OnAttentionListener() {

			@Override
			public void addCancelAttention(int spotlightUserId) {
				CancelAttentionData(spotlightUserId);
			}

			@Override
			public void addAttention(int spotlightUserId) {
				AttentionData(spotlightUserId);
			}
		});

		return view1;
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		pageIndex = 1;
		initData();
	}

	private void AttentionData(int spotlightUserId) {
		// 关注网络
		CustomResponseHandler handler = new CustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data2 = AppShowAddAttention.explainJson(content, mContext);

				if (data2.type == 1) {

					Toast.makeText(mContext, "谢谢你关注我", Toast.LENGTH_SHORT)
							.show();
					pageIndex = 1;
					initData();
				} else {
					Toast.makeText(mContext, data2.msg, Toast.LENGTH_SHORT)
							.show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (user_id == null) {
			// Toast.makeText(mContext, "请登录！！！", 0).show();

		} else if (user_id.equals(spotlightUserId + "")) {

			Toast.makeText(mContext, "不能关注自己！", Toast.LENGTH_SHORT).show();
		} else {
			RequstClient.appAttention(handler, user_id, spotlightUserId + "");
		}
	}

	private void CancelAttentionData(int spotlightUserId) {
		// 取消关注网络
		CustomResponseHandler handler = new CustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data3 = AppShowCancelAttention.explainJson(content, mContext);

				if (data3.type == 1) {

					Toast.makeText(mContext, "亲,希望你能够再次关注我", Toast.LENGTH_SHORT).show();
					pageIndex = 1;
					initData();
				} else {

					Toast.makeText(mContext, data3.msg, Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (user_id == null) {
			// Toast.makeText(mContext, "请登录！！！", 0).show();

			// Toast.makeText(mContext, "取消网络", 0).show();
		} else {
			RequstClient.appCancelAttention(handler, user_id, spotlightUserId
					+ "");
		}
	}

	private void addLikeData(int spotlightId) {
		// 喜欢 网络请求
		CustomResponseHandler handler = new CustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			public void onRefreshData(String content) {

				data1 = AppShowAddLike.explainJson(content, mContext);

				if (data1.type == 1) {

					Toast.makeText(mContext, "谢谢你喜欢我", Toast.LENGTH_SHORT).show();
					pageIndex = 1;
					initData();
				}
				if (data1.type == 2) {
					Toast.makeText(mContext, data1.msg, Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		if (user_id == null) {

			// Toast.makeText(mContext, "请登录！！！", 0).show();
		} else {

			RequstClient.appShowLike(handler, user_id, spotlightId + "");
		}
	}

	private void initData() {
		// 加载网络
		CustomResponseHandler handler = new CustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {
				listview.stopLoadMore();
				listview.stopRefresh();
				data = AppShow.explainJson(content, mContext);

				if (data.type == 1) {

					if (data.spotlight != null
							&& data.spotlight.pageBean != null
							&& data.spotlight.pageBean.size() > 0) {

						if (data.spotlight.pageBean.size() < 10) {

							listview.hideFooter();
							load_flag = true;
							listview.setPullLoadEnable(false);
						} else {

							listview.showFooter();
							listview.setPullLoadEnable(true);
						}
						if (pageIndex == 1) {
							mAdapter.addItem(data.spotlight.pageBean);
						} else {

							mAdapter.addMoreItem(data.spotlight.pageBean);
						}
					} else {

						if (pageIndex == 1) {

							mAdapter.deleteItem();
							Toast.makeText(getActivity(), "暂无数据！",
									Toast.LENGTH_SHORT).show();
							listview.hideFooter();
							load_flag = true;
							listview.setPullLoadEnable(false);
						} else {

							Toast.makeText(getActivity(), "已无更多数据！",
									Toast.LENGTH_SHORT).show();
							listview.hideFooter();
							load_flag = true;
							listview.setPullLoadEnable(false);
						}
					}

					mAdapter.notifyDataSetChanged();
					pageIndex++;
				} else {

					Toast.makeText(mContext, data.msg, Toast.LENGTH_SHORT).show();
					listview.hideFooter();
					load_flag = true;
					listview.setPullLoadEnable(false);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				listview.stopLoadMore();
				listview.stopRefresh();
				return;
			}
		};
		if (AppContext.userId == null) {
			// 未登录状态下 推荐接口
			RequstClient.appShow(handler, "1", 10 + "", pageIndex + "");
		} else {
			//  add by zhyao @2015/6/29 添加isMine参数
			// 登录状态下 推荐接口
			RequstClient.appNoShow(handler, "1", AppContext.userId, "0", 10 + "",
					pageIndex + "");
		}

	}

}
