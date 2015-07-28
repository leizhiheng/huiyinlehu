package com.huiyin.ui.home;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseLazyFragment;
import com.huiyin.bean.WaterPayRecoderBean;
import com.huiyin.utils.ResourceUtils;
import com.huiyin.utils.Utils;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase;
import com.huiyin.wight.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class WaterPayRecoderFragment extends BaseLazyFragment {
	private PullToRefreshListView mListView;
	private int pageIndex = 1;
	private List<WaterPayRecoderBean.ShareBillDataEntity> mBeanList;
	private WaterPayRecoderBean mRecoderBean;
    private MyRecoderAdapter mMyAdapter;

	public WaterPayRecoderFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View Root = inflater.inflate(R.layout.fragment_water_pay_recoder,
				container, false);
		bindViews(Root);
		setListener();
		return Root;
	}

    @Override
    public void onFirstUserInvisible() {
        getDataList();
    }

    private void init() {
        if (mRecoderBean == null || mRecoderBean.getShareBillData() == null)
            return;
        if (mBeanList == null) {
            mBeanList = new ArrayList<WaterPayRecoderBean.ShareBillDataEntity>();
        }
        if (mRecoderBean.getPageIndex() == 1) {
            mBeanList.clear();
            mListView.setMode(PullToRefreshBase.Mode.BOTH);
        }
        for (int i = 0; i < mRecoderBean.getShareBillData().size(); i++) {
            mBeanList.add(mRecoderBean.getShareBillData().get(i));
        }
        if (mMyAdapter == null) {
            mMyAdapter = new MyRecoderAdapter(mBeanList);
            mListView.setAdapter(mMyAdapter);
        } else {
            mMyAdapter.setTempList(mBeanList);
            mMyAdapter.notifyDataSetChanged();
        }
        if (mRecoderBean.getPageIndex() >= mRecoderBean.getTotalPageNum()) {
            mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
	}

	private void setListener() {
		mListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		mListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
		mListView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				"正在加载。。");
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {

			}
		});
		mListView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						mListView
								.getLoadingLayoutProxy(true, false)
								.setLastUpdatedLabel(
                                        "最近更新: "
                                                + Utils.Long2DateStr_detail(System
                                                .currentTimeMillis()));
						pageIndex = 1;
						if (mRecoderBean != null) {
							mBeanList.clear();
						}
						getDataList();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						if (mRecoderBean != null
								&& mRecoderBean.getPageIndex() < mRecoderBean
										.getTotalPageNum()) {
							pageIndex++;
							getDataList();
						}
					}
				});
	}

	private void bindViews(View root) {
		mListView = (PullToRefreshListView) root.findViewById(R.id.water_pay_recoder);
	}

	public void getDataList() {
		if (AppContext.userId != null) {
			RequstClient.shareBillData(AppContext.userId, pageIndex, "0",
					new CustomResponseHandler(getActivity()) {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							mListView.onRefreshComplete();
							mRecoderBean = new Gson().fromJson(content,
									WaterPayRecoderBean.class);
							if (mRecoderBean.getType() != 1) {
								UIHelper.showToast(mRecoderBean.getMsg());
							} else {
								if (mRecoderBean.getShareBillData() != null) {
									init();
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

	class MyRecoderAdapter extends BaseAdapter {
		List<WaterPayRecoderBean.ShareBillDataEntity> mTempList;

		public MyRecoderAdapter(
				List<WaterPayRecoderBean.ShareBillDataEntity> mList) {
			setTempList(mList);
		}

		@Override
		public int getCount() {
			return mTempList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mTempList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			View view = arg1;
			ViewHolder holder;
			if (view == null) {
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.fragment_water_pay_recoder_item, null);
				holder = new ViewHolder();
				holder.recoder_type = (ImageView) view
						.findViewById(R.id.recoder_type);
				holder.recoder_time = (TextView) view
						.findViewById(R.id.recoder_time);
				holder.recoder_status = (TextView) view
						.findViewById(R.id.recoder_status);
				holder.recoder_user_num = (TextView) view
						.findViewById(R.id.recoder_user_num);
				holder.recoder_order_num = (TextView) view
						.findViewById(R.id.recoder_order_num);
				holder.recoder_price = (TextView) view
						.findViewById(R.id.recoder_price);
				holder.recoder_remark = (TextView) view
						.findViewById(R.id.recoder_remark);
				holder.line_middle = view.findViewById(R.id.line_middle);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			WaterPayRecoderBean.ShareBillDataEntity item = mTempList
					.get(position);
            if(item==null)return view;
			holder.recoder_time.setText("缴费日期:" + item.getPAYTIME());
			holder.recoder_user_num.setText(Html.fromHtml("缴费户号: " + ResourceUtils.changeStringColor("#000000", item.getUSERNO())));
			holder.recoder_order_num.setText(Html.fromHtml("平台流水号: " + ResourceUtils.changeStringColor("#000000",  item.getPAYNBR())));
			holder.recoder_price.setText("￥" + item.getBLANCE());
			holder.recoder_remark.setVisibility(View.GONE);
			holder.line_middle.setVisibility(View.GONE);
			if (item.getPAYSTATUS() == 0) {
				// 缴费成功
				holder.recoder_status.setText("缴费成功");
                holder.recoder_status.setTextColor(getResources().getColor(R.color.red));
			} else if (item.getPAYSTATUS() == 1) {
				// 缴费失败
				holder.recoder_status.setText("缴费失败");
                holder.recoder_status.setTextColor(getResources().getColor(R.color.black));
                if (!TextUtils.isEmpty(item.getREMARK())){
                    holder.recoder_remark.setText(item.getREMARK());
                    holder.recoder_remark.setVisibility(View.VISIBLE);
                    holder.line_middle.setVisibility(View.VISIBLE);
                }
			} else {
                // 退款成功
				holder.recoder_status.setText("退款成功");
                holder.recoder_status.setTextColor(getResources().getColor(R.color.red));
			}

			if (item.getPAYITEM().equals("0")) {
				// 电费
                holder.recoder_type.setImageResource(R.drawable.df);
			} else if (item.getPAYITEM().equals("1")) {
				// 水费
                holder.recoder_type.setImageResource(R.drawable.sf);
			} else {
				// 燃气费
                holder.recoder_type.setImageResource(R.drawable.mqf);
			}
			return view;
		}

		public void setTempList(
				List<WaterPayRecoderBean.ShareBillDataEntity> tempList) {
			if (mTempList == null) {
				mTempList = new ArrayList<WaterPayRecoderBean.ShareBillDataEntity>();
			}
			mTempList.clear();
			for (int i = 0; i < tempList.size(); i++) {
				mTempList.add(tempList.get(i));
			}
		}

		public List<WaterPayRecoderBean.ShareBillDataEntity> getTempList() {
			return mTempList;
		}
	}

	class ViewHolder {
		ImageView recoder_type;
		TextView recoder_time, recoder_status, recoder_user_num,
				recoder_order_num, recoder_price, recoder_remark;
		View line_middle;
	}
}
