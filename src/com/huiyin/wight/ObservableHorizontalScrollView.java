package com.huiyin.wight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * 该类继承自HorizontalScrollView，复写方法onScrollChanged(int x, int y, int oldx, int oldy)方法，
 * 可监听HorizontalScrollView从开始滑动到静止过程中scrollX和scrollY的变化
 * @author leizhiheng
 *
 */
public class ObservableHorizontalScrollView extends HorizontalScrollView {

	private ScrollViewListener scrollViewListener;
	
	public interface ScrollViewListener{
		void onScrollChanged(int x, int y, int oldx, int oldy);  
	}
	public ObservableHorizontalScrollView(Context context) {
		this(context, null);
	}

	public ObservableHorizontalScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ObservableHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(x, y, oldx, oldy);
		}
	}
}
