package cn.jiguang.imui.chatinput.record;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

import cn.jiguang.imui.chatinput.R;

import static android.R.attr.textColor;

public class ProgressButton extends AppCompatButton {

    private Paint mPaint;

    /**
     * 圆环的颜色
     */
    private int mRoundColor;

    /**
     * 圆环进度的颜色
     */
    private int mRoundProgressColor;

    /**
     * 圆环的宽度
     */
    private float mRoundWidth;

    /**
     * 最大进度
     */
    private int mMax;

    private AtomicInteger mCurrentPercent = new AtomicInteger(0);
    private Bitmap mPlayBmp;
    private Bitmap mPauseBmp;
    private int mEndAngle;
    private ProgressThread mThread;
    private int mCurrentState = 0;
    private static final int INIT_STATE = 0;
    private static final int PLAYING_STATE = 1;
    private static final int PAUSE_STATE = 2;
    private int mCenterX;
    private int mRadius;
    // 圆环进度
    private RectF mOval;
    // 播放时的中心区域
    private Rect mRect1;
    // 暂停时的中心区域
    private Rect mRect2;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPlayBmp = BitmapFactory.decodeResource(getResources(), R.drawable.aurora_recordvoice_play);
        mPauseBmp = BitmapFactory.decodeResource(getResources(), R.drawable.aurora_recordvoice_pause);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ProgressButton);

        //获取自定义属性和默认值
        mRoundColor = typedArray.getColor(R.styleable.ProgressButton_roundColor, Color.RED);
        mRoundProgressColor = typedArray.getColor(R.styleable.ProgressButton_roundProgressColor, Color.GREEN);
        mRoundWidth = typedArray.getDimension(R.styleable.ProgressButton_roundWidth, 5);
        mMax = typedArray.getInteger(R.styleable.ProgressButton_max, 100);

        typedArray.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        /**
         * 画最外层的大圆环
         */
        mCenterX = getWidth() / 2; //获取圆心的x坐标
        int centerY = getHeight() / 2;
        mRadius = (int) (mCenterX - mRoundWidth / 2); //圆环的半径
        if (mOval == null) {
            mOval = new RectF(mCenterX - mRadius, mCenterX - mRadius, mCenterX + mRadius,
                    mCenterX + mRadius);  //用于定义的圆弧的形状和大小的界限
        }
        if (mRect1 == null) {
            mRect1 = new Rect(mCenterX - mRadius / 2, centerY - mRadius / 2, mCenterX + mRadius / 2, centerY + mRadius / 2);
        }
        if (mRect2 == null) {
            mRect2 = new Rect(mCenterX - mRadius / 2 + 10, centerY - mRadius / 2, mCenterX + mRadius / 2 + 10, centerY + mRadius / 2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mEndAngle = (int) Math.ceil(mCurrentPercent.get() * 3.6);
        if (mEndAngle > 360) {
            mEndAngle = 360;
        }


        switch (mCurrentState) {
            case INIT_STATE:
                mPaint.setColor(mRoundColor); //设置圆环的颜色
                mPaint.setStyle(Paint.Style.STROKE); //设置空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setAntiAlias(true);  //消除锯齿
                canvas.drawCircle(mCenterX, mCenterX, mRadius, mPaint); //画出圆环
                canvas.drawBitmap(mPlayBmp, null, mRect2, mPaint);
                mCurrentPercent.set(0);
                break;
            case PLAYING_STATE:
                Log.e("ProgressButton", "Angle: " + mEndAngle);
                mPaint.setColor(mRoundColor); //设置圆环的颜色
                mPaint.setStyle(Paint.Style.STROKE); //设置空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setAntiAlias(true);  //消除锯齿
                canvas.drawCircle(mCenterX, mCenterX, mRadius, mPaint); //画出圆环
                //设置进度是实心还是空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setColor(mRoundProgressColor);  //设置进度的颜色
                canvas.drawArc(mOval, 270, mEndAngle, false, mPaint);  //根据进度画圆弧
                canvas.drawBitmap(mPauseBmp, null, mRect1, mPaint);
                break;
            case PAUSE_STATE:
                mPaint.setColor(mRoundColor); //设置圆环的颜色
                mPaint.setStyle(Paint.Style.STROKE); //设置空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setAntiAlias(true);  //消除锯齿
                canvas.drawCircle(mCenterX, mCenterX, mRadius, mPaint); //画出圆环
                //设置进度是实心还是空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setColor(mRoundProgressColor);  //设置进度的颜色
                canvas.drawArc(mOval, 270, mEndAngle, false, mPaint);  //根据进度画圆弧
                canvas.drawBitmap(mPlayBmp, null, mRect2, mPaint);
                break;
        }
    }

    public void startPlay() {
        if (mThread == null) {
            mThread = new ProgressThread();
            mThread.start();
        }
        mThread.setProgress(mCurrentPercent.get());
        mThread.play();
        mCurrentState = PLAYING_STATE;
        postInvalidate();
    }


    public void stopPlay() {
        mCurrentState = PAUSE_STATE;
        postInvalidate();
        try {
            if (mThread != null) {
                mThread.exit();
                mThread.join();
                mThread = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        postInvalidate();

    }

    public void finishPlay() {
        try {
            if (mThread != null) {
                mThread.exit();
                mThread.join();
                mThread = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mCurrentState = INIT_STATE;
        mCurrentPercent.set(0);
        postInvalidate();
    }

    public synchronized int getMax() {
        return mMax;
    }

    /**
     * 设置进度的最大值
     *
     * @param max audio duration in seconds
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("mMax not less than 0");
        }
        this.mMax = max;
    }

    private class ProgressThread extends Thread {

        private volatile boolean running = true;
        private float mPercent = 1;

        public void exit() {
            running = false;
        }

        public void play() {
            running = true;
        }

        public void setProgress(float currentPercent) {
            mPercent = currentPercent;
        }

        @Override
        public void run() {
            while (running) {
                for (int i = (int) mPercent; i <= 100; i++) {
                    try {
                        mCurrentPercent.set(i);
                        if (running) {
                            postInvalidate();
                        } else {
                            break;
                        }
                        Thread.sleep(10 * mMax);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    public int getCircleColor() {
        return mRoundColor;
    }

    public void setCircleColor(int circleColor) {
        this.mRoundColor = circleColor;
    }

    public int getCircleProgressColor() {
        return mRoundProgressColor;
    }

    public void setCircleProgressColor(int circleProgressColor) {
        this.mRoundProgressColor = circleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public float getRoundWidth() {
        return mRoundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.mRoundWidth = roundWidth;
    }
}
