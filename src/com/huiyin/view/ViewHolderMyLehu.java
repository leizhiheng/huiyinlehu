package com.huiyin.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.adapter.FragmentViewPagerAdapter;
import com.huiyin.api.URLs;
import com.huiyin.bean.GoodLikeBean;
import com.huiyin.bean.GoodLikeBeanList;
import com.huiyin.ui.user.GoodsLikeFragment;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.ResourceUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 我的乐虎-所有控件
 * @author 刘远祺
 *
 */
public class ViewHolderMyLehu {

	/**上下文**/
	public Activity context;
	
	/**界面的布局**/
	public View layoutView;
	
	
	
	/** 最后登录时间 **/
	public TextView lastLoginTimeTv;
	
	/**有新消息提示(右上角红色小圆点)**/
	public View newMsgTipTv;
	
	
	
	/**登录头像**/
	public ImageView loginHeadImageView;
	
	/** 用户手机/用户名 **/
	public TextView phoneTv;

	/**会员等级图标**/
	public ImageView levelIconIv;
	
	/**用户等级**/
	public TextView levelTextView;
	
	/**账户管理，收货地址管理**/
	public TextView accountManagerTextView;
	
	
	/**签到/未签到**/
	public TextView signInTextView;
	
	/**已签到xx次**/
	public TextView signInCountView;
	
	/**签到时间**/
	public TextView signInTimeView;
	
	
	/**我的关注数量**/
	public TextView attentionCountTextView;
	
	/**浏览记录数量**/
	public TextView browseCountTextView;
	
	
	/**待付款-数量**/
	public TextView waitPayCountTv;
	
	/**待收货-数量**/
	public TextView waitReceiveCountTv;
	
	/**待评价-数量**/
	public TextView waitCommentCountTv;
	
	/**预约/退换-数量**/
	public TextView bespeakCountTv;
	
	
	
	/**红包**/
	public TextView bonusTv;
	
	/**积分**/
	public TextView scoreTv;
	
	/**乐虎红包券**/
	public TextView lehuQuanTv;
	
	/**便民生活服务卡**/
	public TextView serviceCardTv;
	
	
	/**我的服务**/
	public TextView myServiceTv;

	/**会员俱乐部**/
	public TextView clubTv;
	
	
	/**猜你喜欢布局**/
	public View guessLikeLayout;
	
	/**猜你喜欢ViewPager**/
	public ViewPager guessLikeViewPager;
	
	
	public ViewHolderMyLehu(View view, Activity context){
		this.layoutView = view;
		this.context = context;
	}
	
	/**
	 * 初始化控件
	 */
	public void initView(){
		
		lastLoginTimeTv = (TextView)findViewById(R.id.lehu_user_lastest_login);
		newMsgTipTv = findViewById(R.id.new_msg_tip_tv);
		
		loginHeadImageView = (ImageView)findViewById(R.id.lehu_login_head);
		levelIconIv = (ImageView)findViewById(R.id.lehu_user_level_imgview);
		levelTextView = (TextView)findViewById(R.id.lehu_user_level);
		phoneTv = (TextView)findViewById(R.id.lehu_user_phone);
		accountManagerTextView = (TextView)findViewById(R.id.tv_user_adress_manage);
		
		signInTextView = (TextView)findViewById(R.id.tv_sign);
		signInCountView = (TextView)findViewById(R.id.tv_sign_count);
		signInTimeView = (TextView)findViewById(R.id.tv_sign_time);
		
		attentionCountTextView = (TextView)findViewById(R.id.tv_my_attention);
		browseCountTextView = (TextView)findViewById(R.id.tv_browse);
		
		waitPayCountTv = (TextView)findViewById(R.id.waitpay_ordernum);
		waitReceiveCountTv = (TextView)findViewById(R.id.waitreceive_ordernum);
		waitCommentCountTv = (TextView)findViewById(R.id.waitcomment_ordernum);
		bespeakCountTv = (TextView)findViewById(R.id.backgood_ordernum);
		
		
		bonusTv = (TextView)findViewById(R.id.tv_redpackage);
		scoreTv = (TextView)findViewById(R.id.tv_integral);
		lehuQuanTv = (TextView)findViewById(R.id.tv_red);
		serviceCardTv = (TextView)findViewById(R.id.tv_convenience);
		
		myServiceTv = (TextView)findViewById(R.id.complaint_textview);
		clubTv = (TextView)findViewById(R.id.club_textview);
		
		guessLikeLayout = findViewById(R.id.brower_layout);
		guessLikeViewPager = (ViewPager)findViewById(R.id.guess_you_like_pager);
		
	}
	
