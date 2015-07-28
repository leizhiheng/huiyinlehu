package com.huiyin.ui.show;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.dialog.ConfirmDialog;
import com.huiyin.dialog.ConfirmDialog.DialogClickListener;
import com.huiyin.http.JsonHttpResponseHandler;
import com.huiyin.ui.show.player.AudioRecorder;
import com.huiyin.ui.show.player.AudioRecorder.RecordCallBack;
import com.huiyin.ui.show.player.PlayerUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
// add by zhyao @2015/6/20 添加秀歌
public class TheShowReleaseMusicFragment extends Fragment{
	
	private static final String TAG = "TheShowReleaseMusicFragment";
	
	private TextView tv_record_status;
	private TextView tv_record_time;
	private EditText ed_comment;
	private Button btn_record_start;
	private Button btn_record_play;
	private String fileName;
	private String filePath;
	private View view;
	
	private AudioRecorder mRecorder;
	
	private Animation animationWhell;
	private Animation animationWhellSmall;
	private ImageView whell_left_img;
	private ImageView whell_right_img;
	private ImageView whell_left_small_img;
	private ImageView whell_right_small_img;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		if(view == null) {
			view = inflater.inflate(R.layout.theshow_release_music, null);
			initView(view);
		}
		
		ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        } 
		
		return view;
	}
	
	private void initView(View view) {
		
		tv_record_status = (TextView) view.findViewById(R.id.tv_record_status);
		tv_record_time = (TextView) view.findViewById(R.id.tv_record_time);
		ed_comment = (EditText) view.findViewById(R.id.ed_comment);
		btn_record_start = (Button) view.findViewById(R.id.btn_record_start);
		btn_record_play = (Button) view.findViewById(R.id.btn_record_back);
		
		btn_record_start.setOnClickListener(new startRecordListener());
		btn_record_play.setOnClickListener(new startPlayListener());
		
		// 磁带的四个轮子
		whell_left_img = (ImageView) view.findViewById(R.id.imageView2);
		whell_right_img = (ImageView) view.findViewById(R.id.imageView3);
		whell_left_small_img = (ImageView) view.findViewById(R.id.imageView4);
		whell_right_small_img = (ImageView) view.findViewById(R.id.imageView5);
		
		//磁带动画
		animationWhell = AnimationUtils.loadAnimation(getActivity(), R.anim.tape_rotate);
		animationWhell.setInterpolator(new LinearInterpolator());
		animationWhellSmall = AnimationUtils.loadAnimation(getActivity(), R.anim.tape_rotate_small);
		animationWhellSmall.setInterpolator(new LinearInterpolator());
		
		tv_record_status.setText("准备录制:");
	}
	
	
	private void startTapeAnimaition() {
		whell_left_img.startAnimation(animationWhell);
		whell_right_img.startAnimation(animationWhell);
		whell_left_small_img.startAnimation(animationWhellSmall);
		whell_right_small_img.startAnimation(animationWhellSmall);
		
		tv_record_status.setText("正在录制:");
	}
	
	private void clearTapeAnimation() {
		whell_left_img.clearAnimation();
		whell_right_img.clearAnimation();
		whell_left_small_img.clearAnimation();
		whell_right_small_img.clearAnimation();
		
		tv_record_status.setText("完成录制:");
	}
	
	//初始化录音器
	private void initAudioRecorder() {
		mRecorder = new AudioRecorder();
		mRecorder.setRecordCallBack(new RecordCallBack() {
			
			@Override
			public void progress(int progress) {
				tv_record_time.setText(PlayerUtil.parseSec(progress)+"/10:00");
				//最长录音10分钟,10分钟之后自动停止录音
				if(progress == 10 * 60) 
				{
					//清除磁带动画
					clearTapeAnimation();
					mRecorder.stopRecord();
					mRecorder.release();
					
	        		filePath = mRecorder.getRecordFilePath();
	        		fileName = mRecorder.getRecordFileName();
	        		
	        		//恢复播放按钮
	        		btn_record_play.setEnabled(true);
	        		btn_record_start.setBackgroundResource(R.drawable.btn_record_normal);
	        		
	        		Log.d(TAG, "filePath = " + filePath);
				}
			}
		});
	}
	
	//释放录音器
	public void releaseAudioRecorder() {
		if (mRecorder != null) {
			mRecorder.stopRecord();
			mRecorder.release();
			mRecorder = null;
		}
	}
	
	public void refreshRecordUI() {
		btn_record_start.setBackgroundResource(R.drawable.btn_record_normal);
		tv_record_time.setText("00:00/00:00");
	}
	
	@Override
	public void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		releaseAudioRecorder();
		super.onDestroy();
	}
	
	//开始录音  
    class startRecordListener implements OnClickListener{  
  
        @Override  
        public void onClick(View v) {  
        	
        	if(isRecording()) {
        		showDialog("录制完成", "请确认您需要完成录制吗？");
        	}
        	else {
        		//开始播放磁带动画
        		startTapeAnimaition();
        		//初始化录音
        		initAudioRecorder();
        		//开始录音
        		mRecorder.startRecord();
        		//播放按钮不可用
        		btn_record_play.setEnabled(false);
        		btn_record_start.setBackgroundResource(R.drawable.btn_stop_normal);
        	}
        }  
    }  
    
    //播放录音  
    class startPlayListener implements OnClickListener{  
  
        @Override  
        public void onClick(View v) {  
        	Intent intent = new Intent(getActivity(), AudioPlayerActivity.class);
        	intent.putExtra(AudioPlayerActivity.AUDIO_PATH, filePath);
        	startActivity(intent);
        }  
          
    }  
    
    
    public boolean isRecording() {
    	if(mRecorder!=null) {
    		return mRecorder.isRecording();
    	}
    	return false;
    }
    
    public void showDialog(String title, String msg) {
		ConfirmDialog dialog = new ConfirmDialog(getActivity());
		dialog.setCustomTitle(title);
		dialog.setMessage(msg);
		dialog.setConfirm("确定");
		dialog.setCancel("取消");
		dialog.setClickListener(new DialogClickListener() {

			@Override
			public void onConfirmClickListener() {
				//清除磁带动画
				clearTapeAnimation();
				//停止录音
				mRecorder.stopRecord();
				//释放录音
				mRecorder.release();
				
				//录音文件路径
        		filePath = mRecorder.getRecordFilePath();
        		fileName = mRecorder.getRecordFileName();
        		
        		//恢复播放按钮
        		btn_record_play.setEnabled(true);
        		btn_record_start.setBackgroundResource(R.drawable.btn_record_normal);
        		Log.d(TAG, "filePath = " + filePath);
			}

			@Override
			public void onCancelClickListener() {

			}
		});
		dialog.show();
	}
    
    //发布歌曲
    public void releaseMusic() {
    	 queryUpToken();
    }
    
    //后台查询uptoken
    private void queryUpToken() {
    	RequstClient.queryQiNiuUptoken(new JsonHttpResponseHandler(){
    		@Override
    		public void onSuccess(int statusCode, JSONObject response) {
    			super.onSuccess(statusCode, response);
    			try {
					String upToken = response.getString("token");
					upLoadMusic(upToken);
				} catch (JSONException e) {
					e.printStackTrace();
				}
    		}
    		
    		@Override
    		public void onFailure(Throwable e, JSONArray errorResponse) {
    			super.onFailure(e, errorResponse);
    			Toast.makeText(getActivity(), "获取UpToken失败", Toast.LENGTH_SHORT).show();
    		}
    	});
    }

    private void upLoadMusic(String upToken) {
    	Log.i(TAG, "upLoadMusic : upToken = " + upToken);
    	final String expectKey = fileName;
    	
    	if(TextUtils.isEmpty(filePath)) {
    		Toast.makeText(getActivity(), "您需要录制一首歌曲", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	File file = new File(filePath);
    	
        Map<String, String> params = new HashMap<String, String>();
        params.put("x:foo", "fooval");
        final UploadOptions opt = new UploadOptions(params, null, true, null, null);
        new UploadManager().put(file, expectKey, upToken, new UpCompletionHandler() {
			@Override
			public void complete(String key, ResponseInfo rinfo, JSONObject response) {
				Log.i(TAG, "key = " + key + "/n" + "rinfo = " + rinfo + "/n" + "response = " + response);
				if(rinfo.isOK()) {
					appPublish(key);
				}
				else {
					Toast.makeText(getActivity(), "上传文件失败！", Toast.LENGTH_SHORT).show();
				}
			}
        }, opt);

    }
    
    private void appPublish(String musicPath) {
    	// 加载网络
		MyCustomResponseHandler handler = new MyCustomResponseHandler(
				getActivity(), true) {

			@Override
			public void onRefreshData(String content) {
				Toast.makeText(getActivity(),
						"感谢您的提交，我们会尽快审核！", Toast.LENGTH_LONG)
						.show();
				getActivity().finish();
			}

			@Override
			public void onFailure(Throwable error,
					String content) {
				super.onFailure(error, content);
				return;
			}
		};
    	String user_id = AppContext.userId;
		RequstClient.appPublish(handler, user_id, ed_comment.getText().toString(),
				musicPath);
    }
    
}
