package com.huiyin.ui.classic;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.DiscountPackageBean;
import com.huiyin.ui.store.StoreLehu;
import com.huiyin.utils.CommonTools;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.PopupWindowUtils;
import com.huiyin.utils.ResourceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠套餐
 * Created by lian on 2015/5/15.
 */
public class DiscountPackage implements View.OnClickListener {

    private Context context;
    private final String TAG = "DiscountPackage";
    private View view;
    private PopupWindowUtils popuUtils;
    private DiscountPackageBean discountPackageBean;
    private Button mBtn_buy_discount_package;
    private TextView mTv_goods_name;
    private TextView mTv_discount_price;
    private TextView tv_lehuprice;
    private TextView tv_show_discount;
    private HorizontialListView mLv_discount_name;
    private ListView mLv_discount_list;
    private DiscountPackageListAdapter packageListAdapter;
    private int parentIndex;
    private Handler mHandler;

    public DiscountPackage(Context context, View relyview, DiscountPackageBean discountPackageBean,SelectResultListener listener) {
        this.context = context;
        this.discountPackageBean = discountPackageBean;
        this.listener=listener;
        if(null!=discountPackageBean&&0!=discountPackageBean.goodsGroup.size()){
            parentIndex =0;
            discountPackageBean.goodsGroup.get(0).isSelected = true;
            view = LayoutInflater.from(context).inflate(R.layout.layout_pop_discount_package, null);
            popuUtils = new PopupWindowUtils(context, view, relyview);
            init();
        }
    }

