package com.huiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.ui.store.StoreLehu;
import com.huiyin.utils.MathUtil;

import java.util.List;

/**
 * Created by Mike on 2015/7/4.
 */
public class StoreHomeHListAdapter extends BaseAdapter {
    private List<StoreLehu> mMapList;
    private Context mContext;

    public StoreHomeHListAdapter(Context context,List<StoreLehu> mapList) {
        mMapList = mapList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mMapList==null?0:mMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHoder;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.store_home_hl_item,null);
            mHoder=new ViewHolder();
            mHoder.price= (TextView) convertView.findViewById(R.id.price);
            mHoder.holded= (ImageView) convertView.findViewById(R.id.image_holded);
            convertView.setTag(mHoder);
        }
        else{
            mHoder= (ViewHolder) convertView.getTag();
        }
        mHoder.holded.setVisibility(View.GONE);
        mHoder.price.setText(MathUtil.priceForAppWithSign(mMapList.get(position).getPRICE()));
        if (mMapList.get(position).getIS_GET().equals("1")){
            //已领取
            mHoder.holded.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    class ViewHolder{
        TextView price;
        ImageView holded;
    }
}
