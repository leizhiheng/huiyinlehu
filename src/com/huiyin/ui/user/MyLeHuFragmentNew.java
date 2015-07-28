package com.huiyin.ui.user;

import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraFragment;
import com.huiyin.bean.GoodLikeBean;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.menberclub.MemberClubActivity;
import com.huiyin.ui.servicecard.BindServiceCard;
import com.huiyin.ui.servicecard.ServiceCardActivity;
import com.huiyin.ui.user.asset.LehuRedPacketActivity;
import com.huiyin.ui.user.order.AllOrderActivity;
import com.huiyin.ui.user.order.BespeakReturnActivity;
import com.huiyin.ui.user.wallet.MyWalletActivity;
import com.huiyin.utils.DateUtil;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.view.ViewHolderMyLehu;

/**
 * 我的乐虎
 * @author 刘远祺
 *
 */
public class MyLeHuFragmentNew extends BaseCameraFragment implements OnClickListener {

	public static final String TAG = "MyLeHuFragment";
	
	/**登录-Request_Code**/
	public static final int REQUEST_CODE_LOGIN = 1;
	
	
	/**所有控件(只负责初始化控件，和显示数据)**/
	private ViewHolderMyLehu viewHolder;

	/**当前显示的用户信息**/
	private UserInfo currentUser;
	
