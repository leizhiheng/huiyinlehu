package com.huiyin.ui.classic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.adapter.HlistViewAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.TjdpPopBean;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.PopupWindowUtils;
import com.huiyin.utils.ResourceUtils;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择推荐搭配
 */
public class TjdpPopSelect {
	private final SelectResultListener listener;
	private final String goodsNo;
	private String goodsId;
	private String storeId;
	private String name;
	private String price;
	private String imgUrl;
	private String num;
	private Context context;
	private View contentview;
	private ListView mListView;
	private PopupWindowUtils popupWindowUtils;
	private MyDapeiAdpter mAdpter;
	private Handler mHandler;
	private TjdpPopBean mTjdpPopBean;
	private ImageView pop_select_image;
	private TextView pop_select_select_title, pop_select_select_num,
			pop_select_total_price;
	private Button pop_select_buy_now, pop_select_add_shoppingcar;
	private com.huiyin.ui.classic.HorizontialListView pop_select_list_title;
	private HlistViewAdapter mHlistViewAdapter;
	private List<String> mTitles;
	private List<TjdpPopBean.GoodsRecommendEntity.ListEntity> listAll;

	public TjdpPopSelect(Context context, View target, String goodsId,
			String goodsNo, String storeId, String name, String price,
			String imgUrl, String num, SelectResultListener listener) {
		this.context = context;
		this.listener = listener;
		this.goodsId = goodsId;
		this.goodsNo = goodsNo;
		this.storeId = storeId;
		// 测试数据
		// this.goodsNo = "5213";
		// this.storeId = "71";
		this.name = name;
		this.price = price;
		this.imgUrl = imgUrl;
		this.num = num;
		contentview = LayoutInflater.from(context).inflate(
				R.layout.layout_pop_select_tjpj, null);
		popupWindowUtils = new PopupWindowUtils(context, contentview, target);
		init();
		setListener();
		loadData();
	}

