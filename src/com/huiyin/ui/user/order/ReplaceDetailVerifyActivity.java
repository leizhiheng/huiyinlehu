package com.huiyin.ui.user.order;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.adapter.ImageAdapter;
import com.huiyin.adapter.ReplaceDetailGoodsAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.ImageData;
import com.huiyin.bean.ReplaceDetailBean;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.wight.MyListView;

/**
 * 换货详情 ->维权审核
 * @author 刘远祺
 * 
 * @todo TODO
 *
 * @date 2015-7-3
 */
public class ReplaceDetailVerifyActivity extends BaseCameraActivity implements OnItemClickListener, OnClickListener {

	/**修改换货**/
	public static final int REQUEST_CODE_UPDATE			= 1;
	
	/** 换货Id**/
	public static final String EXTRA_REPLACE_ID 		= "replaceId";
	
	/**商品列表**/
	private MyListView lvproduct;
	
	/**售后类型**/
	private TextView salTypeTv;
	
	/**申请理由**/
	private TextView applyReasonTv;
	
	/**描述问题**/
	private TextView desQuestionTv;
	
	/**上传凭证**/
	private GridView uploadGridView;
	
	/**上传凭证布局**/
	private View uploadLayout;
	
	/**修改申请**/
	private Button upadteBtn;
	
	/**取消申请**/
	private Button cancelBtn;
	
	
	/**商品适配器**/
	private ReplaceDetailGoodsAdapter adapter;
	
	/**图片上传适配器**/
	private ImageAdapter imageAdapter;
	
	/**换货ID**/
	private String replaceId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_replace_detail);
		
		//初始化控件
		initView();
		
		//初始化数据
		initData();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView(){
		
		//findView

		lvproduct = (MyListView) findViewById(R.id.product_listView);
		salTypeTv = (TextView) findViewById(R.id.sale_type);
		applyReasonTv = (TextView)findViewById(R.id.apply_reason);
		desQuestionTv = (TextView) findViewById(R.id.des_question);
		uploadGridView = (GridView) findViewById(R.id.upload_gridview);
		uploadLayout = findViewById(R.id.upload_img_layout);
		
		upadteBtn = (Button) findViewById(R.id.update_apply_btn);
		cancelBtn = (Button) findViewById(R.id.cancel_apply_btn);
		
		//设置适配器
		adapter = new ReplaceDetailGoodsAdapter(mContext, null);
		lvproduct.setAdapter(adapter);
		
		int width = (DeviceUtils.getWidthMaxPx(mContext)*4)/5;
		imageAdapter = new ImageAdapter(mContext, width, 4);
		uploadGridView.setAdapter(imageAdapter);
		uploadGridView.setOnItemClickListener(this);
		
		//监听描述问题文本变换
		upadteBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(){
		
		//在数据显示之前不显示标题
		setTitle("");
		
		//售后类型
		replaceId = getStringExtra(EXTRA_REPLACE_ID);
		searchReplaceDetail(replaceId);
		
	}
	
	/**
	 * 显示数据
	 * @param replaceDetailBean
	 */
	private void showData(ReplaceDetailBean replaceDetailBean){
		
		//设置标题
		setTitle(replaceDetailBean.TITLE);
		
		//商品列表
		adapter.refreshData(replaceDetailBean.replaceDetailList);
		
		//售后类型
		salTypeTv.setText(replaceDetailBean.TYPE_NAME);
		
		//申请理由
		applyReasonTv.setText(replaceDetailBean.REASON_NAME);
		applyReasonTv.setTag(replaceDetailBean.REPLACE_ID);
		
		//描述
		desQuestionTv.setText(replaceDetailBean.DESCRIBE);
		
		//更新上传的图片
		List<ImageData> list = replaceDetailBean.getImageData();
		if(null != list && list.size() > 0){
			uploadLayout.setVisibility(View.VISIBLE);
			imageAdapter.refreshData(list);
		}else{
			uploadLayout.setVisibility(View.GONE);
		}
		
	}
	
	/**
	 * 查询-换货详情
	 */
	private void searchReplaceDetail(String replaceId){
		RequstClient.replaceDetail(new CustomResponseHandler(mContext, true) {
			@Override
			public void onRefreshData(String content) {
				
				//解析json
				ReplaceDetailBean mReplaceDetailBean = ReplaceDetailBean.explainJson(content, mContext);
				if(mReplaceDetailBean.type == 1){
					
					//显示换货详情数据
					showData(mReplaceDetailBean);
				}else{
					
					//显示服务器返回的异常信息
					showMyToast(mReplaceDetailBean.msg);
				}
			}

		}, replaceId);
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.upload_gridview:
			
			//上传凭证图片
			startPhotoViewActivity(imageAdapter.getImageUrlList(), arg2);
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.update_apply_btn:
			
			//修改申请
			Intent intent = new Intent(mContext, ReplaceUpdateActivity.class);
			intent.putExtra(ReplaceUpdateActivity.EXTRA_REPLACE_ID, replaceId);
			startActivityForResult(intent, REQUEST_CODE_UPDATE);
			
			
			break;
		case R.id.cancel_apply_btn:
			
			//取消申请
			cancelChangeOrder(replaceId);
			
			break;
		}
	}
	
	/***
	 * 取消换货订单
	 */
	private void cancelChangeOrder(String replaceId) {

		RequstClient.cancelChangeOrder(replaceId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}else{
						
						//取消成功
						showMyToast("取消成功");
						
						//通知刷新列表
						setResult(RESULT_OK);
						finish();
					}
					

				} catch (Exception e) {
					e.printStackTrace();
				} 
			}

		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case REQUEST_CODE_UPDATE:
			
			//修改换货成功，返回刷新数据
			if(resultCode == RESULT_OK){
				
				searchReplaceDetail(replaceId);
			}
			
			break;
		}
		
	}
}
