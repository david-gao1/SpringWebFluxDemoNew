package com.practice.branchFlow;

import com.practice.pojo.TaskPojo;
import com.practice.serivice.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("/executeSteps")
    public Flux<String> executeSteps(@RequestParam String taskName) {
        TaskPojo taskPojo = new TaskPojo(taskName);
        return taskService.executeAllSteps(taskPojo);
    }
}