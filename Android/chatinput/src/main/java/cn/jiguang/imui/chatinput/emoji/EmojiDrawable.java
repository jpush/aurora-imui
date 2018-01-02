package cn.jiguang.imui.chatinput.emoji;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;

/**
 * Use AndroidEmoji(https://github.com/w446108264/AndroidEmoji)
 * author: sj
 */
public class EmojiDrawable extends Drawable {

    private static final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);

    private Bitmap bmp;

    @Override
    public void draw(Canvas canvas) {
        if (bmp == null) {
            return;
        }

        // Bitmap.createScaledBitmap(bmp, width, height, true)
        canvas.drawBitmap(bmp,
                null,
                getBounds(),
                paint);
    }

    public void setBitmap(Bitmap bitmap) {
        assertMainThread();
        if(bitmap == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1 && bitmap.sameAs(bmp)){
            return;
        }
        bmp = bitmap;
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) { }

    @Override
    public void setColorFilter(ColorFilter cf) { }

    public static void assertMainThread() {
        if (!isMainThread()) {
            throw new AssertionError("Main-thread assertion failed.");
        }
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
