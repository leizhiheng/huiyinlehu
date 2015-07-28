package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by 刘远祺 on 2015/6/1.
 */
public class AmendReplaceInitBean extends BaseBean{

    
	public String REASON_TYPE;						//换货理由id
	public String REASON_NAME;						//换货理由
	public String REPLACE_ID;						//换货id
	public String USER_ID;							//用户id
	public String REPLACEMENT_CODE;					//换货编号
	public String QUESTION_DESC;					//换货描述
	public String CREATE_DATE;						//创建时间,
	public String IMG_PATH_LIST;					//图片（多张图片以逗号隔开）,
	public String STATUS_NAME;						//状态名称
	public String REPLACEMENT_STATUS;				//状态id
	public List<GoodsListEntity> replaceDetailList;	//详情
	public List<ReplaceType> replaceList;			//换货理由

	
	public static AmendReplaceInitBean explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			AmendReplaceInitBean orderbean = gson.fromJson(json, AmendReplaceInitBean.class);
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
		if(!TextUtils.isEmpty(IMG_PATH_LIST)){
			String[] array = IMG_PATH_LIST.split(",");
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
		for(ReplaceType type : replaceList){
			if(type.ID.equals(reasonId)){
				return type.NAME;
			}
		}
		return reason;
	}
	
}
