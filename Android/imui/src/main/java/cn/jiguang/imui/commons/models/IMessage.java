package cn.jiguang.imui.commons.models;


import java.util.Date;


public interface IMessage {

    String getId();

    IUser getUserInfo();

    Date getCreatedAt();

    enum MessageType {
        SEND_TEXT,
        RECEIVE_TEXT,

        SEND_IMAGE,
        RECEIVE_IMAGE,

        SEND_VOICE,
        RECEIVE_VOICE,

        SEND_VIDEO,
        RECEIVE_VIDEO,

        SEND_LOCATION,
        RECEIVE_LOCATION,

        SEND_FILE,
        RECEIVE_FILE;

        public String type;

        MessageType() {
        }
    }

    MessageType getType();

    String getText();

    String getContentFilePath();

    long getDuration();
}
