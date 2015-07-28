package com.huiyin.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 猜你喜欢-商品
 * 
 * @author 刘远祺
 * 
 */
public class GoodLikeBeanList implements Serializable {

	public List<GoodLikeBean> dataList;

	public GoodLikeBeanList(List<GoodLikeBean> dataList) {
		this.dataList = dataList;
	}

}