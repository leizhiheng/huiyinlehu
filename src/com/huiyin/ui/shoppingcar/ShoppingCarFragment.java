package com.huiyin.ui.shoppingcar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.adapter.DataChangeListener;
import com.huiyin.adapter.ShoppingCarListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.FillOrderBean;
import com.huiyin.bean.ShoppingCarDataBaseBeanNew;
import com.huiyin.bean.ShoppingCarGoodsBean;
import com.huiyin.bean.ShoppingCarPromotionBean;
import com.huiyin.bean.ShoppingCarPromotionItemBean;
import com.huiyin.dialog.ConfirmDialog;
import com.huiyin.dialog.ConfirmDialog.DialogClickListener;
import com.huiyin.ui.MainActivity;
import com.huiyin.ui.fillorder.FillOrderActivity;
import com.huiyin.ui.user.LoginActivity;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.MathUtil;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 购物车
 *
 * @author lixiaobin
 */
public class ShoppingCarFragment extends Fragment {

    public static final String TAG = "ShoppingCarFragment";

//    private ImageView btnDelete;// 删除按钮

    private CheckBox editBox;// 编辑按钮

    private Button btnLogin;// 登陆按钮

    // 登录布局
    private LinearLayout shop_top_login_layout;

    private ViewSwitcher mViewSwitch;

    private ShoppingCarDataBaseBeanNew data;

    private ShoppingCarListAdapter mAdapter;

    private String shopId = "";

    private Button btnCheckOut;// 结算

    private TextView shop_check_count;//总计

    private TextView shop_total_price_tv;// ,共计

    private CheckBox shop_check_all;// 全选框

    private View rootView;

    private Context mContext;
    /**
     * 第一次显示对话框，第二次以后不显示对话框*
     */
    private boolean showDialog = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.shopping_layout_new, null);
        }
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        findView();
        setListener();
        initData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG, "OnResume");

        if (null == AppContext.userId || AppContext.userId.equals("")) {
            // 如果无用户登录
            shop_top_login_layout.setVisibility(View.VISIBLE);
            mAdapter.deleteItem();
            mViewSwitch.setDisplayedChild(0);
            editBox.setVisibility(View.INVISIBLE);
        } else {
            shop_top_login_layout.setVisibility(View.GONE);
            getTheShopCarList();
        }

        if (shop_check_all != null) {
            shop_check_all.setTag(true);
//            shop_check_all.setChecked(false);
        }
    }

    private void findView() {
        btnCheckOut = (Button) rootView.findViewById(R.id.btnCheckOut);// 结算按钮
        btnCheckOut.setText(String.format(getString(R.string.settlement), 0));// 初始0

        mViewSwitch = (ViewSwitcher) rootView.findViewById(R.id.shopping_viewSwitcher);

        ListView mListview = (ListView) rootView.findViewById(R.id.m_listview);

//        btnDelete = (ImageView) rootView.findViewById(R.id.ab_delete);

        editBox = (CheckBox) rootView.findViewById(R.id.ab_edit);

        // 登录条
        shop_top_login_layout = (LinearLayout) rootView.findViewById(R.id.shop_top_login_layout);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);

        // 共计
        shop_check_count = (TextView) rootView.findViewById(R.id.shop_check_count);
        shop_total_price_tv = (TextView) rootView.findViewById(R.id.shop_total_price_tv);
        shop_total_price_tv.setText(MathUtil.priceForAppWithSign(0));

        // 全选
        shop_check_all = (CheckBox) rootView.findViewById(R.id.shop_check_all);

        if (AppContext.userId == null) {
            shop_top_login_layout.setVisibility(View.VISIBLE);
        } else {
            shop_top_login_layout.setVisibility(View.GONE);
        }

        mAdapter = new ShoppingCarListAdapter(mContext);
        mListview.setAdapter(mAdapter);
    }

    private void setListener() {
        btnCheckOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editBox.isChecked()) {
                    // 弹出对话框
                    if (checkShopId()) {
                        ConfirmDialog dialog = new ConfirmDialog(getActivity());
                        dialog.setCustomTitle("删除商品");
                        dialog.setMessage("您确定删除商品？");
                        dialog.setConfirm("确定");
                        dialog.setCancel("取消");
                        dialog.setClickListener(new DialogClickListener() {
                            @Override
                            public void onConfirmClickListener() {
                                deleteOrder();
                            }

                            @Override
                            public void onCancelClickListener() {

                            }
                        });
                        dialog.show();
                    }
                } else {
                    if (checkShopIdAndStock()) {
                        theGoodsCom();
                    }
                }
            }
        });
