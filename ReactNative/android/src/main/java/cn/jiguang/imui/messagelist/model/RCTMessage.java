package cn.jiguang.imui.messagelist.model;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;

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
    private final String MEDIA_FILE_PATH = "mediaPath";
    private final String DURATION = "duration";
    private final String PROGRESS = "progress";
    private final String FROM_USER = "fromUser";
    private final String EXTRAS = "extras";
    private final String CONTENT_SIZE = "contentSize";

    private String msgId;
    private String status;
    private String msgType;
    private int type;
    private String timeString;
    private String text;
    private String mediaFilePath;
    private long duration = -1;
    private String progress;
    private RCTUser rctUser;
    private boolean isOutgoing;
    private int width;
    private int height;
    private HashMap<String, String> mExtra;
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
    public int getType() {
        if (isOutgoing) {
            switch (msgType) {
                case "text":
                    this.type = MessageType.SEND_TEXT.ordinal();
                    break;
                case "voice":
                    this.type = MessageType.SEND_VOICE.ordinal();
                    break;
                case "image":
                    this.type = MessageType.SEND_IMAGE.ordinal();
                    break;
                case "event":
                    this.type = MessageType.EVENT.ordinal();
                    break;
                case "video":
                    this.type = MessageType.SEND_VIDEO.ordinal();
                    break;
                default:
                    setType(13);
                    this.type = MessageType.SEND_CUSTOM.ordinal();
            }
        } else {
            switch (msgType) {
                case "text":
                    this.type = MessageType.RECEIVE_TEXT.ordinal();
                    break;
                case "voice":
                    this.type = MessageType.RECEIVE_VOICE.ordinal();
                    break;
                case "image":
                    this.type = MessageType.RECEIVE_IMAGE.ordinal();
                    break;
                case "event":
                    this.type = MessageType.EVENT.ordinal();
                    break;
                case "video":
                    this.type = MessageType.RECEIVE_VIDEO.ordinal();
                    break;
                default:
                    setType(14);
                    this.type = MessageType.RECEIVE_CUSTOM.ordinal();
            }
        }
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public void putExtra(String key, String value) {
        if (null == mExtra) {
            mExtra = new HashMap<>();
        }
        mExtra.put(key, value);
    }

    public void setContentSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public HashMap<String, String> getExtras() {
        return this.mExtra;
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

        if (mExtra != null) {
            JsonObject jsonObject = new JsonObject();
            for (String key : mExtra.keySet()) {
                jsonObject.addProperty(key, mExtra.get(key));
            }
            json.add(EXTRAS, jsonObject);
        }

        if (width != 0) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("width", width);
            jsonObject.addProperty("height", height);
            json.add(CONTENT_SIZE, jsonObject);
        }

        return json;
    }

    @Override
    public String toString() {
        return sGSON.toJson(toJSON());
    }
}
