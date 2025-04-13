package com.practice.branchFlow;


import com.practice.pojo.TaskPojo;
import com.practice.serivice.BranchWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {
    @Autowired
    private BranchWorkflowService branchWorkflowService;


    @GetMapping("/run")
    public Flux<String> runWorkflow() {
//        List<TaskPojo> taskPojos = Arrays.asList(
//                new TaskPojo("Task1", "A"),
//                new TaskPojo("Task2", "B"),
//                new TaskPojo("Task3", "A"),
//                new TaskPojo("Task4", "C"),
//                new TaskPojo("Task5", "B"),
//                new TaskPojo("Task6", "C"),
//                new TaskPojo("Task7", "D")  // 未知类型
//        );
        TaskPojo task = new TaskPojo("MainTask");
        return branchWorkflowService.executeWorkflow(task);
    }
}
