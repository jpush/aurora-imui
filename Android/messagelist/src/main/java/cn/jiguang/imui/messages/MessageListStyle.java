package cn.jiguang.imui.messages;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.WindowManager;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.Style;

public class MessageListStyle extends Style {

    private float dateTextSize;
    private int dateTextColor;
    private int datePadding;
    private String dateFormat;

    private int avatarWidth;
    private int avatarHeight;
    private int showDisplayName;
    private int receiveBubbleDrawable;
    private int receiveBubbleColor;
    private int receiveBubblePressedColor;
    private int receiveBubbleSelectedColor;
    private float receiveBubbleTextSize;
    private int receiveBubbleTextColor;
    private int receiveBubblePaddingLeft;
    private int receiveBubblePaddingTop;
    private int receiveBubblePaddingRight;
    private int receiveBubblePaddingBottom;

    private int sendBubbleDrawable;
    private int sendBubbleColor;
    private int sendBubblePressedColor;
    private int sendBubbleSelectedColor;
    private float sendBubbleTextSize;
    private int sendBubbleTextColor;
    private int sendBubblePaddingLeft;
    private int sendBubblePaddingTop;
    private int sendBubblePaddingRight;
    private int sendBubblePaddingBottom;

    private int sendVoiceDrawable;
    private int receiveVoiceDrawable;
    private int playSendVoiceAnim;
    private int playReceiveVoiceAnim;

    // Set bubble's max width, value from 0 to 1. 1 means max width equals with screen width.
    // Default value is 0.8.
    private float bubbleMaxWidth;
    private Drawable sendPhotoMsgBg;
    private Drawable receivePhotoMsgBg;

    private int windowWidth;
    private Drawable sendingProgressDrawable;
    private Drawable sendingIndeterminateDrawable;

    public static MessageListStyle parse(Context context, AttributeSet attrs) {
        MessageListStyle style = new MessageListStyle(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MessageList);
        int dateTextSizePixel = typedArray.getDimensionPixelSize(R.styleable.MessageList_dateTextSize,
                context.getResources().getDimensionPixelOffset(R.dimen.aurora_size_date_text));
        style.dateTextSize = getSPTextSize(context, dateTextSizePixel);
        style.dateTextColor = typedArray.getColor(R.styleable.MessageList_dateTextColor,
                ContextCompat.getColor(context, R.color.aurora_msg_date_text_color));
        style.datePadding = typedArray.getDimensionPixelSize(R.styleable.MessageList_datePadding,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_date_text));
        style.dateFormat = typedArray.getString(R.styleable.MessageList_dateFormat);

