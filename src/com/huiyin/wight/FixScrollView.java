package com.huiyin.wight;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 阻止嵌套子listView后自动滑动的问题
 * Created by Mike on 2015/7/2.
 */
public class FixScrollView extends ScrollView{
    public FixScrollView(Context context) {
        super(context);
    }

    public FixScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
