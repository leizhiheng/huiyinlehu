package com.huiyin.ui.lehuvoucher;

import java.util.Date;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.LehuVoucherListBean;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LehuVoucherApplyFragment extends Fragment implements IXListViewListener {

	private View rootView;
	private Context mContext;
	private XListView mXlistview;

	private int mPageindex;

	private String userId;
	private LehuVoucherListBean data;
	private ApplyListViewAdapter mAdapter;

	@Override
	public void onAttach(Activity activity) {
		Log.i("LehuVoucherFragment", "onAttach");
		super.onAttach(activity);
		mContext = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("LehuVoucherFragment", "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.i("LehuVoucherFragment", "onCreateView");
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.lehu_voucher_apply_fragment, null);
		}
		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i("LehuVoucherFragment", "onResume");
		if (data != null)
			return;
		userId = AppContext.getInstance().userId;
		findView();
		setListener();
		initData();

	}

	private void findView() {
		mXlistview = (XListView) rootView.findViewById(R.id.xlistview);
		mXlistview.setPullLoadEnable(false);
		mXlistview.setPullRefreshEnable(true);
		mXlistview.setXListViewListener(this);

		mAdapter = new ApplyListViewAdapter(mContext);
		mXlistview.setAdapter(mAdapter);
	}

	private void setListener() {

	}

	private void initData() {
		loadPageData(1);
	}

	private void loadPageData(int loadType) {
		if (loadType == 1) {
			mPageindex = 1;
		} else {
			mPageindex += 1;
		}
		CustomResponseHandler handler = new CustomResponseHandler(getActivity(), false) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				data = LehuVoucherListBean.explainJson(content, mContext);
				if (data.type == 1) {

					if (data.getContent() != null && data.getContent().size() > 0) {
						if (mPageindex == 1) {
							mAdapter.deleteItem();
						}
						mAdapter.addItem(data.getContent());
						if (data.getContent().size() >= 10) {
							mXlistview.setPullLoadEnable(true);
						} else {
							mXlistview.setPullLoadEnable(false);
						}
					}
				} else {
					Toast.makeText(mContext, data.msg, Toast.LENGTH_SHORT).show();
					mXlistview.setPullLoadEnable(false);
					if (mPageindex == 1) {
						mAdapter.deleteItem();
					}
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				//endUpData();
			}

			@Override
			public void onFinish() {
				super.onFinish();
				endUpData();
			}

		};
		RequstClient.queryTicketPacketList(handler, userId, mPageindex);
	}

	@Override
	public void onRefresh() {
		loadPageData(1);
	}

	@Override
	public void onLoadMore() {
		loadPageData(2);
	}

	private void endUpData() {
		mXlistview.stopLoadMore(1);
		mXlistview.stopRefresh();
		mXlistview.setRefreshTime(DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString());
	}
}