	/**当前显示的猜你喜欢**/
	private List<GoodLikeBean> curMaybeLike;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.layout_mylehu, null);
		return layoutView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// 初始化控件
		initView();

		// 初始化数据
		showUserInfoData();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		//刷新用户信息
		loadUserInfo();
		
		//加载猜你喜欢
		loadMayBeLikeByUser();
	}
	
	/**
	 * 显示用户基本信息
	 */
	private void showUserInfoData() {
		
		//如果有数据则显示数据
		UserInfo userInfo = AppContext.mUserInfo;
		if(null != userInfo && null != viewHolder){
			viewHolder.showData(userInfo);
			
			//记住当前显示的用户信息
			currentUser = userInfo;
		}else{
			
			//未登录状态下不显示猜你喜欢
			viewHolder.guessLikeLayout.setVisibility(View.GONE);
			
			//显示未登录的数据默认数据
			viewHolder.clearShowData();
			
			//未登录,清空当前记住的user
			currentUser = null;
			curMaybeLike = null;
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		//初始化控件
		viewHolder = new ViewHolderMyLehu(layoutView, mContext);
		viewHolder.initView();
		viewHolder.setOnClick(this);
	}

	/**
	 * 加载用户信息
	 */
	private void loadUserInfo(){
		
		Log.i("UserInfo", "MyLehuFragment loadUserInfo is Null" + (null == AppContext.userId));
		
		//用户已登录，则重新获取用户信息
		if(appIsLogin()){
			
			//显示已登录模块layout
			viewHolder.showLoginView(true);
			
			//显示用户信息(程序第一次进入个人主页，或注销再登录)
			if(null == currentUser){
				showUserInfoData();
			}
			
			//获取我的信息
			RequstClient.myLHInfoLoad(new CustomResponseHandler(mContext, false) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, String content) {
					super.onSuccess(statusCode, headers, content);
					if(!appIsLogin()){
						//用户已经注销，数据请求又回来了，这里不做处理，以解决用户注销失败的问题
						return;
					}
					
					//异常消息显示
					if(JSONParseUtils.isErrorJSONResult(content)){
						String msg = JSONParseUtils.getString(content, "msg");
						showMyToast(msg);
						return;
					}
					
					//解析用户信息
                    AppContext.curTime = JSONParseUtils.getString(content, "curTime");//登录之后系统当前时间
					String userJson = JSONParseUtils.getJSONObject(content, "user");
					UserInfo mUserInfo = UserInfo.explainJson(userJson, mContext);
					AppContext.mUserInfo = mUserInfo;
					AppContext.userId = mUserInfo.userId;
					
					//显示用户信息
					showUserInfoData();
				}

			});
			
			
		}else{
			
			//显示未登录模块layout
			viewHolder.showLoginView(false);
			
			//显示用户信息
			showUserInfoData();
		}
		
	}

	/**
	 * 获取猜你喜欢商品
	 */
	private void loadMayBeLikeByUser(){
		if(appIsLogin() && null == curMaybeLike){
			
			//获取我的信息
			RequstClient.mayBeLikeByUser(new CustomResponseHandler(mContext, false) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, String content) {
					super.onSuccess(statusCode, headers, content);
					if(!appIsLogin()){
						//用户已经注销，数据请求又回来了，这里不做处理，以解决用户注销失败的问题
						return;
					}
					
					//异常消息显示
					if(JSONParseUtils.isErrorJSONResult(content)){
						String msg = JSONParseUtils.getString(content, "msg");
						showMyToast(msg);
						return;
					}
					
					//解析猜你喜欢数据
					List<GoodLikeBean> maybeLike = JSONParseUtils.parseMaybeLike(content);//登录之后系统当前时间
					
					if(null != maybeLike && maybeLike.size() > 0){
						
						curMaybeLike = maybeLike;
						
						//显示猜你喜欢数据
						viewHolder.showLikeList(maybeLike);
					}
					
				}

			});
		}
	}
	
	
	/**
	 * 签到
	 */
	private void solveComplain(){
		
		//未签到
		if(0 == AppContext.mUserInfo.getFlagSign()){
			RequstClient.sign(new CustomResponseHandler(mContext, true) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, String content) {
					super.onSuccess(statusCode, headers, content);
					
					//异常消息显示
					if(JSONParseUtils.isErrorJSONResult(content)){
						String msg = JSONParseUtils.getString(content, "msg");
						showMyToast(msg);
						return;
					}
					
					//签到时间,最新积分
					String curTime = JSONParseUtils.getString(content, "curTime");
					int integral = JSONParseUtils.getInt(content, "INTEGRAL");
					
					
					try{
						String newTime = DateUtil.convertBirthday(curTime);
						curTime = newTime;
					}catch(Exception e){
						e.printStackTrace();
					}
					
					//显示已签到
					showMyToast("已签到");
					AppContext.mUserInfo.FLAG_SIGN = "1";
					AppContext.mUserInfo.addSignDay(1);
					AppContext.mUserInfo.SIGNDAY_TIME = curTime;
					
					//文本赋值
					viewHolder.signInTextView.setText("已签到");
					viewHolder.signInCountView.setText(getString(R.string.sign_count, AppContext.mUserInfo.SIGNDAY));
					viewHolder.signInTimeView.setText(AppContext.mUserInfo.SIGNDAY_TIME);
					
					//刷新积分
					if(integral > 0){
						AppContext.mUserInfo.integral = integral+"";
						viewHolder.showIntegral(integral+"");
					}
				}
				
			});
		}else{
			showMyToast("您已签到");
		}
	}
	
	
	@Override
	public void onClick(View v) {

		//排除登录，注册
		boolean isLoginView = false;
		if(v.getId() == R.id.lehu_login_id || v.getId() ==R.id.lehu_register_id){
			isLoginView = true;
		}
		
		//判断用户是否已登录
		if(!isLoginView && !appIsLogin(true)){
			return;
		}
		
		
		switch (v.getId()) {
		case R.id.lehu_login_id:
		
			// 登录
			Intent intentLogin = new Intent();
			intentLogin.setClass(getActivity(), LoginActivity.class);
			startActivityForResult(intentLogin, REQUEST_CODE_LOGIN);
//			LoginTest test = new LoginTest(mContext);
//			test.doLogin("18589055419", "123456");
			
			break;
		case R.id.lehu_register_id:
			
			//注册
			Intent intentRegist = new Intent();
			intentRegist.setClass(getActivity(), RegisterActivity.class);
			startActivityForResult(intentRegist, REQUEST_CODE_LOGIN);
			
			break;
		case R.id.user_setting:
			
			//设置
			Intent set_i = new Intent();
			set_i.setClass(getActivity(), SettingActivity.class);
			startActivity(set_i);
			
			break;
		case R.id.lehu_user_lastest_login:
			
			//登录日志
            Intent intent=new Intent(mContext,LoginLogActivity.class);
            mContext.startActivity(intent);
			break;
			
		case R.id.new_msg_layout:
			
			//消息
			Intent m_intent = new Intent();
			m_intent.setClass(getActivity(), MyMessageActivity.class);
			startActivity(m_intent);
			
			break;
			
		case R.id.lehu_login_head:
		case R.id.tv_user_adress_manage:
			
			// 头像
			// 账户管理
			Intent am_intent = new Intent(mContext, AccountManageActivityNew.class);
			startActivity(am_intent);
			
			break;

		case R.id.tv_sign:
			
			// 签到
			solveComplain();
			
			break;
			
		case R.id.tv_my_attention:
			
			// 我的关注
			Intent collect = new Intent();
            collect.setClass(mContext,CollectActivity.class);
            startActivity(collect);
			
			break;
		case R.id.tv_browse:
			
			//浏览记录
            Intent intentHistory = new Intent();
            intentHistory.setClass(mContext, MyScanRecorderActivity.class);
            startActivity(intentHistory);
			
			break;

		case R.id.lehu_all_order:
			
			// 全部订单
			Intent intentAllOrder = new Intent();
			intentAllOrder.setClass(getActivity(), AllOrderActivity.class);
			intentAllOrder.putExtra(AllOrderActivity.ORDER_TYPE, 1);
			startActivity(intentAllOrder);
			
			break;

		case R.id.waitpay_orderlist:
			
			// 待付款
			Intent intent1 = new Intent();
            intent1.setClass(getActivity(), AllOrderActivity.class);
            intent1.putExtra(AllOrderActivity.ORDER_TYPE, 2);
			startActivity(intent1);
			
			break;
		case R.id.waitreceive_orderlist:
			//待收货
			Intent wr_i = new Intent();
			wr_i.setClass(getActivity(), AllOrderActivity.class);
			wr_i.putExtra(AllOrderActivity.ORDER_TYPE, 3);
			startActivity(wr_i);

//            Intent wr_i = new Intent();
//            wr_i.setClass(getActivity(), LookForLogisticsActivity.class);
//            wr_i.putExtra(LookForLogisticsActivity.ORDERID, "16157");
//            wr_i.putExtra(LookForLogisticsActivity.ORDERNO, "123456789NO");
//            wr_i.putExtra(LookForLogisticsActivity.STATU, "待收货");
//            startActivity(wr_i);
			
			break;
		case R.id.waitcomment_orderlist:
			
			//待评价
			Intent wo_i = new Intent();
            wo_i.setClass(getActivity(), AllOrderActivity.class);
            wo_i.putExtra(AllOrderActivity.ORDER_TYPE, 4);
			startActivity(wo_i);
			
			break;
		case R.id.backgood_orderlist:
			
			//预约/退换
			Intent bg_i = new Intent();
            bg_i.setClass(getActivity(), BespeakReturnActivity.class);
			startActivity(bg_i);
			
			break;

		case R.id.layout_assets:
			
			// 我的资产
			UserInfo userInfo = AppContext.mUserInfo;
            Intent intentWallet = new Intent();
            intentWallet.setClass(mContext, MyWalletActivity.class);
            intentWallet.putExtra(MyWalletActivity.REDPACK, userInfo.getBonus()+"");
            intentWallet.putExtra(MyWalletActivity.QUAN, userInfo.myCouponAll);
            intentWallet.putExtra(MyWalletActivity.INTEGRAL, userInfo.integral);
            intentWallet.putExtra(MyWalletActivity.CARD, userInfo.getBalance()+"");
            startActivity(intentWallet);
			
			break;
		case R.id.tv_redpackage:
			
			//红包
			Intent hongbao_intent = new Intent();
			hongbao_intent.setClass(getActivity(), MyHongbaoActivity.class);
			startActivity(hongbao_intent);
			
			break;
		case R.id.tv_integral:
			
			//积分
			Intent jf_intent = new Intent();
			jf_intent.setClass(getActivity(), MyJiFenActivity.class);
			startActivity(jf_intent);
			
			break;
		case R.id.tv_red:
			
			//乐虎券
			Intent intentRed = new Intent();
			intentRed.setClass(mContext, LehuRedPacketActivity.class);
			startActivity(intentRed);
			
			
			break;
		case R.id.tv_convenience:
			
			//便民生活服务卡
			Intent fuwu_intent = new Intent();
            if (AppContext.mUserInfo.hasBoundServiceCard()) {
                fuwu_intent.setClass(mContext, ServiceCardActivity.class);
            } else {
                fuwu_intent.setClass(mContext, BindServiceCard.class);
            }
            startActivity(fuwu_intent);
			
			break;

		case R.id.layout_my_service:
			
			// 我的服务
            Intent myservice=new Intent(mContext,MyServiceActivity.class);
            mContext.startActivity(myservice);

			break;
		case R.id.layout_vip:
			
			//会员乐虎
			Intent club_intent = new Intent(getActivity(),MemberClubActivity.class);
//			Intent club_intent = new Intent(getActivity(),BoundPhoneModifyActivity.class);
			startActivity(club_intent);
			
			break;
		}
	}
	
	public void pageSelected(int selectedId) {
		if (selectedId == MainActivity.MY_LEHU_INDEX) {
			onResume();
		}
	}
}
