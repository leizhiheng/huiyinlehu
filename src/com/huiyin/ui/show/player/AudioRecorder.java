package com.huiyin.ui.show.player;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.czt.mp3recorder.MP3Recorder;
// add by zhyao @2015/6/23  添加mp3录音
public class AudioRecorder {
	
	private static final String TAG = "AudioRecorder";

	private MP3Recorder mRecorder;
	
	private String filePath;
	
	private String fileName;
	
	private RecordCallBack callBack;
	
	public AudioRecorder() {
		createRecorder();
	}
	
	public AudioRecorder(String filepath) {
		createRecorder(filepath);
	}
	
	public void createRecorder() {
		createRecorder(createFile());
	}
	
	//创建录音器
	public void createRecorder(String path) {
		try {
			if(mRecorder != null) {
				release();
			}
			mRecorder = new MP3Recorder(new File(path));  
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//开始录音
	public void startRecord() {
		try {
			if(mRecorder != null) {
				mRecorder.start();
				handler.sendEmptyMessageDelayed(AUDIO_PROGRESS, TIME_DELAY);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	//停止录音
	public void stopRecord() {
		if(mRecorder != null) {
			mRecorder.stop();
			handler.removeMessages(AUDIO_PROGRESS);
			progress = 0;
		}
	}
	
	//释放
	public void release() {
		mRecorder = null;
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				handler.removeMessages(AUDIO_PROGRESS);
				progress = 0;
			}
		}, 1000);
	}
	
	//正在录音
	public boolean isRecording() {
		if(mRecorder != null) {
			return mRecorder.isRecording();
		}
		return false;
	}
	
	public String createFile() {
		fileName = generateName();
		File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/huiyinlehu/music");
		//删除文件下之前的录音
		deleteFile(dir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		filePath = dir.getPath() + File.separator + fileName;
		Log.d(TAG, "filePath2 = " + filePath);
		return filePath;
	}
	
	public void deleteFile(File file) {
		if (file.exists() == false) { 
            return; 
        } else { 
            if (file.isFile()) { 
                file.delete(); 
                return; 
            } 
            if (file.isDirectory()) { 
                File[] childFile = file.listFiles(); 
                if (childFile == null || childFile.length == 0) { 
                    return; 
                } 
                for (File f : childFile) { 
                	f.delete(); 
                } 
            } 
        } 
    } 
	
	private String generateName() {
		
		return String.valueOf(System.currentTimeMillis()) + String.valueOf(new Random().nextInt(100)) + ".mp3";
	}
	
	//默认录音文件名，如果是自定义文件名则返回null
	public String getRecordFileName() {
		return fileName;
	}
	
	//默认录音文件路径，如果是自定义文件路径则返回null
	public String getRecordFilePath() {
		return filePath;
	}
	
	public void setRecordCallBack(RecordCallBack callBack) {
		this.callBack = callBack;
	}
	
	private static final int AUDIO_PROGRESS  = 1;
	private static final int TIME_DELAY = 1000;
	private int progress = 0;
	@SuppressLint("HandlerLeak") 
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case AUDIO_PROGRESS:
				progress++;
				Log.d(TAG, "AUDIO_PROGRESS : " + progress);
				if(callBack != null) {
					callBack.progress(progress);
				}
				handler.removeMessages(AUDIO_PROGRESS);
				handler.sendEmptyMessageDelayed(AUDIO_PROGRESS, TIME_DELAY);
				break;

			default:
				break;
			}
		};
	};
	
	public interface RecordCallBack {
		void progress(int progress);
	}
	
}
