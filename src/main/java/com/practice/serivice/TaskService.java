package com.practice.serivice;

import com.practice.pojo.TaskPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.Random;

@Service
public class TaskService {
    private static final Logger log = LoggerFactory.getLogger(TaskService.class);
    private static final Random random = new Random();

    // 执行步骤 1
    public Mono<String> executeStep1(TaskPojo taskPojo) {
        return Mono.just("Step1-" + taskPojo.getName())
               .doOnNext(step -> log.info("Step 1: Start processing {}", step));
    }

    // 执行步骤 2
    public Mono<String> executeStep2(String step1Result) {
        String step2Result = "Step2-" + step1Result;
        return Mono.just(step2Result)
               .doOnNext(step -> log.info("Step 2: Processing {}", step));
    }

    // 执行步骤 3
    public Mono<String> executeStep3(String step2Result) {
        String step3Result = "Step3-" + step2Result;
        return Mono.just(step3Result)
               .doOnNext(step -> log.info("Step 3: Processing {}", step));
    }

    // 执行步骤 4
    public Mono<String> executeStep4(TaskPojo taskPojo) {
        return Mono.just("Step4-" + taskPojo.getName())
               .doOnNext(step -> log.info("Step 4: Start processing {}", step));
    }

    // 执行步骤 5
    public Mono<String> executeStep5(String step4Result) {
        String step5Result = "Step5-" + step4Result;
        return Mono.just(step5Result)
               .doOnNext(step -> log.info("Step 5: Processing {}", step));
    }

    // 执行步骤 6
    public Mono<String> executeStep6(String step5Result) {
        String step6Result = "Step6-" + step5Result;
        return Mono.just(step6Result)
               .doOnNext(step -> log.info("Step 6: Processing {}", step));
    }

    // 根据结果进行分支选择
    public Flux<String> processBranch(String result1To3, String result4To6) {
        int decision = random.nextInt(3);
        log.info("Decision based on {} and {}: Branch{}", result1To3, result4To6, decision);
        switch (decision) {
            case 0:
                return Flux.just("Branch0 - StepA", "Branch0 - StepB");
            case 1:
                return Flux.just("Branch1 - StepX", "Branch1 - StepY", "Branch1 - StepZ");
            case 2:
                return Flux.just("Branch2 - StepM");
            default:
                return Flux.empty();
        }
    }

    public Flux<String> executeAllSteps(TaskPojo taskPojo) {
        Mono<String> step1Result = executeStep1(taskPojo);
        Mono<String> step2Result = step1Result.flatMap(this::executeStep2);
        Mono<String> step3Result = step2Result.flatMap(this::executeStep3);

        Mono<String> step4Result = executeStep4(taskPojo);
        Mono<String> step5Result = step4Result.flatMap(this::executeStep5);
        Mono<String> step6Result = step5Result.flatMap(this::executeStep6);

        return Mono.zip(step3Result, step6Result)
               .flatMapMany(tuple -> processBranch(tuple.getT1(), tuple.getT2()))
               .subscribeOn(Schedulers.parallel());
    }
}