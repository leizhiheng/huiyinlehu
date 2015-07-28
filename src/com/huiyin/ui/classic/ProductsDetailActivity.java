package com.huiyin.ui.classic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.BrowseItem;
import com.huiyin.bean.FillOrderBean;
import com.huiyin.bean.ProductsDetailBeen;
import com.huiyin.db.ScanRecordDao;
import com.huiyin.dialog.SingleConfirmDialog;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.fillorder.FillOrderActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductsDetailActivity extends BaseGoodsDetailActivity implements
		OnClickListener {
	private Context context = ProductsDetailActivity.this;
	private static final String TAG = "ProductsDetailActivity";
	public static final int MIAOSHA_CODE = 1;
    public boolean isUpdated=false;  //是否是刷新当前商品的规格

	/**
	 * 商品详情的key
	 */
	public static final String BUNDLE_KEY_GOODS_ID = "goods_detail_id";
	/**
	 * 商品店铺id
	 */
	public static final String STORE_ID = "STORE_ID";
	/**
	 * 商品编号
	 */
	public static final String GOODS_NO = "GOODS_NO";

	/**
	 * 用户id
	 */
	public static final String USER_ID = "USER_ID";

	/**
	 * 预约key
	 */
	public static final String BUNDLE_KEY_subscribe_ID = "subscribeId";
	/**
	 * 商品的id
	 */
	public String goodsId;// 商品ID
	public ProductsDetailBeen gdbbean;// 商品数据
	private String storeId;// 店铺ID
	private String goodsNo; // 商品编号

	private BasicInformationFragment basicInformationFragment;// 基本信息
	private GoodsDetailWebViewFragment goodsDetailWebViewFragment;
	public Button btn_shopping_car, btn_checkout, btn_add, btn_order;// 底部三按钮
	private TextView tv_shopping_car_count;
	private ViewSwitcher mViewSwitcher;
	public static int showMenuType = 0;// 0加入购物车过来,1结算过来的
	// purchase 立即结算2，加入购物车1，组合购买3

	private ScanRecordDao scanRecordDao;
	private Activity mContext;
	private int subscribeId;// 预约ID

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_layout_products);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.actionbar_common_back_btn2);
		setSlidingActionBarEnabled(false);// 标题栏不移动

		goodsId = getIntent().getStringExtra(BUNDLE_KEY_GOODS_ID);
		subscribeId = getIntent().getIntExtra(BUNDLE_KEY_subscribe_ID, 0);
		storeId = getIntent().getStringExtra(STORE_ID);
		goodsNo = getIntent().getStringExtra(GOODS_NO);

		mContext = this;
		initView();
		initData();
		goodsDetailsHttp();
	}

	private void initView() {
		// 标题
		TextView titleText = (TextView) findViewById(R.id.ab_title);
		titleText.setText("商品详情");

		// 返回按钮
		TextView btnBack = (TextView) findViewById(R.id.ab_back);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});

		// 右边按钮
		ImageView ab_right_btn1 = (ImageView) findViewById(R.id.ab_right_btn1);
		ImageView ab_right_btn2 = (ImageView) findViewById(R.id.ab_right_btn2);
		ab_right_btn1.setImageResource(R.drawable.ab_ic_share);
		// 设置隐藏
		ab_right_btn1.setVisibility(View.GONE);

		ab_right_btn1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// YtShare.createInstance(GoodsDetailActivity.this).share();
			}
		});
		ab_right_btn2.setVisibility(View.GONE);
		ab_right_btn2.setImageResource(R.drawable.ab_ic_customer);
		ab_right_btn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// RongCloud.getInstance(GoodsDetailActivity.this)
				// .startCustomerServiceChat();
			}
		});

		tv_shopping_car_count = (TextView) findViewById(R.id.tv_shopping_car_count);
		btn_shopping_car = (Button) findViewById(R.id.btn_shopping_car);
		btn_checkout = (Button) findViewById(R.id.btn_checkout);
		btn_add = (Button) findViewById(R.id.btn_add);
		btn_order = (Button) findViewById(R.id.btn_order);
		mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
		btn_shopping_car.setOnClickListener(this);
		btn_checkout.setOnClickListener(this);
		btn_add.setOnClickListener(this);
		btn_order.setOnClickListener(this);
	}

	private void initData() {
		scanRecordDao = new ScanRecordDao();
	}

	public void updataProducts(String commodity_id, String storeNo,
			String goodsNo) {
		goodsId = commodity_id;
		storeId = storeNo;
		this.goodsNo = goodsNo;
		goodsDetailsHttp();
	}

	/**
	 * 获取商品数据
	 * <p/>
	 * *
	 */
	public void goodsDetailsHttp() {
		RequstClient.goodsDetailsHttpNew(AppContext.userId, goodsId, storeId,
                goodsNo, new CustomResponseHandler(this, true) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          String content) {
                        if (isActivityDestory) {
                            return;
                        }
                        super.onSuccess(statusCode, headers, content);
                        try {
                            JSONObject obj = new JSONObject(content);
                            String result = obj.getString("type");
                            if ("1".equals(result)) {
                                // 请求成功
                                gdbbean = new Gson().fromJson(content,
                                        ProductsDetailBeen.class);
                                //savaScanRecord();
                                setView();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        obj.getString("msg"), Toast.LENGTH_LONG)
                                        .show();
                                ProductsDetailActivity.this.finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
	}

	/**
	 * 填充数据
	 */
	private void setView() {
		int count = 0;
		count = MathUtil.stringToInt(gdbbean.goodsDetail.SHOPPING_CAR_NUM);
		setShoppingCarNum(count);
        if (isUpdated&&gdbbean!=null){
            if (basicInformationFragment!=null){
                basicInformationFragment.goodsDetailBeen=gdbbean;
                basicInformationFragment.setView();
            }
            return;
        }
		FragmentManager mFragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = mFragmentManager
				.beginTransaction();
		basicInformationFragment = BasicInformationFragment.getInstance(
				gdbbean, subscribeId);
		fragmentTransaction.add(R.id.fragment_products,
				basicInformationFragment);
		fragmentTransaction.commit();
		basicInformationFragment
				.setOnFragmentTouchListener(new BasicInformationFragment.OnFragmentTouchListener() {
					@Override
					public void changeToWebView() {
						if (goodsDetailWebViewFragment == null) {

							goodsDetailWebViewFragment = GoodsDetailWebViewFragment
									.getInstance(goodsId);

							// 图文详情, 包装规格,售后服务(下面数据放到fragment里面去独立请求) add by
							// liuyuanqi 20150710
							// String infoContentMap =
							// gdbbean.goodsDetail.getInfo_content_map();
							// String packingContentMap =
							// gdbbean.goodsDetail.getPackinglist_content_map();
							// String afterServiceMap =
							// gdbbean.goodsDetail.getAfterservice_content_map();
							// goodsDetailWebViewFragment.setWebview(infoContentMap,
							// packingContentMap, afterServiceMap);

							goodsDetailWebViewFragment
									.setOnFragmentTouchListener(new GoodsDetailWebViewFragment.OnFragmentTouchListener() {
										@Override
										public void changeToGoodsDetail() {
											mViewSwitcher
													.setInAnimation(AnimationUtils
                                                            .loadAnimation(
                                                                    mContext,
                                                                    R.anim.push_up_in));
											mViewSwitcher
													.setOutAnimation(AnimationUtils
                                                            .loadAnimation(
                                                                    mContext,
                                                                    R.anim.push_down_out));
											mViewSwitcher.showPrevious();
										}
									});
						}
						if (goodsDetailWebViewFragment.isAdded()) {
							mViewSwitcher.setInAnimation(AnimationUtils
									.loadAnimation(mContext,
											R.anim.push_down_in));
							mViewSwitcher.setOutAnimation(AnimationUtils
									.loadAnimation(mContext, R.anim.push_up_out));
							mViewSwitcher.showNext();
						} else {
							getSupportFragmentManager()
									.beginTransaction()
									.add(R.id.fragment_webview,
											goodsDetailWebViewFragment)
									.commit();
						}
					}
				});
	}

	/**
	 * 改变购物车右上角的提示信息
	 * 
	 * @param count
	 */
	public void setShoppingCarNum(int count) {
		if (count <= 0) {
			tv_shopping_car_count.setVisibility(View.GONE);
		} else {
			if (count > 99) {
				tv_shopping_car_count.setText("99+");
			} else {
				tv_shopping_car_count.setText(count + "");
			}
			tv_shopping_car_count.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View view) {
		if (view == btn_add) {
			if (StringUtils.isBlank(AppContext.userId)) {
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				this.startActivity(intent);
				return;
			}
			// 加入购物车
			// ((ProductsDetailActivity) this).showMenu(0);
			showMenuType = 0;
			addShoppingCar(basicInformationFragment.numAfterParams, "-1", "-1");
		} else if (view == btn_checkout) {// 马上结算
			if (StringUtils.isBlank(AppContext.userId)) {
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(this, LoginActivity.class);
				intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
				this.startActivity(intent);
				return;
			}
			if (null != basicInformationFragment) {
				buyNow(basicInformationFragment.numAfterParams);
			}
		} else if (view == btn_shopping_car) {
			// 购物车
			AppContext.MAIN_TASK = AppContext.SHOPCAR;
			Intent i_main = new Intent(this, MainActivity.class);
			i_main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i_main);
			this.finish();
		} else if (view == btn_order) {
			// 活动按钮
			buyActivityGoods();
		}
	}

	private void buyActivityGoods() {
		if (StringUtils.isBlank(AppContext.userId)) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
			this.startActivity(intent);
			return;
		}
        //0不存在任何活动,1新品预约，2团购，3秒杀，4单品，5闪购
        switch (gdbbean.goodsDetail.getFLAG_ACTIVITY()){
            case 2:
            case 3:
            case 5:
                if (null != basicInformationFragment) {
                    buyNow(basicInformationFragment.numAfterParams);
                }
                break;
            case 1:
                //新品预约
                BespeakRecord();
                break;
            //按正常的立即购买流程
            case 0:break;
            case 4:break;

        }
	}

	/**
	 * 转换活动type为提交的shoppingType
	 * 
	 * @return shoppingType //3、普通商品 5、团购 6、秒杀 7、分销 8、套餐 9、闪购 10、礼品兑换 11、天天低价
	 */
	private String parseSubmitType() {
		// FLAG_ACTIVITY:0不存在任何活动,1新品预约，2团购，3秒杀，4单品，5闪购（倒计时/0,4无倒计时、折扣价/0不存在折扣价）
		switch (gdbbean.goodsDetail.getFLAG_ACTIVITY()) {
		case 0:
			return "3";
		case 2:
			return "5";
		case 3:
			return "6";
		case 4:
			return "3";
		case 5:
			return "9";
		}
		return "3";
	}

	/**
	 * 立即结算提交
	 * 
	 * @param num
	 *            已选商品数量数量
	 */
	public void buyNow(int num) {
		if (StringUtils.isBlank(AppContext.userId)) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
			this.startActivity(intent);
			return;
		}
		if (!checkTheNum(num)) {
			return;
		}
		UIHelper.showLoadDialog(this, this.getString(R.string.loading));

		final String submitType = parseSubmitType();
		RequstClient.cartPromptlyInit(AppContext.userId, submitType, num,
				gdbbean.goodsDetail.ID, gdbbean.goodsDetail.GOODS_NO,
				new CustomResponseHandler(mContext) {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							String content) {
						super.onSuccess(statusCode, headers, content);
						UIHelper.cloesLoadDialog();
						LogUtil.d(TAG, "buynow数据==" + content);
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
							Intent mIntent = new Intent(context,
									FillOrderActivity.class);
							mIntent.putExtra("fill_order_bean", mFillOrderBean);
							mIntent.putExtra("type",
									Integer.valueOf(submitType));
							if (submitType.equals("6")) {
								// 秒杀
								startActivityForResult(mIntent, MIAOSHA_CODE);
							} else {
								startActivity(mIntent);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case MIAOSHA_CODE:// 秒杀
			if (resultCode == RESULT_OK) {
				goodsDetailsHttp();
			}
			break;
		}
	}

	/**
	 * 提交预约信息
	 */
	private void BespeakRecord() {
		if (!basicInformationFragment.isActive) {
			Toast.makeText(context, "不在预约时间内，不可预约！", Toast.LENGTH_SHORT).show();
			return;
		}
		CustomResponseHandler handler = new CustomResponseHandler(
				ProductsDetailActivity.this, true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				if (bean.type == 1) {
					// Toast.makeText(getActivity(), "预约成功", Toast.LENGTH_SHORT)
					// .show();
					btn_order.setText("已预约");
					btn_order
							.setBackgroundResource(R.drawable.common_btn_gray2_selector);
					btn_order.setEnabled(false);
					showBespeakDialog();
				} else {
					Toast.makeText(context, bean.msg, Toast.LENGTH_SHORT)
							.show();
				}
			}

		};
		RequstClient.bespeakRecord(gdbbean.goodsDetail.getNEW_GOODS_ID(), handler);
	}

	private void showBespeakDialog() {
		SingleConfirmDialog dialog = new SingleConfirmDialog(context);
		dialog.setCustomTitle("预约成功");
		dialog.setMessage("恭喜您,预约成功!\n请在" + gdbbean.goodsDetail.END_TIME
				+ "后进行购买,我们将会以短信通知您。");
		dialog.setConfirm("确定");
		dialog.show();
	}

	/**
	 * 判断数量限制
	 *
	 * @param selectGoodNum
	 *            购买数量
	 * @return 数量限制是否通过
	 */
	private boolean checkTheNum(int selectGoodNum) {
		LogUtil.d("selectGoodNum", "selectGoodNum:" + selectGoodNum);
		if (gdbbean.goodsDetail.getGOODS_STATUS() == 2) {
			Toast.makeText(this, "已下架", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (gdbbean.goodsDetail.getGOODS_STOCK() <= 0) {
			Toast.makeText(this, "暂无库存", Toast.LENGTH_SHORT).show();
			return false;
		}

		int promotionType = gdbbean.goodsDetail.getFLAG_ACTIVITY();
		// 0不存在任何活动,1新品预约，2团购，3秒杀，4单品，5闪购（0/4不存在倒计时，0 1 不存在折扣价）
		int priominId = -1;
		if (promotionType > 1 && promotionType < 5) {
			if (gdbbean.goodsDetail.getQUOTA_ONE() == 0) {
				if (selectGoodNum != 1) {
					Toast.makeText(mContext, "该商品限购一件", Toast.LENGTH_SHORT)
							.show();
					return false;
				}
			} else if (gdbbean.goodsDetail.getQUOTA_ONE() == 1) {
				if (gdbbean.goodsDetail.getMOST_QUANTITY() > 0
						&& selectGoodNum > gdbbean.goodsDetail.getMOST_QUANTITY()) {
					Toast.makeText(
							mContext,
							"该商品最多购买" + gdbbean.goodsDetail.MOST_QUANTITY + "件",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				if (gdbbean.goodsDetail.getLEAST_QUANTITY() > 0
						&& selectGoodNum < gdbbean.goodsDetail.getLEAST_QUANTITY()) {
					Toast.makeText(
							mContext,
							"该商品最少购买" + gdbbean.goodsDetail.LEAST_QUANTITY
									+ "件", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		// 赠品的数量限制判断
		if (gdbbean.goodsDetail.getFLAG_GIFT() == 1) {
			for (int i = 0; i < gdbbean.goodsDetail.PROMOTION_GIFT.size(); i++) {
				if (gdbbean.goodsDetail.PROMOTION_GIFT.get(i).MOST_QUANTITY > 0
						&& selectGoodNum > gdbbean.goodsDetail.PROMOTION_GIFT
								.get(i).MOST_QUANTITY) {
					Toast.makeText(
							mContext,
							"赠品最多购买"
									+ gdbbean.goodsDetail.PROMOTION_GIFT.get(i).MOST_QUANTITY
									+ "件", Toast.LENGTH_SHORT).show();
					return false;
				}
			}
		}
		// 秒杀数量判断
		if (promotionType == 3 && gdbbean.goodsDetail.getFLAG_BUY() == 0) {
			Toast.makeText(mContext, "秒杀失败", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 加入购物车
	 * 
	 * @param selectGoodNum
	 *            购买数量
	 * @param promotionId
	 *            套餐id
	 * @param type
	 *            类型 -1、普通商品 5、团购 6、秒杀 7、分销 8、套餐 9、闪购 10、礼品兑换 11、天天低价
	 */
	public void addShoppingCar(int selectGoodNum, String promotionId,
			String type) {
		if (StringUtils.isBlank(AppContext.userId)) {
			Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(LoginActivity.TAG, LoginActivity.hkdetail_code);
			this.startActivity(intent);
			return;
		}
		if (!checkTheNum(selectGoodNum)) {
			return;
		}

		CustomResponseHandler handler = new CustomResponseHandler(this, true) {

			@Override
			public void onRefreshData(String content) {
				super.onRefreshData(content);
				Gson gson = new Gson();
				BaseBean bean = gson.fromJson(content, BaseBean.class);
				if (bean != null && bean.type > 0) {
					int num = 0;
					try {
						JSONObject object = new JSONObject(content);
						num = object.getInt("SHOPPING_CAR_NUM");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					setShoppingCarNum(num);
					if (basicInformationFragment != null) {
						basicInformationFragment.dismissAllDiscountPop();
					}
					Toast.makeText(mContext, "成功加入购物车", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(context, "加入购物车失败", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}
		};
		String userId = AppContext.userId;
		RequstClient.addShoppingCart(handler, gdbbean.goodsDetail.GOODS_NO,
				gdbbean.goodsDetail.ID, userId, Utils.anInt(promotionId),
				selectGoodNum, Utils.anInt(type));
	}

	/**
	 * 保存该商品到本地的浏览记录
	 */
	private void savaScanRecord() {
		BrowseItem record = new BrowseItem();
		record.COMMODITY_ID = gdbbean.goodsDetail.ID;// 商品id
		record.PRICE = gdbbean.goodsDetail.GOODS_PRICE;// 乐虎价格
		record.COMMODITY_NAME = gdbbean.goodsDetail.GOODS_NAME;// 商品名称
		String imageUrl = gdbbean.goodsDetail.GOODS_IMG_LIST;// 产品图片
		String url[] = null;
		if (imageUrl != null) {
			url = imageUrl.split(",");
		}
		if (url != null && url.length > 0) {
			record.COMMODITY_IMAGE_PATH = url[0];
		} else {
			record.COMMODITY_IMAGE_PATH = "";
		}
		List<BrowseItem> lists = scanRecordDao.fetcheAll();

		if (lists.size() >= 30) {
			BrowseItem db = scanRecordDao.fetcheById(record.COMMODITY_ID);
			if (db != null) {
				scanRecordDao.delete(db);
			} else {
				scanRecordDao.delete(lists.get(lists.size() - 1));
			}
		} else {
			BrowseItem db = scanRecordDao.fetcheById(record.COMMODITY_ID);
			if (db != null) {
				scanRecordDao.delete(db);
			}
		}
		scanRecordDao.add(record);
	}

	@Override
	public void finish() {
		// 用于详情页收藏处理 考虑用刷新处理
		/*
		 * if (listPosition != -1 && basicInformationFragment != null) { Intent
		 * i = new Intent(); i.putExtra("positon", listPosition);
		 * i.putExtra("isCollect",
		 * basicInformationFragment.getCollection_checkbox());
		 * setResult(RESULT_OK, i); }
		 */
		super.finish();
	}

	boolean isActivityDestory = false;

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isActivityDestory = true;
		// YtShare.createInstance(this).destroy();
	}

}
