package com.huiyin.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.bean.ShoppingCarGoodsBean;
import com.huiyin.bean.ShoppingCarPromotionItemBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ResourceUtils;
import com.huiyin.utils.ViewHolder;
import com.huiyin.wight.ChoseGiftView;
import com.huiyin.wight.DropDownBoxsView;
import com.huiyin.wight.MyListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by justme on 15/5/26.
 * 购物车适配器
 */
public class ShoppingCarListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ShoppingCarGoodsBean> list;

    private DropDownBoxsView.DropDownBoxsCallback mDropDownBoxsCallback;
    private DataChangeListener listener;
    private Dialog dialog;

    public ShoppingCarListAdapter(final Context mContext) {
        this.mContext = mContext;
        list = new ArrayList<ShoppingCarGoodsBean>();

        mDropDownBoxsCallback = new DropDownBoxsView.DropDownBoxsCallback() {
            @Override
            public void onSelectItem(Object object) {
                if (listener != null) {
                    HashMap<String, Object> temp = (HashMap<String, Object>) object;
                    final ShoppingCarPromotionItemBean bean = (ShoppingCarPromotionItemBean)
                            temp.get("promotionItemBean");
                    int position = (Integer) temp.get("position");
                    /*//如果是满赠的促销类型 要选择促销商品  5表示是满赠商品
                    if (bean.getPromotionType() == 5) {
                        int storeId = list.get(position).getStoreId();
                        double sumPrice = 0;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).isSelect() && storeId == list.get(i).getStoreId()) {
                                sumPrice += list.get(i).getGoodsPrice() * list.get(i).getNum();
                            }
                        }
                        checkTheGift(position, bean,sumPrice);
                    } else {*/
                    listener.onPromotionChange(position, bean);
//                    }
                }
            }
        };
    }

    @Override
    public int getCount() {
        return list.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ShoppingCarGoodsBean temp = list.get(position);

        if (temp.getPromotionType() == 2) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).
                        inflate(R.layout.fragment_shopping_car_item_combo, parent, false);
            }
            ImageView storeLogo = ViewHolder.get(convertView, R.id.store_logo);
            TextView storeName = ViewHolder.get(convertView, R.id.store_name);
            ImageView storeInfoNavigator = ViewHolder.get(convertView, R.id.store_info_navigator);
            CheckBox checkbox = ViewHolder.get(convertView, R.id.checkbox);
            ImageView btnMinus = ViewHolder.get(convertView, R.id.btnMinus);
            TextView count = ViewHolder.get(convertView, R.id.count);
            ImageView btnAdd = ViewHolder.get(convertView, R.id.btnAdd);
            TextView goodsSumPrice = ViewHolder.get(convertView, R.id.goods_sum_price);
            MyListView goodsBaseListview = ViewHolder.get(convertView, R.id.goods_base_listview);

            if (temp.getStoreId() <= 0) {
                storeLogo.setImageResource(R.drawable.lehu);
                storeInfoNavigator.setVisibility(View.GONE);
                storeName.setOnClickListener(null);
                if (TextUtils.isEmpty(temp.getStoreName())) {
                    storeName.setText(R.string.lehu_store_name);
                } else {
                    storeName.setText(temp.getStoreName());
                }
            } else {
                ImageManager.LoadWithServer(temp.getStoreLogo(), storeLogo);
                storeInfoNavigator.setVisibility(View.VISIBLE);
                storeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, StoreHomeActivity.class);
                        intent.putExtra(StoreHomeActivity.STORE_ID, temp.getStoreId());
                        mContext.startActivity(intent);
                    }
                });
                storeName.setText(temp.getStoreName());
            }

            checkbox.setChecked(temp.isSelect());
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    temp.setSelect(!(temp.isSelect()));
                    if (listener != null) {
                        listener.onChange();
                    }
                }
            });

            //价格要处理
            goodsSumPrice.setText(MathUtil.priceForAppWithSign(temp.getDiscountPrice()));
            count.setText(String.valueOf(temp.getNum() > 0 ? temp.getNum() : 1));

            final ShoppingComboListAdapter mAdapter = new ShoppingComboListAdapter(mContext, temp.getGoodsList());
            goodsBaseListview.setAdapter(mAdapter);

            count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showUpdataNumDialog(position, mAdapter.getMinStock());
                }
            });
            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (temp.getNum() > 1) {
                        int count = temp.getNum();
                        temp.setNum(count - 1);
                        notifyDataSetChanged();
                        if (listener != null) {
                            listener.onNumChange(position, count, count - 1);
                        }
                    }
                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (temp.getNum() <= mAdapter.getMinStock() - 1) {
                        int count = temp.getNum();
                        temp.setNum(count + 1);
                        notifyDataSetChanged();
                        if (listener != null) {
                            listener.onNumChange(position, count, count + 1);
                        }
                    } else {
                        Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return convertView;
        }

        //满赠促销
        if (temp.getPromotionType() == 5) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).
                        inflate(R.layout.fragment_shopping_car_item_promition, parent, false);
            }

            ImageView storeLogo = ViewHolder.get(convertView, R.id.store_logo);
            TextView storeName = ViewHolder.get(convertView, R.id.store_name);
            ImageView storeInfoNavigator = ViewHolder.get(convertView, R.id.store_info_navigator);
            MyListView goodsBaseListview = ViewHolder.get(convertView, R.id.goods_base_listview);
            LinearLayout giftBaseLayout = ViewHolder.get(convertView, R.id.gift_base_layout);
            LinearLayout promotion_layout = ViewHolder.get(convertView, R.id.promotion_layout);
            Button reselect = ViewHolder.get(convertView, R.id.reselect);
            DropDownBoxsView goods_drop = ViewHolder.get(convertView, R.id.goods_drop);

            if (temp.getStoreId() <= 0) {
                storeLogo.setImageResource(R.drawable.lehu);
                storeInfoNavigator.setVisibility(View.GONE);
                storeName.setOnClickListener(null);
                if (TextUtils.isEmpty(temp.getStoreName())) {
                    storeName.setText(R.string.lehu_store_name);
                } else {
                    storeName.setText(temp.getStoreName());
                }
            } else {
                ImageManager.LoadWithServer(temp.getStoreLogo(), storeLogo);
                storeInfoNavigator.setVisibility(View.VISIBLE);
                storeName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, StoreHomeActivity.class);
                        intent.putExtra(StoreHomeActivity.STORE_ID, temp.getStoreId());
                        mContext.startActivity(intent);
                    }
                });
                storeName.setText(temp.getStoreName());
            }

            /*if (giftBaseLayout.getChildCount() > 0) {
                giftBaseLayout.removeAllViews();
            }

            if (temp.getGoodsList() != null && temp.getGoodsList().size() > 1) {
                if (temp.getGoodsList().get(0).isGift()) {
                    LinearLayout goodsGiftLayout = (LinearLayout) LayoutInflater.from(mContext).
                            inflate(R.layout.fragment_shopping_car_item_single_goodsgift, null, false);
                    ImageView gift_imageview = (ImageView) goodsGiftLayout.findViewById(R.id.gift_imageview);
                    TextView gift_title = (TextView) goodsGiftLayout.findViewById(R.id.gift_title);
                    TextView gift_price = (TextView) goodsGiftLayout.findViewById(R.id.gift_price);
                    TextView gift_count = (TextView) goodsGiftLayout.findViewById(R.id.gift_count);

                    ImageManager.LoadWithServer(temp.getGoodsList().get(0).getGoodsImg(), gift_imageview);
                    gift_imageview.setOnClickListener(new OnClickListenerToGoods(temp.getGoodsList().get(0).getGoodsId(),
                            temp.getGoodsList().get(0).getGoodsNo(), temp.getGoodsList().get(0).getStoreId(),
                            temp.getGoodsList().get(0).getIsShowApp()));
                    String zeng = ResourceUtils.changeStringColor("#dd434d", "赠品");
                    gift_title.setText(Html.fromHtml(zeng + temp.getGoodsList().get(0).getGoodsName()));
                    gift_price.setText(MathUtil.priceForAppWithSign(temp.getGoodsList().get(0).getGoodsPrice()));
                    gift_count.setText("x" + temp.getGoodsList().get(0).getNum());
                    giftBaseLayout.addView(goodsGiftLayout);

                    temp.getGoodsList().remove(0);
                }
            }*/

            ShoppingPromitionListAdapter mAdapter = new ShoppingPromitionListAdapter(mContext,
                    temp.getGoodsList(), listener, position);
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

            if (temp.getGiftGoodsList() != null && temp.getGiftGoodsList().size() > 0) {
                promotion_layout.setVisibility(View.VISIBLE);
                reselect.setVisibility(View.VISIBLE);
                reselect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChoseGiftView dialog = new ChoseGiftView(mContext, R.style.drop_down_box_dialog_style);
                        dialog.setData(temp.getGiftGoodsList(), position);
                        dialog.setPrice(temp.getPromotion().getDiscountCash());
                        dialog.setChoseGiftCallback(new ChoseGiftView.ChoseGiftCallback() {
                            @Override
                            public void onSelectItem(int fatherPosition, int position, ShoppingCarGoodsBean object) {
                                listener.onPromotionChange(fatherPosition, temp.getPromotion(), object);
                            }
                        });
                        dialog.show();
                    }
                });
            } else {
                promotion_layout.setVisibility(View.GONE);
                reselect.setVisibility(View.GONE);
            }
            return convertView;
        }

        
        //单品
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.fragment_shopping_car_item_single, parent, false);
        }
        ImageView storeLogo = ViewHolder.get(convertView, R.id.store_logo);
        TextView storeName = ViewHolder.get(convertView, R.id.store_name);
        ImageView storeInfoNavigator = ViewHolder.get(convertView, R.id.store_info_navigator);
        CheckBox checkbox = ViewHolder.get(convertView, R.id.checkbox);
        ImageView goodsImageview = ViewHolder.get(convertView, R.id.goods_imageview);
        TextView goodsTitle = ViewHolder.get(convertView, R.id.goods_title);
        TextView guige = ViewHolder.get(convertView, R.id.goods_guige);
        TextView goodsPrice = ViewHolder.get(convertView, R.id.goods_price);
        ImageView btnMinus = ViewHolder.get(convertView, R.id.btnMinus);
        TextView count = ViewHolder.get(convertView, R.id.count);
        ImageView btnAdd = ViewHolder.get(convertView, R.id.btnAdd);
        TextView goodsStock = ViewHolder.get(convertView, R.id.goods_stock);
        LinearLayout giftBaseLayout = ViewHolder.get(convertView, R.id.gift_base_layout);
        LinearLayout promotion_layout = ViewHolder.get(convertView, R.id.promotion_layout);
        DropDownBoxsView goods_drop = ViewHolder.get(convertView, R.id.goods_drop);

        if (temp.getStoreId() <= 0) {
            storeLogo.setImageResource(R.drawable.lehu);
            storeInfoNavigator.setVisibility(View.GONE);
            storeName.setOnClickListener(null);
            if (TextUtils.isEmpty(temp.getStoreName())) {
                storeName.setText(R.string.lehu_store_name);
            } else {
                storeName.setText(temp.getStoreName());
            }
        } else {
            ImageManager.LoadWithServer(temp.getStoreLogo(), storeLogo);
            storeInfoNavigator.setVisibility(View.VISIBLE);
            storeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, StoreHomeActivity.class);
                    intent.putExtra(StoreHomeActivity.STORE_ID, temp.getStoreId());
                    mContext.startActivity(intent);
                }
            });
            storeName.setText(temp.getStoreName());
        }

        //删除订单时不能考虑库存，所以要可以选中
