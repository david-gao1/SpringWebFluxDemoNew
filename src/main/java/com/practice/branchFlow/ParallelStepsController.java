package com.practice.branchFlow;

import com.practice.pojo.TaskPojo;
import com.practice.serivice.ParallelStepsService3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/tasks")
public class ParallelStepsController {
    @Autowired
    private ParallelStepsService3 parallelStepsService3;


    @PostMapping(value = "/execute", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> executeTask(@RequestBody TaskPojo taskPojo) {
        return parallelStepsService3.executeTask(taskPojo);
    }
}
