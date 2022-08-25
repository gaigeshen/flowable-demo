package work.gaigeshen.flowable.demo.simple;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.flowable.engine.*;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author gaigeshen
 */
public class HolidayRequest {

  public static void main(String[] args) {
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration();
    cfg.setJdbcUrl("jdbc:mysql://114.116.99.177:31001/demo");
    cfg.setJdbcDriver("com.mysql.cj.jdbc.Driver");
    cfg.setJdbcUsername("root");
    cfg.setJdbcPassword("123456");
    cfg.setDatabaseSchemaUpdate("drop-create");

    ProcessEngine processEngine = cfg.buildProcessEngine();

    RepositoryService repositoryService = processEngine.getRepositoryService();

    repositoryService.createDeployment()
            .addClasspathResource("holiday-request.bpmn20.xml")
            .deploy();

    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey("holidayRequest")
            .singleResult();

    System.out.println("processDefinition = " + processDefinition);

    FormService formService = processEngine.getFormService();

    StartFormData startFormData = formService.getStartFormData(processDefinition.getId());

    System.out.println("The form: \n" + ToStringBuilder.reflectionToString(startFormData, ToStringStyle.JSON_STYLE));

    Scanner scanner = new Scanner(System.in);

    System.out.println("Who are you?");

    String employee = scanner.nextLine();

    System.out.println("What type holiday do you want to request? \n 1) event \n 2) sick");

    String type = scanner.nextLine();

    System.out.println("How many days do you want to request?");

    Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

    System.out.println("Why do you need them?");

    String description = scanner.nextLine();

    RuntimeService runtimeService = processEngine.getRuntimeService();

    Map<String, Object> variables = new HashMap<>();

    variables.put("employee", employee);
    variables.put("type", type);
    variables.put("nrOfHolidays", nrOfHolidays);
    variables.put("description", description);

    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("holidayRequest", "my_business_key", variables);

    TaskService taskService = processEngine.getTaskService();

    List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();

    System.out.println("You have " + tasks.size() + " tasks:");

    for (int i = 0; i < tasks.size(); i++) {
      System.out.println((i + 1) + ") " + tasks.get(i).getName());
    }

    System.out.println("Which task would you like to complete?");

    int taskIndex = Integer.parseInt(scanner.nextLine());

    Task task = tasks.get(taskIndex - 1);

    Map<String, Object> processVariables = taskService.getVariables(task.getId());
    TaskFormData taskFormData = formService.getTaskFormData(task.getId());

    System.out.println(processVariables.get("employee") + " wants "
            + processVariables.get("nrOfHolidays") + " of holidays. Do you approve it?\n"
            + ToStringBuilder.reflectionToString(taskFormData, ToStringStyle.JSON_STYLE));


    boolean approved = scanner.nextLine().equalsIgnoreCase("y");

    Map<String, Object> newVariables = new HashMap<>();
    newVariables.put("approved", approved);

    taskService.complete(task.getId(), newVariables);

    Task afterApprovedTask = taskService.createTaskQuery().taskAssignee(employee).singleResult();

    TaskFormData afterApprovedTaskForm = formService.getTaskFormData(afterApprovedTask.getId());

    System.out.println(employee + " holiday request approved, now complete the task by myself\n"
            + ToStringBuilder.reflectionToString(afterApprovedTaskForm, ToStringStyle.JSON_STYLE));

    taskService.complete(afterApprovedTask.getId());

    HistoryService historyService = processEngine.getHistoryService();
    List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
            .processInstanceId(processInstance.getId())
            .finished()
            .orderByHistoricActivityInstanceEndTime()
            .asc()
            .list();

    for (HistoricActivityInstance instance : historicActivityInstances) {
      System.out.println(instance.getActivityId() + " took " + instance.getDurationInMillis() + " milliseconds");
    }
  }

}
