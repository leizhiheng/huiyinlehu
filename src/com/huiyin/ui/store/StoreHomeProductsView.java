package com.huiyin.ui.store;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;


public class StoreHomeProductsView extends LinearLayout {

    private Context mContext;
    private ArrayList<StoreListItem> mList;
    private OnClickListener mClickListener;
    private int type;//0:产品推荐 1:新品发布
    private int storeId;

    // Content View Elements

    private TextView title;
    private TextView more;
    private LinearLayout store_home_item_layout1;
    private ImageView store_home_item_iv1;
    private TextView store_home_item_name1;
    private TextView store_home_item_jiage1;
    private LinearLayout store_home_item_layout2;
    private ImageView store_home_item_iv2;
    private TextView store_home_item_name2;
    private TextView store_home_item_jiage2;
    private LinearLayout store_home_item_layout3;
    private ImageView store_home_item_iv3;
    private TextView store_home_item_name3;
    private TextView store_home_item_jiage3;
    private LinearLayout store_home_item_layout4;
    private ImageView store_home_item_iv4;
    private TextView store_home_item_name4;
    private TextView store_home_item_jiage4;

    // End Of Content View Elements

    public StoreHomeProductsView(Context context) {
        super(context);
        mContext = context;
        init();
        SetListener();
    }

