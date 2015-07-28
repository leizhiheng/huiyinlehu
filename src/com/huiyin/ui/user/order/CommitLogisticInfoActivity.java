package com.huiyin.ui.user.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.CompanyListEntity;
import com.huiyin.bean.LogisticsInfoBean;
import com.huiyin.constants.OrderConstants;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow;

/**
 * 提交物流信息
 * Created by lian on 2015/6/1.
 */
public class CommitLogisticInfoActivity extends BaseActivity implements View.OnClickListener{

	public static final String ID ="id";			//退换货id
	public static final String FLAG ="flag";		//数据bean
	
	/**快递寄回说明**/
    private TextView tvtip;
    
    /**快递公司**/
    private TextView tvexpresstype;
    
    /**提交物流信息**/
    private Button btncommit;
    
    /**还未寄包裹**/
    private Button btnnotsend;
    
    
    /**物流单号**/
    private EditText et_no;
    private LinearLayout layout_select_logistics;
    
    /**物流信息**/
    private LogisticsInfoBean bean;
    
//    /**物流ID**/
//    private String selectedId1;
//    
//    /**物流名称**/
//    private String selectedName1;
    
    
    /**退货Id，换货Id**/
    private String Id;

    /** 1退款，2换货 **/
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_commit_logistics_info);
       
        //取参数
        Id =getIntent().getStringExtra(ID);
        flag =getIntent().getIntExtra(FLAG, 1);
        
        //按钮点击事件
        setListener();
        
        //获取物流信息
        getLogisticInfo();
    }



    public void onFindViews(){
        this.btnnotsend = (Button) findViewById(R.id.btn_not_send);
        this.btncommit = (Button) findViewById(R.id.btn_commit);
        this.tvexpresstype = (TextView) findViewById(R.id.tv_express_type);
        this.tvtip = (TextView) findViewById(R.id.tv_tip);
        this.et_no = (EditText) findViewById(R.id.et_no);
        this.layout_select_logistics = (LinearLayout) findViewById(R.id.layout_select_logistics);
    }

    /**
     * 设置点击事件
     */
    private void setListener() {
        btncommit.setOnClickListener(this);
        btnnotsend.setOnClickListener(this);
        layout_select_logistics.setOnClickListener(this);
    }

    /**
     * 显示默认的物流信息
     */
    public void showDefaultCompanyData(){
    	
        tvtip.setText(Html.fromHtml(bean.getDescrition()));
        
        //默认选中第一个物流公司
        CompanyListEntity company = bean.getCompanyList().get(0);
        tvexpresstype.setTag(company.getID());
        tvexpresstype.setText(company.getCOMPANY_NAME());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_not_send:
            	//还未寄包裹;
                this.finish();
                break;
            case R.id.layout_select_logistics:
            	
            	//选择物流
                List<CompanyListEntity> list= bean.getCompanyList();
                Map<String,String> data=new HashMap<String, String>();
                for (CompanyListEntity item:list){
                    data.put(item.getID()+"",item.getCOMPANY_NAME());
                }
               
                showReasonPopwindow(layout_select_logistics,data , new SelectApplyReasonWindow.IOnItemClick() {
                    @Override
                    public void onItemClick(String id, String value) {
                        
                    	//显示选中的物流信息
                    	tvexpresstype.setTag(id);
                        tvexpresstype.setText(value);//设置快递类型
                    }
                });
                
                break;
            case R.id.btn_commit:
            	
            	//提交
                commit();
                
                break;
        }
    }

    /**
     logisticsId 物流公司
     kdCode      物流单号
     id          退/换货id
     userId      用户id
     flag        1退款，2换货
     */
    public void commit(){
        if(check()){
            String kdCode=et_no.getText().toString();
            
            //物流公司ID
            String companyId = tvexpresstype.getTag().toString().trim();
            
            RequstClient.commitLogistics(AppContext.userId,kdCode, Id, companyId, flag, new CustomResponseHandler(mContext,true){
                @Override
                public void onSuccess(int statusCode, Header[] headers, String content) {
                    super.onSuccess(statusCode, headers, content);
                    if(isSuccessResponse(content)){
                    	
                    	Toast.makeText(getBaseContext(), "提交成功", Toast.LENGTH_SHORT).show();
                    	
                    	if(flag == OrderConstants.Flag_Replace){
	                    	
                    		//通知-请寄回-界面刷新
                    		setResult(RESULT_OK);
                    		
                    		//跳转到等待商家发货界面-
	                    	Intent intent=new Intent(mContext,ReplaceDetailFinishActivity.class);
	                    	intent.putExtra(ReplaceDetailFinishActivity.EXTRA_REPLACE_ID, Id);
	                    	startActivity(intent);
	                    	finish();
	                    	
                    	}else{
                    		
                    		//通知-请寄回-界面刷新
                    		setResult(RESULT_OK);
                    		
                    		//跳转到等待商家发货界面
	                    	Intent intent=new Intent(mContext,ReturnDetailFinishActivity.class);
	                    	intent.putExtra(ReturnDetailFinishActivity.EXTRA_RETURN_ID, Id);
	                    	startActivity(intent);
	                    	finish();
                    	}
                    }
                }
            });
        }
    }

    /**
     * 数据验证
     * @return
     */
    public boolean check(){
        String text=tvexpresstype.getText().toString();
        if("".equals(text)){
            Toast.makeText(getBaseContext(), "请选择快递", Toast.LENGTH_SHORT).show();
            return false;
        }
        if("".equals(et_no.getText().toString())){
            Toast.makeText(getBaseContext(), "请输入单号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 获取物流信息
     */
    public void getLogisticInfo(){
        RequstClient.getLogisticsInfo(new CustomResponseHandler(mContext){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bean = new Gson().fromJson(content,LogisticsInfoBean.class);
                    if(null==bean){
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    showDefaultCompanyData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
