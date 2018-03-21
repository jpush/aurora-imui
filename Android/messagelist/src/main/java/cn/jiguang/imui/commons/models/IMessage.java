package cn.jiguang.imui.commons.models;


import java.util.HashMap;

public interface IMessage {

    /**
     * Message id.
     * @return unique
     */
    String getMsgId();

    /**
     * Get user info of message.
     * @return UserInfo of message
     */
    IUser getFromUser();

    /**
     * Time string that display in message list.
     * @return Time string
     */
    String getTimeString();

    /**
     * Type of Message
     */
    enum MessageType {
        EVENT,
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
        RECEIVE_FILE,

        SEND_CUSTOM,
        RECEIVE_CUSTOM;

        MessageType() {
        }
    }

    /**
     * Type of message
     * @return integer
     */
    int getType();

    /**
     * Status of message, enum.
     */
    enum MessageStatus {
        CREATED,
        SEND_GOING,
        SEND_SUCCEED,
        SEND_FAILED,
        SEND_DRAFT,
        RECEIVE_GOING,
        RECEIVE_SUCCEED,
        RECEIVE_FAILED;
    }

    MessageStatus getMessageStatus();


    /**
     * Text of message.
     * @return text
     */
    String getText();

    /**
     * If message type is photo, voice, video or file,
     * get file path through this method.
     * @return file path
     */
    String getMediaFilePath();

    /**
     * If message type is voice or video, get duration through this method.
     * @return duration of audio or video, TimeUnit: SECONDS.
     */
    long getDuration();

    String getProgress();

    /**
     * Return extra key value of message
     * @return {@link HashMap<>}
     */
    HashMap<String, String> getExtras();
}
