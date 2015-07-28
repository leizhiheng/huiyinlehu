package com.huiyin.wight;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huiyin.R;
import com.huiyin.bean.CommentItemForJson;
import com.huiyin.bean.OrderDetail;
import com.huiyin.ui.user.UploadImageLinearLayout;
import com.huiyin.utils.ImageManager;
import com.huiyin.utils.imageupload.ImageUpload;

import java.util.ArrayList;

/**
 * Created by justme on 15/5/21.
 */

public class OrderCommodityView extends LinearLayout {

    private Context mContext;

    // Content View Elements

    private ImageView commodity_img;
    private TextView commodity_title;
    private TextView commodity_color;
    private TextView commodity_price;
    private TextView commodity_num;
    private Button commodity_button;
    private LinearLayout layout_content;
    private RatingBar collect_store_rating;
    private CheckBox commodity_check;
    private EditText comment_description;
    private com.huiyin.ui.user.UploadImageLinearLayout upload_layout;
    private TextView commodity_keyword;
    private EditText commodity_key_edit;
    private Button commodity_keyword_add;

    // End Of Content View Elements
//    LayoutTransition mTransition;


    private OrderDetail good_item;
    private int position;
    private onWhichOneClickListener mClickListener;
    private String webImage; //网络图片的地址加，隔开

    public OrderCommodityView(Context context) {
        super(context);
        mContext = context;
        init();
        SetListener();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.order_commodity_view, this, true);

        commodity_img = (ImageView) findViewById(R.id.commodity_img);
        commodity_title = (TextView) findViewById(R.id.commodity_title);
        commodity_color = (TextView) findViewById(R.id.commodity_color);
        commodity_price = (TextView) findViewById(R.id.commodity_price);
        commodity_num = (TextView) findViewById(R.id.commodity_num);
        commodity_button = (Button) findViewById(R.id.commodity_button);

        layout_content = (LinearLayout) findViewById(R.id.layout_content);
        collect_store_rating = (RatingBar) findViewById(R.id.collect_store_rating);
        commodity_check = (CheckBox) findViewById(R.id.commodity_check);
        comment_description = (EditText) findViewById(R.id.comment_description);
        upload_layout = (com.huiyin.ui.user.UploadImageLinearLayout) findViewById(R.id.upload_layout);
        upload_layout.setMaxSize(3);
        commodity_keyword = (TextView) findViewById(R.id.commodity_keyword);
        commodity_key_edit = (EditText) findViewById(R.id.commodity_key_edit);
        commodity_keyword_add = (Button) findViewById(R.id.commodity_keyword_add);

        layout_content.measure(0,0);
        ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) layout_content.getLayoutParams();
        mMarginLayoutParams.bottomMargin = 0-layout_content.getMeasuredHeight();
        layout_content.requestLayout();
