package com.huiyin.ui.store;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.wight.XListView;

import java.util.Date;

/**
 * Created by justme on 15/5/14.
 */
public class StoreNewComeActivity extends Activity {

    // Content View Elements

    private TextView ab_back;
    private TextView ab_title;
    private XListView mXlistview;

    // End Of Content View Elements

    private Context mContext;
    private int storeId;

    private GoodsListBean data;
    private int mPageindex;
    private StoreNewGoodsListAdpter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_promotion_list_layout);
        mContext = this;
        storeId = getIntent().getIntExtra("storeId", -1);
        findView();
        setListener();
        InitData();
    }

    private void findView() {
        ab_back = (TextView) findViewById(R.id.ab_back);
        ab_title = (TextView) findViewById(R.id.ab_title);
        ab_title.setText("新品");

        mXlistview = (XListView) findViewById(R.id.promotion_listview);
        mXlistview.setFooterDividersEnabled(false);
        mXlistview.setPullLoadEnable(false);
        mXlistview.setPullRefreshEnable(false);
        mAdapter = new StoreNewGoodsListAdpter(mContext,true);
        mXlistview.setAdapter(mAdapter);
    }

    private void setListener() {
        ab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mXlistview.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                loadPageData(1);
            }

            @Override
            public void onLoadMore() {
                loadPageData(2);
            }
        });
    }

    private void InitData() {
        loadPageData(1);
    }

    private void loadPageData(int loadType) {
        if (loadType == 1) {
            mPageindex = 1;
        } else {
            mPageindex += 1;
        }
        CustomResponseHandler handler = new CustomResponseHandler(this, true) {
            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                data = GoodsListBean.explainJson(content, mContext);
                if (data != null && data.type == 1) {

                    if (data.getGoodsList() != null && data.getGoodsList().size() > 0) {
                        if (mPageindex == 1) {
                            mAdapter.deleteItem();
                        }
                        mAdapter.addItem(data.getGoodsList());
                        if (data.getGoodsList().size() >= 10) {
                            mXlistview.setPullLoadEnable(true);
                        } else {
                            mXlistview.setPullLoadEnable(false);
                        }
                    }
                } else {
                    Toast.makeText(mContext, data.msg, Toast.LENGTH_SHORT).show();
                    mXlistview.setPullLoadEnable(false);
                    if (mPageindex == 1) {
                        mAdapter.deleteItem();
                    }
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                //endUpData();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                endUpData();
            }

        };
        RequstClient.goodsCategoryList(handler, storeId, null, 0, null, null, 5, 2, 7, mPageindex);

    }

    private void endUpData() {
        mXlistview.stopLoadMore(1);
        mXlistview.stopRefresh();
        mXlistview.setRefreshTime(DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString());
    }
}
