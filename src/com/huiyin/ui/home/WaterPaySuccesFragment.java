package com.huiyin.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.utils.ResourceUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaterPaySuccesFragment extends Fragment implements
		View.OnClickListener {
	private static final String ARG_STATUS = "status";
	private static final String ARG_PRICE = "price";
	private static final String ARG_USERNO = "userNo";
	private static final String ARG_WAY = "way";
	private static final String ARG_MSG = "msg";
	private TextView water_payment_succes_ok, water_payment_succes_consumor;
	private TextView water_payment_succes_money;
	private ImageView water_payment_succes_way;
	private ImageView water_payment_succes_close;
	private ImageView water_payment_succes_tomain;
	private String userNo;
	private String price;
	private int way;
	private String status;
	private String msg;

	public WaterPaySuccesFragment() {
		// Required empty public constructor
	}

	public static WaterPaySuccesFragment newInstance(String status,
			String userNo, String price, int way,String msg) {
		WaterPaySuccesFragment fragment = new WaterPaySuccesFragment();
		Bundle args = new Bundle();
		args.putString(ARG_STATUS, status);
		args.putString(ARG_PRICE, price);
		args.putString(ARG_USERNO, userNo);
		args.putString(ARG_MSG, msg);
        args.putInt(ARG_WAY, way);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			price = getArguments().getString(ARG_PRICE);
			userNo = getArguments().getString(ARG_USERNO);
			way = getArguments().getInt(ARG_WAY);
			status = getArguments().getString(ARG_STATUS);
            msg = getArguments().getString(ARG_MSG);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View Root = inflater.inflate(R.layout.fragment_water_pay_succes,
				container, false);
		bindViews(Root);
		setListener();
		init();
		return Root;
	}

	private void init() {
		if (status.equals("1")) {
			// 支付成功
			water_payment_succes_ok.setText("缴费成功");
            water_payment_succes_consumor.setText("缴费用户:" + userNo);
            water_payment_succes_money.setText(Html.fromHtml("缴费金额: "
                    + ResourceUtils.changeStringColor("#FE0000", "￥" + price)));
		} else if (status.equals("2")) {
			// 支付失败
			water_payment_succes_ok.setText("缴费失败");
            water_payment_succes_consumor.setText("失败原因:" + msg);
		}

		if (1 == way) {// 支付宝
			water_payment_succes_way.setImageResource(R.drawable.pay_alipay);
		} else if (2 == way) {// 银联
			water_payment_succes_way.setImageResource(R.drawable.pay_up);
		} else if (3 == way) {// 微信
			water_payment_succes_way.setImageResource(R.drawable.pay_weixin);
		} else if (4 == way) {// 服务卡
			water_payment_succes_way
					.setImageResource(R.drawable.pay_service_card);
		} else if (5 == way) {// 中行
			water_payment_succes_way
					.setImageResource(R.drawable.logo_china_bank);
		} else if (6 == way) {// 建行
			water_payment_succes_way
					.setImageResource(R.drawable.logo_construction_bank);
		}
	}

	private void setListener() {
		water_payment_succes_close.setOnClickListener(this);
		water_payment_succes_tomain.setOnClickListener(this);
	}

	private void bindViews(View root) {
		water_payment_succes_consumor = (TextView) root
				.findViewById(R.id.water_payment_succes_consumor);
		water_payment_succes_ok = (TextView) root
				.findViewById(R.id.water_payment_succes_ok);
		water_payment_succes_money = (TextView) root
				.findViewById(R.id.water_payment_succes_money);
		water_payment_succes_way = (ImageView) root
				.findViewById(R.id.water_payment_succes_way);
		water_payment_succes_close = (ImageView) root
				.findViewById(R.id.water_payment_succes_close);
		water_payment_succes_tomain = (ImageView) root
				.findViewById(R.id.water_payment_succes_tomain);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.water_payment_succes_close:
            ((PayProcessFragment) getParentFragment()).onFirstUserVisible();
			((WaterCoalElectricActivity) getActivity()).changeViewPager(1);
			break;
		case R.id.water_payment_succes_tomain:
			getActivity().finish();
			break;
		}
	}
}
