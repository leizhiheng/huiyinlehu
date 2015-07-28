package com.huiyin.ui.menberclub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseLazyFragment;
import com.huiyin.bean.ExchangeResultBean;
import com.huiyin.bean.VIPGoodsBean;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.Utils;
import com.huiyin.wight.ConfirmDialog;
import com.huiyin.wight.pulltorefresh.PullToRefreshBase;
import com.huiyin.wight.pulltorefresh.PullToRefreshGridView;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员商品分类fragment
 */
public class ClubItemFragment extends BaseLazyFragment {
	private static final String ARG_PARAM1 = "categoryId";
	public static final int REQUEST = 1;
	private String categoryId;

	private OnFragmentInteractionListener mListener;
	private int pageIndex = 1;
	private LayoutInflater mInflater;
	private List<VIPGoodsBean.GoodsListEntity> mBeanList;
	private PullToRefreshGridView mGridView;
	private VIPGoodsBean mGoodsBean;
	private MyAdapter mMyAdapter;
    private ConfirmDialog mDialog;

    public static ClubItemFragment newInstance(String categoryId) {
		ClubItemFragment fragment = new ClubItemFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, categoryId);
		fragment.setArguments(args);
		return fragment;
	}

	public ClubItemFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			categoryId = getArguments().getString(ARG_PARAM1);
		}
		mInflater = LayoutInflater.from(this.getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_club_item, null);
		mGridView = (PullToRefreshGridView) view
				.findViewById(R.id.gridview_menber_club_all);
		setPullRefreshView();
		return view;
	}

	@Override
	public void onFirstUserVisible() {
		getDataList();
	}

	private void setPullRefreshView() {
		mGridView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
		mGridView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
		mGridView.getLoadingLayoutProxy(false, true).setRefreshingLabel(
				"正在加载。。");
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
					long arg3) {
                if (mBeanList.get(position).getEXCHG_TYPE()==1||mBeanList.get(position).getEXCHG_TYPE()==3) {
                    Intent mIntent=new Intent(getActivity(),ExchangeDetailActivity.class);
                    mIntent.putExtra(ExchangeDetailActivity.PRARMS,mBeanList.get(position).getID()+"");
                    getActivity().startActivityForResult(mIntent, REQUEST);
                } else if (mBeanList.get(position).getEXCHG_TYPE()==2){
                    //乐虎券
                    mDialog = new ConfirmDialog(getActivity(),"兑换该乐虎券","确定","下次再说",new ConfirmDialog.ClickListenerInterface() {
                       @Override
                       public void doConfirm() {
                           submitExchange("2",mBeanList.get(position).getID()+"","1");
                           mDialog.dismiss();
                       }

                       @Override
                       public void doCancel() {
                           mDialog.dismiss();
                       }
                   });
                    mDialog.show();
                }
            }
		});
		mGridView
				.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<GridView> refreshView) {
                        mGridView
                                .getLoadingLayoutProxy(true, false)
                                .setLastUpdatedLabel(
                                        "最近更新: "
                                                + Utils.Long2DateStr_detail(System
                                                .currentTimeMillis()));
                        pageIndex = 1;
                        if (mGoodsBean != null) {
                            mBeanList.clear();
                        }
                        getDataList();
                    }

                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<GridView> refreshView) {
                        if (mGoodsBean != null
                                && mGoodsBean.getPageIndex() < mGoodsBean
                                .getTotalPageNum()) {
                            pageIndex++;
                            getDataList();
                        }
                    }
                });
	}

	/**
	 * activity回调
	 * 
	 */
	public void onActivityCallBack(ExchangeResultBean bean) {
		if (mListener != null) {
			mListener.onFragmentInteraction(bean);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener {
		public void onFragmentInteraction(ExchangeResultBean bean);
	}

	/**
	 * 获取网络数据
	 */
	private void getDataList() {
		if (AppContext.userId != null) {
			RequstClient.clubGoods(AppContext.userId, categoryId, pageIndex
					+ "", new CustomResponseHandler(getActivity()) {
				@Override
				public void onSuccess(String content) {
					mGridView.onRefreshComplete();
					super.onSuccess(content);
					if (LogUtil.isDebug)
						Logger.json(content);
					mGoodsBean = new Gson().fromJson(content,
							VIPGoodsBean.class);
					if (mGoodsBean.getType() != 1) {
						UIHelper.showToast(mGoodsBean.getMsg());
						return;
					}
					update(mGoodsBean);
				}

				@Override
				public void onFinish() {
					super.onFinish();
					mGridView.onRefreshComplete();
				}
			});
		}
	}

	private void update(VIPGoodsBean mGoodsBean) {
		if (mGoodsBean == null || mGoodsBean.getGoodsList() == null)
			return;
		if (mBeanList == null) {
			mBeanList = new ArrayList<VIPGoodsBean.GoodsListEntity>();
		}
		if (mGoodsBean.getPageIndex() == 1) {
			mBeanList.clear();
			mGridView.setMode(PullToRefreshBase.Mode.BOTH);
		}
		for (int i = 0; i < mGoodsBean.getGoodsList().size(); i++) {
			mBeanList.add(mGoodsBean.getGoodsList().get(i));
		}
		if (mMyAdapter == null) {
			mMyAdapter = new MyAdapter(mBeanList);
			mGridView.setAdapter(mMyAdapter);
		} else {
			mMyAdapter.setTempList(mBeanList);
			mMyAdapter.notifyDataSetChanged();
		}
		if (mGoodsBean.getPageIndex() >= mGoodsBean.getTotalPageNum()) {
			mGridView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		}
	}

	/**
	 * 提交兑换的商品
	 *
	 * @param type
	 *            商品类型，1为虚拟商品，3为实物商品
	 * @param giftId
	 *            商品id
	 * @param num
	 *            数量
	 */
	private void submitExchange(String type, String giftId, String num) {
		if (AppContext.userId != null) {
			if (type.equals("2")) { // 兑换乐虎红包
				RequstClient.convertGift(AppContext.userId, giftId, num,
						new CustomResponseHandler(getActivity()) {
							@Override
							public void onSuccess(String content) {
								super.onSuccess(content);
								try {
									JSONObject mObject = new JSONObject(content);
									if (!mObject.getString("type").equals("1")) {
										UIHelper.showToast(mObject
												.getString("msg"));
									} else {
										UIHelper.showToast("兑换成功");
                                        onActivityCallBack(new Gson().fromJson(content,ExchangeResultBean.class));
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
			}
		}
	}

    class MyAdapter extends BaseAdapter {
		List<VIPGoodsBean.GoodsListEntity> mTempList;

		public MyAdapter(List<VIPGoodsBean.GoodsListEntity> mList) {
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
				view = mInflater.inflate(
						R.layout.layout_menber_club_gridview_item, null);
				holder = new ViewHolder();
				// 商品
				holder.vip_layout_goods = (LinearLayout) view
						.findViewById(R.id.vip_layout_goods);
				holder.iv_big = (ImageView) view.findViewById(R.id.iv_big);
				holder.goods_name = (TextView) view
						.findViewById(R.id.goods_name);
				holder.jifen_num = (TextView) view.findViewById(R.id.jifen);
				holder.lehu_price = (TextView) view
						.findViewById(R.id.price_lehu);
				// 虎券
				holder.vip_layout_lehuquan = (LinearLayout) view
						.findViewById(R.id.vip_layout_lehuquan);
				holder.hongbao_title = (TextView) view
						.findViewById(R.id.hongbao_title);
				holder.hongbao_value = (TextView) view
						.findViewById(R.id.hongbao_value);
				holder.change_tip = (TextView) view
						.findViewById(R.id.change_tip);
				holder.click_receive = (ImageView) view
						.findViewById(R.id.click_receive);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.vip_layout_goods.setVisibility(View.GONE);
			holder.vip_layout_lehuquan.setVisibility(View.GONE);
			VIPGoodsBean.GoodsListEntity item = mTempList.get(position);
			switch (item.getEXCHG_TYPE()) {
			case 1:
				// 虚拟礼品
				holder.vip_layout_goods.setVisibility(View.VISIBLE);
				holder.vip_layout_lehuquan.setVisibility(View.GONE);
				ImageManager.LoadWithServer(item.getGIFT_IMG(), holder.iv_big);
				holder.goods_name.setText(item.getGIFT_NAME());
				if (item.getEXCHG_MODE() == 0) {
					// 仅积分兑换
					holder.jifen_num.setText("积分: " + MathUtil.priceForAppRound(item.getEXCHG_INTEGRAL()));
				} else {
					// 积分+金额
					holder.jifen_num.setText("积分: " + MathUtil.priceForAppRound(item.getEXCHG_INTEGRAL())
							+ " + ￥" + item.getADD_AMOUNT() + "元");
				}
				holder.lehu_price.setVisibility(View.GONE);
				break;
			case 2:
				// 乐虎红包（券）
				holder.vip_layout_goods.setVisibility(View.GONE);
				holder.vip_layout_lehuquan.setVisibility(View.VISIBLE);
				holder.hongbao_title.setText(MathUtil.priceForAppRound(item.getEXCHG_INTEGRAL()) + "积分兑换");
				holder.hongbao_value.setText(item.getTICKET_PRICE());
				if (item.getHQ_TYPE()!=null&&item.getHQ_TYPE().equals("1")) {
					// 满减
					holder.change_tip.setText(Html.fromHtml(String.format(
							getResources().getString(R.string.hongbao_manjian),
							item.getTICKET_DEMAND(), item.getTICKET_PRICE())));
				} else {
					// 直接使用
					holder.change_tip.setText(Html.fromHtml(String.format(
							getResources().getString(R.string.hongbao_lijian),
							item.getTICKET_PRICE())));
				}

				break;
			case 3:
				// 实物礼品
				holder.vip_layout_goods.setVisibility(View.VISIBLE);
				holder.vip_layout_lehuquan.setVisibility(View.GONE);
				ImageManager.LoadWithServer(item.getGIFT_IMG(), holder.iv_big);
				holder.goods_name.setText(item.getGIFT_NAME());
				if (item.getEXCHG_MODE() == 0) {
					// 仅积分兑换
					holder.jifen_num.setText("积分: " + MathUtil.priceForAppRound(item.getEXCHG_INTEGRAL()));
				} else {
					// 积分+金额
					holder.jifen_num.setText("积分: " + MathUtil.priceForAppRound(item.getEXCHG_INTEGRAL())
							+ " + ￥" + item.getADD_AMOUNT() + "元");
				}
				holder.lehu_price.setVisibility(View.VISIBLE);
				holder.lehu_price.getPaint().setFlags(
						Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 中划线
				holder.lehu_price.setText("乐虎价: " + item.getGOODS_PRICE());
				break;
			}
			return view;
		}

		public void setTempList(List<VIPGoodsBean.GoodsListEntity> tempList) {
			if (mTempList == null) {
				mTempList = new ArrayList<VIPGoodsBean.GoodsListEntity>();
			}
			mTempList.clear();
			for (int i = 0; i < tempList.size(); i++) {
				mTempList.add(tempList.get(i));
			}
		}

		public List<VIPGoodsBean.GoodsListEntity> getTempList() {
			return mTempList;
		}
	}

	class ViewHolder {
		// 商品
		LinearLayout vip_layout_goods;
		ImageView iv_big;
		TextView goods_name;
		TextView jifen_num;
		TextView lehu_price;
		// 乐虎券
		LinearLayout vip_layout_lehuquan;
		TextView hongbao_title;
		TextView hongbao_value;
		TextView change_tip;
		ImageView click_receive;
	}

}
