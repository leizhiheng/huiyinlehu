package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.text.TextUtils;

public class ReportItemDetail extends BaseBean {
	public String typeName;
	public String goodsImg;
	public String goodsPrice;
	public String proceType;
	public String proceContent;
	public String reportContent;
	public String titleName;
	public String createDate;
	public String goodsNo;
	public String storeId;
	public String goodsName;
	private String status;
	private String goodsId;
	private String storeName;
	private String img;
	
	
	public String getImg() {
		return img;
	}
	
	/**
	 * 获取图片集合
	 * @return
	 */
	public List<ImageData> getImageData(){
		List<ImageData> list = new ArrayList<ImageData>();
		if(!TextUtils.isEmpty(img)){
			String[] arr = img.split(",");
			for(int i=0; i<arr.length; i++){
				
				list.add(new ImageData("", arr[i]));
			}
			
		}
		return list;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getGoodsImg() {
		return goodsImg;
	}
	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getProceType() {
		return proceType;
	}
	
	public String getProceTypeText() {
		if (proceType.equals("1")) {
           return ("无效举报");
        } else if (proceType.equals("2")) {
        	return("恶意举报");
        } else if (proceType.equals("3")) {
        	return("有效举报");
        } else {
        	return("未处理");
        }
	}
	
	/**
	 * 是否为未处理
	 * @return
	 */
	public boolean isUnProce(){
		if(null != proceType){
			return !proceType.equals("1") && !proceType.equals("2") && !proceType.equals("3");
		}
		return false;
	}
	
	public void setProceType(String proceType) {
		this.proceType = proceType;
	}
	public String getProceContent() {
		return proceContent;
	}
	public void setProceContent(String proceContent) {
		this.proceContent = proceContent;
	}
	public String getReportContent() {
		return reportContent;
	}
	public void setReportContent(String reportContent) {
		this.reportContent = reportContent;
	}
	public String getTitleName() {
		return titleName;
	}
	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getGoodsNo() {
		return goodsNo;
	}
	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	
}
