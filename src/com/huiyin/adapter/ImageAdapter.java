package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.ImageData;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.imageupload.BitmapUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * uploadImage图片adapter
 * 
 * @author 刘远祺
 * 
 */
public class ImageAdapter extends BaseAdapter {

	/**上下文**/
	private Activity context;

	/**数据**/
	private List<ImageData> dataList;
	
	private DisplayImageOptions mOptions;
	
	/**每一项的宽度**/
	private int itemWidth;
	
	public ImageAdapter(Activity context, int parentWidth, int numColumns) {
		this.context = context;
		this.dataList = new ArrayList<ImageData>();
		itemWidth = (parentWidth/numColumns) - 25;
		
		Options bitmapOptions = new Options();
		bitmapOptions.inSampleSize = 8;
		mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showStubImage(0)
				.showImageForEmptyUri(0)
				.decodingOptions(bitmapOptions)
				.showImageOnFail(0).build();
	}

	/**
	 * 刷新数据
	 * @param dataList
	 */
	public void refreshData(List<ImageData> dataList) {
		this.dataList = dataList;
		this.notifyDataSetChanged();
	}

	/**
	 * 添加一个数据到最后
	 * @param data
	 */
	public void appendData(ImageData data){
		this.dataList.add(data);
		this.notifyDataSetChanged();
	}
	
	
	/**
	 * 获取adapter刷新的数据
	 * @return
	 */
	public List<ImageData> getDataList(){
		return this.dataList;
	}
	
	/**
	 * 获取网络资源图片集合
	 * @return
	 */
	public ArrayList<String> getImageUrlList(){
		ArrayList<String> list = new ArrayList<String>();
		for(int i=0; i<dataList.size(); i++){
			list.add(URLs.IMAGE_URL + dataList.get(i).imgUrl);
		}
		
		return list;
	}
	
	@Override
	public int getCount() {
		return getPicCount();
	}

	/**
	 * 获取图片的数量
	 * @return
	 */
	public int getPicCount(){
		return null != dataList ? dataList.size() : 0; 
	}
	
	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_update_lookimage, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		
		//显示数据
		holder.show(position);
		
		return convertView;
	}

	class ViewHolder {

		
		public ImageView picImageView;	// 要上传的图片
		
		ViewHolder(View convertView) {
			
			picImageView = (ImageView) convertView.findViewById(R.id.goods_pic_imageview);
			picImageView.setScaleType(ScaleType.FIT_XY);
			
			View prentView = convertView.findViewById(R.id.goods_pic_relativelayout);
			LayoutParams params = prentView.getLayoutParams();
			params.width = itemWidth;
			params.height = itemWidth;
			prentView.setLayoutParams(params);
			
		}

		/**
		 * 显示商品信息
		 * @param GoodReplaceBean
		 */
		void show(int position){
			
			//普通的图片
			picImageView.setBackgroundResource(0);
			ImageData data = dataList.get(position);
			if(data.localFileExist()){
				//加载本地图片
				ImageLoader.getInstance().displayImage("file://" + data.fileName, picImageView, mOptions);
			}else{
				
				//加载网络图片
				ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + data.imgUrl, picImageView);
			}
			
		}
	}

	/**
	 * 获取上传完的图片
	 * @return
	 */
	public String getImages() {
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<dataList.size(); i++){
			if(i==0){
				sb.append(dataList.get(i).imgUrl);
			}else{
				sb.append(","+dataList.get(i).imgUrl);
			}
		}
		return sb.toString();
	}

}
