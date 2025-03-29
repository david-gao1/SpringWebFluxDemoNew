package com.solinvictus.SpringWebFluxDemoNew.branchFlow;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {
    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping("/run")
    public Flux<String> runWorkflow() {
        List<TaskPojo> tasks = Arrays.asList(
                new TaskPojo("Task1", "A"),
                new TaskPojo("Task2", "B"),
                new TaskPojo("Task3", "A"),
                new TaskPojo("Task4", "C"),
                new TaskPojo("Task5", "B"),
                new TaskPojo("Task6", "C"),
                new TaskPojo("Task7", "D")  // 未知类型
        );

        return workflowService.executeWorkflow(tasks);
    }
}
