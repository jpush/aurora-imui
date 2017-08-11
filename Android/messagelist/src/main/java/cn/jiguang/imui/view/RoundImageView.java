package cn.jiguang.imui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import cn.jiguang.imui.R;


public class RoundImageView extends BaseImageView {

    private int mRadius;
    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MessageList);
        mRadius = array.getDimensionPixelSize(R.styleable.MessageList_avatarRadius,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_radius_avatar_default));
        array.recycle();
    }

    public Bitmap getBitmap(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        canvas.drawRoundRect(new RectF(0.0f, 0.0f, width, height), mRadius, mRadius, paint);

        return bitmap;
    }

    @Override
    public Bitmap getBitmap() {
        return getBitmap(getWidth(), getHeight());
    }

    public void setBorderRadius(int avatarRadius) {
        mRadius = avatarRadius;
        invalidate();
    }
}
