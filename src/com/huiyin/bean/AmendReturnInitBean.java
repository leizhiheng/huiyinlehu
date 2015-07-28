package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * 退货初始化
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-3
 */
public class AmendReturnInitBean extends BaseBean{

	public String REASON_TYPE;		//退货理由id
	public String REASON_NAME;		//退货理由名称
	public String ORDER_ID;			//对应的订单id
	public String DESCRIBE;			//问题描述
	public String IMG;				//退货上传的图片

	public List<GoodsListEntity> returnDetailList;	//详情
	public List<ReplaceType> returnList;			//退货理由

	
	public static AmendReturnInitBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			AmendReturnInitBean orderbean = gson.fromJson(json, AmendReturnInitBean.class);
			return orderbean;
		} catch (Exception e) {
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}

	/**
	 * 获取图片数据
	 * @return
	 */
	public List<ImageData> getImageDataList() {
		List<ImageData> list = new ArrayList<ImageData>();
		if(!TextUtils.isEmpty(IMG)){
			String[] array = IMG.split(",");
			for(int i=0; i<array.length; i++){
				list.add(new ImageData("", array[i]));
			}
			
		}
		return list;
	}

	/**
	 * 根据理由ID获取理由文本
	 * @param reasonId
	 * @return
	 */
	public String getReason(String reasonId){
		String reason = "";
		for(ReplaceType type : returnList){
			if(type.ID.equals(reasonId)){
				return type.NAME;
			}
		}
		return reason;
	}

	public List<ReplaceType> getReturnList() {
		return returnList;
	}
	
}
