package com.huiyin.ui.user.order;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.adapter.ImageAdapter;
import com.huiyin.adapter.ReturnDetailGoodsAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.ImageData;
import com.huiyin.bean.ReturnDetailBean;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.wight.MyListView;

/**
 * 退货详情->买家请退货 未发货
 * @author 刘远祺
 *
 * @todo 卖家已经审核通过退货申请，买家需要把商品退回给卖家
 *
 * @date 2015-7-3
 */
public class ReturnDetailReturnActivity extends BaseCameraActivity implements AdapterView.OnItemClickListener, OnClickListener {

	/**提交物流信息**/
	public static final int REQUEST_COMMIT_LOGISTIC = 1;
	
	
    /** 退货Id**/
    public static final String EXTRA_RETURN_ID 		= "returnId";

    /**商品列表**/
    private MyListView lvproduct;

    /**售后类型**/
    private TextView salTypeTv;

    private TextView tv_time;//申请时间

    private TextView tv_return_adress;//退货地址
    private TextView tv_return_time;//退货时间
    private TextView tv_return_phone;//退货电话


    /**申请理由**/
    private TextView applyReasonTv;

    /**退款金额**/
    private TextView returnMoneyTv;
    
    /**描述问题**/
    private TextView desQuestionTv;

    /**上传凭证**/
    private GridView uploadGridView;

    /**上传凭证布局**/
	private View uploadLayout;
    
    /**寄回商品**/
    private Button upadteBtn;

    /**还未寄包裹**/
    private Button cancelBtn;


    /**商品适配器**/
    private ReturnDetailGoodsAdapter adapter;

    /**图片上传适配器**/
    private ImageAdapter imageAdapter;

    /**退货ID**/
    private String returnId;

