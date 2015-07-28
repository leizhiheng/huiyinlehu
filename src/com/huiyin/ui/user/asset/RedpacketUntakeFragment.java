package com.huiyin.ui.user.asset;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.huiyin.R;
import com.huiyin.adapter.LehuTicketAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.TicketDataBean;
import com.huiyin.bean.TicketDataBean.Ticket;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

/**
 * 乐虎红包券-待领取
 *
 */
public class RedpacketUntakeFragment extends BaseFragment implements OnItemClickListener {


	/**乐虎券适配器**/
	private LehuTicketAdapter adapter = null;
	
	/**乐虎券ListView**/
	private XListView lehuListView;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_redpacket_untake, null);
        return layoutView;
    }

   
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	//初始化控件
    	initView();

    	//初始化数据
    	initData();
    	
    }
   
    /**
     * 初始化控件
     */
    private void initView(){
    	
    	lehuListView = (XListView)findViewById(R.id.data_listview);
    	lehuListView.setPullLoadEnable(false);
    	lehuListView.setFooterDividersEnabled(false);
    	lehuListView.setPullRefreshEnable(true);
    	
    	
    	adapter = new LehuTicketAdapter(mContext);
    	lehuListView.setAdapter(adapter);
    	lehuListView.setOnItemClickListener(this);
    	
    	lehuListView.setXListViewListener(new IXListViewListener() {
            @Override
            public void onRefresh() {
                
                adapter.tickData.pageIndex = 1;
                getLeHuList(adapter.tickData.pageIndex, true);
            }

            @Override
            public void onLoadMore() {
                getLeHuList((adapter.tickData.pageIndex+1), false);
            }
        });
    }
    
    /**
     * 初始化数据
     */
    private void initData(){
    	
    	getLeHuList(adapter.tickData.pageIndex, false);
    }
    
    /**
     * 获取乐虎券
     * @param status 0即将过期  1全部
     * @param pageIndex 页索引 从1开始
     */
    private void getLeHuList(int pageIndex, final boolean isRefresh){
    	RequstClient.ticketData(pageIndex, 3, new CustomResponseHandler(mContext, true) {
			
    		
    		@Override
            public void onStart() {
                super.onStart();
                lehuListView.stopLoadMore();
                lehuListView.stopRefresh();
            }
    		
    		@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				
				//异常消息显示
				if(JSONParseUtils.isErrorJSONResult(content)){
					String msg = JSONParseUtils.getString(content, "msg");
					showMyToast(msg);
					
					//隐藏加载更多
					lehuListView.stopLoadMore();
	                lehuListView.stopRefresh();
					return;
				}
				
				//解析数据
				TicketDataBean tickData = TicketDataBean.explainJson(content, mContext);
				if(null == tickData){
					return;
				}else{
					
					if (tickData.hasMoreData()) {
						 lehuListView.setPullLoadEnable(true);
						 lehuListView.showFooter();
                    } else {
                   	 lehuListView.setPullLoadEnable(false);
                   	 lehuListView.hideFooter();
                    }
				}
				
				//刷新列表数据
				if(isRefresh){
					adapter.refreshData(tickData);
				}else{
					adapter.appendData(tickData);
				}
				
			}
			
		});
    }


	/**
	 * 领取乐虎券
	 * @param acitveId
	 */
	private void getLHTicket(final Ticket ticket){
		
		if(null == ticket){
			return;
		}
		
		RequstClient.getLHTicket(ticket.ACTIVE_ID, new CustomResponseHandler(mContext, true) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				
				//异常消息显示
				if(JSONParseUtils.isErrorJSONResult(content)){
					String msg = JSONParseUtils.getString(content, "msg");
					showMyToast(msg);
					return;
				}
				
				showMyToast("领取成功");
				
				//通知未使用的界面刷新界面
				((LehuRedPacketActivity)mContext).tagRefreshUnuse(true);
				
				adapter.removeTicket(ticket);
			}
			
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		int headCount = lehuListView.getHeaderViewsCount();
		position -= headCount;
		
		Ticket ticket = (Ticket)adapter.getItem(position);
		getLHTicket(ticket);
		
	}
}
