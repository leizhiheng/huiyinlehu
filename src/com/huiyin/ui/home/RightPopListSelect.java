package com.huiyin.ui.home;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.utils.PopupWindowUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 右边出现的POP选择界面
 */
public class RightPopListSelect {
	private final String title;
	private final SelectResultListener listener;
	private Context context;
	private View contentview;
	private List<String> primary;
	private List<String> secondary;
	private List<String> hindmost;
	private HashMap<String, List<String>> secondaryMap;
	private HashMap<String, List<String>> hindmostMap;
	private HashMap<String, String> selectedMap; // 已选中的地点
	private ListView lv_position;
	private ImageView btn_back;

	private PopupWindowUtils popupWindowUtils;
	private MyAreaAdpter mAdpter;
	private int level = 0;
	private int type = 0; // 0数据之间没有耦合关系，日期；1数据之间有耦合关系，地点。
	private Handler mHandler;

	public RightPopListSelect(Context context, View target,
			List<String> primary, List<String> secondary,
			List<String> hindmost, HashMap<String, String> selectedMap,
			String title, SelectResultListener listener) {
		this.context = context;
		this.primary = primary;
		this.secondary = secondary;
		this.hindmost = hindmost;
		this.selectedMap = selectedMap;
		this.title = title;
		this.listener = listener;
		contentview = LayoutInflater.from(context).inflate(
				R.layout.layout_pop_selectposition, null);
		init();
		popupWindowUtils = new PopupWindowUtils(context, contentview, target);
		setListener();

	}

	public RightPopListSelect(int type, Context context, View target,
			List<String> primary, HashMap<String, List<String>> secondaryMap,
			HashMap<String, List<String>> hindmostMap,
			HashMap<String, String> selectedMap, String title,
			SelectResultListener listener) {
		this.context = context;
		this.primary = primary;
		this.secondaryMap = secondaryMap;
		this.hindmostMap = hindmostMap;
		this.selectedMap = selectedMap;
		this.title = title;
		this.listener = listener;
		contentview = LayoutInflater.from(context).inflate(
				R.layout.layout_pop_selectposition, null);
		init();
		popupWindowUtils = new PopupWindowUtils(context, contentview, target);
		setListener();
		this.type = type;
	}

	private void init() {
		lv_position = (ListView) contentview.findViewById(R.id.lv_position);
		btn_back = (ImageView) contentview.findViewById(R.id.btn_back);
		TextView mSelect_title = (TextView) contentview
				.findViewById(R.id.select_title);
		if (selectedMap == null) {
			selectedMap = new HashMap<String, String>();
			mAdpter = new MyAreaAdpter(primary, null);
		} else {
			mAdpter = new MyAreaAdpter(primary, selectedMap.get("0"));
		}
		lv_position.setAdapter(mAdpter);
		mSelect_title.setText(title);
	}

	private void setListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (2 == level) {
					level--;
					mAdpter.setList(secondary);
					mAdpter.setSelect(selectedMap.get(level + ""));
					mAdpter.notifyDataSetChanged();
				} else if (1 == level) {
					level--;
					mAdpter.setList(primary);
					mAdpter.setSelect(selectedMap.get(level + ""));
					mAdpter.notifyDataSetChanged();
				} else if (0 == level) {
					popupWindowUtils.getPopupWindow().dismiss();
				}
			}
		});
		lv_position
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						listItemListener(position);
					}
				});
		popupWindowUtils.getPopupWindow().setOnDismissListener(
				new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						WindowManager.LayoutParams lp = ((Activity) context)
								.getWindow().getAttributes();
						lp.alpha = 1; // 0.0-1.0
						((Activity) context).getWindow().setAttributes(lp);
						if (null != listener && selectedMap != null) {
							listener.selectResult(selectedMap);// 回掉接口
						}
					}
				});

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (popupWindowUtils != null) {
					popupWindowUtils.getPopupWindow().dismiss();
				}
			}
		};
	}

	private void listItemListener(int position) {
		if (0 == level && primary != null) {
			selectedMap.put(level + "", primary.get(position));
			if (type == 1 && secondaryMap != null) {
				secondary = secondaryMap.get(primary.get(position));
			}
			update(1, secondary, primary.get(position));
		} else if (1 == level && secondary != null) {
			selectedMap.put(level + "", secondary.get(position));
			if (type == 1 && hindmostMap != null) {
				hindmost = hindmostMap.get(secondary.get(position));
			}
			update(1, hindmost, secondary.get(position));
		} else if (2 == level && hindmost != null) {
			selectedMap.put(level + "", hindmost.get(position));
			update(0, null, hindmost.get(position));
			dismiss();
		} else {
			popupWindowUtils.getPopupWindow().dismiss();
		}
	}

	/**
	 * 刷新界面数据
	 * 
	 * @param which
	 *            0刷新当前界面 1刷新下个界面
	 * @param target
	 *            下个界面的数据
	 * @param choose
	 *            当前选中的数据
	 */
	private void update(int which, List<String> target, String choose) {
		switch (which) {
		case 0:// 刷新本界面
			mAdpter.setSelect(choose);
			mAdpter.notifyDataSetChanged();
			break;
		case 1:// 刷新下个界面
			if (target != null) {
				level++;
				mAdpter.setList(target);
				mAdpter.setSelect(selectedMap.get(level + ""));
				mAdpter.notifyDataSetChanged();
			} else {
				update(0, null, choose);
				dismiss();
			}
			break;
		}
	}

	/**
	 * 延迟dismiss
	 */
	private void dismiss() {
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(0, 200);
		}
	}

	public class MyAreaAdpter extends BaseAdapter {
		List<String> list;
		String select = "";

		public MyAreaAdpter(List<String> templist, String selected) {
			setList(templist);
			setSelect(selected);
		}

		public void setList(List<String> templist) {

			if (list == null) {
				list = new ArrayList<String>();
			}
			if (templist != null) {
				list.clear();
				for (int i = 0; i < templist.size(); i++) {
					list.add(templist.get(i));
				}
			}
		}

		public void setSelect(String selected) {
			if (!TextUtils.isEmpty(selected)) {
				select = selected;
			}
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyHolder holder = null;
			if (null == convertView) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.layout_position_lv_item, null);
				holder = new MyHolder();
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.iv_gou = (ImageView) convertView
						.findViewById(R.id.iv_gou);
				convertView.setTag(holder);
			} else
				holder = (MyHolder) convertView.getTag();
			holder.tv_name.setText(list.get(position));
			if (list.get(position).equals(select)) {
				holder.iv_gou.setImageResource(R.drawable.xz2);
				holder.iv_gou.setVisibility(View.VISIBLE);
			} else {
				holder.iv_gou.setVisibility(View.GONE);
			}
			return convertView;
		}
	}

	static class MyHolder {
		TextView tv_name;
		ImageView iv_gou;
	}

	public interface SelectResultListener {
		void selectResult(HashMap<String, String> result);
	}
}
