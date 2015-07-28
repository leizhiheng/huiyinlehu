package com.huiyin.ui.user.order;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.BespeakRecordAdapter;
import com.huiyin.adapter.ReturnRecordAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.BespeakReturnBean;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.XListView;

/**
 * 查看预约纪录
 *
 */
public class BespeakRecordFragment extends BaseFragment {

	private static final String TAG = "BespeakRecordFragment";
    private com.huiyin.wight.XListView lvorder;
    private LinearLayout layout_search;
    public static final String DELETE_ORDERID = "deleteId";//删除订单id
    public static final int ORDER_DELETE =1;//删除订单
    private BespeakRecordAdapter adapter;
    private BespeakReturnBean orderListBean;
    private int pageIndex=1;//当前页
    private int pageSize=10;//每页数据
    private LinearLayout layout_empty;//提示数据为空布局

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.layout_search_order_fragment, null);
        return layoutView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ORDER_DELETE://删除订单

                break;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getBespeakRecordData(true);
    }

    @Override
    public void findView() {
        this.lvorder = (XListView)findViewById(R.id.lv_order);
        this.layout_search = (LinearLayout)findViewById(R.id.layout_search);
        this.layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
        layout_search.setVisibility(View.GONE);
        lvorder.setPullLoadEnable(false);
        lvorder.setFooterDividersEnabled(false);
        lvorder.setPullRefreshEnable(true);
        lvorder.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getBespeakRecordData(false);
            }

            @Override
            public void onLoadMore() {
//                pageIndex = adapter.getCount() / pageSize + 1;
//                if (adapter.getCount() % pageSize > 0)
//                    pageIndex++;
                getBespeakRecordData(false);
            }
        });
    }


    /**
     * 搜索订单
     */
    private void getBespeakRecordData(boolean showDialog) {
        RequstClient.getBespeakRecordData(AppContext.userId, pageIndex, "1", new CustomResponseHandler(mContext, showDialog) {

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
                        orderListBean = new Gson().fromJson(content,
                                BespeakReturnBean.class);
                        if (null == orderListBean) {//点单列表为空
                            layout_empty.setVisibility(View.VISIBLE);
                            if (null != adapter) {
                                orderListBean.getDataList().clear();
                                adapter.setOrderList(orderListBean.getDataList());
                                adapter.notifyDataSetChanged();
                            }
                            return;
                        } else if ((null != orderListBean &&null == orderListBean.getDataList() )|| 0 == orderListBean.getDataList().size()) {//点单列表为空
                            layout_empty.setVisibility(View.VISIBLE);
                            if (null != adapter) {
                                orderListBean.getDataList().clear();
                                adapter.setOrderList(orderListBean.getDataList());
                                adapter.notifyDataSetChanged();
                            }
                            return;
                        }
                        adapter = new BespeakRecordAdapter(mContext, orderListBean.getDataList());
                        lvorder.setAdapter(adapter);
                        lvorder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                String status = orderListBean.getDataList().get(position).getSTATUS()+"";
                                    /*26	预约：审核成功27	预约：审核失败28	预约：取消*/
                                if ("26".equals(status) || "27".equals(status) || "28".equals(status)) {
                                    Intent intent = new Intent(mContext, CommonConfrimCancelDialog.class);
                                    intent.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_DELETE_ORDER);
                                    intent.putExtra(DELETE_ORDERID, orderListBean.getDataList().get(position).getID());
                                    mContext.startActivityForResult(intent, BespeakRecordFragment.ORDER_DELETE);
                                    return true;
                                }
                                return false;
                            }
                        });
                        if (adapter.getCount() < pageSize) {
                            lvorder.hideFooter();
                            lvorder.setPullLoadEnable(false);
                        } else {
                            lvorder.setPullLoadEnable(true);
                            lvorder.showFooter();
                        }
                    } else {
                        BespeakReturnBean bean = new Gson().fromJson(
                                content, BespeakReturnBean.class);
                        if (bean.getDataList() != null) {
                            if (bean.getDataList().size() > 0) {
                            	pageIndex++;
                                orderListBean.getDataList()
                                        .addAll(bean.getDataList());
                                adapter.notifyDataSetChanged();
                                if (bean.getDataList().size() >= 10) {
                                    lvorder.setPullLoadEnable(true);
                                } else {
                                    lvorder.setPullLoadEnable(false);
                                }
                            } else {
                                lvorder.setPullLoadEnable(false);
                                Toast.makeText(mContext, "已无更多数据！",
                                        Toast.LENGTH_LONG).show();
                                lvorder.hideFooter();
                            }
                        } else {
                            lvorder.setPullLoadEnable(false);
                            Toast.makeText(mContext, "已无更多数据！",
                                    Toast.LENGTH_LONG).show();
                            lvorder.hideFooter();
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
	

}
