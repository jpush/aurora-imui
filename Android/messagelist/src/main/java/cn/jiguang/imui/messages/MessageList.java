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
     * @param adapter   Adapter, extends MsgListAdapter.
     * @param <MESSAGE> Message model extends IMessage.
     */
    public <MESSAGE extends IMessage> void setAdapter(MsgListAdapter<MESSAGE> adapter) {
        mAdapter = adapter;
        SimpleItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        setItemAnimator(itemAnimator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, true);
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

    public void setSendBubblePaddingLeft(int paddingLeft) {
        mMsgListStyle.setSendBubblePaddingLeft(paddingLeft);
    }

    public void setSendBubblePaddingTop(int paddingTop) {
        mMsgListStyle.setSendBubblePaddingTop(paddingTop);
    }

    public void setSendBubblePaddingRight(int paddingRight) {
        mMsgListStyle.setSendBubblePaddingRight(paddingRight);
    }

    public void setSendBubblePaddingBottom(int paddingBottom) {
        mMsgListStyle.setSendBubblePaddingBottom(paddingBottom);
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

    public void setReceiveBubblePaddingLeft(int paddingLeft) {
        mMsgListStyle.setReceiveBubblePaddingLeft(paddingLeft);
    }

    public void setReceiveBubblePaddingTop(int paddingTop) {
        mMsgListStyle.setReceiveBubblePaddingTop(paddingTop);
    }

    public void setReceiveBubblePaddingRight(int paddingRight) {
        mMsgListStyle.setReceiveBubblePaddingRight(paddingRight);
    }

    public void setReceiveBubblePaddingBottom(int paddingBottom) {
        mMsgListStyle.setReceiveBubblePaddingBottom(paddingBottom);
    }

    public void setDateTextSize(float size) {
        mMsgListStyle.setDateTextSize(size);
    }

    public void setDateTextColor(int color) {
        mMsgListStyle.setDateTextColor(color);
    }

    public void setDatePadding(int padding) {
        mMsgListStyle.setDatePadding(padding);
    }

    public void setEventTextColor(int color) {
        mMsgListStyle.setEventTextColor(color);
    }

    public void setEventTextSize(float size) {
        mMsgListStyle.setEventTextSize(size);
    }

    public void setEventTextPadding(int padding) {
        mMsgListStyle.setEventTextPadding(padding);
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

    public void setShowSenderDisplayName(int showDisplayName) {
        mMsgListStyle.setShowSenderDisplayName(showDisplayName);
    }

    public void setShowReceiverDisplayName(int showDisplayName) {
        mMsgListStyle.setShowReceiverDisplayName(showDisplayName);
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
}
