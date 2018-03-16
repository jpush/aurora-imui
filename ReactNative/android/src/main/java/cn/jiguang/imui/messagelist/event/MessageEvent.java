package cn.jiguang.imui.messagelist.event;

import javax.annotation.Nullable;

import cn.jiguang.imui.messagelist.model.RCTMessage;

public class MessageEvent {

    private RCTMessage message;
    private String action;
    private RCTMessage[] messages;
    private String msgId;

    public MessageEvent(@Nullable RCTMessage message, String action) {
        this.message = message;
        this.action = action;
    }

    public MessageEvent(RCTMessage[] messages, String action) {
        this.messages = messages;
        this.action = action;
    }

    public MessageEvent(String id, String action) {
        this.msgId = id;
        this.action = action;
    }

    public RCTMessage getMessage() {
        return this.message;
    }

    public RCTMessage[] getMessages() {
        return this.messages;
    }

    public String getAction() {
        return this.action;
    }

    public void setMsgId(String id) {
        this.msgId = id;
    }

    public String getMsgId() {
        return this.msgId;
    }
}
