package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 系统消息列表
 * 
 * @author 刘远祺
 * 
 * @todo TODO
 * 
 * @date 2015-7-20
 */
public class SysMessageList extends PageBean {

	private static final long serialVersionUID = 1L;
	public List<SysMessage> letterList;

	public void parseData(JSONObject obj){
		type = obj.optInt("type");
		msg = obj.optString("msg");
		pageIndex = obj.optInt("pageIndex");
		totalPageNum = obj.optInt("totalPageNum");
		JSONArray array = obj.optJSONArray("letterList");
		if(null != array && array.length() > 0){
			letterList = new ArrayList<SysMessageList.SysMessage>();
			for(int i=0; i<array.length(); i++){
				letterList.add(new SysMessage(array.optJSONObject(i)));
			}
		}
	}
	
	
	
	public class SysMessage {

		public String CONTENT; // 消息内容
		public String SEND_TIME; // 消息发送时间
		public String ID; // 消息id
		public String NUM; // 1
		public String TITLE; // 消息标题
		public String READ_STATUS; // 1：已阅读 0；未阅读
		public String URL_TYPE; // 跳转类型，相当于通知栏的msgType
		public String JUMP_DATA; // 跳转需要的数据，相当于通知栏的jumpDate

		public SysMessage(JSONObject obj){
			try{
				JUMP_DATA = obj.optJSONObject("JUMP_DATA").toString();
			}catch(Exception e){
				JUMP_DATA = "";
			}
			READ_STATUS = obj.optString("READ_STATUS");
			CONTENT = obj.optString("CONTENT");
			SEND_TIME = obj.optString("SEND_TIME");
			ID = obj.optString("ID");
			URL_TYPE = obj.optString("URL_TYPE");
			NUM = obj.optString("NUM");
			TITLE = obj.optString("TITLE");
		}
		
		@Override
		public String toString() {
			return "SysMessage [ID=" + ID + ", SEND_TIME=" + SEND_TIME + ", SEND_TITLE=" + TITLE + ", SEND_CONTENT=" + CONTENT + ", READ_STATUS=" + READ_STATUS + ", VIEW_TYPE=" + URL_TYPE + "]";
		}

		/**
		 * 获取消息类型
		 * 
		 * @return
		 */
		public int getMsgType() {
			try {
				return Integer.parseInt(URL_TYPE);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
	}
}
