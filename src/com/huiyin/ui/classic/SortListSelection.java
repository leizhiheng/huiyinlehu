package com.huiyin.ui.classic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.custom.vg.list.CustomListView;
import com.custom.vg.list.OnItemClickListener;
import com.custom.vg.list.OnItemLongClickListener;
import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.adapter.AttributeListNameAdapter;
import com.huiyin.adapter.SelecteAttributeAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.Attribute;
import com.huiyin.bean.CommodityTypeName;
import com.huiyin.bean.CommodityTypeName.TypeList;
import com.huiyin.bean.CommodityTypeName.TypeName;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class SortListSelection extends BaseActivity implements OnClickListener {
    private static final String TAG = "SortListSelection";
    private TextView tv_select_title;// 页面的标题
    private TextView tv_commit;// 提交
    private LinearLayout delete_all;// 清除全部
    private CustomListView clv_select;// 已选择的参数
    private String attributeId;// 三级类目id
    private ArrayList<Attribute> attributes = new ArrayList<Attribute>();// 已选的属性列表数据
    private CommodityTypeName commodityTypeName;// 商品属性列表数据
    private SelecteAttributeAdapter adapter;

    private LinearLayout layout_back;// 返回按钮
    private LinearLayout layout_main;// 主体布局
    private ArrayList<AttributeListNameAdapter> adapters;
    private boolean isFirst = true;//是否第一次加载


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_selection);
//		attributeId = getIntent().getStringExtra("category_id");
        attributeId = "151";
