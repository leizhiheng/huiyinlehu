package com.huiyin.ui.user.order;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.huiyin.R;
import com.huiyin.adapter.AddCommentAdapter;
import com.huiyin.adapter.AddCommentAdapter.ICommitClick;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.GoodCommentBean;
import com.huiyin.bean.ImageData;
import com.huiyin.bean.OrderCommentDetailBean;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;

/**
 * 追加评论 Created by 刘远祺 on 2015/6/1.
 */
public class AddCommentActivity extends BaseCameraActivity implements ICommitClick {

	/**订单ID**/
	public static final String EXTRA_ORDERID 	= "orderId";
	
	/**界面标题**/
	public static final String EXTRA_TITLE 		= "title";
	
	/**商品列表**/
	private ListView dataListView;
	
	/**评论适配器**/
	private AddCommentAdapter adapter;
	
	/**订单ID**/
	private String orderId;
	
	/**评论数据**/
	private OrderCommentDetailBean mReplaceDetailBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_comment);
		
		//初始化控件
		initView();
		
		//初始化数据
		initData();
	}

	
	
	/**
	 * 初始化控件
	 */
	private void initView(){
		
		dataListView = (ListView)findViewById(R.id.lv_product);
		adapter = new AddCommentAdapter(mContext, null);
		adapter.setOnSubmitCallback(this);
		dataListView.setAdapter(adapter);
	}
	
	
	/**
	 * 初始化数据
	 */
	private void initData(){
	
		String title = getStringExtra(EXTRA_TITLE);
		if(TextUtils.isEmpty(title)){
			setTitle("追加评价");
		}else{
			setTitle(title);
		}
		
		//加载评论数据
		this.orderId = getStringExtra(EXTRA_ORDERID);
		loadCommentData(true);
		
	}
	
	/**
	 * 加载评论信息
	 */
	private void loadCommentData(boolean showDialog){
		
		RequstClient.commentsView(new CustomResponseHandler(mContext, showDialog) {
			@Override
			public void onRefreshData(String content) {
				
				//解析json
				mReplaceDetailBean = OrderCommentDetailBean.explainJson(content, mContext);
				if(mReplaceDetailBean.type == 1){
					
					//显示换货详情数据
					showData();
					
				}else{
					
					//显示服务器返回的异常信息
					showMyToast(mReplaceDetailBean.msg);
				}
			}

		}, orderId);
	}
	
	/**
	 * 显示数据
	 */
	private void showData(){
		
		//刷新数据
		adapter.refreshData(mReplaceDetailBean.evaList);
		
	}

	
	@Override
	public void onSubmitClick(final GoodCommentBean goodCommentBean) {
		
		//待上传的图片
		List<ImageData> list = goodCommentBean.getAddUploadImgFile();
		uploadImage(list, new UpLoadImageListener() {
			
			@Override
			public void UpLoadSuccess(ArrayList<String> netimageurls) {
				
				String img = getImgs(netimageurls);
				submitCommit(goodCommentBean, img);
			}
			
			@Override
			public void UpLoadFail() {
				showMyToast("图片上传失败");
			}
		});
	}

	/**
	 * 提交追加评论
	 * @param goodCommentBean
	 * @param img
	 */
	private void submitCommit(final GoodCommentBean goodCommentBean, final String img){
		String evaGoodsId = goodCommentBean.ID;
		String content = goodCommentBean.getCommitContent();
		
		//提交评价
		RequstClient.goodsCommentsAdd(new CustomResponseHandler(mContext, true) {
			@Override
			public void onRefreshData(String content) {
				
				//解析json
				int type = JSONParseUtils.getInt(content, "type");
				if(type == 1){
					
					//显示换货详情数据
					showMyToast("评论已追加");
					
					//重新刷新数据(无状态刷)
					goodCommentBean.IS_ADD_EVA = "1";
					goodCommentBean.EVA_TIME_ADD = JSONParseUtils.getString(content, "curTime");
					goodCommentBean.CONTENT_ADD = goodCommentBean.getCommitContent();
					goodCommentBean.EVA_ADD_IMG = img;
					adapter.notifyDataSetChanged();
					
					
					//如果所有订单评论都已追加，通知上一个界面刷新评论状态--"查看评价"
					if(adapter.isAllAddComment()){
						setResult(RESULT_OK);
					}
					
				}else{
					
					//显示服务器返回的异常信息
					String msg = JSONParseUtils.getString(content, "msg");
					showMyToast(msg);
				}
			}

		}, evaGoodsId, content, img);
	}
	

	@Override
	public void onCommitPicItemClick(ArrayList<String> imgUrlList, int showIndex) {
		
		//开启图片预览的Activity
		startPhotoViewActivity(imgUrlList, showIndex);
	}



	@Override
	public void onClickAddUploadPic(GoodCommentBean goodCommentBean) {
		
		//记住当前需要上传图片的bean对象
		this.tempGoodCommentBean = goodCommentBean;
		
		//取相册相机图片(不上传图片，在提交的时候批量上传)
		showCameraPopwindow(dataListView, false, false);
	}
	
	private GoodCommentBean tempGoodCommentBean;
	@Override
	public void onUpLoadSuccess(String imageUrl, String imageFile) {
		
		//在点击 “申请提交” 的时候在上传图片
		if(null != tempGoodCommentBean && !TextUtils.isEmpty(imageFile)){
			tempGoodCommentBean.addUploadImgFile(imageFile);
			
			//刷新界面
			adapter.notifyDataSetChanged();
		}
	}
}
