package com.huiyin.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;

import java.util.ArrayList;
import java.util.List;

public class HlistViewAdapter extends BaseAdapter {
	private Context mContext;
	private List<String> mData;
	private String select = "";

	public HlistViewAdapter(Context context, List<String> data) {
		this(context, data, "");
	}

	public HlistViewAdapter(Context context, List<String> data, String selected) {
		mContext = context;
		setData(data);
		select = selected;
	}

	public void setData(List<String> tempData) {
		if (mData == null) {
			mData = new ArrayList<String>();
		}
		if (tempData != null) {
			for (int i = 0; i < tempData.size(); i++) {
				mData.add(tempData.get(i));
			}
		}

	}

	public void setSelect(String select) {
		if (!TextUtils.isEmpty(select)) {
			this.select = select;
		}
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MyHolderHlv myHolderHlv;
		if (null == convertView) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_hlv_item, null);
			myHolderHlv = new MyHolderHlv();
			myHolderHlv.tv_hlv = (TextView) convertView
					.findViewById(R.id.tv_hlv);
			convertView.setTag(myHolderHlv);
		} else
			myHolderHlv = (MyHolderHlv) convertView.getTag();
		if (mData.get(position) == null)
			return convertView;
		if (mData.get(position).equals(select)) {// 选中
			myHolderHlv.tv_hlv.setTextColor(mContext.getResources().getColor(
					R.color.red));
		} else {
			myHolderHlv.tv_hlv.setTextColor(mContext.getResources().getColor(
					R.color.black));
		}
		myHolderHlv.tv_hlv.setText(mData.get(position));
		return convertView;
	}

	static class MyHolderHlv {
		TextView tv_hlv;
	}
}
