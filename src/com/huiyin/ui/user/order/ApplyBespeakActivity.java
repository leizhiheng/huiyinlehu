package com.huiyin.ui.user.order;

import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.ApplyBespeakGoodsAdapter;
import com.huiyin.adapter.ApplyBespeakTypeAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.GoodBean;
import com.huiyin.bean.OrderBespeakInfoBean;
import com.huiyin.ui.user.YuYueDetailActivity;
import com.huiyin.utils.JSONParseUtils;

/**
 * 申请预约</br>
 * 
 * @author 刘远祺
 */
public class ApplyBespeakActivity extends BaseActivity implements OnClickListener, OnItemClickListener{

	//Intent 参数
	/** 订单Id**/
	public static final String EXTRA_ORDERID 	= "orderCode";
	
	/** 订单编号**/
	public static final String EXTRA_NUMBER 	= "orderNumber";
	
	/**订单ID**/
	private static final int FLAG_ORDER_ID 		= 1;
	
	/**订单编号**/
	private static final int FLAG_ORDER_NUMBER 	= 2;
	
	
	
	/**订单编号**/
	private TextView orderNumberTv;
	
	/**商品集合**/
	private ListView productListView;
	
	/**预约类型**/
	private ListView typeListView;
	
	/**联系-姓名**/
	private TextView contactInfoNameTv;
	
	/**联系-手机号码**/
	private TextView contactInfo_mobile_tv;
	
	/**联系-地址**/
	private TextView contact_info_address;
	
	/**备注信息**/
	private TextView remark_info;
	
	/**备注信息提示**/
	private TextView remark_info_tip;
	
	/**提交申请**/
	private Button submitApplyBtn;

	/**商品数据适配器**/
	private ApplyBespeakGoodsAdapter goodsAdapter;
	
	/**预约类型适配器**/
	private ApplyBespeakTypeAdapter typeAdapter;
	
