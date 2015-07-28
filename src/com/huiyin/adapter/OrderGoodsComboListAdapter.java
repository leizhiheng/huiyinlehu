package com.huiyin.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.ShoppingCarGoodsBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;

import java.util.ArrayList;

/**
 * Created by justme on 15/7/14.
 * 商品清单满赠和套餐的适配器
 */
public class OrderGoodsComboListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ShoppingCarGoodsBean> list;

    public OrderGoodsComboListAdapter(Context mContext, ArrayList<ShoppingCarGoodsBean> list) {
        this.mContext = mContext;
        this.list = list;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_goods_list_item_of_item, parent, false);
        }
        ImageView goodsImageview = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_imageview);
        TextView goodsTitle = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_title);
        TextView goodsGuige = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_guige);
        TextView goodsPrice = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_price);
        TextView goods_num = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_num);
        LinearLayout giftBaseLayout = com.huiyin.utils.ViewHolder.get(convertView, R.id.gift_base_layout);

        final ShoppingCarGoodsBean temp = list.get(position);

        ImageManager.LoadWithServer(temp.getGoodsImg(), goodsImageview);
        goodsImageview.setOnClickListener(new OnClickListenerToGoods(temp.getGoodsId(),
                temp.getGoodsNo(), temp.getStoreId(), temp.getIsShowApp()));
        goodsTitle.setText(temp.getGoodsName());
        goodsTitle.setOnClickListener(new OnClickListenerToGoods(temp.getGoodsId(),
                temp.getGoodsNo(), temp.getStoreId(), temp.getIsShowApp()));
        if (TextUtils.isEmpty(temp.getNormsValus())) {
            goodsGuige.setVisibility(View.GONE);
        } else {
            goodsGuige.setVisibility(View.VISIBLE);
            goodsGuige.setText(temp.getNormsValus());
        }
        goodsPrice.setText(MathUtil.priceForAppWithSign(temp.getDiscountPrice()));
        goods_num.setText("x" + (temp.getNum() > 0 ? temp.getNum() : 1));

        return convertView;
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
