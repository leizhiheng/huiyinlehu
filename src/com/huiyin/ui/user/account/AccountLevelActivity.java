package com.huiyin.ui.user.account;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.CircleImageView;
import com.huiyin.wight.MyListView;
import com.huiyin.wight.SpringProgressView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 会员等级页面
 * 
 * @author leizhiheng
 * 
 */
public class AccountLevelActivity extends BaseActivity {
	private static final String TAG = "AccountLevelActivity";
	
	private LayoutInflater mInflater;
	private ArrayList<rankChangeRecord> recordList;
	private ArrayList<MemberRankDescription> rankDescriptionsList;

	private String growthValue;
	private String gradeExplain;
	private String nextGrowthValue;
	private String nextGrowthNeed;
	private String nextGrowthName;

	private TextView my_rank_level, rank_level_next, member_discount_des,
			rank_change_record;
	private ImageView my_rank_icon;
	private CircleImageView user_icon;
	private MyListView rank_listView;
	private SpringProgressView member_rank_level_bar;
	
	private MyRankAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.member_rank_layout1);
		init();
		loadMemberRankInfo();
	}

	public void init() {
		findViewById(R.id.right_ib).setVisibility(View.GONE);
		mInflater = LayoutInflater.from(this);
		rankDescriptionsList = new ArrayList<AccountLevelActivity.MemberRankDescription>();
		recordList = new ArrayList<AccountLevelActivity.rankChangeRecord>();
		TextView titleTv = (TextView) findViewById(R.id.middle_title_tv);
		titleTv.setText(getResources().getString(R.string.account_page_title));
		user_icon = (CircleImageView) findViewById(R.id.my_rank_level_head);
		my_rank_level = (TextView) findViewById(R.id.my_rank_level);
		my_rank_icon = (ImageView) findViewById(R.id.my_rank_icon);
		rank_level_next = (TextView) findViewById(R.id.my_rank_level_next);
		member_discount_des = (TextView) findViewById(R.id.my_rank_level_my_rank_level_discount_content);
		rank_change_record = (TextView) findViewById(R.id.my_rank_level_up);
		rank_listView = (MyListView) findViewById(R.id.listview_rank);
		member_rank_level_bar = (SpringProgressView) findViewById(R.id.my_rank_level_bar);
		mAdapter = new MyRankAdapter();
		rank_listView.setAdapter(mAdapter);

		my_rank_level.setFocusable(true);
		my_rank_level.setFocusableInTouchMode(true);
		my_rank_level.requestFocus();
	}

	
	public void setValue() {
		ImageManager.LoadWithServer(URLs.IMAGE_URL + AppContext.mUserInfo.img, user_icon,new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_head)
				.showImageForEmptyUri(R.drawable.icon_head)
				.showImageOnFail(R.drawable.icon_head)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build());
		my_rank_level.setText("当前等级："+AppContext.mUserInfo.level );
	    ImageLoader.getInstance().displayImage(URLs.IMAGE_URL+AppContext.mUserInfo.levelLogo, my_rank_icon);
		rank_level_next.setText(Html.fromHtml(String.format(getResources()
				.getString(R.string.premote_tip), nextGrowthNeed,
				nextGrowthName)));
		StringBuffer recordsString = new StringBuffer();
		for (int i = 0; i < recordList.size(); i++) {
			LogUtil.d(TAG, "record:" + recordList.get(i));
			rankChangeRecord record = recordList.get(i);
			recordsString.append(record.message + "        " + record.time
					+ "\n");
		}
		member_discount_des.setText(Html.fromHtml(gradeExplain));
		rank_change_record.setText(recordsString.toString());
		setRankbar();
		mAdapter.notifyDataSetChanged();
	}
	
	 private void setRankbar() {
	        member_rank_level_bar.initView(this, R.color.bluesky, R.color.bluesky, R.color.white);
	        member_rank_level_bar.reset();
	        member_rank_level_bar.setDuringTime(2000);
	        member_rank_level_bar.setMaxCount(Integer.valueOf(nextGrowthValue));
	        member_rank_level_bar.setCurrentCount(AppContext.mUserInfo.levelValue=Integer.valueOf(growthValue));
	    }

	public void loadMemberRankInfo() {
		RequstClient.memberRankInfo(AppContext.userId,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {

						super.onSuccess(statusCode, headers, content);
						Log.d(TAG, "load message:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								Log.d(TAG, "load member info failed,msg:"
										+ content);
								Toast.makeText(AccountLevelActivity.this,
										"会员等级信息获取失败", Toast.LENGTH_SHORT)
										.show();
							} else {
								nextGrowthValue = JSONParseUtils.getString(obj,
										"NEXT_GROWTH_VALUE");
								growthValue = JSONParseUtils.getString(obj,
										"GROWTH_VALUE");
								gradeExplain = JSONParseUtils.getString(obj,
										"GRADE_EXPLAN");
								nextGrowthNeed = JSONParseUtils.getString(obj,
										"NEXT_GROWTH_NEED");
								nextGrowthName = JSONParseUtils.getString(obj,
										"NEXT_GROWTH_NAME");
								parseRankDescriptions(obj);
								parseRecords(obj);
								setValue();
							}
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void parseRankDescriptions(JSONObject obj) {
		JSONArray array;
		try {
			array = obj.getJSONArray("userLevelData");
			JSONObject object = null;
			MemberRankDescription description = null;
			for (int i = 0; i < array.length(); i++) {
				object = (JSONObject) array.get(i);
				description = new MemberRankDescription();
				description.id = object.getString("ID");
				description.title = object.getString("TITLE");
				description.maxPoint = object.getString("MAX_GROWTH_VALUE");
				description.minPoint = object.getString("MIN_GROWTH_VALUE");
				description.url = object.getString("URL");
				rankDescriptionsList.add(description);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private void parseRecords(JSONObject obj) {
		JSONArray array;
		try {
			array = obj.getJSONArray("gradeRecordData");
			JSONObject object = null;
			rankChangeRecord record = null;
			for (int i = 0; i < array.length(); i++) {
				object = (JSONObject) array.get(i);
				record = new rankChangeRecord();
				record.time = object.getString("CREATE_TIME");
				record.message = object.getString("TITLE");
				recordList.add(record);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	class MyRankAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return rankDescriptionsList == null ? 0 : rankDescriptionsList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View view, ViewGroup arg2) {
			ViewHolder holder;
			if(view == null){
				holder = new ViewHolder();
				view = mInflater.inflate(R.layout.layout_rank_descrition_item, null);
				holder.rank_icon = (ImageView) view.findViewById(R.id.rank_icon);
				holder.rank_title = (TextView) view.findViewById(R.id.rank_title);
				holder.rank_message = (TextView) view.findViewById(R.id.rank_message);
				view.setTag(holder);
			}
			
			holder = (ViewHolder) view.getTag();
			holder.rank_title.setText(rankDescriptionsList.get(arg0).title);
			holder.rank_message.setText("成长值："+rankDescriptionsList.get(arg0).minPoint+" -- "+rankDescriptionsList.get(arg0).maxPoint);
			ImageManager.LoadWithServer(rankDescriptionsList.get(arg0).url,holder.rank_icon);
			return view;
		}
	}
	
	public void setRankIcon(String id,ImageView rankIcon){
		int rankId = Integer.valueOf(id);
		if(rankId == 0){
			rankIcon.setImageResource(R.drawable.hy1);
		}else if(rankId == 191){
			rankIcon.setImageResource(R.drawable.hy2);
		}else if(rankId == 192){
			rankIcon.setImageResource(R.drawable.hy3);
		}else if(rankId == 193){
			rankIcon.setImageResource(R.drawable.hy4);
		}
	}
	
	class ViewHolder{
		ImageView rank_icon;
		TextView rank_title;
		TextView rank_message;
	}

	class MemberRankDescription {
		String id;
		String title;
		String minPoint;
		String maxPoint;
		String url;
		@Override
		public String toString() {
			return "id:"+id+",title:"+title+",minPoint:"+minPoint+",maxPoint:"+maxPoint;
		}
	}

	class rankChangeRecord {
		String time;
		String message;
		public String toString(){
			return "time:"+time+",message:"+message;
		}
	}
}
