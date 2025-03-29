package com.solinvictus.SpringWebFluxDemoNew.branchFlow;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TaskProcessingService {
    private static final Logger log = LoggerFactory.getLogger(TaskProcessingService.class);

    public Mono<String> processTypeATask(TaskPojo task) {
        log.info("Processing Type A Task: {}", task.getName());
        return Mono.just("Processed A: " + task.getName());
    }

    public Mono<String> processTypeBTask(TaskPojo task) {
        log.info("Processing Type B Task: {}", task.getName());
        return Mono.just("Processed B: " + task.getName());
    }

    public Mono<String> processTypeCTask(TaskPojo task) {
        log.info("Processing Type C Task: {}", task.getName());
        return Mono.just("Processed C: " + task.getName());
    }

    public Mono<String> processDefaultTask(TaskPojo task) {
        log.info("Processing Default Task: {}", task.getName());
        return Mono.just("Processed Default: " + task.getName());
    }
}