//        btnDelete.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 弹出对话框
//                if (checkShopId()) {
//                    ConfirmDialog dialog = new ConfirmDialog(getActivity());
//                    dialog.setCustomTitle("删除商品");
//                    dialog.setMessage("您确定删除商品？");
//                    dialog.setConfirm("确定");
//                    dialog.setCancel("取消");
//                    dialog.setClickListener(new DialogClickListener() {
//                        @Override
//                        public void onConfirmClickListener() {
//                            deleteOrder();
//                        }
//
//                        @Override
//                        public void onCancelClickListener() {
//
//                        }
//                    });
//                    dialog.show();
//                }
//            }
//        });
        editBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editBox.setText("完成");
                    btnCheckOut.setText("删除");
                    shop_check_count.setVisibility(View.INVISIBLE);
                    shop_total_price_tv.setVisibility(View.INVISIBLE);
                } else {
                    editBox.setText("编辑");
                    if (data != null && data.getCart() != null) {
                        btnCheckOut.setText(String.format(getString(R.string.settlement), data.getCart().getGoodsSum()));
                    }
                    shop_check_count.setVisibility(View.VISIBLE);
                    shop_total_price_tv.setVisibility(View.VISIBLE);
                    if (mAdapter != null && mAdapter.getCount() > 0) {
                        updateSelect();
                    }
                }
            }
        });
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });
        shop_check_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean isByCheckBoxSelf = (Boolean) shop_check_all.getTag();
                if (isByCheckBoxSelf) {
                    for (ShoppingCarGoodsBean item : mAdapter.getList()) {
                        if (item.getPromotionType() == 5) {
                            for (ShoppingCarGoodsBean goodsBean : item.getGoodsList()) {
                                goodsBean.setSelect(isChecked);
                            }
                        } else {
                            item.setSelect(isChecked);
                        }
                    }
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    if (!editBox.isChecked()) {
                        updateSelect();
                    }
                } else {
                    shop_check_all.setTag(true);
                }
            }
        });
        mAdapter.setListener(new DataChangeListener() {
            @Override
            public void onChange() {
                if (!editBox.isChecked()) {
                    updateSelect();
                } else {
                    boolean isAllCheck = true;
                    for (ShoppingCarGoodsBean item : mAdapter.getList()) {
                        if (item.getPromotionType() == 5) {
                            for (ShoppingCarGoodsBean goodsBean : item.getGoodsList()) {
                                if (!goodsBean.isSelect()) {
                                    isAllCheck = false;
                                    break;
                                }
                            }
                        } else {
                            if (!item.isSelect()) {
                                isAllCheck = false;
                                break;
                            }
                        }
                    }
                    if (isAllCheck ^ shop_check_all.isChecked()) {
                        shop_check_all.setTag(false);
                        shop_check_all.setChecked(isAllCheck);
                    }
                }
            }

            @Override
            public void onNumChange(final int position, final int beforeNum, final int afterNum) {
                ShoppingCarGoodsBean temp = mAdapter.getList().get(position);
                CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {

                    @Override
                    public void onRefreshData(String content) {
                        super.onRefreshData(content);
                        handlerData(content);
                        if (data == null || data.type != 1) {
                            mAdapter.getList().get(position).setNum(beforeNum);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                };
                RequstClient.updateShoppingCartNum(handler, String.valueOf(temp.getGoodsNo()),
                        temp.getGoodsId(), AppContext.userId, afterNum,
                        temp.getPromotionId(), temp.getShopCartType(), getSelectShopId());
            }

            @Override
            public void onNumChange(final int gruopPosition, final int position, final int beforeNum, int afterNum) {
                ShoppingCarGoodsBean temp = mAdapter.getList().get(gruopPosition).getGoodsList().get(position);
                CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {

                    @Override
                    public void onRefreshData(String content) {
                        super.onRefreshData(content);
                        handlerData(content);
                        if (data == null || data.type != 1) {
                            mAdapter.getList().get(gruopPosition).getGoodsList().get(position).setNum(beforeNum);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                };
                RequstClient.updateShoppingCartNum(handler, String.valueOf(temp.getGoodsNo()),
                        temp.getGoodsId(), AppContext.userId, afterNum,
                        temp.getPromotionId(), temp.getShopCartType(), getSelectShopId());
            }

            @Override
            public void onPromotionChange(int position, ShoppingCarPromotionItemBean promotionItemBean) {
                ShoppingCarGoodsBean temp = mAdapter.getList().get(position);
                CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {

                    @Override
                    public void onRefreshData(String content) {
                        super.onRefreshData(content);
                        handlerData(content);
                    }
                };
                RequstClient.updatePromotionType(handler, null, -1, promotionItemBean.getPromotionId(),
                        promotionItemBean.getPromotionType(), AppContext.userId,
                        temp.getShoppingId(), getSelectShopId());
            }

            @Override
            public void onPromotionChange(int gruopPosition, int position, ShoppingCarPromotionItemBean promotionItemBean) {
                ShoppingCarGoodsBean temp = mAdapter.getList().get(gruopPosition).getGoodsList().get(position);
                CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {

                    @Override
                    public void onRefreshData(String content) {
                        super.onRefreshData(content);
                        handlerData(content);
                    }
                };
                RequstClient.updatePromotionType(handler, null, -1, promotionItemBean.getPromotionId(),
                        promotionItemBean.getPromotionType(), AppContext.userId,
                        temp.getShoppingId(), getSelectShopId());
            }

            @Override
            public void onPromotionChange(int position, ShoppingCarPromotionBean promotionItemBean, ShoppingCarGoodsBean selectGiftBean) {
                ShoppingCarGoodsBean temp = mAdapter.getList().get(position);
                CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {

                    @Override
                    public void onRefreshData(String content) {
                        super.onRefreshData(content);
                        handlerData(content);
                    }
                };
                if (null == selectGiftBean) {
                    RequstClient.deleteCartPromotionGift(handler, temp.getPromotionId(),
                            AppContext.userId, getSelectShopId());
                } else {
                    RequstClient.updateShopppingCartPromotionGift(handler, String.valueOf(selectGiftBean.getGoodsNo()),
                            selectGiftBean.getGoodsId(), temp.getPromotionId(),
                            temp.getPromotionType(), AppContext.userId,
                            temp.getShoppingId(), getSelectShopId());
                }
            }
        });


    }

    /**
     * 初始化视图
     */
    private void initData() {
    }

    /**
     * 初始化查询购物车数据
     */
    private void getTheShopCarList() {
        CustomResponseHandler handler = new CustomResponseHandler(getActivity(), showDialog) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                handlerData(content);
            }
        };
        RequstClient.shoppingCatData(handler, AppContext.userId);
        showDialog = false;
    }

    /**
     * 购物车提交（进入填写订单界面）
     */
    private void theGoodsCom() {
        CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                LogUtil.d(TAG, "buynow数据==" + content);
                JSONObject obj;
                try {
                    obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getActivity(), errorMsg,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (obj.getString("baseInfo") == null || obj.getString("baseInfo").equals("null")
                            || obj.getString("baseInfo").equals("")) {
                        Toast.makeText(getActivity(), "返回的填写订单数据为空",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (obj.getString("cart") == null || obj.getString("cart").equals("null")
                            || obj.getString("cart").equals("")) {
                        Toast.makeText(getActivity(), "返回的购物数据为空",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    FillOrderBean mFillOrderBean = new Gson().fromJson(content,
                            FillOrderBean.class);
                    Intent mIntent = new Intent(getActivity(), FillOrderActivity.class);
                    mIntent.putExtra("fill_order_bean", mFillOrderBean);
                    startActivity(mIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequstClient.submintCart(handler, shopId, AppContext.userId);
    }

    /**
     * 生成shopId, 并检查shopId的值的可用性
     */
    private boolean checkShopId() {
        shopId = "";
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mAdapter.getList().get(i).getPromotionType() == 5) {
                for (int j = 0; j < mAdapter.getList().get(i).getGoodsList().size(); j++) {
                    if (mAdapter.getList().get(i).getGoodsList().get(j).isSelect()) {
                        shopId += mAdapter.getList().get(i).getGoodsList().get(j).getShoppingId() + ",";
                    }
                }
            } else {
                if (mAdapter.getList().get(i).isSelect()) {
                    shopId += mAdapter.getList().get(i).getShoppingId() + ",";
                }
            }
        }
        if (shopId.contains(",")) {
            shopId = shopId.substring(0, shopId.length() - 1);
            return true;
        } else if (shopId.equals("")) {
            Toast.makeText(getActivity(), "请选择商品！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private String getSelectShopId() {
        String shopId = "";
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mAdapter.getList().get(i).getPromotionType() == 5) {
                for (int j = 0; j < mAdapter.getList().get(i).getGoodsList().size(); j++) {
                    if (mAdapter.getList().get(i).getGoodsList().get(j).isSelect()) {
                        shopId += mAdapter.getList().get(i).getGoodsList().get(j).getShoppingId() + ",";
                    }
                }
            } else {
                if (mAdapter.getList().get(i).isSelect()) {
                    shopId += mAdapter.getList().get(i).getShoppingId() + ",";
                }
            }
        }
        if (shopId.contains(",")) {
            shopId = shopId.substring(0, shopId.length() - 1);
            return shopId;
        } else {
            return null;
        }
    }

    /**
     * 生成shopId, 并检查shopId的值的可用性以及库存
     */
    private boolean checkShopIdAndStock() {
        shopId = "";
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (mAdapter.getList().get(i).getPromotionType() == 5) {
                for (int j = 0; j < mAdapter.getList().get(i).getGoodsList().size(); j++) {
                    if (mAdapter.getList().get(i).getGoodsList().get(j).isSelect()) {
                        if (mAdapter.getList().get(i).getGoodsList().get(j).getGoodsStock() >= mAdapter.getList().get(i).getGoodsList().get(j).getNum()) {
                            shopId += mAdapter.getList().get(i).getGoodsList().get(j).getShoppingId() + ",";
                        } else {
                            Toast.makeText(getActivity(), "部分商品库存不足！", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    }
                }
            } else {
                if (mAdapter.getList().get(i).isSelect()) {
                    if (mAdapter.getList().get(i).getGoodsStock() >= mAdapter.getList().get(i).getNum()) {
                        shopId += mAdapter.getList().get(i).getShoppingId() + ",";
                    } else {
                        Toast.makeText(getActivity(), "部分商品库存不足！", Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
            }
        }
        if (shopId.contains(",")) {
            shopId = shopId.substring(0, shopId.length() - 1);
            return true;
        } else if (shopId.equals("")) {
            Toast.makeText(getActivity(), "请选择商品！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /*private void noGoodsDialog() {
        SingleConfirmDialog dialog = new SingleConfirmDialog(getActivity());
        dialog.setCustomTitle("提交失败");
        dialog.setMessage("商品已下架");
        dialog.setCancelable(false);
        dialog.setConfirm("重新选择");
        dialog.setClickListener(new SingleConfirmDialog.DialogClickListener() {
            @Override
            public void onConfirmClickListener() {
            }
        });
        dialog.show();
    }*/

    /**
     * 删除服务器内保存的订单
     */
    private void deleteOrder() {
        if (AppContext.userId == null) {
            return;
        }
        RequstClient.doDeleteOrder(shopId, AppContext.userId, new CustomResponseHandler(getActivity()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (!obj.getString("type").equals("1")) {
                        String errorMsg = obj.getString("msg");
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    } else {
                        getTheShopCarList();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 用于mainActivty通知当前Fragment被选中
     */
    public void pageSelected(int selectedId) {
        if (selectedId == MainActivity.SHOPPING_CAR_INDEX) {
            onResume();
        }
    }

    public void handlerData(String content) {
        data = ShoppingCarDataBaseBeanNew.explainJson(content, mContext);
        if (data != null && data.type > 0) {

            mAdapter.deleteItem();

            boolean isAllCheck = true;
            int goodsSum = 0;
            for (int i = 0; i < data.getCart().getCart().size(); i++) {
                for (ShoppingCarGoodsBean temp : data.getCart().getCart().get(i).getGoodsList()) {
                    if (temp.getPromotionType() == 5) {
                        if (temp.getGoodsList() != null && temp.getGoodsList().size() > 0) {
                            ShoppingCarGoodsBean promotionBean = temp.getGoodsList().get(0);
                            //当等于满赠时候 且第一个商品是赠品 移除第一个商品
                            if (promotionBean.isGift()) {
                                goodsSum += promotionBean.getNum();
                                temp.getGoodsList().remove(0);
                            }
                        }

                        for (ShoppingCarGoodsBean promotionBean : temp.getGoodsList()) {
                            if (!promotionBean.isSelect()) {
                                isAllCheck = false;
                            }
                            goodsSum += promotionBean.getNum();
                        }
                    } else {
                        if (!temp.isSelect()) {
                            isAllCheck = false;
                        }
                        goodsSum += temp.getNum();
                    }
                }
                mAdapter.addItem(data.getCart().getCart().get(i).getGoodsList(), i == data.getCart().getCart().size() - 1);
            }

            if (mAdapter.getCount() < 1) {
                editBox.setChecked(false);
                editBox.setVisibility(View.INVISIBLE);
                mViewSwitch.setDisplayedChild(0);
                ((MainActivity) getActivity()).setTheShoppcar(0);
            } else {
                mViewSwitch.setDisplayedChild(1);
                editBox.setVisibility(View.VISIBLE);

                if (isAllCheck ^ shop_check_all.isChecked()) {
                    shop_check_all.setTag(false);
                    shop_check_all.setChecked(isAllCheck);
                }

                if (!editBox.isChecked()) {
                    btnCheckOut.setText(String.format(getString(R.string.settlement), data.getCart().getGoodsSum()));// 结算按钮
                }
                shop_total_price_tv.setText(MathUtil.priceForAppWithSign(data.getCart().getOrderPrice()));// 总价格
                ((MainActivity) getActivity()).setTheShoppcar(goodsSum);
            }
        } else {
            mAdapter.deleteItem();
            mViewSwitch.setDisplayedChild(0);
            editBox.setChecked(false);
            editBox.setVisibility(View.INVISIBLE);
            ((MainActivity) getActivity()).setTheShoppcar(0);
        }
    }

    private void updateSelect() {
        CustomResponseHandler handler = new CustomResponseHandler(getActivity(), true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                handlerData(content);
            }
        };
        RequstClient.ajaxCart(handler, AppContext.userId, getSelectShopId());
    }
}