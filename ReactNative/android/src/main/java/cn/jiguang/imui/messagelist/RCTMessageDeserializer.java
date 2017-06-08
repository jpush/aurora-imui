package cn.jiguang.imui.messagelist;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by caiyaoguan on 2017/6/6.
 */

public class RCTMessageDeserializer implements JsonDeserializer<RCTMessage> {
    @Override
    public RCTMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject userObject = jsonObject.get("fromUser").getAsJsonObject();
        String userId = userObject.get("userId").getAsString();
        String displayName = userObject.get("displayName").getAsString();
        String avatarPath = userObject.get("avatarPath").getAsString();
        RCTUser rctUser = new RCTUser(userId, displayName, avatarPath);
        String msgId = jsonObject.get("msgId").getAsString();
        String status = jsonObject.get("status").getAsString();
        String msgType = jsonObject.get("msgType").getAsString();
        boolean isOutgoing = jsonObject.get("isOutgoing").getAsBoolean();
        RCTMessage rctMessage = new RCTMessage(msgId, status, msgType, isOutgoing);
        rctMessage.setFromUser(rctUser);
        if (jsonObject.has("mediaPath")) {
            rctMessage.setMediaFilePath(jsonObject.get("mediaPath").getAsString());
        }
        if (jsonObject.has("duration")) {
            rctMessage.setDuration(jsonObject.get("duration").getAsLong());
        }
        if (jsonObject.has("text")) {
            rctMessage.setText(jsonObject.get("text").getAsString());
        }
        if (jsonObject.has("timeString")) {
            rctMessage.setTimeString(jsonObject.get("timeString").getAsString());
        }
        if (jsonObject.has("progress")) {
            rctMessage.setProgress(jsonObject.get("progress").getAsString());
        }
        return rctMessage;
    }
}
