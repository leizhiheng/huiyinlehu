package com.huiyin.bean;

import org.json.JSONException;
import org.json.JSONObject;

import com.huiyin.utils.StringUtils;

/**
 * 商品内容
 * 
 * @author 刘远祺
 * 
 * @todo TODO
 * 
 * @date 2015-7-10
 */
public class GoodsContent extends BaseBean {
	private static final long serialVersionUID = 1L;
	public String info_content_map;			// 商品信息
	public String packinglist_content_map;	// 包装参数,
	public String afterservice_content_map;	// 售后服务
	public String curTime;					// 系统当前时间

	public GoodsContent(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			type = obj.optInt("type");
			msg = obj.optString("msg");
			curTime = obj.optString("curTime");

			JSONObject content = obj.optJSONObject("goodsContent");
			info_content_map = content.optString("info_content_map");
			packinglist_content_map = content.optString("packinglist_content_map");
			afterservice_content_map = content.optString("afterservice_content_map");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getInfo_content_map() {
		if(StringUtils.isEmpty(info_content_map)){
			return "";
		}
		return info_content_map;
	}

	public String getAfterservice_content_map() {
		if(StringUtils.isEmpty(afterservice_content_map)){
			return "";
		}
		return afterservice_content_map;
	}

	public String gePackinglist_content_map() {
		if(StringUtils.isEmpty(packinglist_content_map)){
			return "";
		}
		return packinglist_content_map;
	}
}
