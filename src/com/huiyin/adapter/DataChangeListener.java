package com.huiyin.adapter;

import com.huiyin.bean.ShoppingCarGoodsBean;
import com.huiyin.bean.ShoppingCarPromotionBean;
import com.huiyin.bean.ShoppingCarPromotionItemBean;

/**
 * Created by justme on 15/7/4.
 */

public interface DataChangeListener {

    void onChange();

    void onNumChange(int position, int beforeNum, int afterNum);

    void onNumChange(int gruopPosition, int position, int beforeNum, int afterNum);

    void onPromotionChange(int position, ShoppingCarPromotionItemBean promotionItemBean);

    void onPromotionChange(int gruopPosition, int position, ShoppingCarPromotionItemBean promotionItemBean);

    void onPromotionChange(int position, ShoppingCarPromotionBean promotionItemBean, ShoppingCarGoodsBean selectGiftBean);

}

