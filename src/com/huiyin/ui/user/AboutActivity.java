package com.huiyin.ui.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.AboutBean;
import com.huiyin.bean.UpdataInfo;
import com.huiyin.ui.introduce.IntroduceActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.PreferenceUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.UpdateManager;
import com.huiyin.utils.UpdateVersionTool;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AboutActivity extends BaseActivity implements OnClickListener{

	TextView left_ib, middle_title_tv, phone_bind_tv, vision_textview,tel;

    private LinearLayout kefu_telephone;//客服电话
    private LinearLayout kefu_check_version;//检查更新
    private LinearLayout kefu_server_agreement;//服务协议
    private AboutBean bean;
    private ScrollView sv_mian;//主布局
    private ImageView iv_logo;//logo
    private ImageView iv_code;//二维码图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
        findView();
        getAboutData();
	}

    private void findView(){
        kefu_telephone = (LinearLayout) findViewById(R.id.kefu_telephone);
        kefu_check_version = (LinearLayout) findViewById(R.id.kefu_check_version);
        kefu_server_agreement = (LinearLayout) findViewById(R.id.kefu_server_agreement);
        sv_mian = (ScrollView) findViewById(R.id.sv_mian);
        left_ib = (TextView) findViewById(R.id.left_ib);
        tel = (TextView) findViewById(R.id.tel);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        iv_code = (ImageView) findViewById(R.id.iv_code);
    }

	private void initView() {
        sv_mian.setVisibility(View.VISIBLE);//显示主题布局
        ImageManager.LoadWithServer(bean.getLogo(),iv_logo);
        ImageManager.LoadWithServer(bean.getCode(),iv_code);
		left_ib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
        kefu_telephone.setOnClickListener(this);
        kefu_check_version.setOnClickListener(this);
        kefu_server_agreement.setOnClickListener(this);
		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("关于");
		vision_textview = (TextView) findViewById(R.id.vision_textview);
		vision_textview.setText("For Android V"
				+ UpdateVersionTool.getVersionName(this));
        tel.setText("电话客服："+ PreferenceUtil.getInstance(mContext).getHotLine());
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kefu_check_version://检查版本更新
                requestVersion();
                break;
            case R.id.kefu_telephone://客服电话
                String telPhone = tel.getText().toString();
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(telPhone);
                telPhone = m.replaceAll("").trim();
                Log.i("解析结果", "====" + telPhone);

                if(StringUtils.isBlank(telPhone))
                    return;
                Intent d_intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + telPhone));
                d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(d_intent);
                break;
            case R.id.kefu_server_agreement://服务协议
                Intent c_intent = new Intent(mContext,IntroduceActivity.class);
                c_intent.putExtra("id", 1);
                startActivity(c_intent);
                break;
        }
    }

    /***
     * 检测版本
     * */
    private void requestVersion() {
        CustomResponseHandler handler = new CustomResponseHandler(this, true) {
            @Override
            public void onRefreshData(String content) {
                analyticalVersionData(content);
            }
        };
        RequstClient.getVersion(handler);
    }

    /***
     * 解析数据
     * */
    private void analyticalVersionData(String content) {
        try {
            JSONObject roots = new JSONObject(content);
            if (roots.getString("type").equals("1")) {
                String url = roots.getString("editionUrl");
                String versionName = roots.getString("editionName");
                String versionVersion = roots.getString("editionExplain");

                int versionCode = Integer.valueOf(roots.getString("edition"));
                UpdataInfo updateInfo = new UpdataInfo();
                updateInfo.setUrl(url);
                updateInfo.setVersion(versionName);
                updateInfo.setVersionCode(versionCode);
                updateInfo.setDescription(versionVersion);

                updateApk(updateInfo);

            } else {
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateApk(final UpdataInfo updateInfo) {
        if (UpdateVersionTool.getVersionCode(this) >= updateInfo
                .getVersionCode()) {
            Toast.makeText(mContext, "已是最新版本！", Toast.LENGTH_SHORT).show();
            return;
        } else {

            UpdateManager manager = new UpdateManager(this, "huiyin",
                    updateInfo.getUrl());
            manager.checkUpdate(updateInfo.getVersionCode(), updateInfo.getDescription());

        }
    }

    private void getAboutData(){
        RequstClient.about(new CustomResponseHandler(mContext,true){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj=new JSONObject(content);
                    String msg=obj.getString("msg");
                    if(1!=obj.getInt("type")){
                        showMyToast(msg);
                        return ;
                    }
                    bean=new Gson().fromJson(content,AboutBean.class);
                    initView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
