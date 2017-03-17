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

    private int mWidth;
    private Path mPath;
    private Paint mPaint;

    public RecordControllerView(Context context) {
        super(context);
        init();
    }

    public RecordControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        mWidth = this.getWidth();
        mPath = new Path();
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        canvas.drawCircle(100, 200, 50, mPaint);
        canvas.drawCircle(this.getRight() - 100, 200, 50, mPaint);
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
