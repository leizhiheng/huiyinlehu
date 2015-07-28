package com.huiyin.ui.user.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.ReplaceGoodsAddAdapter;
import com.huiyin.adapter.ReplaceTypeAdapter;
import com.huiyin.adapter.UploadImageAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.GoodsListEntity;
import com.huiyin.bean.ImageData;
import com.huiyin.bean.OrderRecordBean;
import com.huiyin.bean.ReplaceListEntity;
import com.huiyin.bean.ReservationType;
import com.huiyin.bean.ReturnListEntity;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow.IOnItemClick;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.huiyin.wight.MyListView;

/**
 * <b>申请售后 (换货，退货)</b></br>
 * Created by lian on 2015/5/29.
 */
public class ReplaceAddActivity extends BaseCameraActivity implements OnItemClickListener, OnClickListener {

	/** 数据模型 **/
	public static final String EXTRA_MODEL 			= "model";
	
	/** 订单ID **/
	public static final String EXTRA_ORDERID 		= "orderId";
	
	/** 售后类型 **/
	public static final String EXTRA_SEARCH_TYPE 	= "searchType";
	
	/** 换货Id / 退货Id**/
	public static final String EXTRA_SEARCHID 		= "searchId";
	
	
	
	/**售后类型-换货**/
	public static final int SEARCH_REPLACE 	= 0;
	
	/**售后类型-退货**/
	public static final int SEARCH_RETURN 	= 1;
	
	
	
	/**商品列表**/
	private MyListView lvproduct;
	
	/**售后类型**/
	private MyListView lvsaleaftertype;
	
	/**申请理由**/
	private TextView applyResonTv;
	
	/**描述问题**/
	private EditText remarkinfo;
	
	/**描述问题-提示(0-200)**/
	private TextView remarkinfotip;
	
	/**我已阅读**/
	private CheckBox readCheckbox;
	
	/**提交申请**/
	private Button submitBtn;
	
	/**上传图片**/
	private GridView uploadGridView;
	
	/**商品适配器**/
	private ReplaceGoodsAddAdapter adapter;
	
	/**售后类型适配器**/
	private ReplaceTypeAdapter typeAdapter;
	
	/**图片上传适配器**/
	private UploadImageAdapter uploadImageAdapter;
	
	/** 售后类型 0:换货，1：退货 **/
	private int searchType;
	
	
	/**订单ID**/
	private String orderId; 
	
