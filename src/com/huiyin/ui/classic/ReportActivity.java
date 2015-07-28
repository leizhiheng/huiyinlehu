package com.huiyin.ui.classic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ProductsDetailBeen;
import com.huiyin.bean.ReportBaseInfoTitle;
import com.huiyin.bean.ReportBaseInfoType;
import com.huiyin.dialog.UploadSuccessDialog;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow.IOnItemClick;
import com.huiyin.ui.housekeeper.SelectPicPopupWindow;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.huiyin.wight.ImageViewWithDel;
import com.huiyin.wight.ImageViewWithDel.MyImageViewCallBack;
import com.huiyin.wight.ScrollGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * “我要举报”页面，用于客户填写举报信息
 * 
 * @author leizhiheng
 * 
 */
public class ReportActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "ReportActivity";

	private View mTitleLayout;
	private ImageView mIvShopIcon;
	private TextView mTvBack;
	private TextView mTvTitle;
	private TextView mTvShopName;
	private TextView mTvGoodsName;
	private TextView mTvGoodPrice;
	private TextView mTvReportCategory;
	private TextView mTvCategoryDescription;
	private TextView mTvreportThemes;
	private TextView mTvHandReport;
	private ImageView mIvGoodsImg;
	private ImageView mIvToStore;
	private TextView mTvInputNum;
	private EditText mEtReportMessage;
	private ScrollGridView mGridView;

	private Dialog mDialog;// 选择举报类型和主题弹出的Dialog

	private MyGridAdapter myGridAdapter;
	private ArrayList<Picture> mPicList;// GridView中显示需要上传的图片
	private ArrayList<String> mPathList;// 上传图片的本地路径
	private ArrayList<String> mUrlList;// 图片上传后返回图片的网络地址
	private Picture mAddPicture = new Picture(R.drawable.add_pic, null);

	private int CAMERA_WITH_DATA = 0x11;
	private int CAMERA_PICK_PHOTO = 0x21;

