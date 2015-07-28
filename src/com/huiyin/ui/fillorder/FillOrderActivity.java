package com.huiyin.ui.fillorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.AddressItem;
import com.huiyin.bean.FillOrderBean;
import com.huiyin.ui.shoppingcar.CommitOrderActivity;
import com.huiyin.ui.shoppingcar.HuJuanDeductionActivity;
import com.huiyin.ui.shoppingcar.InvoiceInfoActivity;
import com.huiyin.ui.shoppingcar.PayWayActivity;
import com.huiyin.ui.shoppingcar.PaymentSuccessActivity;
import com.huiyin.ui.user.AddressManagementActivity;
import com.huiyin.ui.user.AddressModifyActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.Utils;
import com.huiyin.wight.ConfirmDialog;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class FillOrderActivity extends BaseActivity implements
        View.OnClickListener {
    private static final String TAG = "FillOrderActivity";
    private android.widget.Button fillorderreceiverbtn;
    private android.widget.TextView fillorderreceivername;
    private android.widget.TextView fillorderreceiverphone;
    private android.widget.TextView fillorderreceiveraddress;
    private android.widget.Button fillorderwaybtn;
    private android.widget.TextView fillorderwaypay;
    private android.widget.TextView fillorderwaydelivery;
    private android.widget.TextView fillorderwaytime;
    private android.widget.Button fillorderinvoicebtn;
    private android.widget.TextView fillorderinvoicetype;
    private android.widget.TextView fillorderinvoicebegin;
    private android.widget.TextView fillorderinvoicecontent;
    private android.widget.ToggleButton fillorderintegralswich;
    private android.widget.TextView fillorderintegraltotal;
    private android.widget.EditText fillorderintegralinput;
    private android.widget.TextView fillorderintegralexchange;
    private android.widget.Button fillorderintegralbtn;
    //add by zhyao @2015/5/6  声明红包相关控件
    private android.widget.ToggleButton fillorderredpacketswitch;//红包开关
    private android.widget.TextView fillorderredpackettotal;//红包总额
    private android.widget.EditText fillorderredpacketinput;//红包输入框 
    private android.widget.TextView fillorderredpacketexchange;//红包可抵金额
    private android.widget.Button fillorderredpacketbtn;//红包确定 
    private android.widget.Button fillorderhumoneybtn;
    private android.widget.TextView fillorderhumoneytotal;
    private android.widget.RelativeLayout fillorderdetailsrl;
    private android.widget.TextView fillorderaccountfreight;
    private android.widget.TextView fillorderaccountproducttip;
    private android.widget.TextView fillorderaccountproduct;
    private android.widget.TextView fillorderaccountdeliverytip;
    private android.widget.TextView fillorderaccountdelivery;
    private android.widget.TextView fillorderaccountintegraltip;
    private android.widget.TextView fillorderaccountintegral;
    private android.widget.TextView fillorderaccounthumoneytip;
    //add by zhyao @2015/5/6  声明红包相关控件
    private android.widget.TextView fillorderaccountredpacket;//红包 - 
    private android.widget.TextView fillorderaccounthumoney;
    private android.widget.TextView fillorderaccountalltip;
    private android.widget.TextView fillorderaccountall;
    private android.widget.ScrollView fillordersv;
    private android.widget.TextView fillorderaccountallconfirm;
    private android.widget.Button fillordersubmit;
    private android.widget.RelativeLayout fillorderbottom;
    //积分抵扣,红包抵扣,虎券抵扣区域
    private LinearLayout integral_area;
    private LinearLayout red_packet_area;
    private LinearLayout humoney_area;
    //积分抵扣,红包抵扣,虎券抵扣结算信息
    private RelativeLayout pay_integral_area;
    private RelativeLayout pay_red_packet_area;
    private RelativeLayout pay_humoney_area;

    private RelativeLayout fill_order_integral_rl;
    private View fill_order_integral_line;
    private TextView fill_order_integral_warn;
    private TextView fill_order_account_discount;
    //add by zhyao @2015/5/6  声明红包相关控件
    private RelativeLayout fill_order_red_packet_rl;
    private View fill_order_red_packet_line;
    private TextView fill_order_red_packet_warn;
    private FillOrderBean mFillOrderBean;
    private static int request_addr = 1, request_pay = 11,
            request_invoice = 12, request_lhj = 13, request_addr_unlogin = 14,
            request_invoice_unlogin = 15, request_addr_init = 16,
            request_detail = 17;
    /**
     * ****************************提交订单参数*****************************
     */
    private String shopIds = "-1";                // 购物车ids
    private int type;                             // 提交类型//-1.购物车商品 3、普通商品 5、团购 6、秒杀 7、分销 8、套餐 9、闪购 10、礼品兑换 11、天天低价
    private int points_deduction = 0;             // 积分输入值
    //add by zhyao @2015/5/6  声明红包抵扣金额
    private double redpacket_deduction = 0.00;    // 红包输入值
    private String remarksJson = "";              // 备注 格式：[{ REMARKS:备注内容,STORE_ID:店铺id }, { REMARKS:备注内容,STORE_ID:店铺id }]
    private String addressId="-1";                // 地址（地址ID）
    private String invoiceId = "-1";              // 发票ID
    private String lh_id = "-1";                  // 乐虎券（乐虎劵ID）
    private String origin = "1";                  // 0:PC端 ,1:Android端，2:点单端, 3:IOS 4:Wap端 5:微信端
    // modify by zhyao @2015/6/12 添加3
    private String shipping_method = "2";         // 送货方式：1上门自提 , 2送货/服务上门 , 3消费卷
    private String deliveryTime = "";             // 送货时间/提货时间
    private int nearbyId = -1;                    // 附近门店（ID）
    private String staffId="";                    // 快递员工号

    /**
     * ****************************END*****************************
     */
    private String send;
    private String payWayId = "1";
    private String sendWayId = "1";
    private String hujuan_value = "0.00";
    private double exemption = 38;
    private double hujuanMoney;
    private double allPay;
    private double ratio;
    private double myFreight;
    private double goodsSumPrice;
    private String addressName;
    private boolean canChangeInvoiceTitle=true;
    private int seletedInvoice=0;
    //add by zhyao @2015/6/12  添加是否是消费卷
	private boolean isAllConsumption = false;
	 //add by zhyao @2015/6/23  详细地址
	private String addressDetail;
    private int allIntegral;  //实物兑换需要的积分
    private ConfirmDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //阻止EditText自动让输入法弹出
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_fill_order);
        findView();
        init();
        setListener();
    }

    private void init() {
        setTitle("填写订单");
        mFillOrderBean = (FillOrderBean) getIntent().getSerializableExtra(
                "fill_order_bean");
        type=getIntent().getIntExtra("type",-1);
        allIntegral=getIntent().getIntExtra("integral",0);
        if (mFillOrderBean != null) {
        	//add by zhyao @2015/6/12  添加是否是消费卷
        	isAllConsumption = mFillOrderBean.getCart().isAllConsumptionGoods();
            FillOrderBean.BaseInfoEntity.AddressMapEntity mAddressMapEntity = mFillOrderBean
                    .getBaseInfo().getAddressMap();
            if (mAddressMapEntity != null) {
                addressId = mAddressMapEntity.getID();
                //add by zhyao @2015/6/24   添加详细地址
                addressDetail = mAddressMapEntity.getADDRESS();
                setReciever(mAddressMapEntity.getNAME(),
                        mAddressMapEntity.getPHONE(),
                        mAddressMapEntity.getADDRESS());
            } else {
            	Intent i = new Intent();
            	//modify by zhyao @2015/6/30  收货地址为空，全为消费券则跳转到收获地址管理页面选择默认地址，否则跳转地址编辑页面填写地址信息
                if(isAllConsumption) {
                	//编辑收货人
                	i.setClass(this, AddressManagementActivity.class);
                	i.putExtra(AddressManagementActivity.TAG, AddressManagementActivity.order);
                	i.putExtra("orderPrice", mFillOrderBean.getCart().getGoodsPriceSumStr());
                	i.putExtra("freight", mFillOrderBean.getBaseInfo().getGoodsSumfreightStr());
                	i.putExtra("shopIds", getShoppingIds());
                	i.putExtra("addressId", mFillOrderBean.getBaseInfo()
                			.getAddressMap() == null ? "-1" : mFillOrderBean.getBaseInfo()
                					.getAddressMap().getID());
                	// add by zhyao @2015/6/22 添加是不是全部消费卷字段
                	i.putExtra("isAllConsumption", isAllConsumption);
                	startActivityForResult(i, request_addr);
                } else {
                    i.setClass(this, AddressModifyActivity.class);
                	i.putExtra(AddressModifyActivity.TAG,
                			AddressModifyActivity.INIT);
                	// add by zhyao @2015/6/22 添加是不是全部消费卷字段
                	i.putExtra("isAllConsumption", isAllConsumption);
                	startActivityForResult(i, request_addr_init);
                }
            }

            setWay(null, null, null);
            setInvoice(null, null, null);
            setIntegral(true,-1);
            //add by zhyao @2015/5/6  设置红包金额
            setRedPacket(-1);
            setHuMoney(null);
            allPay = Utils.anDouble(mFillOrderBean.getCart().getOrderPriceStr());
            myFreight = Utils.anDouble(mFillOrderBean.getBaseInfo().getGoodsSumfreightStr());
            //modify by zhyao @2015/5/6  添加红包参数
            setPayInfo(exemption, myFreight, points_deduction, redpacket_deduction, hujuanMoney, allPay);
            deliveryTime = mFillOrderBean.getBaseInfo().getDeliveryTime();
            addressName=mFillOrderBean.getBaseInfo().getAddressMap()==null?
                    "":mFillOrderBean.getBaseInfo().getAddressMap().getNAME();
            setInvoice(null,addressName,null);
            
            //add by zhyao @2015/6/12  添加是否是消费卷
        	if(isAllConsumption) {
        		shipping_method = "3";//运送方式消费卷模式
        		fillorderwaybtn.setVisibility(View.GONE);
        		setWay(null, "消费券商品", null);
        	}
        	else {
        		fillorderwaybtn.setVisibility(View.VISIBLE);
        	}
            //实物礼品兑换隐藏抵扣优惠
            if (hideDiscountArea(mFillOrderBean.getShoppingType())){
                points_deduction=allIntegral;
                setPayInfo(exemption, myFreight, points_deduction, redpacket_deduction, hujuanMoney, allPay);
            }
        }
    }

    private void setListener() {
        fillorderreceiverbtn.setOnClickListener(this);
        fillorderwaybtn.setOnClickListener(this);
        fillorderinvoicebtn.setOnClickListener(this);
        fillorderintegralbtn.setOnClickListener(this);
        //add by zhyao @2015/5/6  添加红包确定监听事件
        fillorderredpacketbtn.setOnClickListener(this);
        fillorderhumoneybtn.setOnClickListener(this);
        fillorderdetailsrl.setOnClickListener(this);
        fillordersubmit.setOnClickListener(this);
        fillorderintegralswich
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean b) {
                        if (b) {
                            setVisible(View.VISIBLE);
                        } else {
                            fillorderintegralinput.setText("");
                            fillorderintegralexchange.setText(Html
                                    .fromHtml("可抵"
                                            + changeStringColor("#FE0000", "￥0")
                                            + "元"));
                            //modify by zhyao @2015/5/6  添加红包参数
                            calculatShowMoney(-1, -1, 0, -1, -1);
                            setVisible(View.GONE);
                        }
                    }
                });
        //add by zhyao @2015/5/6  设置红包开关监听事件
        fillorderredpacketswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton,
                                         boolean b) {
                if (b) {
                    setRedpacketVisible(View.VISIBLE);
                } else {
                    fillorderredpacketinput.setText("");
                    fillorderredpacketexchange.setText(Html
                            .fromHtml("可抵"
                                    + changeStringColor("#FE0000", "￥0")
                                    + "元"));
                    calculatShowMoney(-1, -1, 0, -1, -1);
                    setRedpacketVisible(View.GONE);
                }
            }
        });
        fillorderhumoneybtn.setOnClickListener(this);
    }

    private void findView() {
        findViewById(R.id.right_ib).setVisibility(View.GONE);
        this.fillorderbottom = (RelativeLayout) findViewById(R.id.fill_order_bottom);
        this.fillordersubmit = (Button) findViewById(R.id.fill_order_submit);
        this.fillorderaccountallconfirm = (TextView) findViewById(R.id.fill_order_account_all_confirm);
        this.fillordersv = (ScrollView) findViewById(R.id.fill_order_sv);
        this.fillorderaccountall = (TextView) findViewById(R.id.fill_order_account_all);
        this.fillorderaccountalltip = (TextView) findViewById(R.id.fill_order_account_all_tip);
        this.fillorderaccounthumoney = (TextView) findViewById(R.id.fill_order_account_humoney);
        this.fillorderaccounthumoneytip = (TextView) findViewById(R.id.fill_order_account_humoney_tip);
        this.fillorderaccountintegral = (TextView) findViewById(R.id.fill_order_account_integral);
        this.fillorderaccountintegraltip = (TextView) findViewById(R.id.fill_order_account_integral_tip);
        this.fillorderaccountdelivery = (TextView) findViewById(R.id.fill_order_account_delivery);
        this.fillorderaccountdeliverytip = (TextView) findViewById(R.id.fill_order_account_delivery_tip);
        this.fillorderaccountproduct = (TextView) findViewById(R.id.fill_order_account_product);
        this.fillorderaccountproducttip = (TextView) findViewById(R.id.fill_order_account_product_tip);
        this.fillorderaccountfreight = (TextView) findViewById(R.id.fill_order_account_freight);
        this.fillorderdetailsrl = (RelativeLayout) findViewById(R.id.fill_order_details_rl);
        this.fillorderhumoneytotal = (TextView) findViewById(R.id.fill_order_humoney_total);
        this.fillorderhumoneybtn = (Button) findViewById(R.id.fill_order_humoney_btn);
        this.fillorderintegralbtn = (Button) findViewById(R.id.fill_order_integral_btn);
        this.fillorderintegralexchange = (TextView) findViewById(R.id.fill_order_integral_exchange);
        this.fillorderintegralinput = (EditText) findViewById(R.id.fill_order_integral_input);
        this.fillorderintegraltotal = (TextView) findViewById(R.id.fill_order_integral_total);
        this.fillorderintegralswich = (ToggleButton) findViewById(R.id.fill_order_integral_swich);
        this.fillorderinvoicecontent = (TextView) findViewById(R.id.fill_order_invoice_content);
        this.fillorderinvoicebegin = (TextView) findViewById(R.id.fill_order_invoice_begin);
        this.fillorderinvoicetype = (TextView) findViewById(R.id.fill_order_invoice_type);
        this.fillorderinvoicebtn = (Button) findViewById(R.id.fill_order_invoice_btn);
        this.fillorderwaytime = (TextView) findViewById(R.id.fill_order_way_time);
        this.fillorderwaydelivery = (TextView) findViewById(R.id.fill_order_way_delivery);
        this.fillorderwaypay = (TextView) findViewById(R.id.fill_order_way_pay);
        this.fillorderwaybtn = (Button) findViewById(R.id.fill_order_way_btn);
        this.fillorderreceiveraddress = (TextView) findViewById(R.id.fill_order_receiver_address);
        this.fillorderreceiverphone = (TextView) findViewById(R.id.fill_order_receiver_phone);
        this.fillorderreceivername = (TextView) findViewById(R.id.fill_order_receiver_name);
        this.fillorderreceiverbtn = (Button) findViewById(R.id.fill_order_receiver_btn);
        fill_order_integral_rl = (RelativeLayout) findViewById(R.id.fill_order_integral_rl);
        fill_order_integral_line = findViewById(R.id.fill_order_integral_line);
        fill_order_integral_warn = (TextView) findViewById(R.id.fill_order_integral_warn);
        //add by zhyao @2015/5/6  声明红包相关控件
        this.fillorderaccountredpacket = (TextView) findViewById(R.id.fill_order_account_red_packet);
        this.fillorderredpacketbtn = (Button) findViewById(R.id.fill_order_red_packet_btn);
        this.fillorderredpacketexchange = (TextView) findViewById(R.id.fill_order_red_packet_exchange);
        this.fillorderredpacketinput = (EditText) findViewById(R.id.fill_order_red_packet_input);
        this.fillorderredpackettotal = (TextView) findViewById(R.id.fill_order_red_packet_total);
        this.fillorderredpacketswitch = (ToggleButton) findViewById(R.id.fill_order_red_packet_swich);
        fill_order_red_packet_rl = (RelativeLayout) findViewById(R.id.fill_order_red_packet_rl);
        fill_order_red_packet_line = findViewById(R.id.fill_order_red_packet_line);
        fill_order_red_packet_warn = (TextView) findViewById(R.id.fill_order_red_packet_warn);
        fill_order_account_discount= (TextView) findViewById(R.id.fill_order_account_discount);
        //积分抵扣,红包抵扣,虎券抵扣区域
        integral_area= (LinearLayout) findViewById(R.id.integral_area);
        red_packet_area= (LinearLayout) findViewById(R.id.red_packet_area);
        humoney_area= (LinearLayout) findViewById(R.id.humoney_area);
        pay_integral_area= (RelativeLayout) findViewById(R.id.pay_integral_area);
        pay_red_packet_area= (RelativeLayout) findViewById(R.id.pay_red_packet_area);
        pay_humoney_area= (RelativeLayout) findViewById(R.id.pay_humoney_area);
    }

    private void setVisible(int visible) {
        fill_order_integral_rl.setVisibility(visible);
        fill_order_integral_line.setVisibility(visible);
        fill_order_integral_warn.setVisibility(visible);
    }
    
    //add by zhyao @2015/5/6  显示/隐藏红包相关信息
    private void setRedpacketVisible(int visible) {
    	fill_order_red_packet_rl.setVisibility(visible);
    	fill_order_red_packet_line.setVisibility(visible);
    	fill_order_red_packet_warn.setVisibility(visible);
    }

    /**
     * 设置收货人信息
     */
    private void setReciever(String name, String phone, String address) {
        fillorderreceivername.setText(Html.fromHtml("收货人: "
                + changeStringColor("#000000", name)));
        fillorderreceiverphone.setText(Html.fromHtml("手机号码: "
                + changeStringColor("#000000", phone)));
        // modify by zhyao @2015/7/6 添加服务地址
        fillorderreceiveraddress.setText(Html.fromHtml("收货/服务地址: "
                + changeStringColor("#000000", address)));
        //add by zhyao @2015/6/25  全是消费卷，隐藏收获地址
        if(isAllConsumption) {
        	fillorderreceiveraddress.setVisibility(View.GONE);
        }
    }

    /**
     * 设置配送方式
     */
    private void setWay(String payWay, String deliverWay, String deliverTime) {
        fillorderwaypay.setText(Html
                .fromHtml("支付方式: "
                        + changeStringColor("#000000", payWay == null ? PayWayActivity.DEFAULT_PAY
                        : payWay)));
        // modify by zhyao @2015/7/7 添加服务地址
        fillorderwaytime.setText(Html.fromHtml("配送/服务方式: "
                + changeStringColor("#000000", deliverWay == null ? PayWayActivity.DEFAULT_DELIVER
                : deliverWay)));
        // modify by zhyao @2015/7/7 添加服务地址
        fillorderwaydelivery.setText(Html.fromHtml("送货/服务时间: "
                + changeStringColor("#000000",
                deliverTime == null ? mFillOrderBean.getBaseInfo()
                        .getDeliveryTime() : deliverTime)));
    }

    /**
     * 设置发票
     */
    private void setInvoice(String type, String begin, String content) {
        fillorderinvoicetype.setText(Html.fromHtml("发票类型: "
                + changeStringColor("#000000", type == null ? "普通发票" : type)));
        fillorderinvoicebegin.setText(Html.fromHtml("发票抬头: "
                + changeStringColor("#000000", begin == null||begin.equals("") ? "个人" : begin)));
        fillorderinvoicecontent.setText(Html
                .fromHtml("发票内容: "
                        + changeStringColor("#000000", content == null ? "明细"
                        : content)));
    }

    /**
     * 设置积分抵扣
     */
    private void setIntegral(boolean isFrist,int deduction) {
        if (mFillOrderBean.getBaseInfo().getFLAG_INTEGRAL_DEDUCTION()
                .equals("0")&&isFrist) {
            // 没有开启积分抵扣
            fillorderintegralswich.setChecked(false);
            setVisible(View.GONE);
        }
        ratio = 1;
        String integral = mFillOrderBean.getBaseInfo().getINTEGRAL();
        String cash = mFillOrderBean.getBaseInfo().getCASH();
        if (!TextUtils.isEmpty(integral) && !TextUtils.isEmpty(cash)) {
            double d_integral = Double.parseDouble(integral);
            double d_cash = Double.parseDouble(cash);
            ratio = d_integral / d_cash;
        }
        String allintegral = mFillOrderBean.getBaseInfo().getUSER_INTEGRAL();
        double d_allintegral = TextUtils.isEmpty(allintegral) ? 0 : Double
                .parseDouble(allintegral);
        fillorderintegraltotal.setText(Html.fromHtml("共"
                + changeStringColor("#FE0000", MathUtil.keepMost2Decimal(d_allintegral) + "")
                + "个积分可用，抵"
                + changeStringColor("#FE0000", "￥" + MathUtil.keep2decimal(d_allintegral / ratio)
                + "元")));
        fillorderintegralexchange
                .setText(Html.fromHtml("可抵" + changeStringColor("#FE0000",
                        "￥" + (deduction < 0 ? 0 : MathUtil.keep2decimal(deduction / ratio))) + "元"));

    }
    
    //add by zhyao @2015/5/6
    /**
     * 设置红包抵扣
     */
    private void setRedPacket(double redpacket) {
    	String totalRedpacket = mFillOrderBean.getBaseInfo().getUSER_BONUS();
    	String canUseRedpacket = mFillOrderBean.getBaseInfo().getTotalBonus();
    	Log.d(TAG, "setRedPacket : canUseRedpacket = " + canUseRedpacket);
    	
    	//modify by zhyao @2015/6/10  修改红包提示
    	fillorderredpackettotal.setText(Html.fromHtml("您账户共有"
                + changeStringColor("#FE0000", totalRedpacket)
                + "元红包，本次订单可抵用红包"
                + changeStringColor("#FE0000", "￥" + canUseRedpacket)
                + "元"));
        fillorderredpacketexchange
                .setText(Html.fromHtml("可抵"
                        + changeStringColor("#FE0000",
                        "￥"
                                + (redpacket < 0 ? 0
                                : redpacket)) + "元"));
    }

    /**
     * 设置虎券抵扣
     */
    private void setHuMoney(String money) {
        String template = "";
        if (!TextUtils.isEmpty(money) && !money.equals("0.00")) {
            template = "，打算使用一张面值为￥" + money + "元的虎券";
        }
        fillorderhumoneytotal.setText(Html.fromHtml("共"
                + changeStringColor("#3592e2", mFillOrderBean.getBaseInfo()
                .getTICKET_NUM()) + "张可使用虎券" + template));

    }

    //modify by zhyao @2015/5/6  添加红包参数
    /**
     * 设置结算信息
     */
    private void setPayInfo(double exemption, double deliverMoney,
                            double integral, double redpacket, double hu, double all) {
        fillorderaccountfreight.setText("{满" + exemption + "元免运费}");
        fillorderaccountproduct.setText("￥"
                + mFillOrderBean.getCart().getGoodsPriceSumStr());
        fill_order_account_discount.setText("-       ￥"
                + MathUtil.priceForAppWithOutSign(mFillOrderBean.getCart().getPreferentialPriceSumStr()));
        if (shipping_method.equals("2")) {
            //送货上门
            fillorderaccountdelivery.setText("+       ￥" + (MathUtil.priceForAppWithOutSign(deliverMoney)));
        } else {
            //上门自提
            fillorderaccountdelivery.setText("+       ￥0.00");
        }

        if (mFillOrderBean.getShoppingType().equals("10")){
            //实物礼品兑换,就直接显示需要的积分
            fillorderaccountintegral.setText(MathUtil.priceForAppRound(integral+""));
        }else{
            fillorderaccountintegral.setText("-       ￥" + (MathUtil.priceForAppWithOutSign(integral)));
        }
        //add by zhyao @2015/5/6  显示红包抵扣金额
        fillorderaccountredpacket.setText("-       ￥" + (MathUtil.priceForAppWithOutSign(redpacket)));
        fillorderaccounthumoney.setText("-       ￥" + MathUtil.priceForAppWithOutSign(hu));
        fillorderaccountall.setText((Html.fromHtml(changeStringColor(
                "#FE0000", "￥" + ((all <= 0 ? "0.00" : String.format("%.2f", allPay)))))));
        fillorderaccountallconfirm.setText((Html.fromHtml("应付金额: "
                + changeStringColor("#FE0000", "￥"
                + (all <= 0 ? "0.00" : String.format("%.2f", allPay))))));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fill_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String changeStringColor(String color, String content) {
        return "<font color='" + color + "'>" + content + "</font>";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fill_order_receiver_btn:
                //编辑收货人
                Intent i = new Intent();
                i.setClass(this, AddressManagementActivity.class);
                i.putExtra(AddressManagementActivity.TAG,
                        AddressManagementActivity.order);
                i.putExtra("orderPrice", mFillOrderBean.getCart().getGoodsPriceSumStr());
                i.putExtra("freight", mFillOrderBean.getBaseInfo().getGoodsSumfreightStr());
                i.putExtra("shopIds", getShoppingIds());
                i.putExtra("addressId", mFillOrderBean.getBaseInfo()
                        .getAddressMap() == null ? "-1" : mFillOrderBean.getBaseInfo()
                        .getAddressMap().getID());
                // add by zhyao @2015/6/22 添加是不是全部消费卷字段
                i.putExtra("isAllConsumption", isAllConsumption);
                startActivityForResult(i, request_addr);
                break;
            case R.id.fill_order_way_btn:
                //编辑支付和配送方式
                Intent pay_intent = new Intent();
                pay_intent.setClass(this, PayWayActivity.class);
                pay_intent.putExtra("sendWayId", sendWayId);
                pay_intent.putExtra("shopId", nearbyId);
                pay_intent.putExtra("send", send);
                pay_intent.putExtra("deliveryTime", deliveryTime);
                startActivityForResult(pay_intent, request_pay);
                break;
            case R.id.fill_order_invoice_btn:
                //编辑发票信息
                Intent invoice_intent = new Intent();
                invoice_intent.setClass(this, InvoiceInfoActivity.class);
                invoice_intent
                        .putExtra(InvoiceInfoActivity.INTENT_KEY_FLAG, Integer
                                .parseInt(mFillOrderBean.getBaseInfo()
                                        .getFLAG_VAT()));
                invoice_intent.putExtra(InvoiceInfoActivity.INTENT_KEY_FLAG1,
                        Integer.parseInt(mFillOrderBean.getBaseInfo().getVAT()));
                invoice_intent.putExtra(InvoiceInfoActivity.INTENT_KEY_FLAG2,
                        seletedInvoice);
                if (!TextUtils.isEmpty(addressName)){
                    invoice_intent.putExtra("addressName", addressName);
                }
                startActivityForResult(invoice_intent, request_invoice);
                break;
            case R.id.fill_order_integral_btn:
                //确认输入的积分
                if (mFillOrderBean.getBaseInfo().getFLAG_INTEGRAL_DEDUCTION()
                        .equals("0")){
                    UIHelper.showToast("暂时没开启积分抵扣");
                    return;
                }
                setIntegralLimit();
                break;
            //add by zhyao @2015/5/6  红包确定事件
            case R.id.fill_order_red_packet_btn://红包确定
            	//确认输入的红包
                setRedpacketLimit();
                break;
            case R.id.fill_order_humoney_btn:
                //编辑虎券抵扣
                Intent hu_intent = new Intent();
                hu_intent.setClass(this, HuJuanDeductionActivity.class);
                hu_intent.putExtra("lh_id", lh_id);
                hu_intent.putExtra("hujuan_value", hujuan_value);
                hu_intent.putExtra("total", mFillOrderBean.getCart().getGoodsPriceSumStr());
                hu_intent.putExtra("thirdIds", mFillOrderBean.getCart().getThridIds());
                hu_intent.putExtra("brandIds", mFillOrderBean.getCart().getBrandIds());
                hu_intent.putExtra("goodIds", mFillOrderBean.getCart().getGoodsIds());
                hu_intent.putExtra("num", mFillOrderBean.getBaseInfo().getTICKET_NUM());
                hu_intent.putExtra("storeIds", mFillOrderBean.getCart().getStoreIds());
                startActivityForResult(hu_intent, request_lhj);
                break;
            case R.id.fill_order_details_rl:
                //查看商品清单
                Intent mIntent = new Intent(this, FillOrderGoodsListActivity.class);
                mIntent.putExtra(FillOrderGoodsListActivity.GOODS_LIST,
                        mFillOrderBean.getCart());
                mIntent.putExtra(FillOrderGoodsListActivity.REMARK, remarksJson);
                startActivityForResult(mIntent, request_detail);
                break;
            case R.id.fill_order_submit:
                //提交订单
                submit();
                break;
        }
    }

    /**
     * 设置积分抵扣的限制规制
     */
    private void setIntegralLimit() {
        if (!TextUtils.isEmpty(fillorderintegralinput.getText().toString())) {
            if (mFillOrderBean.getBaseInfo().getFLAG_INTEGRAL_DEDUCTION()
                    .equals("0")) {
                UIHelper.showToast("现在没有开启积分抵扣");
                return;
            }
            // 最多使用积分
            int limit = Utils.anInt(mFillOrderBean.getBaseInfo()
                    .getINTEGRAL_UPPER_LIMIT());
            // 输入的积分
            int input = Utils.anInt(fillorderintegralinput.getText()
                    .toString());
            // 用户的所有积分
            int all = Utils.anInt(mFillOrderBean.getBaseInfo()
                    .getUSER_INTEGRAL());
            // 最少达到的总商品金额
            double least = Utils.anDouble(mFillOrderBean.getBaseInfo()
                    .getORDER_CASH());
            // 本订单的商品总金额
            double price = Utils.anDouble(mFillOrderBean.getCart().getGoodsPriceSumStr());
            // 本订单的商品已优惠的金额
            double preferential_price = Utils.anDouble(mFillOrderBean.getCart().getPreferentialPriceSumStr());
            // 本次订单应付的总金额(除开虎券抵扣和红包抵扣,商品优惠后的总金额+当前运费)
            double shouldPay = price - preferential_price + myFreight;

            // 只有使用按金额使用积分抵扣才有最少金额限制
            if (mFillOrderBean.getBaseInfo().getFLAG_INTEGRAL_DEDUCTION()
                    .equals("1")
                    && least > shouldPay) {
                UIHelper.showToast("您的订单总额没有满" + least + "元无法使用积分抵扣");
                return;
            }
            if (input > all) {
                UIHelper.showToast("不能超过你共有的" + all + "个积分");
                return;
            }
//            if (input/ratio > price&&preferential_price<=0) {
//                UIHelper.showToast("抵扣金额不能超过商品总金额" + price + "元");
//                return;
//            }
            if (input/ratio > shouldPay) {
                UIHelper.showToast("抵扣金额不能超过应付总金额" + shouldPay+ "元");
                return;
            }
            // 按金额
            if (mFillOrderBean.getBaseInfo().getFLAG_INTEGRAL_DEDUCTION()
                    .equals("1")) {
                if (input > limit) {
                    UIHelper.showToast("按金额最多不能超过" + limit + "个积分");
                    return;
                }
            } else if (mFillOrderBean.getBaseInfo()
                    .getFLAG_INTEGRAL_DEDUCTION().equals("2")) {
                // 按比例最多可以使用积分抵扣的金额,单位元
                double mostDiscount = Utils.anDouble(mFillOrderBean.getBaseInfo().getMAX_UPPER_LIMIT());
                if(input/ratio>mostDiscount){
                    UIHelper.showToast("按比例抵扣的金额最多不能超过" + MathUtil.priceForAppWithOutSign(mostDiscount) + "元");
                    return;
                }
                // 按商品优惠后的总金额+当前运费 的比例抵扣的金额
                limit = (int)((limit / 100.00) * shouldPay * ratio);
                if (input > limit) {
                    UIHelper.showToast("按比例最多不能超过" + limit + "个积分");
                    return;
                }
            }
            setIntegral(false, input);
            calculatShowMoney(-1, -1, input, -1, -1);
        }
    }
    
    //add by zhyao @2015/5/6  设置红包限制
    /**
     * 设置红包抵扣的限制规制
     */
    private void setRedpacketLimit() {
        if (!TextUtils.isEmpty(fillorderredpacketinput.getText().toString())) {
           
            // 最多使用红包
            double limit = Utils.anDouble(mFillOrderBean.getBaseInfo().getTotalBonus());
            // 输入的红包（保留到小数点后两位）
            double input = Utils.anDouble(MathUtil.priceForAppWithOutSign(fillorderredpacketinput.getText().toString()));
            // 用户的所有红包
            double all = Utils.anDouble(mFillOrderBean.getBaseInfo().getUSER_BONUS());
            // 商品总价
            double price = Utils.anDouble(mFillOrderBean.getCart().getGoodsPriceSumStr());
            Log.d(TAG, "setRedpacketLimit : limit = " + limit + " price = " + price + " all = " + all);
            
            //不能超过用户拥有的红包总额
            if (input > all) {
                UIHelper.showToast("不能超过你共有的" + all + "个红包");
                return;
            }
            // 不能超过最大限制使用的红包数
            if (input > limit) {
                UIHelper.showToast("最多不能超过" + limit + "个红包");
                return;
            }
            //modify by zhyao @2015/6/26 修改商品总额
            // 不能超过商品总金额
            if (input > price) {
            	 UIHelper.showToast("抵扣金额不能超过该订单的总金额" + price + "元");
            	 return;
            }
            setRedPacket(input);
            calculatShowMoney(-1, -1, -1, -1, input);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_addr && resultCode == RESULT_OK) {
            //地址管理界面返回的结果
            dealWithAddressManagerResult(data);
        } else if (requestCode == request_addr_init && resultCode == RESULT_OK) {
            //地址新增返回结果
            dealWithAddressModifyResulr(data);
        } else if (requestCode == request_pay && resultCode == RESULT_OK) {
            //支付和配送方式返回结果
            dealWithPayWayResult(data);
        } else if (requestCode == request_invoice && resultCode == RESULT_OK) {
            //发票返回结果
            dealWithInvoiceResult(data);
        } else if (requestCode == request_lhj && resultCode == RESULT_OK) {
            //虎券返回结果
            dealWithHuquanResult(data);
        } else if (requestCode == request_detail && resultCode == RESULT_OK) {
            //商品清单返回结果
            if (data.getStringExtra("remarksJson") != null) {
                remarksJson = data.getStringExtra("remarksJson");
            }
        }
    }
    /**
     * 处理虎券返回结果
     * @param data  返回的Intent数据
     */
    private void dealWithHuquanResult(Intent data) {
        lh_id = data.getStringExtra("lhj_value_id");
        if (!TextUtils.isEmpty(data.getStringExtra("lhj_value_price"))) {
            hujuan_value = data.getStringExtra("lhj_value_price");
            // modify by zhyao @2015/5/6  添加红包参数
            calculatShowMoney(-1,-1,-1, Utils.anDouble(hujuan_value), -1);
        }
        if (!TextUtils.isEmpty(data.getStringExtra("lhj_num"))) {
            mFillOrderBean.getBaseInfo().setTICKET_NUM(
                    data.getStringExtra("lhj_num"));
        }
        setHuMoney(hujuan_value);
    }

    /**
     * 处理发票返回结果
     * @param data  返回的Intent数据
     */
    private void dealWithInvoiceResult(Intent data) {
        invoiceId = data.getStringExtra("invoice_id");
        String invoice_way = data.getStringExtra("invoice_way");
        String invoice_title = data.getStringExtra("invoice_title");
        seletedInvoice=data.getIntExtra("invoice_flag2", 0);
        if (invoice_way.equals("1")) {
            setInvoice("增值发票", invoice_title, "明细");
        } else {
            setInvoice("普通发票", invoice_title, "明细");
        }
        canChangeInvoiceTitle=false;
    }

    /**
     *  支付和配送方式返回结果
     * @param data 返回的Intent数据
     */
    private void dealWithPayWayResult(Intent data) {
        payWayId = data.getStringExtra("payWayId");
        sendWayId = data.getStringExtra("sendWayId");
        String payId = data.getStringExtra("pay");
        send = data.getStringExtra("send");
        String sendTime = data.getStringExtra("sendtime");
        nearbyId = data.getIntExtra("shopId", -1);
        staffId = data.getStringExtra("staffId");
        setWay(payId, send, sendTime);
        deliveryTime = sendTime;
        if (send.equals(PayWayActivity.DEFAULT_DELIVER)) {
            //送货/服务上门
            shipping_method = "2";
        } else {
            //上门自提
            shipping_method = "1";
            //由于上门自提没有运费，导致总金额变化，已选积分会被清零
            clearInputIntegral();
        }
        // modify by zhyao @2015/5/6  添加红包参数
        calculatShowMoney(-1, -1, -1, -1, -1);
    }

    /**
     * 清空已选积分抵扣
     */
    private void clearInputIntegral() {
        if(!mFillOrderBean.getShoppingType().equals("10")) {//排除实物礼品积分兑换的情况
            points_deduction = 0;
            fillorderintegralinput.setText("");
            setIntegral(false, -1);
        }
    }

    /**
     * 处理地址新增界面返回的结果
     * @param data 返回的Intent数据
     */
    private void dealWithAddressModifyResulr(Intent data) {
        AddressItem addrItem = (AddressItem) data
                .getSerializableExtra("addr");
        
        //记住地址ID，提交的时候上传
        addressId = addrItem.ADDRESSID;
        addressName=addrItem.CONSIGNEE_NAME;
        
        //显示地址
        addressDetail = addrItem.LEVELADDR + addrItem.ADDRESS;
        setReciever(addrItem.CONSIGNEE_NAME, addrItem.CONSIGNEE_PHONE, addressDetail);
    }

    /**
     * 处理地址管理界面返回的结果
     * @param data 返回的Intent数据
     */
    private void dealWithAddressManagerResult(Intent data) {
        if (data.getSerializableExtra("addr")!=null) {
            AddressItem addrItem = (AddressItem) data
                    .getSerializableExtra("addr");
            // 显示省市区
            String addre;
            // add by zhyao @2015/6/24 商品全部为消费卷时，地址可以为空
            if(TextUtils.isEmpty(addrItem.LEVELADDR)) {
            	addre = "";
            }
            else if (addrItem.ADDRESS.startsWith(addrItem.LEVELADDR)) {
                addre = addrItem.ADDRESS;
            } else {
                addre = addrItem.LEVELADDR + addrItem.ADDRESS;
            }
            //add by zhyao @2015/6/24   添加详细地址
            addressDetail = addre;
            setReciever(addrItem.CONSIGNEE_NAME, addrItem.CONSIGNEE_PHONE,
                    addre);
            //切换收货地址可能改变运费，导致总金额变化，已选积分会被清零
            clearInputIntegral();
            // modify by zhyao @2015/5/6  添加红包参数
            calculatShowMoney(-1, data.getDoubleExtra("goodsSumfreightStr", 0.00), -1, -1, -1);
            addressId = addrItem.ADDRESSID;
            addressName=addrItem.CONSIGNEE_NAME;
            if (canChangeInvoiceTitle){
                setInvoice(null,addressName,null);
            }
        } else {
            if (data.getIntExtra("goodsSumfreightStr",0)==-1){
                setReciever("", "", "");
                addressId="-1";
                addressName="";
                if (canChangeInvoiceTitle){
                    setInvoice(null,addressName,null);
                }
            }
        }
    }

    /**
     * 提交订单
     */
    private void submit() {
        if (AppContext.userId != null) {
            if (addressId.equals("-1")) {
                UIHelper.showToast("请选择一个收获地址");
                return;
            }
            // add by zhyao @2015/6/22 如果有实体商品，判断收获地址，没有则提示地址不全请补充完整
            if(!isAllConsumption && TextUtils.isEmpty(addressDetail)) {
            	UIHelper.showToast("请补充完整详细地址");
            	return;
            }
            getShoppingIds();
            // modify by zhyao @2015/5/6  添加红包参数
            String infojson = "{"
                    + String.format(
                    "\"userId\":\"%s\",\"shopIds\":\"%s\",\"addressId\":\"%s\",\"invoiceId\":\"%s\",\"points_deduction\":\"%s\",\"redpacket_deduction\":\"%s\","
                            + "\"remarksJson\":\"%s\",\"lh_id\":\"%s\",\"origin\":\"%s\",\"shipping_method\":\"%s\",\"deliveryTime\":\"%s\","
                            + "\"nearbyId\":\"%s\",\"staffId\":\"%s\"", AppContext.userId,
                    shopIds, addressId, invoiceId, points_deduction,
                    "", redpacket_deduction + "",lh_id, origin, shipping_method, deliveryTime,
                    nearbyId,staffId) + "}";
            Logger.json(infojson);
            Logger.i(remarksJson);

            // modify by zhyao @2015/5/6  添加红包参数
            RequstClient.sumbitOrder(AppContext.userId, shopIds, addressId,
                    invoiceId, points_deduction + "", MathUtil.priceForAppWithOutSign(redpacket_deduction), remarksJson + "", lh_id,
                    origin, shipping_method, deliveryTime, nearbyId + "",staffId,type,
                    new CustomResponseHandler(this) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers,
                                              String content) {
                            super.onSuccess(statusCode, headers, content);
                            if (LogUtil.isDebug) {
                                Logger.json(content);
                            }
                            JSONObject obj = null;
                            try {
                                obj = new JSONObject(content);
                                if (!obj.getString("type").equals("1")){
                                    String errorMsg = obj.getString("msg");
                                    UIHelper.showToast(errorMsg);
                                    return;
                                }
                                UIHelper.showToast("订单提交成功");
                                dealWithSubmitResult(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * 处理提交成功后的结果
     * @param obj 提交成功后返回的json
     */
    private void dealWithSubmitResult(JSONObject obj){
        Intent i;
        try {
            i = new Intent();
            if (allPay <= 0) {
                i.setClass(this,
                        PaymentSuccessActivity.class);
                i.putExtra(
                        PaymentSuccessActivity.INTENT_KEY_ORDER_ID,
                        obj.getString("orderId"));
                i.putExtra(
                        PaymentSuccessActivity.INTENT_KEY_ORDER_NUM,
                        obj.getString("orderCode"));
            } else {
                i.setClass(this,
                        CommitOrderActivity.class);
                i.putExtra("serialNum",
                        obj.getString("orderId"));
                i.putExtra("number",
                        obj.getString("orderCode"));
                i.putExtra("totalPrice", MathUtil
                        .keepMost2Decimal(obj
                                .getDouble("price")));
                i.putExtra("perpaidCard", MathUtil
                        .keepMost2Decimal(obj
                                .getDouble("perpaidCard")));
            }
            if (type==6){
                //秒杀要返回通知到详情页面
                setResult(Activity.RESULT_OK);
            }
            startActivity(i);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getShoppingIds() {
        return shopIds = mFillOrderBean.getShoppingId();
    }

    // modify by zhyao @2015/5/6  添加红包参数
    /**
     * 计算显示的金额清单
     *
     * @param gsp      商品总价,-1即为上次的总价
     * @param freight  总运费，-1即为上次的总运费
     * @param integral 使用的积分抵扣金额，-1即为上次的积分抵扣
     * @param humoney  使用的虎券抵扣金额，-1即为上次的虎券抵扣
     * @param redpacket 使用的红包抵扣金额，-1即为上次的红包抵扣
     */
    private void calculatShowMoney(double gsp, double freight, int integral, double humoney, double redpacket) {
        if (gsp == -1) {
            goodsSumPrice = Utils.anDouble(mFillOrderBean.getCart().getGoodsPriceSumStr());
        } else {
            goodsSumPrice = gsp;
        }
        //商品活动已优惠的金额
        double preferentialPrice = Utils.anDouble(mFillOrderBean.getCart().getPreferentialPriceSumStr());
        if (freight != -1)
            myFreight = freight;
        if (integral != -1)
            points_deduction = integral;
        double pointsToRMBForCalculat;
        if (mFillOrderBean.getShoppingType().equals("10")) {
            //实物礼品兑换就不用将积分转换成RMB
            ratio = 1;
            pointsToRMBForCalculat=0;
        }else{
            pointsToRMBForCalculat=points_deduction / ratio;
        }

        if (humoney != -1)
            hujuanMoney = humoney;
        // modify by zhyao @2015/5/6  添加红包参数
        if (redpacket != -1) 
        	redpacket_deduction = redpacket;
        //modify by zhyao @2015/6/12 新增消费卷模式
        if (shipping_method.equals("1") || shipping_method.equals("3")) {
        	// modify by zhyao @2015/5/6  添加红包抵扣
            //上门自提,不需要运费
            allPay = goodsSumPrice - preferentialPrice - pointsToRMBForCalculat- redpacket_deduction - hujuanMoney;
        } else if (shipping_method.equals("2")) {
        	// modify by zhyao @2015/5/6  添加红包抵扣
            //送货上门
            allPay = goodsSumPrice - preferentialPrice + myFreight - pointsToRMBForCalculat - redpacket_deduction - hujuanMoney;
        }

        setPayInfo(exemption, myFreight, points_deduction/ratio, redpacket_deduction, hujuanMoney, allPay);
    }

    /**
     * 实物兑换时,隐藏积分抵扣,虎券抵扣,红包抵扣的优惠信息编辑区域(结算中的积分抵扣除外)
     * @param shoppingType 10/为礼品兑换
     */
    private boolean hideDiscountArea(String shoppingType){
        if(shoppingType.equals("10")){
            integral_area.setVisibility(View.GONE);
            red_packet_area.setVisibility(View.GONE);
            humoney_area.setVisibility(View.GONE);

            pay_red_packet_area.setVisibility(View.GONE);
            pay_humoney_area.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private void showActivityOffDialog(){
        mDialog = new ConfirmDialog(this,"该商品活动未开始或已结束,是否继续购买","确定","下次再说",new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                type=3; //按普通商品购买
                submit();
                mDialog.dismiss();
            }

            @Override
            public void doCancel() {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }
}
