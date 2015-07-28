package com.huiyin.ui.seckill;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BaseBean;
import com.huiyin.ui.seckill.SeckillListBean.SeckillList;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.ViewHolder;

import java.util.ArrayList;

public class SeckillAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<SeckillList> list;

    public SeckillAdapter(Context mContext) {
        this.mContext = mContext;
        list = new ArrayList<SeckillList>();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.seckill_list_item, parent, false);
        }

        ImageView image = ViewHolder.get(convertView, R.id.image);
        TextView name = ViewHolder.get(convertView, R.id.seckill_name);
        TextView seckill_price = ViewHolder.get(convertView, R.id.seckill_price);
        TextView price = ViewHolder.get(convertView, R.id.price);
        TextView discount = ViewHolder.get(convertView, R.id.discount);
        Button collect = ViewHolder.get(convertView, R.id.collect);

        SeckillList temp = list.get(position);

        name.setText(temp.getCOMMODITYNAME());

        SpannableString sp = null;
        sp = new SpannableString(MathUtil.priceForAppWithSign(temp.getPRICE()));
        sp.setSpan(new StrikethroughSpan(), 0, MathUtil.priceForAppWithSign(temp.getPRICE()).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        price.setText(sp);
        int type=list.get(position).getDISCOUNTTYPE();//1、折扣 2、直降 3、折后降
        String text="";
        switch (type){
            case 1:
                text=MathUtil.keep1decimal(temp.getDISCOUNT()) + "折";
                break;
            case 2:
                text="直降"+MathUtil.priceForAppWithSign(temp.getDISCOUNT());
                break;
            case 3:
                text="优惠"+MathUtil.priceForAppWithSign(temp.getDISCOUNT());
                break;
        }
        discount.setText(text);
        //
        //seckill_price.setText(MathUtil.priceForAppWithSign(temp.getPRICE() * temp.getDISCOUNT() / 10));
        seckill_price.setText(MathUtil.priceForAppWithSign(temp.getDISCOUNTPRICE()));
        ImageManager.LoadWithServer(temp.getCOMMODITYIMAGEPATH(), image);
        if (temp.getCOLLECTFLAG() == 1) {
            collect.setText("已收藏");
        } else {
            collect.setText("收藏");
        }
        collect.setOnClickListener(new OnClick(temp, position));
        return convertView;
    }

    public void deleteItem() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addItem(ArrayList<SeckillList> temp) {
        if (temp == null) {
            return;
        }
        if (temp instanceof ArrayList) {
            list.addAll(temp);
        }
        notifyDataSetChanged();
    }

    private class OnClick implements View.OnClickListener {
        private SeckillList temp;
        private int position;

        public OnClick(SeckillList temp, int position) {
            this.temp = temp;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (AppContext.userId == null) {
                Toast.makeText(mContext, "您还没有登陆", Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setClass(mContext, LoginActivity.class);
                mContext.startActivity(i);
                return;
            }
            if (temp.getCOLLECTFLAG() == 1) {
                unCollect(AppContext.userId, String.valueOf(temp.getGOODSNO()), temp.getCOMMODITYID(), temp.getStoreId(),position);
            } else {
                collect(AppContext.userId, String.valueOf(temp.getGOODSNO()), temp.getCOMMODITYID(), temp.getStoreId(),position);
            }
        }

    }

    public int getId(int position) {
        return list.get(position).getCOMMODITYID();
    }

    public long getGoodsNo(int position) {
        return list.get(position).getGOODSNO();
    }

    public int getStoreId(int position) {
        return list.get(position).getStoreId();
    }
    /**
     * 收藏商品
     */
    private void collect(String userId, String goodsNo, int goodsId, int storeId,final int position) {
        MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {
            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(content, BaseBean.class);
                if (bean.type > 0) {
                    list.get(position).setCOLLECTFLAG(1);
                    Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "收藏失败", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        };
        RequstClient.addFocus(handler, userId, storeId, goodsNo, goodsId, 1);
    }

    /**
     * 取消收藏商品
     */
    private void unCollect(String userId, String goodsNo, int goodsId, int storeId,final int position) {
        MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(content, BaseBean.class);
                if (bean.type > 0) {
                    list.get(position).setCOLLECTFLAG(0);
                    Toast.makeText(mContext, "取消收藏", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "取消收藏失败", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }

        };
        RequstClient.cancelFocus(handler, userId, storeId, goodsNo, goodsId, 1);
    }

    /**
     * 设置某一项收藏与否
     *
     * @param position
     * @param isCollect
     */
    public void setCollect(int position, int isCollect) {
        list.get(position).setCOLLECTFLAG(isCollect);
        notifyDataSetChanged();
    }
}
