package com.practice.serivice;


import com.practice.pojo.TaskPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

/**
 * 分支flux
 * 串行执行
 */
@Service
public class BranchWorkflowService {
    private static final Logger log = LoggerFactory.getLogger(BranchWorkflowService.class);
    private final Random random = new Random();

    public Flux<String> executeWorkflow(TaskPojo taskPojo) {
        return executeInitialSteps(taskPojo) // 先执行 Step1 → Step2 → Step3
                .flatMapMany(this::branchProcessing) // 进入分支（A、B、C、D）
                .flatMap(this::finalStep); // 处理最终结果
    }

    /** 🚀 合并 Step1 → Step2 → Step3 **/
    private Mono<String> executeInitialSteps(TaskPojo taskPojo) {
        return Mono.just("Step1-" + taskPojo.getName())
                .doOnNext(step -> log.info("Step 1: Start processing {}", step))
                .flatMap(step1Result -> {
                    String step2Result = "Step2-" + step1Result;
                    log.info("Step 2: Processing {}", step2Result);
                    return Mono.just(step2Result);
                })
                .flatMap(step2Result -> {
                    int decision = random.nextInt(4); // 随机选择 0-3 进入不同分支
                    String branch = "Branch" + decision;
                    log.info("Step 3: Decision making for {}, entering {}", step2Result, branch);
                    return Mono.just(branch);
                });
    }

    /** 进入不同的分支 **/
    private Flux<String> branchProcessing(String branch) {
        switch (branch) {
            case "Branch0":
                return branchA();
            case "Branch1":
                return branchB();
            case "Branch2":
                return branchC();
            case "Branch3":
                return branchD();
            default:
                return Flux.just("Unknown branch");
        }
    }

    /** 分支 A（3步） **/
    private Flux<String> branchA() {
        return Flux.just("A1", "A2", "A3")
                .flatMap(this::branchStep)
                .doOnNext(step -> log.info("Branch A processing {}", step));
    }

    /** 分支 B（3步） **/
    private Flux<String> branchB() {
        return Flux.just("B1", "B2", "B3")
                .flatMap(this::branchStep)
                .doOnNext(step -> log.info("Branch B processing {}", step));
    }

    /** 分支 C（3步） **/
    private Flux<String> branchC() {
        return Flux.just("C1", "C2", "C3")
                .flatMap(this::branchStep)
                .doOnNext(step -> log.info("Branch C processing {}", step));
    }

    /** 分支 D（3步） **/
    private Flux<String> branchD() {
        return Flux.just("D1", "D2", "D3")
                .flatMap(this::branchStep)
                .doOnNext(step -> log.info("Branch D processing {}", step));
    }

    /** 每个分支的具体步骤 **/
    private Mono<String> branchStep(String step) {
        log.info("Executing step: {}", step);
        return Mono.just("Processed-" + step);
    }

    /** 最终处理 **/
    private Mono<String> finalStep(String result) {
        log.info("Final step processing for {}", result);
        return Mono.just("FinalResult: " + result);
    }
}

