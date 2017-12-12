package cn.jiguang.imui.messagelist;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import java.util.Map;

import cn.jiguang.imui.messages.MessageList;
import cn.jiguang.imui.messages.ptr.PtrDefaultHeader;
import cn.jiguang.imui.messages.ptr.PtrHandler;
import cn.jiguang.imui.messages.ptr.PullToRefreshLayout;
import cn.jiguang.imui.utils.DisplayUtil;

/**
 * Created by caiyaoguan on 2017/11/3.
 */

public class ReactPtrLayout extends ViewGroupManager<PullToRefreshLayout> {

    private final static String RCTPTRLAYOUT = "PtrLayout";
    private final static int STOP_REFRESH = 0;
    private static final String ON_PULL_TO_REFRESH_EVENT = "onPullToRefresh";

    @Override
    public String getName() {
        return RCTPTRLAYOUT;
    }

    @Override
    protected PullToRefreshLayout createViewInstance(final ThemedReactContext reactContext) {
        PullToRefreshLayout rootView = (PullToRefreshLayout) LayoutInflater.from(reactContext).inflate(R.layout.ptr_layout, null);
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
        return rootView;
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("stop_refresh",STOP_REFRESH);
    }

    @Override
    public void receiveCommand(PullToRefreshLayout root, int commandId, @Nullable ReadableArray args) {
        switch (commandId){
            case STOP_REFRESH:
                Log.i(RCTPTRLAYOUT, "Refresh has completed");
                root.refreshComplete();
        }
    }

    @ReactProp(name = "messageListBackgroundColor")
    public void setBackgroundColor(PullToRefreshLayout layout, String color) {
        int colorRes = Color.parseColor(color);
        layout.setBackgroundColor(colorRes);
    }

    @Override
    protected void addEventEmitters(final ThemedReactContext reactContext, final PullToRefreshLayout view) {
        super.addEventEmitters(reactContext, view);
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put(ON_PULL_TO_REFRESH_EVENT, MapBuilder.of("registrationName", ON_PULL_TO_REFRESH_EVENT))
                .build();
    }


    @Override
    public void addView(PullToRefreshLayout parent, View child, int index) {
        super.addView(parent, child, index);
        parent.updateLayout();
    }
}
