package cn.jiguang.imui.messages;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jiguang.imui.BuildConfig;
import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;

public class PhotoViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private TextView mDateTv;
    private ImageView mPhotoIv;
    private ImageView mAvatarIv;
    private boolean mIsSender;

    public PhotoViewHolder(View itemView, boolean isSender) {
        super(itemView);
        mIsSender = isSender;
        mDateTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mPhotoIv = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_photo);
        mAvatarIv = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
    }

    @Override
    public void onBind(final MESSAGE message) {
        if (message.getTimeString() != null) {
            mDateTv.setText(message.getTimeString());
        }

        mImageLoader.loadAvatarImage(mAvatarIv, message.getFromUser().getAvatarFilePath());
        mImageLoader.loadImage(mPhotoIv, message.getMediaFilePath());

        mAvatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAvatarClickListener != null) {
                    mAvatarClickListener.onAvatarClick(message);
                }
            }
        });

        mPhotoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMsgClickListener != null) {
                    mMsgClickListener.onMessageClick(message);
                }
            }
        });

        mPhotoIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mMsgLongClickListener != null) {
                    mMsgLongClickListener.onMessageLongClick(message);
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.w("MsgListAdapter", "Didn't set long click listener! Drop event.");
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        if (mIsSender) {
            mPhotoIv.setBackground(style.getSendPhotoMsgBg());
        } else {
            mPhotoIv.setBackground(style.getReceivePhotoMsgBg());
        }
        ViewGroup.LayoutParams layoutParams = mAvatarIv.getLayoutParams();
        layoutParams.width = style.getAvatarWidth();
        layoutParams.height = style.getAvatarHeight();
        mAvatarIv.setLayoutParams(layoutParams);
    }
}