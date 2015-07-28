package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.api.URLs;
import com.huiyin.bean.GoodCommentBean;
import com.huiyin.utils.DeviceUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 预约申请-商品adapter
 * 
 * @author 刘远祺
 * 
 */
public class AddCommentAdapter extends BaseAdapter {

	private Activity context;

	/** 上下文  **/
	private List<GoodCommentBean> dataList;

	/** 用于记录追加评论打开记录 **/
	private Map<String, Boolean> addCommentOpenMap;
	
	public AddCommentAdapter(Activity context, List<GoodCommentBean> dataList) {
		this.context = context;
		this.dataList = dataList;
		this.addCommentOpenMap = new HashMap<String, Boolean>();
	}

	/**
	 * 刷新数据
	 * 
	 * @param dataList
	 */
	public void refreshData(List<GoodCommentBean> dataList) {
		this.dataList = dataList;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return null != dataList ? dataList.size() : 0;
		//return 10;
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
	 * 是否全部订单都已经追加评论
	 * @return
	 */
	public boolean isAllAddComment(){
		if(null == dataList){
			return false;
		}
		
		for(GoodCommentBean bean : dataList){
			if(!bean.isHaveAddComment()){
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_add_comment_goods, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolder) convertView.getTag();

		// 显示数据
		holder.show(dataList.get(position), position);

		return convertView;
	}

	class ViewHolder implements OnClickListener, OnItemClickListener {

		public ImageView iv_logo; 		// 商品logo
		public TextView tv_name; 		// 商品名称
		public TextView tv_norms; 		// 商品规格
		public TextView tv_price;		// 商品价格
		public TextView tv_count;		// 商品数量

		public RatingBar gradeRaBar;	// 评分
		public TextView niceTv;			// 加精
		public TextView cmtContentTv;	// 评论内容
		public TextView cmtDateTv;		// 评论时间
		public View commentPicLayout;	// 评论布局
		public GridView commentPicGv;	// 评论图片
		
		
		public View cmtAddCommentLayoutl;// 追加评论布局块
		public TextView cmtContentAddTv;// 追加评论内容
		public TextView cmtDateAddTv;	// 追加评论时间
		public GridView commentPicAddGv;// 追加评论图片
		
		
		public View commentLayout;		// 追加评论布局(控制可写部分显示隐藏)
		public Button commentBtn; 		// 追加评价

		public View addCommentLayoutl; 	// 追加评论布局块(可写部分布局)
		public EditText writeCommentEt; // 写评论
		public TextView writeCommentTipTv; // 写评论字数提示

		public GridView addCommentPicGv; 	// 上传图片
		public View readLayout;			// 我已阅读父容器
		public CheckBox readCheckBox; 	// 我已阅读《会换货条款》

		public Button applySubmitBtn; 	// 申请提交

		//评论图片适配器
		private ImageAdapter commentPicAdapter;		
		
		//追加评论图片适配器
		private ImageAdapter commentPicAddAdapter;		
		
		//追加评论图片适配器
		private UploadImageAdapter addcommentPicAdapter;	

		
		private GoodCommentBean currentBean;	//当前显示的Bean数据
		
		ViewHolder(View convertView) {

			//商品
			iv_logo = (ImageView) convertView.findViewById(R.id.goods_pic_imageview);
			tv_name = (TextView) convertView.findViewById(R.id.goods_name_tv);
			tv_norms = (TextView) convertView.findViewById(R.id.norms_value_tv);
			tv_price = (TextView)convertView.findViewById(R.id.product_price_tv);
			tv_count = (TextView)convertView.findViewById(R.id.apply_product_count_textview);

			//初次评价
			gradeRaBar = (RatingBar) convertView.findViewById(R.id.comment_rating);
			niceTv = (TextView) convertView.findViewById(R.id.qiujing_state_tv);
			cmtContentTv = (TextView) convertView.findViewById(R.id.comment_content_tv);
			cmtDateTv = (TextView) convertView.findViewById(R.id.comment_date_tv);
			commentPicGv = (GridView) convertView.findViewById(R.id.upload_gridview);
			commentPicLayout = convertView.findViewById(R.id.comment_pic_layout);
			
			//追加评论
			cmtAddCommentLayoutl = convertView.findViewById(R.id.commit_add_layout);
			cmtContentAddTv = (TextView) convertView.findViewById(R.id.comment_content_add_tv);
			cmtDateAddTv = (TextView) convertView.findViewById(R.id.comment_date_add_tv);
			commentPicAddGv = (GridView) convertView.findViewById(R.id.comment_add_pic_gridview);
			
			//追加评价按钮，布局
			commentBtn = (Button) convertView.findViewById(R.id.open_comment_button);
			commentBtn.setOnClickListener(this);
			commentLayout = convertView.findViewById(R.id.open_comment_layout);
			
			//追加评价-填写内容布局
			addCommentLayoutl = convertView.findViewById(R.id.add_comment_layout);
			writeCommentEt = (EditText) convertView.findViewById(R.id.comment_content_edittext);
			writeCommentTipTv = (TextView) convertView.findViewById(R.id.comment_content_tip_tv);
			setEmptyListener(writeCommentEt, writeCommentTipTv);
			addCommentPicGv = (GridView) convertView.findViewById(R.id.upload_add_gridview);
			readCheckBox = (CheckBox) convertView.findViewById(R.id.read_checkbox);
			readLayout = convertView.findViewById(R.id.read_layout);
			readLayout.setVisibility(View.GONE);
			
			//申请提交
			applySubmitBtn = (Button) convertView.findViewById(R.id.submit_apply_btn);
			applySubmitBtn.setOnClickListener(this);
			
			//评论
			int screenWidth = DeviceUtils.getWidthMaxPx(context);
			commentPicAdapter = new ImageAdapter(context, screenWidth, 5);
			commentPicGv.setAdapter(commentPicAdapter);
			commentPicGv.setOnItemClickListener(this);
			
			//追加评论
			commentPicAddAdapter = new ImageAdapter(context, screenWidth, 5);
			commentPicAddGv.setAdapter(commentPicAddAdapter);
			commentPicAddGv.setOnItemClickListener(this);
			
			//填写追加评论
			addcommentPicAdapter = new UploadImageAdapter(context, screenWidth, 5);
			addCommentPicGv.setAdapter(addcommentPicAdapter);
			addCommentPicGv.setOnItemClickListener(this);
		}

		/**
		 * 显示商品信息
		 * 
		 * @param goodCommentBean
		 */
		void show(final GoodCommentBean goodCommentBean, int position) {

			//记住当前显示的bean
			currentBean = goodCommentBean;
			
			//显示商品
			ImageLoader.getInstance().displayImage(URLs.IMAGE_URL + goodCommentBean.GOODS_IMG , iv_logo);
			tv_name.setText(goodCommentBean.GOODS_NAME);
			tv_price.setText(goodCommentBean.GOODS_PRICE);
			tv_count.setText("x"+goodCommentBean.QUANTITY);
			tv_norms.setText(goodCommentBean.NORMS_VALUE);
			
			//显示评价
			float rating = Float.valueOf(goodCommentBean.EVA_GRADE);
			gradeRaBar.setRating(rating);
			niceTv.setText(goodCommentBean.getNice());
			cmtContentTv.setText(goodCommentBean.CONTENT);
			cmtDateTv.setText(goodCommentBean.EVA_TIME);
			if(goodCommentBean.hasCommentPic()){
				commentPicAdapter.refreshData(goodCommentBean.getImageData());
				commentPicLayout.setVisibility(View.VISIBLE);
			}else{
				commentPicLayout.setVisibility(View.GONE);
			}
			
			//显示追加评论
			if(goodCommentBean.isHaveAddComment()){
				cmtContentAddTv.setText(goodCommentBean.CONTENT_ADD);
				cmtDateAddTv.setText(goodCommentBean.EVA_TIME_ADD);
				commentPicAddAdapter.refreshData(goodCommentBean.getAddImageData());
				cmtAddCommentLayoutl.setVisibility(View.VISIBLE);
			}else{
				
				//没有追加评论，则不显示追加评论
				cmtAddCommentLayoutl.setVisibility(View.GONE);
			}
			
			//追加评论按钮布局(如果已经有了追加评论，就不显示按钮)
			commentLayout.setVisibility(goodCommentBean.isHaveAddComment() ? View.GONE :View.VISIBLE);
			
			//显示追加评价(可写部分)
			addCommentLayoutl.setVisibility(getVisibleAddCommentView(goodCommentBean));

			//显示上传的图片
			writeCommentEt.setText(goodCommentBean.getCommitContent());
			writeCommentEt.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
				public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
				public void afterTextChanged(Editable arg0) {
					//保存当前文本变化
					goodCommentBean.setCommitContent(arg0.toString().trim());
				}
			});
			addcommentPicAdapter.refreshData(goodCommentBean.getAddUploadImgFile());
			
		}

	
		/**
		 * 获取追加布局的显示
		 * @param goodCommentBean
		 * @return
		 */
		private int getVisibleAddCommentView(GoodCommentBean goodCommentBean){
			
			//已经追加，则不再显示追加布局
			if(goodCommentBean.isHaveAddComment()){
				return View.GONE;
			}
			
			//未追加，但未打开追加布局
			String goodId = goodCommentBean.GOODS_ID;
			if(!addCommentOpenMap.containsKey(goodId) || !addCommentOpenMap.get(goodId)){
				return View.GONE;
			}
			
			//未追加，打开了追加布局
			return View.VISIBLE;
		}
		
		
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.open_comment_button:

				//我要评论
				
				//保存当前填写的评论内容(处理点击追加评论，隐藏再打开的生活，评论信息没有了)
				currentBean.setCommitContent(writeCommentEt.getText().toString().trim());
				
				String goodId = currentBean.GOODS_ID;
				if(!addCommentOpenMap.containsKey(goodId) || !addCommentOpenMap.get(goodId)){
					addCommentOpenMap.put(goodId, true);
				}else{
					addCommentOpenMap.put(goodId, false);
				}
				
				//通知刷新数据,打开开关
				notifyDataSetChanged();
				
				break;

			case R.id.submit_apply_btn:

				//申请提交
				if(null != submitCallback){
					
					//判断评论内容非空
					String comtContent = writeCommentEt.getText().toString().trim();
					if(TextUtils.isEmpty(comtContent)){
						Toast.makeText(context, "请输入评论内容", Toast.LENGTH_SHORT).show();
						return;
					}
					
					//勾选阅读退换货条款
//					if(!readCheckBox.isChecked()){
//						Toast.makeText(context, "请阅读 退换货条款", Toast.LENGTH_SHORT).show();
//						return;
//					}
					
					
					//设置提交文本
					currentBean.setCommitContent(comtContent);
					submitCallback.onSubmitClick(currentBean);
				}
				
				break;

			}
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			switch (arg0.getId()) {
			case R.id.upload_gridview:
				
				if(null != submitCallback){
					
					//预览图片
					submitCallback.onCommitPicItemClick(currentBean.getCommitImageUrls(), arg2);
				}
				
				break;
			case R.id.comment_add_pic_gridview:
				
				if(null != submitCallback){
					
					//预览图片
					submitCallback.onCommitPicItemClick(currentBean.getCommitAddImageUrls(), arg2);
				}
				
				break;

			case R.id.upload_add_gridview:
				if(addcommentPicAdapter.isClickAddPic(arg2)){
					if(null != submitCallback){
						
						//上传图片
						submitCallback.onClickAddUploadPic(currentBean);
					}
				}
				break;
			}
		}
	}
	
	
	private ICommitClick submitCallback;
	public void setOnSubmitCallback(ICommitClick submitCallback){
		this.submitCallback = submitCallback;
	}
	
	
	/**
	 * 提交回调
	 * @author 刘远祺
	 *
	 */
	public interface ICommitClick{
		
		/**评论的图片-OnItemClick**/
		public void onCommitPicItemClick(ArrayList<String> imgUrlList, int showIndex);
		
		/**点击了添加图片按钮**/
		public void onClickAddUploadPic(GoodCommentBean goodCommentBean);
		
		/**用户触发了 提交评论 按钮 **/
		public void onSubmitClick(GoodCommentBean goodCommentBean);
		
	}
	
	/**
	 * 监听originTextView文本值变化，有值showTextView显示，否则不显示
	 * @param originTextView
	 * @param showTextView
	 */
	protected void setEmptyListener(TextView originTextView, final TextView showTextView){
		if(null == originTextView || null == showTextView){
			return;
		}
		
		originTextView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
				String text = arg0.toString().trim();
				int vis = TextUtils.isEmpty(text) ? View.VISIBLE : View.GONE;
				showTextView.setVisibility(vis);
			}
		});
		
	}
}
