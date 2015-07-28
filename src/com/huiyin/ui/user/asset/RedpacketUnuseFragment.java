package com.huiyin.ui.user.asset;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.LehuTicketAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.TicketDataBean;
import com.huiyin.ui.lehuvoucher.LehuVoucherActivity;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.StringUtils;
import com.huiyin.wight.XListView;
import com.huiyin.wight.XListView.IXListViewListener;

/**
 * 乐虎红包券-未使用
 *
 */
public class RedpacketUnuseFragment extends BaseFragment implements OnClickListener {

	/**乐虎券编号**/
	private EditText lehucodeEditText;

	/**即将过期优惠券**/
	private TextView couponoutTextview;
	
	/**乐虎券适配器**/
	private LehuTicketAdapter adapter = null;
	
	/**乐虎券ListView**/
	private XListView lehuListView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.fragment_redpacket_unuse, null);
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
     * 重新刷新数据
     */
    public void refreshData(){
    	
    	//显示了，并且
    	if(isAdded()){
	    	adapter.tickData.pageIndex = 1;
	        getLeHuList(adapter.tickData.pageIndex, true);
    	}
    }
    
    /**
     * 初始化控件
     */
    private void initView(){
    	
    	lehuListView = (XListView)findViewById(R.id.data_listview);
    	lehuListView.setPullLoadEnable(false);
    	lehuListView.setFooterDividersEnabled(false);
    	lehuListView.setPullRefreshEnable(true);
    	lehuListView.setXListViewListener(new IXListViewListener() {
             @Override
             public void onRefresh() {
            	 
            	 //重新刷新数据
            	 refreshData();
             }

             @Override
             public void onLoadMore() {
                 getLeHuList((adapter.tickData.pageIndex+1), false);
             }
         });
    	
    	adapter = new LehuTicketAdapter(mContext, true);
    	lehuListView.setAdapter(adapter);
    	
    	
    	View headView  = LayoutInflater.from(mContext).inflate(R.layout.fragment_redpacket_unuse_headview, null);
    	couponoutTextview = (TextView)headView.findViewById(R.id.couponout_textview);
    	lehucodeEditText = (EditText)headView.findViewById(R.id.lehu_code_edittext);
    	
    	//上传乐虎券，即将过期乐虎券，激活按钮
    	headView.findViewById(R.id.upload_lehu_ticket_layout).setOnClickListener(this);
    	headView.findViewById(R.id.timeout_ticket_layout).setOnClickListener(this);
    	headView.findViewById(R.id.activate_button).setOnClickListener(this);
    	lehuListView.addHeaderView(headView);
    }
    
    /**
     * 初始化数据
     */
    private void initData(){
    	
    	//加载乐虎券数据
    	getLeHuList(adapter.tickData.pageIndex, false);
    }

    /**
     * 激活乐虎券
     */
    private void activateLehuQuan(){
    	
    	//非空判断
    	String code = getText(lehucodeEditText);
		if (StringUtils.isBlank(code)) {
			showMyToast("请输入乐虎券编号");
			return;
		}
		
		//乐虎券校验
		RequstClient.postCodeValidate(AppContext.userId, code, new MyCustomResponseHandler(mContext, true) {

			@Override
			public void onRefreshData(String content) {

				int type = JSONParseUtils.getInt(content, "type");
				if (type > 0) {
					
					lehucodeEditText.setText("");
					showMyToast("乐虎券激活成功!");
					
					//重新刷新数据--需要重新定位
					getLeHuList(0, true);
				} else {
					
					String msg = JSONParseUtils.getString(content, "msg");
					Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
				}
			}

		});
    }
    
    /**
     * 获取乐虎券
     * @param status 0即将过期  1全部
     * @param pageIndex 页索引 从1开始
     */
    private void getLeHuList(int pageIndex, final boolean isRefresh){
    	RequstClient.ticketData(pageIndex, 1, new CustomResponseHandler(mContext, true) {
			
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
				
				//刷新过期数
				couponoutTextview.setText(tickData.myCouponOut+"张");
				
				//刷新列表数据
				if(isRefresh){
					adapter.refreshData(tickData);
				}else{
					adapter.appendData(tickData);
				}
				
			}
			
		});
    }
    
    
    
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.upload_lehu_ticket_layout:
			
			//上传发票
			startActivity(LehuVoucherActivity.class);
			
			break;

		case R.id.timeout_ticket_layout:
			
			//过期优惠券
			startActivity(TicketOverdueActivity.class);
			
			break;
		case R.id.activate_button:
			
			//激活乐虎券
			activateLehuQuan();
			
			break;
		}
	}
}
