package cn.jiguang.imui.messagelist;


import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.jiguang.imui.commons.BitmapLoader;
import cn.jiguang.imui.messagelist.event.GetTextEvent;
import cn.jiguang.imui.messagelist.event.LoadedEvent;
import cn.jiguang.imui.messagelist.event.MessageEvent;
import cn.jiguang.imui.messagelist.event.ScrollEvent;
import cn.jiguang.imui.messagelist.event.StopPlayVoiceEvent;
import cn.jiguang.imui.messagelist.model.RCTMessage;
import cn.jiguang.imui.messagelist.model.RCTUser;
import cn.jiguang.imui.messagelist.viewmanager.ReactMsgListManager;

import static cn.jiguang.imui.messagelist.viewmanager.ReactMsgListManager.RCT_REMOVE_ALL_MESSAGE_ACTION;
import static cn.jiguang.imui.messagelist.viewmanager.ReactMsgListManager.RCT_REMOVE_MESSAGE_ACTION;

public class AuroraIMUIModule extends ReactContextBaseJavaModule {

    private final String REACT_MSG_LIST_MODULE = "AuroraIMUIModule";
    public static final String RCT_MESSAGE_LIST_LOADED_ACTION = "cn.jiguang.imui.messagelist.intent.messageLoaded";

    private static final String MESSAGE_LIST_DID_LOAD_EVENT = "IMUIMessageListDidLoad";
    public static final String GET_INPUT_TEXT_EVENT = "getInputText";

