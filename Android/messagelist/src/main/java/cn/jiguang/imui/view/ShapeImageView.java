package cn.jiguang.imui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.Arrays;

import cn.jiguang.imui.R;


public class ShapeImageView extends android.support.v7.widget.AppCompatImageView {

    private Paint mPaint;
    private Shape mShape;

    private float mRadius;

    public ShapeImageView(Context context) {
        super(context);
    }

    public ShapeImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShapeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayerType(LAYER_TYPE_HARDWARE, null);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MessageList);
            mRadius = a.getDimensionPixelSize(R.styleable.MessageList_photoMessageRadius,
                    context.getResources().getDimensionPixelSize(R.dimen.aurora_radius_photo_message));
            a.recycle();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mShape == null) {
            float[] radius = new float[8];
            Arrays.fill(radius, mRadius);
            mShape = new RoundRectShape(radius, null, null);
        }
        mShape.resize(getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.getSaveCount();
        canvas.save();
        super.onDraw(canvas);
        if (mShape != null) {
            mShape.draw(canvas, mPaint);
        }
        canvas.restoreToCount(saveCount);
    }

    public void setBorderRadius(int radius) {
        this.mRadius = radius;
        invalidate();
    }
}
