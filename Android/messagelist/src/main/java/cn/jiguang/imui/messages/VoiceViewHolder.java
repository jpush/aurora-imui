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
        implements MsgListAdapter.DefaultMessageViewHolder {

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
    private VoicePlaybackState mPlaybackState;
    private PlaybackChangeListener mPlaybackChangeListener;
    private String mBoundMessageId;

    interface PlaybackChangeListener {
        void onPlaybackChanged(String previousMessageId, String currentMessageId);
    }

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
    }

    @Override
    public void onBind(final MESSAGE message) {
        mBoundMessageId = message.getMsgId();
        renderPlaybackState(message);
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

                String messageId = message.getMsgId();
                if (mPlaybackState.isCurrent(messageId)) {
                    if (mMediaPlayer.isPlaying()) {
                        pauseVoice();
                        mPlaybackState.pause(messageId);
                        notifyPlaybackChanged(messageId, messageId);
                        renderPlaybackState(message);
                    } else if (mSetData) {
                        mMediaPlayer.start();
                        mPlaybackState.play(messageId);
                        notifyPlaybackChanged(messageId, messageId);
                        renderPlaybackState(message);
                    } else {
                        playVoice(getAdapterPosition(), message);
                    }
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

    public void playVoice(int position, final MESSAGE message) {
        String previousMessageId = mPlaybackState.getMessageId();
        try {
            mMediaPlayer.reset();
            mFIS = new FileInputStream(message.getMediaFilePath());
            mMediaPlayer.setDataSource(mFIS.getFD());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mSetData = false;
                    mPlaybackState.stop(message.getMsgId());
                    notifyPlaybackChanged(message.getMsgId(), null);
                    if (message.getMsgId().equals(mBoundMessageId)) {
                        renderPlaybackState(message);
                    }
                }
            });
            mMediaPlayer.prepare();
            mPlaybackState.play(message.getMsgId());
            notifyPlaybackChanged(previousMessageId, message.getMsgId());
            renderPlaybackState(message);
        } catch (IOException e) {
            mMediaPlayer.reset();
            mPlaybackState.stop(previousMessageId);
            notifyPlaybackChanged(previousMessageId, null);
            e.printStackTrace();
        } catch (IllegalStateException e) {
            mPlaybackState.stop(previousMessageId);
            notifyPlaybackChanged(previousMessageId, null);
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

    void setPlaybackState(VoicePlaybackState playbackState, PlaybackChangeListener listener) {
        mPlaybackState = playbackState;
        mPlaybackChangeListener = listener;
    }

    private void notifyPlaybackChanged(String previousMessageId, String currentMessageId) {
        if (mPlaybackChangeListener != null) {
            mPlaybackChangeListener.onPlaybackChanged(previousMessageId, currentMessageId);
        }
    }

    private void renderPlaybackState(MESSAGE message) {
        if (mPlaybackState == null || mVoiceIv == null) {
            return;
        }
        if (mVoiceAnimation != null) {
            mVoiceAnimation.stop();
            mVoiceAnimation = null;
        }
        if (mPlaybackState.isPlaying(message.getMsgId())) {
            mVoiceIv.setImageResource(mIsSender ? mPlaySendAnim : mPlayReceiveAnim);
            if (mVoiceIv.getDrawable() instanceof AnimationDrawable) {
                mVoiceAnimation = (AnimationDrawable) mVoiceIv.getDrawable();
                mVoiceAnimation.start();
            }
        } else {
            mVoiceIv.setImageResource(mIsSender ? mSendDrawable : mReceiveDrawable);
        }
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

}
