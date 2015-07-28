package com.huiyin.ui.user.asset;

import org.apache.http.Header;

import android.os.Bundle;

import com.huiyin.R;
import com.huiyin.adapter.LehuTicketAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.TicketDataBean;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

/**
 * 乐虎红包-过期券
 * @author 刘远祺
 *
 */
public class TicketOverdueActivity extends BaseActivity{

	/**乐虎券适配器**/
	private LehuTicketAdapter adapter = null;
	
	/**乐虎券ListView**/
	private XListView lehuListView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lehu_overdue);
		setTitle("即将过期");
		
		//初始化控件
		initView();
		
		//获取数据
		getLeHuList(adapter.tickData.pageIndex, false);
	}
	
	/**
	 * 初始化控件
	 */
	private void initView(){
		
		lehuListView = (XListView)findViewById(R.id.data_listview);
		lehuListView = (XListView)findViewById(R.id.data_listview);
    	lehuListView.setPullLoadEnable(false);
    	lehuListView.setFooterDividersEnabled(false);
    	lehuListView.setPullRefreshEnable(true);
		
		
    	adapter = new LehuTicketAdapter(mContext);
    	lehuListView.setAdapter(adapter);
    	
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
     * 获取乐虎券
     * @param status 0即将过期  1全部
     * @param pageIndex 页索引 从1开始
     */
    private void getLeHuList(int pageIndex, final boolean isRefresh){
    	RequstClient.ticketData(pageIndex, 2, new CustomResponseHandler(mContext, true) {
			
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
				} else {

					if (tickData.hasMoreData()) {
						 lehuListView.setPullLoadEnable(true);
						 lehuListView.showFooter();
                    } else {
                   	 lehuListView.setPullLoadEnable(false);
                   	 lehuListView.hideFooter();
                    }
				}
				
				
				if(isRefresh){
					adapter.refreshData(tickData);
				}else{
					adapter.appendData(tickData);
				}
			}
		});
    }
}
