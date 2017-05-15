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
        setLayoutManager(layoutManager);

        adapter.setLayoutManager(layoutManager);
        adapter.setStyle(mContext, mMsgListStyle);
        addOnScrollListener(new ScrollMoreListener(layoutManager, adapter));
        super.setAdapter(adapter);
    }
}
