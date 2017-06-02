package cn.jiguang.imui.messagelist;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;

/**
 * Created by caiyaoguan on 2017/5/22.
 */

public class ReactMsgListManager extends ViewGroupManager<MessageList> {

    private static final String REACT_MESSAGE_LIST = "RCTMessageList";
    private static final String SEND_MESSAGE = "send_message";
    private static final String RECEIVE_MESSAGE = "receive_message";
    private static final String LOAD_HISTORY = "load_history_message";
    private static final String UPDATE_MESSAGE = "update_message";

    private static final String ON_AVATAR_CLICK_EVENT = "onAvatarClick";
    private static final String ON_MSG_CLICK_EVENT = "onMsgClick";
    private static final String ON_MSG_LONG_CLICK_EVENT = "onMsgLongClick";
    private static final String ON_STATUS_VIEW_CLICK_EVENT = "onStatusViewClick";

    private static final String RCT_ACTION = "cn.jiguang.imui.messagelist.intent.MSGLIST";

    private MsgListAdapter mAdapter;
    private ReactContext mContext;

    @Override
    public String getName() {
        return REACT_MESSAGE_LIST;
    }

    @Override
    protected MessageList createViewInstance(final ThemedReactContext reactContext) {
        mContext = reactContext;
        final MessageList msgList = new MessageList(reactContext, null);
        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                // You can use other image load libraries.
                if (string.contains("R.drawable")) {
                    String drawableName = string.substring(11);
                    Log.d("ReactMsgListManager", "Set drawable name: " + drawableName);
                    avatarImageView.setImageResource(IdHelper.getDrawable(reactContext, drawableName));
                } else {
                    Glide.with(reactContext)
                            .load(string)
                            .placeholder(IdHelper.getDrawable(reactContext, "aurora_headicon_default"))
                            .into(avatarImageView);
                }
            }

            @Override
            public void loadImage(ImageView imageView, String string) {
                // You can use other image load libraries.
                Glide.with(reactContext)
                        .load(string)
                        .fitCenter()
                        .placeholder(IdHelper.getDrawable(reactContext, "aurora_picture_not_found"))
                        .override(400, Target.SIZE_ORIGINAL)
                        .into(imageView);
            }
        };
        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);
        msgList.setAdapter(mAdapter);
        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<RCTMessage>() {
            @Override
            public void onMessageClick(RCTMessage message) {
                WritableMap event = Arguments.createMap();
                event.putString("message", message.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(msgList.getId(), ON_MSG_CLICK_EVENT, event);
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<RCTMessage>() {
            @Override
            public void onMessageLongClick(RCTMessage message) {
                WritableMap event = Arguments.createMap();
                event.putString("message", message.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(msgList.getId(), ON_MSG_LONG_CLICK_EVENT, event);
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<RCTMessage>() {
            @Override
            public void onAvatarClick(RCTMessage message) {
                WritableMap event = Arguments.createMap();
                event.putString("message", message.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(msgList.getId(), ON_AVATAR_CLICK_EVENT, event);
            }
        });

        mAdapter.setMsgResendListener(new MsgListAdapter.OnMsgResendListener<RCTMessage>() {
            @Override
            public void onMessageResend(RCTMessage message) {
                WritableMap event = Arguments.createMap();
                event.putString("message", message.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(msgList.getId(), ON_STATUS_VIEW_CLICK_EVENT, event);
            }
        });
        return msgList;
    }

    @SuppressWarnings("unchecked")
    @ReactProp(name = "action")
    public void handleAction(MessageList messageList, ReadableMap action) {
        String actionType = action.getString("actionType");
        ReadableArray messages = action.getArray("messages");
        if (actionType.equals(LOAD_HISTORY)) {
            List<RCTMessage> historyMsg = new ArrayList<>();
            for (int i = 0; i < messages.size(); i++) {
                RCTMessage rctMessage = configMessage(messages.getMap(i));
                historyMsg.add(rctMessage);
            }
            mAdapter.addToEnd(historyMsg);
        } else {
            for (int i = 0; i < messages.size(); i++) {
                RCTMessage rctMessage = configMessage(messages.getMap(i));
                switch (actionType) {
                    case SEND_MESSAGE:
                        Log.d("RCTMessageListManager", "Add send message to start, message: " + rctMessage);
                        mAdapter.addToStart(rctMessage, true);
                        break;
                    case RECEIVE_MESSAGE:
                        Log.d("RCTMessageListManager", "Add receive message to start, message: " + rctMessage);
                        mAdapter.addToStart(rctMessage, false);
                        break;
                    case UPDATE_MESSAGE:
                        Log.d("RCTMessageListManager", "updating message, message: " + rctMessage);
                        mAdapter.updateMessage(rctMessage.getMsgId(), rctMessage);
                        break;
                }
            }
        }


    }

    private RCTMessage configMessage(ReadableMap message) {
        RCTMessage rctMsg = new RCTMessage(message.getString(RCTMessage.MSG_ID),
                message.getString(RCTMessage.STATUS), message.getString(RCTMessage.MSG_TYPE),
                message.getBoolean(RCTMessage.IS_OUTGOING));
        switch (rctMsg.getType()) {
            case SEND_VOICE:
            case RECEIVE_VOICE:
            case SEND_VIDEO:
            case RECEIVE_VIDEO:
                rctMsg.setMediaFilePath(message.getString("mediaFilePath"));
                rctMsg.setDuration(message.getInt("duration"));
                break;
            case SEND_IMAGE:
            case RECEIVE_IMAGE:
                rctMsg.setMediaFilePath(message.getString("mediaFilePath"));
                break;
            default:
                rctMsg.setText(message.getString("text"));
        }
        ReadableMap user = message.getMap("fromUser");
        RCTUser rctUser = new RCTUser(user.getString("userId"), user.getString("displayName"),
                user.getString("avatarPath"));
        Log.d("RCTMsgListManager", "fromUser: " + rctUser);
        rctMsg.setUser(rctUser);
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

    @ReactProp(name = "sendBubble")
    public void setSendBubble(MessageList messageList, String resName) {
        int resId = mContext.getResources().getIdentifier(resName, "drawable", mContext.getPackageName());
        if (resId != 0) {
            messageList.setSendBubbleDrawable(resId);
        }
    }

    @ReactProp(name = "receiveBubble")
    public void setReceiveBubble(MessageList messageList, String resName) {
        int resId = mContext.getResources().getIdentifier(resName, "drawable", mContext.getPackageName());
        if (resId != 0) {
            messageList.setReceiveBubbleDrawable(resId);
        }
    }

    public static class RCTMsgListReceiver extends BroadcastReceiver {
        public RCTMsgListReceiver() {

        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RCT_ACTION)) {

            }
        }

    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object> builder()
                .put(ON_AVATAR_CLICK_EVENT, MapBuilder.of("registrationName", ON_AVATAR_CLICK_EVENT))
                .put(ON_MSG_CLICK_EVENT, MapBuilder.of("registrationName", ON_MSG_CLICK_EVENT))
                .put(ON_MSG_LONG_CLICK_EVENT, MapBuilder.of("registrationName", ON_MSG_LONG_CLICK_EVENT))
                .put(ON_STATUS_VIEW_CLICK_EVENT, MapBuilder.of("registrationName", ON_STATUS_VIEW_CLICK_EVENT))
                .build();
    }


}
