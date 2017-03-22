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

    /**
     * 当前进度
     */
    private int mProgress;

    /**
     * 进度的风格，实心或者空心
     */
    private int mStyle;
    private boolean mPlaying;
    private Bitmap mPlayBmp;
    private Bitmap mPauseBmp;

    public static final int STROKE = 0;
    public static final int FILL = 1;

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
        mStyle = typedArray.getInt(R.styleable.ProgressButton_style, 0);

        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        int centerX = getWidth()/2; //获取圆心的x坐标
        int centerY = getHeight() / 2;
        int radius = (int) (centerX - mRoundWidth /2); //圆环的半径
        mPaint.setColor(mRoundColor); //设置圆环的颜色
        mPaint.setStyle(Paint.Style.STROKE); //设置空心
        mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
        mPaint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(centerX, centerX, radius, mPaint); //画出圆环

        Log.e("log", centerX + "");

        /**
         * 画圆弧 ，画圆环的进度
         */

        //设置进度是实心还是空心
        mPaint.setStrokeWidth(mRoundWidth); //设置圆环的宽度
        mPaint.setColor(mRoundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centerX - radius / 2, centerX - radius / 2, centerX
                + radius / 2, centerX + radius / 2);  //用于定义的圆弧的形状和大小的界限
        Rect rect = new Rect(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        switch (mStyle) {
            case STROKE:{
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360 * mProgress / mMax, false, mPaint);  //根据进度画圆弧
                break;
            }
            case FILL:{
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(mProgress !=0)
                    canvas.drawArc(oval, 0, 360 * mProgress / mMax, true, mPaint);  //根据进度画圆弧
                break;
            }
        }

        if (mPlaying) {
            canvas.drawBitmap(mPauseBmp, null, rect, mPaint);
        } else {
            canvas.drawBitmap(mPlayBmp, null, rect, mPaint);
        }

    }

    public void stopPlay() {
        mPlaying = false;
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
        return mProgress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("mProgress not less than 0");
        }
        mPlaying = true;
        if(progress > mMax){
            progress = mMax;
        }
        if(progress <= mMax){
            this.mProgress = progress;
            postInvalidate();
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
