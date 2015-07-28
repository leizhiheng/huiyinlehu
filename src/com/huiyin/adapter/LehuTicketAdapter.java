package com.huiyin.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.TicketDataBean;
import com.huiyin.bean.TicketDataBean.Ticket;
import com.huiyin.utils.JSONParseUtils;
import com.view.swipe.adapters.BaseSwipeAdapter;

/**
 * 乐虎券适配器
 * 
 * @author 刘远祺
 * 
 */
public class LehuTicketAdapter extends BaseSwipeAdapter {

	/**上下文**/
	private Context context;
	
	public TicketDataBean tickData;
	
	public LehuTicketAdapter(Context context) {
		this(context, false);
	}

	
	public LehuTicketAdapter(Context context, boolean swipeEnable) {
		this.context = context;
		this.tickData = new TicketDataBean();
		super.mSwipeEnabled = swipeEnable;
	}

	/**
	 * 刷新数据(分页加载)
	 * @param dataList
	 */
	public void appendData(TicketDataBean tempData) {
		
		if(null == tempData || null == tempData.ticketList){
			return;
		}
		
		//累加赋值
		this.tickData.pageIndex = tempData.pageIndex;
		this.tickData.totalPageNum = tempData.totalPageNum;
		this.tickData.appendTickList(tempData.ticketList);
		
		this.notifyDataSetChanged();
	}

	/**
	 * 刷新数据
	 * @param dataList
	 */
	public void refreshData(TicketDataBean tempData) {
		
		if(null == tempData || null == tempData.ticketList){
			return;
		}
		
		//完全赋值
		this.tickData.pageIndex = tempData.pageIndex;
		this.tickData.totalPageNum = tempData.totalPageNum;
		this.tickData.ticketList = tempData.ticketList;
		
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(null == tickData){
			return 0;
		}
		
		if(null == tickData.ticketList){
			return 0;
		}
		
		return tickData.ticketList.size();
	}

	@Override
	public Object getItem(int position) {
		return tickData.ticketList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}


	class ViewHolder {

		public View lehuTickLayout;	// 父容器布局
		public View lehuIcon;		// 乐虎券小图标
		public TextView lehuquanTv;	// 乐虎红包券(左边)
		public TextView rangeNameTv;// 满足的条件(仅限手机...)
		public TextView nameTv; 	// 乐虎券名称(满200减10)
		public TextView timeTv; 	// 有效日期
		public TextView stopTimeTv; // 截止日期(右边-距离截止6天)
		public View iconTimeView;	// 有效期前那个小icon
		
		public View stopTimeLayoutTv;// 截止日期(右边-距离截止6天)
		
		
		ViewHolder(View convertView) {
			
			lehuTickLayout = convertView.findViewById(R.id.lehu_ticket_layout);
			lehuIcon = convertView.findViewById(R.id.lehu_quan_icon);
			lehuquanTv = (TextView)convertView.findViewById(R.id.lehu_quan_textview);
			
			rangeNameTv = (TextView) convertView.findViewById(R.id.condition_tv);
			nameTv = (TextView) convertView.findViewById(R.id.name_tv);
			
			timeTv = (TextView) convertView.findViewById(R.id.time_tv);
			stopTimeTv = (TextView) convertView.findViewById(R.id.stop_time_tv);
			stopTimeLayoutTv = convertView.findViewById(R.id.right_layout);
			iconTimeView = convertView.findViewById(R.id.icon_time_view);
		}

		/**
		 * 显示乐虎券信息
		 * @param tick
		 */
		void show(Ticket tick, int position){
			
			rangeNameTv.setText(tick.RANGE_NAME);
			
			//满减,面值
			nameTv.setText(Html.fromHtml(tick.getTextMiddle()));
			timeTv.setText("有效期："+tick.START_TIME+" - "+tick.END_TIME);
			
			int status = tick.getStatus();
			if(1 == status || 0 == status){
				// 0:未领取  1未使用  
				
				if(1 == status){
					//距离时间
					stopTimeTv.setText("距离\n截止\n"+tick.DISTANCE_END+"\n天");
				}else{
					//马上领取
					stopTimeTv.setText("马\n上\n领\n取");
				}
				
				stopTimeLayoutTv.setBackgroundResource(R.drawable.hb1_1);
				lehuTickLayout.setBackgroundResource(R.drawable.shape_frame_red);
				lehuIcon.setBackgroundResource(R.drawable.icon_quan_on);
				
				rangeNameTv.setTextColor(context.getResources().getColor(R.color.font_black));
				lehuquanTv.setTextColor(context.getResources().getColor(R.color.font_black));
				nameTv.setTextColor(context.getResources().getColor(R.color.font_black));
				timeTv.setTextColor(context.getResources().getColor(R.color.font_black));
				iconTimeView.setBackgroundResource(R.drawable.icon_time);
				
			}else{
				
				//2已使用 3已过期 4已作废
				
				stopTimeTv.setText(getStopTime(status));
				stopTimeLayoutTv.setBackgroundResource(R.drawable.hb2_2);
				lehuTickLayout.setBackgroundResource(R.drawable.shape_frame_gray);
				lehuIcon.setBackgroundResource(R.drawable.icon_quan_in);
				
				rangeNameTv.setTextColor(context.getResources().getColor(R.color.gray));
				lehuquanTv.setTextColor(context.getResources().getColor(R.color.gray));
				nameTv.setTextColor(context.getResources().getColor(R.color.gray));
				timeTv.setTextColor(context.getResources().getColor(R.color.gray));
				iconTimeView.setBackgroundResource(R.drawable.icon_time_in);
				
			}
			
		}
		
		String getStopTime(int status) {
			switch (status) {
			case 0:
				return "未\n领\n取";
			case 1:
				return "未\n使用\n用";
			case 2:
				return "已\n使\n用";
			case 3:
				return "已\n过\n期";
			case 4:
				return "已\n作\n废";
			default:
				return "已\n过\n期";
			}
		}
	}

	/**
	 * 移除当前领取的票
	 * @param ticket
	 */
	public void removeTicket(Ticket ticket) {
		tickData.ticketList.remove(ticket);
		this.notifyDataSetChanged();
	}

	
	//***********swipe start***********
	@Override
	public int getSwipeLayoutResourceId(int position) {
		//返回xml 里面 对应 SwipeLayout对应的id
		return R.id.swipe;
	}

	@Override
	public View generateView(int position, ViewGroup parent) {
		View convertView = LayoutInflater.from(context).inflate(R.layout.adapter_lehu_ticket, null);
		ViewHolder viewHolder = new ViewHolder(convertView);
		convertView.setTag(viewHolder);

		return convertView;
	}

	@Override
	public void fillValues(int position, View convertView) {

		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		viewHolder = (ViewHolder) convertView.getTag();
		
		//显示数据
		viewHolder.show((Ticket)getItem(position), position);
	}
	
	@Override
	protected void onDeleteClick(int position) {

		// 显示数据
		Ticket ticket = tickData.ticketList.get(position);
		deleteCoupon(ticket.getId(), position);
		
	}
	
	//***********swipe end***********
	
	private void deleteCoupon(final int id, final int position) {
		CustomResponseHandler handler = new CustomResponseHandler(context, true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				
				int type = JSONParseUtils.getInt(content, "type");
				if (type > 0) {
					
					//删除成功
					Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
					tickData.ticketList.remove(position);
					notifyDataSetChanged();
					
				} else {
					
					//删除失败
					String msg = JSONParseUtils.getString(content, "msg");
					Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				}
			}
		};
		RequstClient.appMyCouponDelete(handler, id);
	}
}
