package com.huiyin.ui.user.order;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.CommentItemForJson;
import com.huiyin.bean.GoodList;
import com.huiyin.bean.OrderDetail;
import com.huiyin.ui.common.CommonConfrimCancelDialog;
import com.huiyin.utils.imageupload.BitmapUtils;
import com.huiyin.utils.imageupload.ImageFolder;
import com.huiyin.wight.OrderCommodityView;

import java.io.File;
import java.util.ArrayList;

/**
 * 评价晒单
 */
public class OrderCommentActivity extends BaseActivity {

    private final static String TAG = "OrderCommentActivity";
    public static final String ORDER_ITEM = "orderItem";

    public static final int CAMER_CODE = 007;
    public static final int LOCAL_CODE = 006;

    TextView middle_title_tv, left_ib;
    LinearLayout order_add_comment_item;
    private GoodList orderItem;
    public ArrayList<OrderDetail> orderDetailList;

    // 商品包装、送货速度、配送满意度
    RatingBar good_package_iv, send_speed_iv, distribute_iv;
    // 匿名评价
    CheckBox guest_check;
    // 订单评价
    TextView all_comment_tv;
    // 订单id
    String order_id;
    LinearLayout all_comment_linearlayout;

    private int poi;//定义是哪一个item发起的相机请求

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_comment_layout);

        initView();

    }


    private void initView() {


        left_ib = (TextView) findViewById(R.id.left_ib);
        left_ib.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                finish();
            }
        });

        middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
        middle_title_tv.setText("订单评价");
        order_add_comment_item = (LinearLayout) findViewById(R.id.order_add_comment_item);

        orderItem = (GoodList) getIntent().getSerializableExtra(ORDER_ITEM);
        orderDetailList = orderItem.orderDetalList;
        if (orderDetailList != null) {
            // 订单id
            order_id = orderDetailList.get(0).ORDER_ID;
            all_comment_linearlayout = (LinearLayout) findViewById(R.id.all_comment_linearlayout);

            for (int i = 0; i < orderDetailList.size(); i++) {
                OrderDetail good_item = orderDetailList.get(i);
                OrderCommodityView view_item = new OrderCommodityView(mContext);
                view_item.RefreshView(good_item, i);
                view_item.setWhichOneClickListener(new OrderCommodityView.onWhichOneClickListener() {
                    @Override
                    public void onThisClick(int position) {
                        poi = position;
                    }

                    @Override
                    public void onPicUpSucc(boolean isSuccess, int position) {
                        controlUpload(++position);
                    }
                });
                order_add_comment_item.addView(view_item);
            }

            good_package_iv = (RatingBar) findViewById(R.id.good_package_iv);
            send_speed_iv = (RatingBar) findViewById(R.id.send_speed_iv);
            distribute_iv = (RatingBar) findViewById(R.id.distribute_iv);

            guest_check = (CheckBox) findViewById(R.id.guest_check);
            all_comment_tv = (TextView) findViewById(R.id.all_comment_tv);

            if (getIntent().getIntExtra("flag", 0) == 1) {
                all_comment_linearlayout.setVisibility(View.GONE);
                all_comment_tv.setEnabled(false);
            } else {
                all_comment_linearlayout.setVisibility(View.VISIBLE);
                all_comment_tv.setEnabled(true);
            }

            all_comment_tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    float startNum1 = good_package_iv.getRating();
                    float startNum2 = send_speed_iv.getRating();
                    float startNum3 = distribute_iv.getRating();
                    if (startNum1 == 0 || startNum2 == 0 || startNum3 == 0) {
                        showMyToast("请选择评论星数！");
                    } else {
                        controlUpload(0);
                    }
                }
            });
        } else {
            return;
        }
    }

    /**
     * 如评论全部输入完毕
     * @return
     */
    private boolean commentInputOver(){
    	
    	OrderCommodityView commdView = null;
    
    	//判断所有商品评价信息
    	boolean flag = false;
    	for (int i = 0; i < order_add_comment_item.getChildCount(); i++) {
    		commdView = (OrderCommodityView) order_add_comment_item.getChildAt(i);
    		if(!flag){
    			flag = true;
    			if(!commdView.validateShowInputTip()){
    				//第一个商品会每一个都提示错误信息
    				return false;
    			}
    		}else{
    			
    			if(!commdView.isInputOver()){
    				showMyToast("亲，您需要评价完所有商品才能提交本次订单评价哦！");
    				return false;
    			}
    		}
        }
    	
    	//判断订单评价信息
    	if(good_package_iv.getRating() <= 0){
    		
    		showMyToast("亲，给商品包装打个分吧！");
    		return false;
    	}
    	
    	if(send_speed_iv.getRating() <= 0){
    		showMyToast("亲，给送货速度打个分吧！");
    		return false;
    	}
    	if(distribute_iv.getRating() <= 0){
    		showMyToast("亲，给配送服务打个分吧！");
    		return false;
    	}
    	return true;
    }
    
    
    /**
     * 提交订单评价
     */
    private void postTotalComment() {
    	
    	//输入验证
    	if(!commentInputOver()){
    		return;
    	}
    	
        CustomResponseHandler handler = new CustomResponseHandler(this, true) {
            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                Gson gson = new Gson();
                BaseBean bean = gson.fromJson(content, BaseBean.class);
                if (bean.type > 0) {
                    Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                    
                    //通知订单列表刷新
                    setResult(RESULT_OK);
                    
                    finish();
                } else {
                    Toast.makeText(mContext, bean.msg, Toast.LENGTH_SHORT).show();
                }
            }
        };
        int isGuest = guest_check.isChecked() ? 1 : 0;
        String pack = String.valueOf(good_package_iv.getRating());
        String speed = String.valueOf(send_speed_iv.getRating());
        String server = String.valueOf(distribute_iv.getRating()) ;

        ArrayList<CommentItemForJson> commentItemList = new ArrayList<CommentItemForJson>();
        for (int i = 0; i < order_add_comment_item.getChildCount(); i++) {
            commentItemList.add(((OrderCommodityView) order_add_comment_item.getChildAt(i)).getTheResult());
        }
        Gson gson = new Gson();
        String evaData = gson.toJson(commentItemList);
        
        String userId = AppContext.mUserInfo.userId;
        RequstClient.postTotalComment(userId, orderItem.ID, orderItem.SELLER, isGuest, speed, server, pack, 2, evaData, handler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE
                && resultCode == RESULT_OK) {
            /* 选择图片来源 */
            String uploadType = data
                    .getStringExtra(CommonConfrimCancelDialog.TAG);
            if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_CAMER)) {
				/* 相机 */
                callCamerImage();
            } else if (uploadType
                    .equals(CommonConfrimCancelDialog.UPLOAD_TYPE_LOCAL)) {
				/* 图库 */
                callLocalImage();
            }
        } else if ((requestCode == CAMER_CODE || requestCode == LOCAL_CODE)) {
            if (resultCode == RESULT_OK) {
				/* 相机或者图库返回OK */
                Log.i(TAG, "获取图片返回成功");
                dealWithImage(data);
            } else {
                Log.e(TAG, "获取图片失败");
            }

        }
    }

    private void dealWithImage(Intent data) {
        if (data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = this.getContentResolver().query(selectedImage,
                    filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            ((OrderCommodityView) order_add_comment_item.getChildAt(poi)).addPicture(picturePath);
        } else {
            File f = new File(capturePath);
            if (!f.exists()) {
                return;
            } else {
                ((OrderCommodityView) order_add_comment_item.getChildAt(poi)).addPicture(capturePath);
            }
        }
    }

    private String capturePath;

    private void callCamerImage() {
        Log.i(TAG, "启动照相机");
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent captureIntent = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            String out_file_path;
            out_file_path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/huiyin";
            File dir = new File(out_file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            capturePath = ImageFolder.getTempImageName().getPath();
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(capturePath)));
            startActivityForResult(captureIntent, CAMER_CODE);
        }
    }

    private void callLocalImage() {
        Log.i(TAG, "打开图库");
        Intent localIntent = new Intent(Intent.ACTION_PICK);
        localIntent.setType("image/*");
        startActivityForResult(localIntent, LOCAL_CODE);
    }

    private void controlUpload(int poi) {
        if (order_add_comment_item.getChildCount() > poi) {
            ((OrderCommodityView) order_add_comment_item.getChildAt(poi)).UpLoadImage();
        } else {
        	
        	//等待图片上传成功后在提交评价
            postTotalComment();
        }
    }
}
