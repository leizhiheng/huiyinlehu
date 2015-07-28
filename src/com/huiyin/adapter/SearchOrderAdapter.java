package com.huiyin.adapter;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.bean.GoodList;
import com.huiyin.bean.OrderRecordBean;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.ui.user.order.AllOrderActivity;
import com.huiyin.ui.user.order.AllOrderDetailActivity;
import com.huiyin.ui.user.order.ApplyBespeakActivity;
import com.huiyin.ui.user.order.BespeakReturnActivity;
import com.huiyin.ui.user.order.ReplaceAddActivity;
import com.huiyin.utils.ImageManager;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 预约退换货-搜索-订单适配器
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-3
 */
public class SearchOrderAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<GoodList> orderList;
    private OrderRecordBean returnBean;

    public SearchOrderAdapter(Activity context, ArrayList<GoodList> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void setOrderList( ArrayList<GoodList> orderList){
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return null!=orderList?orderList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

	public String getOrderId(int position) {
		return orderList.get(position).ID;
	}
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        OrderHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_search_order_lv_item, null);
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
            holder.tv_commit_order_time = (TextView) convertView
                    .findViewById(R.id.tv_commit_order_time);
            holder.btn_sqyy = (TextView) convertView.findViewById(R.id.btn_sqyy);
            holder.btn_sqsh = (TextView) convertView.findViewById(R.id.btn_sqsh);
            holder.tv_order_no = (TextView) convertView.findViewById(R.id.tv_order_no);
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
        holder.tv_order_no.setText(orderList.get(position).ORDER_CODE);
        holder.tv_store_name.setText(orderList.get(position).STORE_NAME+"  ");
        holder.tv_status.setText(orderList.get(position).STATUS_NAME);
        setShowOrGone(holder, View.GONE);
        if (1 == orderList.get(position).orderDetalList.size()) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).orderDetalList.get(0).GOODS_IMG,
                    holder.iv_order1);
            holder.tv_describ
                    .setText(orderList.get(position).orderDetalList.get(0).GOODS_NAME);
            holder.tv_describ.setVisibility(View.VISIBLE);
            holder.iv_order1.setVisibility(View.VISIBLE);
        } else if (2 == orderList.get(position).orderDetalList.size()) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).orderDetalList.get(0).GOODS_IMG,
                    holder.iv_order1);
            loader.displayImage(URLs.IMAGE_URL
                            + orderList.get(position).orderDetalList.get(1).GOODS_IMG,
                    holder.iv_order2);
            holder.iv_order1.setVisibility(View.VISIBLE);
            holder.iv_order2.setVisibility(View.VISIBLE);
        } else if (3 <= orderList.get(position).orderDetalList.size()) {
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
        holder.tv_commit_order_time.setText("下单时间： "+orderList.get(position).CREATE_DATE);
        if("0".equals(orderList.get(position).FLAG_BESPEAK)){
        	//申请预约
            holder.btn_sqyy.setText("申请预约");
            holder.btn_sqyy.setVisibility(View.VISIBLE);
        }else{
        	//查看预约-(订单列表不做查看预约-add by liuyuanqi)
            holder.btn_sqyy.setText("查看预约");
            holder.btn_sqyy.setVisibility(View.GONE);
        }
        if("5".equals(orderList.get(position).STATUS)){//交易完成
            int FLAG_AFTER=orderList.get(position).FLAG_AFTER;
            if(FLAG_AFTER==0){//0 可以退换货，
                holder.btn_sqsh.setVisibility(View.VISIBLE);
                holder.btn_sqsh.setText("申请售后");
                holder.btn_sqsh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appVerifyChangeAndReturn(orderList.get(position).ID );
                    }
                });
            }else if(FLAG_AFTER>0){//>0 不可以退换货
                holder.btn_sqsh.setVisibility(View.GONE);
            }
        }else{
            holder.btn_sqsh.setVisibility(View.GONE);
        }
        
        //点击进入订单详情 add by liuyuan 20150617
        holder.layout_order.setOnClickListener(new View.OnClickListener() {//点击进入订单详情
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AllOrderDetailActivity.class);
                intent.putExtra(AllOrderDetailActivity.ORDER_ID, orderList.get(position).ID);
                ((BespeakReturnActivity)context).startActivityForResult(intent,AllOrderActivity.ORDER_DETAIL);
            }
        });
        
        //废弃代码 注释--liuyuanqi 20150617
//        holder.layout_order.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
        holder.btn_sqyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!orderList.get(position).hasBespeak()) {
                	// 申请预约
                    Intent intent = new Intent(context, ApplyBespeakActivity.class);
                    intent.putExtra(ApplyBespeakActivity.EXTRA_ORDERID, orderList.get(position).ID);
                    intent.putExtra(ApplyBespeakActivity.EXTRA_NUMBER, orderList.get(position).ORDER_CODE);
                    context.startActivity(intent);
                }else {
                	// 查看预约(订单列表不做该功能，在详情界面里做查看预约--add by liuyuanqi)
//                	String bespeakId = orderList.get(position).BESPEAK_TIME
//                    Intent intent = new Intent(context, YuYueDetailActivity.class);
//                    intent.putExtra(YuYueDetailActivity.EXTRA_BESPEAKID, bespeakId);
//                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    /**
     * 验证是否能申请售后
     */
    private void appVerifyChangeAndReturn(final String orderid) {
        RequstClient.appVerifyAfter(orderid,
                new CustomResponseHandler(context, true) {

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        UIHelper.cloesLoadDialog();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String content) {
                        super.onSuccess(statusCode, headers, content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            if (!obj.getString("type").equals("1")) {
                                String errorMsg = obj.getString("msg");
                                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            returnBean=new Gson().fromJson(content,OrderRecordBean.class);
                            if(returnBean.canReplace()){
                            	
                            	//跳转到售后申请界面
                            	Intent intent = new Intent(context, ReplaceAddActivity.class);
                            	intent.putExtra(ReplaceAddActivity.EXTRA_MODEL, returnBean);
                            	intent.putExtra(ReplaceAddActivity.EXTRA_ORDERID, orderid);
                            	context.startActivity(intent);
                            }else{
                            	Toast.makeText(context, "不能退换货", Toast.LENGTH_SHORT).show();
                            }
                            

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
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
        public TextView tv_order_no;// 订单编号
        public ImageView iv_order1;// 产品图片1
        public ImageView iv_order2;// 产品图片2
        public ImageView iv_order3;// 产品图片3
        public TextView tv_point;// 。。。
        public TextView tv_describ;// 产品描述
        public TextView tv_commit_order_time;// 下单时间
        public TextView btn_sqyy;// 申请预约
        public TextView btn_sqsh;// 申请售后
        public LinearLayout layout_order;// item
    }


}
