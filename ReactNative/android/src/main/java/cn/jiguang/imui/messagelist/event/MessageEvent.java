package cn.jiguang.imui.messagelist.event;

import cn.jiguang.imui.messagelist.RCTMessage;

public class MessageEvent {

    private RCTMessage message;
    private String action;
    private RCTMessage[] messages;

    public MessageEvent(RCTMessage message, String action) {
        this.message = message;
        this.action = action;
    }

    public MessageEvent(RCTMessage[] messages, String action) {
        this.messages = messages;
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
}
