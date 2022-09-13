package work.gaigeshen.flowable.demo.simple;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.engine.impl.form.DateFormType;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author gaigeshen
 */
public class DateDurationFormType extends DateFormType {

    public DateDurationFormType(String datePattern) {
        super(datePattern);
    }

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        if (StringUtils.isEmpty(propertyValue)) {
            return null;
        }
        String[] dateValues = propertyValue.split(",");
        if (dateValues.length != 2) {
            return null;
        }
        try {
            return Arrays.asList(dateFormat.parseObject(dateValues[0]), dateFormat.parseObject(dateValues[1]));
        } catch (ParseException e) {
            throw new FlowableIllegalArgumentException("invalid date duration value " + propertyValue, e);
        }
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        if (Objects.isNull(modelValue) || !(modelValue instanceof List)) {
            return null;
        }
        List<?> dateValues = (List<?>) modelValue;
        if (dateValues.size() != 2) {
            return null;
        }
        return dateFormat.format(dateValues.get(0)) + "," + dateFormat.format(dateValues.get(1));
    }

    @Override
    public String getName() {
        return "dateDuration";
    }
}
