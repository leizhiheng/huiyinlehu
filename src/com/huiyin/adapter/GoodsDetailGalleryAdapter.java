package com.huiyin.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huiyin.R;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.utils.ImageManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoodsDetailGalleryAdapter extends PagerAdapter {

	@SuppressLint("UseSparseArrays")
	private Map<Integer, View> views = new HashMap<Integer, View>();
	private ArrayList<String> listDatas;
	private Activity activity;
	private boolean isQuota = false;
	
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	public GoodsDetailGalleryAdapter(List<String> listDatas, Activity activity) {
		this.listDatas = (ArrayList<String>) listDatas;
		this.activity = activity;
		
		imageLoader = ImageLoader.getInstance();

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true)
				.showStubImage(R.drawable.image_default_gallery)
				.showImageForEmptyUri(R.drawable.image_default_gallery)
				.showImageOnFail(R.drawable.image_default_gallery)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
		views.remove(position);
	}

	@Override
	public int getCount() {
		if (listDatas != null) {
			return listDatas.size();
		}
		return 0;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View v = LayoutInflater.from(activity).inflate(
				R.layout.fragment_goods_detail_baseinfo_top_image, null);
		ImageView mImageView = (ImageView) v
				.findViewById(R.id.main_image);
		ImageView xiangou = (ImageView) v
				.findViewById(R.id.xiangou);
//		xiangou.setVisibility(isQuota?View.VISIBLE:View.INVISIBLE);
		xiangou.setVisibility(View.INVISIBLE);//功能暂时去掉，隐藏
		String imageUrl = listDatas.get(position);
        //统一图片获取方式用于省流量设置
        ImageManager.LoadWithServer(imageUrl,mImageView,options);
		
		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(activity,
//						GoodsDetailGalleryActivity.class);
//				intent.putStringArrayListExtra(
//						GoodsDetailGalleryActivity.INTENT_KEY_GALLERYS,
//						listDatas);
				Intent intent = new Intent(activity,
						PhotoViewActivity.class);
				intent.putStringArrayListExtra(
						PhotoViewActivity.INTENT_KEY_PHOTO,
						listDatas);
				intent.putExtra(PhotoViewActivity.INTENT_KEY_POSITION, position);
				activity.startActivity(intent);
			}
		});
		views.put(position, v);
		container.addView(v);
		return v;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	public void setQuota(boolean isQuota) {
		this.isQuota = isQuota;
	}

}
