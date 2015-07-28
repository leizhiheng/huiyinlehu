package com.huiyin.ui.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.ui.home.SiteSearchActivity;

/**
 * Created by justme on 15/5/12.
 */
public class StoreGoodsListActivity extends Activity {

    // Content View Elements

    private LinearLayout index_sousuo_linlayout;
    private ImageView ab_back;
    private TextView ab_search;
    private ImageView ab_filter;
    private android.support.v4.widget.DrawerLayout drawer_layout;
    private FrameLayout content_frame;
    private ExpandableListView mExpandableListView;

    // End Of Content View Elements

    private Context mContext;
    private int storeId;
    private int categoryId;
    private int mark;
    private StoreCategoryBean data;
    private StoreCategoryAdapter mAdapter;
    private StoreGoodsListFragment mStoreGoodsListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_goods_list_layout);
        mContext = this;
        Intent intent = getIntent();

        storeId = intent.getIntExtra("storeId", -1);
        categoryId = intent.getIntExtra("categoryId",-1);
        mark = intent.getIntExtra("mark",-1);

        findView();
        setListener();
        InitData();
    }

    private void findView() {
        index_sousuo_linlayout = (LinearLayout) findViewById(R.id.index_sousuo_linlayout);
        ab_back = (ImageView) findViewById(R.id.ab_back);
        ab_search = (TextView) findViewById(R.id.ab_search);
        ab_filter = (ImageView) findViewById(R.id.ab_filter);
        drawer_layout = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer_layout.setDrawerLockMode(android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        content_frame = (FrameLayout) findViewById(R.id.content_frame);
        mExpandableListView = (ExpandableListView) findViewById(R.id.filter_list);
        mAdapter = new StoreCategoryAdapter(mContext);
        mExpandableListView.setAdapter(mAdapter);

        mStoreGoodsListFragment = new StoreGoodsListFragment();
        mStoreGoodsListFragment.setMark(mark);
        mStoreGoodsListFragment.setStoreId(storeId);
        if(categoryId!=-1)
            mStoreGoodsListFragment.setCategoryId(categoryId);
        getFragmentManager().beginTransaction().add(R.id.content_frame,mStoreGoodsListFragment).commit();
    }

    private void setListener() {
        ab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SiteSearchActivity.class);
                startActivity(intent);
            }
        });
        ab_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer_layout.isDrawerOpen(Gravity.END)) {
                    drawer_layout.closeDrawer(Gravity.END);
                } else {
                    drawer_layout.openDrawer(Gravity.END);
                }
            }
        });
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (mAdapter.getChildrenCount(groupPosition) <= 0) {
                    int categoryId = data.getStoreCategoryList().get(groupPosition).getID();
                    if(mStoreGoodsListFragment==null) {
                        mStoreGoodsListFragment = new StoreGoodsListFragment();
                    }
                    mStoreGoodsListFragment.setStoreId(storeId);
                    mStoreGoodsListFragment.setMark(2);
                    mStoreGoodsListFragment.setCategoryId(categoryId);
                    if(mStoreGoodsListFragment.isAdded()) {
                        mStoreGoodsListFragment.initData();
                    } else {
                        getFragmentManager().beginTransaction().add(R.id.content_frame,mStoreGoodsListFragment).commit();
                    }
                    drawer_layout.closeDrawer(Gravity.END);
                    return true;
                }
                return false;
            }
        });
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                int categoryId = data.getStoreCategoryList().get(groupPosition).getTWOCATEGORY().get(childPosition).getID();
                if(mStoreGoodsListFragment==null) {
                    mStoreGoodsListFragment = new StoreGoodsListFragment();
                }
                mStoreGoodsListFragment.setStoreId(storeId);
                mStoreGoodsListFragment.setMark(3);
                mStoreGoodsListFragment.setCategoryId(categoryId);
                if(mStoreGoodsListFragment.isAdded()) {
                    mStoreGoodsListFragment.initData();
                } else {
                    getFragmentManager().beginTransaction().add(R.id.content_frame,mStoreGoodsListFragment).commit();
                }
                drawer_layout.closeDrawer(Gravity.END);
                return true;
            }
        });
    }

    private void InitData() {
        CustomResponseHandler handler = new CustomResponseHandler(this, true) {
            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                data = StoreCategoryBean.explainJson(content, mContext);
                if (data != null && data.type > 0) {
                    mAdapter.addItem(data.getStoreCategoryList());
                }
            }
        };
        RequstClient.storeCategory(handler, storeId);
    }

    @Override
    public void finish() {
        if (drawer_layout.isDrawerOpen(Gravity.END)) {
            drawer_layout.closeDrawer(Gravity.END);
            return;
        }
        super.finish();
    }
}
