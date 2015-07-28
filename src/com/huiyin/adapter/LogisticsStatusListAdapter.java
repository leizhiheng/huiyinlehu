package com.huiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 物流状态流程
 * Created by lian on 2015/5/27.
 */
public class LogisticsStatusListAdapter extends BaseAdapter{
    private Context context;
    private List<HashMap<String,String>> mMapList;
    public LogisticsStatusListAdapter(Context context,List<HashMap<String, String>> mapList){
        this.context=context;
        setMapList(mapList);
    }

    public void setMapList(List<HashMap<String, String>> mapList) {
        if (mMapList==null){
            mMapList=new ArrayList<HashMap<String,String>>();
        }
        if (mapList!=null){
            for (int i = 0; i < mapList.size(); i++) {
                mMapList.add(mapList.get(i));
            }
        }
    }

    @Override
    public int getCount() {
        return mMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Myholder myholder;
        if(null==convertView){
            myholder=new Myholder();
            convertView=LayoutInflater.from(context).inflate(R.layout.layout_logistics_status_item,null);
            myholder.iv_status= (ImageView) convertView.findViewById(R.id.iv_status);
            myholder.dotted= convertView.findViewById(R.id.dotted);
            myholder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            myholder.tv_describ=(TextView)convertView.findViewById(R.id.tv_describ);
            convertView.setTag(myholder);
        }else
            myholder=(Myholder)convertView.getTag();
        HashMap<String,String> item=mMapList.get(position);
        myholder.tv_time.setText(item.get("time"));
        myholder.tv_describ.setText(item.get("context"));

        return convertView;
    }
    static class Myholder{
        public ImageView iv_status;
        public View dotted;
        public TextView tv_time;
        public TextView tv_describ;
    }
}
