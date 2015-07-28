package com.huiyin.ui.user.order;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.ReturnRecordAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.BespeakReturnBean;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.XListView;

/**
 * 查看退换纪录
 *
 */
public class ReturnRecordFragment extends BaseFragment {

    private static final String TAG = "BespeakRecordFragment";
    public static final String DELETE_ORDERID = "deleteId";//删除订单id
    public static final int ORDER_DELETE =1;//删除订单
    
    /**退货，查看详情**/
    public static final int REQUEST_CODE_RETURN = 2;
    
    private XListView xListView;
    
    private LinearLayout layout_empty;//提示数据为空布局
    private LinearLayout layout_search;
    
    private ReturnRecordAdapter adapter;
    
    private BespeakReturnBean orderListBean;
    
    private int pageIndex=1;//当前页

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.layout_search_order_fragment, null);
        return layoutView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ORDER_DELETE:
            	
            	//删除订单
                if(requestCode== Activity.RESULT_OK){
                    String orderId=data.getStringExtra(DELETE_ORDERID);
                    removeOrder(orderId);
                }
                break;
            case REQUEST_CODE_RETURN:
            	
            	// 请退货，返回结果
            	
            	//自动回到第一页，全部刷新订单列表
            	pageIndex = 1;
            	getBespeakRecordData(false);
            	
            	break;
        }
    }

    
    
    public void removeOrder(String orderId) {
        RequstClient.deleteOrder(orderId, 4, new CustomResponseHandler(mContext) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                LogUtil.i(TAG, "removeOrder:" + content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(mContext, errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    refreshData();
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        //加载退换货订单列表
        getBespeakRecordData(true);
    }

    @Override
    public void findView() {
        this.xListView = (XListView)findViewById(R.id.lv_order);
        this.layout_search = (LinearLayout)findViewById(R.id.layout_search);
        this.layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
        layout_search.setVisibility(View.GONE);
        xListView.setPullLoadEnable(false);
        xListView.setFooterDividersEnabled(false);
        xListView.setPullRefreshEnable(true);
        xListView.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getBespeakRecordData(false);
            }

            @Override
            public void onLoadMore() {
                getBespeakRecordData(false);
            }
        });
        
        adapter = new ReturnRecordAdapter(mContext, null);
        xListView.setAdapter(adapter);
    }


    /**
     * 搜索订单
     */
    private void getBespeakRecordData(boolean showDialog) {
        RequstClient.getBespeakRecordData(AppContext.userId, pageIndex, "2", new CustomResponseHandler(mContext, showDialog) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                xListView.stopLoadMore();
                xListView.stopRefresh();
                
                try {
                	
                	//http结果判断
                    if (!isSuccessResponse(content)) {
                        return;
                    }


                    //解析数据
                    BespeakReturnBean tempList = new Gson().fromJson(content, BespeakReturnBean.class);
                    if (pageIndex == 1) {
                    	//下拉刷新
                    	
                        if (null == tempList || tempList.isEmpty()) {
                        	//点单列表为空
                            layout_empty.setVisibility(View.VISIBLE);
                            adapter.refreshData(null);
                            return;
                        }
                        
                        
                        //数据成功才++pageIndex
                        pageIndex++;
                        adapter.refreshData(tempList.getDataList());
                        xListView.setAdapter(adapter);
                        
                        //判断是否有更多的数据
                        if (tempList.hasMoreData()) {
                            xListView.setPullLoadEnable(true);
                            xListView.showFooter();
                        } else {
                        	xListView.hideFooter();
                        	xListView.setPullLoadEnable(false);
                        }
                    } else {
                    	
                        //上拉加载下一页
						if (null == tempList || tempList.isEmpty()) {

							xListView.setPullLoadEnable(false);
							xListView.hideFooter();
							Toast.makeText(mContext, "已无更多数据！", Toast.LENGTH_LONG).show();
							return;
							
						} 
						
						//数据成功才++pageIndex
                        pageIndex++;
                        adapter.appendData(tempList.getDataList());
                        
                        //判断是否有更多的数据
                        if (tempList.hasMoreData()) {
                        	xListView.setPullLoadEnable(true);
                        	xListView.showFooter();
                        } else {
                        	xListView.hideFooter();
                        	xListView.setPullLoadEnable(false);
                        }
                           
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void refreshData(){
        pageIndex=1;
        getBespeakRecordData(false);
    }

}