package cn.jiguang.imui.messagelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by caiyaoguan on 2017/11/9.
 */

public class LoadImageAsync extends AsyncTask<String, Void, Drawable> {
    private WeakReference<CustomViewHolder.URLDrawable> mDrawable;
    private WeakReference<Context> mContext;
    private WeakReference<TextView> mTextView;
    private WeakReference<LruCache<String, Bitmap>> mCache;

    public LoadImageAsync(Context context, CustomViewHolder.URLDrawable drawable, TextView textView,
                          LruCache<String, Bitmap> cache) {
        this.mContext = new WeakReference<>(context);
        this.mDrawable = new WeakReference<>(drawable);
        this.mTextView = new WeakReference<>(textView);
        this.mCache = new WeakReference<>(cache);
    }


    @Override
    protected Drawable doInBackground(String... strings) {
        String fileName = strings[1];
        Drawable drawable = null;
        try {
            URL url = new URL(strings[0]);
            InputStream in = url.openStream();
            saveBitmap2File(in, mContext.get().getFilesDir(), fileName);
            in.close();
            File file = new File(mContext.get().getFilesDir() + fileName);
            if (file.exists()) {
                Bitmap bitmap = decodeThumbBitmapForFile(file.getAbsolutePath(), 640, 480);
                mCache.get().put(strings[0], bitmap);
                drawable = new BitmapDrawable(mContext.get().getResources(), bitmap);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return drawable;
    }

    private void saveBitmap2File(InputStream in, File dir, String fileName) throws IOException {
        if (null == in) {
            return;
        }
        if (null == fileName || fileName.equals("")) {
            return;
        }
        File file = new File(dir + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        OutputStream out = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        while((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
    }

    private Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        //设置缩放比例
        options.inSampleSize = calculateInSampleSize(options, viewWidth, viewHeight);

        //设置为false,解析Bitmap对象加入到内存中
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 计算压缩比例值
     *
     * @param options   解析图片的配置信息
     * @param reqWidth  所需图片压缩尺寸最小宽度
     * @param reqHeight 所需图片压缩尺寸最小高度
     * @return 压缩比例
     */
    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // 保存图片原宽高值
        final int height = options.outHeight;
        final int width = options.outWidth;

        // 初始化压缩比例为1
        int inSampleSize = 1;

        // 当图片宽高值任何一个大于所需压缩图片宽高值时,进入循环计算系统
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 压缩比例值每次循环两倍增加,
            // 直到原图宽高值的一半除以压缩值后都~大于所需宽高值为止
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    protected void onPostExecute(Drawable drawable) {
        TextView textView = mTextView.get();
        if (drawable != null && textView != null) {
            mDrawable.get().drawable = drawable;
            textView.requestLayout();
        }
    }
}
