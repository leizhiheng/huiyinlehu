package com.huiyin.utils;

import java.util.Random;
import java.util.UUID;

import com.huiyin.AppContext;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
/**
 * 设备相关信息的获取
 * 
 * @author gongyj.kai7
 *
 */
public class DeviceUtils{
	
	public static void printDeviceInfo(Activity activity){
		String release = android.os.Build.VERSION.RELEASE;
		int sdk_int = android.os.Build.VERSION.SDK_INT;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int wMaxDP = (160*dm.widthPixels/dm.densityDpi);
		StringBuffer sb = new StringBuffer();
		sb.append("release:"+release+"\n");
		sb.append("sdk_int:"+sdk_int+"\n");
		sb.append("wMaxDP:"+wMaxDP+"\n");
	}
	
	//获取手机IMEI
	public static String getDeviceId(Context ctx){  
	    TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
	    //山寨手机有可能取不到
	    String deviceId = tm.getDeviceId();
	    if(!TextUtils.isEmpty(deviceId)){
	    	return "deviceid_"+deviceId;
	    }
	    
	    //每个android系统的唯一标识
	    String android_id = Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID); 
	    if(!TextUtils.isEmpty(android_id)){
	    	return "androidid_"+ android_id;
	    }
	    
	    //既没有deviceId,又没有androidId,生成本地一个随机窜，做临时设备ID(在应用清除数据，或卸载重装时，设备ID被清空)
	    String createDeviceId = PreferenceUtil.getInstance(AppContext.getInstance()).getString("deviceIdRand", "");
	    if(TextUtils.isEmpty(createDeviceId)){
	    	//100000000000 ~ 200000000000的一个随机窜
	    	long deviceLong = new Random(200000000000L).nextLong() + 100000000000L;
	    	createDeviceId = String.valueOf(deviceLong);
	    	PreferenceUtil.getInstance(AppContext.getInstance()).putString("deviceIdRand", createDeviceId);
	    }
	    return "createid_" + createDeviceId;
	}
	
	//获取本地的mac地址
	private static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
	
	//获取手机系统版本号
	public static String getOsVersion(){ 
		return android.os.Build.VERSION.RELEASE;
	}
	
	public static int getOsVersionCode(){
		return android.os.Build.VERSION.SDK_INT;
	}
	
	//获取应用软件VersionCode 如1
	public static int getVersion(Context ctx){
		PackageManager pm = ctx.getPackageManager();
		PackageInfo pinfo = null;
		try {
			pinfo = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return pinfo.versionCode;
	}
	
	/**
	 * 获取版本名称如 1.1
	 * @param ctx
	 * @return
	 */
	public static String getVersionName(Context ctx){
		PackageManager pm = ctx.getPackageManager();
		PackageInfo pinfo = null;
		try {
			pinfo = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return pinfo.versionName;
	}
	
	/**
	 * 获取屏幕宽度最大dp值
	 * @param activity
	 * @return
	 */
	public static int getWidthMaxDp(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int wMaxDP = (160*dm.widthPixels/dm.densityDpi);
		//Toast.makeText(activity, "Device width max dp"+wMaxDP, Toast.LENGTH_LONG).show();
		return wMaxDP;
	}
	
	/**
	 * 获取屏幕宽度最大dp值
	 * @param activity
	 * @return
	 */
	public static int getWidthMaxPx(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
	 * 获取屏幕高度最大px值
	 * @param activity
	 * @return
	 */
	public static int getHeightMaxPx(Activity activity){
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
}
