package com.huiyin.ui.classic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.UIHelper;
import com.huiyin.adapter.MyRecommendAdpter;
import com.huiyin.api.RequstClient;
import com.huiyin.bean.RqzhPopBean;
import com.huiyin.utils.MyCustomResponseHandler;
import com.huiyin.utils.PopupWindowUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择人气组合
 */
public class RqzhPopSelect {
	private final SelectResultListener listener;
	private final String goodsId;
	private Context context;
	private View contentview;
	private ListView mListView;
	private PopupWindowUtils popupWindowUtils;
	private MyRecommendAdpter mAdpter;
    private List<HashMap<String,String>> mMapList;
	private Handler mHandler;

	public RqzhPopSelect(Context context, View target, String goodsId,
			SelectResultListener listener) {
		this.context = context;
		this.listener = listener;
		this.goodsId = goodsId;
		contentview = LayoutInflater.from(context).inflate(
				R.layout.layout_pop_select_rqzh, null);
		popupWindowUtils = new PopupWindowUtils(context, contentview, target);
        loadData();
	}

	private void init() {
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (popupWindowUtils != null) {
					popupWindowUtils.getPopupWindow().dismiss();
				}
			}
		};
		mListView = (ListView) contentview.findViewById(R.id.pop_select_rqzh);
        mAdpter=new MyRecommendAdpter(context,mMapList);
        mListView.setAdapter(mAdpter);
        setListener();
    }

	private void setListener() {

		popupWindowUtils.getPopupWindow().setOnDismissListener(
				new PopupWindow.OnDismissListener() {
					@Override
					public void onDismiss() {
						WindowManager.LayoutParams lp = ((Activity) context)
								.getWindow().getAttributes();
						lp.alpha = 1; // 0.0-1.0
						((Activity) context).getWindow().setAttributes(lp);
						if (null != listener) {
							listener.selectResult(null);// 回掉接口
						}
					}
				});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listItemListener(position);
			}
		});

	}

	private void listItemListener(int position) {
        Intent intent = new Intent(context, ProductsDetailActivity.class);
        intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, mMapList.get(position).get("id"));
        context.startActivity(intent);
	}

	private void loadData() {
        RequstClient.popularGroup(goodsId,new MyCustomResponseHandler(context){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                RqzhPopBean mRqzhPopBean=new Gson().fromJson(content,RqzhPopBean.class);
                if (mRqzhPopBean.type!=1){
                    UIHelper.showToast(mRqzhPopBean.msg);
                }else{
                    if (mRqzhPopBean.goodsRecommend!=null){
                        mMapList=new ArrayList<HashMap<String, String>>();
                        HashMap<String ,String > item;
                        for (int i = 0; i < mRqzhPopBean.goodsRecommend.size(); i++) {
                            item=new HashMap<String, String>();
                            item.put("id", mRqzhPopBean.goodsRecommend.get(i).GOODS_ID+"");
                            item.put("name", mRqzhPopBean.goodsRecommend.get(i).GOODS_NAME);
                            item.put("price", mRqzhPopBean.goodsRecommend.get(i).GOODS_PRICE);
                            item.put("stock", mRqzhPopBean.goodsRecommend.get(i).NUM+"");
                            item.put("image", mRqzhPopBean.goodsRecommend.get(i).GOODS_IMG);
                            mMapList.add(item);
                        }
                        init();
                    }
                }
            }
        });
	}

	/**
	 * 延迟dismiss
	 */
	private void dismiss() {
		if (mHandler != null) {
			mHandler.sendEmptyMessageDelayed(0, 200);
		}
	}


	public interface SelectResultListener {
		void selectResult(HashMap<String, String> result);
	}
}
