package com.huiyin.ui.menberclub;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;

public class FragmentAll extends Fragment {
	
	private ArrayList<String> testData = new ArrayList<String>();
	private Context mContext;
	private LayoutInflater mInflater;
	
	public FragmentAll(){
		
	}
	
	interface OnGridViewItemClickListener{
		void onItemClick(int id);
	}
	
	private static OnGridViewItemClickListener mListener;
	
	public void setOnGridViewItemClickListener(OnGridViewItemClickListener listener){
		this.mListener = listener;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(FragmentAll.this.getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_menber_club_all, null);
		GridView gridView = (GridView) view.findViewById(R.id.gridview_menber_club_all);
		gridView.setAdapter(new MyAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mListener != null){
					mListener.onItemClick(arg2);
				}
				startActivity(new Intent(FragmentAll.this.getActivity(),ExchangeDetailActivity.class));
			}
		});
		return view;
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view = arg1;
			ViewHolder holder = null;
			if(mInflater == null){
			}
			if(view == null){
				view = mInflater.inflate(R.layout.layout_menber_club_gridview_item, null);
				holder = new ViewHolder();
				holder.iv_big = (ImageView) view.findViewById(R.id.iv_big);
				holder.iv_small = (ImageView) view.findViewById(R.id.iv_small);
				holder.goods_name = (TextView) view.findViewById(R.id.goods_name);
				holder.jifen_num = (TextView) view.findViewById(R.id.jifen);
				holder.lehu_price = (TextView) view.findViewById(R.id.price_lehu);
				view.setTag(holder);
			}
			holder = (ViewHolder) view.getTag();
			return view;
		}
		
	}
	
	class ViewHolder {
		ImageView iv_big;
		ImageView iv_small;
		TextView goods_name;
		TextView jifen_num;
		TextView lehu_price;
	}
}
