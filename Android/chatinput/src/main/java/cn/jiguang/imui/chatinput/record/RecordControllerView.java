package cn.jiguang.imui.chatinput.record;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.jiguang.imui.chatinput.R;


public class RecordControllerView extends View {

    private final static String TAG = "RecordControllerView";
    private int mWidth;
    private Path mPath;
    private Paint mPaint;
    private int mRecordBtnLeft;
    private int mRecordBtnRight;
    private int mRecordBtnTop;
    private int mRecordBtnBottom;
    private RecordVoiceButton mRecordVoiceBtn;
    private final int MAX_RADIUS = 90;

    private int mCurrentState = 0;
    private float mNowX;
    private final static int INIT_STATE = 0;
    private final static int MOVING_LEFT = 1;
    private final static int MOVE_ON_LEFT = 2;
    private final static int MOVING_RIGHT = 3;
    private final static int MOVE_ON_RIGHT = 4;

    private Bitmap mCancelBmp;
    private Bitmap mPreviewBmp;
    private Bitmap mCancelPresBmp;
    private Bitmap mPreviewPresBmp;
    private Rect mLeftRect;
    private Rect mRightRect;
    private OnRecordActionListener mListener;

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
        mCancelBmp = BitmapFactory.decodeResource(getResources(), R.drawable.aurora_recordvoice_cancel_record);
        mPreviewBmp = BitmapFactory.decodeResource(getResources(), R.drawable.aurora_recordvoice_preview_play);
        mCancelPresBmp = BitmapFactory.decodeResource(getResources(), R.drawable.aurora_recordvoice_cancel_record_pres);
        mPreviewPresBmp = BitmapFactory.decodeResource(getResources(), R.drawable.aurora_recordvoice_preview_play_pres);
    }

    public void setWidth(int width) {
        mWidth = width;
        Log.e("RecordControllerView", "mWidth: " + mWidth);
        mLeftRect = new Rect((int) (155 - 25 * Math.sqrt(2)), (int) (200 - 25 * Math.sqrt(2)),
                (int) (155 + 25 * Math.sqrt(2)), (int) (200 + 25 * Math.sqrt(2)));
        mRightRect = new Rect((int) (mWidth - 150 - 25 * Math.sqrt(2)), (int) (200 - 25 * Math.sqrt(2)),
                (int) (mWidth - 150 + 25 * Math.sqrt(2)), (int) (200 + 25 * Math.sqrt(2)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mCurrentState) {
            case INIT_STATE:
                mPaint.setColor(Color.rgb(211, 211, 211));
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setAntiAlias(true);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(150, 200, 60, mPaint);
                canvas.drawCircle(mWidth - 150, 200, 60, mPaint);
                mPaint.setColor(Color.GRAY);
                canvas.drawBitmap(mPreviewBmp, null, mLeftRect, mPaint);
                canvas.drawBitmap(mCancelBmp, null, mRightRect, mPaint);
                break;
            case MOVING_LEFT:
                float radius;
                if (mNowX < 150 + MAX_RADIUS) {
                    radius = MAX_RADIUS;
                } else {
                    radius = 40.0f * (mRecordBtnLeft - mNowX) / (mRecordBtnLeft - 250.0f) + 60.0f;
                }
                mPaint.setColor(Color.rgb(211, 211, 211));
                canvas.drawCircle(150, 200, radius, mPaint);
                canvas.drawCircle(mWidth - 150, 200, 60, mPaint);
                mPaint.setColor(Color.GRAY);
                canvas.drawBitmap(mPreviewBmp, null, mLeftRect, mPaint);
                canvas.drawBitmap(mCancelBmp, null, mRightRect, mPaint);
                break;
            case MOVING_RIGHT:
                radius = 40.0f * (mNowX - mRecordBtnRight) / (mWidth - mRecordBtnRight) + 60.0f;
                mPaint.setColor(Color.rgb(211, 211, 211));
                canvas.drawCircle(150, 200, 60, mPaint);
                canvas.drawCircle(mWidth - 150, 200, radius, mPaint);
                mPaint.setColor(Color.GRAY);
                canvas.drawBitmap(mPreviewBmp, null, mLeftRect, mPaint);
                canvas.drawBitmap(mCancelBmp, null, mRightRect, mPaint);
                break;
            case MOVE_ON_LEFT:
                radius = MAX_RADIUS;
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(150, 200, radius, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawBitmap(mPreviewPresBmp, null, mLeftRect, mPaint);
                canvas.drawCircle(mWidth - 150, 200, 60, mPaint);
                canvas.drawBitmap(mCancelBmp, null, mRightRect, mPaint);
                break;
            case MOVE_ON_RIGHT:
                radius = MAX_RADIUS;
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(mWidth - 150, 200, radius, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(150, 200, 60, mPaint);
                canvas.drawBitmap(mPreviewBmp, null, mLeftRect, mPaint);
                canvas.drawBitmap(mCancelPresBmp, null, mRightRect, mPaint);
                break;
        }

    }

    public void onActionDown() {
        if (mListener != null) {
            mListener.onStart();
        }
    }

    public void onActionMove(float x, float y) {
        mNowX = x;
        if (x <= 150 + MAX_RADIUS && y >= 200 - mRecordBtnTop - MAX_RADIUS
                && y <= 200 + MAX_RADIUS - mRecordBtnTop) {
            mCurrentState = MOVE_ON_LEFT;
            if (mListener != null) {
                mListener.onMovedLeft();
            }
        } else if (x > 200 + MAX_RADIUS && x < mRecordBtnLeft) {
            mCurrentState = MOVING_LEFT;
            if (mListener != null) {
                mListener.onMoving();
            }
        } else if (mRecordBtnLeft < x && x < mRecordBtnRight) {
            mCurrentState = INIT_STATE;
            if (mListener != null) {
                mListener.onMoving();
            }
        } else if (x > mRecordBtnRight && x < mWidth - 150 - MAX_RADIUS) {
            mCurrentState = MOVING_RIGHT;
            if (mListener != null) {
                mListener.onMoving();
            }
        } else if (x >= mWidth - 150 - MAX_RADIUS && y > 200 - mRecordBtnTop - MAX_RADIUS
                && y < 200 + MAX_RADIUS - mRecordBtnTop) {
            mCurrentState = MOVE_ON_RIGHT;
            if (mListener != null) {
                mListener.onMovedRight();
            }
        }
        postInvalidate();
    }

    public void setRecordButton(RecordVoiceButton button) {
        mRecordBtnLeft = button.getLeft();
        mRecordBtnRight = button.getRight();
        mRecordBtnTop = button.getTop();
        mRecordBtnBottom = button.getBottom();
        mRecordVoiceBtn = button;
    }

    public void onActionUp() {
        switch (mCurrentState) {
            case MOVE_ON_LEFT:
                mRecordVoiceBtn.finishRecord(true);
                if (mListener != null) {
                    mListener.onLeftUpTapped();
                }
                break;
            case MOVE_ON_RIGHT:
                mRecordVoiceBtn.cancelRecord();
                if (mListener != null) {
                    mListener.onRightUpTapped();
                }
                break;
            default:
                mRecordVoiceBtn.finishRecord(false);
                if (mListener != null) {
                    mListener.onFinish();
                }
        }
        mCurrentState = INIT_STATE;
        postInvalidate();
    }

    public void resetState() {
        mCurrentState = INIT_STATE;
        postInvalidate();
    }

    public void setOnControllerListener(OnRecordActionListener listener) {
        mListener = listener;
    }

    public interface OnRecordActionListener {

        void onStart();

        void onMoving();

        void onMovedLeft();

        void onMovedRight();

        void onRightUpTapped();

        void onLeftUpTapped();

        void onFinish();
    }
}
