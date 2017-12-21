package cn.jiguang.imui.messages;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

import java.util.List;

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
    protected MsgListAdapter.OnMsgStatusViewClickListener<MESSAGE> mMsgStatusViewClickListener;
    protected MediaPlayer mMediaPlayer;
    protected boolean mScroll;
    protected List<MsgListAdapter.Wrapper> mData;

    public BaseMessageViewHolder(View itemView) {
        super(itemView);
    }
}