package com.huiyin.bean;

import java.io.Serializable;

/**
 * 支付参数
 * 
 * @author 刘远祺
 * 
 * @todo TODO
 * 
 * @date 2015-7-8
 */
public class PayParmas implements Serializable {

	public String money; 		// 应缴费用
	public String status; 		// 返回的缴费状态码
	public String number; 		// 缴费订单编号
	public String orderId;	 	// 缴费订单ID
	public String perpaidCard; 	// 便民卡预付金
	public String userNo;		// 话费(存手机号码)，水电煤(存水电煤用户编号) 
	
	
	public int payResult;		// 支付结果状态 1成功， 2失败
	public int payWay;			// 支付方式
	public String payStatuMsg;	// 支付结果消息

	public PayParmas(){}
	
	public PayParmas(String money, String status, String number, String orderId, String perpaidCard, String msg) {
		this.money = money;
		this.status = status;
		this.number = number;
		this.orderId = orderId;
		this.perpaidCard = perpaidCard;
		this.payStatuMsg = msg;
	}
}
