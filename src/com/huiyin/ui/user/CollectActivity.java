package com.huiyin.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CollectActivity extends BaseActivity {

    public final static String TAG = "CollectActivity";
    TextView left_rb, middle_title_tv;
    // 商品id
    String commodityId;
    //商品规格
    int goodsNo;
    // 位置
    int pos;
    // 店铺ID
    int storeId;
    private XListView mListView;
    private CollectBean mCollectBean;
    private CollectAdapter mCollectAdapter;
    private StoreBean mStoreBean;
    private StoreAdapter mStoreAdapter;
    private RadioButton goods, store;
    private int page = 1;
    private int pageSize = 10;
    private int request_code = 123;
    private int type; //关注类型： 1 ：商品 2 ：店铺

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_list_layout);

        initView();
        getCollectList(true);

    }

    private void initView() {

        middle_title_tv = (TextView) findViewById(R.id.ab_title);
        middle_title_tv.setText("我的关注");

        left_rb = (TextView) findViewById(R.id.ab_back);
        left_rb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        goods = (RadioButton) findViewById(R.id.goods_RadioButton);
        store = (RadioButton) findViewById(R.id.store_RadioButton);
        type = 1;
        goods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = 1;
                    page = 1;
                    getCollectList(false);
                }
            }
        });
        store.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = 2;
                    page = 1;
                    getCollectList(false);
                }
            }
        });

        mCollectBean = new CollectBean();
        mListView = (XListView) findViewById(R.id.xlistview);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.setFooterDividersEnabled(false);
        mListView.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getCollectList(false);
            }

            @Override
            public void onLoadMore() {
                int count = type == 1 ? mCollectAdapter.getCount() : mStoreAdapter.getCount();
                page = count / pageSize + 1;
                if (count % pageSize > 0)
                    page++;
                getCollectList(false);
            }
        });

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == 0) {
                    return;
                }
                pos = arg2;
                if (type == 1) {
                    commodityId = mCollectBean.collectList.get(arg2 - 1).GOODS_ID;
                    goodsNo = mCollectBean.collectList.get(arg2 - 1).getGOODS_NO();
                    storeId = mCollectBean.collectList.get(arg2 - 1).getSTORE_ID();
                } else {
                    storeId = mStoreBean.collectList.get(arg2 - 1).STORE_ID;
                }
                Intent i = new Intent(CollectActivity.this, CommonConfrimCancelDialog.class);
                i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CANCEL_FOCUS_TYPE);
                startActivityForResult(i, request_code);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (request_code == requestCode && resultCode == RESULT_OK) {
            if (type == 1) {
                // 查看商品详情
                Intent i = new Intent(CollectActivity.this, ProductsDetailActivity.class);
                i.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, commodityId);
                i.putExtra(ProductsDetailActivity.GOODS_NO, String.valueOf(goodsNo));
                i.putExtra("position", pos);
                startActivityForResult(i, 69);
            } else {
                //查看店铺主页
                Intent intent = new Intent(CollectActivity.this, StoreHomeActivity.class);
                intent.putExtra(StoreHomeActivity.STORE_ID, storeId);
                intent.putExtra("position", pos);
                startActivityForResult(intent, 96);
            }
        } else if (request_code == requestCode && resultCode == RESULT_FIRST_USER) {
            // 取消关注
            cancleFoucs();
        } else if (resultCode == RESULT_OK && requestCode == 69) {
            int position = data.getIntExtra("positon", -1);
            int isCollect = data.getIntExtra("isCollect", 0);
            if (isCollect != 1 && position != -1) {
                mCollectBean.collectList.remove(pos - 1);
                mCollectAdapter.notifyDataSetChanged();
            }
        } else if (resultCode == RESULT_OK && requestCode == 96) {
            int position = data.getIntExtra("positon", -1);
            int isCollect = data.getIntExtra("isCollect", 0);
            if (isCollect != 1 && position != -1) {
                mStoreBean.collectList.remove(pos - 1);
                mStoreAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 取消关注
     */
    private void cancleFoucs() {
        if (AppContext.userId == null) {
            return;
        }
        CustomResponseHandler handler = new CustomResponseHandler(
                this, true) {
            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                LogUtil.i(TAG, "cancelFoucs:" + content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (type == 1) {
                        mCollectBean.collectList.remove(pos - 1);
                        mCollectAdapter.notifyDataSetChanged();
                    } else {
                        mStoreBean.collectList.remove(pos - 1);
                        mStoreAdapter.notifyDataSetChanged();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if (type == 1) {
            RequstClient.cancelFocus(handler, AppContext.getInstance().userId, storeId, String.valueOf(goodsNo), MathUtil.stringToInt(commodityId), 1);
        } else {
            RequstClient.cancelFocus(handler, AppContext.getInstance().userId, storeId, null, 0, 2);
        }
    }

    /**
     * 获取收藏列表
     *
     * @param initB
     */
    private void getCollectList(final boolean initB) {

        if (AppContext.getInstance().userId == null) {
            return;
        }
        RequstClient.getCollectList(AppContext.getInstance().userId, page + "", type, new CustomResponseHandler(this, true) {

            @Override
            public void onFinish() {

                super.onFinish();
                mListView.stopLoadMore(1);
                mListView.stopRefresh();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                LogUtil.i(TAG, "getCollectList:" + content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (type == 1) {
                        if (page == 1) {
                            mCollectBean = new Gson().fromJson(content, CollectBean.class);
                            mCollectAdapter = new CollectAdapter(mCollectBean);
                            mListView.setAdapter(mCollectAdapter);

                            if (mCollectAdapter.getCount() < pageSize) {
                                mListView.hideFooter();
                                mListView.setPullLoadEnable(false);
                            } else {
                                mListView.showFooter();
                                mListView.setPullLoadEnable(true);
                            }
                        } else {
                            CollectBean collectBean = new Gson().fromJson(content, CollectBean.class);
                            if (collectBean.collectList != null) {
                                if (collectBean.collectList.size() > 0) {
                                    if (collectBean.collectList.size() < pageSize) {
                                        mListView.hideFooter();
                                        mListView.setPullLoadEnable(false);
                                    } else {
                                        mListView.showFooter();
                                        mListView.setPullLoadEnable(true);
                                    }
                                    mCollectBean.collectList.addAll(collectBean.collectList);
                                    mCollectAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(CollectActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
                                    mListView.hideFooter();
                                    mListView.setPullLoadEnable(false);
                                }
                            }
                            mListView.stopLoadMore(1);
                            mListView.stopRefresh();
                        }
                    } else if (type == 2) {
                        if (page == 1) {
                            mStoreBean = new Gson().fromJson(content, StoreBean.class);
                            mStoreAdapter = new StoreAdapter(mStoreBean);
                            mListView.setAdapter(mStoreAdapter);
                            if (mCollectAdapter.getCount() < pageSize) {
                                mListView.hideFooter();
                                mListView.setPullLoadEnable(false);
                            } else {
                                mListView.showFooter();
                                mListView.setPullLoadEnable(true);
                            }
                        } else {
                            StoreBean storeBean = new Gson().fromJson(content, StoreBean.class);
                            if (storeBean.collectList != null) {
                                if (storeBean.collectList.size() > 0) {
                                    if (storeBean.collectList.size() < pageSize) {
                                        mListView.hideFooter();
                                        mListView.setPullLoadEnable(false);
                                    } else {
                                        mListView.showFooter();
                                        mListView.setPullLoadEnable(true);
                                    }
                                    mStoreBean.collectList.addAll(storeBean.collectList);
                                    mStoreAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(CollectActivity.this, "已无更多数据！", Toast.LENGTH_LONG).show();
                                    mListView.hideFooter();
                                }

                            }
                            mListView.stopLoadMore(1);
                            mListView.stopRefresh();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    class CollectAdapter extends BaseAdapter {

        LayoutInflater inflater;
        CollectBean mCollectBean;

        public CollectAdapter(CollectBean mCollectBean) {
            this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mCollectBean = mCollectBean;
        }

        @Override
        public int getCount() {
            return mCollectBean.collectList == null ? 0 : mCollectBean.collectList.size();
        }

        @Override
        public Object getItem(int position) {
            return mCollectBean.collectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mCollectBean.collectList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CollectBean.CollectItem item = mCollectBean.collectList.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.collect_list_item, null);
            }

            TextView collect_good_title = (TextView) convertView.findViewById(R.id.collect_good_title);
            TextView collect_people_num = (TextView) convertView.findViewById(R.id.collect_people_num);
            TextView collect_good_price = (TextView) convertView.findViewById(R.id.collect_good_price);
            ImageView collect_listitem_image = (ImageView) convertView.findViewById(R.id.collect_listitem_image);

            ImageManager.LoadWithServer(item.IMG, collect_listitem_image);

            collect_good_title.setText(item.GOODS_NAME);
            collect_people_num.setText(item.COLLECT_COUNT + "人收藏");

            collect_good_price.setText(MathUtil.priceForAppWithSign(item.GOODS_PRICE));

            return convertView;
        }

    }

    class StoreAdapter extends BaseAdapter {

        LayoutInflater inflater;
        StoreBean mStoreBean;

        public StoreAdapter(StoreBean mStoreBean) {
            this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mStoreBean = mStoreBean;
        }

        @Override
        public int getCount() {
            return mStoreBean.collectList == null ? 0 : mStoreBean.collectList.size();
        }

        @Override
        public Object getItem(int position) {
            return mStoreBean.collectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mStoreBean.collectList.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final StoreBean.StoreItem item = mStoreBean.collectList.get(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.collect_store_item, null);
            }

            TextView collect_store_title = (TextView) convertView.findViewById(R.id.collect_store_title);
            RatingBar collect_store_rating = (RatingBar) convertView.findViewById(R.id.collect_store_rating);
            TextView collect_store_star = (TextView) convertView.findViewById(R.id.collect_store_star);
            TextView collect_people_num = (TextView) convertView.findViewById(R.id.collect_people_num);
            ImageView collect_store_new = (ImageView) convertView.findViewById(R.id.collect_store_new);
            ImageView collect_store_image = (ImageView) convertView.findViewById(R.id.collect_store_image);

            ImageManager.LoadWithServer(item.STORE_LOGO, collect_store_image);

            collect_store_title.setText(item.STORE_NAME);
            collect_people_num.setText(item.COLLECT_COUNT + "人收藏");
            collect_store_rating.setRating(item.STORE_SCORE);
            if (item.STORE_SCORE == 0) {
                item.STORE_SCORE = 5;
            }
            collect_store_star.setText(item.STORE_SCORE + "分");
            collect_store_new.setVisibility(item.NEW_GOODS_NUM > 0 ? View.VISIBLE : View.INVISIBLE);

            return convertView;
        }

    }

    public class CollectBean {

        public ArrayList<CollectItem> collectList = new ArrayList<CollectItem>();

        public class CollectItem {
            public String COLLECT_COUNT;// 收藏人数
            public String GOODS_PRICE;// 商品价格
            public String GOODS_ID;// 商品id
            public String IMG;// 收藏图标
            public String GOODS_NAME;// 商品名称
            public String FLAG_DEPRECIATE;// 如果等于0表示，没有降价，如果小于0表示价格
            public String PROMOTIONS_PRICE; //原来的促销价格什么的，更改接口之后去掉了
            public String GOODS_NO;//商品规格
            public String STORE_ID;//店铺ID

            public int getGOODS_NO() {
                try {
                    return Integer.parseInt(GOODS_NO);
                } catch (Exception e) {
                    return 0;
                }
            }

            public int getSTORE_ID() {
                try {
                    return Integer.parseInt(STORE_ID);
                } catch (Exception e) {
                    return 0;
                }
            }
        }
    }

    public class StoreBean {
        public ArrayList<StoreItem> collectList = new ArrayList<StoreItem>();

        public class StoreItem {
            public String STORE_NAME;//店铺名称
            public String STORE_LOGO;// 店铺logo
            public String COLLECT_COUNT;// 收藏人数
            public int NEW_GOODS_NUM;//新品数量（如果大于0，则显示”新品“），虎券，暂时不做
            public int STORE_ID;// 店铺id
            public float STORE_SCORE;// 店铺评分
        }
    }
}
