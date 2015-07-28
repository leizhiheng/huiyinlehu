package com.huiyin.bean;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.huiyin.utils.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;


public class DiscountPackageBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public int type;
	public String msg;
    public ArrayList<GOODS> goodsGroup;//套餐列表

    public class GOODS{
        public boolean isSelected;
        public String GROUP_PRICE;//团购价格
        public String LH_PRICE;//乐虎价格
        public String REDUCE_PRICE;//减少价格
        public String NAME;//套餐名称
        public String QUOTA_NUMBER;//限购数量
        public String ID;//活动id
        public String REMARK;//活动备注
        public String STORE_ID;//活动店铺id
        public String USER_GRADE;//会员等级
        public ArrayList<GOOD>  list;//商品列表
    }

    public class GOOD{
        public String AD;//商品广告语
        public String GOODS_PRICE;//商品价格
        public String PRICE;//减少金额
        public String PROMOTION_ID;//活动id
        public String STORE_ID;//商品所在店铺id
        public String GOODS_NO;//商品编号
        public String GOODS_NAME;//商品名称
        public String GOODS_ID;//商品id
        public String IMG;//商品图片
        public String JOIN_STOCK;//参与数量
    }
	
	public static DiscountPackageBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			DiscountPackageBean experLightBean = gson.fromJson(json, DiscountPackageBean.class);
			return experLightBean;
		} catch (Exception e) {
            LogUtil.d("AppShowCancelAttention", e.toString());
			//Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;
		}
	}
	
}
