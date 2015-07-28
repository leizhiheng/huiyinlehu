package com.huiyin.ui.user.order;


import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.adapter.ImageAdapter;
import com.huiyin.adapter.ReplaceDetailGoodsAdapter;
import com.huiyin.adapter.ReturnLogisticsAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.ImageData;
import com.huiyin.bean.ReplaceDetailBean;
import com.huiyin.constants.OrderConstants;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.wight.MyListView;

/**
 * 换货详情->维权完成
 * @author 刘远祺
 *
 * @todo 显示换货完成的订单状态
 *
 * @date 2015-7-3
 */
public class ReplaceDetailFinishActivity extends BaseCameraActivity implements AdapterView.OnItemClickListener {


    /** 换货Id**/
    public static final String EXTRA_REPLACE_ID 		= "replaceId";

    /**商品列表**/
    private MyListView lvproduct;

    /**售后类型**/
    private TextView salTypeTv;

    /**时间类型**/
    private TextView tv_time_tip;
    
    /**发货时间**/
    private TextView tv_time;



    /**申请理由**/
    private TextView applyReasonTv;

    /**描述问题**/
    private TextView desQuestionTv;

    /**上传凭证**/
    private GridView uploadGridView;

    /**上传凭证布局**/
	private View uploadLayout;
    
	/**物流布局块**/
	private View logistics_layout;
	
    /**物流公司**/
    private TextView tv_company;
    
    /**快递单号**/
    private TextView tv_logistNumber;
    
    /**管理商品订单编号**/
    private TextView manager_order_number_textview;
    
    /**物流跟踪**/
    private ListView logisticsListView;

    /**商品适配器**/
    private ReplaceDetailGoodsAdapter adapter;

    /**图片上传适配器**/
    private ImageAdapter imageAdapter;

    /**物流数据适配器**/
    private ReturnLogisticsAdapter logisticAdapter;
    
    /**换货ID**/
    private String replaceId;

    private ReplaceDetailBean mReplaceDetailBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wait_merchant_receive);

        //初始化控件
        initView();

        //初始化数据
        initData();
    }

    /**
     * 初始化控件
     */
	private void initView() {

		// findView
		lvproduct = (MyListView) findViewById(R.id.product_listView);
		salTypeTv = (TextView) findViewById(R.id.sale_type);
		tv_time_tip = (TextView) findViewById(R.id.tv_time_tip);
		tv_time = (TextView) findViewById(R.id.tv_time);
		applyReasonTv = (TextView) findViewById(R.id.apply_reason);
		desQuestionTv = (TextView) findViewById(R.id.des_question);
		uploadGridView = (GridView) findViewById(R.id.upload_gridview);
		uploadLayout = findViewById(R.id.upload_img_layout);
		
		//物流布局
		logistics_layout = findViewById(R.id.logistics_layout);
		logisticsListView = (ListView) findViewById(R.id.lv_logistics);

		//物流公司，快递单号，管理商品订单编号
		tv_company = (TextView) findViewById(R.id.tv_company);
		tv_logistNumber = (TextView) findViewById(R.id.tv_logic_number);
		manager_order_number_textview = (TextView)findViewById(R.id.manager_order_number_textview);
		
		// 设置适配器
		adapter = new ReplaceDetailGoodsAdapter(mContext, null);
		lvproduct.setAdapter(adapter);

		// 商品凭证图片适配器
		int width = (DeviceUtils.getWidthMaxPx(mContext) * 4) / 5;
		imageAdapter = new ImageAdapter(mContext, width, 4);
		uploadGridView.setAdapter(imageAdapter);
		uploadGridView.setOnItemClickListener(this);

		// 物流适配器
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
        replaceId = getStringExtra(EXTRA_REPLACE_ID);
        searchReplaceDetail(replaceId);

    }

    /**
     * 显示数据
     * @param replaceDetailBean
     */
    private void showData(ReplaceDetailBean replaceDetailBean){

        //设置标题
        //setTitle("请退货");
    	setTitle(replaceDetailBean.TITLE);
    	
        //商品列表
        adapter.refreshData(replaceDetailBean.replaceDetailList);
        
        //售后类型
        salTypeTv.setText(replaceDetailBean.TYPE_NAME);
        
        //时间类型
        String text = OrderConstants.getReplaceStatuTip(replaceDetailBean.getREPLACEMENT_STATUS());
        tv_time_tip.setText(text);
        tv_time.setText(replaceDetailBean.getStatuTime());//审核时间

        //申请理由
        applyReasonTv.setText(replaceDetailBean.REASON_NAME);
        applyReasonTv.setTag(replaceDetailBean.REPLACE_ID);

        //描述
        desQuestionTv.setText(replaceDetailBean.DESCRIBE);

        //更新上传的图片
        List<ImageData> list = replaceDetailBean.getImageData();
		if(null != list && list.size() > 0){
			uploadLayout.setVisibility(View.VISIBLE);
			imageAdapter.refreshData(list);
		}else{
			uploadLayout.setVisibility(View.GONE);
		}
        
		//管理商品订单编号
		manager_order_number_textview.setText(replaceDetailBean.REPLACE_ORDER_CODE);
		
        //快递公司，物流单号，物流信息(非上门自提)
        if(null != replaceDetailBean && null != replaceDetailBean.LOGISTICS && !replaceDetailBean.isGetBySelf()){
        	tv_company.setText(replaceDetailBean.getLOGISTICS().COMPANY_NAME);
        	tv_logistNumber.setText(replaceDetailBean.getLOGISTICS().DELIVERY_CODE);
        	logisticAdapter.refreshData(replaceDetailBean.getLogisticList());
        	
        	//显示物流布局
        	logistics_layout.setVisibility(View.VISIBLE);
        }else{
        	
        	//上门自提的也不显示物流
        	
        	//不显示物流布局
        	logistics_layout.setVisibility(View.GONE);
        }
    }

    /**
     * 查询-换货详情
     */
    private void searchReplaceDetail(String replaceId){
        RequstClient.replaceDetail(new CustomResponseHandler(mContext, true) {
            @Override
            public void onRefreshData(String content) {

                //解析json
                mReplaceDetailBean = ReplaceDetailBean.explainJson(content, mContext);
                if (mReplaceDetailBean.type == 1) {

                    //显示换货详情数据
                    showData(mReplaceDetailBean);
                } else {

                    //显示服务器返回的异常信息
                    showMyToast(mReplaceDetailBean.msg);
                }
            }

        }, replaceId);
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