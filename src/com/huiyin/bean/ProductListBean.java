package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lian on 2015/5/9.
 */
public class ProductListBean {
    public String type;
    public String msg;
    public String totalPageNum;
    public List<GoodList> goodsList = new ArrayList<GoodList>();

    public class GoodList {
        public String GOODS_IMG;// 商品图片
        public String PRICE;// 商品价格
        public String REVIEW_NUMBER;// 评价次数
        public String GOODS_NAME;// 商品名称
        public String GOODS_NO;// 商品编号
        public String GOODS_ID;// 商品id
        public String STORE_ID;// 店铺id
        public String NUM;// 1
        public String REVIEW_PERCENT;// 评价概率
    }
}
