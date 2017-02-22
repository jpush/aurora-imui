package cn.jiguang.imui.commons.models;


import java.util.Date;


public interface IMessage {

    String getId();

    UserInfo getUserInfo();

    Date getCreatedAt();

    enum MessageType {
        TEXT,
        IMAGE,
        VOICE,
        VIDEO,
        LOCATION,
        FILE;

        public String type;

        private MessageType() {}
    }

    MessageType getType();

    String getText();
}
