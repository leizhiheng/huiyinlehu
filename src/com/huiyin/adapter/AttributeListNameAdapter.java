package com.huiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.custom.vg.list.CustomAdapter;
import com.huiyin.R;
import com.huiyin.bean.CommodityTypeName;

import java.util.ArrayList;

public class AttributeListNameAdapter extends CustomAdapter {
    private Context context;
    private ArrayList<CommodityTypeName.TypeName> VALUE_LIST;
    public AttributeListNameAdapter(Context context,ArrayList<CommodityTypeName.TypeName> VALUE_LIST1){
        VALUE_LIST=VALUE_LIST1;
        this.context=context;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return null != VALUE_LIST ? VALUE_LIST.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return VALUE_LIST.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View contentView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        if (null == contentView) {
            viewHolder = new ViewHolder();
            contentView = LayoutInflater.from(context).inflate(R.layout.layout_ex_child_item_cus, null);
            viewHolder.name = (TextView) contentView.findViewById(R.id.tv_attribute_name);
            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }
        viewHolder.name.setBackgroundResource(R.drawable.common_btn_white_selector);
        viewHolder.name.setTextColor(context.getResources().getColor(R.color.deep_gray));
        if(VALUE_LIST.get(position).isSelected()){
            viewHolder.name.setBackgroundResource(R.drawable.common_btn_red_selector);
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.white));
        }
        viewHolder.name.setText(VALUE_LIST.get(position).NAME);
        return contentView;
    }
    private static class ViewHolder {
        private TextView name;
    }

}
