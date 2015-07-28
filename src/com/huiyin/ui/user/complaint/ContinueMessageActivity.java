package com.huiyin.ui.user.complaint;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.UploadImageAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.bean.ImageData;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.imageupload.ImageUpload;
import com.huiyin.wight.MyGridView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 继续留言
 * Created by lian on 2015/6/2.
 */
public class ContinueMessageActivity extends BaseCameraActivity implements AdapterView.OnItemClickListener{

    public static final String COMPLAINT_ID="complaintId";
    private android.widget.EditText commentcontentedittext;
    private android.widget.TextView commentcontenttiptv;
    private com.huiyin.wight.MyGridView uploadaddgridview;
    private android.widget.Button submitapplybtn;
    /**图片上传适配器**/
    private UploadImageAdapter uploadImageAdapter;
    private String complaintId;//留言id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_continue_message);
        findView();
        complaintId=getIntent().getStringExtra(COMPLAINT_ID);
        initView();
    }

    private void findView(){
        this.submitapplybtn = (Button) findViewById(R.id.submit_apply_btn);
        this.uploadaddgridview = (MyGridView) findViewById(R.id.upload_add_gridview);
        this.commentcontenttiptv = (TextView) findViewById(R.id.comment_content_tip_tv);
        this.commentcontentedittext = (EditText) findViewById(R.id.comment_content_edittext);
    }

    public void initView(){
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
        setTextChangeListener(commentcontentedittext, commentcontenttiptv, 200);
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
                    complainContinue(img);
                }

                @Override
                public void UpLoadFail() {
                    showMyToast("图片上传失败");
                }
            });
        }else{
            //post请求
            complainContinue("");
        }
    }

    /**
     * 提交投诉请求
     */
    private void complainContinue(String img){
        /**
         * 发起投诉
         userId  订单编号
         message 留言类型
         img     图片，不可以超过五张
         complaintId 留言id
         */
        String describ=commentcontentedittext.getText().toString();
        RequstClient.complainContinue(AppContext.userId, describ, img, complaintId, new CustomResponseHandler(mContext, true) {
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
                            setResult(RESULT_OK);
                            mContext.finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private boolean check(){
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
}
