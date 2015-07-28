package com.huiyin.wight;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.bean.ShoppingCarGoodsBean;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ViewHolder;

import java.util.ArrayList;

/**
 * Created by justme on 15/5/27.
 */
public class ChoseGiftView extends Dialog {
    /**
     * 回调接口
     */
    private ChoseGiftCallback mCallback;

    private Context mContext;

    private ArrayList<ShoppingCarGoodsBean> mListData;

    private View mView;
    private TextView textView;
    private GridView mGridView;
    private TextView count;
    private Button confirm;
    private double price;

    private ChoseGiftAdapter mAdapter;

    private ShoppingCarGoodsBean mSelectData;

    private String mTitleName = "选择商品";
    private int fatherPosition;
    private int selectPosition;

    public ChoseGiftView(Context context, int theme) {
        super(context, R.style.drop_down_box_dialog_style);
        this.mContext = context;
        initView();
        price = 0;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void initView() {

        mView = LayoutInflater.from(mContext).inflate(R.layout.chose_gift_dialog_view, null);
        textView = (TextView) mView.findViewById(R.id.drop_down_boxs_dialog_title_tv);
        textView.setText(mTitleName);
        mGridView = (GridView) mView.findViewById(R.id.drop_down_boxs_dialog_lv);
        mListData = new ArrayList<ShoppingCarGoodsBean>();
        mAdapter = new ChoseGiftAdapter(mContext);
        mGridView.setAdapter(mAdapter);
        count = (TextView) mView.findViewById(R.id.count_textview);
        count.setText("已选择0件商品");
        confirm = (Button) mView.findViewById(R.id.confirm);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!mListData.get(position).isSelect()) {
                    if (selectPosition != -1)
                        mListData.get(selectPosition).setSelect(false);
                    selectPosition = position;
                    mListData.get(position).setSelect(true);
                    mSelectData = mListData.get(position);
                    count.setText("已选择1件商品");
                } else {
                    selectPosition = -1;
                    mListData.get(position).setSelect(false);
                    mSelectData = null;
                    count.setText("已选择0件商品");
                }
                mAdapter.notifyDataSetChanged();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (mSelectData == null) {
//                    Toast.makeText(mContext, "请选择一件商品", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                dismiss();
                if (mCallback != null) {
                    mCallback.onSelectItem(fatherPosition, selectPosition, mSelectData);
                }
            }
        });
        setContentView(mView);
        Window dialogWindow = getWindow();
//        dialogWindow.setWindowAnimations(R.style.dialogWindowAnimZoom);
        dialogWindow.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        dialogWindow.setWindowAnimations(R.style.PopupAnimation);  //添加动画

        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.6); // 高度设置为屏幕的0.6
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 1); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(lp);
    }

    public void setData(ArrayList<ShoppingCarGoodsBean> list, int fatherPosition) {
        this.fatherPosition = fatherPosition;
        if (list == null) {
            mListData = new ArrayList<ShoppingCarGoodsBean>();
        } else {
            mListData = list;
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 重置View的所有数据
     */
    public void resetView() {
        mListData = new ArrayList<ShoppingCarGoodsBean>();
        mSelectData = null;
    }

    public ShoppingCarGoodsBean getData() {
        return mSelectData;
    }

    /**
     * 获取选中 id
     *
     * @return id
     */
    public int getSelectVar() {
        if (mSelectData == null) {
            return -1;
        }
        return mSelectData.getGoodsId();
    }

    public String getText() {
        if (mSelectData == null) {
            return "";
        }
        return mSelectData.getGoodsName();
    }

    /**
     * 设置选中项
     *
     * @param id 选中项目id
     */
    public void setSelectItemById(int id) {
        if (mListData == null || mListData.size() == 0) {
            return;
        }
        int tempid = -1;
        for (ShoppingCarGoodsBean item : mListData) {
            tempid = item.getGoodsId();
            if (tempid == id) {
                mSelectData = item;
            }
        }
    }

    public void setChoseGiftCallback(ChoseGiftCallback callback) {
        mCallback = callback;
    }

    public interface ChoseGiftCallback {
        public void onSelectItem(int fatherPosition, int position, ShoppingCarGoodsBean object);
    }

    class ChoseGiftAdapter extends BaseAdapter {

        private Context mContext;

        public ChoseGiftAdapter(Context context) {
            this.mContext = context;
        }

        public int getId(int arg0) {
            return mListData.get(arg0).getGoodsId();
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ShoppingCarGoodsBean data = mListData.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.chose_gift_dialog_grid_item, parent, false);
            }
            ImageView goodsImage = ViewHolder.get(convertView, R.id.goods_imageview);
            TextView goods_name = ViewHolder.get(convertView, R.id.goods_name);
            TextView goods_price = ViewHolder.get(convertView, R.id.goods_price);
            CheckBox check = ViewHolder.get(convertView, R.id.checkbox);

            goods_name.setText(data.getGoodsName());
            goods_price.setText(MathUtil.priceForAppWithSign(price));
            if (data.isSelect()) {
                check.setChecked(true);
                mSelectData = data;
                selectPosition = position;
                count.setText("已选择1件商品");
            } else {
                check.setChecked(false);
            }
            ImageManager.LoadWithServer(data.getGoodsImg(), goodsImage);

            return convertView;
        }
    }
}
