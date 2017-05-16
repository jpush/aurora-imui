package cn.jiguang.imui.messages;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.CircleImageView;


public class VideoViewHolder<Message extends IMessage> extends BaseMessageViewHolder<Message>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private final TextView mTextDate;
    private final CircleImageView mImageAvatar;
    private final ImageView mImageCover;
    private final ImageView mImagePlay;
    private final TextView mTvDuration;
    private boolean mIsSender;
    private ProgressBar mSendingPb;
    private ImageButton mResendIb;

    public VideoViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;
        mTextDate = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mImageAvatar = (CircleImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        mImageCover = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_cover);
        mImagePlay = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_play);
        mTvDuration = (TextView) itemView.findViewById(R.id.aurora_tv_duration);
        if (isSender) {
            mSendingPb = (ProgressBar) itemView.findViewById(R.id.aurora_pb_msgitem_sending);
            mResendIb = (ImageButton) itemView.findViewById(R.id.aurora_ib_msgitem_resend);
        }
    }

    @Override
    public void onBind(final Message message) {
        if (message.getTimeString() != null) {
            mTextDate.setText(message.getTimeString());
        }
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null
                && !message.getFromUser().getAvatarFilePath().isEmpty();

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(message.getMediaFilePath(),
                MediaStore.Images.Thumbnails.MINI_KIND);
        mImageCover.setImageBitmap(thumb);
        mImageCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMsgClickListener.onMessageClick(message);
            }
        });
        mImageCover.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                mMsgLongClickListener.onMessageLongClick(message);
                return false;
            }
        });

        String durationStr = String.format(Locale.CHINA, "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(message.getDuration()),
                TimeUnit.MILLISECONDS.toSeconds(message.getDuration()));
        mTvDuration.setText(durationStr);

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
                            if (mMsgResendListener != null) {
                                mMsgResendListener.onMessageResend(message);
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
        mTextDate.setTextSize(style.getDateTextSize());
        mTextDate.setTextColor(style.getDateTextColor());
        if (mIsSender) {
            if (style.getSendingProgressDrawable() != null) {
                mSendingPb.setProgressDrawable(style.getSendingProgressDrawable());
            }
            if (style.getSendingIndeterminateDrawable() != null) {
                mSendingPb.setIndeterminateDrawable(style.getSendingIndeterminateDrawable());
            }
        }
        android.view.ViewGroup.LayoutParams layoutParams = mImageAvatar.getLayoutParams();
        layoutParams.width = style.getAvatarWidth();
        layoutParams.height = style.getAvatarHeight();
        mImageAvatar.setLayoutParams(layoutParams);
    }
}