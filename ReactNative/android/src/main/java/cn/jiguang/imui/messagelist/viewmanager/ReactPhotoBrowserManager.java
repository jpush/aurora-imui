package cn.jiguang.imui.messagelist.viewmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.jiguang.imui.commons.BitmapLoader;
import cn.jiguang.imui.messagelist.view.ImgBrowserViewPager;
import cn.jiguang.imui.messagelist.R;
import cn.jiguang.imui.messagelist.view.photoview.PhotoView;


public class ReactPhotoBrowserManager extends ViewGroupManager<LinearLayout> {

    private static final String REACT_PHOTO_BROWSER = "RCTPhotoBrowser";
    private static final int RECEIVE_PATH_EVENT = 0;
    private Context mContext;
    private ImgBrowserViewPager mViewPager;
    private List<Object> mPathList = new ArrayList<>();
    private List<Object> mMsgIdList = new ArrayList<>();
    private String mClickMsgId;
    private LruCache<String, Bitmap> mCache;
    private int mWidth;
    private int mHeight;

    @Override
    public String getName() {
        return REACT_PHOTO_BROWSER;
    }

    @Override
    protected LinearLayout createViewInstance(ThemedReactContext reactContext) {
        mContext = reactContext;
        LinearLayout layout = (LinearLayout) LayoutInflater.from(reactContext).inflate(R.layout.browser_photo_layout, null);
        mViewPager = (ImgBrowserViewPager) layout.findViewById(R.id.img_browser_viewpager);
        DisplayMetrics dm = reactContext.getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;

        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 4;
        mCache = new LruCache<>(cacheSize);
        mViewPager.setAdapter(pagerAdapter);
        return layout;
    }

    PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mPathList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(true, mContext);
            photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            photoView.setTag(position);
            String path = mPathList.get(position).toString();
            if (path != null) {
                Bitmap bitmap = mCache.get(path);
                if (bitmap != null) {
                    photoView.setImageBitmap(bitmap);
                } else {
                    File file = new File(path);
                    if (file.exists()) {
                        bitmap = BitmapLoader.getBitmapFromFile(path, mWidth, mHeight);
                        if (bitmap != null) {
                            photoView.setImageBitmap(bitmap);
                            mCache.put(path, bitmap);
                        } else {
                            photoView.setImageResource(R.drawable.aurora_picture_not_found);
                        }
                    } else {
                        photoView.setImageResource(R.drawable.aurora_picture_not_found);
                    }
                }
            } else {
                photoView.setImageResource(R.drawable.aurora_picture_not_found);
            }
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            View view = (View) object;
            int currentPage = mViewPager.getCurrentItem();
            if (currentPage == (Integer) view.getTag()) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }
    };

    private void initCurrentItem() {
        mViewPager.getAdapter().notifyDataSetChanged();
        PhotoView photoView = new PhotoView(true, mContext);
        int position = mMsgIdList.indexOf(mClickMsgId);
        if (position != -1) {
            String path = mPathList.get(position).toString();
            if (path != null) {
                Bitmap bitmap = mCache.get(path);
                if (bitmap != null) {
                    photoView.setImageBitmap(bitmap);
                } else {
                    File file = new File(path);
                    if (file.exists()) {
                        bitmap = BitmapLoader.getBitmapFromFile(path, mWidth, mHeight);
                        if (bitmap != null) {
                            photoView.setImageBitmap(bitmap);
                            mCache.put(path, bitmap);
                        } else {
                            photoView.setImageResource(R.drawable.aurora_picture_not_found);
                        }
                    } else {
                        photoView.setImageResource(R.drawable.aurora_picture_not_found);
                    }
                }
            } else {
                photoView.setImageResource(R.drawable.aurora_picture_not_found);
            }
            mViewPager.setCurrentItem(position);
        }
    }

    @Override
    public @Nullable Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("unregister", 1);
    }

    @Override
    public void receiveCommand(LinearLayout root, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        Log.i(REACT_PHOTO_BROWSER, "Got command event, commandId: " + commandId);
        if (null == args) {
            Log.d(REACT_PHOTO_BROWSER, "Parameters should not be null");
            return;
        }
        switch (commandId){
            case RECEIVE_PATH_EVENT:
                mPathList = args.getArray(0).toArrayList();
                mMsgIdList = args.getArray(1).toArrayList();
                mClickMsgId = args.getString(2);
                initCurrentItem();
                break;
        }
    }
}
