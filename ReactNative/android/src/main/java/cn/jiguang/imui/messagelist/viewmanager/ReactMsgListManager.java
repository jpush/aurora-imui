package cn.jiguang.imui.messagelist.viewmanager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Nullable;

import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messagelist.AuroraIMUIModule;
import cn.jiguang.imui.messagelist.CustomViewHolder;
import cn.jiguang.imui.messagelist.IdHelper;
import cn.jiguang.imui.messagelist.R;
import cn.jiguang.imui.messagelist.model.RCTMessage;
import cn.jiguang.imui.messagelist.event.LoadedEvent;
import cn.jiguang.imui.messagelist.event.MessageEvent;
import cn.jiguang.imui.messagelist.event.OnTouchMsgListEvent;
import cn.jiguang.imui.messagelist.event.ScrollEvent;
import cn.jiguang.imui.messagelist.event.StopPlayVoiceEvent;
import cn.jiguang.imui.messages.CustomMsgConfig;
import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.MsgListAdapter;
import cn.jiguang.imui.messages.ViewHolderController;
import cn.jiguang.imui.messages.ptr.PtrDefaultHeader;
import cn.jiguang.imui.messages.ptr.PtrHandler;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import cn.jiguang.imui.utils.DisplayUtil;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by caiyaoguan on 2017/5/22.
 */

public class ReactMsgListManager extends ViewGroupManager<PullToRefreshLayout> implements SensorEventListener {

    private static final String REACT_MESSAGE_LIST = "RCTMessageList";
    private final static int STOP_REFRESH = 0;
    private static final String ON_PULL_TO_REFRESH_EVENT = "onPullToRefresh";

    public static final String SEND_MESSAGE = "send_message";
    private static final String RECEIVE_MESSAGE = "receive_message";
    private static final String LOAD_HISTORY = "load_history_message";
    private static final String UPDATE_MESSAGE = "update_message";

    private static final String ON_AVATAR_CLICK_EVENT = "onAvatarClick";
    private static final String ON_MSG_CLICK_EVENT = "onMsgClick";
    private static final String ON_MSG_LONG_CLICK_EVENT = "onMsgLongClick";
    private static final String ON_STATUS_VIEW_CLICK_EVENT = "onStatusViewClick";
    private static final String ON_TOUCH_MSG_LIST_EVENT = "onTouchMsgList";

    public static final String RCT_APPEND_MESSAGES_ACTION = "cn.jiguang.imui.messagelist.intent.appendMessages";
    public static final String RCT_UPDATE_MESSAGE_ACTION = "cn.jiguang.imui.messagelist.intent.updateMessage";
    public static final String RCT_INSERT_MESSAGES_ACTION = "cn.jiguang.imui.messagelist.intent.insertMessages";
    public static final String RCT_SCROLL_TO_BOTTOM_ACTION = "cn.jiguang.imui.messagelist.intent.scrollToBottom";
    public static final String RCT_REMOVE_MESSAGE_ACTION = "cn.jiguang.imui.messagelist.intent.removeMessage";
    public static final String RCT_REMOVE_ALL_MESSAGE_ACTION = "cn.jiguang.imui.messagelist.intent.removeAllMessages";

