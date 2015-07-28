package com.huiyin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.HistoryListEntity;
import com.huiyin.bean.LoginLogBean;

import java.util.List;

/**
 * Created by kuangyong on 2015/6/15.
 */
public class LoginLogListAdapter extends BaseAdapter{

    private Context context;
    private List<HistoryListEntity> historyList;

    public LoginLogListAdapter(Context context,LoginLogBean bean){
        this.context=context;
        if(null!=bean){
            historyList=bean.getHistoryList();
        }
        if(null!=historyList&&historyList.size()!=0){
            addHeader();//加入表头
        }
    }

    @Override
    public int getCount() {
        return historyList.size()==0?0:historyList.size();
    }

    /**
     * 加入表头
     */
    private void addHeader(){
        HistoryListEntity entity=new HistoryListEntity();
        entity.setDATA("登录日期");
        entity.setTIME("登录时间");
        entity.setIP_ADDRESS("参考地点");
        entity.setLASTIP("IP地址");
        historyList.add(0,entity);
    }

    @Override
    public Object getItem(int position) {
        return historyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LoginLogHolder loginLogHolder=null;
        if(null==convertView){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_login_lgo,null);
            loginLogHolder=new LoginLogHolder();
            loginLogHolder.layout_main=(LinearLayout)convertView.findViewById(R.id.layout_main);
            loginLogHolder.tv_login_date=(TextView)convertView.findViewById(R.id.tv_login_date);
            loginLogHolder.tv_login_time=(TextView)convertView.findViewById(R.id.tv_login_time);
            loginLogHolder.tv_ip=(TextView)convertView.findViewById(R.id.tv_ip);
            loginLogHolder.tv_adress=(TextView)convertView.findViewById(R.id.tv_adress);
            convertView.setTag(loginLogHolder);
        }else{
            loginLogHolder=(LoginLogHolder)convertView.getTag();
        }
        setItemBackground(loginLogHolder.layout_main,position);
        HistoryListEntity entity=historyList.get(position);
        loginLogHolder.tv_login_date.setText(entity.getDATA());//日期
        loginLogHolder.tv_login_time.setText(entity.getTIME());//时间
        loginLogHolder.tv_ip.setText(entity.getLASTIP());//IP
        loginLogHolder.tv_adress.setText(entity.getIP_ADDRESS());//参考地点
        return convertView;
    }

    /**
     * 设置每行背景
     * @param layout
     * @param position 当前位置
     */
    private void setItemBackground(LinearLayout layout,int position){
        int mod=position%2;
        if(mod==0){
            layout.setBackgroundColor(context.getResources().getColor(R.color.login_log_item_bg_gray));
        }else{
            layout.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    static class LoginLogHolder{
        private LinearLayout layout_main;//每行布局
        private TextView tv_login_date;//登录日期
        private TextView tv_login_time;//登录时间
        private TextView tv_ip;//ip
        private TextView tv_adress;//参考地址
    }
}
