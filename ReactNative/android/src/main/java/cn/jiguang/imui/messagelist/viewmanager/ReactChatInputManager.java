package cn.jiguang.imui.messagelist.viewmanager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.modules.core.PermissionListener;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.emoji.EmoticonsKeyboardUtils;
import cn.jiguang.imui.chatinput.listener.CameraControllerListener;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.messagelist.AuroraIMUIModule;
import cn.jiguang.imui.messagelist.R;
import cn.jiguang.imui.messagelist.event.GetTextEvent;
import cn.jiguang.imui.messagelist.event.OnTouchMsgListEvent;
import cn.jiguang.imui.messagelist.event.ScrollEvent;
import cn.jiguang.imui.messagelist.event.StopPlayVoiceEvent;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by caiyaoguan on 2017/5/22.
 */

public class ReactChatInputManager extends ViewGroupManager<ChatInputView> implements EasyPermissions.PermissionCallbacks {

    private static final String REACT_CHAT_INPUT = "RCTChatInput";

    private static final String ON_SEND_TEXT_EVENT = "onSendText";
    private static final String ON_SEND_FILES_EVENT = "onSendGalleryFiles";
    private static final String SWITCH_TO_MIC_EVENT = "onSwitchToMicrophoneMode";
    private static final String SWITCH_TO_GALLERY_EVENT = "onSwitchToGalleryMode";
    private static final String SWITCH_TO_CAMERA_EVENT = "onSwitchToCameraMode";
    private static final String SWITCH_TO_EMOJI_EVENT = "onSwitchToEmojiMode";
    private static final String TAKE_PICTURE_EVENT = "onTakePicture";
    private static final String SWITCH_CAMERA_MODE_EVENT = "switchCameraMode";
    private static final String START_RECORD_VIDEO_EVENT = "onStartRecordVideo";
    private static final String FINISH_RECORD_VIDEO_EVENT = "onFinishRecordVideo";
    private static final String CANCEL_RECORD_VIDEO_EVENT = "onCancelRecordVideo";
    private static final String START_RECORD_VOICE_EVENT = "onStartRecordVoice";
    private static final String FINISH_RECORD_VOICE_EVENT = "onFinishRecordVoice";
    private static final String CANCEL_RECORD_VOICE_EVENT = "onCancelRecordVoice";
    private static final String CLOSE_CAMERA_EVENT = "closeCamera";
    private static final String ON_TOUCH_EDIT_TEXT_EVENT = "onTouchEditText";
    private static final String ON_FULL_SCREEN_EVENT = "onFullScreen";
    private static final String ON_RECOVER_SCREEN_EVENT = "onRecoverScreen";
    private static final String ON_INPUT_SIZE_CHANGED_EVENT = "onSizeChange";
    private static final String ON_CLICK_SELECT_ALBUM_EVENT = "onClickSelectAlbum";
    private static final int RC_RECORD_VOICE = 0x0002;
    private static final int RC_SELECT_PHOTO = 0x0003;
    private static final int RC_CAMERA = 0x0004;

    private final String SOFT_KEYBOARD_HEIGHT = "softKeyboardHeight";
    private final String AURORA_IMUI_SHARED_PREFERENCES = "cn.jiguang.imui.sp";
    private final int INIT_MENU_HEIGHT = 99;
    private final int CLOSE_SOFT_INPUT = 100;
    private final int GET_INPUT_TEXT = 101;
    private final int RESET_MENU_STATE = 102;

    private ChatInputView mChatInput;
    private ReactContext mContext;
    private int mWidth;
    private boolean mInitState = true;
    private boolean mShowMenu = false;
    private double mCurrentInputHeight = 48;
    private int mInitialChatInputHeight = 100;
    private int mSoftKeyboardHeight;
    private double mLineExpend = 0;
    private int mScreenWidth;
    private String mLastPhotoPath = "";
    /**
     * Initial soft input height, set this value via {@link #setMenuContainerHeight}
     */
    private int mMenuContainerHeight = 831;
    private float mDensity;
    private int mLastClickId = -1;

    @Override
    public String getName() {
        return REACT_CHAT_INPUT;
    }

