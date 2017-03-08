package cn.jiguang.imui.messages;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import cn.jiguang.imui.R;
import cn.jiguang.imui.utils.ImgBrowserViewPager;

public class ChatInputView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private EditText mChatInput;
    private ImageButton mSendBtn;
    private TextView mSendCountTv;
    private CharSequence mInput;
    private Space mInputMarginLeft;
    private Space mInputMarginRight;
    private ImageButton mVoiceBtn;
    private ImageButton mPhotoBtn;
    private ImageButton mCameraBtn;
    private FrameLayout mMenuContainer;
    private LinearLayout mRecordVoiceLl;
    private LinearLayout mPhotoLl;
    private RecordVoiceButton mRecordVoiceBtn;
    private ImgBrowserViewPager mImgViewPager;
    private OnMenuClickListener mListener;
    private ChatInputStyle mStyle;
    private InputMethodManager mImm;
    private Window mWindow;

    private int mMenuHeight = 0;
    private boolean mShowSoftInput = false;
    private boolean mShowMenu = false;

    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;

    public ChatInputView(Context context) {
        super(context);
        init(context);
    }

    public ChatInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChatInputView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mMenuContainer = (FrameLayout) findViewById(R.id.menu_container);
        mRecordVoiceLl = (LinearLayout) findViewById(R.id.record_voice_container);
        mPhotoLl = (LinearLayout) findViewById(R.id.photo_container);
        mRecordVoiceBtn = (RecordVoiceButton) findViewById(R.id.record_btn);
        mImgViewPager = (ImgBrowserViewPager) findViewById(R.id.photo_vp);

        mMenuContainer.setVisibility(GONE);
        mChatInput.addTextChangedListener(this);
        mSendBtn.setOnClickListener(this);
        mVoiceBtn.setOnClickListener(this);
        this.mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = ((Activity)context).getWindow();
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

    public RecordVoiceButton getRecordVoiceButton() {
        return mRecordVoiceBtn;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_msg_ib) {
            if (onSubmit()) {
                mChatInput.setText("");
            }
        } else {
            if (!mShowMenu) {
                showMenuLayout();
                dismissSoftInput();
            } else {
                dismissMenuLayout();
            }

            if (view.getId() == R.id.voice_ib) {
                if (mListener != null) {
                    mListener.onVoiceClick();
                }
                showRecordVoiceLayout();
            } else if (view.getId() == R.id.photo_ib) {
                if (mListener != null) {
                    mListener.onPhotoClick();
                }
                showPhotoLayout();
            } else if (view.getId() == R.id.camera_ib) {
                if (mListener != null) {
                    mListener.onCameraClick();
                }

            }
        }
    }

    public void dismissMenuLayout() {
        mMenuContainer.setVisibility(GONE);
        mShowMenu = false;
    }

    public void showMenuLayout() {
        mMenuContainer.setVisibility(VISIBLE);
        mShowMenu = true;
    }

    public void showRecordVoiceLayout() {
        mRecordVoiceLl.setVisibility(VISIBLE);
    }

    public void dismissRecordVoiceLayout() {
        mRecordVoiceLl.setVisibility(GONE);
    }

    public void showPhotoLayout() {
        mPhotoLl.setVisibility(VISIBLE);
    }

    public void dismissPhotoLayout() {
        mPhotoLl.setVisibility(GONE);
    }

    /**
     * Set menu container's height, invoke this method once the menu was initialized.
     * @param height Height of menu, set same height as soft keyboard so that display to perfection.
     */
    public void setMenuContainerHeight(int height) {
        if(height > 0){
            mMenuHeight = height;
            mMenuContainer.setLayoutParams(new LinearLayout
                    .LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
        }
    }

    private boolean onSubmit() {
        return mListener != null && mListener.onSubmit(mInput);
    }

    public boolean getSoftInputState() {
        return mShowSoftInput;
    }

    public void setSoftInputState(boolean state) {
        mShowSoftInput = state;
    }

    public boolean getMenuState() {
        return mShowMenu;
    }

    /**
     * Menu items' callbacks
     */
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


    public void showSoftInputAndDismissMenu() {
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dismissMenuLayout();
        mChatInput.requestFocus();
        if (mImm != null) {
            mImm.showSoftInput(mChatInput, InputMethodManager.SHOW_FORCED);//强制显示键盘
        }
        mShowSoftInput = true;
        setMenuContainerHeight(mMenuHeight);
    }

    public void dismissSoftInput() {
        if (mShowSoftInput) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mChatInput.getWindowToken(), 0);
                mShowSoftInput = false;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
