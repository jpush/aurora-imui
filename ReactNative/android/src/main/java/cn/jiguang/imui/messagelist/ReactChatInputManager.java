package cn.jiguang.imui.messagelist;

import android.Manifest;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.common.MapBuilder;
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
import cn.jiguang.imui.chatinput.listener.CameraControllerListener;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.messagelist.event.ScrollEvent;
import cn.jiguang.imui.messagelist.event.StopPlayVoiceEvent;

/**
 * Created by caiyaoguan on 2017/5/22.
 */

public class ReactChatInputManager extends ViewGroupManager<ChatInputView> {

    private static final String REACT_CHAT_INPUT = "RCTChatInput";

    private static final String ON_SEND_TEXT_EVENT = "onSendText";
    private static final String ON_SEND_FILES_EVENT = "onSendGalleryFiles";
    private static final String SWITCH_TO_MIC_EVENT = "onSwitchToMicrophoneMode";
    private static final String SWITCH_TO_GALLERY_EVENT = "onSwitchToGalleryMode";
    private static final String SWITCH_TO_CAMERA_EVENT = "onSwitchToCameraMode";
    private static final String SWITCH_TO_EMOJI_EVENT = "onSwitchToEmojiMode";
    private static final String TAKE_PICTURE_EVENT = "onTakePicture";
    private static final String START_RECORD_VIDEO_EVENT = "onStartRecordVideo";
    private static final String FINISH_RECORD_VIDEO_EVENT = "onFinishRecordVideo";
    private static final String CANCEL_RECORD_VIDEO_EVENT = "onCancelRecordVideo";
    private static final String START_RECORD_VOICE_EVENT = "onStartRecordVoice";
    private static final String FINISH_RECORD_VOICE_EVENT = "onFinishRecordVoice";
    private static final String CANCEL_RECORD_VOICE_EVENT = "onCancelRecordVoice";
    private static final String ON_TOUCH_EDIT_TEXT_EVENT = "onTouchEditText";
    private static final String ON_FULL_SCREEN_EVENT = "onFullScreen";
    private static final String ON_RECOVER_SCREEN_EVENT = "onRecoverScreen";
    private final int REQUEST_PERMISSION = 0x0001;

    private boolean mIsShowSoftInput;
    private ChatInputView mChatInput;

    @Override
    public String getName() {
        return REACT_CHAT_INPUT;
    }

    @Override
    protected ChatInputView createViewInstance(final ThemedReactContext reactContext) {
        EventBus.getDefault().register(this);
        final Activity activity = reactContext.getCurrentActivity();
        mChatInput = new ChatInputView(activity, null);
        mChatInput.getInputView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mIsShowSoftInput = true;
                    reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_TOUCH_EDIT_TEXT_EVENT, null);
                    mChatInput.dismissMenuLayout();
                    mChatInput.getInputView().requestFocus();
                    EventBus.getDefault().post(new ScrollEvent(false));
                }
                return false;
            }
        });
        // Use default layout
        mChatInput.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
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
                for (FileItem fileItem : list) {
                    WritableMap map = new WritableNativeMap();
                    if (fileItem.getType().ordinal() == 0) {
                        map.putString("mediaType", "image");
                    } else {
                        map.putString("mediaType", "video");
                        map.putInt("duration", (int) ((VideoItem) fileItem).getDuration());
                    }
                    map.putString("mediaPath", fileItem.getFilePath());
                    array.pushMap(map);
                }
                event.putArray("mediaFiles", array);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_SEND_FILES_EVENT, event);
            }

            @Override
            public boolean switchToMicrophoneMode() {
                if (mChatInput.getSoftInputState() || mChatInput.getMenuState() == View.VISIBLE) {
                    EventBus.getDefault().post(new ScrollEvent(false));
                } else {
                    EventBus.getDefault().post(new ScrollEvent(true));
                }
                String[] perms = new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };

