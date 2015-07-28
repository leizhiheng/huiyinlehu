package com.huiyin.ui.housekeeper;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.adapter.ApplyReasonAdapter;

/**
 * 申请理由popwindow
 * @author 刘远祺
 *
 */
public class SelectApplyReasonWindow extends PopupWindow implements OnItemClickListener {

	private Activity context;
	
	private ListView listView;
	private ApplyReasonAdapter adapter;
	
	//click事件回传
	private IOnItemClick itemsOnClick;
	
	private View mMenuView = null;
	
	
	public SelectApplyReasonWindow(Activity context, Map<String, String> dataMap, IOnItemClick itemsOnClick) {
		this(context, dataMap, null, itemsOnClick);
	}
	
	public SelectApplyReasonWindow(Activity context, Map<String, String> dataMap, String checkedkey, IOnItemClick itemsOnClick) {
		this(context, dataMap, checkedkey, true, itemsOnClick);
	}
	
	public SelectApplyReasonWindow(Activity context, Map<String, String> dataMap,  String checkedKey, boolean showBottomTip, IOnItemClick itemsOnClick) {
		this(context, dataMap, checkedKey, showBottomTip, null, itemsOnClick);
	}
	
	public SelectApplyReasonWindow(Activity context, Map<String, String> dataMap,  String checkedKey, boolean showBottomTip, String bottomTip, IOnItemClick itemsOnClick) {
		super(context);
		
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.alert_apply_reason, null);
		
		adapter = new ApplyReasonAdapter(context, dataMap, checkedKey);
		listView = (ListView) mMenuView.findViewById(R.id.lv_reason);
		listView.setAdapter(adapter);
		listView.requestLayout();
		
		listView.setOnItemClickListener(this);
		this.itemsOnClick = itemsOnClick;
		
		
		TextView tip = (TextView)mMenuView.findViewById(R.id.show_tip_textview);
//		View contentView  = mMenuView.findViewById(R.id.list_content_view);
//		contentView.requestLayout();
//		int listViewHeight = listView.getMeasuredHeight();
//		int contentHeight = contentView.getMeasuredHeight();
//		if(listViewHeight < contentHeight){
//			contentHeight = listViewHeight;
//			
//			int tipHeight = tip.getMeasuredHeight();
//			contentView.getLayoutParams().height = (contentHeight + tipHeight);
//		}
//		
		//隐藏底部提示语
		if(!showBottomTip){
			tip.setVisibility(View.GONE);
		}else{
			if(!TextUtils.isEmpty(bottomTip)){
				tip.setText(bottomTip);
			}
		}
		
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
		ColorDrawable dw = new ColorDrawable(Color.WHITE);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		this.setOutsideTouchable(true);
		this.update();
	}

	
	/**
	 * 该方法要在popwindow show之后才能调用
	 */
	public void resetHeight(){
		
		mMenuView.measure(0, 0);
		TextView tip = (TextView)mMenuView.findViewById(R.id.show_tip_textview);
		View contentView  = mMenuView.findViewById(R.id.list_content_view);
		contentView.requestLayout();
		int listViewHeight = listView.getMeasuredHeight();
		int contentHeight = contentView.getMeasuredHeight();
		if(listViewHeight < contentHeight){
			contentHeight = listViewHeight;
			
			tip.measure(0, 0);
			int tipHeight = tip.getMeasuredHeight();
			contentView.getLayoutParams().height = (contentHeight + tipHeight);
		}
	}
	
	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		
		resetHeight();
		
		backgroundAlpha(0.7f);
		setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				backgroundAlpha(1f);
			}
		});
	}
	
	
	
	 /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity)context).getWindow().setAttributes(lp);
    }
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		String key = adapter.getItem(arg2).toString().trim() ;
		String value = adapter.getValue(key);
		if(null != itemsOnClick){
			
			itemsOnClick.onItemClick(key, value);
		}
		dismiss();
	}
	
	public interface IOnItemClick{
		
		public void onItemClick(String id, String value);
		
	}
}
