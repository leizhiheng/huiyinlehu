package com.huiyin.ui.classic;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.huiyin.R;

public class SelectReportTypeAndTitlePopwindow extends PopupWindow {

	private Button btn_cancel;
	public ListView mListView;
	private View mMenuView;

	interface OnTouchPopwindowOutListener{
		void onTouchOut();
	}
	private static  OnTouchPopwindowOutListener mTouchOutListener;
	public void setOnTouchPopwindowOutListener(OnTouchPopwindowOutListener listener){
		mTouchOutListener = listener;
	}
	
	public SelectReportTypeAndTitlePopwindow(Activity context, OnItemClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.alert_dialog_report_type, null);
		mListView = (ListView) mMenuView.findViewById(R.id.listview_report_type);
		mListView.setOnItemClickListener(itemsOnClick);
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		// 取消按钮
//		btn_cancel.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// 销毁弹出框
//				dismiss();
//			}
//		});
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		this.setOutsideTouchable(true);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//		 mMenuView.setOnTouchListener(new OnTouchListener() {
//		
//			 public boolean onTouch(View v, MotionEvent event) {
//				
//				 int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//				 int y = (int) event.getY();
//				 if (event.getAction() == MotionEvent.ACTION_UP) {
//					 if (y < height) {
//						 mTouchOutListener.onTouchOut();
//						 dismiss();
//					 }
//				 }
//				 return true;
//			 }
//
//		 });
		this.update();
	}

	
	public SelectReportTypeAndTitlePopwindow(Context context, OnItemClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.alert_dialog, null);
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		// 取消按钮
//		btn_cancel.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// 销毁弹出框
//				dismiss();
//			}
//		});
		// 设置SelectPicPopupWindow的View
		setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		setAnimationStyle(R.style.PopupAnimation);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		setBackgroundDrawable(dw);
		setOutsideTouchable(true);
		update();
	}
	
	public ListView getListView(){
		return mListView;
	}
	
	public void setAdapter(BaseAdapter adapter){
		mListView.setAdapter(adapter);
	}
	
}