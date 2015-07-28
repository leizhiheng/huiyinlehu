package com.view.swipe.adapters;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.huiyin.R;
import com.view.swipe.SimpleSwipeListener;
import com.view.swipe.SwipeLayout;
import com.view.swipe.implments.SwipeItemMangerImpl;
import com.view.swipe.interfaces.SwipeAdapterInterface;
import com.view.swipe.interfaces.SwipeItemMangerInterface;

public abstract class BaseSwipeAdapter extends BaseAdapter implements SwipeItemMangerInterface, SwipeAdapterInterface {

    protected SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

    protected boolean mSwipeEnabled = true;
    
    /**
     * return the {@link com.daimajia.swipe.SwipeLayout} resource id, int the view item.
     * @param position
     * @return
     */
    public abstract int getSwipeLayoutResourceId(int position);

    /**
     * generate a new view item.
     * Never bind SwipeListener or fill values here, every item has a chance to fill value or bind
     * listeners in fillValues.
     * to fill it in {@code fillValues} method.
     * @param position
     * @param parent
     * @return
     */
    public abstract View generateView(int position, ViewGroup parent);

    /**
     * fill values or bind listeners to the view.
     * @param position
     * @param convertView
     */
    public abstract void fillValues(int position, View convertView);


    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            v = generateView(position, parent);
            init(v, position);
            
            mItemManger.initialize(v, position);
        }else{
            mItemManger.updateConvertView(v, position);
        }
        
        //填充数据
        fillValues(position, v);
        
        //设置tag(删除按钮的tag)
        v.findViewById(R.id.ll_menu).setTag(position);
        
        return v;
    }

    private void init(View convertView, int position){
    	final SwipeLayout swipeLayout = (SwipeLayout) convertView
				.findViewById(getSwipeLayoutResourceId(position));
		// 当隐藏的删除menu被打开的时候的回调函数
		swipeLayout.addSwipeListener(new SimpleSwipeListener() {
			@Override
			public void onOpen(SwipeLayout layout) {
				//Toast.makeText(context, "Open", Toast.LENGTH_SHORT).show();
			}
		});
		// 双击的回调函数
		swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
					@Override
					public void onDoubleClick(SwipeLayout layout,
							boolean surface) {
//						Toast.makeText(context, "DoubleClick",
//								Toast.LENGTH_SHORT).show();
					}
				});
		
		// 设置是否可以侧滑
		swipeLayout.setSwipeEnabled(mSwipeEnabled);
		
		// 添加删除布局的点击事件
		convertView.findViewById(R.id.ll_menu).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
				// 点击完成之后，关闭删除menu
				swipeLayout.close();
				
				//通知做了删除处理
				int position = Integer.parseInt(arg0.getTag().toString().trim());
				onDeleteClick(position);
			}
		});
    }
    
    /**
     * 做了删除操作
     * @param position
     */
    protected void onDeleteClick(int position){
    	
    }
    
    @Override
    public void openItem(int position) {
        mItemManger.openItem(position);
    }

    @Override
    public void closeItem(int position) {
        mItemManger.closeItem(position);
    }

    @Override
    public void closeAllExcept(SwipeLayout layout) {
        mItemManger.closeAllExcept(layout);
    }

    @Override
    public List<Integer> getOpenItems() {
        return mItemManger.getOpenItems();
    }

    @Override
    public List<SwipeLayout> getOpenLayouts() {
        return mItemManger.getOpenLayouts();
    }

    @Override
    public void removeShownLayouts(SwipeLayout layout) {
        mItemManger.removeShownLayouts(layout);
    }

    @Override
    public boolean isOpen(int position) {
        return mItemManger.isOpen(position);
    }

    @Override
    public SwipeItemMangerImpl.Mode getMode() {
        return mItemManger.getMode();
    }

    @Override
    public void setMode(SwipeItemMangerImpl.Mode mode) {
        mItemManger.setMode(mode);
    }
    
    /**
     * 设置ItemView是否可以侧滑删除，该方法必须在setAdapter之前调用
     * @param enabled
     */
    public void setSwipeEnabled(boolean enabled) {
		mSwipeEnabled = enabled;
	}
}