    @Override
    protected ChatInputView createViewInstance(final ThemedReactContext reactContext) {
        mContext = reactContext;
        final SharedPreferences sp = reactContext.getSharedPreferences(AURORA_IMUI_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        mSoftKeyboardHeight = sp.getInt(SOFT_KEYBOARD_HEIGHT, 0);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        final Activity activity = reactContext.getCurrentActivity();
        mChatInput = new ChatInputView(activity, null);
        final EditText editText = mChatInput.getInputView();
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_TOUCH_EDIT_TEXT_EVENT, null);
                    if (!mChatInput.isFocused()) {
                        EmoticonsKeyboardUtils.openSoftKeyboard(mChatInput.getInputView());
                    }
                    EventBus.getDefault().post(new ScrollEvent(true));
                    int height = mChatInput.getSoftKeyboardHeight();
                    if (mSoftKeyboardHeight != height && height != 0 && height < 1000) {
                        mSoftKeyboardHeight = mChatInput.getSoftKeyboardHeight();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(SOFT_KEYBOARD_HEIGHT, mSoftKeyboardHeight);
                        editor.commit();
                    }

                }
                return false;
            }
        });

        editText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                editText.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mWidth = editText.getWidth();
            }
        });
        DisplayMetrics dm = new DisplayMetrics();
        reactContext.getCurrentActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mDensity = dm.density;
        mScreenWidth = dm.widthPixels;
        mMenuContainerHeight = (int) (mMenuContainerHeight / mDensity);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                WritableMap event = Arguments.createMap();
                double layoutHeight = calculateMenuHeight();
                mInitState = false;
                event.putDouble("height", layoutHeight);
                editText.setLayoutParams(new LinearLayout.LayoutParams(editText.getWidth(), (int) (mCurrentInputHeight)));
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, event);
            }
        });
        // Use default layout
        mChatInput.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