	private void init() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (popupWindowUtils != null) {
					popupWindowUtils.getPopupWindow().dismiss();
				}
			}
		};
		pop_select_total_price = (TextView) contentview
				.findViewById(R.id.pop_select_total_price);
		pop_select_select_num = (TextView) contentview
				.findViewById(R.id.pop_select_select_num);
		pop_select_select_title = (TextView) contentview
				.findViewById(R.id.pop_select_select_title);
		pop_select_image = (ImageView) contentview
				.findViewById(R.id.pop_select_image);
		pop_select_list_title = (HorizontialListView) contentview
				.findViewById(R.id.pop_select_list_title);
		pop_select_buy_now = (Button) contentview
				.findViewById(R.id.pop_select_buy_now);
		pop_select_add_shoppingcar = (Button) contentview
				.findViewById(R.id.pop_select_add_shoppingcar);
		mListView = (ListView) contentview
				.findViewById(R.id.pop_select_list_content);

		ImageManager.LoadWithServer(imgUrl, pop_select_image);
		pop_select_select_title.setText(Html.fromHtml("已搭配"
				+ ResourceUtils.changeStringColor("#dd434d", "0") + "件"));
		pop_select_total_price.setText(MathUtil.priceForAppWithSign(price));
	}

	private void initList() {
		listAll = queryTypeList(-1);
		mAdpter = new MyDapeiAdpter(context, listAll);
		mListView.setAdapter(mAdpter);
		mHlistViewAdapter = new HlistViewAdapter(context, getListTitle(), "全部");
		pop_select_list_title.setAdapter(mHlistViewAdapter);

	}

	private void setListener() {

		popupWindowUtils.getPopupWindow().setOnDismissListener(
				new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						WindowManager.LayoutParams lp = ((Activity) context)
								.getWindow().getAttributes();
						lp.alpha = 1; // 0.0-1.0
						((Activity) context).getWindow().setAttributes(lp);
						if (null != listener) {
							listener.selectResult(null);// 回掉接口
						}
					}
				});

		pop_select_list_title
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (mHlistViewAdapter != null && mTitles != null) {
							mHlistViewAdapter.setSelect(mTitles.get(position));
							mHlistViewAdapter.notifyDataSetChanged();
							if (mAdpter != null) {
								mAdpter.setList(queryTypeList(position - 1));
								mAdpter.notifyDataSetChanged();
							}
						}
					}
				});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listItemListener(position);
			}
		});

		pop_select_buy_now.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		pop_select_add_shoppingcar
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						addCarts("-1", getResultHashMap());
					}
				});
	}

	/**
	 * 获得主商品和配件勾选的结果
	 * 
	 * @return
	 */
	private List<AddCarsParamsBean> getResultHashMap() {
		List<AddCarsParamsBean> result = null;
		if (mAdpter != null && mAdpter.getSelected().size() > 0) {
			result = new ArrayList<AddCarsParamsBean>();
			// 添加主商品
			AddCarsParamsBean main = new AddCarsParamsBean();
			main.goodsId = this.goodsId;
			main.goodsNo = this.goodsNo;
			main.num = this.num;
			result.add(main);
			for (int i = 0; i < mAdpter.getSelected().size(); i++) {
				AddCarsParamsBean item = new AddCarsParamsBean();
				item.goodsId = mAdpter.getSelected().get(i);
				item.goodsNo = getGoodsNoById(mAdpter.getSelected().get(i));
				item.num = "1";
				result.add(item);
			}
		} else {
			UIHelper.showToast("请选择搭配商品");
		}
		return result;
	}

	/**
	 * 根据商品id获得商品编号
	 * 
	 * @param goodsId
	 *            商品id
	 * @return
	 */
	private String getGoodsNoById(String goodsId) {
		if (mTjdpPopBean == null || mTjdpPopBean.goodsRecommend == null
				|| mTjdpPopBean.goodsRecommend.size() < 1)
			return "";
		for (int i = 0; i < mTjdpPopBean.goodsRecommend.size(); i++) {
			for (int j = 0; j < mTjdpPopBean.goodsRecommend.get(i).list.size(); j++) {
				if (goodsId.equals(mTjdpPopBean.goodsRecommend.get(i).list
						.get(j).GOODS_ID + "")) {
					return mTjdpPopBean.goodsRecommend.get(i).list.get(j).GOODS_NO;
				}
			}
		}
		return "";
	}

	/**
	 * 获得需要展示的数据
	 * 
	 * @param position
	 *            -1为全部
	 * @return
	 */
	private List<TjdpPopBean.GoodsRecommendEntity.ListEntity> queryTypeList(
			int position) {
		List<TjdpPopBean.GoodsRecommendEntity.ListEntity> temp = new ArrayList<TjdpPopBean.GoodsRecommendEntity.ListEntity>();
		if (mTjdpPopBean == null || mTjdpPopBean.goodsRecommend == null
				|| mTjdpPopBean.goodsRecommend.size() < 1)
			return temp;
		if (position == -1) {

			for (int i = 0; i < mTjdpPopBean.goodsRecommend.size(); i++) {
				for (int j = 0; j < mTjdpPopBean.goodsRecommend.get(i).list
						.size(); j++) {
					temp.add(mTjdpPopBean.goodsRecommend.get(i).list.get(j));
				}
			}
			return temp;
		} else {
			for (int j = 0; j < mTjdpPopBean.goodsRecommend.get(position).list
					.size(); j++) {
				temp.add(mTjdpPopBean.goodsRecommend.get(position).list.get(j));
			}
			return temp;
		}
	}

	/**
	 * 提取水平的分类信息
	 * 
	 * @return
	 */
	private List<String> getListTitle() {
		mTitles = new ArrayList<String>();
		if (mTjdpPopBean == null || mTjdpPopBean.goodsRecommend == null
				|| mTjdpPopBean.goodsRecommend.size() < 1)
			return mTitles;
		if (mTjdpPopBean != null && mTjdpPopBean.goodsRecommend != null
				&& mTjdpPopBean.goodsRecommend.size() > 0) {
			mTitles.add("全部");
			for (int i = 0; i < mTjdpPopBean.goodsRecommend.size(); i++) {
				mTitles.add(mTjdpPopBean.goodsRecommend.get(i).NAME);
			}
		}
		return mTitles;
	}

	private void listItemListener(int position) {
		if (mAdpter.getList().get(position) != null) {
			Intent intent = new Intent(context, ProductsDetailActivity.class);
			intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, mAdpter
					.getList().get(position).GOODS_ID + "");
			context.startActivity(intent);
		}
	}

	/**
	 * 获取推荐搭配信息
	 */
	private void loadData() {
		RequstClient.goodsRecommend(goodsNo, storeId,
				new MyCustomResponseHandler(context) {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						mTjdpPopBean = new Gson().fromJson(content,
								TjdpPopBean.class);
						if (mTjdpPopBean.type != 1) {
							UIHelper.showToast(mTjdpPopBean.msg);
						} else {
							if (mTjdpPopBean.goodsRecommend != null
									&& mTjdpPopBean.goodsRecommend.size() > 0) {
								initList();
							}
						}
					}
				});
	}

	/**
	 * 延迟dismiss
	 */
	private void dismiss() {
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(0, 200);
		}
	}

	/**
	 * 加入多个商品到购物车
	 * 
	 * @param shoppingIds
	 *            如果userId为空，则传入本地缓存的购物车id
	 * @param paramsBeans
	 *            加入购物车商品的集合
	 */
	public void addCarts(String shoppingIds, List<AddCarsParamsBean> paramsBeans) {
		if (StringUtils.isBlank(AppContext.userId)) {
			Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(context, LoginActivity.class);
			intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
			context.startActivity(intent);
			return;
		}
		if (paramsBeans != null) {
			String userId = AppContext.userId == null ? "-1"
					: AppContext.userId;
			String json = new Gson().toJson(paramsBeans);
			Logger.json(json);
			RequstClient.addCarts(userId, shoppingIds, json,
					new CustomResponseHandler(context) {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							try {
								JSONObject object = new JSONObject(content);
								if (object.optInt("type") != 1) {
									UIHelper.showToast(object.optString("msg"));
								} else {
									UIHelper.showToast(object.optString("msg"));
									int num = object.getInt("SHOPPING_CAR_NUM");
									if (listener != null) {
										listener.addShoppingCar(num);
									}
									dismiss();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

	/**
	 * 改变搭配的信息
	 * 
	 * @param result
	 *            已选搭配结果
	 */
	public void changeShowSelected(List<String> result) {
		int select_num = result.size();
		double allPrice = Utils.anDouble(price)*Utils.anDouble(num);
		for (int i = 0; i < listAll.size(); i++) {
			if (result.contains(listAll.get(i).GOODS_ID + "")) {
				allPrice += Utils.anDouble(listAll.get(i).GOODS_PRICE);
			}
		}
		pop_select_select_title.setText(Html.fromHtml("已搭配"
				+ ResourceUtils.changeStringColor("#dd434d", select_num + "") + "件"));
		pop_select_total_price.setText(Html.fromHtml(ResourceUtils
				.changeStringColor("#999999", "搭配价格: ")
				+ ResourceUtils.changeStringColor("#dd434d",
						MathUtil.priceForAppWithSign(allPrice))));
    }

	public interface SelectResultListener {
		void buyNow(HashMap<String, String> result);

		void addShoppingCar(int num);

		void selectResult(List<String> result);
	}

	/**
	 * 搭配list的适配器
	 */
	private class MyDapeiAdpter extends BaseAdapter {
		List<TjdpPopBean.GoodsRecommendEntity.ListEntity> list;
		List<String> selected;
		Context mContext;

		public MyDapeiAdpter(Context context,
				List<TjdpPopBean.GoodsRecommendEntity.ListEntity> templist) {
			mContext = context;
			setList(templist);
			if (selected == null) {
				selected = new ArrayList<String>();
			}
		}

		public void setList(
				List<TjdpPopBean.GoodsRecommendEntity.ListEntity> templist) {

			if (list == null) {
				list = new ArrayList<TjdpPopBean.GoodsRecommendEntity.ListEntity>();
			}
			if (templist != null) {
				list.clear();
				for (int i = 0; i < templist.size(); i++) {
					list.add(templist.get(i));
				}
			}
		}

		public List<TjdpPopBean.GoodsRecommendEntity.ListEntity> getList() {
			return list;
		}

		private void addSelected(String tempSelected) {

			if (!TextUtils.isEmpty(tempSelected)) {
				selected.add(tempSelected);
			}
		}

		private void removeSelected(String tempSelected) {
			if (!TextUtils.isEmpty(tempSelected)) {
				selected.remove(tempSelected);
			}
		}

		private List<String> getSelected() {
			return selected;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			MyHolder holder;
			if (null == convertView) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.my_product_recommed_item, null);
				holder = new MyHolder();
				holder.my_recommoned_item_title = (TextView) convertView
						.findViewById(R.id.my_recommoned_item_title);
				holder.my_recommoned_item_price = (TextView) convertView
						.findViewById(R.id.my_recommoned_item_price);
				holder.my_recommoned_item_stock = (TextView) convertView
						.findViewById(R.id.my_recommoned_item_stock);
				holder.my_recommoned_item_image = (ImageView) convertView
						.findViewById(R.id.my_recommoned_item_image);
				holder.my_recommoned_item_check = (ImageView) convertView
						.findViewById(R.id.my_recommoned_item_check);
				convertView.setTag(holder);
			} else
				holder = (MyHolder) convertView.getTag();
			if (list.get(position) == null) {
				return convertView;
			}
			holder.my_recommoned_item_title
					.setText(list.get(position).GOODS_NAME);
			holder.my_recommoned_item_price.setText(MathUtil
					.priceForAppWithSign(list.get(position).GOODS_PRICE));

			ImageManager.LoadWithServer(list.get(position).GOODS_IMG,
					holder.my_recommoned_item_image);
			holder.my_recommoned_item_check.setVisibility(View.VISIBLE);
			if (list.get(position).GOODS_STOCK > 0) {
				holder.my_recommoned_item_check.setVisibility(View.VISIBLE);
				holder.my_recommoned_item_stock.setText("");
				if (selected.contains(list.get(position).GOODS_ID + "")) {
					holder.my_recommoned_item_check
							.setImageResource(R.drawable.checkbox_gray_true);
				} else {
					holder.my_recommoned_item_check
							.setImageResource(R.drawable.checkbox_gray_false);
				}
				holder.my_recommoned_item_check
						.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (selected.contains(list.get(position).GOODS_ID
                                        + "")) {
                                    removeSelected(list.get(position).GOODS_ID
                                            + "");
                                } else {
                                    addSelected(list.get(position).GOODS_ID
                                            + "");
                                }
                                notifyDataSetChanged();
                                if (listener != null) {
                                    listener.selectResult(selected);
                                }
                                changeShowSelected(selected);

                            }
                        });
			} else {
				holder.my_recommoned_item_stock.setText("无货");
				holder.my_recommoned_item_check.setVisibility(View.GONE);
			}

			return convertView;
		}

		class MyHolder {
			TextView my_recommoned_item_title, my_recommoned_item_price,
					my_recommoned_item_stock;
			ImageView my_recommoned_item_image, my_recommoned_item_check;
		}
	}
}
