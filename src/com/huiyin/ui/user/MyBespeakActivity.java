package com.huiyin.ui.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.ImageManager;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 我的预购
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-23
 */
public class MyBespeakActivity extends BaseActivity {

    public final static String TAG = "MyBespeakActivity";
    private final static int CANCEL_NEWGOODS=1;//取消预购
    private final static String CANCEL_NEWGOODS_ID="recordId";//取消预购
    TextView left_rb, right_rb, middle_title_tv;
    private XListView mListView;
    private MyBespeakAdapter myBespeakAdapter;
    private MyBespeakBean myBespeakBean;

    boolean load_flag;

    /**
     * 当前页
     */
    private int mPageindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mybespeak_list_layout);

        initView();
        getMyBespeakList(1);
    }

    private void initView() {

        load_flag = false;

        middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
        middle_title_tv.setText("我的预购");

        left_rb = (TextView) findViewById(R.id.left_ib);
        right_rb = (TextView) findViewById(R.id.right_ib);
        left_rb.setVisibility(View.VISIBLE);
        right_rb.setVisibility(View.GONE);
        left_rb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                finish();
            }
        });
        mListView = (XListView) findViewById(R.id.xlistview);
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        mListView.hideFooter();
        mListView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {

                load_flag = false;
                mListView.setPullLoadEnable(true);
                getMyBespeakList(1);
            }

            @Override
            public void onLoadMore() {

                getMyBespeakList(2);
            }
        });
        myBespeakAdapter = new MyBespeakAdapter();
        mListView.setAdapter(myBespeakAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                Intent intent = new Intent(mContext, ProductsDetailActivity.class);
                intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, myBespeakAdapter.getItem(arg2 - 1).GOODS_ID);
                intent.putExtra(ProductsDetailActivity.GOODS_NO, myBespeakAdapter.getItem(arg2 - 1).GOODS_NO);
                intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_subscribe_ID, myBespeakAdapter.getItem(arg2 - 1).BESPEAK_ID);
                mContext.startActivity(intent);
            }
        });
    }

    private void getMyBespeakList(int loadType) {
        if (loadType == 1) {
            mPageindex = 1;
        } else {
            mPageindex += 1;
        }
        RequstClient.bespeakRecordList(mPageindex, new CustomResponseHandler(
                this) {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String content) {

                super.onSuccess(statusCode, headers, content);
                try {
                    mListView.stopLoadMore();
                    mListView.stopRefresh();
                    JSONObject obj = new JSONObject(content);
                    if (obj.getInt("type") != 1) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (mPageindex == 1) {
                            myBespeakAdapter.deleteItem();
                        }
                        myBespeakBean = new Gson().fromJson(content,
                                MyBespeakBean.class);
                        if (myBespeakBean != null
                                && myBespeakBean.newGoodsList != null
                                && myBespeakBean.newGoodsList.size() == 10) {
                            mListView.showFooter();
                        } else {
                            mListView.hideFooter();
                            load_flag = true;
                            mListView.setPullLoadEnable(false);
                        }
                        if (myBespeakBean != null
                                && myBespeakBean.newGoodsList != null
                                && myBespeakBean.newGoodsList.size() > 0)
                            myBespeakAdapter.addItem(myBespeakBean.newGoodsList);
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });
    }

    class MyBespeakAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<MyBespeakBean.MyBespeakItem> list;

        public MyBespeakAdapter() {
            inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            list = new ArrayList<MyBespeakActivity.MyBespeakBean.MyBespeakItem>();
        }

        @Override
        public int getCount() {

            return list.size();
        }

        @Override
        public MyBespeakBean.MyBespeakItem getItem(int position) {

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        public void deleteItem() {
            list.clear();
            notifyDataSetChanged();
        }

        public void deleteItemByID(String id){
            for(int i=0; i<list.size(); i++){
                if(list.get(i).ID.equals(id)){
                    list.remove(i);
                    i--;
                }
            }
            this.notifyDataSetChanged();
        }

        public void addItem(ArrayList<MyBespeakBean.MyBespeakItem> temp) {
            if (temp == null) {
                return;
            }
            if (temp instanceof ArrayList) {
                list.addAll(temp);
                notifyDataSetChanged();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final MyBespeakBean.MyBespeakItem item = list.get(position);
            viewHolder holder;
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.mybespeak_list_item,
                        null);
                holder = new viewHolder();
                holder.mybespeak_image = (ImageView) convertView
                        .findViewById(R.id.mybespeak_image);
                holder.mybespeak_title = (TextView) convertView
                        .findViewById(R.id.mybespeak_title);
                holder.mybespeak_price = (TextView) convertView
                        .findViewById(R.id.mybespeak_price);
                holder.mybespeak_time = (TextView) convertView
                        .findViewById(R.id.mybespeak_time);
                holder.mybespeak_btn = (ImageView) convertView
                        .findViewById(R.id.mybespeak_btn);
                holder.tv_phone = (TextView) convertView
                        .findViewById(R.id.tv_phone);
                convertView.setTag(holder);
            } else {
                holder = (viewHolder) convertView.getTag();
            }

            ImageManager.LoadWithServer(item.GOODS_IMG,
                    holder.mybespeak_image);
            holder.mybespeak_title.setText(item.ACTIVITY_STATUS_NAME);//预约状态
            holder.mybespeak_price.setText(item.USER_NAME);//预购姓名
            holder.mybespeak_time.setText("预约时间：" + item.ADD_DATE);
            holder.tv_phone.setText("预留电话：" + AppContext.mUserInfo.phone);
            if ("0".equals(item.ACTIVITY_STATUS)) {//取消预约
                holder.mybespeak_btn.setImageResource(R.drawable.qxyg);
            } else if ("1".equals(item.ACTIVITY_STATUS)) {//立即抢购
                holder.mybespeak_btn.setImageResource(R.drawable.ljgoum);
            }
            holder.mybespeak_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("0".equals(item.ACTIVITY_STATUS)) {//取消预约
                        Intent intent2 = new Intent(mContext, CommonConfrimCancelDialog.class);
                        intent2.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.CANCEL_NEWGOODS);
                        intent2.putExtra(CANCEL_NEWGOODS_ID,item.ID);
                        mContext.startActivityForResult(intent2, CANCEL_NEWGOODS);
                    } else if ("1".equals(item.ACTIVITY_STATUS)) {//立即抢购
                        Intent intent = new Intent(mContext, ProductsDetailActivity.class);
                        intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID,
                                item.GOODS_ID);
                        intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_subscribe_ID,
                                item.BESPEAK_ID);
                        mContext.startActivity(intent);
                    }
                }
            });
            return convertView;
        }

        public class viewHolder {
            ImageView mybespeak_image;
            TextView mybespeak_title;
            TextView mybespeak_price;
            TextView mybespeak_time;
            ImageView mybespeak_btn;
            TextView tv_phone;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==CANCEL_NEWGOODS){//取消预购
                if(null!=data){
                    String id=data.getStringExtra(CANCEL_NEWGOODS_ID);
                    cancelNewGoods(id);
                }
            }
        }
    }

    private void cancelNewGoods(final String id) {
        RequstClient.cancelNewGoods(id, new CustomResponseHandler(mContext, true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.getInt("type") != 1) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //delItem(id);
                    myBespeakAdapter.deleteItemByID(id);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        });
    }


    public class MyBespeakBean {

        public ArrayList<MyBespeakItem> newGoodsList = new ArrayList<MyBespeakItem>();//新品预约列表

        public class MyBespeakItem {
            public String GOODS_IMG;// 商品图片
            public String ADD_DATE;// 预约时间
            public String GOODS_STATUS;// 商品状态
            public String GOODS_ID;// 商品id
            public String ID;// 预约记录id
            public String BESPEAK_ID;// 预约活动id
            public String GOODS_NO;// 商品编号
            public String USER_ID;// 用户id
            public String USER_NAME;// 预约人姓名
            public String ACTIVITY_STATUS_NAME;// 活动状态名称（等待购买，抢购中，活动已结束）
            public String ACTIVITY_STATUS;// 活动状态:0等待购买，1立即抢购
        }
    }
}
