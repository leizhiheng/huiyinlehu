package com.huiyin.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.LogisticsOrderBean.DataEntity;

/**
 * 退换货物流状态流程
 * 
 * @author 刘远祺
 * 
 * @todo TODO
 * 
 * @date 2015-7-7
 */
public class ReturnLogisticsAdapter extends BaseAdapter {

	/** 上下文 **/
	private Context context;

	/** 物流跟踪信息 **/
	private List<DataEntity> dataList;

	public ReturnLogisticsAdapter(Context context, List<DataEntity> dataList) {
		this.context = context;
		this.dataList = dataList;
	}

	public void refreshData(List<DataEntity> dataList) {
		this.dataList = dataList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return null != dataList ? dataList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Myholder myholder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_logistics_status_item, null);
			myholder = new Myholder(convertView);
			convertView.setTag(myholder);
		} else {
			myholder = (Myholder) convertView.getTag();
		}
		
		myholder.show((DataEntity) getItem(position));

		return convertView;
	}

	class Myholder {
		
		public TextView tv_time;
		public TextView tv_context;
		public TextView tv_area;

		Myholder(View view) {
			tv_time = (TextView) view.findViewById(R.id.time_textview);
			tv_context = (TextView) view.findViewById(R.id.context_textview);
			tv_area = (TextView) view.findViewById(R.id.area_textview);
		}

		void show(DataEntity data) {
			tv_time.setText(data.ftime);
			tv_context.setText(data.context);
			tv_area.setText(data.areaName);
		}
	}
}
