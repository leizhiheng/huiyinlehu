package com.huiyin.ui.shoppingcar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.InvoiceAddBean;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.Utils;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class InvoiceInfoActivity extends BaseActivity {
	private final static String TAG = "InvoiceInfoActivity";
	TextView left_rb, middle_title_tv, right_rb;
	ImageView inv_title_personal, inv_title_company;
	Button normal_inv, value_added_inv;
	CheckBox inv_detail;
	// 发票抬头
	EditText inv_title;
	// 单位、识别号、注册地址、电话、开户银行、银行账号
	EditText inv_company, inv_identify, inv_regist_addr, inv_phone, inv_bank,
			inv_account;
	// 收票人地址、手机、地址
	EditText inv_receiver, inv_receiver_phone, inv_receiver_addr;

	TextView inv_save_info;

	LinearLayout added_inv_layout_1, added_inv_layout_2, normal_inv_layout;

	String invoice_way, invoice_title, invoice_content;

	String company_name, identification_number, registered_address,
			registered_phone, bank, account, collector_name, collector_phone,
			collector_address;

	private static final String NORMAL = "0", ADDED = "1";
	private static final String DETAIL_UNCHECK = "0", DETAIL_CHECK = "1";
	// 发票id
	String invoices_id;
	// 发票抬头类型 1个人 2单位
	String invoice_title_type = "1";
	public static final String INTENT_KEY_FLAG = "flag";
	public static final String INTENT_KEY_FLAG1 = "flag1";
	public static final String INTENT_KEY_FLAG2 = "flag2";
	public int flag;	// 大于0读取后台已填写的增值税发票

    public int flag1;//>0表示可以开增值税发票,否则不可以（是否可以增值税发票）先判断是否可以开增值税发票，如果可以，再判断有误增值税发票
    private int flag2;
    private String AddID ="0";

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invoice_info_layout);
		initView();
	}

	private void initView() {
		left_rb = (TextView) findViewById(R.id.left_ib);
		right_rb = (TextView) findViewById(R.id.right_ib);
		right_rb.setVisibility(View.GONE);
		left_rb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

		middle_title_tv = (TextView) findViewById(R.id.middle_title_tv);
		middle_title_tv.setText("发票信息");

		inv_title_personal = (ImageView) findViewById(R.id.inv_title_personal);
		inv_title_company = (ImageView) findViewById(R.id.inv_title_company);

		inv_title_personal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				inv_title_personal
						.setBackgroundResource(R.drawable.radio_btn_checked);
				inv_title_company
						.setBackgroundResource(R.drawable.radio_btn_normal);
				invoice_title_type = "1";
			}
		});

		inv_title_company.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				inv_title_personal
						.setBackgroundResource(R.drawable.radio_btn_normal);
				inv_title_company
						.setBackgroundResource(R.drawable.radio_btn_checked);
				invoice_title_type = "2";
                inv_title.setText("");
			}
		});
		value_added_inv = (Button) findViewById(R.id.value_added_inv);
		normal_inv = (Button) findViewById(R.id.normal_inv);

		inv_detail = (CheckBox) findViewById(R.id.inv_detail);
		invoice_content = DETAIL_UNCHECK;
		inv_detail.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					inv_detail
							.setBackgroundResource(R.drawable.radio_btn_checked);
					invoice_content = DETAIL_CHECK;
				} else {
					inv_detail
							.setBackgroundResource(R.drawable.radio_btn_normal);
					invoice_content = DETAIL_UNCHECK;
				}
			}
		});

		inv_title = (EditText) findViewById(R.id.inv_title);
		inv_company = (EditText) findViewById(R.id.inv_company);
		inv_identify = (EditText) findViewById(R.id.inv_identify);
		inv_regist_addr = (EditText) findViewById(R.id.inv_regist_addr);
		inv_phone = (EditText) findViewById(R.id.inv_phone);
		inv_bank = (EditText) findViewById(R.id.inv_bank);
		inv_account = (EditText) findViewById(R.id.inv_account);
		inv_receiver = (EditText) findViewById(R.id.inv_receiver);
		inv_receiver_phone = (EditText) findViewById(R.id.inv_receiver_phone);
		inv_receiver_addr = (EditText) findViewById(R.id.inv_receiver_addr);

		added_inv_layout_1 = (LinearLayout) findViewById(R.id.added_inv_layout_1);
		added_inv_layout_2 = (LinearLayout) findViewById(R.id.added_inv_layout_2);
		normal_inv_layout = (LinearLayout) findViewById(R.id.normal_inv_layout);

		inv_save_info = (TextView) findViewById(R.id.inv_save_info);
		inv_save_info.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (normal_inv_layout.getVisibility() == View.VISIBLE) {
					// 普通发票
					invoice_title = inv_title.getText().toString();
					if (invoice_title.equals("")) {
						Toast.makeText(InvoiceInfoActivity.this, "发票抬头不能为空！",
								Toast.LENGTH_LONG).show();
						return;
					}
					if (AppContext.userId == null) {
						backInfo();
					} else {
						postNormalInvInfo();
					}

				} else {
					// 增值发票
					invoice_title = inv_company.getText().toString();
					if (!checkInfo()) {
						return;
					}
					if (AppContext.userId == null) {
						backInfo();
					} else {
						postAddedInvInfo();
					}
				}
			}
		});

		normal_inv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				added_inv_layout_1.setVisibility(View.GONE);
				added_inv_layout_2.setVisibility(View.GONE);
				normal_inv_layout.setVisibility(View.VISIBLE);
				normal_inv.setSelected(true);
                normal_inv.setTextColor(getResources().getColor(R.color.white));
				value_added_inv.setSelected(false);
                value_added_inv.setTextColor(getResources().getColor(R.color.black));
				invoice_way = NORMAL;
			}
		});

		value_added_inv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				added_inv_layout_1.setVisibility(View.VISIBLE);
				added_inv_layout_2.setVisibility(View.VISIBLE);
				normal_inv_layout.setVisibility(View.GONE);
				normal_inv.setSelected(false);
                normal_inv.setTextColor(getResources().getColor(R.color.black));
				value_added_inv.setSelected(true);
                value_added_inv.setTextColor(getResources().getColor(R.color.white));
                invoice_way = ADDED;
			}
		});
		flag = getIntent().getIntExtra(INTENT_KEY_FLAG, 0);//>0有增值税发票，需要请求后台接口
        flag1 = getIntent().getIntExtra(INTENT_KEY_FLAG1, 0);//>0表示可以开增值税发票,否则不可以
        flag2 = getIntent().getIntExtra(INTENT_KEY_FLAG2, 0);//在增值税发票和普通发票都能使用的情况下，0为普通发票，1为增值发票
		invoice_way = NORMAL;
        normal_inv.setSelected(true);
        normal_inv.setTextColor(getResources().getColor(R.color.white));
        value_added_inv.setSelected(false);
        value_added_inv.setTextColor(getResources().getColor(R.color.black));
        if (flag1>0){
            if (flag>0){
                getAddedInvoice();
            }
        }else{
            value_added_inv.setVisibility(View.GONE);
            normal_inv.setBackgroundResource(R.drawable.bg_blue_stroke_normal);
            normal_inv.setTextColor(getResources().getColor(R.color.white));
        }
        String addressName=getIntent().getStringExtra("addressName");
        if (addressName!=null){
            inv_title.setText(addressName);
        }
	}

	/***
	 * 返回上页
	 */
	private void backInfo() {
		finish();
	}

	/***
	 * 普通发票
	 */
	private void postNormalInvInfo() {
		if (AppContext.userId == null) {
			return;
		}
		RequstClient.postNormalInvoiceInfo(AppContext.userId, invoice_way,
				invoice_title, invoice_content, invoice_title_type,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "postNormalInvoiceInfo:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							invoices_id = obj.optString("invoiceId");
							Intent i = new Intent();
							i.setClass(InvoiceInfoActivity.this,
                                    WriteOrderActivity.class);
							i.putExtra("invoice_id", invoices_id);
							i.putExtra("invoice_way", invoice_way);
							i.putExtra("invoice_title", invoice_title);
							i.putExtra("invoice_content", invoice_content);
							i.putExtra("invoice_title_type", invoice_title_type);
							setResult(RESULT_OK, i);
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

	private boolean checkInfo() {
		company_name = inv_company.getText().toString();
		identification_number = inv_identify.getText().toString();
		registered_address = inv_regist_addr.getText().toString();
		registered_phone = inv_phone.getText().toString();
		bank = inv_bank.getText().toString();
		account = inv_account.getText().toString();
		collector_name = inv_receiver.getText().toString();
		collector_phone = inv_receiver_phone.getText().toString();
		collector_address = inv_receiver_addr.getText().toString();

		if (TextUtils.isEmpty(company_name)) {
			Toast.makeText(InvoiceInfoActivity.this, "单位名称不能为空！",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(identification_number)) {
			Toast.makeText(InvoiceInfoActivity.this, "识别号不能为空！",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(registered_address)) {
			Toast.makeText(InvoiceInfoActivity.this, "注册地址不能为空！",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(registered_phone)) {
			Toast.makeText(InvoiceInfoActivity.this, "固定电话不能为空！",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(bank)) {
			Toast.makeText(InvoiceInfoActivity.this, "开户银行不能为空！",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(account)) {
			Toast.makeText(InvoiceInfoActivity.this, "银行账号不能为空！",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(collector_name)) {
			Toast.makeText(InvoiceInfoActivity.this, "收票人姓名不能为空！",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(collector_phone)) {
			Toast.makeText(InvoiceInfoActivity.this, "收票人手机不能为空！",
					Toast.LENGTH_LONG).show();
			return false;
		}else if (!Utils.checkPhoneREX(collector_phone)) {
			Toast.makeText(InvoiceInfoActivity.this, "收票人手机号码格式不正确！",
					Toast.LENGTH_LONG).show();
			return false;
		} else if (TextUtils.isEmpty(collector_address)) {
			Toast.makeText(InvoiceInfoActivity.this, "收票人地址不能为空！",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;

	}

	/**
	 * 增值发票
	 */

	private void postAddedInvInfo() {

		if (AppContext.userId == null) {
			return;
		}
		RequstClient.postAddedInvoiceInfo(AppContext.userId, invoice_way,
				invoice_content, company_name, identification_number,
				registered_address, registered_phone, bank, account,
				collector_name, collector_phone, collector_address,"", AddID,
				new CustomResponseHandler(this) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						LogUtil.i(TAG, "postAddedInvInfo:" + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (!obj.getString("type").equals("1")) {
								String errorMsg = obj.getString("msg");
								Toast.makeText(getBaseContext(), errorMsg,
										Toast.LENGTH_SHORT).show();
								return;
							}
							invoices_id = obj.optString("invoiceId");
							Intent i = new Intent();
							i.putExtra("invoice_id", invoices_id);
							i.putExtra("invoice_way", invoice_way);
							i.putExtra("invoice_title", invoice_title);
                            flag2=1;
                            i.putExtra("invoice_flag2", flag2);

                            setResult(RESULT_OK, i);
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
     * 查询已有的增值税发票
     */
	private void getAddedInvoice() {
		if (AppContext.userId != null) {
			RequstClient.vat(AppContext.userId,
					new CustomResponseHandler(this) {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
							Logger.json(content);
							JSONObject obj = null;
							try {
								obj = new JSONObject(content);
								if (!obj.getString("type").equals("1")) {
									String errorMsg = obj.getString("msg");
									UIHelper.showToast(errorMsg);
									return;
								}
								InvoiceAddBean mAddBean = new Gson().fromJson(
										content, InvoiceAddBean.class);
								if (mAddBean==null||mAddBean.getVat()==null)
									return;
                                AddID =mAddBean.getVat().getID();
								inv_company.setText(mAddBean.getVat()
										.getCOMPANY_NAME());
								inv_identify.setText(mAddBean.getVat()
										.getIDENTIFICATION_NUMBER());
								inv_regist_addr.setText(mAddBean.getVat()
										.getREGISTERED_ADDRESS());
								inv_phone.setText(mAddBean.getVat()
										.getREGISTERED_PHONE());
								inv_bank.setText(mAddBean.getVat().getBANK());
								inv_account.setText(mAddBean.getVat()
										.getACCOUNT());
								inv_receiver.setText(mAddBean.getVat()
										.getCOLLECTOR_NAME());
								inv_receiver_phone.setText(mAddBean.getVat()
										.getCOLLECTOR_PHONE());
								inv_receiver_addr.setText(mAddBean.getVat()
										.getCOLLECTOR_ADDRESS());
                                if (flag2==1){
                                    added_inv_layout_1.setVisibility(View.VISIBLE);
                                    added_inv_layout_2.setVisibility(View.VISIBLE);
                                    normal_inv_layout.setVisibility(View.GONE);
                                    normal_inv.setSelected(false);
                                    normal_inv.setTextColor(getResources().getColor(R.color.black));
                                    value_added_inv.setSelected(true);
                                    value_added_inv.setTextColor(getResources().getColor(R.color.white));
                                    invoice_way = ADDED;
                                }
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}
}
