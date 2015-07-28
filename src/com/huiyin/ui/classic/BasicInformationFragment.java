package com.huiyin.ui.classic;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.FragmentViewPagerAdapter;
import com.huiyin.adapter.GoodsDetailGalleryAdapter;
import com.huiyin.adapter.GoodsGiftAdapter;
import com.huiyin.adapter.GoodsMJAdapter;
import com.huiyin.adapter.GoodsMZAdapter;
import com.huiyin.anim.DepthPageTransformer;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.DEFAULT_NORM;
import com.huiyin.bean.DiscountPackageBean;
import com.huiyin.bean.EVA_GOODS;
import com.huiyin.bean.GDBItem;
import com.huiyin.bean.GOOD;
import com.huiyin.bean.MayBeLikeBean;
import com.huiyin.bean.PROMOTION;
import com.huiyin.bean.ProductsDetailBeen;
import com.huiyin.db.SQLOpearteImpl;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.Utils;
import com.huiyin.wight.NestScrollView;
import com.huiyin.wight.rongcloud.RongCloud;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BasicInformationFragment extends BaseFragment implements
        OnClickListener, OnPageChangeListener {
    private static final String TAG = "BasicInformation";
    // --------------------产品轮换图---------
    private ViewPager gallery;
    private GoodsDetailGalleryAdapter galleryAdapter;
    private List<String> listGallerys;
    private TextView gallery_count;
    private LinearLayout indicator;
    private List<ImageView> indicatorList;
    // ----------------也许喜欢
    private LinearLayout maybe_like_base;
    private ViewPager mebyLikeViewPager;
    private List<Fragment> listLikeFragments;
    private FragmentViewPagerAdapter likeViewpagerAdapter;
    public ProductsDetailBeen goodsDetailBeen;
    // -----------------乐虎价格
    private TextView lehu_price;
    private TextView lehu_price1;
    //add by zhyao @2015/5/6  添加商品使用红包抵扣
    private TextView lehu_redpacket_deduction;//商品使用红包抵扣
    private TextView reference_price;
    private TextView nameTextView, descriptionTextView;
    private CheckBox collection_checkbox;
    private TextView collection_count;
    private int collection;
    private LinearLayout cuxiaojia;
    private LinearLayout cankaojia;
    private LinearLayout layout_huodong;
    // 基本信息
    private LinearLayout layout_baseinfo;
    private LinearLayout layout_ps;//配送
    private RatingBar ratingBar;
    private View layout_evaluation;
    private TextView tv_arraw;
    private LinearLayout layout_dianping;
    private View layout_shaidan;
    private View layout_kefu;
    private TextView shaidan_tv;

    // 计时的
    private LinearLayout countTimeLayout;
    private TextView countTime;
    private CountDownTimer mCountDownTimer;
    private int subscribeId;// 预约ID
    private TextView mTv_jb;
    private LinearLayout layout_select_params;//选规格
    private TextView mTv_lehujuan;
    private TextView mTv_jifen;
    private TextView mTv_money;
    private com.huiyin.wight.MyListView mLv_zs;
    private GoodsGiftAdapter goodsGiftAdapter;
    private com.huiyin.wight.MyListView mLv_mj;
    private GoodsMJAdapter goodsMJAdapter;
    private GoodsMZAdapter goodsMZAdapter;
    private com.huiyin.wight.MyExpandableListView mLv_mz;
    private TextView mTv_location;
    private ImageView mIv_dw;
    private TextView mTv_kc;
    private TextView mTv_shuxing;
    private TextView mTv_tip;
    private TextView mTv_product_tip;
    private TextView mTv_rqzh;
    private TextView mTv_tjdp;
    private TextView mTv_yhtc;
    private TextView mTv_dpname;
    private RatingBar mRb_pf;
    private TextView mTv_pf;
    private TextView mBtn_jrdp;
    private TextView mBtn_scdp;
    private ImageView mIv_arraw;
    private LinearLayout mLayout_zs;
    private LinearLayout mLayout_cx;
    private TextView mKefu_tv;
    private View relyview;
    public int numAfterParams = 1;//当前商品选择的数量
    private StringBuffer paramsWithOutNum;//规格（不包括数量）
    private SpecificationsSelect specificationsSelect;//选规格
    private ProductsDetailActivity activity;
    private NestScrollView scoroll_view;
    private TextView tv_to_webview;
    private LinearLayout layout_dianpu;//店铺布局
    private View line_zs;//赠送
    private View line_mj;//赠送
    private View tv_rqzh_line;
    private View tv_tjdp_line;
    //定位
    /**
     * 经度
     */
    private double lng = 0.0;
    /**
     * 纬度
     */
    private double lat = 0.0;
    private String provinceName;
    private String cityName;
    private String areaName;
    private int provinceId;
    private int cityId;
    private int areaId;
    private LocationClient mLocationClient;

    private static final String ARG_PARAM1="gdbbean";
    private static final String ARG_PARAM2="subscribeId";

    public boolean isRefresh=false;//是否刷新规格参数

    private String selectGoodNo="";
    private DiscountPackage discountPackage;
    public boolean isActive; //1新品预约，2团购，3秒杀，4单品，5闪购（倒计时/0,4无倒计时、折扣价/0不存在折扣价）等活动是否正在进行

    public BasicInformationFragment(){

    }

    public static BasicInformationFragment getInstance(ProductsDetailBeen gdbbean, int subscribeId){
        BasicInformationFragment fragment = new BasicInformationFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, gdbbean);
        args.putInt(ARG_PARAM2, subscribeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            goodsDetailBeen = (ProductsDetailBeen) getArguments().getSerializable(ARG_PARAM1);
            subscribeId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_goods_detail_baseinfo, null);
        initAllLists();
        findViewsAndSetListeners();
        return layoutView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (goodsDetailBeen != null) {
            // 解决一个网络数据与UI操作先后时序的BUG
            setView();
        }
    }

    private void initAllLists() {
        listGallerys = new ArrayList<String>();
        indicatorList = new ArrayList<ImageView>();
        listLikeFragments = new ArrayList<Fragment>();
    }

    private void findViewsAndSetListeners() {
        cuxiaojia = (LinearLayout) findViewById(R.id.cuxiaojia);
        cankaojia = (LinearLayout) findViewById(R.id.cankaojia);
        layout_huodong = (LinearLayout) findViewById(R.id.layout_huodong);
        lehu_price = (TextView) findViewById(R.id.lehu_price);
        lehu_price1 = (TextView) findViewById(R.id.lehu_price1);
        //add by zhyao @2015/5/6  添加商品使用红包抵扣
        lehu_redpacket_deduction = (TextView) findViewById(R.id.lehu_redpacket_deduction);
        reference_price = (TextView) findViewById(R.id.reference_price);
        nameTextView = (TextView) findViewById(R.id.name);
        descriptionTextView = (TextView) findViewById(R.id.description);
        layout_evaluation = findViewById(R.id.layout_evaluation);
        mTv_jb = (TextView) findViewById(R.id.tv_jb);
        layout_evaluation.setOnClickListener(this);
        mTv_jb.setOnClickListener(this);
        layout_select_params = (LinearLayout) findViewById(R.id.layout_select_params);

        layout_select_params.setOnClickListener(this);
        layout_dianping = (LinearLayout) findViewById(R.id.layout_dianping);
        layout_shaidan = findViewById(R.id.layout_shaidan);
        layout_shaidan.setOnClickListener(this);
        shaidan_tv = (TextView) findViewById(R.id.shaidan_tv);
        layout_kefu = findViewById(R.id.layout_kefu);
        layout_kefu.setOnClickListener(this);
        collection_checkbox = (CheckBox) findViewById(R.id.collection_checkbox);
        collection_count = (TextView) findViewById(R.id.collection_count);
        gallery_count = (TextView) findViewById(R.id.gallery_count);
        indicator = (LinearLayout) findViewById(R.id.indicator);
        gallery = (ViewPager) findViewById(R.id.gallery);
        galleryAdapter = new GoodsDetailGalleryAdapter(listGallerys,
                getActivity());
        gallery.setPageTransformer(true, new DepthPageTransformer());
        gallery.setAdapter(galleryAdapter);
        gallery.setOnPageChangeListener(this);
        likeViewpagerAdapter = new FragmentViewPagerAdapter(
                getFragmentManager(), listLikeFragments);
        maybe_like_base = (LinearLayout) findViewById(R.id.maybe_like_base);
        mebyLikeViewPager = (ViewPager) findViewById(R.id.mebyLikeViewPager);
        mebyLikeViewPager.setAdapter(likeViewpagerAdapter);
        layout_baseinfo = (LinearLayout) findViewById(R.id.layout_baseinfo);
        layout_ps = (LinearLayout) findViewById(R.id.layout_ps);
        layout_baseinfo.setOnClickListener(this);
        layout_ps.setOnClickListener(this);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setEnabled(false);
        tv_arraw = (TextView) findViewById(R.id.tv_arraw);
        countTimeLayout = (LinearLayout) findViewById(R.id.countTime_layout);
        countTime = (TextView) findViewById(R.id.countTime);
        mTv_jb = (TextView) findViewById(R.id.tv_jb);
        mTv_lehujuan = (TextView) findViewById(R.id.tv_lehujuan);
        mTv_jifen = (TextView) findViewById(R.id.tv_jifen);
        mTv_money = (TextView) findViewById(R.id.tv_money);
        mLv_zs = (com.huiyin.wight.MyListView) findViewById(R.id.lv_zs);
        mLv_mj = (com.huiyin.wight.MyListView) findViewById(R.id.lv_mj);
        mLv_mz = (com.huiyin.wight.MyExpandableListView) findViewById(R.id.lv_mz);
        mTv_location = (TextView) findViewById(R.id.tv_location);
        mIv_dw = (ImageView) findViewById(R.id.iv_dw);
        mTv_kc = (TextView) findViewById(R.id.tv_kc);
        mTv_shuxing = (TextView) findViewById(R.id.tv_shuxing);
        mTv_tip = (TextView) findViewById(R.id.tv_tip);
        mTv_product_tip = (TextView) findViewById(R.id.tv_product_tip);
        mTv_rqzh = (TextView) findViewById(R.id.tv_rqzh);
        tv_rqzh_line = findViewById(R.id.tv_rqzh_line);
        mTv_tjdp = (TextView) findViewById(R.id.tv_tjdp);
        tv_tjdp_line = findViewById(R.id.tv_tjdp_line);
        mTv_yhtc = (TextView) findViewById(R.id.tv_yhtc);
        mTv_dpname = (TextView) findViewById(R.id.tv_dpname);
        mRb_pf = (RatingBar) findViewById(R.id.rb_pf);
        mTv_pf = (TextView) findViewById(R.id.tv_pf);
        mBtn_jrdp = (TextView) findViewById(R.id.btn_jrdp);
        mBtn_scdp = (TextView) findViewById(R.id.btn_scdp);
        mIv_arraw = (ImageView) findViewById(R.id.iv_arraw);
        mLayout_zs = (LinearLayout) findViewById(R.id.layout_zs);
        mLayout_cx = (LinearLayout) findViewById(R.id.layout_cx);
        layout_dianpu = (LinearLayout) findViewById(R.id.layout_dianpu);
        line_zs = (View) findViewById(R.id.line_zs);
        line_mj = (View) findViewById(R.id.line_mj);
        mKefu_tv = (TextView) findViewById(R.id.kefu_tv);
        scoroll_view = (NestScrollView) findViewById(R.id.scoroll_view);
        tv_to_webview = (TextView) findViewById(R.id.tv_to_webview);
        tv_to_webview.setOnClickListener(this);
        scoroll_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        View contentView = scoroll_view.getChildAt(0);
                        if (contentView != null
                                && contentView.getMeasuredHeight() <= scoroll_view.getScrollY()
                                + scoroll_view.getHeight()) {
                            if (null != listener) {
                                listener.changeToWebView();
                            }
                        }
                        break;
                }
                return false;
            }
        });
        mTv_dpname.setOnClickListener(this);
        mBtn_jrdp.setOnClickListener(this);
        mBtn_scdp.setOnClickListener(this);

        mTv_rqzh.setOnClickListener(this);
        mTv_tjdp.setOnClickListener(this);
        mTv_yhtc.setOnClickListener(this);
    }

    /**
     * 填充页面数据
     */
    public void setView() {
        activity = ((ProductsDetailActivity) getActivity());
        relyview = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_goods_detail_baseinfo, null);
        isActive = isNowInActivity();
        if (null != specificationsSelect) { //当刷新商品规格时,先刷新规格选择页面的数据
            specificationsSelect.setNum(numAfterParams);
            specificationsSelect.refreshUI(goodsDetailBeen,isActive);
        }
        fillMainImageGallery();
        addViewToIndicator();
        setGoodsCollectionCheck();
        String name = goodsDetailBeen.goodsDetail.GOODS_NAME;// 产品名称
        String description = goodsDetailBeen.goodsDetail.AD;// 广告语
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        //add by zhyao @2015/5/6  添加商品使用红包抵扣
        //可用红包抵用金额
        if (!TextUtils.isEmpty(goodsDetailBeen.goodsDetail.BONUS) && Double.parseDouble(goodsDetailBeen.goodsDetail.BONUS) > 0) {
            lehu_redpacket_deduction.setText("可用红包金额 " + goodsDetailBeen.goodsDetail.BONUS+"元");
        }
        showPromotionPrice();
        showActivityButton();
        fillActivityTimer();
        showStockAndLocationAndServe();
        showStoreRating();
        showGoodsRatingAndScale();
        showRecommedGroupProducts();
        mayBeLike(goodsDetailBeen.goodsDetail.ID,goodsDetailBeen.goodsDetail.THIRD_GOODSCATEGORY_ID);
    }

    /**
     * 当前是否出于活动中,或者活动是否进行中<br/>
     * <br/>PS: 当FLAG_ACTIVITY==4 ,即单品促销时候,不管活动时间直接判定未活动激活.
     * @return
     */
    private boolean isNowInActivity() {
        return goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY()==4||
                Utils.beingActivity(goodsDetailBeen.goodsDetail.START_TIME
                , goodsDetailBeen.goodsDetail.END_TIME, goodsDetailBeen.curTime);
    }

    /**
     * 填充商品的主图片
     */
    private void fillMainImageGallery() {
        // 产品图
        String listGallerysStr = goodsDetailBeen.goodsDetail.GOODS_IMG_LIST;
        String gallerys[] = listGallerysStr.split(",");

        if (listGallerys == null) {
            listGallerys = new ArrayList<String>();
        } else {
            listGallerys.clear();
        }

        for (String string : gallerys) {
            listGallerys.add(URLs.IMAGE_URL + string);
        }
        galleryAdapter
                .setQuota(goodsDetailBeen.goodsDetail.getQUOTA_ONE() == 0);
        galleryAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化主图片下面的圆点指示器
     */
    private void addViewToIndicator() {
        if (0 != indicator.getChildCount()) {
            indicator.removeAllViews();
        }
        if (0 != indicatorList.size()) {
            indicatorList = new ArrayList<ImageView>();
        }
        if (listGallerys.size() > 0) {
            gallery_count.setText(1 + "/" + listGallerys.size() + "张");
            for (int i = 0; i < listGallerys.size(); i++) {
                ImageView imageView = new ImageView(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT); // , 1是可选写的
                lp.setMargins(5, 0, 5, 0);
                imageView.setLayoutParams(lp);
                if (i == 0) {
                    imageView
                            .setImageResource(R.drawable.page_indicator_focused);
                } else {
                    imageView.setImageResource(R.drawable.page_indicator);
                }
                indicator.addView(imageView);
                indicatorList.add(imageView);
            }
            if (listGallerys.size() > 1) {
                indicator.setVisibility(View.VISIBLE);
            } else {
                indicator.setVisibility(View.GONE);
            }
        } else {
            gallery_count.setText("");
            indicator.setVisibility(View.GONE);
        }
    }

    /**
     * 填充猜你喜欢的数据
     */
    private void fillMayLike(List<GDBItem> listLikes) {
        List<GDBItem> listTemp = new ArrayList<GDBItem>();
        if (listLikes != null) {
            listLikeFragments.clear();
            for (int i = 0; i < listLikes.size(); i++) {
                if (listTemp.size() < 4) {
                    listTemp.add(listLikes.get(i));
                    if (i >= listLikes.size() - 1) {
                        List<GDBItem> listItems = new ArrayList<GDBItem>();
                        listItems.addAll(listTemp);
                        GoodsDetailLikeFragment fragment = new GoodsDetailLikeFragment();
                        fragment.setData(listItems);
                        listLikeFragments.add(fragment);
                    }
                } else {
                    List<GDBItem> listItems = new ArrayList<GDBItem>();
                    listItems.addAll(listTemp);
                    GoodsDetailLikeFragment fragment = new GoodsDetailLikeFragment();
                    fragment.setData(listItems);
                    listLikeFragments.add(fragment);
                    listTemp = new ArrayList<GDBItem>();
                    listTemp.add(listLikes.get(i));
                }
            }

            likeViewpagerAdapter.notifyDataSetChanged();
            if (listLikes.size() > 0) {
                maybe_like_base.setVisibility(View.VISIBLE);
            } else {
                maybe_like_base.setVisibility(View.GONE);
            }
        }
    }
    /**
     * 填充商品评分的情况
     */
    private void showGoodsRatingAndScale() {
        String scale = goodsDetailBeen.goodsDetail.REVIEW_PERCENT;// 评分，人数，百分比
        float score = 0;// 评分，人数，百分比
        if (!"".equals(scale)) {
            score = Float.parseFloat(scale);
        }
        if (score <= 0) {
            score = 100;
        }
        tv_arraw.setText(MathUtil.keepMost1Decimal(score) + "%(" + goodsDetailBeen.goodsDetail.getREVIEW_NUMBER() + "人评价)");
        ratingBar.setMax(100);
        ratingBar.setProgress((int) score);
        shaidan_tv.setText("商品晒单(" + goodsDetailBeen.goodsDetail.getREVIEW_NUMBER() + ")");

        layout_dianping.removeAllViews();
        if (goodsDetailBeen.goodsDetail.EVA_GOODS_LIST != null) {

            if (goodsDetailBeen.goodsDetail.EVA_GOODS_LIST.size() >= 1) {
                layout_dianping
                        .addView(addDianPingToLayout(goodsDetailBeen.goodsDetail.EVA_GOODS_LIST
                                .get(0)));
            }
            if (goodsDetailBeen.goodsDetail.EVA_GOODS_LIST.size() >= 2) {
                layout_dianping
                        .addView(addDianPingToLayout(goodsDetailBeen.goodsDetail.EVA_GOODS_LIST
                                .get(1)));
            }
        }
    }

    /**
     * 设置商品是否收藏和收藏的提交事件
     */
    private void setGoodsCollectionCheck() {
        //		collection = goodsDetailBeen.commodity.commodity.COLLECT;// 收藏个数
        boolean isCollection = goodsDetailBeen.goodsDetail.getFLAG_FOCUS_GOODS() == 1;// 是否收藏

        //		collection_count.setText("（" + collection + "人）");
        collection_checkbox.setChecked(isCollection);

        collection_checkbox
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                                                 boolean isChecked) {
                        if (AppContext.userId == null) {
                            Toast.makeText(getActivity(), "请先登录",
                                    Toast.LENGTH_SHORT).show();
                            collection_checkbox.setChecked(false);
                            Intent intent = new Intent(getActivity(),
                                    LoginActivity.class);
                            intent.putExtra(LoginActivity.TAG,
                                    LoginActivity.hkdetail_code);
                            getActivity().startActivity(intent);
                            return;
                        }
                        if (isChecked) {
                            collect(AppContext.userId);
                        } else {
                            unCollect(AppContext.userId);
                        }
                    }
                });
    }

    /**
     * 显示促销价格
     */
    private void showPromotionPrice() {
        String price = goodsDetailBeen.goodsDetail.GOODS_PRICE;// 乐虎价格
//		String peferencePrice = goodsDetailBeen.commodity.commodity.REFERENCE_PRICE;// 推荐价格
        SpannableString sp = null;
        String title = null;
        //促销方式 PROMOTION_TYPE 1、折扣 2、直降 3、折后降
        if (goodsDetailBeen.goodsDetail.PROMOTION_TYPE!=null&&isActive){
            switch (Utils.anInt(goodsDetailBeen.goodsDetail.PROMOTION_TYPE)) {
                case 1:
                    sp = new SpannableString(MathUtil.priceForAppWithSign(price));
                    sp.setSpan(new StrikethroughSpan(), 0, price.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    lehu_price.setTextColor(getActivity().getResources().getColor(
                            R.color.grey_text));
                    title = MathUtil
                            .priceForAppWithSign(goodsDetailBeen.goodsDetail.DISCOUNT_PRICE)
                            + " ("
                            + MathUtil
                            .stringKeep1Decimal(goodsDetailBeen.goodsDetail.DISCOUNT)
                            + "折)";
                    lehu_price.setText(sp);
                    lehu_price1.setText(title);

                    cuxiaojia.setVisibility(View.VISIBLE);
                    cankaojia.setVisibility(View.GONE);
                    break;
                case 2:
                    sp = new SpannableString(MathUtil.priceForAppWithSign(price));
                    sp.setSpan(new StrikethroughSpan(), 0, price.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    lehu_price.setTextColor(getActivity().getResources().getColor(
                            R.color.grey_text));
                    title = MathUtil
                            .priceForAppWithSign(goodsDetailBeen.goodsDetail.DISCOUNT_PRICE)
                            + " (直降"
                            + MathUtil
                            .priceForAppWithSign(goodsDetailBeen.goodsDetail.DISCOUNT)
                            + "元)";
                    lehu_price.setText(sp);
                    lehu_price1.setText(title);
                    cuxiaojia.setVisibility(View.VISIBLE);
                    cankaojia.setVisibility(View.GONE);
                    break;
                case 3:
                    sp = new SpannableString(MathUtil.priceForAppWithSign(price));
                    sp.setSpan(new StrikethroughSpan(), 0, price.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    lehu_price.setTextColor(getActivity().getResources().getColor(
                            R.color.grey_text));
                    title = MathUtil
                            .priceForAppWithSign(goodsDetailBeen.goodsDetail.DISCOUNT_PRICE)
                            + " (优惠"
                            + MathUtil
                            .priceForAppWithSign(goodsDetailBeen.goodsDetail.DISCOUNT)
                            + "元)";
                    lehu_price.setText(sp);
                    lehu_price1.setText(title);
                    cuxiaojia.setVisibility(View.VISIBLE);
                    cankaojia.setVisibility(View.GONE);
                    break;
            }
        } else {//无促销或者促销未开始,已结束，显示乐虎价
            lehu_price.setTextColor(getActivity().getResources().getColor(
                    R.color.red));
            lehu_price.setText(MathUtil.priceForAppWithSign(price));
            cuxiaojia.setVisibility(View.GONE);
        }
    }

    /**
     * 显示活动倒计时
     */
    private void fillActivityTimer() {
        if (goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY() == 1
                || goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY() == 2
                || goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY() == 3
                || goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY() == 5) {
            if (!Utils.isActivityEnd(goodsDetailBeen.goodsDetail.START_TIME,
                    goodsDetailBeen.goodsDetail.END_TIME,goodsDetailBeen.curTime)) {
                countTimeLayout.setVisibility(View.VISIBLE);
                switch (goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY()) {
                    case 1:
                        titleString = "距离预约";
                        break;
                    case 2:
                        titleString = "距离团购";
                        break;
                    case 3:
                        titleString = "距离秒杀";
                        break;
                    case 5:
                        titleString = "距离闪购";
                        break;
                }
                TextTask(goodsDetailBeen.goodsDetail.START_TIME,
                        goodsDetailBeen.goodsDetail.END_TIME,
                        goodsDetailBeen.curTime);
            }
        } else {
            countTimeLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示活动商品的按钮
     */
    private void showActivityButton() {
        if (goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY() == 1&&isActive) {
            activity.btn_checkout.setVisibility(View.GONE);
            activity.btn_add.setVisibility(View.GONE);
            activity.btn_order.setVisibility(View.VISIBLE);
            if (goodsDetailBeen.goodsDetail.FLAG_BESPEAK
                    .equals("0")) {
                activity.btn_order.setText("马上预约");
                activity.btn_order
                        .setBackgroundResource(R.drawable.common_btn_red_selector);
                activity.btn_order.setEnabled(true);
            } else {
                //>0为已预约
                activity.btn_order.setText("已预约");
                activity.btn_order
                        .setBackgroundResource(R.drawable.common_btn_gray2_selector);
                activity.btn_order.setEnabled(false);
            }
        }else if (goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY() == 2&&isActive){
            activity.btn_checkout.setVisibility(View.GONE);
            activity.btn_add.setVisibility(View.GONE);
            activity.btn_order.setVisibility(View.VISIBLE);
            activity.btn_order.setText("马上团");
        } else if (goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY() == 3&&isActive) {
            activity.btn_checkout.setVisibility(View.GONE);
            activity.btn_add.setVisibility(View.GONE);
            activity.btn_order.setVisibility(View.VISIBLE);
            activity.btn_order.setText("马上秒杀");
            if (goodsDetailBeen.goodsDetail.getFLAG_BUY() == 0) {//不可以继续购买
                activity.btn_order
                        .setBackgroundResource(R.drawable.common_btn_gray2_selector);
                activity.btn_order.setEnabled(false);
            } else {
                activity.btn_order
                        .setBackgroundResource(R.drawable.common_btn_red_selector);
                activity.btn_order.setEnabled(true);
            }
        }else if (goodsDetailBeen.goodsDetail.getFLAG_ACTIVITY() == 5&&isActive) {
            activity.btn_checkout.setVisibility(View.GONE);
            activity.btn_add.setVisibility(View.GONE);
            activity.btn_order.setVisibility(View.VISIBLE);
            activity.btn_order.setText("马上抢");
        }else{
            //无商品活动,或者单品促销活动,都显示立即结算和加入购物车
            activity.btn_checkout.setVisibility(View.VISIBLE);
            activity.btn_add.setVisibility(View.VISIBLE);
            activity.btn_order.setVisibility(View.GONE);
        }
    }

    /**
     * 显示库存和定位,服务
     */
    private void showStockAndLocationAndServe() {
        mTv_tip.setText(goodsDetailBeen.goodsDetail.SERVE);//服务
        /*定位*/
        lng = AppContext.getInstance().lng;
        lat = AppContext.getInstance().lat;
        cityName = AppContext.getInstance().cityName;

        BDLocation mBd = AppContext.getInstance().getBDLocation();
        if (null != mBd) {
            provinceName = mBd.getProvince();
            cityName = mBd.getCity();
            areaName = mBd.getDistrict();
        }
        if (lng == 0.0 || lat == 0.0) {
            StartLoacation();
        } else {
            SQLOpearteImpl temp = new SQLOpearteImpl(getActivity());
            provinceId = temp.checkIdByName(provinceName);
            cityId = temp.checkIdByName(cityName);
            areaId = temp.checkIdByName(areaName);
            temp.CloseDB();
            mTv_location.setText(provinceName + ">" + cityName + ">" + areaName);
        }
        showGiftAndPromotion();
        //库存
        int kc = goodsDetailBeen.goodsDetail.getGOODS_STOCK();
        if (kc > 0) {
            mTv_kc.setText("有货");
            mTv_kc.setTextColor(getResources().getColor(R.color.red));
        } else {
            mTv_kc.setText("无货");
            mTv_kc.setTextColor(getResources().getColor(R.color.light_gray));
        }
        //已选数量
        if (!isRefresh) {
            //规格参数
            StringBuffer params = new StringBuffer();
            paramsWithOutNum = new StringBuffer();
            ArrayList<DEFAULT_NORM> currentParams = goodsDetailBeen.goodsDetail.DEFAULT_NORMS;
            for (int i = 0; i < currentParams.size(); i++) {
                DEFAULT_NORM param = currentParams.get(i);
                paramsWithOutNum.append(param.NORMS_NAME).append("：").append(param.NORMS_VALUE_NAME).append("，");
                if(!StringUtils.isBlank(param.NORMS_NAME)&&!StringUtils.isBlank(param.NORMS_VALUE_NAME)) {
                    params.append(param.NORMS_NAME).append("：").append(param.NORMS_VALUE_NAME).append("，");
                }
            }
            if (goodsDetailBeen.goodsDetail.getGOODS_STOCK() < numAfterParams) {
                numAfterParams = goodsDetailBeen.goodsDetail.getGOODS_STOCK();
            }
            params.append(numAfterParams + "件");
            mTv_shuxing.setText(params);
        }

    }

    /**
     * 填充人气组合,推荐搭配,优惠套餐等组合购买信息
     */
    private void showRecommedGroupProducts() {
        //是否有人气组合
        if (goodsDetailBeen.goodsDetail.getCOUNT_POPULAR()<=0) {
            mTv_rqzh.setVisibility(View.GONE);
            tv_rqzh_line.setVisibility(View.GONE);

        } else {
            mTv_rqzh.setVisibility(View.VISIBLE);
            tv_rqzh_line.setVisibility(View.VISIBLE);
        }
        //是否有推荐搭配
        if (goodsDetailBeen.goodsDetail.getCOUNT_RECOMMEND()<=0) {
            mTv_tjdp.setVisibility(View.GONE);
            tv_tjdp_line.setVisibility(View.GONE);
        } else {
            mTv_tjdp.setVisibility(View.VISIBLE);
            tv_tjdp_line.setVisibility(View.VISIBLE);
        }
        //是否有优惠套餐
        if (1 != goodsDetailBeen.goodsDetail.getFLAG_GROUP()) {
            mTv_yhtc.setVisibility(View.GONE);
            tv_tjdp_line.setVisibility(View.GONE);
        } else {
            mTv_yhtc.setVisibility(View.VISIBLE);
        }
        //是否隐藏整个优惠活动布局
        if (mTv_rqzh.getVisibility()==View.GONE&&mTv_tjdp.getVisibility()==View.GONE&&mTv_yhtc.getVisibility()==View.GONE){
            layout_huodong.setVisibility(View.GONE);
        }
    }

    /**
     * 显示店铺的评分和是否关注
     */
    private void showStoreRating() {
        mTv_dpname.setText("  " + goodsDetailBeen.goodsDetail.STORE_NAME);
        float score = 0;
        String pingfen = "";
        if (0 >= goodsDetailBeen.goodsDetail.STORE_SCORE) {
            score = 5;
        } else {
            score = goodsDetailBeen.goodsDetail.STORE_SCORE;
            pingfen = "（" + goodsDetailBeen.goodsDetail.STORE_SCORE + "分）";
        }
        mRb_pf.setRating(score);
        mTv_pf.setText(pingfen);

        if (!"".equals(goodsDetailBeen.goodsDetail.STORE_ID) && Integer.parseInt(goodsDetailBeen.goodsDetail.STORE_ID) > 0) {
            if (1 == goodsDetailBeen.goodsDetail.getFLAG_FOCUS_STORE()) {
                mBtn_scdp.setText("已关注");
            } else
                mBtn_scdp.setText("关注店铺");
            mBtn_scdp.setTag(goodsDetailBeen.goodsDetail.FLAG_FOCUS_STORE);//是否收藏了店铺
        } else {
            layout_dianpu.setVisibility(View.GONE);
        }
    }

    /**
     * 显示实物赠送,满减,满赠和虚拟商品(积分,虎券等)
     */
    private void showGiftAndPromotion() {
        //促销实物商品
        boolean showCX = false;
        if (1 == goodsDetailBeen.goodsDetail.getFLAG_GIFT()) {
            //赠送
            showCX = true;
            goodsGiftAdapter = new GoodsGiftAdapter(getActivity(), goodsDetailBeen.goodsDetail.PROMOTION_GIFT);
            mLv_zs.setAdapter(goodsGiftAdapter);
            mLv_zs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String IS_SHOW_APP = goodsDetailBeen.goodsDetail.PROMOTION_GIFT.get(position).IS_SHOW_APP;
                    PROMOTION pro = goodsDetailBeen.goodsDetail.PROMOTION_GIFT.get(position);
                    refreshProductDetail(IS_SHOW_APP, pro.ID, pro.STORE_ID, pro.GOODS_NO);
                }
            });
        }
        if (1 == goodsDetailBeen.goodsDetail.getFLAG_REDUCE()) {
            //满减
            showCX = true;
            goodsMJAdapter = new GoodsMJAdapter(getActivity(), goodsDetailBeen.goodsDetail.PROMOTION_REDUCE);
            mLv_mj.setAdapter(goodsMJAdapter);
        }
        if (1 == goodsDetailBeen.goodsDetail.getFLAG_DONATE()) {
            //满赠
            showCX = true;
            goodsMZAdapter = new GoodsMZAdapter(getActivity(), goodsDetailBeen.goodsDetail.PROMOTION_DONATE);
            mLv_mz.setAdapter(goodsMZAdapter);
            mLv_mz.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    String IS_SHOW_APP = goodsDetailBeen.goodsDetail.PROMOTION_DONATE.get(groupPosition).goodsList.get(childPosition).IS_SHOW_APP;
                    GOOD pro = goodsDetailBeen.goodsDetail.PROMOTION_DONATE.get(groupPosition).goodsList.get(childPosition);
                    refreshProductDetail(IS_SHOW_APP, pro.GOODS_ID, pro.STORE_ID, pro.GOODS_NO);
                    return false;
                }
            });
        }
        //分割线的显示与隐藏
        if (1 == goodsDetailBeen.goodsDetail.getFLAG_GIFT() && 1 == goodsDetailBeen.goodsDetail.getFLAG_REDUCE()) {
            line_zs.setVisibility(View.VISIBLE);
        } else {
            line_zs.setVisibility(View.GONE);
        }
        if (1 == goodsDetailBeen.goodsDetail.getFLAG_REDUCE() && 1 == goodsDetailBeen.goodsDetail.getFLAG_DONATE()) {
            line_mj.setVisibility(View.VISIBLE);
        } else {
            line_mj.setVisibility(View.GONE);
        }
        if (showCX) {//是否显示赠送
            mLayout_cx.setVisibility(View.VISIBLE);
        } else {
            mLayout_cx.setVisibility(View.GONE);
        }
        //赠送虚拟商品
        boolean zs = false;
        if (goodsDetailBeen.goodsDetail.getIS_TICKET() == 1) {//有赠送乐虎券
            mTv_lehujuan.setText(goodsDetailBeen.goodsDetail.TICKET_NAME);
            zs = true;
        } else {
            mTv_lehujuan.setVisibility(View.GONE);
        }
        if (goodsDetailBeen.goodsDetail.getIS_CASH() == 1) {//有赠送现金
            mTv_money.setText(goodsDetailBeen.goodsDetail.CASH);
            zs = true;
        } else {
            mTv_money.setVisibility(View.GONE);
        }
        if (goodsDetailBeen.goodsDetail.getIS_INTEGRAL() == 1) {//有赠送积分
            mTv_jifen.setText(goodsDetailBeen.goodsDetail.INTEGRAL);
            zs = true;
        } else {
            mTv_jifen.setVisibility(View.GONE);
        }
        if (zs) {//是否显示赠送
            mLayout_zs.setVisibility(View.VISIBLE);
        } else {
            mLayout_zs.setVisibility(View.GONE);
        }
    }

    private void StartLoacation() {
        AppContext.getInstance().initLocationClient();
        if (mLocationClient == null)
            mLocationClient = AppContext.getInstance().mLocationClient;
        if (mLocationClient.isStarted()) {
            return;
        }
        mLocationClient.start();
        AppContext.getInstance().setLocationCallBack(new AppContext.LocationCallBack() {

            @Override
            public void getPoistion(BDLocation location) {
                lng = location.getLongitude();
                lat = location.getLatitude();
                cityName = location.getCity();
//                mLocation_textview.setText("您的位置是：" + location.getAddrStr());
                if (lng != 0.0 && lat != 0.0) {
                    if (null != location) {
                        provinceName = location.getProvince();
                        cityName = location.getCity();
                        areaName = location.getDistrict();
                    }
                    mLocationClient.stop();
                    SQLOpearteImpl temp = new SQLOpearteImpl(getActivity());
                    provinceId = temp.checkIdByName(provinceName);
                    cityId = temp.checkIdByName(cityName);
                    areaId = temp.checkIdByName(areaName);
                    temp.CloseDB();
                    mTv_location.setText(provinceName + ">" + cityName + ">" + areaName);
                }
            }
        });
    }

    public void refreshUi(String ID, String STORE_ID, String GOODS_NO) {
        numAfterParams = 1;
        ((ProductsDetailActivity) getActivity()).isUpdated=true;
        ((ProductsDetailActivity) getActivity()).updataProducts(ID, STORE_ID, GOODS_NO);//刷新商品详情页
    }

    public void refreshProductDetail(String IS_SHOW_APP, String ID, String STORE_ID, String GOODS_NO) {
        if ("1".equals(IS_SHOW_APP)) {//显示在app
            Intent intent = new Intent(getActivity(), ProductsDetailActivity.class);
            intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, ID);
            intent.putExtra(ProductsDetailActivity.STORE_ID, STORE_ID);
            intent.putExtra(ProductsDetailActivity.GOODS_NO, GOODS_NO);
            getActivity().startActivity(intent);
        }
    }

    /**
     * 填充单个评价内容
     * @param item 评价数据
     * @return
     */
    private View addDianPingToLayout(EVA_GOODS item) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dian_ping_item, null);

        RatingBar dianping_ratingBar = (RatingBar) view
                .findViewById(R.id.dianping_ratingBar);
        dianping_ratingBar.setEnabled(false);
        dianping_ratingBar.setMax(5);
        dianping_ratingBar.setProgress(MathUtil.stringToInt(item.EVA_GRADE));

        TextView dianping_username = (TextView) view
                .findViewById(R.id.dianping_username);
        dianping_username.setText(item.USER_NAME);

        TextView dianping_time = (TextView) view
                .findViewById(R.id.dianping_time);
        dianping_time.setText(item.EVA_TIME);

        TextView dianping_pingjia = (TextView) view
                .findViewById(R.id.dianping_pingjia);
        dianping_pingjia.setText(item.CONTENT);

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DianPingShaiDan.class);
                intent.putExtra(DianPingShaiDan.INTENT_KEY_ID, goodsDetailBeen.goodsDetail.ID);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        if (null == goodsDetailBeen) {
            Log.d(TAG, "BasicInformation.onClick:goodsDetailBeen is null");
            return;
        }
        if (view == layout_baseinfo) {
            Intent intent = new Intent(getActivity(), PictureAndWordsDetailActivity.class);
            intent.putExtra(PictureAndWordsDetailActivity.GOODSID, goodsDetailBeen.goodsDetail.ID);
            intent.putExtra(PictureAndWordsDetailActivity.PICTURE_WORDS, goodsDetailBeen.goodsDetail.info_content_map);
            intent.putExtra(PictureAndWordsDetailActivity.PACKINGLIST, goodsDetailBeen.goodsDetail.packinglist_content_map);
            intent.putExtra(PictureAndWordsDetailActivity.AFTERSERVICE, goodsDetailBeen.goodsDetail.afterservice_content_map);
            getActivity().startActivity(intent);
        } else if (view == layout_evaluation) {
            if (goodsDetailBeen.goodsDetail.getREVIEW_NUMBER() <= 0) {
                Toast.makeText(getActivity(), "暂无评价", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), DianPingShaiDan.class);
                intent.putExtra(DianPingShaiDan.INTENT_KEY_ID, goodsDetailBeen.goodsDetail.ID);
                startActivity(intent);
            }
        } else if (view == layout_shaidan) {
            // 商品晒单
            if (goodsDetailBeen.goodsDetail.getREVIEW_NUMBER() <= 0) {
                Toast.makeText(getActivity(), "暂无晒单", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), DianPingShaiDan.class);
                intent.putExtra(DianPingShaiDan.INTENT_KEY_ID, goodsDetailBeen.goodsDetail.ID);
                intent.putExtra(DianPingShaiDan.INTENT_KEY_STATE, 1);
                startActivity(intent);
            }
        } else if (view == layout_kefu) {
            // 在线客服
            RongCloud.getInstance(getActivity()).startCustomerServiceChat();
        } else if (view == mTv_jb) {//举报
            Log.d(TAG, "mTv_jb button has been clicked");
            if (!appIsLogin(true))return;
            Intent intent = new Intent(this.getActivity(), ReportActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("goods_id", goodsDetailBeen.goodsDetail.ID);
            bundle.putString("store_id", goodsDetailBeen.goodsDetail.STORE_ID);
            bundle.putString("store_logo", goodsDetailBeen.goodsDetail.STORE_LOGO);
            bundle.putString("store_name", goodsDetailBeen.goodsDetail.STORE_NAME);
            bundle.putString("goods_price", goodsDetailBeen.goodsDetail.GOODS_PRICE);
            bundle.putString("goods_name", goodsDetailBeen.goodsDetail.GOODS_NAME);
            bundle.putString("goods_img", goodsDetailBeen.goodsDetail.GOODS_IMG);

            intent.putExtras(bundle);
            startActivity(intent);
            
        } else if (view == layout_ps) {//选择位置
            PositionSelect positionSelect = new PositionSelect(getActivity(), relyview, provinceId, cityId, areaId);
            positionSelect.setOnSelectResultListener(new PositionSelect.SelectResultListener() {
                @Override
                public void selectResult(int pId, int cId, int aId) {
                    provinceId = pId;
                    cityId = cId;
                    areaId = aId;
                    SQLOpearteImpl temp = new SQLOpearteImpl(getActivity());
                    ArrayList<SQLOpearteImpl.Area> provices = temp.checkAllProvince();
                    for (SQLOpearteImpl.Area area : provices) {
                        if (area.rowId == provinceId) {
                            provinceName = area.areaName;
                        }
                    }
                    ArrayList<SQLOpearteImpl.Area> cities = temp.checkAllCityById(provinceId);
                    for (SQLOpearteImpl.Area area : cities) {
                        if (area.rowId == cityId) {
                            cityName = area.areaName;
                        }
                    }
                    if (aId == -1) {//如果没有3级
                        mTv_location.setText(provinceName + ">" + cityName);
                        return;
                    }
                    ArrayList<SQLOpearteImpl.Area> areas = temp.checkAllDistriceById(cityId);
                    for (SQLOpearteImpl.Area area : areas) {
                        if (area.rowId == areaId) {
                            areaName = area.areaName;
                        }
                    }
                    mTv_location.setText(provinceName + ">" + cityName + ">" + areaName);

                }
            });
        } else if (view == layout_select_params) {//选择规格参数
            specificationsSelect = new SpecificationsSelect(getActivity(), relyview, goodsDetailBeen,isActive);
            specificationsSelect.setNum(numAfterParams);
            specificationsSelect.init();
            specificationsSelect.setOnSelectResultListener(new SpecificationsSelect.SelectResultListener() {
                @Override
                public void selectResult(String GOODS_NO, int selectGoodNum) {
                    if ("".equals(GOODS_NO)) {
                        if (numAfterParams != selectGoodNum) {
                            numAfterParams = selectGoodNum;
                            mTv_shuxing.setText(paramsWithOutNum + "" + numAfterParams + "件");
                        }
                    } else {
                        refreshUi(goodsDetailBeen.goodsDetail.ID, goodsDetailBeen.goodsDetail.STORE_ID, GOODS_NO);
                    }
                }

                @Override
                public void addToShoppingCar(int selectGoodNum) {
                    ((ProductsDetailActivity) getActivity()).addShoppingCar(selectGoodNum,"-1","-1");
                }

                @Override
                public void buyNow(int selectGoodNum) {
                    ((ProductsDetailActivity) getActivity()).buyNow(selectGoodNum);
                }
            });
        } else if (view == mTv_yhtc) {//优惠套餐
            getDiscountPackageData();//获取优惠套餐的数据
        } else if (view == mTv_dpname || view == mBtn_jrdp) {//店铺
            Intent intent = new Intent(getActivity(), StoreHomeActivity.class);
            intent.putExtra(StoreHomeActivity.STORE_ID, Integer.parseInt(goodsDetailBeen.goodsDetail.STORE_ID));
            getActivity().startActivity(intent);
        } else if (view == tv_to_webview) {//查看图文详情
            if (null != listener) {
                listener.changeToWebView();
            }
        } else if (view == mBtn_scdp) {//关注店铺
            String userId = AppContext.userId;
            if (userId == null) {
                Toast.makeText(getActivity(), "您还没有登陆", Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setClass(getActivity(), LoginActivity.class);
                getActivity().startActivity(i);
                return;
            }
            FouceOn();
        }else if (view==mTv_tjdp){
            //推荐搭配
            TjdpPopSelect mTjdpPopSelect=new TjdpPopSelect(mContext, relyview, goodsDetailBeen.goodsDetail.ID,goodsDetailBeen.goodsDetail.GOODS_NO,
                    goodsDetailBeen.goodsDetail.STORE_ID, goodsDetailBeen.goodsDetail.GOODS_NAME, goodsDetailBeen.goodsDetail.GOODS_PRICE
                    , goodsDetailBeen.goodsDetail.GOODS_IMG,numAfterParams+"", new TjdpPopSelect.SelectResultListener() {

                @Override
                public void buyNow(HashMap<String, String> result) {

                }

                @Override
                public void addShoppingCar(int result) {
                    ((ProductsDetailActivity) getActivity()).setShoppingCarNum(result);
                }

                @Override
                public void selectResult(List<String> result) {

                }
            });
        } else if (view==mTv_rqzh){
            //人气组合
            RqzhPopSelect mRqzhPopSelect=new RqzhPopSelect(mContext,relyview,goodsDetailBeen.goodsDetail.ID,null);
        }
    }

    /**
     * 提交店铺关注和取消店铺关注
     */
    private void FouceOn() {
        CustomResponseHandler handler = new CustomResponseHandler(
                getActivity(), true) {
            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(content, BaseBean.class);
                if (bean.type > 0) {
                    int tag = Integer.parseInt(mBtn_scdp.getTag().toString().trim());
                    if (tag == 0) {
                        mBtn_scdp.setTag(1);
                        mBtn_scdp.setText("已关注");
                        Toast.makeText(getActivity(), "关注成功", Toast.LENGTH_SHORT).show();
                    } else {
                        mBtn_scdp.setTag(0);
                        mBtn_scdp.setText("关注店铺");
                        Toast.makeText(getActivity(), "取消关注成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), bean.msg, Toast.LENGTH_SHORT).show();
                }
            }
        };
        String userId = AppContext.userId;
        int tag = Integer.parseInt(mBtn_scdp.getTag().toString().trim());
        int storeId = Integer.parseInt(goodsDetailBeen.goodsDetail.STORE_ID);
        if (tag == 0) {
            RequstClient.addFocus(handler, userId, storeId, null, 0, 2);
        } else {
            RequstClient.cancelFocus(handler, userId, storeId, null, 0, 2);
        }
    }

    /**
     * 获得优惠套餐的pop
     */
    private void getDiscountPackageData() {
        RequstClient.getDiscountPackageData(goodsDetailBeen.goodsDetail.GOODS_NO, goodsDetailBeen.goodsDetail.STORE_ID, new CustomResponseHandler(getActivity()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject json = new JSONObject(content);
                    String type = json.optString("type");
                    if ("1".equals(type)) {//成功
                        DiscountPackageBean discountPackageBean = DiscountPackageBean.explainJson(content, getActivity());
                        if (null != discountPackageBean) {
                            discountPackage = new DiscountPackage(getActivity(), relyview, discountPackageBean,
                            new DiscountPackage.SelectResultListener(){

                                @Override
                                public void selectResult(String Promotion_ID) {
                                    ((ProductsDetailActivity) getActivity()).addShoppingCar(numAfterParams,Promotion_ID,"8");
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        gallery_count.setText((position + 1) + "/" + listGallerys.size() + "张");
        for (int i = 0; i < indicatorList.size(); i++) {
            if (i == position) {
                indicatorList.get(i).setImageResource(
                        R.drawable.page_indicator_focused);
            } else {
                indicatorList.get(i)
                        .setImageResource(R.drawable.page_indicator);
            }
        }
    }

    /**
     * 收藏商品
     */
    private void collect(String userId) {
        CustomResponseHandler handler = new CustomResponseHandler(
                getActivity(), true) {
            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(content, BaseBean.class);
                if (bean.type > 0) {
                    collection++;
                    collection_count.setText("（" + collection + "人）");
                    Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(), "收藏失败", Toast.LENGTH_SHORT)
                            .show();
                    collection_checkbox.setChecked(false);
                }
            }
        };
        RequstClient.appCollect(userId,
                goodsDetailBeen.goodsDetail.ID, goodsDetailBeen.goodsDetail.GOODS_NO, goodsDetailBeen.goodsDetail.STORE_ID, 1, handler);
    }

    /**
     * 取消收藏商品
     */
    private void unCollect(String userId) {
        CustomResponseHandler handler = new CustomResponseHandler(
                getActivity(), true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(content, BaseBean.class);
                if (bean.type > 0) {
                    collection--;
                    collection_count.setText("（" + collection + "人）");
                    Toast.makeText(getActivity(), "取消收藏", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getActivity(), "取消收藏失败", Toast.LENGTH_SHORT)
                            .show();
                    collection_checkbox.setChecked(true);
                }
            }

        };
        RequstClient.appCancelCollect(userId,
                goodsDetailBeen.goodsDetail.ID, goodsDetailBeen.goodsDetail.GOODS_NO, goodsDetailBeen.goodsDetail.STORE_ID, 1, handler);
    }

    private long nowSecond = 0;
    private String titleString;

    /**
     * 开启预约,团购,秒杀,闪购倒计时
     * @param starttime 开始时间
     * @param endtime 结束时间
     * @param curtime 当前服务器时间
     */
    protected void TextTask(final String starttime, final String endtime,
                            final String curtime) {
        Date start = StringUtils.toDate(starttime);
        Date end = StringUtils.toDate(endtime);
        Date nowTime = StringUtils.toDate(curtime);

        long startSecond = start.getTime();
        long endSecond = end.getTime();
        if (nowSecond == 0)
            nowSecond = nowTime.getTime();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (nowSecond < startSecond) {
            mCountDownTimer = new CountDownTimer(startSecond - nowSecond, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    int day = (int) (millisUntilFinished / 86400000);
                    int hour = (int) ((millisUntilFinished % 86400000) / 3600000);
                    int minute = (int) ((millisUntilFinished % 3600000) / 60000);
                    int second = (int) ((millisUntilFinished % 60000) / 1000);
                    String temp;
                    if (day != 0) {
                        temp = titleString + "开始：<font color=\"red\">" + day
                                + "</font>天<font color=\"red\">" + hour
                                + "</font>时<font color=\"red\">" + minute
                                + "</font>分<font color=\"red\">" + second
                                + "</font>秒";
                    } else {
                        temp = titleString + "开始：<font color=\"red\">" + hour
                                + "</font>时<font color=\"red\">" + minute
                                + "</font>分<font color=\"red\">" + second
                                + "</font>秒";
                    }
                    countTime.setText(Html.fromHtml(temp));
                    nowSecond += 1000;
                }

                @Override
                public void onFinish() {
                    nowSecond += 1000;
                    TextTask(starttime, endtime, curtime);
                }
            };
            mCountDownTimer.start();
        } else if (nowSecond < endSecond) {
            if (!isActive) //之前未开始,现在开始就刷新
                timeToUpdateActivity();
            mCountDownTimer = new CountDownTimer(endSecond - nowSecond, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    int day = (int) (millisUntilFinished / 86400000);
                    int hour = (int) ((millisUntilFinished % 86400000) / 3600000);
                    int minute = (int) ((millisUntilFinished % 3600000) / 60000);
                    int second = (int) ((millisUntilFinished % 60000) / 1000);
                    String temp;
                    if (day != 0) {
                        temp = titleString + "结束：<font color=\"red\">" + day
                                + "</font>天<font color=\"red\">" + hour
                                + "</font>时<font color=\"red\">" + minute
                                + "</font>分<font color=\"red\">" + second
                                + "</font>秒";
                    } else {
                        temp = titleString + "结束：<font color=\"red\">" + hour
                                + "</font>时<font color=\"red\">" + minute
                                + "</font>分<font color=\"red\">" + second
                                + "</font>秒";
                    }
                    countTime.setText(Html.fromHtml(temp));
                    nowSecond += 1000;
                }

                @Override
                public void onFinish() {
                    nowSecond += 1000;
                    TextTask(starttime, endtime, curtime);
                }
            };
            mCountDownTimer.start();
        } else if (nowSecond >= endSecond) {
            if (isActive) //之前已开始,现在结束就刷新
                timeToUpdateActivity();
            countTime.setText("活动结束");
        }
    }

    /**
     * 如果该商品活动开始或者结束,刷新促销价和底部购买按钮
     */
    private void timeToUpdateActivity() {
        isActive=!isActive;
        showPromotionPrice();
        showActivityButton();
        if (specificationsSelect!=null){
            specificationsSelect.update(isActive);
        }
    }
    public int getCollection_checkbox() {
        return collection_checkbox.isChecked() ? 1 : 0;
    }

    private OnFragmentTouchListener listener;

    public void setOnFragmentTouchListener(OnFragmentTouchListener listener) {
        this.listener = listener;
    }

    public interface OnFragmentTouchListener {
        void changeToWebView();
    }

    /**
     * 关闭推荐搭配的pop
     */
    public void dismissAllDiscountPop(){
        if (discountPackage!=null){
            discountPackage.dismiss();
        }
    }

    /**
     * 分步加载猜你喜欢的数据，加快商品详情的显示速度
     * @param goodsId 商品id
     * @param thirdCategoryId 商品三级分类id
     */
    private void mayBeLike(String goodsId,String thirdCategoryId){
        RequstClient.mayBeLike(goodsId,thirdCategoryId,new CustomResponseHandler(getActivity(),false){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                if (LogUtil.isDebug) Logger.json(content);
                MayBeLikeBean mBeLike=new Gson().fromJson(content,MayBeLikeBean.class);
                if (mBeLike.type!=1){
                    LogUtil.w("商品详情猜你喜欢",mBeLike.msg);
                }else{
                    if (mBeLike.goodsList!=null){
                        fillMayLike(mBeLike.goodsList);
                    }
                }
            }
        });
    }
}
