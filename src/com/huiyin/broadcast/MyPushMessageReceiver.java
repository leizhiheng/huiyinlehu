package com.huiyin.broadcast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.huiyin.AppContext;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.club.ClubActivity;
import com.huiyin.ui.flash.FlashPrefectureActivity;
import com.huiyin.ui.home.Logistics.LogisticsQueryActivity;
import com.huiyin.ui.home.LotteryActivity;
import com.huiyin.ui.home.NewsTodayActivity;
import com.huiyin.ui.home.PhonePayActivity;
import com.huiyin.ui.home.WaterCoalElectricActivity;
import com.huiyin.ui.home.prefecture.ZhuanQuActivity;
import com.huiyin.ui.nearshop.NearTheShopActivityNew;
import com.huiyin.ui.seckill.SeckillActivity;
import com.huiyin.ui.servicecard.BindServiceCard;
import com.huiyin.ui.servicecard.ServiceCardActivity;
import com.huiyin.ui.show.TheShowActivity;
import com.huiyin.ui.show.TheShowCommentActivity;
import com.huiyin.ui.user.KefuCenterActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.ui.user.MessageWebActivity;
import com.huiyin.ui.user.MyMessageActivity;
import com.huiyin.ui.user.MyOrderDetailActivity;
import com.huiyin.ui.user.YuYueDetailActivity;
import com.huiyin.ui.user.order.AllOrderDetailActivity;
import com.huiyin.ui.user.order.ApplyBespeakActivity;
import com.huiyin.ui.user.order.ReplaceDetailVerifyActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Push消息处理receiver。请编写您需要的回调函数， 一般来说： onBind是必须的，用来处理startWork返回值；
 * onMessage用来接收透传消息； onSetTags、onDelTags、onListTags是tag相关操作的回调；
 * onNotificationClicked在通知被点击时回调； onUnbind是stopWork接口的返回值回调
 * 
 * 返回值中的errorCode，解释如下： 0 - Success 10001 - Network Problem 30600 - Internal
 * Server Error 30601 - Method Not Allowed 30602 - Request Params Not Valid
 * 30603 - Authentication Failed 30604 - Quota Use Up Payment Required 30605 -
 * Data Required Not Found 30606 - Request Time Expires Timeout 30607 - Channel
 * Token Timeout 30608 - Bind Relation Not Found 30609 - Bind Number Too Many
 * 
 * 当您遇到以上返回错误时，如果解释不了您的问题，请用同一请求的返回值requestId和errorCode联系我们追查问题。
 * 
 */
public class MyPushMessageReceiver extends FrontiaPushMessageReceiver {
	/** TAG to Log */
	public static final String TAG = MyPushMessageReceiver.class.getSimpleName();
	private Context context;

	/**
	 * 调用PushManager.startWork后，sdk将对push
	 * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
	 * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
	 * 
	 * @param context
	 *            BroadcastReceiver的执行Context
	 * @param errorCode
	 *            绑定接口返回值，0 - 成功
	 * @param appid
	 *            应用id。errorCode非0时为null
	 * @param userId
	 *            应用user id。errorCode非0时为null
	 * @param channelId
	 *            应用channel id。errorCode非0时为null
	 * @param requestId
	 *            向服务端发起的请求id。在追查问题时有用；
	 * @return none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid, String userId, String channelId, String requestId) {
		// 绑定成功，设置已绑定flag，可以有效的减少不必要的绑定请求
		if (errorCode == 0) {
			Utils.setBind(context, true);
		}
		// 应用请在这里加入自己的处理逻辑
		AppContext.getInstance().userIdBai = userId;
		AppContext.getInstance().channelIdBai = channelId;
		
		SharedPreferences mPreferences = context.getSharedPreferences("bind", Context.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("userId", userId);
		mEditor.putString("channelId", channelId);
		mEditor.commit();

		LogUtil.d("userId+channelId", "userId+channelId:" + userId + "," + channelId + ";");

		Intent intent = new Intent(context,BindUserIdServer.class);
		intent.putExtra("channelId",channelId);
		intent.putExtra("bdUserId",userId);
		context.startService(intent);
	}

	/**
	 * 接收透传消息的函数。
	 * 
	 * @param context
	 *            上下文
	 * @param message
	 *            推送的消息
	 * @param customContentString
	 *            自定义内容,为空或者json字符串
	 */
	@Override
	public void onMessage(Context context, String message, String customContentString) {
	}

