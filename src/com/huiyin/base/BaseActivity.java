package com.huiyin.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.UserInfo;
import com.huiyin.api.URLs;
import com.huiyin.bean.LoginInfo;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow.IOnItemClick;
import com.huiyin.ui.user.ExitSuccessActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.ui.user.SettingActivity;
import com.huiyin.utils.AppManager;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.MyText;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class BaseActivity extends FragmentActivity {
    private final static String TAG = "BaseActivity";
	public Activity mContext;
    public boolean isDestory=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
		//添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);
		mContext = this;
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		//将所有界面的右上角三条线去掉(强哥要求)
		setRightText(View.GONE);
		
		onFindViews();
        onSetListener();
        onLoadData();
        
        baseInit();
	}
	
	/**
	 * 基类初始化
	 */
	private void baseInit(){
		
		//返回事件
		View backView = findViewById(R.id.left_ib);
		if(null == backView){
			backView = findViewById(R.id.btn_back);
		}
		
		if(null == backView){
			backView = findViewById(R.id.ab_back);
		}
		
		
		if(null != backView){
			backView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});
		}
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		//tv_title
		View view = findViewById(R.id.tv_title);
		if(null == view){
			view = findViewById(R.id.middle_title_tv);
		}
		if(null == view){
			view = findViewById(R.id.com_tip_tv);
		}
		if(null == view){
			view = findViewById(R.id.ab_title);
		}
		

		try{
			TextView titleTv = (TextView)view;
			titleTv.setText(title);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(int title){
		try{
			TextView titleTv = (TextView)findViewById(R.id.middle_title_tv);
            if (titleTv!=null) {
                titleTv.setText(title);
            }
        }catch(Exception e){
			e.printStackTrace();
		}
	}
    /**
     * 设置右上角的按钮
     * @param visible GONE或者VISIBLE
     * @param title 标题
     * @param bgRes 背景图片
     */
    public void setRightText(int visible,String title,int bgRes,OnClickListener clickListener){
        try{
            MyText titleRight = (MyText)findViewById(R.id.right_ib);
           if (titleRight!=null){
               titleRight.setText(title);
               titleRight.setBackgroundResource(bgRes);
               titleRight.setVisibility(visible);
				if (clickListener != null)
					titleRight.setOnClickListener(clickListener);
           }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 设置右上角的按钮
     * @param visible GONE或者VISIBLE
     * @param title 标题
     */
    public void setRightText(int visible, String title,OnClickListener clickListener) {
        setRightText(visible, title, R.drawable.trans, clickListener);
    }

	/**
	 * 设置右上角的按钮
	 *
	 * @param visible
	 *            GONE或者VISIBLE
	 * @param title
	 *            标题
	 */
    public void setRightText(int visible,String title){
        setRightText(visible,title,R.drawable.trans,null);
    }
    /**
     * 设置右上角的按钮
     * @param visible GONE或者VISIBLE
     */
    public void setRightText(int visible){
        setRightText(visible,"",R.drawable.trans,null);
    }
	/**
	 * 控件初始化 
	 */
	protected void onFindViews(){
	    LogUtil.i(TAG, "onFindViews()---->");
	}
	
	/**
	 * 设置控件事件
	 */
	protected void onSetListener(){
	    LogUtil.i(TAG, "onSetListener()---->");
	}
	
	protected void onLoadData(){
		LogUtil.i(TAG, "onLoadData()---->");
    }
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        // 点击空白处，隐藏软键盘
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null
                    && getCurrentFocus().getWindowToken() != null) {
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		 try{
             isDestory=true;
			 UIHelper.cloesLoadDialog();
	        }catch (Exception e) {
	            System.out.println("myDialog取消，失败！");
	        }
		super.onDestroy();
	}
	
	/**
	 * 显示toast
	 * @param msg
	 */
	protected void showMyToast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 获取textview文本 
	 * @param tv
	 * @return
	 */
	protected String getText(TextView tv){
		try{
			return tv.getText().toString().trim();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	protected String getTag(View view){
		try{
			return view.getTag().toString().trim();
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 监听originTextView文本值变化，有值showTextView显示，否则不显示
	 * @param originTextView
	 * @param showTextView
	 */
	protected void setEmptyListener(TextView originTextView,
			final TextView showTextView) {
		if (null == originTextView || null == showTextView) {
			return;
		}
		originTextView.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			public void afterTextChanged(Editable arg0) {
				String text = arg0.toString().trim();
				int vis = TextUtils.isEmpty(text) ? View.VISIBLE : View.GONE;
				showTextView.setVisibility(vis);
			}
		});
		
	}


    /**
     * 监听originTextView文本值变化，有值showTextView显示相对应的字数值
     * @param originTextView
     * @param showTextView
     * @param maxLength 最大字数
     */
    protected void setTextChangeListener(TextView originTextView,  final TextView showTextView,final int maxLength) {
        if (null == originTextView || null == showTextView) {
            return;
        }
        originTextView.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            public void afterTextChanged(Editable arg0) {
                String text = arg0.toString().trim();
                int textLength=text.length();
                String showText=textLength+"/"+maxLength;
                showTextView.setText(showText);
            }
        });

    }
	
	/**
	 * 获取String值
	 * @param key
	 * @return
	 */
	protected String getStringExtra(String key){
		return getStringExtra(key, "");
	}
	
	/**
	 * 获取String值
	 * @param key
	 * @return
	 */
	protected String getStringExtra(String key, String defaultValue){
		String value = defaultValue;
		try{
			Intent intent = getIntent();
			if(null != intent && intent.hasExtra(key)){
				value = intent.getStringExtra(key);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 获取int值
	 * @param key
	 * @return
	 */
	protected int getIntExtra(String key){
		return getIntExtra(key, 0);
	}
	
	/**
	 * 获取int值
	 * @param key
	 * @return
	 */
	protected int getIntExtra(String key, int defaultValue){
		int value = defaultValue;
		try{
			Intent intent = getIntent();
			if(null != intent){
				value = intent.getIntExtra(key, defaultValue);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
	}
	
	/**
	 * 启动到图片预览界面
	 * @param imgUrlList
	 * @param showIndex
	 */
	public void startPhotoViewActivity(ArrayList<String> imgUrlList, int showIndex){
		Intent intent = new Intent();
		intent.setClass(mContext,PhotoViewActivity.class);
		intent.putStringArrayListExtra(PhotoViewActivity.INTENT_KEY_PHOTO, imgUrlList);
		intent.putExtra(PhotoViewActivity.INTENT_KEY_POSITION, showIndex);
		startActivity(intent);
	}

	/**
	 * 显示申请理由
	 * @param view
	 * @param dataMap
	 */
	public void showReasonPopwindow(View view, Map<String, String> dataMap, IOnItemClick onItenClick){
        showReasonPopwindow(view, dataMap, null, onItenClick);
	}

    /**
     * 显示申请理由
     * @param view
     * @param dataMap
     */
    public void showReasonPopwindow(View view, Map<String, String> dataMap, String checkedKey, IOnItemClick onItenClick){
    	showReasonPopwindow(view, dataMap, null, false, onItenClick);
    }
    
    /**
     * 显示申请理由
     * @param view
     * @param dataMap
     */
    public void showReasonPopwindow(View view, Map<String, String> dataMap, String checkedKey, boolean showBottomTip, IOnItemClick onItenClick){
        SelectApplyReasonWindow pop = new SelectApplyReasonWindow(mContext, dataMap, checkedKey, showBottomTip, onItenClick);

        if (!pop.isShowing()) {
            pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            
            int height = pop.getHeight();
            int screenHeight = DeviceUtils.getHeightMaxPx(mContext);
            int maxHeight = ((screenHeight/3)*2);
            if(height > maxHeight){
            	height = maxHeight;
            	pop.setHeight(height);
            }
        }
    }
    
    /**
     * 显示申请理由
     * @param view
     * @param dataMap
     */
    public void showReasonPopwindow(View view, Map<String, String> dataMap, String checkedKey, boolean showBottomTip, String tip, IOnItemClick onItenClick){
        SelectApplyReasonWindow pop = new SelectApplyReasonWindow(mContext, dataMap, checkedKey, showBottomTip, tip, onItenClick);

        if (!pop.isShowing()) {
            pop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            
            int height = pop.getHeight();
            int screenHeight = DeviceUtils.getHeightMaxPx(mContext);
            int maxHeight = ((screenHeight/3)*2);
            if(height > maxHeight){
            	height = maxHeight;
            	pop.setHeight(height);
            }
        }
    }
	
	/**
	 * 根据List<String>获得 "str1,str2,str3..."
	 * @param netimageurls
	 * @return
	 */
	public String getImgs(List<String> netimageurls){
		if(null == netimageurls || netimageurls.size() == 0){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<netimageurls.size(); i++){
			if(i==0){
				sb.append(netimageurls.get(i));
			}else{
				sb.append(","+netimageurls.get(i));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 判断App用户是否已经登录
	 * @return
	 */
	public boolean appIsLogin(boolean startToLoginActivity) {
		String userId = AppContext.getInstance().userId;
		if (TextUtils.isEmpty(userId) || null == AppContext.mUserInfo) {
			Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
			
			if(startToLoginActivity){
				mContext.startActivity(new Intent(mContext, LoginActivity.class));
			}
			return false;
		}
		return true;
	}

	
	/**
	 * 加载用户头像
	 * @param account_head
	 */
	public void loadUserHead(ImageView account_head){
		UserInfo mUserInfo = AppContext.mUserInfo;
		if (null != mUserInfo && mUserInfo.img != null) {
			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
					.showStubImage(R.drawable.default_head).showImageForEmptyUri(R.drawable.default_head)
					.showImageOnFail(R.drawable.default_head).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
					.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(100)).build();
			ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + mUserInfo.img, account_head, options);

		} else {
			account_head.setImageDrawable(getResources().getDrawable(R.drawable.default_head));
		}
	}
	
	/**
	 * 清空用户信息
	 */
	public void clearUserInfo(){
		// 退出登录
        LoginInfo mLoginInfo = new LoginInfo();
        mLoginInfo.psw = "";
        mLoginInfo.userName = "";
        mLoginInfo.isChecked = false;
        AppContext.mUserInfo = null;
        AppContext.userId = null;
        AppContext.saveLoginInfo(getApplicationContext(), mLoginInfo);

        //清空登录记住的信息
        UserInfo.clearUserInfo(mContext);

		LogUtil.i("UserInfo", "MainActivity OnCreate is Null" + (null == AppContext.userId));
	}
	
	/**
	 * 跳转到退出成功的界面
	 */
	public void startToExistSuccessActivity(){
		
		//清空用户信息
		clearUserInfo();
        
        Intent intent = new Intent(mContext, ExitSuccessActivity.class);
        startActivity(intent);
        finish();
	}
	
	/**
	 * 跳转到主界面，并自动切换我的乐虎选项卡
	 * @param mainTask</br>
	 * 	AppContext.FIRST_PAGE 	= "0" 首页</br>
		AppContext.CLASSIC 		= "1" 分类</br>
		AppContext.SHOPCAR 		= "2" 购物车</br>
		AppContext.HOUSEKEEPER 	= "3" 管家</br>
		AppContext.LEHU 		= "4" 我的乐虎
	 */
	public void startToMainActivity(String mainTask){

        //自动跳转到我的乐虎界面
        AppContext.MAIN_TASK = mainTask;
		Intent i = new Intent(this, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();
	}
	
	/**
	 * 判断是否为一次正常的http请求结果
	 * @param jsonResult
	 * @return
	 */
	public boolean isSuccessResponse(String jsonResult){
		int type = JSONParseUtils.getInt(jsonResult, "type");
		if(1 != type){
			String errorMsg = JSONParseUtils.getString(jsonResult, "msg");
			if(!TextUtils.isEmpty(errorMsg)){
				showMyToast(errorMsg);
			}
			return false;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * 百度统计
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onResume(this);
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        /**
         * 百度统计
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onPause(this);
    }
}
