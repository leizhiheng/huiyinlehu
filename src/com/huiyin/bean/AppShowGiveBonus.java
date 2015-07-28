package com.huiyin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.utils.LogUtil;

// add by zhyao @2015/6/24 添加秀场查询赠送红包返回
public class AppShowGiveBonus extends BaseBean {
	private static final long serialVersionUID = 143234325L;
	
	public String bonus;//用户可用红包金额
	public ArrayList<Info> spotlight;
	public ArrayList<Item> bonusList;
	
	public class Info{
		
		public ArrayList<TouXiang> touxiangList;
		
		public String FACE_IMAGE_PATH;//头像
		public String SPO_IMG;//晒图
		public int USER_ID;//用户id,
		public String LIKENUM;//喜欢人数
		public String CONTENT;//内容
		public String USER_NAME;//用户名
		public String ID;//本页面的SPOTLIGHT_ID
		public String ADDTIME;//时间
		public int STATUS;//是否审核
		public String APPRAISENUM;//评价人数
		public String ATTENTION_ID; //是否关注 为 "-1" 未关注
		// add by zhyao @2015/6/24 添加赠送红包的人数
		public int BONUSNUM;
	}
	
	public class TouXiang{//头像列表
		public String FACE_IMAGE_PATH;//头像
		public int USER_ID;//用户ID
		public String USER_NAME;//用户名
		public String SPOTLIGHT_ID;//列表用户ID
		public String ADDTIME;//时间
	}

	public class Item {
		public String USER_URL;//赠送红包列表头像
		public String BONUS;//赠送红包金额
		public String CREATETIME;//日期
		public String USER_NAME;//用户名
		public String PHONE;//手机号
		public String ACTIVITY_ID;//活动ID
    }
	
	public static AppShowGiveBonus explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			AppShowGiveBonus experLightBean = gson.fromJson(json, AppShowGiveBonus.class);
			return experLightBean;
		} catch (Exception e) {
			LogUtil.d("AppShowGiveBonus", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
	
}
