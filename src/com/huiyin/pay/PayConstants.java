package com.huiyin.pay;

/**
 * 支付常量
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-15
 */
public class PayConstants {
	
	
	/**支付方式-支付宝-1**/
	public static final int PAY_TYPE_ALIPAY 			= 1;
	
	/**支付方式-银联-2**/
	public static final int PAY_TYPE_YINLIAN 			= 2;
	
	/**支付方式-微信支付-3**/
	public static final int PAY_TYPE_WXPAY 				= 3;
	
	/**支付方式-服务卡-4**/
	public static final int PAY_TYPE_SERVERCARD 		= 4;
	
	/**支付方式-中国银行-5**/
	public static final int PAY_TYPE_CHINA_BANK 		= 5;
	
	/**支付方式-建设银行-6**/
	public static final int PAY_TYPE_CONSTRUCTION_BANK	= 6;
	
	
	
	
	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境, "01" - 连接银联测试环境
	 *****************************************************************/
	public static final String MODE = "00";
	
	
	/**支付结果-成功-1**/
	public static final int SUCCESS = 1;
	
	/**支付结果-失败-2**/
	public static final int FAILED 	= 2;
	
	
}
