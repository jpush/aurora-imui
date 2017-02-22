package cn.jiguang.imui.messages;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.Style;


public class ChatInputStyle extends Style {

    private static final int DEFAULT_MAX_LINES = 4;

    private Drawable switchInputBg;
    private Drawable switchVoiceIcon;
    private Drawable switchInputIcon;
    private Drawable inputEditTextBg;
    private int inputMarginLeft;
    private int inputMarginRight;
    private int inputMaxLines;
    private String inputHint;
    private String inputText;
    private int inputTextSize;
    private int inputTextColor;
    private int inputHintColor;
    private Drawable inputCursorDrawable;
    private Drawable sendBtnBg;

    private int inputDefaultPaddingLeft;
    private int inputDefaultPaddingRight;
    private int inputDefaultPaddingTop;
    private int inputDefaultPaddingBottom;

    public static ChatInputStyle parse(Context context, AttributeSet attrs) {
        ChatInputStyle style = new ChatInputStyle(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChatInput);
        style.switchInputBg = typedArray.getDrawable(R.styleable.ChatInput_switchInputBg);
        style.switchVoiceIcon = typedArray.getDrawable(R.styleable.ChatInput_switchVoiceIcon);
        style.switchInputIcon = typedArray.getDrawable(R.styleable.ChatInput_switchInputIcon);
        style.inputEditTextBg = typedArray.getDrawable(R.styleable.ChatInput_inputEditTextBg);
        style.inputMarginLeft = typedArray.getDimensionPixelSize(R.styleable.ChatInput_inputMarginLeft,
                style.getDimension(R.dimen.input_margin_left));
        style.inputMarginRight = typedArray.getDimensionPixelSize(R.styleable.ChatInput_inputMarginRight,
                style.getDimension(R.dimen.input_margin_right));
        style.inputMaxLines = typedArray.getInt(R.styleable.ChatInput_inputMaxLines, DEFAULT_MAX_LINES);
        style.inputHint = typedArray.getString(R.styleable.ChatInput_inputHint);
        style.inputText = typedArray.getString(R.styleable.ChatInput_inputText);
        style.inputTextSize = typedArray.getDimensionPixelSize(R.styleable.ChatInput_inputTextSize,
                style.getDimension(R.dimen.input_text_size));
        style.inputTextColor = typedArray.getColor(R.styleable.ChatInput_inputTextColor,
                style.getColor(R.color.input_text_color));
        style.inputHintColor = typedArray.getColor(R.styleable.ChatInput_inputHintColor,
                style.getColor(R.color.input_hint_color));
        style.inputCursorDrawable = typedArray.getDrawable(R.styleable.ChatInput_inputCursorDrawable);
        style.sendBtnBg = typedArray.getDrawable(R.styleable.ChatInput_sendBtnBg);
        typedArray.recycle();

        style.inputDefaultPaddingLeft = style.getDimension(R.dimen.input_padding_left);
        style.inputDefaultPaddingRight = style.getDimension(R.dimen.input_padding_right);
        style.inputDefaultPaddingTop = style.getDimension(R.dimen.input_padding_top);
        style.inputDefaultPaddingBottom = style.getDimension(R.dimen.input_padding_bottom);

        return style;
    }

    private ChatInputStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Drawable getSwitchInputBg() {
        return this.switchInputBg;
    }

    public Drawable getSwitchVoiceIcon() {
        return this.switchVoiceIcon;
    }

    public Drawable getSwitchInputIcon() {
        return this.switchInputIcon;
    }

    public Drawable getInputEditTextBg() {
        return this.inputEditTextBg;
    }

    public int getInputMarginLeft() {
        return this.inputMarginLeft;
    }

    public int getInputMarginRight() {
        return this.inputMarginRight;
    }

    public int getInputMaxLines() {
        return this.inputMaxLines;
    }

    public String getInputHint() {
        return this.inputHint;
    }

    public String getInputText() {
        return this.inputText;
    }

    public int getInputTextSize() {
        return this.inputTextSize;
    }

    public int getInputTextColor() {
        return this.inputTextColor;
    }

    public int getInputHintColor() {
        return this.inputHintColor;
    }

    public Drawable getInputCursorDrawable() {
        return this.inputCursorDrawable;
    }

    public Drawable getSendBtnBg() {
        return this.sendBtnBg;
    }

    public int getInputDefaultPaddingLeft() {
        return inputDefaultPaddingLeft;
    }

    public int getInputDefaultPaddingRight() {
        return inputDefaultPaddingRight;
    }

    public int getInputDefaultPaddingTop() {
        return inputDefaultPaddingTop;
    }

    public int getInputDefaultPaddingBottom() {
        return inputDefaultPaddingBottom;
    }
}
