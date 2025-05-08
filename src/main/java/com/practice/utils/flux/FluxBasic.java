package com.practice.utils.flux;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class FluxBasic {

    /** 
     * 从静态数据创建 Flux 
     */
    public static Flux<String> createFluxFromValues() {
        return Flux.just("A", "B", "C");
    }

    /** 
     * 从集合创建 Flux 
     */
    public static Flux<String> createFluxFromList(List<String> list) {
        return Flux.fromIterable(list);
    }

    /** 
     * 生成范围数据 Flux 
     */
    public static Flux<Integer> createFluxFromRange(int start, int count) {
        return Flux.range(start, count);
    }

    /** 
     * 并行合并 Flux 
     */
    public static Flux<String> mergeFlux(Flux<String> flux1, Flux<String> flux2) {
        return Flux.merge(flux1, flux2);
    }

    /** 
     * 串行合并 Flux 
     */
    public static Flux<String> concatFlux(Flux<String> flux1, Flux<String> flux2) {
        return Flux.concat(flux1, flux2);
    }

    /** 
     * zip 组合 Flux 
     */
    public static Flux<String> zipFlux(Flux<String> flux1, Flux<Integer> flux2) {
        return Flux.zip(flux1, flux2, (a, b) -> a + b);
    }

    /** 
     * 过滤偶数 Flux 
     */
    public static Flux<Integer> filterEvenNumbers(Flux<Integer> flux) {
        return flux.filter(num -> num % 2 == 0);
    }

    /** 
     * 转换字符串为大写 
     */
    public static Flux<String> toUpperCase(Flux<String> flux) {
        return flux.map(String::toUpperCase);
    }

    /** 
     * flatMap 示例（拆分字符串） 
     */
    public static Flux<String> flatMapExample(Flux<String> flux) {
        return flux.flatMap(s -> Flux.just(s.split("")));
    }

    /** 
     * 监听 & 调试 Flux 
     */
    public static Flux<Integer> logFlux(Flux<Integer> flux) {
        return flux.doOnNext(num -> System.out.println("Processing: " + num));
    }

    /** 
     * 限制 Flux 元素数量 
     */
    public static Flux<Integer> takeElements(Flux<Integer> flux, int count) {
        return flux.take(count);
    }

    /** 
     * 模拟异步流（每秒发射一个元素） 
     */
    public static Flux<String> delayedFlux(Flux<String> flux, Duration delay) {
        return flux.delayElements(delay);
    }

    /** 
     * 若 Flux 为空，则提供默认值 
     */
    public static Flux<String> defaultIfEmptyExample(Flux<String> flux) {
        return flux.defaultIfEmpty("Default Value");
    }

    /** 
     * 若 Flux 为空，则切换到另一个 Flux 
     */
    public static Flux<String> switchIfEmptyExample(Flux<String> flux) {
        return flux.switchIfEmpty(Flux.just("Fallback"));
    }

    /** 
     * 在新线程上运行 Flux 
     */
    public static <T> Flux<T> runOnNewThread(Flux<T> flux) {
        return flux.subscribeOn(Schedulers.boundedElastic());
    }

    /** 
     * 运行示例
     * 先看成是流
     */
    public static void main(String[] args) throws InterruptedException {
        //用例：subscribe触发消费
        System.out.println("1️⃣ 创建 Flux:");
        createFluxFromValues().subscribe(System.out::println);

        System.out.println("\n2️⃣ 从集合创建 Flux:");
        createFluxFromList(Arrays.asList("X", "Y", "Z")).subscribe(System.out::println);

        System.out.println("\n3️⃣ 生成范围数据:");
        createFluxFromRange(1, 5).subscribe(System.out::println);

        /**
         * 合并流
         */
        System.out.println("\n4️⃣ 并行合并 Flux:");
        //并行
        //先返回A、B再返回C、D
        mergeFlux(Flux.just("A", "B"), Flux.just("C", "D")).subscribe(System.out::println);

        //串行
        System.out.println("\n5️⃣ 串行合并 Flux:");
        concatFlux(Flux.just("44","1", "2"), Flux.just("3", "4")).subscribe(System.out::println);

        System.out.println("\n6️⃣ zip 组合 Flux:");
        /**
         * A1
         * B2
         * C3
         */
        zipFlux(Flux.just("A", "B", "C"), Flux.just(1, 2, 3)).subscribe(System.out::println);

        System.out.println("\n7️⃣ 过滤偶数:");
        filterEvenNumbers(Flux.range(1, 10)).subscribe(System.out::println);

        System.out.println("\n8️⃣ 转换为大写:");
        toUpperCase(Flux.just("hello", "world")).subscribe(System.out::println);

        System.out.println("\n9️⃣ flatMap 拆分:");
        flatMapExample(Flux.just("Hello", "World")).subscribe(System.out::println);

        System.out.println("\n🔟 监听 Flux:");
        logFlux(Flux.range(1, 5)).subscribe();

        System.out.println("\n1️⃣1️⃣ 只取前 3 个元素:");
        takeElements(Flux.range(1, 10), 3).subscribe(System.out::println);

        System.out.println("\n1️⃣2️⃣ 异步流（每秒发射一个元素）:");
        delayedFlux(Flux.just("A", "B", "C"), Duration.ofSeconds(1)).subscribe(System.out::println);
        Thread.sleep(4000); // 等待 4 秒，确保异步流完成

        System.out.println("\n1️⃣3️⃣ 默认值:");
        defaultIfEmptyExample(Flux.empty()).subscribe(System.out::println);

        System.out.println("\n1️⃣4️⃣ 切换 Flux:");
        switchIfEmptyExample(Flux.empty()).subscribe(System.out::println);
    }
}
