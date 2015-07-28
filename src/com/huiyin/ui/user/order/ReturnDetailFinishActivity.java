package com.huiyin.ui.user.order;


import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.adapter.ImageAdapter;
import com.huiyin.adapter.ReturnDetailGoodsAdapter;
import com.huiyin.adapter.ReturnLogisticsAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.ImageData;
import com.huiyin.bean.ReturnDetailBean;
import com.huiyin.constants.OrderConstants;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.wight.MyListView;

/**
 * 退货详情->维权完成
 * @author 刘远祺
 *
 * @todo 显示退货完成的订单状态
 *
 * @date 2015-7-3
 */
public class ReturnDetailFinishActivity extends BaseCameraActivity implements AdapterView.OnItemClickListener {


    /** 退货Id**/
    public static final String EXTRA_RETURN_ID 		= "returnId";

    /**商品列表**/
    private MyListView lvproduct;

    /**售后类型**/
    private TextView salTypeTv;

    /**时间类型(退货时间，申请时间)**/
    private TextView tv_time_tip;
    
    /**发货时间**/
    private TextView tv_time;//发货时间
    

    /**物流布局块**/
	private View logistics_layout;
    
    /**物流公司**/
    private TextView tv_company;
    
    /**快递单号**/
    private TextView tv_logistNumber;
    
    

    /**申请理由**/
    private TextView applyReasonTv;
    
    /**实退金额**/
    private TextView returnMoneyTv;
    
    /**描述问题**/
    private TextView desQuestionTv;

    /**上传凭证**/
    private GridView uploadGridView;

    /**上传凭证布局**/
	private View uploadLayout;
    
    /**物流跟踪**/
    private ListView logisticsListView;
    
    /**退款金额布局**/
    private View returnLayout;
    
    /**商品适配器**/
    private ReturnDetailGoodsAdapter adapter;

    /**图片上传适配器**/
    private ImageAdapter imageAdapter;

    /**物流数据适配器**/
    private ReturnLogisticsAdapter logisticAdapter;
    
    
    /**退货ID**/
    private String returnId;

    private ReturnDetailBean mReturnDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_finish);

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
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time_tip = (TextView) findViewById(R.id.tv_time_tip);
        applyReasonTv = (TextView)findViewById(R.id.apply_reason);
        desQuestionTv = (TextView) findViewById(R.id.des_question);
        uploadGridView = (GridView) findViewById(R.id.upload_gridview);
        uploadLayout = findViewById(R.id.upload_img_layout);
        
        //物流布局
        logistics_layout = findViewById(R.id.logistics_layout);
        logisticsListView = (ListView)findViewById(R.id.lv_logistics);
       
        //退款金额
        returnMoneyTv = (TextView)findViewById(R.id.return_money);
        returnLayout =  findViewById(R.id.return_actual_layout);
        
        //物流公司，快递单号
        tv_company = (TextView) findViewById(R.id.tv_company);
        tv_logistNumber = (TextView) findViewById(R.id.tv_logic_number);
        
        //商品适配器
        adapter = new ReturnDetailGoodsAdapter(mContext, null);
        lvproduct.setAdapter(adapter);

        //商品凭证图片适配器
        int width = (DeviceUtils.getWidthMaxPx(mContext)*4)/5;
        imageAdapter = new ImageAdapter(mContext, width, 4);
        uploadGridView.setAdapter(imageAdapter);
        uploadGridView.setOnItemClickListener(this);

        //物流适配器
        logisticAdapter = new ReturnLogisticsAdapter(mContext, null);
        logisticsListView.setAdapter(logisticAdapter);
        
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
    	//setTitle("请退货");
    	setTitle(returnDetailBean.TITLE);
    	
        //商品列表
        adapter.refreshData(returnDetailBean.returnDetailList);
        
        //售后类型
        salTypeTv.setText(returnDetailBean.TYPE_NAME);
        
        //时间类型
        int status=Integer.parseInt(returnDetailBean.STATUS);
        String text = OrderConstants.getReturnStatuTip(status);
        tv_time_tip.setText(text);
        tv_time.setText(returnDetailBean.getStatuTime());

        //申请理由
        applyReasonTv.setText(returnDetailBean.REASON_NAME);
        applyReasonTv.setTag(returnDetailBean.ID);

        //实退金额
        returnMoneyTv.setText("¥ " + returnDetailBean.RETURN_MONEY);
        returnLayout.setVisibility(TextUtils.isEmpty(returnDetailBean.RETURN_MONEY) ? View.GONE : View.VISIBLE);
        
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
        
        //物流信息(有物流，并且不是上门自提的，才显示物流信息)
        if(null != returnDetailBean && null != returnDetailBean.LOGISTICS && !returnDetailBean.isGetBySelf()){
        	tv_company.setText(returnDetailBean.getLOGISTICS().COMPANY_NAME);
        	tv_logistNumber.setText(returnDetailBean.getLOGISTICS().DELIVERY_CODE);
        	logisticAdapter.refreshData(returnDetailBean.getLogisticList());
        	
        	//显示物流布局
        	logistics_layout.setVisibility(View.VISIBLE);
        	
        }else{
        	
        	//上门自提的也不显示物流
        	
        	//不显示物流布局
        	logistics_layout.setVisibility(View.GONE);
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
                if (mReturnDetailBean.type == 1) {

                    //显示退货详情数据
                    showData(mReturnDetailBean);
                    
                } else {

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


}