package com.huiyin.ui.show.player;

import java.util.Formatter;
import java.util.Locale;

public class PlayerUtil {
	
	private static StringBuilder mFormatBuilder;
	private static Formatter mFormatter;
	static {
		mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
	}
	
	/**
     * 将时间转为时分秒 。
     * 
     * @param totalSeconds
     * @return
     */
    public static String parseSec(int totalSeconds)
    {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        mFormatBuilder.setLength(0);
        return mFormatter.format("%02d:%02d", minutes, seconds).toString();
    }

}
