package com.huiyin.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.huiyin.utils.LogUtil;

public class LehuVoucherTypeListBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Expose
	private ArrayList<List> list = new ArrayList<List>();

	/**
	 * 
	 * @return The list
	 */
	public ArrayList<List> getList() {
		return list;
	}

	/**
	 * 判断是否有数据
	 * @return
	 */
	public boolean hasData(){
		return null != list && list.size() > 0;
	}
	
	/**
	 * 
	 * @param list
	 *            The list
	 */
	public void setList(ArrayList<List> list) {
		this.list = list;
	}

	public class List {

		@Expose
		private String NAME;
		@Expose
		private int ID;

		/**
		 * 
		 * @return The NAME
		 */
		public String getNAME() {
			return NAME;
		}

		/**
		 * 
		 * @param NAME
		 *            The NAME
		 */
		public void setNAME(String NAME) {
			this.NAME = NAME;
		}

		/**
		 * 
		 * @return The ID
		 */
		public int getID() {
			return ID;
		}

		/**
		 * 
		 * @param ID
		 *            The ID
		 */
		public void setID(int ID) {
			this.ID = ID;
		}

	}
	public static LehuVoucherTypeListBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			LehuVoucherTypeListBean experLightBean = gson.fromJson(json, LehuVoucherTypeListBean.class);
			return experLightBean;
		} catch (Exception e) {
			LogUtil.d("LehuVoucherListBean", e.toString());
			return null;

		}
	}
	
	
	/**List数据 Map**/
	private Map<String, String> dataMap;
	public Map<String, String> getDataMap(){
		if(null == dataMap){
			dataMap = new HashMap<String, String>();
			ArrayList<List> list = getList();
			if(null != list){
				for(int i=0; i<list.size(); i++){
					dataMap.put(list.get(i).ID+"", list.get(i).NAME);
				}
			}
		}
		return dataMap;
	}
}
