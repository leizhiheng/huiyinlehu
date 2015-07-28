package com.huiyin.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.WaterPayRecoderBean.ShareBillDataEntity;
import com.huiyin.utils.ResourceUtils;

/**
 * 话费-缴费记录适配器
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-8
 */
public class PhonePayRecordAdapter extends BaseAdapter {
	
	/**上下文**/
	private Context context;
	
	/**缴费记录**/
	private List<ShareBillDataEntity> dataList;

	
	
	public PhonePayRecordAdapter(Context context) {
		this.context = context;
	}

	/**
	 * 刷新数据
	 * @param dataList
	 */
	public void refreshData(List<ShareBillDataEntity> dataList){
		this.dataList = dataList;
		this.notifyDataSetChanged();
	}
	
	/**
	 * 叠加数据
	 * @param tempList
	 */
	public void appendData(List<ShareBillDataEntity> tempList){
		this.dataList.addAll(tempList);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return null != dataList ? dataList.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return dataList.get(arg0);
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
			view = LayoutInflater.from(context).inflate(R.layout.adapter_phone_pay_recoder_item, null);
			holder = new ViewHolder(view);
			
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		//显示数据
		holder.showData((ShareBillDataEntity)getItem(position));
		
		return view;
	}

	
	class ViewHolder {
		
		ImageView recoder_type;
		TextView recoder_time;
		TextView recoder_status;
		TextView recoder_user_num;
		TextView recoder_order_num;
		TextView recoder_price;
		TextView recoder_remark;
		View line_middle;
		
		ViewHolder(View view){
			recoder_type = (ImageView) view.findViewById(R.id.recoder_type);
			recoder_time = (TextView) view.findViewById(R.id.recoder_time);
			recoder_status = (TextView) view.findViewById(R.id.recoder_status);
			recoder_user_num = (TextView) view.findViewById(R.id.recoder_user_num);
			recoder_order_num = (TextView) view.findViewById(R.id.recoder_order_num);
			recoder_price = (TextView) view.findViewById(R.id.recoder_price);
			recoder_remark = (TextView) view.findViewById(R.id.recoder_remark);
			line_middle = view.findViewById(R.id.line_middle);
		}
		
		void showData(ShareBillDataEntity item){
			
			recoder_time.setText("缴费日期:" + item.getPAYTIME());
			recoder_user_num.setText(Html.fromHtml("缴费户号: " + ResourceUtils.changeStringColor("#000000", item.getUSERNO())));
			recoder_order_num.setText(Html.fromHtml("平台流水号: " + ResourceUtils.changeStringColor("#000000", item.getPAYNBR())));
			recoder_price.setText("¥" + item.getBLANCE());
			
			recoder_remark.setVisibility(View.GONE);
			line_middle.setVisibility(View.GONE);
			
			if (item.getPAYSTATUS() == 0) {
				
				// 缴费成功
				recoder_status.setText("充值成功");
				recoder_status.setTextColor(context.getResources().getColor(R.color.red));
				
			} else if (item.getPAYSTATUS() == 1) {
				
				// 缴费失败
				recoder_status.setText("充值失败");
				recoder_status.setTextColor(context.getResources().getColor(R.color.black));
				if (!TextUtils.isEmpty(item.getREMARK())) {
					recoder_remark.setText(item.getREMARK());
					recoder_remark.setVisibility(View.VISIBLE);
					line_middle.setVisibility(View.VISIBLE);
				}
			}
		}
	}
}

