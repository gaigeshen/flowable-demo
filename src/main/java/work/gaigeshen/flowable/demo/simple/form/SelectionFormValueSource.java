package work.gaigeshen.flowable.demo.simple.form;

import java.util.List;

/**
 * @author gaigeshen
 */
public interface SelectionFormValueSource {

    List<FormValueSource> getSource(String processDefinitionKey, String formPropertyId);



    interface FormValueSource {

        String getId();

        String getName();
    }

    class DefaultFormValueSource implements FormValueSource {

        private final String id;

        private final String name;

        public DefaultFormValueSource(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
