package imui.jiguang.cn.imuisample.messages;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
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
    private int mRadius;
    // 气泡顶点距离矩形的宽度
    private int mBubbleVertexWidth;
    // 气泡底边高度
    private int mBubbleHeight;
    // 气泡顶点 Y 坐标
    private int mBubbleVertexY;
    private float mMinWidth;
    private float mMaxWidth;
    private float mMinHeight;
    private float mMaxHeight;

    public ImageTarget(ImageView view, float density) {
        super(view);
        this.mImageView = view;
        this.mDensity = density;
        mMinWidth = 60 * density;
        mMaxWidth = 200 * density;
        mMinHeight = 60 * density;
        mMaxHeight = 250 * density;
    }

    private Bitmap resolveBitmap(Bitmap resource) {
        float width = resource.getWidth();
        float height = resource.getHeight();
        if (width > height) {
            if (width < mMinWidth) {
                height = mMinWidth / width * height;
                width = mMinWidth;
            } else if (width > mMaxWidth) {
                height = mMaxWidth / width * height;
                width = mMaxWidth;
            }
        } else {
            if (height < mMinHeight) {
                width = mMinHeight / height * width;
                height = mMinHeight;
            } else if (height > mMaxHeight) {
                width = mMaxHeight / height * width;
                height = mMaxHeight;
            }
        }

        ViewGroup.LayoutParams params = mImageView.getLayoutParams();
        params.width = (int) width;
        params.height = (int) height;
        mImageView.setLayoutParams(params);

        return resource;
    }

    @Override
    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
        super.onResourceReady(resolveBitmap(resource), transition);
    }
}
