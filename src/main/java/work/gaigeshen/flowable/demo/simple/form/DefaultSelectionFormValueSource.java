package work.gaigeshen.flowable.demo.simple.form;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaigeshen
 */
public class DefaultSelectionFormValueSource implements SelectionFormValueSource {

    public static final DefaultSelectionFormValueSource INSTANCE = new DefaultSelectionFormValueSource();

    @Override
    public List<Map<String, String>> getSource(String processDefinitionKey, String formPropertyId) {
        Map<String, String> selection1 = new HashMap<>();
        selection1.put("id", "id1");
        selection1.put("name", "name1");

        Map<String, String> selection2 = new HashMap<>();
        selection2.put("id", "id2");
        selection2.put("name", "name2");

        return Arrays.asList(selection1, selection2);
    }
}
