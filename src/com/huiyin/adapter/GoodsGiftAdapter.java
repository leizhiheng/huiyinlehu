package com.huiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.PROMOTION;

import java.util.ArrayList;

/**
 * Created by lian on 2015/5/13.
 */
public class GoodsGiftAdapter extends BaseAdapter{
    public static final String TAG="GoodsGiftAdapter";
    private ArrayList<PROMOTION> PROMOTION_GIFT;
    private Context context;

    public GoodsGiftAdapter(Context context,ArrayList<PROMOTION> PROMOTION_GIFT){
        this.context=context;
        this.PROMOTION_GIFT=PROMOTION_GIFT;
    }
    @Override
    public int getCount() {
        return null != PROMOTION_GIFT ? PROMOTION_GIFT.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return PROMOTION_GIFT.get(position);
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
        if("1".equals(PROMOTION_GIFT.get(position).IS_SHOW_APP)){
            myHolder.tv_jiantou.setVisibility(View.VISIBLE);
        }else{
            myHolder.tv_jiantou.setVisibility(View.GONE);
        }
        myHolder.tv_git_type.setText("赠送");
        myHolder.tv_show_content.setText(PROMOTION_GIFT.get(position).GOODS_NAME);
        return convertView;
    }
    static class MyHolder{
        public TextView tv_git_type;//类型
        public TextView tv_show_content;//显示内容
        public TextView tv_jiantou;//箭头
    }
}
