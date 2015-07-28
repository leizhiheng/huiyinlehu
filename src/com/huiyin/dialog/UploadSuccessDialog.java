package com.huiyin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.huiyin.R;

public class UploadSuccessDialog extends Dialog{

	private Context mContext;
	private ImageView mIvDismiss;
	public UploadSuccessDialog(Context context) {
		super(context, R.style.dialog);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(true);
		setContentView(R.layout.dialog_upload_success);
		mIvDismiss = (ImageView) findViewById(R.id.iv_dismiss);
	}

	public void setClickListener(DialogClickListener clickListener) {
		exitClickListener = clickListener;
	}


	private DialogClickListener exitClickListener;

	public interface DialogClickListener {
		void onConfirmClickListener();
	}

	public void setOnClickListener(android.view.View.OnClickListener listener){
		mIvDismiss.setOnClickListener(listener);
	}
}

