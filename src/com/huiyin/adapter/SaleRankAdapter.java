package com.huiyin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.ui.classic.SaleRankActivity.LvlBean.lBeanItem;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

public class SaleRankAdapter extends BaseAdapter {

	private Context mContext;

	private List<lBeanItem> listDatas;
	public LayoutInflater layoutInflater;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	public SaleRankAdapter(Context context, List<lBeanItem> listDatas) {
		layoutInflater = LayoutInflater.from(context);
		mContext = context;

		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).showStubImage(R.drawable.image_default_gallery).showImageForEmptyUri(R.drawable.image_default_gallery)
						.showImageOnFail(R.drawable.image_default_gallery).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader = ImageLoader.getInstance();

		this.listDatas = listDatas;
	}

	@Override
	public int getCount() {
		return null != listDatas ? listDatas.size() : 0;
	}

	@Override
	public Object getItem(int arg0) {
		return listDatas.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		lBeanItem bean = listDatas.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.activity_scale_item, null);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.rank = (TextView) convertView.findViewById(R.id.rank);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.count = (TextView) convertView.findViewById(R.id.count);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.rank.setText("NO."+(position + 1) + "");
		holder.price.setText(MathUtil.priceForAppWithSign(bean.PRICE));
		holder.title.setText(bean.COMMODITY_NAME);
		holder.count.setText(bean.SALES_VOLUME);

		holder.price.setText(MathUtil.priceForAppWithSign(bean.PRICE));
        //统一图片获取方式用于省流量设置
        ImageManager.LoadWithServer(URLs.IMAGE_URL + bean.COMMODITY_IMAGE_PATH, holder.image, options);
		return convertView;
	}

	private class ViewHolder {
		TextView rank;
		ImageView image;
		TextView title;
		TextView price;
		TextView count;
	}

}