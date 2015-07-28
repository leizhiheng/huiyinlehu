package com.huiyin.base;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.huiyin.AppContext;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow.IOnItemClick;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.JSONParseUtils;

/**
 * Fragment基类
 * @author 刘远祺
 *
 */
public class BaseFragment extends Fragment {
    public Object tag;
	
    private final static String TAG = BaseFragment.class.getSimpleName();
    
    /**上下文**/
	public FragmentActivity mContext;
    protected View layoutView;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.mContext = getActivity();
        findView();
    }

    protected View findViewById(int Id){
        try{
            return layoutView.findViewById(Id);
        }catch (Exception e){
            return null;
        }
    }

    public void setFragmentTag(Object tag) {
        this.tag = tag;
    }

    public Object getFragmentTag(){
        return tag;
    }

    public void findView(){}
	
	
	/**
	 * 启动Activity
	 * @param toActivity
	 */
	public void startActivity(Class toActivity){
		Intent intent = new Intent(mContext, toActivity);
		mContext.startActivity(intent);
	}
	
	/**
	 * 启动Activity
	 * @param toActivity
	 */
	public void startActivity(Intent intent){
		mContext.startActivity(intent);
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
	 * 判断用户是否登录
	 * @return
	 */
	public boolean appIsLogin(){
		String userId = AppContext.userId;
		if (!TextUtils.isEmpty(userId) && null != AppContext.mUserInfo) {
			return true;
		}
		return false;
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
	
	/**
	 * 获取textview Tag
	 * @param tv
	 * @return
	 */
	protected String getTag(TextView tv){
		try{
			return tv.getTag().toString().trim();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 显示toast
	 * @param msg
	 */
	protected void showMyToast(String msg){
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 判断文本值是否为空
	 * @param tv
	 * @return
	 */
	protected boolean isNullText(TextView tv){
		if(null == tv){
			return true;
		}
		return TextUtils.isEmpty(tv.getText().toString().trim());
	}
	
	/**
	 * 判断文本值是否为空
	 * @param tv
	 * @return
	 */
	protected boolean isNullTag(TextView tv){
		if(null == tv){
			return true;
		}
		return TextUtils.isEmpty(tv.getTag().toString().trim());
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
		Intent i = new Intent(mContext, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		mContext.finish();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		/**
		 * 百度统计
         * 页面起始（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onResume(this);
	}
	
	@Override
	public void onPause() {
        super.onPause();
        /**
         * 百度统计
         * 页面结束（每个Activity中都需要添加，如果有继承的父Activity中已经添加了该调用，那么子Activity中务必不能添加）
         * 不能与StatService.onPageStart一级onPageEnd函数交叉使用
         */
        StatService.onPause(this);
    }
}
