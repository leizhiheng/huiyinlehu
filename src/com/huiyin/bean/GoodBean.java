package com.huiyin.bean;

import android.text.TextUtils;


/**
 * 预约商品
 * @author 刘远祺
 *
 */
public class GoodBean extends BaseBean {
	
	public String GOODS_IMG;		// 商品图片
	
	public String GOODS_PRICE;		// 商品价格
	
	public String GOODS_NAME;		// 商品名称
	
	public String GOODS_ID;			// 商品id
	
	public String GOODS_NO;			// 商品编号
	
	public String QUANTITY;			// 购买数量
	
	public String NORMS_VALUE;		// 规格数
	
	
	private int bespeakNumber = 1;	//预约数量
	public boolean addBespeak(int num){
		int totalNum = Integer.parseInt(QUANTITY);
		if((bespeakNumber+num) > totalNum){
			//不能大于totalNum
			return false;
		}else if((bespeakNumber+num) < 1){
			//不能小于1
			return false;
		}
		bespeakNumber += num;
		return true;
	}
	
	public int getBespeakNumber(){
		return this.bespeakNumber;
	}
	
}