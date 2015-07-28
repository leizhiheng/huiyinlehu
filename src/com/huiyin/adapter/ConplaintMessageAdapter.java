package com.huiyin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyin.R;
import com.huiyin.bean.MesaageList;
import com.huiyin.ui.classic.PhotoViewActivity;
import com.huiyin.utils.DeviceUtils;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.LogUtil;
import com.huiyin.wight.CircleImageView;

/**
 * 留言列表适配器
 * Created by kuangyong on 2015/6/10.
 */
public class ConplaintMessageAdapter extends BaseAdapter{
    private List<MesaageList> messageList;
    private Context context;

    public ConplaintMessageAdapter(List<MesaageList> messageList,Context context){
        this.messageList=messageList;
        this.context=context;
    }
    @Override
    public int getCount() {
        return null != messageList ? messageList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyHolder myHolder;
        if(null==convertView){
            convertView=LayoutInflater.from(context).inflate(R.layout.lv_item_complaint_message,null);
            myHolder=new MyHolder(convertView, (Activity)context);
            convertView.setTag(myHolder);
        }else{
            myHolder= (MyHolder) convertView.getTag();
        }

        LogUtil.i("CompaintDetail", "------------------------GetView Position : " + position + "  ---------------------");
        
        
       //控制显示隐藏
        myHolder. layoutfirst.setVisibility(View.GONE);
        myHolder. tvcontent.setVisibility(View.VISIBLE);
        myHolder.linehead.setVisibility(View.VISIBLE);//图片上面的竖线
        myHolder.linefoot.setVisibility(View.VISIBLE);//图片下面的竖线
        myHolder.lineend.setVisibility(View.VISIBLE);//最后分割线
        if(0==position){//第一个
            myHolder.linehead.setVisibility(View.INVISIBLE);
        }else if(messageList.size()==(position+1)){//最后一个
            myHolder.linefoot.setVisibility(View.GONE);
            myHolder.lineend.setVisibility(View.GONE);
        }

        /**
         * 设置头像、名称和时间
         */
        String messageType=messageList.get(position).getMESSAGE_TYPE();//1.客户留言2.平台管理员留言，3.商户留言
        String name="";//名称
        if("1".equals(messageType)){//.客户留言
            if(messageList.size()==(position+1)){
                name="您的投诉";
            }else{
                name="您的留言";
            }
            ImageManager.LoadWithServer(messageList.get(position).getUSER_URL(), myHolder.civlogo);
        }else  if("2".equals(messageType)) {//.平台管理员留言
            name=messageList.get(position).getADMIN_NAME();
            myHolder.civlogo.setImageResource(R.drawable.lehu);
        }else  if("3".equals(messageType)) {//.商户留言
            name=messageList.get(position).getSTORE_NAME();
            ImageManager.LoadWithServer(messageList.get(position).getSTORE_LOGO(), myHolder.civlogo);
        }
        myHolder.tvnametime.setText(name+"  "+messageList.get(position).getCREATEDATE());//名称和时间
        myHolder. tvcontent.setText(messageList.get(position).getMESSAGE());
        myHolder.adapter.refreshData(messageList.get(position).getImageData());//加载图片
        return convertView;
    }

    static class MyHolder{
        ImageAdapter adapter;
        View lineend;//最后的线
        GridView uploadaddgridview;//加载图片
        TextView tvcontent;//回复内容
        LinearLayout layoutfirst;//发起投诉布局
        TextView tvdescrib ;//描述
        TextView tvtitle ;//问题标题
        TextView tvnametime;//回复人和时间
        View linefoot;//图片下的线
        CircleImageView civlogo ;//图片
        View linehead ;//图片上的线
        
		MyHolder(View convertView, final Activity activity) {
			lineend = (View) convertView.findViewById(R.id.line_end);
			uploadaddgridview = (GridView) convertView.findViewById(R.id.upload_add_gridview);
			tvcontent = (TextView) convertView.findViewById(R.id.tv_content);
			layoutfirst = (LinearLayout) convertView.findViewById(R.id.layout_first);
			tvdescrib = (TextView) convertView.findViewById(R.id.tv_describ);
			tvtitle = (TextView) convertView.findViewById(R.id.tv_title);
			tvnametime = (TextView) convertView.findViewById(R.id.tv_name_time);
			linefoot = (View) convertView.findViewById(R.id.line_foot);
			civlogo = (CircleImageView) convertView.findViewById(R.id.civ_logo);
			linehead = (View) convertView.findViewById(R.id.line_head);
			
			int with= DeviceUtils.getWidthMaxPx(activity);
			adapter=new ImageAdapter(activity, ((with/6)*5),5);
			uploadaddgridview.setAdapter(adapter);
			uploadaddgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					ArrayList<String> imgUrlList = adapter.getImageUrlList();

					Intent intent = new Intent();
					intent.setClass(activity, PhotoViewActivity.class);
					intent.putStringArrayListExtra(PhotoViewActivity.INTENT_KEY_PHOTO, imgUrlList);
					intent.putExtra(PhotoViewActivity.INTENT_KEY_POSITION, position);
					activity.startActivity(intent);
				}
			});
		}
    }

}
