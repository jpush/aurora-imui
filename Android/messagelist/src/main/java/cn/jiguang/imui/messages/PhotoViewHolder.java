package cn.jiguang.imui.messages;


import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.jiguang.imui.BuildConfig;
import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.RoundImageView;

public class PhotoViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private boolean mIsSender;
    private TextView mDateTv;
    private TextView mDisplayNameTv;
    private ImageView mPhotoIv;
    private RoundImageView mAvatarIv;
    private ProgressBar mSendingPb;
    private ImageButton mResendIb;



    public PhotoViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;
        mDateTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mPhotoIv = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_photo);
        mAvatarIv = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        if (mIsSender) {
            mSendingPb = (ProgressBar) itemView.findViewById(R.id.aurora_pb_msgitem_sending);
            mResendIb = (ImageButton) itemView.findViewById(R.id.aurora_ib_msgitem_resend);
            mDisplayNameTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_sender_display_name);
        } else {
            mDisplayNameTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_receiver_display_name);
        }
    }

    @Override
    public void onBind(final MESSAGE message) {
        if (message.getTimeString() != null) {
            mDateTv.setText(message.getTimeString());
        }
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null
                && !message.getFromUser().getAvatarFilePath().isEmpty();
        if (isAvatarExists && mImageLoader != null) {
            mImageLoader.loadAvatarImage(mAvatarIv, message.getFromUser().getAvatarFilePath());
        }

        if (mScroll) {
            mPhotoIv.setImageResource(R.drawable.aurora_picture_not_found);
        } else {
            mImageLoader.loadImage(mPhotoIv, message.getMediaFilePath());
        }


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
        if (mDisplayNameTv.getVisibility() == View.VISIBLE) {
            mDisplayNameTv.setText(message.getFromUser().getDisplayName());
        }
        if (mIsSender) {
            switch (message.getMessageStatus()) {
                case SEND_GOING:
                    Log.i("PhotoViewHolder", "sending image, progress: " + message.getProgress());
                    mSendingPb.setVisibility(View.VISIBLE);
                    mResendIb.setVisibility(View.GONE);
                    break;
                case SEND_FAILED:
                    mResendIb.setVisibility(View.VISIBLE);
                    mSendingPb.setVisibility(View.GONE);
                    mResendIb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mMsgStatusViewClickListener != null) {
                                mMsgStatusViewClickListener.onStatusViewClick(message);
                            }
                        }
                    });
                    Log.i("PhotoViewHolder", "send image failed");
                    break;
                case SEND_SUCCEED:
                    mSendingPb.setVisibility(View.GONE);
                    mResendIb.setVisibility(View.GONE);
                    Log.i("PhotoViewHolder", "send image succeed");
                    break;
            }
        }
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        if (mIsSender) {
            mPhotoIv.setBackground(style.getSendPhotoMsgBg());
            if (style.getSendingProgressDrawable() != null) {
                mSendingPb.setProgressDrawable(style.getSendingProgressDrawable());
            }
            if (style.getSendingIndeterminateDrawable() != null) {
                mSendingPb.setIndeterminateDrawable(style.getSendingIndeterminateDrawable());
            }
            if (style.getShowSenderDisplayName() == 1) {
                mDisplayNameTv.setVisibility(View.VISIBLE);
            }
        } else {
            if (style.getShowReceiverDisplayName() == 1) {
                mDisplayNameTv.setVisibility(View.VISIBLE);
            }
            mPhotoIv.setBackground(style.getReceivePhotoMsgBg());
        }
        android.view.ViewGroup.LayoutParams layoutParams = mAvatarIv.getLayoutParams();
        layoutParams.width = style.getAvatarWidth();
        layoutParams.height = style.getAvatarHeight();
        mAvatarIv.setLayoutParams(layoutParams);
        mAvatarIv.setBorderRadius(style.getAvatarRadius());
    }

}
