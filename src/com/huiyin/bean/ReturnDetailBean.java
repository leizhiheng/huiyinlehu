package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.bean.LogisticsOrderBean.DataEntity;
import com.huiyin.constants.OrderConstants;
import com.huiyin.utils.DateUtil;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ResourceUtils;

/**
 * 退货详情
 * 
 * @author 刘远祺
 * 
 */
public class ReturnDetailBean extends BaseBean {

	 public String FLAG;					// 1：退货，2退货，3预约
	 public String RETURN_DAY;				// 退货期限
	 public String REASON_NAME;				// 退货理由
	 public String ID;						// 退货id
	 public String FINISH_TIME;				// 交易完成时间,
	 public String STATUS;					// 订单状态
	 public String DESCRIBE;				// 描述
	 public String REASON_TYPE;				// 退货理由id（可能为空）
	 public String ORDER_ID;				// 对应的订单id
	 public String DEFAULT_ADDRESS;			// 退货地址,
	 public String TEL;						// 退货联系电话号码
	 public String AUDIT_TIME;				// 审核通过时间
	 public String FLAG_CONTINUE;			// 是否可以继续退货：1可以，<=0不可以
	 public List<GoodReturnBean> returnDetailList;//退货商品集合
	 public String IMG;						//退货图片列表,
	 public String RETURN_TYPE;				//退款方式：如果=2，在“请退货”页面中不需要显示“寄回商品按钮(代表上门自提,同时也不需要显示物流)”
	 public String TITLE;					//页面标题
	 public String TYPE_NAME;				//售后类型名称
	 public String CONSIGNEE_DELIVER_TIME;	//买家发货时间
	 public String RECEIVE_TIME;			//商家收货时间 
	 public String RETURN_TIME;				//取消时间,完成时间(服务端决定的)
	 public String curTime;					//服务器系统当前时间
	 public LogisticsOrderBean LOGISTICS;	//物流信息字段
	
	 public String RETURN_MONEY;			//退款金额----待念哥添加字段
	
	 public int getFlag(){
		 //念哥没有返回该字段，所以自动返回1
		 if(!TextUtils.isEmpty(FLAG)){
			 return MathUtil.stringToInt(FLAG);
		 }
		 return 1;
	 }
	
	 /**
	  * 是否有物流
	  * @return
	  */
	 public boolean hasLogistics(){
		 return null != LOGISTICS && null != LOGISTICS.RETURNVAL;
	 }
	 
	/**
	 * 获取图片集合
	 * @return
	 */
	public List<ImageData> getImageData(){
		List<ImageData> list = new ArrayList<ImageData>();
		if(!TextUtils.isEmpty(IMG)){
			String[] arr = IMG.split(",");
			for(int i=0; i<arr.length; i++){
				
				list.add(new ImageData("", arr[i]));
			}
			
		}
		return list;
	}
	

