package com.huiyin.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.ComplainDataEntity;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.ui.user.complaint.ComplaintDetailActivity;
import com.huiyin.ui.user.order.AllOrderDetailActivity;
import com.huiyin.utils.ImageManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 投诉列表适配器
 * Created by kuangyong on 2015/6/8.
 */
public class MyComplaintListAdapter extends BaseAdapter {
    private Activity context;
    private List<ComplainDataEntity>  orderList;

    public MyComplaintListAdapter(Activity context, List<ComplainDataEntity> orderList) {
        this.context = context;
        this.orderList = orderList;
    }
    
    public void setOrderList( List<ComplainDataEntity> orderList){
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
                    R.layout.layout_complaint_lv_item, null);
            holder = new OrderHolder();
            holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
            holder.tv_store_name = (TextView) convertView
                    .findViewById(R.id.tv_store_name);
            holder.tv_order_no = (TextView) convertView
                    .findViewById(R.id.tv_order_no);
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
            holder.btn_complaint_content = (TextView) convertView.findViewById(R.id.btn_complaint_content);
            convertView.setTag(holder);
        }
        holder = (OrderHolder) convertView.getTag();
        final int storeId=Integer.parseInt(orderList.get(position).getSTORE_ID());
        if ( storeId> 0) {// 店铺
            ImageManager.LoadWithServer(orderList.get(position).getSTORE_LOGO(), holder.iv_logo);
            holder.tv_store_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_black_arraw, 0);
            holder.tv_store_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StoreHomeActivity.class);
                    intent.putExtra(StoreHomeActivity.STORE_ID, storeId);
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
        if(3==orderList.get(position).getAUDIT_STATUS()){//1.待审核(等待处理)2.处理中，3处理解决
            holder.tv_order_no.setVisibility(View.GONE);
        }else{
            holder.tv_order_no.setVisibility(View.VISIBLE);
        }
        holder.tv_order_no.setText("订单编号："+orderList.get(position).getORDER_CODE());
        holder.tv_store_name.setText(orderList.get(position).getSTORE_NAME()+"  ");
        holder.tv_status.setText(orderList.get(position).getSTATUS_NAME());
        setShowOrGone(holder, View.GONE);
        if (null!=orderList.get(position).getComplainDetaillList()&&1 == orderList.get(position).getComplainDetaillList().size()) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).getComplainDetaillList().get(0).getGOODS_IMG(),
                    holder.iv_order1);
            holder.tv_describ
                    .setText(orderList.get(position).getComplainDetaillList().get(0).getGOODS_NAME());
            holder.tv_describ.setVisibility(View.VISIBLE);
            holder.iv_order1.setVisibility(View.VISIBLE);
        } else if (null!=orderList.get(position).getComplainDetaillList()&&2 == orderList.get(position).getComplainDetaillList().size()) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).getComplainDetaillList().get(0).getGOODS_IMG(),
                    holder.iv_order1);
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).getComplainDetaillList().get(1).getGOODS_IMG(),
                    holder.iv_order2);
            holder.iv_order1.setVisibility(View.VISIBLE);
            holder.iv_order2.setVisibility(View.VISIBLE);
        } else if (null!=orderList.get(position).getComplainDetaillList()&&3 <= orderList.get(position).getComplainDetaillList().size()) {
            setShowOrGone(holder, View.VISIBLE);
            holder.tv_describ.setVisibility(View.GONE);
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).getComplainDetaillList().get(0).getGOODS_IMG(),
                    holder.iv_order1);
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).getComplainDetaillList().get(1).getGOODS_IMG(),
                    holder.iv_order2);
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).getComplainDetaillList().get(2).getGOODS_IMG(),
                    holder.iv_order3);
        }
        holder.tv_price.setText("投诉时间："+orderList.get(position).getCREATE_DATE());
        holder.layout_order.setOnClickListener(new View.OnClickListener() {//点击进入订单详情
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllOrderDetailActivity.class);
                intent.putExtra(AllOrderDetailActivity.ORDER_ID, orderList.get(position).getORDER_ID());
                context.startActivity(intent);
            }
        });
        holder.layout_order.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        holder.btn_complaint_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//投诉详情
                Intent intent=new Intent(context,ComplaintDetailActivity.class);
                intent.putExtra(ComplaintDetailActivity.COMPLAIN_ID,orderList.get(position).getID());
                context.startActivity(intent);
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
        public TextView tv_order_no;// 订单编号
        public TextView tv_status;// 状态
        public ImageView iv_order1;// 产品图片1
        public ImageView iv_order2;// 产品图片2
        public ImageView iv_order3;// 产品图片3
        public TextView tv_point;// 。。。
        public TextView tv_describ;// 产品描述
        public TextView tv_price;// 价格
        public TextView btn_complaint_content;// 举报内容
        public LinearLayout layout_order;// item
    }
}
