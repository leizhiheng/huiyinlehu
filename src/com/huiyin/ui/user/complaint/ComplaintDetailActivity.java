package com.huiyin.ui.user.complaint;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.adapter.ConplaintMessageAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.MyComplaintDetailBean;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.wight.MyListView;

/**
 * 投诉详情
 * Created by lian on 2015/6/2.
 */
public class ComplaintDetailActivity extends BaseActivity implements View.OnClickListener{

	public static final String TAG = "ComplaintDetailActivity";
	
    public static final String COMPLAIN_ID="complainId";
    private static final int SOLVE_COMPLAINT=1;//投诉解决
    private static final int COMPLAINT_CONTINUE=2;//继续留言
    private static final int CANCEL_COMPLAINT=3;//取消投诉
    private android.widget.TextView btndeal;//已解决
    private android.widget.TextView btncontinuemessage;//继续留言
    private android.widget.TextView btn_cancel;//取消
    private android.widget.TextView tvorderno;//投诉订单
    private android.widget.TextView tvprocess1;//流程1
    private android.widget.TextView tvcircle1;//圆1
    private android.view.View line1;//横线1
    private android.widget.TextView tvdescrib1;//描述1
    private android.widget.TextView tvprocess2;//流程2
    private android.view.View line2;//横线2
    private android.widget.TextView tvcircle2;//圆2
    private android.widget.TextView tvdescrib2;//描述2
    private android.widget.TextView tvprocess3;//流程3
    private android.widget.TextView tvcircle3;//圆3
    private android.view.View line3;//横线3
    private android.widget.TextView tvdescrib3;//描述3
    private com.huiyin.wight.MyListView lvcomment;//留言列表
    private LinearLayout layout_process1;//流程1
    private LinearLayout layout_process2;//流程2
    private LinearLayout layout_process3;//流程3
    private String complainId;//投诉id
    private MyComplaintDetailBean bean;//投诉详情
    private ScrollView middle;//
    private LinearLayout order_detail_bottom_id;//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_complaint_detail);
        complainId=getIntent().getStringExtra(COMPLAIN_ID);
        complainDetail();
        findView();
        setListener();
    }

    public void findView(){
        this.lvcomment = (MyListView) findViewById(R.id.lv_comment);
        this.tvdescrib3 = (TextView) findViewById(R.id.tv_describ3);
        this.line3 =  findViewById(R.id.line3);
        this.tvcircle3 = (TextView) findViewById(R.id.tv_circle3);
        this.tvprocess3 = (TextView) findViewById(R.id.tv_process3);
        this.tvdescrib2 = (TextView) findViewById(R.id.tv_describ2);
        this.tvcircle2 = (TextView) findViewById(R.id.tv_circle2);
        this.line2 =  findViewById(R.id.line2);
        this.tvprocess2 = (TextView) findViewById(R.id.tv_process2);
        this.tvdescrib1 = (TextView) findViewById(R.id.tv_describ1);
        this.line1 =  findViewById(R.id.line1);
        this.tvcircle1 = (TextView) findViewById(R.id.tv_circle1);
        this.tvprocess1 = (TextView) findViewById(R.id.tv_process1);
        this.tvorderno = (TextView) findViewById(R.id.tv_order_no);
        this.btncontinuemessage = (TextView) findViewById(R.id.btn_continue_message);
        this.btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        this.btndeal = (TextView) findViewById(R.id.btn_deal);
        this.middle = (ScrollView) findViewById(R.id.middle);
        this.order_detail_bottom_id = (LinearLayout) findViewById(R.id.order_detail_bottom_id);
        this.layout_process1 = (LinearLayout) findViewById(R.id.layout_process1);
        this.layout_process2 = (LinearLayout) findViewById(R.id.layout_process2);
        this.layout_process3 = (LinearLayout) findViewById(R.id.layout_process3);
    }

    public void setListener(){
        btndeal.setOnClickListener(this);
        btncontinuemessage.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    /**
     * 初始化UI
     */
    public void initView(){
    	
    	//显示流程部分
        tvorderno.setText("投诉订单："+bean.getORDER_CODE());
        tvdescrib1.setText(bean.getCREATEDATE());
        tvdescrib3.setText(bean.getFINISH_TIME());
        
        //显示步骤1流程
        showWitchAndLight(layout_process1,tvcircle1,line1,1);//显示第一个流程
        
        //已取消
        if("1".equals(bean.getCANCEL())){
            tvprocess3.setText("投诉已取消");
            order_detail_bottom_id.setVisibility(View.GONE);
        }
        
        //已关闭
        if("1".equals(bean.getIS_CLOSED())){
            tvprocess3.setText("投诉已关闭");
            order_detail_bottom_id.setVisibility(View.GONE);
        }
    	    
        //已解决
        if(bean.isDeal()){
        	tvprocess3.setText("投诉解决");
            order_detail_bottom_id.setVisibility(View.GONE);
        }
        
        
        if("1".equals(bean.getAUDIT_STATUS())){
        	
        	//等待审核
            if("1".equals(bean.getCANCEL())||"1".equals(bean.getIS_CLOSED())){//已取消/已关闭
                showWitchAndLight(layout_process3,tvcircle3,line3,1);//显示第三个流程
            }else{
                showWitchAndLight(layout_process2,tvcircle2,line2,0);//显示第二个流程
                showWitchAndLight(layout_process3,tvcircle3,line3,0);//显示第三个流程
            }
        }else if("2".equals(bean.getAUDIT_STATUS())){
        	
        	//处理中
            showWitchAndLight(layout_process2,tvcircle2,line2,1);//显示第二个流程
            showWitchAndLight(layout_process3,tvcircle3,line3,0);//显示第三个流程
        }else if("3".equals(bean.getAUDIT_STATUS())){
        	
        	//投诉解决
            showWitchAndLight(layout_process2,tvcircle2,line2,1);//显示第二个流程
            showWitchAndLight(layout_process3,tvcircle3,line3,1);//显示第三个流程
        }

        /**
         * 显示留言部分
         */
        ConplaintMessageAdapter adapter=new ConplaintMessageAdapter(bean.getMessageList(),mContext);
        lvcomment.setAdapter(adapter);
    }

    /**
     *
     * @param layout_process 布局
     * @param tvcircle 圆
     * @param line 线
     * @param color 1.为蓝色 0为灰色
     */
    private void showWitchAndLight(LinearLayout layout_process,TextView tvcircle,View line,int color){
        layout_process.setVisibility(View.VISIBLE);
        if(0==color){//灰色
            tvcircle.setBackgroundResource(R.drawable.bg_radius_count_white);
            tvcircle.setTextColor(getResources().getColor(R.color.complaint_gray));
            line.setBackgroundColor(getResources().getColor(R.color.complaint_gray));
        }else if(1==color){//为蓝色
            tvcircle.setBackgroundResource(R.drawable.bg_radius_count_blue);
            tvcircle.setTextColor(getResources().getColor(R.color.white));
            line.setBackgroundColor(getResources().getColor(R.color.complaint_circle_color_blue));
        }
    }

    /**
     * 获取投诉详情
     */
    private void complainDetail(){
        RequstClient.complainDetail(complainId,new CustomResponseHandler(mContext,true){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bean=new Gson().fromJson(content,MyComplaintDetailBean.class);
                    if(null==bean){
                        showMyToast("网络错误");
                        return;
                    }
                    //显示布局
                    middle.setVisibility(View.VISIBLE);
                    order_detail_bottom_id.setVisibility(View.VISIBLE);
                    initView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_continue_message://继续留言
                Intent intent1 = new Intent(mContext, ContinueMessageActivity.class);
                intent1.putExtra(ContinueMessageActivity.COMPLAINT_ID, complainId);
                mContext.startActivityForResult(intent1, COMPLAINT_CONTINUE);
                break;
            case R.id.btn_cancel://取消
                Intent intent2 = new Intent(mContext, CommonConfrimCancelDialog.class);
                intent2.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CANCEL_COMPLAINT);
                mContext.startActivityForResult(intent2, CANCEL_COMPLAINT);
                break;
            case R.id.btn_deal://已解决
                Intent intent = new Intent(mContext, CommonConfrimCancelDialog.class);
                intent.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_SOLVE_COMPLAINT);
                mContext.startActivityForResult(intent, SOLVE_COMPLAINT);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK==resultCode){
            switch (requestCode){
                case SOLVE_COMPLAINT://投诉解决
                    solveComplain();
                    break;
                case COMPLAINT_CONTINUE://继续留言
                    complainDetail();
                    break;
                case CANCEL_COMPLAINT://取消投诉
                    cancelComplain();
                    break;
            }
        }
    }

    /**
     * 投诉解决
     */
    private void solveComplain(){
    	
    	//数据未请求到，则直接返回
    	if(null == bean){
    		return;
    	}
    	
        RequstClient.solveComplain(complainId, bean.getORDER_CODE(), new CustomResponseHandler(mContext,true){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    complainId=obj.getString("complainId");
                    complainDetail();//获取详情
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 取消投诉
     */
    private void cancelComplain(){
    	
    	//数据未请求到，则直接返回
    	if(null == bean){
    		return;
    	}
    	
        RequstClient.cancelComplain(complainId, bean.getORDER_CODE(), new CustomResponseHandler(mContext,true){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    complainId=obj.getString("complainId");
                    complainDetail();//获取详情
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
