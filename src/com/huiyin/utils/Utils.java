package com.huiyin.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;

public class Utils {
	public static final String TAG = "PushDemoActivity";
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_MESSAGE = "com.baiud.pushdemo.action.MESSAGE";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	protected static final String EXTRA_ACCESS_TOKEN = "access_token";
	public static final String EXTRA_MESSAGE = "message";

	public static String logStringCache = "";

	// 获取ApiKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	// 用share preference来实现是否绑定的开关。在ionBind且成功时设置true，unBind且成功时设置false
	public static boolean hasBind(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		String flag = sp.getString("bind_flag", "");
		if ("ok".equalsIgnoreCase(flag)) {
			return true;
		}
		return false;
	}

	public static void setBind(Context context, boolean flag) {
		String flagStr = "not";
		if (flag) {
			flagStr = "ok";
		}
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("bind_flag", flagStr);
		editor.commit();
	}

	public static List<String> getTagsList(String originalText) {
		if (originalText == null || originalText.equals("")) {
			return null;
		}
		List<String> tags = new ArrayList<String>();
		int indexOfComma = originalText.indexOf(',');
		String tag;
		while (indexOfComma != -1) {
			tag = originalText.substring(0, indexOfComma);
			tags.add(tag);

			originalText = originalText.substring(indexOfComma + 1);
			indexOfComma = originalText.indexOf(',');
		}

		tags.add(originalText);
		return tags;
	}

	public static String getLogText(Context context) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString("log_text", "");
	}

	public static void setLogText(Context context, String text) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString("log_text", text);
		editor.commit();
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrTiem() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");// 可以方便地修改日期格式
		return dateFormat.format(now);
	}

	/**
	 * 将string转换成int
	 * 
	 * @param target
	 *            目标字符串
	 * @return int
	 */
	public static int anInt(String target) {
		try{
			if (TextUtils.isEmpty(target))
				return 0;
			if (target.contains(".")) {
				return (int) Double.parseDouble(target);
			} else {
				return Integer.valueOf(target);
			}
		}catch(Exception e){
			return 0;
		}
	}

	/**
	 * 将string转换成double
	 * 
	 * @param target
	 *            目标字符串
	 * @return double
	 */
	public static double anDouble(String target) {
		if (TextUtils.isEmpty(target))
			return 0.00;
		else
			return Double.parseDouble(target);

	}

	/**
	 * 正则匹配电话号码
	 */
	public static boolean checkPhoneREX(String value) {

		String regExp = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		// String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(value);
		return m.find();
	}

	public static String Long2DateStr_detail(long time) {
		String format = "yyyy-M-d HH:mm";
		Date date = new Date(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String long_time = sdf.format(date);
		return long_time;
	}

	public static int[] getYMD(String date) {
		// 2015-06-19 10:38:37
		int[] YMD = new int[3];
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
			YMD[0] = cal.get(Calendar.YEAR);
			YMD[1] = cal.get(Calendar.MONTH) + 1;
			YMD[2] = cal.get(Calendar.DAY_OF_MONTH);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return YMD;
	}

    /**
     *  转换时间格式
     * @param formate 需要的格式
     * @param time 原始格式
     * @return
     */
    public static String getSpecialTime(String formate,String time) {
		// 2015-06-19 10:38:37
        try {
            SimpleDateFormat frist=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sec=new SimpleDateFormat(formate);
            Date mDate=frist.parse(time);
            return sec.format(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
    
    public static int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				res.getDisplayMetrics());
	}

    /**
     * 商品活动是否进行中
     * @param start 开始时间
     * @param end 结束时间
     * @param cur 当前时间
     * @return
     */
    public static boolean beingActivity(String start,String end,String cur){
        if (!TextUtils.isEmpty(start)&&!TextUtils.isEmpty(end)&&!TextUtils.isEmpty(cur)){
            try {
                long startSecond = StringUtils.toDate(start).getTime();
                long endSecond = StringUtils.toDate(end).getTime();
                long nowTime = StringUtils.toDate(cur).getTime();
                return nowTime >= startSecond && nowTime < endSecond;
            }catch (Exception eNull){
                eNull.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }
    /**
     * 商品活动是否结束
     * @param start 开始时间
     * @param end 结束时间
     * @param cur 当前时间
     * @return
     */
    public static boolean isActivityEnd(String start ,String end,String cur){
        if (!TextUtils.isEmpty(start)&&!TextUtils.isEmpty(end)&&!TextUtils.isEmpty(cur)){
            try {
                long endSecond = StringUtils.toDate(end).getTime();
                long nowTime = StringUtils.toDate(cur).getTime();
                return nowTime >= endSecond;
            }catch (Exception eNull){
                eNull.printStackTrace();
                return true;
            }
        }else{
            return true;
        }
    }
    
    
    /**
     * 判断是否是在后台
     * @param context
     * @param tag，只是标记作用，没有任何作用，好让知道是在哪里调用了这个方法
     * @return
     */
	public static boolean isInBackground(Context context, String tag) {
		Log.i(TAG, "tag--------------" + tag);

		boolean inBackground = false;

		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();

		String processName = null;
		String currentProcessName = context.getPackageName();
		Log.i(TAG, "currentProcesName----" + currentProcessName);

		for (RunningAppProcessInfo process : runningApps) {
			processName = process.processName;

			if (processName.equals(currentProcessName)) {
				Log.i(TAG, "processName " + process.processName + ", " + process.uid + ", state :" + process.importance);
				switch (process.importance) {
				case RunningAppProcessInfo.IMPORTANCE_FOREGROUND:// 100前台

					inBackground = false;

					break;

				case RunningAppProcessInfo.IMPORTANCE_BACKGROUND:// 400后台,当home键后，在service里面调用此方法，得到这个值

					inBackground = true;

					break;

				case RunningAppProcessInfo.IMPORTANCE_EMPTY:// 500 这个进程不存在

					break;

				case RunningAppProcessInfo.IMPORTANCE_SERVICE:// 300这个进程包括service在里面

					inBackground = true;

					break;
				case RunningAppProcessInfo.IMPORTANCE_VISIBLE:// 200这个进程是可见的，正展现给用户

					break;

				case 130:// 4.0以上专有的值 IMPORTANCE_PERCEPTIBLE(可察觉到的)

					inBackground = true;

					break;

				default:

					inBackground = true;

					break;
				}

			}
		}

		return inBackground;
	}
}