//        if(temp.getGoodsStock()<=0) {
//            temp.setSelect(false);
//        }
        checkbox.setChecked(temp.isSelect());
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(temp.getGoodsStock()>0) {
                temp.setSelect(!(temp.isSelect()));
                if (listener != null) {
                    listener.onChange();
                }
//                } else {
//                    Toast.makeText(mContext, "该商品暂时无货", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        //视图重用的时候会触发该事件
        /*checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                list.get(position).setSelect(isChecked);
                if (listener != null) {
                    listener.onChange();
                }
            }
        });*/

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
        count.setText(String.valueOf(temp.getNum() > 0 ? temp.getNum() : 1));
        count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdataNumDialog(position, temp.getGoodsStock());
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp.getNum() > 1) {
                    int count = temp.getNum();
                    temp.setNum(count - 1);
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.onNumChange(position, count, count - 1);
                    }
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp.getNum() <= temp.getGoodsStock() - 1) {
                    int count = temp.getNum();
                    temp.setNum(count + 1);
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.onNumChange(position, count, count + 1);
                    }
                } else {
                    Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                }
            }
        });
        goodsStock.setText(temp.getGoodsStock() >= temp.getNum() ? "有货" : "无货");

        if (giftBaseLayout.getChildCount() > 0) {
            giftBaseLayout.removeAllViews();
        }
        if (temp.getPromotionType() != 5) {
            for (int i = 0; i < temp.getGiftGoodsList().size(); i++) {
                LinearLayout giftLayout = (LinearLayout) LayoutInflater.from(mContext).
                        inflate(R.layout.fragment_shopping_car_item_single_gift, null, false);
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
        }

        //当促销活动的size为1的时候，第一个活动会是不使用活动优惠
        if (temp.getPromotionList().size() > 1) {
            ArrayList<HashMap<String, Object>> nameList = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < temp.getPromotionList().size(); i++) {
                HashMap<String, Object> hashMaptemp = new HashMap<String, Object>();
                hashMaptemp.put("id", temp.getPromotionList().get(i).getPromotionId());
                hashMaptemp.put("name", temp.getPromotionList().get(i).getPromotionName());
                hashMaptemp.put("promotionItemBean", temp.getPromotionList().get(i));
                hashMaptemp.put("position", position);
                nameList.add(hashMaptemp);
            }
            promotion_layout.setVisibility(View.VISIBLE);
            goods_drop.setVisibility(View.VISIBLE);
            goods_drop.setData(nameList);
            goods_drop.setSelectItemById(temp.getPromotionId());
            goods_drop.setDropDownBoxsCallback(mDropDownBoxsCallback);
        } else {
            promotion_layout.setVisibility(View.GONE);
            goods_drop.setVisibility(View.GONE);
        }
        return convertView;
    }

    public void deleteItem() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<ShoppingCarGoodsBean> temp, boolean isAddFinish) {
        if (temp == null) {
            return;
        }
        if (temp instanceof ArrayList) {
            list.addAll(temp);
        }
        if (isAddFinish) {
            notifyDataSetChanged();
        }
    }

    public void addItem(ShoppingCarGoodsBean temp, boolean isAddFinish) {
        if (temp == null) {
            return;
        }
        list.add(temp);
        if (isAddFinish) {
            notifyDataSetChanged();
        }
    }

    public void setListener(DataChangeListener listener) {
        this.listener = listener;
    }

    /*public void checkTheGift(final int position, final ShoppingCarPromotionItemBean data,double sumPrice) {
        MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                DonateBean bean = DonateBean.explainJson(content, mContext);
                if (bean != null && bean.type > 0) {
                    ChoseGiftView dialog = new ChoseGiftView(mContext, R.style.drop_down_box_dialog_style);
                    dialog.setData(bean.getGoodsList(), position);
                    dialog.setChoseGiftCallback(new ChoseGiftView.ChoseGiftCallback() {
                        @Override
                        public void onSelectItem(int fatherPosition, int position, int goodsId, DonateBean.GiftList object) {
                            listener.onPromotionChange(fatherPosition, data, object);
                        }
                    });
                    dialog.show();
                } else {
                    Toast.makeText(mContext,bean.msg,Toast.LENGTH_SHORT).show();
                }
            }
        };
        RequstClient.donateData(handler, data.getPromotionId(),sumPrice);
    }*/

    public ArrayList<ShoppingCarGoodsBean> getList() {
        return list;
    }

    private void showUpdataNumDialog(final int position, final int stock) {
        final ShoppingCarGoodsBean temp = list.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.shoppingcar_update_num, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("修改购买数量");
        ImageView btnMinus = (ImageView) view.findViewById(R.id.btnMinus);
        final EditText countEdit = (EditText) view.findViewById(R.id.count);
        ImageView btnAdd = (ImageView) view.findViewById(R.id.btnAdd);
        Button btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        countEdit.setText(String.valueOf(temp.getNum()));
        countEdit.setSelection(String.valueOf(temp.getNum()).length());
        btnConfirm.setText("确定");
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = MathUtil.stringToInt(countEdit.getText().toString());
                if (count <= 0) {
                    countEdit.setText("1");
                    return;
                }
                dialog.dismiss();
                notifyDataSetChanged();
                if (listener != null) {
                    listener.onNumChange(position, temp.getNum(), count);
                }
            }
        });
        btnCancel.setText("取消");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = MathUtil.stringToInt(countEdit.getText().toString());
                if (count > 1) {
                    countEdit.setText(String.valueOf(count - 1));

                } else {
                    countEdit.setText("1");
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = MathUtil.stringToInt(countEdit.getText().toString());
                if (count <= stock - 1) {
                    countEdit.setText(String.valueOf(count + 1));
                } else {
                    Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                }
            }
        });
        countEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int count = MathUtil.stringToInt(s.toString());
//                if (count < 1) {
//                    countEdit.setText("1");
//                } else
                if (count > stock) {
                    countEdit.setText(String.valueOf(stock));
                    Toast.makeText(mContext, "该商品没有这么多库存！", Toast.LENGTH_SHORT).show();
                }
                countEdit.setSelection(s.length());
            }
        });
        dialog = new Dialog(mContext, R.style.dialog);
        dialog.setContentView(view);
        dialog.show();
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