    public void init() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (popuUtils != null) {
                    popuUtils.getPopupWindow().dismiss();
                }
            }
        };
        findView();
        setView();
        setListener();
    }


    public void findView() {
        mBtn_buy_discount_package = (Button) view.findViewById(R.id.btn_buy_discount_package);
        mTv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
        tv_show_discount = (TextView) view.findViewById(R.id.tv_show_discount);
        mTv_discount_price = (TextView) view.findViewById(R.id.tv_discount_price);
        tv_lehuprice = (TextView) view.findViewById(R.id.tv_lehuprice);
        mLv_discount_name = (com.huiyin.ui.classic.HorizontialListView) view.findViewById(R.id.lv_discount_name);
        mLv_discount_list = (ListView) view.findViewById(R.id.lv_discount_list);
    }

    public void setView() {
        DiscountPackageBean.GOODS goods = discountPackageBean.goodsGroup.get(0);
        goods.isSelected=true;
        setTopShow(goods);
        packageListAdapter = new DiscountPackageListAdapter(discountPackageBean.goodsGroup.get(0).list);
        mLv_discount_list.setAdapter(packageListAdapter);
        final HlistViewAdapter adapter = new HlistViewAdapter();
        mLv_discount_name.setLayoutParams(getLayoutParams(context,adapter.getCount(),100,45));
        mLv_discount_name.setAdapter(adapter);

        mLv_discount_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!discountPackageBean.goodsGroup.get(position).isSelected) {
                    for (DiscountPackageBean.GOODS goods : discountPackageBean.goodsGroup) {
                        goods.isSelected = false;
                    }
                    discountPackageBean.goodsGroup.get(position).isSelected = true;
                    adapter.notifyDataSetChanged();
                    if (null != packageListAdapter) {
                        packageListAdapter.setList(discountPackageBean.goodsGroup.get(position).list);
                        packageListAdapter.notifyDataSetChanged();
                    }
                    parentIndex = position;
                    setTopShow(discountPackageBean.goodsGroup.get(position));
                }
            }
        });
    }
    /**
     *  根据套餐数量动态设置长度
     *  @param context 上下文环境
     *  @param num 子view个数
     *  @param mWidthDP 子view长度,dp
     *  @param mHeightDP 子view高度,dp
     */
    private LinearLayout.LayoutParams getLayoutParams(Context context,int num,int mWidthDP,int mHeightDP) {
        int width;
        if (num*CommonTools.dip2px(context, mWidthDP) < popuUtils.getPopupWindow().getWidth()){
            width=num*CommonTools.dip2px(context,mWidthDP);
        }else{
            width=LinearLayout.LayoutParams.MATCH_PARENT;
        }
        int height=CommonTools.dip2px(context, mHeightDP);
        return new LinearLayout.LayoutParams(width, height);
    }
    public void setTopShow(DiscountPackageBean.GOODS goods ) {
        tv_show_discount.setText("立省\n" + MathUtil.priceForAppWithSign(goods.REDUCE_PRICE));
        mTv_goods_name.setText(goods.NAME);
        Spanned text = Html.fromHtml(ResourceUtils.changeStringColor("#999999", "套餐价：") +
                ResourceUtils.changeStringColor("#E0545C", MathUtil.priceForAppWithSign(goods.GROUP_PRICE)));
        mTv_discount_price.setText(text);
        String lehuPrice="乐虎价："+MathUtil.priceForAppWithSign(goods.LH_PRICE);
        SpannableString sp = new SpannableString(lehuPrice);
        sp.setSpan(new StrikethroughSpan(), 0, lehuPrice.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_lehuprice.setText(sp);
    }

    public void setListener() {
        mBtn_buy_discount_package.setOnClickListener(this);
        mLv_discount_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context,ProductsDetailActivity.class);
                ArrayList<DiscountPackageBean.GOOD>  list=discountPackageBean.goodsGroup.get(parentIndex).list;
                intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID,list.get(position).GOODS_ID);
                intent.putExtra(ProductsDetailActivity.STORE_ID,list.get(position).STORE_ID);
                intent.putExtra(ProductsDetailActivity.GOODS_NO,list.get(position).GOODS_NO);
                context.startActivity(intent);
            }
        });
    }


    private SelectResultListener listener;

    public void setOnSelectResultListener(SelectResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btn_buy_discount_package){
            if (listener!=null){
                listener.selectResult(getPromotionId());
            }
        }
    }
    private String getPromotionId(){
        for (DiscountPackageBean.GOODS goods : discountPackageBean.goodsGroup) {
           if ( goods.isSelected){
               if (goods.list!=null&&!goods.list.isEmpty()){
                        return goods.list.get(0).PROMOTION_ID;
               }
           }
        }
        return "-1";
    }
    /**
     * 延迟dismiss
     */
    public void dismiss() {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(0, 200);
        }
    }
    public interface SelectResultListener {
        void selectResult(String Promotion_ID);
    }


    private static class ViewHolder {
        private TextView name;
    }


    /**
     * 优惠套餐名称横向列表
     */
    private class HlistViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return discountPackageBean.goodsGroup.size();
        }

        @Override
        public Object getItem(int position) {
            return discountPackageBean.goodsGroup.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyHolderHlv myHolderHlv;
            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.discount_package_hl_item, null);
                myHolderHlv = new MyHolderHlv();
                myHolderHlv.tv_hlv = (TextView) convertView.findViewById(R.id.dicount_h_list_item);
                convertView.setTag(myHolderHlv);
            } else
                myHolderHlv = (MyHolderHlv) convertView.getTag();

            if (discountPackageBean.goodsGroup.get(position).isSelected) {//选中
                myHolderHlv.tv_hlv.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                myHolderHlv.tv_hlv.setTextColor(context.getResources().getColor(R.color.black));
            }
            myHolderHlv.tv_hlv.setText(discountPackageBean.goodsGroup.get(position).NAME);
            return convertView;
        }
    }

    static class MyHolderHlv {
        TextView tv_hlv;
    }

    private class DiscountPackageListAdapter extends BaseAdapter {
        private ArrayList<DiscountPackageBean.GOOD> list;

        public DiscountPackageListAdapter(ArrayList<DiscountPackageBean.GOOD> list) {
            this.list = list;
        }

        public void setList(ArrayList<DiscountPackageBean.GOOD> list) {
            this.list = list;
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
            final DiscountPackageHolder myHolderHlv;
            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.layout_item_discountpackage, null);
                myHolderHlv = new DiscountPackageHolder();
                myHolderHlv.iv_dicountpackage = (ImageView) convertView.findViewById(R.id.iv_dicountpackage);
                myHolderHlv.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                myHolderHlv.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
                convertView.setTag(myHolderHlv);
            } else
                myHolderHlv = (DiscountPackageHolder) convertView.getTag();
            ImageManager.LoadWithServer(list.get(position).IMG,  myHolderHlv.iv_dicountpackage);
            myHolderHlv.tv_name.setText(list.get(position).GOODS_NAME);
            myHolderHlv.tv_price.setText(MathUtil.priceForAppWithSign(list.get(position).GOODS_PRICE));
            return convertView;
        }
    }

    static class DiscountPackageHolder {
        public ImageView iv_dicountpackage;
        public TextView tv_name;
        public TextView tv_price;
    }
}
