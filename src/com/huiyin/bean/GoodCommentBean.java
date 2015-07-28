package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import com.huiyin.api.URLs;
import com.huiyin.utils.StringUtils;

import android.text.TextUtils;

/**
 * 预约商品
 * 
 * @author 刘远祺
 * 
 */
public class GoodCommentBean extends BaseBean {

	public String ID;				// 评价ID
	public String GOODS_PRICE;		// 商品价格
	public String GOODS_ID;			// 商品id
	public String GOODS_NO;			// 商品编号
	public String GOODS_NAME;		// 商品名称,
	public String NORMS_VALUE;		// 规格名称
	public String GOODS_IMG;		// 商品图片
	public String QUANTITY;			// 购买数量

	public String IS_NICE;			// 是否加精：1加精，0没有加精
	public String CONTENT;			// 评价内容
	public String EVA_GRADE;		// 分数
	public String EVA_TIME;			// 评价时间
	public String EVA_IMG;			// 评论图片（多张以逗号隔开）

	public String EVA_TIME_ADD;		// 追加时间
	public String CONTENT_ADD;		// 追加内容（如果追加时间，追加内容都为空，则表明没有追加数据）
	public String EVA_ADD_IMG;		// 追加评论图片（多张以逗号隔开）
	public String IS_ADD_EVA;		// 1有 0没有
	
	
	
	
	/**
	 * 获取评价分数
	 * @return
	 */
	public float getCommentGrade(){
		try{
			return Float.parseFloat(EVA_GRADE);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获得加精文本
	 * @return
	 */
	public String getNice(){
		try{
			return IS_NICE.equals("1") ? "(已申请加精)" : "(未申请加精)";
		}catch(Exception e){
			return "(未申请加精)";
		}
	}
	
	
	/**
	 * 是否有追加评论
	 * @return
	 */
	public boolean isHaveAddComment(){
		try{
			//return IS_ADD_EVA.equals("1");
			
			//逻辑改为--判断追加时间不为空，并且追加内容不为空 add by liuyuanqi 20150721
			return !StringUtils.isEmpty(CONTENT_ADD) && !StringUtils.isEmpty(CONTENT_ADD);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	private List<ImageData> imgList;
	/**获取评论图片集合**/
	public List<ImageData> getImageData(){
		if(null == imgList){
			if(!TextUtils.isEmpty(EVA_IMG)){
				imgList = new ArrayList<ImageData>();
				String[] array = EVA_IMG.split(",");
				for(int i=0; i<array.length; i++){
					imgList.add(new ImageData("", array[i]));
				}
			}
		}
		
		return imgList;
	}
	
	
	private List<ImageData> addImgList;
	/**获取追加评论图片集合**/
	public List<ImageData> getAddImageData(){
		if(null == addImgList){
			if(!TextUtils.isEmpty(EVA_ADD_IMG)){
				addImgList = new ArrayList<ImageData>();
				String[] array = EVA_ADD_IMG.split(",");
				for(int i=0; i<array.length; i++){
					addImgList.add(new ImageData("", array[i]));
				}
			}
		}
		
		return addImgList;
	}
	
	/**
	 * 获取评论图片集-供图片预览使用
	 * @return
	 */
	public ArrayList<String> getCommitImageUrls(){
		
		ArrayList<String> list = new ArrayList<String>();
		if(!TextUtils.isEmpty(EVA_IMG)){
			String[] array = EVA_IMG.split(",");
			for(int i=0; i<array.length; i++){
				list.add(URLs.IMAGE_URL + array[i]);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取评论图片集-供图片预览使用
	 * @return
	 */
	public ArrayList<String> getCommitAddImageUrls(){
		
		ArrayList<String> list = new ArrayList<String>();
		if(!TextUtils.isEmpty(EVA_ADD_IMG)){
			String[] array = EVA_ADD_IMG.split(",");
			for(int i=0; i<array.length; i++){
				list.add(URLs.IMAGE_URL + array[i]);
			}
		}
		
		return list;
	}

	private List<ImageData> addUploadImg =  new ArrayList<ImageData>();
	/**添加一张上传的图片**/
	public void addUploadImgFile(String imgFile){
		if(TextUtils.isEmpty(imgFile)){
			return;
		}
		
		//假设文件不存在
		boolean imgFileNotExsit = true;
		for(int i=0; i<addUploadImg.size(); i++){
			if(addUploadImg.get(i).equalsFile(imgFile)){
				imgFileNotExsit = false;
				break;
			}
		}
		
		//如果列表里面没有该图片，则加入到图片列表里面
		if(imgFileNotExsit){
			addUploadImg.add(new ImageData(imgFile, ""));
		}
	}
	
	/**剔除一张上传的图片**/
	public void delUploadImgFile(String imgFile){
		if(TextUtils.isEmpty(imgFile)){
			return;
		}
		
		//假设文件不存在
		ImageData data = null;
		for(int i=0; i<addUploadImg.size(); i++){
			data = addUploadImg.get(i);
			if(data.equalsFile(imgFile)){
				addUploadImg.remove(i);
				i--;
			}
		}
	}
	/**获取上传的图片**/
	public List<ImageData> getAddUploadImgFile(){
		return addUploadImg;
	}
	
	
	
	
	
	
	
	
	/**要追加的评论的内容**/
	private String commitContent;
	public void setCommitContent(String content){
		this.commitContent = content;
	}
	public String getCommitContent(){
		return this.commitContent;
	}

	/**
	 * 判断是否有评论图片
	 */
	public boolean hasCommentPic() {
		return !TextUtils.isEmpty(EVA_IMG);
	}
	
	/**
	 * 判断是否有追加评论的图片
	 * @return
	 */
	public boolean hasAddCommentPic(){
		return !TextUtils.isEmpty(EVA_ADD_IMG);
	}
}