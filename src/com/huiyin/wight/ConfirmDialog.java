package com.huiyin.wight;

/**
 * Created by Mike on 2015/6/16.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.huiyin.R;

public class ConfirmDialog extends Dialog {

	private Context context;
	private String title;
	private String confirmButtonText;
	private String cacelButtonText;
	private ClickListenerInterface clickListenerInterface;

	public interface ClickListenerInterface {

		public void doConfirm();

		public void doCancel();
	}

	public ConfirmDialog(Context context, String title,
			String confirmButtonText, String cacelButtonText,ClickListenerInterface listener) {
		super(context, R.style.MyDialogStyle);
		this.context = context;
		this.title = title;
		this.confirmButtonText = confirmButtonText;
		this.cacelButtonText = cacelButtonText;
        setClicklistener(listener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.goods_shopping_car_dialog_layout, null);
		setContentView(view);

		TextView tvTitle = (TextView) view.findViewById(R.id.com_tip_tv);
		TextView tvConfirm = (TextView) view.findViewById(R.id.com_ok_btn);
		TextView tvCancel = (TextView) view.findViewById(R.id.com_cancel_btn);

		tvTitle.setText(title);
		tvConfirm.setText(confirmButtonText);
		tvCancel.setText(cacelButtonText);

		tvConfirm.setOnClickListener(new clickListener());
		tvCancel.setOnClickListener(new clickListener());

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
		lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
		dialogWindow.setAttributes(lp);
	}

	public void setClicklistener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}

	private class clickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
            switch (v.getId()) {
			case R.id.com_ok_btn:
				clickListenerInterface.doConfirm();
				break;
			case R.id.com_cancel_btn:
				clickListenerInterface.doCancel();
				break;
			}
		}

	};

}