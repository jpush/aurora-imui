package cn.jiguang.imui.messages;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.CircleImageView;
import cn.jiguang.imui.utils.DateFormatter;


public class VideoViewHolder<Message extends IMessage> extends BaseMessageViewHolder<Message>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private final TextView mTextDate;
    private final CircleImageView mImageAvatar;
    private final ImageView mImageCover;
    private final ImageView mImagePlay;
    private final TextView mTvDuration;

    public VideoViewHolder(View itemView, boolean isSender) {
        super(itemView);
        mTextDate = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mImageAvatar = (CircleImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        mImageCover = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_cover);
        mImagePlay = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_play);
        mTvDuration = (TextView) itemView.findViewById(R.id.aurora_tv_duration);
    }

    @Override
    public void onBind(final Message message) {
        mTextDate.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));

        boolean isAvatarExists = message.getUserInfo().getAvatar() != null
                && !message.getUserInfo().getAvatar().isEmpty();

        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(message.getContentFile(),
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

        if (mImageLoader != null) {
            if (isAvatarExists) {
                mImageLoader.loadImage(mImageAvatar, message.getUserInfo().getAvatar());
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

        android.view.ViewGroup.LayoutParams layoutParams = mImageAvatar.getLayoutParams();
        layoutParams.width = style.getAvatarWidth();
        layoutParams.height = style.getAvatarHeight();
        mImageAvatar.setLayoutParams(layoutParams);
    }
}