    private ReturnDetailBean mReturnDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_return);

        //初始化控件
        initView();

        //初始化数据
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView(){

        //findView
        lvproduct = (MyListView) findViewById(R.id.product_listView);
        salTypeTv = (TextView) findViewById(R.id.sale_type);
        returnMoneyTv = (TextView)findViewById(R.id.return_money);
        
        
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_return_adress = (TextView) findViewById(R.id.tv_return_adress);//退货地址
        tv_return_time = (TextView) findViewById(R.id.tv_return_time);//退货时间
        tv_return_phone = (TextView) findViewById(R.id.tv_return_phone);//退货电话
       
        
        applyReasonTv = (TextView)findViewById(R.id.apply_reason);
        desQuestionTv = (TextView) findViewById(R.id.des_question);
        uploadGridView = (GridView) findViewById(R.id.upload_gridview);
        uploadLayout = findViewById(R.id.upload_img_layout);
        
        upadteBtn = (Button) findViewById(R.id.update_apply_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_apply_btn);

        //设置适配器
        adapter = new ReturnDetailGoodsAdapter(mContext, null);
        lvproduct.setAdapter(adapter);

        int width = (DeviceUtils.getWidthMaxPx(mContext)*4)/5;
        imageAdapter = new ImageAdapter(mContext, width, 4);
        uploadGridView.setAdapter(imageAdapter);
        uploadGridView.setOnItemClickListener(this);

        //监听描述问题文本变换
        upadteBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData(){

        //在数据显示之前不显示标题
        setTitle("");

        //售后类型
        returnId = getStringExtra(EXTRA_RETURN_ID);
        searchReturnDetail(returnId);

    }

    /**
     * 显示数据
     * @param returnDetailBean
     */
    private void showData(ReturnDetailBean returnDetailBean){

        //设置标题
        setTitle("请退货");

        //商品列表
        adapter.refreshData(returnDetailBean.returnDetailList);

        //售后类型
        salTypeTv.setText(returnDetailBean.TYPE_NAME);

        //退款金额
        returnMoneyTv.setText("¥ " + returnDetailBean.getRETURN_MONEY());
        
        tv_time.setText(returnDetailBean.getStatuTime());//申请时间
        tv_return_adress.setText(returnDetailBean.DEFAULT_ADDRESS);//退货地址
        tv_return_time.setText(Html.fromHtml(mReturnDetailBean.getRETURN_DAY()));//退货时间
        if(mReturnDetailBean.orderIsClose()){
        	
        	//退货期限已过，寄回商品，还未寄包裹，隐藏
            upadteBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
        }
        
        
        tv_return_phone.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        tv_return_phone.getPaint().setAntiAlias(true);//抗锯齿
        tv_return_phone.setText(returnDetailBean.TEL);//退货电话
        tv_return_phone.setOnClickListener(this);

        //上门自提
        if(returnDetailBean.isGetBySelf()){
        	
        	//退款方式：如果=2，在“请退货”页面中不需要显示“寄回商品按钮”(代表上门自提)
            //监听描述问题文本变换
            upadteBtn.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
        }
        //申请理由
        applyReasonTv.setText(returnDetailBean.REASON_NAME);
        applyReasonTv.setTag(returnDetailBean.ID);

        //描述
        desQuestionTv.setText(returnDetailBean.DESCRIBE);

        //更新上传的图片
        List<ImageData> list = returnDetailBean.getImageData();
		if(null != list && list.size() > 0){
			uploadLayout.setVisibility(View.VISIBLE);
			imageAdapter.refreshData(list);
		}else{
			uploadLayout.setVisibility(View.GONE);
		}
    }

    /**
     * 查询-退货详情
     */
    private void searchReturnDetail(String returnId){
        RequstClient.returnDetail(new CustomResponseHandler(mContext, true) {
            @Override
            public void onRefreshData(String content) {

                //解析json
                mReturnDetailBean = ReturnDetailBean.explainJson(content, mContext);
                if(mReturnDetailBean.type == 1){

                    //显示退货详情数据
                    showData(mReturnDetailBean);
                }else{

                    //显示服务器返回的异常信息
                    showMyToast(mReturnDetailBean.msg);
                }
            }

        }, returnId);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        switch (arg0.getId()) {
            case R.id.upload_gridview:

                //上传凭证图片
                Intent intent = new Intent();
                intent.setClass(mContext,PhotoViewActivity.class);
                intent.putStringArrayListExtra(PhotoViewActivity.INTENT_KEY_PHOTO, imageAdapter.getImageUrlList());
                intent.putExtra(PhotoViewActivity.INTENT_KEY_POSITION, arg2);
                startActivity(intent);
                
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.update_apply_btn:
            	
            	//寄回商品
                Intent intent=new Intent(mContext,CommitLogisticInfoActivity.class);
                intent.putExtra(CommitLogisticInfoActivity.ID, returnId);
                intent.putExtra(CommitLogisticInfoActivity.FLAG, mReturnDetailBean.getFlag());
                startActivityForResult(intent, REQUEST_COMMIT_LOGISTIC);
                
                break;
            case R.id.cancel_apply_btn:
            	
            	//取消申请
            	cancelBackOrder(returnId);
                
                break;
            case R.id.tv_return_phone:
            	
            	//拨打电话
                Intent d_intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tv_return_phone.getText()));
                d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(d_intent);
                break;
        }
    }

    /***
	 * 取消退货
	 */
	private void cancelBackOrder(String returnId) {

		RequstClient.cancelBackOrder(returnId, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}else{
						
						//取消成功
						showMyToast("取消成功");
						
						//通知刷新界面
						setResult(RESULT_OK);
						
						finish();
					}
					

				} catch (Exception e) {
					e.printStackTrace();
				} 
			}

		});
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch (requestCode) {
		case REQUEST_COMMIT_LOGISTIC:
			
			//寄回商品成功
			if(RESULT_OK == resultCode){
				
				//通知刷新订单列表(退换货)
				setResult(RESULT_OK);
				finish();
			}
			break;
		}
    }
}