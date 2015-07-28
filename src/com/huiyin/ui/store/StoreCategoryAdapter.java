package com.huiyin.ui.store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.utils.ViewHolder;

import java.util.ArrayList;

/**
 * Created by justme on 15/5/12.
 */
public class StoreCategoryAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<StoreCategory> list;

    public StoreCategoryAdapter(Context mContext) {
        this.mContext = mContext;
        list = new ArrayList<StoreCategory>();
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getTWOCATEGORY() == null ? 0 : list.get(groupPosition).getTWOCATEGORY().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (list.get(groupPosition).getTWOCATEGORY() != null) {
            return list.get(groupPosition).getTWOCATEGORY().get(childPosition);
        } else return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.category_group_item, parent, false);
        }
        TextView textView = ViewHolder.get(convertView, R.id.tv_attribute);
        ImageView arrow = ViewHolder.get(convertView, R.id.category_arrow);
        StoreCategory temp = list.get(groupPosition);
        textView.setText(temp.getNAME());
        if (temp.getTWOCATEGORY() != null && temp.getTWOCATEGORY().size() > 0) {
            arrow.setVisibility(View.VISIBLE);
            arrow.setImageResource(isExpanded ? R.drawable.arraw_up : R.drawable.arraw_next);
        } else {
            arrow.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.category_group_item, parent, false);
        }
        TextView textView = ViewHolder.get(convertView, R.id.tv_attribute);
        ImageView arrow = ViewHolder.get(convertView, R.id.category_arrow);
        StoreCategory.TwoCategory temp = list.get(groupPosition).getTWOCATEGORY().get(childPosition);
        textView.setText(temp.getNAME());
        arrow.setVisibility(View.INVISIBLE);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void deleteItem() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<StoreCategory> temp) {
        if (temp == null) {
            return;
        }
        if (temp instanceof ArrayList) {
            list.addAll(temp);
        }
        notifyDataSetChanged();
    }
}
