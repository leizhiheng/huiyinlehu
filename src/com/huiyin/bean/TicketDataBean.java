package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.huiyin.utils.ResourceUtils;

/**
 * 
 * @author 刘远祺
 * 
 */
public class TicketDataBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	
	public int pageIndex = 1;			//当前页索引
	public int totalPageNum;			//总页数
	public int myCouponOut;				//: 1即将过期的虎券数量
	public String curTime;				//: 当前系统时间
	
	public List<Ticket> ticketList;		//乐虎券列表
	
	
	/**
	 * 追加到后面
	 * @param appendList
	 */
	public void appendTickList(List<Ticket> appendList){
		if(null == ticketList){
			ticketList = new ArrayList<TicketDataBean.Ticket>();
		}
		
		if(null != appendList){
			ticketList.addAll(appendList);
		}
	}
	
	
	/**
	 * 判断是否有更多的数据局
	 * @return
	 */
	public boolean hasMoreData(){
		return pageIndex < totalPageNum;
	}
	
	
	/**
	 * 乐虎券
	 */
	public class Ticket{
		
		public String PRICE;		//面值（减多少）	
		public String RANGE_NAME;	//提示语
		public String RANGES;		//限制类型 1限分类  2限品牌 3限店铺  4限单品  5全场通用
		public String DISTANCE_END;	//距离结束时间还有多久
		public String ID;			//虎券id,
		public String END_TIME;		//虎券结束时间
		public String START_TIME;	//开始时间
		public String STATUS;		//状态 0:未领取  1未使用  2已使用 3已过期 4已作废
		public String NUM;			//数量
		public String DEMAND;		//要求（满多少）
		public String HQ_TYPE;		//优惠券类型：1满额，2面值
		public String ACTIVE_ID;	//虎券活动id
		
		public int getId(){
			try{
				return Integer.parseInt(ID);
			}catch(Exception e){
				return 0;
			}
		}
		
		public int getStatus() {
			try{
				return Integer.parseInt(STATUS);
			}catch(Exception e){
				return 3;
			}
		}
		
		/**
		 * 获取 面值，满减
		 * @return
		 */
		public String getTextMiddle(){
			StringBuffer sb = new StringBuffer();
			if(null != HQ_TYPE && HQ_TYPE.equals("1")){
				//满额(未领取，未使用)
				if("1".equals(STATUS) || "0".equals(STATUS)){
					sb.append("满");
					sb.append(ResourceUtils.changeStringColor("#ff0000", DEMAND));
					sb.append("减");
					sb.append(ResourceUtils.changeStringColor("#ff0000", PRICE));
				}else{
					sb.append("满");
					sb.append(DEMAND);
					sb.append("减");
					sb.append(PRICE);
				}
			}else{
				
				//面值(未领取，未使用)
				if("1".equals(STATUS) || "0".equals(STATUS)){
					sb.append("面值"+ResourceUtils.changeStringColor("#ff0000", PRICE));
				}else{
					sb.append("面值"+PRICE);
				}
			}
			return sb.toString();
		}
		
	}
	
	public static TicketDataBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			TicketDataBean bean = gson.fromJson(json,TicketDataBean.class);
			return bean;
		} catch (Exception e) {
			return null;
		}
	}
}
