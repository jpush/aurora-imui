package cn.jiguang.imui.messagelist;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by caiyaoguan on 2017/6/2.
 */

public class InterfaceAdapter<T> implements JsonSerializer, JsonDeserializer<T> {
    @Override
    public T deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        String className = jsonObj.get("type").getAsString();
        try {
            Class<?> clz = Class.forName(className);
            return jsonDeserializationContext.deserialize(jsonElement, clz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Object object, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonElement jsonEle = jsonSerializationContext.serialize(object, object.getClass());
        jsonEle.getAsJsonObject().addProperty("type",
                object.getClass().getCanonicalName());
        return jsonEle;
    }
}
