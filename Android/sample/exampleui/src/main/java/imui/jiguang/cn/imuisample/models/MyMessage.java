package imui.jiguang.cn.imuisample.models;

import android.graphics.Rect;

import java.util.Date;
import java.util.UUID;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;


public class MyMessage implements IMessage {

    private long id;
    private String text;
    private Date createAt;
    private MessageType type;
    private IUser user;
    private String contentFile;
    private long duration;

    private final Rect mCurrentViewRect = new Rect();

    public MyMessage(String text, MessageType type) {
        this.text = text;
        this.type = type;
        this.id = UUID.randomUUID().getLeastSignificantBits();
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public IUser getUserInfo() {
        if (user == null) {
            return new DefaultUser("0", "user1", null);
        }
        return user;
    }

    public void setUserInfo(IUser user) {
        this.user = user;
    }

    public void setContentFile(String path) {
        this.contentFile = path;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public Date getCreatedAt() {
        return createAt == null ? new Date() : createAt;
    }

    @Override
    public MessageType getType() {
        return type;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getContentFilePath() {
        return contentFile;
    }
}