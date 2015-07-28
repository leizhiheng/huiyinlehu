package com.huiyin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.ProductListBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.ViewHolder;

public class ClassSortListAdapter extends BaseAdapter {

	private ProductListBean bean;
	private Context mContext;

	private boolean isTable;

	public ClassSortListAdapter(ProductListBean bean, Context mContext,boolean isTable) {
		this.bean = bean;
		this.mContext = mContext;
		this.isTable = isTable;
	}

	@Override
	public int getCount() {
		if (isTable) {
			return bean.goodsList == null ? 0 : (bean.goodsList.size() + 1) / 2;
		}
		return bean.goodsList == null ? 0 : bean.goodsList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return bean.goodsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (isTable) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.class_sort_list1_img_mode, parent, false);
			}
			LinearLayout class_sort_list1_img_mode_layout1 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_layout1);
			ImageView class_sort_list1_img_mode_iv1 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_iv1);
			TextView class_sort_list1_img_mode_name1 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_name1);
			TextView class_sort_list1_img_mode_jiage1 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_jiage1);

			LinearLayout class_sort_list1_img_mode_layout2 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_layout2);
			ImageView class_sort_list1_img_mode_iv2 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_iv2);
			TextView class_sort_list1_img_mode_name2 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_name2);
			TextView class_sort_list1_img_mode_jiage2 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_jiage2);

            ProductListBean.GoodList temp = bean.goodsList.get(position*2);
			ImageManager.LoadWithServer(temp.GOODS_IMG, class_sort_list1_img_mode_iv1);
			class_sort_list1_img_mode_name1.setText(temp.GOODS_NAME);
//			if (temp.MARK != -1) {
//				class_sort_list1_img_mode_jiage1.setText(MathUtil.priceForAppWithSign(temp.PROMOTIONS_PRICE));
//			} else {
				class_sort_list1_img_mode_jiage1.setText(MathUtil.priceForAppWithSign(temp.PRICE));
//			}

			class_sort_list1_img_mode_layout1.setOnClickListener(new ClickListener(temp));

			if (bean.goodsList.size() > position*2 + 1) {
				class_sort_list1_img_mode_layout2.setVisibility(View.VISIBLE);
                ProductListBean.GoodList temp2 = bean.goodsList.get(position*2 + 1);
				ImageManager.LoadWithServer(temp2.GOODS_IMG, class_sort_list1_img_mode_iv2);
				class_sort_list1_img_mode_name2.setText(temp2.GOODS_NAME);
//				if (temp2.MARK != -1) {
//					class_sort_list1_img_mode_jiage2.setText(MathUtil.priceForAppWithSign(temp2.PROMOTIONS_PRICE));
//				} else {
					class_sort_list1_img_mode_jiage2.setText(MathUtil.priceForAppWithSign(temp2.PRICE));
//				}
				class_sort_list1_img_mode_layout2.setOnClickListener(new ClickListener(temp2));
			} else {
				class_sort_list1_img_mode_layout2.setVisibility(View.INVISIBLE);
				class_sort_list1_img_mode_layout2.setOnClickListener(null);
			}

		} else {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.class_sort_list1_normal_mode, parent, false);
			}

			RelativeLayout class_sort_list1_normal = ViewHolder.get(convertView, R.id.class_sort_list1_normal);
			ImageView class_sort_list1_ll_iv = ViewHolder.get(convertView, R.id.class_sort_list1_ll_iv);
			TextView class_sort_list1_ll_title = ViewHolder.get(convertView, R.id.class_sort_list1_ll_title);
			TextView class_sort_list1_ll_jiage1 = ViewHolder.get(convertView, R.id.class_sort_list1_ll_jiage1);
			TextView class_sort_list1_ll_jiage2 = ViewHolder.get(convertView, R.id.class_sort_list1_ll_jiage2);
			TextView class_sort_list1_ll_haopingdu = ViewHolder.get(convertView, R.id.class_sort_list1_ll_haopingdu);

            ProductListBean.GoodList temp = bean.goodsList.get(position);

			ImageManager.LoadWithServer(temp.GOODS_IMG, class_sort_list1_ll_iv);
			class_sort_list1_ll_title.setText(temp.GOODS_NAME);
//			if (temp.MARK != -1) {
//				class_sort_list1_ll_jiage1.setText(MathUtil.priceForAppWithSign(temp.PROMOTIONS_PRICE));
//			} else {
            class_sort_list1_ll_jiage1.setText(MathUtil.priceForAppWithSign(temp.PRICE));
//			}
            class_sort_list1_ll_haopingdu.setText(temp.REVIEW_PERCENT + "%");
			if (StringUtils.isBlank(temp.REVIEW_NUMBER)) {
				String hh = class_sort_list1_ll_haopingdu.getText().toString();
				class_sort_list1_ll_haopingdu.setText(hh + "(0人)");
			} else {
				String hh = class_sort_list1_ll_haopingdu.getText().toString();
				class_sort_list1_ll_haopingdu.setText(hh + "(" + temp.REVIEW_NUMBER + "人)");
			}
