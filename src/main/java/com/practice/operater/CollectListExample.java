package com.practice.operater;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 通过使用 collectList() 操作符，你可以方便地将 Flux 中的元素收集到一个列表中，以便进行批量处理或其他操作。
 *
 */
public class CollectListExample {
    public static void main(String[] args) {
        // 创建一个包含 1 到 5 的整数的 Flux
        Flux<Integer> numbers = Flux.range(1, 5);

        // 使用 collectList() 将 Flux 中的元素收集到一个列表中
        Mono<List<Integer>> listMono = numbers.collectList();

        // 订阅 Mono 并处理结果
        listMono.subscribe(list -> {
            System.out.println("Collected list: " + list);
            // 可以在这里对列表进行批量处理
            int sum = 0;
            for (int num : list) {
                sum += num;
            }
            System.out.println("Sum of numbers: " + sum);
        });

        try {
            // 为了确保异步操作有足够的时间完成
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}