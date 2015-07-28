package com.huiyin.ui.user.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.AccountManageActivity;

/**
 * Created by kuangyong on 2015/6/19.
 */
public class UpdateUserNameActiviry extends BaseActivity implements View.OnClickListener{

    public static final String USER_NAME="username";
    private String userName;

    private android.widget.EditText etname;
    private android.widget.TextView btndel;
    private android.widget.TextView btncommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_update_username);
        userName=getIntent().getStringExtra(USER_NAME);
        findView();
        setListener();
    }

    private void findView(){
        this.btncommit = (TextView) findViewById(R.id.btn_commit);
        this.btndel = (TextView) findViewById(R.id.btn_del);
        this.etname = (EditText) findViewById(R.id.et_name);
        this.etname.setText(AppContext.mUserInfo.userName);
    }

    private void setListener(){
        btndel.setOnClickListener(this);
        btncommit.setOnClickListener(this);
        etname.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if(etname.getText().toString().length()>20){
                    Toast.makeText(mContext, "您输入的用户名已经超过最大的长度！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_commit://提交
                commit();
                break;
            case R.id.btn_del://删除
                etname.setText("");
                break;
        }
    }

    private void commit(){
        String change_name = etname.getText().toString();
        if(etname.equals("")){
            Toast.makeText(mContext, "用户名不能为空！", Toast.LENGTH_SHORT).show();
            return;
            //^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]{2,15}$
        }
        
        if(!change_name.matches("[-\\w\u4e00-\u9fa5]{1,20}")){
            Toast.makeText(mContext, "用户名不符合命名规则，请重新输入！",Toast.LENGTH_SHORT).show();
            etname.setText("");
            return;
        }
       
        int length = change_name.length(); 
        if(length < 4 || length > 20){
        	showMyToast("字符长度为4-20");
        	return;
        }
        
        
        
        Intent i = new Intent(mContext,AccountManageActivity.class);
        i.putExtra("username", change_name);
        setResult(RESULT_OK, i);
        finish();
    }
}