//                if ((ActivityCompat.checkSelfPermission(activity, perms[0]) != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(activity, perms[1]) != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(activity, perms[2]) != PackageManager.PERMISSION_GRANTED
//                        && ActivityCompat.checkSelfPermission(activity, perms[3]) != PackageManager.PERMISSION_GRANTED)) {
//                    ActivityCompat.requestPermissions(activity, perms, REQUEST_PERMISSION);
//                }
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_MIC_EVENT, null);
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {
                if (mChatInput.getSoftInputState() || mChatInput.getMenuState() == View.VISIBLE) {
                    EventBus.getDefault().post(new ScrollEvent(false));
                } else {
                    EventBus.getDefault().post(new ScrollEvent(true));
                }
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_GALLERY_EVENT, null);
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                if (mChatInput.getSoftInputState() || mChatInput.getMenuState() == View.VISIBLE) {
                    EventBus.getDefault().post(new ScrollEvent(false));
                } else {
                    EventBus.getDefault().post(new ScrollEvent(true));
                }
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_CAMERA_EVENT, null);
                return true;
            }

            @Override
            public boolean switchToEmojiMode() {
                if (mChatInput.getSoftInputState() || mChatInput.getMenuState() == View.VISIBLE) {
                    EventBus.getDefault().post(new ScrollEvent(false));
                } else {
                    EventBus.getDefault().post(new ScrollEvent(true));
                }
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_EMOJI_EVENT, null);
                return true;
            }
        });

        mChatInput.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                WritableMap event = Arguments.createMap();
                event.putString("mediaPath", photoPath);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        TAKE_PICTURE_EVENT, event);
            }

            @Override
            public void onStartVideoRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        START_RECORD_VIDEO_EVENT, null);
            }

            @Override
            public void onFinishVideoRecord(String videoPath) {
                WritableMap event = Arguments.createMap();
                event.putString("mediaPath", videoPath);
                MediaPlayer mediaPlayer = MediaPlayer.create(reactContext, Uri.parse(videoPath));
                int duration = mediaPlayer.getDuration() / 1000;    // Millisecond to second.
                mediaPlayer.release();
                event.putInt("duration", duration);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        FINISH_RECORD_VIDEO_EVENT, event);
            }

            @Override
            public void onCancelVideoRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        CANCEL_RECORD_VIDEO_EVENT, null);
            }
        });

        mChatInput.getRecordVoiceButton().setRecordVoiceListener(new RecordVoiceListener() {
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
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        FINISH_RECORD_VOICE_EVENT, event);
            }

            @Override
            public void onCancelRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        CANCEL_RECORD_VOICE_EVENT, null);
            }
        });

        mChatInput.setOnClickEditTextListener(new OnClickEditTextListener() {
            @Override
            public void onTouchEditText() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        ON_TOUCH_EDIT_TEXT_EVENT, null);
            }
        });
        mChatInput.setCameraControllerListener(new CameraControllerListener() {
            @Override
            public void onFullScreenClick() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        ON_FULL_SCREEN_EVENT, null);
                mChatInput.bringToFront();
            }

            @Override
            public void onRecoverScreenClick() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        ON_RECOVER_SCREEN_EVENT, null);
            }

            @Override
            public void onCloseCameraClick() {

            }

            @Override
            public void onSwitchCameraModeClick() {

            }
        });
        return mChatInput;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StopPlayVoiceEvent event) {
        mChatInput.pauseVoice();
    }

    @ReactProp(name = "menuContainerHeight")
    public void setMenuContainerHeight(ChatInputView chatInputView, int height) {
        Log.d("ReactChatInputManager", "Setting menu container height: " + height);
        chatInputView.setMenuContainerHeight(height);
    }

    @ReactProp(name = "isDismissMenuContainer")
    public void dismissMenuContainer(ChatInputView chatInputView, boolean isDismiss) {
        if (isDismiss) {
            chatInputView.dismissMenuLayout();
        }
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
                .build();
    }

    @Override
    public void onDropViewInstance(ChatInputView view) {
        super.onDropViewInstance(view);
        EventBus.getDefault().unregister(this);
    }
}
