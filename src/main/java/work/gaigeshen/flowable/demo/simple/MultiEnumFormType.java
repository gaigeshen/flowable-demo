package work.gaigeshen.flowable.demo.simple;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.engine.impl.form.EnumFormType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author gaigeshen
 */
public class MultiEnumFormType extends EnumFormType {

    public MultiEnumFormType(Map<String, String> values) {
        super(values);
    }

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        if (StringUtils.isEmpty(propertyValue)) {
            return null;
        }
        List<String> modelValue = new ArrayList<>();
        for (String value : propertyValue.split(",")) {
            validateValue(value);
            modelValue.add(value);
        }
        return modelValue;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        if (Objects.isNull(modelValue) || !(modelValue instanceof List)) {
            return null;
        }
        List<?> listModelValue = (List<?>) modelValue;
        for (Object value : listModelValue) {
            if (!(value instanceof String)) {
                throw new FlowableIllegalArgumentException("model item value should be a String");
            }
            validateValue((String) value);
        }
        return listModelValue.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    @Override
    public String getName() {
        return "multiEnum";
    }
}
