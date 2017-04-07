package cn.jiguang.imui.messages;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private TextView mTextDate;
    private CircleImageView mImageAvatar;
    private ImageView mImageCover;
    private VideoPlayerView mVideoView;

    private final VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData currentItemMetaData) {

        }
    });

    public VideoViewHolder(View itemView, boolean isSender) {
        super(itemView);
        mTextDate = (TextView) itemView.findViewById(R.id.date_tv);
        mImageAvatar = (CircleImageView) itemView.findViewById(R.id.avatar_iv);
        mImageCover = (ImageView) itemView.findViewById(R.id.image_msglist_cover);

        if (isSender) {
            mVideoView = (VideoPlayerView) itemView.findViewById(R.id.video_msglist_send);
        } else {
            mVideoView = (VideoPlayerView) itemView.findViewById(R.id.video_msglist_receive);
        }
    }

    @Override
    public void onBind(final Message message) {
        mTextDate.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));

        boolean isAvatarExists = message.getUserInfo().getAvatar() != null
                && !message.getUserInfo().getAvatar().isEmpty();

        mVideoPlayerManager.playNewVideo(null, mVideoView, message.getContentFile());

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