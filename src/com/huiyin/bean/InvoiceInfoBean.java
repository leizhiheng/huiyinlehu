package com.huiyin.bean;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.utils.LogUtil;

public class InvoiceInfoBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	public InfoBean invoiceInfo;

	public class InfoBean {
		public String USER_ID;// 1",
		public String COMPANY_NAME;// 增值税发票的单位名称
		public String COLLECTOR_NAME;// 收票人名称
		public String ACCOUNT;// 银行账户
		public String REGISTERED_PHONE;// 注册电话
		public String BANK;// 开户银行
		public String COLLECTOR_ADDRESS;// 收票人地址
		public String INVOICE_TITLE;// 发票抬头 收件人 或者 收货单位名字
		public String INVOICE_TITLE_TYPE;// 发票抬头类型：1个人，2单位
		public String COLLECTOR_PHONE;// 收票人手机
		public String REGISTERED_ADDRESS;// 注册地址
		public String INVOICING_METHOD;// 发票开具方式：0 普通发票1 增值税发票
		public String ID;// 1",
		public String IDENTIFICATION_NUMBER;// 纳税人识别号
		public String INVOICE_CONTENT;// 发票明细内容(0不选中，1选中)
		
		public String getUSER_ID() {
			return USER_ID;
		}

		public void setUSER_ID(String uSER_ID) {
			USER_ID = uSER_ID;
		}

		public String getCOMPANY_NAME() {
			return COMPANY_NAME;
		}

		public void setCOMPANY_NAME(String cOMPANY_NAME) {
			COMPANY_NAME = cOMPANY_NAME;
		}

		public String getCOLLECTOR_NAME() {
			return COLLECTOR_NAME;
		}

		public void setCOLLECTOR_NAME(String cOLLECTOR_NAME) {
			COLLECTOR_NAME = cOLLECTOR_NAME;
		}

		public String getACCOUNT() {
			return ACCOUNT;
		}

		public void setACCOUNT(String aCCOUNT) {
			ACCOUNT = aCCOUNT;
		}

		public String getREGISTERED_PHONE() {
			return REGISTERED_PHONE;
		}

		public void setREGISTERED_PHONE(String rEGISTERED_PHONE) {
			REGISTERED_PHONE = rEGISTERED_PHONE;
		}

		public String getBANK() {
			return BANK;
		}

		public void setBANK(String bANK) {
			BANK = bANK;
		}

		public String getCOLLECTOR_ADDRESS() {
			return COLLECTOR_ADDRESS;
		}

		public void setCOLLECTOR_ADDRESS(String cOLLECTOR_ADDRESS) {
			COLLECTOR_ADDRESS = cOLLECTOR_ADDRESS;
		}

		public String getINVOICE_TITLE() {
			return INVOICE_TITLE;
		}

		public void setINVOICE_TITLE(String iNVOICE_TITLE) {
			INVOICE_TITLE = iNVOICE_TITLE;
		}

		public int getINVOICE_TITLE_TYPE() {
			try{
				return Integer.parseInt(INVOICE_TITLE_TYPE);
			}catch(Exception e){
				return 1;
			}
		}

		public void setINVOICE_TITLE_TYPE(String iNVOICE_TITLE_TYPE) {
			INVOICE_TITLE_TYPE = iNVOICE_TITLE_TYPE;
		}

		public String getCOLLECTOR_PHONE() {
			return COLLECTOR_PHONE;
		}

		public void setCOLLECTOR_PHONE(String cOLLECTOR_PHONE) {
			COLLECTOR_PHONE = cOLLECTOR_PHONE;
		}

		public String getREGISTERED_ADDRESS() {
			return REGISTERED_ADDRESS;
		}

		public void setREGISTERED_ADDRESS(String rEGISTERED_ADDRESS) {
			REGISTERED_ADDRESS = rEGISTERED_ADDRESS;
		}

		public int getINVOICING_METHOD() {
			try{
				return Integer.parseInt(INVOICING_METHOD);
			}catch(Exception e){
				return 0;
			}
		}

		public void setINVOICING_METHOD(String iNVOICING_METHOD) {
			INVOICING_METHOD = iNVOICING_METHOD;
		}

		public String getID() {
			return ID;
		}

		public void setID(String iD) {
			ID = iD;
		}

		public String getIDENTIFICATION_NUMBER() {
			return IDENTIFICATION_NUMBER;
		}

		public void setIDENTIFICATION_NUMBER(String iDENTIFICATION_NUMBER) {
			IDENTIFICATION_NUMBER = iDENTIFICATION_NUMBER;
		}

		public int getINVOICE_CONTENT() {
			try{
				return Integer.parseInt(INVOICE_CONTENT);
			}catch(Exception e){
				return 0;
			}
		}

		public void setINVOICE_CONTENT(String iNVOICE_CONTENT) {
			INVOICE_CONTENT = iNVOICE_CONTENT;
		}

	}

	public static InvoiceInfoBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			InvoiceInfoBean experLightBean = gson.fromJson(json,
					InvoiceInfoBean.class);
			return experLightBean;
		} catch (Exception e) {
			LogUtil.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
