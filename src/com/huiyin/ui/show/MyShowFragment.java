package com.huiyin.ui.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.AppShow;
import com.huiyin.bean.AppShowAddLike;
import com.huiyin.dialog.ConfirmDialog;
import com.huiyin.dialog.ConfirmDialog.DialogClickListener;
import com.huiyin.ui.show.adapter.ShowMyAdapter;
import com.huiyin.ui.show.adapter.ShowMyAdapter.OnDeleteMyShowItemListener;
import com.huiyin.ui.show.interf.OnAddShowLikeListener;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;
//add by zhyao @2015/6/29 添加我的秀场页面
public class MyShowFragment extends Fragment {
	private ShowMyAdapter mAdapter;
	private Context mContext;
	private int pageIndex = 1;

	public String user_id;

	AppShow data;
	AppShowAddLike data1;
	private XListView listview;

	boolean load_flag;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		user_id = AppContext.userId;
		View view1 = inflater.inflate(R.layout.fragment_attention1, null);
		listview = (XListView) view1.findViewById(R.id.listView_attention);

		load_flag = false;

		mAdapter = new ShowMyAdapter(mContext);
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

		mAdapter.setmOnDeleteMyShowItemListener(new OnDeleteMyShowItemListener() {
			
			@Override
			public void onDeleteMyShowItem(int id) {
				showDialog("删除", "确定要删除该条记录吗？", id);
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
	
	 public void showDialog(String title, String msg, final int id) {
			ConfirmDialog dialog = new ConfirmDialog(getActivity());
			dialog.setCustomTitle(title);
			dialog.setMessage(msg);
			dialog.setConfirm("确定");
			dialog.setCancel("取消");
			dialog.setClickListener(new DialogClickListener() {

				@Override
				public void onConfirmClickListener() {
					deleteMyShowItem(id);
				}

				@Override
				public void onCancelClickListener() {

				}
			});
			dialog.show();
		}
	
	private void deleteMyShowItem(int id) {
		// 关注网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext,
				true) {

			@SuppressLint("NewApi")
			@Override
			public void onRefreshData(String content) {

				data = AppShow.explainJson(content, mContext);

				Toast.makeText(mContext, data.msg, Toast.LENGTH_SHORT).show();
				if (data.type == 1) {
					pageIndex = 1;
					initData();
				}
				
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				return;
			}
		};
		
		RequstClient.delAppShow(handler, id + "");
				
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
			Toast.makeText(mContext, "请登录！！！", Toast.LENGTH_LONG).show();
		} else {
			//  add by zhyao @2015/6/29 添加isMine参数
			// 登录状态下 推荐接口
			RequstClient.appNoShow(handler, "0", "1", AppContext.userId, 10 + "",
					pageIndex + "");
		}

	}

}
