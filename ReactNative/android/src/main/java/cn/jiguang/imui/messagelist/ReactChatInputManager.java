package cn.jiguang.imui.messagelist;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateFormat;
import android.util.Log;

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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnClickEditTextListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;

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
    private static final String TAKE_PICTURE_EVENT = "onTakePicture";
    private static final String START_RECORD_VIDEO_EVENT = "onStartRecordVideo";
    private static final String FINISH_RECORD_VIDEO_EVENT = "onFinishRecordVideo";
    private static final String CANCEL_RECORD_VIDEO_EVENT = "onCancelRecordVideo";
    private static final String START_RECORD_VOICE_EVENT = "onStartRecordVoice";
    private static final String FINISH_RECORD_VOICE_EVENT = "onFinishRecordVoice";
    private static final String CANCEL_RECORD_VOICE_EVENT = "onCancelRecordVoice";
    private static final String ON_TOUCH_EDIT_TEXT_EVENT = "onTouchEditText";
    private final int REQUEST_PERMISSION = 0x0001;

    @Override
    public String getName() {
        return REACT_CHAT_INPUT;
    }

    @Override
    protected ChatInputView createViewInstance(final ThemedReactContext reactContext) {
        final Activity activity = reactContext.getCurrentActivity();
        final ChatInputView chatInput = new ChatInputView(activity, null);
        chatInput.setMenuContainerHeight(1000);
        // Use default layout
        chatInput.setMenuClickListener(new OnMenuClickListener() {
            @Override
            public boolean onSendTextMessage(CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
                WritableMap event = Arguments.createMap();
                event.putString("text", input.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(), ON_SEND_TEXT_EVENT, event);
                return true;
            }

            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }
                WritableMap event = Arguments.createMap();
                WritableArray array = new WritableNativeArray();
                for (FileItem fileItem: list) {
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
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(), ON_SEND_FILES_EVENT, event);
            }

            @Override
            public void switchToMicrophoneMode() {
                Activity activity = reactContext.getCurrentActivity();
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
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        SWITCH_TO_MIC_EVENT, null);
            }

            @Override
            public void switchToGalleryMode() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        SWITCH_TO_GALLERY_EVENT, null);
            }

            @Override
            public void switchToCameraMode() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        SWITCH_TO_CAMERA_EVENT, null);
            }
        });

        chatInput.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                WritableMap event = Arguments.createMap();
                event.putString("mediaPath", photoPath);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        TAKE_PICTURE_EVENT, event);
            }

            @Override
            public void onStartVideoRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        START_RECORD_VIDEO_EVENT, null);
            }

            @Override
            public void onFinishVideoRecord(String videoPath) {
                WritableMap event = Arguments.createMap();
                event.putString("mediaPath", videoPath);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        FINISH_RECORD_VIDEO_EVENT, event);
            }

            @Override
            public void onCancelVideoRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        CANCEL_RECORD_VIDEO_EVENT, null);
            }
        });

        chatInput.getRecordVoiceButton().setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        START_RECORD_VOICE_EVENT, null);
                File rootDir = reactContext.getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/voice";
                chatInput.getRecordVoiceButton().setVoiceFilePath(fileDir, DateFormat.format("yyyy_MMdd_hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                WritableMap event = Arguments.createMap();
                event.putString("mediaPath", voiceFile.getAbsolutePath());
                event.putInt("duration", duration);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        FINISH_RECORD_VOICE_EVENT, event);
            }

            @Override
            public void onCancelRecord() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        CANCEL_RECORD_VOICE_EVENT, null);
            }
        });

        chatInput.setOnClickEditTextListener(new OnClickEditTextListener() {
            @Override
            public void onTouchEditText() {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(chatInput.getId(),
                        ON_TOUCH_EDIT_TEXT_EVENT, null);
            }
        });
        return chatInput;
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
        return MapBuilder.<String, Object> builder()
                .put(ON_SEND_TEXT_EVENT, MapBuilder.of("registrationName", ON_SEND_TEXT_EVENT))
                .put(ON_SEND_FILES_EVENT, MapBuilder.of("registrationName", ON_SEND_FILES_EVENT))
                .put(SWITCH_TO_MIC_EVENT, MapBuilder.of("registrationName", SWITCH_TO_MIC_EVENT))
                .put(SWITCH_TO_GALLERY_EVENT, MapBuilder.of("registrationName", SWITCH_TO_GALLERY_EVENT))
                .put(SWITCH_TO_CAMERA_EVENT, MapBuilder.of("registrationName", SWITCH_TO_CAMERA_EVENT))
                .put(TAKE_PICTURE_EVENT, MapBuilder.of("registrationName", TAKE_PICTURE_EVENT))
                .put(START_RECORD_VIDEO_EVENT, MapBuilder.of("registrationName", START_RECORD_VIDEO_EVENT))
                .put(FINISH_RECORD_VIDEO_EVENT, MapBuilder.of("registrationName", FINISH_RECORD_VIDEO_EVENT))
                .put(CANCEL_RECORD_VIDEO_EVENT, MapBuilder.of("registrationName", CANCEL_RECORD_VIDEO_EVENT))
                .put(START_RECORD_VOICE_EVENT, MapBuilder.of("registrationName", START_RECORD_VOICE_EVENT))
                .put(FINISH_RECORD_VOICE_EVENT, MapBuilder.of("registrationName", FINISH_RECORD_VOICE_EVENT))
                .put(CANCEL_RECORD_VOICE_EVENT, MapBuilder.of("registrationName", CANCEL_RECORD_VOICE_EVENT))
                .put(ON_TOUCH_EDIT_TEXT_EVENT, MapBuilder.of("registrationName", ON_TOUCH_EDIT_TEXT_EVENT))
                .build();
    }


}
