package com.practice.branchFlow;

import reactor.core.publisher.Flux;

public class FluxNestedBranchExample {
    public static void main(String[] args) {
        Flux<Integer> numbers = Flux.just(1, 2, 3, 4, 5);

        Flux<String> result = numbers.flatMap(num -> {
            if (num % 2 == 0) {
                // 偶数分支
                return Flux.just(num).flatMap(evenNum -> {
                    if (evenNum % 4 == 0) {
                        // 能被 4 整除的偶数分支
                        return Flux.just(evenNum + " 是能被 4 整除的偶数");
                    } else {
                        // 不能被 4 整除的偶数分支
                        return Flux.just(evenNum + " 是不能被 4 整除的偶数");
                    }
                });
            } else {
                // 奇数分支
                return Flux.just(num).flatMap(oddNum -> {
                    if (oddNum % 3 == 0) {
                        // 能被 3 整除的奇数分支
                        return Flux.just(oddNum + " 是能被 3 整除的奇数");
                    } else {
                        // 不能被 3 整除的奇数分支
                        return Flux.just(oddNum + " 是不能被 3 整除的奇数");
                    }
                });
            }
        });

        result.subscribe(System.out::println);
    }
}