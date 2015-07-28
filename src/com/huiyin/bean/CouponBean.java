package com.huiyin.bean;

import java.io.Serializable;
import java.util.ArrayList;
// add by zhyao @2015/6/12 添加消费卷bean文件
/**
 * {
    "msg": "成功",
    "type": 200,
    "showMsg": "iPhone 6P 4G联通 消费券消费券为：100629664282 ",
    "coupons": [
        {
            "GOOD_NAME": "iPhone 6P 4G联通 消费券",
            "CONSUMPTION_STATUS": 0,
            "ORDER_ID": "S1434096931173",
            "CONSIGNEE_PHONE": 18680374975,
            "STORE_ID": 351,
            "ID": 682,
            "CONSUMPTION_ID": 100629664282,
            "CONSUMPTION_TIME": null,
            "CONSUMPTION_TIMESTAMP": null,
            "GOOD_ID": 10721,
            "CONSIGNEE_NAME": "yzh"
        }
    ]
}
 * @author zhyao
 *
 */
public class CouponBean {
	
	private String type;
	
	private String msg;
	
	private String showMsg;
	
	private ArrayList<Coupons> coupons;
	
	public ArrayList<Coupons> getCoupons() {
		return coupons;
	}

	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public String getShowMsg() {
		return showMsg;
	}


	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}


	public void setCoupon(ArrayList<Coupons> coupons) {
		this.coupons = coupons;
	}

	public class Coupons implements Serializable{
		private static final long serialVersionUID = 1123213123L;
		private String GOOD_NAME;//消费卷名
		private String CONSUMPTION_STATUS;//状态
		private String ORDER_ID;//商品订单号
		private String CONSIGNEE_PHONE;//手机号码
		private String STORE_ID;//商店ID
		private String ID;//消费卷ID
		private String CONSUMPTION_ID;//消费卷编号
		private String CONSUMPTION_TIME;//时间
		private String CONSUMPTION_TIMESTAMP;//时间戳
		private String GOOD_ID;//商品ID
		private String CONSIGNEE_NAME;//姓名
		public String getGOOD_NAME() {
			return GOOD_NAME;
		}
		public void setGOOD_NAME(String gOOD_NAME) {
			GOOD_NAME = gOOD_NAME;
		}
		public String getCONSUMPTION_STATUS() {
			return CONSUMPTION_STATUS;
		}
		public void setCONSUMPTION_STATUS(String cONSUMPTION_STATUS) {
			CONSUMPTION_STATUS = cONSUMPTION_STATUS;
		}
		public String getORDER_ID() {
			return ORDER_ID;
		}
		public void setORDER_ID(String oRDER_ID) {
			ORDER_ID = oRDER_ID;
		}
		public String getCONSIGNEE_PHONE() {
			return CONSIGNEE_PHONE;
		}
		public void setCONSIGNEE_PHONE(String cONSIGNEE_PHONE) {
			CONSIGNEE_PHONE = cONSIGNEE_PHONE;
		}
		public String getSTORE_ID() {
			return STORE_ID;
		}
		public void setSTORE_ID(String sTORE_ID) {
			STORE_ID = sTORE_ID;
		}
		public String getID() {
			return ID;
		}
		public void setID(String iD) {
			ID = iD;
		}
		public String getCONSUMPTION_ID() {
			return CONSUMPTION_ID;
		}
		public void setCONSUMPTION_ID(String cONSUMPTION_ID) {
			CONSUMPTION_ID = cONSUMPTION_ID;
		}
		public String getCONSUMPTION_TIME() {
			return CONSUMPTION_TIME;
		}
		public void setCONSUMPTION_TIME(String cONSUMPTION_TIME) {
			CONSUMPTION_TIME = cONSUMPTION_TIME;
		}
		public String getCONSUMPTION_TIMESTAMP() {
			return CONSUMPTION_TIMESTAMP;
		}
		public void setCONSUMPTION_TIMESTAMP(String cONSUMPTION_TIMESTAMP) {
			CONSUMPTION_TIMESTAMP = cONSUMPTION_TIMESTAMP;
		}
		public String getGOOD_ID() {
			return GOOD_ID;
		}
		public void setGOOD_ID(String gOOD_ID) {
			GOOD_ID = gOOD_ID;
		}
		public String getCONSIGNEE_NAME() {
			return CONSIGNEE_NAME;
		}
		public void setCONSIGNEE_NAME(String cONSIGNEE_NAME) {
			CONSIGNEE_NAME = cONSIGNEE_NAME;
		}
		
	}

}
