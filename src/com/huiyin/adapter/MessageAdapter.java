package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.SysMessageList.SysMessage;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.club.ClubActivity;
import com.huiyin.ui.flash.FlashPrefectureActivity;
import com.huiyin.ui.home.LotteryActivity;
import com.huiyin.ui.home.NewsTodayActivity;
import com.huiyin.ui.home.PhonePayActivity;
import com.huiyin.ui.home.WaterCoalElectricActivity;
import com.huiyin.ui.home.Logistics.LogisticsQueryActivity;
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
import com.huiyin.ui.user.YuYueDetailActivity;
import com.huiyin.ui.user.order.AllOrderDetailActivity;
import com.huiyin.ui.user.order.ApplyBespeakActivity;
import com.huiyin.ui.user.order.ReplaceDetailVerifyActivity;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.ExpandableTextView;

public class MessageAdapter extends BaseAdapter {

	private List<SysMessage> messages = new ArrayList<SysMessage>();
	Context context;

	public MessageAdapter(Context context) {
		this.context = context;
	}

	public List<SysMessage> returnDatas() {
		return messages;
	}

	public void add(List<SysMessage> data) {
		this.messages.addAll(data);
		notifyDataSetChanged();
	}

	public void refresh(List<SysMessage> arrayList) {
		this.messages.clear();
		this.messages = null;
		this.messages = arrayList;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return null != messages ? messages.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.message_list_item, null);
			holder.ll_root = (LinearLayout) convertView.findViewById(R.id.ll_root);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.content = (ExpandableTextView) convertView.findViewById(R.id.content);
			holder.img_arraw = (ImageView) convertView.findViewById(R.id.img_arraw);
			holder.img = (ImageView) convertView.findViewById(R.id.img);

			holder.content_root = (LinearLayout) convertView.findViewById(R.id.content_root);
			holder.content_root2 = (LinearLayout) convertView.findViewById(R.id.content_root2);
			holder.content2 = (TextView) convertView.findViewById(R.id.content2);
			holder.examine = (TextView) convertView.findViewById(R.id.examine);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(messages.get(position).TITLE);
		holder.time.setText(messages.get(position).SEND_TIME);
		holder.content.setText(messages.get(position).CONTENT);
		holder.content.setImg(holder.img_arraw, holder.img);

		if (messages.get(position).READ_STATUS.equals("1")) {
			holder.title.setTextColor(0xffb6b6b6);
			holder.content.setTextColor(0xffb6b6b6);
			holder.content2.setTextColor(0xffb6b6b6);
			holder.examine.setTextColor(0xffb6b6b6);
		} else {
			holder.title.setTextColor(0xff000000);
			holder.content.setTextColor(0xff000000);
			holder.content2.setTextColor(0xff000000);
			holder.examine.setTextColor(0xff000000);
		}

		if (messages.get(position).CONTENT != null) {
			if (messages.get(position).CONTENT.length() < 40) {
				holder.img_arraw.setVisibility(View.GONE);
				holder.ll_root.setVisibility(View.GONE);
			} else {
				holder.img_arraw.setVisibility(View.VISIBLE);
				holder.ll_root.setVisibility(View.GONE);
			}
		} else {
			holder.img_arraw.setVisibility(View.GONE);
			holder.ll_root.setVisibility(View.GONE);
		}

		// if(!messages.get(position).VIEW_TYPE.equals("1")&&!messages.get(position).VIEW_TYPE.equals("5")){
		holder.ll_root.setVisibility(View.VISIBLE);
		holder.content_root.setVisibility(View.GONE);
		holder.content_root2.setVisibility(View.VISIBLE);
		try {
			// holder.content2.setText(messages.get(position).SEND_CONTENT.replaceAll("[^\\u4e00-\\u9fa5]",
			// ""));
			holder.content2.setText(messages.get(position).CONTENT.replaceAll("</?[^>]+>", "").replaceAll("\\s*|\t|\r|\n", "").replaceAll("&nbsp;", ""));
		} catch (Exception e) {
		}
		// }else{
		// holder.content_root.setVisibility(View.VISIBLE);
		// holder.content_root2.setVisibility(View.GONE);
		// holder.ll_root.setVisibility(View.GONE);
		// holder.content.setText(messages.get(position).CONTENT);
		// }

		convertView.setOnClickListener(new MyViewOnclick(holder.title, holder.time, holder.content, holder.content2, holder.examine, position));

