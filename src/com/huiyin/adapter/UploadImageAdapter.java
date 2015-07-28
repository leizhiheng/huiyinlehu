package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.BitmapFactory.Options;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.ImageData;
import com.huiyin.utils.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * uploadImage图片adapter
 * 
 * @author 刘远祺
 * 
 */
public class UploadImageAdapter extends BaseAdapter {

	/**上下文**/
	private Activity context;

	/**数据**/
	private List<ImageData> dataList;
	
	private Map<Integer, ViewHolder> mapView = new HashMap<Integer, ViewHolder>();
	
	/**每一项的宽度**/
	private int itemWidth;
	
	/**最大显示数**/
	private int max = 5;
	
	/**隐藏删除小图标**/
	private boolean hidDelPic = false;
	
	private DisplayImageOptions mOptions;
	
	public UploadImageAdapter(Activity context, int parentWidth, int numColumns) {
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
	 * 因此添加按钮
	 */
	public void hidAddButton(){
		
		//max = 0就不会显示add图片
		this.max = 0;
		this.notifyDataSetChanged();
	}
	
	/**
	 * 隐藏删除小图标
	 */
	public void hidDelImgView(){
		hidDelPic = true;
	}
	
	
	/**
	 * 获取adapter刷新的数据
	 * @return
	 */
	public List<ImageData> getDataList(){
		return this.dataList;
	}
	
	/**
	 * 是否有需要上传的新图片文件
	 * @return
	 */
	public boolean hasNewUploadImgFile(){
		if(null == dataList){
			return false;
		}
		
		//如果有上传的文件
		for(ImageData imgData : dataList){
			if(imgData.localFileExist()){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 设置最大显示数
	 * @param max
	 */
	public void setMax(int max){
		this.max = max;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		int count = getPicCount();
		
		boolean showAdd = getPicCount() < max;
		
		//加1 是因为还有一个 + 按钮，用于添加图片
		count += (showAdd ? 1 : 0);
		return count; 
		//return 5;
	}

	/**
	 * 获取图片的数量
	 * @return
	 */
	public int getPicCount(){
		return null != dataList ? dataList.size() : 0; 
		//return 4;
	}
	
	/**
	 * 判断是否点击的加号图片
	 * @param position
	 * @return
	 */
	public boolean isClickAddPic(int position){
		return position == getPicCount();
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
		ViewHolder holder = null;
		convertView = LayoutInflater.from(context).inflate(R.layout.adapter_update_image, null);
		holder = new ViewHolder(convertView);
		convertView.setTag(holder);
		
//		if (null == convertView) {
//			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_update_image, null);
//			holder = new ViewHolder(convertView);
//			convertView.setTag(holder);
//		}
//		holder = (ViewHolder) convertView.getTag();
		
		
		//显示数据
		holder.show(position);
		
		return convertView;
	}

	class ViewHolder implements OnClickListener {

		public ImageView picImageView;	// 要上传的图片
		public ImageView delImageView; 	// 删除小图片
		
		private int currPosition;		//当前显示的图片下标地址
		
		ViewHolder(View convertView) {
			
			picImageView = (ImageView) convertView.findViewById(R.id.goods_pic_imageview);
			picImageView.getLayoutParams().width = itemWidth;
			picImageView.getLayoutParams().height = itemWidth;
			picImageView.setScaleType(ScaleType.CENTER_INSIDE);
			delImageView = (ImageView) convertView.findViewById(R.id.goods_delete_imageview);
			delImageView.setOnClickListener(this);
		}

		/**
		 * 显示商品信息
		 * @param GoodReplaceBean
		 */
		void show(int position){
			
			//记住当前下标索引
			currPosition = position;

			LogUtil.i("UploadImageAdapter", "position:" + position + "  getPicCount:" + getPicCount());
			if(position < getPicCount()){

				LogUtil.i("UploadImageAdapter", "load image");
				
				//如果不显示删除图标，则隐藏
				if(hidDelPic){
					delImageView.setVisibility(View.GONE);
				}else{
					delImageView.setVisibility(View.VISIBLE);
				}
				
				ImageData data = dataList.get(position);
				
				if(data.localFileExist()){
					
					//加载本地图片
					ImageLoader.getInstance().displayImage("file://" + data.fileName, picImageView, mOptions);
					LogUtil.i("UploadImageAdapter", "Position:"+position+" load local file img");
				}else{
					
					//加载网络图片
					ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + data.imgUrl, picImageView, mOptions);
					LogUtil.i("UploadImageAdapter", "Position:"+position+" load remote file img");
				}
				
				
			}else{

				LogUtil.i("UploadImageAdapter", "show add image");
				
				//加号图片
				picImageView.setImageResource(R.drawable.icon_add_pic);
				delImageView.setVisibility(View.GONE);

				LogUtil.i("UploadImageAdapter", "Position:"+position+" set add pic ");
			}
			
		}

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.goods_delete_imageview:
				
				//删除图片
				dataList.remove(currPosition);
				notifyDataSetChanged();
				
				break;
			}
		}
	}

	/**
	 * 获取上传完的图片
	 * @return
	 */
	public String getImages() {
		
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		for(int i=0; i<dataList.size(); i++){
			
			//排除空图片
			if(TextUtils.isEmpty(dataList.get(i).imgUrl)){
				continue;
			}
			
			if(!flag){
				flag = true;
				sb.append(dataList.get(i).imgUrl);
			}else{
				sb.append(","+dataList.get(i).imgUrl);
			}
		}
		return sb.toString();
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
	
}
