package com.practice.branchFlow;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Duration;

public class ParallelFluxDelayExample {

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        // 每个元素都处理 3 秒（总共 9 秒左右）
        Flux<String> task1 = Flux.just("Task1 - A", "Task1 - B", "Task1 - C")
                .flatMap(item -> Mono.just(item)
                        .delayElement(Duration.ofSeconds(3)));

        // 每个元素处理 2 秒（总共 6 秒左右）
        Flux<String> task2 = Flux.just("Task2 - X", "Task2 - Y", "Task2 - Z")
                .flatMap(item -> Mono.just(item)
                        .delayElement(Duration.ofSeconds(2)));

        // 合并并行执行
        Flux<String> merged = Flux.merge(task1, task2)
                .doOnSubscribe(sub -> System.out.println("Tasks started..."))
                .doOnNext(item -> System.out.println("[Result] " + item + " at " + (System.currentTimeMillis() - startTime) + " ms"))
                .doFinally(signal -> {
                    long endTime = System.currentTimeMillis();
                    System.out.println("All tasks completed. Total time: " + (endTime - startTime) + " ms");
                });

        // 执行
        merged.subscribe();

        // 等待时间足够长，避免主线程提前退出
        Thread.sleep(10000);
    }
}
