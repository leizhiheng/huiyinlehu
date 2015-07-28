package com.huiyin.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.huiyin.bean.GoodBean;
import com.huiyin.bean.GoodLikeBean;
import com.huiyin.bean.GoodsListEntity;
import com.huiyin.bean.ReportBaseInfoTitle;
import com.huiyin.bean.ReportBaseInfoType;
import com.huiyin.bean.ReportItem;
import com.huiyin.bean.ReportItemDetail;
import com.huiyin.bean.SysMessageList;

/**
 * Json解析工具，处理简单的解析
 * @author 刘远祺
 *
 */
public class JSONParseUtils {

	/**
	 * 解析一个key值(方便 解析状态值)
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static String getString(String json, String key){
		try {
			JSONObject obj = new JSONObject(json);
			return obj.optString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return null;
	}
	
	/**
	 * 解析一个key值(方便 解析状态值)
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static String getString(JSONObject jsonObj, String key){
		try {
			return jsonObj.optString(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return null;
	}
	
	/**
	 * 解析一个key值(方便 解析状态值)
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static int getInt(String json, String key){
		try {
			JSONObject obj = new JSONObject(json);
			return obj.optInt(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		return -1;
	}

	/**
	 * 获取新增预约中商品的json
	 * @param goods
	 * @return
	 */
	public static String get_addBespeak_goods_JSON(List<GoodBean> goods) {
		JSONArray array = new JSONArray();
		if(null != goods){
			JSONObject obj = null;
			for(int i=0; i<goods.size(); i++){
				obj = new JSONObject();
				try {
					obj.put("goodsId", goods.get(i).GOODS_ID);
					obj.put("goodsNo", goods.get(i).GOODS_NO);
					obj.put("num", goods.get(i).getBespeakNumber());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				array.put(obj);
			}
		}
		return array.toString();
	}
	
	/**
	 * 获取新增预约中商品的json
	 * @param goods
	 * @return
	 */
	public static String get_amendReplace_goods_JSON(List<GoodsListEntity> goods) {
		JSONArray array = new JSONArray();
		if(null != goods){
			JSONObject obj = null;
			for(int i=0; i<goods.size(); i++){
				obj = new JSONObject();
				try {
					obj.put("replaceDetailId", goods.get(i).getID());
					obj.put("num", goods.get(i).getReplaceUpdateNumber()+"");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				array.put(obj);
			}
		}
		return array.toString();
	}
	
	/**
	 * 获取修改换货商品的json
	 * @param goods
	 * @return
	 */
	public static String get_amendReturn_goods_JSON(List<GoodsListEntity> goods) {
		JSONArray array = new JSONArray();
		if(null != goods){
			JSONObject obj = null;
			for(int i=0; i<goods.size(); i++){
				obj = new JSONObject();
				try {
					obj.put("returnDetailId", goods.get(i).getID());
					obj.put("orderDetailId", goods.get(i).getORDER_DETAIL_ID());
					obj.put("num", goods.get(i).getReplaceUpdateNumber()+"");
					obj.put("oldNum", goods.get(i).getQUANTITY());
					obj.put("goodsName", goods.get(i).getGOODS_NAME());

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				array.put(obj);
			}
		}
		return array.toString();
	}
	
	/**
	 * 获取新增预约中商品的json
	 * @param goods
	 * @return
	 */
	public static String get_applyReplace_goods_JSON(List<GoodsListEntity> goods) {
		JSONArray array = new JSONArray();
		if(null != goods){
			JSONObject obj = null;
			for(int i=0; i<goods.size(); i++){
				obj = new JSONObject();
				try {
					obj.put("goodsId", goods.get(i).getGOODS_ID()+"");
					obj.put("goodsNo", goods.get(i).getGOODS_NO());
					obj.put("storeId", goods.get(i).getSTORE_ID());
					obj.put("num", goods.get(i).getReplaceAddNumber()+"");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				array.put(obj);
			}
		}
		return array.toString();
	}
	
	/**
	 * 获取新增退货中商品的json
	 * @param goods
	 * @return
	 */
	public static String get_applyReturn_goods_JSON(List<GoodsListEntity> goods) {
		JSONArray array = new JSONArray();
		if(null != goods){
			JSONObject obj = null;
			for(int i=0; i<goods.size(); i++){
				obj = new JSONObject();
				try {
					obj.put("orderDetailId", goods.get(i).getORDER_DETAIL_ID()+"");
					obj.put("goodsName", goods.get(i).getGOODS_NAME());
					obj.put("goodsId", goods.get(i).getGOODS_ID());
					obj.put("goodsNo", goods.get(i).getGOODS_NO());
					obj.put("storeId", goods.get(i).getSTORE_ID());
					obj.put("num", goods.get(i).getReplaceAddNumber()+"");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				array.put(obj);
			}
		}
		return array.toString();
	}
	
	/**
	 * 解析基本举报信息
	 */
	public static ArrayList<ReportBaseInfoType> parseReportBaseInfo(String jsonString){
		ArrayList<ReportBaseInfoType> list = new ArrayList<ReportBaseInfoType>();
		ReportBaseInfoType baseInfo = null;
		try {
			JSONObject obj = new JSONObject(jsonString);
			JSONArray typeArray = obj.getJSONArray("reportBaseInfo");
			for(int i = 0;i < typeArray.length();i++){
				
				JSONObject typeObject = (JSONObject) typeArray.get(i);//获取一个举报类型
				String typeName = typeObject.getString("NAME");
				String typeDescription = typeObject.getString("DESCRPTION");
				int typeId = typeObject.getInt("ID");
				baseInfo = new ReportBaseInfoType(typeName, typeId);
				baseInfo.setTypeDescription(typeDescription);
				JSONArray titleArray = typeObject.getJSONArray("titleList");//获取该举报类型下的所有举报主题
				for(int j = 0;j < titleArray.length();j++){
					JSONObject titleObject = (JSONObject) titleArray.get(j);
					String titleName = titleObject.getString("TITLE");
					int titleId = titleObject.getInt("ID");
					baseInfo.addTitle(new ReportBaseInfoTitle(titleName, titleId,typeId));
				}
				list.add(baseInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 解析举报列表信息
	 */
	public static ArrayList<ReportItem> parseReportList(String jsonString){
		ArrayList<ReportItem> list = new ArrayList<ReportItem>();
		JSONArray reportArray;
		JSONObject reportOjb = null;
		ReportItem item = null;
		try {
			reportOjb = new JSONObject(jsonString);
			reportArray = reportOjb.getJSONArray("reportData"); 
			for(int i=0;i<reportArray.length();i++){
				reportOjb = (JSONObject) reportArray.get(i);
				String time = reportOjb.getString("TIME");
				long userId = reportOjb.getLong("USER_ID");
				long goodsId = reportOjb.getLong("GOODS_ID");
				long goodsNo = reportOjb.getLong("GOODS_NO");
				long storeId = reportOjb.getLong("STORE_ID");
				String storeName = reportOjb.getString("STORE_NAME");
				String storeLogo = reportOjb.getString("STORE_LOGO");
				String goodsName = reportOjb.getString("GOODS_NAME");
				long reportId = reportOjb.getLong("ID");
				int status = reportOjb.getInt("STATUS");
				String goodsImg = reportOjb.getString("GOODS_IMG");
				int num = reportOjb.getInt("NUM");
				
				item = new ReportItem();
				item.setReportTime(time);
				item.setUserId(userId);
				item.setGoodsId(goodsId);
				item.setGoodsNo(goodsNo);
				item.setStoreId(storeId);
				item.setStoreName(storeName);
				item.setStoreLogo(storeLogo);
				item.setGoodsName(goodsName);
				item.setReportId(reportId);
				item.setStatus(status);
				item.setGoodsImg(goodsImg);
				item.setNum(num);
				
				list.add(item);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 * 解析举报详情
	 */
	public static ReportItemDetail parseReportDetail(String jsonString){
		ReportItemDetail detail = new ReportItemDetail();
		JSONObject detailObject = null;
		try {
		    detailObject = new JSONObject(jsonString);
		    detailObject =(JSONObject) detailObject.get("reportId");
			String typeName = detailObject.getString("TYPE_NAME");
			String goodsImg = detailObject.getString("GOODS_IMG");
			String goodsPrice =  detailObject.getString("GOODS_PRICE");
			String proceType = detailObject.getString("PROCE_TYPE");
			String proceContent = detailObject.getString("PROCE_CONTENT");
			String reportContent = detailObject.getString("CONTENT");
			String titleName = detailObject.getString("TITLE_NAME");
			String createDate = detailObject.getString("CREATE_DATE");
			String goodsNo = detailObject.getString("GOODS_NO");
			String storeId = detailObject.getString("STORE_ID");
			String goodsName = detailObject.getString("GOODS_NAME");
			String status = detailObject.getString("STATUS");
			String goodsId = detailObject.getString("GOODS_ID");
			String img = detailObject.getString("IMG");
			
			detail.setTypeName(typeName);
			detail.setGoodsImg(goodsImg);
			detail.setGoodsPrice(goodsPrice);
			detail.setProceType(proceType);
			detail.setProceContent(proceContent);
			detail.setReportContent(reportContent);
			detail.setTitleName(titleName);
			detail.setCreateDate(createDate);
			detail.setGoodsNo(goodsNo);
			detail.setStoreId(storeId);
			detail.setGoodsName(goodsName);
			detail.setStatus(status);
			detail.setGoodsId(goodsId);
			detail.setImg(img);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return detail;
	}

	/**
	 * 判断http返回的json是否为一个正常的返回值
	 * @param content
	 * @return
	 */
	public static boolean isErrorJSONResult(String content) {
		
		if(!TextUtils.isEmpty(content)){
			int type = getInt(content, "type");
			return type != 1;
		}
		return true;
	}

	/**
	 * 返回key对应的jsonObject
	 * @param content
	 * @param key
	 * @return
	 */
	public static String getJSONObject(String content, String key) {
		try{
			JSONObject obj = new JSONObject(content);
			return obj.optJSONObject(key).toString();
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 解析系统消息
	 * @param json
	 * @return
	 */
	public static SysMessageList parseSysMessageList(String json){
		SysMessageList list = new SysMessageList();
		try {
			JSONObject obj = new JSONObject(json);
			list.parseData(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 解析猜你喜欢
	 * @param content
	 * @return
	 */
	public static List<GoodLikeBean> parseMaybeLike(String content) {
		List<GoodLikeBean> maybeLike = new ArrayList<GoodLikeBean>();
		if(null != content){
			try {
				JSONObject obj  = new JSONObject(content);
				JSONArray array = obj.optJSONArray("goodsList");
				for(int i=0; i<array.length(); i++){
					maybeLike.add(new GoodLikeBean(array.optJSONObject(i)));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return maybeLike;
	}
}
