package cn.jiguang.imui.chatinput;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;



public class ChatInputStyle extends Style {

    private static final int DEFAULT_MAX_LINES = 4;

    private int inputEditTextBg;

    private int inputMarginLeft;
    private int inputMarginRight;
    private int inputMaxLines;

    private String inputText;
    private int inputTextSize;
    private int inputTextColor;

    private String inputHint;
    private int inputHintColor;

    private int inputPaddingLeft;
    private int inputPaddingRight;
    private int inputPaddingTop;
    private int inputPaddingBottom;

    private int inputCursorDrawable;

    private Drawable voiceBtnBg;
    private int voiceBtnIcon;

    private Drawable photoBtnBg;
    private int photoBtnIcon;

    private Drawable cameraBtnBg;
    private int cameraBtnIcon;

    private Drawable sendBtnBg;
    private int sendBtnIcon;
    private int sendBtnPressedIcon;
    private Drawable sendCountBg;
    private boolean showSelectAlbumBtn;

    public static ChatInputStyle parse(Context context, AttributeSet attrs) {
        ChatInputStyle style = new ChatInputStyle(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ChatInputView);
        style.inputEditTextBg = typedArray.getResourceId(R.styleable.ChatInputView_inputEditTextBg,
                R.drawable.aurora_edittext_bg);
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
        style.inputCursorDrawable = typedArray.getResourceId(R.styleable.ChatInputView_inputCursorDrawable,
                R.drawable.aurora_edittext_cursor_bg);

        style.voiceBtnBg = typedArray.getDrawable(R.styleable.ChatInputView_voiceBtnBg);
        style.voiceBtnIcon = typedArray.getResourceId(R.styleable.ChatInputView_voiceBtnIcon, R.drawable.aurora_menuitem_mic);
        style.photoBtnBg = typedArray.getDrawable(R.styleable.ChatInputView_photoBtnBg);
        style.photoBtnIcon = typedArray.getResourceId(R.styleable.ChatInputView_photoBtnIcon, R.drawable.aurora_menuitem_photo);
        style.cameraBtnBg = typedArray.getDrawable(R.styleable.ChatInputView_cameraBtnBg);
        style.cameraBtnIcon = typedArray.getResourceId(R.styleable.ChatInputView_cameraBtnIcon, R.drawable.aurora_menuitem_camera);
        style.sendBtnBg = typedArray.getDrawable(R.styleable.ChatInputView_sendBtnBg);
        style.sendBtnIcon = typedArray.getResourceId(R.styleable.ChatInputView_sendBtnIcon, R.drawable.aurora_menuitem_send);
        style.sendBtnPressedIcon = typedArray.getResourceId(R.styleable.ChatInputView_sendBtnPressedIcon, R.drawable.aurora_menuitem_send_pres);
        style.sendCountBg = typedArray.getDrawable(R.styleable.ChatInputView_sendCountBg);
        style.showSelectAlbumBtn = typedArray.getBoolean(R.styleable.ChatInputView_showSelectAlbum, true);
        style.inputPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.ChatInputView_inputPaddingLeft,
                style.getDimension(R.dimen.aurora_padding_input_left));
        style.inputPaddingTop = typedArray.getDimensionPixelSize(R.styleable.ChatInputView_inputPaddingTop,
                style.getDimension(R.dimen.aurora_padding_input_top));
        style.inputPaddingRight = typedArray.getDimensionPixelSize(R.styleable.ChatInputView_inputPaddingRight,
                style.getDimension(R.dimen.aurora_padding_input_right));
        style.inputPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.ChatInputView_inputPaddingBottom,
                style.getDimension(R.dimen.aurora_padding_input_bottom));
        typedArray.recycle();
        return style;
    }

    private ChatInputStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Drawable getVoiceBtnBg() {
        return this.voiceBtnBg;
    }

    public int getVoiceBtnIcon() {
        return this.voiceBtnIcon;
    }

    public int getInputEditTextBg() {
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

    public int getInputCursorDrawable() {
        return this.inputCursorDrawable;
    }

    public Drawable getPhotoBtnBg() {
        return photoBtnBg;
    }

    public int getPhotoBtnIcon() {
        return photoBtnIcon;
    }

    public Drawable getCameraBtnBg() {
        return cameraBtnBg;
    }

    public int getCameraBtnIcon() {
        return cameraBtnIcon;
    }

    public int getSendBtnIcon() {
        return sendBtnIcon;
    }

    public int getSendBtnPressedIcon() {
        return this.sendBtnPressedIcon;
    }

    public void setSendBtnPressedIcon(int resId) {
        this.sendBtnPressedIcon = resId;
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

    public int getInputPaddingLeft() {
        return inputPaddingLeft;
    }

    public int getInputPaddingRight() {
        return inputPaddingRight;
    }

    public int getInputPaddingTop() {
        return inputPaddingTop;
    }

    public int getInputPaddingBottom() {
        return inputPaddingBottom;
    }

    public boolean getShowSelectAlbum() {
        return this.showSelectAlbumBtn;
    }
}
