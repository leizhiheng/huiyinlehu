package com.huiyin.ui.user.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.huiyin.adapter.ReplaceGoodsUpdateAdapter;
import com.huiyin.adapter.UploadImageAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.AmendReplaceInitBean;
import com.huiyin.bean.GoodsListEntity;
import com.huiyin.bean.ImageData;
import com.huiyin.bean.ReplaceType;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow.IOnItemClick;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.huiyin.wight.MyListView;

/**
 * <b>修改售后 (换货，退货)</b></br>
 * Created by lian on 2015/06/01.
 */
public class ReplaceUpdateActivity extends BaseCameraActivity implements OnItemClickListener, OnClickListener {

	
	/** 订单ID **/
	public static final String EXTRA_REPLACE_ID 		= "replaceId";

	
	
	/**商品列表**/
	private MyListView lvproduct;
	
	/**售后类型**/
	private TextView lvsaleaftertype;
	
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
	private ReplaceGoodsUpdateAdapter adapter;
	
	/**图片上传适配器**/
	private UploadImageAdapter uploadImageAdapter;
	
	/**换货ID**/
	private String mReplaceId;
	
	/**换货详情数据**/
	private AmendReplaceInitBean initBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_replace_update);
		
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
		this.lvsaleaftertype = (TextView) findViewById(R.id.apply_type_textview);
		this.lvproduct = (MyListView) findViewById(R.id.lv_product);
		this.submitBtn = (Button)findViewById(R.id.submit_apply_btn);
		this.readCheckbox = (CheckBox)findViewById(R.id.read_checkbox);
		
		//设置适配器
		adapter = new ReplaceGoodsUpdateAdapter(mContext, null);
		lvproduct.setAdapter(adapter);
		
		int screenWidth = DeviceUtils.getWidthMaxPx(mContext);
		uploadImageAdapter = new UploadImageAdapter(mContext, screenWidth, 5);
		uploadGridView.setAdapter(uploadImageAdapter);
		
		//监听描述问题文本变换
		setTextChangeListener(remarkinfo, remarkinfotip, 200);
		uploadGridView.setOnItemClickListener(this);
		lvproduct.setOnItemClickListener(this);
		
		submitBtn.setOnClickListener(this);
		findViewById(R.id.apply_reason_layout).setOnClickListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		
		setTitle("申请售后");
		
		//设置售后类型数据
		lvsaleaftertype.setText("我要换货");
		
		//加载 初始化数据
		mReplaceId = getStringExtra(EXTRA_REPLACE_ID);
		loadAmendReplaceInit();
	}
	
	/**
	 * 修改换货初始化
	 */
	private void loadAmendReplaceInit(){
		
		RequstClient.amendReplaceInit(new CustomResponseHandler(mContext, true) {
			@Override
			public void onRefreshData(String content) {
				
				//解析json
				initBean = AmendReplaceInitBean.explainJson(content, mContext);
				if(initBean.type == 1){
					
					//显示换货详情数据
					showData();
					
				}else{
					
					//显示服务器返回的异常信息
					showMyToast(initBean.msg);
				}
			}

		},  mReplaceId);
	}
	
	/**
	 * 显示数据
	 * @param replaceDetailBean
	 */
	private void showData(){
		
		if(null == initBean || null == initBean.replaceDetailList){
			return;
		}
		
		//显示商品信息
		adapter.refreshData(initBean.replaceDetailList);
		
		//申请理由
		applyResonTv.setTag(initBean.REASON_TYPE);
		applyResonTv.setText(initBean.REASON_NAME);
		
		//描述问题
		remarkinfo.setText(initBean.QUESTION_DESC);
		
		//显示上传图片
		uploadImageAdapter.refreshData(initBean.getImageDataList());
		
	}

	/**
	 * 修改换货
	 */
	private void amendReplace(String img){
		
		String userId = AppContext.userId;
		String reasonId = applyResonTv.getTag().toString().trim();
		String reasonName = applyResonTv.getText().toString().trim();
		List<GoodsListEntity> goodsList = adapter.getCheckedGoods();
		String describe = getText(remarkinfo);
		String origin = "3";
		
		RequstClient.amendReplace(new CustomResponseHandler(mContext, true) {
			@Override
			public void onRefreshData(String content) {
				
				//解析json
				int type = JSONParseUtils.getInt(content, "type");
				if(type == 1){
					
					//通知上一个界面刷新数据
					setResult(RESULT_OK);
					finish();
					
				}else{
					
					String msg = JSONParseUtils.getString(content, "msg");
					//显示服务器返回的异常信息
					showMyToast(msg);
				}
			}

		},  mReplaceId, userId, reasonId, reasonName, goodsList, describe, img, origin);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		switch (arg0.getId()) {
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
				
				//没有需要上传的文件
				if(!uploadImageAdapter.hasNewUploadImgFile()){
					
					//直接修改数据
					String img = uploadImageAdapter.getImages();
					amendReplace(img);
					
					return;
				}
				
				//上传文件
				uploadImage(uploadImageAdapter.getDataList(), new UpLoadImageListener() {
					
					@Override
					public void UpLoadSuccess(ArrayList<String> netimageurls) {
						
						String newAppendImg = getImgs(netimageurls);
						String img = uploadImageAdapter.getImages();
						
						if(!TextUtils.isEmpty(img)){
							newAppendImg = newAppendImg + "," + img;
						}
						
						//修改换货
						amendReplace(newAppendImg);
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
			showReasonPopwindow(view, getDataMap(initBean.replaceList), new IOnItemClick() {
				@Override
				public void onItemClick(String id, String value) {
					applyResonTv.setTag(id);
					applyResonTv.setText(value);
				}
			});
			
			break;
		}
	}
	
	
	/**
	 * 获取数据map
	 * @param dataList
	 * @return
	 */
	private Map<String, String> getDataMap(List<ReplaceType> dataList){
		Map<String, String> dataMap = new HashMap<String, String>();
		for(int i=0; i<dataList.size(); i++){
			dataMap.put(dataList.get(i).ID, dataList.get(i).NAME);
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
