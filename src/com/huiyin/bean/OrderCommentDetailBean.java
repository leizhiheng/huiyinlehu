package com.huiyin.bean;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.utils.LogUtil;

/**
 * 订单评论详情
 * 
 * @author 刘远祺
 * 
 */
public class OrderCommentDetailBean extends BaseBean {

	/**评论商品集合**/
	public List<GoodCommentBean> evaList;

	public static OrderCommentDetailBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			OrderCommentDetailBean orderbean = gson.fromJson(json, OrderCommentDetailBean.class);
			return orderbean;
		} catch (Exception e) {
			LogUtil.d("dataExplainJson", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}
}
