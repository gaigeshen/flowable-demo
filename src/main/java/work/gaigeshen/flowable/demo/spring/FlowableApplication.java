package work.gaigeshen.flowable.demo.spring;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author gaigeshen
 */
@SpringBootApplication
public class FlowableApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlowableApplication.class, args);
  }

  @Bean
  public CommandLineRunner init(
          RepositoryService repositoryService,
          RuntimeService runtimeService, TaskService taskService) {
    return args -> {
      System.out.println("number of process definitions: " + repositoryService.createProcessDefinitionQuery().count());
      System.out.println("number of tasks: " + taskService.createTaskQuery().count());
      runtimeService.startProcessInstanceByKey("oneTaskProcess");
      System.out.println("number of tasks after process start: " + taskService.createTaskQuery().count());
    };
  }
}
