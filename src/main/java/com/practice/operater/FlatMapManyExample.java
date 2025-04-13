package com.practice.operater;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FlatMapManyExample {
    public static void main(String[] args) {
        // 创建一个包含单个元素的 Mono
        Mono<String> mono = Mono.just("hello");

        // 使用 flatMapMany 将 Mono 中的元素转换为多个元素的 Flux
        Flux<Character> charFlux = mono.flatMapMany(s -> {
            // 将字符串转换为字符数组
            char[] chars = s.toCharArray();
            // 将 char[] 转换为 Character[]
            Character[] charObjects = new Character[chars.length];
            for (int i = 0; i < chars.length; i++) {
                charObjects[i] = chars[i];
            }
            // 创建一个 Flux
            return Flux.fromArray(charObjects);
        });

        // 订阅 Flux 并打印结果
        charFlux.subscribe(System.out::println);
    }
}