//                WritableMap map = Arguments.createMap();
//                map.putDouble("height", mInitialChatInputHeight);
//                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, map);
                WritableMap event = Arguments.createMap();
                event.putString("text", input.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_SEND_TEXT_EVENT, event);
                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }
                WritableMap event = Arguments.createMap();
                WritableArray array = new WritableNativeArray();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                for (FileItem fileItem : list) {
                    WritableMap map = new WritableNativeMap();
                    if (fileItem.getType().ordinal() == 0) {
                        map.putString("mediaType", "image");
                        BitmapFactory.decodeFile(fileItem.getFilePath(), options);
                        map.putInt("width", options.outWidth);
                        map.putInt("height", options.outHeight);
                    } else {
                        map.putString("mediaType", "video");
                        map.putInt("duration", (int) ((VideoItem) fileItem).getDuration());
                    }
                    map.putDouble("size", fileItem.getLongFileSize());
                    map.putString("mediaPath", fileItem.getFilePath());
                    array.pushMap(map);
                }
                event.putArray("mediaFiles", array);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_SEND_FILES_EVENT, event);
            }

            @Override
            public boolean switchToMicrophoneMode() {
                String[] perms = new String[]{
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(activity, perms)) {
                    EasyPermissions.requestPermissions(activity,
                            activity.getResources().getString(R.string.rationale_record_voice),
                            RC_RECORD_VOICE, perms);
                }
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_MIC_EVENT, null);
                // If menu is visible, close menu.
                if (mLastClickId == 0 && mShowMenu) {
                    mShowMenu = false;
                    mChatInput.dismissMenuLayout();
                    mChatInput.dismissRecordVoiceLayout();
                    sendSizeChangedEvent(mInitialChatInputHeight + mLineExpend);
                } else if (mShowMenu) {
                    mChatInput.showMenuLayout();
                    mChatInput.showRecordVoiceLayout();
                    mChatInput.requestLayout();
                } else {
                    mShowMenu = true;
                    mChatInput.setPendingShowMenu(true);
                    EmoticonsKeyboardUtils.closeSoftKeyboard(editText);
                    sendSizeChangedEvent(calculateMenuHeight());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mChatInput.showMenuLayout();
                            mChatInput.showRecordVoiceLayout();
                            mChatInput.requestLayout();
                        }
                    }, 150);
                }
                mLastClickId = 0;
                return false;
            }

            @Override
            public boolean switchToGalleryMode() {
                String[] perms = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                if (!EasyPermissions.hasPermissions(activity, perms)) {
                    EasyPermissions.requestPermissions(activity,
                            activity.getResources().getString(R.string.rationale_photo),
                            RC_SELECT_PHOTO, perms);
                    return false;
                }
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_GALLERY_EVENT, null);
                if (mLastClickId == 1 && mShowMenu) {
                    mShowMenu = false;
                    mChatInput.dismissMenuLayout();
                    mChatInput.dismissPhotoLayout();
                    sendSizeChangedEvent(mInitialChatInputHeight + mLineExpend);
                } else if (mShowMenu) {
                    mChatInput.getSelectPhotoView().updateData();
                    mChatInput.showMenuLayout();
                    mChatInput.showSelectPhotoLayout();
                    mChatInput.requestLayout();
                } else {
                    mChatInput.getSelectPhotoView().updateData();
                    mShowMenu = true;
                    mChatInput.setPendingShowMenu(true);
                    EmoticonsKeyboardUtils.closeSoftKeyboard(editText);
                    sendSizeChangedEvent(calculateMenuHeight());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mChatInput.showMenuLayout();
                            mChatInput.showSelectPhotoLayout();
                            mChatInput.requestLayout();
                        }
                    }, 150);
                }
                mLastClickId = 1;
                return false;
            }

            @Override
            public boolean switchToCameraMode() {
                String[] perms = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };

                if (!EasyPermissions.hasPermissions(activity, perms)) {
                    EasyPermissions.requestPermissions(activity,
                            activity.getResources().getString(R.string.rationale_camera),
                            RC_CAMERA, perms);
                    return false;
                }
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_CAMERA_EVENT, null);
                if (mLastClickId == 2 && mShowMenu) {
                    mShowMenu = false;
                    mChatInput.dismissMenuLayout();
                    mChatInput.dismissCameraLayout();
                    sendSizeChangedEvent(mInitialChatInputHeight + mLineExpend);
                } else if (mShowMenu) {
                    mChatInput.initCamera();
                    mChatInput.showMenuLayout();
                    mChatInput.showCameraLayout();
                    mChatInput.requestLayout();
                } else {
                    mShowMenu = true;
                    mChatInput.setPendingShowMenu(true);
                    mChatInput.initCamera();
                    EmoticonsKeyboardUtils.closeSoftKeyboard(editText);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mChatInput.showMenuLayout();
                            mChatInput.showCameraLayout();
                            sendSizeChangedEvent(calculateMenuHeight());
                            mChatInput.requestLayout();
                        }
                    }, 100);
                }
                mLastClickId = 2;
                return false;
            }

            @Override
            public boolean switchToEmojiMode() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_EMOJI_EVENT, null);
                if (mLastClickId == 3 && mShowMenu) {
                    mShowMenu = false;
                    mChatInput.dismissMenuLayout();
                    mChatInput.dismissEmojiLayout();
                    sendSizeChangedEvent(mInitialChatInputHeight + mLineExpend);
                } else if (mShowMenu) {
                    mChatInput.showMenuLayout();
                    mChatInput.showEmojiLayout();
                    mChatInput.requestLayout();
                } else {
                    mShowMenu = true;
                    mChatInput.setPendingShowMenu(true);
                    EmoticonsKeyboardUtils.closeSoftKeyboard(editText);
                    sendSizeChangedEvent(calculateMenuHeight());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mChatInput.showMenuLayout();
                            mChatInput.showEmojiLayout();
                            mChatInput.requestLayout();
                        }
                    }, 150);
                }
                mLastClickId = 3;
                return false;
            }
        });

        mChatInput.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {

                if(mLastPhotoPath.equals(photoPath)){
                    return;
                }
                mLastPhotoPath = photoPath;

                if (mChatInput.isFullScreen()) {
                    mContext.runOnUiQueueThread(new Runnable() {
                        @Override
                        public void run() {
                            mChatInput.dismissCameraLayout();
                            mChatInput.dismissMenuLayout();
                        }
                    });
                }
                WritableMap event = Arguments.createMap();
                event.putString("mediaPath", photoPath);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(photoPath, options);
                event.putInt("width", options.outWidth);
                event.putInt("height", options.outHeight);
                File file = new File(photoPath);
                event.putDouble("size", file.length());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        TAKE_PICTURE_EVENT, event);
                mChatInput.dismissMenuLayout();
            }

            @Override
            public void onStartVideoRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        START_RECORD_VIDEO_EVENT, null);
            }

            @Override
            public void onFinishVideoRecord(String videoPath) {
                if (videoPath != null) {
                    WritableMap event = Arguments.createMap();
                    event.putString("mediaPath", videoPath);
                    MediaPlayer mediaPlayer = MediaPlayer.create(reactContext, Uri.parse(videoPath));
                    int duration = mediaPlayer.getDuration() / 1000;    // Millisecond to second.
                    mediaPlayer.release();
                    File file = new File(videoPath);
                    event.putDouble("size", file.length());
                    event.putInt("duration", duration);
                    reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                            FINISH_RECORD_VIDEO_EVENT, event);
                }
            }

            @Override
            public void onCancelVideoRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        CANCEL_RECORD_VIDEO_EVENT, null);
            }
        });

        mChatInput.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        START_RECORD_VOICE_EVENT, null);
                File rootDir = reactContext.getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/voice";
                mChatInput.getRecordVoiceButton().setVoiceFilePath(fileDir, DateFormat.format("yyyy_MMdd_hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                WritableMap event = Arguments.createMap();
                event.putString("mediaPath", voiceFile.getAbsolutePath());
                event.putInt("duration", duration);
                event.putDouble("size", voiceFile.length());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        FINISH_RECORD_VOICE_EVENT, event);
            }

            @Override
            public void onCancelRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        CANCEL_RECORD_VOICE_EVENT, null);
            }

            @Override
            public void onPreviewCancel() {

            }

            @Override
            public void onPreviewSend() {
                sendSizeChangedEvent(mInitialChatInputHeight + mLineExpend);
            }
        });

        mChatInput.setCameraControllerListener(new CameraControllerListener() {
            @Override
            public void onFullScreenClick() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        ON_FULL_SCREEN_EVENT, null);
            }

            @Override
            public void onRecoverScreenClick() {
                mShowMenu = false;
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        ON_RECOVER_SCREEN_EVENT, null);
                WritableMap map = Arguments.createMap();
                Log.e(REACT_CHAT_INPUT, "send onSizeChangedEvent to js, height: " + mInitialChatInputHeight + mLineExpend);
                map.putDouble("height", mInitialChatInputHeight + mLineExpend);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, map);
            }

            @Override
            public void onCloseCameraClick() {
                mShowMenu = false;
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), CLOSE_CAMERA_EVENT, null);
            }

            @Override
            public void onSwitchCameraModeClick(boolean isRecordVideoMode) {
                WritableMap map = Arguments.createMap();
                map.putBoolean("isRecordVideoMode", isRecordVideoMode);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), SWITCH_CAMERA_MODE_EVENT, map);
            }
        });
        mChatInput.getSelectAlbumBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_CLICK_SELECT_ALBUM_EVENT, null);
            }
        });
        mChatInput.getEmojiContainer().getEmoticonsFuncView().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e(REACT_CHAT_INPUT, "EmotionPage Position" + position);
                mChatInput.requestLayout();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return mChatInput;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mChatInput.showMenuLayout();
        switch (requestCode) {
            case RC_RECORD_VOICE:
                mChatInput.showRecordVoiceLayout();
                break;
            case RC_SELECT_PHOTO:
                mChatInput.showSelectPhotoLayout();
                mChatInput.getSelectPhotoView().initData();
                break;
            default:
                mChatInput.showCameraLayout();
                break;
        }
        mChatInput.requestLayout();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (mContext.getCurrentActivity() != null) {
            if (EasyPermissions.somePermissionPermanentlyDenied(mContext.getCurrentActivity(), perms)) {
                new AppSettingsDialog.Builder(mContext.getCurrentActivity()).build().show();
            }
        }

    }

    private void sendSizeChangedEvent(final double n) {
        WritableMap event = Arguments.createMap();
        event.putDouble("height", n);
        mContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, event);
    }

    private double calculateMenuHeight() {
        double layoutHeight = mInitialChatInputHeight;
        if (mShowMenu) {
            if (mChatInput.getSoftKeyboardHeight() != 0) {
                layoutHeight += mChatInput.getSoftKeyboardHeight() / mDensity;
            } else {
                layoutHeight += mMenuContainerHeight / mDensity;
            }

        }
        switch (mChatInput.getInputView().getLineCount()) {
            case 0:
            case 1:
                mLineExpend = 0;
                break;
            case 2:
                mLineExpend = 5.4;
                break;
            case 3:
                mLineExpend = 25.5;
                break;
            default:
                mLineExpend = 45.7;
        }
        layoutHeight += mLineExpend;
        mCurrentInputHeight = (mLineExpend + 48) * mDensity;
        return layoutHeight;
    }

    private void moveToBack(View currentView) {
        ViewGroup viewGroup = ((ViewGroup) currentView.getParent());
        int index = viewGroup.indexOfChild(currentView);
        for (int i = 0; i < index; i++) {
            viewGroup.bringChildToFront(viewGroup.getChildAt(i));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StopPlayVoiceEvent event) {
        mChatInput.pauseVoice();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnTouchMsgListEvent event) {
        if (mChatInput.isKeyboardVisible() || mChatInput.getMenuState() == View.VISIBLE) {
            sendSizeChangedEvent(mInitialChatInputHeight + mLineExpend);
            mShowMenu = false;
            mChatInput.dismissMenuLayout();
        }
    }

    @ReactProp(name = "chatInputBackgroundColor")
    public void setBackgroundColor(ChatInputView chatInputView, String color) {
        int colorRes = Color.parseColor(color);
        chatInputView.setBackgroundColor(colorRes);
    }

    @ReactProp(name = "menuContainerHeight")
    public void setMenuContainerHeight(ChatInputView chatInputView, int height) {
        Log.d("ReactChatInputManager", "Setting menu container height: " + height);
        mMenuContainerHeight = height;
//        chatInputView.setMenuContainerHeight(height);
    }

    @ReactProp(name = "isDismissMenuContainer")
    public void dismissMenuContainer(ChatInputView chatInputView, boolean isDismiss) {
        if (isDismiss) {
            chatInputView.dismissMenuLayout();
        }
    }

    @ReactProp(name = "inputViewHeight")
    public void setEditTextHeight(ChatInputView chatInputView, int height) {
        Log.i("React", "setting edit text height: " + height);
        EditText editText = chatInputView.getInputView();
        editText.setLayoutParams(new LinearLayout.LayoutParams(mWidth, height));
    }

    @ReactProp(name = "showSelectAlbumBtn")
    public void showSelectAlbumBtn(ChatInputView chatInputView, boolean flag) {
        chatInputView.getSelectAlbumBtn().setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @ReactProp(name = "showRecordVideoBtn")
    public void showRecordVideoBtn(ChatInputView chatInputView, boolean flag) {
        if(flag){
            chatInputView.getRecordVideoBtn().setVisibility(View.VISIBLE);
            chatInputView.getRecordVideoBtn().setTag("VISIBLE");
        }else{
            chatInputView.getRecordVideoBtn().setVisibility(View.GONE);
            chatInputView.getRecordVideoBtn().setTag("GONE");
        }
    }

    @ReactProp(name = "inputPadding")
    public void setEditTextPadding(ChatInputView chatInputView, ReadableMap map) {
        try {
            int left = map.getInt("left");
            int top = map.getInt("top");
            int right = map.getInt("right");
            int bottom = map.getInt("bottom");
            chatInputView.getInputView().setPadding(chatInputView.dp2px(left),
                    chatInputView.dp2px(top), chatInputView.dp2px(right),
                    chatInputView.dp2px(bottom));
        } catch (Exception e) {
            Log.e(REACT_CHAT_INPUT, "Input padding key error");
        }
    }

    @ReactProp(name = "inputTextColor")
    public void setEditTextTextColor(ChatInputView chatInputView, String color) {
        int colorRes = Color.parseColor(color);
        chatInputView.getInputView().setTextColor(colorRes);
    }

    @ReactProp(name = "inputTextSize")
    public void setEditTextTextSize(ChatInputView chatInputView, int size) {
        chatInputView.getInputView().setTextSize(size);
    }

    @ReactProp(name = "inputTextLineHeight")
    public void setEditTextLineSpacing(ChatInputView chatInputView, int spacing) {
        chatInputView.getInputView().setLineSpacing(spacing, 1.0f);
    }

    @ReactProp(name = "hideCameraButton")
    public void hideCameraButton(ChatInputView chatInputView, boolean hide) {
        chatInputView.getCameraBtnContainer().setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @ReactProp(name = "hideVoiceButton")
    public void hideVoiceButton(ChatInputView chatInputView, boolean hide) {
        chatInputView.getVoiceBtnContainer().setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @ReactProp(name = "hideEmojiButton")
    public void hideEmojiButton(ChatInputView chatInputView, boolean hide) {
        chatInputView.getEmojiBtnContainer().setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @ReactProp(name = "hidePhotoButton")
    public void hidePhotoButton(ChatInputView chatInputView, boolean hide) {
        chatInputView.getPhotoBtnContainer().setVisibility(hide ? View.GONE : View.VISIBLE);
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(ON_SEND_TEXT_EVENT, MapBuilder.of("registrationName", ON_SEND_TEXT_EVENT))
                .put(ON_SEND_FILES_EVENT, MapBuilder.of("registrationName", ON_SEND_FILES_EVENT))
                .put(SWITCH_TO_MIC_EVENT, MapBuilder.of("registrationName", SWITCH_TO_MIC_EVENT))
                .put(SWITCH_TO_GALLERY_EVENT, MapBuilder.of("registrationName", SWITCH_TO_GALLERY_EVENT))
                .put(SWITCH_TO_CAMERA_EVENT, MapBuilder.of("registrationName", SWITCH_TO_CAMERA_EVENT))
                .put(SWITCH_TO_EMOJI_EVENT, MapBuilder.of("registrationName", SWITCH_TO_EMOJI_EVENT))
                .put(TAKE_PICTURE_EVENT, MapBuilder.of("registrationName", TAKE_PICTURE_EVENT))
                .put(START_RECORD_VIDEO_EVENT, MapBuilder.of("registrationName", START_RECORD_VIDEO_EVENT))
                .put(FINISH_RECORD_VIDEO_EVENT, MapBuilder.of("registrationName", FINISH_RECORD_VIDEO_EVENT))
                .put(CANCEL_RECORD_VIDEO_EVENT, MapBuilder.of("registrationName", CANCEL_RECORD_VIDEO_EVENT))
                .put(START_RECORD_VOICE_EVENT, MapBuilder.of("registrationName", START_RECORD_VOICE_EVENT))
                .put(FINISH_RECORD_VOICE_EVENT, MapBuilder.of("registrationName", FINISH_RECORD_VOICE_EVENT))
                .put(CANCEL_RECORD_VOICE_EVENT, MapBuilder.of("registrationName", CANCEL_RECORD_VOICE_EVENT))
                .put(ON_TOUCH_EDIT_TEXT_EVENT, MapBuilder.of("registrationName", ON_TOUCH_EDIT_TEXT_EVENT))
                .put(ON_FULL_SCREEN_EVENT, MapBuilder.of("registrationName", ON_FULL_SCREEN_EVENT))
                .put(ON_RECOVER_SCREEN_EVENT, MapBuilder.of("registrationName", ON_RECOVER_SCREEN_EVENT))
                .put(ON_INPUT_SIZE_CHANGED_EVENT, MapBuilder.of("registrationName", ON_INPUT_SIZE_CHANGED_EVENT))
                .put(ON_CLICK_SELECT_ALBUM_EVENT, MapBuilder.of("registrationName", ON_CLICK_SELECT_ALBUM_EVENT))
                .put(SWITCH_CAMERA_MODE_EVENT, MapBuilder.of("registrationName", SWITCH_CAMERA_MODE_EVENT))
                .put(CLOSE_CAMERA_EVENT, MapBuilder.of("registrationName", CLOSE_CAMERA_EVENT))
                .build();
    }

    @Override
    public void onDropViewInstance(ChatInputView view) {
        super.onDropViewInstance(view);
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("close_soft_input", CLOSE_SOFT_INPUT);
    }

    @Override
    public void receiveCommand(ChatInputView root, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case INIT_MENU_HEIGHT:
                if (args == null) {
                    return;
                }
                mMenuContainerHeight = root.dp2px(args.getInt(0));
                mSoftKeyboardHeight = mMenuContainerHeight;
                root.setMenuContainerHeight(mMenuContainerHeight);
                break;
            case CLOSE_SOFT_INPUT:
                EmoticonsKeyboardUtils.closeSoftKeyboard(root.getInputView());
                break;
            case GET_INPUT_TEXT:
                EventBus.getDefault().post(new GetTextEvent(root.getInputView().getText().toString(),
                        AuroraIMUIModule.GET_INPUT_TEXT_EVENT));
                break;
            case RESET_MENU_STATE:
                try {
                    WritableMap event = Arguments.createMap();
                    mShowMenu = args.getBoolean(0);
                    mContext.getCurrentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    if (mInitState) {
                        event.putDouble("height", mInitialChatInputHeight);
                    } else {
                        event.putDouble("height", calculateMenuHeight());
                    }
                    mContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
