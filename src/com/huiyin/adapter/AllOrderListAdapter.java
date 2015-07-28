package com.huiyin.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.huiyin.api.URLs;
import com.huiyin.bean.GoodList;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.ui.shoppingcar.CommitOrderActivity;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.ui.user.order.AllOrderActivity;
import com.huiyin.ui.user.order.AllOrderDetailActivity;
import com.huiyin.ui.user.order.OrderCommentActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ResourceUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lian on 2015/5/20.
 */
public class AllOrderListAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<GoodList> orderList;

    public AllOrderListAdapter(Activity context, ArrayList<GoodList> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOrderList( ArrayList<GoodList> orderList){
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return null != orderList ? orderList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        OrderHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_order_lv_item, null);
            holder = new OrderHolder();
            holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.tv_store_name = (TextView) convertView
                    .findViewById(R.id.tv_store_name);
            holder.layout_order = (LinearLayout) convertView
                    .findViewById(R.id.layout_order);
            holder.tv_status = (TextView) convertView
                    .findViewById(R.id.tv_status);
            holder.iv_order1 = (ImageView) convertView
                    .findViewById(R.id.iv_order1);
            holder.iv_order2 = (ImageView) convertView
                    .findViewById(R.id.iv_order2);
            holder.iv_order3 = (ImageView) convertView
                    .findViewById(R.id.iv_order3);
            holder.tv_point = (TextView) convertView
                    .findViewById(R.id.tv_point);
            holder.tv_describ = (TextView) convertView
                    .findViewById(R.id.tv_describ);
            holder.tv_price = (TextView) convertView
                    .findViewById(R.id.tv_price);
            holder.btn_pay = (TextView) convertView.findViewById(R.id.btn_pay);
            convertView.setTag(holder);
        }else{
        	holder = (OrderHolder) convertView.getTag();
        }
        
        if (orderList.get(position).SELLER > 0) {// 店铺
            ImageManager.LoadWithServer(orderList.get(position).STORE_LOGO, holder.iv_logo);
            holder.tv_store_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_black_arraw, 0);
            holder.tv_store_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StoreHomeActivity.class);
                    intent.putExtra(StoreHomeActivity.STORE_ID, orderList.get(position).SELLER);
                    context.startActivity(intent);
                }
            });
        } else {// 乐虎自营
            holder.tv_store_name.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
            holder.tv_store_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.iv_logo.setImageResource(R.drawable.lehu);
        }
        holder.tv_store_name.setText(orderList.get(position).STORE_NAME+"  ");
        holder.tv_status.setText(orderList.get(position).STATUS_NAME);
        setShowOrGone(holder, View.GONE);
        if (null!=orderList.get(position).orderDetalList&&1 == orderList.get(position).orderDetalList.size()) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).orderDetalList.get(0).GOODS_IMG,
                    holder.iv_order1);
            holder.tv_describ
                    .setText(orderList.get(position).orderDetalList.get(0).GOODS_NAME);
            holder.tv_describ.setVisibility(View.VISIBLE);
            holder.iv_order1.setVisibility(View.VISIBLE);
        } else if (null!=orderList.get(position).orderDetalList&&2 == orderList.get(position).orderDetalList.size()) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).orderDetalList.get(0).GOODS_IMG,
                    holder.iv_order1);
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).orderDetalList.get(1).GOODS_IMG,
                    holder.iv_order2);
            holder.iv_order1.setVisibility(View.VISIBLE);
            holder.iv_order2.setVisibility(View.VISIBLE);
        } else if (null!=orderList.get(position).orderDetalList&&3 <= orderList.get(position).orderDetalList.size()) {
            setShowOrGone(holder, View.VISIBLE);
            holder.tv_describ.setVisibility(View.GONE);
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).orderDetalList.get(0).GOODS_IMG,
                    holder.iv_order1);
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).orderDetalList.get(1).GOODS_IMG,
                    holder.iv_order2);
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).orderDetalList.get(2).GOODS_IMG,
                    holder.iv_order3);
        }
        float total = orderList.get(position).TOTAL_PRICE;
        Spanned price = Html.fromHtml("实付款："
                + ResourceUtils.changeStringColor("#DC434F",
                MathUtil.priceForAppWithSign(total)));
        holder.tv_price.setText(price);
        final String status = orderList.get(position).STATUS;
        holder.btn_pay.setVisibility(View.VISIBLE);
        if (null != status && status.equals("1")) {// 未付款
            holder.btn_pay.setText("去付款");
        } else if (null != status && status.equals("2")) {// 订单待审核
            holder.btn_pay.setVisibility(View.GONE);
        } else if (null != status && status.equals("3")) {// 等待发货
            // 未做，暂时隐藏
            holder.btn_pay.setText("提醒发货");
            holder.btn_pay.setVisibility(View.GONE);
        } else if (null != status && status.equals("4")) {// 商品已出库
            holder.btn_pay.setText("确认收货");
        } else if (null != status && status.equals("5")) {// 交易完成
            if("1".equals(orderList.get(position).COMMENTS_STATUS)){
                holder.btn_pay.setVisibility(View.GONE);
            }else {
            	//0未评论
            	holder.btn_pay.setText("评价晒单");
            }
        } else if (null != status && status.equals("6")) {// 取消订单
            holder.btn_pay.setVisibility(View.GONE);
        }else{

        	//退换货订单，不显示按钮
        	holder.btn_pay.setVisibility(View.GONE);
        }
        
        holder.layout_order.setOnClickListener(new View.OnClickListener() {//点击进入订单详情
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AllOrderDetailActivity.class);
                intent.putExtra(AllOrderDetailActivity.GOOD_LIST, orderList.get(position));
                ((AllOrderActivity)context).startActivityForResult(intent,AllOrderActivity.ORDER_DETAIL);
            }
        });
        holder.layout_order.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        holder.btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != status && status.equals("1")) {// 未付款
                    String number = orderList.get(position).ORDER_CODE;
                    String serialNum = orderList.get(position).ID;
                    float totalPrice = orderList.get(position).TOTAL_PRICE;
                    Intent intent = new Intent(context, CommitOrderActivity.class);
                    intent.putExtra("number", number);
                    intent.putExtra("serialNum", serialNum);
                    intent.putExtra("totalPrice", totalPrice + "");
                    context.startActivity(intent);
                } else if (null != status && status.equals("3")) {// 等待发货
                    // 未做，暂时隐藏
                } else if (null != status && status.equals("4")) {// 确认收货
                    Intent intent = new Intent(context, CommonConfrimCancelDialog.class);
                    intent.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_SURE_RECEIVE);
                    intent.putExtra(AllOrderActivity.CONFIRM_RECEIPT_ORDERID,orderList.get(position).ID);
                    context.startActivityForResult(intent,AllOrderActivity.CONFIRM_RECEIPT);
                } else if (null != status && status.equals("5")) {// 交易完成
                  
                	Intent intent = new Intent(context, OrderCommentActivity.class);
                    intent.putExtra(OrderCommentActivity.ORDER_ITEM, orderList.get(position));
                    context.startActivityForResult(intent, AllOrderActivity.Request_Code_OrderComment);
                }
            }
        });
        return convertView;
    }

    public void setShowOrGone(OrderHolder holder, int visible) {
        holder.iv_order1.setVisibility(visible);
        holder.iv_order2.setVisibility(visible);
        holder.iv_order3.setVisibility(visible);
        holder.tv_point.setVisibility(visible);
        holder.tv_describ.setVisibility(visible);
    }

    static class OrderHolder {
        public ImageView iv_logo;// 店铺logo
        public TextView tv_store_name;// 店铺名称
        public TextView tv_status;// 状态
        public ImageView iv_order1;// 产品图片1
        public ImageView iv_order2;// 产品图片2
        public ImageView iv_order3;// 产品图片3
        public TextView tv_point;// 。。。
        public TextView tv_describ;// 产品描述
        public TextView tv_price;// 价格
        public TextView btn_pay;// 去付款
        public LinearLayout layout_order;// item
    }
}
