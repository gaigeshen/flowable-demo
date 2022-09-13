package work.gaigeshen.flowable.demo.simple.form;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author gaigeshen
 */
public class DefaultSelectionFormValueSource implements SelectionFormValueSource {

    public static final DefaultSelectionFormValueSource INSTANCE = new DefaultSelectionFormValueSource();

    @Override
    public List<FormValueSource> getSource(String processDefinitionKey, String formPropertyId) {
        if ("multiSelection".equals(formPropertyId)) {
            return Arrays.asList(
                    new DefaultFormValueSource("multiSelection1", "multiSelection1"),
                    new DefaultFormValueSource("multiSelection2", "multiSelection2"));
        }
        else if ("singleSelection".equals(formPropertyId)) {
            return Arrays.asList(
                    new DefaultFormValueSource("singleSelection1", "singleSelection1"),
                    new DefaultFormValueSource("singleSelection2", "singleSelection2"));
        }
        return Collections.emptyList();
    }
}
