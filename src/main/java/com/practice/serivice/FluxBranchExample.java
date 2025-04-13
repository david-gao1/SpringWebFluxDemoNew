package com.practice.serivice;

import reactor.core.publisher.Flux;

public class FluxBranchExample {

    public static void main(String[] args) {
        // 创建包含多个事件的 Flux
        Flux<Integer> allEvents = Flux.range(1, 12);

        // 执行前三个事件并进行分支选择
        Flux<Integer> result = allEvents
               .take(3)
               .doOnNext(System.out::println) // 前三个元素，都会执行一遍
               .collectList()//会阻塞流，因为会等待Flux完成，
               .flatMapMany(list -> {
                    // 获取第三个事件的结果
                    Integer thirdEventResult = list.get(2);

                    // 根据第三个事件的结果进行分支判断
                    if (thirdEventResult % 2 == 0) {
                        //todo：当做普通的方法看就好
                        // 分支 1：选择 4, 5, 6 事件
                        return allEvents.skip(3).take(3);
                    } else if (thirdEventResult % 3 == 0) {
                        // 分支 2：选择 7, 8, 9 事件
                        return allEvents.skip(6).take(3);
                    } else {
                        // 分支 3：选择 10, 11, 12 事件
                        return allEvents.skip(9).take(3);
                    }
                });

        // 订阅最终的 Flux 以执行操作
        result.subscribe(System.out::println);

        try {
            // 为确保异步操作有足够的时间完成
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}