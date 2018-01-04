package cn.jiguang.imui.messagelist;

import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
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
import cn.jiguang.imui.chatinput.emoji.EmoticonsKeyboardUtils;
import cn.jiguang.imui.chatinput.listener.CameraControllerListener;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.messagelist.event.GetTextEvent;
import cn.jiguang.imui.messagelist.event.OnTouchMsgListEvent;
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
    private static final String ON_INPUT_SIZE_CHANGED_EVENT = "onSizeChange";
    private static final String ON_CLICK_SELECT_ALBUM_EVENT = "onClickSelectAlbum";
    private final int REQUEST_PERMISSION = 0x0001;
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
    private int mScreenWidth;
    /**
     * Initial soft input height, set this value via {@link #setMenuContainerHeight}
     */
    private int mMenuContainerHeight = 831;

    @Override
    public String getName() {
        return REACT_CHAT_INPUT;
    }

    @Override
    protected ChatInputView createViewInstance(final ThemedReactContext reactContext) {
        mContext = reactContext;
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
                        mChatInput.invisibleMenuLayout();
                    }
                    EventBus.getDefault().post(new ScrollEvent(false));

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
        final float density = dm.density;
        mScreenWidth = dm.widthPixels;
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
                double layoutHeight = calculateMenuHeight(density);
                mInitState = false;
                event.putDouble("height", layoutHeight);
                editText.setLayoutParams(new LinearLayout.LayoutParams(mWidth, (int)(mCurrentInputHeight)));
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
                WritableMap map = Arguments.createMap();
                map.putDouble("height", mInitialChatInputHeight);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, map);
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
                initMenu(density);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_MIC_EVENT, null);
                return true;
            }

            @Override
            public boolean switchToGalleryMode() {
                initMenu(density);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_GALLERY_EVENT, null);
                return true;
            }

            @Override
            public boolean switchToCameraMode() {
                initMenu(density);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        SWITCH_TO_CAMERA_EVENT, null);
                return true;
            }

            @Override
            public boolean switchToEmojiMode() {
                initMenu(density);
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

        mChatInput.setCameraControllerListener(new CameraControllerListener() {
            @Override
            public void onFullScreenClick() {
                FrameLayout container = mChatInput.getCameraContainer();
                container.bringToFront();
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        ON_FULL_SCREEN_EVENT, null);
            }

            @Override
            public void onRecoverScreenClick() {
                moveToBack(mChatInput.getCameraContainer());
                FrameLayout container = mChatInput.getCameraContainer();
                container.setLayoutParams(new FrameLayout.LayoutParams(mScreenWidth, mChatInput.getSoftKeyboardHeight()));
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(),
                        ON_RECOVER_SCREEN_EVENT, null);
                WritableMap map = Arguments.createMap();
                map.putDouble("height", mInitialChatInputHeight + mChatInput.getSoftKeyboardHeight() / density);
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, map);
            }

            @Override
            public void onCloseCameraClick() {

            }

            @Override
            public void onSwitchCameraModeClick() {

            }
        });
        mChatInput.getSelectAlbumBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_CLICK_SELECT_ALBUM_EVENT, null);
            }
        });
        return mChatInput;
    }

    private void initMenu(float density) {
        mShowMenu = true;
        if (mChatInput.getSoftInputState()) {
            EmoticonsKeyboardUtils.closeSoftKeyboard(mChatInput.getInputView());
        }
        if (mChatInput.getSoftInputState() || mChatInput.getMenuState() == View.VISIBLE) {
            EventBus.getDefault().post(new ScrollEvent(false));
        } else {
            EventBus.getDefault().post(new ScrollEvent(true));
        }
        WritableMap event = Arguments.createMap();
        if (mChatInput.getSoftKeyboardHeight() == 0) {
            event.putDouble("height", mInitialChatInputHeight + mMenuContainerHeight / density);
        } else {
            event.putDouble("height", mInitialChatInputHeight + mChatInput.getSoftKeyboardHeight() / density);
        }
        mContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, event);
    }

    private double calculateMenuHeight(float density) {
        double layoutHeight = mInitialChatInputHeight;
        if (mShowMenu) {
            layoutHeight += mChatInput.getSoftKeyboardHeight() / density;
        }
        switch (mChatInput.getInputView().getLineCount()) {
            case 0:
            case 1:
                mCurrentInputHeight = 48 * density;
                break;
            case 2:
                if (mShowMenu)
                    layoutHeight += 5.4;
                mCurrentInputHeight = 53.4 * density;
                break;
            case 3:
                layoutHeight += 25.5;
                mCurrentInputHeight = 73.5 * density;
                break;
            default:
                layoutHeight += 45.7;
                mCurrentInputHeight = 93.7 * density;
        }
        return layoutHeight;
    }

    private void moveToBack(View currentView) {
        ViewGroup viewGroup = ((ViewGroup) currentView.getParent());
        int index = viewGroup.indexOfChild(currentView);
        for(int i = 0; i<index; i++) {
            viewGroup.bringChildToFront(viewGroup.getChildAt(i));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StopPlayVoiceEvent event) {
        mChatInput.pauseVoice();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnTouchMsgListEvent event) {
        WritableMap map = Arguments.createMap();
        map.putDouble("height", mInitialChatInputHeight);
        mContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, map);
    }

    @ReactProp(name = "chatInputBackgroupColor")
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
        chatInputView.getSelectAlbumBtn().setVisibility(flag? View.VISIBLE: View.GONE);
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
        return MapBuilder.of("close_soft_input",CLOSE_SOFT_INPUT);
    }

    @Override
    public void receiveCommand(ChatInputView root, int commandId, @Nullable ReadableArray args) {
        switch (commandId){
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
                    float dp = mContext.getResources().getDisplayMetrics().density;
                    mContext.getCurrentActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    if (mInitState) {
                        event.putDouble("height", mInitialChatInputHeight);
                    } else {
                        event.putDouble("height", calculateMenuHeight(dp));
                    }
                    mContext.getJSModule(RCTEventEmitter.class).receiveEvent(mChatInput.getId(), ON_INPUT_SIZE_CHANGED_EVENT, event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
