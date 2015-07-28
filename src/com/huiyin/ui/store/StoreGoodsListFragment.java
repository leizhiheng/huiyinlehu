package com.huiyin.ui.store;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.wight.XListView;

import java.util.Date;

/**
 * Created by justme on 15/5/12.
 */
public class StoreGoodsListFragment extends Fragment {
    private View rootView;
    private Context mContext;

    private TextView class_sort_list1_xiaoliang;
    private LinearLayout class_sort_list1_jiage_ll;
    private ImageView class_sort_list1_jiage_up;
    private ImageView class_sort_list1_jiage_down;
    private TextView class_sort_list1_jiage;
    private TextView class_sort_list1_pingjia;
    private TextView class_sort_list1_renqi;

    private XListView mXlistview;
    private GoodsListBean data;
    private int mPageindex;
    private StoreGoodsListAdpter mAdapter;

    private int currentSelectId;//顶部排序方式，当前选择的类型ID

    private String userId;
    private int storeId;
    private int categoryId;
    private int sortMode = 1;//排序方式 1销量，2价格，3评价，4人气
    private int sortType = 2;//排序类型： 1,升序，2降序
    private int mark;

    private StoreGoodsOnScroll mStoreGoodsOnScroll = null;

    @Override
    public void onAttach(Activity activity) {
        Log.i("GoodsList", "onAttach");
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("GoodsList", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("GoodsList", "onCreateView");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.store_goods_list_fragment, null);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("GoodsList", "onResume");
        if (data != null)
            return;
        userId = AppContext.getInstance().userId;
        findView();
        setListener();
        initData();
    }

    private void findView() {
        class_sort_list1_xiaoliang = (TextView) rootView.findViewById(R.id.class_sort_list1_xiaoliang);
        class_sort_list1_jiage_ll = (LinearLayout) rootView.findViewById(R.id.class_sort_list1_jiage_l1);
        class_sort_list1_jiage = (TextView) rootView.findViewById(R.id.class_sort_list1_jiage);
        class_sort_list1_jiage_up = (ImageView) rootView.findViewById(R.id.class_sort_list1_jiage_up);
        class_sort_list1_jiage_down = (ImageView) rootView.findViewById(R.id.class_sort_list1_jiage_down);
        class_sort_list1_pingjia = (TextView) rootView.findViewById(R.id.class_sort_list1_pingjia);
        class_sort_list1_renqi = (TextView) rootView.findViewById(R.id.class_sort_list1_renqi);
        mXlistview = (XListView) rootView.findViewById(R.id.class_sort_list1_xv);
        mXlistview.setPullLoadEnable(false);
        mXlistview.setPullRefreshEnable(true);

        mAdapter = new StoreGoodsListAdpter(mContext, false);
        mXlistview.setAdapter(mAdapter);


        currentSelectId = R.id.class_sort_list1_xiaoliang;
        sortMode = 1;
        sortType = 2;
        class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.index_red));
    }

    private void setListener() {
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
        //因为有两个位置用到了该Fragment，一个需要监听xListView是否滚动到顶部
        if (mStoreGoodsOnScroll != null) {
            mXlistview.setPullRefreshEnable(false);
            mXlistview.setOnScrollListener(new XListView.OnXScrollListener() {
                @Override
                public void onXScrolling(View view) {

                }

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == XListView.OnXScrollListener.SCROLL_STATE_IDLE)
                        // 判断滚动到顶部
                        if (view.getFirstVisiblePosition() == 0) {
                            if (mStoreGoodsOnScroll != null) {
                                mStoreGoodsOnScroll.ListScrollToTop();
                            }
                        }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
        class_sort_list1_xiaoliang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectId != R.id.class_sort_list1_xiaoliang) {
                    currentSelectId = R.id.class_sort_list1_xiaoliang;
                    class_sort_list1_pingjia.setTextColor(getResources().getColor(R.color.black));
                    class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.index_red));
                    class_sort_list1_renqi.setTextColor(getResources().getColor(R.color.black));
                    class_sort_list1_jiage.setTextColor(getResources().getColor(R.color.black));
                    sortMode = 1;
                    sortType = 2;
                    loadPageData(1);
                }
            }
        });

        class_sort_list1_jiage_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectId = R.id.class_sort_list1_jiage_l1;
                class_sort_list1_pingjia.setTextColor(getResources().getColor(R.color.black));
                class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.black));
                class_sort_list1_renqi.setTextColor(getResources().getColor(R.color.black));
                class_sort_list1_jiage.setTextColor(getResources().getColor(R.color.index_red));
                if (sortType == 2) {
                    class_sort_list1_jiage_up.setVisibility(View.VISIBLE);
                    class_sort_list1_jiage_down.setVisibility(View.GONE);
                    sortMode = 2;
                    sortType = 1;
                } else {
                    class_sort_list1_jiage_up.setVisibility(View.GONE);
                    class_sort_list1_jiage_down.setVisibility(View.VISIBLE);
                    sortMode = 2;
                    sortType = 2;
                }
                loadPageData(1);
            }
        });
        class_sort_list1_pingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectId != R.id.class_sort_list1_pingjia) {
                    currentSelectId = R.id.class_sort_list1_pingjia;
                    class_sort_list1_pingjia.setTextColor(getResources().getColor(R.color.index_red));
                    class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.black));
                    class_sort_list1_renqi.setTextColor(getResources().getColor(R.color.black));
                    class_sort_list1_jiage.setTextColor(getResources().getColor(R.color.black));
                    sortMode = 3;
                    sortType = 2;
                    loadPageData(1);
                }
            }
        });
        class_sort_list1_renqi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectId != R.id.class_sort_list1_renqi) {
                    currentSelectId = R.id.class_sort_list1_renqi;
                    class_sort_list1_pingjia.setTextColor(getResources().getColor(R.color.black));
                    class_sort_list1_xiaoliang.setTextColor(getResources().getColor(R.color.black));
                    class_sort_list1_renqi.setTextColor(getResources().getColor(R.color.index_red));
                    class_sort_list1_jiage.setTextColor(getResources().getColor(R.color.black));
                    sortMode = 4;
                    sortType = 2;
                    loadPageData(1);
                }
            }
        });
    }

    public void initData() {
        loadPageData(1);
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    private void loadPageData(int loadType) {
        if (loadType == 1) {
            mPageindex = 1;
        } else {
            mPageindex += 1;
        }
        CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {
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
                    } else {
                        mAdapter.deleteItem();
                        Toast.makeText(mContext,"该分类下没有商品",Toast.LENGTH_SHORT).show();
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
        RequstClient.goodsCategoryList(handler, storeId, null, categoryId, null, null, sortMode, sortType, mark, mPageindex);

    }

    private void endUpData() {
        mXlistview.stopLoadMore(1);
        mXlistview.stopRefresh();
        mXlistview.setRefreshTime(DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString());
    }

    public interface StoreGoodsOnScroll {
        void ListScrollToTop();
    }

    public void setmStoreGoodsOnScroll(StoreGoodsOnScroll mStoreGoodsOnScroll) {
        this.mStoreGoodsOnScroll = mStoreGoodsOnScroll;
    }
}
