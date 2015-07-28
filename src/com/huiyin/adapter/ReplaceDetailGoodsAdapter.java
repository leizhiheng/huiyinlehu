package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.GoodReplaceBean;
import com.huiyin.utils.ImageManager;

/**
 * 换货详情-商品adapter
 * 
 * @author 刘远祺
 * 
 */
public class ReplaceDetailGoodsAdapter extends BaseAdapter {
	
	private Context context;
	
	/**上下文**/
	private List<GoodReplaceBean> dataList;

	/**选中的商品**/
	private Map<String, GoodReplaceBean> checkedGoods;
	
	public ReplaceDetailGoodsAdapter(Context context, List<GoodReplaceBean> dataList) {
		this.context = context;
		this.dataList = dataList;
		this.checkedGoods = new HashMap<String, GoodReplaceBean>();
	}

	/**
	 * 刷新数据
	 * @param dataList
	 */
	public void refreshData(List<GoodReplaceBean> dataList) {
		this.dataList = dataList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
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

	/**
	 * 选中商品
	 * @param goodReplaceBean
	 */
	public void checkProduct(GoodReplaceBean goodReplaceBean){
		
		if(null == goodReplaceBean){
			return;
		}
		
		if(!checkedGoods.containsKey(goodReplaceBean.GOODS_ID)){
			
			checkedGoods.put(goodReplaceBean.GOODS_ID, goodReplaceBean);
		}else{
			checkedGoods.remove(goodReplaceBean.GOODS_ID);
		}
		
		//刷新
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_replace_detail_goods, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		
		//显示数据
		holder.show(dataList.get(position), position);
		
		return convertView;
	}

	class ViewHolder {

		public ImageView iv_check;	// 选中的图标
		public ImageView iv_logo; 	// 商品logo
		public TextView tv_name; 	// 商品名称
		public TextView tv_norms; 	// 商品规格
		
		public TextView numberTv; 	// 数量
		
		public View line; 			// 线条
		
		ViewHolder(View convertView) {
			iv_check = (ImageView) convertView.findViewById(R.id.goods_check_imageview);
			iv_logo = (ImageView) convertView.findViewById(R.id.goods_pic_imageview);
			tv_name = (TextView) convertView.findViewById(R.id.goods_name_tv);
			tv_norms = (TextView) convertView.findViewById(R.id.norms_value_tv);
			line = convertView.findViewById(R.id.line);
			
			numberTv = (TextView) convertView.findViewById(R.id.apply_product_count_textview);
		}

		/**
		 * 显示商品信息
		 * @param GoodReplaceBean
		 */
		void show(GoodReplaceBean GoodReplaceBean, int position){
			
			//名称，规格
			ImageManager.LoadWithServer(GoodReplaceBean.GOODS_IMG, iv_logo);
			tv_name.setText(GoodReplaceBean.GOODS_NAME);
			tv_norms.setText(GoodReplaceBean.NORMS_VALUE);
			numberTv.setText(GoodReplaceBean.QUANTITY);
			
			//选中状态
			boolean isChecked = checkedGoods.containsKey((GoodReplaceBean.GOODS_ID));
			int vis = isChecked ? View.VISIBLE : View.GONE;
			iv_check.setVisibility(vis);
			line.setVisibility(position == (getCount()-1) ? View.GONE : View.VISIBLE);
			
		}
	}

	/**
	 * 获取被选中的商品
	 * @return
	 */
	public List<GoodReplaceBean> getCheckedGoods() {
		List<GoodReplaceBean> GoodReplaceBeans = new ArrayList<GoodReplaceBean>();
		GoodReplaceBeans.addAll(checkedGoods.values());
		return GoodReplaceBeans;
	}
}
