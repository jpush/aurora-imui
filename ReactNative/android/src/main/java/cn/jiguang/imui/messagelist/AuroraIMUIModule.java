package cn.jiguang.imui.messagelist;


import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

/**
 * Created by caiyaoguan on 2017/6/2.
 */

public class AuroraIMUIModule extends ReactContextBaseJavaModule {

    private final String REACT_MSG_LIST_MODULE = "AuroraIMUIModule";

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

    }

    @ReactMethod
    public void appendMessages(ReadableArray messages) {
        String[] rctMessages = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            RCTMessage rctMessage = configMessage(messages.getMap(i));
            rctMessages[i] = rctMessage.toString();
        }
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_APPEND_MESSAGES_ACTION);
        intent.putExtra("messages", rctMessages);
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void updateMessage(ReadableMap message) {
        RCTMessage rctMessage = configMessage(message);
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_UPDATE_MESSAGE_ACTION);
        intent.putExtra("message", rctMessage.toString());
        getReactApplicationContext().sendBroadcast(intent);
    }

    @ReactMethod
    public void insertMessagesToTop(ReadableArray messages) {
        String[] rctMessages = new String[messages.size()];
        for (int i = 0; i < messages.size(); i++) {
            RCTMessage rctMessage = configMessage(messages.getMap(i));
            rctMessages[i] = rctMessage.toString();
        }
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_INSERT_MESSAGES_ACTION);
        intent.putExtra("messages", rctMessages);
        getReactApplicationContext().sendBroadcast(intent);
    }

    private RCTMessage configMessage(ReadableMap message) {
        Log.d("AuroraIMUIModule", "configure message: " + message);
        RCTMessage rctMsg = new RCTMessage(message.getString(RCTMessage.MSG_ID),
                message.getString(RCTMessage.STATUS), message.getString(RCTMessage.MSG_TYPE),
                message.getBoolean(RCTMessage.IS_OUTGOING));
        switch (rctMsg.getType()) {
            case SEND_VOICE:
            case RECEIVE_VOICE:
            case SEND_VIDEO:
            case RECEIVE_VIDEO:
                rctMsg.setMediaFilePath(message.getString("mediaPath"));
                rctMsg.setDuration(message.getInt("duration"));
                break;
            case SEND_IMAGE:
            case RECEIVE_IMAGE:
                rctMsg.setMediaFilePath(message.getString("mediaPath"));
                break;
            default:
                rctMsg.setText(message.getString("text"));
        }
        ReadableMap user = message.getMap("fromUser");
        RCTUser rctUser = new RCTUser(user.getString("userId"), user.getString("displayName"),
                user.getString("avatarPath"));
        Log.d("AuroraIMUIModule", "fromUser: " + rctUser);
        rctMsg.setFromUser(rctUser);
        try {
            String timeString = message.getString("timeString");
            if (timeString != null) {
                rctMsg.setTimeString(timeString);
            }
            String progress = message.getString("progress");
            if (progress != null) {
                rctMsg.setProgress(progress);
            }
            return rctMsg;
        } catch (Exception e) {
            e.printStackTrace();
            return rctMsg;
        }
    }

    @ReactMethod
    public void scrollToBottom() {
        Intent intent = new Intent();
        intent.setAction(ReactMsgListManager.RCT_SCROLL_TO_BOTTOM_ACTION);
        getReactApplicationContext().sendBroadcast(intent);
    }



}
