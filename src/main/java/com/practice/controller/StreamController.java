package com.practice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/stream")
public class StreamController {

    private final List<String> queue = new CopyOnWriteArrayList<>();

    public StreamController() {
        fillQueue(); // 初始化数据
    }

    private void fillQueue() {
        queue.clear(); // 清空旧数据
        IntStream.rangeClosed(1, 50).forEach(i -> queue.add("Message-" + i));
        queue.add("END_OF_STREAM"); // 结束标志
    }

    @GetMapping(value = "/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamMessages() {
        return Flux.<String>create(sink -> {  // 明确指定 String 类型
            int index = 0;
            while (index < queue.size()) {
                String message = queue.get(index);

                if ("END_OF_STREAM".equals(message)) {
                    sink.complete(); // 结束流
                    break;
                }

                sink.next(message); // 明确是 String
                index++;

                try {
                    Thread.sleep(20); // 模拟间隔 20ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        })
        .delayElements(Duration.ofMillis(20))  // 每 20 毫秒发送一次
        .doOnTerminate(this::fillQueue); // 结束后重置数据
    }
}