//			// 给textview中间加横线
//			class_sort_list1_ll_jiage2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//			// 参考价
//			class_sort_list1_ll_jiage2.setText(MathUtil.priceForAppWithSign(temp.REFERENCE_PRICE));

			class_sort_list1_normal.setOnClickListener(new ClickListener(temp));
		}
		return convertView;
	}

	public void ChangeView(boolean isTable) {
		this.isTable = isTable;
		notifyDataSetChanged();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return isTable ? 1 : 0;
	}

	public class ClickListener implements View.OnClickListener {
		private ProductListBean.GoodList temp;

		public ClickListener(ProductListBean.GoodList temp) {
			this.temp = temp;
		}

		@Override
		public void onClick(View v) {
//			Intent intent = new Intent(mContext, ProductsDetailActivity.class);
//			String goods_detail_id = temp.GOODS_ID;
//			if (goods_detail_id.equals("")) {
//				return;
//			}
//			intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, goods_detail_id);
//			mContext.startActivity(intent);
//
            Intent intent = new Intent(mContext, ProductsDetailActivity.class);
            String goods_detail_id = temp.GOODS_ID;
            String STORE_ID=temp.STORE_ID;//店铺id
            String GOODS_NO=temp.GOODS_NO;//商品编号
            //商品编号
            if (goods_detail_id.equals("")) {
                return;
            }
            //商品编号
            if (STORE_ID.equals("")) {
                return;
            }
            //商品编号
            if (GOODS_NO.equals("")) {
                return;
            }
            intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, goods_detail_id);
            intent.putExtra(ProductsDetailActivity.STORE_ID, STORE_ID);
            intent.putExtra(ProductsDetailActivity.GOODS_NO, GOODS_NO);
            mContext.startActivity(intent);
		}

	}
	// 一堆垃圾。

	// private class ViewHolder {
	// ImageView class_sort_list1_ll_iv;
	// TextView class_sort_list1_ll_title;
	// TextView class_sort_list1_ll_jiage1;
	// TextView class_sort_list1_ll_jiage2;
	// TextView class_sort_list1_ll_haopingdu;
	// }

	// ViewHolder holder = null;
	// if (v != null) {
	// holder = (ViewHolder) v.getTag();
	// } else {
	// holder = new ViewHolder();
	// v =
	// LayoutInflater.from(mContext).inflate(R.layout.class_sort_list1_normal_mode,
	// null);
	// holder.class_sort_list1_ll_iv = (ImageView)
	// v.findViewById(R.id.class_sort_list1_ll_iv);
	// holder.class_sort_list1_ll_title = (TextView)
	// v.findViewById(R.id.class_sort_list1_ll_title);
	// holder.class_sort_list1_ll_jiage1 = (TextView)
	// v.findViewById(R.id.class_sort_list1_ll_jiage1);
	// holder.class_sort_list1_ll_jiage2 = (TextView)
	// v.findViewById(R.id.class_sort_list1_ll_jiage2);
	// holder.class_sort_list1_ll_haopingdu = (TextView)
	// v.findViewById(R.id.class_sort_list1_ll_haopingdu);
	// v.setTag(holder);
	// }
	//
	// ImageManager.LoadWithServer(bean.commodityList.get(arg0).COMMODITY_IMAGE_PATH,
	// holder.class_sort_list1_ll_iv);
	// holder.class_sort_list1_ll_title.setText(bean.commodityList.get(arg0).COMMODITY_NAME);
	// if (bean.commodityList.get(arg0).MARK != -1) {
	// holder.class_sort_list1_ll_jiage1.setText(MathUtil.priceForAppWithSign(bean.commodityList.get(arg0).PROMOTIONS_PRICE));
	// } else {
	// holder.class_sort_list1_ll_jiage1.setText(MathUtil.priceForAppWithSign(bean.commodityList.get(arg0).PRICE));
	// }
	// if (bean.commodityList.get(arg0).COUNTREVIEW != null &&
	// MathUtil.stringToFloat(bean.commodityList.get(arg0).COUNTREVIEW) != 0) {
	// holder.class_sort_list1_ll_haopingdu.setText(MathUtil.keepMost2Decimal(MathUtil.stringToFloat(bean.commodityList.get(arg0).COUNTREVIEW)
	// * 100.0f) + "%");
	// } else {
	// holder.class_sort_list1_ll_haopingdu.setText("100%");
	// }
	// if (StringUtils.isBlank(bean.commodityList.get(arg0).REVIEW_NUMBER)) {
	// String hh = holder.class_sort_list1_ll_haopingdu.getText().toString();
	// holder.class_sort_list1_ll_haopingdu.setText(hh + "(0人)");
	// } else {
	// String hh = holder.class_sort_list1_ll_haopingdu.getText().toString();
	// holder.class_sort_list1_ll_haopingdu.setText(hh + "(" +
	// bean.commodityList.get(arg0).REVIEW_NUMBER + "人)");
	// }
	// // 给textview中间加横线
	// holder.class_sort_list1_ll_jiage2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
	// // 参考价
	// holder.class_sort_list1_ll_jiage2.setText(MathUtil.priceForAppWithSign(bean.commodityList.get(arg0).REFERENCE_PRICE));
}
