package com.huiyin.ui.user.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;

import com.huiyin.R;
import com.huiyin.base.BaseActivity;
import com.huiyin.ui.user.account.FragmentBindEmailStepTwo.OnStepToBindFinishListener;
import com.huiyin.ui.user.account.FragmentModifyEmailOne.OnStepToModifyEmailListener;
import com.huiyin.ui.user.account.FragmentModifyEmailTwo.OnStepToModifyFinishListener;

/**
 * 修改邮箱
 * @author 刘远祺
 *
 * @todo TODO
 *
 * @date 2015-7-6
 */
public class BoundEmailModifyActivity extends BaseActivity {
	
	private FragmentManager fragmentManager;
	private FragmentModifyEmailOne fragmentModifyEmailOne;
	private FragmentModifyEmailTwo fragmentModifyEmailTwo;
	private FragmentBindEmailStepTwo fragmentBindEmailStepTwo;
    private ImageView iv_icon_one;
    private ImageView iv_icon_two;
	private ImageView step2,step3;
	public static String new_email;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_modify_boundemail);
		init();
	}
	
	public void init(){
		fragmentManager = this.getSupportFragmentManager();
		fragmentModifyEmailOne = new FragmentModifyEmailOne();
		fragmentModifyEmailTwo = new FragmentModifyEmailTwo();
		fragmentBindEmailStepTwo = new FragmentBindEmailStepTwo();
		
		//顶部三个状态的图标，包括小箭头
        iv_icon_one = (ImageView) findViewById(R.id.iv_icon_one);
        iv_icon_two = (ImageView) findViewById(R.id.iv_icon_two);
		step2 = (ImageView) findViewById(R.id.iv_mod_email_step_2);
		step3 = (ImageView) findViewById(R.id.iv_mod_email_step_3);
		
		//校验登录密码
		fragmentModifyEmailOne.setOnStepToCheckEmailListener(new OnStepToModifyEmailListener() {
			
			@Override
			public void stepToModifyEmail() {
				replaceFragment(fragmentModifyEmailTwo);
                iv_icon_one.setImageResource(R.drawable.sjjt1);
                iv_icon_two.setImageResource(R.drawable.sjjt1);
				step2.setImageResource(R.drawable.account_modify_pw_sel);
			}
		});
		
		//修改邮箱
		fragmentModifyEmailTwo.setOnStepToModifyFinishListener(new OnStepToModifyFinishListener() {
			
			@Override
			public void stepToModifyFinish(String newEmail, String emailUrl) {
				new_email = newEmail;
				fragmentBindEmailStepTwo.setEmail(newEmail, emailUrl);
				replaceFragment(fragmentBindEmailStepTwo);
			}
		});
		
		//
		fragmentBindEmailStepTwo.setOnStepToBindFinishListener(new OnStepToBindFinishListener() {
			
			@Override
			public void bindSuccess(boolean isSuccess) {
				if(isSuccess){
					step3.setImageResource(R.drawable.account_modify_finish_sel);
				}
			}
		});
		
		replaceFragment(fragmentModifyEmailOne);
	}
	
	/**
	 * 替换fragment
	 * @param fragment
	 */
	public void replaceFragment(Fragment fragment){
		fragmentManager.beginTransaction().replace(R.id.fragment_container_mod_email, fragment).commit();
	}
}
