package com.huiyin.ui.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.StoreHomeHListAdapter;
import com.huiyin.adapter.StoreViewFlowAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BaseBean;
import com.huiyin.ui.classic.HorizontialListView;
import com.huiyin.ui.home.SiteSearchActivity;
import com.huiyin.ui.store.StoreGoodsListFragment.StoreGoodsOnScroll;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.CommonTools;
import com.huiyin.utils.DensityUtil;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.ShareUtils;
import com.huiyin.wight.NestScrollView;
import com.huiyin.wight.viewflow.CircleFlowIndicator;
import com.huiyin.wight.viewflow.ViewFlow;

import java.util.ArrayList;
import java.util.List;

public class StoreHomeActivity extends Activity implements StoreGoodsOnScroll {

	/**店铺ID**/
	public static final String STORE_ID = "store_id";
	

    private LinearLayout ab_search;
    private TextView ab_back;
    private ImageView activity_index_sousuo_iv;
    private TextView activity_index_search_content;
    private TextView search_the_store;

    private ViewSwitcher mViewSwitcher;
    private TextView store_home_classify;
    private TextView store_home_infomation;
    private TextView store_home_contact;

    private NestScrollView scrollView;
    private ImageView store_home_head_image;
    private LinearLayout linearLayout;
    private TextView store_home_title;
    private TextView store_home_flower_people;
    private Button store_home_flower;
    private TextView store_home_all_prodect;
    private TextView store_home_new_prodect;
    private TextView store_home_sale;
    private LinearLayout store_home_share;
    private StoreHomeProductsView store_home_tuijian;
    private StoreHomeProductsView store_home_xinpin;
    // 广告轮换图
    private RelativeLayout layout_gallery;
    private ViewFlow viewFlow;
    private CircleFlowIndicator indic;
    private HorizontialListView store_home_hl;
    private LinearLayout store_home_hl_ll;
    private View gallery_top_line;
    private View gallery_second_line;
    // End Of Content View Elements


