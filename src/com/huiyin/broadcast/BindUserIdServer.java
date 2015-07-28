package com.huiyin.broadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BaseBean;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.NetworkUtils;

import org.apache.http.Header;

/**
 * Created by justme on 15/6/12.
 * 用于更新百度推送的ID和用户ID
 */
public class BindUserIdServer extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (NetworkUtils.isNetworkAvailable(BindUserIdServer.this)) {
            baiduPushInfo(intent);
        } else {
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void baiduPushInfo(Intent intent) {
        if (intent == null) {
            stopSelf();
            return;
        }
        String channelId = intent.getStringExtra("channelId");
        String bdUserId = intent.getStringExtra("bdUserId");
        String userId = AppContext.getInstance().userId;
        if (TextUtils.isEmpty(userId)) {
            stopSelf();
            return;
        }
        MyCustomResponseHandler mHandler = new MyCustomResponseHandler(BindUserIdServer.this, false) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(content, BaseBean.class);
                if (bean != null && bean.type > 0) {

                }
                LogUtil.d("BindUserIdServer", "content:" + content);
                stopSelf();
            }
        };
        RequstClient.baiduPushInfo(mHandler, channelId, bdUserId, userId);
    }
}
