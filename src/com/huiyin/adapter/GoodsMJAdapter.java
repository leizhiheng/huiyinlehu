package com.huiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.HINT;

import java.util.ArrayList;

/**
 * Created by lian on 2015/5/13.
 */
public class GoodsMJAdapter extends BaseAdapter{
    public static final String TAG="GoodsGiftAdapter";
    private ArrayList<HINT> PROMOTION_REDUCE;
    private Context context;

    public GoodsMJAdapter(Context context, ArrayList<HINT> PROMOTION_REDUCE){
        this.context=context;
        this.PROMOTION_REDUCE=PROMOTION_REDUCE;
    }
    @Override
    public int getCount() {
        return null != PROMOTION_REDUCE ? PROMOTION_REDUCE.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return PROMOTION_REDUCE.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if(null==convertView){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_goods_list_cx,null);
            myHolder=new MyHolder();
            myHolder.tv_git_type= (TextView) convertView.findViewById(R.id.tv_git_type);
            myHolder.tv_show_content= (TextView) convertView.findViewById(R.id.tv_show_content);
            myHolder.tv_jiantou= (TextView) convertView.findViewById(R.id.tv_jiantou);
            convertView.setTag(myHolder);
        }else
            myHolder= (MyHolder) convertView.getTag();
        myHolder.tv_jiantou.setVisibility(View.GONE);
        myHolder.tv_git_type.setText("满减");
        myHolder.tv_show_content.setText(PROMOTION_REDUCE.get(position).hint);
        return convertView;
    }
    static class MyHolder{
        public TextView tv_git_type;//类型
        public TextView tv_show_content;//显示内容
        public TextView tv_jiantou;//箭头
    }
}
