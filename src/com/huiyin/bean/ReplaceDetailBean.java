package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.bean.LogisticsBean.OrderLogisticsEntity;
import com.huiyin.bean.LogisticsOrderBean.DataEntity;
import com.huiyin.constants.OrderConstants;
import com.huiyin.utils.DateUtil;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ResourceUtils;

/**
 * 换货详情
 * 
 * @author 刘远祺
 * 
 */
public class ReplaceDetailBean extends BaseBean {

	public String REPLACE_DAY; 						//换货期限
	public String REASON_NAME; 						//换货理由
	public String REPLACE_ID; 						//换货id
	public String FINISH_TIME; 						//交易完成时间,
	public String REPLACEMENT_STATUS; 				//订单状态
	public String DESCRIBE;				 			//描述
	public String REASON_TYPE; 						//换货理由id（可能为空）
	public String ORDER_ID; 						//对应的订单id
	public String DEFAULT_ADDRESS; 					//退货地址,
	public String TEL; 								//退货联系电话号码
	public String AUDIT_TIME; 						//审核通过时间
	public String FLAG_CONTINUE; 					//是否可以继续换货：1可以，<=0不可以
	public List<GoodReplaceBean> replaceDetailList;	//退货商品
	public String IMG_PATH_LIST; 					//换货图片列表,
	public String RETURN_TYPE; 						//退款方式：如果=2，在“请退货”页面中不需要显示“寄回商品按钮”
	public int FLAG;						        //1：退货，2换货，3预约;
	public String TITLE; 							//页面标题
	public String TYPE_NAME;						//售后类型名称
	public String CONSIGNEE_DELIVER_TIME; 			//买家发货时间
	public String RECEIVE_TIME; 					//商家收货时间
	public String RETURN_TIME;						//取消时间,商家退款时间 都用这个字段(服务端决定的)
	public String REPLACE_ORDER_CODE; 				//关联的订单编号
	public LogisticsOrderBean LOGISTICS;			//物流信息字段
	public String curTime;							//服务器系统当前时间
	
	/**
	 * 获取图片集合
	 * @return
	 */
	public List<ImageData> getImageData(){
		List<ImageData> list = new ArrayList<ImageData>();
		if(!TextUtils.isEmpty(IMG_PATH_LIST)){
			String[] arr = IMG_PATH_LIST.split(",");
			for(int i=0; i<arr.length; i++){
				
				list.add(new ImageData("", arr[i]));
			}
			
		}
		return list;
	}
	

	public static ReplaceDetailBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			ReplaceDetailBean orderbean = gson.fromJson(json, ReplaceDetailBean.class);
			return orderbean;
		} catch (Exception e) {
			LogUtil.d("dataExplainJson", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}

	/**
	 * 获取退货时间倒计时
	 * @return
	 */
	public String getREPLACE_DAY() {
		
		//总共的限制时间(单位：天)
		long replaceDay = MathUtil.stringToInt(REPLACE_DAY);
		String curDate = curTime;
		String finishDate = FINISH_TIME;
		
		//获取已经过去多少天
		long passDay = DateUtil.getDays(curDate, finishDate);
		
		//剩余天数
		long remainDay = replaceDay - passDay;
		//remainDay = remainDay > 0 ? remainDay : 0;
		
		StringBuffer sb = new StringBuffer();
		if(remainDay > 0){
			sb.append("剩余");
			sb.append(ResourceUtils.changeStringColor("#3592e2", ""+remainDay));
			sb.append("天，");
			sb.append("逾期未退货申请将自动关闭");
		}else{
			sb.append("逾期未退货申请已关闭");
		}
		
		return sb.toString();
	}

	/**
	 * 逾期未退货申请已关闭
	 * @return
	 */
	public boolean orderIsClose(){
		
		long replaceDay = MathUtil.stringToInt(REPLACE_DAY);
		String curDate = curTime;
		String finishDate = FINISH_TIME;
		
		//获取已经过去多少天
		long passDay = DateUtil.getDays(curDate, finishDate);
		
		//剩余天数
		long remainDay = replaceDay - passDay;
		if(remainDay <= 0){
			return true;
		}else{
			return false;
		}
	}
	

	public int getFLAG() {
		if(0 == FLAG){
			return 2;
		}
		return FLAG;
	}


	/**
	 * 获取物流信息
	 * @return
	 */
	public List<DataEntity> getLogisticList() {
		
		if(null != LOGISTICS.RETURNVAL){
			return LOGISTICS.RETURNVAL.data;
		}
		return null;
	}
	

	public LogisticsOrderBean getLOGISTICS() {
		if(null == LOGISTICS || TextUtils.isEmpty(LOGISTICS.COMPANY_NAME)){
		}
		return LOGISTICS;
	}

	/**
	 * 获取换货订单状态
	 * @return
	 */
	public int getREPLACEMENT_STATUS() {
		return MathUtil.stringToInt(REPLACEMENT_STATUS);
	}

	/**
	 * 是否为上门自提
	 * @return
	 */
	public boolean isGetBySelf(){
		if(null != RETURN_TYPE){
			return "2".equals(RETURN_TYPE);
		}
		return false;
	}
	
	/**
	 * 获取订单状态对应的时间
	 * @return
	 */
	public String getStatuTime() {

		//取消时间，退款时间，买家发货时间，完成时间,用return time
		
		int orderStatu = getREPLACEMENT_STATUS();
		switch (orderStatu) {

		case OrderConstants.Order_Status_15:// 换货：维权审核
		case OrderConstants.Order_Status_16:// 换货：买家请退货
		case OrderConstants.Order_Status_17:// 换货：维权拒绝
		case OrderConstants.Order_Status_19:// 换货：商品检测
			//"审核时间：";
			return AUDIT_TIME;

		case OrderConstants.Order_Status_18:// 换货：买家已发货
		case OrderConstants.Order_Status_20:// 换货：商家发货
		case OrderConstants.Order_Status_22:// 换货：买家收货

			//"发货时间：";
			return CONSIGNEE_DELIVER_TIME;

		case OrderConstants.Order_Status_21:// 换货：取消换货

			//"取消时间：";
			return RETURN_TIME;

		case OrderConstants.Order_Status_23:// 换货：维权完成

			//"完成时间：";
			return RETURN_TIME;

		case OrderConstants.Order_Status_32:// 换货：商品检测不通过 换货：商品检测不通过

			//"检测时间：";
			return RECEIVE_TIME;
		}
		return AUDIT_TIME;
	}
	
	/**
	 * 获取订单状态对应的时间
	 * @return
	 */
	public String getStatuTimeBackup() {
		return RETURN_TIME;
	}
}
