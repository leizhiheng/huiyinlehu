package com.huiyin.ui.show.player;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.util.Log;
// add by zhyao @2015/6/23 添加录音播放器
public class AudioPlayer implements OnCompletionListener, OnErrorListener {

	private static final String TAG = "AudioPlayer";
	
	private MediaPlayer mPlayer;
	
	private PlayCallBack callBack;
	
	private OnError onErrorListener;
	
	public AudioPlayer(String filePath) {
		createPlayer(filePath);
	}
	
	public void createPlayer(String filePath) {
		Log.d(TAG, "createPlayer filePath = " + filePath);
         try{  
        	 mPlayer = new MediaPlayer();  
             mPlayer.setDataSource(filePath);  
             mPlayer.prepare();  
             mPlayer.setOnCompletionListener(this);
             mPlayer.setOnErrorListener(this);
         }catch(IOException e){  
           
         }  
	}
	
	//播放
	public void start() {
		mPlayer.start();  
		handler.sendEmptyMessageDelayed(PLAY_PROGRESS, TIME_DELAY);
	}
	
	//暂停
	public void pause() {
		mPlayer.pause();
		handler.removeMessages(PLAY_PROGRESS);
	}
	
	//停止
	public void stop() {
		mPlayer.stop();  
		handler.removeMessages(PLAY_PROGRESS);
	}
	
	//是否正在播放
	public boolean isPlaying() {
		return mPlayer.isPlaying();
	}
	
	//时长
	public int getDuration() {
		return mPlayer.getDuration();
	}
	
	//当前播放点
	public int getCurrentPosition() {
		return mPlayer.getCurrentPosition();
	}
	
	//拖动进度
	public void seekTo(int msec) {
		mPlayer.seekTo(msec);
	}
	
	//释放
	public void release() {
		mPlayer.release();
		mPlayer = null;  
		handler.removeMessages(PLAY_PROGRESS);
	}

	//
	@Override
	public void onCompletion(MediaPlayer mp) {
		handler.sendEmptyMessage(PLAY_COMPLETION);
	}
	
	public void setPlayCallBack(PlayCallBack callBack) {
		this.callBack = callBack;
	}
	
	private static final int PLAY_PROGRESS  = 1;
	private static final int PLAY_COMPLETION = 2;
	private static final int TIME_DELAY = 1000;
	@SuppressLint("HandlerLeak") 
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PLAY_PROGRESS:
				if(callBack != null) {
					callBack.progress(getCurrentPosition()/1000, getDuration()/1000);
				}
				handler.removeMessages(PLAY_PROGRESS);
				handler.sendEmptyMessageDelayed(PLAY_PROGRESS, TIME_DELAY);
				break;
			case PLAY_COMPLETION:
				handler.removeMessages(PLAY_PROGRESS);
				if(callBack != null) {
					callBack.playCompletion();
				}
				break;
			default:
				break;
			}
		};
	};
	
	public void setOnErrorListener(OnError onError) {
		this.onErrorListener = onError;
	}
	
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.e(TAG, "what = " + what + " extra = " + extra);
		if(onErrorListener != null) {
			onErrorListener.onError(mp, what, extra);
		}
		return false;
	}
	
	public interface PlayCallBack {
		void progress(int progress, int duration);
		void playCompletion();
	}
	
	public interface OnError {
		void onError(MediaPlayer mp, int what, int extra);
	}

}
