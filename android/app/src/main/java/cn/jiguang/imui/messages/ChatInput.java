package cn.jiguang.imui.messages;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.jiguang.imui.R;

public class ChatInput extends LinearLayout implements View.OnClickListener, TextWatcher {

    private EditText mChatInput;
    private ImageButton mSwitchBtn;
    private Button mSendBtn;
    private CharSequence mInput;
    private Space mInputMarginLeft;
    private Space mInputMarginRight;
    private ImageButton mMoreMenuBtn;
    private RecordVoiceButton mVoiceBtn;
    private InputListener mListener;
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
        mInputMarginLeft = (Space) findViewById(R.id.input_margin_left);
        mInputMarginRight = (Space) findViewById(R.id.input_margin_right);
        mSwitchBtn = (ImageButton) findViewById(R.id.switch_input_ib);
        mSendBtn = (Button) findViewById(R.id.send_msg_btn);
        mMoreMenuBtn = (ImageButton) findViewById(R.id.more_menu_btn);
        mVoiceBtn = (RecordVoiceButton) findViewById(R.id.voice_btn);

        mChatInput.addTextChangedListener(this);
        mSendBtn.setOnClickListener(this);
        mSwitchBtn.setOnClickListener(this);
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
        setCursor(mStyle.getInputCursorDrawable());
        mSwitchBtn.setImageDrawable(mStyle.getSwitchVoiceIcon());
        mSwitchBtn.setBackground(mStyle.getSwitchInputBg());
        mSendBtn.setBackground(mStyle.getSendBtnBg());

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

    public void setInputListener(InputListener listener) {
        mListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        mInput = s;
        if (mInput.length() > 0) {
            mSendBtn.setVisibility(VISIBLE);
            mMoreMenuBtn.setVisibility(GONE);
        } else {
            mSendBtn.setVisibility(GONE);
            mMoreMenuBtn.setVisibility(VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_msg_btn:
                if (onSubmit()) {
                    mChatInput.setText("");
                }
                break;
            case R.id.switch_input_ib:
                if (mChatInput.getVisibility() == VISIBLE) {
                    mSwitchBtn.setImageDrawable(mStyle.getSwitchInputIcon());
                    mChatInput.setVisibility(GONE);
                    mVoiceBtn.setVisibility(VISIBLE);
                } else {
                    mVoiceBtn.setVisibility(GONE);
                    mChatInput.setVisibility(VISIBLE);
                    mSwitchBtn.setImageDrawable(mStyle.getSwitchVoiceIcon());
                }
                break;

        }
    }

    private boolean onSubmit() {
        return mListener != null && mListener.onSubmit(mInput);
    }

    public interface InputListener {


        boolean onSubmit(CharSequence input);
    }
}
