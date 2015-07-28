package com.huiyin.ui.user.order;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huiyin.AppContext;
import com.huiyin.R;
import com.huiyin.UserInfo;
import com.huiyin.adapter.FragmentViewPagerAdapter;
import com.huiyin.adapter.LogisticsStatusListAdapter;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.GoodLikeBean;
import com.huiyin.bean.GoodLikeBeanList;
import com.huiyin.bean.LogisticsBean;
import com.huiyin.ui.user.GoodsLikeFragment;
import com.huiyin.utils.JSONParseUtils;
import com.huiyin.utils.LogUtil;
import com.huiyin.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

public class LogisticsFragment extends BaseFragment{
    private static final String ENTITY_PARAM = "entity";
    private LogisticsBean.OrderLogisticsEntity entity;
    private TextView tv_ps_company;
    private TextView tv_tel;
    private com.huiyin.wight.MyListView mListView;
    // ----------------也许喜欢
    private ViewPager mebyLikeViewPager;

    public static LogisticsFragment newInstance(LogisticsBean.OrderLogisticsEntity entity) {
        LogisticsFragment fragment = new LogisticsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ENTITY_PARAM, entity);
        fragment.setArguments(args);
        return fragment;
    }

    public LogisticsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (LogisticsBean.OrderLogisticsEntity) getArguments().getSerializable(ENTITY_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_logistics, container, false);
        bindViews(root);
        init();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
    	//加载猜你喜欢数据
    	loadMayBeLikeByUser();
    }
    
    /**
	 * 获取猜你喜欢商品
	 */
	private void loadMayBeLikeByUser(){
		if(appIsLogin()){
			
			//获取我的信息
			RequstClient.mayBeLikeByUser(new CustomResponseHandler(mContext, false) {
				@Override
				public void onSuccess(int statusCode, Header[] headers, String content) {
					super.onSuccess(statusCode, headers, content);
					if(!appIsLogin()){
						//用户已经注销，数据请求又回来了，这里不做处理，以解决用户注销失败的问题
						return;
					}
					
					//异常消息显示
					if(JSONParseUtils.isErrorJSONResult(content)){
						String msg = JSONParseUtils.getString(content, "msg");
						showMyToast(msg);
						return;
					}
					
					//解析猜你喜欢数据
					List<GoodLikeBean> maybeLike = JSONParseUtils.parseMaybeLike(content);//登录之后系统当前时间
					if(null != maybeLike && maybeLike.size() > 0){
						showLikeList(maybeLike);
					}
					
				}

			});
		}
	}
    
    
    private void init() {
        tv_ps_company.setText(entity.COMPANY_NAME);
        tv_tel.setText(entity.DELIVERY_CODE);
        //根据data字段是否有无数据判定是乐虎快递还是第三方快递
        if (entity.data == null) {
            //乐虎快递
            tv_tel.setText(entity.DELIVERY_CODE);
            tv_tel.setTextColor(getResources().getColor(R.color.blue_color));
            tv_tel.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            tv_tel.getPaint().setAntiAlias(true);//抗锯齿
            tv_tel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String telPhone = tv_tel.getText().toString();
                    String regEx = "[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(telPhone);
                    telPhone = m.replaceAll("").trim();
                    LogUtil.i("解析结果", "====" + telPhone);
                    if (StringUtils.isBlank(telPhone))
                        return;
                    Intent d_intent = new Intent(Intent.ACTION_DIAL,
                            Uri.parse("tel:" + telPhone));
                    d_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(d_intent);
                }
            });
        }else{
            //第三方快递
            tv_tel.setText("快递单号："+entity.DELIVERY_CODE);
        }
        LogisticsStatusListAdapter mAdapter = new LogisticsStatusListAdapter(getActivity(), getAdpterMap());
        mListView.setAdapter(mAdapter);
       
        //默认不显示猜你喜欢
        showLikeList(null);
        
        
        //解决子控件自动滚动的问题
        tv_ps_company.setFocusable(true);
        tv_ps_company.setFocusableInTouchMode(true);
        tv_ps_company.requestFocus();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

	private List<HashMap<String, String>> getAdpterMap() {
		List<HashMap<String, String>> mMaps = new ArrayList<HashMap<String, String>>();
		if (entity.data != null) {
			for (int i = 0; i < entity.data.size(); i++) {
				HashMap<String, String> temp = new HashMap<String, String>();
				temp.put("time", entity.data.get(i).ftime);
				temp.put("context", entity.data.get(i).context);
				mMaps.add(temp);
			}
			// return mMaps;
		} else {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("time", entity.ftime);
			temp.put("context", entity.context);
			mMaps.add(temp);
			// return mMaps;
		}

		// 显示下面倒数第二条
		if (null != entity.two) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("time", entity.two.time);
			temp.put("context", entity.two.desc);
			mMaps.add(temp);
		}

		// 显示下面倒数第一条
		if (null != entity.one) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("time", entity.one.time);
			temp.put("context", entity.one.desc);
			mMaps.add(temp);
		}

		// 显示第一条
		if (null != entity.three) {
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("time", entity.three.time);
			temp.put("context", entity.three.desc);
			mMaps.add(0, temp);
		}

		return mMaps;
	}

    private void bindViews(View root) {
        tv_ps_company = (TextView) root.findViewById(R.id.tv_ps_company);
        tv_tel = (TextView) root.findViewById(R.id.tv_tel);
        mListView = (com.huiyin.wight.MyListView) root.findViewById(R.id.lv_logistics);
        mebyLikeViewPager = (ViewPager)root.findViewById(R.id.mebyLikeViewPager);
    }
    /**
     * 显示猜你喜欢数据
     */
    private void showLikeList(List<GoodLikeBean> maybeLike){

        //没有数据
        if(null == maybeLike || 0 == maybeLike.size()){
            mebyLikeViewPager.setVisibility(View.GONE);
            return;
        }else{
            mebyLikeViewPager.setVisibility(View.VISIBLE);
        }


        List<GoodLikeBean> dataList = new ArrayList<GoodLikeBean>();
        List<Fragment> listFragments = new ArrayList<Fragment>();

        for (int i = 0; i < maybeLike.size(); i++) {
            int yu = i%6;
            if(((yu == 0) || i==(maybeLike.size()-1)) && (i!=0)){
                //6的倍数 || 是最后一个  && 不是第一个 （每六条数据显示一屏）
                GoodsLikeFragment fragment = GoodsLikeFragment.getInstance(new GoodLikeBeanList(dataList));
                listFragments.add(fragment);

                dataList = new ArrayList<GoodLikeBean>();
            }
            dataList.add(maybeLike.get(i));
        }

        FragmentViewPagerAdapter adapter = new FragmentViewPagerAdapter(getChildFragmentManager(), listFragments);
        mebyLikeViewPager.setAdapter(adapter);
    }
}
