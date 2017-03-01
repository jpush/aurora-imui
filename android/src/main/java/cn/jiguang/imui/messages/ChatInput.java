package cn.jiguang.imui.messages;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.jiguang.imui.R;

public class ChatInput extends LinearLayout implements View.OnClickListener, TextWatcher {

    private EditText mChatInput;
    private ImageButton mSendBtn;
    private TextView mSendCountTv;
    private CharSequence mInput;
    private Space mInputMarginLeft;
    private Space mInputMarginRight;
    private ImageButton mVoiceBtn;
    private ImageButton mPhotoBtn;
    private ImageButton mCameraBtn;
    private OnMenuClickListener mListener;
    private ChatInputStyle mStyle;

    public ChatInput(Context context) {
        super(context);
        init(context);
    }

    public ChatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChatInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_chat_input, this);
        mChatInput = (EditText) findViewById(R.id.chat_input_et);
        mVoiceBtn = (ImageButton) findViewById(R.id.voice_ib);
        mPhotoBtn = (ImageButton) findViewById(R.id.photo_ib);
        mSendBtn = (ImageButton) findViewById(R.id.send_msg_ib);
        mSendCountTv = (TextView) findViewById(R.id.send_count_tv);
        mCameraBtn = (ImageButton) findViewById(R.id.camera_ib);
        mInputMarginLeft = (Space) findViewById(R.id.input_margin_left);
        mInputMarginRight = (Space) findViewById(R.id.input_margin_right);
        mChatInput.addTextChangedListener(this);
        mSendBtn.setOnClickListener(this);
        mVoiceBtn.setOnClickListener(this);
    }

    private void init(Context context, AttributeSet attrs) {
        init(context);
        mStyle = ChatInputStyle.parse(context, attrs);

        mChatInput.setMaxLines(mStyle.getInputMaxLines());
        mChatInput.setHint(mStyle.getInputHint());
        mChatInput.setText(mStyle.getInputText());
        mChatInput.setTextSize(TypedValue.COMPLEX_UNIT_PX, mStyle.getInputTextSize());
        mChatInput.setTextColor(mStyle.getInputTextColor());
        mChatInput.setHintTextColor(mStyle.getInputHintColor());
        mChatInput.setBackground(mStyle.getInputEditTextBg());
        mInputMarginLeft.getLayoutParams().width = mStyle.getInputMarginLeft();
        mInputMarginRight.getLayoutParams().width = mStyle.getInputMarginRight();
//        setCursor(mStyle.getInputCursorDrawable());
        mVoiceBtn.setImageDrawable(mStyle.getVoiceBtnIcon());
        mVoiceBtn.setBackground(mStyle.getVoiceBtnBg());
        mPhotoBtn.setBackground(mStyle.getPhotoBtnBg());
        mPhotoBtn.setImageDrawable(mStyle.getPhotoBtnIcon());
        mCameraBtn.setBackground(mStyle.getCameraBtnBg());
        mCameraBtn.setImageDrawable(mStyle.getCameraBtnIcon());
        mSendBtn.setBackground(mStyle.getSendBtnBg());
        mSendBtn.setImageDrawable(mStyle.getSendBtnIcon());
        mSendCountTv.setBackground(mStyle.getSendCountBg());

        if (getPaddingLeft() == 0
                && getPaddingRight() == 0
                && getPaddingTop() == 0
                && getPaddingBottom() == 0) {
            setPadding(
                    mStyle.getInputDefaultPaddingLeft(),
                    mStyle.getInputDefaultPaddingTop(),
                    mStyle.getInputDefaultPaddingRight(),
                    mStyle.getInputDefaultPaddingBottom()
            );
        }

    }

    private void setCursor(Drawable drawable) {
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(mChatInput, drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMenuClickListener(OnMenuClickListener listener) {
        mListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        mInput = s;
        if (mInput.length() > 0) {
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send_pres));
        } else {
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send));
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public EditText getInputView() {
        return mChatInput;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_msg_ib) {
            if (onSubmit()) {
                mChatInput.setText("");
            }
        } else if (view.getId() == R.id.voice_ib) {
            if (mListener != null) {
                mListener.onVoiceClick();
            }
        } else if (view.getId() == R.id.photo_ib) {
            if (mListener != null) {
                mListener.onPhotoClick();
            }
        } else {
            if (mListener != null) {
                mListener.onCameraClick();
            }
        }
    }

    private boolean onSubmit() {
        return mListener != null && mListener.onSubmit(mInput);
    }

    public interface OnMenuClickListener {

        /**
         * Fires when send button is on click.
         * @param input Input content
         * @return boolean
         */
        boolean onSubmit(CharSequence input);

        /**
         * Fires when voice button is on click.
         */
        void onVoiceClick();

        /**
         * Fires when photo button is on click.
         */
        void onPhotoClick();

        /**
         * Fires when camera button is on click.
         */
        void onCameraClick();
    }
}
