package com.huiyin.ui.user.order;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.SearchOrderAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.MyOrderListBean;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 预约退换货-搜索界面
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-3
 */
public class SearchOrderFragment extends BaseFragment implements View.OnClickListener, OnItemClickListener{
    private static final String TAG = "ReturnRecordFragment";
    private android.widget.ImageView searchorderiv;
    private android.widget.EditText searchcontented;
    private com.huiyin.wight.XListView lvorder;
    private SearchOrderAdapter adapter;
    private MyOrderListBean orderListBean;
    private String keyWord;//关键字
    private int pageIndex=1;//当前页
    private int pageSize=10;//每页数据
    private LinearLayout layout_empty;//提示数据为空布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.layout_search_order_fragment, null);
        return layoutView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
        keyWord="";
        searchOrder(keyWord, true);
    }

    @Override
    public void findView() {
        this.lvorder = (XListView)findViewById(R.id.lv_order);
        this.searchcontented = (EditText) findViewById(R.id.search_content_ed);
        this.searchorderiv = (ImageView) findViewById(R.id.search_order_iv);
        this.layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
       
        lvorder.setOnItemClickListener(this);
        
        lvorder.setPullLoadEnable(false);
        lvorder.setFooterDividersEnabled(false);
        lvorder.setPullRefreshEnable(true);
        lvorder.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                
                pageIndex = 1;
                searchOrder(keyWord, false);
            }

            @Override
            public void onLoadMore() {
            	
            	searchOrder(keyWord, false);
            }
        });
    }

    private void setListener() {
        searchorderiv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_order_iv://搜索
                keyWord=searchcontented.getText().toString();
                pageIndex = 1;
                searchOrder(keyWord, true);
                break;
        }
    }

    /**
     * 搜索订单
     */
    private void searchOrder(String keyWord, boolean showDialog) {
        RequstClient.getOrderListNew(AppContext.userId, 5, pageIndex, keyWord, new CustomResponseHandler(mContext, showDialog){
                @Override
                public void onStart() {
                    super.onStart();
                    lvorder.stopLoadMore();
                    lvorder.stopRefresh();
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers,
                        String content) {
                    super.onSuccess(statusCode, headers, content);
                    LogUtil.i(TAG, "getOrderList:" + content);
                    try {
                        JSONObject obj = new JSONObject(content);
                        if (!obj.getString("type").equals("1")) {
                            String errorMsg = obj.getString("msg");
                            Toast.makeText(mContext, errorMsg,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (pageIndex == 1) {
                        	pageIndex++;
                            orderListBean = new Gson().fromJson(content, MyOrderListBean.class);
                            if (null == orderListBean || null == orderListBean.orderList || 0 == orderListBean.orderList.size()) {//点单列表为空
                                layout_empty.setVisibility(View.VISIBLE);
                                if (null != adapter && null != orderListBean && null !=orderListBean.orderList) {
                                    orderListBean.orderList.clear();
                                    adapter.setOrderList(orderListBean.orderList);
                                    adapter.notifyDataSetChanged();
                                }
                                return;
                            } 
                            adapter = new SearchOrderAdapter(mContext, orderListBean.orderList);
                            lvorder.setAdapter(adapter);
                            if (adapter.getCount() < pageSize) {
                                lvorder.hideFooter();
                                lvorder.setPullLoadEnable(false);
                            } else {
                                lvorder.setPullLoadEnable(true);
                                lvorder.showFooter();
                            }
                        } else {
                            MyOrderListBean bean = new Gson().fromJson(content, MyOrderListBean.class);
                            if (bean.orderList != null) {
                                if (bean.orderList.size() > 0) {
                                	pageIndex++;
                                    orderListBean.orderList.addAll(bean.orderList);
                                    adapter.notifyDataSetChanged();
                                    if (bean.orderList.size() >= 10) {
                                        lvorder.setPullLoadEnable(true);
                                    } else {
                                        lvorder.setPullLoadEnable(false);
                                    }
                                } else {
                                    lvorder.setPullLoadEnable(false);
                                    lvorder.hideFooter();
                                    Toast.makeText(mContext, "已无更多数据！", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                lvorder.setPullLoadEnable(false);
                                lvorder.hideFooter();
                                Toast.makeText(mContext, "已无更多数据！", Toast.LENGTH_LONG).show();
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


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(mContext,AllOrderDetailActivity.class);
        intent.putExtra(AllOrderDetailActivity.ORDER_ID, adapter.getOrderId(arg2));
        startActivityForResult(intent,AllOrderActivity.ORDER_DETAIL);
	}
}