//        mTransition = new LayoutTransition();
//        mTransition.setAnimator(
//                LayoutTransition.APPEARING,
//                (mAppear.isChecked() ? mTransition
//                        .getAnimator(LayoutTransition.APPEARING) : null));
//        mTransition
//                .setAnimator(
//                        LayoutTransition.CHANGE_APPEARING,
//                        (mChangeAppear.isChecked() ? mTransition
//                                .getAnimator(LayoutTransition.CHANGE_APPEARING)
//                                : null));
//        mTransition.setAnimator(
//                LayoutTransition.DISAPPEARING,
//                (mDisAppear.isChecked() ? mTransition
//                        .getAnimator(LayoutTransition.DISAPPEARING) : null));
//        mTransition.setAnimator(
//                LayoutTransition.CHANGE_DISAPPEARING,
//                (mChangeDisAppear.isChecked() ? mTransition
//                        .getAnimator(LayoutTransition.CHANGE_DISAPPEARING)
//                        : null));
//        mGridLayout.setLayoutTransition(mTransition);

    }

    private void SetListener() {
        commodity_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                layout_content.setVisibility(View.VISIBLE);
//                commodity_button.setVisibility(View.INVISIBLE);

                ValueAnimator animator = ValueAnimator.ofInt(0-layout_content.getMeasuredHeight(), 0);
                animator.setTarget(layout_content);
                animator.setDuration(1000).start();
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) layout_content.getLayoutParams();
                        mMarginLayoutParams.bottomMargin = (Integer) animation.getAnimatedValue();
                        if (mMarginLayoutParams.bottomMargin == 0) {
                            commodity_button.setVisibility(View.INVISIBLE);
                        }
                        layout_content.requestLayout();
                    }
                });

            }
        });

        upload_layout.setAddPicOnClick(new UploadImageLinearLayout.AddPicOnClick() {
            @Override
            public void OnAddClick() {
                if (mClickListener != null) {
                    mClickListener.onThisClick(position);
                }
            }
        });

        commodity_keyword_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(commodity_key_edit.getText())) {
                    return;
                }
                if (TextUtils.isEmpty(commodity_keyword.getText())) {
                    commodity_keyword.setText(commodity_key_edit.getText());
                } else {
                    commodity_keyword.append("," + commodity_key_edit.getText());
                }
                commodity_key_edit.setText("");
            }
        });
    }

    //界面刷新
    public void RefreshView(OrderDetail good_item, int position) {
        this.good_item = good_item;
        this.position = position;
        ImageManager.LoadWithServer(good_item.GOODS_IMG, commodity_img);
        commodity_title.setText(good_item.GOODS_NAME);
        commodity_price.setText(good_item.getGoodsPrice());
    }

    
    /**
     * 是否输入完成
     * @return
     */
    public boolean isInputOver(){
    	
    	//评价等级
    	float rating = collect_store_rating.getRating();
    	if(rating <=0){
    		return false;
    	}
    	
    	//评价内容
    	String comment = comment_description.getText().toString().trim();
    	if(TextUtils.isEmpty(comment)){
    		return false;
    	}
    	
    	return true;
    }
    
    private void show(String msg){
    	Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * 显示输入提示
     * @return
     */
    public boolean validateShowInputTip() {
    	
    	//评价等级
    	float rating = collect_store_rating.getRating();
    	if(rating <=0){
    		show("亲！给商品打个分吧");
    		return false;
    	}
    	
    	//评价内容
    	String comment = comment_description.getText().toString().trim();
    	if(TextUtils.isEmpty(comment)){
    		show("请输入商品描述！");
    		return false;
    	}
    	
    	return true;
    }

    public CommentItemForJson getTheResult() {

        CommentItemForJson mCommentItemForJson = new CommentItemForJson();
        mCommentItemForJson.orderDetailId = good_item.ORDER_DETAIL_ID;
        mCommentItemForJson.goodsId = good_item.GOODS_ID;
        mCommentItemForJson.goodsNo = good_item.GOODS_NO;
        mCommentItemForJson.storeId = good_item.STORE_ID;
        mCommentItemForJson.evaGrade = collect_store_rating.getRating();
        mCommentItemForJson.nice = commodity_check.isChecked() ? 1 : 0;
        mCommentItemForJson.content = comment_description.getText().toString();
        mCommentItemForJson.label = commodity_keyword.getText().toString();
        mCommentItemForJson.picture = webImage;
        return mCommentItemForJson;
    }

    public void addPicture(String capturePath) {
        if (upload_layout != null) {
            upload_layout.addUrls(capturePath);
        }
    }

    public interface onWhichOneClickListener {
        void onThisClick(int position);

        void onPicUpSucc(boolean isSuccess,int position);
    }

    public void setWhichOneClickListener(onWhichOneClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public void UpLoadImage() {
        ArrayList<String> hostImage = upload_layout.getUrls();
        if (hostImage != null && hostImage.size() > 0) {
            new ImageUpload(mContext, hostImage, new ImageUpload.UpLoadImageListener() {
                @Override
                public void UpLoadSuccess(ArrayList<String> netimageurls) {
                    webImage = "";
                    if (netimageurls != null && netimageurls.size() > 0) {
                        for (int i = 0; i < netimageurls.size(); i++) {
                            webImage += netimageurls.get(i);
                            webImage += ",";
                        }
                        webImage = webImage.substring(0, webImage.length() - 1);
                        if (mClickListener != null) {
                            mClickListener.onPicUpSucc(true,position);
                        }
                    }
                }

                @Override
                public void UpLoadFail() {
                    if (mClickListener != null) {
                        mClickListener.onPicUpSucc(false,position);
                    }
                }
            }).startLoad();
        } else {
            if (mClickListener != null) {
                mClickListener.onPicUpSucc(false,position);
            }
        }

    }
}
