package work.gaigeshen.flowable.demo.simple;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

/**
 *
 * @author gaigeshen
 */
public class SendRejectionMail implements JavaDelegate {
  @Override
  public void execute(DelegateExecution execution) {
    System.out.println("Send rejection mail for employee " + execution.getVariable("employee"));
  }
}
