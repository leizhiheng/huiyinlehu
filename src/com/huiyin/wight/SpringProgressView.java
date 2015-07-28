package com.huiyin.wight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义进度条
 * @author mike 2015/6/13
 */
public class SpringProgressView extends View {

	/**
	 * 分段颜色
	 */
	private static int[] SECTION_COLORS = { Color.GREEN, Color.YELLOW,
			Color.RED };
	/**
	 * 进度条最大值
	 */
	private int maxCount;
	/**
	 * 进度条当前值
	 */
	private int currentCount;

	private float addCount = 0;
	private int mWidth, mHeight;
	private int strokeColor = Color.rgb(71, 76, 80);// 边框颜色
	private int bgColor=-1 ;// 背景颜色
	private int textColor = Color.WHITE;// 字体颜色
	private long speed = 100;
	private int num = 0;
	private long DuringTime = 2000; // 执行时间为2000ms
	private long lasttime;
    private float FACATOR; //增长因数

    public SpringProgressView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context, null, -1, -1, -1);
	}

	public SpringProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, null, -1, -1, -1);
	}

	public SpringProgressView(Context context) {
		this(context, null, -1, -1, -1);

	}
	public SpringProgressView(Context context, int[] COLORS, int strokeColor,
                              int bgColor, int textColor) {
		super(context);
		initView(context, COLORS, strokeColor, bgColor, textColor);
	}

    /**
     * 初始化填充，边框,文字的颜色
     * @param context 上下文
     * @param COLORS 填充的颜色
     * @param strokeColor 边框颜色
     * @param textColor 文字颜色
     */
    public void initView(Context context, int COLORS, int strokeColor,
                         int textColor) {
        this.initView(context,new int[]{COLORS,COLORS,COLORS},strokeColor,-1,textColor);
    }

	public void initView(Context context, int[] COLORS, int strokeColor,
			int bgColor, int textColor) {
		if (COLORS != null) {
			for (int i = 0; i < COLORS.length; i++) {
				SECTION_COLORS[i] = context.getResources().getColor(COLORS[i]);
			}
		}
		if (strokeColor != -1) {
			this.strokeColor = context.getResources().getColor(strokeColor);
		}
		if (bgColor != -1) {
			this.bgColor = context.getResources().getColor(bgColor);
		}
		if (textColor != -1) {
			this.textColor = context.getResources().getColor(textColor);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint mPaint = new Paint();
		mPaint.setAntiAlias(true);
		int round = mHeight / 2;
		// 绘制外边框
		mPaint.setColor(strokeColor);
		mPaint.setStrokeWidth(2);
		mPaint.setStyle(Paint.Style.STROKE);
		RectF rectStrok = new RectF(0, 0, mWidth, mHeight);
		canvas.drawRoundRect(rectStrok, round, round, mPaint);
        // 绘制内部填充
        mPaint.setStrokeWidth(0);
		mPaint.setStyle(Paint.Style.FILL);
		float section = addCount / maxCount;
		RectF rectProgressBg = new RectF(0, 0, mWidth * section, mHeight);
		if (section <= 1.0f / 3.0f) {
			if (section != 0.0f) {
				mPaint.setColor(SECTION_COLORS[0]);
			} else {
				mPaint.setColor(Color.TRANSPARENT);
			}
		} else {
			int count = (section <= 1.0f / 3.0f * 2) ? 2 : 3;
			int[] colors = new int[count];
			System.arraycopy(SECTION_COLORS, 0, colors, 0, count);
			LinearGradient shader = new LinearGradient(3, 3, (mWidth - 3)
					* section, mHeight - 3, colors, null,
					Shader.TileMode.MIRROR);
			mPaint.setShader(shader);
		}
		canvas.drawRoundRect(rectProgressBg, round, round, mPaint);
        // 绘制文字
		String mText = (int)addCount + "/" + maxCount;
		mPaint = new Paint();
		mPaint.setColor(textColor);
		mPaint.setTextSize(30);
		canvas.drawText(mText, (float) (getWidth() / 3),
				(float) (getHeight() / 2 + 10), mPaint);
	}

	private int dipToPx(int dip) {
		float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	/**
	 * 设置最大的进度值
	 */
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	/**
	 * 设置当前的进度值
	 */
	public void setCurrentCount(int currentCount) {
		lasttime = System.currentTimeMillis();
		this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        caculatFactor();
		invalidate();
		if (mHandler.hasMessages(0))
			mHandler.removeMessages(0);
		mHandler.sendEmptyMessage(0);
	}

	public float getMaxCount() {
		return maxCount;
	}

	public float getCurrentCount() {
		return currentCount;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		if (widthSpecMode == MeasureSpec.EXACTLY
				|| widthSpecMode == MeasureSpec.AT_MOST) {
			mWidth = widthSpecSize;
		} else {
			mWidth = 0;
		}
		if (heightSpecMode == MeasureSpec.AT_MOST
				|| heightSpecMode == MeasureSpec.UNSPECIFIED) {
			mHeight = dipToPx(15);
		} else {
			mHeight = heightSpecSize;
		}
		setMeasuredDimension(mWidth, mHeight);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (addCount < currentCount) {
					addCount = ((num++) * FACATOR);
					if (addCount < currentCount) {
						invalidate();
					}
					sendEmptyMessageDelayed(0, speed);
				} else {
					addCount = currentCount;
					invalidate();
					Log.d("自定义进度条",
							"加载时间："
									+ long2Str(System.currentTimeMillis()
											- lasttime) + "秒");
					if (mHandler.hasMessages(0))
						mHandler.removeMessages(0);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 计算进度条的增长因数
	 */
	private void caculatFactor() {
		int count = (int) (DuringTime / speed);
        FACATOR = (float) currentCount / count;
		Log.d("自定义进度条", "caculatFactor()&&FACATOR=" + FACATOR);
	}

	/**
	 * 重置
	 */
	public void reset() {
		addCount = 0.0f;
		currentCount = 0;
		num = 0;
		invalidate();
	}

	public long getDuringTime() {
		return DuringTime;
	}

    /**
     * 设置动画持续时间
     * @param duringTime
     */
	public void setDuringTime(long duringTime) {
		DuringTime = duringTime;
	}

	private String long2Str(long time) {
		SimpleDateFormat mFormat = new SimpleDateFormat("ss");
		return mFormat.format(new Date(time));
	}
}
