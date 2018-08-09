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
    private int datePaddingLeft;
    private int datePaddingTop;
    private int datePaddingRight;
    private int datePaddingBottom;
    private int dateBgCornerRadius;
    private int dateBgColor;
    private int eventPaddingLeft;
    private int eventPaddingTop;
    private int eventPaddingRight;
    private int eventPaddingBottom;
    private float eventTextSize;
    private int eventTextColor;
    private int eventBgColor;
    private int eventLineSpacingExtra;
    private int eventBgCornerRadius;
    private String dateFormat;

    private int avatarWidth;
    private int avatarHeight;
    private int avatarRadius;
    private boolean showSenderDisplayName;
    private boolean showReceiverDisplayName;
    private float displayNameTextSize;
    private int displayNameTextColor;
    private int displayNamePaddingLeft;
    private int displayNamePaddingTop;
    private int displayNamePaddingRight;
    private int displayNamePaddingBottom;
    private int displayNameEmsNumber;
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

    private int videoDurationTextColor;
    private float videoDurationTextSize;
    private int videoMessageRadius;
    private int photoMessageRadius;

    // Set bubble's max width, value from 0 to 1. 1 means max width equals with
    // screen width.
    // Default value is 0.8.
    private float bubbleMaxWidth;
    private Drawable sendPhotoMsgBg;
    private Drawable receivePhotoMsgBg;
    private int lineSpacingExtra;
    private float lineSpacingMultiplier;

    private int windowWidth;
    private Drawable sendingProgressDrawable;
    private Drawable sendingIndeterminateDrawable;

    public static MessageListStyle parse(Context context, AttributeSet attrs) {
        MessageListStyle style = new MessageListStyle(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MessageList);
        int dateTextSizePixel = typedArray.getDimensionPixelSize(R.styleable.MessageList_dateTextSize,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_size_date_text));
        style.dateTextSize = getSPTextSize(context, dateTextSizePixel);
        style.dateTextColor = typedArray.getColor(R.styleable.MessageList_dateTextColor,
                ContextCompat.getColor(context, R.color.aurora_msg_date_text_color));
        style.datePaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MessageList_datePaddingLeft,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_left_date_text));
        style.datePaddingTop = typedArray.getDimensionPixelSize(R.styleable.MessageList_datePaddingTop,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_top_date_text));
        style.datePaddingRight = typedArray.getDimensionPixelSize(R.styleable.MessageList_datePaddingRight,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_right_date_text));
        style.datePaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MessageList_datePaddingBottom,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_bottom_date_text));
        style.dateBgColor = typedArray.getColor(R.styleable.MessageList_dateBackgroundColor,
                ContextCompat.getColor(context, R.color.aurora_msg_date_bg_color));
        style.dateBgCornerRadius = typedArray.getDimensionPixelSize(R.styleable.MessageList_dateCornerRadius,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_size_date_bg_radius));
        int eventTextSizePixel = typedArray.getDimensionPixelSize(R.styleable.MessageList_eventTextSize,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_size_event_text));
        style.eventTextSize = getSPTextSize(context, eventTextSizePixel);
        style.eventPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MessageList_eventPaddingLeft,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_event_text));
        style.eventPaddingTop = typedArray.getDimensionPixelSize(R.styleable.MessageList_eventPaddingTop,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_event_text));
        style.eventPaddingRight = typedArray.getDimensionPixelSize(R.styleable.MessageList_eventPaddingRight,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_event_text));
        style.eventPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MessageList_eventPaddingBottom,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_event_text));
        style.eventTextColor = typedArray.getColor(R.styleable.MessageList_eventTextColor,
                ContextCompat.getColor(context, R.color.aurora_msg_event_text_color));
        style.eventBgCornerRadius = typedArray.getDimensionPixelSize(R.styleable.MessageList_eventCornerRadius,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_event_bg_corner_radius));
        style.eventBgColor = typedArray.getColor(R.styleable.MessageList_eventBackgroundColor,
                ContextCompat.getColor(context, R.color.aurora_event_msg_bg_color));
        style.dateFormat = typedArray.getString(R.styleable.MessageList_dateFormat);

        style.avatarWidth = typedArray.getDimensionPixelSize(R.styleable.MessageList_avatarWidth,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_width_msg_avatar));
        style.avatarHeight = typedArray.getDimensionPixelSize(R.styleable.MessageList_avatarHeight,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_height_msg_avatar));
        style.avatarRadius = typedArray.getDimensionPixelSize(R.styleable.MessageList_avatarRadius,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_radius_avatar_default));
        style.showSenderDisplayName = typedArray.getBoolean(R.styleable.MessageList_showSenderDisplayName, false);
        style.showReceiverDisplayName = typedArray.getBoolean(R.styleable.MessageList_showReceiverDisplayName, false);
        int displayNameTextSize = typedArray.getDimensionPixelSize(R.styleable.MessageList_displayNameTextSize,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_size_display_name_text));
        style.displayNameTextSize = getSPTextSize(context, displayNameTextSize);
        style.displayNameTextColor = typedArray.getColor(R.styleable.MessageList_displayNameTextColor,
                ContextCompat.getColor(context, R.color.aurora_display_name_text_color));
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
        style.receiveBubblePaddingLeft = typedArray.getDimensionPixelSize(
                R.styleable.MessageList_receiveBubblePaddingLeft,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_receive_text_left));
        style.receiveBubblePaddingTop = typedArray.getDimensionPixelSize(
                R.styleable.MessageList_receiveBubblePaddingTop,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_receive_text_top));
        style.receiveBubblePaddingRight = typedArray.getDimensionPixelSize(
                R.styleable.MessageList_receiveBubblePaddingRight,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_receive_text_right));
        style.receiveBubblePaddingBottom = typedArray.getDimensionPixelSize(
                R.styleable.MessageList_receiveBubblePaddingBottom,
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
        style.sendBubblePaddingBottom = typedArray.getDimensionPixelSize(
                R.styleable.MessageList_sendBubblePaddingBottom,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_padding_send_text_bottom));
        style.lineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.MessageList_lineSpacingExtra,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_line_spacing_extra_default));
        style.lineSpacingMultiplier = typedArray.getFloat(R.styleable.MessageList_lineSpacingMultiplier, 1.0f);
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
        style.windowWidth = wm.getDefaultDisplay().getWidth();

        style.sendPhotoMsgBg = typedArray.getDrawable(R.styleable.MessageList_sendPhotoMsgBg);
        style.receivePhotoMsgBg = typedArray.getDrawable(R.styleable.MessageList_receivePhotoMsgBg);
        style.videoDurationTextColor = typedArray.getColor(R.styleable.MessageList_videoDurationTextColor,
                ContextCompat.getColor(context, R.color.aurora_video_message_duration_text_color));
        int videoDurationTextSize = typedArray.getDimensionPixelSize(R.styleable.MessageList_videoDurationTextSize,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_size_video_message_duration_text));
        style.videoDurationTextSize = getSPTextSize(context, videoDurationTextSize);
        style.photoMessageRadius = typedArray.getDimensionPixelSize(R.styleable.MessageList_photoMessageRadius,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_radius_photo_message));
        style.videoMessageRadius = typedArray.getDimensionPixelSize(R.styleable.MessageList_videoMessageRadius,
                context.getResources().getDimensionPixelSize(R.dimen.aurora_radius_video_message));
        style.sendingProgressDrawable = typedArray.getDrawable(R.styleable.MessageList_sendingProgressDrawable);
        style.sendingIndeterminateDrawable = typedArray
                .getDrawable(R.styleable.MessageList_sendingIndeterminateDrawable);
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
        DrawableCompat.setTintList(button, new ColorStateList(
                new int[][] { new int[] { android.R.attr.state_selected }, new int[] { android.R.attr.state_pressed },
                        new int[] { -android.R.attr.state_pressed, -android.R.attr.state_selected } },
                new int[] { selectedColor, pressedColor, normalColor }));
        return button;
    }

    public float getDateTextSize() {
        return dateTextSize;
    }

    public int getDateTextColor() {
        return dateTextColor;
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

    public int getAvatarRadius() {
        return avatarRadius;
    }

    public void setAvatarRadius(int radius) {
        this.avatarRadius = radius;
    }

    public boolean getShowSenderDisplayName() {
        return showSenderDisplayName;
    }

    public boolean getShowReceiverDisplayName() {
        return showReceiverDisplayName;
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

    public void setEventTextColor(int eventTextColor) {
        this.eventTextColor = eventTextColor;
    }

    public int getEventTextColor() {
        return this.eventTextColor;
    }

    public void setEventTextSize(float textSize) {
        this.eventTextSize = textSize;
    }

    public float getEventTextSize() {
        return this.eventTextSize;
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

    public void setShowSenderDisplayName(boolean showSenderDisplayName) {
        this.showSenderDisplayName = showSenderDisplayName;
    }

    public void setShowReceiverDisplayName(boolean showReceiverDisplayName) {
        this.showReceiverDisplayName = showReceiverDisplayName;
    }

    public void setDisplayNameTextSize(float displayNameTextSize) {
        this.displayNameTextSize = displayNameTextSize;
    }

    public float getDisplayNameTextSize() {
        return this.displayNameTextSize;
    }

    public void setDisplayNameTextColor(int color) {
        this.displayNameTextColor = color;
    }

    public int getDisplayNameTextColor() {
        return this.displayNameTextColor;
    }

    public void setDisplayNamePadding(int left, int top, int right, int bottom) {
        this.displayNamePaddingLeft = left;
        this.displayNamePaddingTop = top;
        this.displayNamePaddingRight = right;
        this.displayNamePaddingBottom = bottom;
    }

    public int getDisplayNamePaddingLeft() {
        return this.displayNamePaddingLeft;
    }

    public int getDisplayNamePaddingTop() {
        return this.displayNamePaddingTop;
    }

    public int getDisplayNamePaddingRight() {
        return this.displayNamePaddingRight;
    }

    public int getDisplayNamePaddingBottom() {
        return this.displayNamePaddingBottom;
    }

    public int setDisplayNameEmsNumber(int displayNameEmsNumber) {
        return this.displayNameEmsNumber = displayNameEmsNumber;
    }

    public int getDisplayNameEmsNumber() {
        return this.displayNameEmsNumber <= 0 ? 5 : this.displayNameEmsNumber;
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

    public void setReceiveBubblePadding(int left, int top, int right, int bottom) {
        this.receiveBubblePaddingLeft = left;
        this.receiveBubblePaddingTop = top;
        this.receiveBubblePaddingRight = right;
        this.receiveBubblePaddingBottom = bottom;
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

    public void setLineSpacingExtra(int spacing) {
        this.lineSpacingExtra = spacing;
    }

    public int getLineSpacingExtra() {
        return this.lineSpacingExtra;
    }

    public void setLineSpacingMultiplier(float mult) {
        this.lineSpacingMultiplier = mult;
    }

    public float getLineSpacingMultiplier() {
        return this.lineSpacingMultiplier;
    }

    public int getEventBgColor() {
        return eventBgColor;
    }

    public void setEventBgColor(int color) {
        this.eventBgColor = color;
    }

    public int getEventLineSpacingExtra() {
        return this.eventLineSpacingExtra;
    }

    public void setEventLineSpacingExtra(int extra) {
        this.eventLineSpacingExtra = extra;
    }

    public void setEventBgCornerRadius(int radius) {
        this.eventBgCornerRadius = radius;
    }

    public int getEventBgCornerRadius() {
        return this.eventBgCornerRadius;
    }

    public int getDatePaddingLeft() {
        return datePaddingLeft;
    }

    public void setDatePadding(int left, int top, int right, int bottom) {
        this.datePaddingLeft = left;
        this.datePaddingTop = top;
        this.datePaddingRight = right;
        this.datePaddingBottom = bottom;
    }

    public int getDatePaddingTop() {
        return datePaddingTop;
    }

    public int getDatePaddingRight() {
        return datePaddingRight;
    }

    public int getDatePaddingBottom() {
        return datePaddingBottom;
    }

    public int getDateBgCornerRadius() {
        return dateBgCornerRadius;
    }

    public void setDateBgCornerRadius(int radius) {
        this.dateBgCornerRadius = radius;
    }

    public int getDateBgColor() {
        return dateBgColor;
    }

    public void setDateBgColor(int color) {
        this.dateBgColor = color;
    }

    public void setEventTextPadding(int left, int top, int right, int bottom) {
        this.eventPaddingLeft = left;
        this.eventPaddingTop = top;
        this.eventPaddingRight = right;
        this.eventPaddingBottom = bottom;
    }

    public int getEventPaddingLeft() {
        return this.eventPaddingLeft;
    }

    public int getEventPaddingTop() {
        return this.eventPaddingTop;
    }

    public int getEventPaddingRight() {
        return this.eventPaddingRight;
    }

    public int getEventPaddingBottom() {
        return this.eventPaddingBottom;
    }

    public void setVideoDurationTextColor(int color) {
        this.videoDurationTextColor = color;
    }

    public int getVideoDurationTextColor() {
        return this.videoDurationTextColor;
    }

    public void setVideoDurationTextSize(float size) {
        this.videoDurationTextSize = size;
    }

    public float getVideoDurationTextSize() {
        return this.videoDurationTextSize;
    }

    public void setVideoMessageRadius(int radius) {
        this.videoMessageRadius = radius;
    }

    public int getVideoMessageRadius() {
        return this.videoMessageRadius;
    }

    public void setPhotoMessageRadius(int radius) {
        this.photoMessageRadius = radius;
    }

    public int getPhotoMessageRadius() {
        return this.photoMessageRadius;
    }

    public void setSendBubblePadding(int left, int top, int right, int bottom) {
        this.sendBubblePaddingLeft = left;
        this.sendBubblePaddingTop = top;
        this.sendBubblePaddingRight = right;
        this.sendBubblePaddingBottom = bottom;
    }
}
