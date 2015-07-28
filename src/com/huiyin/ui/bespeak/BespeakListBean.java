package com.huiyin.ui.bespeak;

import com.huiyin.bean.BaseBean;

import java.util.ArrayList;

public class BespeakListBean extends BaseBean {

	private static final long serialVersionUID = 1L;
	public ArrayList<BespeakListItem> goodsList = new ArrayList<BespeakListBean.BespeakListItem>();

	public class BespeakListItem {
		public String START_BESPEAK_TIME;// 开始预约时间
		public String END_BESPEAK_TIME;// 结束预约时间
		public String NEW_PRODUCT_PICTURE;// 新品图片
		public String COMMODITY_NAME;// 商品名称
		public String SLOGAN;// 广告标语
		public String NEW_PRODUCT_DESCRIBE;// 新品描述
		public String BESPEAK_PRICE;// 预约价
		public String BESPEAK_NUMBER;// 预约数量
		public String COMMODITY_ID;// 商品ID
		public String GOODS_ID;// 商品ID
		public String ID;// 预约活动id
		public int BESPEAK_MARK;// 是否预约(>0预约过，<=0没有)
        public String STORE_ID;//店铺id
        public String GOODS_NO;//商品编号
        public String NEW_PRODUCT_TYPE_ID;//分类id
        public int NUM;//数量
	}
}
