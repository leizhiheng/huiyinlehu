package com.huiyin.wight;

import android.app.Dialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.utils.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by justme on 15/5/27.
 */
public class DropDownBoxsView extends LinearLayout {
    /**
     * 回调接口
     */
    private DropDownBoxsCallback mCallback;

    private Context mContext;

    private ArrayList<HashMap<String, Object>> mListData;

    private View mView;
    private TextView textView;

    private Dialog mDialog;
    private ListView mListView;
    private DropDownBoxsAdapter mAdapter;

    private HashMap<String, Object> mSelectData;

    private String mTitleName = "标题";
    /**
     * 控件hin 提示内容
     */
    private String mHin = "--请选择--";
    /**
     * 无数据时，点击控件时的提示
     */
    private String mTipContent = "无数据";

    public DropDownBoxsView(Context context) {
        super(context);
        this.mContext = context;
        initialize();
    }

    public DropDownBoxsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setCustomAttributes(attrs);
        initialize();
    }

    private void setCustomAttributes(AttributeSet attrs) {
        mTitleName = attrs.getAttributeValue(null, "titleName");
        mHin = attrs.getAttributeValue(null, "texthin");
    }

    private void initialize() {
        mView = LayoutInflater.from(mContext).inflate(R.layout.drop_down_boxs_view, this, true);

        textView = (TextView) mView.findViewById(R.id.drop_down_tv);
        textView.setHint(mHin);
        textView.setOnClickListener(itemClickListener);

    }

    private OnClickListener itemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mListData == null || mListData.size() == 0) {

            } else {
                showdDialog();
            }
        }
    };

    public void setData(ArrayList<HashMap<String, Object>> list) {
        if (list == null) {
            mListData = new ArrayList<HashMap<String, Object>>();
        } else {
            mListData = list;
        }
    }

    /**
     * 重置View的所有数据
     */
    public void resetView() {
        mListData = new ArrayList<HashMap<String, Object>>();
        mSelectData = null;
        mDialog = null;
        textView.setText("");
    }

    private void showdDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext, R.style.drop_down_box_dialog_style);
            View view = LayoutInflater.from(mContext).inflate(R.layout.drop_down_boxss_dialog_view, null);

            TextView drop_down_boxs_dialog_title_tv = (TextView) view.findViewById(R.id.drop_down_boxs_dialog_title_tv);
            drop_down_boxs_dialog_title_tv.setText(mTitleName);

            mListView = (ListView) view.findViewById(R.id.drop_down_boxs_dialog_lv);
            mAdapter = new DropDownBoxsAdapter(mContext);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mSelectData = (HashMap<String, Object>) mAdapter.getItem(position);
                    textView.setText(mSelectData.get("name").toString());
                    if (mCallback != null) {
                        mCallback.onSelectItem(mSelectData);
                    }
                    mDialog.dismiss();
                }
            });
            mDialog.setContentView(view);
        }
        mAdapter.deleteItem();
        mAdapter.addItem(mListData);

        Window dialogWindow = mDialog.getWindow();
//        dialogWindow.setWindowAnimations(R.style.dialogWindowAnimZoom);
        dialogWindow.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        dialogWindow.setWindowAnimations(R.style.PopupAnimation);  //添加动画

        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.height = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.6); // 高度设置为屏幕的0.6
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 1); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(lp);

        mDialog.show();
    }

    class DropDownBoxsAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<HashMap<String, Object>> mList;

        public DropDownBoxsAdapter(Context context) {
            this.mContext = context;
            mList = new ArrayList<HashMap<String, Object>>();
        }

        public int getId(int arg0) {
            return (Integer) mList.get(arg0).get("id");
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HashMap<String, Object> data = mList.get(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.drop_down_boxs_dialog_list_item, parent, false);
            }
            TextView drop_down_boxs_dialog_item_tv = ViewHolder.get(convertView, R.id.drop_down_boxs_dialog_item_tv);
            drop_down_boxs_dialog_item_tv.setText(data.get("name").toString());
            return convertView;
        }

        public void deleteItem() {
            mList.clear();
        }

        public void addItem(ArrayList<HashMap<String, Object>> t) {
            if (t == null) {
                return;
            }
            if (t instanceof ArrayList) {
                mList.addAll(t);
            }
            notifyDataSetChanged();
        }
    }

    public HashMap<String, Object> getData() {
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
        return (Integer) mSelectData.get("id");
    }

    public String getText() {
        if (mSelectData == null) {
            return "";
        }
        return mSelectData.get("name").toString();
    }

    /**
     * 设置选中项
     *
     * @param id 选中项目id
     */
    public void setSelectItemById(long id) {
        if (mListData == null || mListData.size() == 0) {
            return;
        }
        int tempid = -1;
        for (HashMap<String, Object> item : mListData) {
            tempid = (Integer) item.get("id");
            if (tempid == id) {
                mSelectData = item;
                textView.setText(mSelectData.get("name").toString());
            }
        }
    }

    /**
     * 设置点击无数据的提示内容
     *
     * @param tip
     */
    public void setTipContent(String tip) {
        mTipContent = tip;
    }

    public void setDropDownBoxsCallback(DropDownBoxsCallback callback) {
        mCallback = callback;
    }

    public interface DropDownBoxsCallback {
        public void onSelectItem(Object object);
    }
}