	/**订单信息**/
	private OrderBespeakInfoBean mOrderBespeakInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_bespeak);

		// 初始化控件
		initViews();
		
		//初始化数据
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData(){
		
		Intent intent = getIntent();
		if(null != intent && intent.hasExtra(EXTRA_ORDERID));{
			String orderId = intent.getStringExtra(EXTRA_ORDERID);
			String orderNumber= intent.getStringExtra(EXTRA_NUMBER);
			if(!TextUtils.isEmpty(orderId)){
				
				//显示订单编号
				orderNumberTv.setText(orderNumber);
				
				//加载订单信息
				loadOrderBespeakInfo(orderId, FLAG_ORDER_ID);
			}
		}
		
	}
	
	
	/**
	 * 绑定控件
	 */
	private void initViews() {

		//查找控件
		orderNumberTv = (TextView) findViewById(R.id.order_number_tv);
		productListView = (ListView) findViewById(R.id.product_listView);
		typeListView = (ListView) findViewById(R.id.type_listView);
		contactInfoNameTv = (TextView) findViewById(R.id.contact_info_name);
		contactInfo_mobile_tv = (TextView) findViewById(R.id.contact_info_mobile);
		contact_info_address = (TextView) findViewById(R.id.contact_info_address);
		remark_info = (TextView) findViewById(R.id.remark_info);
		remark_info_tip = (TextView) findViewById(R.id.remark_info_tip);
		submitApplyBtn =  (Button) findViewById(R.id.submit_apply_btn);
		setTextChangeListener(remark_info, remark_info_tip, 300);
		
		//点击事件
		findViewById(R.id.search_view).setOnClickListener(this);
		submitApplyBtn.setOnClickListener(this);
		productListView.setOnItemClickListener(this);
		typeListView.setOnItemClickListener(this);
		
		//商品数据
		goodsAdapter = new ApplyBespeakGoodsAdapter(mContext, null);
		productListView.setAdapter(goodsAdapter);
		
		//预约类型数据
		typeAdapter = new ApplyBespeakTypeAdapter(mContext, null);
		typeListView.setAdapter(typeAdapter);
	}

	
	/**
	 * 显示订单预约信息
	 * @param orderBespeakInfo
	 */
	private void showOrderBeseakInfoBean(OrderBespeakInfoBean orderBespeakInfo){
		if(null != orderBespeakInfo){
			
			//商品
			goodsAdapter.refreshData(orderBespeakInfo.goodsList);
			
			//类型
			typeAdapter.refreshData(orderBespeakInfo.reservationType);
			typeAdapter.checkPosition(0);
			
			//联系信息
			orderNumberTv.setText(orderBespeakInfo.ORDER_CODE);
			contactInfoNameTv.setText(orderBespeakInfo.CONSIGNEE_NAME);
			contactInfo_mobile_tv.setText(orderBespeakInfo.CONSIGNEE_PHONE);
			contact_info_address.setText(orderBespeakInfo.CONSIGNEE_ADDRESS);

			//备注信息，只在提交的时候传
			
		}
	}
	
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.search_view:

			// 搜索
			String orderNumber = getText(orderNumberTv);
			if(TextUtils.isEmpty(orderNumber)){
				
				showMyToast("请输入订单编号");
				return;
			}
			
			loadOrderBespeakInfo(orderNumber, FLAG_ORDER_NUMBER);

			break;
		case R.id.submit_apply_btn:
			
			//提交申请
			if(validateBespeakInput()){
				
				addBespeak();
			}
			
			break;
		}
	}

	/**
	 * 提交申请的 输入有效性验证
	 * @return
	 */
	private boolean validateBespeakInput(){
		
		String orderId = getText(orderNumberTv);
		String typeId = typeAdapter.getCurTypeId();
		String phone = getText(contactInfo_mobile_tv);
		String name = getText(contactInfoNameTv);
		String address = getText(contact_info_address);
		String remarks = getText(remark_info);
		List<GoodBean> goods = goodsAdapter.getCheckedGoods();
		
		if(TextUtils.isEmpty(orderId)){
			
			showMyToast("请输入订单编号");
			return false;
		}
		
		if(TextUtils.isEmpty(typeId)){
			
			showMyToast("请选择预约类型");
			return false;
		}
		
		if(TextUtils.isEmpty(phone)){
			
			showMyToast("请输入联系人手机");
			return false;
		}
		
		if(TextUtils.isEmpty(name)){
			
			showMyToast("请输入联系人姓名");
			return false;
		}
		
		if(TextUtils.isEmpty(address)){
			
			showMyToast("请输入联系地址");
			return false;
		}
		
		if(null == goods || goods.size() == 0){
			
			showMyToast("请选择商品");
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * 预约信息：通过订单id/订单编号查询
	 * @param orderInfo 订单id/编号
	 * @param flag 1订单id，2订单编号
	 */
	private void loadOrderBespeakInfo(String orderInfo, int flag){
		
		RequstClient.orderBespeakInfo(new CustomResponseHandler(mContext, true) {
			@Override
			public void onRefreshData(String content) {
				
				//解析json
				mOrderBespeakInfo = OrderBespeakInfoBean.explainJson(content, mContext);
				if(mOrderBespeakInfo.type == 1){
					//显示订单预约数据
					showOrderBeseakInfoBean(mOrderBespeakInfo);
				}else{
					
					//显示服务器返回的异常信息
					showMyToast(mOrderBespeakInfo.msg);
				}
			}

		},orderInfo, flag, AppContext.getInstance().userId);
	}
	
	
	/**
	 * 添加预约 
	 */
	private void addBespeak(){
		
		if(null == mOrderBespeakInfo){
			return;
		}
		
		//获取参数
		String orderId = mOrderBespeakInfo.ORDER_ID;
		String typeId = typeAdapter.getCurTypeId();
		String userId = AppContext.getInstance().userId;
		String phone = getText(contactInfo_mobile_tv);
		String name = getText(contactInfoNameTv);
		String address = getText(contact_info_address);
		String remarks = getText(remark_info);
		List<GoodBean> goods = goodsAdapter.getCheckedGoods();
		String origin = "3";
		
		//请求添加预约
		RequstClient.addBespeak(new CustomResponseHandler(mContext, true) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content){
				
				//得到状态
				int type = JSONParseUtils.getInt(content, "type");
				
				//预约ID
				String bespeakId = JSONParseUtils.getString(content, "bespeakId");
				
				if(1 == type){
					
					showMyToast("预约成功");
					
					//通知上一个界面刷新数据
					setResult(RESULT_OK);
					
					//跳转到预约详情
					Intent intent = new Intent(mContext, YuYueDetailActivity.class);
					intent.putExtra(YuYueDetailActivity.EXTRA_BESPEAKID, bespeakId);
					startActivity(intent);
					
					finish();
					
				}else{
					showMyToast("预约失败");
				}
			}

		}, orderId, typeId, userId, phone, name, address, remarks, goods, origin);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		switch (arg0.getId()) {
		case R.id.product_listView:
			
			//商品
			GoodBean goodBean = (GoodBean)goodsAdapter.getItem(arg2);
			goodsAdapter.checkProduct(goodBean);
			
			break;

		case R.id.type_listView:
			
			//预约类型
			typeAdapter.checkPosition(arg2);
			
			break;
		}
		
	}
	
}
