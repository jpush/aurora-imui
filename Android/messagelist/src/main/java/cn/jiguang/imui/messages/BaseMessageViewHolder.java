package cn.jiguang.imui.messages;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.ViewHolder;
import cn.jiguang.imui.commons.models.IMessage;

public abstract class BaseMessageViewHolder<MESSAGE extends IMessage>
        extends ViewHolder<MESSAGE> {

    protected Context mContext;

    protected float mDensity;
    protected int mPosition;
    protected boolean mIsSelected;
    protected ImageLoader mImageLoader;

    protected MsgListAdapter.OnMsgLongClickListener<MESSAGE> mMsgLongClickListener;
    protected MsgListAdapter.OnMsgClickListener<MESSAGE> mMsgClickListener;
    protected MsgListAdapter.OnAvatarClickListener<MESSAGE> mAvatarClickListener;
    protected MsgListAdapter.OnMsgResendListener<MESSAGE> mMsgResendListener;
    protected MediaPlayer mMediaPlayer;

    public BaseMessageViewHolder(View itemView) {
        super(itemView);
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}