	/**查找控件**/
	public View findViewById(int id){
		return layoutView.findViewById(id);
	}

	/**
	 * 显示登录布局
	 */
	public void showLoginView(boolean showLoginView){
		
		//未登录布局,已登录布局
		View unLoginLayout = findViewById(R.id.unlogin_part_layout);
		View loginLayout = findViewById(R.id.login_part1_layout);
		
		if(showLoginView){
			loginLayout.setVisibility(View.VISIBLE);
			unLoginLayout.setVisibility(View.GONE);
		}else{
			loginLayout.setVisibility(View.GONE);
			unLoginLayout.setVisibility(View.VISIBLE);
		}
		
	}
	
	/**
	 * 显示用户信息
	 * @param userInfo
	 */
	public void showData(UserInfo userInfo){

		//最后登录时间，系统消息提示
		lastLoginTimeTv.setText("最后登录："+userInfo.getLastDate());
		newMsgTipTv.setVisibility(userInfo.hasSystemMessage() ? View.VISIBLE : View.GONE);

		//头像,用户等级头像昵称(手机号码),用户等级
		DisplayImageOptions options = null;
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true)
				.showStubImage(R.drawable.icon_head)
				.showImageForEmptyUri(R.drawable.icon_head)
				.showImageOnFail(R.drawable.icon_head)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		ImageManager.Load(URLs.IMAGE_URL+ userInfo.img, loginHeadImageView, options);
		ImageManager.Load(URLs.IMAGE_URL+ userInfo.levelLogo, levelIconIv);
		phoneTv.setText(userInfo.getUserName());
		levelTextView.setText(userInfo.getLevel());
		//accountManagerTextView.
		
		//签到，已签到次数，签到时间
		signInTextView.setText((0==userInfo.getFlagSign())?"未签到":"已签到");
		signInCountView.setText(context.getString(R.string.sign_count, userInfo.SIGNDAY));
		signInTimeView.setText(userInfo.SIGNDAY_TIME);
		
		//我的关注，浏览记录
		attentionCountTextView.setText(context.getString(R.string.attention_count, userInfo.COUNT_FOCUS));
		browseCountTextView.setText(context.getString(R.string.history_count, userInfo.COUNT_HISTORY));
		
		//待付款，待收货，待评价，预约/退换
		setVisibleByCount(userInfo.countWaitPay, waitPayCountTv);
		setVisibleByCount(userInfo.countDelivery, waitReceiveCountTv);
		setVisibleByCount(userInfo.commentsStatus, waitCommentCountTv);
		setVisibleByCount(userInfo.countChangeReplace, bespeakCountTv);
		
		//积分，乐虎券，便民生活服务卡
		bonusTv.setText(getText("¥"+userInfo.getBonus()+"", "红包"));
		scoreTv.setText(getText(userInfo.getIntegral(), "积分"));
		lehuQuanTv.setText(getText(userInfo.getMyCouponAll(), "乐虎券"));
		serviceCardTv.setText(getText("¥"+userInfo.getBalance()+"", "便民服务卡"));
		
