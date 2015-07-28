package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.huiyin.utils.LogUtil;

public class LehuVoucherListBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Expose
	private ArrayList<Content> content = new ArrayList<Content>();

	/**
	 * 
	 * @return The content
	 */
	public ArrayList<Content> getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 *            The content
	 */
	public void setContent(ArrayList<Content> content) {
		this.content = content;
	}

	public class Content {

		@SerializedName("TYPE_NAME")
		@Expose
		private String TYPENAME;
		@SerializedName("TICKET_CODE")
		@Expose
		private String TICKETCODE;
		@Expose
		private float PRICE;
		@Expose
		private String REMARK;
		@SerializedName("APPLY_TIME")
		@Expose
		private String APPLYTIME;
		@Expose
		private int STATUS;
		@Expose
		private int NUM;

		/**
		 * 
		 * @return The TYPENAME
		 */
		public String getTYPENAME() {
			return TYPENAME;
		}

		/**
		 * 
		 * @param TYPENAME
		 *            The TYPE_NAME
		 */
		public void setTYPENAME(String TYPENAME) {
			this.TYPENAME = TYPENAME;
		}

		/**
		 * 
		 * @return The TICKETCODE
		 */
		public String getTICKETCODE() {
			return TICKETCODE;
		}

		/**
		 * 
		 * @param TICKETCODE
		 *            The TICKET_CODE
		 */
		public void setTICKETCODE(String TICKETCODE) {
			this.TICKETCODE = TICKETCODE;
		}

		/**
		 * 
		 * @return The PRICE
		 */
		public float getPRICE() {
			return PRICE;
		}

		/**
		 * 
		 * @param PRICE
		 *            The PRICE
		 */
		public void setPRICE(float PRICE) {
			this.PRICE = PRICE;
		}

		/**
		 * 
		 * @return The REMARK
		 */
		public String getREMARK() {
			return REMARK;
		}

		/**
		 * 
		 * @param REMARK
		 *            The REMARK
		 */
		public void setREMARK(String REMARK) {
			this.REMARK = REMARK;
		}

		/**
		 * 
		 * @return The APPLYTIME
		 */
		public String getAPPLYTIME() {
			return APPLYTIME;
		}

		/**
		 * 
		 * @param APPLYTIME
		 *            The APPLY_TIME
		 */
		public void setAPPLYTIME(String APPLYTIME) {
			this.APPLYTIME = APPLYTIME;
		}

		/**
		 * 
		 * @return The STATUS
		 */
		public int getSTATUS() {
			return STATUS;
		}

		/**
		 * 
		 * @param STATUS
		 *            The STATUS
		 */
		public void setSTATUS(int STATUS) {
			this.STATUS = STATUS;
		}

		/**
		 * 
		 * @return The NUM
		 */
		public int getNUM() {
			return NUM;
		}

		/**
		 * 
		 * @param NUM
		 *            The NUM
		 */
		public void setNUM(int NUM) {
			this.NUM = NUM;
		}

	}

	public static LehuVoucherListBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			LehuVoucherListBean experLightBean = gson.fromJson(json, LehuVoucherListBean.class);
			return experLightBean;
		} catch (Exception e) {
			LogUtil.d("LehuVoucherListBean", e.toString());
			return null;

		}
	}
}