    public AuroraIMUIModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_MSG_LIST_MODULE;
    }

    @Override
    public void initialize() {
        super.initialize();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoadedEvent event) {
        Log.d(REACT_MSG_LIST_MODULE, "Message did load");
        if (event.getAction().equals(RCT_MESSAGE_LIST_LOADED_ACTION)) {
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(MESSAGE_LIST_DID_LOAD_EVENT, null);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetTextEvent event) {
        if (event.getAction().equals(GET_INPUT_TEXT_EVENT)) {
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(GET_INPUT_TEXT_EVENT, event.getText());
        }
    }

    @ReactMethod
    public void hidenFeatureView(boolean flag) {

    }

    @ReactMethod
    public void appendMessages(ReadableArray messages) {
        RCTMessage[] rctMessages = new RCTMessage[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            RCTMessage rctMessage = configMessage(messages.getMap(i));
            rctMessages[i] = rctMessage;
        }
        EventBus.getDefault().post(new MessageEvent(rctMessages, ReactMsgListManager.RCT_APPEND_MESSAGES_ACTION));
    }

    @ReactMethod
    public void appendCustomMessage(ReadableMap message) {
        RCTMessage rctMessage = configMessage(message);
        RCTMessage[] array = new RCTMessage[1];
        array[0] = rctMessage;
        EventBus.getDefault().post(new MessageEvent(array, ReactMsgListManager.RCT_APPEND_MESSAGES_ACTION));
    }

    @ReactMethod
    public void updateMessage(ReadableMap message) {
        RCTMessage rctMessage = configMessage(message);
        EventBus.getDefault().post(new MessageEvent(rctMessage, ReactMsgListManager.RCT_UPDATE_MESSAGE_ACTION));
    }

    /**
     * 按照时间顺序将消息插入消息列表顶部。
     * @param messages 按照时间顺序排列，最新的消息在最后面。
     */
    @ReactMethod
    public void insertMessagesToTop(ReadableArray messages) {
        RCTMessage[] rctMessages = new RCTMessage[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            RCTMessage rctMessage = configMessage(messages.getMap(i));
            rctMessages[i] = rctMessage;
        }
        EventBus.getDefault().post(new MessageEvent(rctMessages, ReactMsgListManager.RCT_INSERT_MESSAGES_ACTION));
    }

    @ReactMethod
    public void stopPlayVoice() {
        EventBus.getDefault().post(new StopPlayVoiceEvent());
    }

    private RCTMessage configMessage(ReadableMap message) {
        Log.d("AuroraIMUIModule", "configure message: " + message);
        RCTMessage rctMsg = new RCTMessage(message.getString(RCTMessage.MSG_ID),
                message.getString(RCTMessage.STATUS), message.getString(RCTMessage.MSG_TYPE),
                message.getBoolean(RCTMessage.IS_OUTGOING));
        switch (rctMsg.getType()) {
            case 5:
            case 6:
            case 7:
            case 8:
                rctMsg.setMediaFilePath(message.getString("mediaPath"));
                rctMsg.setDuration(message.getInt("duration"));
                break;
            case 3:
            case 4:
                rctMsg.setMediaFilePath(message.getString("mediaPath"));
                break;
            case 13:
            case 14:
            default:
                if (message.hasKey("content")) {
                    rctMsg.setText(message.getString("content"));
                } else if (message.hasKey("text")) {
                    rctMsg.setText(message.getString("text"));
                }
                if (message.hasKey("contentSize")) {
                    ReadableMap size = message.getMap("contentSize");
                    if (size.hasKey("width") && size.hasKey("height")) {
                        rctMsg.setContentSize(size.getInt("width"), size.getInt("height"));
                    }
                }
        }
        ReadableMap user = message.getMap("fromUser");
        RCTUser rctUser = new RCTUser(user.getString("userId"), user.getString("displayName"),
                user.getString("avatarPath"));
        Log.d("AuroraIMUIModule", "fromUser: " + rctUser);
        rctMsg.setFromUser(rctUser);
        if (message.hasKey("timeString")) {
            String timeString = message.getString("timeString");
            if (timeString != null) {
                rctMsg.setTimeString(timeString);
            }
        }
        if (message.hasKey("progress")) {
            String progress = message.getString("progress");
            if (progress != null) {
                rctMsg.setProgress(progress);
            }
        }
        if (message.hasKey("extras")) {
            ReadableMap extra = message.getMap("extras");
            ReadableMapKeySetIterator iterator = extra.keySetIterator();
            while (iterator.hasNextKey()) {
                String key = iterator.nextKey();
                rctMsg.putExtra(key, extra.getString(key));
            }
        }
        return rctMsg;
    }

    @ReactMethod
    public void removeMessage(String id) {
        MessageEvent event = new MessageEvent(id, RCT_REMOVE_MESSAGE_ACTION);
        EventBus.getDefault().post(event);
    }

    @ReactMethod
    public void removeAllMessage() {
        MessageEvent event = new MessageEvent("", RCT_REMOVE_ALL_MESSAGE_ACTION);
        EventBus.getDefault().post(event);
    }

    @ReactMethod
    public void scrollToBottom(boolean flag) {
        EventBus.getDefault().post(new ScrollEvent(flag));
    }

    /**
     * 裁剪图片，将图片按比例裁剪成 width * height 大小
     * @param map 包含 path，width，height 参数
     * @param callback 回调包含裁剪后的路径
     */
    @ReactMethod
    public void scaleImage(ReadableMap map, Callback callback) {
        WritableMap result = Arguments.createMap();
        try {
            String path = map.getString("path");
            int width = map.getInt("width");
            int height = map.getInt("height");
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                Bitmap bitmap = BitmapLoader.getBitmapFromFile(path, width, height);
                String fileName = file.getName();
                String thumbPath = saveBitmapToLocal(bitmap, fileName);
                result.putInt("code", 0);
                result.putString("thumbPath", thumbPath);
                callback.invoke(result);
            } else {
                result.putInt("code", -1);
                result.putString("error", "Path is invalid");
                callback.invoke(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.putInt("code", -1);
            result.putString("error", "Scale error. Should check out log.");
            callback.invoke(result);
        }
    }

    /**
     * 压缩图片。压缩后格式为 JPG
     * @param map 需要传入 path, compressionQuality
     * @param callback 回调包含裁剪后的路径
     */
    @ReactMethod
    public void compressImage(ReadableMap map, Callback callback) {
        WritableMap result = Arguments.createMap();
        try {
            String path = map.getString("path");
            double quality =  100 * map.getDouble("compressionQuality");
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                CompressImageAsync task = new CompressImageAsync(callback);
                String fileSuffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                File resultDir = new File(getReactApplicationContext().getFilesDir() + "/thumbnails/");
                if (!resultDir.exists()) {
                    resultDir.mkdirs();
                }
                String resultPath = resultDir.getPath() + "/" + file.getName();
                Log.d("AuroraIMUIModule", "compress image result path: " + resultPath);
                task.execute(path, (int) quality + "", resultPath, fileSuffix);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.putInt("code", -1);
            result.putString("error", "Scale error. Should check out log.");
            callback.invoke(result);
        }
    }

    /**
     * 根据bitmap保存图片到本地
     *
     * @param bitmap bitmap
     * @return file path
     */
    private String saveBitmapToLocal(Bitmap bitmap, String fileName) {
        if (null == bitmap) {
            return null;
        }
        String filePath;
        FileOutputStream fileOutput = null;
        File imgFile;
        try {
            File desDir = new File(getReactApplicationContext().getFilesDir() + "/thumbnails/");
            if (!desDir.exists()) {
                desDir.mkdirs();
            }
            imgFile = new File(getReactApplicationContext().getFilesDir() + "/thumbnails/", fileName);
            imgFile.createNewFile();
            fileOutput = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutput);
            fileOutput.flush();
            filePath = imgFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            filePath = null;
        } catch (IOException e) {
            e.printStackTrace();
            filePath = null;
        } finally {
            if (null != fileOutput) {
                try {
                    fileOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return filePath;
    }

}
