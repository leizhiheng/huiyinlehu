package com.huiyin.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.AddressItem;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.LogUtil;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 地址管理
 * @author 刘远祺
 *
 */
public class AddressManagementActivity extends BaseActivity {

    public static String TAG = "AddressManagementActivity";
    private TextView mTextView;
    ListView mListView;
    private AddressBean mAddressBean;
    private AddressAdapter mAddressAdapter;
    private TextView left_rb, middle_title_tv;
    private ImageView right_rb;

    private int request_modify_add = 1, request_delete = 2,
            request_default_addr = 3;
    // 1订单编辑进来、2账户管理进来
    public static String order = "1", addr = "2";
    String which;
    // 要删除的地址id\
    private String addressId;
    private String orderPrice;
    private String freight;
    private String shopIds;
    private AddressItem item;
    private String currentAddressId; //当前选中的收货地址
    private boolean selectedAddressHasDel = false; //选中的收货地址是否被删除
    
    // add by zhyao @2015/6/22 添加是不是全部消费卷字段
    private boolean isAllConsumption = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_address_list);

        initView();

        initData();
    }

    /**
     * 初始化
     */
    private void initData() {

        if (AppContext.userId == null) {
            Toast.makeText(AddressManagementActivity.this, "请先登录！",
                    Toast.LENGTH_SHORT).show();
            return;
        }// 1参数无效，这里一次性返回所有地址   // add by zhyao @2015/6/24 添加是不是全部消费卷参数allConsumption
        RequstClient.getAddrList(AppContext.userId, "1", isAllConsumption,
                new CustomResponseHandler(this) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String content) {
                        super.onSuccess(statusCode, headers, content);
                        LogUtil.i(TAG, "getAddrList:" + content);
                        Logger.json(content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            if (!obj.getString("type").equals("1")) {
                                String errorMsg = obj.getString("msg");
                                Toast.makeText(getBaseContext(), errorMsg,
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mAddressBean = new Gson().fromJson(content,
                                    AddressBean.class);
                            mAddressAdapter = new AddressAdapter(mAddressBean);
                            mListView.setAdapter(mAddressAdapter);
                            if (!obj.has("shippingAddress")) {
                                mTextView.setText("暂无收货地址");
                            } else if (mAddressBean.shippingAddress.size() > 0) {
                                mTextView.setText("长按设为默认");
                            } else {
                                mTextView.setText("暂无收货地址");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 初始化
     */

    private void initView() {
        left_rb = (TextView) findViewById(R.id.ab_back);
        right_rb = (ImageView) findViewById(R.id.ab_right_btn);
        right_rb.setImageResource(R.drawable.ab_ic_add);

        middle_title_tv = (TextView) findViewById(R.id.ab_title);
        middle_title_tv.setText("地址管理");

        mTextView = (TextView) findViewById(R.id.mTextView);

        left_rb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                back();
            }
        });

        right_rb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
            	
            	//判断是否已经有20条记录
            	int count = mAddressAdapter.getCount();
            	if(count >= 20){
            		showMyToast("最多添加20个地址");
            		return;
            	}
            	
                Intent i = new Intent();
                i.setClass(AddressManagementActivity.this,
                        AddressModifyActivity.class);
                i.putExtra(AddressModifyActivity.TAG, AddressModifyActivity.ADD);
                // add by zhyao @2015/6/22 添加是不是全部消费卷字段
                i.putExtra("isAllConsumption", isAllConsumption);
                startActivityForResult(i, request_modify_add);
            }
        });

        mAddressBean = new AddressBean();
        mListView = (ListView) findViewById(R.id.mListView);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (which.equals(order)) {
                    item = mAddressBean.shippingAddress.get(arg2);
                 // add by zhyao @2015/6/25 包含实体类商品，如果详细地址为空，则补全地址
                    if((TextUtils.isEmpty(item.PROVINCE_ID) || TextUtils.isEmpty(item.CITY_ID) || TextUtils.isEmpty(item.AREA_ID)) && !isAllConsumption) {
                    	UIHelper.showToast("请补充完整详细地址");
                    	Intent i = new Intent();
                        i.setClass(AddressManagementActivity.this,
                                AddressModifyActivity.class);
                        i.putExtra(AddressModifyActivity.TAG,
                                AddressModifyActivity.MODIFY);
                        i.putExtra("addressItem", item);
                        i.putExtra("isAllConsumption", isAllConsumption);
                        startActivityForResult(i, request_modify_add);
                    }
                    else {
                    	// add by zhyao @2015/6/24 添加是不是全部消费卷参数
                    	rebulidFreight(item.PROVINCE_ID, item.CITY_ID,
                                item.AREA_ID, shopIds, orderPrice, freight, isAllConsumption);
                    }

                }
            }
        });
        mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                AddressItem item = mAddressBean.shippingAddress.get(arg2);
                addressId = item.ADDRESSID;
                Intent i = new Intent(AddressManagementActivity.this,
                        CommonConfrimCancelDialog.class);
                i.putExtra(CommonConfrimCancelDialog.TASK,
                        CommonConfrimCancelDialog.TASK_DEFAULT_ADDR);
                startActivityForResult(i, request_default_addr);
                return true;
            }
        });

        which = getIntent().getStringExtra(TAG);
        orderPrice = getIntent().getStringExtra("orderPrice");
        freight = getIntent().getStringExtra("freight");
        shopIds = getIntent().getStringExtra("shopIds");
        currentAddressId = getIntent().getStringExtra("addressId");
        // add by zhyao @2015/6/22 添加是不是全部消费卷字段
        isAllConsumption = getIntent().getBooleanExtra("isAllConsumption", false);
    }

    private void back() {
        if (selectedAddressHasDel) {
            Intent i = new Intent();
            i.putExtra("goodsSumfreightStr",-1);
            setResult(RESULT_OK, i);
            finish();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == request_modify_add
                && resultCode == Activity.RESULT_OK) {// 不论增加、修改刷新列表
            initData();
        } else if (requestCode == request_delete
                && resultCode == Activity.RESULT_OK) {
            isNeedSelectOtherAddress(addressId);
            doDeleteAddress(addressId);
        } else if (requestCode == request_default_addr
                && resultCode == Activity.RESULT_OK) {
            doModifyDefaultAddr(addressId, "1");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 删除
     */
    private void doDeleteAddress(String addressId) {

        if (AppContext.userId == null) {
            return;
        }
        RequstClient.postDeleteAddress(AppContext.userId, addressId,
                new CustomResponseHandler(this) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String content) {
                        super.onSuccess(statusCode, headers, content);
                        LogUtil.i(TAG, "postDeleteAddress:" + content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            if (!obj.getString("type").equals("1")) {
                                String errorMsg = obj.getString("msg");
                                Toast.makeText(getBaseContext(), errorMsg,
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            initData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 刷新列表
     */
    private void refreshList() {
        if (mAddressBean != null) {
            mAddressAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 修改默认地址（和修改、新增同一个接口, 只传userId、addressId、 isDefault三个字段）
     *
     * @param deafoultAddressId 地址ID
     * @param isDefault “1”为默认
     */

    private void doModifyDefaultAddr(final String deafoultAddressId, String isDefault) {

        if (AppContext.userId == null) {
            return;
        }
        RequstClient.postModifyAddress(AppContext.userId, "", "", "", "", "",
                "", "", addressId, isDefault, "", new CustomResponseHandler(
                        this) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String content) {
                        super.onSuccess(statusCode, headers, content);
                        LogUtil.i(TAG, "postModifyAddress:" + content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            if (!obj.getString("type").equals("1")) {
                                String errorMsg = obj.getString("msg");
                                Toast.makeText(getBaseContext(), errorMsg,
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(getBaseContext(), "默认地址修改成功",
                                    Toast.LENGTH_SHORT).show();
                            setDeafultAddressId(deafoultAddressId);
                            refreshList();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    class AddressAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public AddressAdapter(AddressBean mAddressBean) {
            this.inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mAddressBean.shippingAddress == null ? 0
                    : mAddressBean.shippingAddress.size();
        }

        @Override
        public Object getItem(int position) {
            return mAddressBean.shippingAddress.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mAddressBean.shippingAddress.get(position).hashCode();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(
                        R.layout.address_management_listitem, null);
                mViewHolder = new ViewHolder();
                mViewHolder.addr_username = (TextView) convertView
                        .findViewById(R.id.addr_username);
                mViewHolder.addr_user_phone = (TextView) convertView
                        .findViewById(R.id.addr_user_phone);
                mViewHolder.addr_address_info = (TextView) convertView
                        .findViewById(R.id.addr_address_info);
                mViewHolder.addr_modify = (ImageView) convertView
                        .findViewById(R.id.addr_modify);
                mViewHolder.addr_delete = (ImageView) convertView
                        .findViewById(R.id.addr_delete);
                mViewHolder.addr_default_tv = (TextView) convertView
                        .findViewById(R.id.addr_default_tv);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            final AddressItem item = mAddressBean.shippingAddress.get(position);

            if (item.IS_DEFAULT == null) {
                mViewHolder.addr_default_tv.setVisibility(View.GONE);
            } else if (item.IS_DEFAULT.equals("1")) {
                mViewHolder.addr_default_tv.setVisibility(View.VISIBLE);
            } else {
                mViewHolder.addr_default_tv.setVisibility(View.GONE);
            }
            mViewHolder.addr_modify.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent();
                    i.setClass(AddressManagementActivity.this,
                            AddressModifyActivity.class);
                    i.putExtra(AddressModifyActivity.TAG,
                            AddressModifyActivity.MODIFY);
                    // add by zhyao @2015/6/25 添加商品全部是消费卷
                    i.putExtra("isAllConsumption", isAllConsumption);
                    i.putExtra("addressItem", item);
                    startActivityForResult(i, request_modify_add);
                }

            });
            mViewHolder.addr_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    addressId = item.ADDRESSID;
                    Intent i = new Intent(AddressManagementActivity.this,
                            CommonConfrimCancelDialog.class);
                    i.putExtra(CommonConfrimCancelDialog.TASK,
                            CommonConfrimCancelDialog.TASK_DELETE_ADDR);
                    startActivityForResult(i, request_delete);
                }

            });

            mViewHolder.addr_username.setText(item.CONSIGNEE_NAME);
            mViewHolder.addr_user_phone.setText(item.CONSIGNEE_PHONE);
            // add by zhyao @2015/6/24 判断地址是否为空
            if(TextUtils.isEmpty(item.LEVELADDR) || TextUtils.isEmpty(item.ADDRESS)) {
            	 mViewHolder.addr_address_info
                 .setText("");
            }
            else {
            	mViewHolder.addr_address_info
            	.setText(item.LEVELADDR + item.ADDRESS);
            }

            return convertView;
        }

        class ViewHolder {
            TextView addr_username;
            TextView addr_user_phone;
            TextView addr_address_info;
            ImageView addr_modify;
            ImageView addr_delete;
            TextView addr_default_tv;
        }
    }

    public class AddressBean {
        public ArrayList<AddressItem> shippingAddress = new ArrayList<AddressItem>();
    }

    // add by zhyao @2015/6/24 添加是不是全部消费卷参数
    private void rebulidFreight(String province, String city, String area,
                                String shopIds, String orderPrice, String freight, boolean isAllConsumption) {
        if (AppContext.userId != null) {
            RequstClient.freightByAddres(AppContext.userId, province, city,
                    area, shopIds, orderPrice, freight, isAllConsumption,
                    new CustomResponseHandler(this) {
                        @Override
                        public void onSuccess(String content) {
                            super.onSuccess(content);
                            if (LogUtil.isDebug) {
                                Logger.json(content);
                            }
                            try {
                                JSONObject obj = new JSONObject(content);
                                if (!obj.getString("type").equals("1")) {
                                    String errorMsg = obj.getString("msg");
                                    Toast.makeText(getBaseContext(), errorMsg,
                                            Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Intent i = new Intent();
                                i.putExtra("addr", item);
                                i.putExtra("orderPriceStr",
                                        obj.optDouble("orderPriceStr"));
                                i.putExtra("goodsSumfreightStr",
                                        obj.optDouble("goodsSumfreightStr"));
                                setResult(RESULT_OK, i);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    private void isNeedSelectOtherAddress(String currentAddressId) {
        if (addressId.equals(currentAddressId)) {
            //当前地址ID是即将要删除的ID
            selectedAddressHasDel = true;
        }
    }

    private void setDeafultAddressId(String addressid){
        for (int i = 0; i < mAddressBean.shippingAddress.size(); i++) {
            if (addressid.equals(mAddressBean.shippingAddress.get(i).ADDRESSID)){
                mAddressBean.shippingAddress.get(i).IS_DEFAULT="1" ;
            }else{
                mAddressBean.shippingAddress.get(i).IS_DEFAULT="0" ;
            }
        }
    }
}
