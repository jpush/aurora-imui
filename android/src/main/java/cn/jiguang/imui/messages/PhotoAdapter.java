package cn.jiguang.imui.messages;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.FileItem;
import cn.jiguang.imui.utils.ImgBrowserViewPager;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PhotoAdapter extends PagerAdapter {

    private ImgBrowserViewPager mImgViewPager;
    private List<FileItem> mPhotos = new ArrayList<>();
    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();
    private List<String> mSendFiles;
    private Bitmap mBitmap;
    private OnFileSelectedListener mListener;

    public PhotoAdapter(ImgBrowserViewPager viewPager, List<FileItem> list) {
        this.mImgViewPager = viewPager;
        this.mPhotos = list;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public float getPageWidth(int position) {
        return (float) 0.8;
    }

    /**
     * 点击某张图片预览时，系统自动调用此方法加载这张图片左右视图（如果有的话）
     */
    @Override
    public View instantiateItem(final ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo_select, container, false);
        final ImageView photoView = (ImageView) view.findViewById(R.id.item_photo_iv);
        final ImageView shadow = (ImageView) view.findViewById(R.id.item_shadow_iv);
        final ImageView checkedIcon = (ImageView) view.findViewById(R.id.checked_iv);
        photoView.setTag(position);
        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String path = mPhotos.get(position).getFilePath();
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                mBitmap = decodeFile(file);
                if (mBitmap != null) {
                    photoView.setImageBitmap(mBitmap);
                } else {
                    photoView.setImageResource(R.drawable.jmui_picture_not_found);
                }
            }
        } else {
            photoView.setImageResource(R.drawable.jmui_picture_not_found);
        }

        photoView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (checkedIcon.getVisibility() == GONE && !mSelectedItems.get(position)) {
                    mSelectedItems.put(position, true);
                    checkedIcon.setVisibility(VISIBLE);
                    addSelectedAnimation(photoView);
                    addSelectedAnimation(shadow);
                    shadow.setVisibility(VISIBLE);
                    mSendFiles.add(mPhotos.get(position).getFilePath());
                    if (mListener != null) {
                        mListener.onFileSelected();
                    }
                } else {
                    addDeselectedAnimation(photoView);
                    checkedIcon.setVisibility(GONE);
                    shadow.setVisibility(GONE);
                    mSelectedItems.delete(position);
                    mSendFiles.remove(mPhotos.get(position).getFilePath());
                    if (mListener != null) {
                        mListener.onFileDeselected();
                    }
                }
            }
        });

        container.addView(view, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        View view = (View) object;
        int currentPage = mImgViewPager.getCurrentItem();
        if (currentPage == (Integer) view.getTag()) {
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private Bitmap decodeFile(File f) {
        Bitmap b = null;
        try {
            //Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, options);
            fis.close();

            int width = options.outWidth;
            int height = options.outHeight;
            int ratio = 0;
            //如果宽度大于高度，交换宽度和高度
            if (width > height) {
                int temp = width;
                width = height;
                height = temp;
            }
            //计算取样比例
            int sampleRatio = Math.max(width / 900, height / 1600);

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = sampleRatio;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    private void addDeselectedAnimation(ImageView view) {
        float[] vaules = new float[]{1.0f, 1.1f, 1.2f, 1.3f, 1.2f, 1.1f, 1.0f, 0.9f, 0.8f, 0.7f, 0.75f,
                0.80f, 0.85f, 0.9f, 0.95f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    private void addSelectedAnimation(View view) {
        float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 0.8f, 0.7f, 0.6f, 0.65f, 0.7f,
                0.75f, 0.80f, 0.85f, 0.9f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    public List<String> getSelectedFiles() {
        return mSendFiles;
    }

    public void setOnPhotoSelectedListener(OnFileSelectedListener listener) {
        mListener = listener;
    }

    public void setSelectedFiles(List<String> list) {
        mSendFiles = list;
    }

    public interface OnFileSelectedListener {

        void onFileSelected();

        void onFileDeselected();
    }


}
