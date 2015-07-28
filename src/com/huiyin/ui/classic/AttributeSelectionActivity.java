package com.huiyin.ui.classic;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.adapter.AttributeAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.Attribute;
import com.huiyin.bean.CommodityTypeName;
import com.huiyin.bean.CommodityTypeName.TypeList;
import com.huiyin.bean.CommodityTypeName.TypeName;
import com.huiyin.wight.tagview.OnTagDeleteListener;
import com.huiyin.wight.tagview.Tag;
import com.huiyin.wight.tagview.TagView;

@SuppressLint("ResourceAsColor")
public class AttributeSelectionActivity extends BaseActivity implements OnClickListener {
    
    private TextView tv_commit;// 提交
    private LinearLayout delete_all;// 清除全部
    private String attributeId;// 三级类目id
    private ArrayList<Attribute> attributes = new ArrayList<Attribute>();// 已选的属性列表数据
    private CommodityTypeName commodityTypeName;// 商品属性列表数据

    private LinearLayout layout_back;// 返回按钮

    /**顶部云标签控件(显示已选中的数据)**/
    private TagView tagView;

    /**属性列表**/
    private ExpandableListView lv_attribute;
    
    /**属性适配器**/
    private AttributeAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_attribute_selection);
        
		attributeId = getIntent().getStringExtra("category_id");
//        attributeId = "151";
        
        //初始化控件
        findView();
        
        setListener();
        initData();
    }

    /**
     * 初始化控件
     */
    private void findView() {
    	
    	
    	View headView = LayoutInflater.from(mContext).inflate(R.layout.layout_attribute_selection_headview, null);
    	tagView = (TagView) headView.findViewById(R.id.tagview);
    	delete_all = (LinearLayout) headView.findViewById(R.id.delete_all);
    	
    	lv_attribute = (ExpandableListView) findViewById(R.id.lv_attribute);
    	lv_attribute.addHeaderView(headView);
        
    	adapter = new AttributeAdapter(mContext, null);
        lv_attribute.setAdapter(adapter);
        
        
        tv_commit = (TextView) findViewById(R.id.tv_commit);
        layout_back = (LinearLayout) findViewById(R.id.layout_back);
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
     * 绘头部tagView
     */
    private void drawHeadTagView(){
        if(null!=attributes&&attributes.size()!=0){
        	
        	//清空所有数据
        	tagView.clearAllData();
        	
        	//重新构建tag
        	for (int i=0;i<attributes.size();i++){
                Tag tag=new Tag(attributes.get(i).value.name);
                addTag(tag);
            }
            tagView.setOnTagDeleteListener(new OnTagDeleteListener() {
                @Override
                public void onTagDeleted(Tag tag, int position) {
                    String parentName = attributes.get(position).partnerName;
                    List<TypeList> lists = commodityTypeName.attributeList;
                    for (int i = 0; i < lists.size(); i++) {
                        if (parentName.equals(lists.get(i).NAME)) {// 匹配父属性
                            ArrayList<TypeName> VALUE_LIST = lists.get(i).VALUE_LIST;
                            for (int j = 0; j < VALUE_LIST.size(); j++) {
                                if (attributes.get(position).value.id.equals(VALUE_LIST.get(j).ID)) {// 将该属性的匹配子选项设置为未选中
                                    VALUE_LIST.get(j).setSelected(false);
                                }
                            }
                        }
                    }
                    
                    //刷新数据
                    adapter.notifyDataSetChanged();
                    
                    //删除已选的数据
                    attributes.remove(position);
                }
            });
            
            //重画数据
            tagView.drawTags();
        }
    }


    /**
     * 把tag加入到tagview
     * @param tag
     */
    private void addTag(Tag tag){
        tag.layoutColor= getResources().getColor(R.color.index_red);
        tag.layoutColorPress= getResources().getColor(R.color.index_red);
        tag.deleteIcon="X";
        tag.tagTextColor= getResources().getColor(R.color.white);
        tag.radius=10;
        tagView.add(tag);
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_commit:// 提交
            	
            	if(null == attributes || attributes.size() == 0){
            		showMyToast("请添加筛选条件");
            		return;
            	}
            	
                Intent intent = new Intent(mContext, CategorySearchActivity.class);
                String BRAN_ID = "";// 品牌
                StringBuffer properties = new StringBuffer();// 属性id,多个属性以逗号隔开
                StringBuffer propertiesBRAN_ID = new StringBuffer();// 品牌id,多个属性以逗号隔开
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
                
                //解决bug，从筛选里面进去，继续点击筛选报参数错误的问题 add by liuyuanqi 20150707
                intent.putExtra(CategorySearchActivity.BUNDLE_KEY_CATEGORY_ID, attributeId);
                
                //intent.putExtra(CategorySearchActivity.BUNDLE_PRICE_START, priceStart);
                //intent.putExtra(CategorySearchActivity.BUNDLE_PRICE_END, priceEnd);
                intent.putExtra(CategorySearchActivity.BUNDLE_MARK, "4");
                startActivity(intent);
                
                
                this.finish();
                break;
            case R.id.layout_back:// 返回
                this.finish();
                break;
            case R.id.delete_all:
            	
            	// 清除历史搜索
            	clearAllCheckedData();
            	
                break;
        }
    }

    /**
     * 清空所有被选中的数据
     */
    private void clearAllCheckedData(){
    	
    	//头部选中的数据清空
    	attributes.clear();
    	tagView.clearAllData();
    	tagView.drawTags();
    	
    	//列表数据选中的清空
    	adapter.clearChecked();
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
                        Toast.makeText(AttributeSelectionActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    
                    //绑定数据
                    commodityTypeName = new Gson().fromJson(content, CommodityTypeName.class);
                    adapter.refreshData(commodityTypeName);
                    adapter.setOnAttributeSelectedResultListener(new AttributeAdapter.OnAttributeSelectedResultListener() {

                        @Override
                        public void returnAttribute(Attribute attribute, boolean isAdd) {
                            if(isAdd){
                                attributes.add(attribute);
                            }else{
                                //删除已选中的属性
                                int index=0;
                                for(int i=0;i<attributes.size();i++){
                                    Attribute attr=attributes.get(i);
                                    if(attr.partnerId.equals(attribute.partnerId)&&attr.value.id.equals(attribute.value.id)){
                                        index=i;
                                    }
                                }
                                attributes.remove(index);
                            }
                            drawHeadTagView();
                        }
                    });
                    
                    drawHeadTagView();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
    }
}
