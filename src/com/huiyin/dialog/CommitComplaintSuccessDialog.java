package com.huiyin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.huiyin.R;
import com.huiyin.utils.DeviceUtils;

/**
 * 投诉结果
 * Created by kuangyong on 2015/6/9.
 */
public class CommitComplaintSuccessDialog extends Dialog{

    private Context mContext;
    private ImageView mIvDismiss;
    public CommitComplaintSuccessDialog(Context context) {
        super(context, R.style.dialog);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_commit_complaint_success);
        mIvDismiss = (ImageView) findViewById(R.id.iv_dismiss);
        int realWith=(int)(DeviceUtils.getWidthMaxPx((Activity)mContext)*0.8);
        getWindow().getAttributes().width=realWith;
        mIvDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
