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
import com.huiyin.bean.BrowseItem;
import com.huiyin.bean.GoodLikeBean;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

/**
 * 喜欢商品适配器
 * 
 * @author 刘远祺
 * 
 */
public class GoodLikeAdapter extends BaseAdapter {

	private Context mContext;

	private List<GoodLikeBean> listDatas;
	public LayoutInflater layoutInflater;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private int listSize = 0;

	public GoodLikeAdapter(Context context, List<GoodLikeBean> listDatas) {
		layoutInflater = LayoutInflater.from(context);
		mContext = context;

		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).showStubImage(R.drawable.image_default_gallery).showImageForEmptyUri(R.drawable.image_default_gallery).showImageOnFail(R.drawable.image_default_gallery).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader = ImageLoader.getInstance();

		this.listDatas = listDatas;
	}

	@Override
	public int getCount() {
		return listDatas.size() > 6 ? 6 : listDatas.size();
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
		GoodLikeBean bean = listDatas.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.adapter_good_like, null);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.price = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(bean.GOODS_NAME+"\n");
		holder.price.setText(MathUtil.priceForAppWithSign(bean.PRICE));
		// 统一图片加载方式用于省流量处理
		ImageManager.LoadWithServer(URLs.IMAGE_URL + bean.GOODS_IMG, holder.image, options);

		return convertView;
	}

	private class ViewHolder {
		ImageView image;
		TextView name;
		TextView price;
	}

	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

}