    private MsgListAdapter mAdapter;
    private ReactContext mContext;
    private MessageList mMessageList;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    @Override
    public String getName() {
        return REACT_MESSAGE_LIST;
    }

    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("unchecked")
    @Override
    protected PullToRefreshLayout createViewInstance(final ThemedReactContext reactContext) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mContext = reactContext;
        final PullToRefreshLayout rootView = (PullToRefreshLayout) LayoutInflater.from(reactContext).inflate(R.layout.ptr_layout, null);
        PtrDefaultHeader header = new PtrDefaultHeader(reactContext);
        int[] colors = reactContext.getResources().getIntArray(R.array.google_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PullToRefreshLayout.LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.dp2px(reactContext,15), 0,
                DisplayUtil.dp2px(reactContext,10));
        header.setPtrFrameLayout(rootView);
        rootView.setHeaderView(header);
        rootView.addPtrUIHandler(header);
        rootView.setPinContent(true);
        rootView.setLoadingMinTime(1000);
        rootView.setDurationToCloseHeader(1500);
        rootView.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PullToRefreshLayout view) {
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(view.getId(),
                        ON_PULL_TO_REFRESH_EVENT, null);
            }
        });
        registerProximitySensorListener();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        reactContext.registerReceiver(RCTMsgListReceiver, intentFilter);
        final float density = reactContext.getResources().getDisplayMetrics().density;

        mMessageList = (MessageList) rootView.findViewById(R.id.msg_list);
        mMessageList.setHasFixedSize(true);
        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        ImageLoader imageLoader = new ImageLoader() {

            final float MIN_WIDTH = 60 * density;
            final float MAX_WIDTH = 200 * density;
            final float MIN_HEIGHT = 60 * density;
            final float MAX_HEIGHT = 200 * density;

            @Override
            public void loadAvatarImage(ImageView avatarImageView, String string) {
                int resId = IdHelper.getDrawable(reactContext, string);
                if (resId != 0) {
                    avatarImageView.setImageResource(resId);
                } else {
                    Glide.with(reactContext)
                            .load(string)
                            .apply(new RequestOptions().placeholder(IdHelper.getDrawable(reactContext, "aurora_headicon_default")))
                            .into(avatarImageView);
                }
            }

            @Override
            public void loadImage(final ImageView imageView, String string) {
                // You can use other image load libraries.
                Glide.with(reactContext)
                        .asBitmap()
                        .load(string)
                        .apply(new RequestOptions().fitCenter().placeholder(R.drawable.aurora_picture_not_found))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                int imageWidth = resource.getWidth();
                                int imageHeight = resource.getHeight();

                                // 裁剪 bitmap
                                float width, height;
                                if (imageWidth > imageHeight) {
                                    if (imageWidth > MAX_WIDTH) {
                                        float temp = MAX_WIDTH / imageWidth * imageHeight;
                                        height = temp > MIN_HEIGHT ? temp : MIN_HEIGHT;
                                        width = MAX_WIDTH;
                                    } else if (imageWidth < MIN_WIDTH) {
                                        float temp = MIN_WIDTH / imageWidth * imageHeight;
                                        height = temp < MAX_HEIGHT ? temp : MAX_HEIGHT;
                                        width = MIN_WIDTH;
                                    } else {
                                        float ratio = imageWidth / imageHeight;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        height = imageHeight * ratio;
                                        width = imageWidth;
                                    }
                                } else {
                                    if (imageHeight > MAX_HEIGHT) {
                                        float temp = MAX_HEIGHT / imageHeight * imageWidth;
                                        width = temp > MIN_WIDTH ? temp : MIN_WIDTH;
                                        height = MAX_HEIGHT;
                                    } else if (imageHeight < MIN_HEIGHT) {
                                        float temp = MIN_HEIGHT / imageHeight * imageWidth;
                                        width = temp < MAX_WIDTH ? temp : MAX_WIDTH;
                                        height = MIN_HEIGHT;
                                    } else {
                                        float ratio = imageHeight / imageWidth;
                                        if (ratio > 3) {
                                            ratio = 3;
                                        }
                                        width = imageWidth * ratio;
                                        height = imageHeight;
                                    }
                                }
                                ViewGroup.LayoutParams params = imageView.getLayoutParams();
                                params.width = (int) width;
                                params.height = (int) height;
                                imageView.setLayoutParams(params);
                                Matrix matrix = new Matrix();
                                float scaleWidth = width / imageWidth;
                                float scaleHeight = height / imageHeight;
                                matrix.postScale(scaleWidth, scaleHeight);
                                imageView.setImageBitmap(Bitmap.createBitmap(resource, 0, 0, imageWidth, imageHeight, matrix, true));
                            }
                        });
            }

            @Override
            public void loadVideo(ImageView imageCover, String uri) {
                long interval = 5000 * 1000;
                Glide.with(reactContext)
                        .asBitmap()
                        .load(uri)
                        .apply(new RequestOptions().frame(interval).placeholder(IdHelper.getDrawable(reactContext, "aurora_picture_not_found")).override(200, 400))
                        .into(imageCover);
            }
        };
        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);
        CustomMsgConfig config1 = new CustomMsgConfig(13, R.layout.item_send_text, true, DefaultCustomViewHolder.class);
        CustomMsgConfig config2 = new CustomMsgConfig(14, R.layout.item_receive_txt, false, DefaultCustomViewHolder.class);
        mAdapter.addCustomMsgType(13, config1);
        mAdapter.addCustomMsgType(14, config2);
        mMessageList.setAdapter(mAdapter);
        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<RCTMessage>() {
            @Override
            public void onMessageClick(RCTMessage message) {
                WritableMap event = Arguments.createMap();
                event.putString("message", message.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(rootView.getId(), ON_MSG_CLICK_EVENT, event);
            }
        });

        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<RCTMessage>() {
            @Override
            public void onMessageLongClick(View view, RCTMessage message) {
                WritableMap event = Arguments.createMap();
                event.putString("message", message.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(rootView.getId(), ON_MSG_LONG_CLICK_EVENT, event);
            }
        });

        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<RCTMessage>() {
            @Override
            public void onAvatarClick(RCTMessage message) {
                WritableMap event = Arguments.createMap();
                event.putString("message", message.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(rootView.getId(), ON_AVATAR_CLICK_EVENT, event);
            }
        });

        mAdapter.setMsgStatusViewClickListener(new MsgListAdapter.OnMsgStatusViewClickListener<RCTMessage>() {
            @Override
            public void onStatusViewClick(RCTMessage message) {
                WritableMap event = Arguments.createMap();
                event.putString("message", message.toString());
                reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(rootView.getId(), ON_STATUS_VIEW_CLICK_EVENT, event);
            }
        });

        mMessageList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        EventBus.getDefault().post(new OnTouchMsgListEvent());
                        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(rootView.getId(), ON_TOUCH_MSG_LIST_EVENT, null);
                        if (reactContext.getCurrentActivity() != null) {
                            InputMethodManager imm = (InputMethodManager) reactContext.getCurrentActivity()
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            Window window = reactContext.getCurrentActivity().getWindow();
                            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        }
                        break;
                }
                return false;
            }
        });
        // 通知 AuroraIMUIModule 完成初始化 MessageList
        EventBus.getDefault().post(new LoadedEvent(AuroraIMUIModule.RCT_MESSAGE_LIST_LOADED_ACTION));
        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ScrollEvent event) {
        if (event.getFlag()) {
            Log.i(REACT_MESSAGE_LIST, "Scroll to bottom smoothly");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMessageList.smoothScrollToPosition(0);
                }
            }, 200);
        } else {
            Log.i(REACT_MESSAGE_LIST, "Scroll to bottom");
            mMessageList.scrollToPosition(0);
        }
    }

    @SuppressWarnings("unchecked")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        final Activity activity = mContext.getCurrentActivity();
        switch (event.getAction()) {
            case RCT_APPEND_MESSAGES_ACTION:
                RCTMessage[] messages = event.getMessages();
                for (final RCTMessage rctMessage : messages) {
                    Log.d("RCTMessageListManager", "Add message to start, message: " + rctMessage);
                    if (activity != null) {
                        mAdapter.addToStart(rctMessage, true);
                    }
                }
                break;
            case RCT_UPDATE_MESSAGE_ACTION:
                RCTMessage rctMessage = event.getMessage();
                Log.d("RCTMessageListManager", "updating message, message: " + rctMessage);
                if (activity != null) {
                    mAdapter.updateMessage(rctMessage.getMsgId(), rctMessage);
                }
                break;
            case RCT_INSERT_MESSAGES_ACTION:
                messages = event.getMessages();
                Log.d("RCTMessageListManager", "Add send message to top");
                mAdapter.addToEndChronologically(Arrays.asList(messages));
                break;
            case RCT_REMOVE_MESSAGE_ACTION:
                String msgId = event.getMsgId();
                mAdapter.deleteById(msgId);
                break;
            default:
                mAdapter.clear();
        }
        mMessageList.requestLayout();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StopPlayVoiceEvent event) {
        mAdapter.pauseVoice();
    }

    @ReactProp(name = "isAllowPullToRefresh")
    public void setAllowPullToRefresh(PullToRefreshLayout root, boolean isAllowPullToRefresh) {
        Log.i(REACT_MESSAGE_LIST, "Set isAllowPullToRefresh: " + isAllowPullToRefresh);
        root.setPullToRefresh(isAllowPullToRefresh);
    }

    @ReactProp(name = "sendBubble")
    public void setSendBubble(PullToRefreshLayout root, ReadableMap map) {
        int resId = mContext.getResources().getIdentifier(map.getString("imageName"),
                "drawable", mContext.getPackageName());
        if (resId != 0) {
            mMessageList.setSendBubbleDrawable(resId);
        }
    }

    @ReactProp(name = "receiveBubble")
    public void setReceiveBubble(PullToRefreshLayout root, ReadableMap map) {
        int resId = mContext.getResources().getIdentifier(map.getString("imageName"),
                "drawable", mContext.getPackageName());
        if (resId != 0) {
            mMessageList.setReceiveBubbleDrawable(resId);
        }
    }

    @ReactProp(name = "sendBubbleTextColor")
    public void setSendBubbleTextColor(PullToRefreshLayout root, String color) {
        int colorRes = Color.parseColor(color);
        mMessageList.setSendBubbleTextColor(colorRes);
    }

    @ReactProp(name = "receiveBubbleTextColor")
    public void setReceiveBubbleTextColor(PullToRefreshLayout root, String color) {
        int colorRes = Color.parseColor(color);
        mMessageList.setReceiveBubbleTextColor(colorRes);
    }

    @ReactProp(name = "sendBubbleTextSize")
    public void setSendBubbleTextSize(PullToRefreshLayout root, int size) {
        mMessageList.setSendBubbleTextSize(dip2sp(size));
    }

    @ReactProp(name = "receiveBubbleTextSize")
    public void setReceiveBubbleTextSize(PullToRefreshLayout root, int size) {
        mMessageList.setReceiveBubbleTextSize(dip2sp(size));
    }

    @ReactProp(name = "sendBubblePadding")
    public void setSendBubblePadding(PullToRefreshLayout root, ReadableMap map) {
        int left = map.getInt("left");
        int top = map.getInt("top");
        int right = map.getInt("right");
        int bottom = map.getInt("bottom");
        mMessageList.setSendBubblePadding(dip2px(left), dip2px(top), dip2px(right), dip2px(bottom));
    }

    private int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private float dip2sp(int dip) {
        int px = dip2px(dip);
        float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return px / scale;
    }

    @ReactProp(name = "receiveBubblePadding")
    public void setReceiveBubblePadding(PullToRefreshLayout root, ReadableMap map) {
        int left = map.getInt("left");
        int top = map.getInt("top");
        int right = map.getInt("right");
        int bottom = map.getInt("bottom");
        mMessageList.setReceiveBubblePadding(dip2px(left), dip2px(top), dip2px(right), dip2px(bottom));
    }

    @ReactProp(name = "dateTextSize")
    public void setDateTextSize(PullToRefreshLayout root, int size) {
        mMessageList.setDateTextSize(dip2sp(size));
    }

    @ReactProp(name = "dateTextColor")
    public void setDateTextColor(PullToRefreshLayout root, String color) {
        int colorRes = Color.parseColor(color);
        mMessageList.setDateTextColor(colorRes);
    }

    @ReactProp(name = "datePadding")
    public void setDatePadding(PullToRefreshLayout root, ReadableMap map) {
        int left = map.getInt("left");
        int top = map.getInt("top");
        int right = map.getInt("right");
        int bottom = map.getInt("bottom");
        mMessageList.setDatePadding(dip2px(left), dip2px(top), dip2px(right), dip2px(bottom));
    }

    @ReactProp(name = "dateBackgroundColor")
    public void setDateBgColor(PullToRefreshLayout root, String color) {
        mMessageList.setDateBgColor(Color.parseColor(color));
    }

    @ReactProp(name = "dateCornerRadius")
    public void setDateBgCornerRadius(PullToRefreshLayout root, int radius) {
        mMessageList.setDateBgCornerRadius(dip2px(radius));
    }

    @ReactProp(name = "avatarSize")
    public void setAvatarWidth(PullToRefreshLayout root, ReadableMap map) {
        mMessageList.setAvatarWidth(dip2px(map.getInt("width")));
        mMessageList.setAvatarHeight(dip2px(map.getInt("height")));
    }

    @ReactProp(name = "avatarCornerRadius")
    public void setAvatarCornerRadius(PullToRefreshLayout root, int radius) {
        mMessageList.setAvatarRadius(dip2px(radius));
    }

    /**
     * if showDisplayName equals 1, then show display name.
     *
     * @param root       PullToRefreshLayout
     * @param isShowDisplayName boolean
     */
    @ReactProp(name = "isShowDisplayName")
    public void setShowDisplayName(PullToRefreshLayout root, boolean isShowDisplayName) {
        mMessageList.setShowReceiverDisplayName(isShowDisplayName);
        mMessageList.setShowSenderDisplayName(isShowDisplayName);
    }

    @ReactProp(name = "isShowIncomingDisplayName")
    public void setShowReceiverDisplayName(PullToRefreshLayout root, boolean isShowDisplayName) {
        mMessageList.setShowReceiverDisplayName(isShowDisplayName);
    }

    @ReactProp(name = "isShowOutgoingDisplayName")
    public void setShowSenderDisplayName(PullToRefreshLayout root, boolean isShowDisplayName) {
        mMessageList.setShowSenderDisplayName(isShowDisplayName);
    }

    @ReactProp(name = "displayNameTextSize")
    public void setDisplayNameTextSize(PullToRefreshLayout root, int size) {
        mMessageList.setDisplayNameTextSize(dip2sp(size));
    }

    @ReactProp(name = "displayNameTextColor")
    public void setDisplayNameTextColor(PullToRefreshLayout root, String color) {
        mMessageList.setDisplayNameTextColor(Color.parseColor(color));
    }

    @ReactProp(name = "displayNamePadding")
    public void setDisplayNamePadding(PullToRefreshLayout root, ReadableMap map) {
        int left = map.getInt("left");
        int top = map.getInt("top");
        int right = map.getInt("right");
        int bottom = map.getInt("bottom");
        mMessageList.setDisplayNamePadding(dip2px(left), dip2px(top), dip2px(right), dip2px(bottom));
    }

    @ReactProp(name = "displayNameEmsNumber")
    public void setdisplayNameEmsNumber(PullToRefreshLayout root, int number) {
        mMessageList.setDisplayNameEmsNumber(number);
    }

    @ReactProp(name = "eventTextColor")
    public void setEventTextColor(PullToRefreshLayout root, String color) {
        int colorRes = Color.parseColor(color);
        mMessageList.setEventTextColor(colorRes);
    }

    @ReactProp(name = "eventTextPadding")
    public void setEventTextPadding(PullToRefreshLayout root, ReadableMap map) {
        int left = map.getInt("left");
        int top = map.getInt("top");
        int right = map.getInt("right");
        int bottom = map.getInt("bottom");
        mMessageList.setEventPadding(dip2px(left), dip2px(top), dip2px(right), dip2px(bottom));
    }

    @ReactProp(name = "eventBackgroundColor")
    public void setEventBgColor(PullToRefreshLayout root, String color) {
        mMessageList.setEventBgColor(Color.parseColor(color));
    }

    @ReactProp(name = "eventCornerRadius")
    public void setEventBgCornerRadius(PullToRefreshLayout root, int radius) {
        mMessageList.setEventBgCornerRadius(dip2px(radius));
    }

    @ReactProp(name = "eventTextSize")
    public void setEventTextSize(PullToRefreshLayout root, int size) {
        mMessageList.setEventTextSize(dip2sp(size));
    }

    @ReactProp(name = "eventTextLineHeight")
    public void setEventTextLineSpacing(PullToRefreshLayout root, int spacing) {
        mMessageList.setEventLineSpacingExtra(dip2px(spacing));
    }

    @ReactProp(name = "maxBubbleWidth")
    public void setBubbleMaxWidth(PullToRefreshLayout root, float maxSize) {
        mMessageList.setBubbleMaxWidth(maxSize);
    }

    @ReactProp(name = "messageListBackgroundColor")
    public void setBackgroundColor(PullToRefreshLayout layout, String color) {
        int colorRes = Color.parseColor(color);
        layout.setBackgroundColor(colorRes);
    }

    @ReactProp(name = "messageTextLineHeight")
    public void setMessageTextLineSpacing(PullToRefreshLayout root, int spacing) {
        mMessageList.setLineSpacingExtra(dip2px(spacing));
    }

    @ReactProp(name = "videoMessageRadius")
    public void setVideoMessageRadius(PullToRefreshLayout root, int radius) {
        mMessageList.setVideoMessageRadius(dip2px(radius));
    }

    @ReactProp(name = "videoDurationTextColor")
    public void setVideoDurationTextColor(PullToRefreshLayout root, String color) {
        mMessageList.setVideoDurationTextColor(Color.parseColor(color));
    }

    @ReactProp(name = "videoDurationTextSize")
    public void setVideoDurationTextSize(PullToRefreshLayout root, int size) {
        mMessageList.setVideoDurationTextSize(dip2sp(size));
    }

    @ReactProp(name = "photoMessageRadius")
    public void setPhotoMessageRadius(PullToRefreshLayout root, int radius) {
        mMessageList.setPhotoMessageRadius(dip2px(radius));
    }

    @SuppressWarnings("unchecked")
    private BroadcastReceiver RCTMsgListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent) {
                return;
            }
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                mAdapter.setAudioPlayByEarPhone(intent.getIntExtra("state", 0));
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
    public @Nullable Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("unregister", 1);
    }

    @Override
    public void receiveCommand(PullToRefreshLayout root, int commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);
        switch (commandId){
            case STOP_REFRESH:
                Log.i(REACT_MESSAGE_LIST, "Refresh has completed");
                root.refreshComplete();
        }
    }

    @Override
    public void onDropViewInstance(PullToRefreshLayout view) {
        super.onDropViewInstance(view);
        try {
            EventBus.getDefault().unregister(this);
            mContext.unregisterReceiver(RCTMsgListReceiver);
            mSensorManager.unregisterListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
    }

    private void registerProximitySensorListener() {
        try {
            Activity activity = mContext.getCurrentActivity();
            mPowerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, REACT_MESSAGE_LIST);
            mSensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        AudioManager audioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
        try {
            if (audioManager.isBluetoothA2dpOn() || audioManager.isWiredHeadsetOn()) {
                return;
            }
            if (mAdapter.getMediaPlayer().isPlaying()) {
                float distance = event.values[0];
                if (distance >= mSensor.getMaximumRange()) {
                    mAdapter.setAudioPlayByEarPhone(0);
                    setScreenOn();
                } else {
                    mAdapter.setAudioPlayByEarPhone(2);
                    ViewHolderController.getInstance().replayVoice();
                    setScreenOff();
                }
            } else {
                if (mWakeLock != null && mWakeLock.isHeld()) {
                    mWakeLock.release();
                    mWakeLock = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setScreenOn() {
        if (mWakeLock != null) {
            mWakeLock.setReferenceCounted(false);
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    private void setScreenOff() {
        if (mWakeLock == null) {
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, REACT_MESSAGE_LIST);
        }
        mWakeLock.acquire();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static class DefaultCustomViewHolder extends CustomViewHolder<IMessage> {

        public DefaultCustomViewHolder(View itemView, boolean isSender) {
            super(itemView, isSender);
        }
    }

    @Override
    public void addView(PullToRefreshLayout parent, View child, int index) {
        super.addView(parent, child, index);
        parent.updateLayout();
    }
}