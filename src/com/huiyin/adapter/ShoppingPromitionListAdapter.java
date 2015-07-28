package com.huiyin.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.bean.ShoppingCarGoodsBean;
import com.huiyin.bean.ShoppingCarPromotionItemBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ViewHolder;
import com.huiyin.wight.DropDownBoxsView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by justme on 15/7/4.
 * 购物车满赠的商品适配器
 */
public class ShoppingPromitionListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ShoppingCarGoodsBean> list;

    private DropDownBoxsView.DropDownBoxsCallback mDropDownBoxsCallback;
    private DataChangeListener listener;
    private Dialog dialog;

    private int groupPosition;

    public ShoppingPromitionListAdapter(Context mContext, ArrayList<ShoppingCarGoodsBean> list,
                                        DataChangeListener listenert, int groupPositiont) {
        this.mContext = mContext;
        this.list = list;
        this.listener = listenert;
        this.groupPosition = groupPositiont;

        mDropDownBoxsCallback = new DropDownBoxsView.DropDownBoxsCallback() {
            @Override
            public void onSelectItem(Object object) {
                if (listener != null) {
                    HashMap<String, Object> temp = (HashMap<String, Object>) object;
                    final ShoppingCarPromotionItemBean bean = (ShoppingCarPromotionItemBean)
                            temp.get("promotionItemBean");
                    int position = (Integer) temp.get("position");
                    listener.onPromotionChange(groupPosition, position, bean);
                }
            }
        };
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_shopping_car_item_promition_item, null);
        }

        CheckBox checkbox = ViewHolder.get(convertView, R.id.checkbox);
        ImageView goodsImageview = ViewHolder.get(convertView, R.id.goods_imageview);
        TextView goodsTitle = ViewHolder.get(convertView, R.id.goods_title);
        TextView guige = ViewHolder.get(convertView, R.id.goods_guige);
        TextView goodsPrice = ViewHolder.get(convertView, R.id.goods_price);
        ImageView btnMinus = ViewHolder.get(convertView, R.id.btnMinus);
        TextView count = ViewHolder.get(convertView, R.id.count);
        ImageView btnAdd = ViewHolder.get(convertView, R.id.btnAdd);
        TextView goodsStock = ViewHolder.get(convertView, R.id.goods_stock);
        DropDownBoxsView goods_drop = ViewHolder.get(convertView, R.id.goods_drop);

        final ShoppingCarGoodsBean temp = list.get(position);

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
                        listener.onNumChange(groupPosition, position, count, count - 1);
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
                        listener.onNumChange(groupPosition, position, count, count + 1);
                    }
                } else {
                    Toast.makeText(mContext, "库存不足", Toast.LENGTH_SHORT).show();
                }
            }
        });
        goodsStock.setText(temp.getGoodsStock() >= temp.getNum() ? "有货" : "无货");

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
            goods_drop.setVisibility(View.VISIBLE);
            goods_drop.setData(nameList);
            goods_drop.setSelectItemById(temp.getPromotionId());
            goods_drop.setDropDownBoxsCallback(mDropDownBoxsCallback);
        } else {
            goods_drop.setVisibility(View.GONE);
        }

        return convertView;
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