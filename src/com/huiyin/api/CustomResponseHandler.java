package com.huiyin.api;

import android.content.Context;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.http.AsyncHttpResponseHandler;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.Tip;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class CustomResponseHandler extends AsyncHttpResponseHandler {
    public static final String NETWORK_NOT_GOOD = "7770";
    public static final String SERVICE_TIMEOUT = "7771";
    public boolean isShowLoading = true;
    private Context mContext;

    public CustomResponseHandler(Context context) {
        this.mContext = context;
    }

    public CustomResponseHandler(Context context, boolean isShowLoading) {
        this.isShowLoading = isShowLoading;
        this.mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if (isShowLoading) {
                Tip.showLoadDialog(mContext,
                        mContext.getString(R.string.loading));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        Tip.colesLoadDialog();
    }

    @Override
    public void onFailure(Throwable error, String content) {
        super.onFailure(error, content);
        if (error instanceof HttpException) {
            onFailure("", mContext.getString(R.string.networkFailure));// 网络异常
        } else if (error instanceof SocketTimeoutException) {
            onFailure("", mContext.getString(R.string.responseTimeout)); // 响应超时
        } else if (error instanceof ConnectTimeoutException) {
            onFailure("", mContext.getString(R.string.requestTimeout));// 请求超时
        } else if (error instanceof IOException) {
            onFailure("", mContext.getString(R.string.networkError)); // 网络异常
        } else {
            onFailure("", content); // 无法连接网络
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String content) {
        super.onSuccess(statusCode, headers, content);
        LogUtil.d("RequstClient", content);
        try {
            switch (statusCode) {
                case 200:
                    onRefreshData(content);
                    break;
                case 401:
                    // onFailure("401", mActivity.getString(R.string.error_401));
                    break;
                case 403:
                    // onFailure("404", mActivity.getString(R.string.error_404));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Tip.showTips(mContext, R.drawable.tips_toast_error, "网络不给力！");
        }
    }

    /**
     * 出错
     *
     * @param error
     * @param errorMessage
     */
    public void onFailure(String error, String errorMessage) {
        try {
            if (isShowLoading) {
                Tip.colesLoadDialog();
            }
        } catch (Exception e) {
        }
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
    }

    public void onRefreshData(String content) {
    }

}
