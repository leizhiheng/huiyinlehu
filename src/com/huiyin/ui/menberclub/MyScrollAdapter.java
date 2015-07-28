package com.huiyin.ui.menberclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.VIPInfoBean;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.Utils;
import com.huiyin.wight.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 2015/6/16.
 */
public class MyScrollAdapter extends BaseAdapter {

	private Context mContext;
	private List<VIPInfoBean.RecordDataEntity> mEntities;
	private final DisplayImageOptions mOptions;

	public MyScrollAdapter(Context mContext,
			List<VIPInfoBean.RecordDataEntity> mEntities) {
		this.mContext = mContext;
        setEntities(mEntities);
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_head)
				.showImageForEmptyUri(R.drawable.icon_head)
				.showImageOnFail(R.drawable.icon_head)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

    public void setEntities(List<VIPInfoBean.RecordDataEntity> temp) {
        if (mEntities==null){
            mEntities=new ArrayList<VIPInfoBean.RecordDataEntity>();
        }
        mEntities.clear();
        for (int i = 0; i < temp.size(); i++) {
            mEntities.add(temp.get(i));
        }
    }

    @Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Object getItem(int i) {
		return mEntities.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(
					R.layout.my_scroll_list_item, null);
			holder = new ViewHolder();
			holder.iv_icon = (CircleImageView) view.findViewById(R.id.iv_icon);
			holder.my_scroll_textview = (TextView) view
					.findViewById(R.id.my_scroll_textview);
			holder.my_scroll_textview_time = (TextView) view
					.findViewById(R.id.my_scroll_textview_time);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (mEntities==null||mEntities.size()==0)return view;
		VIPInfoBean.RecordDataEntity mRecordDataEntity = mEntities.get(i
				% mEntities.size());
		ImageManager.LoadWithServer(mRecordDataEntity.getUSER_URL(),
				holder.iv_icon, mOptions);
		holder.my_scroll_textview.setText(getRecordString(mRecordDataEntity));
		holder.my_scroll_textview_time.setText(Utils.getSpecialTime("yyyy-MM-dd HH:mm",mRecordDataEntity
                .getCREATETIME()));
		return view;
	}

	private String getRecordString(VIPInfoBean.RecordDataEntity mEntity) {

		StringBuilder mBuilder = new StringBuilder();
		String user;
		if (Utils.checkPhoneREX(mEntity.getUSER_NAME())) {
			// 如果用户名是手机号码，就需要加密手机号码
			user = mEntity.getUSER_NAME().substring(0, 3) + "****"
					+ mEntity.getUSER_NAME().substring(8, 11);
		} else {
			user = mEntity.getUSER_NAME();
		}
		String goods = mEntity.getGIFT_NAME();
		mBuilder.append(user).append("   成功兑换   ").append("\"").append(goods)
				.append("\"");
		return mBuilder.toString();
	}

	class ViewHolder {
		CircleImageView iv_icon;
		TextView my_scroll_textview, my_scroll_textview_time;
	}
}
