package com.huiyin;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.huiyin.bean.GoodLikeBean;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.PreferenceUtil;

import java.io.Serializable;
import java.util.List;

public class UserInfo implements Serializable {

	public static final String TAG = "UserInfo";
	private static final long serialVersionUID = 1L;
	
	public String myCouponOut; 			// 即将过期的优惠券									不用了
	public String level; 				// 用户等级
	public String levelLogo; 			// 用户等级图标
	public int levelValue; 				// 用户等级成长值
	public String countWaitPay; 		// 待支付数量
	public String countDelivery; 		// 待收货数量
	public String commentsStatus; 		// 待评价数量
	public String countChangeReplace; 	// 退换货中数量
	public String userId; 				// 用户id
	public String myCouponAll; 			// 所有的优惠券									
	public String img; 					// 用户头像
	public String lastDate;				// 最后登录时间
	public String loginAddress;         // 最后登录城市
	public String pwdSfae;              // 密码强度
	public String userName;				// 用户名
	public String integral;				// 积分
	public String bonus;				// 红包											
	public String systemMessage;		// 系统消息
	public String createDate;			// 注册时间										
	public String phone;				// 电话号码
	public String bdStatus;				// 便民生活卡是否绑定 绑定状态，0：未绑定 1：绑定			//这个字段没有
	public String cardNum;				// 卡号											
	public String token = "";			// 令牌											
	public String balance;				// 便民服务卡金额									//这个字段没有 double
	public String real_phone;			// 电话号码(带星号如 ：159******152)
	public String email;				// 邮箱
	public String emailStatus;			// 邮箱状态(1已验证，0未验证)
	public String sex;					// 性别 (0男，1女)-新增字段
	public String birthday;				// 生日
	public String curTime;				// 系统当前时间
	
	public String SHOPPING_CAR;			// 购物车数量
	public String SIGNDAY;				// 签到次数 
	public String FLAG_SIGN;			// 是否已签到：1已签到，0未签到
	public String SIGNDAY_TIME;			// 最后签到时间
	public String COUNT_FOCUS;			// 关注商品数量
	public String COUNT_HISTORY;		// 浏览历史记录
	//public List<GoodLikeBean> maybeLike;// 猜你喜欢
	
	public String getBirthDay(){
		if(TextUtils.isEmpty(birthday)){
			return "";
		}
		return birthday;
	}
	
	public int getSex(){
		try{
			return Integer.parseInt(sex);
		}catch(Exception e){
			return 0;
		}
	}
	
	public String getBonus(){
		double value = 0;
		try{
			value = Double.parseDouble(bonus);
			return MathUtil.keep2decimal(value);
		}catch(Exception e){
			return "0.00";
		}
	}
	

	public void addSignDay(int dayNum){
		try{
			int day = Integer.parseInt(SIGNDAY);
			day += dayNum;
			SIGNDAY = (day+"");
		}catch(Exception e){
			SIGNDAY = dayNum+"";
		}
	}
	
	public int getFlagSign(){
		try{
			return Integer.parseInt(FLAG_SIGN);
		}catch(Exception e){
			return 0;
		}
	}
	
	public int getShoppingCart(){
		try{
			return Integer.parseInt(SHOPPING_CAR);
		}catch(Exception e){
			return 0;
		}
	}
	
	
	/**
	 * 是否有系统最新消息
	 * @return
	 */
	public boolean hasSystemMessage(){
		try{
			Integer count = Integer.parseInt(systemMessage);
			return count > 0;
		}catch(Exception e){
			return false;
		}
			
	}

	public String getBalance(){
		double value = 0;
		try{
			value = Double.parseDouble(balance);
			return MathUtil.keep2decimal(value);
		}catch(Exception e){
			return "0.00";
		}
	}
	
	public double getBalanceDouble(){
		double value = 0;
		try{
			value = Double.parseDouble(balance);
			return Double.parseDouble(MathUtil.keep2decimal(value));
		}catch(Exception e){
			return 0.00d;
		}
	}
	
	/**
	 * 便民生活卡是否绑定
	 * @return
	 */
	public int getBDStatus(){
		try{
			return Integer.parseInt(bdStatus);
		}catch(Exception e){
			return 0;
		}
	}
	
	/**
	 * 是否绑定便民生活服务卡
	 * @return
	 */
	public boolean hasBoundServiceCard(){
		return 1 == getBDStatus();
	}
	
	public void setBDStatus(int bdstatus){
		this.bdStatus = (bdStatus+"");
	}
	
	/**
	 * 获取用户名
	 * @return
	 */
	public String getUserName() {
		if(!TextUtils.isEmpty(userName)){
			return userName;
		}
		
		if(!TextUtils.isEmpty(phone)){
			return phone;
		}
		return "未设置昵称";
	}

	/**
	 * 获取是否签到文本
	 * @return
	 */
	public String getSignFlagText() {
		return null;
	}
	
	public static UserInfo explainJson(String json, Context context) {
		Gson gson = new Gson();
		try {
			UserInfo bean = gson.fromJson(json,UserInfo.class);
			
			//保存数据
			PreferenceUtil.getInstance(context).putString(PreferenceUtil.PRFERENCE_USER_INFO, json);
			
			return bean;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 从本地preference里面读取json再加载数据
	 * @param context
	 * @return
	 */
	public static UserInfo loadUserInfo(Context context){
		String userInfoJson = PreferenceUtil.getInstance(context).getString(PreferenceUtil.PRFERENCE_USER_INFO);
		LogUtil.d(TAG, "userInfo:" + userInfoJson);
		if(null != userInfoJson){
			return explainJson(userInfoJson, context);
		}
		return null;
	}
	
	/**
	 * 清空用户缓存信息
	 * @param context
	 */
	public static void clearUserInfo(Context context){
		PreferenceUtil.getInstance(context).removeKey(PreferenceUtil.PRFERENCE_USER_INFO);
	}

	public String getLevel() {
		if(TextUtils.isEmpty(level)){
			return "普通会员";
		}
		return level;
	}

	/**
	 * 获取最后登录时间
	 * @return
	 */
	public String getLastDate() {
		if(!TextUtils.isEmpty(lastDate)){
			return lastDate;
		}
		
		//没有最后登录时间，则返回注册时间,因为在第一次注册时候，没有最后登录时间，只有注册时间
		return createDate;
	}
	
	/**
	 * 是否绑定邮箱
	 * @return
	 */
	public boolean isBoundEmail(){
		try{
			return "1".equals(emailStatus);
		}catch(Exception e){
			return false;
		}
	}

	/**
	 * 获取积分
	 * @return
	 */
	public String getIntegral() {
		try{
			return Integer.parseInt(integral)+"";
		}catch(Exception e){
			return "0";
		}
	}

	/**
	 * 获取乐虎券
	 * @return
	 */
	public String getMyCouponAll() {
		try{
			return Integer.parseInt(myCouponAll)+"";
		}catch(Exception e){
			return "0";
		}
	}
}
