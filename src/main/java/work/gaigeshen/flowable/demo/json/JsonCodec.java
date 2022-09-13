package work.gaigeshen.flowable.demo.json;

import java.util.Collection;
import java.util.Map;

/**
 * @author gaigeshen
 * @see <a href="http://www.json.org/json-en.html">JSON (JavaScript Object Notation)</a>
 */
public interface JsonCodec {

    static JsonCodec instance() {
        return GsonJsonCodec.INSTANCE;
    }

    String encode(Object obj);

    <T> T decodeObject(String json, Class<T> resultClass);

    <E> Collection<E> decodeCollection(String json, Class<E> itemClass);

    Map<String, Object> decodeObject(String json);

    Collection<Object> decodCollection(String json);
}
