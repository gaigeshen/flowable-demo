package work.gaigeshen.flowable.demo.json;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gaigeshen
 * @see <a href="https://github.com/google/gson">Google Gson</a>
 */
public class GsonJsonCodec implements JsonCodec {

    public static final GsonJsonCodec INSTANCE = new GsonJsonCodec();

    private final Gson gson;

    public GsonJsonCodec(Gson gson) {
        this.gson = gson;
    }

    public GsonJsonCodec() {
        this(new GsonBuilder().serializeNulls().create());
    }

    @Override
    public String encode(Object obj) {
        return gson.toJson(obj);
    }

    @Override
    public <T> T decodeObject(String json, Class<T> resultClass) {
        return gson.fromJson(json, resultClass);
    }

    @Override
    public <E> Collection<E> decodeCollection(String json, Class<E> itemClass) {
        Type type = new TypeToken<ArrayList<E>>() { }.getType();
        return gson.fromJson(json, type);
    }

    @Override
    public Map<String, Object> decodeObject(String json) {
        return convertObject(JsonParser.parseString(json).getAsJsonObject());
    }

    @Override
    public Collection<Object> decodCollection(String json) {
        return convertArray(JsonParser.parseString(json).getAsJsonArray());
    }

    private Map<String, Object> convertObject(JsonObject jsonObject) {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (entry.getValue() instanceof JsonPrimitive) {
                JsonPrimitive jsonPrimitive = (JsonPrimitive) entry.getValue();
                if (jsonPrimitive.isString()) {
                    result.put(entry.getKey(), jsonPrimitive.getAsString());
                }
                else if (jsonPrimitive.isNumber()) {
                    result.put(entry.getKey(), jsonPrimitive.getAsNumber());
                }
                else if (jsonPrimitive.isBoolean()) {
                    result.put(entry.getKey(), jsonPrimitive.getAsBoolean());
                }
                continue;
            }
            if (entry.getValue() instanceof JsonArray) {
                result.put(entry.getKey(), convertArray((JsonArray) entry.getValue()));
                continue;
            }
            if (entry.getValue() instanceof JsonObject) {
                result.put(entry.getKey(), convertObject((JsonObject) entry.getValue()));
                continue;
            }
            if (entry.getValue() instanceof JsonNull) {
                result.put(entry.getKey(), null);
            }
        }
        return result;
    }

    private Collection<Object> convertArray(JsonArray jsonArray) {
        Collection<Object> result = new ArrayList<>();
        for (JsonElement jsonElement : jsonArray) {
            if (jsonElement instanceof JsonPrimitive) {
                JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;
                if (jsonPrimitive.isString()) {
                    result.add(jsonPrimitive.getAsString());
                }
                else if (jsonPrimitive.isNumber()) {
                    result.add(jsonPrimitive.getAsNumber());
                }
                else if (jsonPrimitive.isBoolean()) {
                    result.add(jsonPrimitive.getAsBoolean());
                }
                continue;
            }
            if (jsonElement instanceof JsonArray) {
                result.add(convertArray((JsonArray) jsonElement));
                continue;
            }
            if (jsonElement instanceof JsonObject) {
                result.add(convertObject((JsonObject) jsonElement));
                continue;
            }
            if (jsonElement instanceof JsonNull) {
                result.add(null);
            }
        }
        return result;
    }
}
