package com.huiyin.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.DataListEntityBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.ui.user.order.ReplaceDetailFinishActivity;
import com.huiyin.ui.user.order.ReplaceDetailReturnActivity;
import com.huiyin.ui.user.order.ReplaceDetailVerifyActivity;
import com.huiyin.ui.user.order.ReturnDetailFinishActivity;
import com.huiyin.ui.user.order.ReturnDetailReturnActivity;
import com.huiyin.ui.user.order.ReturnDetailVerifyActivity;
import com.huiyin.ui.user.order.ReturnRecordFragment;
import com.huiyin.utils.ImageManager;
import com.huiyin.wight.MyListView;

/**
 * 查看退换货订单列表适配器
 * 
 * @author 刘远祺
 * 
 * @todo TODO
 * 
 * @date 2015-7-3
 */
public class ReturnRecordAdapter extends BaseAdapter {
	private Activity context;
	private List<DataListEntityBean> orderList;

	public ReturnRecordAdapter(Activity context, List<DataListEntityBean> orderList) {
		this.context = context;
		this.orderList = orderList;
	}

	/**
	 * 刷新整个adapter
	 * @param orderList
	 */
	public void refreshData(List<DataListEntityBean> orderList) {
		this.orderList = orderList;
	}

	/**
	 * 将数据叠加到最后
	 * @param appendList
	 */
	public void appendData(List<DataListEntityBean> appendList){
		if(null != orderList){
			this.orderList.addAll(appendList);
			this.notifyDataSetChanged();
		}
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
					intent.putExtra(StoreHomeActivity.STORE_ID, orderList.get(position).getSTORE_ID());
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
		holder.tv_order_no.setText(orderList.get(position).getORDER_CODE());
		holder.tv_store_name.setText(orderList.get(position).getSTORE_NAME() + "  ");
		holder.tv_status.setText(orderList.get(position).getSTATUS_NAME());

		holder.tv_commit_order_time.setText("申请时间：" + orderList.get(position).getCREATE_DATE());
		ProductListAdapter adapter = new ProductListAdapter(orderList.get(position).getBespeakDetalList(), context);
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

		// 进度查询
		holder.btn_jdcx.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null == orderList) {
					return;
				}

				// 退换货ID,订单状态
				String id = orderList.get(position).getID() + "";
				int status = orderList.get(position).getSTATUSInt();

				switch (orderList.get(position).getFLAGInt()) {
				case 1:

					// 1 退货
					searchReturnDetail(status, id);

					break;
				case 2:

					// 跳转换货相关详情界面
					searchReplaceDetail(status, id);

					break;
				case 3:

					// 3预约
					// 这里不可能会出现预约数据，则不做任何处理

					break;
				}
			}
		});
		return convertView;
	}

	/**
	 * 查看换货进度
	 * 
	 * @param status
	 *            7 退款：取消 
	 *            8 退货：维权审核
	 *            9 退货：请退货 
	 *            10 退货：维权拒绝 
	 *            11 退货：买家已发货 
	 *            12 退货：商家退款中
	 *            13 退货：维权完成 
	 *            14 退货：维权取消
	 * @param returnId
	 */
	private void searchReturnDetail(int status, String returnId) {

		Intent intent = null;
		switch (status) {
		case 8:

			// 审核中
			intent = new Intent(context, ReturnDetailVerifyActivity.class);
			intent.putExtra(ReturnDetailVerifyActivity.EXTRA_RETURN_ID, returnId);
			context.startActivityForResult(intent, ReturnRecordFragment.REQUEST_CODE_RETURN);
			
			break;
		case 9:

			// 请退货
			intent = new Intent(context, ReturnDetailReturnActivity.class);
			intent.putExtra(ReturnDetailReturnActivity.EXTRA_RETURN_ID, returnId);
			context.startActivityForResult(intent, ReturnRecordFragment.REQUEST_CODE_RETURN);

			break;
		case 7:	//退货取消(这个状态没有物流，下面所有状态都有物流)
		case 10: // 维权拒绝
		case 11: // 买家已发货
		case 12: // 商家退款中
		case 13: // 维权完成
		case 14: // 维权取消
		case 33: // 商品检测
		default: // 其他未知状态
			
			intent = new Intent(context, ReturnDetailFinishActivity.class);
			intent.putExtra(ReturnDetailFinishActivity.EXTRA_RETURN_ID, returnId);
			context.startActivity(intent);
			
			break;
		}
	}

	/**
	 * 查看换货进度
	 * 
	 * @param status
	 *            15 换货：维权审核 
	 *            16 换货：买家请退货 未发货 
	 *            17 换货：维权拒绝 为发货 
	 *            18 换货：买家已发货 已发货 
	 *            19 换货：商品检测 已发货(等待商家收货) 
	 *            20 换货：商家发货 
	 *            21 换货：取消换货 
	 *            22 换货：买家收货(处理中) 
	 *            23 换货：维权完成
	 * @param replaceId
	 */
	private void searchReplaceDetail(int status, String replaceId) {
		Intent intent = null;
		switch (status) {
		case 15:

			// 审核中
			intent = new Intent(context, ReplaceDetailVerifyActivity.class);
			intent.putExtra(ReplaceDetailVerifyActivity.EXTRA_REPLACE_ID, replaceId);
			context.startActivityForResult(intent, ReturnRecordFragment.REQUEST_CODE_RETURN);
			break;
		case 16:

			// 请退货
			intent = new Intent(context, ReplaceDetailReturnActivity.class);
			intent.putExtra(ReplaceDetailReturnActivity.EXTRA_REPLACE_ID, replaceId);
			context.startActivityForResult(intent, ReturnRecordFragment.REQUEST_CODE_RETURN);
			break;
		case 17:
		case 18:
		case 19:
		case 20:
		case 21:
		case 22:
		case 23:
		default:

			// 换货完成
			intent = new Intent(context, ReplaceDetailFinishActivity.class);
			intent.putExtra(ReplaceDetailFinishActivity.EXTRA_REPLACE_ID, replaceId);
			context.startActivity(intent);
			break;
		}
	}

	static class OrderHolder {
		public ImageView iv_logo; // 店铺logo
		public TextView tv_store_name; // 店铺名称
		public TextView tv_status; // 状态
		public TextView tv_order_no; // 订单编号
		public TextView tv_commit_order_time;// 下单时间
		public TextView btn_jdcx; // 查看进度
		public MyListView lv_order; // 产品列表
	}

}
