package cn.jiguang.imui.messages;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import cn.jiguang.imui.commons.models.IMessage;

public class MessageList extends RecyclerView implements GestureDetector.OnGestureListener {

    private Context mContext;

    private MessageListStyle mMsgListStyle;
    private final GestureDetector mGestureDetector;
    private MsgListAdapter mAdapter;
    private ScrollMoreListener mScrollMoreListener;

    public MessageList(Context context) {
        this(context, null);
    }

    public MessageList(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseStyle(context, attrs);
        mGestureDetector = new GestureDetector(context, this);
    }

    @SuppressWarnings("ResourceType")
    private void parseStyle(Context context, AttributeSet attrs) {
        mContext = context;
        mMsgListStyle = MessageListStyle.parse(context, attrs);
    }

    /**
     * Set adapter for MessageList.
     *
     * @param adapter Adapter, extends MsgListAdapter.
     * @param         <MESSAGE> Message model extends IMessage.
     */
    public <MESSAGE extends IMessage> void setAdapter(MsgListAdapter<MESSAGE> adapter) {
        mAdapter = adapter;
        SimpleItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        setItemAnimator(itemAnimator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        setLayoutManager(layoutManager);

        adapter.setLayoutManager(layoutManager);
        adapter.setStyle(mContext, mMsgListStyle);
        mScrollMoreListener = new ScrollMoreListener(layoutManager, adapter);
        addOnScrollListener(mScrollMoreListener);
        super.setAdapter(adapter);
    }

    public void forbidScrollToRefresh(boolean disable) {
        mScrollMoreListener.forbidScrollToRefresh(disable);
    }

    public void setSendBubbleDrawable(int resId) {
        mMsgListStyle.setSendBubbleDrawable(resId);
    }

    public void setSendBubbleColor(int color) {
        mMsgListStyle.setSendBubbleColor(color);
    }

    public void setSendBubblePressedColor(int color) {
        mMsgListStyle.setSendBubblePressedColor(color);
    }

    public void setSendBubbleTextSize(float size) {
        mMsgListStyle.setSendBubbleTextSize(size);
    }

    public void setSendBubbleTextColor(int color) {
        mMsgListStyle.setSendBubbleTextColor(color);
    }

    public void setSendBubblePadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        mMsgListStyle.setSendBubblePadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public void setReceiveBubbleDrawable(int resId) {
        mMsgListStyle.setReceiveBubbleDrawable(resId);
    }

    public void setReceiveBubbleColor(int color) {
        mMsgListStyle.setReceiveBubbleColor(color);
    }

    public void setReceiveBubblePressedColor(int color) {
        mMsgListStyle.setReceiveBubblePressedColor(color);
    }

    public void setReceiveBubbleTextSize(float size) {
        mMsgListStyle.setReceiveBubbleTextSize(size);
    }

    public void setReceiveBubbleTextColor(int color) {
        mMsgListStyle.setReceiveBubbleTextColor(color);
    }

    public void setReceiveBubblePadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        mMsgListStyle.setReceiveBubblePadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public void setDateTextSize(float size) {
        mMsgListStyle.setDateTextSize(size);
    }

    public void setDateTextColor(int color) {
        mMsgListStyle.setDateTextColor(color);
    }

    public void setDatePadding(int left, int top, int right, int bottom) {
        mMsgListStyle.setDatePadding(left, top, right, bottom);
    }

    public void setDateBgCornerRadius(int radius) {
        mMsgListStyle.setDateBgCornerRadius(radius);
    }

    public void setDateBgColor(int color) {
        mMsgListStyle.setDateBgColor(color);
    }

    public void setEventTextColor(int color) {
        mMsgListStyle.setEventTextColor(color);
    }

    public void setEventTextSize(float size) {
        mMsgListStyle.setEventTextSize(size);
    }

    public void setAvatarWidth(int width) {
        mMsgListStyle.setAvatarWidth(width);
    }

    public void setAvatarHeight(int height) {
        mMsgListStyle.setAvatarHeight(height);
    }

    public void setAvatarRadius(int radius) {
        mMsgListStyle.setAvatarRadius(radius);
    }

    public void setShowSenderDisplayName(boolean showDisplayName) {
        mMsgListStyle.setShowSenderDisplayName(showDisplayName);
    }

    public void setShowReceiverDisplayName(boolean showDisplayName) {
        mMsgListStyle.setShowReceiverDisplayName(showDisplayName);
    }

    public void setDisplayNameTextSize(float size) {
        mMsgListStyle.setDisplayNameTextSize(size);
    }

    public void setDisplayNameTextColor(int color) {
        mMsgListStyle.setDisplayNameTextColor(color);
    }

    public void setDisplayNamePadding(int left, int top, int right, int bottom) {
        mMsgListStyle.setDisplayNamePadding(left, top, right, bottom);
    }

    public void setDisplayNameEmsNumber(int number) {
        mMsgListStyle.setDisplayNameEmsNumber(number);
    }

    public void setSendVoiceDrawable(int resId) {
        mMsgListStyle.setSendVoiceDrawable(resId);
    }

    public void setReceiveVoiceDrawable(int resId) {
        mMsgListStyle.setReceiveVoiceDrawable(resId);
    }

    public void setPlaySendVoiceAnim(int playSendVoiceAnim) {
        mMsgListStyle.setPlaySendVoiceAnim(playSendVoiceAnim);
    }

    public void setPlayReceiveVoiceAnim(int playReceiveVoiceAnim) {
        mMsgListStyle.setPlayReceiveVoiceAnim(playReceiveVoiceAnim);
    }

    public void setBubbleMaxWidth(float maxWidth) {
        mMsgListStyle.setBubbleMaxWidth(maxWidth);
    }

    public void setSendingProgressDrawable(String drawableName, String packageName) {
        int resId = getResources().getIdentifier(drawableName, "drawable", packageName);
        mMsgListStyle.setSendingProgressDrawable(getResources().getDrawable(resId));
    }

    public void setSendingIndeterminateDrawable(String drawableName, String packageName) {
        int resId = getResources().getIdentifier(drawableName, "drawable", packageName);
        mMsgListStyle.setSendingIndeterminateDrawable(getResources().getDrawable(resId));
    }

    public void setLineSpacingExtra(int spacing) {
        mMsgListStyle.setLineSpacingExtra(spacing);
    }

    public void setLineSpacingMultiplier(float mult) {
        mMsgListStyle.setLineSpacingMultiplier(mult);
    }

    public void setEventBgColor(int color) {
        mMsgListStyle.setEventBgColor(color);
    }

    public void setEventPadding(int left, int top, int right, int bottom) {
        mMsgListStyle.setEventTextPadding(left, top, right, bottom);
    }

    public void setEventLineSpacingExtra(int spacingExtra) {
        mMsgListStyle.setEventLineSpacingExtra(spacingExtra);
    }

    public void setEventBgCornerRadius(int radius) {
        mMsgListStyle.setEventBgCornerRadius(radius);
    }

    public void setVideoDurationTextColor(int color) {
        mMsgListStyle.setVideoDurationTextColor(color);
    }

    public void setVideoDurationTextSize(float size) {
        mMsgListStyle.setVideoDurationTextSize(size);
    }

    public void setVideoMessageRadius(int radius) {
        mMsgListStyle.setVideoMessageRadius(radius);
    }

    public void setPhotoMessageRadius(int radius) {
        mMsgListStyle.setPhotoMessageRadius(radius);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityY) > 4000) {
            mAdapter.setScrolling(true);
        }
        return false;
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
        post(measureAndLayout);
    }

    private final Runnable measureAndLayout = new Runnable() {
        @Override
        public void run() {
            measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.EXACTLY));
            layout(getLeft(), getTop(), getRight(), getBottom());
        }
    };
}
