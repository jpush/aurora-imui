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

        private int type;

        MessageType() {
        }

        public void setCustomType(int type) {
            if (type > 12 || type < 0) {
                this.type = type;
            } else {
                throw new IllegalArgumentException("Custom message type should not set 0-12");
            }
        }

        public int getCustomType() {
            return this.type;
        }
    }

    /**
     * Type of message, enum.
     * @return Message Type
     */
    MessageType getType();

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
     * @return duration of audio or video
     */
    long getDuration();

    String getProgress();

    /**
     * Return extra key value of message
     * @return {@link HashMap<>}
     */
    HashMap<String, String> getExtras();
}
