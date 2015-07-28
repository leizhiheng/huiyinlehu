package com.huiyin.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.BespeakDetalListEntity;
import com.huiyin.utils.ImageManager;

public class ProductListAdapter extends BaseAdapter {

	private Context context;

	private List<BespeakDetalListEntity> productList;

	public ProductListAdapter(List<BespeakDetalListEntity> productList, Context context) {
		this.context = context;
		this.productList = productList;
	}

	@Override
	public int getCount() {
		return null != productList ? productList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return productList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyHolder holder = null;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_orderdetail_productlist_item, null);
			holder = new MyHolder();
			holder.iv_product = (ImageView) convertView.findViewById(R.id.iv_product);
			holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
			holder.tv_product_guige = (TextView) convertView.findViewById(R.id.tv_product_guige);
			convertView.setTag(holder);
		} else
			holder = (MyHolder) convertView.getTag();
		BespeakDetalListEntity product = productList.get(position);
		ImageManager.LoadWithServer(product.getGOODS_IMG(), holder.iv_product);
		holder.tv_product_name.setText(product.getGOODS_NAME());
		holder.tv_product_guige.setText(product.getNORMS_VALUE());
		return convertView;
	}

	class MyHolder {
		private ImageView iv_product;
		private TextView tv_product_name;
		private TextView tv_product_guige;
	}
}
