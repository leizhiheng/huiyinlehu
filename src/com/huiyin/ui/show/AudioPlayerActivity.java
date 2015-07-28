package com.huiyin.ui.show;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.show.player.AudioPlayer;
import com.huiyin.ui.show.player.AudioPlayer.OnError;
import com.huiyin.ui.show.player.AudioPlayer.PlayCallBack;
import com.huiyin.ui.show.player.PlayerUtil;
// add by zhyao @2015/6/23 播放器页面
public class AudioPlayerActivity extends BaseActivity{
	
	private static final String TAG = "AudioPlayerActivity";
	public static final String AUDIO_PATH = "audio_path";
	
	private TextView ab_back;
	private TextView ab_title;
	private ImageView ab_right_btn;
	
	private SeekBar skBar_audio;
	private TextView tv_play_time;
	private ImageView btn_play_pause;
	
	private String filePath;//播放文件路径
	
	private AudioPlayer mPlayer;
	
	private Animation animationWhell;
	private Animation animationWhellSmall;
	private ImageView whell_left_img;
	private ImageView whell_right_img;
	private ImageView whell_left_small_img;
	private ImageView whell_right_small_img;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio_player);
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		initData();
	}

	private void initView() {
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_right_btn = (ImageView) findViewById(R.id.ab_right_btn);
		ab_right_btn.setVisibility(View.GONE);
		
		skBar_audio = (SeekBar) findViewById(R.id.skBar_audio);
		tv_play_time = (TextView) findViewById(R.id.tv_play_time);
		btn_play_pause = (ImageView) findViewById(R.id.btn_play_pause);
		
		ab_title.setText("录音播放器");
		
		ab_back.setOnClickListener(new backListener());
		skBar_audio.setOnSeekBarChangeListener(new audioSeekBarListener());
		btn_play_pause.setOnClickListener(new startPlayListener());
		
		// 磁带的四个轮子
		whell_left_img = (ImageView) findViewById(R.id.imageView2);
		whell_right_img = (ImageView) findViewById(R.id.imageView3);
		whell_left_small_img = (ImageView) findViewById(R.id.imageView4);
		whell_right_small_img = (ImageView) findViewById(R.id.imageView5);
		
		//磁带动画
		animationWhell = AnimationUtils.loadAnimation(this, R.anim.tape_rotate);
		animationWhell.setInterpolator(new LinearInterpolator());
		animationWhellSmall = AnimationUtils.loadAnimation(this, R.anim.tape_rotate_small);
		animationWhellSmall.setInterpolator(new LinearInterpolator());
	}
	
	private void initData() {
		filePath = getIntent().getStringExtra(AUDIO_PATH);
		//filePath = "http://7xjs9u.com1.z0.glb.clouddn.com/143461222274996.mp3";
		Log.d(TAG, "initData : filePath = " + filePath);
		initPlayer(filePath);
	}
	
	//返回
	class backListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			finish();
		}
	}
	
	 //播放录音  
    class startPlayListener implements OnClickListener{  
  
		@Override  
        public void onClick(View v) {  
        	Log.d(TAG, "mPlayer = " + mPlayer);
        	if(mPlayer == null)
        		return;
        	Log.d(TAG, "mPlayer.isPlaying = " + mPlayer.isPlaying());
        	if(mPlayer.isPlaying()) {
        		mPlayer.pause();
        		setAudioPauseState();
        	}
        	else {
        		mPlayer.start();
        		setAudioPlayState();
        	}
        }  
    }  
    
    //播放进度条
    class audioSeekBarListener implements OnSeekBarChangeListener{

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			mPlayer.seekTo(progress);
			tv_play_time.setText(PlayerUtil.parseSec(progress/1000) + "/" + PlayerUtil.parseSec(mPlayer.getDuration()/1000));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			
		}
    	
    }
    
    private void setAudioPlayState() {
    	startTapeAnimaition();
    	btn_play_pause.setBackgroundResource(R.drawable.btn_play_pause_normal);
    }
    
    private void setAudioPauseState() {
    	clearTapeAnimation();
    	btn_play_pause.setBackgroundResource(R.drawable.btn_play_start_normal);
    }

    /**
	 * @param filePath
	 */
	private void initPlayer(String filePath) {
		mPlayer = new AudioPlayer(filePath);
		mPlayer.start();
		setAudioPlayState();
		skBar_audio.setMax(mPlayer.getDuration());
		mPlayer.setPlayCallBack(new PlayCallBack() {
			
			@Override
			public void progress(int progress, int duration) {
				skBar_audio.setProgress(mPlayer.getCurrentPosition());
				tv_play_time.setText(PlayerUtil.parseSec(progress) + "/" + PlayerUtil.parseSec(duration));
			}
			
			@Override
			public void playCompletion() {
				clearTapeAnimation();
				skBar_audio.setProgress(0);
				tv_play_time.setText("00:00/00:00");
				btn_play_pause.setBackgroundResource(R.drawable.btn_play_start_normal);
			}
		});
		mPlayer.setOnErrorListener(new OnError() {
			
			@Override
			public void onError(MediaPlayer mp, int what, int extra) {
				Log.d(TAG, "initPlayer:onError");
				setAudioPauseState();
				Toast.makeText(AudioPlayerActivity.this, "加载失败！", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void releasePlayer() {
		if(mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		releasePlayer();
		super.onDestroy();
	}
	
	private void startTapeAnimaition() {
		whell_left_img.startAnimation(animationWhell);
		whell_right_img.startAnimation(animationWhell);
		whell_left_small_img.startAnimation(animationWhellSmall);
		whell_right_small_img.startAnimation(animationWhellSmall);
	}
	
	private void clearTapeAnimation() {
		whell_left_img.clearAnimation();
		whell_right_img.clearAnimation();
		whell_left_small_img.clearAnimation();
		whell_right_small_img.clearAnimation();
	}
}
