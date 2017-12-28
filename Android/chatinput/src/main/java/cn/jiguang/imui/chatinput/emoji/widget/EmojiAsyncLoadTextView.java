package cn.jiguang.imui.chatinput.emoji.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatTextView;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.jiguang.imui.chatinput.emoji.EmojiDrawable;

/**
 * the view support to load emoji asynchronously.
 * <p>
 * if you want to load emoji asynchronously ,you should use EmojiAsyncLoadTextView.
 * if not, TextView is ok
 * </p>
 * Created by sj on 6/7/16.
 */
public class EmojiAsyncLoadTextView extends AppCompatTextView {

    protected final int POOL_COUNT = 10;

    protected Handler mainHandler = new Handler();

    protected ExecutorService executorService;

    public EmojiAsyncLoadTextView(Context context) {
        this(context, null);
    }

    public EmojiAsyncLoadTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmojiAsyncLoadTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        if (drawable instanceof EmojiDrawable) {
            invalidate();
        } else super.invalidateDrawable(drawable);
    }

    protected boolean checkExecutorService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(POOL_COUNT);
        }

        if (executorService.isShutdown()) {
            return false;
        }
        return true;
    }

    /**
     * should canelAllLoadTask frist ,when setText.
     */
    public void canelAllLoadTask() {
        if (executorService != null) {
            executorService.shutdownNow();
            executorService = null;
        }
    }

    /**
     * add a task to load emoji form file.
     *
     * @param emojiDrawable the EmojiDrawable
     * @param pathName the complete file path.
     */
    public void addAsyncLoadTask(final EmojiDrawable emojiDrawable, final String pathName) {
        if (!checkExecutorService()) {
            return;
        }

        if (emojiDrawable == null || TextUtils.isEmpty(pathName)) {
            return;
        }
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(pathName)) {
                    return;
                }
                final Bitmap bm = BitmapFactory.decodeFile(pathName);
                if (mainHandler == null) {
                    return;
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bm != null) {
                            emojiDrawable.setBitmap(bm);
                        }
                    }
                });
            }
        });
    }

    /**
     * add a task to load emoji form resource.
     *
     * @param emojiDrawable The EmojiDrawable
     * @param res The resources object containing the image data
     * @param id The resource id of the image data
     * @param opts
     */
    public void addAsyncLoadTask(final EmojiDrawable emojiDrawable, final Resources res, final int id, final BitmapFactory.Options opts) {
        if (!checkExecutorService()) {
            return;
        }

        if (emojiDrawable == null || res == null || id < 0) {
            return;
        }
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                final Bitmap bm = BitmapFactory.decodeResource(res, id, opts);
                if (mainHandler == null) {
                    return;
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bm != null) {
                            emojiDrawable.setBitmap(bm);
                        }
                    }
                });
            }
        });
    }

    /**
     * add a task to load emoji form raw data.
     *
     * @param emojiDrawable The EmojiDrawable.
     * @param inputStream The input stream that holds the raw data.
     */
    public void addAsyncLoadTask(final EmojiDrawable emojiDrawable, final InputStream inputStream) {
        if (!checkExecutorService()) {
            return;
        }

        if (emojiDrawable == null || inputStream == null) {
            return;
        }
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                final Bitmap bm = BitmapFactory.decodeStream(inputStream);
                if (mainHandler == null) {
                    return;
                }
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (bm != null) {
                            emojiDrawable.setBitmap(bm);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        canelAllLoadTask();
    }
}
