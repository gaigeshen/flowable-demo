package work.gaigeshen.flowable.demo.simple.form;

import org.flowable.engine.form.AbstractFormType;
import work.gaigeshen.flowable.demo.json.JsonCodec;

import java.util.Collections;
import java.util.Objects;

/**
 * @author gaigeshen
 */
public abstract class SelectionFormType extends AbstractFormType {

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        if (Objects.isNull(propertyValue)) {
            return Collections.emptyList();
        }
        return JsonCodec.instance().decodCollection(propertyValue);
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        if (Objects.isNull(modelValue)) {
            return null;
        }
        return JsonCodec.instance().encode(modelValue);
    }
}