		return convertView;
	}

	class ViewHolder {
		TextView title;
		TextView time;
		ExpandableTextView content;
		ImageView img_arraw;
		ImageView img;
		LinearLayout ll_root;
		LinearLayout content_root;
		LinearLayout content_root2;
		TextView content2;
		TextView examine;
	}


	class MyViewOnclick implements View.OnClickListener {
		TextView title;
		TextView time;
		TextView content;
		TextView content2;
		TextView examine;
		int item;

		public MyViewOnclick(TextView title, TextView time, TextView content, TextView content2, TextView examine, int item) {
			this.title = title;
			this.time = time;
			this.content = content;
			this.content2 = content2;
			this.examine = examine;
			this.item = item;
		}

		@Override
		public void onClick(View v) {

			if (messages.get(item).READ_STATUS.equals("0")) {
				messages.get(item).READ_STATUS = "1";
				appLetterReader(messages.get(item).ID);
				notifyDataSetChanged();
			}
			SysMessage msg = (SysMessage) getItem(item);
			int msgType = msg.getMsgType();
			String jumpData = msg.JUMP_DATA;
				String description = msg.CONTENT;
			jump(msgType, jumpData, description);
		}

	}

	/**
	 * 界面跳转
	 * @param msgType
	 * @param jumpData
	 * @param description
	 */
	private void jump(int msgType, String jumpData, String description) {

		//判断数据非空
		if(StringUtils.isEmpty(jumpData)){
			return;
		}
		
		JSONObject jumpDate = null;
		try {
			jumpDate = new JSONObject(jumpData);

			Intent intent = new Intent();
			switch (msgType) {
			case 1:
				// 订单详情
				intent.putExtra(AllOrderDetailActivity.ORDER_ID, jumpDate.getString("id"));
				intent.putExtra("pushFlag", "1");
				intent.setClass(context.getApplicationContext(), AllOrderDetailActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				break;
			case 2:
				// 预约详情
				intent.putExtra("bespeakId", jumpDate.getString("id"));
				intent.putExtra("pushFlag", "1");
				intent.setClass(context.getApplicationContext(), YuYueDetailActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				break;
			case 3:
				// 消息详情子页
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
				// 换货详情
				intent.putExtra(ReplaceDetailVerifyActivity.EXTRA_REPLACE_ID, jumpDate.getString("id"));
				intent.putExtra("pushFlag", "1");
				intent.setClass(context.getApplicationContext(), ReplaceDetailVerifyActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				break;
			case 5:
				// 秀场评论
				intent.putExtra("id", Integer.parseInt(jumpDate.getString("id")));
				intent.putExtra("username", jumpDate.getString("spotlightName"));
				intent.putExtra("pushFlag", "1");
				intent.setClass(context.getApplicationContext(), TheShowCommentActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				break;
			case 6:
				// 消息中心
				if (AppContext.userId == null) {
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
				// 秒杀活动
				intent.setClass(context.getApplicationContext(), SeckillActivity.class);
				intent.putExtra("id", jumpDate.getInt("id"));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				break;
			case 8:
				intent.setClass(context.getApplicationContext(), FlashPrefectureActivity.class);
				intent.putExtra("id", jumpDate.getInt("id"));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				break;
			case 9:
				// 商品预购
				intent.setClass(context.getApplicationContext(), ProductsDetailActivity.class);
				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, jumpDate.getInt("commodityId") + "");
				intent.putExtra(ProductsDetailActivity.STORE_ID, jumpDate.getInt("storeId") + "");
				intent.putExtra(ProductsDetailActivity.GOODS_NO, jumpDate.getInt("goodsNo") + "");
				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_subscribe_ID, jumpDate.getInt("id"));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				break;
			case 10:
				// 专区活动
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
				if (StringUtils.isBlank(AppContext.userId)) {
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
				if (StringUtils.isBlank(AppContext.userId)) {
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
				// 身边汇银（附近门店）
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(context.getApplicationContext(), NearTheShopActivityNew.class);
				context.startActivity(intent);
				break;
			case 20:
				// 服务卡
				if (StringUtils.isBlank(AppContext.userId)) {
					Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(context.getApplicationContext(), LoginActivity.class);
					context.startActivity(intent);
				} else {
					if (AppContext.mUserInfo.bdStatus == null) {
						intent.setClass(context, ServiceCardActivity.class);
					} else {
						intent.setClass(context, BindServiceCard.class);
					}
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
				break;
			case 21:
				// 专区
				intent.setClass(context.getApplicationContext(), ZhuanQuActivity.class);
				intent.putExtra(ZhuanQuActivity.INTENT_KEY_ID, jumpDate.getInt("id") + "");
				intent.putExtra(ZhuanQuActivity.INTENT_KEY_TYPE, jumpDate.getInt("type"));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				break;
			case 22:
				// 商品详情
				intent.setClass(context, ProductsDetailActivity.class);
				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, jumpDate.getInt("commodityId") + "");
				intent.putExtra(ProductsDetailActivity.STORE_ID, jumpDate.getInt("storeId") + "");
				intent.putExtra(ProductsDetailActivity.GOODS_NO, jumpDate.getInt("goodsNo") + "");
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				break;
			case 23:
				// 换货详情
				intent.setClass(context.getApplicationContext(), ReplaceDetailVerifyActivity.class);
				intent.putExtra(ReplaceDetailVerifyActivity.EXTRA_REPLACE_ID, jumpDate.getString("id"));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				context.getApplicationContext().startActivity(intent);
				break;
			case 24:
				// 秀场评论
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
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void appLetterReader(String letterId) {
		CustomResponseHandler handler = new CustomResponseHandler(context,false) {
			@Override
			public void onRefreshData(String content) {

			}
		};
		RequstClient.appLetterReader(letterId,handler);
	}
}