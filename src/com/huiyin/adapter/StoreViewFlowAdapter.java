package com.huiyin.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.huiyin.R;
import com.huiyin.ui.classic.ProductsDetailActivity;
import com.huiyin.ui.store.StoreBanner;
import com.huiyin.ui.web.MyWebActivity;
import com.huiyin.utils.ImageManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Mike on 2015/7/3.
 */
public class StoreViewFlowAdapter extends BaseAdapter {
    private final DisplayImageOptions options;
    private List<StoreBanner> mMapList;
    private Context mContext;

    public StoreViewFlowAdapter(Context context,List<StoreBanner> mapList) {
        mMapList = mapList;
        mContext = context;
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true)
                .showStubImage(R.drawable.image_default_gallery)
                .showImageForEmptyUri(R.drawable.image_default_gallery)
                .showImageOnFail(R.drawable.image_default_gallery)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(0)).build();
    }

    @Override
    public int getCount() {
        return mMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            convertView = imageView;
            viewHolder.imageView = (ImageView) convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final StoreBanner map=mMapList.get(position);
        //统一图片加载方式用于省流量处理
        ImageManager.LoadWithServer(map.getIMG(), viewHolder.imageView,options);
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealWithLink(map.getURL());
            }
        });
        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }

    /**
     * 处理
     * @param link
     */
    private void dealWithLink(String link){
        if (link.startsWith("http")||link.startsWith("https")){
            int pos=link.lastIndexOf("/");
            if (link.contains("?")){
                int end=link.indexOf("?");
                String requestMethod=link.substring(pos + 1, end);
                HashMap<String,String> mMap=new HashMap<String,String>();
                if (requestMethod.equals("wapGoods.do")){
                    String[] pramrs=link.substring(end+1).split("&");
                    for (int i = 0; i < pramrs.length; i++) {
                        String key=pramrs[i].split("=")[0];
                        String value=pramrs[i].split("=")[1];
                        mMap.put(key,value);
                    }
                    if (!jumpToProductDetail(mMap)){
                        jumpToWeb(link);
                    }
                }
            }else{
                jumpToWeb(link);
            }
        }
    }
    private boolean jumpToProductDetail(HashMap<String,String> mMap){
        if (mMap.get("goods_id")!=null&&mMap.get("store_id")!=null&&mMap.get("goods_no")!=null){
            Intent intent = new Intent(mContext, ProductsDetailActivity.class);
            intent.putExtra(ProductsDetailActivity.BUNDLE_KEY_GOODS_ID, mMap.get("goods_id"));
            intent.putExtra(ProductsDetailActivity.GOODS_NO, mMap.get("store_id"));
            intent.putExtra(ProductsDetailActivity.STORE_ID, mMap.get("goods_no"));
            mContext.startActivity(intent);
            return true;
        }else{
            return false;
        }
    }

    private void jumpToWeb(String url){
        Intent intent = new Intent(mContext, MyWebActivity.class);
        intent.putExtra(MyWebActivity.URL,url);
        mContext.startActivity(intent);
    }
}
