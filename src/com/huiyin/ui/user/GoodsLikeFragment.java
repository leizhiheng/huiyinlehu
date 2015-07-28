package com.huiyin.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.huiyin.R;
import com.huiyin.adapter.GoodLikeAdapter;
import com.huiyin.bean.GoodLikeBean;
import com.huiyin.bean.GoodLikeBeanList;
import com.huiyin.ui.classic.ProductsDetailActivity;

import java.util.List;

/**
 * 猜你喜欢Fragment
 * @author 刘远祺
 *
 */
public class GoodsLikeFragment extends Fragment implements OnItemClickListener {

	private static final String GOODLIKELIST = "goodLikeList";
	
	/**猜喜欢商品**/
	private List<GoodLikeBean> goodLikeList;
	
	private GridView mGridView;
	
	private GoodLikeAdapter adapter;

	public static GoodsLikeFragment getInstance(GoodLikeBeanList dataList){
		GoodsLikeFragment fragment = new GoodsLikeFragment();
        Bundle args = new Bundle();
        args.putSerializable(GOODLIKELIST, dataList);
        fragment.setArguments(args);
        return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle data = getArguments();
		if(null != data){
			GoodLikeBeanList list =  (GoodLikeBeanList)data.getSerializable(GOODLIKELIST);
			goodLikeList = list.dataList;
		}
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_lehu_browse_record, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mGridView = (GridView) view.findViewById(R.id.mBrowseRecordGridView);
		if (goodLikeList != null) {
			setView();
		}
	}

	private void setView() {
		adapter = new GoodLikeAdapter(getActivity(), goodLikeList);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		GoodLikeBean bean = goodLikeList.get(position);

		Intent intent = new Intent(getActivity(), ProductsDetailActivity.class);
		intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, bean.GOODS_ID);
		getActivity().startActivity(intent);

	}
}
