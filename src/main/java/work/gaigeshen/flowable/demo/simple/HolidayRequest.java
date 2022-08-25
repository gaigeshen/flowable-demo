package work.gaigeshen.flowable.demo.simple;

import org.flowable.engine.*;
import org.flowable.engine.form.FormProperty;
import org.flowable.engine.form.StartFormData;
import org.flowable.engine.form.TaskFormData;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.*;

/**
 *
 * @author gaigeshen
 */
public class HolidayRequest {

  public static void main(String[] args) {
    // 配置流程引擎使用的数据库和构建流程引擎
    ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration();
    cfg.setJdbcUrl("jdbc:mysql://114.116.99.177:31001/demo");
    cfg.setJdbcDriver("com.mysql.cj.jdbc.Driver");
    cfg.setJdbcUsername("root");
    cfg.setJdbcPassword("123456");
    cfg.setDatabaseSchemaUpdate("drop-create");
    ProcessEngine processEngine = cfg.buildProcessEngine();

    // 管理流程定义的资源库服务
    RepositoryService repositoryService = processEngine.getRepositoryService();

    // 流程表单服务
    FormService formService = processEngine.getFormService();

    // 部署请假流程定义
    repositoryService.createDeployment().addClasspathResource("holiday-request.bpmn20.xml").deploy();

    // 通过关键字查询请假的流程定义
    ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("holidayRequest").singleResult();

    // 开始流程之前展示流程表单让用户输入
    StartFormData startFormData = formService.getStartFormData(processDefinition.getId());
    System.out.println("the form for input:");
    for (FormProperty formProperty : startFormData.getFormProperties()) {
      System.out.println(formProperty.getId() + " = " + formProperty.getValue() + " (" + formProperty.getType() + ")");
    }

    // 模拟收集用户输入的表单数据
    Scanner scanner = new Scanner(System.in);
    System.out.println("Who are you?");
    String employee = scanner.nextLine();
    System.out.println("What type holiday do you want to request? \n 1) event \n 2) sick");
    String type = scanner.nextLine();
    System.out.println("How many days do you want to request?");
    Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());
    System.out.println("Why do you need them?");
    String description = scanner.nextLine();

    // 提交流程表单来开始这个流程会创建流程实例
    // 注意表单的某个值不可写的话如果传入值就会抛异常
    Map<String, String> formProperties = new HashMap<>();
    formProperties.put("employee", employee);
    formProperties.put("type", type);
    formProperties.put("nrOfHolidays", nrOfHolidays + "");
    formProperties.put("description", description);
    ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), "my_business_key", formProperties);

    // 查询我的代办任务只有该请假流程的任务
    // 因为只部署了请假流程定义
    TaskService taskService = processEngine.getTaskService();
    Task task = taskService.createTaskQuery().taskCandidateGroup("managers").singleResult();

    // 查询任务中的变量和表单数据
    // 因为表单的数据绑定了流程变量所以这里表单的数据和流程变量的数据相同
    Map<String, Object> processVariables = taskService.getVariables(task.getId());
    TaskFormData taskFormData = formService.getTaskFormData(task.getId());
    String descriptionSubmited = null;
    System.out.println("the form submited:");
    for (FormProperty formProperty : taskFormData.getFormProperties()) {
      if (Objects.equals("description", formProperty.getId())) {
        descriptionSubmited = formProperty.getValue();
      }
      System.out.println(formProperty.getId() + " = " + formProperty.getValue() + " (" + formProperty.getType() + ")");
    }
    System.out.println(processVariables.get("employee") + " wants " + processVariables.get("nrOfHolidays") + " of holidays. Do you approve it?");

    // 模拟收集请假流程审批结果是否同意
    boolean approved = scanner.nextLine().equalsIgnoreCase("y");

    // 审批意见作为任务的变量完成此任务
    // 流程根据此变量继续流转下个任务
    Map<String, Object> newVariables = new HashMap<>();
    newVariables.put("approved", approved);
    newVariables.put("description", descriptionSubmited + " (" + (approved ? "approved" : "rejected") + ")");
    taskService.complete(task.getId(), newVariables);

    // 如果审批意见为同意的情况
    // 轮到员工自己来查询当前的代办任务
    Task afterApprovedTask = taskService.createTaskQuery().taskAssignee(employee).singleResult();
    if (Objects.nonNull(afterApprovedTask)) {
      // 查询任务表单包含自己之前填写的表单内容
      TaskFormData afterApprovedTaskForm = formService.getTaskFormData(afterApprovedTask.getId());
      System.out.println("the form after approved:");
      for (FormProperty formProperty : afterApprovedTaskForm.getFormProperties()) {
        System.out.println(formProperty.getId() + " = " + formProperty.getValue() + " (" + formProperty.getType() + ")");
      }

      // 完成我的任务
      System.out.println(employee + " holiday request approved, now complete the task by myself");
      taskService.complete(afterApprovedTask.getId());
    }

    // 查询流程实例的历史记录和花费时间
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
