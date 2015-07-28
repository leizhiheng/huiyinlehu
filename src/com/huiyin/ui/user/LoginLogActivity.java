package com.huiyin.ui.user;

import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.LoginLogListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.LoginLogBean;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.XListView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录日志
 * Created by lian on 2015/6/2.
 */
public class LoginLogActivity extends BaseActivity {
    private static final String TAG = "LoginLogActivity";
    private int pageIndex = 1;//当前页
    private XListView lvLoginLog;
    private LoginLogListAdapter adapter;
    private int pageSize=10;//每页大小
    private LoginLogBean loginLogListBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login_log);
        init();
        loginLog();
    }

    private void init() {
        this.lvLoginLog = (XListView) findViewById(R.id.lv_complaint);
        lvLoginLog.setPullLoadEnable(false);
        lvLoginLog.setFooterDividersEnabled(false);
        lvLoginLog.setPullRefreshEnable(true);
        lvLoginLog.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                loginLog();
            }

            @Override
            public void onLoadMore() {
                pageIndex = adapter.getCount() / pageSize + 1;
                if (adapter.getCount() % pageSize > 0)
                    pageIndex++;
                loginLog();
            }
        });
    }

    /**
     * 获取我的登录日志
     */
    public void loginLog() {
        RequstClient.loginLog(AppContext.userId, pageIndex, new CustomResponseHandler(this, true) {
            @Override
            public void onStart() {
                super.onStart();
                lvLoginLog.stopLoadMore();
                lvLoginLog.stopRefresh();
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
                        loginLogListBean = new Gson().fromJson(content,
                                LoginLogBean.class);
                        if (null != loginLogListBean && null != loginLogListBean.getHistoryList()&& 0 == loginLogListBean.getHistoryList().size()) {
                            Toast.makeText(mContext, "暂无数据", Toast.LENGTH_LONG).show();
                            return;
                        }
                        adapter = new LoginLogListAdapter(mContext,
                                loginLogListBean);
                        lvLoginLog.setAdapter(adapter);
                        if (adapter.getCount() < pageSize) {
                            lvLoginLog.hideFooter();
                            lvLoginLog.setPullLoadEnable(false);
                        } else {
                            lvLoginLog.setPullLoadEnable(true);
                            lvLoginLog.showFooter();
                        }
                    } else {
                        LoginLogBean bean = new Gson().fromJson(
                                content, LoginLogBean.class);
                        if (bean.getHistoryList() != null) {
                            if (bean.getHistoryList().size() > 0) {
                                loginLogListBean.getHistoryList()
                                        .addAll(bean.getHistoryList());
                                adapter.notifyDataSetChanged();
                                if (bean.getHistoryList().size() >= 10) {
                                    lvLoginLog.setPullLoadEnable(true);
                                } else {
                                    lvLoginLog.setPullLoadEnable(false);
                                }
                            } else {
                                lvLoginLog.setPullLoadEnable(false);
                                Toast.makeText(mContext, "已无更多数据！",
                                        Toast.LENGTH_LONG).show();
                                lvLoginLog.hideFooter();
                            }
                        } else {
                            lvLoginLog.setPullLoadEnable(false);
                            Toast.makeText(mContext, "已无更多数据！",
                                    Toast.LENGTH_LONG).show();
                            lvLoginLog.hideFooter();
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
