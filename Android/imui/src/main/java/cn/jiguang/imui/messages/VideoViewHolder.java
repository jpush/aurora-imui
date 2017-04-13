package cn.jiguang.imui.messages;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.utils.CircleImageView;
import cn.jiguang.imui.utils.DateFormatter;


public class VideoViewHolder<Message extends IMessage> extends BaseMessageViewHolder<Message>
        implements MsgListAdapter.DefaultMessageViewHolder {

    public final VideoPlayerView mVideoView;

    private final TextView mTextDate;
    private final CircleImageView mImageAvatar;
    private final ImageView mImageCover;

    public VideoViewHolder(View itemView, boolean isSender) {
        super(itemView);
        mTextDate = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mImageAvatar = (CircleImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        mImageCover = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_cover);

        if (isSender) {
            mVideoView = (VideoPlayerView) itemView.findViewById(R.id.aurora_vpv_msgitem_send);
        } else {
            mVideoView = (VideoPlayerView) itemView.findViewById(R.id.aurora_vpv_msgitem_video_player);
        }
    }

    @Override
    public void onBind(final Message message) {
        mTextDate.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));

        boolean isAvatarExists = message.getUserInfo().getAvatar() != null
                && !message.getUserInfo().getAvatar().isEmpty();

        Glide.with(itemView.getContext())
                .load(message.getContentFile())
                .centerCrop()
                .into(mImageCover);

        mVideoView.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener() {
            @Override
            public void onVideoPreparedMainThread() {
                mImageCover.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVideoCompletionMainThread() {
                mImageCover.setVisibility(View.VISIBLE);
            }
        });

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

        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMsgClickListener != null) {
                    mMsgClickListener.onMessageClick(message);
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