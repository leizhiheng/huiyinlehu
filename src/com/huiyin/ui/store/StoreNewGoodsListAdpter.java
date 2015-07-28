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
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ViewHolder;

/**
 * Created by justme on 15/5/13.
 */
public class StoreNewGoodsListAdpter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TwoItemGoodsList> list;


    public StoreNewGoodsListAdpter(Context mContext, boolean isTable) {
        this.mContext = mContext;
        list = new ArrayList<TwoItemGoodsList>();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).viewType;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        TwoItemGoodsList mTwoItemGoodsList = list.get(position);
        if (mTwoItemGoodsList.viewType == 0) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.textview_list_item, parent, false);
            }
            TextView time = ViewHolder.get(convertView, R.id.list_textView);
            time.setText(mTwoItemGoodsList.time);
            return convertView;
        }
        if (mTwoItemGoodsList.viewType == 1) {
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

            GoodsList temp = mTwoItemGoodsList.item1;
            ImageManager.LoadWithServer(temp.getGOODSIMG(), class_sort_list1_img_mode_iv1);
            class_sort_list1_img_mode_name1.setText(temp.getGOODSNAME());
            class_sort_list1_img_mode_jiage1.setText(MathUtil.priceForAppWithSign(temp.getPRICE()));

            class_sort_list1_img_mode_layout1.setOnClickListener(new ClickListener(temp));

            class_sort_list1_img_mode_layout2.setVisibility(View.INVISIBLE);
            class_sort_list1_img_mode_layout2.setOnClickListener(null);
            return convertView;
        }

        if (mTwoItemGoodsList.viewType == 2) {
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

            GoodsList temp = mTwoItemGoodsList.item1;
            ImageManager.LoadWithServer(temp.getGOODSIMG(), class_sort_list1_img_mode_iv1);
            class_sort_list1_img_mode_name1.setText(temp.getGOODSNAME());
            class_sort_list1_img_mode_jiage1.setText(MathUtil.priceForAppWithSign(temp.getPRICE()));

            class_sort_list1_img_mode_layout1.setOnClickListener(new ClickListener(temp));

            class_sort_list1_img_mode_layout2.setVisibility(View.VISIBLE);
            GoodsList temp2 = mTwoItemGoodsList.item2;
            ImageManager.LoadWithServer(temp2.getGOODSIMG(), class_sort_list1_img_mode_iv2);
            class_sort_list1_img_mode_name2.setText(temp2.getGOODSNAME());
            class_sort_list1_img_mode_jiage2.setText(MathUtil.priceForAppWithSign(temp2.getPRICE()));

            class_sort_list1_img_mode_layout2.setOnClickListener(new ClickListener(temp2));
            return convertView;
        }
        return convertView;
    }

    public class ClickListener implements View.OnClickListener {
        private GoodsList temp;

        public ClickListener(GoodsList temp) {
            this.temp = temp;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ProductsDetailActivity.class);
            String goods_detail_id = String.valueOf(temp.getGOODSID());
            if (goods_detail_id.equals("")) {
                return;
            }
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
        if (temp == null || temp.size() == 0) {
            return;
        }
        //这里需要进行一次分类处理  首先需要在前后两个上架时间不一致的中间插入一个上市时间
        for (int i = temp.size() - 1; i >= 0; i--) {
            if (i==0&&(list.size() == 0 ||!list.get(list.size()-1).time.equals(temp.get(i).getSHELVESTIME()))) {
                GoodsList mGoodsList = new GoodsList();
                mGoodsList.setSHELVESTIME(temp.get(i).getSHELVESTIME());
                mGoodsList.setGOODSID(-1);
                temp.add(0, mGoodsList);
                continue;
            }
            if (!temp.get(i).getSHELVESTIME().equals(temp.get(i - 1).getSHELVESTIME())) {
                GoodsList mGoodsList = new GoodsList();
                mGoodsList.setSHELVESTIME(temp.get(i).getSHELVESTIME());
                mGoodsList.setGOODSID(-1);
                temp.add(i, mGoodsList);
            }
        }
        //然后除开上架时间这种类型，
        for (int i = 0; i < temp.size(); i++) {
            TwoItemGoodsList mTwoItemGoodsList = new TwoItemGoodsList();
            if (temp.get(i).getGOODSID() == -1) {
                mTwoItemGoodsList.viewType = 0;
                mTwoItemGoodsList.time = temp.get(i).getSHELVESTIME();
                list.add(mTwoItemGoodsList);
                continue;
            }
            if (temp.get(i).getGOODSID() != -1 && (i == temp.size() - 1 || temp.get(i + 1).getGOODSID() == -1)) {
                mTwoItemGoodsList.viewType = 1;
                mTwoItemGoodsList.item1 = temp.get(i);
                mTwoItemGoodsList.time = temp.get(i).getSHELVESTIME();
                list.add(mTwoItemGoodsList);
                continue;
            }
            if (temp.get(i).getGOODSID() != -1 && i < temp.size() - 1 && temp.get(i + 1).getGOODSID() != -1) {
                mTwoItemGoodsList.viewType = 2;
                mTwoItemGoodsList.item1 = temp.get(i);
                mTwoItemGoodsList.time = temp.get(i).getSHELVESTIME();
                mTwoItemGoodsList.item2 = temp.get(++i);
                list.add(mTwoItemGoodsList);
            }
        }
        notifyDataSetChanged();
    }

    public class TwoItemGoodsList {
        public int viewType;  // 0是显示时间，1是只有一个商品，2是有两个商品
        public String time;
        public GoodsList item1;
        public GoodsList item2;
    }
}
