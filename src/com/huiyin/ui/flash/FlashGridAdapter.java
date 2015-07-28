package com.huiyin.ui.flash;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.ui.flash.FlashPrefectureBean.FrashGoods;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.MathUtil;
import com.huiyin.utils.ViewHolder;

import java.util.ArrayList;

public class FlashGridAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<FrashGoods> list;

	public FlashGridAdapter(Context mContext) {
		this.mContext = mContext;
		list = new ArrayList<FrashGoods>();
	}

	public FlashGridAdapter(Context mContext, ArrayList<FrashGoods> list) {
		this.mContext = mContext;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.flash_grid_item_layout, parent, false);
		}

		ImageView image = ViewHolder.get(convertView, R.id.zhuan_qu2_iv_img);
		TextView name = ViewHolder.get(convertView, R.id.zhuan_qu2_tv_title);
		TextView price = ViewHolder.get(convertView, R.id.zhuan_qu2_tv_price);
		TextView delPrice = ViewHolder.get(convertView, R.id.zhuan_qu2_tv_del_price);
		TextView count = ViewHolder.get(convertView, R.id.zhuan_qu2_tv_buy_num);

		FrashGoods temp = list.get(position);
		name.setText(temp.getCOMMODITYNAME());
		price.setText(Html.fromHtml("<font color=\"#484848\">闪购价：</font>" + MathUtil.priceForAppWithSign(temp.getLHPRICE())));
		SpannableString sp = null;
		sp = new SpannableString("乐虎价：" + MathUtil.priceForAppWithSign(temp.getPRICE()));
		sp.setSpan(new StrikethroughSpan(), 4,sp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		delPrice.setText(sp);
		// delPrice.setText("乐虎价：" +
		// MathUtil.priceForAppWithSign(temp.getLHPRICE()));
		// delPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		count.setText("已有" + temp.getJOINNUM() + "人参加");
		ImageManager.LoadWithServer(temp.getIMG(), image);

		return convertView;
	}

	public void deleteItem() {
		list.clear();
		notifyDataSetChanged();
	}

	public void addItem(ArrayList<FrashGoods> temp) {
		if (temp == null) {
			return;
		}
		if (temp instanceof ArrayList) {
			list.addAll(temp);
		}
		notifyDataSetChanged();
	}

	public int getID(int position) {
		return ((FrashGoods) getItem(position)).getPRODUCEID();
	}
}
