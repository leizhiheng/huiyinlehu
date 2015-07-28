package com.huiyin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.Attribute;
import com.huiyin.bean.CommodityTypeName;
import com.huiyin.bean.CommodityTypeName.TypeList;
import com.huiyin.bean.CommodityTypeName.TypeName;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.tagview.OnTagClickListener;
import com.huiyin.wight.tagview.Tag;
import com.huiyin.wight.tagview.TagView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuangyong on 2015/6/17.
 */
public class AttributeAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<CommodityTypeName.TypeList> attributeList;

    public AttributeAdapter(Context context,CommodityTypeName commodityTypeName){
        this.context=context;
        if(null!=commodityTypeName){
            attributeList=commodityTypeName.attributeList;
        }
    }

    /**
     * 刷新数据
     */
    public void refreshData(CommodityTypeName commodityTypeName){
    	 if(null!=commodityTypeName){
            
    		 attributeList=commodityTypeName.attributeList;
             notifyDataSetChanged();
         }
    }
    
    @Override
    public int getGroupCount() {
        return (null==attributeList||attributeList.size()==0)?0:attributeList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int count = attributeList.get(groupPosition).VALUE_LIST.size();
        LogUtil.i("count", count + "");
        LogUtil.i("groupPosition",groupPosition+"");
        return count==0?0:1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return attributeList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
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
        MyHolderParent myHolderParent=null;
        if(null==convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.attribut_pitem, null);
            myHolderParent =new MyHolderParent();
            myHolderParent.tv_attribute=(TextView)convertView.findViewById(R.id.tv_attribute);
            myHolderParent.iv_up_down=(ImageView)convertView.findViewById(R.id.iv_up_down);
            myHolderParent.line=(View)convertView.findViewById(R.id.line);
            convertView.setTag(myHolderParent);
        }else{
            myHolderParent=(MyHolderParent)convertView.getTag();
        }
        if(isExpanded){//展开
            myHolderParent.iv_up_down.setImageResource(R.drawable.arraw_next);
        }else{
            myHolderParent.iv_up_down.setImageResource(R.drawable.arraw_up);
        }
        myHolderParent.tv_attribute.setText(attributeList.get(groupPosition).NAME);//属性名称
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MyHolderChild myHolderChild=null;
        if(null==convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.attribut_citem, null);
            myHolderChild =new MyHolderChild();
            myHolderChild.tagview=(TagView)convertView.findViewById(R.id.tagview);
            convertView.setTag(myHolderChild);
        }else{
            myHolderChild=(MyHolderChild)convertView.getTag();
        }
        final ArrayList<CommodityTypeName.TypeName> value_list=attributeList.get(groupPosition).VALUE_LIST;
       
        myHolderChild.tagview.clearAllData();
        for (int i=0;i<value_list.size();i++){
            addTagView(value_list.get(i),myHolderChild);//将tag加入tagview
        }
        myHolderChild.tagview.setOnTagClickListener(new OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                if(null!=listener){
                    //构造一个回调属性对象
                    Attribute attribute=new Attribute();
                    CommodityTypeName.TypeName type=value_list.get(position);
                    attribute.partnerId=attributeList.get(groupPosition).ID;
                    attribute.partnerName=attributeList.get(groupPosition).NAME;
                    attribute.value.id=type.ID;
                    attribute.value.name=type.NAME;
                    type.setSelected(!type.isSelected());//取反
                    boolean isAdd=type.isSelected();
                    listener.returnAttribute(attribute,isAdd);
                }
                notifyDataSetChanged();
            }
        });
        myHolderChild.tagview.drawTags();
        
        return convertView;
    }

    /**
     * 把tag加入到tagview
     * @param type
     * @param myHolderChild
     */
    private void addTagView(CommodityTypeName.TypeName type,MyHolderChild myHolderChild){
        Tag tag=new Tag(type.NAME, false);
        if(type.isSelected()){//是否选中
            tag.layoutColor= context.getResources().getColor(R.color.index_red);
            tag.layoutColorPress= context.getResources().getColor(R.color.index_red);
            tag.tagTextColor= context.getResources().getColor(R.color.white);
        }else{
            tag.layoutColor= context.getResources().getColor(R.color.white);
            tag.layoutColorPress= context.getResources().getColor(R.color.white);
            tag.tagTextColor= Color.parseColor("#9E9E9E");//灰色
        }
        tag.radius=10;
        myHolderChild.tagview.add(tag);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
    static class MyHolderParent{
        TextView tv_attribute;
        ImageView iv_up_down;
        View line;
    }
    static class MyHolderChild{
        TagView tagview;
    }

    public interface OnAttributeSelectedResultListener{
        /**
         * 反调
         * @param attribute 属性对象
         * @param isAdd 是否加入
         */
        void returnAttribute(Attribute attribute,boolean isAdd);
    }

    private  OnAttributeSelectedResultListener listener;

    /**
     * 设置回调
     * @param listener
     */
    public void setOnAttributeSelectedResultListener(OnAttributeSelectedResultListener listener){
        this.listener=listener;
    }

    /**
     * 清空选中的数据
     */
	public void clearChecked() {

		if(null == attributeList){
			return;
		}
		
		for (int i = 0; i < attributeList.size(); i++) {
			ArrayList<TypeName> VALUE_LIST = attributeList.get(i).VALUE_LIST;
			for (int j = 0; j < VALUE_LIST.size(); j++) {// 将该属性的所有子选项设置为未选中
				VALUE_LIST.get(j).setSelected(false);
			}
		}
		
		//刷新数据
		this.notifyDataSetChanged();
	}
}
