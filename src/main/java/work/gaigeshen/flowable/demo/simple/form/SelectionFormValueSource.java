package work.gaigeshen.flowable.demo.simple.form;

import java.util.List;
import java.util.Map;

/**
 * @author gaigeshen
 */
public interface SelectionFormValueSource {

    List<Map<String, String>> getSource(String processDefinitionKey, String formPropertyId);
}
