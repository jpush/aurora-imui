package cn.jiguang.imui.chatinput;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import static android.R.attr.textColor;

public class ProgressButton extends Button {

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

    private int mPercent;
    private float mCurrentPercent;

    private boolean mPlaying;
    private Bitmap mPlayBmp;
    private Bitmap mPauseBmp;
    private int mCacheAngle;
    private ProgressThread mThread;
    private int mCurrentState = 0;
    private static final int INIT_STATE = 0;
    private static final int PLAYING_STATE = 1;
    private static final int PAUSE_STATE = 2;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPlayBmp = BitmapFactory.decodeResource(getResources(), R.drawable.play_audio);
        mPauseBmp = BitmapFactory.decodeResource(getResources(), R.drawable.pause_play_audio);
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int angle = (int) Math.ceil(mCurrentPercent * 3.6);
        if (mCurrentState == PAUSE_STATE) {
            mCacheAngle = angle;
        }
        /**
         * 画最外层的大圆环
         */
        int centerX = getWidth()/2; //获取圆心的x坐标
        int centerY = getHeight() / 2;
        int radius = (int) (centerX - mRoundWidth /2); //圆环的半径
        /**
         * 画圆弧 ，画圆环的进度
         */


        RectF oval = new RectF(centerX - radius, centerX - radius, centerX + radius,
                centerX + radius);  //用于定义的圆弧的形状和大小的界限
        Rect rect = new Rect(centerX - radius / 2, centerY - radius / 2, centerX + radius / 2, centerY + radius / 2);
        Rect rect2 = new Rect(centerX - radius / 2 + 10, centerY - radius / 2, centerX + radius / 2 + 10, centerY + radius / 2);


        switch (mCurrentState) {
            case INIT_STATE:
                mPaint.setColor(mRoundColor); //设置圆环的颜色
                mPaint.setStyle(Paint.Style.STROKE); //设置空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setAntiAlias(true);  //消除锯齿
                canvas.drawCircle(centerX, centerX, radius, mPaint); //画出圆环
                canvas.drawBitmap(mPlayBmp, null, rect2, mPaint);
                mCurrentPercent = 0;
                break;
            case PLAYING_STATE:
                mPaint.setColor(mRoundColor); //设置圆环的颜色
                mPaint.setStyle(Paint.Style.STROKE); //设置空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setAntiAlias(true);  //消除锯齿
                canvas.drawCircle(centerX, centerX, radius, mPaint); //画出圆环
                //设置进度是实心还是空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setColor(mRoundProgressColor);  //设置进度的颜色
                canvas.drawArc(oval, 270, angle, false, mPaint);  //根据进度画圆弧
                canvas.drawBitmap(mPauseBmp, null, rect, mPaint);
                break;
            case PAUSE_STATE:
                mPaint.setColor(mRoundColor); //设置圆环的颜色
                mPaint.setStyle(Paint.Style.STROKE); //设置空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setAntiAlias(true);  //消除锯齿
                canvas.drawCircle(centerX, centerX, radius, mPaint); //画出圆环
                //设置进度是实心还是空心
                mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
                mPaint.setColor(mRoundProgressColor);  //设置进度的颜色
                canvas.drawArc(oval, 270, mCacheAngle, false, mPaint);  //根据进度画圆弧
                canvas.drawBitmap(mPlayBmp, null, rect2, mPaint);
                break;
        }
    }

    public void startPlay(float progress) {
        mCurrentPercent = (progress / mMax) * 100;
        if (mThread == null) {
            mThread = new ProgressThread();
            mThread.setProgress(mCurrentPercent);
            mThread.start();
        }
        mThread.play();
        mCurrentState = PLAYING_STATE;
    }


    public void stopPlay() {
            mCurrentState = PAUSE_STATE;
            postInvalidate();
            if (mThread != null) {
                mThread.exit();
                mThread.stop();
                mThread = null;
            }
            postInvalidate();

    }

    public void finishPlay() {
        mCurrentState = INIT_STATE;
        try {
            if (mThread != null) {
                mThread.exit();
                mThread.join();
                mThread = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mCurrentPercent = 0;
        mCacheAngle = 0;
        postInvalidate();
    }

    public synchronized int getMax() {
        return mMax;
    }

    /**
     * 设置进度的最大值
     * @param max
     */
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("mMax not less than 0");
        }
        this.mMax = max;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return mPercent;
    }

    private class ProgressThread extends Thread {

        private volatile boolean running = true;
        private float mPercent = 0;

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
                        Thread.sleep(10 * mMax);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("ProgressButton", "thread running, percent: " + i);
                    mCurrentPercent = i;
                    postInvalidate();
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
