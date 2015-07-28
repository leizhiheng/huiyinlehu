package com.huiyin.ui.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.utils.ViewHolder;

import java.util.ArrayList;


/**
 * Created by justme on 15/6/30.
 */
public class StoreHomeClassiscAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> list;
    private int groupID;

    public StoreHomeClassiscAdapter(Context mContext) {
        this.mContext = mContext;
        list = new ArrayList<String>();
        groupID = -1;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.category_group_item, parent, false);
        }
        TextView textView = ViewHolder.get(convertView, R.id.tv_attribute);
        ImageView arrow = ViewHolder.get(convertView, R.id.category_arrow);
        textView.setText(list.get(position));
        arrow.setVisibility(View.INVISIBLE);
        return convertView;
    }

    public void deleteItem() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<String> temp) {
        if (temp == null) {
            return;
        }
        if (temp instanceof ArrayList) {
            list.addAll(temp);
        }
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<String> temp,int groupID) {
        list = temp;
        this.groupID = groupID;
        notifyDataSetChanged();
    }

    public int getGroupID() {
        return groupID;
    }
}