        style.avatarWidth = typedArray.getDimensionPixelSize(R.styleable.MessageList_avatarWidth,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_width_msg_avatar));
        style.avatarHeight = typedArray.getDimensionPixelSize(R.styleable.MessageList_avatarHeight,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_height_msg_avatar));
        style.showDisplayName = typedArray.getInt(R.styleable.MessageList_showDisplayName, 0);

        style.receiveBubbleDrawable = typedArray.getResourceId(R.styleable.MessageList_receiveBubbleDrawable, -1);
        style.receiveBubbleColor = typedArray.getColor(R.styleable.MessageList_receiveBubbleColor,
                ContextCompat.getColor(context, R.color.aurora_msg_receive_bubble_default_color));
        style.receiveBubblePressedColor = typedArray.getColor(R.styleable.MessageList_receiveBubblePressedColor,
                ContextCompat.getColor(context, R.color.aurora_msg_receive_bubble_pressed_color));
        style.receiveBubbleSelectedColor = typedArray.getColor(R.styleable.MessageList_receiveBubbleSelectedColor,
                ContextCompat.getColor(context, R.color.aurora_msg_receive_bubble_selected_color));
        int receiveTextSizePixel = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveTextSize,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_size_receive_text));
        style.receiveBubbleTextSize = getSPTextSize(context, receiveTextSizePixel);
        style.receiveBubbleTextColor = typedArray.getColor(R.styleable.MessageList_receiveTextColor,
                ContextCompat.getColor(context, R.color.aurora_msg_receive_text_color));
        style.receiveBubblePaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveBubblePaddingLeft,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_receive_text_left));
        style.receiveBubblePaddingTop = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveBubblePaddingTop,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_receive_text_top));
        style.receiveBubblePaddingRight = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveBubblePaddingRight,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_receive_text_right));
        style.receiveBubblePaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveBubblePaddingBottom,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_receive_text_bottom));

        style.sendBubbleDrawable = typedArray.getResourceId(R.styleable.MessageList_sendBubbleDrawable, -1);
        style.sendBubbleColor = typedArray.getColor(R.styleable.MessageList_sendBubbleColor,
                ContextCompat.getColor(context, R.color.aurora_msg_send_bubble_default_color));
        style.sendBubblePressedColor = typedArray.getColor(R.styleable.MessageList_sendBubblePressedColor,
                ContextCompat.getColor(context, R.color.aurora_msg_send_bubble_pressed_color));
        style.sendBubbleSelectedColor = typedArray.getColor(R.styleable.MessageList_sendBubbleSelectedColor,
                ContextCompat.getColor(context, R.color.aurora_msg_send_bubble_selected_color));
        int sendTextSizePixel = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendTextSize,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_size_send_text));
        style.sendBubbleTextSize = getSPTextSize(context, sendTextSizePixel);
        style.sendBubbleTextColor = typedArray.getColor(R.styleable.MessageList_sendTextColor,
                ContextCompat.getColor(context, R.color.aurora_msg_send_text_color));
        style.sendBubblePaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendBubblePaddingLeft,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_send_text_left));
        style.sendBubblePaddingTop = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendBubblePaddingTop,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_send_text_top));
        style.sendBubblePaddingRight = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendBubblePaddingRight,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_send_text_right));
        style.sendBubblePaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendBubblePaddingBottom,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_send_text_bottom));

        style.sendVoiceDrawable = typedArray.getResourceId(R.styleable.MessageList_sendVoiceDrawable,
                R.drawable.aurora_sendvoice_send_3);
        style.receiveVoiceDrawable = typedArray.getResourceId(R.styleable.MessageList_receiveVoiceDrawable,
                R.drawable.aurora_receivevoice_receive_3);
        style.playSendVoiceAnim = typedArray.getResourceId(R.styleable.MessageList_playSendVoiceAnim,
                R.drawable.aurora_anim_send_voice);
        style.playReceiveVoiceAnim = typedArray.getResourceId(R.styleable.MessageList_playReceiveVoiceAnim,
                R.drawable.aurora_anim_receive_voice);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        style.bubbleMaxWidth = typedArray.getFloat(R.styleable.MessageList_bubbleMaxWidth, 0.8f);
        style.windowWidth =  wm.getDefaultDisplay().getWidth();

        style.sendPhotoMsgBg = typedArray.getDrawable(R.styleable.MessageList_sendPhotoMsgBg);
        style.receivePhotoMsgBg = typedArray.getDrawable(R.styleable.MessageList_receivePhotoMsgBg);
        style.sendingProgressDrawable = typedArray.getDrawable(R.styleable.MessageList_sendingProgressDrawable);
        style.sendingIndeterminateDrawable = typedArray.getDrawable(R.styleable.MessageList_sendingIndeterminateDrawable);
        typedArray.recycle();
        return style;
    }

    public static float getSPTextSize(Context context, int pixel) {
        return pixel / context.getResources().getDisplayMetrics().scaledDensity;
    }

    protected MessageListStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Drawable getMessageSelector(@ColorInt int normalColor, @ColorInt int selectedColor,
                                       @ColorInt int pressedColor, @DrawableRes int shape) {
        Drawable button = DrawableCompat.wrap(getVectorDrawable(shape));
        DrawableCompat.setTintList(button, new ColorStateList(new int[][] {
                new int[] {android.R.attr.state_selected},
                new int[] {android.R.attr.state_pressed},
                new int[] {-android.R.attr.state_pressed, -android.R.attr.state_selected}
        }, new int[] {selectedColor, pressedColor, normalColor}));
        return button;
    }

    public float getDateTextSize() {
        return dateTextSize;
    }

    public int getDateTextColor() {
        return dateTextColor;
    }

    public int getDatePadding() {
        return datePadding;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public int getAvatarWidth() {
        return avatarWidth;
    }

    public int getAvatarHeight() {
        return avatarHeight;
    }

    public int getShowDisplayName() {
        return showDisplayName;
    }

    public void setReceiveBubbleDrawable(int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        if (drawable != null) {
            this.receiveBubbleDrawable = resId;
        }
    }

    public Drawable getReceiveBubbleDrawable() {
        if (receiveBubbleDrawable == -1) {
            return getMessageSelector(receiveBubbleColor, receiveBubbleSelectedColor, receiveBubblePressedColor,
                    R.drawable.aurora_receivetxt_bubble);
        } else {
            return ContextCompat.getDrawable(mContext, receiveBubbleDrawable);
        }
    }

    public int getReceiveBubbleColor() {
        return receiveBubbleColor;
    }

    public int getReceiveBubblePressedColor() {
        return receiveBubblePressedColor;
    }

    public int getReceiveBubbleSelectedColor() {
        return receiveBubbleSelectedColor;
    }

    public void setDateTextSize(float dateTextSize) {
        this.dateTextSize = dateTextSize;
    }

    public void setDateTextColor(int dateTextColor) {
        this.dateTextColor = dateTextColor;
    }

    public void setDatePadding(int datePadding) {
        this.datePadding = datePadding;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setAvatarWidth(int avatarWidth) {
        this.avatarWidth = avatarWidth;
    }

    public void setAvatarHeight(int avatarHeight) {
        this.avatarHeight = avatarHeight;
    }

    public void setShowDisplayName(int showDisplayName) {
        this.showDisplayName = showDisplayName;
    }

    public void setReceiveBubbleColor(int receiveBubbleColor) {
        this.receiveBubbleColor = receiveBubbleColor;
    }

    public void setReceiveBubblePressedColor(int receiveBubblePressedColor) {
        this.receiveBubblePressedColor = receiveBubblePressedColor;
    }

    public void setReceiveBubbleSelectedColor(int receiveBubbleSelectedColor) {
        this.receiveBubbleSelectedColor = receiveBubbleSelectedColor;
    }

    public void setReceiveBubbleTextSize(float receiveBubbleTextSize) {
        this.receiveBubbleTextSize = receiveBubbleTextSize;
    }

    public void setReceiveBubbleTextColor(int receiveBubbleTextColor) {
        this.receiveBubbleTextColor = receiveBubbleTextColor;
    }

    public void setReceiveBubblePaddingLeft(int receiveBubblePaddingLeft) {
        this.receiveBubblePaddingLeft = receiveBubblePaddingLeft;
    }

    public void setReceiveBubblePaddingTop(int receiveBubblePaddingTop) {
        this.receiveBubblePaddingTop = receiveBubblePaddingTop;
    }

    public void setReceiveBubblePaddingRight(int receiveBubblePaddingRight) {
        this.receiveBubblePaddingRight = receiveBubblePaddingRight;
    }

    public void setReceiveBubblePaddingBottom(int receiveBubblePaddingBottom) {
        this.receiveBubblePaddingBottom = receiveBubblePaddingBottom;
    }

    public void setSendBubbleColor(int sendBubbleColor) {
        this.sendBubbleColor = sendBubbleColor;
    }

    public void setSendBubblePressedColor(int sendBubblePressedColor) {
        this.sendBubblePressedColor = sendBubblePressedColor;
    }

    public void setSendBubbleSelectedColor(int sendBubbleSelectedColor) {
        this.sendBubbleSelectedColor = sendBubbleSelectedColor;
    }

    public void setSendBubbleTextSize(float sendBubbleTextSize) {
        this.sendBubbleTextSize = sendBubbleTextSize;
    }

    public void setSendBubbleTextColor(int sendBubbleTextColor) {
        this.sendBubbleTextColor = sendBubbleTextColor;
    }

    public void setSendBubblePaddingLeft(int sendBubblePaddingLeft) {
        this.sendBubblePaddingLeft = sendBubblePaddingLeft;
    }

    public void setSendBubblePaddingTop(int sendBubblePaddingTop) {
        this.sendBubblePaddingTop = sendBubblePaddingTop;
    }

    public void setSendBubblePaddingRight(int sendBubblePaddingRight) {
        this.sendBubblePaddingRight = sendBubblePaddingRight;
    }

    public void setSendBubblePaddingBottom(int sendBubblePaddingBottom) {
        this.sendBubblePaddingBottom = sendBubblePaddingBottom;
    }

    public void setSendVoiceDrawable(int sendVoiceDrawable) {
        Drawable drawable = ContextCompat.getDrawable(mContext, sendVoiceDrawable);
        if (drawable != null) {
            this.sendVoiceDrawable = sendVoiceDrawable;
        }
    }

    public void setReceiveVoiceDrawable(int receiveVoiceDrawable) {
        Drawable drawable = ContextCompat.getDrawable(mContext, receiveVoiceDrawable);
        if (drawable != null) {
            this.receiveVoiceDrawable = receiveVoiceDrawable;
        }
    }

    public void setPlaySendVoiceAnim(int playSendVoiceAnim) {
        this.playSendVoiceAnim = playSendVoiceAnim;
    }

    public void setPlayReceiveVoiceAnim(int playReceiveVoiceAnim) {
        this.playReceiveVoiceAnim = playReceiveVoiceAnim;
    }

    public void setBubbleMaxWidth(float bubbleMaxWidth) {
        this.bubbleMaxWidth = bubbleMaxWidth;
    }

    public void setSendingProgressDrawable(Drawable sendingProgressDrawable) {
        this.sendingProgressDrawable = sendingProgressDrawable;
    }

    public void setSendingIndeterminateDrawable(Drawable sendingIndeterminateDrawable) {
        this.sendingIndeterminateDrawable = sendingIndeterminateDrawable;
    }

    public float getReceiveBubbleTextSize() {
        return receiveBubbleTextSize;
    }

    public int getReceiveBubbleTextColor() {
        return receiveBubbleTextColor;
    }

    public int getReceiveBubblePaddingLeft() {
        return receiveBubblePaddingLeft;
    }

    public int getReceiveBubblePaddingTop() {
        return receiveBubblePaddingTop;
    }

    public int getReceiveBubblePaddingRight() {
        return receiveBubblePaddingRight;
    }

    public int getReceiveBubblePaddingBottom() {
        return receiveBubblePaddingBottom;
    }

    public void setSendBubbleDrawable(int resId) {
        Drawable drawable = ContextCompat.getDrawable(mContext, resId);
        if (drawable != null) {
            this.sendBubbleDrawable = resId;
        }
    }

    public Drawable getSendBubbleDrawable() {
        if (sendBubbleDrawable == -1) {
            return getMessageSelector(sendBubbleColor, sendBubbleSelectedColor, sendBubblePressedColor,
                    R.drawable.aurora_sendtxt_bubble);
        } else {
            return ContextCompat.getDrawable(mContext, sendBubbleDrawable);
        }
    }

    public int getSendBubbleColor() {
        return sendBubbleColor;
    }

    public int getSendBubblePressedColor() {
        return sendBubblePressedColor;
    }

    public int getSendBubbleSelectedColor() {
        return sendBubbleSelectedColor;
    }

    public float getSendBubbleTextSize() {
        return sendBubbleTextSize;
    }

    public int getSendBubbleTextColor() {
        return sendBubbleTextColor;
    }

    public int getSendBubblePaddingLeft() {
        return sendBubblePaddingLeft;
    }

    public int getSendBubblePaddingTop() {
        return sendBubblePaddingTop;
    }

    public int getSendBubblePaddingRight() {
        return sendBubblePaddingRight;
    }

    public int getSendBubblePaddingBottom() {
        return sendBubblePaddingBottom;
    }

    public int getSendVoiceDrawable() {
        return sendVoiceDrawable;
    }

    public int getReceiveVoiceDrawable() {
        return receiveVoiceDrawable;
    }

    public int getPlaySendVoiceAnim() {
        return playSendVoiceAnim;
    }

    public int getPlayReceiveVoiceAnim() {
        return playReceiveVoiceAnim;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public float getBubbleMaxWidth() {
        return bubbleMaxWidth;
    }

    public Drawable getSendPhotoMsgBg() {
        return sendPhotoMsgBg;
    }

    public Drawable getReceivePhotoMsgBg() {
        return receivePhotoMsgBg;
    }

    public Drawable getSendingIndeterminateDrawable() {
        return sendingIndeterminateDrawable;
    }

    public Drawable getSendingProgressDrawable() {
        return sendingProgressDrawable;
    }
}
