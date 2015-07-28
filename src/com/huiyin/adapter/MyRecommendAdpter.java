package com.huiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.utils.ImageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mike on 2015/6/28.
 */
public class MyRecommendAdpter extends BaseAdapter {
    List<HashMap<String,String>> list;
    Context mContext;

    public MyRecommendAdpter(Context context,List<HashMap<String,String>> templist) {
        mContext=context;
        setList(templist);
    }

    public void setList(List<HashMap<String,String>> templist) {

        if (list == null) {
            list = new ArrayList<HashMap<String,String>>();
        }
        if (templist != null) {
            list.clear();
            for (int i = 0; i < templist.size(); i++) {
                list.add(templist.get(i));
            }
        }
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.my_product_recommed_item, null);
            holder = new MyHolder();
            holder.my_recommoned_item_title = (TextView) convertView
                    .findViewById(R.id.my_recommoned_item_title);
            holder.my_recommoned_item_price = (TextView) convertView
                    .findViewById(R.id.my_recommoned_item_price);
            holder.my_recommoned_item_stock = (TextView) convertView
                    .findViewById(R.id.my_recommoned_item_stock);
            holder.my_recommoned_item_image = (ImageView) convertView
                    .findViewById(R.id.my_recommoned_item_image);
            convertView.setTag(holder);
        } else
            holder = (MyHolder) convertView.getTag();
        if (list.get(position) == null) {
            return  convertView;
        }
        holder.my_recommoned_item_title.setText(list.get(position).get("name"));
        holder.my_recommoned_item_price.setText("¥ " + list.get(position).get("price"));
        
        //隐藏stock
        holder.my_recommoned_item_stock.setVisibility(View.INVISIBLE);
        holder.my_recommoned_item_stock.setText(list.get(position).get("stock"));
        ImageManager.LoadWithServer(list.get(position).get("image"), holder.my_recommoned_item_image);
        return convertView;
    }
    static class MyHolder {
        TextView my_recommoned_item_title, my_recommoned_item_price,
                my_recommoned_item_stock;
        ImageView my_recommoned_item_image;
    }
}

