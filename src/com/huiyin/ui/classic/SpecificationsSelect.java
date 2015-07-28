package com.huiyin.ui.classic;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.custom.vg.list.CustomAdapter;
import com.custom.vg.list.CustomListView;
import com.custom.vg.list.OnItemClickListener;
import com.custom.vg.list.OnItemLongClickListener;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.bean.DEFAULT_NORM;
import com.huiyin.bean.GOODS_NORM;
import com.huiyin.bean.GoodsDetailBeen;
import com.huiyin.bean.NORMS_VALUE;
import com.huiyin.bean.ProductsDetailBeen;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.PopupWindowUtils;
import com.huiyin.utils.Utils;

/**
 * 选择规格参数
 * Created by lian on 2015/5/14.
 */
public class SpecificationsSelect implements View.OnClickListener {
    private Context context;
    private final String TAG = "PositionSpecifications";
    private ImageView imageView_jiahao, imageView_jianhao;
    public TextView tv_yuhujiange_show_show, tv_name_shangping, tv_zxs, tv_kucun, xiangou_info;
    public EditText et_num;
    private View view;
    private ProductsDetailBeen gdbbean;
    private LinearLayout layout_spevalues;// 显示规格参数布局
    private ImageView goodsImg;// 商品图片
    private GoodsDetailBeen.SpecialValue1 specList;// 规格参对象
    private Button btn_buy_now;//立即购买
    private Button btn_add_shoppingcar;//加入购物车
    private int num;//商品数量
    private String GOODS_NO;
    public PopupWindowUtils popuUtils;
    private TextWatcher textWatcher;
    private boolean isActive;

    public SpecificationsSelect(Context context, View relyview, ProductsDetailBeen gdbbean,boolean isActive) {
        this.context = context;
        this.gdbbean = gdbbean;
        this.isActive = isActive;
        view = LayoutInflater.from(context).inflate(R.layout.layout_pop_select_params, null);
        popuUtils = new PopupWindowUtils(context, view, relyview);
    }

    public void init() {
        findView();
        setView();
        setListener();
    }

    // 刷新界面数据
    public void refreshUI(ProductsDetailBeen gdbbean,boolean isActive) {
        this.gdbbean = gdbbean;
        this.isActive=isActive;
        init();
    }

    public void setNum(int num){
        this.num=num;
    }

    public void findView() {
        tv_name_shangping = (TextView) view.findViewById(R.id.tv_name_shangping);// 商品名称
        xiangou_info = (TextView) view.findViewById(R.id.xiangou_info);
        imageView_jiahao = (ImageView) view.findViewById(R.id.imageView_jiahao);// 加号
        imageView_jianhao = (ImageView) view.findViewById(R.id.imageView_jianhao);// 减号
        et_num = (EditText) view.findViewById(R.id.et_num);// 商品数量
        btn_buy_now = (Button) view.findViewById(R.id.btn_buy_now);// 立即购买
        btn_add_shoppingcar = (Button) view.findViewById(R.id.btn_add_shoppingcar);// 加入购物车
        tv_zxs = (TextView) view.findViewById(R.id.tv_zxs);// 总销售
        tv_yuhujiange_show_show = (TextView) view.findViewById(R.id.tv_yuhujiange_show_show);// 商品价格
        tv_kucun = (TextView) view.findViewById(R.id.tv_kucun);// 商品库存
        goodsImg = (ImageView) view.findViewById(R.id.imageView_chanpin_ic);// 商品图片

        layout_spevalues = (LinearLayout) view.findViewById(R.id.layout_spevalues);// 显示规格参数布局
    }