//	private ImageLoader mImageLoader;
//	private ProductsDetailBeen goodsDetailBeen;// 用于获取商品信息

	private ArrayList<ReportBaseInfoType> mBaseInfoTypes;// 举报基本信息：类型
	private ArrayList<ReportBaseInfoTitle> mTitlesList;//选择举报类型后对应的举报主题
	private int chosedTypeIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

		initData();
		initView();
	}

	private String goods_id;
	private String store_id;
	private String store_logo;
	private String store_name;
	private String goods_price;
	private String goods_name;
	private String goods_img;
	public void initData() {
		Intent intent = this.getIntent();
		if (intent != null) {
			/*
			 *从BasicInformationFragment传递过来的部分商品信息
			 */
		    Bundle bundle = intent.getExtras();
		    goods_id = bundle.getString("goods_id");
			store_id = bundle.getString("store_id");
			store_logo = bundle.getString("store_logo");
			store_name = bundle.getString("store_name");
			goods_price = bundle.getString("goods_price");
			goods_name = bundle.getString("goods_name");
			goods_img = bundle.getString("goods_img");
		}
		
		Log.d(TAG, "store_id="+store_id+",store_logo"+store_logo+ ",store_name="+store_name+",goods_price="+goods_price+",goods_name="+goods_name+",goods_img"+goods_img);
		
		mBaseInfoTypes = new ArrayList<ReportBaseInfoType>();
		mPicList = new ArrayList<Picture>();
		mPathList = new ArrayList<String>();
		mPicList.add(mAddPicture);
		myGridAdapter = new MyGridAdapter();
		loadReportBaseInfo();
	}

	public void initView() {
		mTitleLayout = findViewById(R.id.layout_ab);
		mTitleLayout.setFocusable(true);
		mTitleLayout.setFocusableInTouchMode(true);
		mTitleLayout.requestFocus();
		
		mIvShopIcon = (ImageView) findViewById(R.id.iv_shop_logo);
		mTvBack = (TextView) findViewById(R.id.ab_back);
		mTvTitle = (TextView) findViewById(R.id.ab_title);
		mTvShopName = (TextView) findViewById(R.id.tv_shop_name);
		mTvGoodsName = (TextView) findViewById(R.id.good_message);
		mTvGoodPrice = (TextView) findViewById(R.id.goods_price);
		mTvReportCategory = (TextView) findViewById(R.id.tv_report_catogery_message);
		mTvCategoryDescription = (TextView) findViewById(R.id.tv_report_catogery_description);
		mTvreportThemes = (TextView) findViewById(R.id.tv_report_theme_message);
		mTvHandReport = (TextView) findViewById(R.id.tv_handup_report);
		mIvToStore = (ImageView) findViewById(R.id.arrow_to_shop);
		mIvGoodsImg = (ImageView) findViewById(R.id.goods_icon);
		mEtReportMessage = (EditText) findViewById(R.id.et_report_message_input);
		mTvInputNum = (TextView) findViewById(R.id.tv_input_num);
		mGridView = (ScrollGridView) findViewById(R.id.gallery);

		mIvShopIcon.setOnClickListener(this);
		mTvBack.setOnClickListener(this);
		mTvShopName.setOnClickListener(this);
		mTvReportCategory.setOnClickListener(this);
		mTvreportThemes.setOnClickListener(this);
		mTvHandReport.setOnClickListener(this);
		
		//文本监听
		setTextChangeListener(mEtReportMessage, mTvInputNum, 200);
		
		mGridView.setAdapter(myGridAdapter);

		mTvTitle.setText("我要举报");
		if(!store_name.equals(this.getResources().getString(R.string.lehu_store_name)) && 
				!store_name.equals("乐虎自营")){
			mIvToStore.setVisibility(View.VISIBLE);
            //统一图片获取方式用于省流量设置
            ImageManager.LoadWithServer(URLs.IMAGE_URL +store_logo,mIvShopIcon);
		}else {
			mIvShopIcon.setImageResource(R.drawable.lehu);
		}
        //统一图片获取方式用于省流量设置
        ImageManager.LoadWithServer(URLs.IMAGE_URL +goods_img,mIvGoodsImg);
		mTvShopName.setText(store_name);
		mTvGoodsName.setText(goods_name);
		mTvGoodPrice.setText(MathUtil.priceForAppWithSign(goods_price));
	}

	public void initDialog() {
		mDialog = new Dialog(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		Window dialogWindow = mDialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
	}

	/*
	 * 获取举报的基本信息，如举报类型，举报主题
	 */
	private void loadReportBaseInfo() {
		Log.d(TAG, "loadReportBaseInfo ==>");
		RequstClient.loadReportInfo(new MyCustomResponseHandler(this, true) {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				UIHelper.cloesLoadDialog();
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(ReportActivity.this, errorMsg,
								Toast.LENGTH_SHORT).show();
						Log.d(TAG, "loadReportInfo failed");
						return;
					} else {
						// 获取举报信息
						if (mBaseInfoTypes == null) {
							mBaseInfoTypes = new ArrayList<ReportBaseInfoType>();
						} else {
							mBaseInfoTypes.clear();
						}
						mBaseInfoTypes = JSONParseUtils.parseReportBaseInfo(content);
						mTitlesList = mBaseInfoTypes.get(chosedTypeIndex).getTitleList();
						mTvCategoryDescription.setText(mBaseInfoTypes.get(0).getTypeDescription());
						
						mTvReportCategory.setText(mBaseInfoTypes.get(0).getTypeName());
						mTvReportCategory.setTag(mBaseInfoTypes.get(0).getTypeId());
						
						mTvreportThemes.setText(mTitlesList.get(0).getTitleName());
						mTvreportThemes.setTag(mTitlesList.get(0).getTitleId());
					}

				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ab_back:
			Log.d(TAG, "ab_back has been clicked");
			finish();
			break;
		case R.id.iv_shop_logo:
			break;
		case R.id.tv_shop_name:
			if(!store_name.equals(this.getResources().getString(R.string.lehu_store_name)) && 
					!store_name.equals("乐虎自营")){
				Intent intent = new Intent(this,StoreHomeActivity.class);
				intent.putExtra(StoreHomeActivity.STORE_ID, MathUtil.stringToInt(store_id));
				startActivity(intent);
			}
			break;
		case R.id.tv_report_catogery_message:
		
			//显示举报类型popwindow
			String typeId = getTag(mTvReportCategory);
			showReasonPopwindow(mTvReportCategory, getReportTypeMap(), typeId, new IOnItemClick() {
				
				@Override
				public void onItemClick(String id, String value) {
					mTvReportCategory.setText(value);
					mTvReportCategory.setTag(id);
				}
			});
			
			
			break;
		case R.id.tv_report_theme_message:
			
			//显示举报主题popwindow
			String themeId = getTag(mTvreportThemes);
			showReasonPopwindow(mTvreportThemes, getThemeTypeMap(), themeId, new IOnItemClick() {
				
				@Override
				public void onItemClick(String id, String value) {
					mTvreportThemes.setText(value);
					mTvreportThemes.setTag(id);
				}
			});
			
			
			break;
		case R.id.tv_handup_report:
			if(mPathList != null && mPathList.size()>0){
				upLoadImage();
			}else{
				uploadReport();
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 获取举报类型map
	 * @return
	 */
	private Map<String, String> getReportTypeMap(){
		Map<String, String> dataMap = new HashMap<String, String>();
		if(null != mBaseInfoTypes && mBaseInfoTypes.size() > 0){
			
			String key = null;
			String value = null;
			for(int i=0; i<mBaseInfoTypes.size(); i++){
				key = mBaseInfoTypes.get(i).getTypeId()+"";
				value = mBaseInfoTypes.get(i).getTypeName();
				dataMap.put(key, value);
			}
		}
		return dataMap;
	}
	
	
	/**
	 * 获取举报类型map
	 * @return
	 */
	private Map<String, String> getThemeTypeMap(){
		Map<String, String> dataMap = new HashMap<String, String>();
		if(null != mTitlesList && mTitlesList.size() > 0){
			
			String key = null;
			String value = null;
			for(int i=0; i<mTitlesList.size(); i++){
				key = mTitlesList.get(i).getTitleId()+"";
				value = mTitlesList.get(i).getTitleName();
				dataMap.put(key, value);
			}
		}
		return dataMap;
	}
	
	/**
	 * 提交举报
	 */
	public void uploadReport() {
		if(mEtReportMessage.getText().toString().length() == 0){
			Toast.makeText(this, "举报内容不能为空", Toast.LENGTH_SHORT).show();;
			return;
		}
		String nameplate_img = "";
		if (mUrlList != null && mUrlList.size() > 0) {
			for (int i = 0; i < mUrlList.size(); i++) {
				nameplate_img += mUrlList.get(i);
				nameplate_img += ",";
			}
			nameplate_img = nameplate_img.substring(0,
					nameplate_img.length() - 1);
		}
		
		String reportTypeName = getText(mTvReportCategory);
		String reportTitleName = getText(mTvreportThemes);
		String reportTypeId = getTag(mTvReportCategory);
		String reportTitleId = getTag(mTvreportThemes);
		
		//提交举报
		RequstClient.submitReport(goods_id,
				AppContext.userId, reportTypeId + "", reportTitleId + "",
				nameplate_img, "2", mEtReportMessage.getText().toString(),
				reportTypeName, reportTitleName,
				new MyCustomResponseHandler(this, true) {

					@Override
					public void onFinish() {
						super.onFinish();
					}
					
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						Log.d(TAG, "提交返回信息：msg:"+content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(ReportActivity.this, "举报提交失败，error："+errorMsg,
										Toast.LENGTH_SHORT).show();
								Log.d(TAG, "提交审核失败,jsonString = "+content);
								return;
							} else {
								final UploadSuccessDialog dialog = new UploadSuccessDialog(ReportActivity.this);
								dialog.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										dialog.dismiss();
										ReportActivity.this.finish();
									}
								});
								dialog.show();
								Log.d(TAG, "提交审核成功，jsonString = "+content);
							}

						} catch (Exception e) {
							e.printStackTrace();
							Log.d(TAG, "提交请求出错，error:"+e);
						}
					}

				});
	}
	
	

	/**
	 * 显示举报类型和主题的popupwindow
	 */
	private SelectReportTypeAndTitlePopwindow mBaseInfoWindow = null;
	private BaseAdapter mAdapter;

