package com.practice.serivice;

import com.practice.pojo.TaskPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Random;

/**
 * concatMap会严格顺序执行
 * flatMap可能会乱序
 *
 */
@Service
public class ParallelStepsService3 {
    private static final Logger log = LoggerFactory.getLogger(ParallelStepsService3.class);
    private static final Random random = new Random();

    // 逐步执行 1-3 步，并返回每个步骤的结果
    private Flux<String> executeSteps1To3(TaskPojo taskPojo) {
        return Flux
                .just("Step1-" + taskPojo.getName())
                .flatMap(step1 -> {
                    log.info("Step 1: Processing {}", step1);
                    return Mono.just(step1);
                })
                .concatWith(Flux.just("Step2-Processed", "Step3-Processed"))
                .subscribeOn(Schedulers.parallel());
    }

    // 逐步执行 4-6 步，并返回每个步骤的结果
    private Flux<String> executeSteps4To6(TaskPojo taskPojo) {
        return Flux.just("Step4-" + taskPojo.getName())
                .flatMap(step4 -> {
                    log.info("Step 4: Processing {}", step4);
                    return Mono.just(step4);
                })
                .concatWith(Flux.just("Step5-Processed", "Step6-Processed"))
                .subscribeOn(Schedulers.parallel());
    }

    // 分支逻辑，实时返回每个步骤的结果
    private Flux<String> processBranch(String result1To3, String result4To6) {
        int decision = random.nextInt(3);
        log.info("Decision based on {} and {}: Branch{}", result1To3, result4To6, decision);
        switch (decision) {
            case 0:
                return Flux.just("Branch0-StepA", "Branch0-StepB");
            case 1:
                return Flux.just("Branch1-StepX", "Branch1-StepY", "Branch1-StepZ");
            case 2:
                return Flux.just("Branch2-StepM");
            default:
                return Flux.empty();
        }
    }

    // **公开 API 方法**：执行整个任务，并**逐步返回结果**
    public Flux<String> executeTask(TaskPojo taskPojo) {
        Flux<String> steps1To3 = executeSteps1To3(taskPojo);
        Flux<String> steps4To6 = executeSteps4To6(taskPojo);
        return Flux.merge(steps1To3, steps4To6)  // 并行执行 1-3 和 4-6
                .concatWith(//todo：是一个串联操作，会保证1-3和4-6执行完后，再执行后续的操作
                        Mono.zip(steps1To3.last(), steps4To6.last())  // 获取 1-3 和 4-6 的最终结果
                                .flatMapMany(tuple -> processBranch(tuple.getT1(), tuple.getT2()))
                )
                .startWith("Started Processing: " + taskPojo.getName())  // 开始任务
                .concatWith(Mono.just("Task Completed!"))  // 任务完成
                .doOnNext(log::info)
                .delayElements(Duration.ofMillis(500));
    }
}
