package com.huiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.LehuQuanBean;
import com.huiyin.utils.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class ChooseLehjuanAdapter extends BaseAdapter {

	private Context mContext;
	private String select = "-1";
	private List<LehuQuanBean.TicketListEntity> listDatas;
	public LayoutInflater layoutInflater;

	public ChooseLehjuanAdapter(Context context,
			List<LehuQuanBean.TicketListEntity> listDatas) {
		layoutInflater = LayoutInflater.from(context);
		mContext = context;
		setListDatas(listDatas);
	}

	public void setListDatas(List<LehuQuanBean.TicketListEntity> tempdata) {
		if (listDatas == null) {
			listDatas = new ArrayList<LehuQuanBean.TicketListEntity>();
		}
		clear();
		for (int i = 0; i < tempdata.size(); i++) {
			listDatas.add(tempdata.get(i));
		}
	}

	public void clear() {
		if (listDatas != null)
			listDatas.clear();
	}

    /**
     * 选中时返回true
     */
	public boolean setSelect(String index) {
		if (select.equals(index)) {
			select = "-1";
			return false;
		} else {
			select = index;
			return true;
		}
	}

	public String getSelect() {
		return select;
	}

	@Override
	public int getCount() {
		return null != listDatas ? listDatas.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return listDatas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(
					R.layout.activity_lehujuan_choose_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			holder.checkbox = (ImageView) convertView
					.findViewById(R.id.checkbox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final LehuQuanBean.TicketListEntity bean = listDatas.get(position);
		if (bean.getHQ_TYPE() == 2) {
			// 面值
			holder.name.setText("乐虎优惠劵面值"
					+ MathUtil.priceForAppWithSign(bean.getCONDITION2()) + "");
		} else {
			holder.name.setText("乐虎优惠劵满"
					+ MathUtil.priceForAppWithSign(bean.getCONDITION1()) + "减"
					+ MathUtil.priceForAppWithSign(bean.getCONDITION2()));
		}

		holder.date.setText("");

		if (select.equals(bean.getID() + "")) {
			holder.checkbox.setImageResource(R.drawable.radio_btn_checked);
		} else {
			holder.checkbox.setImageResource(R.drawable.radio_btn_normal);
		}

		return convertView;
	}

	private LehuCheckChange listener;

	public interface LehuCheckChange {
		void onCheckChage(boolean check, int position);
	}

	public void setOnLehuCheckChange(LehuCheckChange lehuCheckChange) {
		this.listener = lehuCheckChange;
	}

	private class ViewHolder {
		TextView name;
		TextView date;
		ImageView checkbox;
	}

}