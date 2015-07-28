package com.huiyin.ui.menberclub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseActivity;
import com.huiyin.bean.ExchangeResultBean;
import com.huiyin.bean.FillOrderBean;
import com.huiyin.bean.VIPExchangeBean;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.ui.fillorder.FillOrderActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.Utils;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 兑换详情
 * @author 高能
 *
 * @todo TODO
 *
 * @date 2015-7-22
 */
public class ExchangeDetailActivity extends BaseActivity implements
		OnClickListener {

	private static final String TAG = "ExchangeDetail";
	public static final String PRARMS = "giftId";
	private EditText goods_num;
	// 广告轮换图
	private ImageView exchange_goods_image;
	private VIPExchangeBean mExchangeBean;
	private TextView exchange_goods_name, exchange_goods_ad,
			exchange_goods_integral, exchange_goods_price,
			exchange_goods_condition, exchange_goods_stock,
			exchange_goods_explan, exchange_goods_submit;
	private String mGiftId;
	private String lastInput = "1";
	private TextWatcher myTextWathcer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //阻止EditText自动让输入法弹出
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_exchange_detail);
		initView();
		mGiftId = getIntent().getStringExtra(PRARMS);
		getGoodsDetail(mGiftId);
	}

	private void update() {
		ImageManager.LoadWithServer(
				mExchangeBean.getGiftDetail().getGIFT_IMG(),
				exchange_goods_image);
		exchange_goods_name.setText(mExchangeBean.getGiftDetail()
                .getGIFT_NAME());
		exchange_goods_ad.setText(mExchangeBean.getGiftDetail().getAD());
		setExchangeWay();
		setExchangeType();
		setExchangeCondition();
		exchange_goods_explan.setText(Html.fromHtml("<b>兑换介绍 :</b><br>"
                + mExchangeBean.getGiftDetail().getEXCHG_NOTE()));
	}

	/**
	 * 显示的兑换条件
	 */
	private void setExchangeCondition() {

		String condition;
		if (mExchangeBean.getGiftDetail().getIS_NUM_LIMIT().equals("1")) {
			// 限制兑换数量
			condition = mExchangeBean.getGiftDetail().getMIN_GRADE_TITLE()
					+ "及以上,每人最多兑"
					+ mExchangeBean.getGiftDetail().getEXCHG_MAX_COUNT() + "件";
		} else {
			// 不限制兑换数量
			condition = mExchangeBean.getGiftDetail().getMIN_GRADE_TITLE()+"及以上";
		}

		if (mExchangeBean.getGiftDetail().getIS_TIME_LIMIT().equals("1")) {
			// 限制兑换时间
			condition += "\n" + mExchangeBean.getGiftDetail().getBEGIN_TIME()
					+ "到" + mExchangeBean.getGiftDetail().getEND_TIME();
		}
		exchange_goods_condition.setText(condition);
	}

	/**
	 * 设置兑换的商品类型
	 */
	private void setExchangeType() {
		if (mExchangeBean.getGiftDetail().getEXCHG_TYPE().equals("1")) {
			// 虚拟商品
			exchange_goods_price.setVisibility(View.GONE);
			exchange_goods_stock.setText(mExchangeBean.getGiftDetail()
					.getGIFT_STOCK_SUM());
		} else if (mExchangeBean.getGiftDetail().getEXCHG_TYPE().equals("2")) {
			// 红包
			exchange_goods_price.setVisibility(View.GONE);
		} else if (mExchangeBean.getGiftDetail().getEXCHG_TYPE().equals("3")) {
			// 实物
			exchange_goods_price.setVisibility(View.VISIBLE);
			exchange_goods_price.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 中划线
			exchange_goods_price.setText("乐虎价： ￥"
					+ mExchangeBean.getGiftDetail().getGOODS_PRICE());
			// 如果是虚拟商品，直接显示GIFT_STOCK_SUM，如果是实物，就显示GIFT_STOCK_SUM和GOODS_STOCK中较小的那个
			if (Utils.anInt(mExchangeBean.getGiftDetail().getGIFT_STOCK_SUM()) < Utils
					.anInt(mExchangeBean.getGiftDetail().getGOODS_STOCK())) {
				exchange_goods_stock.setText(mExchangeBean.getGiftDetail()
						.getGIFT_STOCK_SUM());
			} else {
				exchange_goods_stock.setText(mExchangeBean.getGiftDetail()
						.getGOODS_STOCK());
			}
		}
	}

	/**
	 * 设置兑换方式
	 */
	private void setExchangeWay() {
		if (mExchangeBean.getGiftDetail().getEXCHG_MODE().equals("0")) {
			// 全积分抵扣
			exchange_goods_integral.setText("积分："
					+ mExchangeBean.getGiftDetail().getEXCHG_INTEGRAL());
		} else if (mExchangeBean.getGiftDetail().getEXCHG_MODE().equals("1")) {
			// 积分+金额抵扣
			exchange_goods_integral.setText("积分: "
                    + mExchangeBean.getGiftDetail().getEXCHG_INTEGRAL()
                    + " + ￥" + mExchangeBean.getGiftDetail().getADD_AMOUNT()
                    + "元");
		}
	}

	public void initView() {
		findViewById(R.id.right_ib).setVisibility(View.GONE);
		TextView mTv_title = (TextView) findViewById(R.id.middle_title_tv);
		TextView mTv_back = (TextView) findViewById(R.id.left_ib);
		goods_num = (EditText) findViewById(R.id.et_num);
		ImageView mIv_add_goods = (ImageView) findViewById(R.id.imageView_add);
		ImageView mIv_delete_goods = (ImageView) findViewById(R.id.imageView_delete);
		exchange_goods_image = (ImageView) findViewById(R.id.exchange_goods_image);
		exchange_goods_name = (TextView) findViewById(R.id.exchange_goods_name);
		exchange_goods_ad = (TextView) findViewById(R.id.exchange_goods_ad);
		exchange_goods_integral = (TextView) findViewById(R.id.exchange_goods_integral);
		exchange_goods_price = (TextView) findViewById(R.id.exchange_goods_price);
		exchange_goods_condition = (TextView) findViewById(R.id.exchange_goods_condition);
		exchange_goods_stock = (TextView) findViewById(R.id.exchange_goods_stock);
		exchange_goods_explan = (TextView) findViewById(R.id.exchange_goods_explan);
		exchange_goods_submit = (TextView) findViewById(R.id.exchange_goods_submit);

		mTv_back.setOnClickListener(this);
		mIv_add_goods.setOnClickListener(this);
		mIv_delete_goods.setOnClickListener(this);
		exchange_goods_submit.setOnClickListener(this);
		exchange_goods_image.setOnClickListener(this);

		mTv_title.setText("兑换详情");
		myTextWathcer = new MyTextWatch();
		setMyText(lastInput);
	}

	private void setMyText(String init) {
		goods_num.setText(init);
        setEditTextCursorLocation(goods_num);
		goods_num.addTextChangedListener(myTextWathcer);
	}

	@Override
	public void onClick(View arg0) {

		switch (arg0.getId()) {
		case R.id.left_ib:
			finish();
			break;
		case R.id.imageView_add:
		case R.id.imageView_delete:
			resetGoodsNum(arg0.getId());
			break;
		case R.id.exchange_goods_submit:
            if (AppContext.mUserInfo.levelValue<Utils.anInt(mExchangeBean.getGiftDetail().getGIFT_MIN_GROWTH())){
                UIHelper.showToast("您目前的等级不够，无法兑换");
                return;
            }
			submitExchange(mExchangeBean.getGiftDetail().getEXCHG_TYPE(),
					mGiftId, goods_num.getText().toString());
			break;
		case R.id.exchange_goods_image:
			Intent intent = new Intent(this, PhotoViewActivity.class);
			ArrayList<String> listDatas = new ArrayList<String>();
			listDatas.add(mExchangeBean.getGiftDetail().getGIFT_IMG());
			intent.putStringArrayListExtra(PhotoViewActivity.INTENT_KEY_PHOTO,
					listDatas);
			intent.putExtra(PhotoViewActivity.INTENT_KEY_POSITION, 0);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void resetGoodsNum(int resId) {
		int goodsNumTemp = Integer.valueOf(goods_num.getText().toString());
		if (resId == R.id.imageView_add) {
			goodsNumTemp++;
		} else if (resId == R.id.imageView_delete) {
			goodsNumTemp--;
		}
		if (checkGoodsNum(goodsNumTemp)) {
			goods_num.setText(goodsNumTemp + "");
            setEditTextCursorLocation(goods_num);
		}
	}

	/**
	 * 检查兑换数量的限制
	 * 
	 * @param goodsNumTemp
	 *            兑换数量
	 */
	private boolean checkGoodsNum(int goodsNumTemp) {
		if (goodsNumTemp < 1) {
			UIHelper.showToast("最少选择一件");
			return false;
		} else if (mExchangeBean.getGiftDetail().getIS_NUM_LIMIT().equals("1")
				&& goodsNumTemp > Utils.anInt(mExchangeBean.getGiftDetail()
						.getEXCHG_MAX_COUNT())) {
			UIHelper.showToast("此商品每人最多兑换"
					+ mExchangeBean.getGiftDetail().getEXCHG_MAX_COUNT() + "件");
			return false;
		} else if (goodsNumTemp > Utils.anInt(exchange_goods_stock.getText()
				.toString())) {
			UIHelper.showToast("兑换的数量不能超过商品的库存"
					+ exchange_goods_stock.getText().toString() + "件");
			return false;
		}
		return true;
	}

	private void getGoodsDetail(String giftId) {
		if (AppContext.userId != null) {
			RequstClient.giftDetail(AppContext.userId, giftId,
                    new CustomResponseHandler(this) {
                        @Override
                        public void onSuccess(String content) {
                            super.onSuccess(content);
                            if (LogUtil.isDebug)
                                Logger.json(content);
                            mExchangeBean = new Gson().fromJson(content,
                                    VIPExchangeBean.class);
                            if (mExchangeBean.getType() != 1) {
                                UIHelper.showToast(mExchangeBean.getMsg());
                                return;
                            } else if (mExchangeBean.getGiftDetail() == null) {
                                UIHelper.showToast("暂无数据");
                                return;
                            }
                            update();
                        }
                    });
		}
	}

	/**
	 * 提交兑换的商品
	 * 
	 * @param type
	 *            商品类型，1为虚拟商品，3为实物商品
	 * @param giftId
	 *            商品id
	 * @param num
	 *            数量
	 */
	private void submitExchange(String type, String giftId, String num) {
		if (AppContext.userId != null) {
			if (type.equals("1")) { // 兑换虚拟商品
				RequstClient.convertGift(AppContext.userId, giftId, num,
						new CustomResponseHandler(this) {
							@Override
							public void onSuccess(String content) {
								super.onSuccess(content);
								try {
									JSONObject mObject = new JSONObject(content);
									if (!mObject.getString("type").equals("1")) {
										UIHelper.showToast(mObject
												.getString("msg"));
									} else {
										UIHelper.showToast("兑换成功");
										Intent data = new Intent();
										data.putExtra(
												"result",
												new Gson()
														.fromJson(
                                                                content,
                                                                ExchangeResultBean.class));
										setResult(Activity.RESULT_OK, data);
										finish();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
			}else{
                buyNow(Utils.anInt(num));
            }
		}
	}
    /**
     * 实物礼品兑换提交
     *
     * @param num
     *            已选商品数量数量
     */
    public void buyNow(final int num) {
        final String submitType="10"; //礼品兑换
        RequstClient.cartPromptlyInit(AppContext.userId, "10", num,
                mExchangeBean.getGiftDetail().getREF_GOODS_ID(), mExchangeBean.getGiftDetail().getREF_GOODS_NO(),
                mExchangeBean.getGiftDetail().getID(),new CustomResponseHandler(mContext) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String content) {
                        super.onSuccess(statusCode, headers, content);
                        UIHelper.cloesLoadDialog();
                        LogUtil.d(TAG, "兑换实物礼品结果==" + content);
                        JSONObject obj;
                        try {
                            obj = new JSONObject(content);
                            if (!obj.getString("type").equals("1")) {
                                String errorMsg = obj.getString("msg");
                                Toast.makeText(getBaseContext(), errorMsg,
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (obj.getString("baseInfo") == null||obj.getString("baseInfo").equals("null")
                                    ||obj.getString("baseInfo").equals("")) {
                                Toast.makeText(getBaseContext(), "返回的填写订单数据为空",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (obj.getString("cart") == null||obj.getString("cart").equals("null")
                                    ||obj.getString("cart").equals("")) {
                                Toast.makeText(getBaseContext(), "返回的购物数据为空",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            FillOrderBean mFillOrderBean = new Gson().fromJson(
                                    content, FillOrderBean.class);
                            Intent mIntent = new Intent(ExchangeDetailActivity.this,
                                    FillOrderActivity.class);
                            mIntent.putExtra("fill_order_bean", mFillOrderBean);
                            mIntent.putExtra("type",
                                    Integer.valueOf(submitType));
                            int allIntegral= Utils.anInt(mExchangeBean.getGiftDetail().getEXCHG_INTEGRAL())*num;
                            mIntent.putExtra("integral",allIntegral);
                            startActivity(mIntent);
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                });
    }

	class MyTextWatch implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			lastInput = s.toString();
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			int goodsNumTemp = Utils.anInt(s.toString());
			if (goodsNumTemp < 1) {
				UIHelper.showToast("最少选择一件");
				goods_num.removeTextChangedListener(myTextWathcer);
				setMyText("1");
			} else if (mExchangeBean.getGiftDetail().getIS_NUM_LIMIT()
					.equals("1")
					&& goodsNumTemp > Utils.anInt(mExchangeBean.getGiftDetail()
							.getEXCHG_MAX_COUNT())) {
				UIHelper.showToast("此商品每人最多兑换"
						+ mExchangeBean.getGiftDetail().getEXCHG_MAX_COUNT()
						+ "件");
				goods_num.removeTextChangedListener(myTextWathcer);
				setMyText(mExchangeBean.getGiftDetail().getEXCHG_MAX_COUNT());
			} else if (goodsNumTemp > Utils.anInt(exchange_goods_stock
					.getText().toString())) {
				UIHelper.showToast("兑换的数量不能超过商品的库存"
						+ exchange_goods_stock.getText().toString() + "件");
				goods_num.removeTextChangedListener(myTextWathcer);
				setMyText(exchange_goods_stock.getText().toString());
			}
		}
	}
    // 将EditText的光标定位到字符的最后面
    public void setEditTextCursorLocation(EditText editText) {
        CharSequence text = editText.getText();
        if (text != null) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }
}
