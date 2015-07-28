package com.huiyin.ui.user.asset;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.introduce.IntroduceActivity;
import com.huiyin.ui.user.WebViewActivity;

/**
 * 乐虎红包(券)
 * @author 刘远祺
 *
 */
public class LehuRedPacketActivity extends BaseActivity implements OnClickListener{

	/**未使用fragment**/
	private RedpacketUnuseFragment unuseFragment;
	
	/**待领取fragment**/
	private RedpacketUntakeFragment unTakeFragment;
	
	
	
	/**使用说明**/
	private Button useTipBtn;
	
	/**未使用**/
	private TextView unuseTv;
	
	/**待领取**/
	private TextView unTakeTv;
	
	/**标记刷新未使用乐虎券 true:切换时刷新，false:切换时不刷新**/
	private boolean tagRefreshUnuse;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lehu_redpacket);
		
		//初始化控件
		initView();
		
		//初始化数据
		initData();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView(){
		
		useTipBtn = (Button)findViewById(R.id.use_tip_button);
		unuseTv = (TextView)findViewById(R.id.unuse_tv);
		unTakeTv = (TextView)findViewById(R.id.untake_tv);
		
		useTipBtn.setOnClickListener(this);
		unuseTv.setOnClickListener(this);
		unTakeTv.setOnClickListener(this);
		
		//实例化fragment
		unuseFragment = new RedpacketUnuseFragment();
		unTakeFragment = new RedpacketUntakeFragment();
		
		//显示未使用的fragment
		showFragment(unuseFragment);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		
	}

	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.use_tip_button:
			
			//使用说明
			Intent intent = new Intent(mContext, IntroduceActivity.class);
			intent.putExtra("id", 182);
			startActivity(intent);
			
			break;
		case R.id.unuse_tv:
			
			//未使用
			showUnuseTab(true);
			showFragment(unuseFragment);
			
			//如果标记了刷新数据，则刷新数据(每领取券，回来都要刷新列表数据)
			if(tagRefreshUnuse){
				unuseFragment.refreshData();
				tagRefreshUnuse = false;
			}
			
			
			break;

		case R.id.untake_tv:
			
			//待待领取
			showUnuseTab(false);
			showFragment(unTakeFragment);
			
			break;
		}
	}
	
	/**
	 * 显示未使用选项卡
	 * @param showUnuse
	 */
	private void showUnuseTab(boolean showUnuse){
		
		if(showUnuse){
			unuseTv.setTextColor(getResources().getColor(R.color.red_color));
			unTakeTv.setTextColor(getResources().getColor(R.color.black));
		}else{
			unuseTv.setTextColor(getResources().getColor(R.color.black));
			unTakeTv.setTextColor(getResources().getColor(R.color.red_color));
		}
		
	}
	
	
	/**当前显示的fragment**/
	private Fragment currentFragment = null;
	
	/**
	 * 显示framgnet
	 * @param fragment
	 */
	private void showFragment(Fragment fragment){
		
		//非空
		if(null == fragment){
			return;
		}
		
		//已经显示
		if(fragment.isAdded() && !fragment.isHidden()){
			return;
		}
		
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transation = fragmentManager.beginTransaction();
		
		//隐藏当前显示的fragment
		if(null != currentFragment && currentFragment.isAdded()){
			transation.hide(currentFragment);
		}
		
		//显示传入的fragment
		if(!fragment.isAdded()){
			transation.add(R.id.fragment_framelayout, fragment);
			transation.commit();
		}else{
			
			transation.show(fragment);
			transation.commit();
		}
		
		//记住当前的fragment
		currentFragment = fragment;
	}
	
	/**
	 * 标记在切换未使用时，是否刷新列表数据
	 * @param refresh
	 */
	public void tagRefreshUnuse(boolean refresh){
		this.tagRefreshUnuse = refresh;
	}
}
