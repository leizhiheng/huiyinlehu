package com.huiyin.bean;

import android.text.TextUtils;
import android.text.TextUtils.StringSplitter;

public class ZhuanquGoodbean {

	private String title;
	private String price;
	private String reprice;
	private int id;
	private String imagePath;
	private String saleSum;
	
	/**好评度百分比**/
	private String reviewPercent;
	
	/**好评总数量**/
	private String reviewNumber;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getSaleSum() {
		return saleSum;
	}

	public void setSaleSum(String saleSum) {
		this.saleSum = saleSum;
	}


	@Override
	public String toString() {
		return "ZhuanquGoodbean [title=" + title + ", price=" + price
				+ ", reprice=" + reprice + ", id=" + id + ", imagePath="
				+ imagePath + ", saleSum=" + saleSum + "]";
	}

	public String getReprice() {
		return reprice;
	}

	public void setReprice(String reprice) {
		this.reprice = reprice;
	}

	/**
	 * 获取好评度
	 * @return
	 */
	public String getReviewPersent() {
		StringBuffer sb = new StringBuffer();
		sb.append("好评度：  ");
		sb.append(getReviewPercent()+"%");
		sb.append("("+getReviewNumber()+"人)");
		
		return sb.toString();
	}

	public String getReviewPercent() {
		if(TextUtils.isEmpty(reviewPercent) || "0".equals(reviewPercent)){
			return "100";
		}
		return reviewPercent;
	}

	public void setReviewPercent(String reviewPercent) {
		this.reviewPercent = reviewPercent;
	}

	public String getReviewNumber() {
		if(TextUtils.isEmpty(reviewNumber)){
			return "0";
		}
		return reviewNumber;
	}

	public void setReviewNumber(String reviewNumber) {
		this.reviewNumber = reviewNumber;
	}
}
