package com.huiyin.ui.lehuvoucher;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.LehuVoucherListBean.Content;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ViewHolder;

public class ApplyListViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Content> list;

	public ApplyListViewAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<Content>();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.lehu_voucher_apply_fragment_list_item, parent, false);
		}

		TextView time = ViewHolder.get(convertView, R.id.lehu_voucherlist_item_apply_time);
		TextView type = ViewHolder.get(convertView, R.id.lehu_voucherlist_item_type);
		TextView price = ViewHolder.get(convertView, R.id.lehu_voucherlist_item_price);
		TextView code = ViewHolder.get(convertView, R.id.lehu_voucherlist_item_code);
		TextView status = ViewHolder.get(convertView, R.id.lehu_voucherlist_item_status);

		Content temp = list.get(position);

		time.setText("申请时间：" + temp.getAPPLYTIME());
		type.setText("发票类型：" + temp.getTYPENAME());
		price.setText("发票金额：" + MathUtil.priceForAppWithSign(temp.getPRICE()));
		switch (temp.getSTATUS()) {
		case 0:
			code.setVisibility(View.GONE);
			status.setText("未领取");
			status.setTextColor(0xFFDD434D);
			break;
		case 1:
			code.setVisibility(View.VISIBLE);
			code.setText("券编号："+temp.getTICKETCODE());
			status.setText("已领取");
			status.setTextColor(0xFF9E9E9E);
			break;
		case 2:
			code.setVisibility(View.GONE);
			status.setText("拒绝领取");
			status.setTextColor(0xFFDD434D);
			break;
		}
		return convertView;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<Content> temp) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
		}
		notifyDataSetChanged();
	}
}
