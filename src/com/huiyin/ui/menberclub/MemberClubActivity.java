package com.huiyin.ui.menberclub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ExchangeResultBean;
import com.huiyin.bean.VIPInfoBean;
import com.huiyin.ui.club.ClubActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.SpringProgressView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 会员俱乐部
 * @author 高能
 *
 * @todo TODO
 *
 * @date 2015-7-22
 */
public class MemberClubActivity extends BaseActivity implements
		OnClickListener, ClubItemFragment.OnFragmentInteractionListener {

	private com.huiyin.wight.CircleImageView menber_icon;
	private TextView tv_back;
	private TextView menber_level;
	private TextView menber_name;
	private TextView rest_points;
	private TextView upgrade_tip;
	private TextView chou_jiang;
	private FragmentManager fragmentManager;
	private com.huiyin.wight.indicator.TabPageIndicator indicator;
	private ViewPager pager;
	private ImageView menber_level_logo;
	private VIPInfoBean mVIPInfoBean;
	private SpringProgressView member_rank_level_bar;
	private List<String> TITLES;
	private List<Integer> Ids;
	private RelativeLayout view_content;
	private ListView my_scroll_list;
	private Timer mTimer;
	private MyTimerTask mTimerTask;
    private MyScrollAdapter mAdapter;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menber_club);
		init();
		setListener();
	}

	private void setListener() {
		tv_back.setOnClickListener(this);
		chou_jiang.setOnClickListener(this);
	}

	public void init() {
		fragmentManager = this.getSupportFragmentManager();
		findViewById(R.id.right_ib).setVisibility(View.GONE);
		tv_back = (TextView) findViewById(R.id.left_ib);
		TextView mTv_title = (TextView) findViewById(R.id.middle_title_tv);
		menber_icon = (com.huiyin.wight.CircleImageView) findViewById(R.id.menber_icon);
		menber_level = (TextView) findViewById(R.id.menber_level);
		menber_level_logo = (ImageView) findViewById(R.id.menber_level_logo);
		menber_name = (TextView) findViewById(R.id.tv_menber_name);
		rest_points = (TextView) findViewById(R.id.tv_rest_points);
		upgrade_tip = (TextView) findViewById(R.id.tv_next_level_tip);
		chou_jiang = (TextView) findViewById(R.id.button_chouj);
		indicator = (com.huiyin.wight.indicator.TabPageIndicator) findViewById(R.id.indicator);
		pager = (ViewPager) findViewById(R.id.pager);
		member_rank_level_bar = (SpringProgressView) findViewById(R.id.member_rank_level_bar);
		view_content = (RelativeLayout) findViewById(R.id.view_content);
		my_scroll_list = (ListView) findViewById(R.id.my_scroll_list);
		mTv_title.setText("会员俱乐部");
		mTimer = new Timer();
		getVIPInfo();
	}

	public void setData() {
		ImageManager.LoadWithServer(AppContext.mUserInfo.img, menber_icon,new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.icon_head)
                .showImageForEmptyUri(R.drawable.icon_head)
                .showImageOnFail(R.drawable.icon_head)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build());
		ImageManager.LoadWithServer(AppContext.mUserInfo.levelLogo,
				menber_level_logo);
		menber_level.setText(AppContext.mUserInfo.level);
		menber_name.setText(AppContext.mUserInfo.getUserName());
		rest_points.setText(Html.fromHtml(String
				.format(getResources().getString(R.string.rest_points),
						AppContext.mUserInfo.integral)));
		upgrade_tip.setText(Html.fromHtml(String.format(getResources()
				.getString(R.string.premote_tip), mVIPInfoBean
				.getNEXT_GROWTH_NEED(), mVIPInfoBean.getNEXT_GROWTH_NAME())));
		setRankbar();
		setTitlesAndIds();
		view_content.setVisibility(View.VISIBLE);
		MyPagerAdapter mAdapter = new MyPagerAdapter(fragmentManager);
		pager.setAdapter(mAdapter);
		indicator.setViewPager(pager);
		setMsgRepeat(mVIPInfoBean.getRecordData());
	}

	/**
	 * 设置底部消息轮播
	 */
	private void setMsgRepeat(List<VIPInfoBean.RecordDataEntity> entityList) {
        if (mAdapter==null){
            mAdapter = new MyScrollAdapter(this, entityList);
            my_scroll_list.setAdapter(mAdapter);
            my_scroll_list.setFocusable(false);
            my_scroll_list.setEnabled(false);
            my_scroll_list.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(final AbsListView absListView, final int scrollState) {
                    if (scrollState == SCROLL_STATE_IDLE) {

                        // Fix for scrolling bug
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                my_scroll_list.setSelection(index);
                            }
                        });
                    }
                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i2, int i3) {

                }
            });
        }else{
            mAdapter.setEntities(entityList);
            mAdapter.notifyDataSetChanged();
        }
		if (mTimerTask != null) {
			mTimerTask.cancel(); // 将原任务从队列中移除
		}
        if (entityList!=null&&entityList.size()>0) {
            mTimerTask = new MyTimerTask();
            mTimer.schedule(mTimerTask, 2000, 2000);
        }
    }
    private int index = 0;

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            index++;
            if (index > my_scroll_list.getCount()) {
                index = 0;
            }
            my_scroll_list.smoothScrollToPositionFromTop(index, 0);
        }
    }
	/**
	 * 商品分类
	 */
	private void setTitlesAndIds() {
		TITLES = new ArrayList<String>();
		TITLES.add("全部");
		TITLES.add("乐虎券");
		Ids = new ArrayList<Integer>();
		Ids.add(-2);
		Ids.add(-1);
		for (int i = 0; i < mVIPInfoBean.getCategoryData().size(); i++) {
			TITLES.add(mVIPInfoBean.getCategoryData().get(i).getNAME());
			Ids.add(mVIPInfoBean.getCategoryData().get(i).getID());
		}
	}

	/**
	 * 设置积分进度条
	 */
	private void setRankbar() {
		member_rank_level_bar.initView(MemberClubActivity.this,
                R.color.bluesky, R.color.bluesky, R.color.white);
		member_rank_level_bar.reset();
		member_rank_level_bar.setDuringTime(1000);
		member_rank_level_bar.setMaxCount(mVIPInfoBean.getNEXT_GROWTH_VALUE());
		member_rank_level_bar.setCurrentCount(AppContext.mUserInfo.levelValue=mVIPInfoBean.getGROWTH_VALUE());
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.left_ib:
			finish();
			break;
		case R.id.button_chouj:
			Intent intent = new Intent(this, ClubActivity.class);
			intent.putExtra(ClubActivity.INTENT_KEY_TITLE, "天天有奖");
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void getVIPInfo() {
		if (AppContext.userId != null) {
			RequstClient.NClub(AppContext.userId, new CustomResponseHandler(
					this) {
				@Override
				public void onSuccess(String content) {
					super.onSuccess(content);
					if (LogUtil.isDebug)
						Logger.json(content);
					mVIPInfoBean = new Gson().fromJson(content,
							VIPInfoBean.class);
					if (mVIPInfoBean.getType() != 1) {
						UIHelper.showToast(mVIPInfoBean.getMsg());
						return;
					}
					setData();
				}
			});
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==ClubItemFragment.REQUEST&&resultCode== Activity.RESULT_OK) {
            if (data!=null&&data.getSerializableExtra("result")!=null){
                onFragmentInteraction((ExchangeResultBean)data.getSerializableExtra("result"));
            }
        }
    }

    @Override
	public void onFragmentInteraction(ExchangeResultBean mResultBean) {
		if (mResultBean != null) {
			AppContext.mUserInfo.integral = mResultBean.getIntegral() + "";
			AppContext.mUserInfo.integral = mResultBean.getIntegral() + "";
			rest_points.setText(Html.fromHtml(String.format(getResources()
					.getString(R.string.rest_points),
					AppContext.mUserInfo.integral)));
			if (mResultBean.getRecordData() != null) {
				setMsgRepeat(mResultBean.getRecordData());
			}
		}
	}



	private class MyPagerAdapter extends FragmentStatePagerAdapter {

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			return ClubItemFragment.newInstance(Ids.get(arg0) + "");
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return TITLES.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES.get(position % TITLES.size());
		}
	}
}
