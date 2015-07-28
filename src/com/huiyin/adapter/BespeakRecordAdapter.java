package com.huiyin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.BespeakDetalListEntity;
import com.huiyin.bean.DataListEntityBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.ui.user.YuYueDetailActivity;
import com.huiyin.ui.user.order.ReplaceDetailVerifyActivity;
import com.huiyin.ui.user.order.ReplaceDetailReturnActivity;
import com.huiyin.ui.user.order.ReplaceDetailFinishActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.wight.MyListView;

import java.util.List;

/**
 * 查看预约-订单适配器
 */
public class BespeakRecordAdapter extends BaseAdapter {
	private Context context;
	private List<DataListEntityBean> orderList;

	public BespeakRecordAdapter(Context context, List<DataListEntityBean> orderList) {
		this.context = context;
		this.orderList = orderList;
	}

	public void setOrderList(List<DataListEntityBean> orderList) {
		this.orderList = orderList;
	}

	@Override
	public int getCount() {
		return null != orderList ? orderList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return orderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		OrderHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_bespeak_return_lv_item, null);
			holder = new OrderHolder();
			holder.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
			holder.tv_store_name = (TextView) convertView.findViewById(R.id.tv_store_name);
			holder.btn_jdcx = (TextView) convertView.findViewById(R.id.btn_jdcx);
			holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
			holder.tv_commit_order_time = (TextView) convertView.findViewById(R.id.tv_commit_order_time);
			holder.tv_order_no = (TextView) convertView.findViewById(R.id.tv_order_no);
			holder.lv_order = (MyListView) convertView.findViewById(R.id.lv_order);
			convertView.setTag(holder);
		}else{
			holder = (OrderHolder) convertView.getTag();
		}
		if (Integer.parseInt(orderList.get(position).getSTORE_ID()) > 0) {// 店铺
			ImageManager.LoadWithServer(orderList.get(position).getSTORE_LOGO(), holder.iv_logo);
			holder.tv_store_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_black_arraw, 0);
			holder.tv_store_name.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, StoreHomeActivity.class);
					intent.putExtra(StoreHomeActivity.STORE_ID, MathUtil.stringToInt(orderList.get(position).getSTORE_ID()));
					context.startActivity(intent);
				}
			});
		} else {// 乐虎自营
			holder.tv_store_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			holder.tv_store_name.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			holder.iv_logo.setImageResource(R.drawable.lehu);
		}
		holder.tv_order_no.setText(orderList.get(position).getBESPEAK_CODE());
		holder.tv_store_name.setText(orderList.get(position).getSTORE_NAME() + "  ");
		holder.tv_status.setText(orderList.get(position).getSTATUS_NAME());
		holder.tv_commit_order_time.setText("申请时间：" + orderList.get(position).getCREATE_DATE());
		ProductListAdapter adapter = new ProductListAdapter(orderList.get(position).getBespeakDetalList());
		holder.lv_order.setAdapter(adapter);
		holder.lv_order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int childPosition, long id) {
				Intent intent = new Intent(context, ProductsDetailActivity.class);
				intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, orderList.get(position).getBespeakDetalList().get(childPosition).getGOODS_ID() + "");
				intent.putExtra(ProductsDetailActivity.GOODS_NO, orderList.get(position).getBespeakDetalList().get(childPosition).getGOODS_NO() + "");
				intent.putExtra(ProductsDetailActivity.STORE_ID, orderList.get(position).getBespeakDetalList().get(childPosition).getSTORE_ID() + "");
				context.startActivity(intent);
			}
		});

		// 进度查询(查询预约进度)
		holder.btn_jdcx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == orderList) {
					return;
				}
				String id = orderList.get(position).getID() + "";
				Intent intent;
				switch (orderList.get(position).getFLAGInt()) {
				case 1:// 1 退货
				case 2:// 2 换货

					// 查看预约订单列表，不可能会出现退货，换货，数据，所已这里不做任何处理(^_^)

					break;
				case 3:// 3预约

					// 跳转到预约详情
					intent = new Intent(context, YuYueDetailActivity.class);
					intent.putExtra(YuYueDetailActivity.EXTRA_BESPEAKID, id);
					context.startActivity(intent);
					break;
				}
			}
		});
		return convertView;
	}

	static class OrderHolder {
		public ImageView iv_logo;// 店铺logo
		public TextView tv_store_name;// 店铺名称
		public TextView tv_status;// 状态
		public TextView tv_order_no;// 订单编号
		public TextView tv_commit_order_time;// 下单时间
		public TextView btn_jdcx;// 查看进度
		public MyListView lv_order;// 产品列表
	}

	class ProductListAdapter extends BaseAdapter {

		private List<BespeakDetalListEntity> productList;

		public ProductListAdapter(List<BespeakDetalListEntity> productList) {
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

	}

	static class MyHolder {
		private ImageView iv_product;
		private TextView tv_product_name;
		private TextView tv_product_guige;
	}
}
