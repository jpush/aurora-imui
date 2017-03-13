package cn.jiguang.imui.messages;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.FileItem;
import cn.jiguang.imui.utils.DisplayUtil;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context mContext;

    private List<FileItem> mPhotos;
    private List<String> mSendFiles;

    private SparseBooleanArray mSelectedItems;

    private OnFileSelectedListener mListener;

    private int mPhotoSide;    // 图片边长

    public PhotoAdapter(List<FileItem> list, int height) {
        mSelectedItems = new SparseBooleanArray();
        if (list == null) {
            mPhotos = new ArrayList<>();
        } else {
            mPhotos = list;
        }
        mPhotoSide = height;
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

    public void resetCheckedState() {
        mSendFiles.clear();
        mSelectedItems.clear();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        FrameLayout photoSelectLayout = (FrameLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.item_photo_select, parent, false);
        PhotoViewHolder holder = new PhotoViewHolder(photoSelectLayout);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        if (holder.container.getMeasuredWidth() != mPhotoSide) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(mPhotoSide, mPhotoSide);
            layoutParams.rightMargin = DisplayUtil.dp2px(mContext, 8);
            holder.container.setLayoutParams(layoutParams);
        }

        FileItem photoFile = mPhotos.get(position);
        String path = photoFile.getFilePath();
        File photo = new File(path);
        Glide.with(mContext)
                .load(photo)
                .placeholder(R.drawable.jmui_picture_not_found)
                .crossFade()
                .into(holder.ivPhoto);

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.ivSelect.getVisibility() == GONE && !mSelectedItems.get(position)) {
                    mSelectedItems.put(position, true);

                    holder.ivSelect.setVisibility(VISIBLE);
                    holder.ivShadow.setVisibility(VISIBLE);
                    addSelectedAnimation(holder.ivPhoto, holder.ivShadow);

                    mSendFiles.add(mPhotos.get(position).getFilePath());
                    if (mListener != null) {
                        mListener.onFileSelected();
                    }
                } else {
                    mSelectedItems.delete(position);

                    holder.ivSelect.setVisibility(GONE);
                    holder.ivShadow.setVisibility(GONE);
                    addDeselectedAnimation(holder.ivPhoto, holder.ivShadow);

                    mSendFiles.remove(mPhotos.get(position).getFilePath());
                    if (mListener != null) {
                        mListener.onFileDeselected();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    private void addDeselectedAnimation(View... views) {
        List<Animator> valueAnimators = new ArrayList<>();
        for (View v : views) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 1.0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 1.0f);

            valueAnimators.add(scaleX);
            valueAnimators.add(scaleY);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(valueAnimators);
        set.setDuration(150);
        set.start();
    }

    private void addSelectedAnimation(View... views) {
        List<Animator> valueAnimators = new ArrayList<>();
        for (View v : views) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(v, "scaleX", 0.90f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(v, "scaleY", 0.90f);

            valueAnimators.add(scaleX);
            valueAnimators.add(scaleY);
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(valueAnimators);
        set.setDuration(150);
        set.start();
    }

    public interface OnFileSelectedListener {

        void onFileSelected();

        void onFileDeselected();
    }

    public static final class PhotoViewHolder extends RecyclerView.ViewHolder {

        public FrameLayout container;
        public ImageView ivPhoto;
        public ImageView ivShadow;
        public ImageView ivSelect;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            container = (FrameLayout) itemView;
            ivPhoto = (ImageView) itemView.findViewById(R.id.item_photo_iv);
            ivShadow = (ImageView) itemView.findViewById(R.id.item_shadow_iv);
            ivSelect = (ImageView) itemView.findViewById(R.id.checked_iv);
        }
    }
}
