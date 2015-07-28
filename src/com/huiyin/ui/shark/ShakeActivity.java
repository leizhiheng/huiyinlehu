// add by zhyao @2015/5/8  添加摇一摇界面
package com.huiyin.ui.shark;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.ClubGridViewAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.Club;
import com.huiyin.bean.Prize;
import com.huiyin.bean.PrizeRecord;
import com.huiyin.dialog.ConfirmDialog;
import com.huiyin.dialog.ConfirmDialog.DialogClickListener;
import com.huiyin.dialog.SingleConfirmDialog;
import com.huiyin.ui.shark.listener.ShakeListener;
import com.huiyin.ui.shark.listener.ShakeListener.OnShakeListener;
import com.huiyin.utils.StringUtils;
/**
 * 摇一摇功能
 * 根据用户积分使用摇一摇抽奖
 * @author yzh
 *
 */
public class ShakeActivity extends Activity implements OnShakeListener, OnClickListener {
	
	public final static String INTENT_KEY_TITLE = "title";
	
	//标题
	private TextView ab_title;
	//返回键
	private TextView ab_back;
	//活动说明
	private Button btn_info;
	//摇一摇图片
	private ImageView sharke_hand;
	//摇一摇抽奖剩余次数
	private TextView tv_count;
	//展示中奖名单
	private TextView tv_ad;
	//展示参与人数
	private GridView gridView;
	private ClubGridViewAdapter gridadapter;
	
	//设备传感器监听
	private ShakeListener shakeListener;
	//摇一摇动画集
	private AnimationSet animationSet;
	//定时器，循环展示动画
	private Timer timer = new Timer();
	//音频处理
	private SoundPool soundPool;  
	private Map<Integer, Integer> soundMap;  
	//最近的摇晃时间
	private long lastTime = 0;
	
	//积分
	private Club club;
	//成功获取中奖信息
	private boolean isSuccess;
	//记录循环展示中奖名单index
	private int prize_count = 0;
	
	//循环展示获奖名单
	private final int HANDLER_PRIZE = 1;
	//获奖结果
	private final int HANDLER_PRIZE_RESULT = 2;
	//摇一摇结束
	private final int HANDLER_PRIZE_STOP = 3;
	//循环显示摇一摇动画
	private final int HANDLER_SHAKE_ANIMATION = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		
		initViews();
		//设置动画
		setAnimation();
		startAnimation();
		//设置声音
		initSound();
		
