package com.huiyin.ui.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BaseBean;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ShareUtils;


public class StoreInfoActivity extends Activity {

    // Content View Elements

    private RelativeLayout layout_ab;
    private TextView ab_back;
    private TextView ab_title;

    private ImageView store_home_head_image;
    private LinearLayout linearLayout;
    private TextView store_home_title;
    private TextView store_home_flower_people;
    private Button store_home_flower;
    private TextView store_home_all_prodect;
    private TextView store_home_new_prodect;
    private TextView store_home_sale;
    private LinearLayout store_home_share;
    private TextView zonghepinfen;
    private View store_info_background;
    private View store_info_top;
    private TextView store_info_scroe;
    private TextView store_info_describe_scroe;
    private TextView store_info_service_scroe;
    private TextView store_info_sent_scroe;
    private TextView zaixiankefu;
    private TextView store_info_qq;
    private TextView store_info_ww;
    private RelativeLayout store_info_ercode;
    private TextView erweima;
    private LinearLayout store_info_company_name_linearlayout;
    private LinearLayout store_info_brand_introduce_linearlayout;
    private TextView store_info_company_name;
    private TextView store_info_company_location;
    private TextView store_info_company_time;
    private TextView store_info_brand_introduce;
    // End Of Content View Elements

    private Context mContext;
    private int storeId;
    private StoreDetailBean data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_info_layout);
        mContext = this;
        storeId = getIntent().getIntExtra("storeId", -1);
        findView();
        setListener();
        InitData();
    }

    private void findView() {
        layout_ab = (RelativeLayout) findViewById(R.id.layout_ab);
        ab_back = (TextView) findViewById(R.id.ab_back);
        ab_title = (TextView) findViewById(R.id.ab_title);
        ab_title.setText("店铺详情");

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
        zonghepinfen = (TextView) findViewById(R.id.zonghepinfen);
        store_info_background = (View) findViewById(R.id.store_info_background);
        store_info_top = (View) findViewById(R.id.store_info_top);
        store_info_scroe = (TextView) findViewById(R.id.store_info_scroe);
        store_info_describe_scroe = (TextView) findViewById(R.id.store_info_describe_scroe);
        store_info_service_scroe = (TextView) findViewById(R.id.store_info_service_scroe);
        store_info_sent_scroe = (TextView) findViewById(R.id.store_info_sent_scroe);
        zaixiankefu = (TextView) findViewById(R.id.zaixiankefu);
        store_info_qq = (TextView) findViewById(R.id.store_info_qq);
        store_info_ww = (TextView) findViewById(R.id.store_info_ww);
        store_info_ercode = (RelativeLayout) findViewById(R.id.store_info_ercode);
        erweima = (TextView) findViewById(R.id.erweima);
        store_info_company_name_linearlayout = (LinearLayout) findViewById(R.id.store_info_company_name_linearlayout);

        store_info_company_name = (TextView) findViewById(R.id.store_info_company_name);
        store_info_company_location = (TextView) findViewById(R.id.store_info_company_location);
        store_info_company_time = (TextView) findViewById(R.id.store_info_company_time);
        store_info_brand_introduce_linearlayout = (LinearLayout) findViewById(R.id.store_info_brand_introduce_linearlayout);
        store_info_brand_introduce = (TextView) findViewById(R.id.store_info_brand_introduce);
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
                ShareUtils.showShare(StoreInfoActivity.this, "汇银商城", data.getStoreInfo().getSTOREURL(), data.getStoreInfo().getSTORELOGO(), "精品好货，尽在乐虎");
            }
        });
        store_info_ercode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null && data.getStoreInfo() != null) {
                    Intent intent = new Intent(mContext, StoreErcodeActivity.class);
                    intent.putExtra("storeName", data.getStoreInfo().getSTORENAME());
                    intent.putExtra("webUrl", data.getStoreInfo().getSTOREURL());
                    startActivity(intent);
                }
            }
        });
        store_info_company_name_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoreLicenseActivity.class);
                intent.putExtra("storeId", storeId);
                startActivity(intent);
            }
        });
        store_info_brand_introduce_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StoreNavigatyActivity.class);
                intent.putExtra("storeId", storeId);
                intent.putExtra("title", data.getStoreInfo().getStoreNavigation().getNAME());
                startActivity(intent);
            }
        });

    }

    private void InitData() {
        CustomResponseHandler handler = new CustomResponseHandler(this, true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                data = StoreDetailBean.explainJson(content, mContext);
                if (data.type > 0) {
                    RefreshStoreDetail(data.getStoreInfo());
                }
            }
        };
        RequstClient.storeDetail(handler, storeId);
    }

    private void RefreshStoreDetail(StoreDetailBean.StoreDe storeInfo) {
        ImageManager.LoadWithServer(storeInfo.getSTORELOGO(), store_home_head_image);
        store_home_title.setText(storeInfo.getSTORENAME());
        store_home_flower_people.setText(storeInfo.getCOUNTFOCUSSTORE() + "人关注");
        store_home_flower.setText(storeInfo.getUSERFOCUSSTORE() == 0 ? "关注" : "已关注");
        store_home_flower.setTag(storeInfo.getUSERFOCUSSTORE());

        store_home_all_prodect.setText(storeInfo.getSTOREGOODSNUM() + "\n全部商品");
        store_home_new_prodect.setText(storeInfo.getNEWGOODSNUM() + "\n新品");
        store_home_sale.setText(storeInfo.getCOUNTPROMOTIONGOODS() + "\n促销");


        store_info_scroe.setText(storeInfo.getStoreScaleAvg() + "分");

        store_info_describe_scroe.setText(MathUtil.keep2decimal(storeInfo.getSTORESPEED()) + "分 " + storeInfo.getDIFFERSPEED());
        store_info_service_scroe.setText(MathUtil.keep2decimal(storeInfo.getSTORESERVER()) + "分 " + storeInfo.getDIFFERSERVER());
        store_info_sent_scroe.setText(MathUtil.keep2decimal(storeInfo.getSTOREPACK()) + "分 " + storeInfo.getDIFFERPACK());

        store_info_company_name.setText(storeInfo.getCOMMPANYNAME());
        store_info_company_location.setText(storeInfo.getCOMMPANYPROVINCENAME() + storeInfo.getCOMMPANYCITYNAME() + storeInfo.getCOMMPANYAREANAME());
        store_info_company_time.setText(storeInfo.getCREATETIME());
        if (storeInfo.getStoreNavigation() != null) {
            store_info_brand_introduce.setText(storeInfo.getStoreNavigation().getNAME());
        } else {
            store_info_brand_introduce_linearlayout.setVisibility(View.GONE);
        }

        TheAnimation(store_info_top, storeInfo.getStoreScaleAvg() / 5);
    }


    public void TheAnimation(View view, float toX) {
        ScaleAnimation animation = new ScaleAnimation(1.0f, toX, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(2000);
        animation.setFillAfter(true);
        view.startAnimation(animation);
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
}