//        LogUtil.i(TAG, "属性筛选" + attributeId);
        findView();
        setListener();
        initData();
    }

    /**
     * 初始化控件
     */
    private void findView() {
        clv_select = (CustomListView) findViewById(R.id.clv_select);
        tv_select_title = (TextView) findViewById(R.id.tv_select_title);
        tv_commit = (TextView) findViewById(R.id.tv_commit);
//		tv_back = (TextView) findViewById(R.id.tv_back);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
        layout_main = (LinearLayout) findViewById(R.id.layout_main);
        delete_all = (LinearLayout) findViewById(R.id.delete_all);
    }

    /**
     * 添加监听
     */
    public void setListener() {
        tv_commit.setOnClickListener(this);
        layout_back.setOnClickListener(this);
        delete_all.setOnClickListener(this);
        delete_all.setOnClickListener(this);
        delete_all.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        adapter = new SelecteAttributeAdapter(mContext, attributes);
        adapter.setOnItemDeleteListner(new SelecteAttributeAdapter.OnItemDeleteListner() {

            @Override
            public void onItemDelete(int position) {
                // TODO Auto-generated method stub
                Attribute attr = attributes.get(position);
                attributes.remove(position);
                String parentName = attr.partnerName;
                List<TypeList> lists = commodityTypeName.attributeList;
                for (int i = 0; i < lists.size(); i++) {
                    if (parentName.equals(lists.get(i).NAME)) {// 匹配父属性
                        ArrayList<TypeName> VALUE_LIST = lists.get(i).VALUE_LIST;
                        for (int j = 0; j < VALUE_LIST.size(); j++) {
                            if (attr.value.id.equals(VALUE_LIST.get(j).ID)) {// 将该属性的匹配子选项设置为未选中
                                VALUE_LIST.get(j).setSelected(false);
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();// 刷新ui
                refreshAll();
            }
        });
        clv_select.setDividerHeight(16);
        clv_select.setDividerWidth(12);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 10, 20, 10);
        clv_select.setLayoutParams(params);
        clv_select.setAdapter(adapter);
        clv_select.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            }
        });
        clv_select.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // 不做任何处理
                return true;
            }
        });
    }

    public void refreshAll() {
        for (int i = 0; i < adapters.size(); i++) {
            adapters.get(i).notifyDataSetChanged();
        }
    }


    /**
     * // 将选中的属性加入已选属性列表
     *
     * @param positionPop 属性位置
     * @param type        属性对象
     */
    public void addAttribute(final int positionPop, TypeName type) {
        Attribute bean = new Attribute();
        String partnerId = commodityTypeName.attributeList.get(positionPop).ID;
        String partnerName = commodityTypeName.attributeList.get(positionPop).NAME;
        for (int i = 0; i < attributes.size(); i++) {
            if (type.ID.equals(attributes.get(i).value.id)) {//匹配id
                attributes.remove(i);
            }
        }
        if (type.isSelected()) {//选中了
            bean.partnerId = partnerId;
            bean.partnerName = partnerName;
            bean.value = bean.new AttributeValue();
            bean.value.id = type.ID;
            bean.value.name = type.NAME;
            attributes.add(bean);// 将选中的属性加入已选中的列表list里面
        }
        if (isFirst) {
            initView();
            isFirst = false;
        } else {
            adapter.setAttributes(attributes);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_commit:// 提交
                Intent intent = new Intent(mContext, CategorySearchActivity.class);
                String BRAN_ID = "";// 品牌
                StringBuffer properties = new StringBuffer();// 属性id,多个属性以逗号隔开
                StringBuffer propertiesBRAN_ID = new StringBuffer();// 品牌id,多个属性以逗号隔开
//                String priceStart = "";// 价格起始值
//                String priceEnd = "";// 价格结束值
                String propertyId = "";
                for (int i = 0; i < attributes.size(); i++) {
                    if ("-1".equals(attributes.get(i).partnerId)) {// 品牌
                        propertiesBRAN_ID.append(attributes.get(i).value.id + ",");
                        BRAN_ID = propertiesBRAN_ID.substring(0, propertiesBRAN_ID.length() - 1);
                    } else {
                        properties.append(attributes.get(i).value.id + ",");
                        propertyId = properties.substring(0, properties.length() - 1);
                    }
                }
                intent.putExtra(CategorySearchActivity.BUNDLE_BRAND_ID, BRAN_ID);
                intent.putExtra(CategorySearchActivity.BUNDLE_PROPERTY_ID, propertyId);
//                intent.putExtra(CategorySearchActivity.BUNDLE_PRICE_START, priceStart);
//                intent.putExtra(CategorySearchActivity.BUNDLE_PRICE_END, priceEnd);
                intent.putExtra(CategorySearchActivity.BUNDLE_MARK, "4");
                this.startActivity(intent);
                this.finish();
                break;
            case R.id.layout_back:// 返回
                this.finish();
                break;
            case R.id.delete_all:// 清除历史搜索
                attributes.removeAll(attributes);
                List<TypeList> lists = commodityTypeName.attributeList;
                for (int i = 0; i < lists.size(); i++) {
                    ArrayList<TypeName> VALUE_LIST = lists.get(i).VALUE_LIST;
                    for (int j = 0; j < VALUE_LIST.size(); j++) {// 将该属性的所有子选项设置为未选中
                        VALUE_LIST.get(j).setSelected(false);
                    }
                }
                adapter.notifyDataSetChanged();// 刷新ui
                refreshAll();
                break;
        }
    }

    /**
     * 将光标置于文本之后
     *
     * @param editText
     */
    public void setEditTextCursorLocation(EditText editText) {
        CharSequence text = editText.getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) text;
            Selection.setSelection(spannable, text.length());
        }
    }

    /**
     * 初始化属性列表
     */
    public void initAttribute() {
        adapters = new ArrayList<AttributeListNameAdapter>();
        for (int i = 0; i < commodityTypeName.attributeList.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.attribut_name_list_item, null);
            TextView tv_attribute = (TextView) view.findViewById(R.id.tv_attribute);
            RelativeLayout layout_attribute = (RelativeLayout) view.findViewById(R.id.layout_attribute);
            final ImageView iv_up_down = (ImageView) view.findViewById(R.id.iv_up_down);
            final CustomListView clv_select_list = (CustomListView) view.findViewById(R.id.clv_select);
            final ArrayList<TypeName> data = commodityTypeName.attributeList.get(i).VALUE_LIST;
            final AttributeListNameAdapter adapterItem = new AttributeListNameAdapter(mContext, data);
            adapters.add(adapterItem);
            clv_select_list.setTag(i);
            clv_select_list.setDividerHeight(16);
            clv_select_list.setDividerWidth(12);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 10, 20, 10);
            clv_select_list.setLayoutParams(params);
            clv_select_list.setAdapter(adapterItem);
            clv_select_list.measure(0, 0);
            clv_select_list.setVisibility(View.GONE);
            iv_up_down.setBackgroundResource(R.drawable.arraw_up);
            clv_select_list.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    //选中当前的
                    data.get(position).setSelected(!data.get(position).isSelected());
                    adapterItem.notifyDataSetChanged();
                    addAttribute((Integer) clv_select_list.getTag(), data.get(position));
                }
            });
            clv_select_list.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // 不做任何处理
                    return true;
                }
            });
            tv_attribute.setText(commodityTypeName.attributeList.get(i).NAME);
            layout_attribute.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (View.VISIBLE == clv_select_list.getVisibility()) {//如果
                        clv_select_list.setVisibility(View.GONE);
                        iv_up_down.setBackgroundResource(R.drawable.arraw_up);
                    } else {
                        clv_select_list.setVisibility(View.VISIBLE);
                        iv_up_down.setBackgroundResource(R.drawable.arraw_next);
                    }
                }
            });
            layout_main.addView(view);
        }
    }

    /**
     * 初始化商品属性列表
     */
    public void initData() {
        RequstClient.selecteAttributes(attributeId, new CustomResponseHandler(this) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, content);

                try {

                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {// 数据错误
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(SortListSelection.this, errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    commodityTypeName = new Gson().fromJson(content, CommodityTypeName.class);
                    initAttribute();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }
}
