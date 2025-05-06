package com.practice.branchFlow;

import reactor.core.publisher.Flux;
import java.time.Duration;

public class ParallelFluxWithTiming {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        // 模拟任务1
        Flux<String> task1 = Flux.just("Task1 - A", "Task1 - B", "Task1 - C")
                .delayElements(Duration.ofMillis(300));

        // 模拟任务2
        Flux<String> task2 = Flux.just("Task2 - X", "Task2 - Y", "Task2 - Z")
                .delayElements(Duration.ofMillis(200));

        // 并行执行任务并记录执行时间
        Flux<String> merged = Flux.merge(task1, task2)
                .doOnSubscribe(s -> System.out.println("Tasks started..."))
                .doOnNext(item -> System.out.println("[Result] " + item))
                .doFinally(signal -> {
                    long endTime = System.currentTimeMillis();
                    System.out.println("All tasks completed. Total time: " + (endTime - startTime) + " ms");
                });

        // 执行
        merged.subscribe();

    }
}