    private Context mContext;
    private int storeId;
    private StoreHomeBean data;
    private boolean status;
    private StoreGoodsListFragment mStoreGoodsListFragment;
    private PopupWindow mPopupWindow;
    private PopupWindow mChildPopupWindow;
    private StoreViewFlowAdapter ViewflowAdapter;
    private StoreHomeHListAdapter mHListAdapter;
    private String lehu_getID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_home_layout);
        mContext = this;
        storeId = getIntent().getIntExtra(STORE_ID, -1);
        
        if (storeId == -1) {
            Toast.makeText(mContext, "参数接收错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        findView();
        setListener();
        InitData();
    }

    private void findView() {
        ab_search = (LinearLayout) findViewById(R.id.ab_search);
        ab_back = (TextView) findViewById(R.id.ab_back);
        activity_index_sousuo_iv = (ImageView) findViewById(R.id.activity_index_sousuo_iv);
        activity_index_search_content = (TextView) findViewById(R.id.activity_index_search_content);
        search_the_store = (TextView) findViewById(R.id.search_the_store);

        mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        store_home_classify = (TextView) findViewById(R.id.store_home_classify);
        store_home_infomation = (TextView) findViewById(R.id.store_home_infomation);
        store_home_contact = (TextView) findViewById(R.id.store_home_contact);

        scrollView = (NestScrollView) findViewById(R.id.store_home_base_scrollView);
        store_home_head_image = (ImageView) findViewById(R.id.store_home_head_image);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        store_home_title = (TextView) findViewById(R.id.store_home_title);
        store_home_flower_people = (TextView) findViewById(R.id.store_home_flower_people);
        store_home_flower = (Button) findViewById(R.id.store_home_flower);
        store_home_flower.setTag(0);
        store_home_all_prodect = (TextView) findViewById(R.id.store_home_all_prodect);
        store_home_new_prodect = (TextView) findViewById(R.id.store_home_new_prodect);
        store_home_sale = (TextView) findViewById(R.id.store_home_sale);
        store_home_share = (LinearLayout) findViewById(R.id.store_home_share);
        store_home_tuijian = (StoreHomeProductsView) findViewById(R.id.store_home_tuijian);
        store_home_xinpin = (StoreHomeProductsView) findViewById(R.id.store_home_xinpin);
        layout_gallery = (RelativeLayout) findViewById(R.id.layout_gallery);
        viewFlow = (ViewFlow) findViewById(R.id.store_home_Viewflow);// 获得viewFlow对象
        indic = (CircleFlowIndicator) findViewById(R.id.store_homeViewflowindic); // viewFlow下的indic
        gallery_top_line=findViewById(R.id.gallery_top_line);
        gallery_second_line=findViewById(R.id.gallery_second_line);
        store_home_hl= (HorizontialListView) findViewById(R.id.store_home_hl);
        store_home_hl_ll= (LinearLayout) findViewById(R.id.store_home_hl_ll);
        layout_gallery.setVisibility(View.GONE);
    }

    private void setListener() {
        ab_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        store_home_flower.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userId = AppContext.getInstance().userId;
                if (userId == null) {
                    Toast.makeText(mContext, "您还没有登陆", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    i.setClass(mContext, LoginActivity.class);
                    mContext.startActivity(i);
                    return;
                }
                FouceOn();
            }
        });
        store_home_all_prodect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoreGoodsListActivity.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("mark", 5);
                startActivity(intent);
            }
        });
        store_home_new_prodect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoreNewComeActivity.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });
        store_home_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StorePromotionActivity.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });
        store_home_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.showShare(StoreHomeActivity.this, "汇银商城", data.getStoreInfo().getSTOREURL(), data.getStoreInfo().getSTORELOGO(), "精品好货，尽在乐虎");
            }
        });
        store_home_classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showClassify();
            }
        });
        store_home_infomation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoreInfoActivity.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });
        store_home_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        View contentView = scrollView.getChildAt(0);
                        if (contentView != null
                                && contentView.getMeasuredHeight() <= scrollView.getScrollY()
                                + scrollView.getHeight()) {
                            if (mStoreGoodsListFragment == null) {
                                mStoreGoodsListFragment = new StoreGoodsListFragment();
                            }
                            mStoreGoodsListFragment.setStoreId(storeId);
                            mStoreGoodsListFragment.setMark(5);
                            mStoreGoodsListFragment.setmStoreGoodsOnScroll(StoreHomeActivity.this);
                            if (mStoreGoodsListFragment.isAdded()) {
                                LogUtil.d("OnAction_up", "scrollviewbottom");
                                mViewSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_down_in));
                                mViewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_up_out));
                                mViewSwitcher.showNext();
                            } else {
                                getFragmentManager().beginTransaction().add(R.id.content_frame, mStoreGoodsListFragment).commit();
                            }
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
        search_the_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(mContext, SiteSearchActivity.class);
                i.putExtra("content", "");
                i.putExtra("storeId", String.valueOf(storeId));
                startActivity(i);
            }
        });
        ab_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(mContext, SiteSearchActivity.class);
                i.putExtra("content", "");
                i.putExtra("storeId", String.valueOf(storeId));
                startActivity(i);
            }
        });
    }

    private void InitData() {
        CustomResponseHandler handler = new CustomResponseHandler(this, true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                data = StoreHomeBean.explainJson(content, mContext);
                if (data.type > 0) {
                    RefreshStoreHome(data.getStoreInfo());
                }  else {
                    Toast.makeText(mContext,data.msg,Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        };
        RequstClient.storeIndex(handler, storeId);
    }

    private void RefreshStoreHome(StoreInfo storeInfo) {
        ImageManager.LoadWithServer(storeInfo.getSTORELOGO(), store_home_head_image);
        store_home_title.setText(storeInfo.getSTORENAME());
        store_home_flower_people.setText(storeInfo.getCOUNTFOCUSSTORE() + "人关注");
        store_home_flower.setText(storeInfo.getUSERFOCUSSTORE() == 0 ? "关注" : "已关注");
        store_home_flower.setTag(storeInfo.getUSERFOCUSSTORE());

        store_home_all_prodect.setText(storeInfo.getSTOREGOODSNUM() + "\n全部商品");
        store_home_new_prodect.setText(storeInfo.getNEWGOODSNUM() + "\n新品");
        store_home_sale.setText(storeInfo.getCOUNTPROMOTIONGOODS() + "\n促销");

        if (storeInfo.getRECOMMENDGOODSLIST() != null && storeInfo.getRECOMMENDGOODSLIST().size() > 0) {
            store_home_tuijian.setData(storeInfo.getRECOMMENDGOODSLIST(), 0, storeId);
        } else {
            store_home_tuijian.setVisibility(View.GONE);
        }

        if (storeInfo.getNEWGOODSLIST() != null && storeInfo.getNEWGOODSLIST().size() > 0) {
            store_home_xinpin.setData(storeInfo.getNEWGOODSLIST(), 1, storeId);
        } else {
            store_home_xinpin.setVisibility(View.GONE);
        }
        fillViewFlow(storeInfo.getSTORE_BANNER_LIST());
        fillHorizontialListView(storeInfo.getTICKET_ACTIVE_LIST());
        //隐藏banner图上的灰色分割线
        if(layout_gallery.getVisibility()==View.GONE&&store_home_hl_ll.getVisibility()==View.GONE){
            gallery_top_line.setVisibility(View.GONE);
            gallery_second_line.setVisibility(View.GONE);
        }
    }

    private void showClassify() {
        if (data.getStoreInfo().getSTORECATEGORY() == null || data.getStoreInfo().getSTORECATEGORY().size() <= 0) {
            Intent intent = new Intent(mContext, StoreGoodsListActivity.class);
            intent.putExtra("storeId", storeId);
            intent.putExtra("mark", 5);
            startActivity(intent);
            return;
        }
        if (mPopupWindow == null) {
            View content = LayoutInflater.from(mContext).inflate(R.layout.store_home_popwindow, null);

            ListView expandableListView = (ListView) content.findViewById(R.id.top_listview);
            final StoreHomeClassiscAdapter mAdapter = new StoreHomeClassiscAdapter(mContext);
            ArrayList<String> gruopName = new ArrayList<String>();
            for (int i = 0; i < data.getStoreInfo().getSTORECATEGORY().size(); i++) {
                gruopName.add(data.getStoreInfo().getSTORECATEGORY().get(i).getNAME());
            }
            mAdapter.addItem(gruopName);
            expandableListView.setAdapter(mAdapter);

            final ListView childListView = (ListView) content.findViewById(R.id.child_listview);
            final StoreHomeClassiscAdapter childAdapter = new StoreHomeClassiscAdapter(mContext);
            childListView.setAdapter(childAdapter);

            expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<StoreCategory.TwoCategory> temp = data.getStoreInfo().getSTORECATEGORY()
                            .get(position).getTWOCATEGORY();
                    if (temp != null && temp.size() > 0) {
                        if (position != childAdapter.getGroupID()) {
                            ArrayList<String> childName = new ArrayList<String>();
                            for (int i = 0; i < temp.size(); i++) {
                                childName.add(temp.get(i).getNAME());
                            }
                            childAdapter.resetData(childName, position);
                            TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                                    0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1f,
                                    Animation.RELATIVE_TO_SELF, 0);
                            animation.setDuration(1000);
                            childListView.startAnimation(animation);
                        }
                    } else {
                        mPopupWindow.dismiss();
                        int categoryId = data.getStoreInfo().getSTORECATEGORY().get(position).getID();
                        Intent intent = new Intent(mContext, StoreGoodsListActivity.class);
                        intent.putExtra("storeId", storeId);
                        intent.putExtra("mark", 2);
                        intent.putExtra("categoryId", categoryId);
                        startActivity(intent);
                    }
                }
            });

            childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mPopupWindow.dismiss();
                    int categoryId = data.getStoreInfo().getSTORECATEGORY().get(childAdapter.getGroupID()).getTWOCATEGORY().get(position).getID();
                    Intent intent = new Intent(mContext, StoreGoodsListActivity.class);
                    intent.putExtra("storeId", storeId);
                    intent.putExtra("mark", 3);
                    intent.putExtra("categoryId", categoryId);
                    startActivity(intent);
                }
            });
            mPopupWindow = new PopupWindow(content, store_home_classify.getWidth()*2, DensityUtil.dp2px(mContext, 200));
            // 设置SelectPicPopupWindow弹出窗体可点击
            mPopupWindow.setFocusable(true);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            mPopupWindow.setAnimationStyle(android.R.style.Animation_Toast);
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0x00000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            mPopupWindow.setBackgroundDrawable(dw);
            mPopupWindow.setOutsideTouchable(true);
        }
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            mPopupWindow.showAtLocation(store_home_classify, Gravity.BOTTOM | Gravity.LEFT, 0, DensityUtil.dp2px(mContext, 55));
        }

    }

    private void showContact() {

    }

    @Override
    public void ListScrollToTop() {
        mViewSwitcher.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_up_in));
        mViewSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_down_out));
        mViewSwitcher.showPrevious();
    }


    private void FouceOn() {
        CustomResponseHandler handler = new CustomResponseHandler(
                this, true) {
            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(content, BaseBean.class);
                if (bean.type > 0) {
                    int tag = (Integer) store_home_flower.getTag();
                    if (tag == 0) {
                        store_home_flower.setTag(1);
                        store_home_flower.setText("已关注");
                        data.getStoreInfo().setCOUNTFOCUSSTORE(data.getStoreInfo().getCOUNTFOCUSSTORE() + 1);
                        store_home_flower_people.setText(data.getStoreInfo().getCOUNTFOCUSSTORE() + "人关注");
                        Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();
                    } else {
                        store_home_flower.setTag(0);
                        store_home_flower.setText("关注");
                        data.getStoreInfo().setCOUNTFOCUSSTORE(data.getStoreInfo().getCOUNTFOCUSSTORE() - 1);
                        store_home_flower_people.setText(data.getStoreInfo().getCOUNTFOCUSSTORE() + "人关注");
                        Toast.makeText(mContext, "取消关注成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, bean.msg, Toast.LENGTH_SHORT).show();
                }
            }
        };
        String userId = AppContext.getInstance().userId;
        int tag = (Integer) store_home_flower.getTag();
        if (tag == 0) {
            RequstClient.addFocus(handler, userId, storeId, null, 0, 2);
        } else {
            RequstClient.cancelFocus(handler, userId, storeId, null, 0, 2);
        }
    }

    /**
     * banner图自动轮播
     */
    private void fillViewFlow(List<StoreBanner> banner){
        // 广告图
        if (banner==null||banner.size()<1){
            layout_gallery.setVisibility(View.GONE);
            return;
        }
        ViewflowAdapter = new StoreViewFlowAdapter(this, banner);
        layout_gallery.setVisibility(View.VISIBLE);
        viewFlow.setAdapter(ViewflowAdapter); // 对viewFlow添加图片
        viewFlow.setmSideBuffer(banner.size());
        viewFlow.setFlowIndicator(indic);
        viewFlow.setTimeSpan(5000);
        viewFlow.setSelection(banner.size() * 10); // 设置初始位置
        viewFlow.stopAutoFlowTimer();
        if (banner.size() <= 1) {
            // 图片大于1才有指示器
            indic.setVisibility(View.GONE);
        } else {
            viewFlow.startAutoFlowTimer(); // 启动自动播放
        }
    }

    private void fillHorizontialListView(final List<StoreLehu> lehus){
        if (lehus==null||lehus.size()<1){
            store_home_hl_ll.setVisibility(View.GONE);
        }else {
            store_home_hl_ll.setVisibility(View.VISIBLE);
            mHListAdapter = new StoreHomeHListAdapter(mContext, lehus);
            //根据乐虎券数量动态设置长度
            LinearLayout.LayoutParams mParams = getLayoutParams(lehus);
            store_home_hl.setLayoutParams(mParams);
            store_home_hl.setAdapter(mHListAdapter);
            store_home_hl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (appIsLogin(true) && lehus.get(position).getIS_GET().equals("0")) {
                        //未领取
                        Intent mIntent = new Intent(StoreHomeActivity.this, StoreLehuActivity.class);
                        mIntent.putExtra(StoreLehuActivity.ID, lehu_getID = lehus.get(position).getACTIVE_ID());
                        mIntent.putExtra(StoreLehuActivity.PRiCE, lehus.get(position).getPRICE());
                        mIntent.putExtra(StoreLehuActivity.TITLE, lehus.get(position).getNAME());
                        mIntent.putExtra(StoreLehuActivity.START, lehus.get(position).getSTART_TIME());
                        mIntent.putExtra(StoreLehuActivity.END, lehus.get(position).getEND_TIME());
                        mIntent.putExtra(StoreLehuActivity.RANGE, lehus.get(position).getRANGE_NAME());
                        startActivityForResult(mIntent, 1);
                    }
                }
            });
        }
    }
    /**
     *  根据乐虎券数量动态设置长度
     */
    private LinearLayout.LayoutParams getLayoutParams(List<StoreLehu> lehus) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width;
        if (lehus.size()* CommonTools.dip2px(this, 128)< dm.widthPixels){
            width=lehus.size()* CommonTools.dip2px(this,128);
        }else{
            width=LinearLayout.LayoutParams.MATCH_PARENT;
        }
        int height=CommonTools.dip2px(this, 80);
        return new LinearLayout.LayoutParams(width, height);
    }

    /**
     * 判断App用户是否已经登录
     * @return
     */
    public boolean appIsLogin(boolean startToLoginActivity) {
        if (TextUtils.isEmpty(AppContext.userId) || null == AppContext.mUserInfo) {
            Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();

            if(startToLoginActivity){
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent backdata) {
        super.onActivityResult(requestCode, resultCode, backdata);
        if (requestCode==1&&resultCode==RESULT_OK){
            for (int i = 0; i < data.getStoreInfo().getTICKET_ACTIVE_LIST().size(); i++) {
                if (lehu_getID.equals( data.getStoreInfo().getTICKET_ACTIVE_LIST().get(i).getACTIVE_ID())){
                    data.getStoreInfo().getTICKET_ACTIVE_LIST().get(i).setIS_GET("1");
                }
            }
            fillHorizontialListView(data.getStoreInfo().getTICKET_ACTIVE_LIST());
        }
    }
}
