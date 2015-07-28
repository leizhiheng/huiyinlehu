package com.huiyin.ui.user.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.adapter.OrderDetailProductListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.AllOrderDetailBean;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.GoodList;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.classic.AddCarsParamsBean;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.ui.shoppingcar.CommitOrderActivity;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.ui.user.InvoiceDetailActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.ui.user.YuYueDetailActivity;
import com.huiyin.ui.user.complaint.ComplaintDetailActivity;
import com.huiyin.ui.user.complaint.SendComplaintActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.Utils;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单详情
 * Created by lian on 2015/5/20.
 */
public class AllOrderDetailActivity extends BaseActivity implements
        View.OnClickListener {

	/**取消订单**/
	private static final int REQUEST_CANCEL	 			= 1;
	
	/**确认订单**/
	private static final int REQUEST_CONFIRM_RECEIPT 	= 2;
	
	/**评价晒单**/
	private static final int REQUEST_COMMENT 			= 3;
	
	/**追加评价**/
	private static final int REQUEST_COMMENT_ADD 		= 4;
	
	/**申请预约**/
	private static final int REQUEST_BESPEAK 			= 5;
	
	/**我要投诉**/
	private static final int REQUEST_COMPLAIN 			= 6;
	
	
    private final static String TAG = "AllOrderDetailActivity";
    private Context context = AllOrderDetailActivity.this;
    public static final String GOOD_LIST = "GoodList";
    public static final String ORDER_ID = "orderId";//订单id
    private TextView mTv_title;
    private LinearLayout mLayout_btns2;
    
    private TextView mBtn1;
    private TextView mBtn2;
    private TextView mBtn3;
    private TextView mBtn4;
    
    /**当前记住的投诉按钮**/
    private TextView curComplaintBtn;
    
    private TextView mOd_ordernum;//订单编号
    private TextView mTv_show_tip;//订单提示
    private View mTv_show_tipView;//订单提示布局
    private TextView mOd_usename;//订单用户名
    private TextView mOd_telephone;//用户手机
    private TextView mOd_addr;//地址
    private ImageView mIv_dp;//店铺图标
    private TextView mTv_dpname;//店铺名称
    private com.huiyin.wight.MyListView mLv_order_list;
    private TextView mOd_payway;//支付方式
    private TextView mOd_peison;//配送方式
    private TextView mOd_peison_time;//配送时间
    private TextView mTv_employee_no;//配送员工编号
    private TextView mOd_invoice;//发票类型
    private TextView buy_message;//订单备注
    private View mOd_invoiceLayout;//发票类型布局
    //    private TextView mTv_fapiao;//抬头发票
//    private TextView mTv_fapiao_detail;//发票明细
    private TextView mOd_total_price;//实付金额
    private TextView mTv_time_ordercommit;//下单时间
    private TextView mOd_good_price;//商品金额
    private TextView mpreferential_price;//优惠金额
    private TextView mOd_logistic_fare;//运费
    private TextView od_coupon;//乐虎劵
    private TextView mOd_jifen;//积分
    // add by zhyao @2015/5/8  添加红包控件
    private TextView od_redpacket;//红包
    private int orderType;//订单类型 1 未付款,2 订单待审核,3 待发货,4 待收货,5 交易完成
    private String orderId;//订单id
    private AllOrderDetailBean detailBean;//订单详情数据
    private OrderDetailProductListAdapter adapter;
   
//    private GoodList list;
    
    //add by zhyao @2015/6/25 判断商品是否全部是消费卷 1.全消费卷  0.不全消费卷
    private String allConsumption;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_order_detail);
