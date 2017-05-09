package cn.jiguang.imui.chatinput;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.Surface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.jiguang.imui.chatinput.camera.CameraNew;
import cn.jiguang.imui.chatinput.camera.CameraOld;
import cn.jiguang.imui.chatinput.camera.CameraSupport;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnFileSelectedListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.chatinput.photo.SelectPhotoView;
import cn.jiguang.imui.chatinput.record.ProgressButton;
import cn.jiguang.imui.chatinput.record.RecordControllerView;
import cn.jiguang.imui.chatinput.record.RecordVoiceButton;

public class ChatInputView extends LinearLayout
        implements View.OnClickListener, TextWatcher, RecordControllerView.OnRecordActionListener,
        OnFileSelectedListener {

    public static final byte KEYBOARD_STATE_SHOW = -3;
    public static final byte KEYBOARD_STATE_HIDE = -2;
    public static final byte KEYBOARD_STATE_INIT = -1;

    public static final int REQUEST_CODE_TAKE_PHOTO = 0x0001;
    public static final int REQUEST_CODE_SELECT_PHOTO = 0x0002;

    private EditText mChatInput;
    private TextView mSendCountTv;
    private CharSequence mInput;
    private Space mInputMarginLeft;
    private Space mInputMarginRight;

    private ImageButton mVoiceBtn;
    private ImageButton mPhotoBtn;
    private ImageButton mCameraBtn;
    private ImageButton mSendBtn;

    private LinearLayout mChatInputContainer;
    private LinearLayout mMenuItemContainer;
    private FrameLayout mMenuContainer;
    private RelativeLayout mRecordVoiceRl;
    private LinearLayout mPreviewPlayLl;
    private ProgressButton mPreviewPlayBtn;
    private Button mSendAudioBtn;
    private Button mCancelSendAudioBtn;
    private LinearLayout mRecordContentLl;
    private RecordControllerView mRecordControllerView;
    private Chronometer mChronometer;
    private TextView mRecordHintTv;
    private RecordVoiceButton mRecordVoiceBtn;

    SelectPhotoView mSelectPhotoView;

    private FrameLayout mCameraFl;
    private TextureView mTextureView;
    private ImageButton mCaptureBtn;
    private ImageButton mCloseBtn;
    private ImageButton mSwitchCameraBtn;
    private ImageButton mFullScreenBtn;
    private ImageButton mRecordVideoBtn;
    private OnMenuClickListener mListener;
    private OnCameraCallbackListener mCameraListener;

    private ChatInputStyle mStyle;

    private InputMethodManager mImm;
    private Window mWindow;
    private int mLastClickId = 0;

    private int mWidth;
    private int mHeight;
    private int mMenuHeight = 300;
    private boolean mShowSoftInput = false;

    private long mRecordTime;
    private boolean mPlaying = false;
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    // To judge if it is record video mode
    private boolean mIsRecordVideoMode = false;

    // To judge if it is recording video now
    private boolean mIsRecordingVideo = false;

    // To judge if is finish recording video
    private boolean mFinishRecordingVideo = false;

    // Video file to be saved at
    private String mVideoFilePath;
    private int mVideoDuration;

    // If audio file has been set
    private boolean mSetData;
    private FileInputStream mFIS;
    private FileDescriptor mFD;
    private boolean mIsEarPhoneOn;

    private File mPhoto;
    private CameraSupport mCameraSupport;
    private int mCameraId = -1;
    private boolean mIsBackCamera = true;
    private boolean mIsFullScreen = false;
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
        inflate(context, R.layout.view_chatinput, this);

        // menu buttons
        mChatInput = (EditText) findViewById(R.id.aurora_et_chat_input);
        mVoiceBtn = (ImageButton) findViewById(R.id.aurora_menuitem_ib_voice);
        mPhotoBtn = (ImageButton) findViewById(R.id.aurora_menuitem_ib_photo);
        mCameraBtn = (ImageButton) findViewById(R.id.aurora_menuitem_ib_camera);
        mSendBtn = (ImageButton) findViewById(R.id.aurora_menuitem_ib_send);

        View voiceBtnContainer = findViewById(R.id.aurora_framelayout_menuitem_voice);
        View photoBtnContainer = findViewById(R.id.aurora_framelayout_menuitem_photo);
        View cameraBtnContainer = findViewById(R.id.aurora_framelayout_menuitem_camera);
        voiceBtnContainer.setOnClickListener(onMenuItemClickListener);
        photoBtnContainer.setOnClickListener(onMenuItemClickListener);
        cameraBtnContainer.setOnClickListener(onMenuItemClickListener);
        mSendBtn.setOnClickListener(onMenuItemClickListener);

        mSendCountTv = (TextView) findViewById(R.id.aurora_menuitem_tv_send_count);
        mInputMarginLeft = (Space) findViewById(R.id.aurora_input_margin_left);
        mInputMarginRight = (Space) findViewById(R.id.aurora_input_margin_right);
        mChatInputContainer = (LinearLayout) findViewById(R.id.aurora_ll_input_container);
        mMenuItemContainer = (LinearLayout) findViewById(R.id.aurora_ll_menuitem_container);
        mMenuContainer = (FrameLayout) findViewById(R.id.aurora_fl_menu_container);
        mRecordVoiceRl = (RelativeLayout) findViewById(R.id.aurora_rl_recordvoice_container);
        mPreviewPlayLl = (LinearLayout) findViewById(R.id.aurora_ll_recordvoice_preview_container);
        mPreviewPlayBtn = (ProgressButton) findViewById(R.id.aurora_pb_recordvoice_play_audio);
        mRecordContentLl = (LinearLayout) findViewById(R.id.aurora_ll_recordvoice_content_container);

        mRecordControllerView = (RecordControllerView) findViewById(R.id.aurora_rcv_recordvoice_controller);
        mChronometer = (Chronometer) findViewById(R.id.aurora_chronometer_recordvoice);
        mRecordHintTv = (TextView) findViewById(R.id.aurora_tv_recordvoice_hint);
        mSendAudioBtn = (Button) findViewById(R.id.aurora_btn_recordvoice_send);
        mCancelSendAudioBtn = (Button) findViewById(R.id.aurora_btn_recordvoice_cancel);
        mRecordVoiceBtn = (RecordVoiceButton) findViewById(R.id.aurora_rvb_recordvoice_record);
        mCameraFl = (FrameLayout) findViewById(R.id.aurora_fl_camera_container);
        mTextureView = (TextureView) findViewById(R.id.aurora_txtv_camera_texture);
        mCloseBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_close);
        mFullScreenBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_full_screen);
        mRecordVideoBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_record_video);
        mCaptureBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_capture);
        mSwitchCameraBtn = (ImageButton) findViewById(R.id.aurora_ib_camera_switch);

        mSelectPhotoView = (SelectPhotoView) findViewById(R.id.aurora_view_selectphoto);
        mSelectPhotoView.setOnFileSelectedListener(this);
        mSelectPhotoView.initData();

        mMenuContainer.setVisibility(GONE);

        mChatInput.addTextChangedListener(this);

        mRecordVoiceBtn.setRecordController(mRecordControllerView);
        mPreviewPlayBtn.setOnClickListener(this);
        mCancelSendAudioBtn.setOnClickListener(this);
        mSendAudioBtn.setOnClickListener(this);
        mCloseBtn.setOnClickListener(this);
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
        mRecordControllerView.setOnControllerListener(this);

        mChatInput.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && !mShowSoftInput) {
                    mShowSoftInput = true;
                    invisibleMenuLayout();
                    mChatInput.requestFocus();
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
        mVoiceBtn.setImageDrawable(mStyle.getVoiceBtnIcon());
        mVoiceBtn.setBackground(mStyle.getVoiceBtnBg());
        mPhotoBtn.setBackground(mStyle.getPhotoBtnBg());
        mPhotoBtn.setImageDrawable(mStyle.getPhotoBtnIcon());
        mCameraBtn.setBackground(mStyle.getCameraBtnBg());
        mCameraBtn.setImageDrawable(mStyle.getCameraBtnIcon());
        mSendBtn.setBackground(mStyle.getSendBtnBg());
        mSendBtn.setImageDrawable(mStyle.getSendBtnIcon());
        mSendCountTv.setBackground(mStyle.getSendCountBg());

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

    public void setOnCameraCallbackListener(OnCameraCallbackListener listener) {
        mCameraListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mInput = s;

        if (mSelectPhotoView.getSelectFiles() == null || mSelectPhotoView.getSelectFiles().size() == 0) {
            if (s.length() >= 1 && start == 0 && before == 0) { // Starting input
                triggerSendButtonAnimation(mSendBtn, true, false);
            } else if (s.length() == 0 && before >= 1) { // Clear content
                triggerSendButtonAnimation(mSendBtn, false, false);
            }
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

    private OnClickListener onMenuItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.aurora_menuitem_ib_send) {
                // Allow send text and photos at the same time.
                if (onSubmit()) {
                    mChatInput.setText("");
                }
                if (mSelectPhotoView.getSelectFiles() != null && mSelectPhotoView.getSelectFiles().size() > 0) {
                    mListener.onSendFiles(mSelectPhotoView.getSelectFiles());

                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(),
                            R.drawable.aurora_menuitem_send));
                    mSendCountTv.setVisibility(View.INVISIBLE);
                    mSelectPhotoView.resetCheckState();
                    dismissMenuLayout();
                }

            } else {
                if (mMenuContainer.getVisibility() != VISIBLE) {
                    dismissSoftInputAndShowMenu();
                } else if (view.getId() == mLastClickId) {
                    dismissMenuAndResetSoftMode();
                    return;
                }

                if (view.getId() == R.id.aurora_framelayout_menuitem_voice) {
                    if (mListener != null) {
                        mListener.switchToMicrophoneMode();
                    }
                    showRecordVoiceLayout();

                } else if (view.getId() == R.id.aurora_framelayout_menuitem_photo) {
                    if (mListener != null) {
                        mListener.switchToGalleryMode();
                    }
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    dismissRecordVoiceLayout();
                    dismissCameraLayout();
                    mSelectPhotoView.setVisibility(VISIBLE);
                    mSelectPhotoView.initData();

                } else if (view.getId() == R.id.aurora_framelayout_menuitem_camera) {
                    if (mListener != null) {
                        mListener.switchToCameraMode();
                    }
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        if (mPhoto == null) {
                            String path = getContext().getFilesDir().getAbsolutePath() + "/photo";
                            File destDir = new File(path);
                            if (!destDir.exists()) {
                                destDir.mkdirs();
                            }
                            mPhoto = new File(destDir,
                                    DateFormat.format("yyyy_MMdd_hhmmss", Calendar.getInstance(Locale.CHINA))
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
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.aurora_pb_recordvoice_play_audio) {
            // press preview play audio button
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
            }

        } else if (view.getId() == R.id.aurora_btn_recordvoice_cancel) {
            // preview play audio widget cancel sending audio
            mPreviewPlayLl.setVisibility(GONE);
            mRecordContentLl.setVisibility(VISIBLE);
            mRecordVoiceBtn.cancelRecord();
            mChronometer.setText("00:00");

        } else if (view.getId() == R.id.aurora_btn_recordvoice_send) {
            // preview play audio widget send audio
            mPreviewPlayLl.setVisibility(GONE);
            dismissMenuLayout();
            mRecordVoiceBtn.finishRecord();
            mChronometer.setText("00:00");

        } else if (view.getId() == R.id.aurora_ib_camera_full_screen) {
            // full screen/recover screen button in texture view
            if (!mIsFullScreen) {
                fullScreen();
            } else {
                recoverScreen();
            }

        } else if (view.getId() == R.id.aurora_ib_camera_record_video) {
            // click record video button
            // if it is not record video mode
            if (!mIsRecordVideoMode) {
                mIsRecordVideoMode = true;
                mCaptureBtn.setBackgroundResource(R.drawable.aurora_preview_record_video_start);
                mRecordVideoBtn.setBackgroundResource(R.drawable.aurora_preview_camera);
                fullScreen();
                mCloseBtn.setVisibility(VISIBLE);
            } else {
                mIsRecordVideoMode = false;
                mRecordVideoBtn.setBackgroundResource(R.drawable.aurora_preview_record_video);
                mCaptureBtn.setBackgroundResource(R.drawable.aurora_menuitem_send_pres);
                mFullScreenBtn.setBackgroundResource(R.drawable.aurora_preview_recover_screen);
                mFullScreenBtn.setVisibility(VISIBLE);
                mCloseBtn.setVisibility(GONE);
            }

        } else if (view.getId() == R.id.aurora_ib_camera_capture) {
            // click capture button in preview camera view
            // is record video mode
            if (mIsRecordVideoMode) {
                if (!mIsRecordingVideo) {   // start recording
                    mCameraSupport.startRecordingVideo();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mCaptureBtn.setBackgroundResource(R.drawable.aurora_preview_record_video_stop);
                            mRecordVideoBtn.setVisibility(GONE);
                            mSwitchCameraBtn.setVisibility(GONE);
                            mCloseBtn.setVisibility(VISIBLE);
                        }
                    }, 200);
                    mIsRecordingVideo = true;

                } else {    // finish recording
                    mVideoFilePath = mCameraSupport.finishRecordingVideo();
                    mIsRecordingVideo = false;
                    mIsRecordVideoMode = false;
                    mFinishRecordingVideo = true;
                    mCaptureBtn.setBackgroundResource(R.drawable.aurora_menuitem_send_pres);
                    mRecordVideoBtn.setVisibility(GONE);
                    mSwitchCameraBtn.setBackgroundResource(R.drawable.aurora_preview_delete_video);
                    mSwitchCameraBtn.setVisibility(VISIBLE);
                    if (mVideoFilePath != null) {
                        playVideo();
                    }
                }
                // if finished recording video, send it
            } else if (mFinishRecordingVideo) {
                if (mListener != null) {
                    VideoItem video = new VideoItem(mVideoFilePath, null, null, null, mMediaPlayer.getDuration());
                    List<FileItem> list = new ArrayList<>();
                    list.add(video);
                    mListener.onSendFiles(list);
                    mFinishRecordingVideo = false;
                    mVideoFilePath = null;
                }
                mMediaPlayer.stop();
                mMediaPlayer.release();
                recoverScreen();
                dismissMenuAndResetSoftMode();
                // take picture and send it
            } else {
                mCameraSupport.takePicture();
                if (mIsFullScreen) {
                    recoverScreen();
                }
            }
        } else if (view.getId() == R.id.aurora_ib_camera_close) {
            try {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            recoverScreen();
            dismissMenuAndResetSoftMode();
            if (mFinishRecordingVideo) {
                mCameraSupport.cancelRecordingVideo();
                mFinishRecordingVideo = false;
            }
        } else if (view.getId() == R.id.aurora_ib_camera_switch) {
            if (mFinishRecordingVideo) {
                mCameraSupport.cancelRecordingVideo();
                mSwitchCameraBtn.setBackgroundResource(R.drawable.aurora_preview_switch_camera);
                mRecordVideoBtn.setBackgroundResource(R.drawable.aurora_preview_camera);
                mRecordVideoBtn.setVisibility(VISIBLE);
                mVideoFilePath = null;
                mFinishRecordingVideo = false;
                mIsRecordVideoMode = true;
                mCaptureBtn.setBackgroundResource(R.drawable.aurora_preview_record_video_start);
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mCameraSupport.open(mCameraId, mWidth, mHeight, mIsBackCamera);
            } else {
                for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                    Camera.CameraInfo info = new Camera.CameraInfo();
                    Camera.getCameraInfo(i, info);
                    if (mIsBackCamera) {
                        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            mCameraId = i;
                            mIsBackCamera = false;
                            mCameraSupport.release();
                            mCameraSupport.open(mCameraId, mTextureView.getWidth(),
                                    mTextureView.getHeight(), mIsBackCamera);
                            break;
                        }
                    } else {
                        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            mCameraId = i;
                            mIsBackCamera = true;
                            mCameraSupport.release();
                            mCameraSupport.open(mCameraId, mTextureView.getWidth(),
                                    mTextureView.getHeight(), mIsBackCamera);
                            break;
                        }
                    }
                }
            }
        }
    }

    // play audio
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

    private void playVideo() {
        try {
            mCameraSupport.release();
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mVideoFilePath);
            Surface surface = new Surface(mTextureView.getSurfaceTexture());
            mMediaPlayer.setSurface(surface);
            surface.release();
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException | IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
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
            mCameraSupport = new CameraOld(getContext(), mTextureView);
        }
        mCameraSupport.setCameraCallbackListener(mCameraListener);
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
                Log.e("ChatInputView", "Opening camera");
                mCameraSupport.open(mCameraId, width, height, mIsBackCamera);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int width,
                                                    int height) {
                Log.e("ChatInputView", "Texture size changed, Opening camera");
                if (mTextureView.getVisibility() == VISIBLE) {
                    mCameraSupport.open(mCameraId, width, height, mIsBackCamera);
                }
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                mCameraSupport.release();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }

    /**
     * Full screen mode
     */
    private void fullScreen() {
        // hide top status bar
        Activity activity = (Activity) getContext();
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mFullScreenBtn.setBackgroundResource(R.drawable.aurora_preview_recover_screen);
        mFullScreenBtn.setVisibility(VISIBLE);
        mChatInputContainer.setVisibility(GONE);
        mMenuItemContainer.setVisibility(GONE);
        mMenuContainer.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mHeight));
        mTextureView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight));
        mIsFullScreen = true;
    }

    /**
     * Recover screen
     */
    private void recoverScreen() {
        Activity activity = (Activity) getContext();
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        mIsFullScreen = false;
        mCloseBtn.setVisibility(GONE);
        mFullScreenBtn.setBackgroundResource(R.drawable.aurora_preview_full_screen);
        mFullScreenBtn.setVisibility(VISIBLE);
        mChatInputContainer.setVisibility(VISIBLE);
        mMenuItemContainer.setVisibility(VISIBLE);
        setMenuContainerHeight(mMenuHeight);
        ViewGroup.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mMenuHeight);
        mTextureView.setLayoutParams(params);
        mRecordVideoBtn.setBackgroundResource(R.drawable.aurora_preview_record_video);
        mRecordVideoBtn.setVisibility(VISIBLE);
        mSwitchCameraBtn.setBackgroundResource(R.drawable.aurora_preview_switch_camera);
        mSwitchCameraBtn.setVisibility(VISIBLE);
        mCaptureBtn.setBackgroundResource(R.drawable.aurora_menuitem_send_pres);
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
        mSelectPhotoView.setVisibility(GONE);
        mCameraFl.setVisibility(GONE);
        mRecordVoiceRl.setVisibility(VISIBLE);
        mRecordContentLl.setVisibility(VISIBLE);
    }

    public void dismissRecordVoiceLayout() {
        mRecordVoiceRl.setVisibility(GONE);
    }

    public void dismissPhotoLayout() {
        mSelectPhotoView.setVisibility(View.GONE);
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
        if (height > 0) {
            mMenuHeight = height;
            mMenuContainer.setLayoutParams(
                    new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
        }
    }

    private boolean onSubmit() {
        return mListener != null && mListener.onSendTextMessage(mInput);
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

    /**
     * Select aurora_menuitem_photo callback
     */
    @Override
    public void onFileSelected() {
        if (mInput.length() == 0 && mSelectPhotoView.getSelectFiles().size() == 1) {
            triggerSendButtonAnimation(mSendBtn, true, true);
        } else if (mInput.length() > 0 && mSendCountTv.getVisibility() != View.VISIBLE) {
            mSendCountTv.setVisibility(View.VISIBLE);
        }
        mSendCountTv.setText(String.valueOf(mSelectPhotoView.getSelectFiles().size()));
    }

    /**
     * Cancel select aurora_menuitem_photo callback
     */
    @Override
    public void onFileDeselected() {
        int size = mSelectPhotoView.getSelectFiles().size();
        if (size > 0) {
            mSendCountTv.setText(String.valueOf(size));
        } else {
            if (mInput.length() == 0) {
                triggerSendButtonAnimation(mSendBtn, false, true);
            } else {
                mSendCountTv.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Trigger aurora_menuitem_send button animation
     *
     * @param sendBtn       aurora_menuitem_send button
     * @param hasContent    EditText has content or photos have been selected
     * @param isSelectPhoto check if selecting photos
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
                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(),
                            R.drawable.aurora_menuitem_send_pres));
                } else {
                    mSendBtn.setImageDrawable(ContextCompat.getDrawable(getContext(),
                            R.drawable.aurora_menuitem_send));
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
     * Set aurora_menuitem_camera capture file path and file name. If user didn't invoke this method, will save in
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

    /**
     * Record audio widget finger on touch record button callback
     */
    @Override
    public void onStart() {
        Log.e("ChatInputView", "starting chronometer");
        mChronometer.setBase(SystemClock.elapsedRealtime());
        mChronometer.start();
        mChronometer.setVisibility(VISIBLE);
        mRecordHintTv.setVisibility(INVISIBLE);
    }

    /**
     * Recording audio mode, finger moving callback
     */
    @Override
    public void onMoving() {
        mChronometer.setVisibility(VISIBLE);
        mRecordHintTv.setVisibility(INVISIBLE);
    }

    /**
     * Recording audio mode, finger moved left button (preview button) callback
     */
    @Override
    public void onMovedLeft() {
        mChronometer.setVisibility(INVISIBLE);
        mRecordHintTv.setVisibility(VISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.preview_play_audio_hint));
    }

    /**
     * Recording audio mode, finger moved right button (cancel button)
     */
    @Override
    public void onMovedRight() {
        mChronometer.setVisibility(INVISIBLE);
        mRecordHintTv.setVisibility(VISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.cancel_record_voice_hint));
    }

    /**
     * Recording audio mode, finger moved left button and release
     */
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

    /**
     * Recording audio mode, finger moved right button and release
     */
    @Override
    public void onRightUpTapped() {
        mChronometer.stop();
        mChronometer.setVisibility(INVISIBLE);
        mRecordHintTv.setText(getContext().getString(R.string.record_voice_hint));
        mRecordHintTv.setVisibility(VISIBLE);
    }

    private long convertStrTimeToLong(String strTime) {
        String[] timeArray = strTime.split(":");
        long longTime = 0;
        if (timeArray.length == 2) { // If time format is MM:SS
            longTime = Integer.parseInt(timeArray[0]) * 60 * 1000 + Integer.parseInt(timeArray[1]) * 1000;
        }
        return SystemClock.elapsedRealtime() - longTime;
    }

    public void dismissMenuAndResetSoftMode() {
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
        // dismiss soft input
        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        showMenuLayout();
        if (mImm != null) {
            mImm.hideSoftInputFromWindow(mChatInput.getWindowToken(), 0);
        }
        setMenuContainerHeight(mMenuHeight);
        mShowSoftInput = false;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCameraSupport != null) {
            mCameraSupport.release();
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    @Override
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (visibility == GONE) {
            if (mCameraSupport != null) {
                mCameraSupport.release();
            }
        }
    }
}