    public StoreHomeProductsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
        SetListener();
    }

    public StoreHomeProductsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
        SetListener();
    }

    public StoreHomeProductsView(Context context, ArrayList<StoreListItem> mList,int type) {
        super(context);
        mContext = context;
        this.type = type;
        this.mList = mList;
        init();
        SetListener();
    }

    public void setData(ArrayList<StoreListItem> mList,int type,int storeId) {
        this.type = type;
        this.mList = mList;
        this.storeId = storeId;
        InitView();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.store_home_item, this, true);

        title = (TextView) findViewById(R.id.title);
        more = (TextView) findViewById(R.id.more);
        store_home_item_layout1 = (LinearLayout) findViewById(R.id.store_home_item_layout1);
        store_home_item_iv1 = (ImageView) findViewById(R.id.store_home_item_iv1);
        store_home_item_name1 = (TextView) findViewById(R.id.store_home_item_name1);
        store_home_item_jiage1 = (TextView) findViewById(R.id.store_home_item_jiage1);
        store_home_item_layout2 = (LinearLayout) findViewById(R.id.store_home_item_layout2);
        store_home_item_iv2 = (ImageView) findViewById(R.id.store_home_item_iv2);
        store_home_item_name2 = (TextView) findViewById(R.id.store_home_item_name2);
        store_home_item_jiage2 = (TextView) findViewById(R.id.store_home_item_jiage2);
        store_home_item_layout3 = (LinearLayout) findViewById(R.id.store_home_item_layout3);
        store_home_item_iv3 = (ImageView) findViewById(R.id.store_home_item_iv3);
        store_home_item_name3 = (TextView) findViewById(R.id.store_home_item_name3);
        store_home_item_jiage3 = (TextView) findViewById(R.id.store_home_item_jiage3);
        store_home_item_layout4 = (LinearLayout) findViewById(R.id.store_home_item_layout4);
        store_home_item_iv4 = (ImageView) findViewById(R.id.store_home_item_iv4);
        store_home_item_name4 = (TextView) findViewById(R.id.store_home_item_name4);
        store_home_item_jiage4 = (TextView) findViewById(R.id.store_home_item_jiage4);

    }

    private void SetListener() {
        more.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent;
                switch (type) {
                    case 0 :
                        intent = new Intent(mContext,StoreGoodsListActivity.class);
                        intent.putExtra("storeId", storeId);
                        intent.putExtra("mark", 5);
                        mContext.startActivity(intent);
                        break;
                    case 1 :
                        intent = new Intent(mContext,StoreNewComeActivity.class);
                        intent.putExtra("storeId", storeId);
                        mContext.startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        mClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (Integer)v.getTag();
                int ID = mList.get(position).getID();
                long goodsno = mList.get(position).getGOODSNO();
                int storeid = mList.get(position).getSTOREID();

                Intent intent = new Intent(mContext, ProductsDetailActivity.class);
                intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, String.valueOf(ID));
                intent.putExtra(ProductsDetailActivity.GOODS_NO,String.valueOf(goodsno));
                intent.putExtra(ProductsDetailActivity.STORE_ID,String.valueOf(storeid));
                mContext.startActivity(intent);
            }
        };
    }

    private void InitView() {
        if(mList==null||mList.size()==0) {
            return;
        }
        title.setText(type==1?"新品发布":"产品推荐");
        if(mList.size()>=4) {
            store_home_item_layout1.setTag(0);
            store_home_item_layout1.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(0).getGOODSIMG(), store_home_item_iv1);
            store_home_item_name1.setText(mList.get(0).getGOODSNAME());
            store_home_item_jiage1.setText(MathUtil.priceForAppWithSign(mList.get(0).getGOODSPRICE()));

            store_home_item_layout2.setTag(1);
            store_home_item_layout2.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(1).getGOODSIMG(), store_home_item_iv2);
            store_home_item_name2.setText(mList.get(1).getGOODSNAME());
            store_home_item_jiage2.setText(MathUtil.priceForAppWithSign(mList.get(1).getGOODSPRICE()));

            store_home_item_layout3.setTag(2);
            store_home_item_layout3.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(2).getGOODSIMG(), store_home_item_iv3);
            store_home_item_name3.setText(mList.get(2).getGOODSNAME());
            store_home_item_jiage3.setText(MathUtil.priceForAppWithSign(mList.get(2).getGOODSPRICE()));

            store_home_item_layout4.setTag(3);
            store_home_item_layout4.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(3).getGOODSIMG(),store_home_item_iv4);
            store_home_item_name4.setText(mList.get(3).getGOODSNAME());
            store_home_item_jiage4.setText(MathUtil.priceForAppWithSign(mList.get(3).getGOODSPRICE()));
            return;
        }

        if(mList.size()>=3) {
            store_home_item_layout1.setTag(0);
            store_home_item_layout1.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(0).getGOODSIMG(), store_home_item_iv1);
            store_home_item_name1.setText(mList.get(0).getGOODSNAME());
            store_home_item_jiage1.setText(MathUtil.priceForAppWithSign(mList.get(0).getGOODSPRICE()));

            store_home_item_layout2.setTag(1);
            store_home_item_layout2.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(1).getGOODSIMG(), store_home_item_iv2);
            store_home_item_name2.setText(mList.get(1).getGOODSNAME());
            store_home_item_jiage2.setText(MathUtil.priceForAppWithSign(mList.get(1).getGOODSPRICE()));

            store_home_item_layout3.setTag(2);
            store_home_item_layout3.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(2).getGOODSIMG(), store_home_item_iv3);
            store_home_item_name3.setText(mList.get(2).getGOODSNAME());
            store_home_item_jiage3.setText(MathUtil.priceForAppWithSign(mList.get(2).getGOODSPRICE()));

            store_home_item_layout4.setVisibility(View.INVISIBLE);
            return;
        }

        if (mList.size() >= 2) {
            store_home_item_layout1.setTag(0);
            store_home_item_layout1.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(0).getGOODSIMG(), store_home_item_iv1);
            store_home_item_name1.setText(mList.get(0).getGOODSNAME());
            store_home_item_jiage1.setText(MathUtil.priceForAppWithSign(mList.get(0).getGOODSPRICE()));

            store_home_item_layout2.setTag(1);
            store_home_item_layout2.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(1).getGOODSIMG(), store_home_item_iv2);
            store_home_item_name2.setText(mList.get(1).getGOODSNAME());
            store_home_item_jiage2.setText(MathUtil.priceForAppWithSign(mList.get(1).getGOODSPRICE()));

            store_home_item_layout3.setVisibility(View.GONE);

            store_home_item_layout4.setVisibility(View.GONE);
            return;
        }

        if(mList.size()>=1) {
            store_home_item_layout1.setTag(0);

            store_home_item_layout1.setOnClickListener(mClickListener);
            ImageManager.LoadWithServer(mList.get(0).getGOODSIMG(), store_home_item_iv1);
            store_home_item_name1.setText(mList.get(0).getGOODSNAME());
            store_home_item_jiage1.setText(MathUtil.priceForAppWithSign(mList.get(0).getGOODSPRICE()));

            store_home_item_layout2.setVisibility(View.INVISIBLE);

            store_home_item_layout3.setVisibility(View.GONE);

            store_home_item_layout4.setVisibility(View.GONE);
            return;
        }
    }
}
