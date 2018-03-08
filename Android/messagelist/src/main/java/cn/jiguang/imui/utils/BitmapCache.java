package cn.jiguang.imui.utils;

import android.graphics.Bitmap;
import android.util.LruCache;


public class BitmapCache {

    private LruCache<String, Bitmap> mMemoryCache;
    private static BitmapCache mInstance = new BitmapCache();

    private BitmapCache() {
        //获取应用程序的最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        //用最大内存的1/4来存储图片
        final int cacheSize = maxMemory / 4;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            //获取每张图片的大小
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };
    }

    public static BitmapCache getInstance() {
        return mInstance;
    }

    public void setBitmapCache(String path, Bitmap bitmap) {
        if (getBitmapFromMemCache(path) == null && bitmap != null) {
            mMemoryCache.put(path, bitmap);
        }
    }

    /**
     * 根据key来获取内存中的图片
     *
     * @param key path
     * @return bitmap
     */
    public Bitmap getBitmapFromMemCache(String key) {
        if (key == null) {
            return null;
        } else {
            return mMemoryCache.get(key);
        }
    }


}
