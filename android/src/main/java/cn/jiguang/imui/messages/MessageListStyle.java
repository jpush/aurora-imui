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

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.Style;

public class MessageListStyle extends Style {

    private int dateTextSize;
    private int dateTextColor;
    private int datePadding;
    private String dateFormat;

    private int avatarWidth;
    private int avatarHeight;

    private int receiveBubbleDrawable;
    private int receiveBubbleColor;
    private int receiveBubblePressedColor;
    private int receiveBubbleSelectedColor;
    private int receiveBubbleTextSize;
    private int receiveBubbleTextColor;
    private int receiveBubblePaddingLeft;
    private int receiveBubblePaddingTop;
    private int receiveBubblePaddingRight;
    private int receiveBubblePaddingBottom;

    private int sendBubbleDrawable;
    private int sendBubbleColor;
    private int sendBubblePressedColor;
    private int sendBubbleSelectedColor;
    private int sendBubbleTextSize;
    private int sendBubbleTextColor;
    private int sendBubblePaddingLeft;
    private int sendBubblePaddingTop;
    private int sendBubblePaddingRight;
    private int sendBubblePaddingBottom;

    public static MessageListStyle parse(Context context, AttributeSet attrs) {
        MessageListStyle style = new MessageListStyle(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MessageList);
        style.dateTextSize = typedArray.getDimensionPixelSize(R.styleable.MessageList_dateTextSize,
                context.getResources().getDimensionPixelOffset(R.dimen.date_text_size));
        style.dateTextColor = typedArray.getColor(R.styleable.MessageList_dateTextColor,
                ContextCompat.getColor(context, R.color.date_text_color));
        style.datePadding = typedArray.getDimensionPixelSize(R.styleable.MessageList_datePadding,
                context.getResources().getDimensionPixelSize(R.dimen.date_text_padding));
        style.dateFormat = typedArray.getString(R.styleable.MessageList_dateFormat);

        style.avatarWidth = typedArray.getDimensionPixelSize(R.styleable.MessageList_avatarWidth,
                context.getResources().getDimensionPixelSize(R.dimen.avatar_width));
        style.avatarHeight = typedArray.getDimensionPixelSize(R.styleable.MessageList_avatarHeight,
                context.getResources().getDimensionPixelSize(R.dimen.avatar_height));

        style.receiveBubbleDrawable = typedArray.getResourceId(R.styleable.MessageList_receiveBubbleDrawable, -1);
        style.receiveBubbleColor = typedArray.getColor(R.styleable.MessageList_receiveBubbleColor,
                ContextCompat.getColor(context, R.color.receive_bubble_default_color));
        style.receiveBubblePressedColor = typedArray.getColor(R.styleable.MessageList_receiveBubblePressedColor,
                ContextCompat.getColor(context, R.color.receive_bubble_pressed_color));
        style.receiveBubbleSelectedColor = typedArray.getColor(R.styleable.MessageList_receiveBubbleSelectedColor,
                ContextCompat.getColor(context, R.color.receive_bubble_selected_color));
        style.receiveBubbleTextSize = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveTextSize,
                context.getResources().getDimensionPixelSize(R.dimen.receive_text_size));
        style.receiveBubbleTextColor = typedArray.getColor(R.styleable.MessageList_receiveTextColor,
                ContextCompat.getColor(context, R.color.receive_text_color));
        style.receiveBubblePaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveBubblePaddingLeft,
                context.getResources().getDimensionPixelSize(R.dimen.receive_text_padding_left));
        style.receiveBubblePaddingTop = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveBubblePaddingTop,
                context.getResources().getDimensionPixelSize(R.dimen.receive_text_padding_top));
        style.receiveBubblePaddingRight = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveBubblePaddingRight,
                context.getResources().getDimensionPixelSize(R.dimen.receive_text_padding_right));
        style.receiveBubblePaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MessageList_receiveBubblePaddingBottom,
                context.getResources().getDimensionPixelSize(R.dimen.receive_text_padding_bottom));

        style.sendBubbleDrawable = typedArray.getResourceId(R.styleable.MessageList_sendBubbleDrawable, -1);
        style.sendBubbleColor = typedArray.getColor(R.styleable.MessageList_sendBubbleColor,
                ContextCompat.getColor(context, R.color.send_bubble_default_color));
        style.sendBubblePressedColor = typedArray.getColor(R.styleable.MessageList_sendBubblePressedColor,
                ContextCompat.getColor(context, R.color.send_bubble_pressed_color));
        style.sendBubbleSelectedColor = typedArray.getColor(R.styleable.MessageList_sendBubbleSelectedColor,
                ContextCompat.getColor(context, R.color.send_bubble_selected_color));
        style.sendBubbleTextSize = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendTextSize,
                context.getResources().getDimensionPixelSize(R.dimen.send_text_size));
        style.sendBubbleTextColor = typedArray.getColor(R.styleable.MessageList_sendTextColor,
                ContextCompat.getColor(context, R.color.send_text_color));
        style.sendBubblePaddingLeft = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendBubblePaddingLeft,
                context.getResources().getDimensionPixelSize(R.dimen.send_text_padding_left));
        style.sendBubblePaddingTop = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendBubblePaddingTop,
                context.getResources().getDimensionPixelSize(R.dimen.send_text_padding_top));
        style.sendBubblePaddingRight = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendBubblePaddingRight,
                context.getResources().getDimensionPixelSize(R.dimen.send_text_padding_right));
        style.sendBubblePaddingBottom = typedArray.getDimensionPixelSize(R.styleable.MessageList_sendBubblePaddingBottom,
                context.getResources().getDimensionPixelSize(R.dimen.send_text_padding_bottom));

        typedArray.recycle();
        return style;
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

    public int getDateTextSize() {
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

    public Drawable getReceiveBubbleDrawable() {
        if (receiveBubbleDrawable == -1) {
            return getMessageSelector(receiveBubbleColor, receiveBubbleSelectedColor, receiveBubblePressedColor,
                    R.drawable.receive_bubble);
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

    public int getReceiveBubbleTextSize() {
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

    public Drawable getSendBubbleDrawable() {
        if (sendBubbleDrawable == -1) {
            return getMessageSelector(sendBubbleColor, sendBubbleSelectedColor, sendBubblePressedColor,
                    R.drawable.send_bubble);
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

    public int getSendBubbleTextSize() {
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
}