	/**申请售后model数据**/
	private OrderRecordBean returnBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_service_after_sale);
		
		//初始化控件
		initView();
		
		//初始化数据
		initData();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView(){
		
		this.uploadGridView = (GridView) findViewById(R.id.upload_gridview);
		this.applyResonTv = (TextView) findViewById(R.id.apply_reson_textview);
		
		this.remarkinfo = (EditText) findViewById(R.id.remark_info);
		this.remarkinfotip = (TextView) findViewById(R.id.remark_info_tip);
		this.lvsaleaftertype = (MyListView) findViewById(R.id.lv_saleafter_type);
		this.lvproduct = (MyListView) findViewById(R.id.lv_product);
		this.submitBtn = (Button)findViewById(R.id.submit_apply_btn);
		this.readCheckbox = (CheckBox)findViewById(R.id.read_checkbox);
		
		//设置适配器
		adapter = new ReplaceGoodsAddAdapter(mContext, null);
		lvproduct.setAdapter(adapter);
		typeAdapter = new ReplaceTypeAdapter(mContext, null);
		lvsaleaftertype.setAdapter(typeAdapter);
		
		int screenWidth = DeviceUtils.getWidthMaxPx(mContext);
		uploadImageAdapter = new UploadImageAdapter(mContext, screenWidth, 5);
		uploadGridView.setAdapter(uploadImageAdapter);
		
		//监听描述问题文本变换
		setTextChangeListener(remarkinfo, remarkinfotip, 200);
		uploadGridView.setOnItemClickListener(this);
		lvsaleaftertype.setOnItemClickListener(this);
		lvproduct.setOnItemClickListener(this);
		
		submitBtn.setOnClickListener(this);
		findViewById(R.id.apply_reason_layout).setOnClickListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		
		//售后类型
		Intent intent = getIntent();
		if(null != intent){
			
			//订单ID
			this.orderId = intent.getStringExtra(EXTRA_ORDERID);
			
			if(intent.hasExtra(EXTRA_MODEL)){
				
				//申请售后
				returnBean =  (OrderRecordBean)intent.getSerializableExtra(EXTRA_MODEL);
				showData(returnBean);
				
			}else{
				
				//修改售后-->请求修改信息
				
				
			}
		}
	}
	
	/**
	 * 显示数据
	 * @param replaceDetailBean
	 */
	private void showData(OrderRecordBean replaceDetailBean){
		
		if(null == replaceDetailBean || null == replaceDetailBean.getGoodsList()){
			return;
		}
		
		//显示商品信息
		adapter.refreshData(replaceDetailBean.getGoodsList());
		
		//设置售后类型数据
		List<ReservationType> dataList = replaceDetailBean.getReservationType();
		typeAdapter.refreshData(dataList);
		typeAdapter.checkPosition(0);
	}
	
	
	/**
	 * 换货新增
	 */
	private void applyReplace(String img){
		
		String orderId = this.orderId;
		String userId = AppContext.userId;
		String reasonId = applyResonTv.getTag().toString().trim();
		String reasonName = applyResonTv.getText().toString().trim();
		List<GoodsListEntity> goodsList = adapter.getCheckedGoods();
		String describe = getText(remarkinfo);
		String origin = "3";
		
		RequstClient.applyReplace(new CustomResponseHandler(mContext, true) {
			@Override
			public void onRefreshData(String content) {
				
				//解析json
				int type = JSONParseUtils.getInt(content, "type");
				if(type == 1){
					
					String replaceId = JSONParseUtils.getString(content, "replaceId");
					
					//显示换货详情数据
					Intent intent = new Intent(mContext, ReplaceDetailVerifyActivity.class);
					intent.putExtra(ReplaceDetailVerifyActivity.EXTRA_REPLACE_ID, replaceId);
					startActivity(intent);
					
					//通知列表刷新售后状态
					setResult(RESULT_OK);
					
					finish();
					
				}else{
					
					String msg = JSONParseUtils.getString(content, "msg");
					//显示服务器返回的异常信息
					showMyToast(msg);
				}
			}

		},  orderId, userId, reasonId, reasonName, goodsList, describe, img, origin);
	}
	
	
	/**
	 * 换货新增
	 */
	private void addReturn(String img){
		
		String orderId = this.orderId;
		String userId = AppContext.userId;
		String reasonId = applyResonTv.getTag().toString().trim();
		String reasonName = applyResonTv.getText().toString().trim();
		List<GoodsListEntity> goodsList = adapter.getCheckedGoods();
		String describe = getText(remarkinfo);
		String origin = "3";
		
		RequstClient.addReturn(new CustomResponseHandler(mContext, true) {
			@Override
			public void onRefreshData(String content) {
				
				//解析json
				int type = JSONParseUtils.getInt(content, "type");
				if(type == 1){
					
					String returnId = JSONParseUtils.getString(content, "returnId");
					
					//显示退货详情数据
					Intent intent = new Intent(mContext, ReturnDetailVerifyActivity.class);
					intent.putExtra(ReturnDetailVerifyActivity.EXTRA_RETURN_ID, returnId);
					startActivity(intent);
					
					//通知列表刷新售后状态
					setResult(RESULT_OK);
					
					finish();
					
				}else{
					
					String msg = JSONParseUtils.getString(content, "msg");
					//显示服务器返回的异常信息
					showMyToast(msg);
				}
			}

		},  userId, orderId,  goodsList, reasonId, reasonName, describe, origin, img);
		
	}
	
	/**
	 * 提交申请
	 */
	private void submitApply(String img){
		String typeId = typeAdapter.getCurTypeId();
		if(typeId.equals("1")){

			//我要退货
			addReturn(img);
			
		}else{
			
			//我要换货
			applyReplace(img);
		}
	}
	
	/**
	 * 设置默认的申请理由
	 * @param applyType
	 */
	private void setDefaultApplyReason(String applyType){
		
		try{
			//代表退货ID
			if(applyType.equals("1")){
				
				//我要退货
				ReturnListEntity a = returnBean.getReturnList().get(0);
				applyResonTv.setTag(a.getID());
				applyResonTv.setText(a.getNAME());
				
			}else{
				
				//我要换货
				ReplaceListEntity a = returnBean.getReplaceList().get(0);
				applyResonTv.setTag(a.getID());
				applyResonTv.setText(a.getNAME());
			}
		}catch(Exception e){
			
			//这里可能会报数组越界
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		switch (arg0.getId()) {
		case R.id.lv_saleafter_type:
			
			//售后类型
			boolean hasChangeType = typeAdapter.checkPosition(arg2);
			
			//设置默认的申请理由
			if(hasChangeType){
				ReservationType type = (ReservationType)typeAdapter.getItem(arg2);
				setDefaultApplyReason(type.RESERVATIONTYP_ID);
			}
			
			
			break;
		case R.id.lv_product:
			
			//商品列表
			GoodsListEntity bean = (GoodsListEntity)adapter.getItem(arg2);
			adapter.checkProduct(bean);
			
			break;
		case R.id.upload_gridview:
			
			//上传图片
			if(uploadImageAdapter.isClickAddPic(arg2)){
				
				//打开相册，相机
				showCameraPopwindow(arg1, false, false);
			}
			
			break;
		}
		
	}
	
	@Override
	public void onUpLoadSuccess(String imageUrl, String imageFile) {

		//非空判断
		if (TextUtils.isEmpty(imageUrl) && TextUtils.isEmpty(imageFile)){
			return;
		}
		
		//显示新增的数据
		uploadImageAdapter.appendData(new ImageData(imageFile, imageUrl));
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.submit_apply_btn:
			
			//提交申请
			if(validateInput()){
				
				uploadImage(uploadImageAdapter.getDataList(), new UpLoadImageListener() {
					
					@Override
					public void UpLoadSuccess(ArrayList<String> netimageurls) {
						
						//上传成功的图片
						String img = getImgs(netimageurls);
						
						//提交申请
						submitApply(img);
					}
					
					@Override
					public void UpLoadFail() {
						showMyToast("图片上传失败");
					}
				});
			}
			
			break;
		case R.id.apply_reason_layout:
			
			//申请理由
			String typeId = typeAdapter.getCurTypeId();
			if(typeId.equals("1")){

				//我要退货
				showReasonPopwindow(view, getReturnDataMap(returnBean.getReturnList()), new IOnItemClick() {
					
					@Override
					public void onItemClick(String id, String value) {
						applyResonTv.setTag(id);
						applyResonTv.setText(value);
					}
				});
				
			}else{
				
				//我要换货
//				Map<String, String> a = getReplaceDataMap(returnBean.getReplaceList());
//				for(int i=1; i<100; i++){
//					a.put(i+"","1"+i);
//				}
				showReasonPopwindow(view, getReplaceDataMap(returnBean.getReplaceList()), new IOnItemClick() {
					
					@Override
					public void onItemClick(String id, String value) {
						applyResonTv.setTag(id);
						applyResonTv.setText(value);
					}
				});
				
			}
			
			break;
		}
	}
	
	/**
	 * 根据List<String>获得 "str1,str2,str3..."
	 * @param netimageurls
	 * @return
	 */
	public String getImgs(List<String> netimageurls){
		if(null == netimageurls || netimageurls.size() == 0){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<netimageurls.size(); i++){
			if(i==0){
				sb.append(netimageurls.get(i));
			}else{
				sb.append(","+netimageurls.get(i));
			}
		}
		
		return sb.toString();
	}
	
	
	/**
	 * 获取数据map
	 * @param dataList
	 * @return
	 */
	private Map<String, String> getReturnDataMap(List<ReturnListEntity> dataList){
		Map<String, String> dataMap = new HashMap<String, String>();
		for(int i=0; i<dataList.size(); i++){
			dataMap.put(dataList.get(i).getID()+"", dataList.get(i).getNAME());
		}
		return dataMap;
	}
	
	
	/**
	 * 获取数据map
	 * @param dataList
	 * @return
	 */
	private Map<String, String> getReplaceDataMap(List<ReplaceListEntity> dataList){
		Map<String, String> dataMap = new HashMap<String, String>();
		for(int i=0; i<dataList.size(); i++){
			dataMap.put(dataList.get(i).getID()+"", dataList.get(i).getNAME());
		}
		return dataMap;
	}
	
	
	/**
	 * 输入有效性验证
	 * @return
	 */
	public boolean validateInput(){
		
		List<GoodsListEntity> goods = adapter.getCheckedGoods();
		
		if(null == goods || goods.size() == 0){
			
			showMyToast("请选择商品");
			return false;
		}
		
		
		if(!readCheckbox.isChecked()){
			
			showMyToast("请阅读 退换货条款");
			return false;
		}
		
		
		if(null == applyResonTv.getTag()){
			showMyToast("请选择申请理由");
			return false;
		}
		
		String des = remarkinfo.getText().toString().trim();
		if(TextUtils.isEmpty(des)){
			showMyToast("请填写问题描述");
			return false;
		}
		
		return true;
	}
}
