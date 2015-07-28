package com.huiyin.ui.user.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.AllOrderListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.MyOrderListBean;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 订单列表
 * Created by lian on 2015/5/20.
 */
public class AllOrderActivity extends BaseActivity implements
        View.OnClickListener {

	/**
	 * 订单评价
	 */
	public static final int Request_Code_OrderComment = 100;
	
    private int orderType;
    public static final String ORDER_TYPE = "orderType";
    private static final String TAG = "AllOrderActivity";
    public static final String CONFIRM_RECEIPT_ORDERID = "orderId";//确认收货订单id
    public static final String DELETE_ORDERID = "deleteId";//删除订单id
    public static final int CANSEL_ORDER = 1;//取消订单
    public static final int CONFIRM_RECEIPT = 2;//确认收货
    public static final int ORDER_DETAIL = 3;//订单详情
    public static final int ORDER_DELETE = 4;//删除订单
    private Context context = AllOrderActivity.this;
    private int pageIndex = 1;// 当前页
    private int pageSize = 10;
    private TextView left_ib;// 返回
    private TextView middle_title_tv;// 标题
    private XListView good_xlistview;
    private AllOrderListAdapter adapter;
    private MyOrderListBean orderListBean;
    private LinearLayout layout_empty;//提示数据为空布局

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_all_order);
        orderType = getIntent().getIntExtra(ORDER_TYPE, 1);
        findView();
        initView();
        setListener();
        getData(true);
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	
    	//getData(true);
    }
    
    private void findView() {
        left_ib = (TextView) findViewById(R.id.left_ib);
        middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
        layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
        good_xlistview = (XListView) findViewById(R.id.good_xlistview);
        good_xlistview.setPullLoadEnable(false);
        good_xlistview.setFooterDividersEnabled(false);
        good_xlistview.setPullRefreshEnable(true);
        good_xlistview.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                
                getData(false);
            }

            @Override
            public void onLoadMore() {
                pageIndex = adapter.getCount() / pageSize + 1;
                if (adapter.getCount() % pageSize > 0)
                    pageIndex++;
                getData(false);
            }
        });
    }

    private void initView() {
        String title = "";
        switch (orderType) {
            case 1:// 我的订单
                title = "我的订单";
                break;
            case 2:// 待付款
                title = "待付款";
                break;
            case 3:// 待收货
                title = "待收货";
                break;
            case 4:// 待评价
                title = "待评价";
                break;
            case 5:// 预约/退货
                title = "预约/退货";
                break;
        }
        middle_title_tv.setText(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode) {
            case CANSEL_ORDER://取消订单
                if (ORDER_DETAIL == requestCode) {
                	Log.i(TAG, "刷新订单列表...");
                    refreshData(true);
                }
                break;
            case RESULT_OK://确认收货
                if (CONFIRM_RECEIPT == requestCode) {
                    if (null != data) {
                        String orderId = data.getStringExtra(CONFIRM_RECEIPT_ORDERID);
                        postSureReceive(orderId);
                    }
                }else if (ORDER_DELETE == requestCode) {//删除订单
                    if(null!=data){
                        String orderId=data.getStringExtra(DELETE_ORDERID);
                        removeOrder(orderId);
                    }
                }else if (ORDER_DETAIL == requestCode) {
                	
                	//确认收货（订单详情页）
                    refreshData(true);
                    
                }else if(requestCode == Request_Code_OrderComment){
                	
            		//待评价的订单，等评价完成后，刷新列表
            		refreshData(true);
            	}
            	
            	break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void setListener() {
        left_ib.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_ib:// 返回
                this.finish();
//                Intent intent=new Intent(context,LookForLogisticsActivity.class);
//                this.startActivity(intent);
                break;
        }
    }

    /**
     * 长按删除订单
     */
    AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            int heads=good_xlistview.getHeaderViewsCount();
            position=position-heads;
            if (orderListBean.orderList.get(position).STATUS.equals("6") || orderListBean.orderList.get(position).STATUS.equals("5")) {//交易完成
                Intent intent = new Intent(context, CommonConfrimCancelDialog.class);
                intent.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_DELETE_ORDER);
                intent.putExtra(DELETE_ORDERID, orderListBean.orderList.get(position).ID);
                ((AllOrderActivity)context).startActivityForResult(intent,AllOrderActivity.ORDER_DELETE);
                return true;
            }
            return false;
        }
    };

    public void removeOrder(String orderId) {
        RequstClient.deleteOrder(orderId, 1, new CustomResponseHandler(mContext, true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                LogUtil.i(TAG, "removeOrder:" + content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    refreshData(false);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refreshData(boolean showLoading) {
        pageIndex = 1;
        getData(showLoading);
    }

    public void getData(boolean showLoading) {
        RequstClient.getOrderListNew(AppContext.userId, orderType, pageIndex,"",
                new CustomResponseHandler(this, showLoading) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        good_xlistview.stopLoadMore();
                        good_xlistview.stopRefresh();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String content) {
                        super.onSuccess(statusCode, headers, content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            if (!obj.getString("type").equals("1")) {
                                String errorMsg = obj.getString("msg");
                                Toast.makeText(getBaseContext(), errorMsg,
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (pageIndex == 1) {
                                orderListBean = new Gson().fromJson(content,
                                        MyOrderListBean.class);
                                if (null == orderListBean) {//点单列表为空
                                    layout_empty.setVisibility(View.VISIBLE);
                                    if (null != adapter) {
                                        orderListBean.orderList.clear();
                                        adapter.setOrderList(orderListBean.orderList);
                                        adapter.notifyDataSetChanged();
                                    }
                                    return;
                                } else if ((null != orderListBean &&null == orderListBean.orderList )|| 0 == orderListBean.orderList.size()) {//点单列表为空
                                    layout_empty.setVisibility(View.VISIBLE);
                                    if (null != adapter) {
                                        orderListBean.orderList.clear();
                                        adapter.setOrderList(orderListBean.orderList);
                                        adapter.notifyDataSetChanged();
                                    }
                                    return;
                                }
                                adapter = new AllOrderListAdapter(mContext,
                                        orderListBean.orderList);
                                good_xlistview.setAdapter(adapter);
                                good_xlistview.setOnItemLongClickListener(longClickListener);
                                if (adapter.getCount() < pageSize) {
                                    good_xlistview.hideFooter();
                                    good_xlistview.setPullLoadEnable(false);
                                } else {
                                    good_xlistview.setPullLoadEnable(true);
                                    good_xlistview.showFooter();
                                }
                            } else {
                                MyOrderListBean bean = new Gson().fromJson(
                                        content, MyOrderListBean.class);
                                if (bean.orderList != null) {
                                    if (bean.orderList.size() > 0) {
                                        orderListBean.orderList
                                                .addAll(bean.orderList);
                                        adapter.notifyDataSetChanged();
                                        if (bean.orderList.size() >= 10) {
                                            good_xlistview.setPullLoadEnable(true);
                                        } else {
                                            good_xlistview.setPullLoadEnable(false);
                                        }
                                    } else {
                                        good_xlistview.setPullLoadEnable(false);
                                        Toast.makeText(context, "已无更多数据！",
                                                Toast.LENGTH_LONG).show();
                                        good_xlistview.hideFooter();
                                    }
                                } else {
                                    good_xlistview.setPullLoadEnable(false);
                                    Toast.makeText(context, "已无更多数据！",
                                            Toast.LENGTH_LONG).show();
                                    good_xlistview.hideFooter();
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 确认收货
     */

    private void postSureReceive(String orderId) {

        RequstClient.makeSureOrder(AppContext.userId, orderId,
                new CustomResponseHandler(this,true) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String content) {

                        super.onSuccess(statusCode, headers, content);
                        LogUtil.i(TAG, "makeSureOrCancel:" + content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            if (!obj.getString("type").equals("1")) {
                                String errorMsg = obj.getString("msg");
                                Toast.makeText(getBaseContext(), errorMsg,
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            refreshData(false);
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
    }
}