	/**
	 * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。
	 * 
	 * @param context
	 *            上下文
	 * @param title
	 *            推送的通知的标题
	 * @param description
	 *            推送的通知的描述
	 * @param customContentString
	 *            自定义内容，为空或者json字符串
	 */
	@Override
	public void onNotificationClicked(Context context, String title, String description, String customContentString) {
		updateContent(context, customContentString, description);
	}

	/**
	 * setTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
	 * @param successTags
	 *            设置成功的tag
	 * @param failTags
	 *            设置失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onSetTags(Context context, int errorCode, List<String> successTags, List<String> failTags, String requestId) {
	}

	/**
	 * delTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
	 * @param successTags
	 *            成功删除的tag
	 * @param failTags
	 *            删除失败的tag
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onDelTags(Context context, int errorCode, List<String> successTags, List<String> failTags, String requestId) {
	}

	/**
	 * listTags() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示列举tag成功；非0表示失败。
	 * @param tags
	 *            当前应用设置的所有tag。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onListTags(Context context, int errorCode, List<String> tags, String requestId) {
	}

	/**
	 * PushManager.stopWork() 的回调函数。
	 * 
	 * @param context
	 *            上下文
	 * @param errorCode
	 *            错误码。0表示从云推送解绑定成功；非0表示失败。
	 * @param requestId
	 *            分配给对云推送的请求的id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode + " requestId = " + requestId;
		LogUtil.d(TAG, responseString);

		// 解绑定成功，设置未绑定flag，
		if (errorCode == 0) {
			Utils.setBind(context, false);
		}
		// Demo更新界面展示代码，应用请在这里加入自己的处理逻辑
		// updateContent(context, responseString);
	}

	private void updateContent(Context context, String customContentString, String description) {
		this.context = context;
		if (!TextUtils.isEmpty(customContentString)) {
			LogUtil.i("customContentString", customContentString);
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				if (customJson.has("msgType")) {
					int msgType = customJson.getInt("msgType");
					JSONObject jumpDate = null;
					if (customJson.has("jumpDate") && !customJson.isNull("jumpDate")) {
						jumpDate = customJson.getJSONObject("jumpDate");
					}
					Intent intent = new Intent();
					switch (msgType) {
						case 1:
							//订单详情
							intent.putExtra(AllOrderDetailActivity.ORDER_ID, jumpDate.getString("id"));
							intent.putExtra("pushFlag", "1");
							intent.setClass(context.getApplicationContext(), AllOrderDetailActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 2:
							//预约详情
							intent.putExtra("bespeakId", jumpDate.getString("id"));
							intent.putExtra("pushFlag", "1");
							intent.setClass(context.getApplicationContext(), YuYueDetailActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 3:
							//消息详情子页
							intent.putExtra("htmlContent", description);
							if (jumpDate.has("url_type") && jumpDate.has("id")) {
								intent.putExtra("url_type", jumpDate.getInt("url_type"));
								intent.putExtra("id", jumpDate.getInt("id"));
								intent.putExtra("commodityId", jumpDate.getInt("commodityId"));
							}
							intent.putExtra("pushFlag", "1");
							intent.setClass(context.getApplicationContext(), MessageWebActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 4:
							//换货详情
							intent.putExtra(ReplaceDetailVerifyActivity.EXTRA_REPLACE_ID, jumpDate.getString("id"));
							intent.putExtra("pushFlag", "1");
							intent.setClass(context.getApplicationContext(), ReplaceDetailVerifyActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 5:
							//秀场评论
							intent.putExtra("id", Integer.parseInt(jumpDate.getString("id")));
							intent.putExtra("username", jumpDate.getString("spotlightName"));
							intent.putExtra("pushFlag", "1");
							intent.setClass(context.getApplicationContext(), TheShowCommentActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 6:
							//消息中心
							if (AppContext.getInstance().userId == null) {
								intent.setClass(context.getApplicationContext(), LoginActivity.class);
								intent.putExtra("pushFlag", "1");
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.getApplicationContext().startActivity(intent);
							} else {
								intent.putExtra("pushFlag", "1");
								intent.setClass(context.getApplicationContext(), MyMessageActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								context.getApplicationContext().startActivity(intent);
							}
							break;
						case 7:
							//秒杀活动
							intent.setClass(context.getApplicationContext(),SeckillActivity.class);
							intent.putExtra("id", jumpDate.getInt("id"));
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 8:
							intent.setClass(context.getApplicationContext(),FlashPrefectureActivity.class);
							intent.putExtra("id", jumpDate.getInt("id"));
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 9:
							//商品预购
							intent.setClass(context.getApplicationContext(), ProductsDetailActivity.class);
							intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, jumpDate.getInt("commodityId") + "");
							intent.putExtra(ProductsDetailActivity.STORE_ID, jumpDate.getInt("storeId") + "");
							intent.putExtra(ProductsDetailActivity.GOODS_NO, jumpDate.getInt("goodsNo") + "");
							intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_subscribe_ID, jumpDate.getInt("id"));
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 10:
							//专区活动
							intent.setClass(context.getApplicationContext(), ZhuanQuActivity.class);
							intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID, jumpDate.getInt("id") + "");
							intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, jumpDate.getInt("type"));
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 11:
							// 今日头条
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(context.getApplicationContext(), NewsTodayActivity.class);
							context.getApplicationContext().startActivity(intent);
							break;
						case 12:
							// 乐虎彩票
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(context.getApplicationContext(), LotteryActivity.class);
							context.getApplicationContext().startActivity(intent);
							break;
						case 13:
							if (StringUtils.isBlank(AppContext.getInstance().userId)) {
								Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setClass(context.getApplicationContext(), LoginActivity.class);
								context.getApplicationContext().startActivity(intent);
							} else {
								// 预约服务
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setClass(context.getApplicationContext(), ApplyBespeakActivity.class);
								context.getApplicationContext().startActivity(intent);
							}
							break;
						case 14:
							// 物流查询
							if (StringUtils.isBlank(AppContext.getInstance().userId)) {
								Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setClass(context.getApplicationContext(), LoginActivity.class);
								context.getApplicationContext().startActivity(intent);
							} else {
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setClass(context.getApplicationContext(), LogisticsQueryActivity.class);
								context.getApplicationContext().startActivity(intent);
							}
							break;
						case 15:
							// 智慧管家
							AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
							intent.setClass(context, MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
							break;
						case 16:
							// 秀场
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(context.getApplicationContext(), TheShowActivity.class);
							context.startActivity(intent);
							break;
						case 17:
							// 积分club
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(context.getApplicationContext(), ClubActivity.class);
							context.startActivity(intent);
							break;
						case 18:
							// 客户中心
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(context.getApplicationContext(), KefuCenterActivity.class);
							context.startActivity(intent);
							break;
						case 19:
							//身边汇银（附近门店）
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							intent.setClass(context.getApplicationContext(), NearTheShopActivityNew.class);
							context.startActivity(intent);
							break;
						case 20:
							// 服务卡
							if (StringUtils.isBlank(AppContext.getInstance().userId)) {
								Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.setClass(context.getApplicationContext(), LoginActivity.class);
								context.startActivity(intent);
							} else {
								if (AppContext.getInstance().mUserInfo.bdStatus == null) {
									intent.setClass(context, ServiceCardActivity.class);
								} else {
									intent.setClass(context, BindServiceCard.class);
								}
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent);
							}
							break;
						case 21:
							//专区
							intent.setClass(context.getApplicationContext(), ZhuanQuActivity.class);
							intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID, jumpDate.getInt("id") + "");
							intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, jumpDate.getInt("type"));
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 22:
							//商品详情
							intent.setClass(context, ProductsDetailActivity.class);
							intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, jumpDate.getInt("commodityId") + "");
							intent.putExtra(ProductsDetailActivity.STORE_ID, jumpDate.getInt("storeId") + "");
							intent.putExtra(ProductsDetailActivity.GOODS_NO, jumpDate.getInt("goodsNo") + "");
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
							break;
						case 23:
							//换货详情
							intent.setClass(context.getApplicationContext(), ReplaceDetailVerifyActivity.class);
							intent.putExtra(ReplaceDetailVerifyActivity.EXTRA_REPLACE_ID, jumpDate.getString("id"));
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 24:
							//秀场评论
							intent.setClass(context.getApplicationContext(), TheShowCommentActivity.class);
							intent.putExtra("id", Integer.parseInt(jumpDate.getString("id")));
							intent.putExtra("username", jumpDate.getString("spotlightName"));
							intent.putExtra("pushFlag", "1");
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 25:
							// 水电煤充值,跳转到充值记录
							intent = new Intent(new Intent(context, WaterCoalElectricActivity.class));
				        	intent.putExtra(WaterCoalElectricActivity.ID, 12);//默认选择水费充值
				        	intent.putExtra(WaterCoalElectricActivity.INDEX, 1);//默认选中缴费记录页签
				        	context.startActivity(intent);
							break;
						case 26:
							// 话费充值,跳转到充值记录
							intent = new Intent(context, PhonePayActivity.class);
							intent.putExtra(PhonePayActivity.INDEX, 1);//默认选中缴费记录页签
				        	context.startActivity(intent);
							break;
						default:
                            AppContext.MAIN_TASK = AppContext.FIRST_PAGE;
                            intent.setClass(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
							break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			AppContext.MAIN_TASK = AppContext.FIRST_PAGE;
			Intent intent = new Intent();
			intent.setClass(context, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

		/*if (!TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);

				if (customJson.has("msgType")) {

					int msgType = customJson.getInt("msgType");
					JSONObject jumpDate = null;
					if (customJson.has("jumpDate")) {
						jumpDate = customJson.getJSONObject("jumpDate");
					}

					if (AppContext.getInstance().userId == null) {
						Intent intent = new Intent();
						intent.setClass(context.getApplicationContext(), LoginActivity.class);
						intent.putExtra("pushFlag", "1");
						intent.putExtra("customContentString", customContentString);
						intent.putExtra("description", description);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.getApplicationContext().startActivity(intent);
					} else {
						switch (msgType) {
						case 7:
						case 8:
						case 10:
						case 11:
						case 12:
						case 13:
						case 14:
						case 15:
						case 16:
						case 17: // 跳转到订单详情页
							Intent intent = new Intent();
							intent.putExtra("order_id", jumpDate.getString("id"));
							intent.putExtra("pushFlag", "1");
							intent.setClass(context.getApplicationContext(), MyOrderDetailActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent);
							break;
						case 19:
						case 20:
						case 21: // 跳转到预约详情页
							Intent intentYuYue = new Intent();
							intentYuYue.putExtra("orderId", jumpDate.getString("id"));
							intentYuYue.putExtra("pushFlag", "1");
							intentYuYue.setClass(context.getApplicationContext(), YuYueDetailActivity.class);
							intentYuYue.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intentYuYue);
							break;
						case 28:
							Intent intentWeb = new Intent();
							intentWeb.putExtra("htmlContent", description);
							if (jumpDate.has("url_type") && jumpDate.has("id")) {
								intentWeb.putExtra("url_type", jumpDate.getInt("url_type"));
								intentWeb.putExtra("id", jumpDate.getInt("id"));
								intentWeb.putExtra("commodityId", jumpDate.getInt("commodityId"));
							}
							intentWeb.putExtra("pushFlag", "1");
							intentWeb.setClass(context.getApplicationContext(), MessageWebActivity.class);
							intentWeb.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intentWeb);
							break;
						case 29:
						case 30:
						case 31:
						case 32:
						case 33:
						case 34:
						case 35:
						case 36:
						case 37:
						case 38:
						case 39:
						case 40:
						case 41:
						case 42: // 跳转到换货详情页
							Intent intentHuanhuo = new Intent();
							intentHuanhuo.putExtra("returnId", jumpDate.getString("id"));
							intentHuanhuo.putExtra("commodityId", jumpDate.getString("commodityId"));
							intentHuanhuo.putExtra("goodName", jumpDate.getString("commodityName"));
							intentHuanhuo.putExtra("statu", Integer.parseInt(jumpDate.getString("status")));
							intentHuanhuo.putExtra("pushFlag", "1");
							intentHuanhuo.setClass(context.getApplicationContext(), AfterSaleDetailActivity.class);
							intentHuanhuo.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intentHuanhuo);
							break;
						case 43:
						case 44:
							Intent intent1 = new Intent();
							intent1.putExtra("id", Integer.parseInt(jumpDate.getString("id")));
							intent1.putExtra("username", jumpDate.getString("spotlightName"));
							intent1.putExtra("pushFlag", "1");
							intent1.setClass(context.getApplicationContext(), TheShowCommentActivity.class);
							intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
							context.getApplicationContext().startActivity(intent1);
							break;
						case 51:
							switch (jumpDate.getInt("url_type")) {
							// 1.秒杀 2.闪购 3.新品预约/商品详情 4.分类聚合 5.快捷服务 6.促销活动
							case 1:
								Intent intent7 = new Intent(context, SeckillActivity.class);
								intent7.putExtra("id", jumpDate.getInt("id"));
								intent7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent7);
								break;
							case 2:
								Intent intent4 = new Intent(context, FlashPrefectureActivity.class);
								intent4.putExtra("id", jumpDate.getInt("id"));
								intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent4);
								break;
							case 3:
								Intent intent3 = new Intent(context, ProductsDetailActivity.class);
								intent3.putExtra("goods_detail_id", jumpDate.getInt("commodityId") + "");
								intent3.putExtra("subscribeId", jumpDate.getInt("id"));
								intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent3);
								break;
							case 4:
								Intent intent2 = new Intent(context, ZhuanQuActivity.class);
								intent2.putExtra(ZhuanQuActivity.INTENT_KEY_ID, jumpDate.getInt("id") + "");
								intent2.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 1);
								intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent2);
								break;
							case 5:
								connt(jumpDate.getInt("id"));
								break;
							case 6:
								Intent intent6 = new Intent(context, ZhuanQuActivity.class);
								intent6.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, 2);
								intent6.putExtra(ZhuanQuActivity.INTENT_KEY_ID, jumpDate.getInt("id") + "");
								intent6.putExtra(ZhuanQuActivity.INTENT_KEY_FLAG, 1);
								intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent6);
								break;
							case 7:
								Intent intent5 = new Intent(context, ProductsDetailActivity.class);
								intent5.putExtra("goods_detail_id", jumpDate.getInt("commodityId") + "");
								intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent5);
								break;
							}

							break;
						}

					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			if (AppContext.getInstance().userId == null) {
				Intent intent = new Intent();
				intent.setClass(context.getApplicationContext(), LoginActivity.class);
				intent.putExtra("pushFlag", "1");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
			} else {
				Intent intentSys = new Intent();
				intentSys.putExtra("pushFlag", "1");
				intentSys.setClass(context.getApplicationContext(), MyMessageActivity.class);
				intentSys.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intentSys);
			}
		}*/
	}

	public void connt(int id) {
		switch (id) {
		case 1:
			// 今日头条
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setClass(context.getApplicationContext(), NewsTodayActivity.class);
			context.startActivity(intent);
			break;
		case 2:
			// 乐虎彩票
			Intent intent1 = new Intent();
			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent1.setClass(context.getApplicationContext(), LotteryActivity.class);
			context.startActivity(intent1);
			break;
		case 3:
			if (StringUtils.isBlank(AppContext.getInstance().userId)) {
				Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
				Intent intent2 = new Intent();
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.setClass(context.getApplicationContext(), LoginActivity.class);
				context.startActivity(intent2);
			} else {
				// 预约服务
				Intent intent2 = new Intent();
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.setClass(context.getApplicationContext(), ApplyBespeakActivity.class);
				context.startActivity(intent2);
			}
			break;
		case 4:
			// 物流查询
			if (StringUtils.isBlank(AppContext.getInstance().userId)) {
				Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
				Intent intent2 = new Intent();
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.setClass(context.getApplicationContext(), LoginActivity.class);
				context.startActivity(intent2);
			} else {
				Intent intent2 = new Intent();
				intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent2.setClass(context.getApplicationContext(), LogisticsQueryActivity.class);
				context.startActivity(intent2);
			}
			break;
		case 5:
			// 智慧管家
			AppContext.MAIN_TASK = AppContext.HOUSEKEEPER;
			Intent i = new Intent();
			i.setClass(context, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			break;
		case 6:
			// 秀场
			Intent intent2 = new Intent();
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent2.setClass(context.getApplicationContext(), TheShowActivity.class);
			context.startActivity(intent2);
			break;
		case 7:
			// 积分club
			Intent intent3 = new Intent();
			intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent3.setClass(context.getApplicationContext(), ClubActivity.class);
			context.startActivity(intent3);
			break;
		case 8:
			// 客户中心
			Intent intent4 = new Intent();
			intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent4.setClass(context.getApplicationContext(), KefuCenterActivity.class);
			context.startActivity(intent4);
			break;
		case 9:
			Intent intent5 = new Intent();
			intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			intent5.setClass(context.getApplicationContext(), NearTheShopActivityNew.class);
			context.startActivity(intent5);
			break;
		case 10:
			// 服务卡
			if (StringUtils.isBlank(AppContext.getInstance().userId)) {
				Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
				Intent intent6 = new Intent();
				intent6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent6.setClass(context.getApplicationContext(), LoginActivity.class);
				context.startActivity(intent6);
			} else {
				Intent fuwu_intent = new Intent();
				if (AppContext.getInstance().mUserInfo.bdStatus == null) {
					fuwu_intent.setClass(context, ServiceCardActivity.class);
				} else {
					fuwu_intent.setClass(context, BindServiceCard.class);
				}
				fuwu_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(fuwu_intent);
			}
			break;
		default:
			break;
		}
	}

}
