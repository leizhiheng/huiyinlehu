package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;

/**
 * 申请理由-adapter
 * 
 * @author 刘远祺
 * 
 */
public class ApplyReasonAdapter extends BaseAdapter {
	
	
	private Context context;
	
	/**预约类型数据集合**/
	private Map<String, String> dataMap;
	private List<String> keys;
	
	/**当前选中的预约类型**/
	private String curCheckedTypeId;
	
	public ApplyReasonAdapter(Context context, Map<String, String> dataMap) {
		this(context, dataMap, null);
	}
	
	public ApplyReasonAdapter(Context context, Map<String, String> dataMap, String checkedKey) {
		this.context = context;
		this.dataMap = dataMap;
		this.keys = new ArrayList<String>();
		this.keys.addAll(dataMap.keySet());
		this.curCheckedTypeId = checkedKey;
	}

	/**
	 * 刷新数据
	 * @param dataMap
	 */
	public void refreshData(Map<String, String> dataMap) {
		this.dataMap = dataMap;
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
	
	@Override
	public int getCount() {
		return null != keys ? keys.size() : 0; 
	}

	@Override
	public Object getItem(int position) {
		return keys.get(position);
	}

	public String getValue(String key){
		return dataMap.get(key);
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_apply_reason, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		
		//显示数据
		holder.show(position);
		
		return convertView;
	}

	class ViewHolder {

		public TextView tv_type; 	// 类型名称
		public View line; 			// 线条
		
		ViewHolder(View convertView) {
			tv_type = (TextView) convertView.findViewById(R.id.type_name_tv);
			line = convertView.findViewById(R.id.line);
		}

		/**
		 * 显示商品信息
		 * @param ReservationType
		 */
		void show(int position){
			
			String key = getItem(position).toString().trim();
			String value = dataMap.get(key);
			tv_type.setText(value);
			
			if(!TextUtils.isEmpty(curCheckedTypeId) && curCheckedTypeId.equals(key)){
				tv_type.setTextColor(context.getResources().getColor(R.color.index_red));
			}else{
				tv_type.setTextColor(context.getResources().getColor(R.color.font_black));
			}
		}
		
	}
}
