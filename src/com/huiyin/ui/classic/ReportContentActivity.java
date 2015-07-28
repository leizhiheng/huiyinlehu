package com.huiyin.ui.classic;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.ImageAdapter;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ReportItemDetail;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 举报内容
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-13
 */
public class ReportContentActivity extends BaseActivity implements OnItemClickListener {

    private static final String TAG = "ReportContent";
    private TextView mTvTitle;
    private TextView mTvShopName;
    private ImageView mIvShopLogo;
    private ImageView mIvGoodsImg;
    private ImageView mIvToStore;
    private TextView mTvGoodsDescription;
    private TextView mTvGoodsPrice;
    private TextView mTvReportType;
    private TextView mTvReportTitle;
    private TextView mTvReportContent;
    
    /**处理结果布局**/
    private View handleResultLayout;
    
    /**上传图片**/
	private GridView uploadGridView;
	
    private TextView mTvDealResult;
    private TextView mTvExtraMessage;

    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private ReportItemDetail mReportDetail;
   
    /**图片上传适配器**/
	private ImageAdapter imageAdapter;

    private String reportId;
    private String storeName;
    private String storeLogo;
    private String[] netImageURLs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_content);

        init();
        loadReportDetail();
    }

    public void init() {
        Intent intent = getIntent();
        if (intent != null) {
            reportId = intent.getLongExtra("reportId", 233) + "";
            storeLogo = intent.getStringExtra("store_logo");
            storeName = intent.getStringExtra("store_name");
        }
        mInflater = LayoutInflater.from(this);
        mImageLoader = ImageLoader.getInstance();
        mReportDetail = new ReportItemDetail();
        findViewById(R.id.right_ib).setVisibility(View.GONE);
        mTvTitle = (TextView) findViewById(R.id.middle_title_tv);
        mTvTitle.setText("举报内容");
        mIvShopLogo = (ImageView) findViewById(R.id.report_content_shop_logo);
        mTvShopName = (TextView) findViewById(R.id.report_content_shop_name);
        mIvGoodsImg = (ImageView) findViewById(R.id.report_content_goods_icon);
        mIvToStore = (ImageView) findViewById(R.id.arrow_to_shop);
        mTvGoodsDescription = (TextView) findViewById(R.id.report_content_good_message);
        mTvGoodsPrice = (TextView) findViewById(R.id.report_content_good_price);
        mTvReportType = (TextView) findViewById(R.id.report_content_report_type);
        mTvReportTitle = (TextView) findViewById(R.id.report_content_report_title);
        mTvReportContent = (TextView) findViewById(R.id.report_content_report_content);
        uploadGridView = (GridView) findViewById(R.id.upload_gridview);
        mTvDealResult = (TextView) findViewById(R.id.report_content_deal_result);
        mTvExtraMessage = (TextView) findViewById(R.id.report_content_extra_message);
        
        //处理结果
        handleResultLayout = findViewById(R.id.handle_result_layout);
        
        int width = (DeviceUtils.getWidthMaxPx(mContext)*4)/5;
		imageAdapter = new ImageAdapter(mContext, width, 4);
		uploadGridView.setAdapter(imageAdapter);
		uploadGridView.setOnItemClickListener(this);
    }

    public void setValue() {
        Log.d(TAG, "setValue==>");
        mTvReportType.setText(mReportDetail.getTypeName());
        mTvReportTitle.setText(mReportDetail.getTitleName());
        mTvReportContent.setText(mReportDetail.getReportContent());
        mTvGoodsDescription.setText(mReportDetail.getGoodsName());
        mTvGoodsPrice.setText(MathUtil.priceForAppWithSign(mReportDetail.getGoodsPrice()));
        //统一图片获取方式用于省流量设置
        ImageManager.LoadWithServer(URLs.IMAGE_URL + mReportDetail.goodsImg, mIvGoodsImg);

        mTvShopName.setText(storeName);
        if (MathUtil.stringToInt(mReportDetail.getStoreId()) > 0) {
            mIvToStore.setVisibility(View.VISIBLE);
            //统一图片获取方式用于省流量设置
            ImageManager.LoadWithServer(URLs.IMAGE_URL + storeLogo, mIvShopLogo);
            mTvShopName.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    //跳转到店铺页面
                    Intent intent = new Intent(ReportContentActivity.this, StoreHomeActivity.class);
                    intent.putExtra(StoreHomeActivity.STORE_ID, MathUtil.stringToInt(mReportDetail.getStoreId()));
                    startActivity(intent);
                }
            });
        }else{
        	
        	//非自营不显示向右箭头
        	mIvToStore.setVisibility(View.GONE);
        }

        //处理结果
        mTvDealResult.setText(mReportDetail.getProceTypeText());
        
        //如果是未处理，则不显示布局
        handleResultLayout.setVisibility(mReportDetail.isUnProce() ? View.GONE : View.VISIBLE);
        
        mTvExtraMessage.setText(mReportDetail.getProceContent());

        if (TextUtils.isEmpty(mReportDetail.getImg())) {
        	uploadGridView.setVisibility(View.GONE);
            return;
        }
        netImageURLs = mReportDetail.getImg().split(",");
        Log.d(TAG, "mReportDetail.getImg=" + mReportDetail.getImg() + ",netImageURLs.length=" + netImageURLs.length);
        if (netImageURLs != null && netImageURLs.length > 0) {
        	uploadGridView.setVisibility(View.VISIBLE);
        	
        	//更新上传的图片
    		imageAdapter.refreshData(mReportDetail.getImageData());
        }
        
        
    }

    public void loadReportDetail() {
        RequstClient.loadReportDetails(AppContext.userId, reportId + "", new MyCustomResponseHandler(this, true) {

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(ReportContentActivity.this, errorMsg,
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "loadReportInfo failed");
                        return;
                    } else {
                        Log.d(TAG, "举报详情下载成功，msg:" + content);
                        mReportDetail = JSONParseUtils.parseReportDetail(content);
                        // 获取举报详情
                        setValue();
                    }

                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "setValue error,msg:" + e);
                }
            }

        });
    }

    class MyGridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return netImageURLs.length;
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
                view = mInflater.inflate(R.layout.layout_gridview_item_content, null);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_big);
            //统一图片获取方式用于省流量设置
            ImageManager.LoadWithServer(URLs.IMAGE_URL + netImageURLs[arg0], imageView);
            return view;
        }

    }

    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg0.getId()) {
		case R.id.upload_gridview:
			
			//上传凭证图片
			startPhotoViewActivity(imageAdapter.getImageUrlList(), arg2);
			
			break;
		}
	}
}
