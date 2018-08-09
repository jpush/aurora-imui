package cn.jiguang.imui.messages;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.utils.BitmapCache;
import cn.jiguang.imui.view.RoundImageView;
import cn.jiguang.imui.view.RoundTextView;

public class VideoViewHolder<Message extends IMessage> extends BaseMessageViewHolder<Message>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private final RoundTextView mDateTv;
    private final RoundImageView mImageAvatar;
    private TextView mDisplayNameTv;
    private final ImageView mImageCover;
    private final ImageView mImagePlay;
    private final TextView mTvDuration;
    private boolean mIsSender;
    private ProgressBar mSendingPb;
    private ImageButton mResendIb;

    public VideoViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;
        mDateTv = (RoundTextView) itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mImageAvatar = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        mImageCover = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_cover);
        mImagePlay = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_play);
        mTvDuration = (TextView) itemView.findViewById(R.id.aurora_tv_duration);
        if (isSender) {
            mSendingPb = (ProgressBar) itemView.findViewById(R.id.aurora_pb_msgitem_sending);
            mResendIb = (ImageButton) itemView.findViewById(R.id.aurora_ib_msgitem_resend);
            mDisplayNameTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_sender_display_name);
        } else {
            mDisplayNameTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_receiver_display_name);
        }
    }

    @Override
    public void onBind(final Message message) {
        String timeString = message.getTimeString();
        mDateTv.setVisibility(View.VISIBLE);
        if (timeString != null && !TextUtils.isEmpty(timeString)) {
            mDateTv.setText(timeString);
        } else {
            mDateTv.setVisibility(View.GONE);
        }
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null
                && !message.getFromUser().getAvatarFilePath().isEmpty();
        if (mImageLoader != null) {
            mImageLoader.loadVideo(mImageCover, message.getMediaFilePath());
        } else {
            if (BitmapCache.getInstance().getBitmapFromMemCache(message.getMediaFilePath()) == null) {
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(message.getMediaFilePath(),
                        MediaStore.Images.Thumbnails.MINI_KIND);
                BitmapCache.getInstance().setBitmapCache(message.getMediaFilePath(), thumb);
            }
            mImageCover.setImageBitmap(BitmapCache.getInstance().getBitmapFromMemCache(message.getMediaFilePath()));
        }
        mImageCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMsgClickListener.onMessageClick(message);
            }
        });
        mImageCover.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                mMsgLongClickListener.onMessageLongClick(view, message);
                return false;
            }
        });

        String durationStr = String.format(Locale.CHINA, "%02d:%02d", TimeUnit.SECONDS.toMinutes(message.getDuration()),
                TimeUnit.SECONDS.toSeconds(message.getDuration()));
        Log.d("VideoViewHolder", "duration: " + message.getDuration() + " durationStr " + durationStr);
        mTvDuration.setText(durationStr);
        if (mDisplayNameTv.getVisibility() == View.VISIBLE) {
            mDisplayNameTv.setText(message.getFromUser().getDisplayName());
        }
        if (mIsSender) {
            switch (message.getMessageStatus()) {
            case SEND_GOING:
                mSendingPb.setVisibility(View.VISIBLE);
                mResendIb.setVisibility(View.GONE);
                break;
            case SEND_FAILED:
                mSendingPb.setVisibility(View.GONE);
                mResendIb.setVisibility(View.VISIBLE);
                mResendIb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mMsgStatusViewClickListener != null) {
                            mMsgStatusViewClickListener.onStatusViewClick(message);
                        }
                    }
                });
                break;
            case SEND_SUCCEED:
                mSendingPb.setVisibility(View.GONE);
                mResendIb.setVisibility(View.GONE);
                break;
            }
        }

        if (mImageLoader != null) {
            if (isAvatarExists) {
                mImageLoader.loadAvatarImage(mImageAvatar, message.getFromUser().getAvatarFilePath());
            }
        }

        mImageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAvatarClickListener != null) {
                    mAvatarClickListener.onAvatarClick(message);
                }
            }
        });
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        mDateTv.setPadding(style.getDatePaddingLeft(), style.getDatePaddingTop(), style.getDatePaddingRight(),
                style.getDatePaddingBottom());
        mDateTv.setBgCornerRadius(style.getDateBgCornerRadius());
        mDateTv.setBgColor(style.getDateBgColor());
        if (mIsSender) {
            if (style.getSendingProgressDrawable() != null) {
                mSendingPb.setProgressDrawable(style.getSendingProgressDrawable());
            }
            if (style.getSendingIndeterminateDrawable() != null) {
                mSendingPb.setIndeterminateDrawable(style.getSendingIndeterminateDrawable());
            }
            if (style.getShowSenderDisplayName()) {
                mDisplayNameTv.setVisibility(View.VISIBLE);
            } else {
                mDisplayNameTv.setVisibility(View.GONE);
            }
        } else {
            if (style.getShowReceiverDisplayName()) {
                mDisplayNameTv.setVisibility(View.VISIBLE);
            } else {
                mDisplayNameTv.setVisibility(View.GONE);
            }
        }
        mTvDuration.setTextColor(style.getVideoDurationTextColor());
        mTvDuration.setTextSize(style.getVideoDurationTextSize());
        mDisplayNameTv.setTextSize(style.getDisplayNameTextSize());
        mDisplayNameTv.setTextColor(style.getDisplayNameTextColor());
        mDisplayNameTv.setPadding(style.getDisplayNamePaddingLeft(), style.getDisplayNamePaddingTop(),
                style.getDisplayNamePaddingRight(), style.getDisplayNamePaddingBottom());
        mDisplayNameTv.setEms(style.getDisplayNameEmsNumber());
        android.view.ViewGroup.LayoutParams layoutParams = mImageAvatar.getLayoutParams();
        layoutParams.width = style.getAvatarWidth();
        layoutParams.height = style.getAvatarHeight();
        mImageAvatar.setLayoutParams(layoutParams);
        mImageAvatar.setBorderRadius(style.getAvatarRadius());
    }
}