		//请求抽奖舒初始化数据
		requestData();
	}
	
	private void initViews() {
		ab_title = (TextView) findViewById(R.id.ab_title);
		ab_title.setText(getIntent().getStringExtra(INTENT_KEY_TITLE));
		ab_back = (TextView) findViewById(R.id.ab_back);
		ab_back.setOnClickListener(this);
		
		btn_info = (Button) findViewById(R.id.btn_info);
		btn_info.setOnClickListener(this);
		
		gridView = (GridView) findViewById(R.id.mGridView);
		
		sharke_hand = (ImageView)findViewById(R.id.sharke_hand);
		
		tv_ad = (TextView) findViewById(R.id.tv_ad);
		tv_count = (TextView) findViewById(R.id.tv_count);
		
	}

	private void setAnimation() {
		
		RotateAnimation rotate1 = new RotateAnimation(0, -45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
		rotate1.setDuration(500);
		
		RotateAnimation rotate2 = new RotateAnimation(-45, 45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
		rotate2.setDuration(500);
		rotate2.setStartOffset(500);
		rotate2.setInterpolator(new AccelerateDecelerateInterpolator());
		
		RotateAnimation rotate3 = new RotateAnimation(45, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
		rotate3.setDuration(500);
		rotate3.setStartOffset(1000);
		
	    animationSet = new AnimationSet(true);
		animationSet.addAnimation(rotate1);
		animationSet.addAnimation(rotate2);
		animationSet.addAnimation(rotate3);
		
	}
	
	private void startAnimation() {
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				Message message = new Message();
				message.what = HANDLER_SHAKE_ANIMATION;
				handler.sendMessage(message);
			}
		}, 0, 3000);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		timer.cancel();
		timer = null;
		if(shakeListener != null) {
			shakeListener.stop();
		}
	}
	
	private void initShake() {
	    shakeListener = new ShakeListener(getApplicationContext());
		shakeListener.setOnShakeListener(this);
		shakeListener.start();
	}

	@Override
	public void onShake() {
		if(System.currentTimeMillis() - lastTime > 5000) {
			lastTime = System.currentTimeMillis();
			playBeepSoundAndVibrate();
			//发送获取中奖信息请求
			requestShakeData();
		}
	};
	
	private void initSound() {
		soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC, 0); 
		soundMap=new HashMap<Integer, Integer>();  
        soundMap.put(1, soundPool.load(ShakeActivity.this, R.raw.shakeing, 1));  
        soundMap.put(2, soundPool.load(ShakeActivity.this, R.raw.shake_something, 1));  
	}
	
	
	private void playBeepSoundAndVibrate() { 
		Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE); 
		vibrator.vibrate(new long[]{300,500}, -1); 
		
        soundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);  
	} 
	
	private void playSuccessSound() {
	    soundPool.play(soundMap.get(2), 1, 1, 0, 0, 1);  
	}

	
	/***
	 * 初始化积分数据
	 * 
	 * */
	private void requestData() {
		CustomResponseHandler handler = new CustomResponseHandler(this, true) {
			@Override
			public void onRefreshData(String content) {
				analyticalData(content);
			}
		};
		RequstClient.getClub(AppContext.userId, handler);
	}
	
	/**
	 * 摇一摇之后，请求抽奖信息
	 */
	private void requestShakeData() {
		if (initDate()) {
				isSuccess = false;
				// 已经登陆
				if (AppContext.userId != null && !AppContext.userId.equals("")) {
					// 当前用户免费抽奖次数小于每天免费的抽奖次数
					if (club.getNum() >= 0 && club.getUserNum() >= 0 && club.getUserNum() < club.getNum()) {
						// 提交抽奖
						isSuccess = false;
						submitData();
					} else {
						// 是否开启积分兑换抽奖机会
						if(club.isIntegralDecuc()) {
							// 积分抽奖
							if (club.getUserIntegral() >= club.getIntegral()) {
								// 当前用户积分大于每次抽奖的积分，积分抽奖
								isSuccess = false;
								submitData();
							} else {
								// 无积分，无免费
								showDialog("温馨提示", "亲，您免费机会已经用完，赶紧去购物赢取积分吧！");
							}
						} else {
							// 当前未开启积分兑换抽奖
							showDialog("温馨提示", "亲，当前还没有开启积分兑换抽奖！");
						}
					}
				} else {
					Toast.makeText(getApplicationContext(), "您还未登录", Toast.LENGTH_SHORT).show();
				}
		}
	}
	
	/***
	 * 解析初始化数据
	 * 
	 * */
	private void analyticalData(String content) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				JSONObject integralClub = roots.getJSONObject("integralClub");
				club = new Club();
				club.setStartTime(integralClub.getString("STARTTIME"));
				club.setEndTime(integralClub.getString("ENDTIME"));
				club.setContent(integralClub.getString("CONTENT"));
				club.setId(integralClub.getString("ID"));
				club.setPrizeSet(integralClub.getString("WINNING"));
				club.setQuantity(integralClub.getString("QUANTITY"));// 参与人数
				club.setNextTime(integralClub.getString("TWO_START_TIME"));
				club.setCurrentTime(roots.getString("datetime"));

				if (roots.getString("userIntegral") != null && !roots.getString("userIntegral").equals("")) {
					club.setUserIntegral(Integer.valueOf(roots.getString("userIntegral")));
				}
				club.setIntegral(roots.getInt("integral"));

				if (roots.getString("userNum") != null && !roots.getString("userNum").equals("")) {
					club.setUserNum(Integer.valueOf(roots.getString("userNum")));
				}
				club.setNum(roots.getInt("num"));

				List<Prize> listPrizes = new ArrayList<Prize>();
				JSONArray luckProbabilityList = roots.getJSONArray("luckProbabilityList");
				for (int i = 0; i < luckProbabilityList.length(); i++) {
					JSONObject obj = luckProbabilityList.getJSONObject(i);
					Prize prize = new Prize();
					prize.setId(obj.getInt("ID"));
					prize.setName(obj.getString("PRIZENAME"));
					prize.setProbability(obj.getInt("PROBABILITY"));
					prize.setSpoil(obj.getInt("SPOIL"));
					listPrizes.add(prize);
				}
				club.setListPrizes(listPrizes);

				List<PrizeRecord> listPrizeRecords = new ArrayList<PrizeRecord>();
				JSONArray zhongJiangJiLu = roots.getJSONArray("zhongJiangJiLu");
				for (int i = 0; i < zhongJiangJiLu.length(); i++) {
					JSONObject obj = zhongJiangJiLu.getJSONObject(i);
					PrizeRecord prize = new PrizeRecord();
					prize.setUserName(obj.getString("USER_NAME"));
					prize.setPrizeName(obj.getString("PRIZE"));
					prize.setAddTime(obj.getString("ADDTIME"));
					listPrizeRecords.add(prize);
				}
				club.setListPrizeRecords(listPrizeRecords);
				if (roots.getInt("shut") < 0) {
					club.setShut(true);
				} else {
					club.setShut(false);
				}
				if (roots.getInt("integralDecuc") == 0) {
					club.setIntegralDecuc(true);
				} else {
					club.setIntegralDecuc(false);
				}
				setView();
			} else {
				String errorMsg = roots.getString("msg");
				Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 提交抽奖
	 * */
	private void submitData() {
		CustomResponseHandler handler = new CustomResponseHandler(this, false) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				getSubmitData(content);
			}

			@Override
			public void onFailure(String error, String errorMessage) {
				super.onFailure(error, errorMessage);
				getDataFail();
			}
		};
		RequstClient.submitClub(AppContext.userId, club.getId(), handler);
	}

	/**
	 * 获取抽奖结果
	 * 
	 * */
	private void getSubmitData(String content) {
		Log.i("ShakeActivity", "yzh getSubmitData content = " + content);
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				int integral = roots.getInt("integral");
				int sumNum = roots.getInt("sumNum");
				int index = roots.getInt("num");

				AppContext.mUserInfo.integral = integral + "";

				isSuccess = true;
				club.setUserNum(sumNum);
				club.setUserIntegral(integral);

				if (index == -1) {
					// 活动已结束
					Message message = new Message();
					message.what = HANDLER_PRIZE_STOP;
					message.arg1 = index;
					message.arg2 = 0;//
					handler.sendMessageDelayed(message, 1000);
				} else {
					Message message = new Message();
					message.what = HANDLER_PRIZE_STOP;
					message.arg1 = index;
					message.arg2 = 1;//
					handler.sendMessageDelayed(message, 1000);
				}

			} else {
				getDataFail();
			}
		} catch (JSONException e) {
			// Json解析失败
			getDataFail();
		}
	}
	
	private void getDataFail() {
		Message message = new Message();
		message.what = HANDLER_PRIZE_STOP;
		message.arg1 = 0;
		message.arg2 = 0;// 失败
		message.obj = "获取奖品失败";
		handler.sendMessageDelayed(message, 1000);
	}
	
	private void setView() {

		setCount();

		setPrizeView();

		// 参与人数
		if ((club.getQuantity() == null) || (club.getQuantity().length() == 0)) {
			club.setQuantity("0");
		}

		//设置参与人数gridview
		LayoutParams params = new LayoutParams(club.getQuantity().length() * (24 + 6), LayoutParams.WRAP_CONTENT);
		gridView.setLayoutParams(params);
		gridView.setColumnWidth(26);
		gridView.setHorizontalSpacing(4);
		gridView.setStretchMode(GridView.NO_STRETCH);
		gridView.setNumColumns(club.getQuantity().length());
		gridadapter = new ClubGridViewAdapter(this, club.getQuantity());
		gridView.setAdapter(gridadapter);

		initDate();
		
		//设置传感器
		initShake();
	}
	
	/**
	 * 判断是否能参加活动
	 * @return
	 */
	private boolean initDate() {
		if (club.isShut()) {
			// 关闭
			showDialog("活动已关闭", "感谢您的参与，活动已经关闭了！");
			return false;
		} else {
			if (compareDate(club.getCurrentTime(), club.getStartTime()) < 0) {
				// 活动未开始
				showDialog("活动未开始", "活动开始时间：" + club.getStartTime());
				return false;
			} else if (compareDate(club.getCurrentTime(), club.getStartTime()) >= 0
					&& compareDate(club.getCurrentTime(), club.getEndTime()) <= 0) {
				// 活动进行中
				return true;
			} else {
				showDialog("活动已经结束", "下次活动开始时间：" + club.getNextTime());
				return false;
			}
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public int compareDate(String date1, String date2) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				// System.out.println("dt1 在dt2前");
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				// System.out.println("dt1在dt2后");
				return -1;
			}

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * 设置剩余的抽奖次数
	 */
	private void setCount() {
		if (AppContext.userId != null && !AppContext.userId.equals("")) {
			// 登陆了
			if (club.getUserNum() < club.getNum() && club.getNum() >= 0 && club.getUserNum() >= 0) {
				tv_count.setText(Html.fromHtml(String.format(getString(R.string.club_count), club.getNum() - club.getUserNum())));
			} else {
				if (club.getUserIntegral() >= club.getIntegral()) {
					tv_count.setText(Html.fromHtml("您的剩余积分为<u>" + club.getUserIntegral() + "</u>,每次抽奖需要扣除<u>"
							+ club.getIntegral() + "</u>,还可参加<u>" + club.getUserIntegral() / club.getIntegral()
							+ "</u>次抽奖，大奖等着你哟！"));
				} else {
					tv_count.setText(Html.fromHtml("您的剩余积分为<u>" + club.getUserIntegral() + "</u>,每次抽奖需要扣除<u>"
							+ club.getIntegral() + "</u>,您积分不足,赶紧赢取积分再来试试吧！"));
				}
			}
		} else {
			// 未登陆
			tv_count.setText(Html.fromHtml(String.format(getString(R.string.club_count_without_login), club.getNum())));
		}
	}
	
	/**
	 * 设置奖品滚动条
	 * 
	 * */
	private void setPrizeView() {
		if (club.getListPrizeRecords() != null && club.getListPrizeRecords().size() > 0) {
			tv_ad.setVisibility(View.VISIBLE);
			String temp = club.getListPrizeRecords().get(prize_count).getUserName();
			if (StringUtils.isMobileNO(temp)) {
				temp = temp.substring(0, 3) + "****" + temp.substring(7);
			}
			tv_ad.setText("恭喜会员" + temp + "于" + club.getListPrizeRecords().get(prize_count).getAddTime() + ",成功抽取"
					+ club.getListPrizeRecords().get(prize_count).getPrizeName() + "。");

			handler.sendEmptyMessageDelayed(HANDLER_PRIZE, 5000);
		} else {
			tv_ad.setVisibility(View.INVISIBLE);
		}
	}
	
	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//循环展示摇一摇动画
			case HANDLER_SHAKE_ANIMATION:
				sharke_hand.startAnimation(animationSet);
				break;
			// 滚动展示获奖名单 
			case HANDLER_PRIZE:
				if (club.getListPrizes() != null && club.getListPrizes().size() > 0) {
					prize_count++;
					String temp = club.getListPrizeRecords().get(prize_count).getUserName();
					if (StringUtils.isMobileNO(temp)) {
						temp = temp.substring(0, 3) + "****" + temp.substring(7);
					}
					if (prize_count < club.getListPrizeRecords().size() - 1) {
						tv_ad.setText("恭喜会员" + temp + "于" + club.getListPrizeRecords().get(prize_count).getAddTime() + ",成功抽取"
								+ club.getListPrizeRecords().get(prize_count).getPrizeName() + "。");
					} else {
						prize_count = 0;
						tv_ad.setText("恭喜会员" + temp + "于" + club.getListPrizeRecords().get(prize_count).getAddTime() + ",成功抽取"
								+ club.getListPrizeRecords().get(prize_count).getPrizeName() + "。");
					}
					handler.sendEmptyMessageDelayed(HANDLER_PRIZE, 5000);
				}
				break;
			case HANDLER_PRIZE_RESULT:
					if (AppContext.userId != null && !AppContext.userId.equals("")) {
						if (isSuccess) {
							setCount();
							playSuccessSound();
							if ((int) msg.arg1 == 2) {
								// 非奖品
								showPrizeDialog("很遗憾", "谢谢参与！");
							} else {
								showPrizeDialog("恭喜您", "恭喜您获得" + (CharSequence) msg.obj);
							}
						}
					} else {
						Toast.makeText(getApplicationContext(), "您还未登录", Toast.LENGTH_SHORT).show();
					}
				break;
			case HANDLER_PRIZE_STOP:
					// 设置中奖项(随机的话请注释)
					if (msg.arg2 == 1) {
						//lotteryView.setAwards(msg.arg1, true);
						// 返回当前所值的奖品
						Prize prize = club.getListPrizes().get(msg.arg1);
						Message msg1 = new Message();
						msg1.what = HANDLER_PRIZE_RESULT;
						msg1.obj = prize.getName();
						msg1.arg1 = prize.getSpoil();
						handler.sendMessage(msg1);
						
					} else {
						// 抽奖失败
						if (msg.arg1 == -1) {
							showDialog("温馨提示", "活动已经结束");
						} else {
							//lotteryView.setAwards(msg.arg1, false);
							Toast.makeText(getApplicationContext(), "获取奖品失败", Toast.LENGTH_LONG).show();
						}
					}
				break;
			default:
				break;
			}
		};
	};

	private void showDialog(String title, String msg) {
		ConfirmDialog dialog = new ConfirmDialog(this);
		dialog.setCustomTitle(title);
		dialog.setMessage(msg);
		dialog.setConfirm("去逛逛");
		dialog.setCancel("再看看");
		dialog.setClickListener(new DialogClickListener() {

			@Override
			public void onConfirmClickListener() {
				finish();
			}

			@Override
			public void onCancelClickListener() {

			}
		});
		dialog.show();
	}
	
	private void showPrizeDialog(String title, String msg) {
		isSuccess = false;
		SingleConfirmDialog dialog = new SingleConfirmDialog(this);
		dialog.setCustomTitle(title);
		dialog.setMessage(msg);
		dialog.setConfirm("确定");
		dialog.show();
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == ab_back) {
			finish();
		}
		//查看活动说明
		else if (arg0 == btn_info) {
			Intent intent = new Intent(this, ShakeAboutActivity.class);
			if (club.getContent() == null) {
				club.setContent("");
			}

			if (club.getPrizeSet() == null) {
				club.setPrizeSet("");
			}
			intent.putExtra(ShakeAboutActivity.INTENT_KEY_CONTENT, club.getContent());
			intent.putExtra(ShakeAboutActivity.INTENT_LEY_ABOUT, club.getPrizeSet());
			intent.putExtra(ShakeActivity.INTENT_KEY_TITLE, getIntent().getStringExtra(INTENT_KEY_TITLE));
			startActivity(intent);
			finish();
		}
		
	}
}
