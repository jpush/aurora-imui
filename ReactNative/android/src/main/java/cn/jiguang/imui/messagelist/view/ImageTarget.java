package cn.jiguang.imui.messagelist.view;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;


public class ImageTarget extends BitmapImageViewTarget {

    // 长图，宽图比例阈值
    public static final int RATIO_OF_LARGE = 3;
    // 长图截取后的高宽比（宽图截取后的宽高比）
    public static int HW_RATIO = 3;
    private ImageView mImageView;
    private float mDensity;

    public ImageTarget(ImageView view, float density) {
        super(view);
        this.mImageView = view;
        this.mDensity = density;
    }

    private Bitmap resolveBitmap(Bitmap resource) {
        float srcWidth = resource.getWidth();
        float srcHeight = resource.getHeight();
        if (srcWidth > srcHeight) {
            float srcWHRatio = srcWidth / srcHeight;
            // 宽图
            if (srcWHRatio > RATIO_OF_LARGE) {
                resource = Bitmap.createBitmap(resource, 0, 0, (int) srcHeight * HW_RATIO, (int) srcHeight);
            }
        } else {
            float srcHWRatio = srcHeight / srcWidth;
            // 长图
            if (srcHWRatio > RATIO_OF_LARGE) {
                resource = Bitmap.createBitmap(resource, 0, 0, (int) srcWidth, (int) srcWidth * HW_RATIO);
            }
        }
        if (srcWidth < 100 * mDensity) {
            srcHeight = srcHeight * (100 * mDensity / srcWidth);
            srcWidth = 100 * mDensity;
        } else if (srcWidth > 250 * mDensity) {
            srcHeight = srcHeight * (200 * mDensity / srcWidth);
            srcWidth = 200 * mDensity;
        }
        if (srcHeight < 100 * mDensity) {
            srcWidth = srcWidth * (100 * mDensity / srcHeight);
            srcHeight = 100 * mDensity;
        } else if (srcHeight > 250 * mDensity) {
            srcWidth = srcWidth * (200 * mDensity / srcHeight);
            srcHeight = 200 * mDensity;
        }
        if (srcWidth < 60 * mDensity) {
            srcWidth = 60 * mDensity;
        }
        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
        params.width = (int) srcWidth;
        params.height = (int) srcHeight;
        mImageView.setLayoutParams(params);
        return resource;
    }

    @Override
    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        super.onResourceReady(resolveBitmap(resource), transition);
    }
}