	public static ReturnDetailBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			ReturnDetailBean orderbean = gson.fromJson(json, ReturnDetailBean.class);
			return orderbean;
		} catch (Exception e) {
			LogUtil.d("dataExplainJson", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}

	
	/**
	 * 退货商品
	 * @author 刘远祺
	 *
	 * @todo TODO
	 *
	 * @date 2015-7-3
	 */
	public class GoodReturnBean{
		
		 public String GOODS_IMG;		//商品图片,
		 public String GOODS_NAME;		//商品名称
		 public String STORE_ID;		//店铺id
		 public String GOODS_NO;		//商品规格,
		 public String GOODS_ID;		//商品id
		 public String NORMS_VALUE;		//规格名称
		 public String RETURN_DETAIL_ID;//退货详情id
		 public String QUANTITY;		//购买数量
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
	
	
	private void buildData(){
		String fake = "{\n" + "type: 1,\n" + "msg: \"成功\",\n"
				+ "orderLogistics: [\n" + "{\n"
				+ "DELIVERY_CODE: \"15378717677\",\n" + "COMPANY_NAME: \"飞虎队\",\n"
				+ "context: \"长江中路-大学南路,长江中路-大学南路附近\",\n"
				+ "ftime: \"2015-07-02 10:33:35\"\n" + "},\n" + "{\n"
				+ "COMPANY_NO: \"tiantian\",\n" + "COMPANY_NAME: \"天天快递\",\n"
				+ "DELIVERY_CODE: \"886143084825\",\n" + "data: [\n" + "{\n"
				+ "context: \"已签收,签收人是草签 \",\n"
				+ "time: \"2015-06-11 13:29:38\",\n"
				+ "ftime: \"2015-06-11 13:29:38\",\n" + "status: \"签收\",\n"
				+ "areaCode: \"310000000000\",\n" + "areaName: \"深圳市\"\n" + "},\n"
				+ "{\n" + "context: \"【宝安西乡】的派件员【廖航18123614262】正在派件 \",\n"
				+ "time: \"2015-06-11 09:34:58\",\n"
				+ "ftime: \"2015-06-11 09:34:58\",\n" + "status: \"在途\",\n"
				+ "areaCode: \"310000000000\",\n" + "areaName: \"深圳市\"\n" + "},\n"
				+ "{\n"
				+ "context: \"快件已到达【宝安西乡】 扫描员是【客服075527301600】上一站是【深圳分拨中心】 \",\n"
				+ "time: \"2015-06-11 08:39:14\",\n"
				+ "ftime: \"2015-06-11 08:39:14\",\n" + "status: \"在途\",\n"
				+ "areaCode: \"310000000000\",\n" + "areaName: \"深圳市\"\n" + "},\n"
				+ "{\n" + "context: \"快件已到达【深圳分拨中心】 扫描员是【操作部黎卫华】上一站是【南通分拨中心】 \",\n"
				+ "time: \"2015-06-11 02:37:5\",\n"
				+ "ftime: \"2015-06-11 02:37:5\",\n" + "status: \"在途\",\n"
				+ "areaCode: \"310000000000\",\n" + "areaName: \"深圳市\"\n" + "},\n"
				+ "{\n" + "context: \"由【南通川港】发往【深圳】 \",\n"
				+ "time: \"2015-06-09 21:36:42\",\n"
				+ "ftime: \"2015-06-09 21:36:42\",\n" + "status: \"在途\",\n"
				+ "areaCode: \"310000000000\",\n" + "areaName: \"南通市\"\n" + "},\n"
				+ "{\n" + "context: \"【南通川港】的收件员【南通川港】已收件 \",\n"
				+ "time: \"2015-06-09 19:11:11\",\n"
				+ "ftime: \"2015-06-09 19:11:11\",\n" + "status: \"在途\",\n"
				+ "areaCode: \"310000000000\",\n" + "areaName: \"南通市\"\n" + "}\n"
				+ "]\n" + "}\n" + "],\n" + "curTime: \"2015-07-02 10:34:17\"\n"
				+ "}";
		
		LogisticsBean bean = new Gson().fromJson(fake, LogisticsBean.class);
		//LOGISTICS = bean.orderLogistics.get(1);
	}

	public LogisticsOrderBean getLOGISTICS() {
		return LOGISTICS;
	}

	/**
	 * 获取退款金额
	 * @return
	 */
	public String getRETURN_MONEY() {
		if(TextUtils.isEmpty(RETURN_MONEY)){
			return "0.00";
		}
		return RETURN_MONEY;
	}

	/**
	 * 获取退货时间倒计时
	 * @return
	 */
	public String getRETURN_DAY() {
		
		//总共的限制时间(单位：天)
		long replaceDay = MathUtil.stringToInt(RETURN_DAY);
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
		
		long replaceDay = MathUtil.stringToInt(RETURN_DAY);
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
		
		 int orderStatu=Integer.parseInt(STATUS);
		 switch (orderStatu) {
			case OrderConstants.Order_Status_7: // 退款：取消
			case OrderConstants.Order_Status_14: // 退款：维权取消
				//"取消时间：";
				return RETURN_TIME;
				
				
			case OrderConstants.Order_Status_8: // 退款：维权审核
			case OrderConstants.Order_Status_9: // 退款：请退货
			case OrderConstants.Order_Status_10: // 退款：维权拒绝
			case OrderConstants.Order_Status_31: // 退款：商品检测不通过 退款：商品检测不通过
				//"审核时间：";
				return AUDIT_TIME;
				
			case OrderConstants.Order_Status_11: // 退款：买家已发货
				//"发货时间：";
				return CONSIGNEE_DELIVER_TIME;
				
			case OrderConstants.Order_Status_12: // 退款：商家退款中
				//"收货时间：";
				return RECEIVE_TIME;
				
			case OrderConstants.Order_Status_13: // 退款：维权完成
				//"退款时间：";
				return RETURN_TIME;
				
			case OrderConstants.Order_Status_29: // 退款：买家收款
				//"退款时间：";
				return RETURN_TIME;
				
			case OrderConstants.Order_Status_33: // 退货：商品检测 退货：商品检测
				//"收货时间：";
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
