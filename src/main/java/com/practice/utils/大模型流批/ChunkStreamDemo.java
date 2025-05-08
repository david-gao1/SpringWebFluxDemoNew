package com.practice.utils.大模型流批;

import com.theokanning.openai.completion.chat.ChatCompletionChunk;
import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.practice.utils.LLMUtils.getChatCompletionChunkFlowable;

public class ChunkStreamDemo {

    public static void main(String[] args) throws InterruptedException {
        new ChunkStreamDemo().run().subscribe(content -> {
            System.out.println("下游收到：" + content);
        });


    }

    public Flux<String> run() {
        Flowable<ChatCompletionChunk> flowable = getChatCompletionChunkFlowable();
        StringBuilder fullContent = new StringBuilder();
        PublishProcessor<String> processor = PublishProcessor.create();

        flowable
                .observeOn(Schedulers.io())
                .takeUntil((Predicate<? super ChatCompletionChunk>)
                        chunk -> "stop".equals(chunk.getChoices().get(0).getFinishReason()))
                .map(chunk -> {
                    String content = chunk.getChoices().get(0).getMessage().getReasoningContent();
                    if (content == null) {
                        content = chunk.getChoices().get(0).getMessage().getContent();
                    }
                    return content == null ? "" : content;
                })
                .filter(Objects::nonNull)
                .buffer(30, TimeUnit.MILLISECONDS) // ✅ 每 30ms 缓冲一批内容
                .filter(batch -> !batch.isEmpty())
                .doOnNext(batch -> {
                    System.out.println("本次批量大小: " + batch.size());
                    String merged = String.join("", batch);
                    fullContent.append(merged);
                    processor.onNext(merged); // ✅ 一批发给调用者
                })
                .doOnComplete(() -> {
                    processor.onComplete();
                    System.out.println("--- 全部内容已积累 ---");
                    System.out.println(fullContent.toString());
                    // 可选 Redis 写入
                })
                .doOnError(processor::onError)
                .subscribe();
        // 将 processor 转为 Flux，每个 chunk 发给下游
        return Flux.from(processor);
    }


}
