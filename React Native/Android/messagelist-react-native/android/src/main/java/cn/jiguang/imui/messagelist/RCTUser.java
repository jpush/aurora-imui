package cn.jiguang.imui.messagelist;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cn.jiguang.imui.commons.models.IUser;

/**
 * Created by caiyaoguan on 2017/5/25.
 */

public class RCTUser implements IUser {

    private final String USER_ID = "userId";
    private final String DISPLAY_NAME = "displayName";
    private final String AVATAR_PATH = "avatarPath";

    private String userId;
    private String displayName;
    private String avatarPath;
    private static Gson sGSON = new Gson();

    public RCTUser(String userId, String displayName, String avatarPath) {
        this.userId = userId;
        this.displayName = displayName;
        this.avatarPath = avatarPath;
    }

    @Override
    public String getId() {
        return this.userId;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getAvatarFilePath() {
        return this.avatarPath;
    }

    public JsonElement toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty(USER_ID, userId);
        json.addProperty(DISPLAY_NAME, displayName);
        json.addProperty(AVATAR_PATH, avatarPath);
        return json;
    }

    @Override
    public String toString() {
        return sGSON.toJson(toJSON());
    }
}
