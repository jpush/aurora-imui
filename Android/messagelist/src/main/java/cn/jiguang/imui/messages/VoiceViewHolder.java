package cn.jiguang.imui.messages;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;

import cn.jiguang.imui.BuildConfig;
import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.RoundImageView;
import cn.jiguang.imui.view.RoundTextView;

public class VoiceViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder, ViewHolderController.ReplayVoiceListener {

    private boolean mIsSender;
    private TextView mMsgTv;
    private RoundTextView mDateTv;
    private TextView mDisplayNameTv;
    private RoundImageView mAvatarIv;
    private ImageView mVoiceIv;
    private TextView mLengthTv;
    private ImageView mUnreadStatusIv;
    private ProgressBar mSendingPb;
    private ImageButton mResendIb;
    private boolean mSetData = false;
    private AnimationDrawable mVoiceAnimation;
    private FileInputStream mFIS;
    private int mSendDrawable;
    private int mReceiveDrawable;
    private int mPlaySendAnim;
    private int mPlayReceiveAnim;
    private ViewHolderController mController;

    public VoiceViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;
        mMsgTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_message);
        mDateTv = (RoundTextView) itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mAvatarIv = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        mVoiceIv = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_voice_anim);
        mLengthTv = (TextView) itemView.findViewById(R.id.aurora_tv_voice_length);
        if (!isSender) {
            mUnreadStatusIv = (ImageView) itemView.findViewById(R.id.aurora_iv_msgitem_read_status);
            mDisplayNameTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_receiver_display_name);
        } else {
            mSendingPb = (ProgressBar) itemView.findViewById(R.id.aurora_pb_msgitem_sending);
            mDisplayNameTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_sender_display_name);
        }
        mResendIb = (ImageButton) itemView.findViewById(R.id.aurora_ib_msgitem_resend);
        mController = ViewHolderController.getInstance();
        mController.setReplayVoiceListener(this);
    }

    @Override
    public void onBind(final MESSAGE message) {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        String timeString = message.getTimeString();
        mDateTv.setVisibility(View.VISIBLE);
        if (timeString != null && !TextUtils.isEmpty(timeString)) {
            mDateTv.setText(timeString);
        } else {
            mDateTv.setVisibility(View.GONE);
        }
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null
                && !message.getFromUser().getAvatarFilePath().isEmpty();
        if (isAvatarExists && mImageLoader != null) {
            mImageLoader.loadAvatarImage(mAvatarIv, message.getFromUser().getAvatarFilePath());
        }
        long duration = message.getDuration();
        String lengthStr = duration + mContext.getString(R.string.aurora_symbol_second);
        int width = (int) (-0.04 * duration * duration + 4.526 * duration + 75.214);
        mMsgTv.setWidth((int) (width * mDensity));
        mLengthTv.setText(lengthStr);
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
        } else {
            switch (message.getMessageStatus()) {
            case RECEIVE_FAILED:
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
            case RECEIVE_SUCCEED:
                mResendIb.setVisibility(View.GONE);
                break;
            }
        }

        mMsgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMsgClickListener != null) {
                    mMsgClickListener.onMessageClick(message);
                }

                // stop animation whatever this time is play or pause audio
                // if (mVoiceAnimation != null) {
                // mVoiceAnimation.stop();
                // mVoiceAnimation = null;
                // }
                mController.notifyAnimStop();
                mController.setMessage(message);
                if (mIsSender) {
                    mVoiceIv.setImageResource(mPlaySendAnim);
                } else {
                    mVoiceIv.setImageResource(mPlayReceiveAnim);
                }
                mVoiceAnimation = (AnimationDrawable) mVoiceIv.getDrawable();
                mController.addView(getAdapterPosition(), mVoiceIv);
                // If audio is playing, pause
                Log.e("VoiceViewHolder",
                        "MediaPlayer playing " + mMediaPlayer.isPlaying() + "now position " + getAdapterPosition());
                if (mController.getLastPlayPosition() == getAdapterPosition()) {
                    if (mMediaPlayer.isPlaying()) {
                        pauseVoice();
                        mVoiceAnimation.stop();
                        if (mIsSender) {
                            mVoiceIv.setImageResource(mSendDrawable);
                        } else {
                            mVoiceIv.setImageResource(mReceiveDrawable);
                        }
                    } else if (mSetData) {
                        mMediaPlayer.start();
                        mVoiceAnimation.start();
                    } else {
                        playVoice(getAdapterPosition(), message);
                    }
                    // Start playing audio
                } else {
                    playVoice(getAdapterPosition(), message);
                }
            }
        });

        mMsgTv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mMsgLongClickListener != null) {
                    mMsgLongClickListener.onMessageLongClick(view, message);
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.w("MsgListAdapter", "Didn't set long click listener! Drop event.");
                    }
                }
                return true;
            }
        });

        mAvatarIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAvatarClickListener != null) {
                    mAvatarClickListener.onAvatarClick(message);
                }
            }
        });
    }

    public void playVoice(int position, MESSAGE message) {
        mController.setLastPlayPosition(position, mIsSender);
        try {
            mMediaPlayer.reset();
            mFIS = new FileInputStream(message.getMediaFilePath());
            mMediaPlayer.setDataSource(mFIS.getFD());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVoiceAnimation.start();
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mVoiceAnimation.stop();
                    mp.reset();
                    mSetData = false;
                    if (mIsSender) {
                        mVoiceIv.setImageResource(mSendDrawable);
                    } else {
                        mVoiceIv.setImageResource(mReceiveDrawable);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (mFIS != null) {
                    mFIS.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void pauseVoice() {
        mMediaPlayer.pause();
        mSetData = true;
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        mDateTv.setPadding(style.getDatePaddingLeft(), style.getDatePaddingTop(), style.getDatePaddingRight(),
                style.getDatePaddingBottom());
        mDateTv.setBgCornerRadius(style.getDateBgCornerRadius());
        mDateTv.setBgColor(style.getDateBgColor());
        mSendDrawable = style.getSendVoiceDrawable();
        mReceiveDrawable = style.getReceiveVoiceDrawable();
        mController.setDrawable(mSendDrawable, mReceiveDrawable);
        mPlaySendAnim = style.getPlaySendVoiceAnim();
        mPlayReceiveAnim = style.getPlayReceiveVoiceAnim();
        if (mIsSender) {
            mVoiceIv.setImageResource(mSendDrawable);
            mMsgTv.setBackground(style.getSendBubbleDrawable());
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
            mVoiceIv.setImageResource(mReceiveDrawable);
            mMsgTv.setBackground(style.getReceiveBubbleDrawable());
            if (style.getShowReceiverDisplayName()) {
                mDisplayNameTv.setVisibility(View.VISIBLE);
            } else {
                mDisplayNameTv.setVisibility(View.GONE);
            }
        }
        mDisplayNameTv.setTextSize(style.getDisplayNameTextSize());
        mDisplayNameTv.setTextColor(style.getDisplayNameTextColor());
        mDisplayNameTv.setPadding(style.getDisplayNamePaddingLeft(), style.getDisplayNamePaddingTop(),
                style.getDisplayNamePaddingRight(), style.getDisplayNamePaddingBottom());
        mDisplayNameTv.setEms(style.getDisplayNameEmsNumber());
        android.view.ViewGroup.LayoutParams layoutParams = mAvatarIv.getLayoutParams();
        layoutParams.width = style.getAvatarWidth();
        layoutParams.height = style.getAvatarHeight();
        mAvatarIv.setLayoutParams(layoutParams);
        mAvatarIv.setBorderRadius(style.getAvatarRadius());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void replayVoice() {
        pauseVoice();
        mController.notifyAnimStop();
        if (mIsSender) {
            mVoiceIv.setImageResource(mPlaySendAnim);
        } else {
            mVoiceIv.setImageResource(mPlayReceiveAnim);
        }
        mVoiceAnimation = (AnimationDrawable) mVoiceIv.getBackground();
        playVoice(mController.getLastPlayPosition(), (MESSAGE) mController.getMessage());
    }
}