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
import com.huiyin.bean.ReturnDetailBean.GoodReturnBean;
import com.huiyin.utils.ImageManager;

/**
 * 换货详情-商品adapter
 * 
 * @author 刘远祺
 * 
 */
public class ReturnDetailGoodsAdapter extends BaseAdapter {
	
	private Context context;
	
	/**上下文**/
	private List<GoodReturnBean> dataList;

	/**选中的商品**/
	private Map<String, GoodReturnBean> checkedGoods;
	
	public ReturnDetailGoodsAdapter(Context context, List<GoodReturnBean> dataList) {
		this.context = context;
		this.dataList = dataList;
		this.checkedGoods = new HashMap<String, GoodReturnBean>();
	}

	/**
	 * 刷新数据
	 * @param dataList
	 */
	public void refreshData(List<GoodReturnBean> dataList) {
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
	 * @param GoodReturnBean
	 */
	public void checkProduct(GoodReturnBean GoodReturnBean){
		
		if(null == GoodReturnBean){
			return;
		}
		
		if(!checkedGoods.containsKey(GoodReturnBean.GOODS_ID)){
			
			checkedGoods.put(GoodReturnBean.GOODS_ID, GoodReturnBean);
		}else{
			checkedGoods.remove(GoodReturnBean.GOODS_ID);
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
		 * @param GoodReturnBean
		 */
		void show(GoodReturnBean GoodReturnBean, int position){
			
			//名称，规格
			ImageManager.LoadWithServer(GoodReturnBean.GOODS_IMG, iv_logo);
			tv_name.setText(GoodReturnBean.GOODS_NAME);
			tv_norms.setText(GoodReturnBean.NORMS_VALUE);
			numberTv.setText(GoodReturnBean.QUANTITY);
			
			//选中状态
			boolean isChecked = checkedGoods.containsKey((GoodReturnBean.GOODS_ID));
			int vis = isChecked ? View.VISIBLE : View.GONE;
			iv_check.setVisibility(vis);
			line.setVisibility(position == (getCount()-1) ? View.GONE : View.VISIBLE);
			
		}
	}

	/**
	 * 获取被选中的商品
	 * @return
	 */
	public List<GoodReturnBean> getCheckedGoods() {
		List<GoodReturnBean> GoodReplaceBeans = new ArrayList<GoodReturnBean>();
		GoodReplaceBeans.addAll(checkedGoods.values());
		return GoodReplaceBeans;
	}
}
