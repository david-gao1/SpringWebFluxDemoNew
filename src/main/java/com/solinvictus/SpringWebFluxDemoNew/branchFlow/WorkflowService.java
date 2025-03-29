package com.solinvictus.SpringWebFluxDemoNew.branchFlow;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class WorkflowService {
    private final TaskProcessingService taskProcessingService;

    public WorkflowService(TaskProcessingService taskProcessingService) {
        this.taskProcessingService = taskProcessingService;
    }

    public Flux<String> executeWorkflow(List<TaskPojo> tasks) {
        return Flux.fromIterable(tasks)
                .groupBy(TaskPojo::getType) // 按任务类型分组
                .flatMap(groupedFlux -> {
                    switch (groupedFlux.key()) {
                        case "A":
                            return groupedFlux.flatMap(taskProcessingService::processTypeATask);
                        case "B":
                            return groupedFlux.flatMap(taskProcessingService::processTypeBTask);
                        case "C":
                            return groupedFlux.flatMap(taskProcessingService::processTypeCTask);
                        default:
                            return groupedFlux.flatMap(taskProcessingService::processDefaultTask);
                    }
                });
    }
}
