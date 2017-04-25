package cn.jiguang.imui.chatinput;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;



public class ChatInputStyle extends Style {

    private static final int DEFAULT_MAX_LINES = 4;

    private Drawable inputEditTextBg;

    private int inputMarginLeft;
    private int inputMarginRight;
    private int inputMaxLines;

    private String inputText;
    private int inputTextSize;
    private int inputTextColor;

    private String inputHint;
    private int inputHintColor;

    private int inputDefaultPaddingLeft;
    private int inputDefaultPaddingRight;
    private int inputDefaultPaddingTop;
    private int inputDefaultPaddingBottom;

    private Drawable inputCursorDrawable;

    private Drawable voiceBtnBg;
    private Drawable voiceBtnIcon;

    private Drawable photoBtnBg;
    private Drawable photoBtnIcon;

    private Drawable cameraBtnBg;
    private Drawable cameraBtnIcon;

    private Drawable sendBtnBg;
    private Drawable sendBtnIcon;
    private Drawable sendCountBg;

    public static ChatInputStyle parse(Context context, AttributeSet attrs) {
        ChatInputStyle style = new ChatInputStyle(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChatInputView);
        style.inputEditTextBg = typedArray.getDrawable(R.styleable.ChatInputView_inputEditTextBg);
        style.inputMarginLeft = typedArray.getDimensionPixelSize(R.styleable.ChatInputView_inputMarginLeft,
                style.getDimension(R.dimen.aurora_margin_input_left));
        style.inputMarginRight = typedArray.getDimensionPixelSize(R.styleable.ChatInputView_inputMarginRight,
                style.getDimension(R.dimen.aurora_margin_input_right));
        style.inputMaxLines = typedArray.getInt(R.styleable.ChatInputView_inputMaxLines, DEFAULT_MAX_LINES);
        style.inputHint = typedArray.getString(R.styleable.ChatInputView_inputHint);
        style.inputText = typedArray.getString(R.styleable.ChatInputView_inputText);
        style.inputTextSize = typedArray.getDimensionPixelSize(R.styleable.ChatInputView_inputTextSize,
                style.getDimension(R.dimen.aurora_textsize_input));
        style.inputTextColor = typedArray.getColor(R.styleable.ChatInputView_inputTextColor,
                style.getColor(R.color.aurora_text_color_input));
        style.inputHintColor = typedArray.getColor(R.styleable.ChatInputView_inputHintColor,
                style.getColor(R.color.aurora_hint_color_input));
        style.inputCursorDrawable = typedArray.getDrawable(R.styleable.ChatInputView_inputCursorDrawable);

        style.voiceBtnBg = typedArray.getDrawable(R.styleable.ChatInputView_voiceBtnBg);
        style.voiceBtnIcon = typedArray.getDrawable(R.styleable.ChatInputView_voiceBtnIcon);
        style.photoBtnBg = typedArray.getDrawable(R.styleable.ChatInputView_photoBtnBg);
        style.photoBtnIcon = typedArray.getDrawable(R.styleable.ChatInputView_photoBtnIcon);
        style.cameraBtnBg = typedArray.getDrawable(R.styleable.ChatInputView_cameraBtnBg);
        style.cameraBtnIcon = typedArray.getDrawable(R.styleable.ChatInputView_cameraBtnIcon);
        style.sendBtnBg = typedArray.getDrawable(R.styleable.ChatInputView_sendBtnBg);
        style.sendBtnIcon = typedArray.getDrawable(R.styleable.ChatInputView_sendBtnIcon);
        style.sendCountBg = typedArray.getDrawable(R.styleable.ChatInputView_sendCountBg);
        typedArray.recycle();

        style.inputDefaultPaddingLeft = style.getDimension(R.dimen.aurora_padding_input_left);
        style.inputDefaultPaddingRight = style.getDimension(R.dimen.aurora_padding_input_right);
        style.inputDefaultPaddingTop = style.getDimension(R.dimen.aurora_padding_input_top);
        style.inputDefaultPaddingBottom = style.getDimension(R.dimen.aurora_padding_input_bottom);

        return style;
    }

    private ChatInputStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Drawable getVoiceBtnBg() {
        return this.voiceBtnBg;
    }

    public Drawable getVoiceBtnIcon() {
        return this.voiceBtnIcon;
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

    public Drawable getPhotoBtnBg() {
        return photoBtnBg;
    }

    public Drawable getPhotoBtnIcon() {
        return photoBtnIcon;
    }

    public Drawable getCameraBtnBg() {
        return cameraBtnBg;
    }

    public Drawable getCameraBtnIcon() {
        return cameraBtnIcon;
    }

    public Drawable getSendBtnIcon() {
        return sendBtnIcon;
    }

    public Drawable getSendBtnBg() {
        return this.sendBtnBg;
    }

    public Drawable getSendCountBg() {
        if (sendCountBg == null) {
            return ContextCompat.getDrawable(mContext, R.drawable.aurora_menuitem_send_count_bg);
        }
        return this.sendCountBg;
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
