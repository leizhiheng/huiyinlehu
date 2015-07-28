package com.huiyin.ui.user.complaint;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.UploadImageAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.api.URLs;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.ComplainTypeBean;
import com.huiyin.bean.ComplainTypeListEntity;
import com.huiyin.bean.GoodList;
import com.huiyin.bean.ImageData;
import com.huiyin.dialog.CommitComplaintSuccessDialog;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow;
import com.huiyin.ui.store.StoreHomeActivity;
import com.huiyin.ui.user.order.AllOrderActivity;
import com.huiyin.ui.user.order.AllOrderDetailActivity;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ResourceUtils;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.wight.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 发起投诉
 * Created by lian on 2015/6/2.
 */
public class SendComplaintActivity extends BaseCameraActivity implements AdapterView.OnItemClickListener{

    public static final String GOOD="good";
    private android.widget.ImageView ivlogo;//店铺logo
    private android.widget.TextView tvstorename;//店铺名称
    private android.widget.TextView tvstatus;//订单状态
    private android.widget.ImageView ivorder1;//商品图片
    private android.widget.ImageView ivorder2;//商品图片
    private android.widget.ImageView ivorder3;//商品图片
    private android.widget.TextView tvpoint;//...
    private android.widget.TextView tvdescrib;//产品描述
    private android.widget.LinearLayout layoutorder;//
    private android.widget.TextView tvprice;//实付款
    private android.widget.TextView tvtime;//下单时间
    private android.widget.TextView tvcomplainttype; //投诉类型
    private android.widget.EditText etquestion;//问题标题
    private android.widget.EditText commentcontentedittext;//问题描述
    private android.widget.TextView commentcontenttiptv;//字数提示
    private com.huiyin.wight.MyGridView uploadaddgridview;//图片列表
    /**图片上传适配器**/
    private UploadImageAdapter uploadImageAdapter;
    private android.widget.Button submitapplybtn;//提交
    private GoodList good;//订单
    private String selectQuestionId;//问题id
    private String selectQuestionName;//问题名称
    private ComplainTypeBean  typeBean;//投诉类型
    private ScrollView cv_main;//主页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_send_complaint);
        good= (GoodList) getIntent().getSerializableExtra(GOOD);
        findView();
        complainType();
    }

    private void findView(){
        this.submitapplybtn = (Button) findViewById(R.id.submit_apply_btn);
        this.uploadaddgridview = (MyGridView) findViewById(R.id.upload_add_gridview);
        this.commentcontenttiptv = (TextView) findViewById(R.id.comment_content_tip_tv);
        this.commentcontentedittext = (EditText) findViewById(R.id.comment_content_edittext);
        this.etquestion = (EditText) findViewById(R.id.et_question);
        this.tvcomplainttype = (TextView) findViewById(R.id.tv_complaint_type);
        this.tvtime = (TextView) findViewById(R.id.tv_time);
        this.tvprice = (TextView) findViewById(R.id.tv_price);
        this.layoutorder = (LinearLayout) findViewById(R.id.layout_order);
        this.tvdescrib = (TextView) findViewById(R.id.tv_describ);
        this.tvpoint = (TextView) findViewById(R.id.tv_point);
        this.ivorder3 = (ImageView) findViewById(R.id.iv_order3);
        this.ivorder2 = (ImageView) findViewById(R.id.iv_order2);
        this.ivorder1 = (ImageView) findViewById(R.id.iv_order1);
        this.tvstatus = (TextView) findViewById(R.id.tv_status);
        this.tvstorename = (TextView) findViewById(R.id.tv_store_name);
        this.ivlogo = (ImageView) findViewById(R.id.iv_logo);
        this.cv_main = (ScrollView) findViewById(R.id.cv_main);
    }

    public void initView(){
        cv_main.setVisibility(View.VISIBLE);
        if (good.SELLER > 0) {// 店铺
            ImageManager.LoadWithServer(good.STORE_LOGO,ivlogo);
            tvstorename.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_black_arraw, 0);
            tvstorename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//进入店铺
                    Intent intent = new Intent(mContext, StoreHomeActivity.class);
                    intent.putExtra(StoreHomeActivity.STORE_ID, good.SELLER);
                    mContext.startActivity(intent);
                }
            });
        } else {// 乐虎自营
            tvstorename.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
            tvstorename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ivlogo.setImageResource(R.drawable.lehu);
        }
        tvstorename.setText(good.STORE_NAME+"  ");
        tvstatus.setText(good.STATUS_NAME);


        /**
         * 控制图片的显示
         */
        setShowOrGone(View.GONE);
        if (null!=good.orderDetalList&&1 == good.orderDetalList.size()) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL + good.orderDetalList.get(0).GOODS_IMG,
                    ivorder1);
            tvdescrib.setText(good.orderDetalList.get(0).GOODS_NAME);
            tvdescrib.setVisibility(View.VISIBLE);
            ivorder1.setVisibility(View.VISIBLE);
        } else if (null != good.orderDetalList&&2 == good.orderDetalList.size()) {
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL + good.orderDetalList.get(0).GOODS_IMG, ivorder1);
            loader.displayImage(URLs.IMAGE_URL + good.orderDetalList.get(1).GOODS_IMG, ivorder2);
            ivorder1.setVisibility(View.VISIBLE);
            ivorder2.setVisibility(View.VISIBLE);
        } else if (null!=good.orderDetalList&&3 <= good.orderDetalList.size()) {
            setShowOrGone(View.VISIBLE);
            tvdescrib.setVisibility(View.GONE);
            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage(URLs.IMAGE_URL + good.orderDetalList.get(0).GOODS_IMG, ivorder1);
            loader.displayImage(URLs.IMAGE_URL + good.orderDetalList.get(1).GOODS_IMG, ivorder2);
            loader.displayImage(URLs.IMAGE_URL + good.orderDetalList.get(2).GOODS_IMG, ivorder3);
        }


        tvtime.setText("下单时间："+good.CREATE_DATE);
        float total = good.TOTAL_PRICE;
        Spanned price = Html.fromHtml("实付款："
                + ResourceUtils.changeStringColor("#DC434F",
                MathUtil.priceForAppWithSign(total)));
        tvprice.setText(price);
        layoutorder.setOnClickListener(new View.OnClickListener() {//点击进入订单详情
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,AllOrderDetailActivity.class);
                intent.putExtra(AllOrderDetailActivity.GOOD_LIST, good);
                mContext.startActivityForResult(intent,AllOrderActivity.ORDER_DETAIL);
            }
        });
        /**
         * 将投诉类型转换成Map
         */
        final Map<String,String> typeMap=new HashMap<String, String>();
        for (ComplainTypeListEntity item:typeBean.getComplainTypeList()){
            typeMap.put(item.getID(),item.getNAME());
        }
        //选择投诉类型
        tvcomplainttype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReasonPopwindow(cv_main,typeMap,selectQuestionId,new SelectApplyReasonWindow.IOnItemClick() {
                    @Override
                    public void onItemClick(String id, String value) {
                        selectQuestionId=id;
                        selectQuestionName=value;
                        tvcomplainttype.setText(selectQuestionName+"  ");
                    }
                });
            }
        });
        //提交投诉
        submitapplybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
        //选择图片
        int screenWidth = DeviceUtils.getWidthMaxPx(mContext);
        uploadImageAdapter = new UploadImageAdapter(mContext, screenWidth, 5);
        uploadaddgridview.setAdapter(uploadImageAdapter);
        uploadaddgridview.setOnItemClickListener(this);
        //监听描述问题文本变换
        setTextChangeListener(commentcontentedittext, commentcontenttiptv,200);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        switch (arg0.getId()) {
            case R.id.upload_add_gridview:
                //上传图片
                if(uploadImageAdapter.isClickAddPic(arg2)){
                    //打开相册，相机
                    showCameraPopwindow(arg1, false, false);
                }
                break;
        }

    }

    @Override
    public void onUpLoadSuccess(String imageUrl, String imageFile) {

        //非空判断
        if (TextUtils.isEmpty(imageUrl) && TextUtils.isEmpty(imageFile)){
            return;
        }

        //显示新增的数据
        uploadImageAdapter.appendData(new ImageData(imageFile, imageUrl));
    }

    /**
     * 提交投诉
     */
    private void commit(){
        if(!check()){
            return;
        }
        if(uploadImageAdapter.hasNewUploadImgFile()){
            uploadImage(uploadImageAdapter.getDataList(), new ImageUpload.UpLoadImageListener() {

                @Override
                public void UpLoadSuccess(ArrayList<String> netimageurls) {
                    String img = getImgs(netimageurls);
                    //post请求
                    submitComplaint(img);
                }

                @Override
                public void UpLoadFail() {
                    showMyToast("图片上传失败");
                }
            });
        }else{
            //post请求
            submitComplaint("");
        }
    }

    /**
     * 提交投诉请求
     */
    private void submitComplaint(String img){
        /**
         * 发起投诉
         userId    用户id
         orderCode 订单编号
         typeId    投诉类型
         typeName：投诉类型名称
         img       图片，不可以超过五张
         title     举报标题
         content   举报内容
         */
        String title=etquestion.getText().toString();
        String describ=commentcontentedittext.getText().toString();
        RequstClient.addComplain(AppContext.userId,good.ORDER_CODE,selectQuestionId ,
                selectQuestionName,img,title,describ,new CustomResponseHandler(mContext,true){
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
                            final String complainId =obj.getString("complainId");
                            if(null==complainId||"".equals(complainId)){
                                showMyToast("网络错误");
                                return ;
                            }
                            CommitComplaintSuccessDialog dialog=new CommitComplaintSuccessDialog(mContext);
                            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    Intent intent=new Intent(mContext,ComplaintDetailActivity.class);
                                    intent.putExtra(ComplaintDetailActivity.COMPLAIN_ID,complainId);
                                    mContext.startActivity(intent);
                                    mContext.setResult(RESULT_OK);
                                    mContext.finish();
                                }
                            });
                            dialog.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private boolean check(){
        //投诉类型
        if(isEmpty(selectQuestionId,"请选择投诉类型")){
            return false;
        }
        //问题标题
        String title=etquestion.getText().toString();
        if(isEmpty(title,"请输入问题标题")){
            return false;
        }
        //问题描述
        String describ=commentcontentedittext.getText().toString();
        if(isEmpty(describ,"请输入问题描述")){
            return false;
        }
        return true;
    }

    private boolean isEmpty(String content,String tip){
        if(null==content||"".equals(content)){
            showMyToast(tip);
            return true;
        }
        return false;
    }

    /**
     * 是否显示3张图片
     * @param visible
     */
    private void setShowOrGone(int visible) {
        ivorder1.setVisibility(visible);
        ivorder2.setVisibility(visible);
        ivorder3.setVisibility(visible);
        tvpoint.setVisibility(visible);
        tvdescrib.setVisibility(visible);
    }

    /**
     * 获取投诉类型
     */
    private void complainType(){
        RequstClient.complainType(new CustomResponseHandler(mContext,true){
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
                    typeBean=new Gson().fromJson(content,ComplainTypeBean.class);
                    if(null==typeBean){
                        Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    initView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
