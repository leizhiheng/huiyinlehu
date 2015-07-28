package com.huiyin.ui.store;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ViewHolder;

/**
 * Created by justme on 15/5/13.
 */
public class StoreGoodsListAdpter extends BaseAdapter {
    private Context mContext;
    private ArrayList<GoodsList> list;
    private boolean isTable;

    public StoreGoodsListAdpter(Context mContext,boolean isTable) {
        this.mContext = mContext;
        this.isTable = isTable;
        list = new ArrayList<GoodsList>();
    }

    @Override
    public int getItemViewType(int position) {
        return isTable ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        if (isTable) {
            return (list.size() + 1) / 2;
        }
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isTable) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.class_sort_list1_img_mode, parent, false);
            }
            LinearLayout class_sort_list1_img_mode_layout1 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_layout1);
            ImageView class_sort_list1_img_mode_iv1 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_iv1);
            TextView class_sort_list1_img_mode_name1 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_name1);
            TextView class_sort_list1_img_mode_jiage1 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_jiage1);

            LinearLayout class_sort_list1_img_mode_layout2 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_layout2);
            ImageView class_sort_list1_img_mode_iv2 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_iv2);
            TextView class_sort_list1_img_mode_name2 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_name2);
            TextView class_sort_list1_img_mode_jiage2 = ViewHolder.get(convertView, R.id.class_sort_list1_img_mode_jiage2);

            GoodsList temp = list.get(position*2);
            ImageManager.LoadWithServer(temp.getGOODSIMG(), class_sort_list1_img_mode_iv1);
            class_sort_list1_img_mode_name1.setText(temp.getGOODSNAME());
            class_sort_list1_img_mode_jiage1.setText(MathUtil.priceForAppWithSign(temp.getPRICE()));


            class_sort_list1_img_mode_layout1.setOnClickListener(new ClickListener(temp));

            if (list.size() > position*2 + 1) {
                class_sort_list1_img_mode_layout2.setVisibility(View.VISIBLE);
                GoodsList temp2 = list.get(position*2 + 1);
                ImageManager.LoadWithServer(temp2.getGOODSIMG(), class_sort_list1_img_mode_iv2);
                class_sort_list1_img_mode_name2.setText(temp2.getGOODSNAME());
                class_sort_list1_img_mode_jiage2.setText(MathUtil.priceForAppWithSign(temp2.getPRICE()));

                class_sort_list1_img_mode_layout2.setOnClickListener(new ClickListener(temp2));
            } else {
                class_sort_list1_img_mode_layout2.setVisibility(View.INVISIBLE);
                class_sort_list1_img_mode_layout2.setOnClickListener(null);
            }

        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.class_sort_list1_normal_mode, parent, false);
            }

            RelativeLayout class_sort_list1_normal = ViewHolder.get(convertView, R.id.class_sort_list1_normal);
            ImageView class_sort_list1_ll_iv = ViewHolder.get(convertView, R.id.class_sort_list1_ll_iv);
            TextView class_sort_list1_ll_title = ViewHolder.get(convertView, R.id.class_sort_list1_ll_title);
            TextView class_sort_list1_ll_jiage1 = ViewHolder.get(convertView, R.id.class_sort_list1_ll_jiage1);
            TextView class_sort_list1_ll_jiage2 = ViewHolder.get(convertView, R.id.class_sort_list1_ll_jiage2);
            TextView class_sort_list1_ll_haopingdu = ViewHolder.get(convertView, R.id.class_sort_list1_ll_haopingdu);

            GoodsList temp = list.get(position);

            ImageManager.LoadWithServer(temp.getGOODSIMG(), class_sort_list1_ll_iv);
            class_sort_list1_ll_title.setText(temp.getGOODSNAME());
            class_sort_list1_ll_jiage1.setText(MathUtil.priceForAppWithSign(temp.getPRICE()));

            class_sort_list1_ll_haopingdu.setText(temp.getREVIEWPERCENT()+"%");
            String hh = class_sort_list1_ll_haopingdu.getText().toString();
            class_sort_list1_ll_haopingdu.setText(hh + "(" + temp.getREVIEWNUMBER() + "人)");
            class_sort_list1_normal.setOnClickListener(new ClickListener(temp));
        }
        return convertView;
    }


    public void ChangeView(boolean isTable) {
        this.isTable = isTable;
        notifyDataSetChanged();
    }

    public class ClickListener implements View.OnClickListener {
        private GoodsList temp;

        public ClickListener(GoodsList temp) {
            this.temp = temp;
        }

        @Override
        public void onClick(View v) {

            String goods_detail_id = String.valueOf(temp.getGOODSID());
            if (goods_detail_id.equals("")) {
                return;
            }
            Intent intent = new Intent(mContext, ProductsDetailActivity.class);
            intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, goods_detail_id);
            intent.putExtra(ProductsDetailActivity.GOODS_NO,String.valueOf(temp.getGOODSNO()));
            intent.putExtra(ProductsDetailActivity.STORE_ID,String.valueOf(temp.getSTOREID()));
            mContext.startActivity(intent);
        }

    }

    public void deleteItem() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<GoodsList> temp) {
        if (temp == null) {
            return;
        }
        if (temp instanceof ArrayList) {
            list.addAll(temp);
        }
        notifyDataSetChanged();
    }
}
