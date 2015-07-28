package com.huiyin.ui.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseCameraActivity;
import com.huiyin.ui.housekeeper.SelectApplyReasonWindow;
import com.huiyin.ui.user.account.AccountLevelActivity;
import com.huiyin.ui.user.account.AccountSecurityActivity;
import com.huiyin.ui.user.account.UpdateUserNameActiviry;
import com.huiyin.utils.DateUtil;
import com.huiyin.utils.DialogUtil;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户管理
 * @author 刘远祺
 *
 */
public class AccountManageActivityNew extends BaseCameraActivity implements View.OnClickListener{

	private final static String TAG = "AccountManageActivity";
	
	/**修改用户名**/
	private static final int REQUEST_CODE_MODIFY_NAME 	= 0x00110;
	
	/**账户安全**/
	private static final int REQUEST_CODE_ACCOUNT_SAFTY = 0x00111;

	/**用户头像**/
	ImageView account_head;

	/**昵称**/
	TextView account_username;

	TextView account_sex;//性别

	/**用户等级**/
	TextView account_level;
	
	/**生日**/
	TextView birthDay;
	
    private String sexId;//性别id
    private String sexName;//性别


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_manage_layout_new);
		setRightText(View.GONE);
		
		initView();
		
		initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		if (AppContext.mUserInfo == null) {
			return;
		}
		
		//显示用户基本信息
		UserInfo mUserInfo = AppContext.mUserInfo;
		account_username.setText(mUserInfo.userName);
		account_level.setText(mUserInfo.level);
		
		//性别
		int sex=AppContext.mUserInfo.getSex();
        sexId=sex+"";
        sexName = 1==sex ? "男" : "女";
        account_sex.setText(sexName);
		
        //生日
        birthDay.setText(mUserInfo.getBirthDay());

        //头像
		loadUserHead(account_head);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		account_head = (ImageView)findViewById(R.id.account_head);
		account_username = (TextView)findViewById(R.id.account_username);
        account_sex = (TextView)findViewById(R.id.account_sex);
		account_level = (TextView)findViewById(R.id.account_level);
		birthDay = (TextView)findViewById(R.id.birthday_birthday);
		
		//点击事件
		findViewById(R.id.account_username_layout).setOnClickListener(this);
		findViewById(R.id.account_sex_layout).setOnClickListener(this);
		findViewById(R.id.account_birthday_layout).setOnClickListener(this);
		findViewById(R.id.account_add_ticket_layout).setOnClickListener(this);
		findViewById(R.id.account_address_layout).setOnClickListener(this);
		findViewById(R.id.account_safty_layout).setOnClickListener(this);
		findViewById(R.id.account_head).setOnClickListener(this);
		findViewById(R.id.layout_account_level).setOnClickListener(this);
		
		//设置标题
		setTitle(getString(R.string.text_my_account));
	}

	/**
	 * 修改账户信息
	 */
	private void doModify(String userId, final String newUserName) {

		if (AppContext.userId == null) {
			return;
		}
		RequstClient.doModifyUsername(userId, newUserName, new CustomResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "doModify:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					account_username.setText(newUserName);
					if (AppContext.mUserInfo != null) {
						AppContext.mUserInfo.userName = newUserName;
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

	
	//所有控件的点击事件
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.account_head:
			
			//修改头像
			//startCameraDialogActivity();
			showCameraPopwindow(view, true, true, true);
			
			break;
		case R.id.account_username_layout:
			
			//昵称
			Intent intentUserName = new Intent(AccountManageActivityNew.this, UpdateUserNameActiviry.class);
			startActivityForResult(intentUserName, REQUEST_CODE_MODIFY_NAME);
			
			break;

		case R.id.layout_account_level:
			
			//会员等级
			Intent intentLevel = new Intent(AccountManageActivityNew.this, AccountLevelActivity.class);
			startActivity(intentLevel);
			
			break;
			
		case R.id.account_sex_layout:
			
			//性别
            Map<String,String > map=new HashMap<String, String>();
            map.put("1","男");
            map.put("0","女");
			showReasonPopwindow(view, map, sexId, false, "", new SelectApplyReasonWindow.IOnItemClick() {
                @Override
                public void onItemClick(String id, String value) {
                    if(id!=sexId){
                        sexId=id;
                        sexName=value;
                        changeSex(sexId);
                    }
                }
            });
			
			break;
		case R.id.account_birthday_layout:
			
			//出生日期
			DialogUtil.showBirthdayDialog(birthDay, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					//修改生日
					changeBirthday(birthDay.getText().toString());
				}
			},mContext);
			
			break;
		case R.id.account_add_ticket_layout:
			
			//增票资质
			Intent i = new Intent();
            i.setClass(mContext, AddedInvoiceActivity.class);
            startActivity(i);
			
			break;
		case R.id.account_address_layout:
			
			//地址管理
			Intent intentAddress = new Intent();
			intentAddress.setClass(AccountManageActivityNew.this, AddressManagementActivity.class);
			intentAddress.putExtra(AddressManagementActivity.TAG, AddressManagementActivity.addr);
			startActivity(intentAddress);
			
			break;
		case R.id.account_safty_layout:
			LogUtil.d("zhiheng","account_safty_layout 1");
			//账户安全
			Intent securityIntent = new Intent(this,AccountSecurityActivity.class);
			startActivityForResult(securityIntent, REQUEST_CODE_ACCOUNT_SAFTY);
			break;
		}
	};

    /**
     * 修改性别
     */
    private void changeSex(final String sex){
        RequstClient.changeSex(AppContext.userId,sex,new CustomResponseHandler(mContext,true){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showMyToast("修改成功！");
                    
                    account_sex.setText(sexName);
                    AppContext.mUserInfo.sex = sex;
                    
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

    private boolean checkSelectBirthday(String birthday){
        boolean result=true;
        String curTime=AppContext.curTime;
        Map<String , Integer> date=DateUtil.strToMap(birthday, false);
        int year=date.get("year");
        int month=date.get("month");
        int day=date.get("day");
        Map<String , Integer> curdate=DateUtil.strToMap(curTime,false);
        int curyear=curdate.get("year");
        int curmonth=curdate.get("month");
        int curday=curdate.get("day");
        if(year>curyear){//当前年大于系统时间
            result=false;
        }else if(month>curmonth){//当前月大于系统时间
            result=false;
        }else if(day>curday){//当前日大于系统时间
            result=false;
        }
        return result;
    }
	
    /**
     * 修改生日
     */
    private void changeBirthday(final String birthday){
        if(checkSelectBirthday(birthday)){
            RequstClient.changeBirthday(AppContext.userId, birthday,new CustomResponseHandler(mContext,true){
                @Override
                public void onSuccess(int statusCode, Header[] headers, String content) {
                    super.onSuccess(statusCode, headers, content);
                    try {
                        JSONObject obj = new JSONObject(content);
                        if (!obj.getString("type").equals("1")) {
                            String errorMsg = obj.getString("msg");
                            Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        showMyToast("修改成功！");
                        AppContext.mUserInfo.birthday = birthday;

                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            birthDay.setText("");
            showMyToast("生日日期不能超过今天！");
        }
    }
    
    /**
     * 修改昵称
     * @param change_name
     */
    private void doModify(final String change_name) {

		if (AppContext.userId == null) {
			return;
		}
		RequstClient.doModifyUsername(AppContext.userId, change_name, new CustomResponseHandler(this, true) {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				LogUtil.i(TAG, "doModify:" + content);
				try {
					JSONObject obj = new JSONObject(content);
					if (!obj.getString("type").equals("1")) {
						String errorMsg = obj.getString("msg");
						Toast.makeText(getBaseContext(), errorMsg, Toast.LENGTH_SHORT).show();
						return;
					}
					account_username.setText(change_name);
					if (AppContext.mUserInfo != null) {
						AppContext.mUserInfo.userName = change_name;
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
    
	@Override
	public void onUpLoadSuccess(String imageUrl, String imageFile) {

		//非空判断
		if (TextUtils.isEmpty(imageUrl) || TextUtils.isEmpty(imageFile)){
			return;
		}
		
		//将图片设置到缓存
		if (AppContext.mUserInfo != null) {
			AppContext.mUserInfo.img = imageUrl;
		}
		
		//重新显示上传成功的图片
		DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.showStubImage(R.drawable.default_head).showImageForEmptyUri(R.drawable.default_head)
				.showImageOnFail(R.drawable.default_head).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.bitmapConfig(Bitmap.Config.RGB_565).displayer(new RoundedBitmapDisplayer(100)).build();
		ImageManager.LoadWithServer(imageUrl, account_head, options);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_MODIFY_NAME:
			if(null!=data){
                //修改用户昵称
                String change_name = data.getStringExtra("username");
                doModify(change_name);
            }
			break;
		case REQUEST_CODE_ACCOUNT_SAFTY:
			
			//在账户安全里面修改了头像，需要重新刷新头像
			loadUserHead(account_head);
			
			break;
		}
		
	}
}
