package com.huiyin.constants;

/**
 * 订单常量类
 * 
 * @author 刘远祺
 * 
 */
public class OrderConstants {

	public static final int Order_Status_1 = 1;// 未付款
	public static final int Order_Status_2 = 2;// 订单待审核
	public static final int Order_Status_3 = 3;// 待发货
	public static final int Order_Status_4 = 4;// 待收货
	public static final int Order_Status_5 = 5;// 交易完成
	public static final int Order_Status_6 = 6;// 取消订单
	public static final int Order_Status_7 = 7;// 退款：取消
	public static final int Order_Status_8 = 8;// 退款：维权审核
	public static final int Order_Status_9 = 9;// 退款：请退货
	public static final int Order_Status_10 = 10;// 退款：维权拒绝
	public static final int Order_Status_11 = 11;// 退款：买家已发货
	public static final int Order_Status_12 = 12;// 退款：商家退款中
	public static final int Order_Status_13 = 13;// 退款：维权完成
	public static final int Order_Status_14 = 14;// 退款：维权取消
	public static final int Order_Status_15 = 15;// 换货：维权审核
	public static final int Order_Status_16 = 16;// 换货：买家请退货
	public static final int Order_Status_17 = 17;// 换货：维权拒绝
	public static final int Order_Status_18 = 18;// 换货：买家已发货
	public static final int Order_Status_19 = 19;// 换货：商品检测
	public static final int Order_Status_20 = 20;// 换货：商家发货
	public static final int Order_Status_21 = 21;// 换货：取消换货
	public static final int Order_Status_22 = 22;// 换货：买家收货
	public static final int Order_Status_23 = 23;// 换货：维权完成
	public static final int Order_Status_24 = 24;// 预约：申请
	public static final int Order_Status_25 = 25;// 预约：审核中
	public static final int Order_Status_26 = 26;// 预约：审核成功
	public static final int Order_Status_27 = 27;// 预约：审核失败
	public static final int Order_Status_28 = 28;// 预约：取消
	public static final int Order_Status_29 = 29;// 退款：买家收款
	public static final int Order_Status_30 = 30;// 订单作废
	public static final int Order_Status_31 = 31;//	退款：商品检测不通过	退款：商品检测不通过
	public static final int Order_Status_32 = 32;//	换货：商品检测不通过	换货：商品检测不通过
	public static final int Order_Status_33 = 33;//	退货：商品检测	退货：商品检测
	
	
	/**退货标志**/
	public static final int Flag_Return 	= 1;
	
	/**换货标志**/
	public static final int Flag_Replace 	= 2;
	
	
	
	
	/**
	 * 获取换货时间类型
	 * @param orderStatu
	 * @return
	 */
	public static String getReplaceStatuTip(int orderStatu) {
		switch (orderStatu) {

		case Order_Status_15:// 换货：维权审核
		case Order_Status_16:// 换货：买家请退货
		case Order_Status_17:// 换货：维权拒绝
		case Order_Status_19:// 换货：商品检测
			return "审核时间：";
			
		case Order_Status_18:// 换货：买家已发货
		case Order_Status_20:// 换货：商家发货
		case Order_Status_22:// 换货：买家收货
			
			return "发货时间：";
			
		case Order_Status_21:// 换货：取消换货
			
			return "取消时间：";
			
		case Order_Status_23:// 换货：维权完成
			
			return "完成时间：";
			
		case Order_Status_32:// 换货：商品检测不通过 换货：商品检测不通过
			
			return "检测时间：";
		}
		return "完成时间：";
	}
	
	
	/**
	 * 获取退货时间类型
	 * @param orderStatu
	 * @return
	 */
	public static String getReturnStatuTip(int orderStatu) {
		switch (orderStatu) {
		case Order_Status_7: // 退款：取消
		case Order_Status_14: // 退款：维权取消
			return "取消时间：";
			
		case Order_Status_8: // 退款：维权审核
		case Order_Status_9: // 退款：请退货
		case Order_Status_10: // 退款：维权拒绝
		case Order_Status_31: // 退款：商品检测不通过 退款：商品检测不通过
			return "审核时间：";
			
			
		case Order_Status_11: // 退款：买家已发货
			return "发货时间：";
			
		case Order_Status_12: // 退款：商家退款中
			return "收货时间：";
			
		case Order_Status_13: // 退款：维权完成
			return "退款时间：";
			
		case Order_Status_29: // 退款：买家收款
			return "收款时间：";
			
		case Order_Status_33: // 退货：商品检测 退货：商品检测
			return "收货时间：";
			
		}
		return "退款时间：";
	}

}
