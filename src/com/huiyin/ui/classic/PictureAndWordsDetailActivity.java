package com.huiyin.ui.classic;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.huiyin.R;

/**
 * Created by lian on 2015/5/18.
 */
public class PictureAndWordsDetailActivity extends FragmentActivity implements View.OnClickListener{
    private GoodsDetailWebViewFragment fragment;
    
    /**商品ID**/
    public static final String GOODSID			="goodsId";
    
    
    public static final String PICTURE_WORDS	="info_content_map";
    
    
    public static final String PACKINGLIST		="packinglist_content_map";
    
    
    public static final String AFTERSERVICE		="afterservice_content_map";
    
    private TextView ab_back;//返回
    private TextView ab_title;//标题
    private String info_content_map;//图文
    private String packinglist_content_map;//规格
    private String afterservice_content_map;//售后
    
    /**商品ID**/
    private String goodsId;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_goods_detail_activity);
        
        goodsId=getIntent().getStringExtra(GOODSID);
        info_content_map=getIntent().getStringExtra(PICTURE_WORDS);
        packinglist_content_map=getIntent().getStringExtra(PACKINGLIST);
        afterservice_content_map=getIntent().getStringExtra(AFTERSERVICE);
        init();
    }

    private void init() {
        ab_back= (TextView) findViewById(R.id.ab_back);
        ab_title= (TextView) findViewById(R.id.ab_title);
        ab_title.setText("商品详情");
        ab_back.setOnClickListener(this);
        fragment = GoodsDetailWebViewFragment.getInstance(goodsId);
        fragment.setWebview(info_content_map,packinglist_content_map,afterservice_content_map);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_picture_words,fragment).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ab_back:
                this.finish();
                break;
        }
    }
}
