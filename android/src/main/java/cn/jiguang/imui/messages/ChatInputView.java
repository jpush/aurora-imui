package cn.jiguang.imui.messages;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.jiguang.imui.BuildConfig;
import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.FileItem;
import cn.jiguang.imui.utils.ImgBrowserViewPager;

public class ChatInputView extends LinearLayout implements View.OnClickListener, TextWatcher,
        PhotoAdapter.OnFileSelectedListener {

    private EditText mChatInput;
    private FrameLayout mSendBtnFl;
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
    private FrameLayout mPhotoFl;
    private RecordVoiceButton mRecordVoiceBtn;
    private ImgBrowserViewPager mImgViewPager;
    private ProgressBar mProgressBar;
    private ImageView checkedIcon;
    private ImageButton mAlbumBtn;
    private OnMenuClickListener mListener;
    private ChatInputStyle mStyle;
    private InputMethodManager mImm;
    private Window mWindow;
    private int mLastClickId = 0;

    private int mMenuHeight = 300;
    private boolean mShowSoftInput = false;
    private PhotoAdapter mAdapter;
    private List<String> mSendFiles = new ArrayList<>();
    private List<FileItem> mPhotos = new ArrayList<>();
    private MyHandler myHandler = new MyHandler(this);

    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;
    private final static int SCAN_OK = 1;
    private final static int SCAN_ERROR = 0;

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
        mSendBtnFl = (FrameLayout) findViewById(R.id.send_btn_fl);
        mSendBtn = (ImageButton) findViewById(R.id.send_msg_ib);
        mSendCountTv = (TextView) findViewById(R.id.send_count_tv);
        mCameraBtn = (ImageButton) findViewById(R.id.camera_ib);
        mInputMarginLeft = (Space) findViewById(R.id.input_margin_left);
        mInputMarginRight = (Space) findViewById(R.id.input_margin_right);
        mMenuContainer = (FrameLayout) findViewById(R.id.menu_container);
        mRecordVoiceLl = (LinearLayout) findViewById(R.id.record_voice_container);
        mPhotoFl = (FrameLayout) findViewById(R.id.photo_container);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        checkedIcon = (ImageView) findViewById(R.id.checked_iv);
        mAlbumBtn = (ImageButton) findViewById(R.id.album_ib);
        mRecordVoiceBtn = (RecordVoiceButton) findViewById(R.id.record_btn);
        mImgViewPager = (ImgBrowserViewPager) findViewById(R.id.photo_vp);

        mImgViewPager.setOffscreenPageLimit(3);
        mImgViewPager.setPageMargin(40);
        mMenuContainer.setVisibility(GONE);
        mChatInput.addTextChangedListener(this);
        mSendBtnFl.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mVoiceBtn.setOnClickListener(this);
        mPhotoBtn.setOnClickListener(this);
        mCameraBtn.setOnClickListener(this);
        this.mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = ((Activity) context).getWindow();

        mChatInput.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !mShowSoftInput) {
                    mShowSoftInput = true;
                    invisibleMenuLayout();
                    mSendFiles.clear();
                    mSendCountTv.setVisibility(GONE);
                    setSendBtnBg();
                }
                return false;
            }
        });

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
        setSendBtnBg();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void setSendBtnBg() {
        if (mInput.length() > 0) {
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send_pres));
            if (Build.VERSION.SDK_INT >= 21) {
                mSendBtn.setElevation(getContext().getResources().getDimension(R.dimen.send_btn_shadow));
            }
        } else {
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send));
            if (Build.VERSION.SDK_INT >= 21) {
                mSendBtn.setElevation(0);
            }
        }
    }

    public EditText getInputView() {
        return mChatInput;
    }

    public RecordVoiceButton getRecordVoiceButton() {
        return mRecordVoiceBtn;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.send_btn_fl || view.getId() == R.id.send_msg_ib) {
            if (mSendFiles.size() > 0) {
                mListener.onSendFiles(mSendFiles);
                mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send));
                mSendCountTv.setVisibility(GONE);
                dismissMenuLayout();
                mAdapter.resetCheckedState();
            } else if (onSubmit()) {
                mChatInput.setText("");
            }
        } else {
            if (mMenuContainer.getVisibility() != VISIBLE) {
                dismissSoftInputAndShowMenu();
            } else if (view.getId() == mLastClickId) {
                dismissMenuAndResetSoftMode();
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
                if (mPhotos.size() == 0) {
                    getPhotos();
                }
            } else if (view.getId() == R.id.camera_ib) {
                if (mListener != null) {
                    mListener.onCameraClick();
                }

            }
            mLastClickId = view.getId();
        }
    }

    public void dismissMenuLayout() {
        mMenuContainer.setVisibility(GONE);
    }

    public void invisibleMenuLayout() {
        mMenuContainer.setVisibility(INVISIBLE);
    }

    public void showMenuLayout() {
        mMenuContainer.setVisibility(VISIBLE);
    }

    public void showRecordVoiceLayout() {
        mPhotoFl.setVisibility(GONE);
        mRecordVoiceLl.setVisibility(VISIBLE);
    }

    public void dismissRecordVoiceLayout() {
        mRecordVoiceLl.setVisibility(GONE);
    }

    public void showPhotoLayout() {
        mRecordVoiceLl.setVisibility(GONE);
        mPhotoFl.setVisibility(VISIBLE);
    }

    public void dismissPhotoLayout() {
        mPhotoFl.setVisibility(GONE);
    }

    /**
     * Set menu container's height, invoke this method once the menu was initialized.
     *
     * @param height Height of menu, set same height as soft keyboard so that display to perfection.
     */
    public void setMenuContainerHeight(int height) {
        if (height > 0 && height != mMenuHeight) {
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

    public int getMenuState() {
        return mMenuContainer.getVisibility();
    }

    @Override
    public void onFileSelected() {
        mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send_pres));
        if (mSendFiles.size() == 1) {
            addSelectedAnimation(mSendBtn);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            mSendBtn.setElevation(getContext().getResources().getDimension(R.dimen.send_btn_shadow));
        }
        mSendCountTv.setText(mSendFiles.size() + "");
        mSendCountTv.setVisibility(VISIBLE);
    }

    @Override
    public void onFileDeselected() {
        if (mSendFiles.size() > 0) {
            mSendCountTv.setVisibility(VISIBLE);
            mSendCountTv.setText(mSendFiles.size() + "");
        } else {
            mSendCountTv.setVisibility(GONE);
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send));
            if (Build.VERSION.SDK_INT >= 21) {
                mSendBtn.setElevation(0);
            }
        }
    }

    private void addSelectedAnimation(View view) {
        float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mSendCountTv.bringToFront();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    /**
     * Menu items' callbacks
     */
    public interface OnMenuClickListener {

        /**
         * Fires when send button is on click.
         *
         * @param input Input content
         * @return boolean
         */
        boolean onSubmit(CharSequence input);

        /**
         * Files when send photos or videos.
         * @param list File paths that will send.
         */
        void onSendFiles(List<String> list);

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

    public void dismissMenuAndResetSoftMode() {
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dismissMenuLayout();
        mChatInput.requestFocus();
    }

    public void dismissSoftInputAndShowMenu() {
        //隐藏软键盘
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); // 隐藏软键盘
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        showMenuLayout();
        if (mImm != null) {
            mImm.hideSoftInputFromWindow(mChatInput.getWindowToken(), 0); //强制隐藏键盘
        }
        setMenuContainerHeight(mMenuHeight);
        mShowSoftInput = false;
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

    private void getPhotos() {
        mProgressBar.setVisibility(VISIBLE);
        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = getContext().getContentResolver();
                String[] projection = new String[]{MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.SIZE};
                Cursor cursor = contentResolver.query(imageUri, projection, null, null,
                        MediaStore.Images.Media.DATE_MODIFIED + " desc");
                if (cursor == null || cursor.getCount() == 0) {
                    myHandler.sendEmptyMessage(SCAN_ERROR);
                } else {
                    while (cursor.moveToNext()) {
                        //获取图片的路径
                        String path = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DATA));
                        String fileName = cursor.getString(cursor
                                .getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                        String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                        FileItem fileItem = new FileItem(path, fileName, size, null);
                        mPhotos.add(fileItem);
                    }
                    cursor.close();
                    //通知Handler扫描图片完成
                    myHandler.sendEmptyMessage(SCAN_OK);
                }
            }
        }).start();

    }

    private static class MyHandler extends Handler {
        private final WeakReference<ChatInputView> mChatInputView;

        public MyHandler(ChatInputView view) {
            mChatInputView = new WeakReference<ChatInputView>(view);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChatInputView chatInputView = mChatInputView.get();
            if (chatInputView != null) {
                switch (msg.what) {
                    case SCAN_OK:
                        //关闭进度条
                        chatInputView.mProgressBar.setVisibility(GONE);
                        chatInputView.mAdapter = new PhotoAdapter(chatInputView.mImgViewPager, chatInputView.mPhotos);
                        chatInputView.mAdapter.setSelectedFiles(chatInputView.mSendFiles);
                        chatInputView.mAdapter.setOnPhotoSelectedListener(chatInputView);
                        chatInputView.mImgViewPager.setAdapter(chatInputView.mAdapter);
                        break;
                    case SCAN_ERROR:
                        chatInputView.mProgressBar.setVisibility(GONE);
                        Toast.makeText(chatInputView.getContext(),
                                chatInputView.getContext().getString(R.string.sdcard_not_prepare_toast),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    }

}