    public void setView() {
        tv_name_shangping.setText(gdbbean.goodsDetail.GOODS_NAME);
//        tv_zxs.setText("已售出 " + gdbbean.goodsDetail.SALES_VOLUME + " 件");// 销量

        if (0 == gdbbean.goodsDetail.getFLAG_DISCOUNT()) {//不存在折扣
            tv_yuhujiange_show_show.setText(MathUtil.priceForAppWithSign(gdbbean.goodsDetail.GOODS_PRICE));
        } else if (1 == gdbbean.goodsDetail.getFLAG_DISCOUNT()) {//打折
            tv_yuhujiange_show_show.setText(MathUtil.priceForAppWithSign(gdbbean.goodsDetail.DISCOUNT_PRICE));
        }
        et_num.setText(num+"");
        setEditTextCursorLocation(et_num);
        // 根据条件查询库存
        /**
         * 拼接规格参数id
         */
        String str[] = null;
        if (gdbbean.goodsDetail.GOODS_IMG_LIST != null) {
            str = gdbbean.goodsDetail.GOODS_IMG_LIST.split(",");
        }
        if (str != null && str.length > 0) {
            ImageManager.LoadWithServer(gdbbean.goodsDetail.GOODS_IMG, goodsImg);
        }
        ArrayList<GOODS_NORM> params = gdbbean.goodsDetail.GOODS_NORMS_LIST;
        layout_spevalues.removeAllViews();
        if (null != params && 0 != params.size()) {
            for (int i = 0; i < params.size(); i++) {
                //将默认规格选中
                for (DEFAULT_NORM dvalues : gdbbean.goodsDetail.DEFAULT_NORMS) {
                    String valueId = dvalues.NORMS_VALUE_NAME;
                    ArrayList<NORMS_VALUE> NORMS_VALUE_LIST = params.get(i).NORMS_VALUE_LIST;
                    for (NORMS_VALUE value : NORMS_VALUE_LIST) {
                        if (null != valueId && valueId.equals(value.NORMS_VALUE_NAME)) {
                            value.isSelected = true;
                        }
                    }
                }
                View view = LayoutInflater.from(context).inflate(R.layout.params_item, null);
                TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                final CustomListView clv_values = (CustomListView) view.findViewById(R.id.clv_values);
                final ArrayList<NORMS_VALUE> data = params.get(i).NORMS_VALUE_LIST;
                final ParamsListNameAdapter adapterItem = new ParamsListNameAdapter(data);
                clv_values.setTag(i);
                clv_values.setDividerHeight(16);
                clv_values.setDividerWidth(12);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(20, 10, 20, 10);
                clv_values.setLayoutParams(layoutParams);
                clv_values.setAdapter(adapterItem);
                clv_values.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        for (NORMS_VALUE value : data) {
                            value.isSelected = false;
                        }
                        data.get(position).isSelected = !data.get(position).isSelected;
                        adapterItem.notifyDataSetChanged();
                        GOODS_NO = data.get(position).GOODS_NO;
                        if (null != listener) {
                            num=1;
                            et_num.setText(num+"");
                            setEditTextCursorLocation(et_num);
                            listener.selectResult(GOODS_NO, num);
                        }
                    }
                });
                clv_values.setOnItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // 不做任何处理
                        return true;
                    }
                });
                tv_name.setText(params.get(i).NORMS_NAME);
                layout_spevalues.addView(view);
            }
        }

    }

    public void setListener() {
        imageView_jianhao.setOnClickListener(this);
        imageView_jiahao.setOnClickListener(this);
        setBuyWithActivityType();
        textWatcher = new MyTextWatcher().invoke();
        et_num.addTextChangedListener(textWatcher);
    }

    public void dismiss(){
        if (popuUtils!=null&&popuUtils.getPopupWindow()!=null)
            popuUtils.getPopupWindow().dismiss();
    }

    /**
     *如果该商品活动开始或者结束,刷新底部购买按钮
     * @param active 开始或者结束
     */
    public void update(boolean active){
        isActive=active;
        setBuyWithActivityType();
    }
    private void setBuyWithActivityType() {
        if(3==gdbbean.goodsDetail.getFLAG_ACTIVITY()&&isActive){//秒杀
            btn_add_shoppingcar.setVisibility(View.GONE);
            btn_buy_now.setOnClickListener(this);
            if(0==gdbbean.goodsDetail.getFLAG_BUY()){//不可继续购买
                btn_buy_now.setText("已购买");
                btn_buy_now
                        .setBackgroundResource(R.drawable.common_btn_gray2_selector);
                btn_buy_now.setEnabled(false);

            }else{
                btn_buy_now.setText("马上秒杀");
                btn_buy_now.setEnabled(true);
                btn_buy_now.setBackgroundResource(R.drawable.common_btn_red_selector);
            }
        }else if (1==gdbbean.goodsDetail.getFLAG_ACTIVITY()&&isActive){//新品预约
            btn_add_shoppingcar.setVisibility(View.GONE);
            btn_buy_now.setOnClickListener(this);
            if (gdbbean.goodsDetail.FLAG_BESPEAK
                    .equals("1")) {
                btn_buy_now.setText("已预约");
                btn_buy_now
                        .setBackgroundResource(R.drawable.common_btn_gray2_selector);
                btn_buy_now.setEnabled(false);
            } else {
                btn_buy_now.setText("马上预约");
                btn_buy_now
                        .setBackgroundResource(R.drawable.common_btn_red_selector);
                btn_buy_now.setEnabled(true);
            }
        }else if (2==gdbbean.goodsDetail.getFLAG_ACTIVITY()&& isActive){//团购
            btn_add_shoppingcar.setVisibility(View.GONE);
            btn_buy_now.setOnClickListener(this);
            btn_buy_now.setText("马上团");
        }else if (5==gdbbean.goodsDetail.getFLAG_ACTIVITY() && isActive){//闪购
            btn_add_shoppingcar.setVisibility(View.GONE);
            btn_buy_now.setOnClickListener(this);
            btn_buy_now.setText("马上抢");
        }else{
            btn_buy_now.setVisibility(View.VISIBLE);
            btn_add_shoppingcar.setVisibility(View.VISIBLE);
            btn_buy_now.setEnabled(true);
            btn_buy_now.setText("立即结算");
            btn_add_shoppingcar.setText("加入购物车");
            btn_add_shoppingcar.setOnClickListener(this);
            btn_buy_now.setOnClickListener(this);
        }
    }

    private void setMyText(String init) {
        et_num.removeTextChangedListener(textWatcher);
        et_num.setText(init);
        et_num.addTextChangedListener(textWatcher);
    }

    // 将EditText的光标定位到字符的最后面
    public void setEditTextCursorLocation(EditText editText) {
        CharSequence text = editText.getText();
        if (text != null) {
            Spannable spanText = (Spannable) text;
            Selection.setSelection(spanText, text.length());
        }
    }

    private SelectResultListener listener;

    public void setOnSelectResultListener(SelectResultListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_jianhao:
                // 减号按钮
                String num1 = et_num.getText() + "";
                if (!"".equals(num1)) {
                    if (1 > Integer.parseInt(num1.trim())) {
                        et_num.setText("1");
                        return;
                    }
                    int i = Integer.parseInt(num1) - 1;
                    et_num.setText(i + "");
                }
                break;
            case R.id.imageView_jiahao:// 加号按钮
                String num2 = et_num.getText() + "";
                if (!"".equals(num2)) {
                    if (gdbbean.goodsDetail.getGOODS_STOCK() < Integer.parseInt(num2.trim())) {
                        et_num.setText(gdbbean.goodsDetail.GOODS_STOCK + "");
                        return;
                    }
                    int j = Integer.parseInt(num2) + 1;
                    et_num.setText(j + "");
                }
                break;
            case R.id.btn_add_shoppingcar:// 加入购物车
                if(listener != null) {
                    String num = et_num.getText().toString();
                    if(TextUtils.isEmpty(num)) {
                        return;
                    }
                    int goodsNum = MathUtil.stringToInt(num);
                    listener.addToShoppingCar(goodsNum);
                }
                break;
            case R.id.btn_buy_now:// 立即购买
                if(listener != null) {
                    String num = et_num.getText().toString();
                    if(TextUtils.isEmpty(num)) {
                        return;
                    }
                    int goodsNum = MathUtil.stringToInt(num);
                    listener.buyNow(goodsNum);
                }
                break;
        }
    }

    public interface SelectResultListener {
        void selectResult(String GOODS_NO, int selectGoodNum);
        void addToShoppingCar(int selectGoodNum);
        void buyNow(int selectGoodNum);
    }

    public class ParamsListNameAdapter extends CustomAdapter {
        private ArrayList<NORMS_VALUE> data;

        public ParamsListNameAdapter(ArrayList<NORMS_VALUE> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int position, View contentView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (null == contentView) {
                viewHolder = new ViewHolder();
                contentView = LayoutInflater.from(context).inflate(R.layout.layout_ex_child_item_cus, null);
                viewHolder.name = (TextView) contentView.findViewById(R.id.tv_attribute_name);
                contentView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) contentView.getTag();
            }
            viewHolder.name.setBackgroundResource(R.drawable.common_btn_white_selector);
            viewHolder.name.setTextColor(context.getResources().getColor(R.color.deep_gray));
            if (data.get(position).isSelected) {
                viewHolder.name.setBackgroundResource(R.drawable.common_btn_red_selector);
                viewHolder.name.setTextColor(context.getResources().getColor(R.color.white));
            }
            viewHolder.name.setText(data.get(position).NORMS_VALUE_NAME);
            return contentView;
        }
    }

    private static class ViewHolder {
        private TextView name;
    }

    private class MyTextWatcher {
        public TextWatcher invoke() {
            return new TextWatcher() {// 给编辑框加监听事件

                @Override
                public void onTextChanged(CharSequence text, int arg1, int arg2, int arg3) {
                    String point = et_num.getText() + "";
                    if (!"".equals(point)) {
                        if (gdbbean.goodsDetail.getGOODS_STOCK() < Integer.parseInt(point.trim())) {
                            UIHelper.showToast("不能超过商品的库存"
                                    + gdbbean.goodsDetail.GOODS_STOCK + "件");
                            setMyText(gdbbean.goodsDetail.GOODS_STOCK + "");
                        } else if (1 > Integer.parseInt(point.trim())) {
                            UIHelper.showToast("最少选择1件");
                            setMyText("1");
                        }
                    } else {
                        setMyText("1");
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence text, int arg1, int arg2, int arg3) {

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    setEditTextCursorLocation(et_num);
                    if (null != listener) {
                        String text = et_num.getText().toString();
                        num = Integer.parseInt(text);
                        listener.selectResult("", num);
                    }
                }
            };
        }
    }
}
