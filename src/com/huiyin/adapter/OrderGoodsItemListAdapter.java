package com.huiyin.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
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
import com.huiyin.utils.ResourceUtils;
import com.huiyin.wight.MyListView;

import java.util.ArrayList;

/**
 * Created by Mike on 2015/6/5.
 */
public class OrderGoodsItemListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ShoppingCarGoodsBean> list;

    public OrderGoodsItemListAdapter(Context context, ArrayList<ShoppingCarGoodsBean> list) {
        mContext = context;
        this.list = list;
//        this.list = new ArrayList<ShoppingCarGoodsBean>();
//        for (ShoppingCarGoodsBean temp : list) {
//            if (temp.getPromotionType() == 2 || temp.getPromotionType() == 5) {
//                this.list.addAll(temp.getGoodsList());
//            } else {
//                this.list.add(temp);
//            }
//        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        //促销类型 0、普通商品 1、单品 2、套餐 3、赠品 4、满减 5、满赠  6、秒杀 7、礼品兑换 8、团购  9、天天低价 10、分销 11、闪购 12、推荐配件
        int promotionType = list.get(position).getPromotionType();
        switch (promotionType) {
            case 2://套餐
                return 1;
            case 5://满赠
                return 2;
            default:
                return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ShoppingCarGoodsBean temp = list.get(position);

        if (temp.getPromotionType() == 2) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).
                        inflate(R.layout.order_goods_list_item_of_combo, parent, false);
            }
            MyListView goodsBaseListview = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_base_listview);

            final OrderGoodsComboListAdapter mAdapter = new OrderGoodsComboListAdapter(mContext, temp.getGoodsList());
            goodsBaseListview.setAdapter(mAdapter);

            return convertView;
        }

        //满赠促销
        if (temp.getPromotionType() == 5) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).
                        inflate(R.layout.order_goods_list_item_of_promition, parent, false);
            }

            MyListView goodsBaseListview = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_base_listview);
            LinearLayout giftBaseLayout = com.huiyin.utils.ViewHolder.get(convertView, R.id.gift_base_layout);

            if (temp.getGoodsList() != null && temp.getGoodsList().size() > 0) {
                ShoppingCarGoodsBean promotionBean = temp.getGoodsList().get(0);
                //当等于满赠时候 且第一个商品是赠品 移除第一个商品
                if (promotionBean.isGift()) {
                    temp.getGoodsList().remove(0);
                }
            }

            OrderGoodsComboListAdapter mAdapter = new OrderGoodsComboListAdapter(mContext,
                    temp.getGoodsList());
            goodsBaseListview.setAdapter(mAdapter);

            if (giftBaseLayout.getChildCount() > 0) {
                giftBaseLayout.removeAllViews();
            }

            for (int i = 0; i < temp.getGiftGoodsList().size(); i++) {
                if (temp.getGiftGoodsList().get(i).isGift()) {
                    LinearLayout goodsGiftLayout = (LinearLayout) LayoutInflater.from(mContext).
                            inflate(R.layout.fragment_shopping_car_item_single_goodsgift, null, false);
                    ImageView gift_imageview = (ImageView) goodsGiftLayout.findViewById(R.id.gift_imageview);
                    TextView gift_title = (TextView) goodsGiftLayout.findViewById(R.id.gift_title);
                    TextView gift_price = (TextView) goodsGiftLayout.findViewById(R.id.gift_price);
                    TextView gift_count = (TextView) goodsGiftLayout.findViewById(R.id.gift_count);

                    ImageManager.LoadWithServer(temp.getGiftGoodsList().get(i).getGoodsImg(), gift_imageview);
                    gift_imageview.setOnClickListener(new OnClickListenerToGoods(temp.getGiftGoodsList().get(i).getGoodsId(),
                            temp.getGiftGoodsList().get(i).getGoodsNo(), temp.getGiftGoodsList().get(i).getStoreId(),
                            temp.getGiftGoodsList().get(i).getIsShowApp()));
                    String zeng = ResourceUtils.changeStringColor("#dd434d", "赠品");
                    gift_title.setText(Html.fromHtml(zeng + temp.getGiftGoodsList().get(i).getGoodsName()));
                    gift_price.setText(MathUtil.priceForAppWithSign(temp.getGiftGoodsList().get(i).getGoodsPrice()));
                    gift_count.setText("x" + temp.getGiftGoodsList().get(i).getNum());
                    giftBaseLayout.addView(goodsGiftLayout);
                    break;
                }
            }
            return convertView;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_goods_list_item_of_item, parent, false);
        }
        ImageView goodsImageview = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_imageview);
        TextView goodsTitle = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_title);
        TextView goodsGuige = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_guige);
        TextView goodsPrice = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_price);
        TextView goods_num = com.huiyin.utils.ViewHolder.get(convertView, R.id.goods_num);
        LinearLayout giftBaseLayout = com.huiyin.utils.ViewHolder.get(convertView, R.id.gift_base_layout);

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

        if (giftBaseLayout.getChildCount() > 0) {
            giftBaseLayout.removeAllViews();
        }
        if (temp.getPromotionType() != 5) {
            for (int i = 0; i < temp.getGiftGoodsList().size(); i++) {
                LinearLayout giftLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.fragment_shopping_car_item_single_gift, null, false);
                TextView gift_title = (TextView) giftLayout.findViewById(R.id.gift_title);
                TextView gift_count = (TextView) giftLayout.findViewById(R.id.gift_count);

                gift_title.setText(temp.getGiftGoodsList().get(i).getGoodsName());
                gift_count.setText("x" + temp.getGiftGoodsList().get(i).getNum());
                gift_title.setOnClickListener(new OnClickListenerToGoods(temp.getGiftGoodsList().get(i).getGoodsId(),
                        temp.getGiftGoodsList().get(i).getGoodsNo(), temp.getGiftGoodsList().get(i).getStoreId(),
                        temp.getGiftGoodsList().get(i).getIsShowApp()));
                giftBaseLayout.addView(giftLayout);
            }
        } else {
            for (int i = 0; i < temp.getGiftGoodsList().size(); i++) {
                if (temp.getGiftGoodsList().get(i).isGift()) {
                    LinearLayout goodsGiftLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.fragment_shopping_car_item_single_goodsgift, null, false);
                    ImageView gift_imageview = (ImageView) goodsGiftLayout.findViewById(R.id.gift_imageview);
                    TextView gift_title = (TextView) goodsGiftLayout.findViewById(R.id.gift_title);
                    TextView gift_price = (TextView) goodsGiftLayout.findViewById(R.id.gift_price);
                    TextView gift_count = (TextView) goodsGiftLayout.findViewById(R.id.gift_count);

                    ImageManager.LoadWithServer(temp.getGiftGoodsList().get(i).getGoodsImg(), gift_imageview);
                    gift_imageview.setOnClickListener(new OnClickListenerToGoods(temp.getGiftGoodsList().get(i).getGoodsId(),
                            temp.getGiftGoodsList().get(i).getGoodsNo(), temp.getGiftGoodsList().get(i).getStoreId(),
                            temp.getGiftGoodsList().get(i).getIsShowApp()));
                    String zeng = ResourceUtils.changeStringColor("#dd434d", "赠品");
                    gift_title.setText(Html.fromHtml(zeng + temp.getGiftGoodsList().get(i).getGoodsName()));
                    gift_price.setText(MathUtil.priceForAppWithSign(temp.getGiftGoodsList().get(i).getGoodsPrice()));
                    gift_count.setText("x" + temp.getGiftGoodsList().get(i).getNum());
                    giftBaseLayout.addView(goodsGiftLayout);
                    break;
                }
            }
        }
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