		//猜你喜欢
		//showLikeList(userInfo.maybeLike);
	}
	
	/**
	 * 显示积分
	 * @param integral
	 */
	public void showIntegral(String integral){
		scoreTv.setText(getText(integral, "积分"));
	}
	
	
	/**
	 * 给textview赋值
	 * @param count
	 * @param tv
	 */
	private void setVisibleByCount(String count, TextView tv){
		if(null == tv || null == count){
			return;
		}
		
		try{
			int num = Integer.parseInt(count);
			int vis = num > 0 ? View.VISIBLE : View.GONE;
			tv.setText(count);
			tv.setVisibility(vis);
			
		}catch(Exception e){
			tv.setVisibility(View.GONE);
		}
	}
	
	private android.text.Spanned getText(String colorText, String appendText){
		String text = ResourceUtils.changeStringColor("#C3C3C3", colorText) + "<br />" +appendText; 
		return Html.fromHtml(text);
	}
	
	/**
	 * 用户从登录状态-> 注销状态,需要把显示的数据清空
	 */
	public void clearShowData() {

		// 待付款，待收货，待评价，预约/退换
		setVisibleByCount("0", waitPayCountTv);
		setVisibleByCount("0", waitReceiveCountTv);
		setVisibleByCount("0", waitCommentCountTv);
		setVisibleByCount("0", bespeakCountTv);

		// 积分，乐虎券，便民生活服务卡
		bonusTv.setText(getText("¥0.00", "红包"));
		scoreTv.setText(getText("0", "积分"));
		lehuQuanTv.setText(getText("0", "乐虎券"));
		serviceCardTv.setText(getText("¥0.00", "便民服务卡"));

		// 猜你喜欢
		showLikeList(null);
	}
	
	/**
	 * 显示猜你喜欢数据
	 */
	public void showLikeList(List<GoodLikeBean> maybeLike){
		
		//没有数据
		if(null == maybeLike || 0 == maybeLike.size()){
			guessLikeLayout.setVisibility(View.GONE);
			return;
		}else{
			guessLikeLayout.setVisibility(View.VISIBLE);
		}
		
		
		List<GoodLikeBean> dataList = new ArrayList<GoodLikeBean>();
		List<Fragment> listFragments = new ArrayList<Fragment>();
		
		for (int i = 0; i < maybeLike.size(); i++) {
			int yu = i%6;
			if(((yu == 0) || i==(maybeLike.size()-1)) && (i!=0)){
				//6的倍数 || 是最后一个  && 不是第一个 （每六条数据显示一屏）
				GoodsLikeFragment fragment = GoodsLikeFragment.getInstance(new GoodLikeBeanList(dataList));
				listFragments.add(fragment);
				
				dataList = new ArrayList<GoodLikeBean>();
			}
			dataList.add(maybeLike.get(i));
		}
		
		FragmentManager fm = ((FragmentActivity)context).getSupportFragmentManager();
		FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(fm, listFragments);
		guessLikeViewPager.setAdapter(adapter);
	}
	
	
	public void setOnClick(OnClickListener onClick){
		
		//设置 ,消息,最后登录时间(登录日志)
		findViewById(R.id.user_setting).setOnClickListener(onClick);
		findViewById(R.id.new_msg_layout).setOnClickListener(onClick);
		findViewById(R.id.lehu_user_lastest_login).setOnClickListener(onClick);
		
		//账户，收货地址管理
		findViewById(R.id.tv_user_adress_manage).setOnClickListener(onClick);
		
		//登录/注册按钮
		findViewById(R.id.lehu_login_id).setOnClickListener(onClick);
		findViewById(R.id.lehu_register_id).setOnClickListener(onClick);
		
		//头像
		findViewById(R.id.lehu_login_head).setOnClickListener(onClick);
		
		//签到
		findViewById(R.id.tv_sign).setOnClickListener(onClick);
		
		//我的关注,浏览记录
		findViewById(R.id.tv_my_attention).setOnClickListener(onClick);
		findViewById(R.id.tv_browse).setOnClickListener(onClick);
		
		//全部订单
		findViewById(R.id.lehu_all_order).setOnClickListener(onClick);
		
		//待付款，待收货，待评价，预约/退换
		findViewById(R.id.waitpay_orderlist).setOnClickListener(onClick);
		findViewById(R.id.waitreceive_orderlist).setOnClickListener(onClick);
		findViewById(R.id.waitcomment_orderlist).setOnClickListener(onClick);
		findViewById(R.id.backgood_orderlist).setOnClickListener(onClick);
		
		//红包，积分，乐虎券，便民生活服务卡
		findViewById(R.id.tv_redpackage).setOnClickListener(onClick);
		findViewById(R.id.layout_assets).setOnClickListener(onClick);
		findViewById(R.id.tv_integral).setOnClickListener(onClick);
		findViewById(R.id.tv_red).setOnClickListener(onClick);
		findViewById(R.id.tv_convenience).setOnClickListener(onClick);
		
		//我的服务，会员乐虎
		findViewById(R.id.layout_my_service).setOnClickListener(onClick);
		findViewById(R.id.layout_vip).setOnClickListener(onClick);
	}
}
