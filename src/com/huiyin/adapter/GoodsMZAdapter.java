package com.huiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.GOODS;

import java.util.ArrayList;

/**
 * Created by lian on 2015/5/13.
 */
public class GoodsMZAdapter extends BaseExpandableListAdapter{
    public static final String TAG="GoodsMZAdapter";
    private ArrayList<GOODS> PROMOTION_DONATE;
    private Context context;
    public GoodsMZAdapter(Context context,ArrayList<GOODS> PROMOTION_DONATE){
        this.PROMOTION_DONATE=PROMOTION_DONATE;
        this.context=context;
    }


    @Override
    public int getGroupCount() {
        return PROMOTION_DONATE.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return PROMOTION_DONATE.get(groupPosition).goodsList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return PROMOTION_DONATE.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return PROMOTION_DONATE.get(groupPosition).goodsList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if(null==convertView){
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_goods_list_cx,null);
            myHolder=new MyHolder();
            myHolder.tv_git_type= (TextView) convertView.findViewById(R.id.tv_git_type);
            myHolder.tv_show_content= (TextView) convertView.findViewById(R.id.tv_show_content);
            myHolder.tv_jiantou= (TextView) convertView.findViewById(R.id.tv_jiantou);
            myHolder.iv_indecator= (ImageView) convertView.findViewById(R.id.iv_indecator);
            convertView.setTag(myHolder);
        }else
            myHolder= (MyHolder) convertView.getTag();
        myHolder.tv_jiantou.setVisibility(View.GONE);
        myHolder.iv_indecator.setVisibility(View.VISIBLE);
        if(isExpanded){//是否展开
            myHolder.iv_indecator.setBackgroundResource(R.drawable.arraw_next);
        }else{
            myHolder.iv_indecator.setBackgroundResource(R.drawable.arraw_up);
        }
        myHolder.tv_git_type.setText("满赠");
        myHolder.tv_show_content.setText(PROMOTION_DONATE.get(groupPosition).hint);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
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
        if("1".equals(PROMOTION_DONATE.get(groupPosition).goodsList.get(childPosition).IS_SHOW_APP)){
            myHolder.tv_jiantou.setVisibility(View.VISIBLE);
        }else{
            myHolder.tv_jiantou.setVisibility(View.GONE);
        }
        myHolder.tv_git_type.setVisibility(View.GONE);
        myHolder.tv_show_content.setText(PROMOTION_DONATE.get(groupPosition).goodsList.get(childPosition).GOODS_NAME);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    static class MyHolder{
        public TextView tv_git_type;//类型
        public TextView tv_show_content;//显示内容
        public TextView tv_jiantou;//箭头
        public ImageView iv_indecator;//箭头
    }
}
