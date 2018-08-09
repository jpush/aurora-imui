package cn.jiguang.imui.messagelist;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.io.File;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messagelist.model.RCTMessage;
import cn.jiguang.imui.messages.BaseMessageViewHolder;
import cn.jiguang.imui.messages.MessageListStyle;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.view.RoundImageView;
import cn.jiguang.imui.view.RoundTextView;


public class CustomViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE>
        implements MsgListAdapter.DefaultMessageViewHolder {

    private TextView mMsgTv;
    private RoundTextView mDateTv;
    private TextView mDisplayNameTv;
    private RoundImageView mAvatarIv;
    private ImageButton mResendIb;
    private ProgressBar mSendingPb;
    private boolean mIsSender;
    private LruCache<String, Bitmap> mCache;

    public CustomViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;
        mMsgTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_message);
        mDateTv = (RoundTextView) itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mAvatarIv = (RoundImageView) itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        if (isSender) {
            mDisplayNameTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_sender_display_name);
        } else {
            mDisplayNameTv = (TextView) itemView.findViewById(R.id.aurora_tv_msgitem_receiver_display_name);
        }
        mResendIb = (ImageButton) itemView.findViewById(R.id.aurora_ib_msgitem_resend);
        mSendingPb = (ProgressBar) itemView.findViewById(R.id.aurora_pb_msgitem_sending);
    }

    @Override
    public void onBind(final MESSAGE message) {
        int memClass = ((ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        mCache = new LruCache<>(1024 * 1024 * memClass / 8);
        RCTMessage rctMessage = (RCTMessage) message;
        int width = (int) (rctMessage.getWidth() * mDensity + 0.5);
        int height = (int) (rctMessage.getHeight() * mDensity + 0.5);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        if (mIsSender) {
            params.addRule(RelativeLayout.LEFT_OF, mAvatarIv.getId());
        } else {
            params.addRule(RelativeLayout.RIGHT_OF, mAvatarIv.getId());
        }
        params.addRule(RelativeLayout.BELOW, mDisplayNameTv.getId());
        if (width != 0 && height != 0) {
            mMsgTv.setLayoutParams(params);
        }
        String text = message.getText();
        if (!TextUtils.isEmpty(text)) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                mMsgTv.setText(Html.fromHtml(text, new MyImageGetter(), null));
            } else {
                mMsgTv.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT, new MyImageGetter(), null));
            }
        }

        if (message.getTimeString() != null && !TextUtils.isEmpty(message.getTimeString())) {
            mDateTv.setText(message.getTimeString());
        } else {
            mDateTv.setVisibility(View.GONE);
        }
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null
                && !message.getFromUser().getAvatarFilePath().isEmpty();
        if (isAvatarExists && mImageLoader != null) {
            mImageLoader.loadAvatarImage(mAvatarIv, message.getFromUser().getAvatarFilePath());
        } else if (mImageLoader == null) {
            mAvatarIv.setVisibility(View.GONE);
        }
        if (mDisplayNameTv.getVisibility() == View.VISIBLE) {
            mDisplayNameTv.setText(message.getFromUser().getDisplayName());
        }

        if (mIsSender) {
            switch (message.getMessageStatus()) {
                case SEND_GOING:
                    mSendingPb.setVisibility(View.VISIBLE);
                    mResendIb.setVisibility(View.GONE);
                    Log.i("CustomViewHolder", "sending message");
                    break;
                case SEND_FAILED:
                    mSendingPb.setVisibility(View.GONE);
                    Log.i("CustomViewHolder", "send message failed");
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
                    Log.i("CustomViewHolder", "send message succeed");
                    break;
            }
        }

        mMsgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMsgClickListener != null) {
                    mMsgClickListener.onMessageClick(message);
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

    class MyImageGetter implements Html.ImageGetter {

        @Override
        public Drawable getDrawable(String source) {
            if (mCache.get(source) != null) {
                Bitmap bitmap = mCache.get(source);
                Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                drawable.setBounds(new Rect(0, 0, 200, 200));
                return drawable;
            }

            File file = new File(source);
            String fileName = source.substring(source.lastIndexOf("/") + 1);
            // load native image
            if (file.exists()) {
                return getNativeFile(file);
                // try to get image from application's file directory
            } else {
                file = new File(mContext.getFilesDir() + "/" + fileName);
                if (file.exists()) {
                    return getNativeFile(file);
                }
            }

            URLDrawable drawable = new URLDrawable(mContext);
            LoadImageAsync loadImageAsync = new LoadImageAsync(mContext, drawable, mMsgTv, mCache);
            loadImageAsync.execute(source, fileName);
            return drawable;
        }

        private Drawable getNativeFile(File file) {
            Bitmap cacheBitmap = mCache.get(file.getAbsolutePath());
            if (cacheBitmap != null) {
                return new BitmapDrawable(mContext.getResources(), cacheBitmap);
            }
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = false;
            opts.inSampleSize = 2;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
            mCache.put(file.getAbsolutePath(), bitmap);
            Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
            drawable.setBounds(new Rect(0, 0, 200, 200));
            return drawable;
        }
    }

    public class URLDrawable extends BitmapDrawable {
        protected Drawable drawable;
        public URLDrawable(Context context) {
            drawable = context.getResources().getDrawable(R.drawable.ic_launcher);
            this.setBounds(new Rect(0,0,200, 200));
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }


    @Override
    public void applyStyle(MessageListStyle style) {
        mMsgTv.setMaxWidth((int) (style.getWindowWidth() * style.getBubbleMaxWidth()));
        mMsgTv.setLineSpacing(style.getLineSpacingExtra(), style.getLineSpacingMultiplier());
        mDisplayNameTv.setTextSize(style.getDisplayNameTextSize());
        mDisplayNameTv.setPadding(style.getDisplayNamePaddingLeft(), style.getDisplayNamePaddingTop(),
                style.getDisplayNamePaddingRight(), style.getDisplayNamePaddingBottom());
        mDisplayNameTv.setTextColor(style.getDisplayNameTextColor());
        mDisplayNameTv.setEms(style.getDisplayNameEmsNumber());
        if (mIsSender) {
            mMsgTv.setBackground(style.getSendBubbleDrawable());
            mMsgTv.setTextColor(style.getSendBubbleTextColor());
            mMsgTv.setTextSize(style.getSendBubbleTextSize());
            mMsgTv.setPadding(style.getSendBubblePaddingLeft(),
                    style.getSendBubblePaddingTop(),
                    style.getSendBubblePaddingRight(),
                    style.getSendBubblePaddingBottom());
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
            mMsgTv.setBackground(style.getReceiveBubbleDrawable());
            mMsgTv.setTextColor(style.getReceiveBubbleTextColor());
            mMsgTv.setTextSize(style.getReceiveBubbleTextSize());
            mMsgTv.setPadding(style.getReceiveBubblePaddingLeft(),
                    style.getReceiveBubblePaddingTop(),
                    style.getReceiveBubblePaddingRight(),
                    style.getReceiveBubblePaddingBottom());
            if (style.getShowReceiverDisplayName()) {
                mDisplayNameTv.setVisibility(View.VISIBLE);
            } else {
                mDisplayNameTv.setVisibility(View.GONE);
            }
        }
        mAvatarIv.setBorderRadius(style.getAvatarRadius());
        android.view.ViewGroup.LayoutParams layoutParams = mAvatarIv.getLayoutParams();
        layoutParams.width = style.getAvatarWidth();
        layoutParams.height = style.getAvatarHeight();
        mAvatarIv.setLayoutParams(layoutParams);
        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        mDateTv.setBgColor(style.getDateBgColor());
        mDateTv.setBgCornerRadius(style.getDateBgCornerRadius());

    }
}
