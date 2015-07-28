package com.huiyin.utils;

import android.util.Log;

/**
 * log计时
 * @author liuyq
 *
 */
public class CountDown {
	
	private static final String TAG = "CountDown";
	
	private long time = 0L;
	
	
	public void start(String msg){
		Log.i(TAG, msg + " start....");
		time = System.currentTimeMillis();
	}
	
	public void stop(String msg){
		long curTime = System.currentTimeMillis();
		Log.i(TAG, msg + " stop...used time:" + (curTime-time));
	}
	
}
