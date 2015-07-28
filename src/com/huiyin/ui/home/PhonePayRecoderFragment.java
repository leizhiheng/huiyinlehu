package com.huiyin.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.PhonePayRecordAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseLazyFragment;
import com.huiyin.bean.WaterPayRecoderBean;
import com.huiyin.utils.Utils;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase;
import com.huiyin.wight.pulltorefresh.PullToRefreshListView;

/**
 * 话费-缴费记录
 * 
 * @author 刘远祺
 * 
 * @todo TODO
 * 
 * @date 2015-7-8
 */
public class PhonePayRecoderFragment extends BaseLazyFragment { 

	/**ListView**/
	private PullToRefreshListView mListView;

	/**缴费记录适配器**/
	private PhonePayRecordAdapter adapter;

	/**页码**/
	private int pageIndex = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View Root = inflater.inflate(R.layout.fragment_water_pay_recoder, container, false);
		bindViews(Root);
		setListener();
		return Root;
	}

	@Override
	public void onFirstUserInvisible() {
		getDataList();
	}

	private void setListener() {
		mListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		mListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
		mListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载。。");
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

			}
		});
		mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				mListView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel("最近更新: " + Utils.Long2DateStr_detail(System.currentTimeMillis()));
				pageIndex = 1;
				
				getDataList();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				getDataList();
			}
		});
	}

	/**
	 * 绑定控件
	 * @param root
	 */
	private void bindViews(View root) {
		mListView = (PullToRefreshListView) root.findViewById(R.id.water_pay_recoder);
		adapter = new PhonePayRecordAdapter(getActivity());
		mListView.setAdapter(adapter);
	}

	/**
	 * 获取充值记录
	 */
	public void getDataList() {
		if (AppContext.userId != null) {
			RequstClient.shareBillData(AppContext.userId, pageIndex , "3", new CustomResponseHandler(getActivity()) {
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					mListView.onRefreshComplete();
					
					if(isSuccessResponse(content)){
						
						WaterPayRecoderBean tempData = new Gson().fromJson(content, WaterPayRecoderBean.class);
						if(null != tempData && tempData.getShareBillData().size() > 0){
							pageIndex++;
						}
						
						//首页数据
						if(pageIndex == 1){
							adapter.refreshData(tempData.getShareBillData());
							mListView.setMode(PullToRefreshBase.Mode.BOTH);
							
						}else{
							
							//下拉数据
							adapter.appendData(tempData.getShareBillData());
							
							if (tempData.getPageIndex() >= tempData.getTotalPageNum()) {
					            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
					        }
						}
					}
				}

				@Override
				public void onFinish() {
					super.onFinish();
					mListView.onRefreshComplete();
				}
			});
		}
	}
}
