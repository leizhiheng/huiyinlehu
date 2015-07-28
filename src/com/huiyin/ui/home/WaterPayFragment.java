package com.huiyin.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.PaymentLocationBean;
import com.huiyin.utils.Code;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.Utils;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class WaterPayFragment extends Fragment implements View.OnClickListener {
	private TextView water_payment;
	private TextView electric_payment;
	private TextView coal_payment;
	private TextView water_payment_area;
	private TextView water_payment_location;
	private EditText water_payment_consumor;
	private EditText water_payment_phone;
	private TextView water_payment_year;
	private TextView water_payment_month;
	private EditText water_payment_check;
	private ImageView water_payment_ma;
	private Button water_payment_next;
	private View Root;
	private String[] monthsArray = { "12", "11", "10", "09", "08", "07", "06",
			"05", "04", "03", "02", "01" };
	private HashMap<String, String> yearMap;
	private HashMap<String, String> monthMap;
	private HashMap<String, String> location;
	private List<String> years;
	private List<String> months;
	private List<String> provinces;
	private HashMap<String, List<String>> citys;
	private PaymentLocationBean mLocationBean;
	private int NowYear;
	private int NowMonth;
	public static final String IDTYPE = "ID";
	private String Id = "0";

	public static WaterPayFragment newInstance(String ID) {
		WaterPayFragment mFragment = new WaterPayFragment();
		Bundle mBundle = new Bundle();
		mBundle.putString(IDTYPE, ID);
		mFragment.setArguments(mBundle);
		return mFragment;
	}

	public WaterPayFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			Id = getArguments().getString(IDTYPE);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		Root = inflater.inflate(R.layout.fragment_water_pay, container, false);
		bindViews(Root);
		setListener();
		getLoacationRes();
		return Root;
	}

	private void setListener() {
		water_payment.setOnClickListener(this);
		electric_payment.setOnClickListener(this);
		coal_payment.setOnClickListener(this);
		water_payment_area.setOnClickListener(this);
		water_payment_location.setOnClickListener(this);
		water_payment_year.setOnClickListener(this);
		water_payment_month.setOnClickListener(this);
		water_payment_next.setOnClickListener(this);
		water_payment_ma.setOnClickListener(this);
	}

	private void init() {
		if (Id.equals("0")) {
			// 电费
			selectWCE(2);
		} else if (Id.equals("1")) {
			// 水费
			selectWCE(1);
		} else if (Id.equals("2")) {
			// 煤气费
			selectWCE(3);
		}
        if (mLocationBean!=null&&mLocationBean.getCityList()!=null&&mLocationBean.getCityList().size()>0){
            water_payment_area.setText(mLocationBean.getCityList().get(0).getKey()
                    + " > "
                    + mLocationBean.getCityList().get(0).getValue().get(0)
                    .getNAME());
            collectDateAndLocation();
            water_payment_year.setText(NowYear + "年");
            water_payment_month.setText(getMonthStr(NowMonth) + "月");
        }
		water_payment_ma.setImageBitmap(Code.getInstance().createBitmap());
	}
	/**
	 * 整理时间和地区
	 */
	private void collectDateAndLocation() {
		provinces = new ArrayList<String>();
		citys = new HashMap<String, List<String>>();
		for (int i = 0; i < mLocationBean.getCityList().size(); i++) {
			provinces.add(mLocationBean.getCityList().get(i).getKey());
			List<String> temp = new ArrayList<String>();
			for (int j = 0; j < mLocationBean.getCityList().get(i).getValue()
					.size(); j++) {
				temp.add(mLocationBean.getCityList().get(i).getValue().get(j)
						.getNAME());
			}
			citys.put(mLocationBean.getCityList().get(i).getKey(), temp);
		}
		if (location == null) {
			location = new HashMap<String, String>();
		}
		location.put("0", mLocationBean.getCityList().get(0).getKey());
		location.put("1", mLocationBean.getCityList().get(0).getValue().get(0)
				.getNAME()
				+ "");
		// 时间
		NowYear = Utils.getYMD(mLocationBean.getCurTime())[0];
		NowMonth = Utils.getYMD(mLocationBean.getCurTime())[1];
        //默认显示上个月
        if (NowMonth==1){
            NowYear-=1;
            NowMonth=12;
        }else{
            NowMonth-=1;
        }
		years = new ArrayList<String>();
		// 从2011年开始
		for (int i = NowYear; i > 2010; i--) {
			years.add(i + "");
		}
		months = new ArrayList<String>();
		Collections.addAll(months, monthsArray);
		if (yearMap == null) {
			yearMap = new HashMap<String, String>();
		}
		if (monthMap == null) {
			monthMap = new HashMap<String, String>();
		}
		yearMap.put("0", NowYear + "");
		monthMap.put("0", getMonthStr(NowMonth)+ "");
	}
    private String getMonthStr(int month){
        if (month<10){
            return "0"+month;
        }else {
            return month+"";
        }
    }

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private void bindViews(View view) {
		water_payment = (TextView) view.findViewById(R.id.water_payment);
		electric_payment = (TextView) view.findViewById(R.id.electric_payment);
		coal_payment = (TextView) view.findViewById(R.id.coal_payment);
		water_payment_area = (TextView) view
				.findViewById(R.id.water_payment_area);
		water_payment_location = (TextView) view
				.findViewById(R.id.water_payment_location);
		water_payment_consumor = (EditText) view
				.findViewById(R.id.water_payment_consumor);
		water_payment_phone = (EditText) view
				.findViewById(R.id.water_payment_phone);
		water_payment_year = (TextView) view
				.findViewById(R.id.water_payment_year);
		water_payment_month = (TextView) view
				.findViewById(R.id.water_payment_month);
		water_payment_check = (EditText) view
				.findViewById(R.id.water_payment_check);
		water_payment_ma = (ImageView) view.findViewById(R.id.water_payment_ma);
		water_payment_next = (Button) view
				.findViewById(R.id.water_payment_next);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.water_payment:
			selectWCE(1);
			Id = "1";
			break;
		case R.id.electric_payment:
			selectWCE(2);
			Id = "0";
			break;
		case R.id.coal_payment:
			selectWCE(3);
			Id = "2";
			break;
		case R.id.water_payment_area:
		case R.id.water_payment_location:
			RightPopListSelect mLoc = new RightPopListSelect(1, getActivity(),
					Root, provinces, citys, null, location, "选择地区",
					new RightPopListSelect.SelectResultListener() {

						@Override
						public void selectResult(HashMap<String, String> result) {
							if (result.get("0") != null
									&& result.get("1") != null) {
								if (location == null) {
									location = new HashMap<String, String>();
								}
								location.put("0", result.get("0"));
								location.put("1", result.get("1"));
								water_payment_area.setText(result.get("0")
										+ " > " + result.get("1"));
							}
						}
					});
			break;
		case R.id.water_payment_year:
			RightPopListSelect mYear = new RightPopListSelect(getActivity(),
					Root, years, null, null, yearMap, "选择年份",
					new RightPopListSelect.SelectResultListener() {

						@Override
						public void selectResult(HashMap<String, String> result) {
							if (result.get("0") != null) {
								if (yearMap == null) {
									yearMap = new HashMap<String, String>();
								}
								yearMap.put("0", result.get("0"));
								water_payment_year.setText(result.get("0")
										+ "年");
							}
						}
					});
			break;
		case R.id.water_payment_month:

			RightPopListSelect mMonth = new RightPopListSelect(getActivity(),
					Root, months, null, null, monthMap, "选择月份",
					new RightPopListSelect.SelectResultListener() {

						@Override
						public void selectResult(HashMap<String, String> result) {
							if (result.get("0") != null) {
								if (monthMap == null) {
									monthMap = new HashMap<String, String>();
								}
								monthMap.put("0", result.get("0"));
								water_payment_month.setText(result.get("0")
										+ "月");
							}
						}
					});
			break;
		case R.id.water_payment_ma:
			water_payment_ma.setImageBitmap(Code.getInstance().createBitmap());
			break;
		case R.id.water_payment_next:
			AppContext.userNum = water_payment_consumor.getText().toString();
            queryBill();
//			pushToNextFragment("1233", "00", "1111", "12155", "212", "dsf");
			break;
		}
	}

	/**
	 * 跳转到确认并缴费页面
	 * 
	 * @param price
	 *            应缴费用
	 * @param status
	 *            返回的缴费状态码
	 * @param number
     *
	 *            缴费订单编号
	 * @param id
	 *            缴费订单ID
	 * @param perpaidCard
	 *            便民卡预付金
	 */
	private void pushToNextFragment(String price, String status, String number,
			String id, String perpaidCard, String msg) {
		((PayProcessFragment) getParentFragment())
				.changeFragment(WaterComfirFragment.newInstance(price, status,
						number, id, perpaidCard, msg));
	}

	private void selectWCE(int type) {
		switch (type) {
		case 1:
			water_payment.setSelected(true);
			water_payment.setTextColor(getResources().getColor(R.color.white));
			electric_payment.setSelected(false);
			electric_payment.setTextColor(getResources()
					.getColor(R.color.black));
			coal_payment.setSelected(false);
			coal_payment.setTextColor(getResources().getColor(R.color.black));
			break;
		case 2:
			water_payment.setSelected(false);
			water_payment.setTextColor(getResources().getColor(R.color.black));
			electric_payment.setSelected(true);
			electric_payment.setTextColor(getResources()
					.getColor(R.color.white));
			coal_payment.setSelected(false);
			coal_payment.setTextColor(getResources().getColor(R.color.black));
			break;
		case 3:
			water_payment.setSelected(false);
			water_payment.setTextColor(getResources().getColor(R.color.black));
			electric_payment.setSelected(false);
			electric_payment.setTextColor(getResources()
					.getColor(R.color.black));
			coal_payment.setSelected(true);
			coal_payment.setTextColor(getResources().getColor(R.color.white));
			break;
		}
	}

	private void getLoacationRes() {
		RequstClient.cityList(new CustomResponseHandler(getActivity()) {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				mLocationBean = new Gson().fromJson(content,
						PaymentLocationBean.class);
				if (mLocationBean.getType() != 1) {
					UIHelper.showToast(mLocationBean.getMsg());
				} else {
					if (mLocationBean.getCityList() == null) {
						UIHelper.showToast("暂无水电煤缴费地区");
					} else {
						init();
					}
				}
			}
		});
	}

	private boolean check() {
		if (TextUtils.isEmpty(water_payment_consumor.getText().toString())) {
			UIHelper.showToast("缴费用户不能为空");
			return false;
		} else if (TextUtils.isEmpty(water_payment_phone.getText().toString())) {
			UIHelper.showToast("手机号码不能为空");
			return false;
		} else if (!Utils.checkPhoneREX(water_payment_phone.getText()
				.toString())) {
			UIHelper.showToast("手机号码不符合规则");
			return false;
		} else if (location == null || location.get("0") == null
				|| location.get("1") == null) {
			UIHelper.showToast("请选择缴费市区");
			return false;
		} else if (yearMap == null || yearMap.get("0") == null
				|| monthMap == null || monthMap.get("0") == null) {
			UIHelper.showToast("请选择查询年月份");
			return false;
		} else if (TextUtils.isEmpty(water_payment_check.getText().toString())) {
			UIHelper.showToast("请输入验证码");
			return false;
		} else if (!water_payment_check.getText().toString().toLowerCase()
				.equals(Code.getInstance().getCode().toLowerCase())) {
			UIHelper.showToast("验证码错误!");
			return false;
		}
		return true;
	}

	private void queryBill() {
		if (AppContext.userId != null) {
			if (!check()) {
				water_payment_ma.setImageBitmap(Code.getInstance()
						.createBitmap());
				return;
			}
            String[] area=getAreaCodeByAreaName(location.get("1"));
			RequstClient.shareBill(AppContext.userId, Id,
                    area[0], location.get("1"),area[1],area[2], water_payment_consumor.getText()
							.toString().trim(), yearMap.get("0"),
					monthMap.get("0"), water_payment_phone.getText().toString()
							.trim(), new CustomResponseHandler(getActivity()) {
						@Override
						public void onSuccess(String content) {
							super.onSuccess(content);
                            if (LogUtil.isDebug) Logger.json(content);
							try {
								JSONObject mJSONObject = new JSONObject(content);
								String type = mJSONObject.optString("type");
								String msg = mJSONObject.optString("msg");
								String respCode = mJSONObject
										.optString("respCode");
								String price = mJSONObject.optString("price");
								String orderId = mJSONObject
										.optString("orderId");
								String orderCode = mJSONObject
										.optString("orderCode");
								String payItem = mJSONObject
										.optString("payItem");
								String perpaidCard = mJSONObject
										.optString("perpaidCard");
								String curTime = mJSONObject
										.optString("curTime");

								pushToNextFragment(price, respCode, orderCode,
										orderId, perpaidCard, msg);

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}
	}

    /**
     * 根据地区名称获得地区编号，商户号，终端号
     * @param areaName
     * @return
     */
	private String[] getAreaCodeByAreaName(String areaName) {
        String[] area=new String[3];
		for (int i = 0; i < mLocationBean.getCityList().size(); i++) {
			for (int j = 0; j < mLocationBean.getCityList().get(i).getValue()
					.size(); j++) {
				if (areaName.equals(mLocationBean.getCityList().get(i)
						.getValue().get(j).getNAME())) {

                    area[0]= mLocationBean.getCityList().get(i).getValue().get(j)
							.getCODE();//地区编号
                    area[1]= mLocationBean.getCityList().get(i).getValue().get(j)
							.getTERMINAL_NO();//商户号
                    area[2]= mLocationBean.getCityList().get(i).getValue().get(j)
							.getMERCHANT_NO(); //终端号
				}
			}
		}
		return area;
	}
}
