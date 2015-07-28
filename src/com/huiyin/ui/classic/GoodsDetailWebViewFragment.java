package com.huiyin.ui.classic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huiyin.R;
import com.huiyin.api.CustomResponseHandler;
import com.huiyin.api.RequstClient;
import com.huiyin.base.BaseFragment;
import com.huiyin.bean.BaseBean;
import com.huiyin.bean.GoodsContent;
import com.huiyin.utils.StringUtils;
import com.huiyin.utils.WebViewUtil;

/**
 * Created by lian on 2015/5/16.
 */
public class GoodsDetailWebViewFragment extends BaseFragment implements View.OnClickListener {

	/**商品ID**/
	public static final String GOODSID = "goodsId";
	
	private WebView webview;
	private TextView btn_twxq;// 图文详情
	private TextView btn_bzgg;// 包装规格
	private TextView btn_shfw;// 售后服务

	private String dataHtmlTwxq;
	private String dataHtmlBzgg;
	private String dataHtmlShfw;
	private float scrollY;

	/**商品ID**/
	private String goodsId;
	
	
	public static GoodsDetailWebViewFragment getInstance(String goodsId){
		GoodsDetailWebViewFragment fragment = new GoodsDetailWebViewFragment();
		Bundle data = new Bundle();
		data.putString(GOODSID, goodsId);
		fragment.setArguments(data);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		if(null != data){
			this.goodsId = data.getString(GOODSID);
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_goods_detail_webview, container, false);

		initViews(view);

		if (dataHtmlTwxq != null && !dataHtmlTwxq.equals("")) {
			loadWebData();
		} else {
			dataHtmlTwxq = "暂无信息";
			loadWebData();
		}
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		//加载商品信息
		goodsContent(goodsId);
	}

	/**
	 * 请求商品详情数据(商品信息,包装参数,售后服务)
	 */
	private void goodsContent(String goodsId) {
		
        RequstClient.goodsContent(goodsId, new CustomResponseHandler(mContext, true) {

            @Override
            public void onRefreshData(String content) {
                super.onRefreshData(content);
                if(isSuccessResponse(content)){
                	GoodsContent goodsContent = new GoodsContent(content);
                	showData(goodsContent);
                }
            }
        });
    }
	
	/**
	 * 显示商品信息 
	 * @param goodsContent
	 */
	private void showData(GoodsContent goodsContent){
		if(null != goodsContent){
			
			// 商品信息 包装参数 售后服务
			setWebview(goodsContent.getInfo_content_map(), goodsContent.gePackinglist_content_map(), goodsContent.getAfterservice_content_map());
		}
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews(View view) {
		webview = (WebView) view.findViewById(R.id.webview);
		btn_twxq = (TextView) view.findViewById(R.id.btn_twxq);
		btn_bzgg = (TextView) view.findViewById(R.id.btn_bzgg);
		btn_shfw = (TextView) view.findViewById(R.id.btn_shfw);
		btn_twxq.setTextColor(getResources().getColor(R.color.red));
		btn_twxq.setOnClickListener(this);
		btn_bzgg.setOnClickListener(this);
		btn_shfw.setOnClickListener(this);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		webview.setBackgroundResource(R.color.transparent); // 设置背景色
		webview.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		webview.setVisibility(View.INVISIBLE);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 结束
				super.onPageFinished(view, url);
				webview.setVisibility(View.VISIBLE);
			}

		});
		webview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					if (webview != null && webview.getScrollY() == 0) {
						if (null != listener) {
							float y = event.getY();
							if (y > scrollY) {
								listener.changeToGoodsDetail();// 跳转到商品详情
								return true;
							}
						}
					}
					break;
				case MotionEvent.ACTION_DOWN:
					scrollY = event.getY();
					break;
				}
				return false;
			}
		});
	}

	/**
	 * 设置HTML内容
	 * 
	 * @param dataHtmlTwxq
	 * @param dataHtmlBzgg
	 * @param dataHtmlShfw
	 */
	public void setWebview(String dataHtmlTwxq, String dataHtmlBzgg, String dataHtmlShfw) {
		this.dataHtmlTwxq = dataHtmlTwxq;
		this.dataHtmlBzgg = dataHtmlBzgg;
		this.dataHtmlShfw = dataHtmlShfw;
		
		if (StringUtils.isBlank(dataHtmlTwxq)) {
			dataHtmlTwxq = "没有搜索到相关信息";
		}
		
		if (StringUtils.isBlank(dataHtmlBzgg)) {
			dataHtmlBzgg = "没有搜索到相关信息";
		}
		
		if (StringUtils.isBlank(dataHtmlShfw)) {
			dataHtmlShfw = "没有搜索到相关信息";
		}
		
		if (webview != null) {
			webview.loadDataWithBaseURL(null, WebViewUtil.initWebViewFor19(dataHtmlTwxq), "text/html", "utf-8", null);
		}
	}

	public void loadWebData() {
		if (webview != null) {
			webview.loadDataWithBaseURL(null, WebViewUtil.initWebViewFor19(dataHtmlTwxq), "text/html", "utf-8", null);
		}
	}

	@Override
	public void onClick(View v) {
		btn_twxq.setTextColor(getResources().getColor(R.color.light_gray));
		btn_bzgg.setTextColor(getResources().getColor(R.color.light_gray));
		btn_shfw.setTextColor(getResources().getColor(R.color.light_gray));
		switch (v.getId()) {
		case R.id.btn_twxq:// 图文详情
			if (webview != null) {
				btn_twxq.setTextColor(getResources().getColor(R.color.red));
				if (StringUtils.isBlank(dataHtmlTwxq)) {
					dataHtmlTwxq = "没有搜索到相关信息";
				}
				webview.loadDataWithBaseURL(null, WebViewUtil.initWebViewFor19(dataHtmlTwxq), "text/html", "utf-8", null);
			}
			break;
		case R.id.btn_bzgg:// 包装规格
			if (webview != null) {
				btn_bzgg.setTextColor(getResources().getColor(R.color.red));
				if (StringUtils.isBlank(dataHtmlBzgg)) {
					dataHtmlBzgg = "没有搜索到相关信息";
				}
				webview.loadDataWithBaseURL(null, WebViewUtil.initWebViewFor19(dataHtmlBzgg), "text/html", "utf-8", null);
			}
			break;
		case R.id.btn_shfw:// 售后服务
			if (webview != null) {
				btn_shfw.setTextColor(getResources().getColor(R.color.red));
				if (StringUtils.isBlank(dataHtmlShfw)) {
					dataHtmlShfw = "没有搜索到相关信息";
				}
				webview.loadDataWithBaseURL(null, WebViewUtil.initWebViewFor19(dataHtmlShfw), "text/html", "utf-8", null);
			}
			break;

		}
	}

	private OnFragmentTouchListener listener;

	public void setOnFragmentTouchListener(OnFragmentTouchListener listener) {
		this.listener = listener;
	}

	public interface OnFragmentTouchListener {
		void changeToGoodsDetail();
	}
}
