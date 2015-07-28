package com.huiyin.utils;

import android.app.Activity;
import cn.bidaround.youtui_template.YouTuiViewType;
import cn.bidaround.youtui_template.YtTemplate;
import cn.bidaround.ytcore.YtShareListener;
import cn.bidaround.ytcore.data.ShareData;
import cn.bidaround.ytcore.data.YtPlatform;
import cn.bidaround.ytcore.util.HttpUtils;
import cn.bidaround.ytcore.util.YtToast;

import com.huiyin.api.URLs;

/**
 * Created by justme on 15/5/18.
 */
public class ShareUtils {

	/**
	 * 分享数据
	 */
	private static ShareData shareData;
	
	
	/**
	 * 显示分享
	 * @param activity		上下文
	 * @param title			分享标题
	 * @param url			分享跳转地址
	 * @param imageUrl		分享显示的图片地址
	 * @param text			分享内容
	 */
    public static void showShare(Activity activity, String title, String url, String imageUrl, String text) {
        
    	imageUrl = imageUrl.trim();
    	if(StringUtils.isEmpty(imageUrl)){
    		
    		//图片为空显示默认图片
    		imageUrl = "http://www.lehumall.com/upload/image/admin/2015/20150416/201504160943242890.png";
    		
    	}else{
    		
    		//正常的图片
    		imageUrl = URLs.IMAGE_URL+imageUrl;
    	}
    	
    	// 友推分享平台初始化
    	YtTemplate.init(activity);
    	
    	// 开启集成检测机制，请在检测完成后关闭，默认不检测
    	// YtTemplate.checkConfig(true);
    	
    	//分享的数据对象
    	shareData = new ShareData();
		shareData.setAppShare(false); 				// 是否为应用分享，如果为true，分享的数据需在友推后台设置
		shareData.setDescription("汇银商城");			// 待分享内容的描述
		shareData.setTitle(title); 					// 待分享的标题
		shareData.setText(text + url);				// 待分享的文字
		shareData.setImage(ShareData.IMAGETYPE_INTERNET, imageUrl);// 设置网络分享地址
		shareData.setPublishTime(DateUtil.getStringDateShort());
		shareData.setTargetId(String.valueOf(100));
		shareData.setTargetUrl(url);				// 待分享内容的跳转链接
		
    	//初始化分享界面
		YtTemplate template = new YtTemplate(activity, YouTuiViewType.WHITE_GRID, true);
		template.setShareData(shareData);
		template.addListeners(getListener(activity, shareData));
		template.setPopwindowHeight(600);			//**设置分享窗口高度*/
		template.setScreencapVisible(false);		//**设置右上角显示截屏涂鸦功能*/
		
		//显示分享界面
		template.setTemplateType(YouTuiViewType.WHITE_GRID);
		template.setHasAct(true);
		template.show();
    }
    
    /**
     * 获取分享回调监听
     * @param activity
     * @param shareData
     * @return
     */
    private static YtShareListener getListener(final Activity activity, final ShareData shareData){
    	/**
    	 * 设置分享监听器
    	 */
    	return new YtShareListener() {

    		/** 分享成功后回调方法 */
    		@Override
    		public void onSuccess(YtPlatform platform, String result) {
    			//YtToast.showS(activity, "onSuccess");

    			/** 清理缓存 */
    			HttpUtils.deleteImage(shareData.getImagePath());
    		}

    		/** 分享之前调用方法 */
    		@Override
    		public void onPreShare(YtPlatform platform) {
    			//YtToast.showS(activity, "onPreShare");
    		}

    		/** 分享失败回调方法 */
    		@Override
    		public void onError(YtPlatform platform, String error) {
    			//YtToast.showS(activity, "onError");
    			//YtToast.showS(activity, error);
    		}

    		/** 分享取消回调方法 */
    		@Override
    		public void onCancel(YtPlatform platform) {
    			//YtToast.showS(activity, "onCancel");
    			//Log.w("YouTui", platform.getName());

    			/** 清理缓存 */
    			HttpUtils.deleteImage(shareData.getImagePath());
    		}
    	};

    }
    
    /**
     * 在Activity的 super.onDestroy();之前调用
     */
    public static void onActivityDestroyDo(Activity activity){
    	// 释放资源
    	YtTemplate.release(activity);
    	
    	//清理缓存
    	if(null != shareData){
    		if(null != shareData.getImagePath()){
    			HttpUtils.deleteImage(shareData.getImagePath());
    		}
    	}
    }
    
}
