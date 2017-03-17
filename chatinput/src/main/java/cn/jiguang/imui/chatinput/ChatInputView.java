package cn.jiguang.imui.chatinput;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class ChatInputView extends LinearLayout implements View.OnClickListener, TextWatcher,
        PhotoAdapter.OnFileSelectedListener, OnCameraCallbackListener {

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
    private RecordControllerView mRecordControllerView;
    private Chronometer mChronometer;
    private RecordVoiceButton mRecordVoiceBtn;

    private RecyclerView mRvPhotos; // 选择照片界面

    private ProgressBar mProgressBar;
    private ImageView checkedIcon;
    private ImageButton mAlbumBtn;
    private FrameLayout mCameraFl;
    private TextureView mTextureView;
    private ImageButton mCaptureBtn;
    private ImageButton mSwitchCameraBtn;
    private ImageButton mFullScreenBtn;
    private ImageButton mRecordVideoBtn;
    private OnMenuClickListener mListener;
    private ChatInputStyle mStyle;
    private InputMethodManager mImm;
    private Window mWindow;
    private int mLastClickId = 0;

    private int mWidth;
    private int mHeight;
    private int mMenuHeight = 300;
    private boolean mShowSoftInput = false;
    private PhotoAdapter mPhotoAdapter;
    private List<String> mSendFiles = new ArrayList<>();
    private List<FileItem> mPhotos = new ArrayList<>();
    private MyHandler myHandler = new MyHandler(this);

    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;
    private final static int SCAN_OK = 1;
    private final static int SCAN_ERROR = 0;
    public static final int REQUEST_CODE_TAKE_PHOTO = 0x0001;

    private File mPhoto;
    private CameraSupport mCameraSupport;
    private int mCameraId = -1;
    private boolean mBackCamera = true;

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
        mRecordControllerView = (RecordControllerView) findViewById(R.id.record_controller_view);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mRecordVoiceBtn = (RecordVoiceButton) findViewById(R.id.record_btn);
        mCameraFl = (FrameLayout) findViewById(R.id.camera_container);
        mTextureView = (TextureView) findViewById(R.id.camera_texture_view);
        mFullScreenBtn = (ImageButton) findViewById(R.id.full_screen_ib);
        mRecordVideoBtn = (ImageButton) findViewById(R.id.record_video_ib);
        mCaptureBtn = (ImageButton) findViewById(R.id.capture_ib);
        mSwitchCameraBtn = (ImageButton) findViewById(R.id.switch_camera_ib);

        mRvPhotos = (RecyclerView) findViewById(R.id.rv_photo);
        mRvPhotos.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false));
        mRvPhotos.setHasFixedSize(true);

        mMenuContainer.setVisibility(GONE);
        mChatInput.addTextChangedListener(this);
        mSendBtnFl.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mVoiceBtn.setOnClickListener(this);
        mRecordVoiceBtn.setRecordController(mRecordControllerView);
        mPhotoBtn.setOnClickListener(this);
        mCameraBtn.setOnClickListener(this);
        mFullScreenBtn.setOnClickListener(this);
        mRecordVideoBtn.setOnClickListener(this);
        mCaptureBtn.setOnClickListener(this);
        mSwitchCameraBtn.setOnClickListener(this);
        mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = ((Activity) context).getWindow();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mRecordControllerView.setWidth(mWidth);

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSendBtn.setElevation(getContext().getResources().getDimension(R.dimen.send_btn_shadow));
            }
        } else {
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSendBtn.setElevation(getContext().getResources().getDimension(R.dimen.send_btn_shadow));
//                mSendBtn.setElevation(0);
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
                mPhotoAdapter.resetCheckedState();
            } else if (onSubmit()) {
                mChatInput.setText("");
            }
        } else if (view.getId() == R.id.full_screen_ib) {
            mTextureView.bringToFront();
            ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(mWidth, mHeight);
            mTextureView.setLayoutParams(params);
        } else if (view.getId() == R.id.record_video_ib) {
            // TODO create video file and start recording
        } else if (view.getId() == R.id.capture_ib) {
            mCameraSupport.takePicture();
        } else if (view.getId() == R.id.switch_camera_ib) {
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (mBackCamera) {
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        mCameraId = i;
                        mBackCamera = false;
                        mCameraSupport.release();
                        mCameraSupport.open(mCameraId, mTextureView.getWidth(), mTextureView.getHeight());
                        break;
                    }
                } else {
                    if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        mCameraId = i;
                        mBackCamera = true;
                        mCameraSupport.release();
                        mCameraSupport.open(mCameraId, mTextureView.getWidth(), mTextureView.getHeight());
                        break;
                    }
                }
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
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    if (mPhoto == null) {
                        String path = getContext().getFilesDir().getAbsolutePath() + "/photo";
                        File destDir = new File(path);
                        if (!destDir.exists()) {
                            destDir.mkdirs();
                        }
                        mPhoto = new File(destDir, new DateFormat().format("yyyy_MMdd_hhmmss",
                                Calendar.getInstance(Locale.CHINA)) + ".png");
                    }
                    if (mCameraSupport == null) {
                        initCamera();
                    }
                    showCameraLayout();
                } else {
                    Toast.makeText(getContext(), getContext().getString(R.string.sdcard_not_exist_toast),
                            Toast.LENGTH_SHORT).show();
                }
            }
            mLastClickId = view.getId();
        }
    }

    private void initCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCameraSupport = new CameraNew(getContext(), mTextureView);
        } else {
            mCameraSupport = new CameraOld(mWidth, mMenuHeight, mTextureView);
        }
        mCameraSupport.setCameraCallbackListener(this);
        mCameraSupport.setOutputFile(mPhoto);
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                mCameraId = i;
                break;
            }
        }
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                mCameraSupport.open(mCameraId, width, height);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width, int height) {
                mCameraSupport.open(mCameraId, width, height);
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
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
        mCameraFl.setVisibility(GONE);
        mRecordVoiceLl.setVisibility(VISIBLE);
    }

    public void dismissRecordVoiceLayout() {
        mRecordVoiceLl.setVisibility(GONE);
    }

    public void showPhotoLayout() {
        dismissRecordVoiceLayout();
        dismissCameraLayout();
        mPhotoFl.setVisibility(VISIBLE);
    }

    public void dismissPhotoLayout() {
        mPhotoFl.setVisibility(GONE);
    }

    public void showCameraLayout() {
        dismissRecordVoiceLayout();
        dismissPhotoLayout();
        mCameraFl.setVisibility(VISIBLE);
    }

    public void dismissCameraLayout() {
        mCameraFl.setVisibility(GONE);
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mMenuHeight);
        mTextureView.setLayoutParams(params);
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
        if (mSendFiles.size() == 1) {
            mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send_pres));
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

    /**
     * 增加文件选中时的发送按钮动画效果
     *
     * @param view
     */
    private void addSelectedAnimation(View view) {
        float[] values = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", values),
                ObjectAnimator.ofFloat(view, "scaleY", values));
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
     * Set camera capture file path and file name. If user didn't invoke this method, will save in
     * default path.
     * @param path Photo to be saved in.
     * @param fileName File name.
     */
    public void setCameraCaptureFile(String path, String fileName) {
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        mPhoto = new File(path, fileName + ".png");
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
         *
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
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mMenuHeight);
        mTextureView.setLayoutParams(params);
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



    @Override
    public void onTakePictureCompleted(File file) {
        List<String> list = new ArrayList<>();
        list.add(file.getAbsolutePath());
        mListener.onSendFiles(list);
    }

    @Override
    public void onRecordVideoCompleted(File file) {

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
                        chatInputView.mPhotoAdapter = new PhotoAdapter(chatInputView.mPhotos,
                                chatInputView.mMenuHeight);
                        chatInputView.mPhotoAdapter.setSelectedFiles(chatInputView.mSendFiles);
                        chatInputView.mPhotoAdapter.setOnPhotoSelectedListener(chatInputView);
                        chatInputView.mRvPhotos.setAdapter(chatInputView.mPhotoAdapter);
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

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCameraSupport != null) {
            mCameraSupport.release();
        }
    }
}
