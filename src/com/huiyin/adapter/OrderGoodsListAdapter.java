package com.huiyin.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.bean.ShoppingCarStoreBean;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.wight.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justme on 15/5/31. 订单商品列表适配器
 */
public class OrderGoodsListAdapter extends BaseAdapter {

	private Context mContext;
	private List<ShoppingCarStoreBean> list;
	private String[] remarkList;

	public OrderGoodsListAdapter(Context mContext,
			ArrayList<ShoppingCarStoreBean> templist,
			List<RemarksJson> mRemarksJsons) {
		this.mContext = mContext;
		setList(templist, mRemarksJsons);
	}

	public void setList(List<ShoppingCarStoreBean> templist,
			List<RemarksJson> mRemarksJsons) {
		if (list == null) {
			list = new ArrayList<ShoppingCarStoreBean>();
		}
		list.clear();
		for (int i = 0; i < templist.size(); i++) {
			list.add(templist.get(i));
		}

		remarkList = new String[list.size()];
		if (mRemarksJsons != null) {
			setTheResult(mRemarksJsons);
		}
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
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.order_goods_list_item_single, parent, false);
            mHolder=new ViewHolder();
            mHolder.storeLogo = (ImageView) convertView.findViewById(R.id.store_logo);
            mHolder.storeName = (TextView) convertView.findViewById(R.id.store_name);
            mHolder.storeInfoNavigator =(ImageView) convertView.findViewById(R.id.store_info_navigator);
            mHolder.order_goods_list_item_lv = (MyListView) convertView.findViewById(R.id.order_goods_list_item_lv);
            mHolder.remark = (EditText) convertView.findViewById(R.id.comment_remark_edit);
            convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
        }


		final ShoppingCarStoreBean temp = list.get(position);

		if (temp.getStoreId() <= 0) {
            mHolder.storeLogo.setImageResource(R.drawable.lehu);
            mHolder.storeInfoNavigator.setVisibility(View.GONE);
            mHolder.storeName.setOnClickListener(null);
			if (TextUtils.isEmpty(temp.getStoreName())) {
				mHolder.storeName.setText(R.string.lehu_store_name);
			} else {
				mHolder.storeName.setText(temp.getStoreName());
			}
		} else {
			ImageManager.LoadWithServer(temp.getStoreLogo(), mHolder.storeLogo);
            mHolder.storeInfoNavigator.setVisibility(View.VISIBLE);
            mHolder.storeName.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,
							StoreHomeActivity.class);
					intent.putExtra(StoreHomeActivity.STORE_ID, temp.getStoreId());
					mContext.startActivity(intent);
				}
			});
			mHolder.storeName.setText(temp.getStoreName());
		}
        mHolder.order_goods_list_item_lv.setAdapter(new OrderGoodsItemListAdapter(
				mContext, temp.getGoodsList()));
		if (remarkList[position] != null) {
            mHolder.remark.setText(remarkList[position]);
		} else {
            mHolder.remark.setText("");
		}

        mHolder.remark.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				remarkList[position] = s.toString();
			}
		});

		return convertView;
	}

	/*
	 * remarksJson 备注 格式：[{ REMARKS:备注内容,STORE_ID:店铺id }, {
	 * REMARKS:备注内容,STORE_ID:店铺id }]
	 */

	public class RemarksJson {
		String REMARKS;
		String STORE_ID;
	}

	public String getTheResult() {
		if (remarkList.length > 0) {
			Gson gson = new Gson();
			List<RemarksJson> jsonList = new ArrayList<RemarksJson>();
			for (int i = 0; i < remarkList.length; i++) {
				RemarksJson js = new RemarksJson();
				js.STORE_ID = String.valueOf(list.get(i).getStoreId());
				js.REMARKS = remarkList[i] == null ? "" : remarkList[i];
				jsonList.add(js);
			}
			return gson.toJson(jsonList);
		} else {
			return "";
		}
	}

	/**
	 * 将已经填过的备注传进来
	 */
	public void setTheResult(List<RemarksJson> mRemarksJsons) {
		if (mRemarksJsons != null) {
			for (int i = 0; i < mRemarksJsons.size(); i++) {
				remarkList[i] = mRemarksJsons.get(i).REMARKS;
			}
		}
	}

    class ViewHolder{
        ImageView storeLogo;
        TextView storeName;
        ImageView storeInfoNavigator;
        MyListView order_goods_list_item_lv;
        EditText remark;
    }

}