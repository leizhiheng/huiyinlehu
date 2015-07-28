package com.huiyin.ui.user.complaint;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.MyComplaintListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ComplaintListBean;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.XListView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的投诉
 * Created by lian on 2015/6/2.
 */
public class MyComplaintActivity extends BaseActivity {
    private static final String TAG = "MyComplaintActivity";
    private int pageIndex = 1;//当前页
    private com.huiyin.wight.XListView lvcomplaint;
    private MyComplaintListAdapter adapter;
    private int pageSize=10;//每页大小
    private ComplaintListBean orderListBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mycomplaint);
        init();
        getMyComplaint();
    }

    private void init() {
        this.lvcomplaint = (XListView) findViewById(R.id.lv_complaint);
        lvcomplaint.setPullLoadEnable(false);
        lvcomplaint.setFooterDividersEnabled(false);
        lvcomplaint.setPullRefreshEnable(true);
        lvcomplaint.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                getMyComplaint();
            }

            @Override
            public void onLoadMore() {
                pageIndex = adapter.getCount() / pageSize + 1;
                if (adapter.getCount() % pageSize > 0)
                    pageIndex++;
                getMyComplaint();
            }
        });
    }

    /**
     * 获取我的投诉列表
     */
    public void getMyComplaint() {
        RequstClient.complainData(AppContext.userId, pageIndex, new CustomResponseHandler(this, true) {
            @Override
            public void onStart() {
                super.onStart();
                lvcomplaint.stopLoadMore();
                lvcomplaint.stopRefresh();
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
                        Toast.makeText(getBaseContext(), errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (pageIndex == 1) {
                        orderListBean = new Gson().fromJson(content,
                                ComplaintListBean.class);
                        if (null == orderListBean) {//点单列表为空
                            Toast.makeText(mContext, "暂无数据", Toast.LENGTH_LONG).show();
                            if (null != adapter) {
                                orderListBean.getComplainData().clear();
                                adapter.setOrderList(orderListBean.getComplainData());
                                adapter.notifyDataSetChanged();
                            }
                            return;
                        } else if (null != orderListBean && null != orderListBean.getComplainData() && 0 == orderListBean.getComplainData().size()) {//点单列表为空
                            Toast.makeText(mContext, "暂无数据", Toast.LENGTH_LONG).show();
                            if (null != adapter) {
                                orderListBean.getComplainData().clear();
                                adapter.setOrderList(orderListBean.getComplainData());
                                adapter.notifyDataSetChanged();
                            }
                            return;
                        }
                        adapter = new MyComplaintListAdapter(mContext,
                                orderListBean.getComplainData());
                        lvcomplaint.setAdapter(adapter);
                        if (adapter.getCount() < pageSize) {
                            lvcomplaint.hideFooter();
                            lvcomplaint.setPullLoadEnable(false);
                        } else {
                            lvcomplaint.setPullLoadEnable(true);
                            lvcomplaint.showFooter();
                        }
                    } else {
                        ComplaintListBean bean = new Gson().fromJson(
                                content, ComplaintListBean.class);
                        if (bean.getComplainData() != null) {
                            if (bean.getComplainData().size() > 0) {
                                orderListBean.getComplainData()
                                        .addAll(bean.getComplainData());
                                adapter.notifyDataSetChanged();
                                if (bean.getComplainData().size() >= 10) {
                                    lvcomplaint.setPullLoadEnable(true);
                                } else {
                                    lvcomplaint.setPullLoadEnable(false);
                                }
                            } else {
                                lvcomplaint.setPullLoadEnable(false);
                                Toast.makeText(mContext, "已无更多数据！",
                                        Toast.LENGTH_LONG).show();
                                lvcomplaint.hideFooter();
                            }
                        } else {
                            lvcomplaint.setPullLoadEnable(false);
                            Toast.makeText(mContext, "已无更多数据！",
                                    Toast.LENGTH_LONG).show();
                            lvcomplaint.hideFooter();
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
