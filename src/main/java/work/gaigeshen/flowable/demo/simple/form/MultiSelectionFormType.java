package work.gaigeshen.flowable.demo.simple.form;

/**
 * @author gaigeshen
 */
public class MultiSelectionFormType extends SelectionFormType {

    public static final MultiSelectionFormType INSTANCE = new MultiSelectionFormType();

    @Override
    public String getName() {
        return "multiSelection";
    }
}
