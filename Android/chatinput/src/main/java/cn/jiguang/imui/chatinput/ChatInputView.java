package cn.jiguang.imui.chatinput;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.VideoColumns;
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
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cn.jiguang.imui.chatinput.camera.CameraNew;
import cn.jiguang.imui.chatinput.camera.CameraOld;
import cn.jiguang.imui.chatinput.camera.CameraSupport;
import cn.jiguang.imui.chatinput.camera.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.photo.PhotoAdapter;
import cn.jiguang.imui.chatinput.record.ProgressButton;
import cn.jiguang.imui.chatinput.record.RecordControllerView;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;
import cn.jiguang.imui.chatinput.utils.FileItem;
import cn.jiguang.imui.chatinput.utils.VideoItem;

public class ChatInputView extends LinearLayout
        implements View.OnClickListener, TextWatcher, RecordControllerView.OnRecordActionListener,
        PhotoAdapter.OnFileSelectedListener, OnCameraCallbackListener {

    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;

    public static final int REQUEST_CODE_TAKE_PHOTO = 0x0001;
    public static final int REQUEST_CODE_SELECT_PHOTO = 0x0002;

    private EditText mChatInput;
    private FrameLayout mSendBtnFl;
    private ImageButton mSendBtn;
    private TextView mSendCountTv;
    private CharSequence mInput;
    private Space mInputMarginLeft;
    private Space mInputMarginRight;
    private ImageButton mVoiceBtn;
    private ImageButton mCameraBtn;
    private FrameLayout mMenuContainer;
    private RelativeLayout mRecordVoiceRl;
    private LinearLayout mPreviewPlayLl;
    private ProgressButton mPreviewPlayBtn;
    private Button mSendAudioBtn;
    private Button mCancelSendAudioBtn;
    private LinearLayout mRecordContentLl;
    private FrameLayout mPhotoFl;
    private RecordControllerView mRecordControllerView;
    private Chronometer mChronometer;
    private TextView mRecordHintTv;
    private RecordVoiceButton mRecordVoiceBtn;

    // Select photo start
    private ImageButton mPhotoBtn;
    private ImageButton mAlbumBtn;

    private RecyclerView mRvPhotos; // 选择照片界面
    private PhotoAdapter mPhotoAdapter;

    private ProgressBar mProgressBar;

    private List<FileItem> mMedias; // 所有的图片和视频文件
    private List<String> mSendFiles = new ArrayList<>();
    // Select photo end

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

    private MyHandler myHandler = new MyHandler(this);
    private long mRecordTime;
    private boolean mPlaying = false;
    private final MediaPlayer mMediaPlayer = new MediaPlayer();
    private boolean mSetData;
    private FileInputStream mFIS;
    private FileDescriptor mFD;
    private boolean mIsEarPhoneOn;

    private final static int SCAN_OK = 1;
    private final static int SCAN_ERROR = 0;

    private File mPhoto;
    private CameraSupport mCameraSupport;
    private int mCameraId = -1;
    private boolean mBackCamera = true;

    private Context mContext;

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
        mContext = context;
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
        mRecordVoiceRl = (RelativeLayout) findViewById(R.id.record_voice_container);
        mPreviewPlayLl = (LinearLayout) findViewById(R.id.preview_play_container);
        mPreviewPlayBtn = (ProgressButton) findViewById(R.id.play_audio_pb);
        mRecordContentLl = (LinearLayout) findViewById(R.id.record_content_container);
        mPhotoFl = (FrameLayout) findViewById(R.id.photo_container);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mAlbumBtn = (ImageButton) findViewById(R.id.album_ib);
        mRecordControllerView = (RecordControllerView) findViewById(R.id.record_controller_view);
        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mRecordHintTv = (TextView) findViewById(R.id.record_hint_tv);
        mSendAudioBtn = (Button) findViewById(R.id.send_voice_btn);
        mCancelSendAudioBtn = (Button) findViewById(R.id.cancel_send_audio_btn);
        mRecordVoiceBtn = (RecordVoiceButton) findViewById(R.id.record_btn);
        mCameraFl = (FrameLayout) findViewById(R.id.camera_container);
        mTextureView = (TextureView) findViewById(R.id.camera_texture_view);
        mFullScreenBtn = (ImageButton) findViewById(R.id.full_screen_ib);
        mRecordVideoBtn = (ImageButton) findViewById(R.id.record_video_ib);
        mCaptureBtn = (ImageButton) findViewById(R.id.capture_ib);
        mSwitchCameraBtn = (ImageButton) findViewById(R.id.switch_camera_ib);

        mRvPhotos = (RecyclerView) findViewById(R.id.rv_photo);
        mRvPhotos.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mRvPhotos.setHasFixedSize(true);

        mMenuContainer.setVisibility(GONE);
        mChatInput.addTextChangedListener(this);
        mSendBtnFl.setOnClickListener(this);
        mSendBtn.setOnClickListener(this);
        mVoiceBtn.setOnClickListener(this);
        mRecordVoiceBtn.setRecordController(mRecordControllerView);
        mPhotoBtn.setOnClickListener(this);
        mCameraBtn.setOnClickListener(this);
        mPreviewPlayBtn.setOnClickListener(this);
        mCancelSendAudioBtn.setOnClickListener(this);
        mSendAudioBtn.setOnClickListener(this);
        mFullScreenBtn.setOnClickListener(this);
        mRecordVideoBtn.setOnClickListener(this);
        mCaptureBtn.setOnClickListener(this);
        mSwitchCameraBtn.setOnClickListener(this);

        // 从相册选择图片
        mAlbumBtn.setOnClickListener(this);
        mAlbumBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof Activity) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivity(intent, REQUEST_CODE_SELECT_PHOTO);
            }
        });

        mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = ((Activity) context).getWindow();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mWidth = dm.widthPixels;
        mHeight = dm.heightPixels;
        mRecordControllerView.setWidth(mWidth);
        mRecordControllerView.setOnControllerListener(this);

        mChatInput.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 软键盘还未打开
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !mShowSoftInput) {
                    mShowSoftInput = true;
                    invisibleMenuLayout();
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

        if (getPaddingLeft() == 0 && getPaddingRight() == 0 && getPaddingTop() == 0 && getPaddingBottom() == 0) {

        }
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
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
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mInput = s;

        if (mSendFiles.size() > 0) {
            return;
        }

        if (s.length() >= 1 && start == 0 && before == 0) { // 开始输入内容
            triggerSendButtonAnimation(mSendBtn, true, false);
        }
        if (s.length() == 0 && before >= 1) { // 清空内容
            triggerSendButtonAnimation(mSendBtn, false, false);
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
        if (view.getId() == R.id.send_btn_fl || view.getId() == R.id.send_msg_ib) {
            // 允许同时发送文字和选中的图片
            if (onSubmit()) {
                mChatInput.setText("");
            }
            if (mSendFiles.size() > 0) {
                mListener.onSendFiles(mSendFiles);
                mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send));
                mSendCountTv.setVisibility(View.INVISIBLE);
                dismissMenuLayout();
                mPhotoAdapter.resetCheckedState();
            }
        } else if (view.getId() == R.id.play_audio_pb) {
            if (!mPlaying) {
                if (mSetData) {
                    mPreviewPlayBtn.startPlay();
                    mMediaPlayer.start();
                    mPlaying = true;
                    mChronometer.setBase(convertStrTimeToLong(mChronometer.getText().toString()));
                    mChronometer.start();
                } else {
                    playVoice();
                }
            } else {
                mSetData = true;
                mMediaPlayer.pause();
                mChronometer.stop();
                mPlaying = false;
                mPreviewPlayBtn.stopPlay();
                // TODO stop play audio
            }
        } else if (view.getId() == R.id.cancel_send_audio_btn) {
            mPreviewPlayLl.setVisibility(GONE);
            mRecordContentLl.setVisibility(VISIBLE);
            mRecordVoiceBtn.cancelRecord();
            mChronometer.setText("00:00");
        } else if (view.getId() == R.id.send_voice_btn) {
            mPreviewPlayLl.setVisibility(GONE);
            dismissMenuLayout();
            mRecordVoiceBtn.finishRecord();
            mChronometer.setText("00:00");
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

                // TODO: 判断是否有新增照片或视频
                if (mMedias == null) {
                    mMedias = new ArrayList<>();
                    mProgressBar.setVisibility(View.VISIBLE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (getPhotos() && getVideos()) {
                                myHandler.sendEmptyMessage(SCAN_OK);
                            } else {
                                myHandler.sendEmptyMessage(SCAN_ERROR);
                            }
                        }
                    }).start();
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
                        mPhoto = new File(destDir,
                                new DateFormat().format("yyyy_MMdd_hhmmss", Calendar.getInstance(Locale.CHINA))
                                        + ".png");
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

    private void playVoice() {
        try {
            mMediaPlayer.reset();
            mFIS = new FileInputStream(mRecordVoiceBtn.getRecordFile());
            mFD = mFIS.getFD();
            mMediaPlayer.setDataSource(mFD);
            if (mIsEarPhoneOn) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
            } else {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mPreviewPlayBtn.startPlay();
                    mChronometer.start();
                    mp.start();
                    mPlaying = true;
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mSetData = false;
                    mChronometer.stop();
                    mPlaying = false;
                    mPreviewPlayBtn.finishPlay();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), getContext().getString(R.string.file_not_found_toast),
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            try {
                if (mFIS != null) {
                    mFIS.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAudioPlayByEarPhone(int state) {
        AudioManager audioManager = (AudioManager) getContext()
                .getSystemService(Context.AUDIO_SERVICE);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        if (state == 0) {
            mIsEarPhoneOn = false;
            audioManager.setSpeakerphoneOn(true);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.STREAM_VOICE_CALL);
        } else {
            mIsEarPhoneOn = true;
            audioManager.setSpeakerphoneOn(false);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, currVolume,
                    AudioManager.STREAM_VOICE_CALL);
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
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width,
                                                    int height) {
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
        mRecordVoiceRl.setVisibility(VISIBLE);
        mRecordContentLl.setVisibility(VISIBLE);
    }

    public void dismissRecordVoiceLayout() {
        mRecordVoiceRl.setVisibility(GONE);
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
        ViewGroup.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mMenuHeight);
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
            mMenuContainer.setLayoutParams(
                    new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
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
        if (mInput.length() == 0 && mSendFiles.size() == 1) {
            triggerSendButtonAnimation(mSendBtn, true, true);
        } else if (mInput.length() > 0 && mSendCountTv.getVisibility() != View.VISIBLE) {
            mSendCountTv.setVisibility(View.VISIBLE);
        }
        mSendCountTv.setText(String.valueOf(mSendFiles.size()));
    }

    @Override
    public void onFileDeselected() {
        if (mSendFiles.size() > 0) {
            mSendCountTv.setText(String.valueOf(mSendFiles.size()));
        } else {
            if (mInput.length() == 0) {
                triggerSendButtonAnimation(mSendBtn, false, true);
            } else {
                mSendCountTv.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 触发发送按钮的动画效果
     *
     * @param sendBtn       发送按钮
     * @param hasContent    输入框或图片选择界面是否有内容
     * @param isSelectPhoto 是否正在选择图片
     */
    private void triggerSendButtonAnimation(final ImageButton sendBtn, final boolean hasContent,
                                            final boolean isSelectPhoto) {
        float[] shrinkValues = new float[]{0.6f};
        AnimatorSet shrinkAnimatorSet = new AnimatorSet();
        shrinkAnimatorSet.playTogether(ObjectAnimator.ofFloat(sendBtn, "scaleX", shrinkValues),
                ObjectAnimator.ofFloat(sendBtn, "scaleY", shrinkValues));
        shrinkAnimatorSet.setDuration(100);

        float[] restoreValues = new float[]{1.0f};
        final AnimatorSet restoreAnimatorSet = new AnimatorSet();
        restoreAnimatorSet.playTogether(ObjectAnimator.ofFloat(sendBtn, "scaleX", restoreValues),
                ObjectAnimator.ofFloat(sendBtn, "scaleY", restoreValues));
        restoreAnimatorSet.setDuration(100);

        restoreAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mSendCountTv.bringToFront();
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    requestLayout();
                    invalidate();
                }
                if (hasContent && isSelectPhoto) {
                    mSendCountTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        shrinkAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (!hasContent && isSelectPhoto) {
                    mSendCountTv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (hasContent) {
                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send_pres));
                } else {
                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.send));
                }
                restoreAnimatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        shrinkAnimatorSet.start();
    }

    /**
     * Set camera capture file path and file name. If user didn't invoke this method, will save in
     * default path.
     *
     * @param path     Photo to be saved in.
     * @param fileName File name.
     */
    public void setCameraCaptureFile(String path, String fileName) {
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        mPhoto = new File(path, fileName + ".png");
    }

    @Override
    public void onStart() {
        Log.e("ChatInputView", "starting chronometer");
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        mChronometer.setVisibility(VISIBLE);
        mRecordHintTv.setVisibility(INVISIBLE);
    }

    @Override
    public void onMoving() {
        mChronometer.setVisibility(VISIBLE);
        mRecordHintTv.setVisibility(INVISIBLE);
    }

    @Override
    public void onMovedLeft() {
        mChronometer.setVisibility(INVISIBLE);
        mRecordHintTv.setVisibility(VISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.preview_play_audio_hint));
    }

    @Override
    public void onMovedRight() {
        mChronometer.setVisibility(INVISIBLE);
        mRecordHintTv.setVisibility(VISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.cancel_record_voice_hint));
    }

    @Override
    public void onLeftUpTapped() {
        mChronometer.stop();
        mRecordTime = SystemClock.elapsedRealtime() - mChronometer.getBase();
        mPreviewPlayBtn.setMax((int) (mRecordTime / 1000));
        mChronometer.setVisibility(VISIBLE);
        mRecordHintTv.setVisibility(INVISIBLE);
        mPreviewPlayLl.setVisibility(VISIBLE);
        mRecordContentLl.setVisibility(GONE);
    }

    @Override
    public void onRightUpTapped() {
        mChronometer.stop();
        mChronometer.setVisibility(INVISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.record_voice_hint));
        mRecordHintTv.setVisibility(VISIBLE);
    }

    private long convertStrTimeToLong(String strTime) {
        // TODO Auto-generated method stub
        String[] timeArray = strTime.split(":");
        long longTime = 0;
        if (timeArray.length == 2) {//如果时间是MM:SS格式
            longTime = Integer.parseInt(timeArray[0]) * 60 * 1000 + Integer.parseInt(timeArray[1]) * 1000;
        }
        return SystemClock.elapsedRealtime() - longTime;
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
        ViewGroup.LayoutParams params =
                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mMenuHeight);
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

    private boolean getPhotos() {
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = getContext().getContentResolver();
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DATE_ADDED
        };
        Cursor cursor = contentResolver.query(imageUri, projection, null, null,
                MediaStore.Images.Media.DATE_ADDED + " desc");

        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() == 0) {
            cursor.close();
            return true;
        }

        while (cursor.moveToNext()) {
            //获取图片的路径
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
            String fileName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
            FileItem item = new FileItem(path, fileName, size, date);
            item.setType(FileItem.Type.Image);
            mMedias.add(item);
        }
        cursor.close();
        return true;
    }

    private boolean getVideos() {
        Uri videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        ContentResolver cr = getContext().getContentResolver();
        String[] projection = new String[]{
                VideoColumns.DATA, VideoColumns.DURATION, VideoColumns.DISPLAY_NAME, VideoColumns.DATE_ADDED
        };

        Cursor cursor = cr.query(videoUri, projection, null, null, null);
        if (cursor == null) {
            return false;
        }
        if (cursor.getCount() == 0) {
            cursor.close();
            return true;
        }

        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
            String date = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
            long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
            VideoItem item = new VideoItem(path, name, null, date, duration);
            item.setType(FileItem.Type.Video);
            mMedias.add(item);
        }
        cursor.close();
        return true;
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

                        Collections.sort(chatInputView.mMedias);

                        chatInputView.mPhotoAdapter =
                                new PhotoAdapter(chatInputView.mMedias, chatInputView.mMenuHeight);
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
        mMediaPlayer.release();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mCameraSupport != null) {
            mCameraSupport.release();
        }
    }
}
