package com.huiyin.ui.classic;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ReportItem;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.wight.PullToRefreshView;
import com.huiyin.wight.PullToRefreshView.OnFooterRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 该页用于显示"我的投诉"列表，用到的数据对象为：ReportItem
 *
 * @author leizhiheng
 */
public class ReportsListActivity extends BaseActivity {

    private static final String TAG = "ReportList";

    private TextView mTvTitle, mTvTip;
    private View mLayoutTip;
    private PullToRefreshView mPullToRefreshView;
    private ListView mListView;
    private ArrayList<ReportItem> mReportDatas;
    private ReportListAdapter mAdapter;
    private int pageIndex = 1;
    private int totalPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        init();
        uploadReportRecords();
    }

    private void init() {
        findViewById(R.id.right_ib).setVisibility(View.GONE);
        mTvTitle = (TextView) findViewById(R.id.middle_title_tv);
        mTvTip = (TextView) findViewById(R.id.tv_tip);
        mLayoutTip = findViewById(R.id.layout_tip);
        mTvTitle.setText("我的举报");
        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh_view);
        mPullToRefreshView.setCanPullDown(false);
        mPullToRefreshView.setOnFooterRefreshListener(new OnFooterRefreshListener() {

            @Override
            public void onFooterRefresh(PullToRefreshView view) {
                if (pageIndex == totalPages) {
                    mPullToRefreshView.onFooterRefreshComplete();
                    Toast.makeText(ReportsListActivity.this, "已没有更多数据", Toast.LENGTH_SHORT).show();
                } else {
                    uploadReportRecords();
                }
            }
        });
        mListView = (ListView) findViewById(R.id.listView_reports);
        mReportDatas = new ArrayList<ReportItem>();
        mAdapter = new ReportListAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    private void uploadReportRecords() {
        RequstClient.loadReportList(AppContext.userId, pageIndex + "", new MyCustomResponseHandler(this, true) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");

                        Toast.makeText(ReportsListActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        mTvTip.setVisibility(View.VISIBLE);
                        mLayoutTip.setVisibility(View.INVISIBLE);
                        return;
                    } else {
                        //请求成功
                        Log.d(TAG, "举报列表信息获取成功：msg:" + content);
                        mReportDatas = JSONParseUtils.parseReportList(content);
                        totalPages = Integer.valueOf(JSONParseUtils.getString(content, "totalPageNum"));
                        pageIndex = Integer.valueOf(JSONParseUtils.getString(content, "pageIndex"));
                        mAdapter.notifyDataSetChanged();
                        mPullToRefreshView.onFooterRefreshComplete();
                        if (mReportDatas.size() > 0) {
                            mTvTip.setVisibility(View.INVISIBLE);
                            mLayoutTip.setVisibility(View.VISIBLE);
                        } else {
                            mTvTip.setVisibility(View.VISIBLE);
                            mLayoutTip.setVisibility(View.INVISIBLE);
                        }
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("base", "onKeyDown == >");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (UIHelper.mLoadDialog != null && UIHelper.mLoadDialog.isShowing()) {
                Log.d("base", "KeyEvent.KEYCODE_BACK clicked, close dialog");
                UIHelper.cloesLoadDialog();
            } else {
                Log.d("base", "KeyEvent.KEYCODE_BACK clicked, finish");
                this.finish();
            }
        }
        return true;
    }

    class ReportListAdapter extends BaseAdapter {

        private static final String TAG = "ReportListAdapter";
        private Context mContext;
        private LayoutInflater inflater;
        private ImageLoader mImageLoader;

        public ReportListAdapter(Context context) {
            this.mContext = context;
            inflater = LayoutInflater.from(mContext);
            mImageLoader = ImageLoader.getInstance();
        }

        @Override
        public int getCount() {
            return mReportDatas.size();
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
            final int position = arg0;
            ViewHolder holder = null;
            final ReportItem item = mReportDatas.get(arg0);
            View view = arg1;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.layout_report_list_item, null);
                holder.storeIcon = (ImageView) view.findViewById(R.id.report_item_shop_logo);
                holder.storeName = (TextView) view.findViewById(R.id.report_item_shop_name);
                holder.dealStatus = (TextView) view.findViewById(R.id.report_item_deal_status);
                holder.goodImg = (ImageView) view.findViewById(R.id.report_item_goods_icon);
                holder.arrowToStore = (ImageView) view.findViewById(R.id.arrow_to_shop);
                holder.goodDescription = (TextView) view.findViewById(R.id.report_item_good_message);
                holder.goodPrice = (TextView) view.findViewById(R.id.report_item_goods_price);
                holder.reportSubmitTime = (TextView) view.findViewById(R.id.report_item_submit_time);
                holder.reportContent = (TextView) view.findViewById(R.id.report_item_report_content);
                holder.productView = view.findViewById(R.id.report_item_product_layout);
                
                view.setTag(holder);
            }else{
            	holder = (ViewHolder) view.getTag();
            }
            //统一图片获取方式用于省流量设置
            ImageManager.LoadWithServer(URLs.IMAGE_URL + item.getGoodsImg(), holder.goodImg);
            Log.d(TAG, "ReportList.goodImg:" + item.getGoodsImg());
            holder.storeName.setText(item.getStoreName());
            if (item.getStoreId() > 0) {
                holder.arrowToStore.setVisibility(View.VISIBLE);
                //统一图片获取方式用于省流量设置
                ImageManager.LoadWithServer(URLs.IMAGE_URL + item.getStoreLogo(), holder.storeIcon);
                holder.storeName.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        //跳转到店铺页面
                        Intent intent = new Intent(ReportsListActivity.this, StoreHomeActivity.class);
                        intent.putExtra(StoreHomeActivity.STORE_ID, (int)item.getStoreId());
                        startActivity(intent);
                    }
                });
            } else {
                holder.storeIcon.setImageResource(R.drawable.lehu);
                holder.arrowToStore.setVisibility(View.INVISIBLE);
            }
            holder.goodDescription.setText(item.getGoodsName());
            if (item.getStatus() == 1) {
                holder.dealStatus.setText("已处理");
            } else {
                holder.dealStatus.setText("未处理");
            }
            holder.reportSubmitTime.setText(item.getReportTime());
            holder.reportContent.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent();
                    intent.setClass(ReportsListActivity.this, ReportContentActivity.class);
                    intent.putExtra("reportId", mReportDatas.get(position).getReportId());
                    intent.putExtra("store_name", mReportDatas.get(position).getStoreName());
                    intent.putExtra("store_logo", mReportDatas.get(position).getStoreLogo());
                    startActivity(intent);
                }
            });
            Log.d(TAG, "ReportListAdapter.getView:position = " + arg0);
            
            
            //跳转到商品详情
            holder.productView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					 
					ReportItem product = mReportDatas.get(position);
					
					Intent intent = new Intent(mContext, ProductsDetailActivity.class);
					intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, product.getGoodsId()+"");
					intent.putExtra(ProductsDetailActivity.STORE_ID, product.getStoreId()+"");
					intent.putExtra(ProductsDetailActivity.GOODS_NO, product.getGoodsNo()+"");
					mContext.startActivity(intent);
				}
			});	
            
            return view;
        }

        class ViewHolder {
            ImageView storeIcon;
            TextView storeName;
            TextView dealStatus;
            ImageView goodImg;
            ImageView arrowToStore;
            TextView goodDescription;
            TextView goodPrice;
            TextView reportSubmitTime;
            TextView reportContent;
            View productView;
        }
    }

}
