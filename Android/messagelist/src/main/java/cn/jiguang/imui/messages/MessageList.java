package cn.jiguang.imui.messages;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.AttributeSet;

import cn.jiguang.imui.commons.models.IMessage;


public class MessageList extends RecyclerView {

    private Context mContext;

    private MessageListStyle mMsgListStyle;

    public MessageList(Context context) {
        super(context);
    }

    public MessageList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseStyle(context, attrs);
    }

    public MessageList(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        parseStyle(context, attrs);
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
        SimpleItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        setItemAnimator(itemAnimator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        setLayoutManager(layoutManager);
        
        adapter.setLayoutManager(layoutManager);
        adapter.setStyle(mContext, mMsgListStyle);
        addOnScrollListener(new ScrollMoreListener(layoutManager, adapter));
        super.setAdapter(adapter);
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

    public void setSendBubbleTextSize(int size) {
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

    public void setReceiveBubbleTextSize(int size) {
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

    public void setDateTextSize(int size) {
        mMsgListStyle.setDateTextSize(size);
    }

    public void setDateTextColor(int color) {
        mMsgListStyle.setDateTextColor(color);
    }

    public void setDatePadding(int padding) {
        mMsgListStyle.setDatePadding(padding);
    }

    public void setAvatarWidth(int width) {
        mMsgListStyle.setAvatarWidth(width);
    }

    public void setAvatarHeight(int height) {
        mMsgListStyle.setAvatarHeight(height);
    }

    public void setShowDisplayName(int showDisplayName) {
        mMsgListStyle.setShowDisplayName(showDisplayName);
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
}