//	private void showBaseInfoPopwindow(final int id) {
//			mBaseInfoWindow = new SelectReportTypeAndTitlePopwindow(this, new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//						long arg3) {
//					Log.d(TAG, "id = "+id);
//					if(id == R.id.tv_report_catogery_message){//选择举报类型
//						chosedTypeIndex = arg2;
//						mTitlesList = mBaseInfoTypes.get(chosedTypeIndex).getTitleList();
//						reportTypeId = mBaseInfoTypes.get(arg2).getTypeId();
//						mTvReportCategory.setText(mBaseInfoTypes.get(arg2).getTypeName());
//						mTvCategoryDescription.setText(mBaseInfoTypes.get(arg2).getTypeDescription());
//						mTvreportThemes.setText(mTitlesList.get(0).getTitleName());
//						Log.d(TAG,"report type chosed,report type id is:"+reportTypeId+",report type name is :"+mBaseInfoTypes.get(arg2).getTypeName());
//					}else if(id == R.id.tv_report_theme_message){
//						reportThemeId = mTitlesList.get(arg2).getTitleId();
//						mTvreportThemes.setText(mTitlesList.get(arg2).getTitleName());
//						Log.d(TAG,"report type chosed,report title id is:"+reportThemeId+",report title name is:"+mTitlesList.get(arg2).getTitleName());
//					}
//					mBaseInfoWindow.dismiss();
//					mBaseInfoWindow = null;
//				}
//			});
//		if(id == R.id.tv_report_catogery_message){
//			mAdapter = new TypeAdapter();
//		}else if(id == R.id.tv_report_theme_message){
//			mAdapter = new TitleAdapter();
//		}
//		mBaseInfoWindow.setAdapter(mAdapter);
//		if (!mBaseInfoWindow.isShowing()) {
//			mBaseInfoWindow.showAtLocation(mTvHandReport, Gravity.BOTTOM, 0, 0);
//		}
//	}

	
	/**
	 * 图片来源选择的popupwindow
	 */
	private SelectPicPopupWindow mPopupWindow = null;

	private void showpopwindow() {
		if (mPopupWindow == null) {
			mPopupWindow = new SelectPicPopupWindow(this,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mPopupWindow.isShowing()) {
								mPopupWindow.dismiss();
							}
							switch (v.getId()) {
							case R.id.btn_take_photo: // 拍照
								callCamerImage();
								break;
							case R.id.btn_pick_photo:
								callLocalImage();
								break;
							}
						}
					});
		}
		if (!mPopupWindow.isShowing()) {
			mPopupWindow.showAtLocation(mTvHandReport, Gravity.BOTTOM, 0, 0);
		}
	}

	/**
	 * 从照相机获取图片
	 */
	private File imageFile;

	private void callCamerImage() {
		imageFile = ImageFolder.getTempImageName();
		Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = Uri.fromFile(imageFile);
		captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		captureIntent.putExtra("return-data", true);
		startActivityForResult(captureIntent, CAMERA_WITH_DATA);
	}

	/**
	 * 从图库获取图片
	 */
	private void callLocalImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, CAMERA_PICK_PHOTO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		String localPath = null;
		Picture picture = null;
		switch (requestCode) {
		case 0x11:
			localPath = imageFile.getPath();
			Log.d(TAG, "图片来自图库，Path：" + imageFile.getPath());
			break;
		case 0x21:
			Uri selectedImage = data.getData();
			ContentResolver resolver = getContentResolver();
			String[] filePathColumns = { MediaStore.Images.Media.DATA };
			Cursor c = this.getContentResolver().query(selectedImage,
					filePathColumns, null, null, null);
			c.moveToFirst();
			int columnIndex = c.getColumnIndex(filePathColumns[0]);
			String picturePath = c.getString(columnIndex);
			c.close();
			localPath = picturePath;
			Log.d(TAG, "图片来自图库，Path：" + picturePath);
			break;
		}
		if(!(localPath.endsWith("jpg") || localPath.endsWith("png"))){
			Toast.makeText(this, "图片上传失败，仅支持“jpg”和“png”格式", Toast.LENGTH_SHORT).show();
			return;
		}if(new File(localPath).length() > (1024*1024)){
			Toast.makeText(this, "图片上传失败，图片尺寸不得超过1M", Toast.LENGTH_SHORT).show();
			return;
		}
		
		picture = new Picture(0, localPath);
		mPathList.add(localPath);
		Log.d(TAG, "添加一张图片，Path="+localPath);
		int size = mPicList.size();
		mPicList.remove(size - 1);
		mPicList.add(picture);
		if (mPicList.size() < 5) {// 最多加载5张照片
			mPicList.add(mAddPicture);
		}
		myGridAdapter.notifyDataSetChanged();
	}

	/**
	 * 上传图片
	 */
	private ImageUpload mImageLoad;

	private void upLoadImage() {
		if ((mPathList != null && mPathList.size() > 0)) {
			Log.d(TAG,"upLoadImage:mPathList.size="+mPathList.size());
			mImageLoad = new ImageUpload(this, mPathList,
					new UpLoadImageListener() {

						@Override
						public void UpLoadSuccess(ArrayList<String> netimageurls) {
							mUrlList = netimageurls;
							Log.d(TAG, "图片提交成功,mUrlList="
									+ mUrlList.toString());
							uploadReport();

						}

						@Override
						public void UpLoadFail() {
							Log.d(TAG, "图片提交失败");
						}
					});

			mImageLoad.startLoad();
			return;
		}

	}

	class MyGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mPicList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ImageViewWithDel view = (ImageViewWithDel) arg1;
			final int position = arg0;
			if (view == null) {
				view = new ImageViewWithDel(ReportActivity.this);
			}
			if (mPicList.get(arg0).getPath() != null) {
				Log.d(TAG, "MyGridAdapter.getView:position = " + arg0
						+ ",path=" + mPicList.get(arg0).getPath());
				
				//优化内存溢出
				view.setImage(mPicList.get(arg0).getPath());
				
				view.setVisibility(View.VISIBLE);
				view.setCallback(new MyImageViewCallBack() {

					@Override
					public void imgClick() {

					}

					@Override
					public void del(View v) {
						mPicList.remove(position);
						mPathList.remove(position);
						myGridAdapter.notifyDataSetChanged();
						if (mPicList.size() < 5 && !mPicList.contains(mAddPicture)) {
							mPicList.add(mAddPicture);
							Log.d(TAG, "deleted a origin picture");
						}
						Log.d(TAG, "deleted a picture,mPicList.size = "+mPicList.size()+",mPathList.size="+mPathList.size());
					}
				});
			} else {
				view.setImage(mPicList.get(arg0).getId());
				view.setVisibility(View.GONE);
				view.setCallback(new MyImageViewCallBack() {

					@Override
					public void imgClick() {
						showpopwindow();
					}

					@Override
					public void del(View v) {

					}
				});
			}

			return view;
		}

	}

	class TypeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mBaseInfoTypes.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = arg1;
			if (view == null) {
				view = LayoutInflater.from(ReportActivity.this).inflate(
						R.layout.commen_text, null);
			}
			TextView textView = (TextView) view
					.findViewById(R.id.textview_common);
//			textView.setClickable(false);
			textView.setText(mBaseInfoTypes.get(arg0).getTypeName());
			return view;
		}
	}
	
	class TitleAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mTitlesList == null ? 0 : mTitlesList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = arg1;
			if (view == null) {
				view = LayoutInflater.from(ReportActivity.this).inflate(
						R.layout.commen_text, null);
			}
			TextView textView = (TextView) view
					.findViewById(R.id.textview_common);
			textView.setClickable(false);
			textView.setText(mTitlesList.get(arg0).getTitleName());
			return view;
		}

	}

	class Picture {
		public int id;
		public String path;

		public Picture(int id, String path) {
			if (id > 0) {
				this.id = id;
			}
			this.path = path;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("base","onKeyDown == >");
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (UIHelper.mLoadDialog != null && UIHelper.mLoadDialog.isShowing()) {
				Log.d("base","KeyEvent.KEYCODE_BACK clicked, close dialog");
				UIHelper.cloesLoadDialog();
			} else {
				Log.d("base","KeyEvent.KEYCODE_BACK clicked, finish");
				this.finish();
			}
		}
		return true;
	}
}
