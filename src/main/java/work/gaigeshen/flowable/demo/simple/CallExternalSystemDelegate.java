package work.gaigeshen.flowable.demo.simple;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 * @author gaigeshen
 */
public class CallExternalSystemDelegate implements JavaDelegate {

  @Override
  public void execute(DelegateExecution execution) {
    System.out.println("Calling the external system for employee " + execution.getVariable("employee"));
  }
}
