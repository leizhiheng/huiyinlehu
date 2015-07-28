package com.huiyin.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.ReservationType;

/**
 * 预约类型-adapter
 * 
 * @author 刘远祺
 * 
 */
public class ReplaceTypeAdapter extends BaseAdapter {
	
	
	private Context context;
	
	/**预约类型数据集合**/
	private List<ReservationType> dataList;

	/**当前选中的预约类型**/
	private String curCheckedTypeId;
	
	public ReplaceTypeAdapter(Context context, List<ReservationType> goodsList) {
		this.context = context;
		this.dataList = goodsList;
	}

	/**
	 * 刷新数据
	 * @param dataList
	 */
	public void refreshData(List<ReservationType> dataList) {
		this.dataList = dataList;
		this.notifyDataSetChanged();
	}

	/**
	 * 设置当前选中的类型
	 * @param typeId
	 */
	public void setCurCheckedTypeID(String typeId){
		this.curCheckedTypeId = typeId;
		this.notifyDataSetChanged();
	}
	
	/**
	 * 获取当前选中的TypeId
	 * 
	 * @return
	 */
	public String getCurTypeId(){
		return this.curCheckedTypeId;
	}
	
	/**
	 * 选中第position项
	 * @param position
	 */
	public boolean checkPosition(int position){
		if(position < getCount()){
			
			//数据项尚未发生改变
			if(!TextUtils.isEmpty(curCheckedTypeId) && curCheckedTypeId.equals(dataList.get(position).RESERVATIONTYP_ID)){
				return false;
			}
			
			curCheckedTypeId = dataList.get(position).RESERVATIONTYP_ID;
			notifyDataSetChanged();
			return true;
		}
		return false;
	}
	
	@Override
	public int getCount() {
		return null != dataList ? dataList.size() : 0;
		//return 3;
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_apply_type, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		
		//显示数据
		holder.show(position);
		
		return convertView;
	}

	class ViewHolder {

		public ImageView iv_logo; 	// 选中图片
		public TextView tv_type; 	// 类型名称
		public View line; 			// 线条
		
		ViewHolder(View convertView) {
			iv_logo = (ImageView) convertView.findViewById(R.id.type_pic_imageview);
			tv_type = (TextView) convertView.findViewById(R.id.type_name_tv);
			line = convertView.findViewById(R.id.line);
		}

		/**
		 * 显示商品信息
		 * @param ReservationType
		 */
		void show(int position){
			
			ReservationType reservationType = dataList.get(position);
			boolean isChecked = reservationType.RESERVATIONTYP_ID.equals(curCheckedTypeId);
			int bg = isChecked ? R.drawable.icon_choice : R.drawable.icon_wancheng;
			
			iv_logo.setBackgroundResource(bg);
			tv_type.setText(reservationType.RESERVATIONTYP_NAME);
			line.setVisibility(position == (getCount()-1) ? View.GONE : View.VISIBLE);
		}
		
	}
}
