package com.practice.branchFlow;

import reactor.core.publisher.Flux;

public class FluxNestedHandleWithInitialFluxExample {
    public static void main(String[] args) {
        // 初始化的 Flux
        Flux<String> initialFlux = Flux.just("开始处理数字啦");

        Flux<Integer> numbers = Flux.just(1, 2, 3, 4, 5);

        Flux<String> result = numbers.handle((num, sink) -> {
            if (num % 2 == 0) {
                // 偶数分支
                Flux.just(num).handle((evenNum, innerSink) -> {
                    if (evenNum % 4 == 0) {
                        innerSink.next(evenNum + " 是能被 4 整除的偶数");
                    } else {
                        innerSink.next(evenNum + " 是不能被 4 整除的偶数");
                    }
                }).subscribe(innerResult -> sink.next(innerResult.toString()));
            } else {
                // 奇数分支
                Flux.just(num).handle((oddNum, innerSink) -> {
                    if (oddNum % 3 == 0) {
                        innerSink.next(oddNum + " 是能被 3 整除的奇数");
                    } else {
                        innerSink.next(oddNum + " 是不能被 3 整除的奇数");
                    }
                }).subscribe(innerResult -> sink.next(innerResult.toString()));
            }
        });

        // 合并初始化的 Flux 和处理结果的 Flux
        Flux<String> finalResult = Flux.concat(initialFlux, result);

        finalResult.subscribe(System.out::println);
    }
}