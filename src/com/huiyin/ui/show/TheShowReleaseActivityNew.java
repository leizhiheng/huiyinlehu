package com.huiyin.ui.show;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.dialog.ConfirmDialog;
import com.huiyin.dialog.ConfirmDialog.DialogClickListener;
import com.huiyin.ui.user.LoginActivity;
//add by zhyao @2105/6/18 新的发布秀场  添加秀歌
/**
 * 发布秀场  秀图和秀歌
 * @author zhyao
 *
 */
public class TheShowReleaseActivityNew extends BaseActivity implements OnClickListener,OnCheckedChangeListener{
	
	public static final String TAG = "TheShowReleaseActivityNew";
	
	public static final int SHOW_PICTURE = 0;
	public static final int SHOW_MUSIC = 1;
	
	//返回
	private TextView left_ib;
	private TextView middle_title_tv;
	private TextView right_ib;
	
	private RadioGroup main_radio;
	private RadioButton rd_show_music;
	
	private String userid = "";// 用户id
	
	private FragmentTransaction transaction;
	private TheShowReleasePictureFragment mTheShowReleasePictureFragment;
	private TheShowReleaseMusicFragment mTheShowReleaseMusicFragment;
	
	private int currentIndex = SHOW_PICTURE;//当前index
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theshow_release_new);
		userid = AppContext.userId;
		initView();
	}

	private void initView() {
		
		left_ib = (TextView) findViewById(R.id.left_ib);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("发布");
		right_ib = (TextView) findViewById(R.id.right_ib);
		right_ib.setBackgroundResource(R.drawable.icon_wancheng);
		right_ib.setVisibility(View.VISIBLE);
		left_ib.setOnClickListener(this);
		right_ib.setOnClickListener(this);
		
		main_radio = (RadioGroup) findViewById(R.id.main_radio);
		main_radio.setOnCheckedChangeListener(this);
		main_radio.check(R.id.rd_show_picture);
		
		rd_show_music = (RadioButton) findViewById(R.id.rd_show_music);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.left_ib:
			if (mTheShowReleaseMusicFragment != null && mTheShowReleaseMusicFragment.isRecording()) {
				showDialog("放弃录制", "请确认您需要放弃录制吗？", true);
			}
			else {
				finish();
			}
			break;
		case R.id.right_ib:
			if (userid != null && !"".equals(userid)) {
//				initData();
				if (currentIndex == SHOW_PICTURE) {
					if (mTheShowReleasePictureFragment == null) {
						mTheShowReleasePictureFragment = new TheShowReleasePictureFragment();
					}
					mTheShowReleasePictureFragment.initData();
				}
				else if(currentIndex == SHOW_MUSIC) {
					if (mTheShowReleaseMusicFragment == null) {
						mTheShowReleaseMusicFragment = new TheShowReleaseMusicFragment();
					}
					mTheShowReleaseMusicFragment.releaseMusic();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请登录！！！",
						Toast.LENGTH_LONG).show();

				// 判断登录
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				startActivity(intent);

				return;
			}
			break;

		}
	
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId) {
		//秀图
		case R.id.rd_show_picture:
			
			if (mTheShowReleaseMusicFragment != null && mTheShowReleaseMusicFragment.isRecording()) {
				showDialog("放弃录制", "请确认您需要放弃录制吗？", false);
			}
			else {
				currentIndex = SHOW_PICTURE;
				if (mTheShowReleasePictureFragment == null) {
					mTheShowReleasePictureFragment = new TheShowReleasePictureFragment();
				}
				transaction = getSupportFragmentManager().beginTransaction();
				transaction.replace(R.id.frame_content, mTheShowReleasePictureFragment);
				transaction.commit();
			}
			
			break;
		//秀歌
		case R.id.rd_show_music:
			Log.d(TAG, "onCheckedChanged rd_show_music");
			currentIndex = SHOW_MUSIC;
			if (mTheShowReleaseMusicFragment == null) {
				mTheShowReleaseMusicFragment = new TheShowReleaseMusicFragment();
			}
			transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.frame_content, mTheShowReleaseMusicFragment);
			transaction.commit();
			break;
		default:
			break;
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown");
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mTheShowReleaseMusicFragment != null && mTheShowReleaseMusicFragment.isRecording()) {
				showDialog("放弃录制", "请确认您需要放弃录制吗？", true);
				return true;
			}
			else {
				finish();
			}
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void showDialog(String title, String msg, final boolean isBack) {
		ConfirmDialog dialog = new ConfirmDialog(this);
		dialog.setCustomTitle(title);
		dialog.setCancelable(false);
		dialog.setMessage(msg);
		dialog.setConfirm("确定");
		dialog.setCancel("取消");
		dialog.setClickListener(new DialogClickListener() {

			@Override
			public void onConfirmClickListener() {
				if(isBack) {
					TheShowReleaseActivityNew.this.finish();
				}
				else {
					//切换到秀图，停止录制
					mTheShowReleaseMusicFragment.releaseAudioRecorder();
					mTheShowReleaseMusicFragment.refreshRecordUI();
					
					currentIndex = SHOW_PICTURE;
					if (mTheShowReleasePictureFragment == null) {
						mTheShowReleasePictureFragment = new TheShowReleasePictureFragment();
					}
					transaction = getSupportFragmentManager().beginTransaction();
					transaction.replace(R.id.frame_content, mTheShowReleasePictureFragment);
					transaction.commit();
				}
			}

			@Override
			public void onCancelClickListener() {
				rd_show_music.setChecked(true);
			}
		});
		dialog.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 点击空白处，隐藏软键盘
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

}