package com.huiyin.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.MyOrderDetailBean;
import com.huiyin.bean.MyOrderDetailBean.Coupon;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ResourceUtils;

import java.util.ArrayList;

/**
 * Created by lian on 2015/5/24.
 */
public class OrderDetailProductListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<MyOrderDetailBean> productList;

    public OrderDetailProductListAdapter(Context context,ArrayList<MyOrderDetailBean> productList){
        this.context=context;
        this.productList=productList;
    }
    @Override
    public int getCount() {
        return null != productList ? productList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder=null;
        if(null==convertView){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_orderdetail_productlist_item,null);
            holder=new MyHolder();
            //add by zhyao @2015/6/16 添加消费卷容器
            holder.layout_product_item = (LinearLayout) convertView.findViewById(R.id.layout_product_item);
            holder.iv_product= (ImageView) convertView.findViewById(R.id.iv_product);
            holder.tv_product_name= (TextView) convertView.findViewById(R.id.tv_product_name);
            holder.tv_product_guige= (TextView) convertView.findViewById(R.id.tv_product_guige);
            holder.tv_product_price_and_num= (TextView) convertView.findViewById(R.id.tv_product_price_and_num);
            convertView.setTag(holder);
        }else
            holder= (MyHolder) convertView.getTag();
        MyOrderDetailBean product=productList.get(position);
        ImageManager.LoadWithServer(product.GOODS_IMG, holder.iv_product);
        holder.tv_product_name.setText(product.GOODS_NAME);
    	holder.tv_product_guige.setText(product.NORMS_VALUE);
    	double price=product.GOODS_PRICE;
    	Spanned priceAndNum= Html.fromHtml("价格："+ ResourceUtils.changeStringColor("#DC434F",
    			MathUtil.priceForAppWithSign(price))+"&nbsp;&nbsp;x"+product.QUANTITY+"件");
    	holder.tv_product_price_and_num.setText(priceAndNum);
    	// add by zhyao @2015/6/16 添加消费卷列表
    	holder.layout_product_item.removeAllViews();
        if(product.coupons != null && !product.coupons.isEmpty()) {
        	for (Coupon couponItem : product.coupons) {
        		View couponView = LayoutInflater.from(context).inflate(R.layout.layout_orderdetail_couponlist_item,null);
        		TextView tv_coupon_no = (TextView) couponView.findViewById(R.id.tv_coupon_no);//消费卷编号
        		TextView tv_coupon_status= (TextView) couponView.findViewById(R.id.tv_coupon_status);//消费卷使用状态
        		tv_coupon_no.setText(couponItem.CONSUMPTION_ID);
        		if(couponItem.CONSUMPTION_STATUS == 0) {
        			tv_coupon_status.setText("未使用");
                }
                else if(couponItem.CONSUMPTION_STATUS == 1){
                	tv_coupon_status.setText("已使用");
                }
                else {
                	tv_coupon_status.setText("未付款");
                }
        		holder.layout_product_item.addView(couponView);
			}
        }
        return convertView;
    }

    static class MyHolder{
    	//add by zhyao @2015/6/16 添加消费卷容器
    	private LinearLayout layout_product_item;
        private ImageView iv_product;
        private TextView tv_product_name;
        private TextView tv_product_guige;
        private TextView tv_product_price_and_num;
    }
}
