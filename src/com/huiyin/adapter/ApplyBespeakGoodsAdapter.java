package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.bean.GoodBean;
import com.huiyin.utils.ImageManager;

/**
 * 预约申请-商品adapter
 * 
 * @author 刘远祺
 * 
 */
public class ApplyBespeakGoodsAdapter extends BaseAdapter {

	
	private Context context;
	
	/**上下文**/
	private List<GoodBean> dataList;

	/**选中的商品**/
	private Map<String, GoodBean> checkedGoods;
	
	public ApplyBespeakGoodsAdapter(Context context, List<GoodBean> dataList) {
		this.context = context;
		this.dataList = dataList;
		this.checkedGoods = new HashMap<String, GoodBean>();
	}

	/**
	 * 刷新数据
	 * @param dataList
	 */
	public void refreshData(List<GoodBean> dataList) {
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
	 * @param goodBean
	 */
	public void checkProduct(GoodBean goodBean){
		
		if(null == goodBean){
			return;
		}
		
		if(!checkedGoods.containsKey(goodBean.GOODS_ID)){
			
			checkedGoods.put(goodBean.GOODS_ID, goodBean);
		}else{
			checkedGoods.remove(goodBean.GOODS_ID);
		}
		
		//刷新
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_apply_bespeak, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();
		
		//显示数据
		holder.show(dataList.get(position), position);
		
		return convertView;
	}

	class ViewHolder implements OnClickListener {

		public ImageView iv_check;	// 选中的图标
		public ImageView iv_logo; 	// 商品logo
		public TextView tv_name; 	// 商品名称
		public TextView tv_norms; 	// 商品规格
		
		public ImageView addImg;	// 加
		public ImageView subImg; 	// 减
		public TextView numberTv; 	// 数量
		
		public View line; 			// 线条
		
		private GoodBean goodBean; 	//当前显示的人商品
		
		ViewHolder(View convertView) {
			iv_check = (ImageView) convertView.findViewById(R.id.goods_check_imageview);
			iv_logo = (ImageView) convertView.findViewById(R.id.goods_pic_imageview);
			tv_name = (TextView) convertView.findViewById(R.id.goods_name_tv);
			tv_norms = (TextView) convertView.findViewById(R.id.norms_value_tv);
			line = convertView.findViewById(R.id.line);
			
			addImg = (ImageView) convertView.findViewById(R.id.apply_add_imageview);
			subImg = (ImageView) convertView.findViewById(R.id.apply_sub_imageview);
			numberTv = (TextView) convertView.findViewById(R.id.apply_product_count_textview);
		
			addImg.setOnClickListener(this);
			subImg.setOnClickListener(this);
		}

		/**
		 * 显示商品信息
		 * @param goodBean
		 */
		void show(GoodBean goodBean, int position){
			
			//记住当前的商品
			this.goodBean = goodBean;
			
			//名称，规格
			ImageManager.LoadWithServer(goodBean.GOODS_IMG, iv_logo);
			tv_name.setText(goodBean.GOODS_NAME);
			tv_norms.setText(goodBean.NORMS_VALUE);
			numberTv.setText(goodBean.getBespeakNumber()+"");
			
			//选中状态
			boolean isChecked = checkedGoods.containsKey(goodBean.GOODS_ID);
			int vis = isChecked ? View.VISIBLE : View.GONE;
			iv_check.setVisibility(vis);
			line.setVisibility(position == (getCount()-1) ? View.GONE : View.VISIBLE);
			
		}

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.apply_add_imageview:
				
				//加
				if(goodBean.addBespeak(1)){
					this.numberTv.setText(goodBean.getBespeakNumber()+"");
				}else{
					Toast.makeText(context, "预约数不能大于购买数", Toast.LENGTH_SHORT).show();
				}
				
				break;

			case R.id.apply_sub_imageview:
				
				//减
				if(goodBean.addBespeak(-1)){
					this.numberTv.setText(goodBean.getBespeakNumber()+"");
				}else{
					Toast.makeText(context, "预约数量至少1件", Toast.LENGTH_SHORT).show();
				}
				
				break;
			}
		}
	}

	/**
	 * 获取被选中的商品
	 * @return
	 */
	public List<GoodBean> getCheckedGoods() {
		List<GoodBean> goodBeans = new ArrayList<GoodBean>();
		goodBeans.addAll(checkedGoods.values());
		return goodBeans;
	}
}
