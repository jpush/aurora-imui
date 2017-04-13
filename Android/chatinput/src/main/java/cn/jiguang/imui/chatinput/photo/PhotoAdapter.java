package cn.jiguang.imui.chatinput.photo;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.jiguang.imui.chatinput.utils.DisplayUtil;
import cn.jiguang.imui.chatinput.utils.FileItem;
import cn.jiguang.imui.chatinput.R;
import cn.jiguang.imui.chatinput.utils.VideoItem;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private Context mContext;

    private List<FileItem> mPhotos;
    private List<FileItem> mSendFiles;

    private List<Integer> mSelectedItems;

    private OnFileSelectedListener mListener;

    private int mPhotoSide;    // length of side

    public PhotoAdapter(List<FileItem> list, int height) {
        mSelectedItems = new ArrayList<>();
        if (list == null) {
            mPhotos = new ArrayList<>();
        } else {
            mPhotos = list;
        }
        mPhotoSide = height;
    }

    public List<FileItem> getSelectedFiles() {
        return mSendFiles;
    }

    public void setOnPhotoSelectedListener(OnFileSelectedListener listener) {
        mListener = listener;
    }

    public void setSelectedFiles(List<FileItem> list) {
        mSendFiles = list;
    }

    public void resetCheckedState() {
        mSendFiles.clear();
        for (int i = 0; i < mSelectedItems.size(); i++) {
            mSelectedItems.remove(i);
            notifyDataSetChanged();
        }
        mSelectedItems.clear();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        View layout = LayoutInflater.from(mContext)
                .inflate(R.layout.item_photo_select, parent, false);
        return new PhotoViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
        if (holder.container.getMeasuredWidth() != mPhotoSide) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(mPhotoSide, mPhotoSide);
            layoutParams.rightMargin = DisplayUtil.dp2px(mContext, 8);
            holder.container.setLayoutParams(layoutParams);
        }

        FileItem item = mPhotos.get(position);
        String path = item.getFilePath();
        File file = new File(path);
        Glide.with(mContext)
                .load(file)
                .placeholder(R.drawable.aurora_picture_not_found)
                .crossFade()
                .into(holder.ivPhoto);

        if (mSelectedItems.contains(position)) {    // Current photo is selected
            holder.ivTick.setVisibility(VISIBLE);
            holder.ivShadow.setVisibility(VISIBLE);
            addSelectedAnimation(holder.ivPhoto, holder.ivShadow);

        } else if (holder.ivTick.getVisibility() == View.VISIBLE) { // Selected before, now have not been selected
            holder.ivTick.setVisibility(GONE);
            holder.ivShadow.setVisibility(GONE);
            addDeselectedAnimation(holder.ivPhoto, holder.ivShadow);
        }

        final FileItem.Type fileItem = item.getType();

        if (fileItem == FileItem.Type.Video) {
            holder.tvDuration.setVisibility(View.VISIBLE);

            long duration = ((VideoItem) item).getDuration();
            String durationStr = String.format(Locale.CHINA, "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration));

            holder.tvDuration.setText(durationStr);
        }

        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int p = holder.getAdapterPosition();

                if (holder.ivTick.getVisibility() == GONE && !mSelectedItems.contains(p)) {
                    mSelectedItems.add(p);
                    mSendFiles.add(mPhotos.get(p));

                    holder.ivTick.setVisibility(VISIBLE);
                    holder.ivShadow.setVisibility(VISIBLE);

                    addSelectedAnimation(holder.container);

                    if (mListener != null) {
                        mListener.onFileSelected();
                    }
                } else {
                    mSelectedItems.remove(Integer.valueOf(p));
                    mSendFiles.remove(mPhotos.get(p));

                    holder.ivTick.setVisibility(GONE);
                    holder.ivShadow.setVisibility(GONE);

                    addDeselectedAnimation(holder.container);

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

    @Override
    public int getItemViewType(int position) {
        return mPhotos.get(position).getType().getCode();
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

    static final class PhotoViewHolder extends RecyclerView.ViewHolder {

        View container;
        TextView tvDuration;
        ImageView ivPhoto;
        ImageView ivShadow;
        ImageView ivTick;

        PhotoViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            tvDuration = (TextView) itemView.findViewById(R.id.text_photoselect_duration);
            ivPhoto = (ImageView) itemView.findViewById(R.id.image_photoselect_photo);
            ivShadow = (ImageView) itemView.findViewById(R.id.image_photoselect_shadow);
            ivTick = (ImageView) itemView.findViewById(R.id.image_photoselect_tick);
        }
    }
}
