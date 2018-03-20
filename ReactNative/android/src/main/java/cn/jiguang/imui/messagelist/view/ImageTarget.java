package cn.jiguang.imui.messagelist.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;


public class ImageTarget extends BitmapImageViewTarget {

    // 长图，宽图比例阈值
    public static final int RATIO_OF_LARGE = 3;
    // 长图截取后的高宽比（宽图截取后的宽高比）
    public static int HW_RATIO = 3;
    private ImageView mImageView;

    public ImageTarget(ImageView view) {
        super(view);
        this.mImageView = view;
    }

    private Bitmap resolveBitmap(Bitmap resource) {
        int srcWidth = resource.getWidth();
        int srcHeight = resource.getHeight();
        if (srcWidth > srcHeight) {
            float srcWHRatio = (float) srcWidth / srcHeight;
            // 宽图
            if (srcWHRatio > RATIO_OF_LARGE) {
                resource = Bitmap.createBitmap(resource, 0, 0, srcHeight * HW_RATIO, srcHeight);
            }
        } else {
            float srcHWRatio = (float) srcHeight / srcWidth;
            // 长图
            if (srcHWRatio > RATIO_OF_LARGE) {
                resource = Bitmap.createBitmap(resource, 0, 0, srcWidth, srcWidth * HW_RATIO);
            }
        }
        return createRoundCornerBitmap(resource);
    }

    private Bitmap createRoundCornerBitmap(Bitmap resource) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap result = Bitmap.createBitmap(resource.getWidth(), resource.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        RectF rectF = new RectF(0, 0, resource.getWidth(), resource.getHeight());
        canvas.drawRoundRect(rectF, 20, 20, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(resource, 0, 0, paint);
        return result;
    }

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        super.onResourceReady(resolveBitmap(resource), glideAnimation);
    }


}