//        orderId=getIntent().getStringExtra(AllOrderDetailActivity.ORDER_ID);
//        orderType=getIntent().getIntExtra(AllOrderDetailActivity.ORDER_TYPE, 1);
        GoodList list = (GoodList) getIntent().getSerializableExtra(GOOD_LIST);
        if(null!=list){
            orderId = list.ID;
        }else{
            orderId=getIntent().getStringExtra(ORDER_ID);
        }
        findView();
        
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	
    	//如果订单信息加载过，第二次刷新要静悄悄的刷新
    	boolean showDialog = (null == detailBean) ? true : false;
    	getOrderDetail(showDialog);
    }
    
    private void findView() {
        //findview
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mLayout_btns2 = (LinearLayout) findViewById(R.id.layout_btns2);
        mBtn1 = (TextView) findViewById(R.id.btn1);
        mBtn2 = (TextView) findViewById(R.id.btn2);
        mBtn3 = (TextView) findViewById(R.id.btn3);
        mBtn4 = (TextView) findViewById(R.id.btn4);
        mOd_ordernum = (TextView) findViewById(R.id.od_ordernum);
        mTv_show_tip = (TextView) findViewById(R.id.tv_show_tip);
        mTv_show_tipView = findViewById(R.id.tv_show_tip_layout);
        mOd_usename = (TextView) findViewById(R.id.od_usename);
        mOd_telephone = (TextView) findViewById(R.id.od_telephone);
        mOd_addr = (TextView) findViewById(R.id.od_addr);
        mIv_dp = (ImageView) findViewById(R.id.iv_dp);
        mTv_dpname = (TextView) findViewById(R.id.tv_dpname);
        mLv_order_list = (com.huiyin.wight.MyListView) findViewById(R.id.lv_order_list);
        mOd_payway = (TextView) findViewById(R.id.od_payway);
        mOd_peison = (TextView) findViewById(R.id.od_peison);
        mOd_peison_time = (TextView) findViewById(R.id.od_peison_time);
        mTv_employee_no = (TextView) findViewById(R.id.tv_employee_no);
        buy_message = (TextView) findViewById(R.id.buy_message);
        mpreferential_price = (TextView)findViewById(R.id.preferential_price);
        
        mOd_invoice = (TextView) findViewById(R.id.od_invoice);
        mOd_invoiceLayout = findViewById(R.id.od_invoice_layout);
        mOd_invoiceLayout.setOnClickListener(this);
//        mTv_fapiao = (TextView) findViewById(R.id.tv_fapiao);
//        mTv_fapiao_detail = (TextView) findViewById(R.id.tv_fapiao_detail);
        mOd_total_price = (TextView) findViewById(R.id.od_total_price);
        mTv_time_ordercommit = (TextView) findViewById(R.id.tv_time_ordercommit);
        mOd_good_price = (TextView) findViewById(R.id.od_good_price);
        mOd_logistic_fare = (TextView) findViewById(R.id.od_logistic_fare);
        od_coupon = (TextView) findViewById(R.id.od_coupon);
        mOd_jifen = (TextView) findViewById(R.id.od_jifen);
        // add by zhyao @2015/5/8  添加红包控件
        od_redpacket = (TextView) findViewById(R.id.od_redpacket);
        
        //商品总金额不显示
    }

    /**
     * 设置底部按钮状态
     */
    private void showBottomButtonState() {
    	
        if (orderType > 3) {//商品出库后
            mTv_show_tip.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_black_arraw, 0);
            mTv_show_tip.setOnClickListener(this);
        }
        
        
        /**
		         待付款 1：取消订单，去付款
		         审核中 2：我要投诉
		         待发货 3：我要投诉
		         待收货 4：申请预约/查询预约，我要投诉，确认收货
		         完成交易5：我要投诉，申请预约/查询预约（申请成功之后，再次出现“申请预约”按钮），评价晒单/追加评价/查看评价，再次购买
         */
        switch (orderType) {
            case 1://未付款
                setBtnVisibleContentOnclick(mBtn3, "取消订单");
                setBtnVisibleContentOnclick(mBtn4, "去付款");
                break;
            case 2://订单待审核
                showComplaintBtn(mBtn4);
                break;
            case 3://待发货
                showComplaintBtn(mBtn4);
                break;
            case 4://待收货
            	// add by zhyao @2015/6/25  全部为消费卷商品时，隐藏预约
                if("1".equals(allConsumption)) {
                	showComplaintBtn(mBtn4);
                }
                else {
                	if("0".equals(detailBean.order.FLAG_BESPEAK)){
                		//0显示预约按钮，
                        setBtnVisibleContentOnclick(mBtn2, "申请预约");
                    }else{
                    	//1显示预约查询按钮
                        setBtnVisibleContentOnclick(mBtn2, "查看预约");
                    }
                	
                	//投诉
                    showComplaintBtn(mBtn3);
                    
                    setBtnVisibleContentOnclick(mBtn4, "确认收货");
                }
                break;
            case 5://交易完成
            	// add by zhyao @2015/6/25  全部为消费卷商品时，隐藏预约
                if("1".equals(allConsumption)) {
                	
                	//投诉
                	showComplaintBtn(mBtn1);
                	
                    if("0".equals(detailBean.order.COMMENTS_STATUS)){
                    	//0未评论
                        setBtnVisibleContentOnclick(mBtn3, "评价晒单");
                    }else  if("1".equals(detailBean.order.COMMENTS_STATUS)){
                    	//1已评论
                        setBtnVisibleContentOnclick(mBtn3, "追加评价");
                    }else{
                    	//2已追加
                        setBtnVisibleContentOnclick(mBtn3, "查看评价");
                    }
                    
                    setBtnVisibleContentOnclick(mBtn4, "再次购买");
                   
                }
                else {
                	
                	//投诉
                	showComplaintBtn(mBtn1);
                	
                	
                	if("0".equals(detailBean.order.FLAG_BESPEAK)){
                		//0显示预约按钮，
                		setBtnVisibleContentOnclick(mBtn2, "申请预约");
                	}else{
                		//1显示预约查询按钮
                		setBtnVisibleContentOnclick(mBtn2, "查看预约");
                	}
                	if("0".equals(detailBean.order.COMMENTS_STATUS)){
                		//0 未评论
                		setBtnVisibleContentOnclick(mBtn3, "评价晒单");
                	}else  if("1".equals(detailBean.order.COMMENTS_STATUS)){
                		//1 已评论
                		setBtnVisibleContentOnclick(mBtn3, "追加评价");
                	}else{
                		//2 已追加
                		setBtnVisibleContentOnclick(mBtn3, "查看评价");
                	}
                	setBtnVisibleContentOnclick(mBtn4, "再次购买");
                }
            	
                break;
        }
    }

    /**
     * 设置投诉按钮监听
     */
    private void showComplaintBtn(TextView btn){
        if("1".equals(detailBean.order.IS_COMPLAINT)){//1.已经投诉过（页面显示查询投诉按钮）
            setBtnVisibleContentOnclick(btn, "查看投诉");
        }else if("0".equals(detailBean.order.IS_COMPLAINT)){//是否已经投诉0，没有（页面显示投诉按钮）
            setBtnVisibleContentOnclick(btn, "我要投诉");
        }
        
        curComplaintBtn = btn;
    }

    /**
     * 设置按钮可见和文字、监听事件
     *
     * @param btn
     * @param content
     */
    public void setBtnVisibleContentOnclick(TextView btn, String content) {
        btn.setVisibility(View.VISIBLE);
        btn.setText(content);
        btn.setOnClickListener(this);
    }

    /**
     * 确认收货
     */

    private void postSureReceive(String orderId) {

        RequstClient.makeSureOrder(AppContext.userId, orderId,
                new CustomResponseHandler(this,true) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String content) {

                        super.onSuccess(statusCode, headers, content);
                        LogUtil.i(TAG, "makeSureOrCancel:" + content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            if (!obj.getString("type").equals("1")) {
                                String errorMsg = obj.getString("msg");
                                Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            
                            showMyToast("已确认收货");
                            setResult(AllOrderActivity.RESULT_OK);
                            finish();
                            
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
    }

    /**
     * 订单详情
     */
    private void getOrderDetail(boolean showDialog) {
        RequstClient.getOrderDetail(orderId, new CustomResponseHandler(this, showDialog) {

            @Override
            public void onFinish() {
                super.onFinish();
                UIHelper.cloesLoadDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  String content) {

                super.onSuccess(statusCode, headers, content);
                LogUtil.i(TAG, "orderDetail:" + content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    detailBean = new Gson().fromJson(content, AllOrderDetailBean.class);
                    if (null != detailBean) {
                        
                    	orderType = Integer.parseInt(detailBean.order.STATUS);
                        
                        // add by zhyao @2015/6/25  添加是否全部消费卷
                        allConsumption = detailBean.order.ALLCONSUMPTION;
                        
                        //设定按钮监听事件
                        showBottomButtonState();
                        
                        //显示订单信息
                        showOrderData();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 显示订单数据
     */
    private void showOrderData() {
        
    	//标题，订单状态
    	mTv_title.setText(detailBean.order.STATUS_NAME);
        
        //订单编号
        mOd_ordernum.setText(detailBean.order.ORDER_CODE);
        mTv_show_tip.setText(detailBean.order.words);
        int vis = TextUtils.isEmpty(detailBean.order.words.trim()) ? View.GONE : View.VISIBLE;
        mTv_show_tipView.setVisibility(vis);
        
        mOd_usename.setText(detailBean.order.CONSIGNEE_NAME);
        mOd_telephone.setText(detailBean.order.CONSIGNEE_PHONE);
        mOd_addr.setText(detailBean.order.PROFILE);
        //int storeId = detailBean.order.STORE_ID;
        mTv_dpname.setText(detailBean.order.STORE_NAME);
        if (detailBean.order.SELLER > 0) {
        	
        	//存在店铺
            ImageManager.LoadWithServer(detailBean.order.STORE_LOGO, mIv_dp);
            mTv_dpname.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_black_arraw, 0);
            mTv_dpname.setOnClickListener(this);
        } else {
        	
        	//乐虎自营
            mIv_dp.setImageResource(R.drawable.lehu);
        }
        
    	//订单商品列表
    	adapter = new OrderDetailProductListAdapter(context, detailBean.order.orderDetailList);
    	mLv_order_list.setAdapter(adapter);
    	mLv_order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			Intent intent = new Intent(context, ProductsDetailActivity.class);
    			intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, detailBean.order.orderDetailList.get(position).GOODS_ID);
    			intent.putExtra(ProductsDetailActivity.GOODS_NO, detailBean.order.orderDetailList.get(position).GOODS_NO);
    			intent.putExtra(ProductsDetailActivity.STORE_ID, detailBean.order.orderDetailList.get(position).STORE_ID);
    			context.startActivity(intent);
    		}
    	});
    	mOd_payway.setText(detailBean.order.PAYMENT_METHOD);
    	// modify by zhyao @2015/7/8 添加服务地址
    	if("送货上门".equals(detailBean.order.SHIPPING_METHOD)) {
    		mOd_peison.setText("送货/服务上门");
    	}
    	else {
    		mOd_peison.setText(detailBean.order.SHIPPING_METHOD);
    	}
        mOd_peison_time.setText(detailBean.order.DELIVERYTIME);
        mTv_employee_no.setText(detailBean.order.STAFFID);
        buy_message.setText(detailBean.order.getRemark());//订单备注
        if (detailBean.order.getINVOICES_ID() <= 0) {//不存在发票
            mOd_invoice.setTextColor(getResources().getColor(R.color.gray));
            mOd_invoice.setText("无");
        } else {
            mOd_invoice.setTextColor(getResources().getColor(R.color.blue_color));
            mOd_invoice.setText(detailBean.order.INVOICING_METHOD);//发票信息
        }
        
//        mTv_fapiao.setText(detailBean.order.PAYMENT_METHOD);
//        mTv_fapiao_detail.setText(detailBean.order.INVOICING_METHOD);

        mTv_time_ordercommit.setText(detailBean.order.CREATE_DATE);
        
        //实付金额
        mOd_total_price.setText(MathUtil.priceForAppWithSign(detailBean.order.TOTAL_PRICE));
       
        //优惠金额
        mpreferential_price.setText(MathUtil.priceForAppWithSign(detailBean.order.getPREFERENTIAL_PRICE()));
        
        //商品总金额--在减优惠金额之前的原价
        mOd_good_price.setText(MathUtil.priceForAppWithSign(detailBean.order.PURCHASE_PRICE));
        
        //运费
        mOd_logistic_fare.setText(MathUtil.priceForAppWithSign(detailBean.order.LOGISTICS_FARE));
        
        if(detailBean.order.LH_AMOUNT>0){//显示乐虎券
            od_coupon.setText(MathUtil.priceForAppWithSign(detailBean.order.LH_AMOUNT));//乐虎劵
        }else{
            od_coupon.setText(MathUtil.priceForAppWithSign(0));//乐虎劵
        }
        mOd_jifen.setText(MathUtil.priceForAppWithSign(detailBean.order.POINTS_DEDUCTION));
        // add by zhyao @2015/5/8  添加红包抵扣金额展示
        od_redpacket.setText(MathUtil.priceForAppWithSign(detailBean.order.MONEY_DEDUCTION));
        // add by zhyao @2015/6/25  隐藏收获地址
        if("1".equals(allConsumption)) {
        	mOd_addr.setVisibility(View.GONE);
        }
    }

//    /**
//     * 验证是否能申请售后
//     */
//    private void appVerifyChangeAndReturn(final String orderid,
//                                          final String commodityId) {
//        RequstClient.appVerifyChangeAndReturn(orderid, commodityId,
//                new CustomResponseHandler(this) {
//
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                        UIHelper.cloesLoadDialog();
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers,
//                                          String content) {
//                        super.onSuccess(statusCode, headers, content);
//                        try {
//                            JSONObject obj = new JSONObject(content);
//                            if (!obj.getString("type").equals("1")) {
//                                String errorMsg = obj.getString("msg");
//                                Toast.makeText(getBaseContext(), errorMsg,
//                                        Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                            Intent i = new Intent();
//                            i.setClass(MyOrderDetailActivity.this,
//                                    ApplyAfterSaleActivity.class);
//                            i.putExtra("orderId", orderid);
//                            i.putExtra("goodId", commodityId);
//                            i.putExtra("totalPrice",
//                                    mOrderDetailBean.orderDetail.TOTAL_PRICE);
//                            i.putExtra("flag", obj.getString("flag"));
//                            if (!obj.isNull("consignee_name"))
//                                i.putExtra("consignee_name",
//                                        obj.getString("consignee_name"));
//                            if (!obj.isNull("consignee_phone"))
//                                i.putExtra("consignee_phone",
//                                        obj.getString("consignee_phone"));
//                            if (!obj.isNull("consignee_address"))
//                                i.putExtra("consignee_address",
//                                        obj.getString("consignee_address"));
//                            if (!obj.isNull("returnRate"))
//                                i.putExtra("returnRate",
//                                        obj.getString("returnRate"));
//                            startActivity(i);
//                            finish();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_show_tip://查看物流
                Intent wl_intent = new Intent();
                wl_intent.setClass(this, LookForLogisticsActivity.class);
                wl_intent.putExtra(LookForLogisticsActivity.ORDERID, orderId);
                wl_intent.putExtra(LookForLogisticsActivity.ORDERNO, detailBean.order.ORDER_CODE);
                wl_intent.putExtra(LookForLogisticsActivity.STATU, detailBean.order.STATUS_NAME);
                startActivity(wl_intent);
            	return;
            case R.id.tv_dpname://进入店铺
                Intent intent = new Intent(context, StoreHomeActivity.class);
                intent.putExtra(StoreHomeActivity.STORE_ID, detailBean.order.SELLER);
                context.startActivity(intent);
                return;
            case R.id.od_invoice_layout:
            	
            	//进入发票详情
            	if (detailBean.order.getINVOICES_ID() > 0) {
        				Intent i = new Intent(mContext, InvoiceDetailActivity.class);
        				i.putExtra(InvoiceDetailActivity.INVOICEID, detailBean.order.getINVOICES_ID());
        				startActivity(i);
        		}
            	
            	return;
        }
        /**
		         待付款：取消订单，去付款
		         审核中：我要投诉
		         待发货：我要投诉
		         待收货：申请预约/查询预约，我要投诉，确认收货
		         完成交易：我要投诉，申请预约/查询预约（申请成功之后，再次出现“申请预约”按钮），评价晒单/追加评价/查看评价，再次购买
         */
        switch (orderType) {
            case 1://未付款
                if (v.getId() == R.id.btn3) {//取消订单
                	
                	Log.i(TAG, "取消订单对话框...");
                    Intent i = new Intent(context, CommonConfrimCancelDialog.class);
                    i.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CANCEL_ORDER);
                    startActivityForResult(i, REQUEST_CANCEL);
                } else if (v.getId() == R.id.btn4) {//去付款
                    String number = detailBean.order.ORDER_CODE;
                    String serialNum = detailBean.order.ORDER_ID;
                    float totalPrice = detailBean.order.TOTAL_PRICE;
                    Intent intent = new Intent(context, CommitOrderActivity.class);
                    intent.putExtra("number", number);
                    intent.putExtra("serialNum", serialNum);
                    intent.putExtra("totalPrice", totalPrice + "");
                    context.startActivity(intent);
                }
                break;
            case 2://订单待审核
                if (v.getId() == R.id.btn4) {//我要投诉
                    goToComplaintActivity();
                }
                break;
            case 3://待发货
                if (v.getId() == R.id.btn4) {//我要投诉
                    goToComplaintActivity();
                }
                break;
            case 4://待收货
            	// add by zhyao @2015/6/25  全部为消费卷商品时，隐藏预约
                if("1".equals(allConsumption)) {//全部消费卷
                	if (v.getId() == R.id.btn4) {//我要投诉
                        goToComplaintActivity();
                    }
                } else {
                	if (v.getId() == R.id.btn2) {//申请预约
                        //订单信息为空，不给预约
                        if(null == detailBean){
                            return;
                        }
                        if("0".equals(detailBean.order.FLAG_BESPEAK)){//0显示预约按钮，
                            Intent intent = new Intent(mContext, ApplyBespeakActivity.class);
                            intent.putExtra(ApplyBespeakActivity.EXTRA_ORDERID, detailBean.order.ORDER_ID);
                            intent.putExtra(ApplyBespeakActivity.EXTRA_NUMBER, detailBean.order.ORDER_CODE);
                            startActivityForResult(intent, REQUEST_BESPEAK);
                        }else if("1".equals(detailBean.order.FLAG_BESPEAK)){//1显示预约查询按钮
                            //跳转到预约详情
                            Intent intent = new Intent(context, YuYueDetailActivity.class);
                            intent.putExtra(YuYueDetailActivity.EXTRA_BESPEAKID,detailBean.order.BESPEAK_ID);
                            startActivity(intent);
                        }
                    } else if (v.getId() == R.id.btn3) {//我要投诉
                        goToComplaintActivity();
                    } else if (v.getId() == R.id.btn4) {//确认收货
                        Intent intent = new Intent(context, CommonConfrimCancelDialog.class);
                        intent.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_SURE_RECEIVE);
                        startActivityForResult(intent,REQUEST_CONFIRM_RECEIPT);
                    }
                }
                
                break;
            case 5://交易完成
            	// add by zhyao @2015/6/25  全部为消费卷商品时，隐藏预约  (这里的投诉和再次购买的id是重复的btn4)
                if("1".equals(allConsumption)) {//全部消费卷
                	if (v.getId() == R.id.btn1) {//我要投诉
                        goToComplaintActivity();
                    } else if (v.getId() == R.id.btn3) {//评价晒单
                        if("0".equals(detailBean.order.COMMENTS_STATUS)){//未评论
                            Intent intent = new Intent(context, OrderCommentActivity.class);
                            intent.putExtra(OrderCommentActivity.ORDER_ITEM, detailBean.getGoodList());
                            startActivityForResult(intent, REQUEST_COMMENT);
                        }else  if("1".equals(detailBean.order.COMMENTS_STATUS)){//已评论

                        	//跳转到追加评论界面
                        	Intent intent = new Intent(mContext, AddCommentActivity.class);
                        	intent.putExtra(AddCommentActivity.EXTRA_ORDERID, detailBean.order.ORDER_ID);
                        	intent.putExtra(AddCommentActivity.EXTRA_TITLE, "追加评价");
                        	startActivityForResult(intent, REQUEST_COMMENT_ADD);
                        	
                        	
                        }else  if("2".equals(detailBean.order.COMMENTS_STATUS)){//已追加

                        	//跳转到查看评价
                        	Intent intent = new Intent(mContext, AddCommentActivity.class);
                        	intent.putExtra(AddCommentActivity.EXTRA_ORDERID, detailBean.order.ORDER_ID);
                        	intent.putExtra(AddCommentActivity.EXTRA_TITLE, "评价详情");
                        	startActivity(intent);
                        }
                    } else if (v.getId() == R.id.btn4) {//再次购买
                        addCarts("-1", getResultHashMap());
                    }
                }
                else {
                	if (v.getId() == R.id.btn1) {//我要投诉
                        goToComplaintActivity();
                    } else if (v.getId() == R.id.btn2) {//申请预约
                    	//订单信息为空，不给预约
                    	if(null == detailBean){
                    		return;
                    	}
                        if("0".equals(detailBean.order.FLAG_BESPEAK)){//0显示预约按钮，
                            Intent intent = new Intent(mContext, ApplyBespeakActivity.class);
                            intent.putExtra(ApplyBespeakActivity.EXTRA_ORDERID, detailBean.order.ORDER_ID);
                            intent.putExtra(ApplyBespeakActivity.EXTRA_NUMBER, detailBean.order.ORDER_CODE);
                            startActivityForResult(intent, REQUEST_BESPEAK);
                        }else if("1".equals(detailBean.order.FLAG_BESPEAK)){//1显示预约查询按钮
                            //跳转到预约详情
                            Intent intent = new Intent(context, YuYueDetailActivity.class);
                            intent.putExtra(YuYueDetailActivity.EXTRA_BESPEAKID,detailBean.order.BESPEAK_ID);
                            context.startActivity(intent);
                        }
                    		
                    } else if (v.getId() == R.id.btn3) {//评价晒单
                        if("0".equals(detailBean.order.COMMENTS_STATUS)){//未评论
                            Intent intent = new Intent(context, OrderCommentActivity.class);
                            intent.putExtra(OrderCommentActivity.ORDER_ITEM, detailBean.getGoodList());
                            startActivityForResult(intent, REQUEST_COMMENT);
                        }else  if("1".equals(detailBean.order.COMMENTS_STATUS)){//已评论

                        	//跳转到追加评论界面
                        	Intent intent = new Intent(mContext, AddCommentActivity.class);
                        	intent.putExtra(AddCommentActivity.EXTRA_ORDERID, detailBean.order.ORDER_ID);
                        	intent.putExtra(AddCommentActivity.EXTRA_TITLE, "追加评价");
                        	startActivityForResult(intent, REQUEST_COMMENT_ADD);
                        	
                        	
                        }else  if("2".equals(detailBean.order.COMMENTS_STATUS)){//已追加

                        	//跳转到查看评价
                        	Intent intent = new Intent(mContext, AddCommentActivity.class);
                        	intent.putExtra(AddCommentActivity.EXTRA_ORDERID, detailBean.order.ORDER_ID);
                        	intent.putExtra(AddCommentActivity.EXTRA_TITLE, "评价详情");
                        	startActivity(intent);
                        }
                    } else if (v.getId() == R.id.btn4) {//再次购买
                        addCarts("-1", getResultHashMap());
                    }
                }
                break;
        }
    }

    /**
     * 跳转到发起投诉页面
     */
    private void goToComplaintActivity(){
        if("1".equals(detailBean.order.IS_COMPLAINT)){//1.已经投诉过（页面显示查询投诉按钮）
            Intent intent=new Intent(mContext,ComplaintDetailActivity.class);
            intent.putExtra(ComplaintDetailActivity.COMPLAIN_ID,detailBean.order.COMPLAINT_ID);
            mContext.startActivity(intent);
        }else if("0".equals(detailBean.order.IS_COMPLAINT)){//是否已经投诉0，没有（页面显示投诉按钮）
            Intent intent=new Intent(mContext, SendComplaintActivity.class);
            intent.putExtra(SendComplaintActivity.GOOD, detailBean.getGoodList());
            mContext.startActivityForResult(intent,REQUEST_COMPLAIN);
        }
    }

    public void cancelOrder(String orderId){
        RequstClient.cancelOrder(orderId,new CustomResponseHandler(mContext, true){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                	
                	Log.i(TAG, "取消订单返回结果...");
                    JSONObject json=new JSONObject(content);
                    String type=json.optString("type");
                    if (!"1".equals(type)) {
                        String errorMsg = json.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setResult(AllOrderActivity.CANSEL_ORDER);
                    AllOrderDetailActivity.this.finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (pushFlag.equals("1")) {
//                Intent i = new Intent();
//                i.setClass(getApplicationContext(), MainActivity.class);
//                startActivity(i);
//            } else {
//                finish();
//            }
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CANCEL:
            	
            	//取消订单
                if(resultCode== RESULT_OK){
                	Log.i(TAG, "取消订单开始...");
                    cancelOrder(detailBean.order.ORDER_ID);
                }
                
                break;
            case REQUEST_CONFIRM_RECEIPT:
            	
            	//确认收货
                if(resultCode == RESULT_OK){
                    postSureReceive(detailBean.order.ORDER_ID);
                }
                
                break;
            case REQUEST_COMMENT:
            	
            	//评价晒单
            	if(resultCode == RESULT_OK){
            		
            		//将button改成追加评价
            		mBtn3.setText("追加评价");
            		detailBean.order.COMMENTS_STATUS = "1";
            		
            		//通知订单列表刷新数据
            		setResult(RESULT_OK);
            	}
            	
            	break;
            case REQUEST_COMMENT_ADD:
            	
            	//追加评论
            	if(resultCode == RESULT_OK){
            		
            		//将button改成追加评价
            		mBtn3.setText("查看评价");
            		detailBean.order.COMMENTS_STATUS = "2";
            		
            		//通知订单列表刷新数据
            		setResult(RESULT_OK);
            	}
            	
            	break;	
            	
            	
            case REQUEST_BESPEAK:
            	
            	//申请预约
            	if(resultCode == RESULT_OK){
            		
            		mBtn2.setText("查看预约");
            		detailBean.order.FLAG_BESPEAK = "1";
            		
            		//通知订单列表刷新数据
            		setResult(RESULT_OK);
            	}
            	
            	break;
            case REQUEST_COMPLAIN:
            	
            	//我要投诉
            	if(resultCode == RESULT_OK){
                    detailBean.order.IS_COMPLAINT="1";
                    
                    //改变投诉按钮的状态
            		showComplaintBtn(curComplaintBtn);
            	}
            	
            	break;
        }
    }

    /**
     * 验证时间
     *
     * @param curDate
     * @param comTime
     * @param days
     * @return
     */
    public boolean isTimeOn(String curDate, String comTime, int days) {
        if (StringUtils.isBlank(curDate) || StringUtils.isBlank(comTime))
            return false;
        Date com = StringUtils.toDate(comTime);
        Date nowTime = StringUtils.toDate(curDate);

        long comSecond = com.getTime();
        long nowSecond = nowTime.getTime();

        if (comSecond <= 0 || nowSecond <= 0) {
            return false;
        } else {
            long tempSec = nowSecond - comSecond;
            if (tempSec <= 0) {
                return false;
            } else {
                long aDay = 3600000 * 24;
                int comday = (int) (tempSec / aDay);

                return comday < days ? true : false;
            }
        }
    }

    /**
     * 加入多个商品到购物车
     *
     * @param shoppingIds
     *            如果userId为空，则传入本地缓存的购物车id
     * @param paramsBeans
     *            加入购物车商品的集合
     */
    public void addCarts(String shoppingIds, List<AddCarsParamsBean> paramsBeans) {
        if (StringUtils.isBlank(AppContext.userId)) {
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
            context.startActivity(intent);
            return;
        }
        String userId = AppContext.userId == null ? "-1" : AppContext.userId;
        String json = "";
        if (paramsBeans != null) {
            json = new Gson().toJson(paramsBeans);
        }
        Logger.json(json);
        RequstClient.addCarts(userId, shoppingIds, json,
                new CustomResponseHandler(context) {
                    @Override
                    public void onSuccess(String content) {
                        super.onSuccess(content);
                        try {
                            JSONObject object = new JSONObject(content);
                            if (object.optInt("type") != 1) {
                                UIHelper.showToast(object.optString("msg"));
                            } else {
                                UIHelper.showToast(object.optString("msg"));
                                int num = object.getInt("SHOPPING_CAR_NUM");
                                // 购物车
                                AppContext.MAIN_TASK = AppContext.SHOPCAR;
                                Intent i_main = new Intent(context, MainActivity.class);
                                i_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i_main);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 再次购买的订单结果
     *
     * @return
     */
    private List<AddCarsParamsBean> getResultHashMap() {
        List<AddCarsParamsBean> result = null;
        if (detailBean!=null&&detailBean.order != null&&detailBean.order.orderDetailList!=null) {
            result = new ArrayList<AddCarsParamsBean>();
            for (int i = 0; i < detailBean.order.orderDetailList.size(); i++) {
                AddCarsParamsBean item = new AddCarsParamsBean();
                item.goodsId = detailBean.order.orderDetailList.get(i).GOODS_ID;
                item.goodsNo = detailBean.order.orderDetailList.get(i).GOODS_NO;
                item.num = "1";
                result.add(item);
            }
        }
        return result;
    }
}
