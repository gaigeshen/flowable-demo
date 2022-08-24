package work.gaigeshen.flowable.demo.simple;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
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

    Deployment deploy = repositoryService.createDeployment()
            .addClasspathResource("holiday-request.bpmn20.xml")
            .deploy();

    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .deploymentId(deploy.getId())
            .singleResult();

    System.out.println("processDefinition = " + processDefinition);

    Scanner scanner = new Scanner(System.in);

    System.out.println("Who are you?");

    String employee = scanner.nextLine();

    System.out.println("How many days do you want to request?");

    Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

    System.out.println("Why do you need them?");

    String description = scanner.nextLine();

    RuntimeService runtimeService = processEngine.getRuntimeService();

    Map<String, Object> variables = new HashMap<>();

    variables.put("employee", employee);
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

    System.out.println(processVariables.get("employee") + " wants " + processVariables.get("nrOfHolidays") + " of holidays. Do you approve it?");

    boolean approved = scanner.nextLine().equalsIgnoreCase("y");

    Map<String, Object> newVariables = new HashMap<>();
    newVariables.put("approved", approved);

    taskService.complete(task.getId(), newVariables);

    Task afterApprovedTask = taskService.createTaskQuery().taskAssignee(employee).singleResult();

    System.out.println(employee + " holiday request approved, now complete the task by myself");

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
