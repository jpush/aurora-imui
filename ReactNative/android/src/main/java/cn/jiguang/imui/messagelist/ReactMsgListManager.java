package cn.jiguang.imui.messagelist;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
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
    public static final String SEND_MESSAGE = "send_message";
    private static final String RECEIVE_MESSAGE = "receive_message";
    private static final String LOAD_HISTORY = "load_history_message";
    private static final String UPDATE_MESSAGE = "update_message";

    private static final String ON_AVATAR_CLICK_EVENT = "onAvatarClick";
    private static final String ON_MSG_CLICK_EVENT = "onMsgClick";
    private static final String ON_MSG_LONG_CLICK_EVENT = "onMsgLongClick";
    private static final String ON_STATUS_VIEW_CLICK_EVENT = "onStatusViewClick";
    private static final String ON_TOUCH_MSG_LIST_EVENT = "onTouchMsgList";
    private static final String ON_PULL_TO_REFRESH_EVENT = "onPullToRefresh";

    public static final String RCT_APPEND_MESSAGES_ACTION = "cn.jiguang.imui.messagelist.intent.appendMessages";
    public static final String RCT_UPDATE_MESSAGE_ACTION = "cn.jiguang.imui.messagelist.intent.updateMessage";
    public static final String RCT_INSERT_MESSAGES_ACTION = "cn.jiguang.imui.messagelist.intent.insertMessages";
    public static final String RCT_SCROLL_TO_BOTTOM_ACTION = "cn.jiguang.imui.messagelist.intent.scrollToBottom";

    private MsgListAdapter mAdapter;
    private ReactContext mContext;

    @Override
    public String getName() {
        return REACT_MESSAGE_LIST;
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Override
    protected MessageList createViewInstance(final ThemedReactContext reactContext) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RCT_APPEND_MESSAGES_ACTION);
        intentFilter.addAction(RCT_UPDATE_MESSAGE_ACTION);
        intentFilter.addAction(RCT_INSERT_MESSAGES_ACTION);
        intentFilter.addAction(RCT_SCROLL_TO_BOTTOM_ACTION);
        reactContext.registerReceiver(RCTMsgListReceiver, intentFilter);
        mContext = reactContext;
        final MessageList msgList = new MessageList(reactContext, null);
        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                int resId = IdHelper.getDrawable(reactContext, string);
                if (resId != 0) {
                    Log.d("ReactMsgListManager", "Set drawable name: " + string);
                    avatarImageView.setImageResource(resId);
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

        msgList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(msgList.getId(), ON_TOUCH_MSG_LIST_EVENT, null);
                        if (reactContext.getCurrentActivity() != null) {
                            InputMethodManager imm = (InputMethodManager) reactContext.getCurrentActivity()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            Window window = reactContext.getCurrentActivity().getWindow();
                            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        break;
                }
                return false;
            }
        });
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int i, int i1) {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(msgList.getId(),
                        ON_PULL_TO_REFRESH_EVENT, null);
            }
        });
        return msgList;
    }

    @ReactProp(name = "sendBubble")
    public void setSendBubble(MessageList messageList, ReadableMap map) {
        int resId = mContext.getResources().getIdentifier(map.getString("imageName"),
                "drawable", mContext.getPackageName());
        if (resId != 0) {
            messageList.setSendBubbleDrawable(resId);
        }
    }

    @ReactProp(name = "receiveBubble")
    public void setReceiveBubble(MessageList messageList, ReadableMap map) {
        int resId = mContext.getResources().getIdentifier(map.getString("imageName"),
                "drawable", mContext.getPackageName());
        if (resId != 0) {
            messageList.setReceiveBubbleDrawable(resId);
        }
    }

    @ReactProp(name = "sendBubbleTextColor")
    public void setSendBubbleTextColor(MessageList messageList, String color) {
        int colorRes = Color.parseColor(color);
        messageList.setSendBubbleTextColor(colorRes);
    }

    @ReactProp(name = "receiveBubbleTextColor")
    public void setReceiveBubbleTextColor(MessageList messageList, String color) {
        int colorRes = Color.parseColor(color);
        messageList.setReceiveBubbleTextColor(colorRes);
    }

    @ReactProp(name = "sendBubbleTextSize")
    public void setSendBubbleTextSize(MessageList messageList, int size) {
        messageList.setSendBubbleTextSize(size);
    }

    @ReactProp(name = "receiveBubbleTextSize")
    public void setReceiveBubbleTextSize(MessageList messageList, int size) {
        messageList.setReceiveBubbleTextSize(size);
    }

    @ReactProp(name = "sendBubblePadding")
    public void setSendBubblePadding(MessageList messageList, ReadableMap map) {
        messageList.setSendBubblePaddingLeft(map.getInt("left"));
        messageList.setSendBubblePaddingTop(map.getInt("top"));
        messageList.setSendBubblePaddingRight(map.getInt("right"));
        messageList.setSendBubblePaddingBottom(map.getInt("bottom"));
    }

    @ReactProp(name = "receiveBubblePadding")
    public void setReceiveBubblePaddingLeft(MessageList messageList, ReadableMap map) {
        messageList.setReceiveBubblePaddingLeft(map.getInt("left"));
        messageList.setReceiveBubblePaddingTop(map.getInt("top"));
        messageList.setReceiveBubblePaddingRight(map.getInt("right"));
        messageList.setReceiveBubblePaddingBottom(map.getInt("bottom"));
    }

    @ReactProp(name = "dateTextSize")
    public void setDateTextSize(MessageList messageList, int size) {
        messageList.setDateTextSize(size);
    }

    @ReactProp(name = "dateTextColor")
    public void setDateTextColor(MessageList messageList, String color) {
        int colorRes = Color.parseColor(color);
        messageList.setDateTextColor(colorRes);
    }

    @ReactProp(name = "datePadding")
    public void setDatePadding(MessageList messageList, int padding) {
        messageList.setDatePadding(padding);
    }

    @ReactProp(name = "avatarSize")
    public void setAvatarWidth(MessageList messageList, ReadableMap map) {
        messageList.setAvatarWidth(map.getInt("width"));
        messageList.setAvatarHeight(map.getInt("height"));
    }

    /**
     * if showDisplayName equals 1, then show display name.
     *
     * @param messageList       MessageList
     * @param isShowDisplayName boolean
     */
    @ReactProp(name = "isShowDisplayName")
    public void setShowDisplayName(MessageList messageList, boolean isShowDisplayName) {
        if (isShowDisplayName) {
            messageList.setShowDisplayName(1);
        }
    }

    @SuppressWarnings("unchecked")
    private BroadcastReceiver RCTMsgListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Activity activity = mContext.getCurrentActivity();
            Gson gson = new GsonBuilder().registerTypeAdapter(RCTMessage.class, new RCTMessageDeserializer())
                    .create();
            if (intent.getAction().equals(RCT_APPEND_MESSAGES_ACTION)) {
                String[] messages = intent.getStringArrayExtra("messages");
                for (String rctMsgStr : messages) {
                    final RCTMessage rctMessage = gson.fromJson(rctMsgStr, RCTMessage.class);
                    Log.d("RCTMessageListManager", "Add message to start, message: " + rctMsgStr);
                    Log.d("RCTMessageListManager", "RCTMessage: " + rctMessage);
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.addToStart(rctMessage, true);
                            }
                        });
                    }
                }
            } else if (intent.getAction().equals(RCT_UPDATE_MESSAGE_ACTION)) {
                String message = intent.getStringExtra("message");
                final RCTMessage rctMessage = gson.fromJson(message, RCTMessage.class);
                Log.d("RCTMessageListManager", "updating message, message: " + rctMessage);
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.updateMessage(rctMessage.getMsgId(), rctMessage);
                        }
                    });
                }
            } else if (intent.getAction().equals(RCT_INSERT_MESSAGES_ACTION)) {
                String[] messages = intent.getStringArrayExtra("messages");
                List<RCTMessage> list = new ArrayList<>();
                for (int i = messages.length - 1; i > -1; i--) {
                    final RCTMessage rctMessage = gson.fromJson(messages[i], RCTMessage.class);
                    list.add(rctMessage);
                }
                Log.d("RCTMessageListManager", "Add send message to top, messages: " + list.toString());
                mAdapter.addToEnd(list);
            } else if (intent.getAction().equals(RCT_SCROLL_TO_BOTTOM_ACTION)) {
                Log.i("RCTMessageListManager", "Scroll to bottom");
                mAdapter.getLayoutManager().scrollToPosition(0);
            }
        }
    };

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(ON_AVATAR_CLICK_EVENT, MapBuilder.of("registrationName", ON_AVATAR_CLICK_EVENT))
                .put(ON_MSG_CLICK_EVENT, MapBuilder.of("registrationName", ON_MSG_CLICK_EVENT))
                .put(ON_MSG_LONG_CLICK_EVENT, MapBuilder.of("registrationName", ON_MSG_LONG_CLICK_EVENT))
                .put(ON_STATUS_VIEW_CLICK_EVENT, MapBuilder.of("registrationName", ON_STATUS_VIEW_CLICK_EVENT))
                .put(ON_TOUCH_MSG_LIST_EVENT, MapBuilder.of("registrationName", ON_TOUCH_MSG_LIST_EVENT))
                .put(ON_PULL_TO_REFRESH_EVENT, MapBuilder.of("registrationName", ON_PULL_TO_REFRESH_EVENT))
                .build();
    }

    @Override
    public void onDropViewInstance(MessageList view) {
        super.onDropViewInstance(view);

    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        mContext.unregisterReceiver(RCTMsgListReceiver);
    }
}
