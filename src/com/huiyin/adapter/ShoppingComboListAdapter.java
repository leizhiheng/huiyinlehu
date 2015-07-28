package com.huiyin.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.ShoppingCarGoodsBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ViewHolder;

import java.util.ArrayList;

/**
 * Created by justme on 15/7/3.
 * 购物车套餐的商品适配器
 */
public class ShoppingComboListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ShoppingCarGoodsBean> list;
    private int minStock;//最少库存

    public ShoppingComboListAdapter(Context mContext, ArrayList<ShoppingCarGoodsBean> list) {
        this.mContext = mContext;
        this.list = list;
        minStock = Integer.MAX_VALUE;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_shopping_car_item_combo_item, null);
        }

        ImageView goodsImageview = ViewHolder.get(convertView, R.id.goods_imageview);
        TextView goodsTitle = ViewHolder.get(convertView, R.id.goods_title);
        TextView guige = ViewHolder.get(convertView, R.id.goods_guige);
        TextView goodsPrice = ViewHolder.get(convertView, R.id.goods_price);
        TextView goodsCount = ViewHolder.get(convertView, R.id.goods_count);

        ShoppingCarGoodsBean temp = list.get(position);

        ImageManager.LoadWithServer(temp.getGoodsImg(), goodsImageview);
        goodsImageview.setOnClickListener(new OnClickListenerToGoods(temp.getGoodsId(),
                temp.getGoodsNo(), temp.getStoreId(), temp.getIsShowApp()));
        goodsTitle.setText(temp.getGoodsName());
        if (TextUtils.isEmpty(temp.getNormsValus())) {
            guige.setVisibility(View.GONE);
        } else {
            guige.setVisibility(View.VISIBLE);
            guige.setText(temp.getNormsValus());
        }
        goodsPrice.setText(MathUtil.priceForAppWithSign(temp.getDiscountPrice()));
        goodsCount.setText(String.valueOf(temp.getNum() > 0 ? temp.getNum() : 1) + "件/套x1");

        return convertView;
    }

    public int getMinStock() {
        if (minStock == Integer.MAX_VALUE) {
            if (list != null) {
                for (int i = 0; i < getCount(); i++) {
                    minStock = minStock > list.get(i).getGoodsStock() ? list.get(i).getGoodsStock() : minStock;
                }
            } else {
                return 0;
            }
        }
        return minStock;
    }

    public class OnClickListenerToGoods implements View.OnClickListener {
        private int goodsId;
        private long goodsNo;
        private int storeId;
        private int isShowApp;

        public OnClickListenerToGoods(int goodsId, long goodsNo, int storeId, int isShowApp) {
            this.goodsId = goodsId;
            this.goodsNo = goodsNo;
            this.storeId = storeId;
            this.isShowApp = isShowApp;
        }

        @Override
        public void onClick(View v) {
            if (isShowApp == 1) {
                Intent intent = new Intent(mContext, ProductsDetailActivity.class);
                intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, String.valueOf(goodsId));
                intent.putExtra(ProductsDetailActivity.GOODS_NO, String.valueOf(goodsNo));
                intent.putExtra(ProductsDetailActivity.STORE_ID, String.valueOf(storeId));
                mContext.startActivity(intent);
            }
        }
    }
}