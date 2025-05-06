package com.practice.defer;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DeferBranchingExample {

    public static void main(String[] args) {
        String requestType = "A";

        Flux<String> result = Flux.defer(() -> {
            System.out.println("==> Main defer triggered");
            return chooseBranch(requestType);
        });

        result.subscribe(value -> System.out.println("Received: " + value));
    }

    // 分支选择
    private static Flux<String> chooseBranch(String type) {
        switch (type) {
            case "A":
                return processBranchA();
            case "B":
                return processBranchB();
            default:
                return Flux.just("Unknown branch");
        }
    }

    // A 分支包含嵌套逻辑
    private static Flux<String> processBranchA() {
        System.out.println("Executing Branch A");
        return Flux.concat(
                Mono.just("A-step1"),
                Mono.defer(() -> {
                    System.out.println("Branch A inner defer triggered");
                    return Mono.just("A-step2");
                }),
                Mono.defer(() -> processSubStep("A-step3"))
        );
    }

    // B 分支简单
    private static Flux<String> processBranchB() {
        System.out.println("Executing Branch B");
        return Flux.just("B-step1", "B-step2");
    }

    // 子步骤（模拟副作用）
    private static Mono<String> processSubStep(String name) {
        System.out.println("SubStep: " + name);
        return Mono.just(name + "-done");
    }
}
