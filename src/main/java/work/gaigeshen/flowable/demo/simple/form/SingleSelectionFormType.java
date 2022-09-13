package work.gaigeshen.flowable.demo.simple.form;

/**
 * @author gaigeshen
 */
public class SingleSelectionFormType extends SelectionFormType {

    public static final SingleSelectionFormType INSTANCE = new SingleSelectionFormType();

    @Override
    public String getName() {
        return "singleSelection";
    }
}
