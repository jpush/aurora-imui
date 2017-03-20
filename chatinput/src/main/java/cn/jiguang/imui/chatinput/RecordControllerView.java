package cn.jiguang.imui.chatinput;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class RecordControllerView extends View {

    private final static String TAG = "RecordControllerView";
    private int mWidth;
    private Path mPath;
    private Paint mPaint;
    private int mRecordBtnLeft;
    private int mRecordBtnRight;
    private RecordVoiceButton mRecordVoiceBtn;

    private int mCurrentState = 0;
    private float mNowX;
    private final static int INIT_STATE = 0;
    private final static int MOVING_LEFT = 1;
    private final static int MOVE_ON_LEFT = 2;
    private final static int MOVING_RIGHT = 3;
    private final static int MOVE_ON_RIGHT = 4;

    public RecordControllerView(Context context) {
        super(context);
        init();
    }

    public RecordControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        mPath = new Path();
        mPaint = new Paint();
    }

    public void setWidth(int width) {
        mWidth = width;
        Log.e("RecordControllerView", "mWidth: " + mWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mCurrentState) {
            case INIT_STATE:
                mPaint.setColor(Color.GRAY);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(150, 200, 50, mPaint);
                canvas.drawCircle(mWidth - 150, 200, 50, mPaint);
                break;
            case MOVING_LEFT:
                float radius = 50.0f * (mRecordBtnLeft - mNowX) / (mRecordBtnLeft - 250.0f) + 50.0f;
                Log.e(TAG, "left radius: " + radius);
                canvas.drawCircle(150, 200, radius, mPaint);
                canvas.drawCircle(mWidth - 150, 200, 50, mPaint);
                break;
            case MOVING_RIGHT:
                radius = 50.0f * (mNowX - mRecordBtnRight) / (mWidth - mRecordBtnRight) + 50.0f;
                Log.e(TAG, "right radius: " + radius);
                canvas.drawCircle(150, 200, 50, mPaint);
                canvas.drawCircle(mWidth - 150, 200, radius, mPaint);
                break;
            case MOVE_ON_LEFT:
                radius = 100;
                canvas.drawCircle(150, 200, radius, mPaint);
                canvas.drawCircle(mWidth - 150, 200, 50, mPaint);
                break;
            case MOVE_ON_RIGHT:
                radius = 100;
                canvas.drawCircle(150, 200, 50, mPaint);
                canvas.drawCircle(mWidth - 150, 200, radius, mPaint);
                break;
        }

    }

    public void onActionMove(float x) {
        mNowX = x;
        if (x < 250) {
            mCurrentState = MOVE_ON_LEFT;
        } else if (x > 250 && x < mRecordBtnLeft) {
            mCurrentState = MOVING_LEFT;
        } else if (mRecordBtnLeft < x && x < mRecordBtnRight) {
            mCurrentState = INIT_STATE;
        } else if (x > mRecordBtnRight && x < mWidth - 250) {
            mCurrentState = MOVING_RIGHT;
        } else {
            mCurrentState = MOVE_ON_RIGHT;
        }
        postInvalidate();
    }

    public void setRecordButton(RecordVoiceButton button) {
        mRecordBtnLeft = button.getLeft();
        mRecordBtnRight = button.getRight();
        mRecordVoiceBtn = button;
    }

    public void onActionUp(float x, float y) {
        if (x <= 250 && y >= 100 && y <= 300) {
            //TODO preview audio
        } else if (x > 250 && x < mWidth - 250) {
            //TODO finish record
            mRecordVoiceBtn.finishRecord();
        } else if (x >= mWidth - 250 && y >= 100 && y <= 300) {
            //TODO cancel record
            mRecordVoiceBtn.cancelRecord();
        }
        mCurrentState = INIT_STATE;
        postInvalidate();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (event.getX() > mWidth - 150 && event.getX() < mWidth - 50
//                        && event.getY() > 150 && event.getY() < 250) {
//                    Log.i("ControllerView", "Move to right button!");
//                }
//                break;
//        }
//        return true;
//    }
}
