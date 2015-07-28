package com.huiyin.ui.lehuvoucher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.UploadImageAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.ImageData;
import com.huiyin.bean.LehuVoucherTypeListBean;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow.IOnItemClick;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;

/**
 * 上传发票
 * 
 * @author 刘远祺
 * 
 */
public class LehuVoucherUploadFragmentNew extends BaseFragment implements OnClickListener,OnItemClickListener{

	/**发票类型**/
	private TextView ticketTypeTv;
	
	/** 发票金额 **/
	private EditText ticketMoneyEt;

	/**上传图片GridView**/
	private GridView uploadGridView;

	/**上传图片适配器**/
	private UploadImageAdapter uploadImgAdapter;
	
	
	/**数据**/
	private LehuVoucherTypeListBean data;

	
	
	@Override
	public void onAttach(Activity activity) {
		Log.i("LehuVoucherFragment", "onAttach");
		super.onAttach(activity);
		mContext = getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("LehuVoucherFragment", "onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (layoutView == null) {
			layoutView = inflater.inflate(R.layout.lehu_voucher_upload_fragment_new, null);
		}
		return layoutView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		//初始化数据
		initView();
		
		//初始化数据
		initData();
	}
	
	/**
	 * 初始化控件
	 */
	private void initView() {
		
		ticketMoneyEt = (EditText)findViewById(R.id.contact_msg_ed);
		ticketTypeTv = (TextView)findViewById(R.id.ticket_type_tv);
		uploadGridView = (GridView)findViewById(R.id.upload_gridview);
		
		int screenWidth = DeviceUtils.getWidthMaxPx(mContext);
		uploadImgAdapter = new UploadImageAdapter(mContext, screenWidth, 5);
		uploadGridView.setAdapter(uploadImgAdapter);
		
		findViewById(R.id.voucher_commit_btn).setOnClickListener(this);
		findViewById(R.id.upload_lehu_ticket_layout).setOnClickListener(this);
		uploadGridView.setOnItemClickListener(this);
	}


	/**
	 * 初始化数据
	 */
	private void initData() {
		CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				
				//返回类型
				data = LehuVoucherTypeListBean.explainJson(content, mContext);
				if (1 == data.type) {
					
					if(data.hasData()){
						ticketTypeTv.setText(data.getList().get(0).getNAME());
						ticketTypeTv.setTag(data.getList().get(0).getID());
					}
					
				}else{
					Toast.makeText(mContext, data.msg, Toast.LENGTH_SHORT).show();
				}
			}
		};
		RequstClient.addTicketPacketInit(handler);
	}

	/**
	 * 申请发票
	 */
	private void ApplyVoucher(String imgList) {
		
		//请求参数
		String userId = AppContext.userId; 
		int type = Integer.parseInt(ticketTypeTv.getTag().toString().trim());
		String typeName = getText(ticketTypeTv); 
		String price = getText(ticketMoneyEt); 
		
		//请求
		RequstClient.addTicketPacket(new CustomResponseHandler(getActivity(), true) {
			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
			
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				
				if (bean.type > 0) {
					ticketMoneyEt.setText("");
					uploadImgAdapter.refreshData(new ArrayList<ImageData>());
					Toast.makeText(mContext, "申请成功", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(mContext, bean.msg, Toast.LENGTH_SHORT).show();
				}
			}
		}, userId, type, typeName, price, imgList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.upload_lehu_ticket_layout:
			
			//选择发票类型
			if(null != data){
				Map<String, String> dataMap = data.getDataMap();
				if(null != dataMap && dataMap.size() > 0){
					
					//选中key
					String key = getTag(ticketTypeTv);
					if(TextUtils.isEmpty(key)){
						key = data.getList().get(0).getID()+"";
					}
					
					//显示弹出上拉列表送
					showReasonPopwindow(v, dataMap, key, false, new IOnItemClick() {
						
						@Override
						public void onItemClick(String id, String value) {
							
							//选中赋值
							ticketTypeTv.setTag(id);
							ticketTypeTv.setText(value);
						}
					});
				}
			}
			
			break;
		case R.id.voucher_commit_btn:
			
			//提交申请
			if(validate()){
				
				//上传图片
				if(mContext instanceof BaseCameraActivity){
					((BaseCameraActivity)mContext).uploadImage(uploadImgAdapter.getDataList(), new UpLoadImageListener() {
						
						@Override
						public void UpLoadSuccess(ArrayList<String> netimageurls) {
							
							//提交申请
							String imgList = getImgs(netimageurls);
							ApplyVoucher(imgList);
						}
						
						@Override
						public void UpLoadFail() {
							showMyToast("图片上传失败");
						}
					});
				}
			}
			break;
		}
	}
	
	
	
	/**
	 * 提交前的输入验证
	 * @return
	 */
	private boolean validate(){
		
		if (isNullTag(ticketTypeTv)) {
			showMyToast("请选择发票类型！");
			return false;
		}
		
		
		if (isNullText(ticketMoneyEt)) {
			showMyToast("请输入金额！");
			return false;
		}
		
		String price = getText(ticketMoneyEt);
		double temp = MathUtil.stringToDouble(price);
		if (temp <= 0) {
			showMyToast("请输入正确的金额！");
			return false;
		}
		
		return true;
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		switch (arg0.getId()) {
		case R.id.upload_gridview:
			
			//上传图片
			if(uploadImgAdapter.isClickAddPic(arg2)){
				
				//打开相册，相机
				if(mContext instanceof BaseCameraActivity){
					((BaseCameraActivity)mContext).showCameraPopwindow(arg1, false, false);
				}
			}
			
			break;
		}
	}
	
	/**
	 * 图片选则完毕或上传完毕
	 * @param imageUrl
	 * @param imageFile
	 */
	public void onUpLoadSuccess(String imageUrl, String imageFile) {

		//非空判断
		if (TextUtils.isEmpty(imageUrl) && TextUtils.isEmpty(imageFile)){
			return;
		}
		
		//显示新增的数据
		uploadImgAdapter.appendData(new ImageData(imageFile, imageUrl));
	}
}
