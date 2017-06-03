package cn.jiguang.imui.messagelist;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;

/**
 * Created by caiyaoguan on 2017/5/24.
 */

public class RCTMessage implements IMessage {

    public static final String MSG_ID = "msgId";
    public static final String STATUS = "status";
    public static final String MSG_TYPE = "msgType";
    public static final String IS_OUTGOING = "isOutgoing";
    private final String TIME_STRING = "timeString";
    private final String TEXT = "text";
    private final String MEDIA_FILE_PATH = "mediaFilePath";
    private final String DURATION = "duration";
    private final String PROGRESS = "progress";
    private final String FROM_USER = "fromUser";

    private String msgId;
    private String status;
    private String msgType;
    private String timeString;
    private String text;
    private String mediaFilePath;
    private long duration = -1;
    private String progress;
    private RCTUser rctUser;
    private boolean isOutgoing;
    private static Gson sGSON = new Gson();

    public RCTMessage(String msgId, String status, String msgType, boolean isOutgoing) {
        this.msgId = msgId;
        this.status = status;
        this.msgType = msgType;
        this.isOutgoing = isOutgoing;
    }

    @Override
    public String getMsgId() {
        return this.msgId;
    }

    public void setFromUser(RCTUser user) {
        this.rctUser = user;
    }

    @Override
    public IUser getFromUser() {
        return rctUser;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    @Override
    public String getTimeString() {
        return this.timeString;
    }

    @Override
    public MessageType getType() {
        if (isOutgoing) {
            switch (msgType) {
                case "text":
                    return MessageType.SEND_TEXT;
                case "voice":
                    return MessageType.SEND_VOICE;
                case "image":
                    return MessageType.SEND_IMAGE;
                default:
                    return MessageType.SEND_VIDEO;
            }
        } else {
            switch (msgType) {
                case "text":
                    return MessageType.RECEIVE_TEXT;
                case "voice":
                    return MessageType.RECEIVE_VOICE;
                case "image":
                    return MessageType.RECEIVE_IMAGE;
                default:
                    return MessageType.RECEIVE_VIDEO;
            }
        }
    }


    @Override
    public MessageStatus getMessageStatus() {
        switch (this.status) {
            case "send_failed":
                return MessageStatus.SEND_FAILED;
            case "send_going":
                return MessageStatus.SEND_GOING;
            case "download_failed":
                return MessageStatus.RECEIVE_FAILED;
            default:
                return MessageStatus.SEND_SUCCEED;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }

    public void setMediaFilePath(String path) {
        this.mediaFilePath = path;
    }

    @Override
    public String getMediaFilePath() {
        return this.mediaFilePath;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    @Override
    public String getProgress() {
        return this.progress;
    }

    public JsonElement toJSON() {
        JsonObject json = new JsonObject();
        if (msgId != null) {
            json.addProperty(MSG_ID, msgId);
        }
        if (status != null) {
            json.addProperty(STATUS, status);
        }
        if (msgType != null) {
            json.addProperty(MSG_TYPE, msgType);
        }
        json.addProperty(IS_OUTGOING, isOutgoing);
        if (timeString != null) {
            json.addProperty(TIME_STRING, timeString);
        }
        if (text != null) {
            json.addProperty(TEXT, text);
        }
        if (mediaFilePath != null) {
            json.addProperty(MEDIA_FILE_PATH, mediaFilePath);
        }
        if (duration != -1) {
            json.addProperty(DURATION, duration);
        }
        if (progress != null) {
            json.addProperty(PROGRESS, progress);
        }
        json.add(FROM_USER, rctUser.toJSON());

        return json;
    }

    @Override
    public String toString() {
        return sGSON.toJson(toJSON());
    }
}
