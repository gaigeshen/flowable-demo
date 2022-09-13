package work.gaigeshen.flowable.demo.simple.form;

import org.flowable.engine.form.AbstractFormType;
import work.gaigeshen.flowable.demo.json.JsonCodec;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * @author gaigeshen
 */
public class DateDurationFormType extends AbstractFormType {

    public static final DateDurationFormType INSTANCE = new DateDurationFormType();

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        if (Objects.isNull(propertyValue)) {
            return Collections.emptyMap();
        }
        return JsonCodec.instance().decodeObject(propertyValue);
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        if (Objects.isNull(modelValue) || !(modelValue instanceof Map)) {
            return null;
        }
        return JsonCodec.instance().encode(modelValue);
    }

    @Override
    public String getName() {
        return "dateDuration";
    }
}
